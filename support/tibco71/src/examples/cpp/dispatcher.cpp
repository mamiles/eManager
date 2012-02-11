
/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)dispatcher.cpp	1.10
 */


/*
 * dispatcher - demonstrates how to use multiple dispatcher threads.
 *
 * There are no parameters required to run this program.
 *
 * The program publishes total of ten messages, two messages every second.
 * The message callback imitates lengthly processing of the
 * message by making a delay for 1 second.
 * If only one dispatcher would dispatch events it would take
 * twice longer to process all messages.
 * Two dispatchers accomplish processing of all messages
 * in half the time because they process messages simultaneously
 * and independently.
 *
 * Try to change the program such that it only uses one dispatcher
 * and you will see it takes much longer to process all messages.
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <signal.h>
#include <string.h>
#include <time.h>

#include "tibrv/tibrvcpp.h"

char*  subject   = "dispatcher.test";   /* test subject */
time_t startTime = 0;                   /* the time we started */
TibrvQueue triggerQueue;                /* trigger queue */

/* We use TIB/RV queue to emulate sleep().
 */
TibrvQueue waitQueue;  

int processedMessageCount = 0;  /* count of processed messages */

#define TOTAL_MESSAGES     10   /* total number of messages, must be even */


/*********************************************************************/
/* Timer callback class                                              */
/*********************************************************************/
class TimerCallback : public TibrvTimerCallback
{
public:

    void onTimer(TibrvTimer* timer)
    {
        /* Trigger timer only needed for the dispatch()
         * call on the trigger queue to return.
         * We simply destroy the event so it won't
         * keep firing since it has 0 interval.
         * Nothing else should be done here.
         */
        timer->destroy();
    }

};

/*********************************************************************/
/* Message callback class                                            */
/*********************************************************************/
class MsgCallback : public TibrvMsgCallback
{
public:

    void onMsg(TibrvListener* listener, TibrvMsg& msg)
    {
        const char* msgString = NULL;

        // Convert the incoming message to a string
        msg.convertToString(msgString);

        // Report we are processing the message
        printf("Processing message %s\n",msgString);
        fflush(stdout);

        // Imitate delay of one second
        waitQueue.timedDispatch((tibrv_f64)1.0);

        /* Increase count of processed messages. In real world
         * access to this variable should be guarded
         * because we may be changing it from multiple
         * dispatcher threads at the same time.
         */
        processedMessageCount++;

        /* If we have processed all messages then report it
         * and create a timer on the trigger queue so the
         * dispatch call on that queue returns.
         */
        if (processedMessageCount == TOTAL_MESSAGES)
        {
            /* we don't need to keep the timer here */
            TibrvTimer* timer = new TibrvTimer();
            timer->create(&triggerQueue,
                          new TimerCallback(),
                          (tibrv_f64)0, /* no delay */
                          NULL);
        }
    }

};

/*********************************************************************/
/* main                                                              */
/*********************************************************************/
int main(int argc, char** argv)
{
    TibrvStatus status;
    int i, j;

    // open Tibrv
    status = Tibrv::open();
    if (status != TIBRV_OK)
    {
        fprintf(stderr,"Error: could not open TIB/RV, status=%d, text=%s\n",
            (int)status,status.getText());
        exit(-1);
    }

    // Get process transport
    TibrvTransport* transport = Tibrv::processTransport();

    // Create the queue
    TibrvQueue queue;
    queue.create();

    // Create trigger queue
    triggerQueue.create();

    // Create wait queue
    waitQueue.create();

    // Create listener
    TibrvListener listener;
    listener.create(&queue,new MsgCallback(),transport,subject,NULL);

    // Create message and set send subject in it.
    TibrvMsg msg;
    msg.setSendSubject(subject);

    // Create two dispatchers
    TibrvDispatcher dispatcher1, dispatcher2;
    dispatcher1.create(&queue);
    dispatcher2.create(&queue);

    // Get start time
    startTime = time(0);

    // We use this to track the message number
    int msgIndex = 0;

    // Report we are starting to publish messages
    printf("Started publishing messages at %d seconds\n\n",
                    (int)(time(0)-startTime));
    fflush(stdout);

    // Start publishing two messages at a time
    // every second, total of TOTAL_MESSAGES messages
    for (i=0; i<TOTAL_MESSAGES/2; i++)
    {
        // Publish 2 messages
        for (j=0; j<2; j++)
        {
            char str[32];
            msgIndex++;
            sprintf(str,"value-%d",msgIndex);
            msg.updateString("field",str);
            transport->send(msg);
        }

        /* Sleep for 1 second if we have not done publishing */
        if (i < TOTAL_MESSAGES/2-1)
        {
            waitQueue.timedDispatch((tibrv_f64)1.0);
        }
    }

    // Report we've published all messages
    printf("\nStopped publishing messages at %d seconds\n\n",
                    (int)(time(0)-startTime));
    fflush(stdout);

    // We should not quit main because that will
    // cause the program to quit before we
    // process all messages.
    // Wait until we process all messages and
    // post an event into the trigger queue which
    // will cause dispatch() to return.
    triggerQueue.dispatch();

    // Report we have processed all messages
    printf("\nProcessed all messages in %d seconds\n",(int)(time(0)-startTime));
    fflush(stdout);

    // Close Tibrv
    Tibrv::close();

    return 0;
}

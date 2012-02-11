
/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)dispatcher.c	1.15
 */


/*
 * dispatcher - demonstrates how to use multiple dispatcher threads.
 *
 * There are no parameters required to run this program.
 *
 * The program publishes total of ten messages, two messages every second.
 * The message callback imitates lengthy processing of the
 * message by making a delay for 1 second.
 * If only one dispatcher would dispatch events it would take
 * 10 seconds to process all messages.
 * Two dispatchers accomplish processing of all messages
 * in 5 seconds because they process messages simultaneously
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

#include "tibrv/tibrv.h"

char*  subject   = "dispatcher.test";   /* test subject */
time_t startTime = 0;                   /* the time we started */
tibrvQueue triggerQueue = TIBRV_INVALID_ID;  /* trigger queue */

/* We use TIB/RV queue to emulate sleep().
 */
tibrvQueue waitQueue = TIBRV_INVALID_ID;

int processedMessageCount = 0;  /* count of processed messages */

#define TOTAL_MESSAGES     10   /* total number of messages, must be even */



/*********************************************************************/
/* Timer callback                                                    */
/*********************************************************************/
void timerCallback(tibrvEvent event, tibrvMsg msg, void* closure)
{
    /* avoid warning "parameter not used" */
    msg = msg;
    closure = closure;

    /* Trigger timer only needed for the dispatch()
     * call on the trigger queue to return.
     * We simply destroy the event so it won't
     * keep firing since it has 0 interval.
     * Nothing else should be done here.
     */
    tibrvEvent_Destroy(event);
}


/*********************************************************************/
/* Message callback                                                  */
/*********************************************************************/
void msgCallback(tibrvEvent event, tibrvMsg msg, void* closure)
{
    const char* msgString = NULL;

    /* Convert the incoming message to a string */
    tibrvMsg_ConvertToString(msg,&msgString);

    /* Report we are processing the message */
    printf("Processing message %s\n",msgString);
    fflush(stdout);

    /* Imitate delay of one second */
    tibrvQueue_TimedDispatch(waitQueue,(tibrv_f64)1.0);

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
        tibrvEvent timer;  /* we don't need to keep it */
        tibrvEvent_CreateTimer(&timer,
                               triggerQueue,
                               (tibrvEventCallback)timerCallback,
                               (tibrv_f64)0, /* no delay */
                               NULL);
    }

}


/*********************************************************************/
/* main                                                              */
/*********************************************************************/
int main(int argc, char** argv)
{
    tibrv_status status;
    tibrvQueue queue;
    tibrvEvent listener;
    tibrvMsg msg;
    tibrvDispatcher dispatcher1, dispatcher2;
    int msgIndex;
    int i, j;

    /* Open Tibrv */
    status = tibrv_Open();
    if (status != TIBRV_OK)
    {
        fprintf(stderr,"Error: could not open TIB/RV, status=%d, text=%s\n",
            (int)status,tibrvStatus_GetText(status));
        exit(-1);
    }

    /* Create the queue */
    tibrvQueue_Create(&queue);

    /* Create trigger queue to wait on it */
    tibrvQueue_Create(&triggerQueue);

    /* Create wait queue */
    tibrvQueue_Create(&waitQueue);

    /* Create listener */
    tibrvEvent_CreateListener(&listener,
                              queue,
                              (tibrvEventCallback)msgCallback,
                              TIBRV_PROCESS_TRANSPORT,
                              subject,
                              NULL);

    /* Prepare the message we'll be sending and
     * set the send subject into it
     */
    tibrvMsg_Create(&msg);
    tibrvMsg_SetSendSubject(msg,subject);

    /* Create two dispatchers.
     * If you comment out creating second dispatcher
     * you'll see it takes twice as long this example
     * to finish.
     */
    tibrvDispatcher_Create(&dispatcher1,queue);
    tibrvDispatcher_Create(&dispatcher2,queue);

    /* Get the start time */
    startTime = time(0);

    /* We use this to track the message number */
    msgIndex = 0;

    /* Report we are starting to publish messages */
    printf("Started publishing messages at %d seconds\n\n",
           (int)(time(0)-startTime));
    fflush(stdout);

    /* Start publishing two messages at a time
     * every second, total of TOTAL_MESSAGES messages
     */
    for (i=0; i<TOTAL_MESSAGES/2; i++)
    {
        /* Publish 2 messages */
        for (j=0; j<2; j++)
        {
            char str[32];
            msgIndex++;
            sprintf(str,"value-%d",msgIndex);
            tibrvMsg_UpdateString(msg,"field",str);
            tibrvTransport_Send(TIBRV_PROCESS_TRANSPORT,msg);
        }

        /* Sleep for 1 second if we have not done publishing */
        if (i < TOTAL_MESSAGES/2-1)
        {
            tibrvQueue_TimedDispatch(waitQueue,(tibrv_f64)1.0);
        }
    }

    /* Report we've published all messages */
    printf("\nStopped publishing messages at %d seconds\n\n",
           (int)(time(0)-startTime));
    fflush(stdout);

    /* We should not quit main because that will
     * cause the program to quit before we
     * process all messages.
     * Wait until we process all messages and
     * post an event into the trigger queue which
     * will cause dispatch() to return.
     */
    tibrvQueue_Dispatch(triggerQueue);

    /* Report we have processed all messages */
    printf("\nProcessed all messages in %d seconds\n",
           (int)(time(0)-startTime));
    fflush(stdout);

    /* Close Tibrv */
    tibrv_Close();

    tibrvMsg_Destroy(msg);

    return 0;
}

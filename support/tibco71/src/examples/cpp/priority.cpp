
/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)priority.cpp	1.6
 */


/*
 * priority - demonstrating queues, groups and priorities.
 *
 * There are no parameters required to run this program.
 *
 * This sample creates two queues with different priorities, creates
 * a queue group containing those queues, then publishes messages
 * to two listeners. After messages have been published the program
 * dispatches the queue group until there are no events in the group.
 * Because queues have different priorities the callback will first
 * receive messages published into the queue with higher priority.
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <signal.h>
#include <string.h>
#include <time.h>

#include "tibrv/tibrvcpp.h"

char* subject1 = "1";
char* subject2 = "2";

/*********************************************************************/
/* Message callback class                                            */
/*********************************************************************/
class MsgCallback : public TibrvMsgCallback
{
public:

    void onMsg(TibrvListener* listener, TibrvMsg& msg)
    {
        const char* sendSubject  = NULL;
        const char* msgString    = NULL;

        // Get the subject name to which this message was sent
        msg.getSendSubject(sendSubject);

        // Convert the incoming message to a string
        msg.convertToString(msgString);

        printf("Received message on subject %s: %s\n",
                   sendSubject, msgString);

        fflush(stdout);
    }

};

/*********************************************************************/
/* main                                                              */
/*********************************************************************/
int main(int argc, char** argv)
{
    TibrvStatus status;
    int i;

    // open Tibrv
    status = Tibrv::open();
    if (status != TIBRV_OK)
    {
        fprintf(stderr,"Error: could not open TIB/RV, status=%d, text=%s\n",
            (int)status,status.getText());
        exit(-1);
    }

    // get process transport
    TibrvTransport* transport = Tibrv::processTransport();

    // create two queues
    TibrvQueue queue1;
    TibrvQueue queue2;
    queue1.create();
    queue2.create();

    // Set priorities
    queue1.setPriority(1);
    queue2.setPriority(2);

    // Create queue group and add queues
    TibrvQueueGroup group;
    group.create();

    group.add(&queue1);
    group.add(&queue2);

    // Create callback object
    MsgCallback* callback = new MsgCallback();

    // Create listeners
    TibrvListener listener1, listener2;
    listener1.create(&queue1,callback,transport,subject1,NULL);
    listener2.create(&queue2,callback,transport,subject2,NULL);

    TibrvMsg msg;

    // Send 10 messages on subject1
    msg.setSendSubject(subject1);
    for (i=0; i<10; i++)
    {
        char valstr[32];
        sprintf(valstr,"value-1-%d",(i+1));
        msg.updateString("field",valstr);
        transport->send(msg);
    }

    // Send 10 messages on subject2
    msg.setSendSubject(subject2);
    for (i=0; i<10; i++)
    {
        char valstr[32];
        sprintf(valstr,"value-2-%d",(i+1));
        msg.updateString("field",valstr);
        transport->send(msg);
    }

    // Dispatch the group. When all events are
    // dispatched timedDispatch() will return
    // TIBRV_TIMEOUT so we'll break out the while() loop.
    while (group.timedDispatch(1) == TIBRV_OK);

    // Close Tibrv
    Tibrv::close();

    return 0;
}

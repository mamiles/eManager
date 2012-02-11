
/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)priority.c	1.9
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

#include "tibrv/tibrv.h"

char* subject1 = "1";
char* subject2 = "2";

/*********************************************************************/
/* Message callback class                                            */
/*********************************************************************/
void msgCallback(tibrvEvent event, tibrvMsg msg, void* closure)
{
    const char* sendSubject  = NULL;
    const char* msgString    = NULL;

    /* Get the subject name to which this message was sent */
    tibrvMsg_GetSendSubject(msg,&sendSubject);

    /* Convert the incoming message to a string */
    tibrvMsg_ConvertToString(msg,&msgString);

    printf("Received message on subject %s: %s\n",
               sendSubject, msgString);

    fflush(stdout);
}

/*********************************************************************/
/* main                                                              */
/*********************************************************************/
int main(int argc, char** argv)
{
    tibrv_status status;
    tibrvQueue queue1, queue2;
    tibrvQueueGroup group;
    tibrvEvent listener1, listener2;
    tibrvMsg msg;
    char valstr[32];
    int i;

    /* Open Tibrv */
    status = tibrv_Open();
    if (status != TIBRV_OK)
    {
        fprintf(stderr,"Error: could not open TIB/RV, status=%d, text=%s\n",
            (int)status,tibrvStatus_GetText(status));
        exit(-1);
    }

    /* Create two queues */
    tibrvQueue_Create(&queue1);
    tibrvQueue_Create(&queue2);

    /* Set priorities */
    tibrvQueue_SetPriority(queue1,1);
    tibrvQueue_SetPriority(queue2,2);

    /* Create queue group and add queues */
    tibrvQueueGroup_Create(&group);
    tibrvQueueGroup_Add(group,queue1);
    tibrvQueueGroup_Add(group,queue2);

    /* Create listeners */
    tibrvEvent_CreateListener(&listener1,
                              queue1,
                              (tibrvEventCallback)msgCallback,
                              TIBRV_PROCESS_TRANSPORT,
                              subject1,
                              NULL);
    tibrvEvent_CreateListener(&listener2,
                              queue2,
                              (tibrvEventCallback)msgCallback,
                              TIBRV_PROCESS_TRANSPORT,
                              subject2,
                              NULL);

    /* Create tibrvMsg we'll be sending */
    tibrvMsg_Create(&msg);

    /* Send 10 messages on subject1 */
    tibrvMsg_SetSendSubject(msg,subject1);
    for (i=0; i<10; i++)
    {
        sprintf(valstr,"value-1-%d",(i+1));
        tibrvMsg_UpdateString(msg,"field",valstr);
        tibrvTransport_Send(TIBRV_PROCESS_TRANSPORT,msg);
    }

    /* Send 10 messages on subject1 */
    tibrvMsg_SetSendSubject(msg,subject2);
    for (i=0; i<10; i++)
    {
        sprintf(valstr,"value-2-%d",(i+1));
        tibrvMsg_UpdateString(msg,"field",valstr);
        tibrvTransport_Send(TIBRV_PROCESS_TRANSPORT,msg);
    }

    /* Dispatch the group. When all events are
     * dispatched timedDispatch() will return
     * TIBRV_TIMEOUT so we'll break out the while() loop.
     */
    while (tibrvQueueGroup_TimedDispatch(group,1) == TIBRV_OK);

    /* Close Tibrv */
    tibrv_Close();

    return 0;
}

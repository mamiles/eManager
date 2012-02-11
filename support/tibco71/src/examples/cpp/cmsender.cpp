
/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)cmsender.cpp	1.7
 */


/*
 * cmsender - sends certified messages on specified subject.
 *
 * This example publishes certified messages on the specified subject
 * and then quits.
 *
 * You can use this example with cmlistener or with cmqmember
 * examples to see Distributed Queue in action.
 *
 * There are no required parameters for this example.
 * Optional parameters are:
 *
 * -service   - RVD transport parameter
 * -network   - RVD transport parameter
 * -daemon    - RVD transport parameter
 * -cmname    - CM name used by CM transport
 * -subject   - subject this example sends messages on
 * -count     - how many messages to send
 *
 * If no transport parameters are specified, default values are used.
 * For information on default values for these parameters, please see
 * the TIBCO/Rendezvous Concepts manual.
 *
 * Default values for other parameters are:
 *  cmname      "cm.sender.cmname"
 *  subject     "cm.test.subject"
 *  count       10
 *
 * Notice that this example omits some error checking
 * in order to make it easier to understand.
 */

#include <stdlib.h>
#include <stdio.h>
#include <signal.h>
#include <string.h>
#include <time.h>

#include "tibrv/tibrvcpp.h"
#include "tibrv/cmcpp.h"

void parseParams(int argc, char** argv);

/*
 * RVD transport parameters
 */
char* service = NULL;
char* network = NULL;
char* rvdaemon  = NULL;

/*
 * The publish subject
 */
char* subject = (char*)"cm.test.subject";

/*
 * CM name
 */
char* cmname = (char*)"cm.sender.cmname";

/*
 * Count of messages to be sent
 */
int count = 10;

/*
 * Confirmation advisory subject
 */
char* confirmAdvisorySubject = (char*)"_RV.INFO.RVCM.DELIVERY.CONFIRM.>";

/*
 * Listener of CM advisory messages
 */
TibrvListener confirmListener;

/*
 * Last message we send.
 */
TibrvMsg* lastMsg = NULL;

/*
 * Sleep queue we use to emulate sleep() and to
 * synchronize at the end of main().
 */
TibrvQueue sleepQueue;


/*---------------------------------------------------------------
 * Advisory Messages Callback
 *
 * Callback receiving CM confirmation advisory messages.
 * The only purspose of this callback is to determine
 * when the last message has been confirmed.
 *-------------------------------------------------------------*/

class AdvCallback : public TibrvMsgCallback
{
public:

    void onMsg(TibrvListener* listener, TibrvMsg& msg)
    {
        tibrv_u64 seqno = (tibrv_u64)0;
        tibrv_u64 lastMsgSeqno = (tibrv_u64)0;

        // print confirmed sequence number
    msg.getU64("seqno",seqno);
        fprintf(stderr,"Confirmed message with seqno=%d\n",(int)seqno);

        // check if the last message has been confirmed
        // and if it was the last message, close Tibrv
    if (lastMsg != NULL)
    {
        TibrvCmMsg::getSequence(*lastMsg,lastMsgSeqno);
            if (lastMsgSeqno == seqno)
        Tibrv::close();
    }
    }
};


/*---------------------------------------------------------------
 * main()
 *-------------------------------------------------------------*/

int main(int argc, char** argv)
{
    TibrvStatus status;
    const char* str;

    // Parse parameters
    parseParams(argc,argv);

    if ((status=Tibrv::open()) != TIBRV_OK)
    {
       fprintf(stderr,"Failed to open Tibrv: %s\n",status.getText());
       exit(-1);
    }

    // Create sleep queue
    sleepQueue.create();

    TibrvNetTransport transport;
    TibrvCmTransport  cmTransport;

    // Create network transport
    status = transport.create(service,network,rvdaemon);
    if (status != TIBRV_OK)
    {
    fprintf(stderr,"Error creating network transport: %s\n",
                status.getText());
    exit(-1);
    }

    // Create CM transport
    status = cmTransport.create(&transport,cmname,TIBRV_TRUE);
    if (status != TIBRV_OK)
    {
    fprintf(stderr,"Error creating CM Queue transport: %s\n",
                status.getText());
    exit(-1);
    }

    // Create listener for delivery confirmation
    // advisory messages. Notice we must use same
    // transport we gave to CM transport.
    status = confirmListener.create(Tibrv::defaultQueue(),
               new AdvCallback(),
                           &transport,
                           confirmAdvisorySubject);
    if (status != TIBRV_OK)
    {
    fprintf(stderr,"Error creating listener of advisory messages: %s\n",
                status.getText());
    exit(-1);
    }

    // Dispatch default queue
    TibrvDispatcher disp;
    disp.create(Tibrv::defaultQueue());

    fprintf(stderr,"Publishing %d certified messages on subject %s\n",(int)count,subject);

    // Create the message
    TibrvMsg msg;

    msg.setSendSubject(subject);

    // Publish count messages.
    for (int i=1; i<=count; i++)
    {
        // Delay for 1 second
        sleepQueue.timedDispatch((tibrv_f64)1.0);

        // Distinguish sent messages
        msg.updateI32("index",(tibrv_i32)i);

        // Print messag we are about to publish
    msg.convertToString(str);
    fprintf(stderr,"Publishing message: %s\n",str);

        // Store if last message.
    // Although msg object was created on stack
    // we can keep it's address as long as we
    // don't exit from this function.
        if (i == count)
        lastMsg = &msg;

        // Send message
        cmTransport.send(msg);

        // If this was the last message, print its seqno
        if (i == count)
        {
        tibrv_u64 lastSeqno = (tibrv_u64)0;
        TibrvCmMsg::getSequence(msg,lastSeqno);
            fprintf(stderr,"Last sequence number to be confirmed = %d\n",(int)lastSeqno);
        }

    }

    // Wait until the last message has been confirmed.
    // Because no events are posted into sleepQueue
    // the dispatch() below will return only when Tibrv
    // is closed in the AdvCallback.
    sleepQueue.dispatch();

    return 0;
}


/*---------------------------------------------------------------
 * usage
 *-------------------------------------------------------------*/

void usage()
{
    fprintf(stderr,"Usage: cmsender [-service service] [-network network]\n");
    fprintf(stderr,"            [-daemon daemon] [-cmname cmname]\n");
    fprintf(stderr,"            [-subject subject] [-count NNN]\n");
    fprintf(stderr,"    default values are:\n");
    fprintf(stderr,"       service = %s\n",(service ? service:""));
    fprintf(stderr,"       network = %s\n",(network ? network:""));
    fprintf(stderr,"       daemon  = %s\n",(rvdaemon ? rvdaemon:""));
    fprintf(stderr,"       cmname  = %s\n",cmname);
    fprintf(stderr,"       subject = %s\n",subject);
    fprintf(stderr,"       count   = %d\n",count);
    exit(-1);
}


/*---------------------------------------------------------------
 * parseParams
 *-------------------------------------------------------------*/

void parseParams(int argc, char** argv)
{
    int i=1;
    while(i < argc)
    {
        if (!strcmp(argv[i],"-h") ||
            !strcmp(argv[i],"-help") ||
            !strcmp(argv[i],"?")) {
            usage();
        }
        else
        if (i == argc-1) // all parameters require values
        {
            usage();
        }
        if (!strcmp(argv[i],"-service"))
        {
            service = argv[i+1];
            i += 2;
        }
        else
        if (!strcmp(argv[i],"-network"))
        {
            network = argv[i+1];
            i += 2;
        }
        else
        if (!strcmp(argv[i],"-daemon"))
        {
            rvdaemon = argv[i+1];
            i += 2;
        }
        else
        if (!strcmp(argv[i],"-subject"))
        {
            subject = argv[i+1];
            i += 2;
        }
        else
        if (!strcmp(argv[i],"-cmname"))
        {
            cmname = argv[i+1];
            i += 2;
        }
        else
        if (!strcmp(argv[i],"-count"))
        {
            count = atoi(argv[i+1]);
            i += 2;
        }
        else
            usage();
    }

}


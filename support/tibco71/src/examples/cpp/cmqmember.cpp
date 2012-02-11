
/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)cmqmember.cpp	1.7
 */


/*
 * cmqmember - implements a member of Distributed Queue.
 *
 * To use this example you should start 3 or more instances
 * of this program and then run cmsender example.
 *
 * There are no required parameters for this example.
 * Optional parameters are:
 *
 * -service   - RVD transport parameter
 * -network   - RVD transport parameter
 * -daemon    - RVD transport parameter
 * -queue     - CM Queue name
 * -subject   - subject this example listens on
 *
 * If no transport parameters are specified, default values are used.
 * For information on default values for these parameters, please see
 * the TIBCO/Rendezvous Concepts manual.
 *
 * Default values for other parameters are:
 *  queue       "cm.queue"
 *  subject     "cm.test.subject"
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
 * Network transport parameters
 */
char* service = NULL;
char* network = NULL;
char* rvdaemon  = NULL;

/*
 * Queue name
 */
char* queueName = (char*)"cm.queue";

/*
 * Queue subject
 */
char* subject   = (char*)"cm.test.subject";

/*
 * Network transport
 */
TibrvNetTransport transport;

/*
 * CM Queue transport
 */
TibrvCmQueueTransport cmqTransport;

TibrvQueue queue;
TibrvCmListener cmListener;


/*---------------------------------------------------------------
 * CM Queue Member Callback
 *-------------------------------------------------------------*/

class CMQCallback : public TibrvCmMsgCallback
{
public:

    void onCmMsg(TibrvCmListener* listener, TibrvMsg& msg)
    {
        const char* str;
        tibrv_u64 seqno;
    TibrvStatus status;

    msg.convertToString(str);

    status = TibrvCmMsg::getSequence(msg,seqno);
    if (status == TIBRV_OK && seqno > (tibrv_u64)0)
    {
        fprintf(stderr,"Received message with seqno=%d: %s\n",
                (int)seqno,str);
    }
    else  // very first message has no sequence number
    {
        fprintf(stderr,"Received message: %s\n",str);
    }
    }
};


/*---------------------------------------------------------------
 * main()
 *-------------------------------------------------------------*/

int main(int argc, char** argv)
{
    TibrvStatus status;

    parseParams(argc,argv);

    // Open Tibrv framework
    Tibrv::open();

    // Create queue
    queue.create();

    // Create RVD transport and then CM transport
    status = transport.create(service,network,rvdaemon);
    if (status != TIBRV_OK)
    {
    fprintf(stderr,"Error creating network transport: %s\n",
                status.getText());
    exit(-1);
    }

    // Create CM Queue transport
    status = cmqTransport.create(&transport,queueName);
    if (status != TIBRV_OK)
    {
    fprintf(stderr,"Error creating CM Queue transport: %s\n",
                status.getText());
    exit(-1);
    }

    // Create listener for CM messages
    status = cmListener.create(&queue,
                   new CMQCallback(),
                   &cmqTransport,
                   subject);
    if (status != TIBRV_OK)
    {
    fprintf(stderr,"Error creating CM Listener: %s\n",
                status.getText());
    exit(-1);
    }

    // Report we have successfully started
    fprintf(stderr,"Queue name=%s, listening on subject %s\n",
            queueName,subject);

    // Dispatch events
    while(queue.dispatch() == TIBRV_OK);

    return 0;
}


/*---------------------------------------------------------------
 * usage
 *-------------------------------------------------------------*/

void usage()
{
    fprintf(stderr,"Usage: cmqmember [-service service] [-network network]\n");
    fprintf(stderr,"            [-daemon daemon] [-queue queue-name]\n");
    fprintf(stderr,"            [-subject subject]\n");
    fprintf(stderr,"    default values are:\n");
    fprintf(stderr,"       service = %s\n",(service ? service:""));
    fprintf(stderr,"       network = %s\n",(network ? network:""));
    fprintf(stderr,"       daemon  = %s\n",(rvdaemon ? rvdaemon:""));
    fprintf(stderr,"       queue   = %s\n",queueName);
    fprintf(stderr,"       subject = %s\n",subject);
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
        else  // all parameters require value
        if (i == argc-1)
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
        if (!strcmp(argv[i],"-queue"))
        {
            queueName = argv[i+1];
            i += 2;
        }
        else
            usage();
    }

}



/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)cmlistener.cpp	1.7
 */


/*
 * cmlistener - listens for certified messages and confirms them
 *
 * There are no required parameters for this example.
 * Optional parameters are:
 *
 * -service   - RVD transport parameter
 * -network   - RVD transport parameter
 * -daemon    - RVD transport parameter
 * -cmname    - CM name used by CM transport
 * -subject   - subject this example listens on
 *
 * If no transport parameters are specified, default values are used.
 * For information on default values for these parameters, please see
 * the TIBCO/Rendezvous Concepts manual.
 *
 * Default values for other parameters are:
 *  cmname      "cm.listener.cmname"
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
 * Subject we listen on
 */
char* subject = (char*)"cm.test.subject";

/*
 * CM name
 */
char* cmname  = (char*)"cm.listener.cmname";

/*
 * Network transport
 */
TibrvNetTransport transport;

/*
 * CM transport
 */
TibrvCmTransport  cmTransport;

TibrvQueue        queue;
TibrvCmListener   cmListener;

/*---------------------------------------------------------------
 * CM Callback
 *-------------------------------------------------------------*/

class CMCallback : public TibrvCmMsgCallback
{
public:

    void onCmMsg(TibrvCmListener* listener, TibrvMsg& msg)
    {
        const char* str;
    TibrvStatus status;
        tibrv_u64 seqno = (tibrv_u64)0;

    // Print message information
    msg.convertToString(str);
    fprintf(stderr,"Received message: %s\n",str);

    // Get message sequence number and if it is valid
    // then confirm the message
        status = TibrvCmMsg::getSequence(msg,seqno);
    if (status == TIBRV_OK && seqno > (tibrv_u64)0)
    {
            fprintf(stderr,"Confirming message with seqno=%d\n",(int)seqno);

        status = cmListener.confirmMsg(msg);
        if (status != TIBRV_OK)
        fprintf(stderr,"Error occurred confirming CM message: %s\n",
                status.getText());
        }
    }
};


/*---------------------------------------------------------------
 * main()
 *-------------------------------------------------------------*/

int main(int argc, char** argv)
{
    TibrvStatus status;

    // Parse parameters
    parseParams(argc,argv);

    // Open Tibrv framework
    Tibrv::open();

    // Create queue
    queue.create();

    // Create network transport
    status = transport.create(service,network,rvdaemon);
    if (status != TIBRV_OK)
    {
    fprintf(stderr,"Error creating network transport: %s\n",
                status.getText());
    exit(-1);
    }

    status = cmTransport.create(&transport,cmname,TIBRV_TRUE);
    if (status != TIBRV_OK)
    {
    fprintf(stderr,"Error creating CM transport: %s\n",
                status.getText());
    exit(-1);
    }

    // Create listener
    status = cmListener.create(&queue,
                   new CMCallback(),
                   &cmTransport,
                   subject);
    if (status != TIBRV_OK)
    {
    fprintf(stderr,"Error creating CM Listener: %s\n",
                status.getText());
    exit(-1);
    }

    // Set explicit confirmation before we start dispatching events
    status = cmListener.setExplicitConfirm();
    if (status != TIBRV_OK)
    {
        fprintf(stderr,"Error occurred setting explicit confirmation: %s\n",
                status.getText());
    exit(-1);
    }


    // Report we have successfully started
    fprintf(stderr,"Listening on subject: %s\n",subject);

    // Dispatch events
    while(queue.dispatch() == TIBRV_OK);

    return 0;
}


/*---------------------------------------------------------------
 * usage
 *-------------------------------------------------------------*/

void usage()
{
    fprintf(stderr,"Usage: cmlistener [-service service] [-network network]\n");
    fprintf(stderr,"            [-daemon daemon] [-cmname cmname]\n");
    fprintf(stderr,"            [-subject subject]\n");
    fprintf(stderr,"    default values are:\n");
    fprintf(stderr,"       service = %s\n",(service ? service:""));
    fprintf(stderr,"       network = %s\n",(network ? network:""));
    fprintf(stderr,"       daemon  = %s\n",(rvdaemon ? rvdaemon:""));
    fprintf(stderr,"       cmname  = %s\n",cmname);
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
        else
        if (i == argc-1) // all parmeters require values
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
            usage();
    }
}


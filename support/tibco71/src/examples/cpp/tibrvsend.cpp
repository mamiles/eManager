/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvsend.cpp	2.8
 */


/*
 * tibrvsend - sample TIB/Rendezvous message publisher
 *
 * This program publishes one or more string messages on a specified
 * subject.  Both the subject and the message(s) must be supplied as
 * command parameters.  Message(s) with embedded spaces should be quoted.
 * A field named "DATA" will be created to hold the string in each
 * message.
 *
 * Optionally the user may specify communication parameters for
 * tibrvTransport_Create.  If none are specified, default values
 * are used.  For information on default values for these parameters,
 * please see the TIBCO/Rendezvous Concepts manual.
 *
 * Normally a listener such as tibrvlisten should be started first.
 *
 * Examples:
 *
 *  Publish two messages on subject a.b.c and default parameters:
 *   tibrvsend a.b.c "This is my first message" "This is my second message"
 *
 *  Publish a message on subject a.b.c using port 7566:
 *   tibrvsend -service 7566 a.b.c message
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "tibrv/tibrvcpp.h"

#define MIN_PARAMS  (3)
#define FIELD_NAME  "DATA"

/*********************************************************************/
/* Transport parameters                                              */
/*********************************************************************/

char* serviceStr = NULL;
char* networkStr = NULL;
char* daemonStr  = NULL;

/*********************************************************************/
/* usage:                                                            */
/*         print usage information and quit                          */
/*********************************************************************/
void usage()
{
    fprintf(stderr,"tibrvsend [-service service] [-network network] \n");
    fprintf(stderr,"          [-daemon daemon] <subject> <messages>\n");
    exit(1);
}

/*********************************************************************/
/* getTransportParams:                                               */
/*         Get from the command line the parameters that should be   */
/*         passed to TibrvNetTransport.create(). Returns the index   */
/*         for where any additional parameters can be found.         */
/*********************************************************************/
int
getTransportParams(int argc, char* argv[])
{
    int i=1;  // skip program name

    while (i+2 <= argc && *argv[i] == '-')
    {
        if (strcmp(argv[i], "-service") == 0)
        {
            serviceStr = argv[i+1];
            i+=2;
        }
        else
        if (strcmp(argv[i], "-network") == 0)
        {
            networkStr = argv[i+1];
            i+=2;
        }
        else
        if (strcmp(argv[i], "-daemon") == 0)
        {
            daemonStr = argv[i+1];
            i+=2;
        }
        else
            usage();
    }

    return i;
}

/*********************************************************************/
/* main                                                              */
/*********************************************************************/
int main(int argc, char **argv)
{
    TibrvStatus status;

    if (argc < MIN_PARAMS)
        usage();

    // Parse arguments for possible optional parameter pairs.
    // These must precede the subject and message strings.
    int i = getTransportParams(argc,argv);

    // We must have at least one subject and one message
    if (i >= argc-1)
        usage();

    // Open Tibrv
    status = Tibrv::open();
    if (status != TIBRV_OK)
    {
        fprintf(stderr,"Error: could not open TIB/RV, status=%d, text=%s\n",
            (int)status,status.getText());
        exit(-1);
    }

    // Initialize the transport with the given parameters or default NULLs.
    TibrvNetTransport transport;
    status = transport.create(serviceStr,networkStr,daemonStr);
    if (status != TIBRV_OK)
    {
        fprintf(stderr,"Error: could not create transport, status=%d, text=%s\n",
            (int)status,status.getText());
        Tibrv::close();
        exit(-1);
    }
    transport.setDescription(argv[0]);

    // Create message
    TibrvMsg msg;

    // Set subject into message
    const char* subject = argv[i];

    status = msg.setSendSubject(subject);
    if (status != TIBRV_OK)
    {
        // likely wrong subject specified
        fprintf(stderr,"Error: could not set subject %s into message, status=%d, text=%s\n",
            argv[i],(int)status,status.getText());
        Tibrv::close();
        exit(-1);
    }

    // Step to next parameter.
    // Remaining parameters are messages to be sent.
    i++;

    // Step through command line publishing each message
    while(i < argc)
    {
        printf("Publishing: subject=%s \"%s\"\n",
                subject, argv[i]);

        // Update the string in our message
        status = msg.updateString(FIELD_NAME,argv[i]);
        if (status == TIBRV_OK)
            status = transport.send(msg);

        if (status != TIBRV_OK)
        {
            fprintf(stderr,"Error sending message, status=%d, text=%s\n",
                (int)status,status.getText());
            break;
        }

        i++;
    }

    // Tibrv::close() will destroy the transport and guarantee delivery
    Tibrv::close();

    exit(0);

    return 0; // keep compiler happy
}

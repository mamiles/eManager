/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvsend.c	1.11
 */


/*
 * tibrvsend - sample Rendezvous message publisher
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

#include "tibrv/tibrv.h"

#define MIN_PARMS       (3)
#define FIELD_NAME      "DATA"

void
usage(void)
{
    fprintf(stderr,"tibrvsend   [-service service] [-network network] \n");
    fprintf(stderr,"            [-daemon daemon] <subject> <messages>\n");
    exit(1);
}

/*********************************************************************/
/* get_InitParms:  Get from the command line the parameters that can */
/*                 be passed to tibrvTransport_Create().             */
/*                                                                   */
/*                 returns the index for where any additional        */
/*                 parameters can be found.                          */
/*********************************************************************/
int
get_InitParms(
    int         argc,
    char*       argv[],
    int         min_parms,
    char**      serviceStr,
    char**      networkStr,
    char**      daemonStr)
{
    int i=1;

    if ( argc < min_parms )
        usage();

    while ( i+2 <= argc && *argv[i] == '-' )
    {
        if (strcmp(argv[i], "-service") == 0)
        {
            *serviceStr = argv[i+1];
            i+=2;
        }
        else if (strcmp(argv[i], "-network") == 0)
        {
            *networkStr = argv[i+1];
            i+=2;
        }
        else if (strcmp(argv[i], "-daemon") == 0)
        {
            *daemonStr = argv[i+1];
            i+=2;
        }
        else
        {
            usage();
        }
    }

    return( i );
}

int
main(int argc, char **argv)
{
    tibrv_status        err;
    tibrvTransport      transport;
    tibrvMsg            message;

    int                 currentArg;
    int                 subjectLocation;

    char*               serviceStr = NULL;
    char*               networkStr = NULL;
    char*               daemonStr  = NULL;

    char*               progname = argv[0];

    /*
     * Parse arguments for possible optional parameter pairs.
     * These must precede the subject and message strings.
     */
    currentArg = get_InitParms(argc, argv, MIN_PARMS, &serviceStr,
                               &networkStr, &daemonStr );

    /*
     * Create internal TIB/Rendezvous machinery
     */
    err = tibrv_Open();
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to open TIB/RV: %s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }


    /*
     * Initialize the transport with the given parameters or default NULLs.
     */

    err = tibrvTransport_Create(&transport, serviceStr,
                                networkStr, daemonStr);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to initialize transport: %s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }

    tibrvTransport_SetDescription(transport, progname);

    /*
     * Create message
     */
    err = tibrvMsg_Create(&message);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to create message: %s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }


    /*
     * Step through command line, getting first the subject,
     * then publishing each message on that subject.
     */

    subjectLocation = currentArg++;

    while(currentArg < argc)
    {
        printf("Publishing: subject=%s \"%s\"\n",
                argv[subjectLocation], argv[currentArg]);

        /* Update the string in our message */
        err = tibrvMsg_UpdateString(message, FIELD_NAME, argv[currentArg]);

        if (err == TIBRV_OK)
        {
            /* Set the subject name */
            err = tibrvMsg_SetSendSubject(message, argv[subjectLocation]);

            if (err == TIBRV_OK)
            {
                err = tibrvTransport_Send(transport, message);
            }
        }

        if ( err != TIBRV_OK)
        {
            fprintf(stderr, "%s: %s in sending \"%s\" to \"%s\"\n",
                    progname, tibrvStatus_GetText(err),
                    argv[currentArg], argv[subjectLocation]);
            break;
        }

        currentArg++;
    }

    /*
     * Destroy message
     */
    tibrvMsg_Destroy(message);

    /*
     * tibrv_Close() will destroy the transport and guarantee delivery.
     */

    tibrv_Close();

    exit(0);
}

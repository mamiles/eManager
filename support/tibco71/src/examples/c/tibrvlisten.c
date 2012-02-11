/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvlisten.c	1.21
 */


/*
 * tibrvlisten - generic Rendezvous subscriber
 *
 * This program listens for any number of messages on a specified
 * set of subject(s).  Message(s) received are printed.
 *
 * Some platforms require proper quoting of the arguments to prevent
 * the command line processor from modifying the command arguments.
 *
 * The user may terminate the program by typing Control-C.
 *
 * Optionally the user may specify communication parameters for
 * tibrvTransport_Create.  If none are specified, default values
 * are used.  For information on default values for these parameters,
 * please see the TIBCO/Rendezvous Concepts manual.
 *
 * Examples:
 *
 * Listen to every message published on subject a.b.c:
 *  tibrvlisten a.b.c
 *
 * Listen to every message published on subjects a.b.c and x.*.Z:
 *  tibrvlisten a.b.c "x.*.Z"
 *
 * Listen to every system advisory message:
 *  tibrvlisten "_RV.*.SYSTEM.>"
 *
 * Listen to messages published on subject a.b.c using port 7566:
 *  tibrvlisten -service 7566 a.b.c
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <signal.h>
#include <string.h>
#include <time.h>

#include "tibrv/tibrv.h"

#define MIN_PARMS (2)

void
my_callback(
    tibrvEvent          event,
    tibrvMsg            message,
    void*               closure)
{
    const char*         send_subject  = NULL;
    const char*         reply_subject = NULL;
    const char*         theString     = NULL;

    /*
     * Get the subject name to which this message was sent.
     */
    tibrvMsg_GetSendSubject(message, &send_subject);

    /*
     * If there was a reply subject, get it.
     */
    tibrvMsg_GetReplySubject(message, &reply_subject);

    /*
     * Convert the incoming message to a string.
     */
    tibrvMsg_ConvertToString(message, &theString);

    if (reply_subject)
        printf("subject=%s, reply=%s, message=%s\n",
               send_subject, reply_subject, theString);
    else
        printf("subject=%s, message=%s\n",
               send_subject, theString);

    fflush(stdout);
}

void
usage(void)
{
    fprintf(stderr,"tibrvlisten [-service service] [-network network] \n");
    fprintf(stderr,"            [-daemon daemon] subject_list\n");
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
    int i = 1;

    if ( argc < min_parms )
        usage();

    while ( i+2 <= argc && *argv[i] == '-' )
    {
        if(strcmp(argv[i], "-service") == 0)
        {
            *serviceStr = argv[i+1];
            i+=2;
        }
        else if(strcmp(argv[i], "-network") == 0)
        {
            *networkStr = argv[i+1];
            i+=2;
        }
        else if(strcmp(argv[i], "-daemon") == 0)
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
main(int argc, char** argv)
{
    tibrv_status        err;
    int                 currentArg;
    tibrvEvent          listenId;
    tibrvTransport      transport;

    char*               serviceStr = NULL;
    char*               networkStr = NULL;
    char*               daemonStr  = NULL;

    char*               progname = argv[0];

    /*
     * Parse the arguments for possible optional parameter pairs.
     * These must precede the subject and message strings.
     */

    currentArg = get_InitParms( argc, argv, MIN_PARMS,
                                &serviceStr, &networkStr, &daemonStr );

    /* Create internal TIB/Rendezvous machinery */
    err = tibrv_Open();

    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to open TIB/Rendezvous: %s\n",
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
     * Listen to each subject.
     */

    while (currentArg < argc)
    {

        printf("tibrvlisten: Listening to subject %s\n", argv[currentArg]);

        err = tibrvEvent_CreateListener(&listenId, TIBRV_DEFAULT_QUEUE,
                                        my_callback, transport,
                                        argv[currentArg], NULL);

        if (err != TIBRV_OK)
        {
            fprintf(stderr, "%s: Error %s listening to \"%s\"\n",
                    progname, tibrvStatus_GetText(err), argv[currentArg]);
            exit(2);
        }

        currentArg++;
    }

    /*
     * Dispatch loop - dispatches events which have been placed on the event queue
     */


    while (tibrvQueue_Dispatch(TIBRV_DEFAULT_QUEUE) == TIBRV_OK);

    /*
     * Shouldn't get here.....
     */

    tibrv_Close();

    return 0;
}

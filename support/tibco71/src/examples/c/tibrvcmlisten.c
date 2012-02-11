/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvcmlisten.c	1.15
 */


/*
 * tibrvcmlisten - generic CM Rendezvous subscriber
 *
 * This program listens for any number of certified messages on a specified
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
 * In addition, the cmname may be specified.
 *
 * Examples:
 *
 * Listen to every message published on subject a.b.c:
 *  tibrvcmlisten a.b.c
 *
 * Listen to every message published on subjects a.b.c and x.*.Z:
 *  tibrvcmlisten a.b.c "x.*.Z"
 *
 * Listen to messages published on subject a.b.c using port 7566:
 *  tibrvcmlisten -service 7566 a.b.c
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <signal.h>
#include <string.h>
#include <time.h>

#include "tibrv/tibrv.h"
#include "tibrv/cm.h"

#define MIN_PARMS (2)

char*   progname;

void
my_callback(
    tibrvcmEvent        event,
    tibrvMsg            message,
    void*               closure)
{
    tibrv_status        err;

    const char*         send_subject  = NULL;
    const char*         reply_subject = NULL;
    const char*         theString     = NULL;
    const char*         cmname        = NULL;

    tibrv_u64           sequenceNumber;

    tibrv_bool          certified = TIBRV_FALSE;
    tibrv_bool          listener_registered = TIBRV_FALSE;

    /*
     * Get the subject name to which this message was sent.
     */
    err = tibrvMsg_GetSendSubject(message, &send_subject);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to get send subject --%s\n",
                progname, tibrvStatus_GetText(err));
    }
    else
    {
        /*
         * If there was a reply subject, get it.
         */
        err = tibrvMsg_GetReplySubject(message, &reply_subject);

        if (err != TIBRV_OK)
        {
            if (err == TIBRV_NOT_FOUND)
            {
                /* this is okay */
                err = TIBRV_OK;
            }
            else
            {
                fprintf(stderr, "%s: Failed to get reply subject --%s\n",
                    progname, tibrvStatus_GetText(err));
            }
        }
    }


    if (err == TIBRV_OK)
    {
        /*
         * Get the correspondent name of the cm sender.
         */
        err = tibrvMsg_GetCMSender(message, &cmname);

        if (err != TIBRV_OK)
        {
            if (err == TIBRV_NOT_FOUND)
            {
                /* must be reliable protocol */
                err = TIBRV_OK;
            }
            else
            {
                fprintf(stderr, "%s: Error getting CM sender--%s\n",
                    progname, tibrvStatus_GetText(err));
            }
        }
        else
        {
            /* Must be using certified delivery protocol */
            certified = TIBRV_TRUE;
            err = tibrvMsg_GetCMSequence(message, &sequenceNumber);

            if (err == TIBRV_OK)
            {
                /* listener is registered for certified delivery */
                listener_registered = TIBRV_TRUE;
            }
            else
            {
                if (err == TIBRV_NOT_FOUND)
                {
                    /* listener isn't registered for certified delivery */
                    err = TIBRV_OK;
                }
                else
                {
                    fprintf(stderr, "%s: Error getting CM sequence--%s\n",
                            progname, tibrvStatus_GetText(err));
                }
            }
        }
    }


    if (err == TIBRV_OK)
    {
        /*
         * Convert the incoming message to a string.
         */
        err = tibrvMsg_ConvertToString(message, &theString);
    }

    if (err == TIBRV_OK)
    {
        if (listener_registered)
        {
            printf("subject=%s, reply=%s, message=%s, ",
           send_subject, ((reply_subject) ? reply_subject: "none"),
           theString);

        printf("certified sender=%s, sequence=%d\n",
           ((certified) ? "TRUE":"FALSE"),
           (tibrv_u32)sequenceNumber);
        }
        else
        {
            printf("subject=%s, reply=%s, message=%s, ",
           send_subject, ((reply_subject) ? reply_subject: "none"),
           theString);

            printf("certified sender=%s, receipt uncertified\n",
           ((certified) ? "TRUE":"FALSE"));


        }
    }

    fflush(stdout);
}

void
usage(void)
{
    fprintf(stderr,"tibrvcmlisten [-service service] [-network network] \n");
    fprintf(stderr,"              [-daemon daemon] [-ledger <filename>]\n");
    fprintf(stderr,"              [-cmname cmname] subject_list\n");
    exit(1);
}

/*********************************************************************/
/* get_InitParms:  Get from the command line the parameters that can */
/*                 be passed to tibrvTransport_Create() and          */
/*                 tibrvcmTransport_Create.                          */
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
    char**      daemonStr,
    char**      ledgerStr,
    char**      cmnameStr)
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
        else if(strcmp(argv[i], "-ledger") == 0)
        {
            *ledgerStr = argv[i+1];
            i+=2;
        }
        else if(strcmp(argv[i], "-cmname") == 0)
        {
            *cmnameStr = argv[i+1];
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
    tibrvcmEvent        cmlistenId;
    tibrvTransport      transport;
    tibrvcmTransport    cmtransport;

    char*               serviceStr = NULL;
    char*               networkStr = NULL;
    char*               daemonStr  = NULL;
    char*               ledgerStr  = NULL;
    char*               cmnameStr  = "RVCMSUB";


    progname = argv[0];

    /*
     * Parse the arguments for possible optional parameter pairs.
     * These must precede the subject and message strings.
     */

    currentArg = get_InitParms( argc, argv, MIN_PARMS,
                                &serviceStr, &networkStr, &daemonStr,
                                &ledgerStr, &cmnameStr );

    /* Create internal TIB/Rendezvous machinery */
    err = tibrv_Open();

    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to open TIB/Rendezvous --%s\n",
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
        fprintf(stderr, "%s: Failed to initialize transport --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }

    /*
     * Initialize the CM transport with the given parameters or default NULLs.
     */

    err = tibrvcmTransport_Create(&cmtransport, transport, cmnameStr,
                                  TIBRV_TRUE, ledgerStr, TIBRV_FALSE, NULL);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to initialize CM transport --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }

    tibrvTransport_SetDescription(transport, progname);

    /*
     * Listen to each subject.
     */

    while (currentArg < argc)
    {

        printf("tibrvcmlisten: Listening to subject %s\n", argv[currentArg]);

        err = tibrvcmEvent_CreateListener(&cmlistenId, TIBRV_DEFAULT_QUEUE,
                                        my_callback, cmtransport,
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
     * Dispatch loop - dispatches events which have been placed on the event
     * queue
     */

    while (tibrvQueue_Dispatch(TIBRV_DEFAULT_QUEUE) == TIBRV_OK);

    /*
     * Shouldn't get here.....
     */

    tibrv_Close();

    return 0;
}

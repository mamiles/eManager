/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvinitval.c	1.14
 */

/*
 * tibrvinitval - TIB/RV Initial Value Example
 *
 * This program illustratates how to get initial values out of rvcache.
 *
 * One or more subject names are specified on the command line.  The
 * program then sets up a listener for each of these subjects.  Next,
 * it sets up a listener for the replies it receives from rvcache.  And
 * lastly, it sends a _SNAP message to rvcache to see if any initial
 * values currently exist for this subject.
 *
 * When the listener which was created first (listening on the command
 * line subject) receives a callback, this program sends another
 * _SNAP message to see what initial value has been stored in the rvcache.
 * In this way, this program continues to monitor what is received by
 * the rvcache.
 *
 * Optionally the user may specify communication parameters for
 * tibrvTransport_Create.  If none are specified, default values
 * are used.  For information on default values for these parameters,
 * please see the TIBCO/Rendezvous Concepts manual.
 *
 *
 * Examples:
 *
 *  Get initial value for one subject with default parameters:
 *   tibrvinitval a.b.c
 *
 *  Get initial value for two subjects using port 7566:
 *   tibrvinitval -service 7566 a.b.c x.y.z
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "tibrv/tibrv.h"

tibrvTransport  transport;
tibrvMsg        msg;
char*           progname;

void
usage(void)
{
    fprintf(stderr,"tibrvinitval [-service service] [-network network] \n");
    fprintf(stderr,"             [-daemon daemon] <subject> [<subject>]*\n");
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
get_InitParms( int argc,
               char *argv[],
               int min_parms,
               char **serviceStr,
               char **networkStr,
               char **daemonStr)
{
    int i=1;

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

/*********************************************************************/
/* create_replyname:  Formats the reply_name for the _SNAP message.  */
/*                                                                   */
/*                    returns void                                   */
/*********************************************************************/
void
create_replyname(
    char *reply_name,
    char *subject)
{
        /* Create reply name for the snap message */
        sprintf(reply_name, "TIBRVIVALUE.%s", subject);
}

/*********************************************************************/
/* send_snap:  Sets up reply name corresponding to the callback      */
/*             for this subject. Sets up the _SNAP send name.  And   */
/*             then sends the (empty) message.                       */
/*                                                                   */
/*             returns tibrv_status if any tibrv errors occur        */
/*********************************************************************/
tibrv_status
send_snap(char *subject)
{
    tibrv_status        err = TIBRV_OK;
    char                snap_name[TIBRV_SUBJECT_MAX];
    char                reply_name[TIBRV_SUBJECT_MAX];

    /* Reuse our message */
    tibrvMsg_Reset(msg);

    /* Create the _SNAP name */
    sprintf(snap_name, "_SNAP.%s", subject);

    err = tibrvMsg_SetSendSubject(msg, snap_name);

    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to set send name: %s --%s\n",
                    progname, snap_name, tibrvStatus_GetText(err));
    }
    else
    {
        /* Get our replyname */
        create_replyname(reply_name, subject);

        err = tibrvMsg_SetReplySubject(msg, reply_name);

        if (err != TIBRV_OK)
        {
            fprintf(stderr, "%s: Failed to set reply name: %s --%s\n",
                    progname, reply_name, tibrvStatus_GetText(err));
        }
    }


    if (err == TIBRV_OK)
    {
        /* Send the _SNAP message */
        err = tibrvTransport_Send(transport, msg);
        if (err != TIBRV_OK)
        {
            fprintf(stderr, "%s: Failed to send message --%s\n",
                    progname, tibrvStatus_GetText(err));
        }
    }

    return(err);
}

/*********************************************************************/
/* listen_callback:  Listens on the subject specified on the command */
/*                   line.  When we hear something, we then send off */
/*                   a request for the new initial value.            */
/*                                                                   */
/*                   returns void                                    */
/*********************************************************************/

void listen_callback (
    tibrvEvent          event,
    tibrvMsg            message,
    void*               closure)
{
    tibrv_status err = TIBRV_OK;
    const char* send_subject;

    err = tibrvMsg_GetSendSubject(message, &send_subject);

    if (err != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: Failed to get send subject in listen callback --%s\n",
                progname, tibrvStatus_GetText(err));
    }
    else
    {
        /* Call routine to send snap message */
        send_snap((char *)send_subject);
    }
}

/*********************************************************************/
/* snap_callback:  This routine gets invoked when we receive a       */
/*                 message from rvcache.  This message contains the  */
/*                 initial value.  We display the message.           */
/*                                                                   */
/*                 returns void                                      */
/*********************************************************************/
void snap_callback (
    tibrvEvent          event,
    tibrvMsg            message,
    void*               closure)
{
    const char*         send_subject  = NULL;
    const char*         theString     = NULL;

    /*
     * Get the subject name to which this message was sent.
     */
    tibrvMsg_GetSendSubject(message, &send_subject);

    /*
     * Convert the incoming message to a string.
     */
    tibrvMsg_ConvertToString(message, &theString);

    printf("subject=%s, message=%s\n", send_subject, theString);

    fflush(stdout);
}


int
main(int argc, char **argv)
{
    int                 currentArg;
    tibrv_status        err = TIBRV_OK;
    tibrvEvent          snapId = TIBRV_INVALID_ID;
    tibrvEvent          listenId = TIBRV_INVALID_ID;
    char                reply_name[TIBRV_SUBJECT_MAX];

    char                *serviceStr=NULL;
    char                *networkStr=NULL;
    char                *daemonStr=NULL;

    progname = argv[0];

    /*
     * Parse arguments for possible optional parameter pairs.
     * These must precede the subject(s).
     */
    currentArg = get_InitParms( argc, argv, 2,
                                &serviceStr, &networkStr, &daemonStr );

    /*
     * Create internal TIB/Rendezvous machinery
     */

    err = tibrv_Open();
    if(err != TIBRV_OK)
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

    tibrvTransport_SetDescription(transport, progname);

    /* Create the message we'll use many times */
    err = tibrvMsg_Create(&msg);

    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to create message --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }


    /*
     * Step through command line, getting subjects
     */

    while (currentArg < argc)
    {
        printf("Publishing: subject=\"%s\"\n", argv[currentArg]);

        /*
         * Create listener for the same subject name that
         * rvcache is listening upon.
         */
        err = tibrvEvent_CreateListener(&listenId,
                                        TIBRV_DEFAULT_QUEUE,
                                        listen_callback,
                                        transport,
                                        argv[currentArg],
                                        NULL);

        if (err != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s: Failed to create listener for subject: %s --%s\n",
                    progname, argv[currentArg], tibrvStatus_GetText(err));
            exit(1);
        }

        create_replyname(reply_name, argv[currentArg]);

        /* Create the listener for snapped message */
        err = tibrvEvent_CreateListener(&snapId,
                                        TIBRV_DEFAULT_QUEUE,
                                        snap_callback,
                                        transport,
                                        reply_name,
                                        NULL);

        if (err != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s: Failed to create listener for snap reply: %s --%s\n",
                    progname, reply_name, tibrvStatus_GetText(err));
            exit(1);
        }

        /* Call routine to send snap message */
        err = send_snap(argv[currentArg]);


        if (err != TIBRV_OK)
        {
            fprintf(stderr, "%s: %s in sending \"%s\"\n",
                    progname, tibrvStatus_GetText(err), argv[currentArg]);
            exit(1);
        }

        currentArg++;
    }

    /*
     * Dispatch loop - dispatches events which have been
     * placed on the event queue
     */

    while (tibrvQueue_Dispatch(TIBRV_DEFAULT_QUEUE) == TIBRV_OK);

    /*
     * Shouldn't get here.....
     */

    tibrvMsg_Destroy(msg);
    tibrvEvent_Destroy(listenId);
    tibrvEvent_Destroy(snapId);

    tibrv_Close();

    return 0;

}

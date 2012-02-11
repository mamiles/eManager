/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvcmsend.c	1.14
 */


/*
 * tibrvcmsend - sample CM Rendezvous message publisher
 *
 * This program publishes a certified string message on a specified
 * subject.  Both the subject and the message must be supplied as
 * command parameters.  A message with embedded spaces should be quoted.
 * A field named "DATA" will be created to hold the string in the
 * message.
 *
 * Optionally the user may specify communication parameters for
 * tibrvTransport_Create.  If none are specified, default values
 * are used.  For information on default values for these parameters,
 * please see the TIBCO/Rendezvous Concepts manual.
 *
 * In addition, the user may specify a reusable name for the CM
 * transport, an interval and the number of rounds.
 *
 *
 * Normally a listener such as tibrvcmlisten should be started first.
 *
 * Examples:
 *
 *  Publish a message on subject a.b.c with default parameters:
 *   tibrvcmsend  a.b.c "This is my first message"
 *
 *  Publish a message on subject a.b.c using port 7566 and
 *  ledger name myledger:
 *   tibrvcmsend  -service 7566 -ledger myledger a.b.c message
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "tibrv/tibrv.h"
#include "tibrv/cm.h"

#define MIN_PARMS       (3)
#define FIELD_NAME      "DATA"

tibrvcmTransport    cmtransport;
tibrvMsg            send_message;
tibrv_u32           current_round;
tibrv_u32           roundsNum = 10;
char*               progname;


void
usage(void)
{
    fprintf(stderr,"tibrvcmsend    [-service service] [-network network] \n");
    fprintf(stderr,"               [-daemon daemon] [-ledger <filename>]\n");
    fprintf(stderr,"               [-cmname <cmname>] [-interval <number>]\n");
    fprintf(stderr,"               [-rounds <number>] subject message\n");
    exit(1);
}

/*********************************************************************/
/* get_InitParms:  Get from the command line the parameters that can */
/*                 be passed to tibrvTransport_Create() and          */
/*                 tibrvcmTransport_Create().                        */
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
    char**      cmnameStr,
    tibrv_f64*  intervalNum,
    tibrv_u32*  roundsNum)
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
        else if (strcmp(argv[i], "-ledger") == 0)
        {
            *ledgerStr = argv[i+1];
            i+=2;
        }
        else if (strcmp(argv[i], "-cmname") == 0)
        {
            *cmnameStr = argv[i+1];
            i+=2;
        }
        else if (strcmp(argv[i], "-interval") == 0)
        {
            *intervalNum = atof(argv[i+1]);
            i+=2;
        }
        else if (strcmp(argv[i], "-rounds") == 0)
        {
            *roundsNum = atoi(argv[i+1]);
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
/* sendMsgCallback:  This timer callback sends the message for the   */
/*                   number of rounds specified on the command line  */
/*                   or for the default number of rounds.            */
/*                                                                   */
/*                   returns void                                    */
/*                                                                   */
/*********************************************************************/
void
sendMsgCallback(
    tibrvEvent event,
    tibrvMsg   message,
    void *     closure)
{
    tibrv_status err;

    /* If we've hit our limit, let's quit */
    if (current_round == roundsNum)
    {
        tibrvEvent_Destroy(event);
    }

    /* Send the message */
    err = tibrvcmTransport_Send(cmtransport, send_message);

    if (err == TIBRV_OK) {
        fprintf(stderr, "%s: %s in sending message %d\n",
                    progname, tibrvStatus_GetText(err), current_round);
    } else {
        fprintf(stderr, "%s: Error sending message --%s\n",
                progname, tibrvStatus_GetText(err));
    }

    current_round++;

}

int
main(int argc, char **argv)
{
    tibrv_status        err;
    tibrvTransport      transport;
    tibrvEvent          timerId;

    int                 currentArg;
    int                 subjectLocation;

    char*               serviceStr = NULL;
    char*               networkStr = NULL;
    char*               daemonStr  = NULL;
    char*               ledgerStr  = NULL;
    char*               cmnameStr  = "RVCMPUB";
    tibrv_f64           intervalNum = 1;


    progname = argv[0];

    /*
     * Parse arguments for possible optional parameter pairs.
     * These must precede the subject and message strings.
     */
    currentArg = get_InitParms(argc, argv, MIN_PARMS, &serviceStr,
                               &networkStr, &daemonStr, &ledgerStr,
                               &cmnameStr, &intervalNum, &roundsNum );

    /*
     * Create internal TIB/Rendezvous machinery
     */
    err = tibrv_Open();
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to open TIB/RV --%s\n",
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
     * Initialize the cm transport with the given parameters or default NULLs.
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
     * Create message
     */
    err = tibrvMsg_Create(&send_message);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to create message --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }


    /*
     * Get the subject from the command line,
     */

    subjectLocation = currentArg++;

    printf("Publishing: subject=%s \"%s\"\n",
                argv[subjectLocation], argv[currentArg]);

    /* Add the input string to our message */
    err = tibrvMsg_AddString(send_message, FIELD_NAME, argv[currentArg]);

    if (err != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: Failed to add the input string to the message --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }

    /* Set the subject name */
    err = tibrvMsg_SetSendSubject(send_message, argv[subjectLocation]);

    if (err != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: Failed to set the send subject in the message --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }


    /* Set up a timer to send the message */
    current_round = 1;
    err = tibrvEvent_CreateTimer(&timerId,
                                 TIBRV_DEFAULT_QUEUE,
                                 sendMsgCallback,
                                 intervalNum,
                                 NULL);

    if (err != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: Failed to start the timer --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }


    fprintf(stderr, "%s: Sending \"%s\" to \"%s\"\n",
                    progname, argv[currentArg], argv[subjectLocation]);


    /*
     * Dispatch loop - dispatches events which have been placed
     * on the event queue
     */

    while (tibrvQueue_Dispatch(TIBRV_DEFAULT_QUEUE) == TIBRV_OK);

    /*
     * Shouldn't get here.....
     */

    /* Destroy Message */
    tibrvMsg_Destroy(send_message);

    /* Destroy transports */
    tibrvcmTransport_Destroy(cmtransport);
    tibrvTransport_Destroy(transport);


    /*
     * tibrv_Close() will destroy the transport and guarantee delivery.
     */

    tibrv_Close();

    exit(0);
}

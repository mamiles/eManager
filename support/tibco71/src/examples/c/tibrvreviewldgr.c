
/*
 * Copyright (c) 2001-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvreviewldgr.c    1.10
 */

/*
 * tibrvreviewldgr - sample CM Rendezvous cm ledger review
 *
 * This program reviews the ledger file of a persistent certified
 * correspondent.  Transport parameters, cmname, ledger file, an
 * optional limit on the number of message subjects to be reviewed,
 * and the subject string are command line arguments to this program.
 *
 * If none are specified, the following defaults are used:
 *  service     32765
 *  network     127.0.0.1       (loopback)
 *  daemon      null
 *  subject     ">"
 *
 * Non-blank cmname and ledger file parameters are required.
 *
 * Examples:
 *
 *  Review all subjects in ledger cm.ldg created by cm transport
 *  with cmname cmsender
 *      tibrvreviewldgr -cmname cmsender -ledger cm.ldg
 *
 * ======================================================================= *
 *                                                                         *
 *                           ***** WARNING *****                           *
 *                                                                         *
 * The tibrvcmtransport_ReviewLedger function is intended to be used in    *
 * the context of the running CM application.  This example shows how to   *
 * obtain information on sent messages from a ledger, but it is not in     *
 * context.                                                                *
 *                                                                         *
 * Re-creating a persistent cm transport in a different program can have   *
 * unintended consequences.  This program should not be used on a system   *
 * with a network connection which would allow it to interfere with a      *
 * production environment.                                                 *
 *                                                                         *
 * ======================================================================= *
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "tibrv/tibrv.h"
#include "tibrv/cm.h"

#define MIN_PARMS       (4)     /* Must have at least the cmname and ledger file */

tibrvcmTransport    cmtransport;
char*               progname;
tibrv_u32           msgcount = 0;
tibrv_u32           maxsubj = 0;

/*
 * Display program usage information if incorrect or insufficient command line
 * arguments are supplied.
 */
void
usage(void)
{
    fprintf(stderr,"tibrvreviewldgr  [-service service] [-network network] \n");
    fprintf(stderr,"                 [-daemon  daemon]  [-ledger  filename]\n");
    fprintf(stderr,"                 [-cmname  cmname]  [-maxsubj #subjects]  subject \n");
    exit(1);
}


/*********************************************************************/
/* get_InitParms:  Get from the command line the parameters that can */
/*                 be passed to tibrvTransport_Create() and          */
/*                 tibrvcmTransport_Create(), the maximum number     */
/*                 of matching subjects to be reviewed, and the      */
/*                 subject string.                                   */
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
    char**      subjectStr)
{
    int i=1;

    if ( argc < min_parms )
        usage();

    while ( i+2 <= argc && *argv[i] == '-' )
    {
        if (strcmp(argv[i], "-service") == 0)
        {
            /* Save string value for cm service parameter. */
            *serviceStr = argv[i+1];
            i+=2;
        }
        else if (strcmp(argv[i], "-network") == 0)
        {
            /* Save string value for cm network parameter. */
            *networkStr = argv[i+1];
            i+=2;
        }
        else if (strcmp(argv[i], "-daemon") == 0)
        {
            /* Save string value for cm daemon parameter. */
            *daemonStr = argv[i+1];
            i+=2;
        }
        else if (strcmp(argv[i], "-ledger") == 0)
        {
            /* Save string value for cm ledger file parameter. */
            *ledgerStr = argv[i+1];
            i+=2;
        }
        else if (strcmp(argv[i], "-cmname") == 0)
        {
            /* Save string value for cm correspondent name parameter. */
            *cmnameStr = argv[i+1];
            i+=2;
        }
        else if (strcmp(argv[i], "-maxsubj") == 0)
        {
            /* Save integer value for maximum number of subject messages. */
            maxsubj = atoi(argv[i+1]);
            i+=2;
        }
        else
        {
            usage();
        }
    }

    if (i+1 <= argc)
    {
        /* If there is a parameter remaining, save it as the subject string. */
        *subjectStr = argv[i++];
    }

    return( i );
}

/*
 * Ledger review callback--  tibrvcmTransport_ReviewLedger calls this routine
 * once for each matching subject in the ledger file on which the cm transport
 * has sent message(s).  The ledger review does not provide information on
 * messages received by the transport.
 *
 * Note that this process does not require the program to create a queue or a
 * dispatcher.
 *
 */
void *
review_callback(
    tibrvcmTransport    cmtransport,
    const char*         subject,
    tibrvMsg            message,
    void*               closure)
{

    tibrv_status        err;
    tibrv_u32           numFields;
    tibrv_u32           i;

    const char*         ldgr_subject   = NULL;
    tibrv_u32           seqno_last_sent;
    tibrv_u32           total_msgs;
    tibrv_u32           total_size;

    tibrvMsgField       listener;
    const char*         listener_cmname    = NULL;
    tibrv_u32           last_confirmed;

    /* Count the message */
    msgcount++;

    /* Get the number of fields in the ledger message. */
    err   = tibrvMsg_GetNumFields(message, &numFields);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Unable to get field count--%s.\n",
                    progname, tibrvStatus_GetText(err));
        exit(1);
    }

    /*
     * Get the data associated with the sender from the first 4 fields
     * in the message.  Display the subject and last sequence number sent.
     */
    err   = tibrvMsg_GetString(message, "subject", &ldgr_subject) ||
            tibrvMsg_GetU32(message, "seqno_last_sent", &seqno_last_sent) ||
            tibrvMsg_GetU32(message, "total_msgs", &total_msgs) ||
            tibrvMsg_GetU32(message, "total_size", &total_size);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Unable to process review message--%s.\n",
                    progname, tibrvStatus_GetText(err));
        exit(1);
    }
    printf("\nSubj: %-45s Last seqno sent: %10u\n",
                    ldgr_subject, seqno_last_sent);

    /*
     * Listener data is provided in submessage fields, one per certified
     * listener for the subject, with index values from 4 to numFields-1.
     * Get and display the data for each listener.
     */
    for (i=4;i<numFields;i++)
    {
        err   = tibrvMsg_GetFieldByIndex(message, &listener, i) ||
                tibrvMsg_GetString(listener.data.msg, "name", &listener_cmname) ||
                tibrvMsg_GetU32(listener.data.msg, "last_confirmed", &last_confirmed);
        if (err != TIBRV_OK)
        {
                fprintf(stderr, "%s: Error getting ledger data--%s\n",
                    progname, tibrvStatus_GetText(err));
        }
        else
        {
            printf("      %-45s Last confirmed:  %10d\n",
                    listener_cmname, last_confirmed);
        }
    }


    /*
     * Display the usage totals if messages on this subject are stored.
     */
    if (total_msgs > 0)
    {
        printf("                    %d messages occupy %d bytes.\n",
                total_msgs, total_size);
    }

    fflush(stdout);

    if ((maxsubj > 0) && (msgcount >= maxsubj))
    {
        /*
         * Return non-null to halt the review if we've reached the max # subjects.
         */
        return (progname);
    } else {
        /*
         * Return null to allow the review to continue.
         */
        return (NULL);
    }

}

/*
 * Main routine
 */
int
main(int argc, char **argv)
{
    tibrv_status        err;
    tibrvTransport      transport;

    int                 currentArg;

    char*               serviceStr = "32765";
    char*               networkStr = "127.0.0.1";
    char*               daemonStr  = NULL;
    char*               ledgerStr  = NULL;
    char*               cmnameStr  = NULL;
    char*               subjectStr = ">";
    FILE                *fptr = NULL;

    progname = argv[0];

    /*
     * Parse arguments for possible optional parameter pairs.
     * These must precede the subject string.
     */
    currentArg = get_InitParms(argc, argv, MIN_PARMS, &serviceStr,
                               &networkStr, &daemonStr, &ledgerStr,
                               &cmnameStr, &subjectStr);

    /* If no cmname given, warn and exit. */
    if (cmnameStr == NULL)
    {
        fprintf(stderr, "%s: Reusable cmname is required for ledger review.\n",
                        progname);
        exit(1);
    }

    /* If no ledger file given, warn and exit. */
    if (ledgerStr == NULL)
    {
        fprintf(stderr, "%s: Ledger file is required for ledger review.\n",
                        progname);
        exit(1);
    }

    /* Try to open the file for readonly access.  If not found, warn and exit. */
    if ((fptr = fopen(ledgerStr, "r")) == NULL)
    {
        fprintf(stderr, "%s: Ledger file %s not found.\n", progname, ledgerStr);
        exit(1);
    } else {
        fclose(fptr);
    }

    /*
     * Display a warning message and read from stdin to confirm that the
     * user really wants to do this review.
     */
    printf("\n*** WARNING ***   Do not use this program to review a ledger file"
           "\n                  on a system which is connected to a production"
           "\n                  environment.  Re-creating a certified transport"
           "\n                  may have unintended consequences.\n"
           "\nAre you sure you want to proceed with this ledger review (Y/N) ?   ");
    if (strchr("Yy",getchar()) == NULL)
    {
        printf("\nSkipping ledger review.\n");
        exit(0);
    }


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

    err = tibrvTransport_SetDescription(transport, "ReviewLdgr");

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
     * Report the file name and subject from the command line,
     */
    printf("\nReviewing cm ledger %s on subject \"%s\"\n",
                ledgerStr, subjectStr);


    /*
     * Start the ledger review for the specified subject string.
     */
    err = tibrvcmTransport_ReviewLedger(cmtransport, review_callback,
                subjectStr, NULL);

    if (err != TIBRV_OK)
    {
        fprintf(stderr, "\n%s: Error in review call--%s.\n",
                    progname, tibrvStatus_GetText(err));
        exit(1);
    }

    /*
     * Report the number of matches.  If maximum subject count has been reached,
     * report as stopped.
     */
    if ((maxsubj == 0) || (msgcount < maxsubj))
    {
        printf("\n                            %d matching subjects.\n",msgcount);
    } else {
        printf("\n                     Stop review after %d matching subjects.\n",
                msgcount);
    }


    /* Destroy transports */
    tibrvcmTransport_Destroy(cmtransport);
    tibrvTransport_Destroy(transport);

    /*
     * Close the tibrv machinery.
     */
    tibrv_Close();

    exit(0);
}

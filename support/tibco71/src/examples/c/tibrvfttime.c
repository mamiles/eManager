/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvfttime.c	1.17
 */


/*
 * tibrvfttime.c  - example timestamp message program using
 *                  TIB/Rendezvous Fault Tolerant API
 *
 * This program publishes the time every ten seconds.  It is designed to
 * be run with a fault tolerant backup to maintain a continuous though
 * unsynchronized timestamp.  One active member of group
 * TIBRVFT_TIME_EXAMPLE will publish the time.
 *
 * The user may specify the weight of each member of the fault tolerant
 * group as an optional command line parameter.  The default is 50.
 *
 * Optionally the user may specify communication parameters for
 * the transport used by the timestamp application and, separately,
 * for the fault tolerance.  If none are specified, default values
 * are used.  For information on default values for these parameters,
 * please see the TIBCO/Rendezvous Concepts manual.
 *
 *
 * To hear the timestamp messages, use tibrvlisten with subject
 * TIBRVFT_TIME.
 *
 * Examples:
 *
 *  Publish timestamp using all default parameters:
 *   tibrvfttime
 *
 *  Start backup timestamp publisher using with weight 10:
 *   tibrvfttime 10
 *
 *  Publish timestamps on default service, but use service 7544 for
 *  TIBRVFT messages:
 *   tibrvfttime -ft-service 7544
 */

#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#include "tibrv/tibrv.h"
#include "tibrv/ft.h"


#define SUBJECT "TIBRVFT_TIME"
char *progname;

struct stfdata
{
    tibrvTransport      transport;
    tibrvEvent          timerId;
    tibrvTransport      fttransport;
    tibrvftMember       groupId;
    tibrv_bool          active;
};

static char* UsageMessage[]=
{
    "-service <s>           Service for rendezvous communications",
    "-network <s>           Network to use",
    "-daemon <s>            Daemon to connect to",
    "-ft-service <s>        Service for fault tolerance communication",
    "-ft-network <s>        Network for fault tolerance",
    "-ft-daemon <s>         Daemon for fault tolerance",
    "-ft-weight <s>         Weight of this instance for fault tolerance",
    "<weight>               Weight can be entered with no identifier",
    NULL
};


void
usage(char * progname)
{
    int i;

    fprintf(stderr, "Usage: %s [option]... \n", progname);
    fprintf(stderr, "Where option is one of the following:\n");

    for(i = 0; UsageMessage[i] != NULL; i++)
        fprintf(stderr, "    %s\n", UsageMessage[i]);

    exit(1);
}


/*
 * RVFT advisory callback may detect and address problems.
 * This simple routine only prints messages.
 */

void
advCB(tibrvEvent        event,
      tibrvMsg          message,
      void *            closure)
{
    tibrv_status        err;
    const char          *string;
    const char          *name;

    err = tibrvMsg_GetSendSubject(message, &name);

    if (err == TIBRV_OK)
    {
        tibrvMsg_ConvertToString(message, &string);
        fprintf(stderr,
                "#### RVFT ADVISORY: %s \nAdvisory message is: %s\n",
                name, string);
    }

    return;
} /* advCB  */


/*
 * Timer callback called every 10 seconds.  Publish time if active.
 */

static void
pubTime(tibrvEvent      event,
        tibrvMsg        message,
        void *          closure)
{
    tibrv_status        err;
    time_t              curtime;
    tibrvMsgDateTime    tib_time;
    tibrvMsg            time_message;

    struct stfdata * datap = (struct stfdata *) closure;

    if (!datap->active)         /* do nothing if not active */
        return;

    /* Initialize tib_time variables */
    tib_time.nsec = 0;
    tib_time.sec = 0;

    time( &curtime);

    /* Set our seconds value */
    tib_time.sec = (tibrv_i64)curtime;

    /*
     * Create message
     */
    err = tibrvMsg_Create(&time_message);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to create message --%s\n",
                progname, tibrvStatus_GetText(err));
    }
    else
    {
        /* Add the time to the message */
        err = tibrvMsg_AddDateTime(
                time_message,
                "DATA",
                &tib_time);

        if(err != TIBRV_OK)
        {
            fprintf(stderr, "Error generating timestamp data: %s\n",
                        tibrvStatus_GetText(err));
        }
        else
        {
            err = tibrvMsg_SetSendSubject(time_message, SUBJECT);
            if(err != TIBRV_OK)
            {
                fprintf(stderr, "Error setting send subject: %s\n",
                        tibrvStatus_GetText(err));
            }
            else
            {
                err = tibrvTransport_Send(datap->transport, time_message);

                if(err != TIBRV_OK)
                {
                    fprintf(stderr, "Error publishing timestamp message: %s\n",
                                tibrvStatus_GetText(err));
                }
            }
        }
        tibrvMsg_Destroy(time_message);
    }
    return;
}


/*
 * Fault tolerance callback.  RVFT tells the application what to do.
 */

void
processInstruction(tibrvftMember        id,
                   const char *         ftGroupName,
                   tibrvftAction        action,
                   void *               closure)
{
    struct stfdata * datap =    (struct stfdata *) closure;

    switch (action)
    {
        case TIBRVFT_PREPARE_TO_ACTIVATE:
            /* include even if not using this */

            fprintf(stdout,"####### PREPARE TO ACTIVATE: %s\n", ftGroupName);
            break;

        case TIBRVFT_ACTIVATE:

            fprintf(stdout,"####### ACTIVATE: %s\n", ftGroupName);
            datap->active = TIBRV_TRUE;
            break;

        case TIBRVFT_DEACTIVATE:

            fprintf(stdout,"####### DEACTIVATE: %s\n", ftGroupName);
            datap->active = TIBRV_FALSE;
            break;

        default:

            break;

    }
    return;
}


/*********************************************************************/
/* get_InitParms:  Get from the command line the parameters that can */
/*                 be passed to tibrvTransport_Create().             */
/*                                                                   */
/*                 returns the index for where any additional        */
/*                 parameters can be found.                          */
/*********************************************************************/
int
get_InitParms( int  argc,
               char *argv[],
               int  min_parms,
               char **serviceStr,
               char **networkStr,
               char **daemonStr,
               char **ftserviceStr,
               char **ftnetworkStr,
               char **ftdaemonStr,
               tibrv_u16  *ftweight)
{
    int i=1;

    if ( argc < min_parms )
        usage(progname);

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
        else if(strcmp(argv[i], "-ft-service") == 0)
        {
            *ftserviceStr = argv[i+1];
            i+=2;
        }
        else if(strcmp(argv[i], "-ft-network") == 0)
        {
            *ftnetworkStr = argv[i+1];
            i+=2;
        }
        else if(strcmp(argv[i], "-ft-daemon") == 0)
        {
            *ftdaemonStr = argv[i+1];
            i+=2;
        }
        else if(strcmp(argv[i], "-ft-weight") == 0)
        {
            *ftweight = (tibrv_u16) atol(argv[i+1]);
            i+=2;
        }
        else
        {
            usage(progname);
        }
    }

    return( i );
}

int
main(int argc, char **argv)
{
    tibrv_status        err;

    int                 i=1;

    char                *serviceStr = NULL;
    char                *networkStr = NULL;
    char                *daemonStr = NULL;
    struct stfdata      data;   /* use for closure data block */

    tibrv_f64 timeInt = 10;     /* publish time every 10 seconds */

    char                *ftserviceStr = NULL;
    char                *ftnetworkStr = NULL;
    char                *ftdaemonStr = NULL;
    char *              groupName = "TIBRVFT_TIME_EXAMPLE";
    tibrv_u16           ftweight=50;
    tibrv_u16           numactive=1;
    tibrv_f64           hbInterval=1.5;
    tibrv_f64           prepareInterval=0;
    tibrv_f64           activateInterval=4.8;

    tibrvEvent          advId;

    /*
     * Warn user - RVFT initialization takes one activation interval.
     */

    progname = argv[0];

    fprintf( stdout, "%s initializing...\n", progname);

    i= get_InitParms( argc, argv, 0,
                      &serviceStr, &networkStr, &daemonStr,
                      &ftserviceStr, &ftnetworkStr, &ftdaemonStr, &ftweight);


    if (argc > i)
    {
        ftweight = (tibrv_u16) atol(argv[i++]);
    }


    /* Create internal TIB/Rendezvous machinery */
    err = tibrv_Open();

    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to open TIB/RV --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }


    /*
     * Main Transport is for timestamp publishing.
     * Initialize it with the given parameters or default NULLs.
     */

    err = tibrvTransport_Create(&data.transport, serviceStr,
                                networkStr, daemonStr);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to initialize main transport --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(2);
    }


    /* Prepare for publishing timestamp every 10 seconds.
     * Start repeating timer here.  Check a flag in the timer
     * callback routine, pubTime, as to whether this member is
     * the active group member.  If so it will format and send
     * the timestamp message.
     */

    fprintf(stdout,
     "Active group member will publish time every 10 seconds.\n");
    fprintf(stdout, "Subject is %s\n", SUBJECT);
    fprintf(stdout, "Inactive will not publish time\n");

    err = tibrvEvent_CreateTimer(
                &data.timerId,
                TIBRV_DEFAULT_QUEUE,
                pubTime,
                timeInt,
                &data);

    if(err != TIBRV_OK)
    {
        fprintf(stderr, "Error adding repeating timer: --%s\n",
                tibrvStatus_GetText(err));
        exit(3);
    }


    /* RVFT communications may benefit from a separate transport. */

    err = tibrvTransport_Create(&data.fttransport, ftserviceStr,
                                ftnetworkStr, ftdaemonStr);
    if (err != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: Failed to initialize fault tolerant transport --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(3);
    }

    /* Join the RVFT group. */

    err = tibrvftMember_Create(&data.groupId,
                                TIBRV_DEFAULT_QUEUE,
                                processInstruction,
                                data.fttransport,
                                groupName,
                                ftweight,
                                numactive,
                                hbInterval,
                                prepareInterval,
                                activateInterval,
                                &data); /* carry our data to others */

    if (err != TIBRV_OK)
    {
        fprintf(stderr,"%s: Failed to join group - %s\n", progname,
                tibrvStatus_GetText(err));
        exit(4);
    }

    /*
     * Subscribe to RVFT advisories.
     */

    err = tibrvEvent_CreateListener(
                &advId,
                TIBRV_DEFAULT_QUEUE,
                advCB,
                data.fttransport,
                "_RV.*.RVFT.*.TIBRVFT_TIME_EXAMPLE",
                &data);

    if(err != TIBRV_OK)
    {
        fprintf(stderr,"%s: Failed to start listening to advisories - %s\n",
                progname, tibrvStatus_GetText(err));
        exit(5);
    }


    /*
     * Dispatch loop - dispatches events which have been placed on the event queue
     */

    while (tibrvQueue_Dispatch(TIBRV_DEFAULT_QUEUE) == TIBRV_OK)
    {
        /* over and over again */
    }

    /*
     * Shouldn't get here.....
     */

    tibrvEvent_Destroy(advId);
    tibrvEvent_Destroy(data.timerId);
    tibrvftMember_Destroy(data.groupId);

    tibrv_Close();

    exit(0);
}

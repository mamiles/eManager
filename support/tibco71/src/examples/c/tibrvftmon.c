/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvftmon.c	1.8
 */


/*
 * tibrvftmon.c - example TIB/Rendezvous fault tolerant group
 *                monitor program
 *
 * This program monitors the fault tolerant group TIBRVFT_TIME_EXAMPLE,
 * the group established by the tibrvfttime timestamp message sending
 * program.   It will report a change in the number of active members
 * of that group.
 *
 * The tibrvfttime program must use the default communication
 * parameters.
 */


#include <stdio.h>
#include <stdlib.h>
#include <signal.h>

#include "tibrv/tibrv.h"
#include "tibrv/ft.h"



/*
 * Fault tolerance monitor callback called when TIBRVFT detects a
 * change in the number of active members in group TIBRVFT_TIME_EXAMPLE.
 */

static void
monCB(
    tibrvftMonitor      monitor,
    const char*         groupName,
    tibrv_u32           numActiveMembers,
    void*               closure)
{
    static unsigned long        oldNumActives = 0;

    printf("Group [%s]: has %d active members (after %s).\n",
           groupName,
           numActiveMembers,
           (oldNumActives > numActiveMembers) ?
           "one deactivated" : "one activated");

    oldNumActives = numActiveMembers;

    return;
}


int
main( int argc, char **argv)
{
    tibrv_status        err;
    tibrvTransport      transport;
    tibrvftMonitor      monitor;
    tibrv_f64           lostInt = 4.8;  /* matches tibrvfttime */

    /*
     * Create internal TIB/Rendezvous machinery
     */
    err = tibrv_Open();
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to open TIB/RV --%s\n",
                argv[0], tibrvStatus_GetText(err));
        exit(1);
    }

    /*
     * Initialize the transport with the default transport values.
     */

    err = tibrvTransport_Create(&transport, NULL, NULL, NULL);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to initialize transport --%s\n",
                argv[0], tibrvStatus_GetText(err));
        exit(1);
    }


    /* Set up the monitoring of the RVFT_TIME_EXAMPLE group */

    err = tibrvftMonitor_Create(
                &monitor,
                TIBRV_DEFAULT_QUEUE,
                monCB,
                transport,
                "TIBRVFT_TIME_EXAMPLE",
                lostInt,
                NULL);


    if(err != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: Failed to start group monitor - %s\n", argv[0],
                tibrvStatus_GetText(err));
        exit(1);
    }

    fprintf(stderr,"%s: Waiting for group information...\n", argv[0]);

    /* Dispatch loop - dispatches events which have been placed on the event queue */

    while (tibrvQueue_Dispatch(TIBRV_DEFAULT_QUEUE) == TIBRV_OK)
    {
        /* over and over again */
    }

    /*
     * Shouldn't get here.....
     */
    tibrvftMonitor_Destroy(monitor);
    tibrv_Close();

    exit(0);
}

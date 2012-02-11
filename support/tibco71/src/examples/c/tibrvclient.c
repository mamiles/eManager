/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvclient.c	1.19
 */

/*
 * TIB/Rendezvous client Program
 *
 * This program will attempt to contact the server program and then
 * perform a seried of tests to determine msg throughput and response
 * times.
 *
 * Optionally the user may specify communication parameters for
 * tibrvTransport_Create.  If none are specified, default values
 * are used.  For information on default values for these parameters,
 * please see the TIBCO/Rendezvous Concepts manual.
 *
 * In addition, the user may specify the number of client requests.
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "tibrv/tibrv.h"

#define DEFAULT_SERVICE     NULL
#define DEFAULT_NETWORK     NULL
#define DEFAULT_DAEMON      NULL

#define DEFAULT_REQUESTS    10000

#define SEARCH_SUBJECT      "TIBRV.LOCATE"  /* Clients use this subject
                                               to locate a server.       */

#define SEARCH_TIMEOUT      60.0            /* Clients quit searching for
                                               a server after SEARCH_TIMEOUT
                                               seconds have elapsed. */

#define REQUEST_TIMEOUT     50.0             /* Clients quit waiting for a
                                               reply from the server after
                                               REQUEST_TIMEOUT seconds have
                                               elapsed. */

char*                       program_name;
static tibrvTransport       transport;
static tibrvQueue           response_queue;
static tibrvEvent           response_id;
static tibrvDispatcher      response_thread;


static tibrv_u32            requests = DEFAULT_REQUESTS;
static tibrv_u32            responses = 0;

static int                  exit_status = 1;

/* This routine displays parameter information if invalid parameters are
   detected or the program is executed with a help request flag of
   -help or -h or -? */

void
usage(void)
{
    fprintf(stderr,"tibrvclient     [-service service] [-network network]\n");
    fprintf(stderr,"                [-daemon daemon] <number of requests>\n");
    exit(1);
}

/* This routine parses the parameters on the command line. */
tibrv_u32
getParameters(int       argc,
              char*     argv[],
              char**    serviceStr,
              char**    networkStr,
              char**    daemonStr)
{
    char*     progptr;
    int       i = 1;

    program_name = argv[0];
    if (strrchr(program_name,'\\') != NULL)
    {   /* strip off directory information in \ format */
        progptr = strrchr(program_name,'\\')+1;
        strncpy(program_name, progptr,
                (strlen(program_name)-(progptr-program_name)+1));
    }
    else if (strrchr(program_name,'/') != NULL)
    {   /* strip off directory information in / format */
        progptr = strrchr(program_name,'/')+1;
        strncpy(program_name, progptr,
                (strlen(program_name)-(progptr-program_name)+1));
    }
    else if (strrchr(program_name,']') != NULL)
    {   /* strip off directory information in / format */
        progptr = strrchr(program_name,']')+1;
        strncpy(program_name, progptr,
                (strlen(program_name)-(progptr-program_name)+1));
    }

    if (i < argc)
    {
        if ((strcmp(argv[i],"-h")==0) ||
            (strcmp(argv[i],"-help")==0) ||
            (strcmp(argv[i],"?")==0))
        {       /* if first command line argument is help flag,
                   display and quit. */
            usage();
        }
    }

    while (((i + 2) <= argc) && (*argv[i] == '-'))      /* These require a
                                                           value. */
    {
        if (strcmp(argv[i], "-service") == 0)
        {
            *serviceStr = argv[i + 1];
            i += 2;
        }
        else if (strcmp(argv[i], "-network") == 0)
        {
            *networkStr = argv[i + 1];
            i += 2;
        }
        else if (strcmp(argv[i], "-daemon") == 0)
        {
            *daemonStr = argv[i + 1];
            i += 2;
        }
        else
        {
            usage();
        }
    }
    return i;
}

/* This routine processes responses from our server when we send it messages
   after we have identified it and received its inbox address.  All we do
   here is count the replies and exit if we have not received all the
   responses.

   When all have been received, destroy the queue to end all dispatching. */

static void
serverResponse(
    tibrvEvent  event,
    tibrvMsg    msg,
    void*       arg)
{
    responses++;
    if (responses >= requests)
    {
        /*Destroy our listener event and dispatcher thread. */
        tibrvEvent_Destroy(event);
        tibrvDispatcher_Destroy(response_thread);
        tibrvQueue_Destroy(response_queue);
    }
}

/* This is the main routine. */
int
main(int argc, char** argv)
{
    int                 last_argument_index;

    tibrv_status        return_code;

    char*               serviceStr = DEFAULT_SERVICE;
    char*               networkStr = DEFAULT_NETWORK;
    char*               daemonStr  = DEFAULT_DAEMON;

    tibrvMsg            search_request;
    tibrvMsg            search_reply;

    const char*         server_subject;
    static char         request_subject[TIBRV_SUBJECT_MAX];
    static char         response_subject[TIBRV_SUBJECT_MAX];

    tibrvMsg            client_request;

    tibrvMsgDateTime    date_time_start, date_time_stop;
    tibrv_f64           time_start, time_stop, elapsed;

    tibrv_u32           i;

    /* Parse the command line. */
    last_argument_index = getParameters(argc, argv,
                                        &serviceStr, &networkStr, &daemonStr);
    if (last_argument_index < argc) {
        requests = atoi(argv[last_argument_index]);
    }

    /* The TIB/Rendezvous machinery needs to be started. */
    return_code = tibrv_Open();
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to open the TIB/Rendezvous machinery: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* A transport needs to be created. */
    return_code = tibrvTransport_Create(&transport,
                                        serviceStr, networkStr, daemonStr);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to create a transport: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }
    tibrvTransport_SetDescription(transport, program_name);

    /* We create the message we will send in order to locate a server. */
    return_code = tibrvMsg_Create(&search_request);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to create a search request: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Set the send subject to locate our server. */
    return_code = tibrvMsg_SetSendSubject(search_request,
                                          SEARCH_SUBJECT);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to set the send subject of a search request: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    fprintf(stdout,
            "%s is searching for a server on subject %s...\n",
            program_name, SEARCH_SUBJECT);

    /* Send a request message to locate a server and receive its reply.
       SendRequest is a synchronous call which uses a private queue to
       receive its reply.  No external dispatching mechanism is involved. */

    return_code = tibrvTransport_SendRequest(transport,
                                             search_request,
                                             &search_reply,
                                             SEARCH_TIMEOUT);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to locate a server: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /*
      The search reply we receive from a server should contain a reply
      subject we can use to send requests to that server.
    */

    return_code = tibrvMsg_GetReplySubject(search_reply,
                       &server_subject);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to get the request subject of a search reply: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }
    strcpy(request_subject, server_subject);

    fprintf(stdout,
            "%s successfully located a server: %s\n",
            program_name, request_subject);

    /* Destroy the server's reply message to reclaim memory. */
    return_code = tibrvMsg_Destroy(search_reply);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to destroy a reply to a client request: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /*
      Create response queue, an inbox point-to-point subject, and a listener
      using that subject for responses from the server to a series of
      messages.
    */

    tibrvQueue_Create(&response_queue);
    tibrvTransport_CreateInbox(transport, response_subject, TIBRV_SUBJECT_MAX);
    return_code = tibrvEvent_CreateListener(&response_id,
                                            response_queue,
                                            serverResponse,
                                            transport,
                                            response_subject,
                                            NULL);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr, "%s failed to initialize response mechanism: %s\n",
                program_name, tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Start a dispatcher thread to dispatch response messages. */
    return_code = tibrvDispatcher_CreateEx(&response_thread,
                                           response_queue,
                                           REQUEST_TIMEOUT);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr, "failed to create response dispatcher--%s\n",
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Because this is only a test, we will repeatedly send the same request
       message to the server.  We'll change the data in the message each time.
    */

    return_code = tibrvMsg_Create(&client_request);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to create a client request: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Set the send subject to the server's (inbox) subject. */
    return_code = tibrvMsg_SetSendSubject(client_request,
                                          request_subject);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to set the send subject of a client request: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Set the reply subject to our inbox subject, allowing a point-to-point
       reply from our server.  We won't use SendRequest, so we won't block
       waiting for th reply. */

    return_code = tibrvMsg_SetReplySubject(client_request,
                                           response_subject);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to set the reply subject of a client request: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    fprintf(stdout, "Starting test...\n");

    /* We will time this test. */
    tibrvMsg_GetCurrentTime(&date_time_start);
    time_start = date_time_start.sec + (date_time_start.nsec / 1000000000.0);

    for (i = 0; i < requests; i++)
    {
        /* Put some data into the message to the server. */
        return_code = tibrvMsg_UpdateU32(client_request, "x",
                     ((tibrv_u32) rand()));
        if (return_code != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s failed to update a client request: %s\n",
                    program_name,
                    tibrvStatus_GetText(return_code));
            exit(1);
        }

        return_code = tibrvMsg_UpdateU32(client_request, "y",
                     ((tibrv_u32) rand()));
        if (return_code != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s failed to update a client request: %s\n",
                    program_name,
                    tibrvStatus_GetText(return_code));
            exit(1);
        }

        /* Send a request message to the server. */
        return_code = tibrvTransport_Send(transport,
                                          client_request);
        if (return_code != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s failed to send a client request: %s\n",
                    program_name,
                    tibrvStatus_GetText(return_code));
            exit(1);
        }

    }

    /* Report the number of messages sent and number received while sending. */
    fprintf(stdout,
        "%d request messages sent.  %d messages received while sending.\n",
            requests, responses);

    tibrvDispatcher_Destroy(response_thread);

    /* Dispatch loop - dispatches events from the event queue in the main thread,
       in addition to the dispatcher thread created earlier.  Note that if the
       last message is dispatched from that thread while this loop is executing
       its TimedDispatch call, the loop will exit only after it times out.  The
       total time will be the time required to receive all the replies plus the
       REQUEST_TIMEOUT value.  */

    while ((responses < requests) &&
           (tibrvQueue_TimedDispatch(response_queue, REQUEST_TIMEOUT) ==
                    TIBRV_OK));

    /* Report the run statistics for this test. */
    tibrvMsg_GetCurrentTime(&date_time_stop);
    time_stop = date_time_stop.sec + (date_time_stop.nsec / 1000000000.0);

    elapsed = time_stop - time_start;

    if (responses >= requests)
    {
        fprintf(stdout, "%s received all %d server replies\n",
                program_name, responses);
        fprintf(stdout, "%d requests took %.2f secs to process.\n",
                requests, elapsed);
        fprintf(stdout,
                "This result implies an effective rate of %.1f requests/second.\n",
                ((tibrv_f64) requests / elapsed));
        exit_status = 0;
    }
    else
    {
        fprintf(stdout, "Received %d responses to  %d requests.\n",
                responses, requests);
        exit_status = 1;
    }


    /* Destroy our listener event and dispatcher thread. */
    tibrvEvent_Destroy(response_id);
    tibrvDispatcher_Destroy(response_thread);

    /* Destroy our queue. */
    tibrvQueue_Destroy(response_queue);

    /* Destroy our transport. */
    tibrvTransport_Destroy(transport);

    /* Close the Tibrv machinery and exit. */
    tibrv_Close();

    exit(exit_status);
}

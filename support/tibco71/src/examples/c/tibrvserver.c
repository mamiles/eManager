/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvserver.c	1.15
 */

/*
 * TIB/Rendezvous server Program
 *
 * This program will answer trivial request from tibrvclient programs.
 * It uses a dispatch loop in a single thread.
 *
 * Optionally the user may specify communication parameters for
 * tibrvTransport_Create.  If none are specified, default values
 * are used.  For information on default values for these parameters,
 * please see the TIBCO/Rendezvous Concepts manual.
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "tibrv/tibrv.h"

#define DEFAULT_SERVICE     NULL
#define DEFAULT_NETWORK     NULL
#define DEFAULT_DAEMON      NULL

#define SEARCH_SUBJECT      "TIBRV.LOCATE"  /* Clients search for servers
                                               using this subject. */

#define SERVER_TIMEOUT      120.            /* Server times out after
                                               2 minutes. */

static char*                program_name;

static tibrvTransport       transport;

static tibrvMsg             search_reply;   /* search_reply has global
                                               scope because we send
                                               the same reply to all
                                               search requests.
                                               Moreover,we create
                                               search_reply in main(),
                                               but send it from
                                               searchCallback(). */

static tibrv_bool           new_msg = TIBRV_FALSE; /* use a new message for
                                               reply if true;
                                               otherwise put sum
                                               in received msg. */

/* This routine lists the program parameters if the first parameter is a help
   flag (-help or -h or -?) or invalid parameters are detected. */

void
usage(void)
{
    fprintf(stderr, "smart_server   [-service service] [-network network]\n");
    fprintf(stderr, "               [-daemon daemon]\n");
    exit(1);
}

/* This routine parses the command line. */
tibrv_u32
getParameters(int       argc,
              char*     argv[],
              char**    serviceStr,
              char**    networkStr,
              char**    daemonStr)
{
    char*               progptr;
    int i = 1;

    /* Program name, possibly with directory data, is the first element. */
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

    /* Check for a help flag as the first parameters. */
    if (i < argc)
    {
        if ((strcmp(argv[i],"-h")==0) ||
            (strcmp(argv[i],"-help")==0) ||
            (strcmp(argv[i],"?")==0))
        {
            /* if first command line argument is help flag,
           display and quit. */
            usage();
        }
    }

    /* Parse parameters with a following value. */
    while (((i + 2) <= argc) && (*argv[i] == '-'))
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
            /* If there is some other parameter with a value,
           display and quit. */
            usage();
        }
    }
    return i;   /* return the index of the next argument */
}

/* This callback is executed when a server search is received.  It sends the
   message prepared in the main routine as a reply to server query messages. */

static void
searchCallback(tibrvEvent event,
               tibrvMsg   message,
               void*      closure)
{
    tibrv_status return_code = TIBRV_OK;

    /*  Send our previously prepared reply message. */
    return_code = tibrvTransport_SendReply(transport, search_reply, message);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to send a reply to a server search: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }
}

/* This callback is executed when a client request is received.  It adds the
   values in the request, puts the result in a message, and sends it as
   a reply. */

static void
requestCallback(tibrvEvent event,
                tibrvMsg   message,
                void*      closure)
{
    tibrv_status    return_code = TIBRV_OK;
    tibrvMsg        request_reply;
    tibrv_u32       x, y, sum;

    /* Get the values in field "x" */
    return_code = tibrvMsg_GetU32(message, "x", &x);;
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to get the value of x: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Get the values in field "y" */
    return_code = tibrvMsg_GetU32(message, "y", &y);;
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to get the value of y: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Add the values and update the reply message with the sum. */
    sum = x + y;


    if (new_msg)
    {
        /* Create a new reply message. */
        return_code = tibrvMsg_Create(&request_reply);
        if (return_code != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s failed to initialize a reply to a client request: %s\n",
                    program_name,
                    tibrvStatus_GetText(return_code));
            exit(1);
        }

        /* Put the sum in the reply message. */
        return_code = tibrvMsg_UpdateU32(request_reply, "sum", sum);
        if (return_code != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s failed to update a reply to a client request: %s\n",
                    program_name,
                    tibrvStatus_GetText(return_code));
            exit(1);
        }

        /* Send a reply to the request message. */

        return_code = tibrvTransport_SendReply(transport, request_reply, message);
        if (return_code != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s failed to send a reply to a client request: %s\n",
                    program_name,
                    tibrvStatus_GetText(return_code));
            exit(1);
        }

        /* Destroy our reply message to reclaim space. */
        return_code = tibrvMsg_Destroy(request_reply);
        if (return_code != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s failed to destroy a reply to a client request: %s\n",
                    program_name,
                    tibrvStatus_GetText(return_code));
            exit(1);
        }
    }
    else
    {
        /* Put the sum in the request message received from the client. */
        return_code = tibrvMsg_UpdateU32(message, "sum", sum);
        if (return_code != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s failed to update a reply to a client request: %s\n",
                    program_name,
                    tibrvStatus_GetText(return_code));
            exit(1);
        }

        /* Send the message back as aa reply to the request message. */
        return_code = tibrvTransport_SendReply(transport, message, message);
        if (return_code != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s failed to send a reply to a client request: %s\n",
                    program_name,
                    tibrvStatus_GetText(return_code));
            exit(1);
        }

    }

}

/* This is the main routine. */
int
main(int    argc,
     char** argv)
{
    tibrv_status        return_code;

    tibrvEvent          search_event = 0;
    tibrvEvent          request_event = 0;

    char*               serviceStr = DEFAULT_SERVICE;
    char*               networkStr = DEFAULT_NETWORK;
    char*               daemonStr  = DEFAULT_DAEMON;

    char                request_subject[TIBRV_SUBJECT_MAX];

    /* Parse the command line and set up the transport parameters. */
    getParameters(argc, argv, &serviceStr, &networkStr, &daemonStr);

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

    /* This listener will pay attention to server searches. */
    return_code = tibrvEvent_CreateListener(&search_event,
                                            TIBRV_DEFAULT_QUEUE,
                                            searchCallback,
                                            transport,
                                            SEARCH_SUBJECT,
                                            NULL);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to create a server search listener: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Since we wish to receive point-to-point requests, we must create an
       inbox.
       We will transmit this inbox subject to our clients, and listen for
       it. */

    tibrvTransport_CreateInbox(transport,
                               request_subject,
                               TIBRV_SUBJECT_MAX);
    return_code = tibrvEvent_CreateListener(&request_event,
                                            TIBRV_DEFAULT_QUEUE,
                                            requestCallback,
                                            transport,
                                            request_subject,
                                            NULL);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to create a client request listener: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* We define the message we will use to reply to server searches.  This
       message will be reused if more than one search query is received. */

    return_code = tibrvMsg_Create(&search_reply);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to create a reply to a server search: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Set the required request subject as the reply subject of our search
       reply.  The client will use it to send requests to the server. */

    return_code = tibrvMsg_SetReplySubject(search_reply, request_subject);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to set reply subject for a server search reply: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Display a server-ready message. */
    fprintf(stderr, "Listening for client requests on subject %s.\n"
            "Wait time is %.0f secs.\n%s ready...\n",
            SEARCH_SUBJECT, SERVER_TIMEOUT, program_name);


    /* If this server remains idle for more than the timeout value, exit. */
    while (tibrvQueue_TimedDispatch(TIBRV_DEFAULT_QUEUE, SERVER_TIMEOUT) == TIBRV_OK);

    /* Destroy our Tibrv objects and close the Tibrv machinery. */
    tibrvMsg_Destroy(search_reply);
    tibrvEvent_Destroy(search_event);
    tibrvEvent_Destroy(request_event);
    tibrvTransport_Destroy(transport);
    tibrv_Close();

    exit(0);
}

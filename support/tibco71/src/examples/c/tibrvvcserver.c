/*
 * Copyright (c) 1998-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvvcserver.c	1.9
 */

/*
 * TIB/Rendezvous Virtual Circuit server Program
 *
 * This program will answer trivial request from vcclient programs.
 *
 * Optionally the user may specify communication parameters for
 * tibrvTransport_Create.  If none are specified, default values
 * are used.  For information on default values for these parameters,
 * please see the TIBCO/Rendezvous Concepts manual.
 *
 * The user may specify the number of threads.  If not specified,
 * the default is 1.
 *
 * Example:
 *
 * Set up a vc server using service 7725, the loopback adapter, a daemon listening
 * on tcp:7505, and running 5 dispatcher threads.
 *  tibrvvcserver -service 7725 -network 127.0.0.1 -daemon 7505 -threads 5
 *
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "tibrv/tibrv.h"

#define DEFAULT_SERVICE     NULL
#define DEFAULT_NETWORK     NULL
#define DEFAULT_DAEMON      NULL

#define DISCONNECT_ADVISORY "_RV.*.SYSTEM.VC.DISCONNECTED"

#define SEARCH_SUBJECT      "TIBRV.VC.LOCATE"  /* Clients search for servers
                                               using this subject. */

#define WORK_SUBJECT        "TIBRV.VC.WORK"    /* Clients send work to the
                                               server on this subject OVER the
                                               virtual circuit.  The VC transport
                                               gets it on the receive side, and
                                               then dispatches it to the appropriate
                                               queue. */

#define VC_ACCEPT_TIMEOUT   20.                /* Our VC acceptors will wait 20
                                               seconds for a connection after we
                                               send the connection response to
                                               a process looking for us.  If we
                                               do not get a circuit connected in
                                               before this timer expires we will
                                               destroy the acceptor and move on. */

#define SERVER_TIMEOUT      120.               /* Server times out after
                                               2 minutes of inactivity. */


#define VC_THREAD_MAX       5                  /* The maximum number of threads to
                                               dispatch off of a server dispatch
                                               queue group.  This is a 'hook' to
                                               allow the specification of the max
                                               number of threads on the command
                                               line. */


static char*                program_name;



/*
 *  A bookkeeping structure for our server.  This program could be
 *  extended to support multiple VC servers; each listening for
 *  search requests.
 */
typedef struct serverStruct {
    tibrvTransport      server_transport;   /* for search requests */
    tibrvEvent          search_event;       /* eventId for searches */
    tibrvEvent          server_inbox_event; /* eventId for the listener */
    unsigned int        vc_count;
    tibrvQueueGroup     vc_queues;          /* what we dispatch the VCs off of */
    tibrvDispatcher     vc_dispatchers[VC_THREAD_MAX];  /* the dispatch threads */
    char                server_name[TIBRV_SUBJECT_MAX]; /* a char identifier */
    char                search_subject[TIBRV_SUBJECT_MAX];
    char                server_inbox[TIBRV_SUBJECT_MAX];
} serverRec, *serverPtr;



/*
 * This is in case we want to know the state of a VC within the server
 * in the future.  These are arbitrary and reflect the design of this
 * example and not any internal state of the VC.
 */
typedef enum  {
    initializing=0,
    waiting,
    connected,
    disconnected
} vcState;



/*
 *  A bookkeeping structure for our virtual circuits.  One of these
 *  is created for each VC that we setup.
 */
typedef struct vcStruct {
    serverPtr       server;             /* backlink to the serverStruct that is
                                        the 'owner' of this VC */
    vcState         vc_state;           /* our own state of the VC */
    unsigned int    msgs_in;
    unsigned int    msgs_out;
    tibrvEvent      vc_disconnect_event;
    tibrvEvent      vc_msg_event;
    tibrvEvent      vc_inbox_event;
    tibrvTransport  vc_transport;       /* the VC transport we create */
    tibrvQueue      vc_queue;           /* the incoming queue for work on this VC */
    char            vc_inbox[TIBRV_SUBJECT_MAX];
    const char*     connect_subject;    /* what we need to send back in response
                                        to queries that uniquely identifies this
                                        connection - the client needs this to
                                        connect to our acceptor */

} vcRec, *vcPtr;


static tibrv_bool           new_msg = TIBRV_FALSE; /* use a new message for
                                               reply if true;
                                               otherwise put sum
                                               in received msg. */



/*
 *  This routine lists the program parameters if the first parameter is a help
 *  flag (-help or -h or -?) or invalid parameters are detected.
 */

void
usage(void)
{
    fprintf(stderr, "usage: tibrvvcserver [-service service] [-network network]\n");
    fprintf(stderr, "                     [-daemon daemon] [-threads threads]\n");
    exit(1);
}



/* This routine parses the command line. */
tibrv_u32
getParameters(int       argc,
              char*     argv[],
              char**    serviceStr,
              char**    networkStr,
              char**    daemonStr,
              int*      threads)
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
        else if (strcmp(argv[i], "-threads") == 0)
        {
            *threads = atoi(argv[i + 1]);
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



/*
 * This callback is executed when a message is received in the control
 * INBOX.  It acts as a 'hook' for server administration functions.  For
 * example, you could have an administrative 'search' request that
 * locates servers that contain specific objects; receiving the control
 * INBOXes of the servers in response to the query.  You could then send
 * a command to these servers via these control INBOXes and tally the
 * responses.
 *
 * There is no action in this callback in this example except for printing
 * out a message.
 */

static void
serverControlInboxCallback(tibrvEvent event,
                tibrvMsg   message,
                void*      closure)
{
    fprintf(stderr, "%s: server control inbox callback not implemented.\n",
	    program_name);
    return;
}



/*
 * This callback is executed when a message is received in the control
 * INBOX, but for virtual circuit INBOXes instead of those on the main
 * network transport.  It acts as a 'hook' for server administration
 * functions.
 *
 * There is no action in this callback in this example except for printing
 * out a message.
 */

static void
vcControlInboxCallback(tibrvEvent event,
                tibrvMsg   message,
                void*      closure)
{
    fprintf(stderr, "%s: VC control inbox callback not implemented.\n",
	    program_name);
    return;
}



/* This callback is executed when a client request is received.  It adds the
   values in the request, puts the result in a message, and sends it as
   a reply.  */

static void
requestCallback(tibrvEvent event,
                tibrvMsg   message,
                void*      closure)
{
    tibrv_status    return_code = TIBRV_OK;
    tibrvMsg        request_reply;
    tibrv_u32       x, y, sum;
    vcRec*          vc = (vcRec*)closure;

    vc->msgs_in++;

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

        /* Send a reply to the request message on the VC transport.

        Note that this either may be a multicast subject or an
        INBOX subject, we do not care which it is. The response will
        go back point to point to the requesting process on the VC
        regardless. */

        return_code = tibrvTransport_SendReply(vc->vc_transport,
                            request_reply, message);
        if (return_code != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s failed to send a reply to a client request: %s\n",
                    program_name,
                    tibrvStatus_GetText(return_code));
            exit(1);
        }

        vc->msgs_out++;

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

        /* Send the message back as a reply to the request message. */
        return_code = tibrvTransport_SendReply(vc->vc_transport,
                                message, message);
        if (return_code != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s failed to send a reply to a client request: %s\n",
                    program_name,
                    tibrvStatus_GetText(return_code));
            exit(1);
        }

        vc->msgs_out++;
    }

}



/* The following routine destroys the VC structures */
void
destroyVC(vcRec*    vc)
{
    tibrv_status    return_code = TIBRV_OK;
    unsigned int    queue_count = 0;

    /* Remove VC queue from active circuit queue group - stops any
    dispatching on the queue by removing it from the group.  You
    could be more sophisticated here and let the queue count go to
    zero before you destroy the queue.

    A good way to do this would be to set a timer and posting
    the timer event to the server dispatch queue; allowing the
    dispatching threads to finish any outstanding processing on
    the VC queue.  Once the timer callback determined that the
    queue count is zero, we would be assured that there would be
    no more pending work and the vc_queue could be removed from the
    dispatching queue group and destroyed without the risk of any
    lost data. */
    return_code = tibrvQueueGroup_Remove(vc->server->vc_queues,
                                    vc->vc_queue);
    return_code = tibrvQueue_GetCount(vc->vc_queue, &queue_count);

    if (queue_count != 0) {
        fprintf(stderr,
                "%s: disconnected VC queue not empty, count: %d \n",
                program_name, queue_count);
    }

    return_code = tibrvQueue_Destroy(vc->vc_queue);

    /* free the VC structure */
    free(vc);
}



/*
 *  This callback is executed whenever we get a virtual circuit disconnect
 *  advisory.  It unwinds all of the virtual circuit setup logic that we
 *  execute in response to a search request (in searchCallback()).
 */

static void
disconnectedVcCallback(tibrvEvent event,
                tibrvMsg    message,
                void*       closure)
{
    tibrv_status    return_code;
    vcRec*          vc = (vcRec*)closure;

    /* destroy our transport */
    return_code = tibrvTransport_Destroy(vc->vc_transport);

    fprintf(stderr, "%s: VC disconnected; in: %d, out: %d\n",
            program_name, vc->msgs_in, vc->msgs_out);
    vc->vc_state = disconnected;

    fprintf(stderr,
            "%s: VCs active: %d\n",
            program_name, --vc->server->vc_count);

    /* no more control messages on the VC or disconnect events
    since the circuit is no more */
    return_code = tibrvEvent_Destroy(vc->vc_disconnect_event);
    return_code = tibrvEvent_Destroy(vc->vc_inbox_event);
    return_code = tibrvEvent_Destroy(vc->vc_msg_event);

    /* wind down the VC data structures */
    destroyVC(vc);

    return;
}



/*
 *  This routine does the work of setting up a virtual circuit
 *  for the server.  It returns the address of the vc struct
 *  to the caller so that the searchCallback() routine can reply
 *  to the client with the appropriate information.
 */

tibrv_status
createVC(serverRec* server,
         vcRec**    vc)
{
    tibrv_status    return_code = TIBRV_OK;
    vcRec*          newVc = NULL;

    /* Since we want to receive virtual circuit connections, we need
       to create a virtual circuit transport to act as an acceptor. */

    newVc = (vcRec*)malloc(sizeof(vcRec));

    newVc->vc_state = initializing;
    newVc->msgs_in = 0;
    newVc->msgs_out = 0;
    newVc->server = server;

    return_code = tibrvTransport_CreateAcceptVc(
            &newVc->vc_transport,
            &newVc->connect_subject,
            server->server_transport);

    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: failed to create AcceptVc: %s\n",
                "createVC",
                tibrvStatus_GetText(return_code));
        free(newVc);
        return(return_code);
    }

    /* Register disconnect callback to unwind all of this work.
    We receive the event on the VC transport, but we process it
    using the server master thread processing on the DEFAULT
    queue */

    return_code = tibrvEvent_CreateListener(&newVc->vc_disconnect_event,
                                            TIBRV_DEFAULT_QUEUE,
                                            disconnectedVcCallback,
                                            newVc->vc_transport,
                                            DISCONNECT_ADVISORY,
                                            (void*)newVc);

    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: failed to set the DISCONNECT_ADVISORY callback: %s\n",
                "createVC",
                tibrvStatus_GetText(return_code));
        tibrvTransport_Destroy(newVc->vc_transport);
        free(newVc);
        return(return_code);
    }

    /* create the incoming event queue */
    return_code = tibrvQueue_Create(&newVc->vc_queue);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: failed to create vc_queue event queue: %s\n",
                "createVC",
                tibrvStatus_GetText(return_code));
        tibrvEvent_Destroy(newVc->vc_disconnect_event);
        tibrvTransport_Destroy(newVc->vc_transport);
        free(newVc);
        return(return_code);
    }

    /* Creeate an inbox subject on our vc for requests.  This could
     * also be a broadcast subject.  The server/client communication
     * will still be point-to-point over a virtual circuit.  Create
     * a listener on this subject. */
    tibrvTransport_CreateInbox(newVc->vc_transport,
                               newVc->vc_inbox,
                               TIBRV_SUBJECT_MAX);

    return_code = tibrvEvent_CreateListener(&newVc->vc_inbox_event,
                                            newVc->vc_queue,
                                            vcControlInboxCallback,
                                            newVc->vc_transport,
                                            newVc->vc_inbox,
                                            (void*)newVc);

    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: failed to set vc_control_inbox callback: %s\n",
                "createVC",
                tibrvStatus_GetText(return_code));
        tibrvQueue_Destroy(newVc->vc_queue);
        tibrvEvent_Destroy(newVc->vc_disconnect_event);
        tibrvTransport_Destroy(newVc->vc_transport);
        free(newVc);
        return(return_code);
    }

    *vc = newVc;
    return(return_code);
}



/*
 *  This callback is executed when a server search message is received.
 *
 *  It replies to the sender with the connect_subject that is required to
 *  initialize the virtual circuit.  It sets the reply_subject in the
 *  response to an INBOX that has been created on the network transport
 *  object so that the server can be uniquely identified.  (An INBOX is
 *  unique throughout the network and can be used as a unique id.)
 *
 *  All the circuit setup logic for the server is here; as is the
 *  registration of a listener event that will handle a disconnect of
 *  the virtual circuit.
 */

static void
searchCallback(tibrvEvent event,
               tibrvMsg   message,
               void*      closure)
{
    vcRec*      vc = NULL;
    serverRec*  server = (serverRec*)closure;
    tibrvMsg    search_reply;

    tibrv_status return_code = TIBRV_OK;

    /* We define the message we will use to reply to server searches. */

    return_code = tibrvMsg_Create(&search_reply);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: failed to create a reply to a server search: %s\n",
                "searchCallback",
                tibrvStatus_GetText(return_code));
        exit(1);
    }


    /* Set the required reply subject as the unique id of our server.
       This is also our control inbox. */

    return_code = tibrvMsg_SetReplySubject(search_reply,
                        server->server_inbox);

    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: failed to set reply subject for a search reply: %s\n",
                "searchCallback",
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Call our routine to accept the virtual circuit received from a
     * client. */
    return_code = createVC(server, &vc);

    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: failed to create VC: %s\n",
                "searchCallback",
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Register the listener on the VC event queue and add the
    vc_queue event queue to the queue group that the server is
    dispatching. */

    return_code = tibrvEvent_CreateListener(&vc->vc_msg_event,
                                            vc->vc_queue,
                                            requestCallback,
                                            vc->vc_transport,
                                            WORK_SUBJECT,
                                            (void*)vc);

    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: failed to set WORK_SUBJECT callback: %s\n",
                "createVC",
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    tibrvQueueGroup_Add(server->vc_queues,
                vc->vc_queue);


    /* Add the subjects as data to the message and send it. */
    return_code = tibrvMsg_AddString(search_reply,
                        "connect_subject",
                        vc->connect_subject);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: failed to set VC connect_subject for a server search reply: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    return_code = tibrvMsg_AddString(search_reply,
                        "request_subject",
                        WORK_SUBJECT);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to set VC request_subject for a server search reply: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }


    return_code = tibrvTransport_SendReply(server->server_transport, search_reply,
                                        message);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed to send a reply to a VC server search: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }


    vc->vc_state = waiting;

    /* Wait for the connection.  The timeout is how long we have to complete
    the handshake with our correspondent */
    return_code = tibrvTransport_WaitForVcConnection(vc->vc_transport,
                                VC_ACCEPT_TIMEOUT);

    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s failed on WaitForVcConnection in VC server search: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
    }

    fprintf(stderr,"%s VC accepted and connected\n", program_name);

    return_code = tibrvMsg_Destroy(search_reply);

    vc->vc_state = connected;

    fprintf(stderr,
            "%s: VCs active: %d\n",
            program_name, ++vc->server->vc_count);

}



/* This is the main routine. */
int
main(int    argc,
     char** argv)
{
    tibrv_status        return_code;

    tibrvEvent          request_event = 0;

    char*               serviceStr = DEFAULT_SERVICE;
    char*               networkStr = DEFAULT_NETWORK;
    char*               daemonStr  = DEFAULT_DAEMON;

    char                disp_name[255]; /* string buffer */

    int                 i, vcthreads=0;

    serverRec*          myServer;


    /* Parse the command line and set up the transport parameters. */
    getParameters(argc, argv, &serviceStr, &networkStr, &daemonStr, &vcthreads);

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

    /* Each 'server' manages multiple virtual circuits.  We create
    a server structure to do the bookkeeping.  A more trivial example
    could just handle one VC at a time. */

    myServer = (serverRec*)malloc(sizeof(serverRec));

    /* A network transport needs to be created. We listen for search
    requests on this transport. */
    return_code = tibrvTransport_Create(&myServer->server_transport,
                                        serviceStr, networkStr, daemonStr);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: failed to create a transport: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }
    tibrvTransport_SetDescription(myServer->server_transport, program_name);

    /* This listener will pay attention to VC server searches. */
    return_code = tibrvEvent_CreateListener(&myServer->search_event,
                                            TIBRV_DEFAULT_QUEUE,
                                            searchCallback,
                                            myServer->server_transport,
                                            SEARCH_SUBJECT,
                                            (void*)myServer);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: failed to create a server search listener: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }


    /*  Since we wish to create a unique identifier for this
        server we will create an _INBOX.  An _INBOX is unique
        not only on this host, but in the entire TIB domain in
        which it is running.  If we want to extend this server
        to receive commands on this _INBOX in the future it is
        trivial to do so.

        We will store this as our server_subject and send it
        as our reply_subject in all control or protocol messages.
    */

    tibrvTransport_CreateInbox(myServer->server_transport,
                               myServer->server_inbox,
                               TIBRV_SUBJECT_MAX);

    return_code = tibrvEvent_CreateListener(&myServer->server_inbox_event,
                                            TIBRV_DEFAULT_QUEUE,
                                            serverControlInboxCallback,
                                            myServer->server_transport,
                                            myServer->server_inbox,
                                            (void*)myServer);
    if (return_code != TIBRV_OK)
    {
        fprintf(stderr,
                "%s: failed to create a client request listener: %s\n",
                program_name,
                tibrvStatus_GetText(return_code));
        exit(1);
    }

    /* Now a queue group to hold the queues for the VCs that we
    have open */

    return_code = tibrvQueueGroup_Create(&myServer->vc_queues);
    if (return_code != TIBRV_OK) {
        fprintf(stderr,
                "%s: failed to create queue group for VCs: %s\n",
                program_name, tibrvStatus_GetText(return_code));
    }

    /* Finally, thread(s) dispatching off of the queue group.  We
    can have a number of threads waiting on this one queue group
    that will service all of the VC(s) and their individual
    queues */

    if (vcthreads>VC_THREAD_MAX) {
        vcthreads = VC_THREAD_MAX;
        fprintf(stderr,
                "%s: Maximum threads is %d - using %d\n",
                program_name, VC_THREAD_MAX, VC_THREAD_MAX);
    }

    else if (vcthreads <= 0) vcthreads = 1; /* need at least one */

    for (i=0;i<vcthreads;i++) {
        return_code = tibrvDispatcher_Create(
            &myServer->vc_dispatchers[i],
            myServer->vc_queues);

        if (return_code != TIBRV_OK)
        {
            fprintf(stderr,
                    "%s: failed to create dispatcher thread: %s\n", program_name,
                    tibrvStatus_GetText(return_code));
        }
        sprintf(disp_name, "%s_%d", myServer->server_name, i);
        return_code = tibrvDispatcher_SetName(
            myServer->vc_dispatchers[i],
            disp_name);
    }


    /* Display a server-ready message. */
    fprintf(stderr, "Listening for client requests on subject %s.\n"
            "Wait time is %.0f secs.\n%s ready...\n",
            SEARCH_SUBJECT, SERVER_TIMEOUT, program_name);


    /* If this server remains idle for more than the timeout value, exit. */
    while (tibrvQueue_TimedDispatch(TIBRV_DEFAULT_QUEUE, SERVER_TIMEOUT) == TIBRV_OK);

    /* Destroy our Tibrv objects and close the Tibrv machinery. */
    tibrvEvent_Destroy(myServer->search_event);
    tibrvEvent_Destroy(request_event);
    tibrvTransport_Destroy(myServer->server_transport);
    free(myServer);
    tibrv_Close();

    exit(0);
}

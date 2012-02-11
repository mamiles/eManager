/*
 * Copyright 1999-2000 TIBCO Software Inc.
 * ALL RIGHTS RESERVED.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)Tibrvcm.xs	1.7
 */


#include "EXTERN.h"
#include "perl.h"
#include "XSUB.h"

#include <tibrv/cm.h>

typedef struct
{
    tibrvEvent        event;
    SV *              perl_cb_func;
    SV *              perl_on_complete_func;
    SV *              perl_cb_func_closure;
} perl_tibrvEvent, *plEvent;


static int
not_here(char *s)
{
    croak("%s not implemented on this architecture", s);
    return -1;
}

static double
constant(char *name, int arg)
{
    errno = 0;
    switch (*name) {
    case 'A':
	break;
    case 'B':
	break;
    case 'C':
	break;
    case 'D':
	break;
    case 'E':
	break;
    case 'F':
	break;
    case 'G':
	break;
    case 'H':
	break;
    case 'I':
	break;
    case 'J':
	break;
    case 'K':
	break;
    case 'L':
	break;
    case 'M':
	break;
    case 'N':
	break;
    case 'O':
	break;
    case 'P':
	break;
    case 'Q':
	break;
    case 'R':
	break;
    case 'S':
	break;
    case 'T':
	if (strEQ(name, "TIBRVCM_CANCEL"))
#ifdef TIBRVCM_CANCEL
	    return TIBRVCM_CANCEL;
#else
	    goto not_there;
#endif
	if (strEQ(name, "TIBRVCM_DEFAULT_ACCEPT_TIME"))
#ifdef TIBRVCM_DEFAULT_ACCEPT_TIME
	    return TIBRVCM_DEFAULT_ACCEPT_TIME;
#else
	    goto not_there;
#endif
	if (strEQ(name, "TIBRVCM_DEFAULT_COMPLETE_TIME"))
#ifdef TIBRVCM_DEFAULT_COMPLETE_TIME
	    return TIBRVCM_DEFAULT_COMPLETE_TIME;
#else
	    goto not_there;
#endif
	if (strEQ(name, "TIBRVCM_DEFAULT_SCHEDULER_ACTIVE"))
#ifdef TIBRVCM_DEFAULT_SCHEDULER_ACTIVE
	    return TIBRVCM_DEFAULT_SCHEDULER_ACTIVE;
#else
	    goto not_there;
#endif
	if (strEQ(name, "TIBRVCM_DEFAULT_SCHEDULER_HB"))
#ifdef TIBRVCM_DEFAULT_SCHEDULER_HB
	    return TIBRVCM_DEFAULT_SCHEDULER_HB;
#else
	    goto not_there;
#endif
	if (strEQ(name, "TIBRVCM_DEFAULT_SCHEDULER_WEIGHT"))
#ifdef TIBRVCM_DEFAULT_SCHEDULER_WEIGHT
	    return TIBRVCM_DEFAULT_SCHEDULER_WEIGHT;
#else
	    goto not_there;
#endif
	if (strEQ(name, "TIBRVCM_DEFAULT_TRANSPORT_TIMELIMIT"))
#ifdef TIBRVCM_DEFAULT_TRANSPORT_TIMELIMIT
	    return TIBRVCM_DEFAULT_TRANSPORT_TIMELIMIT;
#else
	    goto not_there;
#endif
	if (strEQ(name, "TIBRVCM_DEFAULT_WORKER_TASKS"))
#ifdef TIBRVCM_DEFAULT_WORKER_TASKS
	    return TIBRVCM_DEFAULT_WORKER_TASKS;
#else
	    goto not_there;
#endif
	if (strEQ(name, "TIBRVCM_DEFAULT_WORKER_WEIGHT"))
#ifdef TIBRVCM_DEFAULT_WORKER_WEIGHT
	    return TIBRVCM_DEFAULT_WORKER_WEIGHT;
#else
	    goto not_there;
#endif
	if (strEQ(name, "TIBRVCM_PERSIST"))
#ifdef TIBRVCM_PERSIST
	    return TIBRVCM_PERSIST;
#else
	    goto not_there;
#endif
	break;
    case 'U':
	break;
    case 'V':
	break;
    case 'W':
	break;
    case 'X':
	break;
    case 'Y':
	break;
    case 'Z':
	break;
    }
    errno = EINVAL;
    return 0;

not_there:
    errno = ENOENT;
    return 0;
}

void tibrvcmPerlEventCB( tibrvEvent event,
                         tibrvMsg   message,
		         void*      closure)
{
	dSP;
	perl_tibrvEvent   *perl_info = (plEvent)closure;


	ENTER;
	SAVETMPS;

	PUSHMARK(sp);
	EXTEND(sp, 3);
	PUSHs(sv_2mortal(newSViv((IV)perl_info)));
	PUSHs(sv_2mortal(newSViv((IV)message)));
	PUSHs(sv_2mortal(newSVsv(perl_info->perl_cb_func_closure)));
	PUTBACK;

	perl_call_sv(perl_info->perl_cb_func,G_DISCARD);

	SPAGAIN;

	FREETMPS;
	LEAVE;
}


void* tibrvcm_PerlReviewCB( tibrvcmTransport event,
                         const char * subject,
                         tibrvMsg   message,
		         void *      closure)
{
	dSP;
	perl_tibrvEvent   *perl_info = (plEvent)closure;
	int               count;
	void              *retval;

	ENTER;
	SAVETMPS;

	PUSHMARK(sp);
	EXTEND(sp, 4);

	PUSHs(sv_2mortal(newSViv((IV)event)));
	PUSHs(sv_2mortal(newSVpv((char*)subject, 0)));
	PUSHs(sv_2mortal(newSViv((IV)message)));
	PUSHs(sv_2mortal(newSVsv(perl_info->perl_cb_func_closure)));

	PUTBACK;

	count=perl_call_sv(perl_info->perl_cb_func,G_SCALAR);

	SPAGAIN;
	if(count !=1)
	    croak("Apply functions returned wrong number of arguments %d\n",
                   count);

	retval=(void*)POPi;

	PUTBACK;
	FREETMPS;
	LEAVE;

	return((void*)retval);
}


void tibrvcmPerlEventOnCompleteCB( tibrvEvent event,
                                 void*      closure)
{
	dSP;



	perl_tibrvEvent   *perl_info = (plEvent)closure;

	ENTER;
	SAVETMPS;

	PUSHMARK(sp);
	EXTEND(sp, 2);
	PUSHs(sv_2mortal(newSViv((IV)perl_info)));
	PUSHs(sv_2mortal(newSVsv(perl_info->perl_cb_func_closure)));
	PUTBACK;

	perl_call_sv(perl_info->perl_on_complete_func,G_DISCARD);

	SPAGAIN;

	FREETMPS;
	LEAVE;

}

MODULE = Tibrvcm		PACKAGE = Tibrvcm		
PROTOTYPES: ENABLE

double
constant(name,arg)
	char *		name
	int		arg



const char *
tibrvcm_Version()
CODE:
	RETVAL=tibrvcm_Version();
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_Create(cmTransport, transport, cmName, requestOld, ledgerName, syncLedger, relayAgent)
	tibrvcmTransport cmTransport
	tibrvTransport	 transport
	const char *	 cmName
	tibrv_bool	 requestOld
	const char *	 ledgerName
	tibrv_bool	 syncLedger
	const char *	 relayAgent
CODE:

	if( cmName && *cmName==0 )
	    cmName=NULL;

	if( ledgerName && *ledgerName==0 )
	    ledgerName=NULL;


	if( relayAgent && *relayAgent==0 )
	    relayAgent=NULL;

	RETVAL=tibrvcmTransport_Create( &cmTransport, transport,
	                                cmName, requestOld,
					ledgerName, syncLedger,
					relayAgent);
OUTPUT:
cmTransport
RETVAL


tibrv_status
tibrvcmTransport_Send(cmTransport, message)
	tibrvcmTransport	cmTransport
	tibrvMsg	message
CODE:
	RETVAL=tibrvcmTransport_Send( cmTransport, message );
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_SendRequest(cmTransport, message, reply, timeout)
	tibrvcmTransport	cmTransport
	tibrvMsg	message
	tibrvMsg 	reply
	tibrv_f64	timeout
CODE:
	RETVAL=tibrvcmTransport_SendRequest( cmTransport, message,
	                                     &reply, timeout );
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_SendReply(cmTransport, message, requestMessage)
	tibrvcmTransport cmTransport
	tibrvMsg	 message
	tibrvMsg	 requestMessage
CODE:
	RETVAL=tibrvcmTransport_SendReply( cmTransport,
	                                   message, requestMessage );
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_GetTransport(cmTransport, transport)
	tibrvcmTransport	cmTransport
	tibrvTransport   	transport
CODE:
	RETVAL=tibrvcmTransport_GetTransport( cmTransport, &transport );
OUTPUT:
transport
RETVAL

tibrv_status
tibrvcmTransport_GetName(cmTransport, cmName)
	tibrvcmTransport	cmTransport
	const char *	cmName
CODE:
	printf("cmTransport=%d\n",cmTransport);
	RETVAL=tibrvcmTransport_GetName( cmTransport, &cmName );
OUTPUT:
cmName
RETVAL

tibrv_status
tibrvcmTransport_GetRelayAgent(cmTransport, relayAgent)
	tibrvcmTransport	cmTransport
	const char *	relayAgent
CODE:
	RETVAL=tibrvcmTransport_GetRelayAgent( cmTransport, &relayAgent );
OUTPUT:
relayAgent
RETVAL

tibrv_status
tibrvcmTransport_GetLedgerName(cmTransport, ledgerName)
	tibrvcmTransport	cmTransport
	const char *	ledgerName
CODE:
	RETVAL=tibrvcmTransport_GetLedgerName( cmTransport, &ledgerName );
OUTPUT:
ledgerName
RETVAL

tibrv_status
tibrvcmTransport_GetSyncLedger(cmTransport, syncLedger)
	tibrvcmTransport	cmTransport
	tibrv_bool 	syncLedger
CODE:
	RETVAL=tibrvcmTransport_GetSyncLedger( cmTransport, &syncLedger );
OUTPUT:
syncLedger
RETVAL

tibrv_status
tibrvcmTransport_GetRequestOld(cmTransport, requestOld)
	tibrvcmTransport	cmTransport
	tibrv_bool 	requestOld
CODE:
	RETVAL=tibrvcmTransport_GetRequestOld( cmTransport,
	                                       &requestOld);
OUTPUT:
requestOld
RETVAL

tibrv_status
tibrvcmTransport_AllowListener(cmTransport, cmName)
	tibrvcmTransport	cmTransport
	const char *	cmName
CODE:
	RETVAL=tibrvcmTransport_AllowListener( cmTransport, cmName );
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_DisallowListener(cmTransport, cmName)
	tibrvcmTransport	cmTransport
	const char *	cmName
CODE:
	RETVAL=tibrvcmTransport_DisallowListener( cmTransport, cmName );
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_AddListener(cmTransport, cmName, subject)
	tibrvcmTransport	cmTransport
	const char *	cmName
	const char *	subject
CODE:
	RETVAL=tibrvcmTransport_AddListener(cmTransport, cmName, subject );
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_RemoveListener(cmTransport, cmName, subject)
	tibrvcmTransport	cmTransport
	const char *	cmName
	const char *	subject
CODE:
	RETVAL=tibrvcmTransport_RemoveListener( cmTransport, cmName,
	                                        subject);
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_RemoveSendState(cmTransport, subject)
	tibrvcmTransport	cmTransport
	const char *	subject
CODE:
	RETVAL=tibrvcmTransport_RemoveSendState( cmTransport, subject );
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_SyncLedger(cmTransport)
	tibrvcmTransport	cmTransport
CODE:
	RETVAL=tibrvcmTransport_SyncLedger(cmTransport);
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_ConnectToRelayAgent(cmTransport)
	tibrvcmTransport	cmTransport
CODE:
	RETVAL=tibrvcmTransport_ConnectToRelayAgent( cmTransport );
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_DisconnectFromRelayAgent(cmTransport)
	tibrvcmTransport	cmTransport
CODE:
	RETVAL=tibrvcmTransport_DisconnectFromRelayAgent( cmTransport );
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_Destroy(cmTransport)
	tibrvcmTransport	cmTransport
CODE:
	RETVAL=tibrvcmTransport_Destroy(cmTransport);
OUTPUT:
RETVAL

tibrv_status
tibrvcmEvent_CreateListener(event, queue, callback, transport, subject, closure)
	plEvent 	event
	tibrvQueue	queue
	SV* 	        callback
	tibrvTransport	transport
	const char *	subject
	SV*	        closure
CODE:
	plEvent tibrv_event_id;

	tibrv_event_id=(plEvent)calloc(sizeof(perl_tibrvEvent),1);
	
	if( tibrv_event_id )
        {
            RETVAL=tibrvcmEvent_CreateListener(&tibrv_event_id->event, queue, 
                                               tibrvcmPerlEventCB, 
                                               transport,subject, 
		    			       (void*)tibrv_event_id);
            if( RETVAL == TIBRV_OK )
            {
                tibrv_event_id->perl_cb_func         = newSVsv(callback);
		tibrv_event_id->perl_cb_func_closure = newSVsv(closure);
            }
	    event = tibrv_event_id;
        }
OUTPUT:
event
RETVAL


tibrv_status
tibrvcmEvent_GetQueue(event, queue)
	plEvent	        event
	tibrvQueue 	queue
CODE:
	RETVAL=tibrvcmEvent_GetQueue( event->event, &queue );
OUTPUT:
queue
RETVAL

tibrv_status
tibrvcmEvent_GetListenerSubject(event, subject)
	plEvent	event
	const char *	subject
CODE:
	RETVAL=tibrvcmEvent_GetListenerSubject( event->event, &subject );
OUTPUT:
subject
RETVAL

tibrv_status
tibrvcmEvent_GetListenerTransport(event, transport)
	plEvent	 event
	tibrvcmTransport transport
CODE:
	RETVAL=tibrvcmEvent_GetListenerTransport( event->event, &transport);
OUTPUT:
transport
RETVAL

tibrv_status
tibrvcmEvent_SetExplicitConfirm(cmListener)
	plEvent	cmListener
CODE:
	RETVAL=tibrvcmEvent_SetExplicitConfirm(cmListener->event);
OUTPUT:
RETVAL

tibrv_status
tibrvcmEvent_ConfirmMsg(cmListener, message)
	plEvent 	cmListener
	tibrvMsg	message
CODE:
	RETVAL=tibrvcmEvent_ConfirmMsg(cmListener->event,message);
OUTPUT:
RETVAL

tibrv_status
tibrvcmEvent_Destroy(cmListener, cancelAgreements)
	plEvent	cmListener
	tibrv_bool	cancelAgreements
CODE:
	RETVAL=tibrvcmEvent_Destroy(cmListener->event, cancelAgreements);
	if (RETVAL == TIBRV_OK)
	{
	    SvREFCNT_dec(cmListener->perl_cb_func);
	    SvREFCNT_dec(cmListener->perl_cb_func_closure);

	    free(cmListener);
	}
OUTPUT:
RETVAL

tibrv_status
tibrvcmEvent_DestroyEx(cmListener, cancelAgreements, completeCallback)
	plEvent	        cmListener
	tibrv_bool	cancelAgreements
	SV* 	        completeCallback
CODE:
	cmListener->perl_on_complete_func = newSVsv(completeCallback);

	RETVAL=tibrvcmEvent_DestroyEx(cmListener->event, 
	                            cancelAgreements,
				    tibrvcmPerlEventOnCompleteCB);

	if (RETVAL == TIBRV_OK)
	{
	    SvREFCNT_dec(cmListener->perl_cb_func);
	    SvREFCNT_dec(cmListener->perl_cb_func_closure);
	    SvREFCNT_dec(cmListener->perl_on_complete_func);

	    free(cmListener);
	}
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_GetCMSender(message, senderName)
	tibrvMsg	message
	const char *	senderName
CODE:
	RETVAL=tibrvMsg_GetCMSender(message, &senderName);
OUTPUT:
senderName
RETVAL

tibrv_status
tibrvMsg_GetCMSequence(message, sequenceNumber)
	tibrvMsg	message
	tibrv_u64 	sequenceNumber
CODE:
	RETVAL=tibrvMsg_GetCMSequence(message, &sequenceNumber);
OUTPUT:
sequenceNumber
RETVAL

tibrv_status
tibrvMsg_GetCMTimeLimit(message, timeLimit)
	tibrvMsg	message
	tibrv_f64 	timeLimit
CODE:
	RETVAL=tibrvMsg_GetCMTimeLimit( message, &timeLimit );
OUTPUT:
timeLimit
RETVAL

tibrv_status
tibrvMsg_SetCMTimeLimit(message, timeLimit)
	tibrvMsg	message
	tibrv_f64	timeLimit
CODE:
	RETVAL=tibrvMsg_SetCMTimeLimit(message, timeLimit);
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_GetDefaultCMTimeLimit(cmTransport, timeLimit)
	tibrvcmTransport	cmTransport
	tibrv_f64 	timeLimit
CODE:
	RETVAL=tibrvcmTransport_GetDefaultCMTimeLimit( cmTransport,
						       &timeLimit );
OUTPUT:
timeLimit
RETVAL

tibrv_status
tibrvcmTransport_SetDefaultCMTimeLimit(cmTransport, timeLimit)
	tibrvcmTransport	cmTransport
	tibrv_f64	timeLimit
CODE:
	RETVAL=tibrvcmTransport_SetDefaultCMTimeLimit( cmTransport,
	                                               timeLimit );
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_ReviewLedger(cmTransport, callback, subject, closure)
	tibrvcmTransport	cmTransport
	SV*	callback
	const char *	subject
	SV*	closure
CODE:
    perl_tibrvEvent    perl_ApplyArg;


    if( subject && *subject == 0 )
	subject = NULL;

    perl_ApplyArg.perl_cb_func          = callback;
    perl_ApplyArg.perl_cb_func_closure  = closure;

    RETVAL = tibrvcmTransport_ReviewLedger(cmTransport,
                                           tibrvcm_PerlReviewCB, 
					   subject,
                                           (void*)&perl_ApplyArg);
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_CreateDistributedQueue(cmTransport, transport, cmName)
	tibrvcmTransport cmTransport
	tibrvTransport	 transport
	const char *	 cmName
CODE:
	RETVAL=tibrvcmTransport_CreateDistributedQueue(&cmTransport,
	                                               transport,
                                                       cmName);
OUTPUT:
cmTransport
RETVAL

tibrv_status
tibrvcmTransport_CreateDistributedQueueEx(cmTransport, transport, cmName, workerWeight, workerTasks, schedulerWeight, schedulerHeartbeat, schedulerActivation)
	tibrvcmTransport cmTransport
	tibrvTransport	 transport
	const char *	 cmName
	tibrv_u32	 workerWeight
	tibrv_u32	 workerTasks
	tibrv_u16	 schedulerWeight
	tibrv_f64	 schedulerHeartbeat
	tibrv_f64	 schedulerActivation
CODE:
	RETVAL=tibrvcmTransport_CreateDistributedQueueEx(&cmTransport,
	                                                 transport,
                                                         cmName,
                                                         workerWeight,
							 workerTasks,
							 schedulerWeight,
							 schedulerHeartbeat,
							 schedulerActivation);
OUTPUT:
cmTransport
RETVAL



tibrv_status
tibrvcmTransport_GetCompleteTime(cmTransport, completeTime)
	tibrvcmTransport	cmTransport
	tibrv_f64 	completeTime
CODE:
	RETVAL=tibrvcmTransport_GetCompleteTime( cmTransport, &completeTime);
OUTPUT:
completeTime
RETVAL

tibrv_status
tibrvcmTransport_SetWorkerWeight(cmTransport, listenerWeight)
	tibrvcmTransport	cmTransport
	tibrv_u32	listenerWeight
CODE:
	RETVAL=tibrvcmTransport_SetWorkerWeight(cmTransport, listenerWeight);
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_GetWorkerWeight(cmTransport, listenerWeight)
	tibrvcmTransport	cmTransport
	tibrv_u32 	listenerWeight
CODE:
	RETVAL=tibrvcmTransport_GetWorkerWeight( cmTransport, &listenerWeight);
OUTPUT:
listenerWeight
RETVAL

tibrv_status
tibrvcmTransport_SetWorkerTasks(cmTransport, listenerTasks)
	tibrvcmTransport	cmTransport
	tibrv_u32	listenerTasks
CODE:
	RETVAL=tibrvcmTransport_SetWorkerTasks(cmTransport, listenerTasks);
OUTPUT:
RETVAL

tibrv_status
tibrvcmTransport_GetWorkerTasks(cmTransport, listenerTasks)
	tibrvcmTransport	cmTransport
	tibrv_u32 	listenerTasks
CODE:
	RETVAL=tibrvcmTransport_GetWorkerTasks(cmTransport , &listenerTasks);
OUTPUT:
listenerTasks
RETVAL

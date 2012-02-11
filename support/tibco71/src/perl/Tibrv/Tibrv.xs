/*
 * Copyright 1999-2002 TIBCO Software Inc.
 * ALL RIGHTS RESERVED.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)Tibrv.xs	1.22
 */


#include "EXTERN.h"
#include "perl.h"
#include "XSUB.h"

#include <tibrv/tibrv.h>

typedef struct
{
    IV         sec;
    UV         nsec;
} perl_tibrvMsgDateTime;

typedef struct
{
    const char*                 name;
    tibrv_u32                   size;
    tibrv_u32                   count;
    tibrv_u16                   id;
    tibrv_u8                    type;

} perl_tibrvMsgField;

/*************************************************************************/
/*                          support functions                            */
/*************************************************************************/

void buildHashFromField(HV *h, tibrvMsgField field)
{
        perl_tibrvMsgDateTime p_dt;
	SV* data;

        hv_store(h, "name",  strlen("name"),  newSVpv(field.name,0), 0);
        hv_store(h, "size",  strlen("size"),  newSViv(field.size),   0);
        hv_store(h, "count", strlen("count"), newSViv(field.count),  0);
        hv_store(h, "id",    strlen("id"),    newSViv(field.id),     0);
        hv_store(h, "type",  strlen("type"),  newSViv(field.type),   0);

	switch ( field.type )
        {
	case TIBRVMSG_MSG:
            data = newSViv((int) field.data.msg);
	    break;
	case TIBRVMSG_DATETIME:
	    p_dt.sec  = field.data.date.sec;
	    p_dt.nsec = field.data.date.nsec;
	    data = newSVpvn((char*)&p_dt, sizeof(p_dt));
	    break;
	case TIBRVMSG_OPAQUE:
            data = newSVpvn((char*)field.data.buf,field.size);
	    break;
	case TIBRVMSG_XML:
            data = newSVpvn((char*)field.data.buf,field.size);
	    break;
	case TIBRVMSG_STRING:
            data = newSVpvn(field.data.str,field.size);
	    break;
	case TIBRVMSG_BOOL:
  	    data = newSViv(field.data.boolean);
	    break;
        case TIBRVMSG_I8:
  	    data = newSViv(field.data.i8);
	    break;
        case TIBRVMSG_U8:
  	    data = newSViv(field.data.u8);
	    break;
        case TIBRVMSG_I16:
  	    data = newSViv(field.data.i16);
	    break;
        case TIBRVMSG_U16:
  	    data = newSViv(field.data.u16);
	    break;
        case TIBRVMSG_I32:
  	    data = newSViv(field.data.i32);
	    break;
        case TIBRVMSG_U32:
  	    data = newSViv(field.data.u32);
	    break;
        case TIBRVMSG_I64:
  	    data = newSViv(field.data.i64);
	    break;
        case TIBRVMSG_U64:
  	    data = newSViv(field.data.u64);
	    break;
        case TIBRVMSG_F32:
  	    data = newSVnv(field.data.f32);
	    break;
        case TIBRVMSG_F64:
  	    data = newSVnv(field.data.f64);
	    break;
        case TIBRVMSG_I8ARRAY:
        case TIBRVMSG_U8ARRAY:
        case TIBRVMSG_I16ARRAY:
        case TIBRVMSG_U16ARRAY: 
        case TIBRVMSG_I32ARRAY:
        case TIBRVMSG_U32ARRAY:
        case TIBRVMSG_I64ARRAY:
        case TIBRVMSG_U64ARRAY:
        case TIBRVMSG_F32ARRAY:
        case TIBRVMSG_F64ARRAY:    
            /* for now pass it back as a buf... */
            data = newSVpvn((const char*)field.data.array,field.size);
	    break;
        }

    hv_store(h, "data",  strlen("data"), data, 0);
}


/*************************************************************************/
/*                          callback functions                           */
/*************************************************************************/
typedef struct
{
    tibrvEvent        event;
    SV *              perl_cb_func;
    SV *              perl_on_complete_func;
    SV *              perl_cb_func_closure;
} perl_tibrvEvent, *plEvent;


void _tibrvPerlEventCompleteCB( tibrvEvent event,
			        void*      closure)
{
    perl_tibrvEvent   *perl_event = closure;

    SvREFCNT_dec(perl_event->perl_cb_func);
    SvREFCNT_dec(perl_event->perl_cb_func_closure);
    perl_event->event = TIBRV_INVALID_ID;
}

void tibrvPerlEventCB( tibrvEvent event,
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


void tibrvPerlEventOnCompleteCB( tibrvEvent event,
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

MODULE = Tibrv		PACKAGE = Tibrv

PROTOTYPES: ENABLE

#/***************************************************************************/
#/*                           tibrv.h functions                             */
#/***************************************************************************/

char *
tibrv_Version()
CODE:
{
     RETVAL=(char*)tibrv_Version();
}
OUTPUT:
RETVAL


tibrv_status
tibrv_Open()
CODE:
    RETVAL=tibrv_Open();
OUTPUT:
RETVAL

tibrv_status
tibrv_Close()
CODE:
    RETVAL=tibrv_Close();
OUTPUT:
RETVAL

#/***************************************************************************/
#/*                           tibrv/events.h functions                      */
#/***************************************************************************/

tibrv_status
tibrvEvent_CreateListener(event, queue, callback, transport, subject, closure)
	plEvent 	event
	tibrvQueue	queue
	SV* 	        callback
	tibrvTransport	transport
	const char *	subject
	SV*	        closure
CODE:
	perl_tibrvEvent *tibrv_event_id;

	tibrv_event_id=(plEvent)calloc(sizeof(perl_tibrvEvent),1);
	
	if( tibrv_event_id )
        {
            RETVAL=tibrvEvent_CreateListener(&tibrv_event_id->event, queue, 
                                             tibrvPerlEventCB, 
                                             transport,subject, 
		    			     (void*)tibrv_event_id);
            if( RETVAL == TIBRV_OK )
            {
                tibrv_event_id->perl_cb_func         = newSVsv(callback);
		tibrv_event_id->perl_cb_func_closure = newSVsv(closure);
            }
	    event = tibrv_event_id;
        }
	else
            RETVAL=TIBRV_NO_MEMORY;
OUTPUT:
event
RETVAL

tibrv_status
tibrvEvent_CreateTimer(event, queue, callback, interval, closure)
	plEvent 	event
	tibrvQueue	queue
	SV *	        callback
	tibrv_f64	interval
	SV *	closure
CODE:
	perl_tibrvEvent *tibrv_event_id;

	tibrv_event_id=(plEvent)calloc(sizeof(perl_tibrvEvent),1);

	if( tibrv_event_id )
        {
	    RETVAL=tibrvEvent_CreateTimer(&tibrv_event_id->event, 
                                          queue,tibrvPerlEventCB,
	                                  interval, (void*)tibrv_event_id);
            if( RETVAL == TIBRV_OK )
	    {
	        tibrv_event_id->perl_cb_func         = newSVsv(callback);
		tibrv_event_id->perl_cb_func_closure = newSVsv(closure);
	    }
	    event = tibrv_event_id;
        }
	else
	    RETVAL=TIBRV_NO_MEMORY;
OUTPUT:
event
RETVAL

tibrv_status
tibrvEvent_CreateIO(event, queue, callback, socketId, ioType, closure)
	plEvent 	event
	tibrvQueue	queue
	SV *	        callback
	tibrv_i32	socketId
	tibrvIOType	ioType
	SV *	        closure
CODE:
	perl_tibrvEvent *tibrv_event_id;
	tibrv_event_id=(plEvent)calloc(sizeof(perl_tibrvEvent),1);

	if( tibrv_event_id )
        {
	    RETVAL=tibrvEvent_CreateIO(&tibrv_event_id->event, queue, 
                                       tibrvPerlEventCB, 
	                               socketId, ioType,
				       (void*)tibrv_event_id);
	    if( RETVAL == TIBRV_OK )
	    {
	        tibrv_event_id->perl_cb_func          = newSVsv(callback);
		tibrv_event_id->perl_cb_func_closure  = newSVsv(closure);
	    }
	    event = tibrv_event_id;
       }
       else
            RETVAL=TIBRV_NO_MEMORY;
OUTPUT:
event
RETVAL

tibrv_status
tibrvEvent_Destroy(event)
	plEvent	event
CODE:
	perl_tibrvEvent *perl_event=event;   

        RETVAL=tibrvEvent_DestroyEx(perl_event->event, 
				    _tibrvPerlEventCompleteCB);

	if ( RETVAL == TIBRV_OK )
	{

	    perl_event->event = TIBRV_INVALID_ID;
	}
OUTPUT:
RETVAL

tibrv_status
tibrvEvent_GetType(event, type)
	plEvent	event
	tibrvEventType 	type
CODE:
	perl_tibrvEvent* perl_event=event;
	RETVAL=tibrvEvent_GetType(perl_event->event, &type);
OUTPUT:
type
RETVAL

tibrv_status
tibrvEvent_GetQueue(event, queue)
	plEvent	event
	tibrvQueue 	queue
CODE:
	perl_tibrvEvent *perl_event=event;
	RETVAL=tibrvEvent_GetQueue(perl_event->event, &queue);
OUTPUT:
queue
RETVAL

tibrv_status
tibrvEvent_GetListenerSubject(event, subject)
	plEvent	event
	const char *	subject
CODE:
	perl_tibrvEvent *perl_event=event;
	RETVAL=tibrvEvent_GetListenerSubject(perl_event->event, &subject);
OUTPUT:
subject
RETVAL

tibrv_status
tibrvEvent_GetListenerTransport(event, transport)
	plEvent	event
	tibrvTransport 	transport
CODE:
	perl_tibrvEvent *perl_event=event;
	RETVAL=tibrvEvent_GetListenerTransport(perl_event->event, &transport);
OUTPUT:
transport
RETVAL

tibrv_status
tibrvEvent_GetTimerInterval(event, interval)
	plEvent	event
	tibrv_f64 	interval
CODE:
	perl_tibrvEvent *perl_event=event;
	RETVAL=tibrvEvent_GetTimerInterval(perl_event->event, &interval);
OUTPUT:
interval
RETVAL

tibrv_status
tibrvEvent_ResetTimerInterval(event, newInterval)
	plEvent	event
	tibrv_f64	newInterval
CODE:
	perl_tibrvEvent *perl_event=event;
	RETVAL=tibrvEvent_ResetTimerInterval(perl_event->event, newInterval);
OUTPUT:
RETVAL

tibrv_status
tibrvEvent_GetIOSource(event, source)
	plEvent	event
	tibrv_i32 	source
CODE:
	perl_tibrvEvent *perl_event=event;
	RETVAL=tibrvEvent_GetIOSource(perl_event->event, &source);
OUTPUT:
source
RETVAL

tibrv_status
tibrvEvent_GetIOType(event, ioType)
	plEvent	event
	tibrvIOType 	ioType
CODE:
	perl_tibrvEvent *perl_event=event;
	RETVAL=tibrvEvent_GetIOType(perl_event->event, &ioType);
OUTPUT:
ioType
RETVAL



#/***************************************************************************/
#/*                           tibrv/status.h functions                      */
#/***************************************************************************/

const char *
tibrvStatus_GetText(status)
	tibrv_status	status
CODE:
    RETVAL=tibrvStatus_GetText(status);
OUTPUT:
RETVAL

#/***************************************************************************/
#/*                           tibrv/msg.h functions                        */
#/***************************************************************************/

tibrv_status
tibrvMsg_Create(message)
	tibrvMsg 	message
CODE:
	RETVAL=tibrvMsg_Create( &message );
OUTPUT:
message
RETVAL

tibrv_status
tibrvMsg_CreateEx(message, initialStorage)
	tibrvMsg 	message
	tibrv_u32	initialStorage
CODE:
	RETVAL=tibrvMsg_CreateEx(&message, initialStorage);
OUTPUT:
message
RETVAL

tibrv_status
tibrvMsg_Destroy(message)
	tibrvMsg	message
CODE:
	RETVAL=tibrvMsg_Destroy(message);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_Detach(message)
	tibrvMsg	message
CODE:
	RETVAL=tibrvMsg_Detach(message);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_Reset(message)
	tibrvMsg	message
CODE:
	RETVAL=tibrvMsg_Reset(message);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_Expand(message, additionalStorage)
	tibrvMsg	message
	tibrv_i32	additionalStorage
CODE:
	RETVAL=tibrvMsg_Expand( message, additionalStorage );
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_SetSendSubject(message, subject)
	tibrvMsg	message
	const char *	subject
CODE:
    RETVAL=tibrvMsg_SetSendSubject(message, subject);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_GetSendSubject(message, subject)
	tibrvMsg	message
	const char *	subject
CODE:
	RETVAL=tibrvMsg_GetSendSubject(message, &subject);
OUTPUT:
subject
RETVAL

tibrv_status
tibrvMsg_SetReplySubject(message, replySubject)
	tibrvMsg	message
	const char *	replySubject
CODE:
	RETVAL=tibrvMsg_SetReplySubject(message, replySubject);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_GetReplySubject(message, replySubject)
	tibrvMsg	message
	const char *	replySubject
CODE:
	RETVAL=tibrvMsg_GetReplySubject(message, &replySubject);
OUTPUT:
replySubject
RETVAL

tibrv_status
tibrvMsg_GetNumFields(message, numFields)
	tibrvMsg	message
	tibrv_u32 	numFields
CODE:
	RETVAL=tibrvMsg_GetNumFields(message, &numFields);
OUTPUT:
numFields
RETVAL

tibrv_status
tibrvMsg_GetByteSize(message, byteSize)
	tibrvMsg	message
	tibrv_u32 	byteSize
CODE:
	RETVAL=tibrvMsg_GetByteSize(message, &byteSize);
OUTPUT:
byteSize
RETVAL

tibrv_status
tibrvMsg_ConvertToString(message, string)
	tibrvMsg	message
	const char *	string
CODE:
    RETVAL=tibrvMsg_ConvertToString(message, &string);
OUTPUT:
string
RETVAL


tibrv_status
tibrvMsg_CreateFromBytes(message, bytes)
	tibrvMsg 	message
	void *	bytes
CODE:
	RETVAL=tibrvMsg_CreateFromBytes(&message, bytes);
OUTPUT:
message
RETVAL

tibrv_status
tibrvMsg_GetAsBytes(message, bytePtr)
	tibrvMsg	message
	void *	bytePtr
CODE:
	RETVAL=tibrvMsg_GetAsBytes(message, &bytePtr);
OUTPUT:
bytePtr
RETVAL


tibrv_status
tibrvMsg_CreateCopy(message, copy)
	tibrvMsg	message
	tibrvMsg 	copy
CODE:
	RETVAL=tibrvMsg_CreateCopy(message, &copy);
OUTPUT:
copy
RETVAL


#/***************************************************************************/
#/*                           tibrv/msgx.h functions                        */
#/***************************************************************************/


tibrv_status
tibrvMsg_AddMsgEx(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrvMsg	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddMsgEx( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_AddIPAddr32Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_ipaddr32	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddIPAddr32Ex( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_AddIPPort16Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_ipport16	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddIPPort16Ex( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_AddDateTimeEx(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	SV *	value
	tibrv_u16	optIdentifier
CODE:
    {
	tibrvMsgDateTime       c_dt;
	STRLEN svlen;

	perl_tibrvMsgDateTime *p_dt = (perl_tibrvMsgDateTime*)SvPV(value,svlen);

	if( svlen == 2*sizeof(IV))
        {
	 c_dt.sec = p_dt->sec;
	 c_dt.nsec = p_dt->nsec;

	 RETVAL=tibrvMsg_AddDateTimeEx( message, fieldName, 
                                           &c_dt, optIdentifier );
        }
	else
	{
	    RETVAL=TIBRV_INVALID_ARG;
	}
    }
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_AddBoolEx(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_bool	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddBoolEx( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_AddI8Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_i8 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddI8Ex( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_AddU8Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u8	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddU8Ex( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_AddI16Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_i16	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddI16Ex( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_AddU16Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u16	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddU16Ex( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_AddI32Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_i32	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddI32Ex( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_AddU32Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u32	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddU32Ex( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_AddI64Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_i64	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddI64Ex( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_AddU64Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u64	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddU64Ex( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_AddF32Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_f32	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddF32Ex( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_AddF64Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_f64	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddF64Ex( message, fieldName, value, optIdentifier );
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_AddStringEx(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	const char *	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddStringEx(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_AddOpaqueEx(message, fieldName, value, size, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	SV *	        value
	tibrv_u32	size
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddOpaqueEx(message, fieldName,SvPV(value,PL_na), 
	                            size,optIdentifier);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_AddXmlEx(message, fieldName, value, size, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	SV *	        value
	tibrv_u32	size
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_AddXmlEx(message, fieldName,SvPV(value,PL_na), 
	                            size,optIdentifier);
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_GetMsgEx(message, fieldName, subMessage, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrvMsg 	subMessage
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetMsgEx(message, fieldName, &subMessage, optIdentifier);
OUTPUT:
subMessage
RETVAL

tibrv_status
tibrvMsg_GetIPAddr32Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_ipaddr32 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetIPAddr32Ex(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL


tibrv_status
tibrvMsg_GetIPPort16Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_ipport16 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetIPPort16Ex(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL

tibrv_status
tibrvMsg_GetDateTimeEx(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	SV *            value
	tibrv_u16	optIdentifier
CODE:
    {
        perl_tibrvMsgDateTime p_dt;
	tibrvMsgDateTime      c_dt;


	RETVAL=tibrvMsg_GetDateTimeEx( message, fieldName, &c_dt, optIdentifier);
	if( RETVAL == TIBRV_OK )
	{
	    p_dt.sec = c_dt.sec;
	    p_dt.nsec = c_dt.nsec;

	    sv_setpvn(value, (char *)&p_dt, sizeof(p_dt));
	}
    }
OUTPUT:
value
RETVAL


tibrv_status
tibrvMsg_GetBoolEx(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_bool 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetBoolEx(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL

tibrv_status
tibrvMsg_GetI8Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_i8 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetI8Ex(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL


tibrv_status
tibrvMsg_GetU8Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u8 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetU8Ex(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL


tibrv_status
tibrvMsg_GetI16Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_i16 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetI16Ex(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL


tibrv_status
tibrvMsg_GetU16Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u16 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetU16Ex(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL


tibrv_status
tibrvMsg_GetI32Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_i32 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetI32Ex(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL


tibrv_status
tibrvMsg_GetU32Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u32 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetU32Ex(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL


tibrv_status
tibrvMsg_GetI64Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_i64 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetI64Ex(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL

tibrv_status
tibrvMsg_GetU64Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u64 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetU64Ex(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL


tibrv_status
tibrvMsg_GetF32Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_f32 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetF32Ex(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL


tibrv_status
tibrvMsg_GetF64Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_f64 	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetF64Ex(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL


tibrv_status
tibrvMsg_GetStringEx(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	const char *	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_GetStringEx(message, fieldName, &value, optIdentifier);
OUTPUT:
value
RETVAL


tibrv_status
tibrvMsg_GetOpaqueEx(message, fieldName, value, size, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	SV *	        value
	tibrv_u32 	size
	tibrv_u16	optIdentifier
CODE:
{
	const void *c_value;
	RETVAL=tibrvMsg_GetOpaqueEx(message, fieldName, &c_value, &size, 
	                            optIdentifier);
				    
        sv_setpvn(value, (const char *)c_value, size);
}
OUTPUT:
value
size
RETVAL

tibrv_status
tibrvMsg_GetXmlEx(message, fieldName, value, size, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	SV *	        value
	tibrv_u32 	size
	tibrv_u16	optIdentifier
CODE:
{
	const void *c_value;
	RETVAL=tibrvMsg_GetXmlEx(message, fieldName, &c_value, &size, 
	                            optIdentifier);
				    
        sv_setpvn(value, (const char *)c_value, size);
}
OUTPUT:
value
size
RETVAL


tibrv_status
tibrvMsg_RemoveFieldEx(message, fieldName, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_RemoveFieldEx( message, fieldName, optIdentifier );
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_RemoveFieldInstance(message, fieldName, instance)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u32	instance
CODE:
	RETVAL=tibrvMsg_RemoveFieldInstance( message, fieldName, instance );
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_UpdateMsgEx(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrvMsg	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_UpdateMsgEx(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_UpdateIPAddr32Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_ipaddr32	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_UpdateIPAddr32Ex(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_UpdateIPPort16Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_ipport16	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_UpdateIPPort16Ex(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_UpdateDateTimeEx(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	SV *	value
	tibrv_u16	optIdentifier
CODE:
{
	tibrvMsgDateTime       c_dt;
	STRLEN svlen;
	perl_tibrvMsgDateTime *p_dt = (perl_tibrvMsgDateTime*)SvPV(value,svlen);

	if( svlen == 2*sizeof(IV))
        {
	    c_dt.sec = p_dt->sec;
	    c_dt.nsec = p_dt->nsec;

	    RETVAL=tibrvMsg_UpdateDateTimeEx( message, fieldName, 
                                              &c_dt, optIdentifier );
        }
}
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_UpdateBoolEx(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_bool	value
	tibrv_u16	optIdentifier
CODE:
RETVAL=tibrvMsg_UpdateBoolEx(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_UpdateI8Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_i8	value
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_UpdateI8Ex(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_UpdateU8Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u8	value
	tibrv_u16	optIdentifier
CODE:
RETVAL=tibrvMsg_UpdateU8Ex(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_UpdateI16Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_i16	value
	tibrv_u16	optIdentifier
CODE:
RETVAL=tibrvMsg_UpdateI16Ex(message, fieldName, value, optIdentifier=0);
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_UpdateU16Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u16	value
	tibrv_u16	optIdentifier
CODE:
RETVAL=tibrvMsg_UpdateU16Ex(message, fieldName, value, optIdentifier=0);
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_UpdateI32Ex(message, fieldName, value, optIdentifier)
	tibrvMsg	message
	const char *	fieldName
	tibrv_i32	value
	tibrv_u16	optIdentifier
CODE:
RETVAL=tibrvMsg_UpdateI32Ex(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_UpdateU32Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u32	value
	tibrv_u16	optIdentifier
CODE:
RETVAL=tibrvMsg_UpdateU32Ex(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_UpdateI64Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_i64	value
	tibrv_u16	optIdentifier
CODE:
RETVAL=tibrvMsg_UpdateI64Ex(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_UpdateU64Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_u64	value
	tibrv_u16	optIdentifier
CODE:
RETVAL=tibrvMsg_UpdateU64Ex(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_UpdateF32Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_f32	value
	tibrv_u16	optIdentifier
CODE:
RETVAL=tibrvMsg_UpdateF32Ex(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_UpdateF64Ex(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	tibrv_f64	value
	tibrv_u16	optIdentifier
CODE:
RETVAL=tibrvMsg_UpdateF64Ex(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_UpdateStringEx(message, fieldName, value, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	const char *	value
	tibrv_u16	optIdentifier
CODE:
RETVAL=tibrvMsg_UpdateStringEx(message, fieldName, value, optIdentifier);
OUTPUT:
RETVAL


tibrv_status
tibrvMsg_UpdateOpaqueEx(message, fieldName, value, size, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	SV *	value
	tibrv_u32	size
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_UpdateOpaqueEx(message, fieldName, SvPV(value,PL_na), 
				       size, optIdentifier);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_UpdateXmlEx(message, fieldName, value, size, optIdentifier=0)
	tibrvMsg	message
	const char *	fieldName
	SV *	value
	tibrv_u32	size
	tibrv_u16	optIdentifier
CODE:
	RETVAL=tibrvMsg_UpdateXmlEx(message, fieldName, SvPV(value,PL_na), 
				       size, optIdentifier);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_MarkReferences(message)
	tibrvMsg       message
CODE:
RETVAL=tibrvMsg_MarkReferences(message);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_ClearReferences(message)
	tibrvMsg       message
CODE:
RETVAL=tibrvMsg_ClearReferences(message);
OUTPUT:
RETVAL

tibrv_status
tibrvMsg_GetCurrentTime(dateTime)
	SV *            dateTime
CODE:
    {
        perl_tibrvMsgDateTime p_dt;
	tibrvMsgDateTime      c_dt;
	SV * 		      rv;

	RETVAL=tibrvMsg_GetCurrentTime(  &c_dt );
	if( RETVAL == TIBRV_OK )
	{
	    p_dt.sec = c_dt.sec;
	    p_dt.nsec = c_dt.nsec;

	    sv_setpvn(dateTime, (char *)&p_dt, sizeof(p_dt));
	}
    }
OUTPUT:
dateTime
RETVAL


#/***************************************************************************/
#/*                         tibrv/queue.h functions                         */
#/***************************************************************************/

tibrv_status
tibrvQueue_Create(eventQueue)
	tibrvQueue 	eventQueue
CODE:
	RETVAL=tibrvQueue_Create(&eventQueue);
OUTPUT:
eventQueue
RETVAL

tibrv_status
tibrvQueue_TimedDispatch(eventQueue, waitTime)
	tibrvQueue	eventQueue
	tibrv_f64	waitTime
CODE:
	RETVAL=tibrvQueue_TimedDispatch(eventQueue, waitTime);
OUTPUT:
RETVAL

tibrv_status
tibrvQueue_Dispatch(eventQueue)
	tibrvQueue	eventQueue
CODE:
	RETVAL=tibrvQueue_TimedDispatch(eventQueue, TIBRV_WAIT_FOREVER);
OUTPUT:
RETVAL

tibrv_status
tibrvQueue_Poll(eventQueue)
	tibrvQueue	eventQueue
CODE:
	RETVAL=tibrvQueue_TimedDispatch(eventQueue, TIBRV_NO_WAIT);
OUTPUT:
RETVAL

tibrv_status
tibrvQueue_DestroyEx(eventQueue,completeCallback=NULL,closure=NULL)
	tibrvQueue	eventQueue
	SV *            completeCallback
	SV *            closure
CODE:
	if ( completeCallback == NULL )
        {
            RETVAL=tibrvQueue_DestroyEx(eventQueue,NULL,NULL);
        }
	else
        {
            plEvent on_compl;
	    on_compl=(plEvent)calloc(sizeof(perl_tibrvEvent),1);
            if ( on_compl )
	    {
                on_compl->perl_on_complete_func=newSVsv(completeCallback);
		on_compl->perl_cb_func_closure =newSVsv(closure);

                RETVAL=tibrvQueue_DestroyEx(eventQueue,
	                                    tibrvPerlEventOnCompleteCB,
				            on_compl);

            }
	    else
	        RETVAL=TIBRV_NO_MEMORY;
        }
OUTPUT:
RETVAL

tibrv_status
tibrvQueue_GetCount(eventQueue, numEvents)
	tibrvQueue	eventQueue
	tibrv_u32	numEvents
CODE:
	RETVAL=tibrvQueue_GetCount( eventQueue, &numEvents );
OUTPUT:
numEvents
RETVAL

tibrv_status
tibrvQueue_GetPriority(eventQueue, priority)
	tibrvQueue	eventQueue
	tibrv_u32 	priority
CODE:
	RETVAL=tibrvQueue_GetPriority( eventQueue, &priority );
OUTPUT:
priority
RETVAL

tibrv_status
tibrvQueue_SetPriority(eventQueue, priority)
	tibrvQueue	eventQueue
	tibrv_u32	priority
CODE:
	RETVAL=tibrvQueue_SetPriority( eventQueue, priority );
OUTPUT:
RETVAL

tibrv_status
tibrvQueue_GetLimitPolicy(eventQueue, policy, maxEvents, discardAmount)
	tibrvQueue	eventQueue
	tibrvQueueLimitPolicy 	policy
	tibrv_u32 	maxEvents
	tibrv_u32 	discardAmount
CODE:
	RETVAL=tibrvQueue_GetLimitPolicy( eventQueue, &policy, &maxEvents, 
	                                  &discardAmount );
OUTPUT:
policy
maxEvents
discardAmount
RETVAL

tibrv_status
tibrvQueue_SetLimitPolicy(eventQueue, policy, maxEvents, discardAmount)
	tibrvQueue	eventQueue
	tibrvQueueLimitPolicy	policy
	tibrv_u32	maxEvents
	tibrv_u32	discardAmount
CODE:
	RETVAL=tibrvQueue_SetLimitPolicy( eventQueue, policy, maxEvents, 
	                                  discardAmount );
OUTPUT:
RETVAL

tibrv_status
tibrvQueue_SetName(eventQueue, queueName)
	tibrvQueue	eventQueue
	const char *	queueName
CODE:
	RETVAL=tibrvQueue_SetName(eventQueue, queueName);
OUTPUT:
RETVAL

tibrv_status
tibrvQueue_GetName(eventQueue, queueName)
	tibrvQueue	eventQueue
	const char      *queueName
CODE:
	RETVAL=tibrvQueue_GetName(eventQueue, &queueName);
OUTPUT:
queueName
RETVAL


#/***************************************************************************/
#/*                         tibrv/qgroup.h functions                         */
#/***************************************************************************/

tibrv_status
tibrvQueueGroup_Create(eventQueueGroup)
	tibrvQueueGroup  eventQueueGroup
CODE:
	RETVAL=tibrvQueueGroup_Create( &eventQueueGroup );
OUTPUT:
eventQueueGroup
RETVAL

tibrv_status
tibrvQueueGroup_TimedDispatch(eventQueueGroup, waitTime)
	tibrvQueueGroup	eventQueueGroup
	tibrv_f64	waitTime
CODE:
	RETVAL=tibrvQueueGroup_TimedDispatch(eventQueueGroup,waitTime);
OUTPUT:
RETVAL

tibrv_status
tibrvQueueGroup_Dispatch(eventQueueGroup)
	tibrvQueueGroup	eventQueueGroup
CODE:
	RETVAL=tibrvQueueGroup_TimedDispatch(eventQueueGroup,
	                                     TIBRV_WAIT_FOREVER);
OUTPUT:
RETVAL

tibrv_status
tibrvQueueGroup_Poll(eventQueueGroup, waitTime)
	tibrvQueueGroup	eventQueueGroup
CODE:
	RETVAL=tibrvQueueGroup_TimedDispatch(eventQueueGroup,TIBRV_NO_WAIT);
OUTPUT:
RETVAL

tibrv_status
tibrvQueueGroup_Destroy(eventQueueGroup)
	tibrvQueueGroup	eventQueueGroup
CODE:
	RETVAL=tibrvQueueGroup_Destroy(eventQueueGroup);
OUTPUT:
RETVAL
	
tibrv_status
tibrvQueueGroup_Add(eventQueueGroup, eventQueue)
	tibrvQueueGroup	eventQueueGroup
	tibrvQueue	eventQueue
CODE:
	RETVAL=tibrvQueueGroup_Add(eventQueueGroup, eventQueue);
OUTPUT:
RETVAL


tibrv_status
tibrvQueueGroup_Remove(eventQueueGroup, eventQueue)
	tibrvQueueGroup	eventQueueGroup
	tibrvQueue	eventQueue
CODE:
	RETVAL=tibrvQueueGroup_Remove(eventQueueGroup, eventQueue);
OUTPUT:
RETVAL

#/***************************************************************************/
#/*                           tibrv/tport.h functions                       */
#/***************************************************************************/
tibrv_status
tibrvTransport_Create(transport, service=NULL, network=NULL, daemon=NULL)
	tibrvTransport 	transport
	const char *	service
	const char *	network
	const char *	daemon
CODE:
	if ( service && (strlen(service)==0))
	    service=NULL;

	if ( network && (strlen(network)==0))
	    network=NULL;

	if ( daemon && (strlen(daemon)==0))
	    daemon=NULL;

	RETVAL=tibrvTransport_Create( &transport,service,network,daemon);
OUTPUT:
transport
RETVAL


tibrv_status
tibrvTransport_Send(transport, message)
	tibrvTransport	transport
	tibrvMsg	message
CODE:
	RETVAL=tibrvTransport_Send(transport, message);
OUTPUT:
RETVAL

tibrv_status
tibrvTransport_SendRequest(transport, message, reply, timeout)
	tibrvTransport	transport
	tibrvMsg	message
	tibrvMsg 	reply
	tibrv_f64	timeout
CODE:
	RETVAL=tibrvTransport_SendRequest(transport, message,&reply,timeout);
OUTPUT:
reply
RETVAL

tibrv_status
tibrvTransport_SendReply(transport, message, requestMessage)
	tibrvTransport	transport
	tibrvMsg	message
	tibrvMsg	requestMessage
CODE:
	RETVAL=tibrvTransport_SendReply(transport, message,requestMessage);
OUTPUT:
RETVAL

tibrv_status
tibrvTransport_Destroy(transport)
	tibrvTransport	transport
CODE:
	RETVAL=tibrvTransport_Destroy(transport);
OUTPUT:
RETVAL


tibrv_status
tibrvTransport_CreateInbox(transport, subjectString, subjectLimit)
	tibrvTransport	transport
	SV *	subjectString
	tibrv_u32	subjectLimit
CODE:
	sv_setpvn( subjectString, "", 0 );
	sv_grow( subjectString, subjectLimit );
        RETVAL=tibrvTransport_CreateInbox( transport, 
                                           SvPV(subjectString, PL_na),
					   subjectLimit);
        SvCUR_set(subjectString,strlen(SvPV(subjectString,PL_na))); 
OUTPUT:
subjectString
RETVAL

tibrv_status
tibrvTransport_GetService(transport, serviceString)
	tibrvTransport	transport
	const char *	serviceString
CODE:
	RETVAL=tibrvTransport_GetService(transport, &serviceString);
OUTPUT:
serviceString
RETVAL

tibrv_status
tibrvTransport_GetNetwork(transport, networkString)
	tibrvTransport	transport
	const char *	networkString
CODE:
	RETVAL=tibrvTransport_GetNetwork(transport, &networkString);
OUTPUT:
networkString
RETVAL

tibrv_status
tibrvTransport_GetDaemon(transport, daemonString)
	tibrvTransport	transport
	const char *	daemonString
CODE:
	RETVAL=tibrvTransport_GetDaemon(transport, &daemonString);
OUTPUT:
daemonString
RETVAL

tibrv_status
tibrvTransport_SetDescription(transport, description)
        tibrvTransport transport
	const char *   description
CODE:
        RETVAL=tibrvTransport_SetDescription( transport, description);
OUTPUT:
RETVAL

tibrv_status
tibrvTransport_GetDescription(transport, description)
        tibrvTransport transport
	const char *   description
CODE:
        RETVAL=tibrvTransport_GetDescription( transport, &description);
OUTPUT:
description
RETVAL

tibrv_status
tibrvTransport_CreateLicensed(transport, service=NULL, network=NULL, daemon=NULL,license_ticket=NULL)
	tibrvTransport 	transport
	const char *	service
	const char *	network
	const char *	daemon
	const char *    license_ticket
CODE:
	RETVAL=tibrvTransport_CreateLicensed( &transport,service,network,daemon,license_ticket);
OUTPUT:
transport
RETVAL


tibrv_status 
tibrvTransport_CreateAcceptVc( vcTransport, connectSubject, transport )
       tibrvTransport   vcTransport
       const char  *    connectSubject
       tibrvTransport   transport
CODE:
    RETVAL=tibrvTransport_CreateAcceptVc( &vcTransport, connectSubject,transport);
OUTPUT:
vcTransport
RETVAL

tibrv_status 
tibrvTransport_CreateConnectVc( vcTransport, connectSubject, transport )
       tibrvTransport   vcTransport
       const char  *    connectSubject
       tibrvTransport   transport
CODE:
    RETVAL=tibrvTransport_CreateConnectVc( &vcTransport,connectSubject,transport);
OUTPUT:
vcTransport
RETVAL

tibrv_status 
tibrvTransport_WaitForVcConnection( vcTransport, timeout )
       tibrvTransport   vcTransport
       tibrv_f64        timeout
CODE:
    RETVAL=tibrvTransport_WaitForVcConnection( vcTransport, timeout );
OUTPUT:
RETVAL


#/***************************************************************************/
#/*                           tibrv/disp.h  functions                       */
#/***************************************************************************/

tibrv_status
tibrvDispatcher_CreateEx( dispatcher, dispatchable, timeout=0)
	tibrvDispatcher   dispatcher
	tibrvDispatchable dispatchable
	tibrv_f64         timeout
CODE:
	RETVAL=TIBRV_NOT_PERMITTED;
OUTPUT:
RETVAL

tibrv_status
tibrvDispatcher_Destroy( dispatcher )
	tibrvDispatcher   dispatcher
CODE:
	RETVAL=TIBRV_NOT_PERMITTED;
OUTPUT:
RETVAL

tibrv_status
tibrvDispatcher_SetName( dispatcher, dispatchName )
	tibrvDispatcher   dispatcher
	const char  *dispatchName
CODE:
	RETVAL=TIBRV_NOT_PERMITTED;
OUTPUT:
RETVAL

tibrv_status
tibrvDispatcher_GetName( dispatcher, dispatchName )
	tibrvDispatcher   dispatcher
	const char *      dispatchName
CODE:
	RETVAL=TIBRV_NOT_PERMITTED;
OUTPUT:
RETVAL

##########################################################################
# functions that don't fint the standard XSUB format.                    #
##########################################################################

tibrv_status
tibrvMsg_GetFieldEx(message, fieldName, field, optIdentifier=0)
        tibrvMsg        message
	const char *    fieldName
	SV *            field
	tibrv_u16       optIdentifier
PPCODE:
{
	tibrvMsgField      c_field;
	tibrv_status       retval;

        /* Check to see that "field" is truly a pointer to a hash */
	if (! SvROK(field) || SvTYPE(SvRV(field)) != SVt_PVHV) {
            XPUSHs(sv_2mortal(newSViv(1)));
            return;
        }
               
	retval=tibrvMsg_GetFieldEx(message, fieldName, &c_field, 
                                   optIdentifier);

	XPUSHs(sv_2mortal(newSViv(retval)));

        if( retval == TIBRV_OK ) 
	    buildHashFromField((HV*) SvRV(field), c_field);
}

tibrv_status
tibrvMsg_GetFieldByIndex(message, field, fieldIndex)
        tibrvMsg        message
	SV              *field
	tibrv_u16       fieldIndex
PPCODE:
{
	tibrvMsgField      c_field;
	tibrv_status       retval;

        /* Check to see that "field" is truly a pointer to a hash */
	if (! SvROK(field) || SvTYPE(SvRV(field)) != SVt_PVHV) {
            XPUSHs(sv_2mortal(newSViv(1)));
            return;
        }
               
	retval=tibrvMsg_GetFieldByIndex(message, &c_field, fieldIndex);

	XPUSHs(sv_2mortal(newSViv(retval)));

        if( retval == TIBRV_OK ) 
	    buildHashFromField((HV*) SvRV(field), c_field);
}


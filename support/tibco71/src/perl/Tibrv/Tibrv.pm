
# @(#)Tibrv.pm	1.11
#

package Tibrv;

use strict;
use vars qw($VERSION @ISA @EXPORT @EXPORT_OK);

#
# from tibrv/status.h
#
use constant TIBRV_OK			=> ($! = 0);

use constant TIBRV_INIT_FAILURE     	=> ($! = 1);
use constant TIBRV_INVALID_TRANSPORT   	=> ($! = 2);
use constant TIBRV_INVALID_ARG		=> ($! = 3);
use constant TIBRV_NOT_INITIALIZED     	=> ($! = 4);
use constant TIBRV_ARG_CONFLICT		=> ($! = 5);

use constant TIBRV_SERVICE_NOT_FOUND	=> ($! = 16);
use constant TIBRV_NETWORK_NOT_FOUND	=> ($! = 17);
use constant TIBRV_DAEMON_NOT_FOUND	=> ($! = 18);
use constant TIBRV_NO_MEMORY		=> ($! = 19);
use constant TIBRV_INVALID_SUBJECT     	=> ($! = 20);
use constant TIBRV_DAEMON_NOT_CONNECTED	=> ($! = 21);
use constant TIBRV_VERSION_MISMATCH	=> ($! = 22);
use constant TIBRV_SUBJECT_COLLISION   	=> ($! = 23);

use constant TIBRV_NOT_PERMITTED	=> ($! = 27);

use constant TIBRV_INVALID_NAME   	=> ($! = 30);
use constant TIBRV_INVALID_TYPE   	=> ($! = 31);
use constant TIBRV_INVALID_SIZE   	=> ($! = 32);
use constant TIBRV_INVALID_COUNT  	=> ($! = 33);

use constant TIBRV_NOT_FOUND   		=> ($! = 35);
use constant TIBRV_ID_IN_USE      	=> ($! = 36);
use constant TIBRV_ID_CONFLICT    	=> ($! = 37);
use constant TIBRV_CONVERSION_FAILED	=> ($! = 38);
use constant TIBRV_RESERVED_HANDLER	=> ($! = 39);
use constant TIBRV_ENCODER_FAILED	=> ($! = 40);
use constant TIBRV_DECODER_FAILED	=> ($! = 41);
use constant TIBRV_INVALID_MSG		=> ($! = 42);
use constant TIBRV_INVALID_FIELD	=> ($! = 43);
use constant TIBRV_INVALID_INSTANCE	=> ($! = 44);
use constant TIBRV_CORRUPT_MSG		=> ($! = 45);

use constant TIBRV_TIMEOUT		=> ($! = 50);
use constant TIBRV_INTR			=> ($! = 51);

use constant TIBRV_INVALID_DISPATCHABLE => ($! = 52);
use constant TIBRV_INVALID_DISPATCHER  	=> ($! = 53);

use constant TIBRV_INVALID_EVENT	=> ($! = 60);
use constant TIBRV_INVALID_CALLBACK     => ($! = 61);
use constant TIBRV_INVALID_QUEUE	=> ($! = 62);
use constant TIBRV_INVALID_QUEUE_GROUP  => ($! = 63);

use constant TIBRV_INVALID_TIME_INTERVAL => ($! = 64);

use constant TIBRV_INVALID_IO_SOURCE    => ($! = 65);
use constant TIBRV_INVALID_IO_CONDITION	=> ($! = 66);
use constant TIBRV_SOCKET_LIMIT		=> ($! = 67);

use constant TIBRV_OS_ERROR		=> ($! = 68);

use constant TIBRV_INSUFFICIENT_BUFFER	=> ($! = 70);
use constant TIBRV_EOF			=> ($! = 71);
use constant TIBRV_INVALID_FILE		=> ($! = 72);
use constant TIBRV_FILE_NOT_FOUND	=> ($! = 73);
use constant TIBRV_IO_FAILED		=> ($! = 74);

use constant TIBRV_NOT_FILE_OWNER       => ($! = 80);

use constant TIBRV_TOO_MANY_NEIGHBORS   =>  ($! = 90);

use constant TIBRV_SUBJECT_MAX          => ($! = 255);
use constant TIBRV_SUBJECT_TOKEN_MAX    => ($! = 127);
use constant TIBRV_INVALID_ID           => ($! = 0);
use constant TIBRV_TIMER_EVENT          => ($! = 1);
use constant TIBRV_IO_EVENT             => ($! = 2);
use constant TIBRV_LISTEN_EVENT         => ($! = 3);
use constant TIBRVQUEUE_DISCARD_NONE    => ($! = 0);
use constant TIBRVQUEUE_DISCARD_NEW     => ($! = 1);
use constant TIBRVQUEUE_DISCARD_FIRST   => ($! = 2);
use constant TIBRVQUEUE_DISCARD_LAST    => ($! = 3);
use constant TIBRVQUEUE_DEFAULT_POLICY  => ($! = 0);
use constant TIBRVQUEUE_DEFAULT_PRIORITY=> ($! = 1);
use constant TIBRV_WAIT_FOREVER         => ($! = -1.0);
use constant TIBRV_NO_WAIT              => ($! = 0.0);
use constant TIBRV_DEFAULT_QUEUE        => ($! = 1);
use constant TIBRV_PROCESS_TRANSPORT    => ($! = 10);
#
# types.h
#
use constant TIBRV_FIELDNAME_MAX        => ($! = 127);

use constant TIBRVMSG_MSG		=> ($! = 1);
use constant TIBRVMSG_DATETIME	=> ($! = 3);
use constant TIBRVMSG_OPAQUE		=> ($! = 7);
use constant TIBRVMSG_STRING		=> ($! = 8);
use constant TIBRVMSG_BOOL       	=> ($! = 9);
use constant TIBRVMSG_I8		=> ($! = 14); 
use constant TIBRVMSG_U8		=> ($! = 15);
use constant TIBRVMSG_I16		=> ($! = 16);
use constant TIBRVMSG_U16		=> ($! = 17);
use constant TIBRVMSG_I32		=> ($! = 18);
use constant TIBRVMSG_U32		=> ($! = 19);
use constant TIBRVMSG_I64		=> ($! = 20);
use constant TIBRVMSG_U64		=> ($! = 21);
use constant TIBRVMSG_F32		=> ($! = 24);
use constant TIBRVMSG_F64		=> ($! = 25);
use constant TIBRVMSG_IPPORT16      	=> ($! = 26);
use constant TIBRVMSG_IPADDR32      	=> ($! = 27);
use constant TIBRVMSG_ENCRYPTED     	=> ($! = 32);
use constant TIBRVMSG_NONE		=> ($! = 22);
use constant TIBRVMSG_I8ARRAY       	=> ($! = 34); 
use constant TIBRVMSG_U8ARRAY       	=> ($! = 35);
use constant TIBRVMSG_I16ARRAY      	=> ($! = 36);
use constant TIBRVMSG_U16ARRAY      	=> ($! = 37);
use constant TIBRVMSG_I32ARRAY      	=> ($! = 38);
use constant TIBRVMSG_U32ARRAY      	=> ($! = 39);
use constant TIBRVMSG_I64ARRAY      	=> ($! = 40);
use constant TIBRVMSG_U64ARRAY      	=> ($! = 41);
use constant TIBRVMSG_F32ARRAY      	=> ($! = 44);
use constant TIBRVMSG_F64ARRAY      	=> ($! = 45);
use constant TIBRVMSG_USER_FIRST	=> ($! = 128);
use constant TIBRVMSG_USER_LAST	=> ($! = 255);
use constant TIBRVMSG_NO_TAG => ($! = 0);
use constant TIBRV_IO_READ => ($! = 1);
use constant TIBRV_IO_WRITE => ($! = 2);
use constant TIBRV_IO_EXCEPTION => ($! = 4);

require Exporter;
require DynaLoader;
require AutoLoader;


    @ISA = qw(Exporter DynaLoader);

    @EXPORT = qw(
	
		 );
    $VERSION = '7.0';

    bootstrap Tibrv $VERSION;


# Preloaded methods go here.

# Autoload methods go after =cut, and are processed by the autosplit program.

    1;

__END__

=head1 NAME

Tibrv - Perl extension for TIB/Rendezvous V6.0

=head1 SYNOPSIS

  use Tibrv;

  $str=Tibrv::tibrv_Version()

  $status=Tibrv::tibrv_Open()
  $status=Tibrv::tibrv_Close()

  $status=Tibrv::tibrvEvent_CreateListener(event, queue, callback, 
                                           transport, subject, closure)
  $status=Tibrv::tibrvEvent_CreateTimer(event, queue, callback, interval, 
                                        closure)
  $status=Tibrv::tibrvEvent_CreateIO(event, queue, callback, socketId, 
                                     ioType, closure)
  $status=Tibrv::tibrvEvent_Destroy(event)
  $status=Tibrv::tibrvEvent_GetType(event, type)
  $status=Tibrv::tibrvEvent_GetQueue(event, queue)
  $status=Tibrv::tibrvEvent_GetListenerSubject(event, subject)
  $status=Tibrv::tibrvEvent_GetListenerTransport(event, transport)
  $status=Tibrv::tibrvEvent_GetTimerInterval(event, interval)
  $status=Tibrv::tibrvEvent_ResetTimerInterval(event, newInterval)
  $status=Tibrv::tibrvEvent_GetIOSource(event, source)
  $status=Tibrv::tibrvEvent_GetIOType(event, ioType)
  $status=Tibrv::tibrvStatus_GetText(status)
  $status=Tibrv::tibrvMsg_Create(message)
  $status=Tibrv::tibrvMsg_CreateEx(message, initialStorage)
  $status=Tibrv::tibrvMsg_Destroy(message)
  $status=Tibrv::tibrvMsg_Detach(message)
  $status=Tibrv::tibrvMsg_Reset(message)
  $status=Tibrv::tibrvMsg_Expand(message, additionalStorage)
  $status=Tibrv::tibrvMsg_SetSendSubject(message, subject)
  $status=Tibrv::tibrvMsg_GetSendSubject(message, subject)
  $status=Tibrv::tibrvMsg_SetReplySubject(message, replySubject)
  $status=Tibrv::tibrvMsg_GetReplySubject(message, replySubject)
  $status=Tibrv::tibrvMsg_GetNumFields(message, numFields)
  $status=Tibrv::tibrvMsg_GetByteSize(message, byteSize)
  $status=Tibrv::tibrvMsg_ConvertToString(message, string)
  $status=Tibrv::tibrvMsg_CreateFromBytes(message, bytes)
  $status=Tibrv::tibrvMsg_GetAsBytes(message, bytePtr)
  $status=Tibrv::tibrvMsg_CreateCopy(message, copy)
  $status=Tibrv::tibrvMsg_AddMsgEx(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddIPAddr32Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddIPPort16Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddDateTimeEx(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddBoolEx(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddI8Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddU8Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddI16Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddU16Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddI32Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddU32Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddI64Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddU64Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddF32Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddF64Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddStringEx(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_AddOpaqueEx(message, fieldName, value, size, optIdentifier)
  $status=Tibrv::tibrvMsg_AddXmlEx(message, fieldName, value, size, optIdentifier)
  $status=Tibrv::tibrvMsg_GetFieldEx(message,fieldName,field,optIdentifier)
  $status=Tibrv::tibrvMsg_GetFieldByIndex(message,field,fieldIndex)
  $status=Tibrv::tibrvMsg_GetMsgEx(message, fieldName, subMessage, optIdentifier)
  $status=Tibrv::tibrvMsg_GetIPAddr32Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetIPPort16Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetDateTimeEx(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetBoolEx(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetI8Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetU8Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetI16Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetU16Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetI32Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetU32Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetI64Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetU64Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetF32Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetF64Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetStringEx(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_GetOpaqueEx(message, fieldName, value, size, 
                                      optIdentifier)
  $status=Tibrv::tibrvMsg_GetXmlEx(message, fieldName, value, size, 
                                      optIdentifier)
  $status=Tibrv::tibrvMsg_RemoveFieldEx(message, fieldName, optIdentifier)
  $status=Tibrv::tibrvMsg_RemoveFieldInstance(message, fieldName, instance)
  $status=Tibrv::tibrvMsg_UpdateMsgEx(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateIPAddr32Ex(message, fieldName, value, 
                                           optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateIPPort16Ex(message, fieldName, value, 
                                           optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateDateTimeEx(message, fieldName, value, 
                                           optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateBoolEx(message, fieldName, value, 
                                       optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateI8Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateU8Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateI16Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateU16Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateI32Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateU32Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateI64Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateU64Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateF32Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateF64Ex(message, fieldName, value, optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateStringEx(message, fieldName, value, 
                                         optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateOpaqueEx(message, fieldName, value, size, 
                                         optIdentifier)
  $status=Tibrv::tibrvMsg_UpdateXmlEx(message, fieldName, value, size, 
                                         optIdentifier)
  $status=Tibrv::tibrvMsg_MarkReferences(message)
  $status=Tibrv::tibrvMsg_ClearReferences(message)
  $status=Tibrv::tibrvQueue_Create(eventQueue)
  $status=Tibrv::tibrvQueue_TimedDispatch(eventQueue, waitTime)
  $status=Tibrv::tibrvQueue_Dispatch(eventQueue)
  $status=Tibrv::tibrvQueue_Poll(eventQueue)    
  $status=Tibrv::tibrvQueue_DestroyEx(eventQueue,completeCallback=NULL,closure=NULL)
  $status=Tibrv::tibrvQueue_GetCount(eventQueue, numEvents)
  $status=Tibrv::tibrvQueue_GetPriority(eventQueue, priority)
  $status=Tibrv::tibrvQueue_SetPriority(eventQueue, priority)
  $status=Tibrv::tibrvQueue_GetLimitPolicy(eventQueue, policy, maxEvents, 
                                           discardAmount)
  $status=Tibrv::tibrvQueue_SetLimitPolicy(eventQueue, policy, maxEvents, 
                                           discardAmount)
  $status=Tibrv::tibrvQueue_SetName(eventQueue, queueName)
  $status=Tibrv::tibrvQueue_GetName(eventQueue, queueName)
  $status=Tibrv::tibrvQueueGroup_Create(eventQueueGroup)
  $status=Tibrv::tibrvQueueGroup_TimedDispatch(eventQueueGroup, waitTime)
  $status=Tibrv::tibrvQueueGroup_Dispatch(eventQueue)
  $status=Tibrv::tibrvQueueGroup_Poll(eventQueue)    
  $status=Tibrv::tibrvQueueGroup_Destroy(eventQueueGroup)
  $status=Tibrv::tibrvQueueGroup_Add(eventQueueGroup, eventQueue)
  $status=Tibrv::tibrvQueueGroup_Remove(eventQueueGroup, eventQueue)
  $status=Tibrv::tibrvTransport_Create(transport, service, network, daemon)
  $status=Tibrv::tibrvTransport_Send(transport, message)
  $status=Tibrv::tibrvTransport_SendRequest(transport, message, reply, timeout)
  $status=Tibrv::tibrvTransport_SendReply(transport, message, requestMessage)
  $status=Tibrv::tibrvTransport_Destroy(transport)
  $status=Tibrv::tibrvTransport_CreateInbox(transport, subjectString, subjectLimit)
  $status=Tibrv::tibrvTransport_GetService(transport, serviceString)
  $status=Tibrv::tibrvTransport_GetNetwork(transport, networkString)
  $status=Tibrv::tibrvTransport_GetDaemon(transport, daemonString)
  $status=Tibrv::tibrvTransport_SetDescription(transport, description)
  $status=Tibrv::tibrvTransport_GetDescription(transport, description)
  $status=Tibrv::tibrvTransport_CreateLicensed(transport, service, 
                                               network, 
                                               daemon,
                                               license_ticket)
  $status=Tibrv::tibrvTransport_CreateAcceptVc(vcTransport,
					       connectSubject,
					       transport);
  $status=Tibrv::tibrvTransport_CreateConnectVc(vcTransport,
						connectSubject,
						transport);
  $status=Tibrv::tibrvTransport_WaitForVcConnection(vcTransport,
						    timeout);

  $status=Tibrv::tibrvDispatcher_CreateEx( dispatcher, dispatchable, timeout)
  $status=Tibrv::tibrvDispatcher_Destroy( dispatcher )
  $status=Tibrv::tibrvDispatcher_SetName( dispatcher, dispatchName )
  $status=Tibrv::tibrvDispatcher_GetName( dispatcher, dispatchName )

=head1 DESCRIPTION

Provides the base TIB/Rendezvous V6 function call functionality.  See the
TIB/Rendezvous C programmers guide for usage information.



=head1 AUTHOR

TIBCO Software Inc.

=head1 SEE ALSO

perl(1).

=cut


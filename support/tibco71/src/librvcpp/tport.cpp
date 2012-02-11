
/*
 * Copyright (c) 1995-2000 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software, Inc., Palo Alto, California, USA
 *
 * @(#)tport.cpp	2.14
 *
 */

#include "tibrvcpp.h"


//=====================================================================
// TibrvTransport
//=====================================================================

TibrvTransport::TibrvTransport()
{
    _transport = TIBRV_INVALID_ID;
}

TibrvNetTransport::TibrvNetTransport() :
    TibrvTransport()
{
}

TibrvTransport::TibrvTransport(tibrvTransport transport)
{
    _transport = transport;
}

TibrvTransport::~TibrvTransport()
{
    destroy();
}

TibrvStatus
TibrvNetTransport::create (const char* service,
                           const char* network,
                           const char* daemon )
{
    if (_transport != TIBRV_INVALID_ID) return TIBRV_NOT_PERMITTED;
    tibrv_status status = tibrvTransport_Create(&_transport,service,network,daemon);
    if (status != TIBRV_OK)
        _transport = TIBRV_INVALID_ID;
    return status;
}

TibrvStatus
TibrvNetTransport::createLicensed (const char* service,
                                   const char* network,
                                   const char* daemon,
                                   const char* license_ticket)
{
    if (_transport != TIBRV_INVALID_ID) return TIBRV_NOT_PERMITTED;
    tibrv_status status = tibrvTransport_CreateLicensed(&_transport,
                                    service,network,daemon,
                                    license_ticket);
    if (status != TIBRV_OK)
        _transport = TIBRV_INVALID_ID;
    return status;
}

TibrvStatus
TibrvTransport::destroy()
{
    if (_transport == TIBRV_PROCESS_TRANSPORT)
        return TIBRV_NOT_PERMITTED;

    tibrv_status status = TIBRV_OK;
    if (_transport != TIBRV_INVALID_ID)
    {
        status = tibrvTransport_Destroy(_transport);
        _transport = TIBRV_INVALID_ID;
    }
    return status;
}

tibrv_bool
TibrvTransport::isValid() const
{
    return ((_transport != TIBRV_INVALID_ID) ? TIBRV_TRUE : TIBRV_FALSE);
}

TibrvStatus TibrvTransport::setDescription(const char* description)
{
    return tibrvTransport_SetDescription(_transport,description);
}

TibrvStatus TibrvTransport::getDescription(const char** description) const
{
    return tibrvTransport_GetDescription(_transport,description);
}

TibrvStatus
TibrvTransport::send(const TibrvMsg& message)
{
    return tibrvTransport_Send(_transport,message.getHandle());
}

TibrvStatus
TibrvTransport::sendRequest(const TibrvMsg& message, TibrvMsg& reply, tibrv_f64 timeout)
{
    tibrvMsg msg;
    TibrvStatus status = tibrvTransport_SendRequest(_transport,message.getHandle(),&msg,timeout);
    if (status == TIBRV_OK)
        reply.attach(msg,TIBRV_TRUE);

    return status;
}

TibrvStatus
TibrvTransport::sendReply(const TibrvMsg& replyMessage, const TibrvMsg& requestMessage)
{
    return tibrvTransport_SendReply(_transport,replyMessage.getHandle(),requestMessage.getHandle());
}

TibrvStatus
TibrvTransport::createInbox(char* subjectString, tibrv_u32 subjectLimit) const
{
    return tibrvTransport_CreateInbox(_transport,subjectString,subjectLimit);
}

TibrvTransport::TibrvTransport(const TibrvTransport& transport)
{
    _transport = transport.getHandle();
}

TibrvTransport& TibrvTransport::operator=(const TibrvTransport& transport)
{
    _transport = transport.getHandle();
    return *this;
}

TibrvProcessTransport& TibrvProcessTransport::operator=(const TibrvProcessTransport& transport)
{
    _transport = transport.getHandle();
    return *this;
}

TibrvProcessTransport::TibrvProcessTransport(const TibrvProcessTransport& transport)
{
    _transport = transport.getHandle();
}

TibrvStatus
TibrvNetTransport::getService(const char*& serviceString) const
{
    return tibrvTransport_GetService(_transport,&serviceString);
}

TibrvStatus
TibrvNetTransport::getNetwork(const char*& networkString) const
{
    return tibrvTransport_GetNetwork(_transport,&networkString);
}

TibrvStatus
TibrvNetTransport::getDaemon(const char*& daemonString) const
{
    return tibrvTransport_GetDaemon(_transport,&daemonString);
}


TibrvNetTransport::TibrvNetTransport(const TibrvNetTransport& transport)
{
    _transport = transport.getHandle();
}

TibrvStatus
TibrvNetTransport::setBatchMode( tibrvTransportBatchMode mode )
{
    return( tibrvTransport_SetBatchMode(_transport, mode ) );
}

TibrvNetTransport& TibrvNetTransport::operator=(const TibrvNetTransport& transport)
{
    _transport = transport.getHandle();
    return *this;
}

TibrvVcTransport::TibrvVcTransport() :
    TibrvTransport()
{
}

TibrvStatus
TibrvVcTransport::createAcceptVc( const char** connectSubject,
                                  TibrvTransport* transport )
{

    tibrv_status status;
    tibrvTransport vctport;

    if( _transport != TIBRV_INVALID_ID)
        return TIBRV_NOT_PERMITTED;

    if( transport == NULL )
        return TIBRV_INVALID_ARG;

    status = tibrvTransport_CreateAcceptVc(&vctport,
                                           connectSubject,
                                           transport->getHandle());

    if( status != TIBRV_OK )
    {
        _transport=TIBRV_INVALID_ID;
    }
    else
    {
        _transport=vctport;
    }

    return status;
}

TibrvStatus
TibrvVcTransport::createConnectVc( const char* connectSubject,
                                   TibrvTransport* transport )
{
    tibrv_status status;
    tibrvTransport vctport;

    if( _transport != TIBRV_INVALID_ID)
        return TIBRV_NOT_PERMITTED;

    if( transport == NULL )
        return TIBRV_INVALID_ARG;

    status = tibrvTransport_CreateConnectVc(&vctport,
                                            connectSubject,
                                            transport->getHandle());

    if( status != TIBRV_OK )
    {
        _transport=TIBRV_INVALID_ID;
    }
    else
    {
        _transport=vctport;
    }

    return status;
}

TibrvStatus
TibrvVcTransport::waitForVcConnection( tibrv_f64 timeout )
{
    return ( tibrvTransport_WaitForVcConnection( _transport, timeout ));
}

TibrvVcTransport::TibrvVcTransport(const TibrvVcTransport& transport)
{
    _transport = transport.getHandle();
}

TibrvVcTransport& TibrvVcTransport::operator=(const TibrvVcTransport& transport)
{
    _transport = transport.getHandle();
    return *this;
}















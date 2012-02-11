
/*
 * Copyright (c) 1995-2000 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software, Inc., Palo Alto, California, USA
 *
 * @(#)cmq.cpp	2.8
 *
 */

#include "cmcpp.h"

//=====================================================================
// TibrvCmQueueTransport
//=====================================================================

TibrvCmQueueTransport::TibrvCmQueueTransport() :
        TibrvCmTransport()
{
}

TibrvCmQueueTransport::~TibrvCmQueueTransport()
{
    destroy();
}


TibrvStatus TibrvCmQueueTransport::create
                (TibrvTransport* transport,
                 const char* cmName,
                 tibrv_u32 workerWeight,
                 tibrv_u32 workerTasks,
                 tibrv_u16 schedulerWeight,
                 tibrv_f64 schedulerHeartbeat,
                 tibrv_f64 schedulerActivation)
{
    tibrv_status status;

    if (_cmTransport != TIBRV_INVALID_ID)
        return TIBRV_NOT_PERMITTED;

    if (transport == NULL)
        return TIBRV_INVALID_ARG;

    if (cmName == NULL)
        return TIBRV_INVALID_ARG;

    status = tibrvcmTransport_CreateDistributedQueueEx(
                                &_cmTransport,
                                 transport->getHandle(),
                                 cmName,
                                 workerWeight,
                                 workerTasks,
                                 schedulerWeight,
                                 schedulerHeartbeat,
                                 schedulerActivation);
    if (status != TIBRV_OK)
    {
        _cmTransport = TIBRV_INVALID_ID;
        _transport = NULL;
    }
    else
    {
        _transport = transport;
    }

    return status;
}

tibrv_bool
TibrvCmQueueTransport::isValid () const
{
    return ((_cmTransport != TIBRV_INVALID_ID) ? TIBRV_TRUE : TIBRV_FALSE);
}

TibrvStatus
TibrvCmQueueTransport::destroy()
{
    tibrv_status status = TIBRV_INVALID_TRANSPORT;
    if (_cmTransport != TIBRV_INVALID_ID)
    {
        status = tibrvcmTransport_Destroy(_cmTransport);
        _cmTransport = TIBRV_INVALID_ID;
        _transport = NULL;
    }
    return status;
}

TibrvStatus
TibrvCmQueueTransport::setCompleteTime(tibrv_f64 completeTime)
{
    return tibrvcmTransport_SetCompleteTime(_cmTransport,completeTime);
}

TibrvStatus
TibrvCmQueueTransport::getCompleteTime(tibrv_f64& completeTime) const
{
    return tibrvcmTransport_GetCompleteTime(_cmTransport,&completeTime);
}

TibrvStatus
TibrvCmQueueTransport::setWorkerWeight(tibrv_u32 workerWeight)
{
    return tibrvcmTransport_SetWorkerWeight(_cmTransport,workerWeight);
}

TibrvStatus
TibrvCmQueueTransport::getWorkerWeight(tibrv_u32& workerWeight) const
{
    return tibrvcmTransport_GetWorkerWeight(_cmTransport,&workerWeight);
}

TibrvStatus
TibrvCmQueueTransport::setWorkerTasks(tibrv_u32 workerTasks)
{
    return tibrvcmTransport_SetWorkerTasks(_cmTransport,workerTasks);
}

TibrvStatus
TibrvCmQueueTransport::getWorkerTasks(tibrv_u32& workerTasks) const
{
    return tibrvcmTransport_GetWorkerTasks(_cmTransport,&workerTasks);
}

TibrvCmQueueTransport::TibrvCmQueueTransport(const TibrvCmQueueTransport& cmqtport)
{
    _cmTransport = cmqtport.getHandle();
}

TibrvCmQueueTransport& TibrvCmQueueTransport::operator=(const TibrvCmQueueTransport& cmqtport)
{
    _cmTransport = cmqtport.getHandle();
    return *this;
}

//=====================================================================

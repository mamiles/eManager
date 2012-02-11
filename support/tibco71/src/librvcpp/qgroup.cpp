
/*
 * Copyright (c) 1995-2000 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software, Inc., Palo Alto, California, USA
 *
 * @(#)qgroup.cpp	2.4
 *
 */

#include "tibrvcpp.h"


//=====================================================================
// TibrvQueueGroup
//=====================================================================

TibrvQueueGroup::TibrvQueueGroup()
{
    _queueGroup = TIBRV_INVALID_ID;
}

TibrvQueueGroup::~TibrvQueueGroup()
{
    destroy();
}

TibrvStatus
TibrvQueueGroup::create()
{
    if (_queueGroup != TIBRV_INVALID_ID) return TIBRV_NOT_PERMITTED;
    return tibrvQueueGroup_Create(&_queueGroup);
}

TibrvStatus
TibrvQueueGroup::destroy()
{
    if (_queueGroup == TIBRV_INVALID_ID) return TIBRV_NOT_PERMITTED;
    tibrv_status status = tibrvQueueGroup_Destroy(_queueGroup);
    _queueGroup = TIBRV_INVALID_ID;
    return TibrvStatus(status);
}

tibrv_bool
TibrvQueueGroup::isValid() const
{
    return ((_queueGroup != TIBRV_INVALID_ID) ? TIBRV_TRUE : TIBRV_FALSE);
}

TibrvStatus
TibrvQueueGroup::dispatch()
{
    return tibrvQueueGroup_Dispatch(_queueGroup);
}

TibrvStatus
TibrvQueueGroup::timedDispatch(tibrv_f64 timeout)
{
    return tibrvQueueGroup_TimedDispatch(_queueGroup,timeout);
}

TibrvStatus
TibrvQueueGroup::poll()
{
    return tibrvQueueGroup_Poll(_queueGroup);
}

TibrvStatus
TibrvQueueGroup::add(TibrvQueue* queue)
{
    return tibrvQueueGroup_Add(_queueGroup,queue->getHandle());
}

TibrvStatus
TibrvQueueGroup::remove(TibrvQueue* queue)
{
    return tibrvQueueGroup_Remove(_queueGroup,queue->getHandle());
}

TibrvQueueGroup::TibrvQueueGroup(const TibrvQueueGroup& group) : TibrvDispatchable()
{
    _queueGroup = group.getHandle();
}

TibrvQueueGroup& TibrvQueueGroup::operator=(const TibrvQueueGroup& group)
{
    _queueGroup = group.getHandle();
    return *this;
}


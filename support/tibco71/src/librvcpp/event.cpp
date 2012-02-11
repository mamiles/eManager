
/*
 * Copyright (c) 1995-2000 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software, Inc., Palo Alto, California, USA
 *
 * @(#)event.cpp	2.6
 *
 */

#include "tibrvcpp.h"

//=====================================================================
// TibrvEvent
//=====================================================================

void TibrvEvent::_completeCB(tibrvEvent event, void *closure)
{
    USE_PARAM(event);

    if (closure == NULL)
        return;

    TibrvEvent * pEvt = (TibrvEvent*) closure;
    TibrvEventOnComplete * cb = pEvt->_completeCallback;
    if (cb != NULL)
        cb->onComplete(pEvt);
}

TibrvEvent::TibrvEvent()
{
    _event = TIBRV_INVALID_ID;
    _callback = NULL;
    _completeCallback = NULL;
    _closure = NULL;
    _queue = NULL;
    _objType = (tibrvEventType)0;
}

TibrvEvent::~TibrvEvent()
{
    destroy();
}

TibrvStatus
TibrvEvent::destroy(TibrvEventOnComplete* completeCallback)
{
    tibrv_status status = TIBRV_INVALID_EVENT;
    if (_event != TIBRV_INVALID_ID)
    {
        if (completeCallback == NULL)
        {
            status = tibrvEvent_Destroy(_event);
        }
        else
        {
            _completeCallback = completeCallback;
            status = tibrvEvent_DestroyEx(_event,(tibrvEventOnComplete)_completeCB);
        }
        _event = TIBRV_INVALID_ID;
    }
    return status;
}

tibrv_bool
TibrvEvent::isValid() const
{
    return ((_event != TIBRV_INVALID_ID) ? TIBRV_TRUE : TIBRV_FALSE);
}


TibrvStatus
TibrvEvent::getType(tibrvEventType& type) const
{
    return tibrvEvent_GetType(_event,&type);
}

tibrv_bool
TibrvEvent::isListener() const
{
    return ((_objType == TIBRV_LISTEN_EVENT) ? TIBRV_TRUE : TIBRV_FALSE);
}

tibrv_bool
TibrvEvent::isTimer() const
{
    return ((_objType == TIBRV_TIMER_EVENT) ? TIBRV_TRUE : TIBRV_FALSE);
}

tibrv_bool
TibrvEvent::isIOEvent() const
{
    return ((_objType == TIBRV_IO_EVENT) ? TIBRV_TRUE : TIBRV_FALSE);
}

TibrvEvent::TibrvEvent(const TibrvEvent& event)
{
    _event = event.getHandle();
}

TibrvEvent& TibrvEvent::operator=(const TibrvEvent& event)
{
    _event = event.getHandle();
    return *this;
}

//=====================================================================
// TibrvListener
//=====================================================================

void TibrvEvent::_listenCB(tibrvEvent event, tibrvMsg msg, void *closure)
{
    USE_PARAM(event);

    if (closure == NULL)
        return;

    TibrvListener * listener = (TibrvListener*) closure;
    TibrvCallback * cb = ((TibrvEvent*)listener)->_callback;
    if (cb == NULL)
        return;

    TibrvMsg rvmsg(msg,TIBRV_FALSE);
    cb->onEvent(listener,rvmsg);

}

TibrvListener::TibrvListener() :
    TibrvEvent()
{
    _transport = NULL;
    _objType = TIBRV_LISTEN_EVENT;
}

TibrvListener::~TibrvListener()
{
}

TibrvStatus
TibrvListener::create(TibrvQueue* queue,
                      TibrvCallback* callback,
                      TibrvTransport* transport,
                      const char* subject,
                      const void* closure)
{
    if (_event != TIBRV_INVALID_ID)
        return TIBRV_NOT_PERMITTED;

    if (callback == NULL)
        return TIBRV_INVALID_ARG;

    if (transport == NULL)
        return TIBRV_INVALID_ARG;

    if (subject == NULL)
        return TIBRV_INVALID_ARG;

    tibrv_status status;
    tibrvQueue q = TIBRV_DEFAULT_QUEUE;
    tibrvTransport t = TIBRV_PROCESS_TRANSPORT;
    if (queue != NULL)
    {
        _queue = queue;
        q = _queue->getHandle();
    }
    else
        return TIBRV_INVALID_QUEUE;

    _transport = transport;
    t = _transport->getHandle();

    _callback = callback;
    _closure = (void*)closure;
    status = tibrvEvent_CreateListener(&_event,q,TibrvEvent::_listenCB,t,(char*)subject,this);

    if (status != TIBRV_OK)
        _event = TIBRV_INVALID_ID;

    return status;
}

TibrvTransport*
TibrvListener::getTransport() const
{
    return _transport;
}

TibrvStatus
TibrvListener::getSubject(const char*& subject) const
{
    return tibrvEvent_GetListenerSubject(_event,&subject);
}

TibrvListener::TibrvListener(const TibrvListener& event)
{
    _event = event.getHandle();
}

TibrvListener& TibrvListener::operator=(const TibrvListener& event)
{
    _event = event.getHandle();
    return *this;
}

//=====================================================================
// TibrvTimer
//=====================================================================

void TibrvEvent::_timerCB(tibrvEvent event, tibrvMsg msg, void *closure)
{
    USE_PARAM(event);
    USE_PARAM(msg);

    if (closure == NULL)
        return;

    TibrvTimer * timer = (TibrvTimer*) closure;
    TibrvCallback * cb = timer->_callback;
    TibrvMsg rvmsg;
    if (cb == NULL)
        return;

    cb->onEvent(timer,rvmsg);

}

TibrvTimer::TibrvTimer() :
    TibrvEvent()
{
    //_interval = (tibrv_f64)0;
    _objType = TIBRV_TIMER_EVENT;
}

TibrvTimer::~TibrvTimer()
{
}

TibrvStatus
TibrvTimer::create(TibrvQueue* queue,
                   TibrvCallback* callback,
                   tibrv_f64 interval,
                   const void* closure)
{
    if (_event != TIBRV_INVALID_ID)
        return TIBRV_NOT_PERMITTED;

    if (callback == NULL)
        return TIBRV_INVALID_ARG;

    tibrv_status status;
    tibrvQueue q = TIBRV_DEFAULT_QUEUE;

    if (queue != NULL)
    {
        _queue = queue;
        q = _queue->getHandle();
    }
    else
        return TIBRV_INVALID_QUEUE;

    _callback = callback;
    _closure = (void*)closure;
    status = tibrvEvent_CreateTimer(&_event,q,TibrvEvent::_timerCB,interval,this);
    if (status != TIBRV_OK)
        _event = TIBRV_INVALID_ID;
    return status;
}

TibrvStatus
TibrvTimer::getInterval(tibrv_f64& interval) const
{
    return tibrvEvent_GetTimerInterval(_event,&interval);
}

TibrvStatus
TibrvTimer::resetInterval(tibrv_f64 newInterval)
{
    return tibrvEvent_ResetTimerInterval(_event,newInterval);
}

TibrvTimer::TibrvTimer(const TibrvTimer& event)
{
    _event = event.getHandle();
}

TibrvTimer& TibrvTimer::operator=(const TibrvTimer& event)
{
    _event = event.getHandle();
    return *this;
}

//=====================================================================
// TibrvIOEvent
//=====================================================================

void TibrvEvent::_ioCB(tibrvEvent event, tibrvMsg msg, void *closure)
{
    USE_PARAM(event);
    USE_PARAM(msg);

    if (closure == NULL)
        return;

    TibrvIOEvent * ioEvent = (TibrvIOEvent*) closure;
    TibrvCallback * cb = ioEvent->_callback;
    if (cb == NULL)
        return;

    TibrvMsg rvmsg;   // empty message
    cb->onEvent(ioEvent,rvmsg);
}


TibrvIOEvent::TibrvIOEvent() :
    TibrvEvent()
{
    _objType = TIBRV_IO_EVENT;
}

TibrvIOEvent::~TibrvIOEvent()
{
}

TibrvStatus
TibrvIOEvent::create(TibrvQueue* queue,
                     TibrvCallback* callback,
                     tibrv_i32 socketId,
                     tibrvIOType ioType,
                     const void* closure)
{
    if (_event != TIBRV_INVALID_ID)
        return TIBRV_NOT_PERMITTED;

    if (callback == NULL)
        return TIBRV_INVALID_ARG;

    tibrv_status status;
    tibrvQueue q = TIBRV_DEFAULT_QUEUE;

    if (queue != NULL)
    {
        _queue = queue;
        q = _queue->getHandle();
    }
    else
        return TIBRV_INVALID_QUEUE;

    _callback = callback;
    _closure = (void*)closure;
    status = tibrvEvent_CreateIO(&_event,q,TibrvEvent::_ioCB,socketId,ioType,this);
    if (status != TIBRV_OK)
        _event = TIBRV_INVALID_ID;

    return status;
}

TibrvStatus
TibrvIOEvent::getIOSource(tibrv_i32& ioSource) const
{
    return tibrvEvent_GetIOSource(_event,&ioSource);
}

TibrvStatus
TibrvIOEvent::getIOType(tibrvIOType& ioType) const
{
    return tibrvEvent_GetIOType(_event,&ioType);
}


TibrvIOEvent::TibrvIOEvent(const TibrvIOEvent& event)
{
    _event = event.getHandle();
}

TibrvIOEvent& TibrvIOEvent::operator=(const TibrvIOEvent& event)
{
    _event = event.getHandle();
    return *this;
}

//=====================================================================
// TibrvMsgCallback
//=====================================================================

TibrvMsgCallback::TibrvMsgCallback() :
        TibrvCallback()
{
}

void
TibrvMsgCallback::onEvent(TibrvEvent * event, TibrvMsg& msg)
{
    onMsg((TibrvListener*)event, msg);
}


//=====================================================================
// TibrvTimerCallback
//=====================================================================

TibrvTimerCallback::TibrvTimerCallback() :
        TibrvCallback()
{
}

void
TibrvTimerCallback::onEvent(TibrvEvent * event, TibrvMsg& msg)
{
    if (msg.getHandle() == NULL)
        msg.detach();
    onTimer((TibrvTimer*)event);
}


//=====================================================================
// TibrvIOCallback
//=====================================================================

TibrvIOCallback::TibrvIOCallback() :
        TibrvCallback()
{
}

void
TibrvIOCallback::onEvent(TibrvEvent * event, TibrvMsg& msg)
{
    if (msg.getHandle() == NULL)
        msg.detach();
    onIOEvent((TibrvIOEvent*)event);
}

//=====================================================================

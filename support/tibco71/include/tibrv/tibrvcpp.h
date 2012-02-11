
/*
 * Copyright (c) 1995-2000 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvcpp.h	2.34
 *
 */

#ifndef _INCLUDED_tibrvcpp_h
#define _INCLUDED_tibrvcpp_h

#include <string.h>
#include <stdlib.h>

#include "tibrv.h"

//=====================================================================
// Disable Visual C++ warning 4514:
// "unreferenced inline function has been removed"
#ifdef _MSC_VER
#pragma warning( disable : 4514 )
#endif

//=====================================================================

#define USE_PARAM(x)      ((x)=(x))

#define _TIBRV_INLINE inline

//=====================================================================
// Forward declarations
//=====================================================================

class TibrvStatus;

class TibrvMsg;
class TibrvMsgField;
class TibrvMsgDateTime;

class TibrvDispatchable;
class TibrvQueue;
class TibrvQueueGroup;
class TibrvDispatcher;

class TibrvTransport;
class TibrvProcessTransport;
class TibrvNetTransport;

class TibrvCallback;
class TibrvMsgCallback;
class TibrvTimerCallback;
class TibrvIOCallback;

class TibrvEvent;
class TibrvListener;
class TibrvTimer;
class TibrvIOEvent;

class TibrvEventOnComplete;
class TibrvQueueOnComplete;

//=====================================================================
// TibrvStatus
//=====================================================================

class TibrvStatus
{

public:

    TibrvStatus();
    TibrvStatus(tibrv_status status) {_status=status;}

    tibrv_status  getCode () const;
    const char*   getText () const;

    operator tibrv_status() const;

private:

    tibrv_status _status;

};


//=====================================================================
// Tibrv
//=====================================================================

class Tibrv
{

public:

    static TibrvStatus open();
    static TibrvStatus close();

    static const char* version();

    static TibrvQueue* defaultQueue();
    static TibrvProcessTransport* processTransport();

private:

    Tibrv() {}

    static TibrvQueue _defaultQueue;
    static TibrvProcessTransport _processTransport;

};

//=====================================================================
// TibrvDispatchable
//
// An interface implemented by TibrvQueue and TibrvQueueGroup
//=====================================================================

class TibrvDispatchable
{

public:

    virtual tibrv_bool  isValid  () const = 0;
    virtual TibrvStatus destroy  () = 0;

    virtual TibrvStatus dispatch     () = 0;
    virtual TibrvStatus timedDispatch(tibrv_f64 timeout) = 0;
    virtual TibrvStatus poll          () = 0;

    // used by TibrvDispatcher
    virtual tibrvDispatchable getDispatchable() const = 0;

protected:

    TibrvDispatchable(){};

private:

};

//=====================================================================
// TibrvQueue
//
// In order to get default queue use Tibrv::defaultQueue().
//=====================================================================

class TibrvQueue : public TibrvDispatchable
{

public:

    TibrvQueue();
    virtual ~TibrvQueue();

    tibrv_bool  isValid  () const;

    TibrvStatus create   ();
    TibrvStatus destroy  ();  // called automatically by destructor

    // can not make default completeCB=NULL
    // because destroy() is pure virtual.
    TibrvStatus destroy  (TibrvQueueOnComplete* completeCB,
                          const void* closure = NULL);

    TibrvStatus setName  (const char* queueName);
    TibrvStatus getName  (const char*& queueName) const;

    TibrvStatus getCount (tibrv_u32& numEvents) const;

    virtual TibrvStatus dispatch      ();
    virtual TibrvStatus timedDispatch (tibrv_f64 timeout);
    virtual TibrvStatus poll          ();

    TibrvStatus setPriority (tibrv_u32 priority);
    TibrvStatus getPriority (tibrv_u32& priority) const;

    TibrvStatus setLimitPolicy (tibrvQueueLimitPolicy policy,
                                tibrv_u32 maxEvents,
                                tibrv_u32 discardAmount);
    TibrvStatus getLimitPolicy (tibrvQueueLimitPolicy& policy,
                                tibrv_u32& maxEvents,
                                tibrv_u32& discardAmount) const;

    tibrvDispatchable getDispatchable() const
                { return (tibrvDispatchable)_queue; }

    tibrvQueue  getHandle() const { return _queue; }

private:

    tibrvQueue _queue;
    TibrvQueueOnComplete* _completeCallback;
    void* _closure;
    static void _completeCB(tibrvQueue queue, void *closure);

    TibrvQueue(const TibrvQueue& queue);
    TibrvQueue& operator=(const TibrvQueue& queue);

    friend class Tibrv;

    TibrvQueue(tibrvQueue queue);     // from C handle

};

//=====================================================================
// TibrvQueueOnComplete
//=====================================================================

class TibrvQueueOnComplete
{

public:
    TibrvQueueOnComplete(){};

    virtual void onComplete(TibrvQueue* queue, void* closure) = 0;

};

//=====================================================================
// TibrvQueueGroup
//=====================================================================

class TibrvQueueGroup : public TibrvDispatchable
{

public:

    TibrvQueueGroup();
    virtual ~TibrvQueueGroup();

    tibrv_bool  isValid () const;

    TibrvStatus create  ();
    TibrvStatus destroy ();

    virtual TibrvStatus dispatch      ();
    virtual TibrvStatus timedDispatch (tibrv_f64 timeout);
    virtual TibrvStatus poll          ();

    virtual TibrvStatus add   (TibrvQueue* queue);
    virtual TibrvStatus remove(TibrvQueue* queue);

    tibrvDispatchable getDispatchable() const
                { return (tibrvDispatchable)_queueGroup; }
    tibrvQueueGroup  getHandle() const { return _queueGroup; }

private:

    tibrvQueueGroup  _queueGroup;

    TibrvQueueGroup(const TibrvQueueGroup& group);
    TibrvQueueGroup& operator=(const TibrvQueueGroup& group);

};

//=====================================================================
// TibrvDispatcher
//=====================================================================

class TibrvDispatcher
{

public:

    TibrvDispatcher();
    virtual ~TibrvDispatcher();

    tibrv_bool  isValid () const;

    TibrvStatus create  (TibrvDispatchable* dispatchable,
                         tibrv_f64 timeout = TIBRV_WAIT_FOREVER);
    TibrvStatus destroy ();

    TibrvDispatchable* getDispatchable() const { return _dispatchable; }

    TibrvStatus setName (const char* name);
    TibrvStatus getName (const char*& name) const;

    tibrvDispatcher  getHandle() const { return _dispatcher; }

private:

    tibrvDispatcher    _dispatcher;
    TibrvDispatchable* _dispatchable;

    TibrvDispatcher(const TibrvDispatcher& dispatcher);
    TibrvDispatcher& operator=(const TibrvDispatcher& dispatcher);

};

//=====================================================================
// TibrvTransport
//=====================================================================

class TibrvTransport
{

public:

    virtual ~TibrvTransport();

    virtual tibrv_bool isValid() const;
    TibrvStatus destroy();

    TibrvStatus setDescription(const char* description);
    TibrvStatus getDescription(const char** description) const;

    TibrvStatus createInbox(char* subjectString, tibrv_u32 subjectLimit) const;

    virtual TibrvStatus send        (const TibrvMsg& message);
    virtual TibrvStatus sendRequest (const TibrvMsg& message,
                                     TibrvMsg& reply,
                                     tibrv_f64 timeout);
    virtual TibrvStatus sendReply   (const TibrvMsg& replyMessage,
                                     const TibrvMsg& requestMessage);


    tibrvTransport  getHandle() const { return _transport; }

protected:

    TibrvTransport();
    TibrvTransport(tibrvTransport transport);

    tibrvTransport  _transport;

private:

    TibrvTransport(const TibrvTransport& transport);
    TibrvTransport& operator=(const TibrvTransport& transport);

};


//=====================================================================
// TibrvProcessTransport
//
// In order to get [pointer to] process transport use Tibrv::processTransport().
//=====================================================================

class TibrvProcessTransport : public TibrvTransport
{

public:

    // no public constructors, destroy() made private.
    // Use Tibrv::processTransport() to get pointer
    // to the process transport object

    tibrv_bool  isValid() const
                    { return TIBRV_TRUE; }

private:

    TibrvProcessTransport() : TibrvTransport() {}
    TibrvProcessTransport(tibrvTransport transport) : TibrvTransport(transport) {}

    TibrvProcessTransport(const TibrvProcessTransport& transport);
    TibrvProcessTransport& operator=(const TibrvProcessTransport& transport);

    TibrvStatus destroy();

    friend class Tibrv;

};


//=====================================================================
// TibrvNetTransport
//=====================================================================

class TibrvNetTransport : public TibrvTransport
{

public:

    TibrvNetTransport();
    virtual ~TibrvNetTransport(){};

    TibrvStatus create   ( const char* service = NULL,
                           const char* network = NULL,
                           const char* daemon  = NULL );

    TibrvStatus createLicensed(
                            const char* service,
                            const char* network,
                            const char* daemon,
                            const char* license_ticket);

    TibrvStatus getService (const char*& serviceString) const;
    TibrvStatus getNetwork (const char*& networkString) const;
    TibrvStatus getDaemon  (const char*& daemonString ) const;
    TibrvStatus setBatchMode ( tibrvTransportBatchMode mode );

private:

    TibrvNetTransport(tibrvTransport transport) :
                TibrvTransport(transport) {}

    TibrvNetTransport(const TibrvNetTransport& transport);
    TibrvNetTransport& operator=(const TibrvNetTransport& transport);

    friend class Tibrv;
};

//=====================================================================
// TibrvVcTransport
//=====================================================================

class TibrvVcTransport : public TibrvTransport
{

public:

    TibrvVcTransport();
    virtual ~TibrvVcTransport(){};

    TibrvStatus createAcceptVc(const  char** connectSubject,
                               TibrvTransport* transport);

    TibrvStatus createConnectVc(const  char* connectSubject,
                                TibrvTransport* transport);

    TibrvStatus waitForVcConnection( tibrv_f64 timeout );

private:

    TibrvVcTransport(tibrvTransport transport) :
                TibrvTransport(transport) {}

    TibrvVcTransport(const TibrvVcTransport& transport);
    TibrvVcTransport& operator=(const TibrvVcTransport& transport);

    friend class Tibrv;
};


//=====================================================================
// TibrvEvent
//=====================================================================

class TibrvEvent
{

public:

    tibrv_bool    isValid() const;

    virtual TibrvStatus destroy(TibrvEventOnComplete* completeCB = NULL);

    // returns NULL if not created
    TibrvQueue*     getQueue     () const;
    void*           getClosure   () const;

    // convenience
    tibrv_bool     isListener () const;
    tibrv_bool     isTimer    () const;
    tibrv_bool     isIOEvent  () const;

    TibrvStatus    getType   (tibrvEventType &type) const;

    tibrvEvent     getHandle () const { return _event; }

protected:

    TibrvEvent();
    virtual ~TibrvEvent();

    tibrvEvent      _event;
    TibrvCallback*  _callback;

    TibrvEventOnComplete* _completeCallback;
    void *          _closure;
    TibrvQueue*     _queue;
    tibrvEventType  _objType;

    TibrvEvent(const TibrvEvent& event);
    TibrvEvent& operator=(const TibrvEvent& event);

    static void _listenCB (tibrvEvent event, tibrvMsg msg, void *closure);
    static void _timerCB  (tibrvEvent event, tibrvMsg msg, void *closure);
    static void _ioCB     (tibrvEvent event, tibrvMsg msg, void *closure);
    static void _completeCB(tibrvEvent event, void *closure);

};

//=====================================================================
// TibrvListener
//=====================================================================

class TibrvListener : public TibrvEvent
{

public:
             TibrvListener();
    virtual ~TibrvListener();

    // can supply TibrvCallback or TibrvMsgCallback
    TibrvStatus create (TibrvQueue* queue,
                        TibrvCallback* callback,
                        TibrvTransport* transport,
                        const char* subject,
                        const void* closure = NULL);

    // returns NULL if not created
    TibrvTransport*  getTransport() const;

    TibrvStatus  getSubject(const char *& subject) const;

private:

    TibrvTransport* _transport;

    TibrvListener(const TibrvListener& listener);
    TibrvListener& operator=(const TibrvListener& listener);

};

//=====================================================================
// TibrvTimer
//=====================================================================

class TibrvTimer : public TibrvEvent
{

public:
             TibrvTimer();
    virtual ~TibrvTimer();

    // can supply TibrvCallback or TibrvTimerCallback
    TibrvStatus create (TibrvQueue* queue,
                        TibrvCallback* callback,
                        tibrv_f64 interval,
                        const void* closure = NULL);

    TibrvStatus getInterval  (tibrv_f64& interval) const;
    TibrvStatus resetInterval(tibrv_f64  newInterval);

private:

    TibrvTimer(const TibrvTimer& timer);
    TibrvTimer& operator=(const TibrvTimer& timer);

};

//=====================================================================
// TibrvIOEvent
//=====================================================================

class TibrvIOEvent : public TibrvEvent
{

public:
             TibrvIOEvent();
    virtual ~TibrvIOEvent();

    // can supply TibrvCallback or TibrvIOCallback
    TibrvStatus create (TibrvQueue* queue,
                        TibrvCallback* callback,
                        tibrv_i32 socketId,
                        tibrvIOType ioType,
                        const void* closure = NULL);

    TibrvStatus  getIOSource (tibrv_i32& ioSource) const;
    TibrvStatus  getIOType   (tibrvIOType& ioType) const;

private:

    TibrvIOEvent(const TibrvIOEvent& ioEvent);
    TibrvIOEvent& operator=(const TibrvIOEvent& ioEvent);

};

//=====================================================================
// TibrvEventOnComplete
//=====================================================================

class TibrvEventOnComplete
{

public:
    TibrvEventOnComplete(){};

    virtual void onComplete(TibrvEvent* event) = 0;

};

//=====================================================================
// TibrvCallback
//=====================================================================

class TibrvCallback
{

public:
    TibrvCallback(){};

    // msg is empty and must not be used for timer and io callbacks
    virtual void onEvent(TibrvEvent* event, TibrvMsg& msg) = 0;

};


//=====================================================================
// TibrvMsgCallback
//
// If message should be used outside of callback it has to be detached:
//   TibrvMsg* newMsg = msg.detach(); // newMsg is owned by user program
//=====================================================================

class TibrvMsgCallback : public TibrvCallback
{

public:

    TibrvMsgCallback();

    virtual void onMsg(TibrvListener* listener, TibrvMsg& msg) = 0;

private:

    void onEvent(TibrvEvent* event, TibrvMsg& msg);

};

//=====================================================================
// TibrvTimerCallback
//=====================================================================

class TibrvTimerCallback : public TibrvCallback
{

public:

    TibrvTimerCallback();

    virtual void onTimer(TibrvTimer* timer) = 0;

private:

    void onEvent(TibrvEvent* event, TibrvMsg& msg);

};


//=====================================================================
// TibrvIOCallback
//=====================================================================

class TibrvIOCallback : public TibrvCallback
{

public:

    TibrvIOCallback();

    virtual void onIOEvent(TibrvIOEvent* ioEvent) = 0;

private:

    void onEvent(TibrvEvent* event, TibrvMsg& msg);

};


//=====================================================================
// TibrvMsgField
//
// Simply extends tibrvMsgField, i.e. in memory it is nothing
// more than the structure itself.
//=====================================================================

class TibrvMsgField : public tibrvMsgField
{

public:
    TibrvMsgField() { _zeroinit(); }
    TibrvMsgField(const TibrvMsgField& field);
    TibrvMsgField(const tibrvMsgField& field);

    const char*    getName  () const { return name; }
    tibrv_u32      getSize  () const { return size; }
    tibrv_u32      getCount () const { return count; }
    tibrv_u16      getId    () const { return id; }
    tibrv_u8       getType  () const { return type; }
    tibrvLocalData getData  () const { return data; }

    const TibrvMsgField& operator=(const tibrvMsgField& field);

private:
    void _zeroinit() { memset(this,0,sizeof(*this)); }

};

//=====================================================================
// TibrvMsgDateTime
//
// Simply extends TibrvMsgDateTime, i.e. in memory it is nothing
// more than the structure itself.
//=====================================================================

class TibrvMsgDateTime : public tibrvMsgDateTime
{

public:
    TibrvMsgDateTime() { sec=0; nsec=0; }
    TibrvMsgDateTime(const TibrvMsgDateTime& datetime)
                        { sec=datetime.sec; nsec=datetime.nsec; }
    TibrvMsgDateTime(const tibrvMsgDateTime& datetime)
                        { sec=datetime.sec; nsec=datetime.nsec; }

    const TibrvMsgDateTime& operator=(const tibrvMsgDateTime& datetime);

};

_TIBRV_INLINE tibrv_bool operator==(const TibrvMsgDateTime& t1, const TibrvMsgDateTime& t2);
_TIBRV_INLINE tibrv_bool operator==(const TibrvMsgDateTime& t1, const tibrvMsgDateTime& t2);
_TIBRV_INLINE tibrv_bool operator==(const tibrvMsgDateTime& t1, const TibrvMsgDateTime& t2);
_TIBRV_INLINE tibrv_bool operator==(const tibrvMsgDateTime& t1, const tibrvMsgDateTime& t2);
_TIBRV_INLINE tibrv_bool operator!=(const TibrvMsgDateTime& t1, const TibrvMsgDateTime& t2);
_TIBRV_INLINE tibrv_bool operator!=(const TibrvMsgDateTime& t1, const tibrvMsgDateTime& t2);
_TIBRV_INLINE tibrv_bool operator!=(const tibrvMsgDateTime& t1, const TibrvMsgDateTime& t2);
_TIBRV_INLINE tibrv_bool operator!=(const tibrvMsgDateTime& t1, const tibrvMsgDateTime& t2);

//=====================================================================
// TibrvMsg
//
// Notice TibrvMsg does not have create() and destroy(). They
// called automatically.
//
// If the TibrvMsg constructor fails it does not throw any exceptions.
// Instead users may call getStatus() method to determine if the message
// was constructed Ok. In case constructor fails every subsequent call
// returns same error code as getStatus() (notice that if getStatus()
// returns TIBRV_OK it does not mean subsequent calls won't fail).
//=====================================================================

class TibrvMsg
{

public:

    // Creates empty message
    TibrvMsg(tibrv_u32 initialSize=512);

    // Creates an independent copy of the message
    TibrvMsg(const TibrvMsg& msg);

    // Creates an independent copy from bytes
    // previously obtained with getAsBytes
    TibrvMsg(const void* bytes);

    // Creates TibrvMsg from C-level message handle.
    // Second parameter must specify if given tibrvMsg
    // is detached or attached. TIBRV_TRUE if detached.
    TibrvMsg(tibrvMsg msg, tibrv_bool detached);

    // Destroys the underlying message if detached
    virtual ~TibrvMsg();

    // Returns TIBRV_OK if constructed Ok
    // or specific error occured inside the constructor
    TibrvStatus getStatus() const { return _status; }

    // Returns pointer to the new detached message and
    // invalidates (sets to empty) the source message.
    // Returns NULL if not attached, out of memory, or failed.
    // Messages created by user program cannot be detached.
    TibrvMsg*    detach();

    tibrv_bool   isDetached() const;

    TibrvStatus  expand(tibrv_u32 additionalStorage);

    TibrvStatus  reset();
    TibrvStatus  markReferences();
    TibrvStatus  clearReferences();

    TibrvStatus  getNumFields  (tibrv_u32& numFields) const;

    TibrvStatus  convertToString (const char*& string);

    TibrvStatus  setSendSubject  (const char* subject);
    TibrvStatus  getSendSubject  (const char*& subject);

    TibrvStatus  setReplySubject (const char* replySubject);
    TibrvStatus  getReplySubject (const char*& replySubject);

    TibrvStatus  getByteSize     (tibrv_u32& byteSize) const;
    TibrvStatus  getAsBytes      (const void*& bytePtr);
    TibrvStatus  getAsBytesCopy  (void* bytePtr, tibrv_u32 byteSize);

    TibrvStatus  createCopy      (TibrvMsg& copy);

    static TibrvStatus getCurrentTime  (TibrvMsgDateTime& dateTime);

    // Access to C level handle
    tibrvMsg     getHandle() const { return _msg; }

    // Notice that vast variety of types does not allow
    // to use generic names for "add", "get" and "update"
    // methods as it would lead to compiler not being able
    // to pick the right method.
    // Consequently the best approach was to just
    // use same pattern as C-level API.

    // Adding fields

    TibrvStatus  addField   (const TibrvMsgField& field);
    TibrvStatus  addMsg     (const char* fieldName, const TibrvMsg& msg, tibrv_u16 fieldId=0);
    TibrvStatus  addString  (const char* fieldName, const char* value,tibrv_u16 fieldId=0);
    TibrvStatus  addOpaque  (const char* fieldName, const void* value, tibrv_u32 size, tibrv_u16 fieldId=0);
    TibrvStatus  addBool    (const char* fieldName, tibrv_bool value, tibrv_u16 fieldId=0);
    TibrvStatus  addDateTime(const char* fieldName, const TibrvMsgDateTime& value, tibrv_u16 fieldId=0);
    TibrvStatus  addIPPort16(const char* fieldName, tibrv_ipport16 value, tibrv_u16 fieldId=0);
    TibrvStatus  addIPAddr32(const char* fieldName, tibrv_ipaddr32 value, tibrv_u16 fieldId=0);
    TibrvStatus  addXml     (const char* fieldName, const void* value, tibrv_u32 size, tibrv_u16 fieldId=0);

    TibrvStatus  addI8    (const char* fieldName, tibrv_i8  value, tibrv_u16 fieldId=0);
    TibrvStatus  addU8    (const char* fieldName, tibrv_u8  value, tibrv_u16 fieldId=0);
    TibrvStatus  addI16   (const char* fieldName, tibrv_i16 value, tibrv_u16 fieldId=0);
    TibrvStatus  addU16   (const char* fieldName, tibrv_u16 value, tibrv_u16 fieldId=0);
    TibrvStatus  addI32   (const char* fieldName, tibrv_i32 value, tibrv_u16 fieldId=0);
    TibrvStatus  addU32   (const char* fieldName, tibrv_u32 value, tibrv_u16 fieldId=0);
    TibrvStatus  addI64   (const char* fieldName, tibrv_i64 value, tibrv_u16 fieldId=0);
    TibrvStatus  addU64   (const char* fieldName, tibrv_u64 value, tibrv_u16 fieldId=0);
    TibrvStatus  addF32   (const char* fieldName, tibrv_f32 value, tibrv_u16 fieldId=0);
    TibrvStatus  addF64   (const char* fieldName, tibrv_f64 value, tibrv_u16 fieldId=0);

    TibrvStatus  addI8Array (const char* fieldName, const tibrv_i8*  value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus  addU8Array (const char* fieldName, const tibrv_u8*  value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus  addI16Array(const char* fieldName, const tibrv_i16* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus  addU16Array(const char* fieldName, const tibrv_u16* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus  addI32Array(const char* fieldName, const tibrv_i32* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus  addU32Array(const char* fieldName, const tibrv_u32* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus  addI64Array(const char* fieldName, const tibrv_i64* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus  addU64Array(const char* fieldName, const tibrv_u64* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus  addF32Array(const char* fieldName, const tibrv_f32* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus  addF64Array(const char* fieldName, const tibrv_f64* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);

    // Deleting fields

    TibrvStatus  removeField    (const char* fieldName, tibrv_u16 fieldId=0);
    TibrvStatus  removeFieldInstance (const char* fieldName, tibrv_u32 instance);

    // Getting fields by field number and instances

    TibrvStatus  getFieldByIndex  (TibrvMsgField& field, tibrv_u32 fieldIndex);
    TibrvStatus  getFieldInstance (const char* fieldName, TibrvMsgField& fieldAddr, tibrv_u32 instance);

    // Finding fields

    TibrvStatus  getField   (const char* fieldName, TibrvMsgField& field, tibrv_u16 fieldId=0);
    TibrvStatus  getMsg     (const char* fieldName, TibrvMsg& subMessage, tibrv_u16 fieldId=0);
    TibrvStatus  getString  (const char* fieldName, const char*& value, tibrv_u16 fieldId=0);
    TibrvStatus  getOpaque  (const char* fieldName, const void*& value, tibrv_u32& size, tibrv_u16 fieldId=0);
    TibrvStatus  getBool    (const char* fieldName, tibrv_bool& value, tibrv_u16 fieldId=0);
    TibrvStatus  getDateTime(const char* fieldName, TibrvMsgDateTime& value, tibrv_u16 fieldId=0);
    TibrvStatus  getIPPort16(const char* fieldName, tibrv_ipport16& value, tibrv_u16 fieldId=0);
    TibrvStatus  getIPAddr32(const char* fieldName, tibrv_ipaddr32& value, tibrv_u16 fieldId=0);
    TibrvStatus  getXml     (const char* fieldName, const void*& value, tibrv_u32& size, tibrv_u16 fieldId=0);

    TibrvStatus  getI8 (const char* fieldName, tibrv_i8&  value, tibrv_u16 fieldId=0);
    TibrvStatus  getU8 (const char* fieldName, tibrv_u8&  value, tibrv_u16 fieldId=0);
    TibrvStatus  getI16(const char* fieldName, tibrv_i16& value, tibrv_u16 fieldId=0);
    TibrvStatus  getU16(const char* fieldName, tibrv_u16& value, tibrv_u16 fieldId=0);
    TibrvStatus  getI32(const char* fieldName, tibrv_i32& value, tibrv_u16 fieldId=0);
    TibrvStatus  getU32(const char* fieldName, tibrv_u32& value, tibrv_u16 fieldId=0);
    TibrvStatus  getI64(const char* fieldName, tibrv_i64& value, tibrv_u16 fieldId=0);
    TibrvStatus  getU64(const char* fieldName, tibrv_u64& value, tibrv_u16 fieldId=0);
    TibrvStatus  getF32(const char* fieldName, tibrv_f32& value, tibrv_u16 fieldId=0);
    TibrvStatus  getF64(const char* fieldName, tibrv_f64& value, tibrv_u16 fieldId=0);

    TibrvStatus  getI8Array (const char* fieldName, const tibrv_i8*&  array, tibrv_u32& numElements, tibrv_u16 fieldId=0);
    TibrvStatus  getU8Array (const char* fieldName, const tibrv_u8*&  array, tibrv_u32& numElements, tibrv_u16 fieldId=0);
    TibrvStatus  getI16Array(const char* fieldName, const tibrv_i16*& array, tibrv_u32& numElements, tibrv_u16 fieldId=0);
    TibrvStatus  getU16Array(const char* fieldName, const tibrv_u16*& array, tibrv_u32& numElements, tibrv_u16 fieldId=0);
    TibrvStatus  getI32Array(const char* fieldName, const tibrv_i32*& array, tibrv_u32& numElements, tibrv_u16 fieldId=0);
    TibrvStatus  getU32Array(const char* fieldName, const tibrv_u32*& array, tibrv_u32& numElements, tibrv_u16 fieldId=0);
    TibrvStatus  getI64Array(const char* fieldName, const tibrv_i64*& array, tibrv_u32& numElements, tibrv_u16 fieldId=0);
    TibrvStatus  getU64Array(const char* fieldName, const tibrv_u64*& array, tibrv_u32& numElements, tibrv_u16 fieldId=0);
    TibrvStatus  getF32Array(const char* fieldName, const tibrv_f32*& array, tibrv_u32& numElements, tibrv_u16 fieldId=0);
    TibrvStatus  getF64Array(const char* fieldName, const tibrv_f64*& array, tibrv_u32& numElements, tibrv_u16 fieldId=0);


    // Updating fields

    TibrvStatus updateField   (const TibrvMsgField& field);
    TibrvStatus updateMsg     (const char* fieldName, const TibrvMsg& msg, tibrv_u16 fieldId=0);
    TibrvStatus updateString  (const char* fieldName, const char* value, tibrv_u16 fieldId=0);
    TibrvStatus updateOpaque  (const char* fieldName, const void* value, tibrv_u32 size, tibrv_u16 fieldId=0);
    TibrvStatus updateBool    (const char* fieldName, tibrv_bool value, tibrv_u16 fieldId=0);
    TibrvStatus updateDateTime(const char* fieldName, const TibrvMsgDateTime& value, tibrv_u16 fieldId=0);
    TibrvStatus updateIPPort16(const char* fieldName, tibrv_ipport16 value, tibrv_u16 fieldId=0);
    TibrvStatus updateIPAddr32(const char* fieldName, tibrv_ipaddr32 value, tibrv_u16 fieldId=0);
    TibrvStatus updateXml     (const char* fieldName, const void* value, tibrv_u32 size, tibrv_u16 fieldId=0);

    TibrvStatus updateI8 (const char* fieldName, tibrv_i8  value, tibrv_u16 fieldId=0);
    TibrvStatus updateU8 (const char* fieldName, tibrv_u8  value, tibrv_u16 fieldId=0);
    TibrvStatus updateI16(const char* fieldName, tibrv_i16 value, tibrv_u16 fieldId=0);
    TibrvStatus updateU16(const char* fieldName, tibrv_u16 value, tibrv_u16 fieldId=0);
    TibrvStatus updateI32(const char* fieldName, tibrv_i32 value, tibrv_u16 fieldId=0);
    TibrvStatus updateU32(const char* fieldName, tibrv_u32 value, tibrv_u16 fieldId=0);
    TibrvStatus updateI64(const char* fieldName, tibrv_i64 value, tibrv_u16 fieldId=0);
    TibrvStatus updateU64(const char* fieldName, tibrv_u64 value, tibrv_u16 fieldId=0);
    TibrvStatus updateF32(const char* fieldName, tibrv_f32 value, tibrv_u16 fieldId=0);
    TibrvStatus updateF64(const char* fieldName, tibrv_f64 value, tibrv_u16 fieldId=0);

    TibrvStatus updateI8Array (const char* fieldName, const tibrv_i8*  value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus updateU8Array (const char* fieldName, const tibrv_u8*  value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus updateI16Array(const char* fieldName, const tibrv_i16* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus updateU16Array(const char* fieldName, const tibrv_u16* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus updateI32Array(const char* fieldName, const tibrv_i32* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus updateU32Array(const char* fieldName, const tibrv_u32* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus updateI64Array(const char* fieldName, const tibrv_i64* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus updateU64Array(const char* fieldName, const tibrv_u64* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus updateF32Array(const char* fieldName, const tibrv_f32* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);
    TibrvStatus updateF64Array(const char* fieldName, const tibrv_f64* value, tibrv_u32 numElements, tibrv_u16 fieldId=0);

    // internal use
    void  attach(tibrvMsg msg, tibrv_bool detached = TIBRV_FALSE);

private:

    tibrv_status _create();
    TibrvMsg& operator=(const TibrvMsg& msg);

    tibrvMsg     _msg;
    tibrv_bool   _detached;
    tibrv_status _status;
    tibrv_u32    _initsize;

};

//=====================================================================
// inline implementations
//=====================================================================

_TIBRV_INLINE TibrvStatus::TibrvStatus() {_status = TIBRV_OK;}
_TIBRV_INLINE tibrv_status  TibrvStatus::getCode () const { return _status; }
_TIBRV_INLINE const char*  TibrvStatus::getText () const { return tibrvStatus_GetText(_status); }
_TIBRV_INLINE TibrvStatus::operator tibrv_status() const {return _status;}

_TIBRV_INLINE TibrvQueue* Tibrv::defaultQueue()     { return &_defaultQueue; }
_TIBRV_INLINE TibrvProcessTransport* Tibrv::processTransport() { return &_processTransport; }

_TIBRV_INLINE TibrvQueue* TibrvEvent::getQueue() const { return _queue; }
_TIBRV_INLINE void* TibrvEvent::getClosure() const {return _closure;}

_TIBRV_INLINE  TibrvMsgField::TibrvMsgField(const TibrvMsgField& field)
    { memcpy(this,&field,sizeof(field)); }
_TIBRV_INLINE  TibrvMsgField::TibrvMsgField(const tibrvMsgField& field)
    { memcpy(this,&field,sizeof(field)); }
_TIBRV_INLINE const TibrvMsgField& TibrvMsgField::operator=(const tibrvMsgField& field)
    { memcpy(this,&field,sizeof(field)); return *this; }

_TIBRV_INLINE const TibrvMsgDateTime& TibrvMsgDateTime::operator=(const tibrvMsgDateTime& datetime)
    { sec = datetime.sec; nsec = datetime.nsec; return *this; }
_TIBRV_INLINE tibrv_bool operator==(const TibrvMsgDateTime& t1, const TibrvMsgDateTime& t2)
    { return ((t1.sec==t2.sec)&&(t1.nsec==t2.nsec)) ? TIBRV_TRUE : TIBRV_FALSE; }
_TIBRV_INLINE tibrv_bool operator==(const TibrvMsgDateTime& t1, const tibrvMsgDateTime& t2)
    { return ((t1.sec==t2.sec)&&(t1.nsec==t2.nsec)) ? TIBRV_TRUE : TIBRV_FALSE; }
_TIBRV_INLINE tibrv_bool operator==(const tibrvMsgDateTime& t1, const TibrvMsgDateTime& t2)
    { return ((t1.sec==t2.sec)&&(t1.nsec==t2.nsec)) ? TIBRV_TRUE : TIBRV_FALSE; }
_TIBRV_INLINE tibrv_bool operator==(const tibrvMsgDateTime& t1, const tibrvMsgDateTime& t2)
    { return ((t1.sec==t2.sec)&&(t1.nsec==t2.nsec)) ? TIBRV_TRUE : TIBRV_FALSE; }

_TIBRV_INLINE tibrv_bool operator!=(const TibrvMsgDateTime& t1, const TibrvMsgDateTime& t2)
    { return (t1 == t2) ? TIBRV_FALSE : TIBRV_TRUE; }
_TIBRV_INLINE tibrv_bool operator!=(const TibrvMsgDateTime& t1, const tibrvMsgDateTime& t2)
    { return (t1 == t2) ? TIBRV_FALSE : TIBRV_TRUE; }
_TIBRV_INLINE tibrv_bool operator!=(const tibrvMsgDateTime& t1, const TibrvMsgDateTime& t2)
    { return (t1 == t2) ? TIBRV_FALSE : TIBRV_TRUE; }
_TIBRV_INLINE tibrv_bool operator!=(const tibrvMsgDateTime& t1, const tibrvMsgDateTime& t2)
    { return (t1 == t2) ? TIBRV_FALSE : TIBRV_TRUE; }

_TIBRV_INLINE tibrv_bool TibrvMsg::isDetached() const { return _detached; }

//=====================================================================

#endif   /* _INCLUDED_tibrvcpp_h  */

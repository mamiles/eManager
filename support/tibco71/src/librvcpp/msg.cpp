
/*
 * Copyright (c) 1995-2000 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)msg.cpp	2.17
 *
 */

#include "tibrvcpp.h"

//=====================================================================
// TibrvMsg
//=====================================================================

TibrvMsg::TibrvMsg(tibrv_u32 initialSize)
{
    _msg = NULL;
    _detached = TIBRV_TRUE;
    _initsize = initialSize;
    _status = TIBRV_OK;
}

TibrvMsg::TibrvMsg(const TibrvMsg& msg)
{
    _msg = NULL;
    _detached = TIBRV_TRUE;
    _initsize = 0;

    const void* bytes;

    if ((_status = tibrvMsg_GetAsBytes(msg.getHandle(),&bytes)) != TIBRV_OK)
        return;

    if ((_status = tibrvMsg_CreateFromBytes(&_msg,bytes)) != TIBRV_OK)
        _msg = NULL;
}

TibrvMsg::TibrvMsg(const void* bytes)
{
    _msg = NULL;
    _detached = TIBRV_TRUE;
    _initsize = 0;

    if ((_status = tibrvMsg_CreateFromBytes(&_msg,bytes)) != TIBRV_OK)
    {
        _msg = NULL;
        return;
    }
}

TibrvMsg::TibrvMsg(tibrvMsg msg, tibrv_bool detached)
{
    _msg = NULL;
    _detached = TIBRV_TRUE;
    _initsize = 0;

    attach(msg,detached);
}

TibrvMsg::~TibrvMsg()
{
    if ((_detached == TIBRV_TRUE) && (_msg != NULL))
        tibrvMsg_Destroy(_msg);
}

tibrv_status TibrvMsg::_create()
{
    if ((_msg == NULL) && (_status == TIBRV_OK))
    {
        _status = tibrvMsg_CreateEx(&_msg,_initsize);
        if (_status != TIBRV_OK)
            _msg = NULL;
    }
    return _status;
}

void
TibrvMsg::attach(tibrvMsg msg, tibrv_bool detached)
{
    if ((_detached == TIBRV_TRUE) && (_msg != NULL))
        tibrvMsg_Destroy(_msg);

    _msg = msg;
    _detached = detached;
    _status = TIBRV_OK;
    _initsize = 0;
}

TibrvMsg*
TibrvMsg::detach()
{
    if ((_detached == TIBRV_TRUE) || (_status != TIBRV_OK))
        return NULL;

    // allocate new object
    TibrvMsg * newmsg = new TibrvMsg();
    if (newmsg == NULL) return NULL;

    // detach if not yet detached
    if (_detached == TIBRV_FALSE) // this is redundand but may be needed
    {
        TibrvStatus status;
        if (_msg != NULL)
        {
            if ((status = tibrvMsg_Detach(_msg)) != TIBRV_OK)
            {
                delete newmsg;
                return NULL;
            }
        }
        _detached = TIBRV_TRUE;
    }

    newmsg->attach(_msg,TIBRV_TRUE);

    //  invalidate this handle so we won't try to delete it
    _msg=NULL;
    _status = TIBRV_NOT_INITIALIZED;
    _detached = TIBRV_TRUE;

    return newmsg;
}

TibrvStatus
TibrvMsg::expand(tibrv_u32 additionalStorage)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_Expand(_msg,additionalStorage);
}


TibrvStatus
TibrvMsg::getNumFields(tibrv_u32& numFields) const
{
    if ((_msg==NULL) && (_status==TIBRV_OK))
    {
        numFields = 0;
        return TIBRV_OK;
    }
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetNumFields(_msg,&numFields);
}

TibrvStatus
TibrvMsg::getByteSize(tibrv_u32& byteSize) const
{
    if ((_msg==NULL) && (_status==TIBRV_OK))
    {
        byteSize = 0;
        return TIBRV_OK;
    }
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetByteSize(_msg,&byteSize);
}

TibrvStatus
TibrvMsg::convertToString(const char*& string)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_ConvertToString(_msg,&string);
}

TibrvStatus
TibrvMsg::setSendSubject(const char* subject)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_SetSendSubject(_msg,(char*)subject);
}

TibrvStatus
TibrvMsg::getSendSubject(const char*& subject)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetSendSubject(_msg,&subject);
}

TibrvStatus
TibrvMsg::setReplySubject(const char* replySubject)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_SetReplySubject(_msg,(char*)replySubject);
}

TibrvStatus
TibrvMsg::getReplySubject(const char*& replySubject)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetReplySubject(_msg,&replySubject);
}

TibrvStatus
TibrvMsg::getAsBytes(const void*& bytePtr)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetAsBytes(_msg,&bytePtr);
}

TibrvStatus
TibrvMsg::getAsBytesCopy(void* bytePtr,tibrv_u32 byteSize)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetAsBytesCopy(_msg,bytePtr,byteSize);
}

TibrvStatus
TibrvMsg::reset()
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_Reset(_msg);
}

TibrvStatus
TibrvMsg::markReferences()
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_MarkReferences(_msg);
}

TibrvStatus
TibrvMsg::clearReferences()
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_ClearReferences(_msg);
}

TibrvStatus
TibrvMsg::getCurrentTime(TibrvMsgDateTime& dateTime)
{
    return tibrvMsg_GetCurrentTime((tibrvMsgDateTime*)&dateTime);
}

TibrvStatus
TibrvMsg::createCopy(TibrvMsg& copy)
{
    _create();
    if (_status != TIBRV_OK) return _status;
    tibrvMsg newmsg;
    tibrv_status status = tibrvMsg_CreateCopy(_msg,&newmsg);
    if (status == TIBRV_OK)
        copy.attach(newmsg,TIBRV_TRUE);
    return status;
}


//---------------------------------------------------------------------
// add
//---------------------------------------------------------------------

TibrvStatus
TibrvMsg::addField(const TibrvMsgField& field)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddField(_msg,(tibrvMsgField*)&field);
}

TibrvStatus
TibrvMsg::addMsg(const char* fieldName,const TibrvMsg& msg, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddMsgEx(_msg,fieldName,msg.getHandle(),fieldId);
}

TibrvStatus
TibrvMsg::addString(const char* fieldName, const char* value,tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddStringEx(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addOpaque(const char* fieldName, const void* value, tibrv_u32 size, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddOpaqueEx(_msg,fieldName,value,size,fieldId);
}

TibrvStatus
TibrvMsg::addBool(const char* fieldName, tibrv_bool value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddBoolEx(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addDateTime(const char* fieldName,const TibrvMsgDateTime& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddDateTimeEx(_msg,fieldName,
                                  (tibrvMsgDateTime*)&value,fieldId);
}

TibrvStatus
TibrvMsg::addIPPort16 (const char* fieldName, tibrv_ipport16 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddIPPort16Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addIPAddr32 (const char* fieldName, tibrv_ipaddr32 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddIPAddr32Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addXml(const char* fieldName, const void* value, tibrv_u32 size, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddXmlEx(_msg,fieldName,value,size,fieldId);
}

TibrvStatus
TibrvMsg::addI8(const char* fieldName, tibrv_i8 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddI8Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addU8(const char* fieldName, tibrv_u8 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddU8Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addI16(const char* fieldName, tibrv_i16 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddI16Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addU16(const char* fieldName, tibrv_u16 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddU16Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addI32(const char* fieldName, tibrv_i32 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddI32Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addU32(const char* fieldName, tibrv_u32 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddU32Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addI64(const char* fieldName, tibrv_i64 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddI64Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addU64(const char* fieldName, tibrv_u64 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddU64Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addF32(const char* fieldName, tibrv_f32 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddF32Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addF64(const char* fieldName, tibrv_f64 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddF64Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::addI8Array(const char* fieldName, const tibrv_i8* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddI8ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::addU8Array(const char* fieldName, const tibrv_u8* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddU8ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::addI16Array(const char* fieldName, const tibrv_i16* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddI16ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::addU16Array(const char* fieldName, const tibrv_u16* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddU16ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::addI32Array(const char* fieldName, const tibrv_i32* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddI32ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::addU32Array(const char* fieldName, const tibrv_u32* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddU32ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::addI64Array(const char* fieldName, const tibrv_i64* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddI64ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::addU64Array(const char* fieldName, const tibrv_u64* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddU64ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::addF32Array(const char* fieldName, const tibrv_f32* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddF32ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::addF64Array(const char* fieldName, const tibrv_f64* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_AddF64ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

//---------------------------------------------------------------------
// get
//---------------------------------------------------------------------

TibrvStatus
TibrvMsg::getFieldByIndex(TibrvMsgField& field, tibrv_u32 fieldIndex)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetFieldByIndex(_msg,(tibrvMsgField*)&field,fieldIndex);
}

TibrvStatus
TibrvMsg::getFieldInstance(const char* fieldName, TibrvMsgField& fieldAddr, tibrv_u32 instance)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetFieldInstance(_msg,fieldName,
                                     (tibrvMsgField*)&fieldAddr,instance);
}

TibrvStatus
TibrvMsg::getField(const char* fieldName, TibrvMsgField& field, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetFieldEx(_msg,fieldName,
                               (tibrvMsgField*)&field,fieldId);
}

TibrvStatus
TibrvMsg::getMsg(const char* fieldName, TibrvMsg& subMessage, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    TibrvStatus status;
    tibrvMsg msg;
    status = tibrvMsg_GetMsgEx(_msg,fieldName,&msg,fieldId);
    if (status != TIBRV_OK)
        return status;
    subMessage.attach(msg);
    return TIBRV_OK;
}

TibrvStatus
TibrvMsg::getString(const char* fieldName, const char*& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetStringEx(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getOpaque(const char* fieldName, const void*& value, tibrv_u32& size, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetOpaqueEx(_msg,fieldName,&value,&size,fieldId);
}

TibrvStatus
TibrvMsg::getBool(const char* fieldName, tibrv_bool& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetBoolEx(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getDateTime(const char* fieldName, TibrvMsgDateTime& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetDateTimeEx(_msg,fieldName,
                                  (tibrvMsgDateTime*)&value,fieldId);
}

TibrvStatus
TibrvMsg::getIPPort16(const char* fieldName, tibrv_ipport16& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetIPPort16Ex(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getIPAddr32(const char* fieldName, tibrv_ipaddr32& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetIPAddr32Ex(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getXml(const char* fieldName, const void*& value, tibrv_u32& size, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetXmlEx(_msg,fieldName,&value,&size,fieldId);
}

TibrvStatus
TibrvMsg::getI8(const char* fieldName, tibrv_i8& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetI8Ex(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getU8(const char* fieldName, tibrv_u8& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetU8Ex(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getI16(const char* fieldName, tibrv_i16& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetI16Ex(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getU16(const char* fieldName, tibrv_u16& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetU16Ex(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getI32(const char* fieldName, tibrv_i32& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetI32Ex(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getU32(const char* fieldName, tibrv_u32& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetU32Ex(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getI64(const char* fieldName, tibrv_i64& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetI64Ex(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getU64(const char* fieldName, tibrv_u64& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetU64Ex(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getF32(const char* fieldName, tibrv_f32& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetF32Ex(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getF64(const char* fieldName, tibrv_f64& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetF64Ex(_msg,fieldName,&value,fieldId);
}

TibrvStatus
TibrvMsg::getI8Array(const char* fieldName, const tibrv_i8*& array, tibrv_u32& numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetI8ArrayEx(_msg,fieldName,&array,&numElements,fieldId);
}

TibrvStatus
TibrvMsg::getU8Array(const char* fieldName, const tibrv_u8*& array, tibrv_u32& numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetU8ArrayEx(_msg,fieldName,&array,&numElements,fieldId);
}

TibrvStatus
TibrvMsg::getI16Array(const char* fieldName, const tibrv_i16*& array, tibrv_u32& numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetI16ArrayEx(_msg,fieldName,&array,&numElements,fieldId);
}

TibrvStatus
TibrvMsg::getU16Array(const char* fieldName, const tibrv_u16*& array, tibrv_u32& numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetU16ArrayEx(_msg,fieldName,&array,&numElements,fieldId);
}

TibrvStatus
TibrvMsg::getI32Array(const char* fieldName, const tibrv_i32*& array, tibrv_u32& numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetI32ArrayEx(_msg,fieldName,&array,&numElements,fieldId);
}

TibrvStatus
TibrvMsg::getU32Array(const char* fieldName, const tibrv_u32*& array, tibrv_u32& numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetU32ArrayEx(_msg,fieldName,&array,&numElements,fieldId);
}

TibrvStatus
TibrvMsg::getI64Array(const char* fieldName, const tibrv_i64*& array, tibrv_u32& numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetI64ArrayEx(_msg,fieldName,&array,&numElements,fieldId);
}

TibrvStatus
TibrvMsg::getU64Array(const char* fieldName, const tibrv_u64*& array, tibrv_u32& numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetU64ArrayEx(_msg,fieldName,&array,&numElements,fieldId);
}

TibrvStatus
TibrvMsg::getF32Array(const char* fieldName, const tibrv_f32*& array, tibrv_u32& numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetF32ArrayEx(_msg,fieldName,&array,&numElements,fieldId);
}

TibrvStatus
TibrvMsg::getF64Array(const char* fieldName, const tibrv_f64*& array, tibrv_u32& numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_GetF64ArrayEx(_msg,fieldName,&array,&numElements,fieldId);
}

//---------------------------------------------------------------------
// remove
//---------------------------------------------------------------------

TibrvStatus
TibrvMsg::removeField(const char* fieldName, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_RemoveFieldEx(_msg,fieldName,fieldId);
}

TibrvStatus
TibrvMsg::removeFieldInstance(const char* fieldName, tibrv_u32 instance)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_RemoveFieldInstance(_msg,fieldName,instance);
}

//---------------------------------------------------------------------
// update
//---------------------------------------------------------------------

TibrvStatus
TibrvMsg::updateField(const TibrvMsgField& field)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateField(_msg,(tibrvMsgField*)&field);
}

TibrvStatus
TibrvMsg::updateMsg(const char* fieldName,const TibrvMsg& msg, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateMsgEx(_msg,fieldName,msg.getHandle(),fieldId);
}

TibrvStatus
TibrvMsg::updateString(const char* fieldName, const char* value,tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateStringEx(_msg,fieldName,(char*)value,fieldId);
}

TibrvStatus
TibrvMsg::updateOpaque(const char* fieldName, const void* value, tibrv_u32 size, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateOpaqueEx(_msg,fieldName,(void*)value,size,fieldId);
}

TibrvStatus
TibrvMsg::updateBool(const char* fieldName, tibrv_bool value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateBoolEx(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateDateTime(const char* fieldName, const TibrvMsgDateTime& value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateDateTimeEx(_msg,fieldName,
                                     (tibrvMsgDateTime*)&value,fieldId);
}

TibrvStatus
TibrvMsg::updateIPPort16(const char* fieldName, tibrv_ipport16 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateIPPort16Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateIPAddr32(const char* fieldName, tibrv_ipaddr32 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateIPAddr32Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateXml(const char* fieldName, const void* value, tibrv_u32 size, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateXmlEx(_msg,fieldName,(void*)value,size,fieldId);
}

TibrvStatus
TibrvMsg::updateI8(const char* fieldName, tibrv_i8 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateI8Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateU8(const char* fieldName, tibrv_u8 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateU8Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateI16(const char* fieldName, tibrv_i16 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateI16Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateU16(const char* fieldName, tibrv_u16 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateU16Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateI32(const char* fieldName, tibrv_i32 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateI32Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateU32(const char* fieldName, tibrv_u32 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateU32Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateI64(const char* fieldName, tibrv_i64 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateI64Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateU64(const char* fieldName, tibrv_u64 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateU64Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateF32(const char* fieldName, tibrv_f32 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateF32Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateF64(const char* fieldName, tibrv_f64 value, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateF64Ex(_msg,fieldName,value,fieldId);
}

TibrvStatus
TibrvMsg::updateI8Array(const char* fieldName, const tibrv_i8* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateI8ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::updateU8Array(const char* fieldName, const tibrv_u8* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateU8ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::updateI16Array(const char* fieldName, const tibrv_i16* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateI16ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::updateU16Array(const char* fieldName, const tibrv_u16* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateU16ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::updateI32Array(const char* fieldName, const tibrv_i32* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateI32ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::updateU32Array(const char* fieldName, const tibrv_u32* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateU32ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::updateI64Array(const char* fieldName, const tibrv_i64* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateI64ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::updateU64Array(const char* fieldName, const tibrv_u64* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateU64ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::updateF32Array(const char* fieldName, const tibrv_f32* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateF32ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvStatus
TibrvMsg::updateF64Array(const char* fieldName, const tibrv_f64* value, tibrv_u32 numElements, tibrv_u16 fieldId)
{
    _create();
    if (_status != TIBRV_OK) return TibrvStatus(_status);
    return tibrvMsg_UpdateF64ArrayEx(_msg,fieldName,value,numElements,fieldId);
}

TibrvMsg& TibrvMsg::operator=(const TibrvMsg& msg)
{
    _msg = msg.getHandle();
    return *this;
}

//=====================================================================

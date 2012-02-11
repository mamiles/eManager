//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiStatus.h>

AmiStatus& 
AmiStatus::operator=(const ami_ErrorCode errorCode)
{
  _status = ami_ErrorCreate(errorCode, NULL);
  return (*this);
}

AmiStatus& 
AmiStatus::operator=(const ami_Error status)
{
  _status = status;
  return (*this);
}

AmiStatus& 
AmiStatus::operator=(const AmiStatus& status)
{
  _status = status.getAmiError();
  return (*this);
}

ami_Boolean 
AmiStatus::operator==(const AmiStatus& status)
{
  ami_Boolean b = AMI_FALSE;

  if (ami_ErrorGetCode(_status) == ami_ErrorGetCode(status.getAmiError()))
  {
    b = AMI_TRUE; 
  }

  return b;
}

ami_Boolean 
AmiStatus::operator!=(const AmiStatus& status)
{
  ami_Boolean b = AMI_FALSE;

  if (ami_ErrorGetCode(_status) != ami_ErrorGetCode(status.getAmiError()))
  {
    b = AMI_TRUE; 
  }

  return b;
}

ami_Boolean 
AmiStatus::operator==(const ami_Error status)
{
  ami_Boolean b = AMI_FALSE;

  if (ami_ErrorGetCode(_status) == ami_ErrorGetCode(status))
  {
    b = AMI_TRUE; 
  }

  return b;
}

ami_Boolean 
AmiStatus::operator!=(const ami_Error status)
{
  ami_Boolean b = AMI_FALSE;

  if (ami_ErrorGetCode(_status) != ami_ErrorGetCode(status))
  {
    b = AMI_TRUE; 
  }

  return b;
}

ami_Boolean 
AmiStatus::operator==(const ami_ErrorCode errorCode)
{
  ami_Boolean b = AMI_FALSE;

  if (ami_ErrorGetCode(_status) == errorCode)
  {
    b = AMI_TRUE; 
  }

  return b;
}

ami_Boolean 
AmiStatus::operator!=(const ami_ErrorCode errorCode)
{
  ami_Boolean b = AMI_FALSE;

  if (ami_ErrorGetCode(_status) != errorCode)
  {
    b = AMI_TRUE; 
  }

  return b;
}

ami_Boolean 
AmiStatus::ok(void) const
{
  ami_Boolean b = AMI_TRUE;

  if (_status != AMI_OK)
  {
    b = AMI_FALSE;
  }

  return b;
}

ami_Boolean
AmiStatus::operator!() const
{
  ami_Boolean b = AMI_FALSE;

  if (_status != AMI_OK)
  {
    b = AMI_TRUE;
  }

  return b;
}

void 
AmiStatus::stamp(const char * inpFilename, int inLineNumber)
{
  ami_ErrorStamp(_status, inpFilename, inLineNumber);
}

int 
AmiStatus::getCode()
{
  return ami_ErrorGetCode(_status);
}

const char * 
AmiStatus::getText()
{
  return ami_ErrorGetText(_status);
}

int
AmiStatus::getThread()
{
  return ami_ErrorGetThread(_status);
}

void 
AmiStatus::setStatus(int errorCode, const char * format, ...)
{
  va_list args;
  va_start(args, format);
  _status = ami_ErrorCreateV(errorCode, format, args);
  va_end(args);
}

const char * 
AmiStatus::getFile()
{
  return ami_ErrorGetFile(_status);
}

int
AmiStatus::getLine()
{
  return ami_ErrorGetLine(_status);
}

void 
AmiStatus::setStatusV(int errorCode, const char * format, va_list args)
{
  _status = ami_ErrorCreateV(errorCode, format, args);
}

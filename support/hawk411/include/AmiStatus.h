//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiStatus_
#define _INCLUDED_AmiStatus_

#include <ami.h>

class AmiStatus {
public:

  AmiStatus() {_status = AMI_OK;}
  AmiStatus(ami_Error status) {_status = status;}
  AmiStatus(ami_ErrorCode code) {_status = ami_ErrorCreate(code, NULL);}

  virtual ~AmiStatus() {ami_ErrorDestroy(_status);}

  ami_Error getAmiError() const {return _status;}
  operator ami_Error() const {return _status;}

  AmiStatus& operator=(const AmiStatus& status);
  AmiStatus& operator=(const ami_Error status);
  AmiStatus& operator=(const ami_ErrorCode errorCode);

  ami_Boolean operator==(const AmiStatus& status);
  ami_Boolean operator!=(const AmiStatus& status);
  ami_Boolean operator==(const ami_Error status);
  ami_Boolean operator!=(const ami_Error status);
  ami_Boolean operator==(const ami_ErrorCode errorCode);
  ami_Boolean operator!=(const ami_ErrorCode errorCode);

  ami_Boolean operator!() const;
  ami_Boolean ok(void) const;

  void stamp(const char * inpFilename, int inLineNumber);
  int          getCode();
  const char * getText();
  int          getThread();
  const char * getFile();
  int          getLine();
  
  void setStatus(int errorCode, const char * format, ...);
  void setStatusV(int errorCode, const char * format, va_list args);

private:

  ami_Error _status;
};

#endif // _INCLUDED_AmiStatus_

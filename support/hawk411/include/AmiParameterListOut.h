//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiParameterListOut_
#define _INCLUDED_AmiParameterListOut_

#include <AmiParameterList.h>

class AmiMethod;
class AmiStatus;

class AmiParameterListOut : public AmiParameterList {
public:

  AmiParameterListOut(AmiMethod * method);

  virtual ~AmiParameterListOut();

  AmiStatus setValue(const char* name, void* value);

  AmiStatus newRow();

private:

  AmiParameterListOut(AmiMethod * method,
                      ami_ParameterListList * retArgs);

  AmiStatus listDestroy();

  ami_ParameterListList * getResultParams() {return resultParams;}

  ami_ParameterListList * resultParams;
  ami_Boolean createdByApp;

  friend class AmiSyncMethod;
  friend class AmiAsyncMethod;
};

#endif // _INCLUDED_AmiParameterListOut_

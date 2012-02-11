//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiParameterListIn_
#define _INCLUDED_AmiParameterListIn_

#include <AmiParameterList.h>

class AmiStatus;
class AmiMethod;

class AmiParameterListIn : public AmiParameterList {
private:
  AmiParameterListIn(AmiMethod * method,
                   ami_ParameterList args);

public:
  virtual ~AmiParameterListIn();
  AmiStatus getValue(const char* name, void* value);

  friend class AmiSyncMethod;
  friend class AmiAsyncMethod;
  friend class AmiSubscription;
};

#endif // _INCLUDED_AmiParameterListIn_

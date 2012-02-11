//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiSyncMethod_
#define _INCLUDED_AmiSyncMethod_

#include <AmiMethod.h>

class AmiStatus;
class AmiSession;

class AmiSyncMethod : public AmiMethod {
protected:

  AmiSyncMethod(
    AmiSession * session,// AMI session object.
    const char * name,   // Name of the method.
    const char * help,   // Help text of the method. 
    ami_MethodType type, // Type of the method.
                         // must be one of the following:
			 // AMI_METHOD_INFO,
			 // AMI_METHOD_ACTION,
			 // AMI_METHOD_ACTION_INFO.
    int inTimeout);      // The timeout interval of this AMI method.
                         // The default is 10000 milliseconds(10 seconds).

  virtual ~AmiSyncMethod() {}

private:

  AmiStatus
  createSyncMethod(
    const char *   name, 
    const char *   help,
    ami_MethodType type, 
    int            timeout);

  static ami_Error _methodInvokeCb(
    ami_Session ami,
    ami_Method method,
    ami_Subscription context,
    void * closure,
    ami_ParameterList inArgs,
    ami_ParameterListList * retArgs);

  friend class AmiAsyncMethod;
};

#endif // _INCLUDED_AmiSyncMethod_

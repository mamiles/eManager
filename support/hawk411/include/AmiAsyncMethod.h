//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiAsyncMethod_
#define _INCLUDED_AmiAsyncMethod_

#include <AmiMethod.h>

class AmiStatus;
class AmiSession;
class AmiParameterList;

class AmiAsyncMethod : public AmiMethod {
protected:

  AmiAsyncMethod(
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

  virtual ~AmiAsyncMethod() {}

  // This method is invoked by the AMI C++ API whenever an asynchronous
  // method subscription request on this method.  This method implements
  // the start actions to be performed by the application on such event.
  //   context - The context specific to a subscription request.
  //     The lifetime of this context starts at the moment this method
  //     is invoked and stops after the onStop method returns. 
  // This method is optional. The default is noop if the application choose not
  // to implement it. In such case, the AMI session will keep track of the 
  // pertinent context for purpose of sending asynchronous data.
  virtual AmiStatus onStart(
    AmiSubscription * context,  // AMI context associated with invocation. 
    AmiParameterListIn * args); // Input parameters list from the Agent.

  // This method is invoked by the AMI C++ API whenever a cancellation 
  // of asynchronous method subscription arrives for this method. This 
  // method implements the stop actions to be performed by the
  // application on such event. This method is optional.
  virtual AmiStatus onStop(AmiSubscription * context);

public:

  // Sends data asynchronously when an event occurs.
  // This method goes through the context list of the session and sends
  // the data returned by onInvoke() to the appropriate subscription 
  // based on the varying input parameters calculated in onInvoke().
  // It is users responsibility not to return data and call to 
  // AmiParameterListOut::newRow() in onInvoke() if it is
  // not interesting context/subscription.
  // The method should not be supressed usually. The tracking of the context
  // is done by this method and is totally transparent to the users.
  // It is users responsibility to invoke this method  when the event occurs.
  void onData();

  // Sends data asynchronously from an asynchronous AMI method.
  // context - The asynchronous subscription context.
  // data - The reply data to be sent to the subscriber of the asynchronous
  // AMI method.
  AmiStatus sendData(AmiSubscription * context, AmiParameterListOut * data);

  // Reports an error condition for the specified async. method subscription.
  AmiStatus sendError(AmiSubscription * context, AmiStatus& status);

private:

  AmiStatus
  createAsyncMethod(
    const char *   name,
    const char *   help,
    ami_MethodType type,
    int            timeout);

  static ami_Error _methodStartCb(
    ami_Session ami,
    ami_Method method,
    void * closure,
    ami_Subscription context,
    ami_ParameterList inArgs);

  static void _methodStopCb(
    ami_Session  ami,
    ami_Method   method,
    void *       closure,
    ami_Subscription context);
};

#endif // _INCLUDED_AmiAsyncMethod_

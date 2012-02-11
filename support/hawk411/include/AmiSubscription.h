//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiSubscription_
#define _INCLUDED_AmiSubscription_

#include <ami.h>

class AmiStatus;
class AmiMethod;
class AmiParameterListIn;

class AmiSubscription {
public:

  AmiSubscription(
    AmiMethod * method,  // AmiMethod object.
    ami_ParameterList inpArguments,
                         // Handle of AMI input parameter list
    ami_Subscription context);
                         // AMI context associated with invocation.

  virtual ~AmiSubscription();

  // Allows user to retieve the associated AMI method object for 
  // a particular aync. method subscription.
  AmiMethod * getMethod();// Target for returned method object.

  // Allows user to retieve the method argument values for a a particular aync.
  // method subscription. 
  AmiParameterListIn * getArguments();// Handle of AMI input parameter list

  // Allows user to attach application specific data to a particular aync. 
  // method subscription. This function is usually used in the onStart() 
  // callback.
  AmiStatus setUserData(
    void *            inpUserData); // User data.

  // Allows user to retieve the application specific data attached to a 
  // particular aync. method subscription. This function is usually used in 
  // the onInvoke() callback when processing asynchronous method invocations 
  // to obtain access to the application specific data associated with that 
  // invocation.
  void * getUserData(); // Target for returned user data.

  // Indicates that for this subscription the associated onInvoke() callback
  // should be auto-invoked at the specified interval. This provides a psuedo-
  // aynchronous event to trigger (what would normally be) synchronous methods 
  // so that they can behave as asyncronous methods. A typical scenario is a 
  // method which must calculate (polled) data over a precise time interval and
  // return the calculated result based on that interval. In this case the 
  // method returns data not based on a synchronous call but on a specified 
  // time interval.
  AmiStatus setCallbackInterval(
    int               inInterval);       // Interval in seconds. Zero turns off.

private:

  ami_Subscription getSubscription();

  AmiMethod          * s_method;
  AmiParameterListIn * s_inpArguments;
  ami_Subscription     s_context;
  void *               s_userData; 

  friend class AmiAsyncMethod;
};

#endif // _INCLUDED_AmiSubscription_

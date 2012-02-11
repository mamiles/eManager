//
// Copyright (c) 1997-2001 TIBCO Software Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiSyncMethod.h>
#include <AmiStatus.h>
#include <AmiSession.h>
#include <AmiParameterListIn.h>
#include <AmiParameterListOut.h>

AmiSyncMethod::AmiSyncMethod(
  AmiSession *   session,
  const char *   name, 
  const char *   help,
  ami_MethodType type, 
  int            timeout)
  : AmiMethod(session)
{
  m_status = createSyncMethod(name, help, type, timeout);
} 

AmiStatus
AmiSyncMethod::createSyncMethod(
  const char *   name, 
  const char *   help,
  ami_MethodType type, 
  int            timeout)
{
  AmiStatus  amiStatus = AMI_OK;

  amiStatus = ami_MethodCreate(
                m_ami->getAmiSession(), &m_method,
                name, type, help, timeout,
                (ami_OnInvokeCallback)(AmiSyncMethod::_methodInvokeCb),
                (const void*)this);

  return amiStatus;
}

ami_Error
AmiSyncMethod::_methodInvokeCb(
  ami_Session ami,
  ami_Method method,
  ami_Subscription context,
  void * closure,
  ami_ParameterList inArgs,
  ami_ParameterListList * retArgs)
{
  AmiMethod *         m_Method  = (AmiMethod *)closure;
  AmiStatus           amiStatus = AMI_OK;
  AmiParameterListIn  * argsIn  = NULL;
  AmiParameterListOut * argsOut = NULL;
  AmiSubscription     * subscription = NULL;
  void *                userData;

  argsIn  = new AmiParameterListIn(m_Method, inArgs);
  argsOut = new AmiParameterListOut(m_Method, retArgs);

  if (context != NULL)
  {
    ami_SubscriptionGetUserData((m_Method->getSession())->getAmiSession(),
                              context, &userData);
    subscription = (AmiSubscription*)(userData);
  }

  amiStatus = ((AmiSyncMethod*)m_Method)->onInvoke(subscription, 
                                                   argsIn,
                                                   argsOut);
  delete argsIn;
  delete argsOut;

  return amiStatus;
}

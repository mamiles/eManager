//
// Copyright (c) 1997-2001 TIBCO Software Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiAsyncMethod.h>
#include <AmiSyncMethod.h>
#include <AmiSession.h>
#include <AmiStatus.h>
#include <AmiSubscription.h>
#include <AmiParameterListIn.h>
#include <AmiParameterListOut.h>

AmiAsyncMethod::AmiAsyncMethod(
  AmiSession *   session,
  const char *   name, 
  const char *   help, 
  ami_MethodType type,
  int            timeout)
  : AmiMethod(session)
{
  m_status = createAsyncMethod(name, help, type, timeout);
}

AmiStatus 
AmiAsyncMethod::onStart(AmiSubscription * s, AmiParameterListIn * args)
{
  return AMI_OK;
}

AmiStatus 
AmiAsyncMethod::onStop(AmiSubscription * s)
{
  return AMI_OK;
}

void 
AmiAsyncMethod::onData()
{
  ami_SessionOnData((getSession())->getAmiSession(), this->getAmiMethod());
}

AmiStatus 
AmiAsyncMethod::sendData(AmiSubscription * context, AmiParameterListOut * data)
{ 
  return ami_SessionSendData(m_ami->getAmiSession(), context->getSubscription(),
                             *(data->getResultParams()));
}

AmiStatus 
AmiAsyncMethod::sendError(AmiSubscription * context, AmiStatus& err)
{ 
  return ami_SessionSendError(m_ami->getAmiSession(), 
                              context->getSubscription(), err.getAmiError());
}

AmiStatus
AmiAsyncMethod::createAsyncMethod(
  const char *   name,
  const char *   help,
  ami_MethodType type,
  int            timeout)
{
  AmiStatus  amiStatus = AMI_OK;

  amiStatus = ami_AsyncMethodCreate(
                m_ami->getAmiSession(), &m_method,
                name, type, help, timeout,
                (ami_OnInvokeCallback)(AmiSyncMethod::_methodInvokeCb),
                (ami_OnStartCallback)(AmiAsyncMethod::_methodStartCb),
                (ami_OnStopCallback)(AmiAsyncMethod::_methodStopCb),
                (const void*)this);

  return amiStatus;
}

ami_Error
AmiAsyncMethod::_methodStartCb(
  ami_Session ami,
  ami_Method method,
  void * closure,
  ami_Subscription context,
  ami_ParameterList inArgs)
{
  AmiMethod *           m_Method  = (AmiMethod *)closure;
  AmiStatus             amiStatus = AMI_OK;
  AmiSubscription * subscription  = NULL;

  subscription = new AmiSubscription(m_Method, inArgs, context);
  
  amiStatus = ((AmiAsyncMethod*)m_Method)->onStart(subscription, 
                                                  subscription->getArguments());
  return amiStatus;
}

void
AmiAsyncMethod::_methodStopCb(
  ami_Session ami,
  ami_Method method,
  void * closure,
  ami_Subscription context)
{
  AmiAsyncMethod * m_Method = (AmiAsyncMethod *)closure;
  void           * userData;
  
  ami_SubscriptionGetUserData((m_Method->getSession())->getAmiSession(), 
                              context, &userData);
  m_Method->onStop((AmiSubscription*)(userData));
  delete (AmiSubscription*)(userData);
}

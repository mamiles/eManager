//
// Copyright (c) 1997-2001 TIBCO Software Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiSubscription.h>
#include <AmiMethod.h>
#include <AmiSession.h>
#include <AmiStatus.h>
#include <AmiParameterListIn.h>

AmiSubscription::AmiSubscription(
  AmiMethod * method,
  ami_ParameterList inpArguments,
  ami_Subscription context)
{
  s_method = method;
  s_inpArguments = new AmiParameterListIn(method, inpArguments);
  s_context = context;
  s_userData = NULL;
  ami_SubscriptionSetUserData((s_method->getSession())->getAmiSession(), 
                              s_context, (void*)this);
}

AmiSubscription::~AmiSubscription()
{
  delete s_inpArguments;
}

AmiMethod *
AmiSubscription::getMethod()
{
  return s_method;
}

AmiParameterListIn *
AmiSubscription::getArguments()
{
  return s_inpArguments;
}

AmiStatus 
AmiSubscription::setUserData(
  void * inpUserData)
{
  s_userData = inpUserData;
  return AMI_OK;
}

void *
AmiSubscription::getUserData()
{
  return s_userData;
}

AmiStatus 
AmiSubscription::setCallbackInterval(
  int inInterval)
{
  return ami_SubscriptionSetCallbackInterval(
           (s_method->getSession())->getAmiSession(), 
           s_context, inInterval);
}

ami_Subscription
AmiSubscription::getSubscription()
{
  return s_context;
}

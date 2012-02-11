//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiParameterListIn.h>
#include <AmiSession.h>
#include <AmiMethod.h>

AmiParameterListIn::AmiParameterListIn(
  AmiMethod * method,
  ami_ParameterList args) 
  : AmiParameterList(method, args)
{
}

AmiParameterListIn::~AmiParameterListIn()
{
}

AmiStatus 
AmiParameterListIn::getValue(
  const char* name, 
  void* value)
{
  return ami_ParameterGetValue(m_method->getSession()->getAmiSession(),
                               m_method->getAmiMethod(), params,
                               name, value);
}

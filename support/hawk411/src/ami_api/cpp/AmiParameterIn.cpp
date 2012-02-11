//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiParameterIn.h>
#include <AmiSession.h>
#include <AmiMethod.h>
#include <AmiStatus.h>

AmiParameterIn::AmiParameterIn(
  AmiMethod * method,
  const char* name, 
  ami_DataType type, 
  const char* help)
  : AmiParameter(method)
{
  m_status = amiParameterCreateIn(name, type, help);
}

AmiStatus
AmiParameterIn::amiParameterCreateIn(
  const char* name, 
  ami_DataType type, 
  const char* help)
{
  return ami_ParameterCreateIn(m_method->getSession()->getAmiSession(), 
                               m_method->getAmiMethod(), &param,
                               name, type, help);
}

AmiStatus
AmiParameterIn::addLegal(void* value)
{
  return ami_ParameterAddLegal(m_method->getSession()->getAmiSession(),
                               param, value);
}

AmiStatus
AmiParameterIn::addChoice(void* value)
{
  return ami_ParameterAddChoice(m_method->getSession()->getAmiSession(),
                                param, value);
}

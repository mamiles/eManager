//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiParameterOut.h>
#include <AmiSession.h>
#include <AmiMethod.h>
#include <AmiStatus.h>

AmiParameterOut::AmiParameterOut(
  AmiMethod * method,
  const char* name, 
  ami_DataType type, 
  const char* help)
  : AmiParameter(method)
{
  m_status = amiParameterCreateOut(name, type, help);
}

AmiParameterOut::~AmiParameterOut()
{
}

AmiStatus
AmiParameterOut::amiParameterCreateOut(
  const char* name, 
  ami_DataType type, 
  const char* help)
{
  return ami_ParameterCreateOut(m_method->getSession()->getAmiSession(), 
                                m_method->getAmiMethod(), &param,
                                name, type, help);
}

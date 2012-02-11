//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiParameterListOut.h>
#include <AmiSession.h>
#include <AmiMethod.h>

AmiParameterListOut::AmiParameterListOut(AmiMethod * method)
 : AmiParameterList(method, NULL)
{
  resultParams = new ami_ParameterListList;
  *resultParams = NULL;
  createdByApp = AMI_TRUE;
}

AmiParameterListOut::AmiParameterListOut(
  AmiMethod * method,
  ami_ParameterListList * retArgs) 
 : AmiParameterList(method, NULL)
{
  resultParams = retArgs;
  createdByApp = AMI_FALSE;
}

AmiParameterListOut::~AmiParameterListOut()
{
  if (createdByApp)
  {
    listDestroy();
  }
}

AmiStatus 
AmiParameterListOut::setValue(
  const char* name, 
  void* value)
{
  return ami_ParameterSetValue(m_method->getSession()->getAmiSession(),
                               m_method->getAmiMethod(), 
                               params,
                               name, value);
}

AmiStatus 
AmiParameterListOut::newRow()
{
  return ami_ParameterListOut(m_method->getSession()->getAmiSession(),
                              m_method->getAmiMethod(), 
                              resultParams, &params);
}

AmiStatus 
AmiParameterListOut::listDestroy()
{
  AmiStatus amiStatus = AMI_OK;
  amiStatus = ami_ParameterListListDestroy(
                                      m_method->getSession()->getAmiSession(),
                                      *resultParams);
  params = NULL;
  *resultParams = NULL;
  delete resultParams;
  return amiStatus;
}

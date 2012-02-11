//
// Copyright (c) 1997-2001 TIBCO Software Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiMethod.h>
#include <AmiSession.h>
#include <AmiParameter.h>
#include <AmiSubscription.h>
#include <AmiParameterListIn.h>
#include <AmiParameterListOut.h>

AmiMethod::AmiMethod(AmiSession * session)
{
  m_ami     = session;
  m_method  = NULL;
  m_link    = m_ami->m_methods.insert(this);
}

AmiMethod::~AmiMethod()
{
  AmiLink * pParamLink; 
  AmiParameter * pParam;
  for (pParamLink=(m_params.link)->pNext;
       !(pParamLink->IsAnchor); pParamLink=(m_params.link)->pNext)
  {
    pParam = (AmiParameter*)pParamLink->pObject;
    delete pParam;
  }

  m_ami->m_methods.remove(m_link);
}

AmiStatus
AmiMethod::setIndexName(const char * index)
{
  return(ami_MethodSetIndex(m_ami->getAmiSession(), m_method, index));
}

AmiStatus 
AmiMethod::getName(const char ** name) const
{
  AmiStatus amiStatus = AMI_OK;
  amiStatus = ami_MethodGetName(m_ami->getAmiSession(), m_method, name);
  return amiStatus;
} 

AmiStatus 
AmiMethod::getHelp(const char ** help) const
{
  AmiStatus amiStatus = AMI_OK;
  amiStatus = ami_MethodGetHelp(m_ami->getAmiSession(), m_method, help);
  return amiStatus;
}

AmiSession* 
AmiMethod::getSession() const
{
  return(m_ami);
}

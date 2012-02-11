//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiParameter.h>
#include <AmiSession.h>
#include <AmiMethod.h>

AmiParameter::AmiParameter(AmiMethod * method)
{
  m_method = method;
  param    = NULL;
  m_link   = m_method->m_params.insert(this);
}

AmiParameter::~AmiParameter()
{
  m_method->m_params.remove(m_link);
}

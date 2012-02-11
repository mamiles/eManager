//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiParameterList.h>

AmiParameterList::AmiParameterList(
  AmiMethod * method,
  ami_ParameterList args) 
{
  m_method = method;
  params = args;
}

AmiParameterList::~AmiParameterList()
{
}

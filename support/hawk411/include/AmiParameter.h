//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiParameter_
#define _INCLUDED_AmiParameter_

#include <AmiStatus.h>

class AmiMethod;
class AmiLink;

class AmiParameter { 
public:

  virtual ~AmiParameter();

  AmiStatus& getStatus() {return m_status;}

protected:

  AmiParameter(AmiMethod * method);

  ami_Parameter param;
  AmiStatus     m_status;
  AmiMethod *   m_method;
  AmiLink   *   m_link;
};

#endif // _INCLUDED_AmiParamter_

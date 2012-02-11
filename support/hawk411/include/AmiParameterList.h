//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiParameterList_
#define _INCLUDED_AmiParameterList_

#include <ami.h>

class AmiMethod;

class AmiParameterList {
public:

  virtual ~AmiParameterList();

protected:

  AmiParameterList(AmiMethod * method,
                   ami_ParameterList args);

  AmiMethod             * m_method;
  ami_ParameterList       params;
};

#endif // _INCLUDED_AmiParameterList_

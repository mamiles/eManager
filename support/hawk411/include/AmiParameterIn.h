//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiParameterIn_
#define _INCLUDED_AmiParameterIn_

#include <AmiParameter.h>

class AmiMethod;
class AmiStatus;

class AmiParameterIn : public AmiParameter { 
public:

  AmiParameterIn(
    AmiMethod * method,
    const char* name, 
    ami_DataType type, 
    const char* help);

  virtual ~AmiParameterIn() {}

  AmiStatus addLegal(void* value);
  AmiStatus addChoice(void* value);

private:

  AmiStatus amiParameterCreateIn(
    const char* name, 
    ami_DataType type, 
    const char* help);
};

#endif // _INCLUDED_AmiParamterIn_

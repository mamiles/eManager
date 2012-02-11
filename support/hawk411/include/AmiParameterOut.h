//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiParameterOut_
#define _INCLUDED_AmiParameterOut_

#include <AmiParameter.h>

class AmiMethod;
class AmiStatus;

class AmiParameterOut : public AmiParameter { 
public:

  AmiParameterOut(
    AmiMethod * method,
    const char* name, 
    ami_DataType type, 
    const char* help);

  virtual ~AmiParameterOut();

private:

  AmiStatus amiParameterCreateOut(
    const char* name, 
    ami_DataType type, 
    const char* help);
};

#endif // _INCLUDED_AmiParamterOut_

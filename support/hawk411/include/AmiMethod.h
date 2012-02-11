//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiMethod_
#define _INCLUDED_AmiMethod_

#include <AmiStatus.h>
#include <AmiList.h>

class AmiSession;
class AmiParameter;
class AmiSubscription;
class AmiParameterListIn;
class AmiParameterListOut;

class AmiMethod {
protected:
  AmiMethod(AmiSession * session);
  virtual ~AmiMethod();

public:
  // Sets the index for this AmiMethod.  This method can be invoked multiple
  // times to establish the composite index.
  AmiStatus setIndexName(const char * index);

  // Gets the name of the method.
  AmiStatus getName(const char ** name) const;

  // Gets the help text for the method.
  AmiStatus getHelp(const char ** help) const;

  // Gets the AmiSession object this method belongs to.
  AmiSession * getSession(void) const;

  AmiStatus& getStatus() {return m_status;}

protected:
  // This method is invoked by the AMI C++ API whenever a method invocation
  // message arrives from the monitoring agent.  This method should implement
  // the actions to be performed by the application. 
  virtual AmiStatus onInvoke(AmiSubscription * context, 
                             AmiParameterListIn * argsIn,
                             AmiParameterListOut * argsOut) = 0;

  ami_Method getAmiMethod(void) const {return m_method;}
 
  AmiSession * m_ami;
  ami_Method   m_method;
  AmiStatus    m_status;
  AmiList      m_params;
  AmiLink    * m_link;

  friend class AmiSession;
  friend class AmiParameter;
  friend class AmiParameterIn;
  friend class AmiParameterOut;
  friend class AmiParameterListIn;
  friend class AmiParameterListOut;
};

#endif // _INCLUDED_AmiMethod_

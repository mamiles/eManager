//
// Copyright (c) 1997-2001 TIBCO Software Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiSession.h>
#include <AmiMethod.h>

AmiSession::AmiSession(
  ami_TraceCode  traceLevel,
  const char *   service,
  const char *   network,
  const char *   daemon,
  unsigned int   rvTransport,
  unsigned int   rvQueue,
  const char *   name,
  const char *   display,
  const char *   help,
  ami_TraceHandler traceHandler,
  const void *   userData)
{
  m_ami    = NULL;
  m_status = ami_SessionCreate(
               &m_ami, traceLevel, service, network, daemon,
               rvTransport, rvQueue, name, display, help,
               (ami_TraceHandler)traceHandler,
               (const void *)userData);
}

AmiSession::~AmiSession()
{
  ami_SessionDestroy(m_ami);

  AmiLink * pMethodLink;
  AmiMethod * pMethod;
  for (pMethodLink=(m_methods.link)->pNext; 
       !(pMethodLink->IsAnchor); pMethodLink=(m_methods.link)->pNext)
  {
    pMethod = (AmiMethod*)pMethodLink->pObject;
    delete pMethod;
  }
}

AmiStatus
AmiSession::open()
{
  return ami_Open();
}

AmiStatus
AmiSession::close()
{
  return ami_Close();
}

AmiStatus
AmiSession::announce() const
{
  return ami_SessionAnnounce(m_ami);
}

AmiStatus
AmiSession::stop() const
{
  return ami_SessionStop(m_ami);
}

AmiStatus
AmiSession::getName(const char ** name) const
{
  return ami_SessionGetName(m_ami, name);
}

AmiStatus
AmiSession::getDisplayName(const char ** displayName) const
{
  return ami_SessionGetDisplayName(m_ami, displayName);
}

AmiStatus
AmiSession::getHelp(const char ** help) const
{
  return ami_SessionGetHelp(m_ami, help);
}

AmiStatus
AmiSession::getUserData(void ** userData) const
{
  return ami_SessionGetUserData(m_ami, userData);
}

AmiStatus
AmiSession::getTraceLevels(ami_TraceCode * inpTraceLevel) const
{
  return ami_SessionGetTraceLevels(m_ami, inpTraceLevel);
}

AmiStatus
AmiSession::setTraceLevels(ami_TraceCode inpTraceLevel)
{
  return ami_SessionSetTraceLevels(m_ami, inpTraceLevel);
}

AmiStatus
AmiSession::enableTraceLevels(ami_TraceCode inpTraceLevel)
{
  return ami_SessionEnableTraceLevels(m_ami, inpTraceLevel);
}

AmiStatus
AmiSession::disableTraceLevels(ami_TraceCode inpTraceLevel)
{
  return ami_SessionDisableTraceLevels(m_ami, inpTraceLevel);
}

AmiStatus 
AmiSession::sendUnsolicitedMsg(
  ami_AlertType type, 
  const char * text, 
  int id) const
{
  return ami_SessionSendUnsolicitedMsg(m_ami, type, text, id);
}

ami_Session 
AmiSession::getAmiSession(void) const
{
  return m_ami;
}

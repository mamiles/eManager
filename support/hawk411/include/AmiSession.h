//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiSession_
#define _INCLUDED_AmiSession_

#include <ami.h>
#include <AmiStatus.h>
#include <AmiList.h>

class AmiMethod;

class AmiSession {
public:

  // Creates a new instance of class AmiSession. Each instance of this class
  // Corresponds to an independent TIB/Hawk Microagent.
  AmiSession(
    ami_TraceCode traceLevel, // AMI trace levels for this AMI session.
    const char *  service,    // Rendezvous service.
    const char *  network,    // Rendezvous network.
    const char *  daemon,     // Rendezvous daemon.
    unsigned int  rvTransport,// Rendezvous transport for AMI session
    unsigned int  rvQueue,    // Rendezvous queue     for AMI session
    const char *  name,       // Name of the microagent.
    const char *  display,    // User-friendly name string for microagent.
    const char *  help,       // Help text for describing the
                              // funtionalities of this microagent.
    ami_TraceHandler traceHandler,// AMI session error callback function.
    const void *     userData );  // AMI session user data.

  // Destructor
  virtual ~AmiSession();

  // Initializes the AMI C++ API.
  // Must be called prior to calling any other AMI C++ API functions.
  static AmiStatus open();

  // Terminates the AMI C++ API and releases associated resources.
  static AmiStatus close();

  // Announces the existence of the microagent.
  AmiStatus announce(void) const;

  // Stop the AMI session.
  AmiStatus stop(void) const;

  // Gets the name of this microagent.  Note that this name can be
  // different from the display name.
  AmiStatus getName(const char ** name) const;

  // Gets the name of the microagent for display.
  AmiStatus getDisplayName(const char ** displayName) const;

  // Gets the help text of this microagent.
  AmiStatus getHelp(const char ** help) const;

  // Returns the user data of the specified AMI session.
  AmiStatus getUserData(void ** userData) const;

  // Returns the current AMI session trace level settings.
  AmiStatus getTraceLevels(ami_TraceCode * inpTraceLevel) const;

  // Resets all AMI session trace level settings to the specified settings.
  AmiStatus setTraceLevels(ami_TraceCode inTraceLevel);

  // Enables the specified level(s) of trace output. All other trace level
  // settings are uneffected.
  AmiStatus enableTraceLevels(ami_TraceCode inTraceLevel);

  // Disables the specified level(s) of trace output. All other trace level
  // settings are uneffected.
  AmiStatus disableTraceLevels(ami_TraceCode inTraceLevel);

  // Sends unsolicited message to the monitoring Agent.
  AmiStatus sendUnsolicitedMsg(
    ami_AlertType type, // Alert message type.
    const char *  text, // The text message describing the alert condition.
    int id) const;      // An arbitrary identification number defined by
                        // the application.

  AmiStatus& getStatus() {return m_status;}

  // These functions return the AMI C++ API version information.
  static const char * versionName()
    {return("TIBCO Hawk AMI C++ API");}

  static const char * version()
    {return("4.1.1_FINAL");}

  static const char * versionDate()
    {return("Wed 04/30/2003");}

  static const char * banner()
    {return("TIBCO Hawk AMI C++ API");}

  static int versionMajor()
    {return(4);}

  static int versionMinor()
    {return(1);}

  static int versionUpdate()
    {return(1);}

private:

  // Get C ami_Session object.
  ami_Session getAmiSession(void) const;

  AmiStatus    m_status;
  ami_Session  m_ami;
  AmiList      m_methods;

  friend class AmiMethod;
  friend class AmiSyncMethod;
  friend class AmiAsyncMethod;
  friend class AmiParameterIn;
  friend class AmiParameterOut;
  friend class AmiParameterListIn;
  friend class AmiParameterListOut;
  friend class AmiSubscription;
};

#endif // _INCLUDED_AmiSession_

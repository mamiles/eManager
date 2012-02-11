/*********************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.  Possessions and use of this
 * program must conform strictly to the license agreement between the user
 * and Cisco Systems, Inc., and receipt or possession does not convey
 * any rights to divulge, reproduce, or allow others to use this program
 * without specific written authorization of Cisco Systems, Inc.
 *
 * Copyright (c) 2001 by Cisco Systems, Inc.
 * All rights reserved.
 *
 *********************************************************************/
package com.cisco.eManager.eManager.processSequencer.common;


import com.tibco.tibrv.*;
import java.util.Enumeration;
import java.util.Random;
import com.cisco.eManager.eManager.processSequencer.common.PropertiesConstants;

/**
 **/
abstract class IdleCommon
  implements TibrvMsgCallback, TibrvTimerCallback {

  static Random msRandom = new Random ();

  public static final String CREATED = "created";
  public static final String ACTIVATED = "activated";
  public static final String ACKNOWLEDGED = "ack";
  public static final String IDLED = "idled";
  public static final String INTERESTED = "interested";

  TibrvTransport mTransport;
  TibrvListener mListener;
  TibrvTimer mTimer;

  String mSubject;
  String mContext;

  /**
   * The timeout period for this subject.  This is set to the minimum
   * of the timeout periods published by AutoIdlers.
   **/
  double mTimeoutPeriod;

  /**
   * Create a new IdleCommon for the specified session and subject.
   *
   * @param session The TIBCO Rendezvous session.
   * @param subject The subject to talk on.
   * @param timeoutPeriod The timeout for this subject.
   **/
  public IdleCommon (TibrvTransport transport,
		     String subject,
		     double timeoutPeriod)
    throws TibrvException {

    mTransport = transport;
    mSubject = subject;
    mTimeoutPeriod = timeoutPeriod;

    mContext = getClass().getName() + "(" + mSubject + ")";
    mListener = EventUtils.newListener (this, subject);
    mTimer = EventUtils.newTimer (this, mTimeoutPeriod);
  }

}

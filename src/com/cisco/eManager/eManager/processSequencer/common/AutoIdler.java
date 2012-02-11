/*********************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.	Possessions and use of this
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
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

import com.cisco.eManager.eManager.processSequencer.common.AppConfiguration;
import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;
import com.cisco.eManager.eManager.processSequencer.common.PropertiesConstants;

/**
 * <p>The AutoIdler class maintains a true/false flag in a distributed
 * environment.	One or more servers can create an AutoIdler instance
 * for a given subject.	The object initializes its flag to false and
 * maintains it in that state until an IdleInhibitor object is created
 * somewhere on the network for the same subject.</p>
 *
 * <p>When an IdleInhibitor is created, it publishes an event
 * indicating the flag should be turned on.	All AutoIdler instances
 * receive the event and set the flag to true.	As long as some
 * IdleInhibitor continues to periodically publish events indicating
 * interest, the flag stays true.	If the timeout period is exceeded
 * without any client publishing interest, AutoIdler automatically
 * transitions back to the false or idle state.<p>
 *
 * <p>This pair of classes can be used to avoid publishing events if
 * there is no client interested in receiving them.	For example, a
 * GUI client might use an IdleInhibitor to request that a server send
 * send out events discribing its internal workings.	If no client GUI
 * exists, there is no reason for the server to use up network
 * bandwidth.<p>
 *
 * <p>As an optimization, IdleInhibitor classes watch each others
 * messages as a way to avoid polluting the network if there are many
 * clients.	If an IdleInhibitor receives an event published by
 * another IdleInhibitor, it knows it does not need to publish an
 * event itself until the timeout period is approached again.</p>
 */
public class AutoIdler extends IdleCommon {

	private boolean mActive = false;
	private boolean mAlwaysActive = false;
	private TibrvMsg mIdledMsg = null;
	private TibrvMsg mAckMsg = null;
	private	TibrvMsg mActivatedMsg = null;
	private boolean mIdle = true;

	/**
	* @associates AutoIdlerListener
	*/
	Set mListeners = Collections.synchronizedSet(new HashSet ());

	public static final double DEFAULT_PERIOD = 60; // 1 minute
	private static CiscoLogger msLogger = CiscoLogger.getCiscoLogger("AutoIdler");

	public AutoIdler (TibrvTransport transport, String subject, double period)
		throws TibrvException
	{

		super (transport, subject, period);

		TibrvMsg createdMsg = new TibrvMsg ();
		createdMsg.add (CREATED, mTimeoutPeriod);
		createdMsg.setSendSubject (mSubject);
		mTransport.send (createdMsg);
		mAlwaysActive = AppConfiguration.getBooleanProperty (PropertiesConstants.SYSTEM_EVENT + ".AutoIdler." + mSubject,
															 true);
	}

	public AutoIdler (String subject, double period) throws TibrvException {
		this (EventUtils.getDefaultTransport (subject), subject, period);
	}

	public AutoIdler (String subject) throws TibrvException {
		this (EventUtils.getDefaultTransport (subject), subject, DEFAULT_PERIOD);
	}

	/**
	* Receive event published on subject.
	*
	* @param listener Where the message was received from.
	* @param msg The message.
	*/
	public synchronized void onMsg (TibrvListener listener, TibrvMsg msg) {
		try {
			TibrvMsgField field = msg.getFieldByIndex (0);
			if (field.name.equals (ACKNOWLEDGED)
				|| field.name.equals (ACTIVATED)
				|| field.name.equals (IDLED))
			{
				// ignore
			} else if (field.name.equals (INTERESTED)) {
				// Some other IdleInhibitor has expressed interest, so be sure
				// we're not idle.
				// need to.
				if (mIdle) {
					mIdle = false;
					notifyListeners ();
					sendActivated ();
				} else {
					sendAck ();
				}
			}
		}
		catch (TibrvException te) {
			//ignore
			msLogger.fine("In OnMsg of AutoIdler", te);
		}
	}


	public synchronized void onTimer (TibrvTimer invoker) {
		if (!mIdle) {
			mIdle = true;
			notifyListeners ();
			sendIdled ();
		}
	}

	private void sendIdled () {
		try {
			if (mIdledMsg == null) {
				mIdledMsg = new TibrvMsg ();
				mIdledMsg.add (IDLED, true);
				mIdledMsg.setSendSubject (mSubject);
			}
			mTransport.send (mIdledMsg);
		}
		catch (TibrvException te) {
			//ignore
		}
	}

	private void sendAck () {
		try {
			if (mAckMsg == null) {
				mAckMsg = new TibrvMsg ();
				mAckMsg.add (ACKNOWLEDGED, mTimeoutPeriod);
				mAckMsg.setSendSubject (mSubject);
			}

				mTransport.send (mAckMsg);
				mTimer.resetInterval (mTimeoutPeriod);
		}
		catch (TibrvException rve) {
			//ignore
		}
	}

	private void sendActivated () {
		try {
			if (mActivatedMsg == null) {
				mActivatedMsg = new TibrvMsg ();
				mActivatedMsg.add (ACTIVATED, mTimeoutPeriod);
				mActivatedMsg.setSendSubject (mSubject);
			}

			mTransport.send (mActivatedMsg);
			mTimer.resetInterval (mTimeoutPeriod);
		}
		catch (TibrvException rve) {
			//ignore
		}
	}


	/**
	* Test if this AutoIdler is idle.
	*
	* @return True if this is idle, false if active.
	*/
	public boolean idle () {
		return mIdle && !mAlwaysActive;
	}

	/**
	* Test of this AutoIdler is active.
	*
	* @return True if this is active, false if idle.
	*/
	public boolean active () {
		return !mIdle || mAlwaysActive;
	}

	public void addListener (AutoIdlerListener listener) {
		mListeners.add (listener);
	}

	public void removeListener (AutoIdlerListener listener) {
		mListeners.remove (listener);
	}

	void notifyListeners () {
		synchronized (mListeners) {
			Iterator e = mListeners.iterator ();
			while (e.hasNext ()) {
				AutoIdlerListener listener = (AutoIdlerListener) e.next ();
				listener.autoIdlerStateChange (this, mIdle);
			}
		}
	}
}

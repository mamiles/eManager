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
package com.cisco.eManager.eManager.processSequencer.watchdog;

import com.tibco.tibrv.*;
import java.util.Enumeration;

import com.cisco.eManager.eManager.processSequencer.common.MsgCommon;

//for HA
import com.cisco.eManager.eManager.processSequencer.common.PSInetAddress;

/**
 * Abstracts the server status event that is sent via
 * TIBC0.
 */
public class ServerStateEvent extends MsgCommon {

	public static final int VERSION = 1;

	public static final String HOSTNAME_FIELD = "host";
	public static final String IPADDR_FIELD = "ip";
	public static final String NAME_FIELD = "name";
	public static final String STATE_NAME_FIELD = "state";
	public static final String GENERATION_FIELD = "gen";
	public static final String EXEC_TIME_FIELD = "exec";
	public static final String PID_FIELD = "pid";
	public static final String SUCCESSFUL_HEARTBEATS_FIELD = "success";
	public static final String MISSED_HEARTBEATS_FIELD = "missed";
	public static final String BOOT_FIELD = "boot";
	public static final String REVISION_FIELD = "rev";


	private static String initHostName () {
		msIpAddress = "<unknown>";
		try {
			String tmp = PSInetAddress.getLocalHost().toString ();
			int i = tmp.indexOf ("/");
			msIpAddress = tmp.substring (i + 1);
			return tmp.substring (0, i);
		}
		catch (java.net.UnknownHostException uhe) {
			uhe.printStackTrace ();
			System.err.println ("Ignoring exception");
		}
		return "<unknown>";
	}

	private static String msHostName = initHostName ();
	private static String msIpAddress;

	private static long msBoot = System.currentTimeMillis ();

	private String mVersion = "";
	private String mHostname = "";
	private String mIpaddr = "";
	private String mName = "";
	private String mState = "";
	private int mGeneration = 0;
	private long mExecTime = 0;
	private long mPid = 0;
	private int mSuccessfulHeartbeats = 0;
	private int mMissedHeartbeats = 0;
	private long mBoot = 0;
	private long mRevision = 0;

	/**
	 * Decodes the TIBCO msg and populates the fields
	 * of this class.
	 */
	public ServerStateEvent (TibrvMsg msg) throws TibrvException {
		super (msg);

		mHostname = getStringField (msg, HOSTNAME_FIELD);
		mIpaddr = getStringField (msg, IPADDR_FIELD);
		mName = getStringField (msg, NAME_FIELD);
		mState = getStringField (msg, STATE_NAME_FIELD);
		mGeneration = getIntField (msg, GENERATION_FIELD);
		mExecTime = getLongField (msg, EXEC_TIME_FIELD);
		mPid = getLongField (msg, PID_FIELD);
		mSuccessfulHeartbeats = getIntField (msg, SUCCESSFUL_HEARTBEATS_FIELD);
		mMissedHeartbeats = getIntField (msg, MISSED_HEARTBEATS_FIELD);
		mBoot = getLongField (msg, BOOT_FIELD);
		mRevision = getLongField (msg, REVISION_FIELD);
	}

	public String version () {
		return mVersion;
	}

	public String hostname () {
		return mHostname;
	}

	public String ipaddr () {
		return mIpaddr;
	}

	public String name () {
		return mName;
	}

	public String state () {
		return mState;
	}

	public int generation () {
		return mGeneration;
	}

	public long execTime () {
		return mExecTime;
	}

	public long pid () {
		return mPid;
	}

	public int successfulHeartbeats () {
		return mSuccessfulHeartbeats;
	}

	public int missedHeartbeats () {
		return mMissedHeartbeats;
	}

	public long boot () {
		return mBoot;
	}

	public long revision () {
		return mRevision;
	}

	/**
	 * Creates a TIBCO message using the instance variables
	 * of this ServerStateEvent Object.
	 */
	public TibrvMsg toTibrvMsg () throws TibrvException {
		return toTibrvMsg (mName, mState, mGeneration, mExecTime, mPid,
					 mSuccessfulHeartbeats, mMissedHeartbeats,
					 mBoot, mRevision);
	}

	/**
	 * Formats a TIBCO message using the given parameters
	 */
	public static TibrvMsg toTibrvMsg	(String name,
							String stateName,
							int generation,
							long execTime,
							long pid,
							int successfulHeartbeats,
							int missedHeartbeats,
							long revision)
		throws TibrvException {

		return toTibrvMsg (name, stateName, generation, execTime, pid,
					 successfulHeartbeats, missedHeartbeats,
					 msBoot, revision);
	}

	/**
	 * Formats a TIBCO message using the given parameters
	 */
	public static TibrvMsg toTibrvMsg	(String name,
							String stateName,
							int generation,
							long execTime,
							long pid,
							int successfulHeartbeats,
							int missedHeartbeats,
							long boot,
							long revision)
		throws TibrvException {

		TibrvMsg msg = MsgCommon.toTibrvMsg (VERSION);
		msg.add (HOSTNAME_FIELD, msHostName);
		msg.add (IPADDR_FIELD, msIpAddress);
		msg.add (NAME_FIELD, name);
		msg.add (STATE_NAME_FIELD, stateName);
		msg.add (GENERATION_FIELD, generation);
		msg.add (EXEC_TIME_FIELD, execTime);
		msg.add (PID_FIELD, pid);
		msg.add (SUCCESSFUL_HEARTBEATS_FIELD, successfulHeartbeats);
		msg.add (MISSED_HEARTBEATS_FIELD, missedHeartbeats);
		msg.add (BOOT_FIELD, boot);
		msg.add (REVISION_FIELD, revision);
		return msg;
	}

}

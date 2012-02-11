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

import com.tibco.tibrv.TibrvTransport;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvMsgCallback;

import com.cisco.eManager.eManager.processSequencer.common.AutoIdler;
import com.cisco.eManager.eManager.processSequencer.common.EventUtils;
import java.util.Iterator;

/**
 * Interfaces with TIBCO (EventUtils) to help send
 * server status events (both heartbeats and state
 * changes) on the wire.
 */
public class ServerEventAdapter implements WDConstants {

	private static ServerEventAdapter msTheServerEventAdapter = null;

	/**
	 * The event transport for watchdog information.	There is an
	 * assumption that all watchdog events will use the same transport.
	 * If this changes in the future, we'll need multiple transports.
	 **/
	private TibrvTransport mTransport = null;

	/**
	 * Used to track whether anyone is interested in heartbeat information.
	 */
	private AutoIdler mHeartbeatIdler = null;

	private SnapshotListener mSnapshotListener;

	private String mHeartbeatSubject;
	private String mHeartbeatIdlerSubject;
	private String mSnapshotSubject;
	private String mStateSubject;
	private ServerManager mServerManager;

	private ServerEventAdapter (ServerManager sm) throws TibrvException {
		setSubjects ();
		mTransport = EventUtils.getDefaultTransport ("watchdog");
		mSnapshotListener = new SnapshotListener ();
		mHeartbeatIdler = new AutoIdler (mHeartbeatIdlerSubject);
		mServerManager = sm;
	}

	private void setSubjects () {
		String prefix = EventUtils.getSubjectPrefix ();
		mHeartbeatSubject = prefix + HEARTBEAT_SUBJECT;
		mHeartbeatIdlerSubject = prefix + HEARTBEAT_IDLER_SUBJECT;
		mSnapshotSubject = prefix + SNAPSHOT_SUBJECT;
		mStateSubject = prefix + STATE_SUBJECT;
	}

	/**
     * Returns a reference to the ServerEventAdapter object.
     * If none is present, instantiates one.
	 */
	public static synchronized ServerEventAdapter theServerEventAdapter (ServerManager serverManager)
		throws TibrvException
	{
		if (serverManager == null) {
			throw new IllegalArgumentException (WDExUtil.formatMessage(WDExConstants.ARG_NULL,
												WDUtil.toArray("ServerManager")));
		}

		if (msTheServerEventAdapter == null) {
			msTheServerEventAdapter = new ServerEventAdapter (serverManager);
		}
		return msTheServerEventAdapter;
	}


	class SnapshotListener implements TibrvMsgCallback {

		SnapshotListener () throws TibrvException {
			EventUtils.newListener (this, mSnapshotSubject);
		}

		/**
		 * Receive event published on subject.
		 *
		 * @param listener Where the message was received from.
		 * @param msg The message.
		 */
		public synchronized void onMsg (TibrvListener listener, TibrvMsg msg) {
			try {
				Iterator servers = mServerManager.getServers ();
				while (servers.hasNext ()) {
					Server server = (Server) servers.next ();
					mTransport.sendReply (server.getStateEvent (), msg);
				}
			}
			catch (TibrvException rve) {
				rve.printStackTrace ();
			}
		}
	}

	/**
     * Sends the given msg as a heartbeat message
	 */
	public void sendHeartbeatEvent (TibrvMsg msg)
		throws TibrvException
	{
		String name = (String)msg.get("name");
		if (mHeartbeatIdler.active ())	{
			msg.setSendSubject (mHeartbeatSubject);
			mTransport.send (msg);
		}
	}

	/**
     * Sends the given msg as a state change message
	 */
	public void sendStateEvent (TibrvMsg msg)
		throws TibrvException
	{
		msg.setSendSubject (mStateSubject);
		mTransport.send (msg);
	}
}

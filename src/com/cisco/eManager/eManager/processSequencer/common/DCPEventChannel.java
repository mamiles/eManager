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

// For sending and receiving events

import java.io.*;
import java.util.*;

import com.tibco.tibrv.*;
import com.cisco.eManager.eManager.processSequencer.common.EventUtils;


public class DCPEventChannel {

	public DCPEventChannel(String service, String network, String daemon)
		throws EventChannelException
	{
		try {
			EventUtils.createDefaultTransport(service, network, daemon);
		} catch (TibrvException e) {
			e.printStackTrace();
			throw new EventChannelException("Can not open event channel");
		}
		Runtime.getRuntime().addShutdownHook(new ShutdownThread());
	}


	public static void sendDCPChange(String propertyPath, String value)
		throws EventChannelException
	{
		sendDCPChange(null, propertyPath, value);
	}

	public static void sendDCPChange(String[] hosts)
		throws EventChannelException
	{
		String subject = EventUtils.addSubjectPrefix(DCPLib.CHANGE_MSG_TAG
														+ DCPLib.ALL_PROPERTIES);
		TibrvMsg msg = new TibrvMsg();

		try {
			msg.setSendSubject(subject);
			msg.update("host", HostMsgField.getTarget(hosts));
		} catch (TibrvException e) {
			throw new EventChannelException("Failed to build message");
		}

		System.out.println(msg);

		try {
			TibrvTransport transport = EventUtils.getDefaultTransport();
			if ( transport != null) transport.send(msg);
		} catch (TibrvException e) {
			throw new EventChannelException("Error sending a message");
		}
	}

	public static void sendDCPChange(String[] hosts, String propertyPath, String value)
		throws EventChannelException
	{
		String component = DCPLib.removeSuffix(propertyPath);
		String property = DCPLib.getSuffix(propertyPath);

		String subject = EventUtils.addSubjectPrefix(DCPLib.CHANGE_MSG_TAG + component);

		TibrvMsg msg = new TibrvMsg();

		try {
			msg.setSendSubject(subject);
			msg.update("host", HostMsgField.getTarget(hosts));
			msg.update("property", property);
			msg.update("value", value);
		} catch (TibrvException e) {
			throw new EventChannelException("Failed to build message");
		}

		try {
			TibrvTransport transport = EventUtils.getDefaultTransport();
			if ( transport != null) transport.send(msg);
		} catch (TibrvException e) {
			throw new EventChannelException("Error sending a message");
		}
	}

	public void getComponentEvents(String componentPath, TibrvMsgCallback cb)
		throws EventChannelException
	{
		if( componentPath == null) componentPath = ">";
		// register for TIBCO events
		String subject = EventUtils.addSubjectPrefix(DCPLib.CHANGE_MSG_TAG + componentPath);
		try {
			new TibrvListener(Tibrv.defaultQueue(), cb, EventUtils.getDefaultTransport(), subject,
												null);
		} catch (TibrvException e) {
			throw new EventChannelException("Failed to create listener");
		}
	}

	public synchronized void closeEventChannel() throws EventChannelException {
		try {
			EventUtils.closeDefaultTransport();
		} catch(TibrvException e) {
				throw new EventChannelException("Exception dispatching default queue");
		}
	}

	private static class ShutdownThread extends Thread {
		public void run() {
			try {
				EventUtils.closeDefaultTransport();
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	private static class Callback implements TibrvMsgCallback {
	    public void onMsg( TibrvListener listener, TibrvMsg msg ) {
	        try {
				System.out.println(msg.getSendSubject() + " -> " + msg.toString());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String args[])
		throws Exception
	{
		try {
			Tibrv.open(Tibrv.IMPL_NATIVE);
			TibrvTransport mTibcoTransport = EventUtils.createDefaultTransport(args[0], null, null);
			Callback clb = new Callback();
			new TibrvListener(Tibrv.defaultQueue(), clb, mTibcoTransport, "cisco.>", null);
			new TibrvListener(Tibrv.defaultQueue(), clb, mTibcoTransport, ">", null);
			new TibrvListener(Tibrv.defaultQueue(), clb, mTibcoTransport, "com.>", null);

			while(true) {
				try {
					Thread.sleep(10000);
				} catch (Exception ex) {}
			}
		}
		catch (TibrvException e) {
			throw new EventChannelException("Can not open event channel");
		}
	}
}

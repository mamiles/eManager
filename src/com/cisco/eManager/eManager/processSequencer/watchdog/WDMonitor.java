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

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.Properties;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Vector;
import java.util.Iterator;

import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;
import com.cisco.eManager.eManager.processSequencer.common.DCPLib;

class WDMonitor extends Thread {
	private WDProxy wdp;

	private boolean enabled;
	private boolean isDestroyed;
	private Object stateLock;

	private CiscoLogger mLogger;

	public WDMonitor(WDProxy wdp)
	{
		super("Monitor-" + wdp.getHostName());
		mLogger = CiscoLogger.getCiscoLogger("watchdog.monitor");
		this.wdp = wdp;
		stateLock = new Object();
		this.enabled = true;
	}

	public void destroy() {
		synchronized( stateLock ) {
			enabled = false;
			isDestroyed = true;
			stateLock.notifyAll();
		}
	}

	public void setEnabled(boolean b) {
		synchronized( stateLock ) {
			enabled = b;
			stateLock.notifyAll();
		}
	}

	public void run() {
		boolean initState = true;

		while ( true ) {

			//normal hb loop sleep time
			int sleepInterval = DCPLib.getIntProperty("watchdog.heartbeat.wds.delay", 3000);

			if (initState)  {//on init or restart
				sleepInterval = DCPLib.getIntProperty("watchdog.heartbeat.wds.initDelay", 1000);
				initState = false;
			}

			try {
				Thread.sleep(sleepInterval);
			} catch (InterruptedException iex) {}

			synchronized(stateLock) {
				while ( ! enabled ) {
					try {
						stateLock.wait(DCPLib.getIntProperty("watchdog.waitDelay", 3000));
					} catch (InterruptedException iex) {}
				}
				initState = true;
				if(isDestroyed) return;
			}

			long t0 = System.currentTimeMillis();
			Object status=null;
			try {
				status = wdp.getWatchdog().heartbeat();
			} catch (Exception ex) {
				mLogger.warning("Heartbeat failed for watchdog on : " + wdp.getHostName());
				status = ex;
			}
			long t1 = System.currentTimeMillis();
			wdp.updateLastHBStats( t1, (t1-t0), status);
		}
	}
}

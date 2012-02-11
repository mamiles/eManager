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

import com.cisco.eManager.eManager.processSequencer.common.DCPLib;
import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;

class WDData implements WDProxy {

	private Object key;
	private Watchdog wd;
	private HostStatus hostStatus;
	private Properties bootConfig;

	private boolean isMaster;
	private WDMonitor wdMonitor;

	private Object wdUpdateLock;
	private int failed;

	private CiscoLogger logger;

	public WDData(String hostName, String ipAddress, Properties bootCfg,
				Object key, Watchdog wd, boolean isMaster)
	{
		this.key = key;
		this.wd = wd;
		this.bootConfig = bootCfg;
		this.hostStatus = new HostStatus(hostName, ipAddress, isMaster, System.currentTimeMillis());
		this.hostStatus.setDirLocations(bootCfg.getProperty("SYSTEM.tmpdir"),
									bootCfg.getProperty("Logging.Defaults.logLocation"),
									bootCfg.getProperty("Logging.Defaults.TaskLogs.logLocation"));
		this.isMaster = isMaster;
		this.wdUpdateLock = new Object();
		this.logger = CiscoLogger.getCiscoLogger("watchdog.monitor");
	}

	public void setImageList(String[] imageList) {
		synchronized ( hostStatus ) {
			hostStatus.setImageList(imageList);
		}
	}

	public HostStatus getHostStatus() {
		synchronized (hostStatus) {
			return (HostStatus) hostStatus.clone();
		}
	}

	public void update(Object key, Watchdog wd){
		synchronized( wdUpdateLock) {
			this.key = key;
			this.wd = wd;
		}
	}

	public void setActive(boolean active) {
		synchronized ( hostStatus ) {
			hostStatus.setActive(active);
			if( active ) {
				hostStatus.setRegisteredAt(System.currentTimeMillis());
				hostStatus.setUnregisteredAt(0);
				hostStatus.updateHeartbeatStats( 0, 0, null);
				hostStatus.setLastHeartbeatSuccessful(null);

				if(wdMonitor == null && !isMaster) {
					wdMonitor = new WDMonitor(this);
					wdMonitor.start();
				} else if ( !isMaster) {
					wdMonitor.setEnabled(true);
				}
			} else {
				wdMonitor.setEnabled(false);
				hostStatus.setUnregisteredAt(System.currentTimeMillis());
				wd = null;
			}
		}
	}

	public long getUnregisteredAt() {
		synchronized ( hostStatus ) {
			return hostStatus.getUnregisteredAt();
		}
	}

	public boolean isActive() {
		synchronized (hostStatus) {
			return hostStatus.isActive();
		}
	}

	public String getHostName() {
		return hostStatus.getHostName();
	}

	public Object getKey() {
		synchronized (wdUpdateLock) {
			return key;
		}
	}

	public Watchdog getWatchdog() {
		synchronized (wdUpdateLock) {
			return wd;
		}
	}

	public void updateLastHBStats(long at, long duration, Object status) {
		synchronized(hostStatus) {
			if( status instanceof Throwable) {
				failed++;
			} else {
				failed = 0;
			}

			hostStatus.updateHeartbeatStats( at, duration, status);

			if( failed == DCPLib.getIntProperty("watchdog.heartbeat.wds.maxAllowedMisses", 3) ){
				logger.warning("About to unregister " + getHostName() + " because of " + failed + " heartbeat misses");
				if ( hostStatus.isActive()) {
					try {
						MasterWatchdogImpl.getMasterWatchdogImpl().unregister(hostStatus.getHostName(), key);
					} catch (Exception ex) {
						logger.warning("Failed to unregister " + getHostName() + " which is no longer responding", ex);
					}
				}
			}
		}
	}

	public Properties getBootConfig() {
		return bootConfig;
	}
}


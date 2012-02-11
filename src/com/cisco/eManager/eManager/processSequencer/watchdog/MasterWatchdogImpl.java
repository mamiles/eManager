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
import java.util.Set;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Vector;
import java.util.Iterator;
import java.util.logging.Level;

import com.cisco.eManager.eManager.processSequencer.common.logging.LogUtil;
import com.cisco.eManager.eManager.processSequencer.common.ConfigStoreException;

/**
 * Implementation of WatchdogManager interface.
 * This is the master watchdog that runs on the
 * master machine and  manages the worker/slave
 * watchdogs if any.
 */
public final class MasterWatchdogImpl extends WatchdogImpl
		implements WatchdogManager, ConfigAccess
{
	private Map mActiveWDs;
	private Map mInactiveWDs;
        protected String emHome;

        private static MasterWatchdogImpl mwdImplInstance = null;

	public static MasterWatchdogImpl getMasterWatchdogImpl() {
		return mwdImplInstance;
	}

	public MasterWatchdogImpl()
		throws RemoteException
	{
		super();
	}

	protected void init()
	{
		mActiveWDs = Collections.synchronizedMap(new TreeMap());
		mInactiveWDs = Collections.synchronizedMap(new TreeMap());
                emHome = System.getProperty("em.home");
		super.init();
		MasterWatchdogImpl.mwdImplInstance = this;
	}

	/**
	 * This method registers a Watchdog object that has to
	 * be managed by the WatchdogManagerImpl
	 *
	 * @param wd - the remote Watchdog object to be registered
	 * @param machineData - information about the host on which 'wd' is running
	 *
	 * @return - a key that has to be sent by the registering watchdog
	 *			along with the request to unregister it.
	 */
	public synchronized Object register(Watchdog wd, String hostName,
					String ipAddress, Properties bootConfig, String[] versionInfo)
		throws RemoteException
	{
		if(wd == null || ( hostName == null || hostName.trim().equals("")))
		{
			throw new WDRemoteException(WDExConstants.INVALID_DATA, WDUtil.toArray("register"));
		}

		WDData wdData = (WDData) mInactiveWDs.remove(hostName);

		Object key;

		key = (double)(System.currentTimeMillis() * (Math.random() + 1)) + "";
		if(wdData == null) {
			wdData = new WDData(hostName, ipAddress, bootConfig, key, wd, (wd == this));
		} else {
			wdData.update(key, wd);
		}
		wdData.setImageList(versionInfo);

		mActiveWDs.put(hostName, wdData);
		wdData.setActive(true);
		mLogger.config("Registered watchdog : " + hostName);
		return key;
	}

	/**
	 * This method unregisters a Watchdog object that is being
	 * managed by the WatchdogManagerImpl
	 *
	 * @param wd - the remote Watchdog object to be unregistered
	 * @param key - the key that is given in reply to the register
	 *		  operation.
	 */
	public synchronized void unregister(String hostName, Object key)
		throws RemoteException, UnknownWatchdogException
	{
		if(key == null || ( hostName == null || hostName.trim().equals("")))
		{
			throw new WDRemoteException(WDExConstants.INVALID_DATA, WDUtil.toArray("unregister"));
		}

		WDData wdData = getActiveWDData(hostName);

		if(wdData != null) {
			if(wdData.getKey().equals(key)) {
				wdData.setActive(false);
				mInactiveWDs.put(hostName, mActiveWDs.remove(hostName));
				mLogger.config("Unregistered watchdog : " + hostName);
			} else {
				throw new WDRemoteException(WDExConstants.ILLEGAL_KEY, WDUtil.toArray("unregister"));
			}
		} else {
			throw new UnknownWatchdogException(hostName);
		}
	}

	public WDServerStatus[] getStatus(HostData hd)
		throws RemoteException
	{
		String hosts[] = findHosts(hd);
		WDServerStatus[] stat = new WDServerStatus[hosts.length];

		for( int i=0; i < hosts.length; ++i) {
			WDData wdd = getActiveWDData(hosts[i]);
			if(wdd == null) {
				stat[i] = new WDServerStatus(hosts[i]);
				stat[i].setError("No watchdog is running on this machine");
			} else if( !wdd.isActive()){
				stat[i] = new WDServerStatus(hosts[i]);
				stat[i].setError("Unregistered at : " + new java.util.Date(wdd.getUnregisteredAt()));
			} else {
				try {
					Watchdog wd = wdd.getWatchdog();
					stat[i] = wd.getStatus();
				} catch (Exception ex) {
					stat[i] = new WDServerStatus(hosts[i]);
					stat[i].setError(ex);
				}
			}
		}

		return stat;
	}

        public String getErrorDescription(HostData hd) throws RemoteException
        {
            WDServerStatus status[] = getStatus(hd);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < status.length; ++i) {
                if (status[i] != null) {
                    Object err = status[i].getError();
                    if (err != null) {
                        if (err instanceof Throwable) {
                            mLogger.severe(( (Throwable) err).getMessage());
                        }
                        else {
                            mLogger.severe((String)err);
                        }
                    }
                    else {
                        ServerStatus processStatus[] = status[i].getServerStatus();
                        for (int j = 0; j < processStatus.length; ++j) {
                            if (processStatus[j].errorDescription != null) {
                                sb.append(LogUtil.getAppType() + ".");
                                sb.append(LogUtil.getAppInst() + ".");
                                sb.append(processStatus[j].getName() + " = ");
                                sb.append(processStatus[j].errorDescription);
                                sb.append("\n");
                            }
                        }
                    }
                }
            }
            if (sb.length() == 0) {
                return null;
            }
            else {
                return sb.toString();
            }
        }

        public String getEMHome() {
            return emHome;
        }

	protected String[] findHosts(HostData hd) {
		String[] hosts = null;
		if( hd.isLocalHost()) {
			hosts = new String[] { getHostName()} ;
		} else {
			if( hd.isAllHosts()) {
				hosts = (String[]) mActiveWDs.keySet().toArray(new String[0]);
			} else {
				hosts = hd.getHosts();
			}

			if( hosts == null) {
				hosts = new String[] { getHostName()} ;
			}
		}
		return hosts;
	}

	public MultiOpStatus[] restart(HostData hd, TargetComponentData targetComponentData)
		throws RemoteException
	{
		String hosts[] = findHosts(hd);
		MultiOpStatus[] result = new MultiOpStatus[hosts.length];

		for(int i=0; i < hosts.length; ++i) {
			try {
				Watchdog wd = getActiveWatchdog(hosts[i]);
				result[i] = wd.restart(targetComponentData);
			} catch (Exception ex) {
				mLogger.warning("In restarting " + targetComponentData + " on " +  hosts[i], ex);
				result[i] = new MultiOpStatus("restart", hosts[i], ex);
			}
		}
		return result;
	}

	public MultiOpStatus[] stop(HostData hd, TargetComponentData targetComponentData)
		throws RemoteException
	{
		String hosts[] = findHosts(hd);
		MultiOpStatus[] result = new MultiOpStatus[hosts.length];

		for(int i=0; i < hosts.length; ++i) {
			try {
				Watchdog wd = getActiveWatchdog(hosts[i]);
				result[i] = wd.stop(targetComponentData);
			} catch (Exception ex) {
				mLogger.warning("In stopping " + targetComponentData + " on " +  hosts[i], ex);
				result[i] = new MultiOpStatus("start", hosts[i], ex);
			}
		}
		return result;
	}

	public MultiOpStatus[] start(HostData hd, TargetComponentData targetComponentData)
		throws RemoteException
	{
		String hosts[] = findHosts(hd);
		MultiOpStatus[] result = new MultiOpStatus[hosts.length];

		for(int i=0; i < hosts.length; ++i) {
			try {
				Watchdog wd = getActiveWatchdog(hosts[i]);
				result[i] = wd.start(targetComponentData);
			} catch (Exception ex) {
				mLogger.warning("In starting " + targetComponentData + " on " +  hosts[i], ex);
				result[i] = new MultiOpStatus("start", hosts[i], ex);
			}
		}
		return result;
	}

	public String[] getRegisteredHosts()
		throws RemoteException
	{
		return (String[]) mActiveWDs.keySet().toArray(new String[0]);
	}

	public HostStatus[] getHostInfo()
		throws RemoteException
	{
		Vector tmp = new Vector(mActiveWDs.size() + mInactiveWDs.size());
		Iterator iter = mActiveWDs.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry entry = (Entry) iter.next();
			String host = (String) entry.getKey();
			WDData wdd = (WDData) entry.getValue();
			tmp.addElement(wdd.getHostStatus());
		}

		iter = mInactiveWDs.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry entry = (Entry) iter.next();
			String host = (String) entry.getKey();
			WDData wdd = (WDData) entry.getValue();
			tmp.addElement(wdd.getHostStatus());
		}

		HostStatus[] ret = new HostStatus[tmp.size()];
		tmp.copyInto(ret);

		return ret;
	}

	public OperationStatus[] getGroup(HostData hd, String groupName)
		throws RemoteException
	{
		String hosts[] = findHosts(hd);
		OperationStatus[] result = new OperationStatus[hosts.length];

		for(int i=0; i < hosts.length; ++i) {
			result[i] = new OperationStatus("group::"+ groupName, hosts[i]);

			try {
				Watchdog wd = getActiveWatchdog(hosts[i]);
				String[] servers = wd.getGroup(groupName);
				result[i].setResult(servers);
			} catch (Exception ex) {
				mLogger.warning("In getting group " + groupName + " from " + hosts[i], ex);
				result[i].setError(ex);
			}
		}
		return result;
	}

	public OperationStatus[] getConfigProperty(HostData hd, String property)
		throws RemoteException
	{
		String hosts[] = findHosts(hd);
		OperationStatus[] result = new OperationStatus[hosts.length];

		for(int i=0; i < hosts.length; ++i) {
			result[i] = new OperationStatus("configProperty", hosts[i]);
			try {
				Watchdog wd = getActiveWatchdog(hosts[i]);
				String prop = wd.getConfigProperty(property);
				result[i].setResult(prop);
			} catch (Exception ex) {
				mLogger.warning("In getting config from " + hosts[i], ex);
				result[i].setError(ex);
			}
		}
		return result;
	}

	public OperationStatus[] getHealth(HostData hd)
		throws RemoteException
	{
		String hosts[] = findHosts(hd);
		OperationStatus[] result = new OperationStatus[hosts.length];

		for(int i=0; i < hosts.length; ++i) {
			result[i] = new OperationStatus("health", hosts[i]);
			try {
				Watchdog wd = getActiveWatchdog(hosts[i]);
				boolean health= wd.isHealthy();
				result[i].setResult(new Boolean(health));
			} catch (Exception ex) {
				mLogger.warning("In getting health from " + hosts[i], ex);
				result[i].setError(ex);
			}
		}
		return result;
	}

	public OperationStatus[] getGroups(HostData hd)
		throws RemoteException
	{
		String hosts[] = findHosts(hd);
		OperationStatus[] result = new OperationStatus[hosts.length];

		for(int i=0; i < hosts.length; ++i) {
			result[i] = new OperationStatus("groups", hosts[i]);
			try {
				Watchdog wd = getActiveWatchdog(hosts[i]);
				String[] groups = wd.getGroups();
				result[i].setResult(groups);
			} catch (Exception ex) {
				mLogger.warning("In getting groups from " + hosts[i], ex);
				result[i].setError(ex);
			}
		}
		return result;
	}


	public void shutdown(HostData hd)
		throws RemoteException
	{
		synchronized (this) {
			String hosts[] = findHosts(hd);

			Thread[] shuts = new Thread[hosts.length];

			for( int i=0; i < hosts.length; ++i) {
				try {
					Watchdog wd = getActiveWatchdog(hosts[i]);
					if(wd == this) {
						throw new IllegalArgumentException("Cannot shutdown master itself through this API");
					} else if (wd != null) {
						shuts[i] = new ShutdownThread(wd, hosts[i]);
					}
				} catch (Exception ex) { }
			}

			for(int i=0; i < shuts.length; ++i) {
				if ( shuts[i] != null) {
					shuts[i].start();
				}
			}

			for(int i=0; i < shuts.length; ++i) {
				if(shuts[i] != null) {
					try {
						shuts[i].join();
					} catch (InterruptedException iex){}
				}
			}
		}
	}

	void shutdownAll() {

		synchronized (this ) {
			Iterator iter = mActiveWDs.values().iterator();

			Thread[] shuts = new Thread[mActiveWDs.size() -1];

			int i=0;
			while(iter.hasNext()) {
				WDData wdd = (WDData) iter.next();
				Watchdog wd = wdd.getWatchdog();
				if ( wd != this ) {
					shuts[i] = new ShutdownThread(wd, wdd.getHostName());
					shuts[i].start();
				}
			}

			for(i=0; i < shuts.length; ++i) {
				if(shuts[i] != null) {
					try {
						shuts[i].join();
					} catch (InterruptedException iex){}
				}
			}

			mLogger.finer("Application System Shutdown complete.");
		}
		shutdown(false, false);
	}

	synchronized WDData getActiveWDData(String name) {
		if( name == null) {
			name = getHostName();
		}
		return (WDData) mActiveWDs.get(name);
	}


	/**
	 * Same logic as getOnlineWatchdog. But throws a different exception.
	 */
	synchronized Watchdog getActiveWatchdog(String hostName)
		throws WDException
	{
		WDData wdd = getActiveWDData(hostName);
		if( wdd == null) {
			throw new WDException(WDExConstants.WD_INACTIVE, WDUtil.toArray(hostName));
		}
		return wdd.getWatchdog();
	}

	/**
	 * Same logic as getActiveWatchdog. But throws a different exception.
	 */
	private synchronized Watchdog getOnlineWatchdog(String hostName)
		throws WDRemoteException
	{
		WDData wdd = getActiveWDData(hostName);
		if( wdd == null) {
			throw new WDRemoteException(WDExConstants.WD_INACTIVE, WDUtil.toArray(hostName));
		}
		return wdd.getWatchdog();
	}

	public Properties getBootConfiguration(String hostName)
		throws RemoteException, ConfigStoreException
	{
		WDData wdd = getActiveWDData(hostName);
		if( wdd == null) {
			throw new WDRemoteException(WDExConstants.WD_INACTIVE, WDUtil.toArray(hostName));
		}
		return wdd.getBootConfig();
	}

	public Properties getConfiguration(String hostName, String version, boolean merge)
		throws RemoteException, ConfigStoreException
	{
		return ConfigAdapter.getInstance().getConfiguration(hostName, version, merge);
	}

	public void setProperty(String hostName, String property, String value)
		throws RemoteException, ConfigStoreException
	{
		Map map = ConfigAdapter.getInstance().setProperty(hostName, property, value);
		try {
			Watchdog wd = getOnlineWatchdog(hostName);
			ConfigChangeData ccd = new ConfigChangeData(map);
			wd.configurationChanged(ccd);
		} catch (Exception ex) {
			mLogger.info("Could not notify the watchdog on " + hostName + " about the new property value", ex);
		}
	}

	public void setProperties(String hostName, Map propValues)
		throws RemoteException, ConfigStoreException
	{
		Map map = ConfigAdapter.getInstance().setProperties(hostName, propValues);
		try {
			Watchdog wd = getOnlineWatchdog(hostName);
			ConfigChangeData ccd = new ConfigChangeData(map);
			wd.configurationChanged(ccd);
		} catch (Exception ex) {
			mLogger.info("Could not notify the watchdog on " + hostName + " about the new property values", ex);
		}
	}

	public void setProperty(String[] hostNames, String property, String value)
		throws RemoteException, ConfigStoreException
	{
		for(int i=0; i < hostNames.length; ++i) {
			try {
				Map map = ConfigAdapter.getInstance().setProperty(hostNames[i], property, value);
				Watchdog wd = getOnlineWatchdog(hostNames[i]);
				wd.configurationChanged(new ConfigChangeData(map));
			} catch (Exception ex) {
				throw new RemoteException("SetProperty", ex);
			}
		}
	}

	public String getProperty(String hostName, String property, String version)
		throws RemoteException, ConfigStoreException
	{
		return ConfigAdapter.getInstance().getProperty(hostName, property, version);
	}

	public Map getProperty(String[] hostNames, String property, String version)
		throws RemoteException, ConfigStoreException
	{
		return ConfigAdapter.getInstance().getProperty(hostNames, property, version);
	}

	public Map getProperties(String[] hostNames, String[] properties)
		throws RemoteException, ConfigStoreException
	{
		return ConfigAdapter.getInstance().getProperties(hostNames, properties);
	}

	public String[] getVersions(String hostName)
		throws RemoteException, ConfigStoreException
	{
		return ConfigAdapter.getInstance().getVersions(hostName);
	}

	public Map getComponentProperties(String hostName, String version, String compName)
		throws RemoteException, ConfigStoreException
	{
		return ConfigAdapter.getInstance().getComponentProperties(hostName, version, compName);
	}


	public Map findMatches(String hostName, String version, String pattern, int pos)
		throws RemoteException, ConfigStoreException
	{
		return ConfigAdapter.getInstance().findMatches(hostName, version, pattern, pos);
	}

	public String createNewVersion(String hostName)
		throws RemoteException, ConfigStoreException
	{
		String version = ConfigAdapter.getInstance().createNewVersion(hostName);
		try {
			Watchdog wd = getOnlineWatchdog(hostName);
			ConfigChangeData ccd = new ConfigChangeData();
			wd.configurationChanged(ccd);
		} catch (Exception ex) {
			mLogger.info("Could not notify the watchdog on " + hostName + " about the new version", ex);
		}
		return version;
	}

	public void setCurrentVersion(String hostName, String olderVersion)
		throws RemoteException, ConfigStoreException
	{
		ConfigAdapter.getInstance().setCurrentVersion(hostName, olderVersion);
		try {
			Watchdog wd = getOnlineWatchdog(hostName);
			ConfigChangeData ccd = new ConfigChangeData();
			wd.configurationChanged(ccd);
		} catch (Exception ex) {
			mLogger.info("Could not notify the watchdog on " + hostName + " about the version change", ex);
		}
	}

	public void clearCache()
		throws RemoteException, ConfigStoreException
	{
		ConfigAdapter.getInstance().clearCache();
	}

	private class ShutdownThread extends Thread {

		Watchdog wd;
		String hostName;

		ShutdownThread (Watchdog wd, String hname) {
			this.wd = wd;
			this.hostName = hname;
		}

		public void run() {
			try {
				wd.shutdownNow();
			} catch (Exception ex) {
				mLogger.warning("Failed to shut down server on "
					+ hostName,  ex);
			}
		}
	}
}

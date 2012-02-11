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

import java.util.Map;
import java.util.Properties;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.cisco.eManager.eManager.processSequencer.common.ConfigStoreException;
import com.cisco.eManager.eManager.processSequencer.common.NVPair;

/**
 * The interface to manage the configuration
 * of eManager system. <p>
 *
 * @author Rama Taraniganty
 */
public interface ConfigAccess extends java.rmi.Remote
{
	public static final int ANY_POS   = 0;
	public static final int BEGIN_POS = 100;
	public static final int END_POS   = 200;

	public Properties getBootConfiguration(String hostName)
		throws RemoteException, ConfigStoreException;;

	public Properties getConfiguration(String hostName, String version, boolean merge)
		throws RemoteException, ConfigStoreException;

	public void setProperty(String hostName, String property, String value)
		throws RemoteException, ConfigStoreException;

	public void setProperty(String[] hostNames, String property, String value)
		throws RemoteException, ConfigStoreException;

	public void setProperties(String hostName, Map propValues)
		throws RemoteException, ConfigStoreException;

	public String getProperty(String hostName, String property, String version)
		throws RemoteException, ConfigStoreException;

	public Map getProperty(String[] hostNames, String property, String version)
		throws RemoteException, ConfigStoreException;

	public Map getProperties(String[] hostNames, String[] property)
		throws RemoteException, ConfigStoreException;

	public String[] getVersions(String hostName)
		throws RemoteException, ConfigStoreException;

	public Map getComponentProperties(String hostName, String version, String component)
		throws RemoteException, ConfigStoreException;

	public Map findMatches(String hostName, String version, String pattern, int pos)
		throws RemoteException, ConfigStoreException;

	public void clearCache()
		throws RemoteException, ConfigStoreException;

	public String createNewVersion(String hostName)
		throws RemoteException, ConfigStoreException;

	public void setCurrentVersion(String hostName, String olderVersion)
		throws RemoteException, ConfigStoreException;
}



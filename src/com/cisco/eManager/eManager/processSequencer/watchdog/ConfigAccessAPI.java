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

import java.util.Collections;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.Properties;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.cisco.eManager.eManager.processSequencer.watchdog.WatchdogManager;
import com.cisco.eManager.eManager.processSequencer.watchdog.ConfigAccessException;


/**
 * The implementation to manage the configuration
 * of eManager system. <p>
 */

public final class ConfigAccessAPI
{
	private static ConfigAccessAPI msInstance;

	public static synchronized ConfigAccessAPI getInstance()
		throws ConfigAccessException
	{
		if( msInstance == null) {
			msInstance = new ConfigAccessAPI();
		}
		return msInstance;
	}

	private ConfigAccess mCfgAccess;

	private ConfigAccessAPI()
		throws ConfigAccessException
	{
		try {
//			mCfgAccess = (ConfigAccess) RemoteUtil.lookup(WDConstants.WD_SERVICE, ConfigAccess.class, true);
		} catch (Exception ex) {
			throw new ConfigAccessException("200", ex);
		}
	}

	public Properties getBootConfig(String hostName)
		throws ConfigAccessException
	{
		try {
			return mCfgAccess.getBootConfiguration(hostName);
		} catch (Exception ex){
			throw new ConfigAccessException("201", ex);
		}
	}

	public void clearCache()
		throws ConfigAccessException
	{
		try {
			mCfgAccess.clearCache();
		} catch (Exception ex){
			throw new ConfigAccessException("201", ex);
		}
	}

	public Properties getConfiguration(String hostName, String version)
		throws ConfigAccessException
	{
		try {
			return mCfgAccess.getConfiguration(hostName, version, true);
		} catch (Exception ex){
			throw new ConfigAccessException("201", ex);
		}
	}


	public void setProperty(String hostName, String property, String value)
		throws ConfigAccessException
	{
		try {
			mCfgAccess.setProperty(hostName, property, value);
		} catch (Exception ex){
			throw new ConfigAccessException("201", ex);
		}
	}


	public void setProperty(String[] hostNames, String property, String value)
		throws ConfigAccessException
	{
		try {
			mCfgAccess.setProperty(hostNames, property, value);
		} catch (Exception ex){
			throw new ConfigAccessException("201", ex);
		}
	}


	public void setProperties(String hostName, Map propValues)
		throws ConfigAccessException
	{
		try {
			mCfgAccess.setProperties(hostName, propValues);
		} catch (Exception ex){
			throw new ConfigAccessException("201", ex);
		}
	}

	public String getProperty(String hostName, String version, String property)
		throws ConfigAccessException
	{
		try {
			return mCfgAccess.getProperty(hostName, property, version);
		} catch (Exception ex){
			throw new ConfigAccessException("201", ex);
		}
	}


	public Map getProperty(String[] hostNames, String property)
		throws ConfigAccessException
	{
		try {
			return mCfgAccess.getProperty(hostNames, property, null);
		} catch (Exception ex){
			throw new ConfigAccessException("201", ex);
		}
	}


	public Map getProperties(String[] hostNames, String[] properties)
		throws ConfigAccessException
	{
		try {
			return mCfgAccess.getProperties(hostNames, properties);
		} catch (Exception ex){
			throw new ConfigAccessException("201", ex);
		}
	}


	public String[] getVersions(String hostName)
		throws ConfigAccessException
	{
		try {
			return mCfgAccess.getVersions(hostName);
		} catch (Exception ex){
			throw new ConfigAccessException("201", ex);
		}
	}

	public Map getComponentProperties(String hostName, String version, String component)
		throws ConfigAccessException
	{
		try {
			if( component.endsWith(".")) {
				component = component.substring(0, component.length());
			}
			return mCfgAccess.getComponentProperties(hostName, version, component);
		} catch (Exception ex){
			throw new ConfigAccessException("201", ex);
		}
	}

	public Map findMatches(String hostName, String version, String pattern)
		throws ConfigAccessException
	{
		try {
			return mCfgAccess.findMatches(hostName, version, pattern, ConfigAccess.ANY_POS);
		} catch (Exception ex){
			throw new ConfigAccessException("201", ex);
		}
	}

	public String getMetaConfigFileName()
		throws ConfigAccessException
	{
		String bootdir = System.getProperty("em.home");
		if( bootdir == null ) {
			throw new ConfigAccessException("The boot directory is not specified");
		} else {
			java.io.File file = new java.io.File (bootdir + com.cisco.eManager.eManager.processSequencer.common.ConfigStore.CONFIG + "app.properties.xml.tmpl");
			if( file.exists()) {
				if( file.canRead()) {
					return file.getAbsolutePath();
				} else {
					throw new ConfigAccessException(file.getAbsolutePath() + " is not readable");
				}
			} else {
				throw new ConfigAccessException(file.getAbsolutePath() + " does not exist");
			}
		}
	}

	public String createNewVersion(String hostName)
		throws ConfigAccessException
	{
		try {
			return mCfgAccess.createNewVersion(hostName);
		} catch (Exception ex){
			throw new ConfigAccessException("202", ex);
		}
	}

	public void setCurrentVersion(String hostName, String olderVersion)
		throws ConfigAccessException
	{
		try {
			mCfgAccess.setCurrentVersion(hostName, olderVersion);
		} catch (Exception ex){
			throw new ConfigAccessException("202", ex);
		}
	}



	public static void main(String[] args)
			throws Exception
	{
		ConfigAccessAPI capi = ConfigAccessAPI.getInstance();
		System.out.println(capi.getMetaConfigFileName());
	}
}


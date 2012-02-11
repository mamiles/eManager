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
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Properties;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.cisco.eManager.eManager.processSequencer.common.*;

/**
 * The implementation to manage the configuration
 * of App system. <p>
 */

final class ConfigAdapter
{
	private static ConfigAdapter msInstance;

	static synchronized ConfigAdapter getInstance()
		throws ConfigStoreException
	{
		if( msInstance == null) {
			msInstance = new ConfigAdapter();
		}
		return msInstance;
	}

	private ConfigStore mCfgStore;

	private ConfigAdapter()
		throws ConfigStoreException
	{
		mCfgStore = ConfigStore.getInstance();
	}

	void clearCache() {
		//mCfgStore.clear();
	}

	Properties getBootConfiguration(String hostName)
	{
		WDData wdd = MasterWatchdogImpl.getMasterWatchdogImpl().getActiveWDData(hostName);
		if( wdd == null) return null;
		return wdd.getBootConfig();
	}

	Properties getLocalBootConfiguration()
		throws ConfigStoreException
	{
		return mCfgStore.readBootConfig();
	}

	Properties getConfiguration(String hostName, String version)
		throws ConfigStoreException
	{
		return getConfiguration(hostName, version, true);
	}

	Properties getConfiguration(String hostName, String version, boolean merge)
		throws ConfigStoreException
	{
		//long t1 = System.currentTimeMillis();
		Properties actProps = mCfgStore.readConfigForHost(hostName, version);
		//System.out.println( System.currentTimeMillis() -t1 );

		if( actProps != null) {
			if( merge ) {
				Properties bootProps = getBootConfiguration(hostName);
				Properties props = actProps;
				if( bootProps != null) {
					props  = (Properties) actProps.clone();
					props.putAll(bootProps);
				}
				return props;
			}
		}
		//System.out.println( System.currentTimeMillis() -t1 );
		//System.out.println("---------------------");
		return actProps;
	}

	Map setProperty(String hostName, String property, String value)
		throws ConfigStoreException
	{
		Properties  actProps = getConfiguration(hostName, null, false);
		if ( actProps == null) return null;
		actProps.put(property, value);

		Map retMap = new HashMap();
		retMap.put(property, value);

		int index =	property.indexOf(".logLevel") ;
		int lllen = ".logLevel".length();

		if( index == (property.length() - lllen ) )
		{
			Set matches = findMatchingKeys(hostName, null,
						property.substring(0,index+1), ConfigAccess.BEGIN_POS);

			if( matches != null ) {
				Iterator iter = matches.iterator();

				while ( iter.hasNext() ) {
					String matchName = (String) iter.next();
					if( matchName.indexOf(".logLevel") == (matchName.length() - lllen ) )
					{
						actProps.put(matchName, value);
						retMap.put(matchName, value);
					}
				}
			}
		}
		mCfgStore.saveConfigForHost(hostName, actProps);
		return retMap;
	}

	Map setProperties(String hostName, Map propValues)
		throws ConfigStoreException
	{
		Properties  actProps = getConfiguration(hostName, null, false);
		if(actProps == null) return null;

		Map retMap = new HashMap();

		int lllen = ".logLevel".length();

		Iterator entries = propValues.entrySet().iterator();

		while (entries.hasNext()) {
			Entry entry = (Entry) entries.next();
			String property = (String) entry.getKey();
			String value = (String) entry.getValue();

			actProps.put(property, value);
			retMap.put(property, value);

			int index =	property.indexOf(".logLevel") ;

			if( index == (property.length() - lllen ) ) {
				Set matches = findMatchingKeys(hostName, null,
							property.substring(0,index+1), ConfigAccess.BEGIN_POS);

				if( matches != null ) {
					Iterator iter = matches.iterator();

					while ( iter.hasNext() ) {
						String matchName = (String) iter.next();
						if( matchName.indexOf(".logLevel") == (matchName.length() - lllen ) )
						{
							actProps.put(matchName, value);
							retMap.put(matchName, value);
						}
					}
				}
			}
			mCfgStore.saveConfigForHost(hostName, actProps);
		}

		return retMap;
	}

	Map setProperty(String[] hostNames, String property, String value)
		throws ConfigStoreException
	{
		Map retMap = new HashMap();
		for(int i=0; i < hostNames.length; ++i) {
			retMap.put(hostNames[i], setProperty(hostNames[i], property, value));
					//each set is in its own transaction
		}
		return retMap;
	}

	String getProperty(String hostName, String property)
		throws ConfigStoreException
	{
		return getProperty(hostName, property, null);
	}

	String getProperty(String hostName, String property, String version)
		throws ConfigStoreException
	{
		//long t1 = System.currentTimeMillis();
		Properties bootProps = getBootConfiguration( hostName);
		String value = null;
		if( bootProps != null) {
			value = bootProps.getProperty(property);
		}
		//System.out.println( System.currentTimeMillis() -t1 );

		if( value == null ) {
			Properties actProps = getConfiguration(hostName, version, false);
			//System.out.println( System.currentTimeMillis() -t1 );
			if( actProps != null) {
				value = actProps.getProperty(property);
			}
		}

		//System.out.println( System.currentTimeMillis() -t1 );
		//System.out.println("---------------------");
		return value;
	}


	Map getProperty(String[] hostNames, String property)
		throws ConfigStoreException
	{
		return getProperty(hostNames, property, null);
	}

	Map getProperty(String[] hostNames, String property, String version)
		throws ConfigStoreException
	{
		Map map = new HashMap();
		for (int i=0; i < hostNames.length; ++i) {
			map.put(hostNames[i], getProperty(hostNames[i], property, version));
		}
		return map;
	}

	Map getProperties(String[] hostNames, String[] property)
		throws ConfigStoreException
	{
		Map map = new HashMap();

		for (int i=0; i < hostNames.length; ++i) {
			Properties bootProps = getBootConfiguration(hostNames[i]);
			Properties actProps = getConfiguration(hostNames[i], null, false);
			String[] values = new String[property.length];
			for(int j=0; j < property.length; ++j) {
				if (bootProps != null) {
					values[j] = bootProps.getProperty(property[j]);
				}
				if( values[j] == null && actProps != null) {
					values[j] = actProps.getProperty(property[j]);
				}
			}
			map.put(hostNames[i], values);
		}
		return map;
	}

	String[] getVersions(String hostName)
		throws ConfigStoreException
	{
		return mCfgStore.getVersions(hostName);
	}

	Map getComponentProperties( String hostName, String version, String component)
		throws ConfigStoreException
	{
		Map treeMap = new TreeMap();
		Properties bootProps = getBootConfiguration(hostName);
		Properties actProps = getConfiguration(hostName, version);

		if ( bootProps != null) {
			getComponentProperties ( bootProps, treeMap, component);
		}
		if( actProps != null) {
			getComponentProperties ( actProps, treeMap, component);
		}

		return treeMap;
	}

	void getComponentProperties( Properties props, Map m, String component)
		throws ConfigStoreException
	{
		int patLen = component.length();
		ArrayList al = new ArrayList();

		Iterator iter = props.entrySet().iterator();
		while( iter.hasNext()) {
			Entry ent = (Entry) iter.next();
			String key = (String) ent.getKey();
			if( key.startsWith(component)) {
				if(key.lastIndexOf('.') == patLen) {
					m.put(key, ent.getValue());
				}
			}

		}
	}

	Map findMatches(String hostName, String pattern)
		throws ConfigStoreException
	{
		return findMatches(hostName, null, pattern, ConfigAccess.ANY_POS);
	}

	Map findMatches(String hostName, String version, String pattern, int position)
		throws ConfigStoreException
	{
		Map treeMap = new TreeMap();
		Properties bootProps = getBootConfiguration(hostName);
		Properties actProps = getConfiguration(hostName, version);

		if ( bootProps != null) {
			findMatches ( bootProps, treeMap, pattern, position);
		}
		if( actProps != null) {
			findMatches ( actProps, treeMap, pattern, position);
		}

		return treeMap;
	}

	private void findMatches ( Properties props, Map sink, String pattern, int position)
		throws ConfigStoreException
	{
		if( props ==null) return;

		Iterator keys = props.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			switch ( position ) {

				case ConfigAccess.ANY_POS :

					if( key.indexOf(pattern) != -1) {
						sink.put(key, props.get(key));
					}
					break;

				case ConfigAccess.BEGIN_POS :
					if( key.startsWith(pattern)) {
						sink.put(key, props.get(key));
					}
					break;
				case ConfigAccess.END_POS :

					if( key.endsWith(pattern)) {
						sink.put(key, props.get(key));
					}
					break;
			}
		}
	}

	Set findMatchingKeys(String hostName, String version, String pattern, int position)
		throws ConfigStoreException
	{
		Set hashSet = new HashSet();
		Properties bootProps = getBootConfiguration(hostName);
		Properties actProps = getConfiguration(hostName, version);

		if ( bootProps != null) {
			findMatchingKeys ( bootProps, hashSet, pattern, position);
		}
		if( actProps != null) {
			findMatchingKeys ( actProps, hashSet, pattern, position);
		}

		return hashSet;
	}

	private void findMatchingKeys ( Properties props, Set sink, String pattern, int position)
		throws ConfigStoreException
	{
		if( props ==null) return;

		Iterator keys = props.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			switch ( position ) {

				case ConfigAccess.ANY_POS :

					if( key.indexOf(pattern) != -1) {
						sink.add(key);
					}
					break;

				case ConfigAccess.BEGIN_POS :
					if( key.startsWith(pattern)) {
						sink.add(key);
					}
					break;
				case ConfigAccess.END_POS :

					if( key.endsWith(pattern)) {
						sink.add(key);
					}
					break;
			}
		}
	}

	String createNewVersion(String hostName)
		throws ConfigStoreException
	{
		return mCfgStore.createNewVersion(hostName);
	}

	void setCurrentVersion(String hostName, String olderVersion)
		throws ConfigStoreException
	{
		mCfgStore.setCurrentVersion(hostName, olderVersion);
	}

}


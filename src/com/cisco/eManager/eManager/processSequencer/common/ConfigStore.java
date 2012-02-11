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

import java.util.Vector;
import java.util.Properties;
import java.util.Map;

import java.io.*;

//for HA
import com.cisco.eManager.eManager.processSequencer.common.PSInetAddress;

public abstract class ConfigStore {

	protected ConfigStore() {}

	public static final String FS_CFG_STORE = "com.cisco.eManager.eManager.processSequencer.common.FSConfigStore";

	public static final String CONFIG = File.separator + "config" + File.separator;

	private static ConfigStore cfgStore;

	public synchronized static final ConfigStore getInstance()
		throws ConfigStoreException
	{
		if( cfgStore == null ) {
			// see if the system wants to override the default class for
			// the config store.
			String className = System.getProperty("em.config.className");

			if (className == null)
			{
				className = FS_CFG_STORE;

				String emHome = System.getProperty("em.home");

				if( emHome == null) {
					throw new ConfigStoreException("em.home is not set");
				}
			}

			try {
				Class clazz = Class.forName(className);
				cfgStore = (ConfigStore) clazz.newInstance();
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
				throw new ConfigStoreException("The class " + className +
								" was not found");
			} catch (IllegalAccessException ilae) {
				ilae.printStackTrace();
				throw new ConfigStoreException("The class " + className +
								" could not be accessed");
			} catch (InstantiationException inse) {
				inse.printStackTrace();
				throw new ConfigStoreException("Could not instantiate class " + className);
			} catch (ClassCastException cce) {
				cce.printStackTrace();
				throw new ConfigStoreException("The class " + className +
								" could not be cast to a ConfigStore");
			}
			cfgStore.init();
		}

		return cfgStore;
	}

	protected Vector mInitListeners;
	protected boolean mIsMaster;

	protected static Properties msBootCfg;
	protected boolean mInited;

	protected final Object INIT_LOCK = new Object();

	public void init()
		throws ConfigStoreException
	{
		synchronized ( INIT_LOCK ) {
			readBootConfig();
			mInited = true;
		}
	}

	public boolean isMasterStore() {
		return mIsMaster;
	}

	protected void checkInit(String operation)
		throws ConfigStoreException
	{
		if( ! mInited ) {
			throw new ConfigStoreException(operation, ConfigStoreException.NOT_INITED);
		}
	}

	protected void checkAccess(String operation)
		throws ConfigStoreException
	{
		if( ! isMasterStore() ) {
			throw new ConfigStoreException(operation, ConfigStoreException.MASTER_ONLY);
		}
	}

	public static boolean canAccess(String fileName)
	{
		if(fileName == null || fileName.trim().equals("")) return false;

		File file = new File(fileName);
		if( file.exists() && file.canRead() ) {
			return true;
		}
		return false;
	}

	public static boolean isLocalHost(String host) {

		if( host == null || host.equals("localhost") ) return true;

		if( host.equals(getFQDN()) || host.equals(getHostName()) ) {
			return true;
		}
		return false;
	}

	public static String getFQDN(){
		try {
			return PSInetAddress.getLocalHost().getHostName();
		} catch(Exception ex) {
			return "localhost";
		}
	}

	public static String getHostName(){
		String hostName	= null;

		try {
			hostName = PSInetAddress.getLocalHost().getHostName();
		} catch(Exception ex) {
			return "localhost";
		}

		int dot = hostName.indexOf('.');
		if(dot != -1) {
			return hostName.substring(0, dot);
		}
		return hostName;
	}

	/**
	 * Loads the boot properties that provide boot strapping config
	 * information.<p>
	 * Loads the properties file pointed to by em.boot.file system
	 * property (if it exists) and returns the corresponding Properties
	 * object. If this property is not set, in directory given by the
	 * system property "em.bootdir", this method looks for the files : <p>
	 * <hostname>_boot.properties	followed by boot.properties <br>
	 * Builds a Properties object using the first one found.
	 */
	protected static Properties getBootProperties(String hostName) {

		Properties bootProps = new Properties();

		String bootPropsFile = System.getProperty("em.boot.properties");
		if ( bootPropsFile != null) {
			if( canAccess(bootPropsFile)) {
				try {
					bootProps.load(new BufferedInputStream(new FileInputStream(bootPropsFile)));
				} catch(IOException ex) {
					System.out.println("Could not load boot properties from " + bootPropsFile + " ; " + ex.getMessage());
				}
			}
			return bootProps;
		}

		String homeDir = System.getProperty("em.home");

		String defaultBootFile = homeDir + CONFIG + "boot.properties";
		String hostSpecificBootFile = homeDir + CONFIG + getHostName() + "_boot.properties";


		if( canAccess(defaultBootFile)) {
			try {
				bootProps.load(new BufferedInputStream(new FileInputStream(defaultBootFile)));
			} catch(IOException ex) {
					System.out.println("Could not load boot properties from " + defaultBootFile + " ; " + ex.getMessage());
			}
		}

		if( canAccess( hostSpecificBootFile )) {
			Properties hsProps = new Properties();
			try {
				hsProps.load(new BufferedInputStream(new FileInputStream(hostSpecificBootFile)));
			} catch(IOException ex) {
					System.out.println("Could not load boot properties from " + hostSpecificBootFile + " ; " + ex.getMessage());
			}
			bootProps.putAll(hsProps);
		}

		return bootProps;
	}

	public Properties readConfigForHost(String host)
		throws ConfigStoreException
	{
		if( host == null) host = getFQDN();
		return readConfigForHost(host, null);
	}

	public String saveConfigForHost(String host, Properties cfg)
		throws ConfigStoreException
	{
		if( host == null) host = getFQDN();
		return saveConfigForHost(host, cfg, null);
	}


	public static synchronized Properties readBootConfig()
		throws ConfigStoreException
	{
		if( msBootCfg == null) msBootCfg = getBootProperties(null);
		return msBootCfg;
	}

	/**
	 * Called only when eManager jar files are being used remotely as in
	 * a Java Application or Applet.
	 */
	public static void setBootProperties(Properties props)
	{
		if ( msBootCfg == null)
			msBootCfg = props;
	}

	public boolean isInited() {
		synchronized ( INIT_LOCK ) {
			return mInited;
		}
	}

	public void addConfigInitListener(ConfigInitListener cil) {
		synchronized ( INIT_LOCK ) {
			if( mInitListeners == null) mInitListeners = new java.util.Vector(2);
			mInitListeners.add(cil);
		}
	}

	public void removeConfigInitListener(ConfigInitListener cil) {
		synchronized ( INIT_LOCK ) {
			if( mInitListeners == null) return;
			mInitListeners.remove(cil);
		}
	}

	protected void informInitListeners(Properties props) {
		Properties cProps = (Properties) props.clone();
		synchronized ( INIT_LOCK ) {
			if ( mInitListeners != null) {
				int sz = mInitListeners.size();
				for(int i=0; i < sz; ++i) {
					try {
						((ConfigInitListener)mInitListeners.elementAt(i)).configInited(cProps);
					} catch (Exception ex) {}
				}
			}
		}
	}

	public abstract String saveConfigForHost(String host, Properties cfg, String version)
		throws ConfigStoreException;

	public abstract void saveConfigsForHosts(Map map)
		throws ConfigStoreException;

	public abstract String[] getVersions(String hostName)
		throws ConfigStoreException;

	public abstract Properties readConfigForHost(String host, String version)
		throws ConfigStoreException;

	public String createNewVersion(String hostName)
		throws ConfigStoreException
	{
		return createVersion(hostName, null);
	}

	public abstract String createVersion(String hostName, Properties props)
		throws ConfigStoreException;

	public abstract String setCurrentVersion(String hostName, String olderVersion)
		throws ConfigStoreException;

}



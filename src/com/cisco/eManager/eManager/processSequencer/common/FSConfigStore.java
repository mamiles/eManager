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
package com.cisco.eManager.eManager.processSequencer.common;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.io.*;

public class FSConfigStore extends ConfigStore {

	private static final String PROPS_FILE = "app.properties";

	protected String mFileLocation;
	protected String mDirLocation;
	protected String mFileName;

	protected LockMap mLockMap;
	protected Map mCfgMap;

	public FSConfigStore() {
		String home = System.getProperty("em.home");
		mFileLocation = System.getProperty("app.properties.file");

		if( mFileLocation == null ) {
			mFileLocation = home + ConfigStore.CONFIG + PROPS_FILE;
		}


		if(! canAccess(mFileLocation))  {
			mFileLocation = null;
		}

		if( mFileLocation != null) {
			int index = mFileLocation.lastIndexOf(File.separator);
			if(index == mFileLocation.length() -1) {
				mFileLocation = mFileLocation.substring(0, index);
				index = mFileLocation.lastIndexOf(File.separator);
			}

			if( index != -1) {
				mDirLocation = mFileLocation.substring(0, index);
				mFileName = mFileLocation.substring(index+1);
			}
		}
		mLockMap = new LockMap();
		mCfgMap  = Collections.synchronizedMap(new HashMap());
		mInited = true;
	}

	public String saveConfigForHost(String host, Properties cfg)
		throws ConfigStoreException
	{
		Object lock = mLockMap.getLock(host);
		try {
			if(mDirLocation != null) {
				synchronized(lock) {
					mCfgMap.put(host, cfg);
					return storeConfiguration(getFileName(host), cfg);
				}
			}
		} finally {
			mLockMap.releaseLock(host);
		}
		throw new ConfigStoreException("saveConfigForHost(host,cfg)", ConfigStoreException.CANNOT_LOCATE);
	}

	public String saveConfigForHost(String host, Properties cfg, String version)
		throws ConfigStoreException
	{
		if( version == null ) {
			return saveConfigForHost(host, cfg);
		}

		throw new ConfigStoreException("saveConfigForHost(host,cfg,version)", ConfigStoreException.UNSUPPORTED);
	}

	public void saveConfigsForHosts(Map map)
		throws ConfigStoreException
	{
		throw new ConfigStoreException("saveConfigForHost(host,cfg,version)", ConfigStoreException.UNSUPPORTED);
	}

	protected String storeConfiguration(String fileLocation, Properties cfg)
		throws ConfigStoreException
	{
		try {
			PrintStream ps = new PrintStream(new BufferedOutputStream(
											new FileOutputStream(fileLocation)));
			if( cfg != null) {
				cfg.store(ps, "Modified and saved at : " + new java.util.Date().toString());
			}
			ps.flush();
			ps.close();

			try {
			    updateWebStartJars();
			}
			catch (Exception cse) {
			    // ignore as its not fatal to saving em.properties
			}

			return Long.toString(new File(fileLocation).lastModified());
		} catch (Exception ex) {
			throw new ConfigStoreException("save(" + fileLocation +")");
		}
	}

        /**
         * Spawns a new process and waits for it to finish. New process will take
         * the newly updated em.properties and rejar it for access to Java
         * WebStart clients such as Topology or Inventory Manager etc.
         */
        protected void updateWebStartJars() throws Exception
        {
          String command =
              System.getProperty("em.home") +
              File.separator +
              "updateWebStartJars";

          Process p = Runtime.getRuntime().exec(command);
          // just wait for it to finish.
          p.waitFor();
        }

	private String getFileName(String host)
		throws ConfigStoreException
	{
		if( host == null) host = getHostName();

		if(mDirLocation != null) {
			String fileName = mDirLocation + File.separator + host + "_" + mFileName;

			if( ! canAccess(fileName)) {
				fileName = mDirLocation + File.separator + mFileName;
			}

			if( ! canAccess(fileName)) {
				throw new ConfigStoreException("Could not locate : " + fileName, ConfigStoreException.CANNOT_LOCATE);
			}
			return fileName;
		}
		throw new ConfigStoreException("Could not locate : " + host + " : " + mFileLocation + " " + mDirLocation, ConfigStoreException.CANNOT_LOCATE);

	}

	public Properties readConfigForHost(String host)
		throws ConfigStoreException
	{
		if( host == null) host = getHostName();

		Object lock = mLockMap.getLock(host);

		try {
			synchronized(lock ) {
				Properties props = (Properties) mCfgMap.get(host);

				if( props == null) {
					String fileName = getFileName(host);
					props = readConfiguration(fileName);
					mCfgMap.put(host, props);
				}
				return props;
			}
		} finally {
			mLockMap.releaseLock(host);
		}
	}

	public Properties readConfigForHost(String host, String version)
		throws ConfigStoreException
	{
		return readConfigForHost(host);
	}

	protected Properties readConfiguration(String fileName)
		throws ConfigStoreException
	{
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(fileName));
		} catch (Exception ex) {
			throw new ConfigStoreException("read(" + fileName +")");
		}
		return properties;
	}

	public String[] getVersions(String host)
		throws ConfigStoreException
	{
		String fileName = getFileName(host);

		File file = new File(fileName);
		if( file.exists()) {
			return new String[] { Long.toString(file.lastModified()) };
		}
		throw new ConfigStoreException("Could not locate : " + host, ConfigStoreException.CANNOT_LOCATE);
	}

	public String createVersion(String host, Properties cfg)
		throws ConfigStoreException
	{
		throw new ConfigStoreException("createVersion(host,cfg)", ConfigStoreException.UNSUPPORTED);
	}

	public String setCurrentVersion(String host, String olderVersion)
		throws ConfigStoreException
	{
		throw new ConfigStoreException("setCurrentVersion(host,olderVersion)", ConfigStoreException.UNSUPPORTED);
	}

}



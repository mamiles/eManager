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
package com.cisco.eManager.eManager.processSequencer.watchdog.servers;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.cisco.eManager.eManager.processSequencer.watchdog.ProcessExecutor;
import com.cisco.eManager.eManager.processSequencer.watchdog.ExecuteShutdownCmdThread;
import com.cisco.eManager.eManager.processSequencer.common.UnixStdlib;
import com.cisco.eManager.eManager.processSequencer.common.DCPLib;
import java.util.logging.Level;
import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;



/**
 * Abstracts the web server within the watchdog.
 *
 */
public class WDHttpd extends ProcessExecutor {
	private int mHeartbeatPort;
	private String mHeartbeatUrl;
	private char[] mBuf;

	/**
	 * Create a new WDHttpd server to abstract the web server
	 * within the watchdog.
	 *
	 * @param name The server name
	 */
	public WDHttpd (String name) {
		super (name);

		String propertyName = getPropertyPrefix () + ".heartbeat.port";
		mHeartbeatPort = DCPLib.getIntProperty (propertyName, -1);

		mLogger.finer(propertyName + " " + mHeartbeatPort);

		propertyName = getPropertyPrefix () + ".heartbeat.url";
		mHeartbeatUrl = DCPLib.getProperty (propertyName, null);
		if (mHeartbeatUrl == null) {
			mHeartbeatUrl = "http://localhost:8080/";
		}
		mLogger.finer(propertyName + " " + mHeartbeatUrl);

		try {
			if(mHeartbeatPort != -1) {
				URL u = new URL(mHeartbeatUrl);
				String file = u.getFile();
				if(file == null) file ="";
				URL nu = new URL(u.getProtocol(), u.getHost(), mHeartbeatPort, file);
				mHeartbeatUrl = nu.toString();
			}
		} catch (Exception ex) {
			mLogger.finer("In building heartbeat URL",  ex);
		}

		mLogger.finer ("Heartbeat url = " + mHeartbeatUrl);
		mBuf = new char[10240];
	}

	/**
	 * Perform a heartbeat on the web server by retrieving an url.
	 *
	 * @exception Exception If the heartbeat attempt failed for any reason.
	 */
	public Object heartbeat () throws Exception {

		URL url = new URL (mHeartbeatUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
		Object object = connection.getContent ();

		mLogger.finer( mLoggerHBPrefix + "getContent() returned a " + object.getClass().getName());

		java.io.BufferedInputStream contentStream = new java.io.BufferedInputStream(
														(java.io.InputStream) object, 2048);
		java.io.InputStreamReader content = new java.io.InputStreamReader(contentStream);

		StringBuffer sb = new StringBuffer(1024);
		boolean mHeartbeatDebug = mLogger.isLoggable(Level.FINEST);

		int len;
		while ((len = content.read (mBuf, 0, mBuf.length)) > 0) {
			if (mHeartbeatDebug) {
				sb.append (mBuf, 0, len);
			}
		}
		if (mHeartbeatDebug) {
			mLogger.finest(mLoggerHBPrefix + "Content Size: " +  sb.length());
		}

		try {
			contentStream.close();
			content.close();
		} catch (Exception ex) {
			mLogger.severe( mLoggerHBPrefix + "Closing stream and reader",   ex);
		}

		return new Integer(sb.length());
	}

	/**
	 * Stop this server hard.
	 */
	protected void stopServerHard () {
		if (mProcess != null) {
                        String propertyName = getPropertyPrefix () + ".shutdown.cmd";
                        String shutdownCmd = DCPLib.getProperty (propertyName, null);
                        if ( shutdownCmd != null ) {
                            mLogger.finer(propertyName + " " + shutdownCmd);
                            ExecuteShutdownCmdThread script = new ExecuteShutdownCmdThread(shutdownCmd);
                            script.start();
                            try {
                                script.join(60000);
                            }
                            catch (InterruptedException ex1) {
                                mLogger.severe( mLoggerPrefix + "Shutdown command interrupted...");
                            }
                            int rc = script.getExitValue();
                        }

			mLogger.finer( mLoggerPrefix + "Destroying server process");
			if( mPid > 0) {
				mLogger.finer( mLoggerPrefix + "Native kill");
				UnixStdlib.nativeKill(mPid);
			} else {
				mLogger.finer( mLoggerPrefix + "Java destroy");
				mProcess.destroy();
			}
			mLogger.finer( mLoggerPrefix + "Destroyed server process");
			mProcess = null;
			mPid = 0;
		}
	}

	protected String getProcessNameForLog() {
		return mName + "_out";
	}

	protected boolean shouldIndicateNativeLogging() {
		return false;
	}
}


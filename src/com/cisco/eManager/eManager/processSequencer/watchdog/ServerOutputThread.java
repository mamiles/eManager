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

import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;
import java.util.logging.*;

/**
 * Reads output from a server's stdout or stderr and directs it
 * to a Log.
 *
 */
public class ServerOutputThread
	extends Thread {

	private int mMetadata;
	private CiscoLogger mLocalLogger;
	private Handler mExternalLogger;
	private LogRecord mLogRecord;
	private String mName;

	private InputStream mInputStream;

	public ServerOutputThread (String name, CiscoLogger localLogger, Handler externalLogger) {
		super (name);
		mName = name;
		mLocalLogger = localLogger;
		mExternalLogger = externalLogger;
		mExternalLogger.setLevel(Level.FINEST);
		mLogRecord = new LogRecord(Level.FINEST, "");
		mLogRecord.setSourceClassName(ServerOutputThread.class.getName());
		setDaemon (true);
	}

	public synchronized void setStream (InputStream s) {
		mInputStream = s;
		notifyAll ();
	}

	public void run () {
		while (true) {
			InputStream istr;

			// wait until a stream is available
			synchronized (this) {
				while (mInputStream == null) {
					try {
						wait (5000);
					} catch (InterruptedException ie) {
						// ignore
					}
				}
				istr = (InputStream) mInputStream;
			}
			//System.out.println(mName +
				//" at the beginning of main try block" + (mInputStream == istr));

			BufferedReader br = null;

			// drain the stream
			try {
				br = new BufferedReader(new InputStreamReader(istr), 8192);

				String str;
				while ((str = br.readLine()) != null) {
					if( mExternalLogger != null) {
						mLogRecord.setMessage(str);
						mExternalLogger.publish(mLogRecord);
					} else {
						mLocalLogger.finest(str);
					}
				}

				/*
				try {
					Thread.sleep(3000);
				} catch (Exception ex) {}
				System.out.println(mName +
					" at the beginning of sync block" + (mInputStream == istr));
				*/
				//reached end of file.. the stream is no longer open
				synchronized(this) {
					if (mInputStream == istr ) {
						mInputStream = null;
					}
					istr = null;
					continue;
				}
			}
			catch (IOException ioe) {
				mLocalLogger.warning("The process might have exited.", ioe);
				//System.out.println(mName + " caught ioex " + ioe.getMessage());
				synchronized(this) {
					if (mInputStream == istr ) {
						mInputStream = null;
					}
				}
				try {
					istr.close ();
					if( br != null) br.close();
				} catch (IOException istre) {
					mLocalLogger.warning("Failed to close the stream properly.", istre);
				}
				istr = null;
			}

			//System.out.println(mName +
				//" at the end of while loop " + istr + " -- " + mInputStream);
		}
	}
}

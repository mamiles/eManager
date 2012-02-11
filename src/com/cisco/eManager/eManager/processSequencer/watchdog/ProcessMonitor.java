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

import com.cisco.eManager.eManager.processSequencer.common.DCPLib;
import java.io.*;
import java.util.*;

/**
 * Abstracts a ProcessMonitor (poller) within the watchdog.
 * The concrete implementations should implement the
 * heartbeat() method.
 * @see Server#heartbeat
 * @see Server
 */
public abstract class ProcessMonitor extends Server
{
	protected ProcessMonitorThread mMonitorThread;
	protected ProcessHandler mProcessHandler;
	protected String mRestartCmd;

	/**
	 * Constructor.
	 */
	public ProcessMonitor (String name) {
		super(name);
		mServerStatus.setMonitor(true);
		mRestartCmd = DCPLib.getProperty(mPropertyPrefix + ".restartCmd", null);

		if ( mRestartCmd != null) {
			mLogger.config("This process monitor can be restarted using the command : " + mRestartCmd);
		} else {
			mLogger.config("This process monitor cannot restart the process if it dies");
		}

		mMonitorThread = new ProcessMonitorThread(name);
		mMonitorThread.start();
	}

	protected String getCommand() {
		return mRestartCmd;
	}

	/**
	 * No implementation needed for the monitor
	 */
	protected void prepare()
		throws Exception
	{
	}

	/**
	 * No implementation needed for the monitor
	 */
	protected void handleUnexpectedState(Boolean stateChanged) { }

	/**
	 * Marks the exectime, updates generation.
 	 * Starts heartbeat thread.
	 */
	protected synchronized void startExecution() {
		if ( mRestartCmd != null ) {
			if ( mGeneration >= 1) {
				if ( mProcessHandler != null ) {
					mProcessHandler.die();
				}
				mProcessHandler = new ProcessHandler(mName, mRestartCmd);
				mProcessHandler.start();
			}
		}

		mExecTime = System.currentTimeMillis();
		++mGeneration;
		updateServerStatus();
		mHeartbeatThread.enable();
		mMonitorThread.setEnabled(true);
	}

	/**
	 * Simply marks the monitor as stopped.
	 */
	protected synchronized void stopServerHard() {
		if ( mRestartCmd != null ) {
			if ( mGeneration > 1) {
				if( mProcessHandler != null) {
					mProcessHandler.die();
					mProcessHandler = null;
				}
			}
		}
		mMonitorThread.setEnabled(false);
		changeState(STATE_STOPPED);
	}


	/**
	 * This thread is responsible for detecting the death of the
	 * external server processes and communicating that death back to
	 * the ProcessExecutor class.
	 */
	class ProcessMonitorThread extends Thread {

		int mNoticedGeneration = 0;

		boolean mEnabled = false;
		boolean mShouldExit = false;
		boolean mProcessAlive = true;

		/**
		 * Create a new ProcessMonitorThread.
		 */
		public ProcessMonitorThread(String name) {
			super( name + "wait");
			setDaemon(true);
		}

		public synchronized void setEnabled( boolean b) {
			setEnabled(b, true);
		}

		public synchronized void setEnabled( boolean b, boolean shdNotify) {
			if ( mEnabled != b) {
				mEnabled = b;

				if( shdNotify ) {
					notifyAll();
				}
			}
		}

		public synchronized void setComplete( boolean b) {
			if ( mShouldExit != b) {
				mEnabled = b;
				mShouldExit = b ;
				notifyAll();
			}
		}

		public void run() {
			while(true) {
				synchronized(this) {
					while(mNoticedGeneration == mGeneration && !mEnabled && !mShouldExit ) {
						try {
							mLogger.finer( "ProcessMonitorThread is waiting");
							wait(3000);
						}
						catch (InterruptedException ie) {
							// ignore
						}

						if( !mShouldExit && !mEnabled) {
							try {
								heartbeat();
								setEnabled(true, false); //in effect break out of loop
							} catch (Exception ex) {
								//expected.. ignore
							}
						}
					}
					if ( mShouldExit ) return;
					mNoticedGeneration = mGeneration;
				}

				int failed = 0;

			   	while(true) {
					Exception toLog = null;
					try {
						heartbeat();
						failed = 0;
						if( ! mProcessAlive ) {
							startServer(true);
							mProcessAlive = true;
						}
					} catch (Exception ex) {
						failed++;
						toLog = ex;
					}

					if ( failed >= 5 && mProcessAlive ) {
						if( toLog != null) {
							mLogger.severe(ProcessMonitor.this.getName() + " is now stopping because watchdog missed " + failed + " consecutive heartbeats", toLog);
						}

					   	changeState(STATE_STOPPED);
						mProcessAlive = false;
					}

					int sleepTime = 5000;
					if ( failed > 0 ) {
						sleepTime = 3000;
					}

					synchronized (this ) {
						if( mEnabled )  {
							try {
								wait(sleepTime);
							} catch (InterruptedException iex) {}
						}
						if( !mEnabled ) break;
					}
				}
			}
		}
	}


	class ProcessHandler extends Thread {

		private String mCmd;
		private Process mProcess;
		private boolean mDone;
		private PReader mOutReader;
		private PReader mErrReader;

		ProcessHandler(String name, String cmd) {
			super("ProcessHandler." + name);
			this.mCmd = cmd;
		}

		void die() {
			try {
				if( mProcess != null) {
					mProcess.destroy();
				}
				mProcess = null;
			} catch (Exception ex){
				ex.printStackTrace();
			}

			synchronized(this) {
				mOutReader.die();
				mErrReader.die();
				mDone = true;
				notify();
			}
		}

		public void run() {

			try {
				mProcess = Runtime.getRuntime().exec(mCmd);

				mOutReader = new PReader (mProcess.getInputStream());
				mErrReader = new PReader (mProcess.getErrorStream());

				synchronized ( this ) {
					while ( ! mDone ) {
						wait(3000);
					}
				}
				mProcess = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		private class PReader extends Thread {

			boolean mFinish = false;
			BufferedReader mBR = null;

			PReader(InputStream is)
				throws Exception
			{
				mBR = new BufferedReader(new InputStreamReader(is));
			}

			public void run() {

				try {
					while ( mBR.readLine() != null) {
						if( mFinish ) break;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			void die() {
				mFinish = true;
				this.interrupt();
			}

		}
	}
}

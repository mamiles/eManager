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

import java.util.logging.Level;
import com.cisco.eManager.eManager.processSequencer.common.PropertyUtils;
import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;
import java.util.Date;
import com.cisco.eManager.eManager.processSequencer.common.DCPCallback;
import com.cisco.eManager.eManager.processSequencer.common.DCPLib;

/**
 * A thread to call a server's heartbeat method.
 */
public class ServerHeartbeatThread extends Thread {

	static final int MISSED_HEARTBEAT = 100;
	static final int FAILED_TO_START  = 200;
	static final int SUCCESSFUL_HEARTBEAT = 300;

	private HeartbeatConfig mHeartbeatConfig;

	/**
	 * Whether or not the heartbeat thread is enabled.	The thread is
	 * disabled when the server is stopped/disabled.
	 */
	private boolean mEnabled = false;

	/**
	 * Whether or not atleast one heartbeat thread was successful.
	 */
	private boolean mFirstHeartbeatSuccessful = false;


	/**
	 * The last server generation the heartbeat was enabled for.
	 */
	private int mEnabledGeneration = 0;

	/**
	 * Set to the time that this heartbeat thread was enabled.	This is
	 * used to detect a server that never has a successful heartbeat.
	 */
	private long mEnableTime = 0;

	/**
	 * The thread that is actually making the call to Server.heartbeat().
	 */
	private CallerThread mCallerThread = null;

	/**
	 * True if CallerThread is currently in the middle of a call to
	 * Server.heartbeat().
	 */
	private boolean mCallerActive = false;

	/**
	 * The time that the CallerThread called Server.heartbeat().
	 */
	private long mCallerStartTime = 0;

	/**
	 * True if CallerThread's call to Server.heartbeat() did not
	 * complete before the timeout expired.
	 */
	private boolean mCallerTimedOut = false;


	/**
	 * The time (ms) a server can take to start.	If a successful
	 * heartbeat hasn't happened before this is exceeded, the server is
	 * restarted.
	 */
	private static final long DEFAULT_START_TIMEOUT =	2*60*1000; // 2 minutes

	/**
	 * The period (ms) between heartbeats.
	 */
	private static final long DEFAULT_PERIOD =		 2*60*1000; // 2 minutes

	/**
	 * The length of time (ms) to wait after a server is started before
	 * attempting a heartbeat.
	 */
	private static final long DEFAULT_START_DELAY = 10*1000; // 10 seconds

	/**
	 * The timeout (ms) for heartbeats.
	 */
	private static final long DEFAULT_TIMEOUT =		15*1000; // 15 seconds

	/**
	 * The maximum random noise (ms) to add to heartbeat period.	Random
	 * noise is added so that all server heartbeats don't hit at once.
	 */
	private static final long MAXIMUM_RANDOM_NOISE = 1000;
	private static java.util.Random msRandom = new java.util.Random ();

	/**
	 * The server this heartbeat thread is associated with.
	 * @supplierRole mServer */
	private Server mServer;
	private String mServerName;

	private CiscoLogger mLogger;

	private Object mainLock = new Object();

	/**
	 * Construct a new ServerHeartbeatThread.
	 *
	 * @param server The server this heartbeat is associated with
	 */
	public ServerHeartbeatThread(Server server, String loggerName) {
		super(server.getName() + ".heartbeat");
		setDaemon(true);

		mServer = server;
		mServerName = server.getName();

		mLogger = CiscoLogger.getCiscoLogger(loggerName, true);

		mHeartbeatConfig = new HeartbeatConfig(mServer.getPropertyPrefix());
	}

	public void run() {
		ConfigStruct cfg = mHeartbeatConfig.getConfig();

		long nextHeartbeat = System.currentTimeMillis() + cfg.getPeriod();

		synchronized(msRandom) {
			nextHeartbeat += msRandom.nextLong() % MAXIMUM_RANDOM_NOISE;
		}

		// loop endlessly
		do {
			if( cfg.isNew() ) {
				mLogger.finest("Heartbeat config is : " + cfg);
				cfg.setNew(false);
			}

			// Check if heartbeat is enabled and if not, wait until it is
			synchronized(mainLock) {
				if(!mEnabled || mEnabledGeneration != mServer.getGeneration()) {
					while(!mEnabled) {

						while(!mEnabled) {
							try {
								mainLock.wait();
							}
							catch(InterruptedException ie) {
								// ignore
							}
						}

						mEnableTime = System.currentTimeMillis();

						mLogger.finer("Heartbeat enabled @" + mEnableTime + " ; Delaying " +
							cfg.getHbStartDelay() + "ms before attempting first heartbeat");

						long delay = mEnableTime + cfg.getHbStartDelay() - System.currentTimeMillis();

						//wait for "initial delay" amount of time

						while(mEnabled && delay > 0) {
							try {
								mainLock.wait(delay);
							}
							catch(InterruptedException ie) {
								// ignore
							}
							delay = mEnableTime + cfg.getHbStartDelay() - System.currentTimeMillis();
						}
					}

					// The server has just been enabled, so perform more frequent
					// hearbeats until one is successful and then let the server
					// class know it's up.

					nextHeartbeat = computeNextHeartbeatTime(System.currentTimeMillis(), cfg);
					mEnabledGeneration = mServer.getGeneration();
				}
			} //give up monitor


			if(mEnableTime > 0
				&& (System.currentTimeMillis() - mEnableTime) > cfg.getStartTimeout())
			{

				mLogger.warning("Failed to have heartbeat within "
					 +( cfg.getStartTimeout() / 1000)
					 + " seconds");

				notifyServer(FAILED_TO_START, null);
				mEnableTime = 0;
			}

			synchronized(mainLock) {
				if(mCallerThread == null || mCallerActive) {
					// If no caller thread has been create yet or if
					// a previous call is still active(hung), create
					// a new thread and discard the old one.
					mCallerThread = new CallerThread( (new java.util.Date()).toString() + "-" + getName() + ".caller", this, mServer);
					mCallerThread.start();
				}
				mCallerActive = true;
				mCallerTimedOut = false;
				mCallerStartTime = System.currentTimeMillis();
				mCallerThread.trigger();

				long period = getActualPeriod(cfg);

				try {
					mainLock.wait(cfg.getTimeout());
				}
				catch(InterruptedException ie) {
					// ignore
				}

				if(mCallerActive) {
					// The heartbeat hasn't returned, so consider it a timeout
					mCallerTimedOut = true;
					notifyServer(MISSED_HEARTBEAT, null);
				}

				// Wait until next heartbeat is desired
				if(mEnabled) {
					long delay = nextHeartbeat - System.currentTimeMillis();
					if(delay < 0) {
						mLogger.warning( new StringBuffer(80)
							 .append("heartbeat lasted ")
							 .append(-delay)
							 .append("ms longer than period(")
							 .append(period)
							 .append("ms)")
							 .toString());

						nextHeartbeat = computeNextHeartbeatTime(System.currentTimeMillis(), cfg);
					}

					mLogger.finer("waiting until next heartbeat");
					delay = nextHeartbeat - System.currentTimeMillis();

					if(delay > 0) {
						try {
							mainLock.wait(delay);
						}
						catch(InterruptedException ie) {
							// ignore
						}
					}
					nextHeartbeat = computeNextHeartbeatTime(nextHeartbeat, cfg);
				}
			}

			cfg = mHeartbeatConfig.getConfig();
		} while (true );
	}

	private void notifyServer(int code, Object result) {
		mServer.forwardEventPoolRequest(new ServerNotifier(code, result));
	}

	private long getActualPeriod(ConfigStruct cfg) {
		synchronized (mainLock) {
			if( mFirstHeartbeatSuccessful ) {
				return cfg.getPeriod();
			} else {
				return cfg.getTimeout() + 2000;
			}
		}
	}

	private long computeNextHeartbeatTime(long currentHeartbeat, ConfigStruct cfg)
	{
		synchronized (mainLock) {
			long retValue = currentHeartbeat + getActualPeriod(cfg);

			synchronized(msRandom) {
				retValue += msRandom.nextLong() % MAXIMUM_RANDOM_NOISE;
			}
			return retValue;
		}
	}

	/**
	 * Enables the heartbeat thread, causing it to periodically call the
	 * server's heartbeat method.
	 */
	public void enable() {
		synchronized ( mainLock ) {
			if( ! mEnabled ) {
				mFirstHeartbeatSuccessful = false;
				mEnabled = true;
				mainLock.notifyAll();
			}
		}
	}


	/**
	 * Disbles the heartbeat thread, causing it to no longer call the
	 * server's heartbeat method.
	 */
	public void disable() {
		synchronized ( mainLock ) {
			if ( mEnabled ) {
				mEnabled = false;
				mFirstHeartbeatSuccessful = false;
				mainLock.notifyAll();
			}
		}
	}


	/**
	 * Called by CallerThread to indicate that the mServer.heartbeat()
	 * completed.
	 *
	 * @param who The caller thread. This is compared to mCallerThread
	 *	 to determine if this is the most recent caller thread, or some
	 *	 previous thread that took too long to return and was discarded.
	 * @return True if the caller thread should continue running, false
	 *	 if it has been discarded because it took too long.
	 */
	protected boolean heartbeatCompleted(CallerThread who, Object result) {
		synchronized (mainLock ) {
			if(who != mCallerThread) {
				// another thread has been created so
				// return false to tell the caller thread to die
				mLogger.finer("Forgotten caller finally completed");
				return false;
			} else {
				// The server is up and running
				mEnableTime = 0;

				long stopTime = System.currentTimeMillis();
				mCallerActive = false;
				if(mCallerTimedOut) {
					// Missed the timeout, but completed before another heartbeat
					// was scheduled.
					if( mLogger.isLoggable(Level.FINEST) ) {
						mLogger.finest("Heartbeat exceeded timeout but eventually finished after "
								+(stopTime - mCallerStartTime)
								+ "ms");
					}

				} else {
					// Timeout hasn't expired
					if( mLogger.isLoggable(Level.FINEST) ) {
						String msg = " successful heartbeat in "
								 +(stopTime - mCallerStartTime)
								 + "ms";
						mLogger.finest(msg);
					}
					if ( !mFirstHeartbeatSuccessful) {
						mFirstHeartbeatSuccessful = true;
					}
					notifyServer(SUCCESSFUL_HEARTBEAT, result);
				}
			}
			return true;
		}
	}

	/**
	 * Called by CallerThread to indicate that a heartbeat failed.
	 *
	 * @param who The caller thread.
	 * @param throwable The reason it failed.
	 * @return True if the caller thread should continue running, false
	 *	 if it has been discarded because it took too long.
	 */
	protected boolean heartbeatFailed(CallerThread who,
							Object cause)
	{
		synchronized (mainLock ) {
			if(who != mCallerThread) {
				// Too little, too late; another thread has been created
				mLogger.finer("Forgotten caller finally failed");
				return false;
			} else {

				mCallerActive = false;
				if(mEnabled) {
					if( cause != null && cause instanceof Throwable) {
						mLogger.warning("Heartbeat failed due to exception: ", (Throwable) cause);
					} else {
						mLogger.warning("Heartbeat failed");
					}
					notifyServer(MISSED_HEARTBEAT, null);
					mainLock.notifyAll();
				}
			}

			return true;
		}
	}

	/**
	 * This performs the actual call to mServer.heartbeat().	This was
	 * pushed into a separate thread because Thread.interrupt() does not
	 * actually interrupt the tread and could not be used to interrupt
	 * the call to mServer.heartbeat().	Instead, this separate thread
	 * is used, so it can be conveniently discarded if it does not
	 * return in a timely fashion.
	 */
	static class CallerThread
		extends Thread {

		ServerHeartbeatThread mOwner;
		Server mServer;
		boolean mTriggered = false;
		Object triggerLock = new Object();

		/**
		 * Create a new CallerThread.
		 *
		 * @param name The name of this caller thread.
		 * @param owner The owner of this caller thread.
		 * @param server The server this caller thread is associated with.
		 */
		public CallerThread(String name,
			 ServerHeartbeatThread owner,
			 Server server)
		{
			super(name);
			mOwner = owner;
			mServer = server;
		}


		/**
		 * The entry point to this thread.
		 */
		public void run() {

			boolean keepRunning = true;
			while(keepRunning) {

				// wait until triggered
				synchronized(triggerLock) {
					while(!mTriggered) {
						try {
							triggerLock.wait();
						}
						catch(Exception e) {
						}
					}
				}

				Object retVal = null;
				try {
					retVal = mServer.heartbeat();
					keepRunning = signalComplete(true, retVal);
				} catch (Throwable t) {
					keepRunning = signalComplete(false, t);
				}
			}
		}

		private boolean signalComplete(boolean success, Object retVal) {
			synchronized ( mOwner.mainLock ) {

				boolean b = false;
				if( success ) {
					b = mOwner.heartbeatCompleted(this, retVal);
				} else {
					b = mOwner.heartbeatFailed(this, retVal);
				}

				mTriggered = false;
				return b;
			}
		}

		/**
		 * Trigger another call to mServer.heartbeat().
		 */
		void trigger() {
			synchronized (triggerLock) {
				mTriggered = true;
				triggerLock.notifyAll();
			}
		}
	}

	class ServerNotifier implements Runnable {
		private int mOp;
		private Object mResult;

		ServerNotifier(int op, Object result) {
			mOp = op;
			mResult = result;
		}

		public void run() {
			switch (mOp) {
				case MISSED_HEARTBEAT :
					mServer.missedHeartbeat();
					break;
				case FAILED_TO_START :
					mServer.failedToStart();
					break;
				case SUCCESSFUL_HEARTBEAT :
					mServer.successfulHeartbeat(mResult);
					break;
			}
		}
	}

	class ConfigStruct {
		private long mPeriod;
		private long mTimeout;
		private long mHbStartDelay;
		private long mStartTimeout;

		private boolean mIsNew;

		ConfigStruct ( long p, long t, long hsd, long st) {
			mPeriod = p;
			mTimeout = t;
			mHbStartDelay = hsd;
			mStartTimeout = st;

			mIsNew = true;
		}

		long getPeriod() {
			return mPeriod;
		}

		long getTimeout() {
			return mTimeout;
		}

		long getHbStartDelay() {
			return mHbStartDelay;
		}

		long getStartTimeout() {
			return mStartTimeout;
		}

		boolean isNew() {
			return mIsNew;
		}

		void setNew(boolean b) {
			mIsNew = b;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("Heartbeat period = " ).append( mPeriod )
				.append( "; timeout = " ).append( mTimeout )
				.append( "; start timeout = " ).append( mStartTimeout )
				.append("; heartbeat start delay = " ).append(mHbStartDelay);

			return sb.toString();
		}
	}

	class HeartbeatConfig implements DCPCallback {

		private long mPeriod;
		private long mTimeout;
		private long mHbStartDelay;
		private long mStartTimeout;

		private boolean mDirty;

		private ConfigStruct mStruct;

		private String mServerPrefix;

		HeartbeatConfig(String serverPrefix) {
			mServerPrefix = serverPrefix;
			updateConfig(true);

			DCPLib.registerComponent( WDConstants.WD_PREFIX + ".heartbeat", this);
			DCPLib.registerComponent( mServerPrefix + ".heartbeat", this);

			DCPLib.registerComponent( WDConstants.WD_PREFIX + ".server", this);
			DCPLib.registerComponent( mServerPrefix, this);
		}

		private synchronized void updateConfig(boolean init) {

			String shbPrefix = mServerPrefix + ".heartbeat";
			String whbPrefix = WDConstants.WD_PREFIX + ".heartbeat";

			String[] propertyNames = new String[2];
			// determine period
			propertyNames[0] = shbPrefix + ".period";
			propertyNames[1] = whbPrefix + ".period";

			mPeriod = PropertyUtils.getLongProperty( propertyNames, DEFAULT_PERIOD, mLogger);

			// determine timeout
			propertyNames[0] = shbPrefix + ".timeout";
			propertyNames[1] = whbPrefix + ".timeout";
			mTimeout = PropertyUtils.getLongProperty( propertyNames, DEFAULT_TIMEOUT, mLogger);

			// Make sure period and timeout are okay, relative to each other
			if(mTimeout >= mPeriod) {
				mLogger.warning("Heartbeat timeout must be smaller than the period.");
				mLogger.warning("Using default values(period=" + DEFAULT_PERIOD
				 	+ ", timeout=" + DEFAULT_TIMEOUT + ") instead.");
				mTimeout = DEFAULT_TIMEOUT;
				mPeriod = DEFAULT_PERIOD;
			}

			long randomNoise = 0;

			synchronized(msRandom) {
				randomNoise = msRandom.nextLong() % MAXIMUM_RANDOM_NOISE;
			}

			mPeriod = mPeriod + randomNoise;

			// determine start delay
			propertyNames[0] = shbPrefix + ".startDelay";
			propertyNames[1] = whbPrefix + ".startDelay";

			mHbStartDelay = PropertyUtils.getLongProperty( propertyNames, DEFAULT_START_DELAY, mLogger);
			// determine start timeout
			propertyNames[0] = mServerPrefix + ".startTimeout";
			propertyNames[1] = WDConstants.WD_PREFIX +".server.startTimeout";

			mStartTimeout = PropertyUtils.getLongProperty( propertyNames, DEFAULT_START_TIMEOUT, mLogger);
			if( mHbStartDelay >= mStartTimeout) {
				mLogger.warning("Heartbeat start delay must be smaller than the startTimeout.");
				mLogger.warning("Using default values(period=" + DEFAULT_START_DELAY
				 + ", timeout=" + DEFAULT_START_TIMEOUT + ") instead.");
				mStartTimeout = DEFAULT_START_TIMEOUT;
				mHbStartDelay = DEFAULT_START_DELAY;
			}

			mDirty = true;
		}

		public synchronized boolean handleChange(String property, String value) {
			if( property == null) return false;

			if ( property.startsWith(mServerPrefix + ".heartbeat" ) ||
				property.startsWith(WDConstants.WD_PREFIX + ".heartbeat") ||
				property.equals(mServerPrefix + ".startTimeout" ) ||
				property.equals(WDConstants.WD_PREFIX + ".server.startTimeout") )
			{
				updateConfig(false);
				mDirty = true;
			}
			return true;
		}

		synchronized  ConfigStruct getConfig() {
			if( mDirty ) {

				mStruct = new ConfigStruct( mPeriod, mTimeout,
						mHbStartDelay, mStartTimeout);

				mLogger.finest("Server heartbeat config is : "
					+ mStruct.toString());

				mDirty = false;
			}
			return mStruct;
		}
	}
}


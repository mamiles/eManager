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

import com.tibco.tibrv.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import com.cisco.eManager.eManager.processSequencer.common.DCPLib;
import com.cisco.eManager.eManager.processSequencer.common.NoSuchProperty;
import com.cisco.eManager.eManager.processSequencer.common.logging.LogUtil;
import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;
import java.util.logging.*;

/**
 * Abstracts a server within the watchdog.	This class handles
 * creation of the server subprocess, reading its stdout/stderr,
 * waiting for the process to die, restarting it, etc.	Server
 * specific classes are derived from this and must implement a
 * heartbeat method to verify that the server is functioning
 * correctly.
 */
public abstract class Server
	extends Thread
	implements ServerStateListener
{
	/**
	 * The name of this server.
	 */
	protected String mName;

	/**
	 * The UNIX process id for this server.
	 **/
	protected long mPid;

	/**
	 * The state of this server.
	 */
	protected int mState = STATE_DISABLED;

	protected ServerStatus mServerStatus;


	/**
	 * True if this server has been disabled.	It can take a short
	 * amount of time for mState to reach STATE_DISABLED.
	 */
	protected boolean mDisabled = true;

	/**
	 * The current generation of the server.	This is incremented each
	 * time the server is execed.
	 */
	protected int mGeneration = 0;

	protected String mPropertyPrefix = null;


	/**
	 * When the server process was last execed.
	 */
	protected long mExecTime = 0;

	/**
	 * The number of successful heartbeats this generation.
	 */
	protected int mSuccessfulHeartbeats = 0;

	/**
	 * The number of missed heartbeats since this server entered
	 * STATE_STARTED, triggered by the first successful heartbeat.
	 */
	protected int mMissedHeartbeats = 0;

	/**
	 * The number of consecutive missed heartbeats.	This is used to
	 * determine whether the server needs to be restarted.
	 */
	protected int mConsecutiveMissedHeartbeats = 0;

	/**
	 * This tracks when the server process stopped (if the stop was
	 * unexpected).
	 */
	protected long mStopTime = 0;

	/**
	 * This tracks when a gentle stop was attempted.
	 */
	protected long mGentleStopTime = 0;

        /**
         * This keeps track of errors descriptions encountered starting a server
         */
        protected String mErrorDescription = null;


	protected Comparator serverComparator = new ServerComparator();

	/**
	 * The set of servers that this server depends on (i.e. must be
	 * started before this server).
	 */
	protected Set mPrincipals = Collections.synchronizedSet(new TreeSet (serverComparator));

	/**
	 * The set of servers that depend on me (i.e. these servers must be
	 * must be stopped before stopping this server. these servers can be
     * started only after this server is started)
	 */
	protected Set mServersDependentOnMe = Collections.synchronizedSet(new TreeSet(serverComparator));

	/**
	 * This is incremented each time an event is sent out so that
	 * listeners can tell if data is old.
	 */
	protected long mRevisionOfEvent = 0;

	/**
     * The heartbeat result object.
	 */
	protected Object mHBResult = null;

	/**
	 * This flag indicates that the thread has started
     * working.
	 */

	private boolean mStartedWorking = false;

	/**
     * Maximum number of consecutive heartbeats that can
     * be missed before the server should be disabled
	 */
	public static final int MAX_ALLOWED_MISSED_HEARTBEATS = 3;

	/**
	 * If a server dies within this amount of time (milliseconds) after
	 * it's started, the server is diagnosed as having died quickly.	If
	 * the server dies quickly three times in a row, it is disabled.
	 */
	protected long mQuickDieThreshold = DEFAULT_QUICK_DIE_THRESHOLD;
	protected static final long MINIMUM_QUICK_DIE_THRESHOLD =	30*1000; // 30s
	protected static final long DEFAULT_QUICK_DIE_THRESHOLD =	3*60*1000; // 3min
	protected static final long MAXIMUM_QUICK_DIE_THRESHOLD = 10*60*1000; // 10min

	/**
	 * If a server dies quickly this many times in a row, it is disabled.
	 */
	protected int mQuickDieLimit = DEFAULT_QUICK_DIE_LIMIT;
	protected static final int MINIMUM_QUICK_DIE_LIMIT = 1;
	protected static final int DEFAULT_QUICK_DIE_LIMIT = 3;
	protected static final int MAXIMUM_QUICK_DIE_LIMIT = 10;

	/**
	 * This tracks how many times in a row the server has died quickly.
	 */
	protected int mQuickDieCount = 0;

	/**
	 * If a server class has implemented a gentleStop() method, this is
	 * the length of time it has to stop the server.	If a gentle stop
	 * doesn't happen within this amount of time, the server is stopped
	 * hard.
	 */
	protected long mGentleStopTimeout = DEFAULT_GENTLE_STOP_TIMEOUT;
	protected static final long MINIMUM_GENTLE_STOP_TIMEOUT =	1000; // 1 sed
	protected static final long DEFAULT_GENTLE_STOP_TIMEOUT = 10*1000; // 10 sec
	protected static final long MAXIMUM_GENTLE_STOP_TIMEOUT = 30*1000; // 30 sec

	/**
	 * When a server is being restarted, wait this long before execing
	 * the process.	This prevents some problems with lock manager ports
	 * not being freed yet.
	 */
	protected long mRestartDelay = DEFAULT_RESTART_DELAY;
	protected static final long MINIMUM_RESTART_DELAY =	 1*1000; // 1 second
	protected static final long DEFAULT_RESTART_DELAY =	 5*1000; // 5 seconds
	protected static final long MAXIMUM_RESTART_DELAY = 10*60*1000; // 10 minutes

	/**
	 * This server has been asked to start, but is waiting for servers
	 * it depends on to start.	Once all dependent servers have started,
	 * this server will transition to STATE_STARTING.
	 */
	public static final int STATE_START_DEPENDENCIES = 1;

	/**
	 * This server is currently starting.	If this server has server
	 * dependencies, it may be in this state for a while, waiting for
	 * those servers to to start.	Once all dependent servers have
	 * started, the exec will be issued. */
	public static final int STATE_STARTING = 2;

	/**
	 * This server is currently started and running.	*/
	public static final int STATE_STARTED = 3;

	/**
	 * This server is supposed to be shut down, but is waiting for
	 * servers it depends on to be shut down first.
	 */
	public static final int STATE_STOP_DEPENDENCIES = 4;

	/**
	 * This server is in the process of stopping.
	 */
	public static final int STATE_STOPPING_GENTLY = 5;

	/**
	 * This server is in the process of stopping.
	 */
	public static final int STATE_STOPPING_HARD = 6;

	/**
	 * This server is stopped.	The watchdog will either start it again
	 * or disable it if it has been frequently dying.
	 */
	public static final int STATE_STOPPED = 7;

	/**
	 * This server is disabled and must be manually restarted.
	 */
	public static final int STATE_DISABLED = 8;

	/**
	 * This server is disabled because one or more servers it depends on
	 * is disabled on is disabled.	If all servers it depends on are
	 * enabled, this server will automatically start.
	 */
	public static final int STATE_DISABLED_DEPENDENT = 9;

	/**
	 * This server is delaying before restarting.
	 */
	public static final int STATE_RESTART_DELAY = 10;

	/**
	 * This array maps STATE_XXXXXX variables onto printable strings.
	 */
	public static final String[] mStateNames = {
		"unknown", "start_depends", "starting", "started", "stop_depends",
		"stopping_gently", "stopping_hard", "stopped", "disabled",
		"disabled_dependent", "restart_delay"
	};

	/**
     * The thread that monitors the health of the server
     * by sending heartbeat events.
	 */
	protected ServerHeartbeatThread mHeartbeatThread;

	/**
	 * Listeners interested in the statechanges of this server
	 */
	protected Set mStateListeners = Collections.synchronizedSet(new HashSet ());

	/**
	 * The owner of all the servers (WatchdogImpl)
	 */
	protected ServerManager mManager;

	/**
	 * Logger for this server.
	 */
	protected CiscoLogger mLogger;

	/**
	 * Prefix string for logging messages of this server
	 */
	protected String mLoggerPrefix;

	/**
	 * Prefix string for logging heartbeat messages of this server
	 */
	protected String mLoggerHBPrefix;

	/**
	 * Construct a new Server.
	 * @param name name of this Server
	 */
	public Server (String name) {
		super(name);

		mPid = 0;

		mName = name;
		mPropertyPrefix = WDConstants.WD_PREFIX + ".server." + mName;
		mLogger = CiscoLogger.getCiscoLogger(mPropertyPrefix);

		mLoggerPrefix = "server." + mName + " - ";
		mLoggerHBPrefix = "server." + mName + ".heartbeat" + " - ";

		setupQuickDieLimit();

		mServerStatus = new ServerStatus (mName,
							mPid,
							stateName(),
							mGeneration,
							mExecTime,
							mSuccessfulHeartbeats,
							mMissedHeartbeats, null,
							false, false);
		startHeartbeatThread ();
	}

	protected void setupQuickDieLimit() {
		String[] qdProps = new String[] { mPropertyPrefix + ".maxQuickDieCount",
									WDConstants.WD_PREFIX + ".server..maxQuickDieCount" };

		String qdLimit = getProperty( qdProps );
		if( qdLimit != null && qdLimit.trim().length() > 0) {
			try {
				mQuickDieLimit = Integer.parseInt(qdLimit);
			} catch (Exception ex) {
				mQuickDieLimit = DEFAULT_QUICK_DIE_LIMIT;
			}
		} else {
			mQuickDieLimit = DEFAULT_QUICK_DIE_LIMIT;
		}

		mLogger.finest("Quick die limit is : " + mQuickDieLimit);
	}

    /**
     * Sets the ServerManager. ServerManager can provide handles
     * to other servers.
     * @see ServerManager
     */
	void setServerManager(ServerManager sm) {
		this.mManager = sm;
	}

    /**
     * Starts the thread that periodically calls the heartbeat
     * method.
     * @see Server#heartbeat
     */
    protected void startHeartbeatThread () {
        mHeartbeatThread = new ServerHeartbeatThread (this, mPropertyPrefix + ".heartbeat");
        mHeartbeatThread.start ();
    }

	/**
     * Implementors of this method should build the
     * necessary state to be able to start a server
	 * process (not applicable for a monitor)
	 */
	protected abstract void prepare()
		throws Exception;

	/**
     * Sets the server to disabled
	 */
	protected synchronized void setDisabled ( boolean b) {
		mDisabled = b;
	}

	/**
     * Returns true if the server is in disabled state
	 */
	protected synchronized boolean isDisabled() {
		return mDisabled;
	}

	/**
     * Subclasses should implement this method to handle
     * a state transition to an unknown/unexpected state.
	 */
	protected abstract void handleUnexpectedState(Boolean stateChanged);


	/**
	 * Entry point for the server's thread.
	 * Calls prepare to set up the command to start the
     * Server. If prepare is successful, runs the
	 * state machine infinite loop in runForEver().
	 */
	public void run () {
		try {
			prepare();
		} catch (Exception ex) {
			mLogger.severe("Improper configuration for server", ex);
			return;
		}
		runForever();
	}

	private final synchronized void setStartedWorking() {
		if(!mStartedWorking) {
			mStartedWorking = true;
		}
	}

	private synchronized boolean hasStartedWorking() {
		return mStartedWorking;
	}

	/**
	 * In an infinite loop does the following:
	 * <xmp>
	 * while (true) {
	 *	 wait for STATE_STARTING
	 *	 startExecution
	 *	 if (startEexecution failed) {
	 *	 STATE_DISABLED
	 *	 } else {
	 *	 wait for server to exit
	 *	 STATE_STOPPED
	 *	 }
	 * }
	 * </xmp>
	 */

	protected void runForever() {
		// infinite loop
		synchronized (this) {
			int lastState = mState;
			while (true) {
				//showStack ( stateName(lastState) );

				// The state hasn't changed, so wait for a change
				if (lastState == mState) {
					setStartedWorking();
					try {
						wait();
					} catch (InterruptedException iex) {}
				}
				setStartedWorking();

				//showStack ( stateName(mState) );

				Boolean stateChanged = Boolean.FALSE;

				if ( lastState != mState) {
					mLogger.finer( "Entering main state machine. Last state : " + mStateNames[lastState]
							+ ". Current state : " + mStateNames[mState]);
					stateChanged = Boolean.TRUE;
				}

				lastState = mState;

				// Figure out what we need to do now

				switch (mState) {
					case STATE_DISABLED:
					case STATE_STARTING:
					case STATE_STOPPING_HARD:
					case STATE_STARTED:
						break;

					case STATE_DISABLED_DEPENDENT:
						mLogger.finer( "disabled dependent");
						handleStateDisabledDependent(stateChanged);
						break;

					case STATE_START_DEPENDENCIES:
						handleStateStartDependencies(stateChanged);
						break;

					case STATE_STOP_DEPENDENCIES:
						handleStateStopDependencies(stateChanged);
						break;

					case STATE_STOPPING_GENTLY:
						if (System.currentTimeMillis() - mGentleStopTime > 120000)
						{
							mLogger.finer( "Gentle stop doesn't seem to have worked");
							changeState (STATE_STOPPING_HARD);
						}
						break;

					case STATE_STOPPED:
						handleStateStopped(stateChanged);
						break;

					case STATE_RESTART_DELAY:
						handleStateRestartDelay(stateChanged);
						break;

					default:
						mLogger.severe( "Unknown state in state machine" + mState);
						handleUnexpectedState(stateChanged);
						break;

				} //switch
			} //while
		}//synchronized
	}

	/**
	 * Get the state of this server.
	 *
	 * @return The state of this server.
	 */
	protected int getState () {
		return mState;
	}

	/**
	 * Get this server's generation. Each time the server is restarted,
	 * the generation is incremented.
	 *
	 * @return This server's generation.
	 */
	protected int getGeneration () {
		return mGeneration;
	}

	/**
	 * Get the time that this server was last startExecution()ed.
	 */
	protected long getExecTime () {
		return mExecTime;
	}

	/**
	 * Another server this one is listening to has changed its state.
	 * This may mean that this server was waiting for a dependent
	 * server to start, and now it has.
	 *
	 * @param server the server which changed
	 * @param newState the server's new state.
	 */
	public synchronized void serverStateChanged (Server server, int newState) {
		if( mLogger.isLoggable(Level.FINER) ) {
			mLogger.finer( "Notified that server "
				 + server.getName ()
			 	+ " changed to state "
			 	+ stateName (newState));
		}

		if (mPrincipals.contains (server)) {
			// A server I depend on has changed state.	Check if this is a
			// state change I care about and if so wake up my state machine.
			switch (newState) {
				case STATE_STARTED:
					if (mState == STATE_DISABLED_DEPENDENT
						|| mState == STATE_START_DEPENDENCIES)
					{
							// I'm waiting for servers I depend on to start.	One of them
							// has just started, so it's time to reevaluate if I should
							// start.
						notifyAll ();
					}
					break;

				case STATE_STOP_DEPENDENCIES:
				case STATE_STOPPING_GENTLY:
				case STATE_STOPPING_HARD:
				case STATE_STOPPED:
				case STATE_DISABLED_DEPENDENT:
				case STATE_DISABLED:
					if (mState == STATE_START_DEPENDENCIES) {
						// I'm waiting for servers I depend on to start and one of
						// them has indicated that it won't be starting.	Force
						// another trip through the state machine logic in case I
						// need a state change as well.
						notifyAll ();
					} else if (mState == STATE_STARTING || mState == STATE_STARTED) {
						// I'm in the process of starting or already started, but
						// somethine I depend on has gone down.	Shut me down as
						// well.
						changeState (STATE_STOP_DEPENDENCIES);
					}
					break;

				case STATE_START_DEPENDENCIES:
				case STATE_STARTING:
					if (mState == STATE_DISABLED_DEPENDENT) {
						// If I'm waiting for
						Iterator e = mPrincipals.iterator ();
						while (e.hasNext ()) {
							Server s = (Server) e.next ();
							boolean okayToStart = true;
							switch (s.getState ()) {
								case STATE_START_DEPENDENCIES:
								case STATE_STARTING:
								case STATE_STARTED:
								case STATE_STOP_DEPENDENCIES:
								case STATE_STOPPING_GENTLY:
								case STATE_STOPPING_HARD:
								case STATE_STOPPED:
								case STATE_RESTART_DELAY:
									// These are okay, the server should eventually come up.
									break;

								case STATE_DISABLED:
								case STATE_DISABLED_DEPENDENT:
									okayToStart = false;
									break;
							}
							if (okayToStart) {
								changeState (STATE_START_DEPENDENCIES);
							}
						}
					}
					break;
			}
		} else if (mServersDependentOnMe.contains (server)) {
			// A server who depends on me has changed state.
			if ((mState == STATE_STOP_DEPENDENCIES || mState == STATE_STOPPED)
			&& (newState == STATE_STOPPED
				|| newState == STATE_DISABLED_DEPENDENT
				|| newState == STATE_DISABLED))
			{
				// I'm waiting for servers that depend on me to stop and one
				// of them has.	Notify the state machine so it can deal with
				// any necessary state changes.
				notifyAll ();
			}
		}
	}

	/**
	 * The implemntors could override this method
     * to do something before start up.
	 */
	protected void customStartup () {
		// do nothing
	}

	/**
     * Returns the string watchdog.&lt;server_name&gt;
	 */
	protected String getPropertyPrefix () {
		return mPropertyPrefix;
	}

	/**
     * Finds out the property using the property names
     * in the list. The first non-null property value
     * is returned.
	 */
	protected String getProperty (String[] properties) {
		if( properties == null) return null;
		for(int i=0; i < properties.length; ++i) {
			String rc = DCPLib.getProperty (properties[i], null);
			if (rc != null && rc.trim().length() > 0) {
				return rc;
			}
		}
		return null;
	}

	/**
     * Finds out the property using the property names
     * in the list. The first non-null property value
     * is returned.
	 */
	protected String getProperty (List properties) {
		if( properties == null) return null;
		while (properties.size () > 0) {
			String propertyName = (String) properties.remove(0);
			String rc = DCPLib.getProperty (propertyName, null);
			if (rc != null && rc.trim().length() > 0) {
				return rc;
			}
		}
		return null;
	}

	/**
     * Returns the property value for the given property
	 */
	protected String getProperty (String propertyName) {
		String val = DCPLib.getProperty(propertyName, null);
		if( val != null && (val.trim().length() > 0 )) return val;
		return null;
	}

	/**
     * Prefixes mPropertyPrefix to the given propertyName , to
     * build a new propertyName and returns the value of that property.
	 */
	protected String getServerProperty (String propertyName) {
		String val = DCPLib.getProperty(mPropertyPrefix + "." + propertyName, null);
		if( val != null && (val.trim().length() > 0) ) return val;
		return null;
	}

	/**
	 * Each derived server class must provide a heartbeat method to
	 * detect that the server is functioning correctly.
	 *
	 * @return any status description or null (placeholder)
	 * @exception Exception If the heartbeat attempt failed for any reason.
	 */
	public abstract Object heartbeat () throws Exception;


	/**
     * Once all the servers that this one depends on are
     * started, this server will have to be started.
	 * At that point this method is called by the state machine.
	 * Sub-classes should start executing the server process
     * in their implementation of this method.
	 */
	protected abstract void startExecution();

	/**
	 * Issue a warning about an unexpected state transition.
	 *
	 * @param fromState The state being transitioned from.
	 * @param toState The state being transitioned to.
	 */
	protected void transitionWarning (int fromState, int toState) {
		mLogger.warning( "Transitioning to state "
			 + stateName (fromState)
			 + " from state "
			 + stateName (toState));
	}

	/**
     * Returns the logger object reference
	 */
	protected CiscoLogger getLog() {
		return mLogger;
	}

	/**
     * Wrapper for the actual changeState method.
	 * Useful for logging and debugging.
	 * @see Server#_changeState
	 */

	protected synchronized void changeState ( int toState) {
		if(mLogger.isLoggable(Level.FINEST))
			mLogger.finest( "Changing state from " + stateName(mState) + "  to  " + stateName(toState));

		_changeState(toState);

		if(mLogger.isLoggable(Level.FINEST))
			mLogger.finest( "Changed state to " + stateName(mState));
	}


	/**
	 * Change this server's state to "toState"
	 * Listeners are notified of the state change.
	 */
	protected synchronized void _changeState (int toState) {
		if (mState == toState) {
			return; // isn't really a state change
		}

		int fromState = mState;
		mState = toState;

		try {
			ServerEventAdapter
				.theServerEventAdapter (mManager)
				.sendStateEvent (getStateEvent ());

		}
		catch (TibrvException rve) {
			mLogger.warning( "Ignoring exception", rve);
		}

		switch (mState) {
			case STATE_START_DEPENDENCIES:
				break;

			case STATE_STARTING:
				if (fromState != STATE_START_DEPENDENCIES) {
					transitionWarning (fromState, toState);
				}
				mConsecutiveMissedHeartbeats = 0;
				startExecution();
				break;

			case STATE_STARTED:
				break;

			case STATE_STOP_DEPENDENCIES:
				if (fromState != STATE_STARTED) {
					transitionWarning (fromState, toState);
				}
				break;

			case STATE_STOPPING_GENTLY:
				// All servers this server depends on are now stopped, so
				// take this server down as well.
				if (fromState != STATE_STOP_DEPENDENCIES) {
					transitionWarning (fromState, toState);
				}
				tryToStopGently ();
				break;

			case STATE_STOPPING_HARD:
				if (fromState != STATE_STOPPING_GENTLY) {
					transitionWarning (fromState, toState);
				}
				stopServerHard ();
				break;

			case STATE_STOPPED:
				if (fromState != STATE_STARTING
					&& fromState != STATE_STARTED
					&& fromState != STATE_STOP_DEPENDENCIES
					&& fromState != STATE_STOPPING_GENTLY
					&& fromState != STATE_STOPPING_HARD)
				{
					transitionWarning (fromState, toState);
				}
				// If this was an unexpected stop, record when it happened so we
				// can detect if the server is dying quickly after it was
				// started.
				mStopTime = System.currentTimeMillis ();
				mPid = 0;
				break;

			case STATE_DISABLED:
				if (fromState != STATE_STARTING && fromState != STATE_STOPPED) {
					transitionWarning (fromState, toState);
				}
				break;

			case STATE_DISABLED_DEPENDENT:
					if (fromState != STATE_START_DEPENDENCIES && fromState != STATE_STOPPED) {
					transitionWarning (fromState, toState);
				}
				break;

			case STATE_RESTART_DELAY:
				if (fromState != STATE_STOPPED) {
					transitionWarning (fromState, toState);
				}
				break;

			default:
				mLogger.warning( "Unexpected state: " + toState);
				break;
		}

		if (mState == STATE_STARTING || mState == STATE_STARTED) {
			mHeartbeatThread.enable ();
		} else {
			mHeartbeatThread.disable ();
			mSuccessfulHeartbeats = 0;
			mServerStatus.successfulHeartbeats = 0;
			mMissedHeartbeats = 0;
			mServerStatus.missedHeartbeats = 0;
			mHBResult = null;
			mServerStatus.hbResult = null;
			updateServerStatus ();
		}

		notifyListeners ();
		notifyAll ();
	}

	/**
     * Notifies the registered listeners about the state change in this server.
	 */
	protected void notifyListeners () {
		Iterator listeners = mStateListeners.iterator ();
		while (listeners.hasNext ()) {
			ServerStateListener listener =	(ServerStateListener) listeners.next ();
			forwardEventPoolRequest (new NotifyListener (listener, mState));
		}
	}

	/**
	 * Add an object to this server's set of state listeners.	Each time
	 * this server's state changes, all listeners will be notified.
	 *
	 * @param listener An object that cares about this server's state changes.
	 */
	protected void addStateListener (ServerStateListener listener) {
		mStateListeners.add (listener);
	}


	/**
	 * Get a String version of an integer state.
	 *
	 * @param state The state to get.
	 * @return The correspinding String.
	 */
	protected static String stateName (int state) {
		String stateName = "unknown";
		try {
			stateName = mStateNames[state];
		}
		catch (Exception e) {
			// ignore
		}
		return stateName;
	}

	/**
	 * Get a String version of this servers current state.
	 *
	 * @return The correspinding String.
	 */
	protected String stateName () {
		return stateName (mState);
	}

	protected void showStack(String msg) {
		if( mName.equals("------") ||
			mName.equals("!!!!!!!"))
		{
			new Exception(mName + " :::: " + msg).printStackTrace();
		}
	}

	/**
	 * Start this server.	If the server has been disabled, this will
	 * reenable it.
	 */
	protected synchronized void startServer(boolean waitFlag)
		throws ServerStateChangeException, PrincipalDisabledException
	{

		while ( ! hasStartedWorking() ) {
			try {
				wait(500);
			} catch (Exception ex) {}
		}

		setDisabled( false );

		//showStack("Enabled just now...");

		switch (mState) {
			case STATE_START_DEPENDENCIES:
			case STATE_STARTING:
			case STATE_STARTED:
			case STATE_DISABLED_DEPENDENT:
			case STATE_RESTART_DELAY:
				// Everything is on track to get there
				break;

			case STATE_STOP_DEPENDENCIES:
			case STATE_STOPPING_GENTLY:
			case STATE_STOPPING_HARD:
			case STATE_STOPPED:
				// Since mDisabled has been cleared here, the server will come
				// up again once the in progress shutdown has completed.
				break;

			case STATE_DISABLED:
				//showStack("About to call change state...");
				changeState (STATE_START_DEPENDENCIES);
				//showStack("Done with change state...");
				break;

			default:
				mLogger.warning( "Unhandled state: " + stateName ());
				break;
		}

		if (waitFlag) {

			// Wait for it to become started or for someone else to disable it
			// again.

			long endTime = System.currentTimeMillis() + 180000;

			while (!isDisabled() && mState != STATE_STARTED) {

				Iterator e = mPrincipals.iterator ();
				while (e.hasNext ()) {
					Server s = (Server) e.next ();
					switch (s.getState ()) {
						case STATE_START_DEPENDENCIES:
						case STATE_STARTING:
						case STATE_STARTED:
						case STATE_STOP_DEPENDENCIES:
						case STATE_STOPPING_GENTLY:
						case STATE_STOPPING_HARD:
						case STATE_STOPPED:
						case STATE_RESTART_DELAY:
						// These are okay, the server should eventually come up.
							break;

						case STATE_DISABLED:
						case STATE_DISABLED_DEPENDENT:
							throw new PrincipalDisabledException( mName,
										ServerStateChangeException.START_OPERATION,
										mState, s.getName(), s.getState() );
					}
				}

				long delay = endTime - System.currentTimeMillis();

				if ( delay > 0) {

					mLogger.fine("Will wait " + delay + " milli seconds for server to start");
					try {
						wait(delay);
					}
					catch (InterruptedException ie) {
						// ignore
					}
				} else {
					mLogger.fine("Timed out waiting for server to start");
					break;
				}
			}


			if (isDisabled() || mState != STATE_STARTED) {
				throw new ServerStateChangeException(mName,
							ServerStateChangeException.START_OPERATION,
							mState);
			}
		}

	}

	/**
	 * Stop this server by attempting a gentle stop and then a hard stop
	 * if that doesn't work.
	 */
	protected synchronized void stopServer (boolean waitFlag)
		throws ServerStateChangeException
	{
		setDisabled ( true );

		switch (mState) {
			case STATE_START_DEPENDENCIES:
			case STATE_RESTART_DELAY:
				changeState (STATE_DISABLED);
				break;

			case STATE_STARTING:
				changeState (STATE_STOPPING_HARD);
				break;

			case STATE_STARTED:
				changeState (STATE_STOP_DEPENDENCIES);
				break;

			case STATE_STOP_DEPENDENCIES:
			case STATE_STOPPING_GENTLY:
			case STATE_STOPPING_HARD:
				// Nothing to do, we're already heading there
				break;

			case STATE_STOPPED:
				// Don't change it, we're probably waiting for servers that
				// depend on this server to stop.
				break;

			case STATE_DISABLED:
				// nothing to do
				break;

			case STATE_DISABLED_DEPENDENT:
				changeState (STATE_DISABLED);
				break;

			default:
				mLogger.warning( "Unhandled state: " + stateName());
				break;
		}

		if (waitFlag) {
			// Wait for it to become disabled or for someone else to enable it
			// again.
			long endTime = System.currentTimeMillis() + 120000;

			while (isDisabled() && mState != STATE_DISABLED) {
				long delay = endTime - System.currentTimeMillis();

				if ( delay > 0) {
					mLogger.fine("Will wait " + delay + " milli seconds for server to stop");
					try {
						wait(delay);
					}
					catch (InterruptedException ie) {
						// ignore
					}
				} else {
					mLogger.fine("Timed out waiting for server to stop");
					break;
				}
			}

			if (!isDisabled() || mState != STATE_DISABLED) {
				throw new ServerStateChangeException(mName,
							ServerStateChangeException.STOP_OPERATION,
							mState);
			}
		}
	}

	/**
	 * Stop this server hard.
	 */
	protected abstract void stopServerHard ();


	/**
     * Called by watchdog at the time of exiting.
	 * The implemenation of this method, does some cleanup
     * followed by stopServerHard.
	 */
	protected synchronized void shutdown () {
		setDisabled ( true );
		mHeartbeatThread.disable ();
		mLogger.severe( "Watchdog stopped by stopwd command");
		stopServerHard ();
		notifyAll ();
	}

	/**
	 * Update the server status structure and send a TIBCO event
     * to inform the interested parties of the same.
	 */
	protected void updateServerStatus () {
		synchronized (mServerStatus) {
			mServerStatus.pid = mPid;
			mServerStatus.state = stateName();
			mServerStatus.generation = mGeneration;
			mServerStatus.execTime = mExecTime;
			mServerStatus.successfulHeartbeats = mSuccessfulHeartbeats;
			mServerStatus.missedHeartbeats = mMissedHeartbeats;
			mServerStatus.hbResult = mHBResult;
                        mServerStatus.errorDescription = mErrorDescription;
			mServerStatus.notifyAll ();
			try {
				if( DCPLib.getBooleanProperty(WDConstants.WD_PREFIX + ".heartbeat.sendEvents", false)) {
					ServerEventAdapter
						.theServerEventAdapter (mManager)
						.sendHeartbeatEvent (getStateEvent ());
				}
			}
			catch (TibrvException rve) {
				mLogger.warning( "Ignoring exception", rve);
			}
		}
	}


	/**
	 * WatchdogImpl calls this method to see if this server is
     * down.
	 * @return true if the server is totally disabled and is non-functional; false otherwise
	 */
	protected boolean isNonFunctional()  {
		if( mState == STATE_DISABLED ) {
			return true;
		}
		return false;
	}

	/**
	 * Get a snapshot of this server's status.
	 *
	 * @return Snapshot of this server's status.
	 */
	protected ServerStatus getStatus () {
		synchronized (mServerStatus) {
			return (ServerStatus) mServerStatus.clone ();
		}
	}

	/**
	 * The maximum length of time to wait for update status information.
	 * This prevents corba threads from hanging around after the client
	 * exits.
	 */
	protected long mWaitTimeout = 60*1000; // 1 minute

	/**
	 * Get a snapshot of this server's status, but wait for it to be
	 * different from the specified status.
	 *
	 * @param lastStatus The status it must differ from
	 * @return a snapshot of this server's status.
	 */
	protected ServerStatus getStatus (ServerStatus lastStatus) {
		synchronized (mServerStatus) {
			if (lastStatus.generation == mServerStatus.generation
				&& lastStatus.successfulHeartbeats == mServerStatus.successfulHeartbeats
				&& lastStatus.missedHeartbeats == mServerStatus.missedHeartbeats
				&& lastStatus.state.equals (mServerStatus.state))
			{
				try {
					mServerStatus.wait (mWaitTimeout);
				}
				catch (InterruptedException ie) {
					// ignore
				}
			}
			if (lastStatus.generation == mServerStatus.generation
				&& lastStatus.successfulHeartbeats == mServerStatus.successfulHeartbeats
				&& lastStatus.missedHeartbeats == mServerStatus.missedHeartbeats
				&& lastStatus.state.equals (mServerStatus.state) )
			{
				mLogger.warning( "Server.getStatus: timeout out");
			}
			return (ServerStatus) mServerStatus.clone ();
		}
	}

	/**
	 * Called by ServerHeartbeatThread to notify this server that the
	 * server has failed to have a successful heartbeat within the
	 * allowed time after being started.
	 */
	protected synchronized void failedToStart () {
		if (mState == STATE_STARTING) {
			 mLogger.severe( "Killing server process because it has not yet had a successful heartbeat");
                         mErrorDescription = "Killing server process because it has not yet had a successful heartbeat";
			mConsecutiveMissedHeartbeats = -1;
			changeState (STATE_STOPPING_HARD);
		}
	}

	/**
	 * Called by ServerHeartbeatThread to notify this server that a heartbeat
	 * has been missed.
	 *
	 */
	protected synchronized void missedHeartbeat () {
		if (mSuccessfulHeartbeats > 0) {
			mMissedHeartbeats++;
			updateServerStatus ();
			mLogger.warning( "Missed a heartbeat " + (mConsecutiveMissedHeartbeats+1) + " "
				+ MAX_ALLOWED_MISSED_HEARTBEATS);
			if (++mConsecutiveMissedHeartbeats == MAX_ALLOWED_MISSED_HEARTBEATS)
			{
				mLogger.severe( "Restarting server because of " + MAX_ALLOWED_MISSED_HEARTBEATS +
					" consecutive missed heartbeats");
                                mErrorDescription = "Restarting server process because of " + MAX_ALLOWED_MISSED_HEARTBEATS +
					" consecutive missed heartbeats";
				changeState (STATE_STOPPING_HARD);
			}
		}
	}

	/**
	 * Called by HeartbeatThread to notify this server that a heartbeat
	 * has been successful.
	 * @param result the object that could contain some description of the status
	 */
	protected synchronized void successfulHeartbeat (Object result) {
		if (mState == STATE_STARTED || mState == STATE_STARTING) {
			if (mConsecutiveMissedHeartbeats > 0) {
				mLogger.finer( "Successful heartbeat");
			}
                        mErrorDescription = null;
			mConsecutiveMissedHeartbeats = 0;
			if (mSuccessfulHeartbeats++ == 0) {
				changeState (STATE_STARTED);
			}
			mHBResult = result;
			updateServerStatus ();
		}
	}

	/**
     * Adds a task to the event pool in the ServerManager.
	 */
	protected void forwardEventPoolRequest(Runnable runnable) {
		if(mManager != null) {
			mManager.addEventToPool(runnable);
		}
	}

	/**
	 * This initializes dependencies between servers as specified by the
	 * watchdog.server.NAME.depends property.	This server's
	 * mPrincipals set is initialized and this server is added to
	 * the mServersDependentOnMe sets of the servers this depends on.
	 *
	 * @return true if there was a cycle in the dependencies, false
	 *	 otherwise.
	 */
	protected boolean addDependencies () {
		String dependencies = getServerProperty ("dependencies");
		if (dependencies != null) {

			String[] path = new String[mManager.getNumServers ()];
			for (int i = 0; i < path.length; i++) {
				path[i] = null;
			}

			StringTokenizer tokenizer = new StringTokenizer (dependencies, ", \t\n");
			while (tokenizer.hasMoreTokens ()) {
				String serverName = tokenizer.nextToken ();
				Server server = mManager.getServer (serverName);

				if (server != null) {
					if (server.eventuallyDependsOn (this, 0, path)) {
						String cycle = getName ();
						for (int i = 0; i < path.length && path[i] != null; i++) {
							cycle += "->" + path[i];
						}
						mLogger.severe( "Detected cycle in dependency graph:" + cycle);
						return true;
					} else {
						mPrincipals.add (server);
						server.addDependency (this);
						addStateListener (server);
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if this server eventually depends on the specified server.
	 *
	 * @param server The server to check for a dependency on.
	 * @param index The index into path for this server to write to.
	 * @param path The dependency path that leads to the server.
	 * @return true if this server eventually depends on the specified
	 *	 server, false otherwise.	If true is returned, path contains
	 *	 the dependency route to the specified server.	Unused entries
	 *	 in the path parameter are set to null.
	 */
	protected boolean eventuallyDependsOn (Server server,
						int index,
						String[] path) {
		path[index] = getName ();

		if (server == this) {
			return true;
		}

		Iterator e = mPrincipals.iterator ();
		while (e.hasNext ()) {
			Server next = (Server) e.next ();
			if (next.eventuallyDependsOn (server, index + 1, path)) {
				return true;
			}
		}

		path[index] = null;
		return false;
	}

	/**
	 * This is called by another server to register that server's
	 * dependence on this server.	The server is added to this
	 * mServersDependentOnMe and is also registered as a state listener
	 * for this server.
	 * @param server A server that is dependent on this server.
	 */
	protected void addDependency (Server server) {
		synchronized ( mServersDependentOnMe) {
			mServersDependentOnMe.add (server);
		}
		addStateListener (server);
	}

	/**
	 * Returns an unmodifiable collection of servers that depend on this server
	 */
	protected Collection getDependents() {
		return Collections.unmodifiableCollection(mServersDependentOnMe);
	}

	/**
	 * Returns an unmodifiable collection of servers that depend on this server
	 */
	protected Collection getPrincipals() {
		return Collections.unmodifiableCollection(mPrincipals);
	}

	/**
	 * This is called from the state machine whenever this server is in
	 * STATE_START_DEPENDENCIES.	All servers that this server depends
	 * on are checked and if they are all in STATE_STARTED this server
	 * enters STATE_STARTING.	Otherwise, this server may transition to
	 * STATE_DISABLED_DEPENDENT.
	 */
	protected void handleStateStartDependencies(Boolean stateChanged) {
		if (mState != STATE_START_DEPENDENCIES) {
			mLogger.warning( "handleStateStartDependencies called from state "
				 + stateName());
		} else {
			boolean waiting = false;
			Iterator e = mPrincipals.iterator ();
			while (e.hasNext ()) {
				Server server = (Server) e.next ();
				String serverName = server.getName ();
				switch (server.getState ()) {
					case STATE_DISABLED:
					case STATE_DISABLED_DEPENDENT:
					case STATE_STOP_DEPENDENCIES:
						changeState (STATE_DISABLED_DEPENDENT);
						return;

					case STATE_STOPPED:
					case STATE_RESTART_DELAY:
					case STATE_START_DEPENDENCIES:
					case STATE_STARTING:
					case STATE_STOPPING_GENTLY:
					case STATE_STOPPING_HARD:
						waiting = true;
						if ( Boolean.TRUE.equals(stateChanged)) {
							mLogger.finer( "waiting for " + serverName + " which is currently " + server.stateName());
						}
						break;

					case STATE_STARTED:
						// this is what we want;
						break;

					default:
						if ( Boolean.TRUE.equals(stateChanged)) {
							mLogger.warning( "Unrecognized state of server " + server.getName() + " : " + server.stateName() );
						}
						break;
				}
			}

			if (!waiting ) {
				changeState (STATE_STARTING);
			}
		}
	}


	/**
	 * This is called from the state machine whenver this server is in
	 * STATE_STOP_DEPENDENCIES.	All servers that depend on this server
	 * are checked to see if they are stopped yet.	If they are, this
	 * server transitions to STATE_STOPPING.
	 */
	protected void handleStateStopDependencies(Boolean stateChanged) {
		if (mState != STATE_STOP_DEPENDENCIES) {
			mLogger.warning( "handleStateStopDependencies called from state "
				 + stateName());
		} else {
			boolean shouldWaitForDependents = false;
			Iterator e = mServersDependentOnMe.iterator ();
			while (e.hasNext ()) {
				Server server = (Server) e.next ();
				switch (server.getState ()) {
					case STATE_STOPPED:
					case STATE_DISABLED:
					case STATE_DISABLED_DEPENDENT:
						// This is what we want.
						break;
					case STATE_STARTING:
					case STATE_START_DEPENDENCIES:
					case STATE_STARTED:
					case STATE_STOP_DEPENDENCIES:
					case STATE_STOPPING_GENTLY:
					case STATE_STOPPING_HARD:
					case STATE_RESTART_DELAY:
						shouldWaitForDependents = true;
						if ( Boolean.TRUE.equals(stateChanged)) {
							mLogger.finer( "Waiting for " + server.getName () + " which is currently " + server.stateName());
						}
						break;

					default:
						if ( Boolean.TRUE.equals(stateChanged)) {
							mLogger.warning( "Unrecognized state of server " + server.getName() + " : " + server.stateName() );
						}
						break;
				}
			}

			if (!shouldWaitForDependents) {
				changeState (STATE_STOPPING_GENTLY);
			}
		}
	}


	/**
	 * This is called from the state machine whenever this server is in
	 * STATE_STOPPED.	If this server is being disabled, it transitions
	 * to STATE_DISABLED.	Otherwise, if servers that depend on this
	 * server are not stopped yet, we sit here until they are.	Once all
	 * servers that depend on this server are stopped, this server can
	 * transition back to STATE_START_DEPENDENCIES.
	 */
	protected void handleStateStopped(Boolean stateChanged) {
		if (mState != STATE_STOPPED) {
			mLogger.warning( "handleStateStopped called from state " + stateName());
		} else {
			boolean shouldWaitForDependents = false;

			Iterator e = mServersDependentOnMe.iterator ();
			while (e.hasNext ()) {
				Server server = (Server) e.next ();
				switch (server.getState ()) {
					case STATE_STOPPED:
					case STATE_DISABLED:
					case STATE_DISABLED_DEPENDENT:
					case STATE_START_DEPENDENCIES:
					case STATE_RESTART_DELAY:
						// This is what we want.
						break;
					case STATE_STARTING:
					case STATE_STARTED:
					case STATE_STOP_DEPENDENCIES:
					case STATE_STOPPING_GENTLY:
					case STATE_STOPPING_HARD:
						shouldWaitForDependents = true;
						if ( Boolean.TRUE.equals(stateChanged)) {
							mLogger.finer( "waiting for " + server.getName () + " which is currently " + server.stateName());
						}
						break;

					default:
						if ( Boolean.TRUE.equals(stateChanged)) {
							mLogger.warning( "Unrecognized state of server " + server.getName() + " : " + server.stateName() );
						}
						break;
				}
			}

			if (!shouldWaitForDependents) {
				// decide if the server should be restarted
				if (isDisabled()) {
					changeState (STATE_DISABLED);
				} else {
					e = mPrincipals.iterator ();
					while (e.hasNext ()) {
						Server server = (Server) e.next ();
						if (server.getState () != STATE_STARTED) {
							changeState (STATE_DISABLED_DEPENDENT);
							return;
						}
					}

					mLogger.info( "Length of run : "  + (mStopTime - mExecTime)
						+ "ms. Successful heartbeats : " + mSuccessfulHeartbeats
						+ ". Consecutive misses  " + mConsecutiveMissedHeartbeats);

					if ((mStopTime - mExecTime) < mQuickDieThreshold ||
						(mConsecutiveMissedHeartbeats >= MAX_ALLOWED_MISSED_HEARTBEATS ||  //started but stopped heartbeating after a while
						mConsecutiveMissedHeartbeats < 0 ) )  //never had a successful heartbeat
					{
						mQuickDieCount++;
						mLogger.severe( "died quickly or failed to heartbeat for the " + mQuickDieCount + " time");
						if (mQuickDieCount >= mQuickDieLimit) {
							mLogger.severe( "Disabling server because it keeps dying");
							synchronized (this) {
                                                                mErrorDescription = "Disabling server process because it keeps dying";
								setDisabled (true);
								changeState (STATE_DISABLED);
								mQuickDieCount = 0;
							}
						} else {
							changeState (STATE_RESTART_DELAY);
						}
					} else {
						// the server stayed up for a while, so clear quickDieCount
						mQuickDieCount = 0;
						mLogger.finer( "Clearing quickDieCount");
						changeState (STATE_RESTART_DELAY);
					}
				}
			}
		}
	}

	/**
     * This method handles the state transition to
     * DISABLED_DEPENDENT state.
	 */
	protected void handleStateDisabledDependent(Boolean stateChanged) {
		if (mState != STATE_DISABLED_DEPENDENT) {
			mLogger.warning( "handleStateDisabledDependent called from state "
				 + stateName());
		} else {
			boolean shouldWait = false;

			Iterator e = mPrincipals.iterator ();
			while (e.hasNext ()) {
				Server server = (Server) e.next ();
				if ( Boolean.TRUE.equals(stateChanged)) {
					mLogger.finer( "State of principal server " + server.mName + " is " + server.stateName());
				}
				if (server.getState () != STATE_STARTED) {
					shouldWait = true;
				}
			}

			if (!shouldWait) {
				changeState (STATE_RESTART_DELAY);
			}
		}
	}

	/**
	 * This method should handle the state transition
	 * to "restart_delay"
	 */
	protected void handleStateRestartDelay(Boolean stateChanged) {
		long delay = mRestartDelay - (System.currentTimeMillis () - mStopTime);
        if( delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (Exception ex) {}
        }
		changeState (STATE_START_DEPENDENCIES);
	}

	/**
	 * This tries to stop a server gently if a stopGentle method has
	 * been defined by the derived class.	If no such method has been
	 * defined, this server transitions to STATE_STOP_HARD.
	 */
	protected void tryToStopGently () {
		try {
			Class myClass = getClass ();
			Method stopMethod = myClass.getMethod ("stopServerGentle",
							 new Class [0]);

			// There is a stopServerGentle(), so give it a try in a
			// subthread.
			mLogger.finer( "Attempting a gentle stop");
			StopGentleThread sgt = new StopGentleThread (stopMethod);
			sgt.start ();

			// Remember when the gentle stop was attempted so we can to a
			// hard stop if it takes too long
			mGentleStopTime = System.currentTimeMillis ();
		}
		catch (Exception e) {
			// There was no stop gentle method, so take it down hard
			mLogger.finer( "Server does not have a way to stop gently");
			changeState (STATE_STOPPING_HARD);
		}
	}


	/**
	 * This is used to gently stop a server.	A separate thread is used
	 * so that the server will still come down if the gentle stop
	 * method hangs.
	 */
	class StopGentleThread extends Thread {
		Method mStopMethod;

		StopGentleThread (Method stopMethod) {
			super ();
			setDaemon (true);
			mStopMethod = stopMethod;
		}

		public void run () {
			try {
				mStopMethod.invoke (Server.this, new Object[0]);
			}
			catch (InvocationTargetException ite) {
				Server.this.mLogger.warning("In invoking " + mStopMethod.getName(), ite);
			}
			catch (IllegalAccessException iae) {
				Server.this.mLogger.warning("In invoking " + mStopMethod.getName(), iae);
			}
		}
	}


	/**
	 * This class is used to decouple state change notification from the
	 * server itself (via the watchdog's event thread pool).	This is
	 * important to avoid deadlock if two servers try to notify each
	 * other that their states have changed.
	 */
	class NotifyListener implements Runnable {
		ServerStateListener mListener;
		int mNewState;

		NotifyListener (ServerStateListener listener, int newState) {
			mListener = listener;
			mNewState = newState;
		}

		public void run () {
			mListener.serverStateChanged (Server.this, mNewState);
		}
	}

	/**
     * Comparator implementation that can sort the
     * the Server objects by name.
	 */
	static class ServerComparator implements Comparator {

		public int compare (Object o1, Object o2) {
			Server s1 = (Server) o1;
			Server s2 = (Server) o2;
			return s1.getName ().compareTo(s2.getName ());
		}

		public boolean equals(Object obj) {
			return (obj == this);
		}
	}


	protected TibrvMsg getStateEvent ()
		throws TibrvException
	{
		synchronized ( mServerStatus ) {
			TibrvMsg msg = ServerStateEvent.toTibrvMsg (mName, stateName(), mGeneration,
						mExecTime, mPid, mSuccessfulHeartbeats,
						mMissedHeartbeats, mRevisionOfEvent++);
			return msg;
		}
	}

}


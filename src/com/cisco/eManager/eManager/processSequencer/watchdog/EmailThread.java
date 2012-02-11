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

import java.util.*;

import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.util.StringTokenizer;

import com.cisco.eManager.eManager.processSequencer.common.PropertiesConstants;
import com.cisco.eManager.eManager.processSequencer.common.SMTPMailer;
import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;
import com.cisco.eManager.eManager.processSequencer.common.*;
import com.cisco.eManager.eManager.processSequencer.common.logging.*;

/**
 * An instance of this class keeps track of all server state changes
 * and their ordering. Once the state of all servers stabilizes
 * sends e-mail out to the recipients specified by the
 * <em>watchdog.serverStatus.emailRecipients</em> property. <p>
 * The class implements ServerStateListener with method serverStateChanged.
 * It retrieves all of the Server objects and adds this StateListener.
 * From this point onwards, it keeps track of all state changes and their
 * ordering. <p>
 * Once the state of all servers stabilizes, an email is sent to the
 * appropriate alias giving a detailed description of state changes.
 * The threshold value (stableTime) as well as the email alias are
 * configurable. If the email-recipient list is not specified, mail is
 * not sent.
 */

public class EmailThread extends Thread implements ServerStateListener, WDConstants {

	final static String Spaces =
	"                                                                      ";

	private static final int DEFAULT_STABLE_TIME =   20000; // msecs
	private static final int DEFAULT_MONITOR_INTERVAL = 1000; //msecs

	private HashMap mServers;
	private ArrayList mEvents;
	private HashSet mChangedServers;
	private ByteArrayOutputStream mOrigState;
	private CiscoLogger mLogger = CiscoLogger.getCiscoLogger(LOGGER_NAME);

	private EmailConfig mEmailConfig;

	private static EmailThread msInstance;

	public synchronized static EmailThread getInstance()
		throws InvalidConfigException
	{
		if( msInstance == null) {
			msInstance = new EmailThread();
		}
		return msInstance;
	}

	/**
	 * EmailThread constructor
	 * Gets all the server objects and adds this StateListener to them
	 */
	private EmailThread()
		throws InvalidConfigException
	{
		super("Watchdog Email Thread");

		mEmailConfig = new EmailConfig();
		mLogger.info("Server status e-mail config is " + mEmailConfig.toString());

		mChangedServers = new HashSet();
		mServers = new HashMap();
		mEvents  = new ArrayList();

		mOrigState = new ByteArrayOutputStream();

		PrintWriter tmpWriter = new PrintWriter(mOrigState);
		WatchdogImpl wd = WatchdogImpl.getWatchdogImpl ();
		try {
			ServerStatus[] serverStatus = wd.getServerStatus ();
			PrintHelper.print (tmpWriter, serverStatus);
		} catch (Exception ex) {
			PrintHelper.print (tmpWriter, ex);
		} finally {
			tmpWriter.close();
		}

	}



	/**
	 * Entry point for EmailThread's thread. Looks for all servers to
	 * reach a stable state.  Then emails with information about each
	 * servers sequence of state changes and final state. If the email
	 * alias is null, does not send email.
	 **/
	public void run() {

		WatchdogImpl watchdog = WatchdogImpl.getWatchdogImpl ();
		Iterator iter = watchdog.getServers();

		synchronized (this) {
			while(iter.hasNext()) {
				Server server = (Server)iter.next();
				server.addStateListener(this);
				String servName = server.getName();
				mServers.put(servName, new ServerInfo (servName, server.getState()));
			}
		}


		while(true) {

			EmailConfig config = mEmailConfig.getConfig();

			long stableTime =  DCPLib.getIntProperty(PROP_STABLE_TIME, DEFAULT_STABLE_TIME);

			boolean allServersStable = true;
			boolean msgMailed = false;
			int count = 0;

			try {
				Thread.sleep(DEFAULT_MONITOR_INTERVAL);
			} catch(InterruptedException ie) { }

			/* check to see if all servers are stable
			* in any case, increment timestamps
			* if timestamp is greater than THRESHOLD, set server to stable
			*/
			Iterator servIter = mServers.values().iterator();
			while (servIter.hasNext()) {
				ServerInfo tmpSrvInfo = (ServerInfo) servIter.next();
				tmpSrvInfo.incrTime(DEFAULT_MONITOR_INTERVAL);
				if(tmpSrvInfo.getTime() > stableTime) {
					tmpSrvInfo.setStable(true);
				}
				if(! tmpSrvInfo.isStable()) allServersStable = false;
			}

			//if all servers are stable, email with information of state changes

			synchronized (this) {
				if(allServersStable && mEvents.size() > 0 )  {
					sendEmail(config);
					mEvents.clear();
					mChangedServers.clear();
				}
			}
		}
	}

	private synchronized void sendEmail(EmailConfig cfg) {

		SMTPMailer smtpMailer = new SMTPMailer (cfg.getEmailHost());
		smtpMailer.setFrom (cfg.getEmailFrom());

		Iterator iter = cfg.getUsers().iterator();
		while (iter.hasNext ()) {
			smtpMailer.addRecepient ((String) iter.next());
		}

		smtpMailer.setSubject("eManager update - " + LogUtil.getAppType() + "-" + LogUtil.getAppInst() + " - Server status");

		try {
			PrintWriter smtpWriter = smtpMailer.startSend();

                        smtpWriter.println("Application Type Name: " + LogUtil.getAppType() +
                                           " - Application Instance: " + LogUtil.getAppInst() + "\n");

			smtpWriter.println("The following servers had state changes: \n");

			Iterator srvrs = mChangedServers.iterator();
			while(srvrs.hasNext()) {
				smtpWriter.println ("\t" + srvrs.next());
			}

			smtpWriter.println("\n\nThe previous state of the system: \n");
			smtpWriter.println(mOrigState);

			WatchdogImpl wd = WatchdogImpl.getWatchdogImpl ();
			ServerStatus[] serverStatus = wd.getServerStatus ();
			mOrigState.reset();

			PrintWriter tmpWriter = new PrintWriter(mOrigState);
			PrintHelper.print (tmpWriter, serverStatus);

			smtpWriter.println("\nThe new state of the system: \n");
			smtpWriter.println(mOrigState);

                        smtpWriter.println("\nOutstanding errors: \n");
                        if (isOutstandingErrors(serverStatus)) {
                            for (int i = 0; i < serverStatus.length; i++) {
                                if (serverStatus[i].errorDescription != null) {
                                    smtpWriter.println(serverStatus[i].getName() + "  " +
                                                       serverStatus[i].errorDescription);
                                }
                            }
                            smtpWriter.println("\n");
                        }
                        else {
                            smtpWriter.println("NONE\n");
                        }

			smtpWriter.println("\nThe order of state changes: \n");
			Iterator msgs = mEvents.iterator();
			while(msgs.hasNext()) {
				Message event = (Message) msgs.next();
				smtpWriter.println(event.getMsg());
			}
			tmpWriter.close();
			smtpWriter.close();
		} catch(Exception ex) {
			mLogger.warning("EmailThread (in sending e-mail)", ex);
		}
	}

        private boolean isOutstandingErrors(ServerStatus[] status) {
            if (status != null) {
                for (int i = 0; i < status.length; i++) {
                    if (status[i].errorDescription != null) {
                        return true;
                    }
                }
            }
            return false;
        }

	/**
	 * Adds this event to the list of events taken place in the overall system.
	 */
	public synchronized void serverStateChanged (Server server, int newState) {
		int numServers = mServers.size();
		boolean found = false; //whether we found the server
		String serverName = server.getName();

		ServerInfo tmpServer = (ServerInfo) mServers.get(serverName);

		if( tmpServer != null) {
			Message msg = new Message (serverName,
				   Server.stateName(tmpServer.getState()),
				   Server.stateName(newState));

			// Need to figure out whether add a new element or just add
			// on an existing one.

			int numEvents = mEvents.size();

			if(numEvents < 1) {
			  mEvents.add(msg);
			} else {
				//otherwise we need to check with previous message
				Message prevMsg = (Message) mEvents.get(numEvents - 1);

				if(prevMsg.checkServer(serverName) == true) {
					prevMsg.addState(Server.stateName(newState));
				} else {
					mEvents.add(msg);
				}
			}

			//change its state
			tmpServer.changeState(newState);
			mChangedServers.add(serverName);
		} else {
			mLogger.warning("Received an unexpected serverStateChanged event from server : " + serverName);
		}
	}

	private static class ServerInfo {
		private String mServerName;
		private long mTime; /* in sec */
		private int mLatestState;
		private boolean mStable; /* whether server has reached a stable state */

		/* constructors */
		public ServerInfo(String server, int state) {
		  mServerName = server;
		  mTime = 0;
		  mLatestState = state;
		  mStable = false;
		}

		public void changeState(int newState) {
		  mTime = 0;
		  mLatestState = newState;
		  this.setStable(false);
		}

		public void incrTime(long msec) {
		  mTime += msec;
		}

		public String getServerName() {
		  return mServerName;
		}

		public long getTime() {
		  return mTime;
		}

		public int getState() {
		  return mLatestState;
		}

		public boolean isStable() {
		  return mStable;
		}

		public void setStable(boolean val) {
		  mStable = val;
		}
	}

	private static class Message {
		private String mServer;
		private StringBuffer mMsgBuffer;
		private int count; //number of state changes on this line

		static final int TRANSITIONS_PER_LINE = 4;

		public Message(String server, String state1, String state2) {
			this.mServer = server;

			mMsgBuffer = new StringBuffer(128);
			mMsgBuffer.append(mServer).append(" : ").append(Spaces.substring(mServer.length() + 2, 25))
					.append( state1 ).append( " -> ").append( state2 );

			count = 1;
		}

		public void addState(String state) {
			if(count < TRANSITIONS_PER_LINE) {
				count++;
				mMsgBuffer.append( " -> " ).append( state );
			} else {
				count = 0;
				mMsgBuffer.append( "\n" ).append( Spaces.substring(0, 25))
					.append( "-> " ).append( state );
			}
		}

		public String getMsg() {
		  return mMsgBuffer.toString();
		}

		public boolean checkServer(String serv) {
		  return ( mServer.equals(serv) );
		}
	}

	class DCPListener implements DCPCallback {

		EmailConfig mConfig;

		DCPListener(EmailConfig config) {
			mConfig = config;
		}

		public synchronized boolean handleChange(String property, String value) {
			if( property == null) return false;

			if (property.startsWith(SYS_EMAIL_PREFIX) ) {

				if( property.equals ( PROP_EMAIL_HOST )) {
					mConfig.setSmtpHost( value, false );
				}
				else if (property.equals( PROP_EMAIL_FROM )) {
					mConfig.setEmailFrom( value, false );
				}
			}
			else if (property.startsWith(WD_SERV_STAT_PREFIX) ) {

				if( property.equals ( PROP_SS_EMAIL_RCPTS )) {
					mConfig.setEmailRcpts( value, false );
				}
			}

			return true;
		}
	}

	class EmailConfig {

		private Set mUserList;
		private String mSmtpHost;
		private String mEmailFrom;

		private boolean mDirty;

		private EmailConfig mClone;

		EmailConfig() {
			mUserList = Collections.synchronizedSet(new TreeSet());

			setSmtpHost (DCPLib.getProperty (PROP_EMAIL_HOST, null), true);
			setEmailFrom(DCPLib.getProperty (PROP_EMAIL_FROM, null), true);
			setEmailRcpts(DCPLib.getProperty (PROP_SS_EMAIL_RCPTS, null), true);

			mDirty = true;

			DCPListener dcpl = new DCPListener(this);
			DCPLib.registerComponent(WD_SERV_STAT_PREFIX, dcpl);
			DCPLib.registerComponent(SYS_EMAIL_PREFIX, dcpl);
		}

		private EmailConfig(String host, String from, Set users) {
			mUserList = Collections.synchronizedSet(new TreeSet());
			if( users != null ) {
				mUserList.addAll(users);
			}
			mSmtpHost = host;
			mEmailFrom = from;
		}

		synchronized EmailConfig getConfig() {
			blockTillValidConfig();

			if( mDirty ) {
				mClone = new EmailConfig(mSmtpHost, mEmailFrom, mUserList);
				mLogger.finest("Server status e-mail config is : "
					+ mClone.toString());
				mDirty = false;
			}
			return mClone;
		}

		private synchronized boolean isConfigValid() {
			boolean valid = true;
			if( (mSmtpHost == null || mSmtpHost.trim().length() == 0) ||
				(mEmailFrom == null || mEmailFrom.trim().length() == 0) ||
				(mUserList == null || mUserList.size() == 0) )
			{
				valid = false;
			}
			return valid;
		}

		private synchronized void blockTillValidConfig() {
			if ( ! isConfigValid() ) {
				mLogger.finest("Server status e-mail config is insufficient : " + toString());
				do {
					try {
						wait(30000);
					} catch (InterruptedException ex) {}
				} while ( !isConfigValid());
			}
		}

		private synchronized void setSmtpHost(String host, boolean init) {
			mSmtpHost = host;
			notifyChange(init);
		}

		private synchronized void setEmailFrom(String from, boolean init) {
			if( from != null && !from.equals("")) {
				mEmailFrom = from;
			}	else {
				mEmailFrom = DEFAULT_EMAIL_FROM;
			}
			notifyChange(init);
		}

		private synchronized void setEmailRcpts(String emailRcpts, boolean init) {
			mUserList.clear();
			if (emailRcpts != null && emailRcpts.trim().length() != 0) {
				StringTokenizer tokenizer = new StringTokenizer (emailRcpts, ", \t\n");
				while (tokenizer.hasMoreElements ()) {
					mUserList.add (tokenizer.nextToken ());
				}
			}
			notifyChange(init);
		}

		private synchronized void notifyChange(boolean init) {
			if (!init) {
				mDirty = true;
				notifyAll();
			}
		}

		synchronized Set getUsers() {
			return mUserList;
		}

		synchronized String getEmailFrom() {
			return mEmailFrom;
		}

		synchronized String getEmailHost() {
			return mSmtpHost;
		}


		public String toString() {

			StringBuffer sb = new StringBuffer(1024);

			sb.append("Email Host : ").append(mSmtpHost).append( "; " )
				.append("Email From : ").append(mEmailFrom).append( "; " )
				.append("Email To  : ").append( mUserList).append("\n");

			return sb.toString();
		}
	}
}

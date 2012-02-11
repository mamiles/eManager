/*
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.  Possessions and use of this
 * program must conform strictly to the license agreement between the user
 * and Cisco Systems, Inc., and receipt or possession does not convey
 * any rights to divulge, reproduce, or allow others to use this program
 * without specific written authorization of Cisco Systems, Inc.
 *
 *      Copyright 2001, an unpublished work by
 *    Cisco Systems, Inc.  All rights reserved.
 */

package com.cisco.eManager.eManager.processSequencer.watchdog.servers;

//tibco imports
import com.tibco.tibrv.*;

//java library imports
import java.net.*;

//other imports
import com.cisco.eManager.eManager.processSequencer.watchdog.*;
import com.cisco.eManager.eManager.processSequencer.common.*;

//for HA
import com.cisco.eManager.eManager.processSequencer.common.PSInetAddress;

/**
 * WDLogServer abstracts the LogServer within the watchdog.
 *
 */
public class WDLogServer extends ProcessExecutor implements TibrvMsgCallback {

  public static final String HOSTNAME_FIELD = "hostname";
  public static final long MIN_EVENT_TIMEOUT = 10*1000; // 10 seconds
  public static final long MAX_EVENT_TIMEOUT = 10*60*1000; // 10 minutes
  public static final long DEFAULT_EVENT_TIMEOUT = 2*60*1000; // 2 minutes

  /*
   * Create a new WDLogServer server to abstract the LogServer within the
   * watchdog.
   *
   * @param name The server name
   */
  public WDLogServer(String name) {
    super(name);
/*
    String[] properties = {
      NetsysPropertiesConstants.NETSYS_WATCHDOG
      + ".server." + name + ".eventTimeout"
    };
    mEventTimeout = PropertyUtils.getLongProperty ("scheduler event timeout",
						   properties,
						   MIN_EVENT_TIMEOUT,
						   MAX_EVENT_TIMEOUT,
						   DEFAULT_EVENT_TIMEOUT);
*/
    mEventTimeout = DEFAULT_EVENT_TIMEOUT;
  }

  private int mNoticedEventServiceGeneration = 0;
  private int mNoticedSchedulerGeneration = 0;
  private long mMostRecentTimestamp = 0;
  private long mEventTimeout;

  /**
   * Perform a heartbeat on the scheduler.
   *
   * @exception Exception If the heartbeat attempt failed for any reason.
   */
  public Object heartbeat () throws Exception {
    synchronized (this) {
      if (mNoticedSchedulerGeneration < mGeneration) {
	mMostRecentTimestamp = 0;

	if (mGeneration == 1) {
	  subscribe();
	}

	mNoticedSchedulerGeneration = mGeneration;
      }
    }

    synchronized (this) {
      long curTimestamp = System.currentTimeMillis ();
      long sinceLastTimestamp = curTimestamp - mMostRecentTimestamp;
      if (sinceLastTimestamp > mEventTimeout) {
	throw new Exception ("timeout");
      }
    }
	return "";
  }

  /**
   * Receive an event sent out by the LogServer
   *
   * @param listener Listener of the event.
   * @param TibrvMsg The Tibco message received
   */
  public void onMsg(TibrvListener listener, TibrvMsg msg) {
    mLogger.finest("received an event WDScheduler::onMsg");
    synchronized (this) {
      try {
        //check the hostname first
        TibrvMsgField hostnameField =
          msg.getField(HOSTNAME_FIELD);

        String hostname = (String)hostnameField.data;
        if (hostname.equalsIgnoreCase(
              PSInetAddress.getLocalHost().getHostName())) {
          TibrvMsgField timestampField =
            msg.getField(MsgCommon.TIMESTAMP_FIELD);

          Long timestampLong = (Long)timestampField.data;
          long timestamp = timestampLong.longValue();
          mLogger.finest("event timestamp = " + timestamp);
          if (timestamp > mMostRecentTimestamp)
          {
            mLogger.finest("more recent than previous timestamp");
            mMostRecentTimestamp = timestamp;
            notifyAll();
          } else {
            // out of order events
            mLogger.finest("prior to most recent timestamp");
          }
        }
      }
      catch (Exception e) {
         //e.printStackTrace(mLog.getPrintWriter());
         // fall through, the server will end up disabled if this
         // problem persists.  It should never happen.
      }
    }
  }

  /**
   * This is just so the event service knows I'm still here.
   */
  public void ping() {
    // nothing to do
  }

  /**
   * This is called when the event service shuts down.
   */
  public void shutdown(int reason) {
    // nothing to do
  }

  /**
   * Subscribe to the heartbeat events.
   */
   public void subscribe() {
      String subject = "watchdog.log.heartbeat";

      String subjectWithPrefix = EventUtils.addSubjectPrefix(subject);

      try {
        EventUtils.getDefaultTransport(subjectWithPrefix);
        EventUtils.newListener(this, subjectWithPrefix);
      }
      catch (TibrvException e) {
         System.out.println("\nCaught TibrvException exception " + e);
      }
   }
}

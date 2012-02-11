/*********************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.	Possessions and use of this
 * program must conform strictly to the license agreement between the user
 * and Cisco Systems, Inc., and receipt or possession does not convey
 * any rights to divulge, reproduce, or allow others to use this program
 * without specific written authorization of Cisco Systems, Inc.
 *
 * Copyright (c) 2002 by Cisco Systems, Inc.
 * All rights reserved.
 *
 *********************************************************************/

/* Package */
package com.cisco.eManager.eManager.processSequencer.common;

import java.util.logging.Level;

import com.tibco.tibrv.*;
import com.cisco.eManager.eManager.processSequencer.common.PropertiesConstants;
import com.cisco.eManager.eManager.processSequencer.common.AppConfiguration;
import com.cisco.eManager.eManager.processSequencer.common.logging.*;

/**
 *  Common utility functions for interacting with Tibco Rendezvous
 *  events.
 */
public class EventUtils
{
  /**
   *  The default transport for this process.
   */
  static TibrvTransport msTransport = null;

  /**
   *  Flag used to decide whether or not to create RVD or RVA Transport
   *  objects.
   */
  static boolean msUseRVD = true;

  /**
   *  The logger object for this class
   */
  static CiscoLogger msLogger;

  /**
   *  debug flag
   */
  static boolean msDebug = false;

  /**
   *  The subject prefix for all messages.
   */
  static String msSubjectPrefix = null;

  /**
   *  Will be set to true if transport creation fails due to the port
   *  number being absent
   */
  static boolean msTransportCreationFailed = false;

  /**
   *  The dispatch thread
   */
  static DispatchThread msDispatchThread = null;

  /**
   *  Gets the boolean flag that tells us the preferred Tibco Transport
   *  object to create.
   *
   * @return    true if should create TibrvRvdTransport or false if
   *      TibrvRvaTransport objects should be created.
   */
  public static boolean isRVDPreferred()
  {
    return EventUtils.msUseRVD;
  }

  /**
   *  Sets the boolean flag that tells us the preferred Tibco Transport
   *  object to create.
   *
   * @param  useRVD  - pass true if system should create
   *      TibrvRvdTransport or false if TibrvRvaTransport objects should
   *      be created.
   */
  public static void setRVDPreferred(boolean useRVD)
  {
    EventUtils.msUseRVD = useRVD;
  }

  /**
   *  Get the prefix that should be applied to all subjects. This prefix
   *  includes a trailing "." character.
   *
   * @return    A string that should be prefixed to all tibrv subjects.
   */
  public static synchronized String getSubjectPrefix()
  {
    if (msSubjectPrefix == null)
    {
      String property = PropertiesConstants.SYSTEM_TIBCO + ".prefix";
      msSubjectPrefix = AppConfiguration.getProperty(property, "cisco.em.");
      if (!msSubjectPrefix.endsWith("."))
      {
        msSubjectPrefix += ".";
      }
    }
    return msSubjectPrefix;
  }

  /**
   *  Gets the logger attribute of the EventUtils class
   *
   * @return    The logger value
   */
  private static CiscoLogger getLogger()
  {
    if (msLogger == null)
    {
      msLogger = CiscoLogger.getCiscoLogger("tibcoEvents");
    }
    return msLogger;
  }

  /**
   *  Add a prefix to a subject.
   *
   * @param  subject  The subject that needs prefixing.
   * @return          The prefixed subject.
   */
  public static String addSubjectPrefix(String subject)
  {
    if (msSubjectPrefix == null)
    {
      getSubjectPrefix();
    }
    return msSubjectPrefix + subject;
  }

  /**
   *  Remove the prefix from the subject.
   *
   * @param  subject  The subject from which the prefix needs to be
   *      removed
   * @return          The subject without the prefix.
   */
  public static String removeSubjectPrefix(String subject)
  {
    if (msSubjectPrefix == null)
    {
      getSubjectPrefix();
    }
    if (subject == null || subject.trim().equals(""))
    {
      return subject;
    }
    if (subject.startsWith(msSubjectPrefix))
    {
      return subject.substring(msSubjectPrefix.length());
    }
    return subject;
  }

  /**
   *  Get the default transport for this process. The preferred method
   *  for determining the transport is the one below that takes a
   *  subject as a parameter.
   *
   * @param  service             Description of the Parameter
   * @param  network             Description of the Parameter
   * @param  daemon              Description of the Parameter
   * @return                     The default transport for this process.
   * @exception  TibrvException  Description of the Exception
   */
  public static synchronized TibrvTransport createDefaultTransport(String service,
      String network, String daemon)
       throws TibrvException
  {
    if (msTransport == null)
    {
      if (!EventUtils.isRVDPreferred())
      {
        EventUtils.createRvaTransport();
      }
      else
      {
        Tibrv.open(Tibrv.IMPL_NATIVE);
        debug("Opening default transport");

        if (service != null)
        {
          debug("Creating default transport for " + service + ":" + network + ":" + daemon);
          for (int i = 0; i < 5; ++i)
          {
            try
            {
              msTransport = new TibrvRvdTransport(service, network, daemon);
              if (msTransport != null)
              {
                break;
              }
            } catch (TibrvException tre)
            {
              if (i == 5)
              {
                debug("Failed to create transport");
                throw tre;
              }
              if (tre.error == TibrvStatus.DAEMON_NOT_CONNECTED)
              {
                try
                {
                  Thread.sleep(300);
                } catch (InterruptedException ex)
                {
                }
                debug("Could not connect to Tibrv Daemon. Trying again.");
                continue;
              }
              else
              {
                debug("Could not create transport");
                tre.printStackTrace();
                throw tre;
              }
            }
          }


          if (msTransport == null)
          {
            msTransportCreationFailed = true;
            throw new RuntimeException("Could bot create transport. The daemon (rvd) could be down");
          }
          else
          {

            debug("Created transport");
            msTransportCreationFailed = false;
            startDispatchThread();
          }
        }
        else
        {
          debug("Failed to create default transport");
          msTransportCreationFailed = true;
        }
      }
    }
    return msTransport;
  }

  /**
   *  Closes the default transport for this process and interrupts the
   *  dispatcher thread.
   *
   * @exception  TibrvException  Description of the Exception
   */
  public static synchronized void closeDefaultTransport()
       throws TibrvException
  {
    if (msTransport != null)
    {
      if (msDispatchThread != null)
      {
        msDispatchThread.terminate();
        msDispatchThread = null;
      }
      Tibrv.close();
      msTransport = null;
    }
  }

  /**
   *  Get the default transport for this process. The preferred method
   *  for determining the transport is the one below that takes a
   *  subject as a parameter.
   *
   * @return                     The default transport for this process.
   * @exception  TibrvException  Description of the Exception
   */
  public static synchronized TibrvTransport getDefaultTransport()
       throws TibrvException
  {
    if (msTransport == null && !msTransportCreationFailed)
    {
      if (EventUtils.isRVDPreferred())
      {
        EventUtils.createRvdTransport();
      }
      else
      {
        EventUtils.createRvaTransport();
      }
    }
    return msTransport;
  }


  /**
   *  Description of the Method
   *
   * @param  s  Description of the Parameter
   */
  public static void debug(String s)
  {
    if (msDebug)
    {
      System.err.println(s);
    }
  }

  /**
   * @return    Description of the Return Value
   */
  protected static void createRvdTransport() throws TibrvException
  {
    String prefix = PropertiesConstants.SYSTEM_TIBCO;
    String service = AppConfiguration.getProperty(prefix + ".port", null);
    if (service != null)
    {
      String network = AppConfiguration.getProperty(prefix + ".network", null);
      String daemon = AppConfiguration.getProperty(prefix + ".daemon", null);

      EventUtils.createDefaultTransport(service,
        network, daemon);
    }
    else
    {
      debug("Failed to create default transport - tibco port not found");
      msTransportCreationFailed = true;
    }
  }

  /**
   *  This method is called at boot time of the application. If the transport
   *  object can not be created the system logs a severe message stating the
   *  cause and shows a user error message dialog.
   */
  protected static void createRvaTransport()
  {
    if (EventUtils.msTransport == null)
    {
      try
      {
        String masterServer =
          AppConfiguration.getProperty("SYSTEM.masterServer");

        // when zero is passed to the constructor Tibco automatically chooses
        // the default port.

        int rvaPort = AppConfiguration.getIntProperty(
	  PropertiesConstants.SYSTEM_TIBCO + ".rva-port", 0);

        Tibrv.open(Tibrv.IMPL_JAVA);

        TibrvTransport tibrvTransport = new TibrvRvaTransport(masterServer,
            rvaPort,
            TibrvRvaTransport.HTTP_TUNNEL_ENABLE);

        Object params[] =
            {
            masterServer,
            new Integer(rvaPort),
            "HTTP Tunnelling Enabled"
            };

        if (!tibrvTransport.isValid())
        {
          EventUtils.getLogger().log(Level.SEVERE,
              "Created an invalid Tibco Transport connection to {0}, using port {1} with {2}",
              params);

          debug("Failed to create default transport");
          EventUtils.msTransportCreationFailed = true;

          tibrvTransport.destroy();
          tibrvTransport = null;
        }
        else
        {
          // sucess - store the transport object
          EventUtils.msTransportCreationFailed = false;
          EventUtils.msTransport = tibrvTransport;

          startDispatchThread();

          EventUtils.getLogger().log(Level.INFO,
              "Successfully created Tibco Transport connection to {0}, using port {1} with {2}",
              params);
        }

      } catch (Throwable rvEx)
      {
        debug("Failed to create default transport");
        EventUtils.msTransportCreationFailed = true;
      }
    }
  }


  /**
   *  Get the default transport for this process for a specified
   *  subject. This is the preferred way for code to identify the
   *  transport that should be used. Although the initial implementation
   *  always returns the same transport, this provides a single place to
   *  partition the subject space across multiple transports as a way of
   *  improving performance.
   *
   * @param  subject             The subject to get the transport for.
   *      Specifying the subject allows tye system to direct specific
   *      subjects to distinct transports.
   * @return                     The defaultTransport value
   * @exception  TibrvException  Description of the Exception
   */
  public static synchronized TibrvTransport getDefaultTransport(String subject)
       throws TibrvException
  {
    return getDefaultTransport();
  }

  /**
   *  Create a new listener, attached to the transport associated with
   *  the specified subject.
   *
   * @param  callback            The object to callback when messages
   *      arrive.
   * @param  subject             The subject to listen to.
   * @return                     Description of the Return Value
   * @exception  TibrvException  Description of the Exception
   */
  public static TibrvListener newListener(TibrvMsgCallback callback, String subject)
       throws TibrvException
  {
    return newListener(callback, subject, null);
  }

  /**
   *  Create a new listener, attached to the transport associated with
   *  the specified subject.
   *
   * @param  callback            The object to callback when messages
   *      arrive.
   * @param  subject             The subject to listen to.
   * @param  closure             A closure argument for the callback.
   * @return                     Description of the Return Value
   * @exception  TibrvException  Description of the Exception
   */
  public static TibrvListener newListener(TibrvMsgCallback callback,
      String subject,
      Object closure)
       throws TibrvException
  {
    getDefaultTransport();
    return new TibrvListener(Tibrv.defaultQueue(), callback,
        getDefaultTransport(subject), subject, closure);
  }

  /**
   *  Create a new interval timer.
   *
   * @param  callback            The object to callback when the timer
   *      triggers.
   * @param  interval            The interval (seconds) between
   *      callbacks
   * @return                     Description of the Return Value
   * @exception  TibrvException  Description of the Exception
   */
  public static TibrvTimer newTimer(TibrvTimerCallback callback, double interval)
       throws TibrvException
  {
    return newTimer(callback, interval, null);
  }

  /**
   *  Create a new interval timer.
   *
   * @param  callback            The object to callback when the timer
   *      triggers.
   * @param  interval            The interval (seconds) between
   *      callbacks
   * @param  closure             A closure argument for the callback.
   * @return                     Description of the Return Value
   * @exception  TibrvException  Description of the Exception
   */
  public static TibrvTimer newTimer(TibrvTimerCallback callback,
      double interval,
      Object closure)
       throws TibrvException
  {
    getDefaultTransport();
    return new TibrvTimer(Tibrv.defaultQueue(), callback, interval, null);
  }

  /**
   *  start the dispatch Thread
   */
  public static synchronized void startDispatchThread()
  {
    if (msDispatchThread == null)
    {
      msDispatchThread = new DispatchThread("Watchdog-Tibco-dispatcher");
      msDispatchThread.start();
      debug("Dispatch thread started for the default transport");
    }
  }

  /**
   *  The dispatch thread is used to deliver messages. An instance of
   *  this thread is created for the default transport.
   *
   * @author     pmadden
   * @created    May 20, 2002
   */
  static class DispatchThread extends Thread
  {

    private boolean mFinished = false;

    /**
     *  Create a new DispatchThread.
     *
     * @param  name  The name of the thread.
     */
    public DispatchThread(String name)
    {
      super(name);
      setDaemon(true);
    }

    /**
     *  Description of the Method
     */
    public void terminate()
    {
      mFinished = true;
      this.interrupt();
    }

    /**
     *  Run forever, delivering messages.
     */
    public void run()
    {
      int consecutiveFailures = 0;
      while (true)
      {
        if (mFinished)
        {
          break;
        }
        try
        {
          com.tibco.tibrv.TibrvQueue queue = Tibrv.defaultQueue();
          queue.dispatch();
          consecutiveFailures = 0;
          if (EventUtils.getLogger() != null)
          {
            EventUtils.getLogger().finest("Dispatched an event");
          }
        } catch (TibrvException e)
        {
          if (!mFinished)
          {
            if (EventUtils.getLogger() != null)
            {
              EventUtils.getLogger().warning("In dispatch", e);
            }

            if (++consecutiveFailures > 10)
            {
              try
              {
                sleep(1000);
              } catch (InterruptedException ie)
              {
                //ignore
              }
            }
          }
        } catch (InterruptedException ie)
        {
          //ignore
        }
      }
    }
  }

  /**
   *  Description of the Method
   *
   * @param  args           Description of the Parameter
   * @exception  Exception  Description of the Exception
   */
  public static void main(String args[])
       throws Exception
  {
    EventUtils.createDefaultTransport(args[0], null, null);
  }
}

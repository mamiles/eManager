package com.cisco.eManager.eManager.processSequencer.watchdog;

import com.tibco.tibrv.*;
import COM.TIBCO.hawk.console.hawkeye.*;
import COM.TIBCO.hawk.talon.*;
import com.cisco.eManager.eManager.util.GlobalProperties;
import com.cisco.eManager.eManager.processSequencer.common.logging.*;
import com.cisco.eManager.eManager.processSequencer.common.*;

import java.util.Properties;

public class WatchdogHawkConsole
    implements AgentMonitorListener, MicroAgentListMonitorListener, ErrorExceptionListener
{
    protected CiscoLogger mLogger;
    protected String mDomain = null;
    protected String mService = null;
    protected String mNetwork = null;
    protected String mDaemon = null;
    protected String emHome = null;
    protected TibrvQueue mRvQueue = null;
    protected TIBHawkConsole mConsole = null;
    protected AgentManager mAgentManager = null;
    protected AgentMonitor mAgentMonitor = null;
    protected String mAgentName = null;
    protected MicroAgentID mMaidRBE = null;
    private static WatchdogHawkConsole instance;
    final String RULEBASEENGINE_MICROAGENT_NAME = "COM.TIBCO.hawk.microagent.RuleBaseEngine";

    final String RULEDATA_OP = "COM.TIBCO.hawk.config.rbengine.rulebase.operators.RuleData";
    final String EQUALOBJ_OP = "COM.TIBCO.hawk.config.rbengine.rulebase.operators.EqualsObject";
    final String OR_OP = "COM.TIBCO.hawk.config.rbengine.rulebase.operators.Or";

    final int subscriptionInterval = 15 * 1000;

    public static WatchdogHawkConsole instance() {
        if (instance == null)
        {
            instance = new WatchdogHawkConsole();
        }
        return instance;
    }

    private WatchdogHawkConsole()
    {
        mLogger = CiscoLogger.getCiscoLogger("watchdog");
        mLogger.info("WatchdogHawkConsole starting up now");
        setupConfig();
        if (mAgentName == null)
        {
            try
            {
                java.net.InetAddress hostInetInfo = java.net.InetAddress.getLocalHost();
                mAgentName = hostInetInfo.getHostName();
            }
            catch (java.net.UnknownHostException uhe)
            {
                mLogger.severe("Unable to retrieve host name.\n" + uhe.toString());
            }
        }
        try
        {

            // setup console
            mConsole = new TIBHawkConsole(mDomain, mService, mNetwork, mDaemon);

            mAgentMonitor = mConsole.getAgentMonitor();
            mAgentManager = mConsole.getAgentManager();

            // create and add listener for console errors
            mAgentMonitor.addErrorExceptionListener(this);

            // create and add listener for agents
            mAgentMonitor.addAgentMonitorListener(this);

            // create and add listener for microagents
            mAgentMonitor.addMicroAgentListMonitorListener(this);

        }
        catch (java.util.TooManyListenersException e)
        {
            mLogger.severe("Too many Listeners in Hawk console:" + e.toString());
            e.printStackTrace();
        }

        try
        {
            mAgentMonitor.initialize();
            mAgentManager.initialize();
        }
        catch (ConsoleInitializationException e)
        {
            mLogger.severe("Unable to initilize Hawk console: " + e.toString());
            e.printStackTrace();
        }

    }

    private void setupConfig()
    {
        mService = DCPLib.getSystemProperty("tibhawk.service", "7474");
        mLogger.finest("TibHawk UDP Service: " + mService);
        mNetwork = DCPLib.getSystemProperty("tibhawk.network", null);
        mLogger.finest(
        "TibHawk network to use for outbound session communications: " +
        mNetwork);
        mDaemon = DCPLib.getSystemProperty("tibhawk.daemon", "tcp:7474");
        mLogger.finest(
        "TIBCO Rendezvous daemon to handle communication for the session: " +
        mDaemon);
        mDomain = DCPLib.getSystemProperty("tibhawk.domain", "default");
        mLogger.finest("TibHawk Domain on which the console is to communicate: " + mDomain);
        emHome = System.getProperty("em.home");
        mLogger.finest("eManager Home directory (EM_HOME):" + emHome);
    }

    final void cleanUp()
    {
        mAgentMonitor.removeErrorExceptionListener(this);
        mAgentMonitor.removeAgentMonitorListener(this);
        mAgentMonitor.removeMicroAgentListMonitorListener(this);

        mAgentMonitor.shutdown();
        mAgentMonitor.shutdown();
    }

    /**
     *  Retrieve the agent name.
     */
    public String getAgentName()
    {
        return mAgentName;
    }

    /**
     *  Retrieve the rulebase microagent ID.
     */
    public MicroAgentID getRulebaseEngineMicroagentID()
    {
        return mMaidRBE;
    }

    /**
     * Invoke a MethodInvocation an a given microagent ID.
     */
    public MicroAgentData invoke(MicroAgentID maid, MethodInvocation mi)
        throws MicroAgentException
    {
        //AgentManager am = mConsole.getAgentManager();
        return (mAgentManager.invoke(maid, mi));
    }

    /**
     * Save rulebase engine or spot microagent IDs when they are deteceted.
     */
    public void addMicroagent(MicroAgentID mID)
    {
        mLogger.fine("addMicroagent:  " + mID.getAgent().getName() + ":" + mID.getName());
        if (mID.getName().equals(RULEBASEENGINE_MICROAGENT_NAME))
        {
            mMaidRBE = mID;
        }
    }

    /**
     * Remove rulebase engine or spot microagent IDs when they are expired.
     */
    public void removeMicroagent(MicroAgentID mID)
    {
        mLogger.fine("removeMicroagent:  " + mID.getAgent().getName() + ":" + mID.getName());
        if (mID.getName().equals(RULEBASEENGINE_MICROAGENT_NAME))
        {
            mMaidRBE = null;
        }
    }

    /**
     * Process event when an agent is discovered.
     */
    public synchronized void onAgentAlive(AgentMonitorEvent event)
    {
        AgentInstance agntInst = event.getAgentInstance();
        //log("onAgentAlive:  " + agntInst.getAgentID().getName()+":"+ agntInst.getAgentID().getHawkDomain());
        if (!agntInst.getAgentID().getName().equals(mAgentName))
        {
            return;
        }
        mLogger.fine("Found agent: " + mAgentName);
        MicroAgentID[] mAgentIDs = agntInst.getStatusMicroAgents();
        for (int i = 0; i < mAgentIDs.length; i++)
        {
            addMicroagent(mAgentIDs[i]);
        }
    }

    /**
     * Process event when an agent have expired.
     */
    public synchronized void onAgentExpired(AgentMonitorEvent event)
    {
        AgentInstance agntInst = event.getAgentInstance();
        //log("onAgentExpired:  " + agntInst.getAgentID().getName()+":"+ agntInst.getAgentID().getHawkDomain());
        if (!agntInst.getAgentID().getName().equals(mAgentName))
        {
            return;
        }
        mLogger.fine("Loosing agent: " + mAgentName);
        MicroAgentID[] mIDs = agntInst.getStatusMicroAgents();
        for (int i = 0; i < mIDs.length; i++)
        {
            removeMicroagent(mIDs[i]);
        }
    }

    /**
     * Process event when a microAgent is added.
     */
    public synchronized void onMicroAgentAdded(MicroAgentListMonitorEvent event)
    {
        MicroAgentID mID = event.getMicroAgentID();
        addMicroagent(mID);
    }

    /**
     * Process event when a microAgent is removed.
     */
    public synchronized void onMicroAgentRemoved(MicroAgentListMonitorEvent event)
    {
        MicroAgentID mID = event.getMicroAgentID();
        removeMicroagent(mID);
    }

    /**
     * Process event when error is signaled.
     */
    public synchronized void onErrorExceptionEvent(ErrorExceptionEvent event)
    {
        mLogger.fine("onErrorExceptionEvent: event=" + event);
    }

}

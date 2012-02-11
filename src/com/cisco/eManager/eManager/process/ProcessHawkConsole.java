package com.cisco.eManager.eManager.process;
import com.tibco.tibrv.*;
import COM.TIBCO.hawk.console.hawkeye.*;
import COM.TIBCO.hawk.talon.*;
import org.apache.log4j.Logger;
import java.util.Properties;
import com.cisco.eManager.eManager.util.GlobalProperties;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class ProcessHawkConsole
    implements AgentMonitorListener, MicroAgentListMonitorListener, ErrorExceptionListener
{
    private static Logger mLogger = Logger.getLogger(ProcessHawkConsole.class);
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
    protected MicroAgentID mMaidProcessSeq = null;
    protected Hashtable mMaidWatchdog = new Hashtable();
    private static ProcessHawkConsole instance;
    public static final String WATCHDOG_MICRO_AGENT_NAME = "com.cisco.eManager.eManager.processSequencer.watchdog.WD-";
    public static final String PROCESS_MICRO_AGENT_NAME = "com.cisco.eManager.eManager.processSequencer.processSequencer";


    public static ProcessHawkConsole instance() {
        if (instance == null)
        {
            instance = new ProcessHawkConsole();
        }
        return instance;
    }

    private ProcessHawkConsole()
    {
        mLogger.info("ProcessHawkConsole starting up now");
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
                mLogger.error("Unable to retrieve host name.\n" + uhe.toString());
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
            mLogger.error("Too many Listeners in Hawk console:" + e.toString());
            e.printStackTrace();
        }

        try
        {
            mAgentMonitor.initialize();
            mAgentManager.initialize();
        }
        catch (ConsoleInitializationException e)
        {
            mLogger.error("Unable to initilize Hawk console: " + e.toString());
            e.printStackTrace();
        }

    }

    private void setupConfig()
    {
        GlobalProperties globalProp = GlobalProperties.instance();
        Properties emProp = globalProp.getProperties();

        mService = emProp.getProperty("SYSTEM.tibhawk.service", "7474");
        mLogger.debug("TibHawk UDP Service: " + mService);
        mNetwork = emProp.getProperty("SYSTEM.tibhawk.network", null);
        mLogger.debug(
            "TibHawk network to use for outbound session communications: " +
            mNetwork);
        mDaemon = emProp.getProperty("SYSTEM.tibhawk.daemon", "tcp:7474");
        mLogger.debug(
            "TIBCO Rendezvous daemon to handle communication for the session: " +
            mDaemon);
        mDomain = emProp.getProperty("SYSTEM.tibhawk.domain", "default");
        mLogger.debug("TibHawk Domain on which the console is to communicate: " + mDomain);
        emHome = System.getProperty("EMANAGER_ROOT");
        mLogger.debug("eManager Home directory (EMANAGER_ROOT):" + emHome);
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
     *  Retrieve the Process Sequencer microagent ID.
     */
    public MicroAgentID getProcessMicroAgentID()
    {
        return mMaidProcessSeq;
    }

    public MicroAgentID getWatchdogMicroAgentID(String appType, String appInst) {

        MicroAgentID maidWatchdog = (MicroAgentID)mMaidWatchdog.get(appType + "-" + appInst);
        if (maidWatchdog == null) {
            mLogger.debug("Could not find Watchdog Micro Agent for appType: " + appType + "-" + appInst);
        }

        return maidWatchdog;
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
        mLogger.debug("addMicroagent:  " + mID.getAgent().getName() + ":" + mID.getName());
        if (mID.getName().equals(PROCESS_MICRO_AGENT_NAME))
        {
            mMaidProcessSeq = mID;
        }
        if (mID.getName().startsWith(WATCHDOG_MICRO_AGENT_NAME))
        {
            StringTokenizer st = new StringTokenizer(mID.getName(), "-");
            if (st.countTokens() == 3) {
                String xx = st.nextToken();
                String appType = st.nextToken();
                String appInst = st.nextToken();
                mLogger.debug("Adding MicroAgent to hashtable.  appType: " + appType + " appInst: " + appInst);
                mMaidWatchdog.put(appType + "-" + appInst, mID);
            }
        }
    }

    /**
     * Remove rulebase engine or spot microagent IDs when they are expired.
     */
    public void removeMicroagent(MicroAgentID mID)
    {
        mLogger.debug("removeMicroagent:  " + mID.getAgent().getName() + ":" + mID.getName());
        if (mID.getName().equals(PROCESS_MICRO_AGENT_NAME))
        {
            mMaidProcessSeq = null;
        }
        if (mID.getName().startsWith(WATCHDOG_MICRO_AGENT_NAME))
        {
            StringTokenizer st = new StringTokenizer(mID.getName(), "-");
            if (st.countTokens() == 3) {
                String xx = st.nextToken();
                String appType = st.nextToken();
                String appInst = st.nextToken();
                mLogger.debug("Removeing MicroAgent from hashtable.  appType: " + appType + " appInst: " + appInst);
                mMaidWatchdog.remove(appType + "-" + appInst);
            }
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
        mLogger.debug("Found agent: " + mAgentName);
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
        mLogger.debug("Loosing agent: " + mAgentName);
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
        mLogger.debug("onErrorExceptionEvent: event=" + event);
    }

}

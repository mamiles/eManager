//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\AgentListener.java

package com.cisco.eManager.eManager.network;

import java.util.*;
import org.apache.log4j.*;
import COM.TIBCO.hawk.console.hawkeye.*;
import COM.TIBCO.hawk.talon.*;
import com.cisco.eManager.common.event.*;
import com.cisco.eManager.common.database.*;
import com.cisco.eManager.eManager.database.*;
import com.cisco.eManager.eManager.event.*;
import com.cisco.eManager.eManager.inventory.instrumentation.*;
import com.cisco.eManager.eManager.inventory.network.*;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.appType.*;
import com.cisco.eManager.eManager.inventory.host.*;

import com.cisco.eManager.eManager.util.GlobalProperties;
import com.cisco.eManager.eManager.util.WatchdogRulebaseNameHelper;
import com.cisco.eManager.eManager.util.WatchdogRulebaseNameHelper.WatchdogRulebaseNameComponents;

public class AgentListener extends Thread
{
    private String m_domain = null;
    private String m_service = null;
    private String m_network = null;
    private String m_daemon = null;
    private static Logger logger = Logger.getLogger(AgentListener.class);
    private TIBHawkConsole m_console = null;
    private AgentManager m_agentManager = null;
    private AgentMonitor m_agentMonitor = null;

    /**
     * @param domain
     * @param service
     * @param network
     * @param daemon
     * @throws java.lang.Exception
     * @throws Exception
     * @roseuid 3F4D092B007F
     */
    public AgentListener() throws Exception
    {
        super("agent listener thread");
        logger.debug("enter");
        GlobalProperties gp = GlobalProperties.instance();
        m_domain =
            gp.getProperties().getProperty("SYSTEM.tibhawk.domain", "default");
        logger.debug("using domain " + m_domain);
        m_service =
            gp.getProperties().getProperty("SYSTEM.tibhawk.service", "7474");
        logger.debug("using service " + m_service);
        m_network =
            gp.getProperties().getProperty("SYSTEM.tibhawk.network", "null");
        logger.debug("using network " + m_network);
        m_daemon =
            gp.getProperties().getProperty("SYSTEM.tibhawk.daemon", "tcp:7474");
        logger.debug("using daemon " + m_daemon);
        m_console =
            new TIBHawkConsole(m_domain, m_service, m_network, m_daemon);
        m_agentManager = m_console.getAgentManager();
        m_agentMonitor = m_console.getAgentMonitor();

        // Reference the event message queues so we can get threads
        // up and running.
        EventMessageQueue.instance();
        SyncMessageQueue.instance();
        try {
            DatabaseInterface.instance();
        }
        catch (EmanagerDatabaseException e) {
            // fix
            // probably exit here
        }
    }

    /**
     * @param event
     * @roseuid 3F58D889031B
     */
    private void agentAlive(AgentMonitorEvent event)
    {
        logger.debug("event = " + toString(event));
        AddAgent msg = new AddAgent(event, m_agentManager);
        MsgHandler.instance().handleMessage(msg);
    }

    /**
     * @param event
     * @roseuid 3F58D88C02DA
     */
    private void agentExpired(AgentMonitorEvent event)
    {
        logger.debug("event = " + toString(event));
        RemoveAgent msg = new RemoveAgent(event);
        MsgHandler.instance().handleMessage(msg);
    }

    /**
     * @param event
     * @roseuid 3F58D88D03DF
     */
    private void addMicroAgent(MicroAgentListMonitorEvent event)
    {
        logger.debug("event = " + toString(event));
        AddMicroAgent msg = new AddMicroAgent(event);
        MsgHandler.instance().handleMessage(msg);
    }

    /**
     * @param event
     * @roseuid 3F58D891036D
     */
    private void removeMicroAgent(MicroAgentListMonitorEvent event)
    {
        logger.debug("event = " + toString(event));
        RemoveMicroAgent msg = new RemoveMicroAgent(event);
        MsgHandler.instance().handleMessage(msg);
    }

    /**
     * @param event
     * @roseuid 3F58D894003C
     */
    private void addRulebase(RuleBaseListMonitorEvent event)
    {
        logger.debug("event = " + toString(event));
        AddMgmtPolicy msg = new AddMgmtPolicy(event);
        MsgHandler.instance().handleMessage(msg);
    }

    /**
     * @param event
     * @roseuid 3F58D8960220
     */
    private void removeRulebase(RuleBaseListMonitorEvent event)
    {
        logger.debug("event = " + toString(event));
        RemoveMgmtpolicy msg = new RemoveMgmtpolicy(event);
        MsgHandler.instance().handleMessage(msg);
    }

    /**
     * @param event
     * @param eventType
     * @roseuid 3F58D8970037
     */
    private void alertEvent(AlertMonitorEvent event, EventType eventType)
    {
        logger.debug("event = " + toString(event));
        logger.debug("eventType = " + eventType.intValue());

        AppInstance appInstance;
	RuleBaseStatus rulebaseStatus;
	WatchdogRulebaseNameComponents rulebaseNameComponents;

        AgentId agentId = new AgentId(event.getAgentInstance().getAgentID());

	// First, let's check to see if this event is related to the
	// watchdog rulebase;
        appInstance = null;
        rulebaseNameComponents = null;
        rulebaseStatus = event.getRuleBaseStatus();
        if (rulebaseStatus != null) {
	    rulebaseNameComponents = WatchdogRulebaseNameHelper.instance().parseWatchdogRulebaseName(rulebaseStatus.getName());
	    if (rulebaseNameComponents != null) {
		Host host;
		AppType appType;

		host = HostManager.instance().find (agentId.name());
		if (host != null) {
		    appType = AppTypeManager.instance().find (rulebaseNameComponents.appTypeName);
		    if (appType != null) {
                        List appInstances;
			appInstances = AppInstanceManager.instance().find(host.id(), appType.id());
                        if (appInstances != null &&
                            appInstances.size() > 0) {
                            // Assume only single app instance per host
                            appInstance = (AppInstance) appInstances.get(0);
                        }
		    }
		}
	    }
	}

	if (rulebaseNameComponents != null &&
	    appInstance == null) {
	    // We can't process this event.  some error.  We need the app instance.
	    return;
	}

        Date eventTime = new Date(event.getTimeGenerated());
        EventSeverity eventSeverity = null;
        int eventId = (int)event.getAlertID();
        ManagementPolicyId managementPolicyId = null;
        MicroagentId microagentId = null;
        String rule = null;
        String test = null;
        String displayText = null;

        if (event instanceof PostAlertEvent)
        {
            PostAlertEvent paEvent = (PostAlertEvent)event;
            displayText = paEvent.getAlertText();
            logger.debug("displayText = " + displayText);
            switch (paEvent.getAlertState())
            {
                case AlertState.NO_ALERT:
                    eventSeverity =EventSeverity.Informational;
                    break;
                case AlertState.ALERT_LOW:
                    eventSeverity = EventSeverity.Low;
                    break;
                case AlertState.ALERT_MEDIUM:
                    eventSeverity = EventSeverity.Medium;
                    break;
                case AlertState.ALERT_HIGH:
                    eventSeverity = EventSeverity.High;
                    break;
                default:
                    eventSeverity = EventSeverity.Informational;
                    break;
            }
            Properties properties = paEvent.getProperties();
            rule = properties.getProperty("Rule", null);
            test = properties.getProperty("Test", null);
            managementPolicyId =
                new ManagementPolicyId (agentId,
                                        paEvent.getRuleBaseStatus().getName());
            String dataSource =
                properties.getProperty("DataSource", null);
            if ( dataSource == null )
            {
                logger.warn("discarding event due to null datasource");
                return;
            }

            microagentId = dataSourceToMicroagentId(agentId, dataSource);
        }
        else if (event instanceof ClearAlertEvent)
        {
            ClearAlertEvent caEvent = (ClearAlertEvent)event;
            eventSeverity = EventSeverity.Informational;

            displayText = caEvent.getReasonClearedText();
            rule = null;
            test = null;
            managementPolicyId =
                new ManagementPolicyId (agentId,
                                        caEvent.getRuleBaseStatus().getName());
        }

        TibcoEventMessage tibEvent;
	if (rulebaseNameComponents != null) {
	    // watchdog rulebase generated event
	    tibEvent = new TibcoEventMessage(eventType,
                                             eventTime,
                                             eventSeverity,
                                             eventId,
                                             null,
                                             microagentId,
                                             rule,
                                             test,
                                             displayText);
	    tibEvent.setManagedObjectId (appInstance.id());
	} else {
	    tibEvent = new TibcoEventMessage(eventType,
                                             eventTime,
                                             eventSeverity,
                                             eventId,
                                             managementPolicyId,
                                             microagentId,
                                             rule,
                                             test,
                                             displayText);
	}

        EventMessageQueue.instance().queueMessage(tibEvent);
    }

    /**
     * @param agentId
     * @param dataSource
     * @return com.cisco.eManager.eManager.network.MicroagentId
     * @roseuid 3F5CF90D0094
     */
    private MicroagentId dataSourceToMicroagentId(AgentId agentId, String dataSource)
    {
        Instrumentation instrumentation;
        MicroagentId uAgentId;
        StringTokenizer st;

       st = new StringTokenizer(dataSource, ":");
        if (st.countTokens() != 2)
        {
            logger.error("event datasource: " + dataSource +
                         " is not in the form of microagentname:instance");
            return null;
        }
        String microAgentName = st.nextToken();
        Integer instanceInt = new Integer(st.nextToken());

        uAgentId = null;
        // First attempt will be to look it up locally
        instrumentation =
            InstrumentationManager.instance().find(agentId,
                                                   microAgentName,
                                                   instanceInt.toString().trim());
        if (instrumentation != null) {
            uAgentId = instrumentation.networkId();
        } else {
            MicroAgentID[] uAgentIds = null;

            try{
                uAgentIds = m_agentManager.getMicroAgentIDs(microAgentName, 1);
                if (uAgentIds.length != 0) {
                    uAgentId = new MicroagentId(agentId.raw(), uAgentIds[0]);
                }
            }
            catch (Exception e){
                logger.error("exception caught trying to obtain MicroAgentID: " + e);
            }
        }

        return uAgentId;
    }

    /**
     * @roseuid 3F60C7340025
     */
    public void run()
    {
        logger.debug("enter");
        try
        {
            m_agentMonitor.addAgentMonitorListener(new AgentMonitorListener()
            {
                public void onAgentAlive(AgentMonitorEvent event)
                {
                    logger.debug("*** onAgentAlive ***");
                    agentAlive(event);
                }

                public void onAgentExpired(AgentMonitorEvent event)
                {
                    logger.debug("*** onAgentExpired ***");
                    agentExpired(event);
                }
            }
            );

            m_agentMonitor.addMicroAgentListMonitorListener(
                new MicroAgentListMonitorListener()
            {
                public void onMicroAgentAdded(
                    MicroAgentListMonitorEvent event)
                {
                    logger.debug("*** onMicroAgentAdded ***");
                    addMicroAgent(event);
                }

                public void onMicroAgentRemoved(
                    MicroAgentListMonitorEvent event)
                {
                    logger.debug("*** onMicroAgentRemoved ***");
                    removeMicroAgent(event);
                }
            }
            );

            m_agentMonitor.addRuleBaseListMonitorListener(
                new RuleBaseListMonitorListener()
            {
                public void onRuleBaseAdded(RuleBaseListMonitorEvent event)
                {
                    logger.debug("*** onRuleBaseAdded ***");
                    addRulebase(event);
                }

                public void onRuleBaseRemoved(
                    RuleBaseListMonitorEvent event)
                {
                    logger.debug("*** onRuleBaseRemoved ***");
                    removeRulebase(event);
                }
            }
            );

            m_agentMonitor.addAlertMonitorListener(new AlertMonitorListener()
            {
                public void onRetransmittedAlert(AlertMonitorEvent event)
                {
                    logger.debug("*** onRestransmittedAlert ***");
                    alertEvent(event, EventType.RetransmitType);
                }

                public void onAlertMonitorEvent(AlertMonitorEvent event)
                {
                    if (event instanceof PostAlertEvent)
                    {
                        logger.debug("*** PostAlertEvent ***");
                        alertEvent(event, EventType.PostType);
                    }
                    else if (event instanceof ClearAlertEvent)
                    {
                        logger.debug("*** ClearAlertEvent ***");
                        alertEvent(event, EventType.ClearType);
                    }
                }
            });
        }
        catch (java.util.TooManyListenersException e)
        {
            e.printStackTrace();
            logger.debug("Catch Exception: " + e);
            //throw e;
        }

        try
        {
            m_agentManager.initialize();
            m_agentMonitor.initialize();
        }
        catch (ConsoleInitializationException e)
        {
            e.printStackTrace();
            logger.debug("Catch Exception: " + e);
            //throw e;
        }
    }

    private String strElement(String name, String value)
    {
        return "{" + name + " = " + value + "}";
    }

    private String strElement(String name, int value)
    {
        Integer iValue = new Integer(value);
        return strElement(name, iValue.toString());
    }

    private String strElement(String name, long value)
    {
        Long lValue = new Long(value);
        return strElement(name, lValue.toString());
    }

    private String toString(AgentMonitorEvent event)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strElement("AgentInstance",
                             toString(event.getAgentInstance())));
        return sb.toString();
    }

    private String toString(MicroAgentListMonitorEvent event)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strElement("AgentInstance",
                             toString(event.getAgentInstance())));
        sb.append(strElement("AgentInstance",
                             toString(event.getMicroAgentID())));
        return sb.toString();
    }

    private String toString(RuleBaseListMonitorEvent event)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strElement("AgentInstance",
                             toString(event.getAgentInstance())));
        sb.append(strElement("RuleBaseStatus",
                             toString(event.getRuleBaseStatus())));
        return sb.toString();
    }

    private String toString(AlertMonitorEvent event)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strElement("AgentInstance",
                             toString(event.getAgentInstance())));
        sb.append(strElement("AlertID", event.getAlertID()));
        sb.append(strElement("RuleBaseEngineState", event.getRuleBaseEngineState()));
        sb.append(strElement("RuleBaseStatus", toString(event.getRuleBaseStatus())));
        sb.append(strElement("TimeGenerated", event.getTimeGenerated()));
        return sb.toString();
    }

    private String toString(AgentInstance ai)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strElement("AgentID", toString(ai.getAgentID())));
        sb.append(strElement("AgentPlatform", toString(ai.getAgentPlatform())));
        sb.append(strElement("AgentVersion", toString(ai.getAgentVersion())));
        sb.append(strElement("Cluster", ai.getCluster()));
        sb.append(strElement("IPAddress", ai.getIPAddress()));
        return sb.toString();
    }

    private String toString(AgentID ai)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strElement("Dns", ai.getDns()));
        sb.append(strElement("HawkDomain", ai.getHawkDomain()));
        sb.append(strElement("Name", ai.getName()));
        return sb.toString();
    }

    private String toString(AgentPlatform ap)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strElement("OsArch", ap.getOsArch()));
        sb.append(strElement("OsName", ap.getOsName()));
        sb.append(strElement("OsVersion", ap.getOsVersion()));
        return sb.toString();
    }

    private String toString(AgentVersion av)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strElement("MajorVersion", av.getMajorVersion()));
        sb.append(strElement("MinorVersion", av.getMinorVersion()));
        sb.append(strElement("UpdateVersion", av.getUpdateVersion()));
        return sb.toString();
    }

    private String toString(MicroAgentID uai)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strElement("Agent", toString(uai.getAgent())));
        sb.append(strElement("DisplayName", uai.getDisplayName()));
        sb.append(strElement("Instance", uai.getInstance()));
        sb.append(strElement("Name", uai.getName()));
        return sb.toString();
    }

    private String toString(Agent a)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strElement("Dns", a.getDns()));
        sb.append(strElement("HawkDomain", a.getHawkDomain()));
        sb.append(strElement("Name", a.getName()));
        return sb.toString();
    }

    private String toString(RuleBaseStatus rbs)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strElement("Name", rbs.getName()));
        sb.append(strElement("State", rbs.getState()));
        return sb.toString();
    }
}

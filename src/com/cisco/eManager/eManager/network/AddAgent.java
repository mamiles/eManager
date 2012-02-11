//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\AddAgent.java

package com.cisco.eManager.eManager.network;

import COM.TIBCO.hawk.console.hawkeye.*;

/**
 * @author wstubb
 * @version 1.0
 */
public class AddAgent extends AgentMessage
{
    private AgentMonitorEvent m_agentMonitorEvent = null;
    private AgentManager m_agentManager = null;

    /**
     * @param msg
     * @roseuid 3F5D00670304
     */
    public AddAgent(AgentMonitorEvent msg, AgentManager am)
    {
        super(msg);
        m_agentMonitorEvent = msg;
        m_agentManager = am;
    }

    public AgentManager agentManager()
    {
        return m_agentManager;
    }
}

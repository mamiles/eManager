//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\RemoveAgent.java

package com.cisco.eManager.eManager.network;

import COM.TIBCO.hawk.console.hawkeye.*;

/**
 * @author wstubb
 * @version 1.0
 */
public class RemoveAgent extends AgentMessage 
{
    private AgentMonitorEvent m_agentMonitorEvent;
    
    /**
     * @param msg
     * @roseuid 3F5D00A60067
     */
    public RemoveAgent(AgentMonitorEvent msg) 
    {
        super(msg);
        m_agentMonitorEvent = msg;     
    }
}

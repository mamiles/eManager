//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\AddMicroAgent.java

package com.cisco.eManager.eManager.network;

import COM.TIBCO.hawk.console.hawkeye.*;

/**
 * @author wstubb
 * @version 1.0
 */
public class AddMicroAgent extends AgentMessage
{
    private MicroAgentListMonitorEvent m_instrumentationEvent;
    private MicroagentId m_instrumentationId;

    /**
     * @param msg
     * @roseuid 3F5D007B0227
     */
    public AddMicroAgent(MicroAgentListMonitorEvent msg)
    {
        super(msg);
        m_instrumentationEvent = msg;
        m_instrumentationId =
            new MicroagentId(super.agentId().raw(), msg.getMicroAgentID());
    }

    /**
     * @return com.cisco.eManager.eManager.network.MicroagentId
     * @roseuid 3F60DEB80176
     */
    public MicroagentId instrumentationId()
    {
        return m_instrumentationId;
    }
}

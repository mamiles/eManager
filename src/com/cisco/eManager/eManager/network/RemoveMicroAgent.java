//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\RemoveMicroAgent.java

package com.cisco.eManager.eManager.network;

import COM.TIBCO.hawk.console.hawkeye.*;

/**
 * @author wstubb
 * @version 1.0
 */
public class RemoveMicroAgent extends AgentMessage
{
    private MicroAgentListMonitorEvent m_instrumentationEvent;
    private MicroagentId m_instrumentationId;

    /**
     * @param msg
     * @roseuid 3F5D00A9029C
     */
    public RemoveMicroAgent(MicroAgentListMonitorEvent msg)
    {
        super(msg);
        m_instrumentationEvent = msg;
    }

    /**
     * @return com.cisco.eManager.eManager.network.MicroagentId
     * @roseuid 3F60E24102DE
     */
    public MicroagentId instrumentationId()
    {
        return new MicroagentId(agentId().raw(),
                                m_instrumentationEvent.getMicroAgentID());
    }
}

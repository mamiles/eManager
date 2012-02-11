//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\MicroagentId.java

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\MicroagentId.java

package com.cisco.eManager.eManager.network;

import org.apache.log4j.Logger;
import COM.TIBCO.hawk.talon.*;
import COM.TIBCO.hawk.console.hawkeye.*;

public class MicroagentId
{
    private static Logger logger = Logger.getLogger(MicroagentId.class);
    private AgentId m_agentId;
    private MicroAgentID m_uAgentId;

    public MicroagentId(AgentID agentId, MicroAgentID uAgentId)
    {
        m_uAgentId = uAgentId;
        m_agentId = new AgentId(agentId);
    }

    public MicroAgentID raw()
    {
        return m_uAgentId;
    }

    /**
     * @return java.lang.String
     * @roseuid 3F5CF92A00AA
     */
    public String name()
    {
        return m_uAgentId.getName();
    }

    /**
     * @return java.lang.String
     * @roseuid 3F5CF92C003F
     */
    public String displayName()
    {
        return m_uAgentId.getDisplayName();
    }

    /**
     * @return java.lang.String
     * @roseuid 3F5CF92D0036
     */
    public String instanceId()
    {
        return m_uAgentId.getInstance();
    }

    /**
     * @return com.cisco.eManager.eManager.network.AgentId
     * @roseuid 3F5CF92E0132
     */
    public AgentId agentId()
    {
        return m_agentId;
    }

    public boolean equals (Object object)
    {
        if (object instanceof MicroagentId) {
            MicroagentId uagentId;

            uagentId = (MicroagentId) object;
            if (m_agentId.equals(uagentId.agentId()) == true) {
                if (m_uAgentId.equals(uagentId.raw()) == true) {
                    return true;
                }
            }
        }

        return false;
    }
}

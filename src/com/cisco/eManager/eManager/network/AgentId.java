//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\AgentId.java

package com.cisco.eManager.eManager.network;

import org.apache.log4j.Logger;
import COM.TIBCO.hawk.console.hawkeye.*;

public class AgentId
{
    private static Logger logger = Logger.getLogger(AgentId.class);
    private AgentID m_agentId;

    public AgentId(AgentID agentId)
    {
        m_agentId = agentId;
    }

    /**
     * Access method for the name property.
     * @return   the current value of the name property
     * @roseuid 3F5CF8CC01CF
     */
    public String name()
    {
        return m_agentId.getName();
    }

    /**
     * Access method for the tibhawkDomain property.
     * @return   the current value of the tibhawkDomain property
     * @roseuid 3F5CF8CD00CC
     */
    public String tibhawkDomain()
    {
        return m_agentId.getHawkDomain();
    }

    /**
     * Access method for the dns property.
     * @return   the current value of the dns property
     * @roseuid 3F5CF8CE001A
     */
    public String dns()
    {
        return m_agentId.getDns();
    }

    public int hashCode()
    {
        return m_agentId.hashCode();
    }

    public boolean equals(AgentId agentId)
    {
        return m_agentId.equals(agentId.m_agentId);
    }

    public AgentID raw()
    {
        return m_agentId;
    }
}

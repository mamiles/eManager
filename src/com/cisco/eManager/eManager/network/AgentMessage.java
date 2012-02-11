//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\AgentMessage.java

package com.cisco.eManager.eManager.network;

import org.apache.log4j.Logger;
import COM.TIBCO.hawk.console.hawkeye.*;

public class AgentMessage
{
    private static Logger logger = Logger.getLogger(AgentMessage.class);
    private MonitorEvent m_event;
    private AgentId m_agentId;

    /**
     * @param event
     * @roseuid 3F4D092C01AD
     */
    public AgentMessage(MonitorEvent event)
    {
        logger.debug("enter");
        m_event = event;

        // set the AgentId
        AgentId aId = null;
        AgentInstance ai = m_event.getAgentInstance();
        AgentID aID = ai.getAgentID();
        m_agentId = new AgentId(aID);
    }

    /**
     * @return com.cisco.eManager.eManager.network.AgentId
     * @roseuid 3F60C73600BE
     */
    public AgentId agentId()
    {
        logger.debug("enter");
        return m_agentId;
    }

    public AgentInstance agentInstance()
    {
        logger.debug("enter");
        return m_event.getAgentInstance();
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof AgentMessage)
        {
            AgentMessage am = (AgentMessage)obj;
            return m_event.equals(am.m_event);
        }
        else
        {
            return false;
        }
    }
}

//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\AddMgmtPolicy.java

package com.cisco.eManager.eManager.network;

import COM.TIBCO.hawk.console.hawkeye.*;
import org.apache.log4j.Logger;

/**
 * @author wstubb
 * @version 1.0
 */
public class AddMgmtPolicy extends AgentMessage 
{
    private String m_name;
    private RuleBaseListMonitorEvent m_mgmtPolicyEvent;
    private static Logger logger = Logger.getLogger(AddMgmtPolicy.class);
    private ManagementPolicyId m_mgmtPolicyId;
    
    /**
     * @param msg
     * @roseuid 3F5D0077004B
     */
    public AddMgmtPolicy(RuleBaseListMonitorEvent msg) 
    {
        super(msg);
        logger.debug("enter");
        m_mgmtPolicyEvent = msg;
        m_name = msg.getRuleBaseStatus().getName();     
    }
    
    /**
     * @return com.cisco.eManager.eManager.network.ManagementPolicyId
     * @roseuid 3F60C72C016E
     */
    public ManagementPolicyId mgmtPolicyId() 
    {
        logger.debug("enter");
        return new ManagementPolicyId(agentId(),m_name);     
    }
    
    /**
     * @return String
     * @roseuid 3F60C72C0290
     */
    public String name() 
    {
        return m_name;     
    }
}

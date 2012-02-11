//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\RemoveMgmtpolicy.java

package com.cisco.eManager.eManager.network;

import COM.TIBCO.hawk.console.hawkeye.RuleBaseListMonitorEvent;

/**
 * @author wstubb
 * @version 1.0
 */
public class RemoveMgmtpolicy extends AgentMessage
{
    private RuleBaseListMonitorEvent m_mgmtPolicyEvent;
    private ManagementPolicyId m_mgmtPolicyId;

    /**
     * @param msg
     * @roseuid 3F5D00A201AC
     */
    public RemoveMgmtpolicy(RuleBaseListMonitorEvent msg)
    {
        super(msg);
        m_mgmtPolicyEvent = msg;
    }

    /**
     * @return com.cisco.eManager.eManager.network.ManagementPolicyId
     * @roseuid 3F60E1D701D5
     */
    public ManagementPolicyId mgmtPolicyId()
    {
        return new ManagementPolicyId(
                               agentId(),
                               m_mgmtPolicyEvent.getRuleBaseStatus().getName());
    }
}
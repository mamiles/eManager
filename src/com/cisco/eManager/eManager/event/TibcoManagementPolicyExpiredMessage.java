//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\event\\TibcoManagementPolicyExpiredMessage.java

package com.cisco.eManager.eManager.event;

import com.cisco.eManager.eManager.network.AgentId;
import com.cisco.eManager.eManager.network.ManagementPolicyId;

public class TibcoManagementPolicyExpiredMessage
{
    private AgentId agentId;
    private ManagementPolicyId managementPolicyId;

    /**
     * @param hostAgentId
     * @param managementPolicyId
     * @roseuid 3F4E5FC50129
     */
    public TibcoManagementPolicyExpiredMessage(AgentId hostAgentId, ManagementPolicyId managementPolicyId)
    {

    }

    /**
     * Access method for the agentId property.
     *
     * @return   the current value of the agentId property
     */
    public AgentId getAgentId()
    {
        return agentId;
    }

    /**
     * Access method for the managementPolicyId property.
     *
     * @return   the current value of the managementPolicyId property
     */
    public ManagementPolicyId getManagementPolicyId()
    {
        return managementPolicyId;
    }
}

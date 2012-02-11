//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\ManagementPolicyId.java

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\network\\ManagementPolicyId.java

package com.cisco.eManager.eManager.network;


public class ManagementPolicyId
{
    private String m_name;
    private AgentId m_agentId;

    /**
     * @param aId
     * @param name
     * @roseuid 3F60C73603B7
     */
    public ManagementPolicyId(AgentId aId, String name)
    {
        m_agentId = aId;
        m_name = name;
    }

    /**
     * Access method for the agentId property.
     * @return   the current value of the agentId property
     * @roseuid 3F60C7370029
     */
    public AgentId agentId()
    {
        return m_agentId;
    }

    /**
     * @return String
     * @roseuid 3F60C7370065
     */
    public String name()
    {
        return m_name;
    }

    public boolean equals (Object object)
    {
        if (object instanceof ManagementPolicyId) {
            ManagementPolicyId mgmtPolicyId;

            mgmtPolicyId = (ManagementPolicyId) object;
            if (m_name.equals(mgmtPolicyId.name()) == true) {
                if (m_agentId.equals(mgmtPolicyId.agentId()) == true) {
                    return true;
                }
            }
        }

        return false;
    }
}
/**
 * ManagementPolicyId.ManagementPolicyId()
 */

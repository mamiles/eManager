//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImMgmtPolicyCreation.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy;

public class ImMgmtPolicyCreation 
{
    private MgmtPolicy m_mgmtPolicy;
    
    /**
     * @param createdMgmtPolicy
     * @roseuid 3F6E00100373
     */
    ImMgmtPolicyCreation(MgmtPolicy createdMgmtPolicy) 
    {
        m_mgmtPolicy = createdMgmtPolicy;     
    }
    
    /**
     * @return com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy
     * @roseuid 3F6E0012034E
     */
    public MgmtPolicy mgmtPolicy() 
    {
        return m_mgmtPolicy;     
    }
}

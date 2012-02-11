//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\mgmtPolicy\\PolicyCreation.java

package com.cisco.eManager.eManager.inventory.mgmtPolicy;


public class PolicyCreation 
{
    private MgmtPolicy m_mgmtPolicy;
    
    /**
     * @param newMgmtPolicy
     * @roseuid 3F4D08930023
     */
    PolicyCreation(MgmtPolicy newMgmtPolicy) 
    {
        m_mgmtPolicy = newMgmtPolicy;     
    }
    
    /**
     * @return com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy
     * @roseuid 3F215DBE0358
     */
    public MgmtPolicy policy() 
    {
        return m_mgmtPolicy;     
    }
}

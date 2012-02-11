//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImMgmtPolicyUnload.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy;

public class ImMgmtPolicyUnload 
{
    private MgmtPolicy m_mgmtPolicy;
    
    /**
     * @param unloadedMgmtPolicy
     * @roseuid 3F6E00100373
     */
    ImMgmtPolicyUnload(MgmtPolicy unloadedMgmtPolicy) 
    {
        m_mgmtPolicy = unloadedMgmtPolicy;     
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

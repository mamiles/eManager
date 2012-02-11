//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImMgmtPolicyLoad.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy;

public class ImMgmtPolicyLoad 
{
    private MgmtPolicy m_mgmtPolicy;
    
    /**
     * @param loadedMgmtPolicy
     * @roseuid 3F6E00100373
     */
    ImMgmtPolicyLoad(MgmtPolicy loadedMgmtPolicy) 
    {
        m_mgmtPolicy = loadedMgmtPolicy;     
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

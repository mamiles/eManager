//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImMgmtPolicyDeletion.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy;

public class ImMgmtPolicyDeletion 
{
    private MgmtPolicy m_mgmtPolicy;
    
    /**
     * @param deletedMgmtPolicy
     * @roseuid 3F6E001A01FB
     */
    ImMgmtPolicyDeletion(MgmtPolicy deletedMgmtPolicy) 
    {
        m_mgmtPolicy = deletedMgmtPolicy;     
    }
    
    /**
     * @return com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy
     * @roseuid 3F6E001B00C6
     */
    public MgmtPolicy mgmtPolicy() 
    {
        return m_mgmtPolicy;     
    }
}

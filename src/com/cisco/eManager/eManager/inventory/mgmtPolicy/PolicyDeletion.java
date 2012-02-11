//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\mgmtPolicy\\PolicyDeletion.java

package com.cisco.eManager.eManager.inventory.mgmtPolicy;

import com.cisco.eManager.common.inventory.ManagedObjectId;

public class PolicyDeletion 
{
    private MgmtPolicy m_mgmtPolicy;
    
    /**
     * @param deletedMgmtPolicy
     * @roseuid 3F4D089302CC
     */
    PolicyDeletion(MgmtPolicy deletedMgmtPolicy) 
    {
        m_mgmtPolicy = deletedMgmtPolicy;     
    }
    
    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectKey
     * @roseuid 3F215E070040
     */
    public MgmtPolicy policy() 
    {
        return m_mgmtPolicy;     
    }
}

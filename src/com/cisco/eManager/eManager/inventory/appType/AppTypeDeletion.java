//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\appType\\AppTypeDeletion.java

package com.cisco.eManager.eManager.inventory.appType;

import com.cisco.eManager.common.inventory.ManagedObjectId;

public class AppTypeDeletion 
{
    private AppType m_appType;
    
    /**
     * @param deletedAppType
     * @roseuid 3F6724490376
     */
    AppTypeDeletion(AppType deletedAppType) 
    {
        m_appType = deletedAppType;     
    }
    
    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectKey
     * @roseuid 3F215B220311
     */
    public AppType appType() 
    {
        return m_appType;     
    }
}

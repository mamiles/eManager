//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImAppTypeDeletion.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.eManager.inventory.appType.AppType;

public class ImAppTypeDeletion 
{
    private AppType m_appType;
    
    /**
     * @param deletedAppType
     * @roseuid 3F6DFFCF010D
     */
    ImAppTypeDeletion(AppType deletedAppType) 
    {
        m_appType = deletedAppType;     
    }
    
    /**
     * @return com.cisco.eManager.eManager.inventory.appType.AppType
     * @roseuid 3F6DFFD003A3
     */
    public AppType appType() 
    {
        return m_appType;     
    }
}

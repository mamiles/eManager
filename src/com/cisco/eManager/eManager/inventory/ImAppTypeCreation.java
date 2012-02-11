//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImAppTypeCreation.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.eManager.inventory.appType.AppType;

public class ImAppTypeCreation 
{
    private AppType m_appType;
    
    /**
     * @param createdAppType
     * @roseuid 3F6DFFC10045
     */
    ImAppTypeCreation(AppType createdAppType) 
    {
        m_appType = createdAppType;     
    }
    
    /**
     * @return com.cisco.eManager.eManager.inventory.appType.AppType
     * @roseuid 3F6DFFC2037B
     */
    public AppType appType() 
    {
        return m_appType;     
    }
}

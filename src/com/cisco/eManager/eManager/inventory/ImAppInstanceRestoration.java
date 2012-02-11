//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImAppInstanceCreation.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;

public class ImAppInstanceRestoration
{
    private AppInstance m_appInstance;
    
    /**
     * @param appInstance
     * @roseuid 3F671E2F03D4
     */
    ImAppInstanceRestoration(AppInstance appInstance) 
    {
        m_appInstance = appInstance;     
    }
    
    /**
     * @return the app instance which have been created
     * @roseuid 3F671E300138
     */
    public AppInstance appInstance() 
    {
        return m_appInstance;     
    }
}

//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImAppInstanceUnmanage.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;

public class ImAppInstanceUnmanage 
{
    private AppInstance m_appInstance;
    
    /**
     * @param unmanagedAppInstance
     * @roseuid 3F671E4402EE
     */
    ImAppInstanceUnmanage(AppInstance unmanagedAppInstance) 
    {
        m_appInstance = unmanagedAppInstance;     
    }
    
    /**
     * @return com.cisco.eManager.eManager.inventory.appInstance.AppInstance
     * @roseuid 3F671E45019B
     */
    public AppInstance appInstance() 
    {
        return m_appInstance;     
    }
}

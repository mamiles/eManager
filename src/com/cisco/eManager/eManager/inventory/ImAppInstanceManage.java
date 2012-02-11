//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImAppInstanceManage.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;

public class ImAppInstanceManage 
{
    private AppInstance m_appInstance;
    
    /**
     * @param managedAppInstance
     * @roseuid 3F671E390389
     */
    ImAppInstanceManage(AppInstance managedAppInstance) 
    {
        m_appInstance = managedAppInstance;     
    }
    
    /**
     * @return com.cisco.eManager.eManager.inventory.appInstance.AppInstance
     * @roseuid 3F671E3A0204
     */
    public AppInstance appInstance() 
    {
        return m_appInstance;     
    }
}

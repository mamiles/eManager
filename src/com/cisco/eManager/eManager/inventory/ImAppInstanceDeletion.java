//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImAppInstanceDeletion.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;

public class ImAppInstanceDeletion 
{
    private AppInstance m_appInstance;
    
    /**
     * @param deletedAppInstanceId
     * @param hostId
     * @param deletedAppInstance
     * @roseuid 3F671E3301E5
     */
    ImAppInstanceDeletion(AppInstance deletedAppInstance) 
    {
        m_appInstance = deletedAppInstance;     
    }
    
    /**
     * @return ID of the app instance which has been deleted
     * @roseuid 3F671E340269
     */
    public AppInstance appInstance() 
    {
        return m_appInstance;     
    }
}

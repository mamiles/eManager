//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\appInstance\\AppInstanceDeletion.java

package com.cisco.eManager.eManager.inventory.appInstance;

import com.cisco.eManager.common.inventory.ManagedObjectId;

public class AppInstanceDeletion 
{
    
    /**
     * the ID of the application instance which has been deleted
     */
    private AppInstance m_deletedAppInstance;
    
    /**
     * @param deletedAppInstance the application instance which was deleted
     * @roseuid 3F65AC4C0349
     */
    AppInstanceDeletion(AppInstance deletedAppInstance) 
    {
        m_deletedAppInstance = deletedAppInstance;     
    }
    
    /**
     * @return the application instance which has been deleted
     * @roseuid 3F65AC4E02AC
     */
    public AppInstance deletedAppInstance() 
    {
        return m_deletedAppInstance;     
    }
}

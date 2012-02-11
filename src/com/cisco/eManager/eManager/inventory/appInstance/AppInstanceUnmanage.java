//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\appInstance\\AppInstanceUnmanage.java

package com.cisco.eManager.eManager.inventory.appInstance;


public class AppInstanceUnmanage 
{
    
    /**
     * the application instance which has been transitioned to the "unmanaged" mode
     */
    private AppInstance m_appInstance;
    
    /**
     * @param appInstance the application instance which has been transitioned to the
     * "unmanaged" mode
     * @roseuid 3F65AC8A02DB
     */
    AppInstanceUnmanage(AppInstance appInstance) 
    {
        m_appInstance = appInstance;     
    }
    
    /**
     * @return the application instance which has been transitioned to the "unmanaged"
     * mode
     * @roseuid 3F65AC8C0158
     */
    public AppInstance appInstance() 
    {
        return m_appInstance;     
    }
}

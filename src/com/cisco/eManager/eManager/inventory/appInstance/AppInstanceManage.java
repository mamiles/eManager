//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\appInstance\\AppInstanceManage.java

package com.cisco.eManager.eManager.inventory.appInstance;


public class AppInstanceManage 
{
    
    /**
     * the application instance which has been transitioned to the "managed" mode
     */
    private AppInstance m_appInstance;
    
    /**
     * @param appInstance the application instance which has been transitioned to the
     * "managed" mode
     * @roseuid 3F65AC5A015F
     */
    AppInstanceManage(AppInstance appInstance) 
    {
        m_appInstance = appInstance;     
    }
    
    /**
     * @return the application instance which has been transitioned to the "managed"
     * mode
     * @roseuid 3F65AC5C01EE
     */
    public AppInstance appInstance() 
    {
        return m_appInstance;     
    }
}

package com.cisco.eManager.eManager.inventory.appInstance;


public class AppInstanceRestoration
{
    
    /**
     * the application instance which has been created
     */
    private AppInstance m_appInstance;
    
    /**
     * @param appInstance the application instance which has been created
     * @roseuid 3F65AC3E01E0
     */
    AppInstanceRestoration(AppInstance appInstance) 
    {
        m_appInstance = appInstance;     
    }
    
    /**
     * @return the application instance which has been created
     * @roseuid 3F65AC40021F
     */
    public AppInstance appInstance() 
    {
        return m_appInstance;     
    }
}

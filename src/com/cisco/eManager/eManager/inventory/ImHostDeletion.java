//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImHostDeletion.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.eManager.inventory.host.Host;

public class ImHostDeletion 
{
    private Host m_host;
    
    /**
     * @param deletedHost
     * @roseuid 3F6DFFE60228
     */
    ImHostDeletion(Host deletedHost) 
    {
        m_host = deletedHost;     
    }
    
    /**
     * @return com.cisco.eManager.eManager.inventory.host.Host
     * @roseuid 3F6DFFE90219
     */
    public Host host() 
    {
        return m_host;     
    }
}

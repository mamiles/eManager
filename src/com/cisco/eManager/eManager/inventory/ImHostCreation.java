//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImHostCreation.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.eManager.inventory.host.Host;

public class ImHostCreation 
{
    private Host m_host;
    
    /**
     * @param createdHost
     * @roseuid 3F6DFFDC0075
     */
    ImHostCreation(Host createdHost) 
    {
        m_host = createdHost;     
    }
    
    /**
     * @return com.cisco.eManager.eManager.inventory.host.Host
     * @roseuid 3F6DFFDD035C
     */
    public Host host() 
    {
        return m_host;     
    }
}

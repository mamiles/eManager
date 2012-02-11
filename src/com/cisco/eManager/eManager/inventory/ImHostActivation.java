//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImHostActivation.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.eManager.inventory.host.Host;

public class ImHostActivation
{
    private Host m_host;

    ImHostActivation(Host createdHost)
    {
        m_host = createdHost;
    }

    public Host host()
    {
        return m_host;
    }
}

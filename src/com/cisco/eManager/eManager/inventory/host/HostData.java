//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\HostData\\HostData.java

package com.cisco.eManager.eManager.inventory.host;

import com.cisco.eManager.common.inventory.ManagedObjectId;

public class HostData
{
    private ManagedObjectId m_id;  // the key component of this is the ip addr as an int
    private String m_ipAddressStr;
    private String m_domain;
    private String m_hostname;

    public HostData(ManagedObjectId id,
                    String ipAddressStr,
                    String domain,
                    String hostname)
    {
        m_id = id;
        m_ipAddressStr = ipAddressStr;
        m_domain = domain;
        m_hostname = hostname;
    }

    public ManagedObjectId id()
    {
        return m_id;
    }

    public String ipAddress()
    {
        return m_ipAddressStr;
    }

    void ipAddress(String ipAddressStr)
    {
        m_ipAddressStr = ipAddressStr;
    }

    public String domain()
    {
        return m_domain;
    }

    void domain(String domain)
    {
        m_domain = domain;
    }

    public String hostname()
    {
        return m_hostname;
    }

    void hostname(String hostname)
    {
        m_hostname = hostname;
    }

    public String toString()
    {
        return "Id:" + m_id + " IP Address:" + m_ipAddressStr + " TibDomain:" + m_domain + " Host:" +
            m_hostname;
    }
}

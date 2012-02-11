//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\host\\HostNotification.java

package com.cisco.eManager.eManager.inventory.host;


public class HostNotification
{
    private HostNotificationType m_ntfcnType;
    private Host m_host;

    HostNotification(HostNotificationType type, Host host)
    {
        m_ntfcnType = type;
        m_host = host;
    }

    public HostNotificationType ntfcnType()
    {
        return m_ntfcnType;
    }

    public Host host()
    {
        return m_host;
    }
}

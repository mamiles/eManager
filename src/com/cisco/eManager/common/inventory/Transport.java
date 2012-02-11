//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\common\\inventory\\Transport.java

package com.cisco.eManager.common.inventory;


/**
 * @author wstubb
 * @version 1.0
 */
public class Transport
{

    private static final int XPORT_SSH = 1;
    private static final String XPORT_SSH_STR = "SSH";
    private static final int XPORT_TELNET = 2;
    private static final String XPORT_TELNET_STR = "TELNET";
    public static final Transport SSH = new Transport (XPORT_SSH);
    public static final Transport TELNET = new Transport (XPORT_TELNET);
    private int m_xport = XPORT_TELNET;

    public Transport()
    {
        // this class shouldn't have a default ctor, except that it needs one
        // because it's part of a SOAP/http interface described via wsdl
        m_xport = XPORT_TELNET;
    }

    public Transport(String str)
    {
        fromString(str);
    }

    /**
     * @param xport
     * @roseuid 3F6226AE013B
     */
    private Transport(int xport)
    {
        m_xport = xport;
    }

    /**
     * @return boolean
     * @roseuid 3F6226EC0393
     */
    public boolean isSsh()
    {
        return (m_xport == XPORT_SSH);
    }

    /**
     * @return boolean
     * @roseuid 3F6226F40196
     */
    public boolean isTelnet()
    {
        return (m_xport == XPORT_TELNET);
    }

    public int intValue()
    {
        return m_xport;
    }

    public String toString()
    {
        if ( isSsh() )
        {
            return XPORT_SSH_STR;
        }
        else
        {
            return XPORT_TELNET_STR;
        }
    }

    public void fromString(String str)
    {
        if ( str == null )
        {
            m_xport = XPORT_TELNET;
        }
        else
        {
            String sanitizedStr = str.toUpperCase().trim();
            if (sanitizedStr.equals(XPORT_SSH_STR))
            {
                m_xport = XPORT_SSH;
            }
            else
            {
                m_xport = XPORT_TELNET;
            }
        }
    }
}

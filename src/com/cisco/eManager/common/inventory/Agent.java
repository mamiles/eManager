//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\common\\inventory\\Agent.java

package com.cisco.eManager.common.inventory;


/**
 * @author wstubb
 * @version 1.0
 */
public class Agent 
{
    private String m_dnsHostname;
    private String m_name;
    private String m_mgmtDomain;
    private ManagedObjectId m_id;
    
    /**
     * @roseuid 3F561E3502F4
     */
    public Agent() 
    {
     
    }
    
    /**
     * @return String
     * @roseuid 3F54BD6B03E3
     */
    public String dnsHostname() 
    {
     return null;
    }
    
    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectKey
     * @roseuid 3F54B1D90221
     */
    public ManagedObjectId id() 
    {
     return null;
    }
    
    /**
     * In the underlying DB schema supporting this object, the IP address serves as
     * the primary key.  Therefore, the IP address is the value component of this
     * object's ID (m_id).  This method returns that value as an int.
     * @return int
     * @roseuid 3F54BB5201CE
     */
    public int ipAddress() 
    {
     return 0;
    }
    
    /**
     * In the underlying DB schema supporting this object, the IP address serves as
     * the primary key.  Therefore, the IP address is the value component of this
     * object's ID (m_id).  This method returns that value as a String.
     * @return String
     * @roseuid 3F54BB63033B
     */
    public String ipAddressString() 
    {
     return null;
    }
    
    /**
     * @return String
     * @roseuid 3F54BD750012
     */
    public String mgmtDomain() 
    {
     return null;
    }
    
    /**
     * @return String
     * @roseuid 3F54BB4603B1
     */
    public String name() 
    {
     return null;
    }
}

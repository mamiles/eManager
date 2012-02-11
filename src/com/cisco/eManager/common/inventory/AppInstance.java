//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\common\\inventory\\AppInstance.java

package com.cisco.eManager.common.inventory;


/**
 * @author wstubb
 * @version 1.0
 */
public class AppInstance
{
    private String m_logfileDirectories;
    private String m_logfilePassword;
    private String m_logfileUsername;
    private String m_name;
    private ManagedObjectId m_id;
    private AppInstanceMgmtMode m_managementMode;
    private ManagedObjectId m_appTypeId;
    private ManagedObjectId m_hostId;
    private Transport m_logfileTransport;

    public AppInstance()
    {
        m_id = null;
        m_appTypeId = null;
        m_hostId = null;
        m_name = null;
        m_logfileUsername = null;
        m_logfilePassword = null;
        m_logfileDirectories = null;
        m_logfileTransport = null;
        m_managementMode = null;
    }

    /**
     * @param id
     * @param appTypeId
     * @param hostId
     * @param name
     * @param logfileUsername
     * @param logfilePassword
     * @param logfileDirectories
     * @param logfileTransport
     * @param managementMode
     * @roseuid 3F561E440332
     */
    public AppInstance(ManagedObjectId id, ManagedObjectId appTypeId, ManagedObjectId hostId, String name, String logfileUsername, String logfilePassword, String logfileDirectories, Transport logfileTransport, AppInstanceMgmtMode managementMode)
    {
        m_id = id;
        m_appTypeId = appTypeId;
        m_hostId = hostId;
        m_name = name;
        m_logfileUsername = logfileUsername;
        m_logfilePassword = logfilePassword;
        m_logfileDirectories = logfileDirectories;
        m_logfileTransport = logfileTransport;
        m_managementMode = managementMode;
    }

    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectKey
     * @roseuid 3F54B77702A1
     */
    public ManagedObjectId appTypeId()
    {
        return m_appTypeId;
    }

    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectKey
     * @roseuid 3F54B780036C
     */
    public ManagedObjectId hostId()
    {
        return m_hostId;
    }

    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectKey
     * @roseuid 3F54B1B901DF
     */
    public ManagedObjectId id()
    {
        return m_id;
    }

    /**
     * @return String
     * @roseuid 3F54B7890167
     */
    public String logfileDirectories()
    {
        return m_logfileDirectories;
    }

    /**
     * @return String
     * @roseuid 3F54B79400D6
     */
    public String logfilePassword()
    {
        return m_logfilePassword;
    }

    /**
     * @return com.cisco.eManager.common.inventory.AppInstance.Transport
     * @roseuid 3F54B7D00064
     */
    public Transport logfileTransport()
    {
        return m_logfileTransport;
    }

    /**
     * @return String
     * @roseuid 3F54B7A002FA
     */
    public String logfileUsername()
    {
        return m_logfileUsername;
    }

    /**
     * @return com.cisco.eManager.common.inventory.AppInstanceMgmtMode
     * @roseuid 3F54BBE40296
     */
    public AppInstanceMgmtMode managementMode()
    {
        return m_managementMode;
    }

    /**
     * @return String
     * @roseuid 3F54B7B102C3
     */
    public String name()
    {
        return m_name;
    }
}

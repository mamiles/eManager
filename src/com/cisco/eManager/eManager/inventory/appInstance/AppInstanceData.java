//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\AppInstanceData\\AppInstanceData.java

package com.cisco.eManager.eManager.inventory.appInstance;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.AppInstanceMgmtMode;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.common.inventory.Transport;

public class AppInstanceData
{
    private static Logger logger = Logger.getLogger(AppInstanceData.class);
    private ManagedObjectId m_id;
    private ManagedObjectId m_hostId;
    private ManagedObjectId m_appTypeId;
    private String m_name;
    private AppInstanceMgmtMode m_mgmtMode;
    private String m_logfileDirectories;
    private Transport m_logfileTransport;
    private String m_transportUsername;
    private String m_transportPassword;
    private String m_transportUnixPrompt;
    private String m_propertyFile;

    public AppInstanceData(ManagedObjectId id,
                           ManagedObjectId hostId,
                           ManagedObjectId appTypeId,
                           String name,
                           AppInstanceMgmtMode mgmtMode,
                           String logfileDirectories,
                           Transport logfileTransport,
                           String transportUsername,
                           String transportPassword,
                           String transportUnixPrompt,
                           String propertyFile)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;
        if ( !id.getManagedObjectType().equals(
                ManagedObjectIdType.ApplicationInstance ))
        {
            reason = "id must be of type " +
                     ManagedObjectIdType.ApplicationInstance.toString();
            logger.error(reason);
            throw new Exception(reason);
        }

        if ( !hostId.getManagedObjectType().equals(
                ManagedObjectIdType.HostAgent ))
        {
            reason = "host id must be of type " +
                     ManagedObjectIdType.HostAgent.toString();
            logger.error(reason);
            throw new Exception(reason);
        }

        if ( !appTypeId.getManagedObjectType().equals(
                ManagedObjectIdType.ApplicationType ))
        {
            reason = "app type id must be of type " +
                     ManagedObjectIdType.ApplicationType.toString();
            logger.error(reason);
            throw new Exception(reason);
        }

        m_id = id;
        m_hostId = hostId;
        m_appTypeId = appTypeId;
        m_name = name;
        m_mgmtMode =
            mgmtMode == null ? AppInstanceMgmtMode.MANAGED : mgmtMode;
        m_logfileDirectories =
            logfileDirectories == null ? "" : logfileDirectories;
        m_logfileTransport =
            logfileTransport == null ? Transport.TELNET : logfileTransport;
        m_transportUsername =
            transportUsername == null ? "" : transportUsername;
        m_transportPassword =
            transportPassword == null ? "" : transportPassword;
        m_transportUnixPrompt =
            transportUnixPrompt == null ? "" : transportUnixPrompt;
        m_propertyFile = propertyFile == null ? "" : propertyFile;
    }

    public ManagedObjectId id()
    {
        return m_id;
    }

    public ManagedObjectId hostId()
    {
        return m_hostId;
    }

    public ManagedObjectId appTypeId()
    {
        return m_appTypeId;
    }

    public String name()
    {
        return m_name;
    }

    public AppInstanceMgmtMode mgmtMode()
    {
        return m_mgmtMode;
    }

    void mgmtMode(AppInstanceMgmtMode newMode)
    {
        m_mgmtMode = newMode;
    }

    public String logfileDirectories()
    {
        return m_logfileDirectories;
    }

    void logfileDirectories(String newLogfileDirectories)
    {
        m_logfileDirectories = newLogfileDirectories;
    }

    public Transport logfileTransport()
    {
        return m_logfileTransport;
    }

    void logfileTransport(Transport newTransport)
    {
        m_logfileTransport = newTransport;
    }

    public String transportUsername()
    {
        return m_transportUsername;
    }

    void transportUsername(String newTransportUsername)
    {
        m_transportUsername = newTransportUsername;
    }

    public String transportPassword()
    {
        return m_transportPassword;
    }

    void transportPassword(String newTransportPassword)
    {
        m_transportPassword = newTransportPassword;
    }

    public String transportUnixPrompt()
    {
        return m_transportUnixPrompt;
    }

    void transportUnixPrompt(String newTransportUnixPrompt)
    {
        m_transportUnixPrompt = newTransportUnixPrompt;
    }

    public String propertyFile()
    {
        return m_propertyFile;
    }

    void propertyFile(String propertyFile)
    {
        m_propertyFile = propertyFile;
    }

    public String toString()
    {
        return "Id:" + m_id.toString() + " Name:" + m_name + " LogfileDir:" + m_logfileDirectories +
            " Username:" + m_transportUsername + " Password:" + m_transportPassword +
            " Prompt:" + m_transportUnixPrompt + " Transport:" + m_logfileTransport +
            " Mode:" + m_mgmtMode + " HostId:" + m_hostId + " AppTypeId:" + m_appTypeId;
    }
}
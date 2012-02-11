//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\mgmtPolicy\\MgmtPolicyData.java

package com.cisco.eManager.eManager.inventory.mgmtPolicy;

import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.inventory.appType.*;
import com.cisco.eManager.eManager.network.ManagementPolicyId;

public class MgmtPolicyData
{
    private static Logger logger = Logger.getLogger(MgmtPolicyData.class);
    private ManagedObjectId m_id;
    private String m_name;
    private String m_path;
    private ManagedObjectId m_appTypeId;
    private ManagedObjectId m_hostId;

    public MgmtPolicyData(ManagedObjectId id,
                          String name,
                          String path,
                          ManagedObjectId appTypeId,
                          ManagedObjectId hostId)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;

        if ( !id.getManagedObjectType().equals(ManagedObjectIdType.MgmtPolicy) )
        {
            reason = "id must be of type " + ManagedObjectIdType.MgmtPolicy;
            logger.error(reason);
            throw new Exception(reason);
        }

        if ( !appTypeId.getManagedObjectType().equals(
                ManagedObjectIdType.ApplicationType) )
        {
            reason = "app type id must be of type " +
                     ManagedObjectIdType.ApplicationType;
            logger.error(reason);
            throw new Exception(reason);
        }

        if ( !hostId.getManagedObjectType().equals(
                ManagedObjectIdType.HostAgent) )
        {
            reason = "host id must be of type " +
                     ManagedObjectIdType.HostAgent;
            logger.error(reason);
            throw new Exception(reason);
        }

        m_id = id;
        m_name = name;
        m_path = path;
        m_appTypeId = appTypeId;
        m_hostId = hostId;
    }

    public ManagedObjectId id()
    {
        return m_id;
    }

    public String name()
    {
        return m_name;
    }

    public String path()
    {
        return m_path;
    }

    synchronized void path(String newPathname)
    {
        m_path = newPathname;
    }

    public ManagedObjectId appTypeId()
    {
        return m_appTypeId;
    }

    synchronized void appTypeId(ManagedObjectId newAppTypeId)
    {
        m_appTypeId = newAppTypeId;
    }

    public ManagedObjectId hostId()
    {
        return m_hostId;
    }

    public String toString()
    {
        return "Id:" + m_id + " Name:" + m_name + " Path:" + m_path + " AppTypeId:" + m_appTypeId +
            " HostId:" + m_hostId;
    }
}
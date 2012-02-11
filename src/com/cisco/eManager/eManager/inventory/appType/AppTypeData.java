package com.cisco.eManager.eManager.inventory.appType;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.*;

public class AppTypeData
{
    private static Logger logger = Logger.getLogger(AppTypeData.class);
    private ManagedObjectId m_id;
    private String m_name;
    private String m_version;

    public AppTypeData(ManagedObjectId id,
                       String name,
                       String version)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;

        if ( !id.getManagedObjectType().equals(
                ManagedObjectIdType.ApplicationType) )
        {
            reason = "id must be of type " +
                     ManagedObjectIdType.ApplicationType;
        }

        m_id = id;
        m_name = name;
        m_version = version;
    }

    public ManagedObjectId id()
    {
        return m_id;
    }

    public String name()
    {
        return m_name;
    }

    public String version()
    {
        return m_version;
    }

    public String toString()
    {
        return "Id:" + m_id + " Name:" + m_name + " Version:" + m_version;
    }
}

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.*;

public class ApplicationHierarchyRelationshipData
{
    private static Logger logger = Logger.getLogger(ApplicationHierarchyRelationshipData.class);
    private ManagedObjectId m_appTypeId;
    private ManagedObjectId m_appHierarchyId;

    public ApplicationHierarchyRelationshipData(ManagedObjectId appTypeId,
						ManagedObjectId appHierarchyId)
        throws Exception
    {
        if (!appTypeId.getManagedObjectType().equals(ManagedObjectIdType.ApplicationType))
        {
            throw new Exception ("id must be of type " + ManagedObjectIdType.ApplicationType);
        }

        if (!appHierarchyId.getManagedObjectType().equals(ManagedObjectIdType.ApplicationHierarchyContainer))
        {
            throw new Exception ("apps view container id must be of type " + ManagedObjectIdType.ApplicationHierarchyContainer);
        }

	m_appTypeId = appTypeId;
	m_appHierarchyId = appHierarchyId;
    }

    public ManagedObjectId appTypeId()
    {
        return m_appTypeId;
    }

    public ManagedObjectId appHierarchyId()
    {
        return m_appHierarchyId;
    }

    public String toString()
    {
        return "AppTypeId:" + m_appTypeId + " AppHierarchyId:" + m_appHierarchyId;
    }
}

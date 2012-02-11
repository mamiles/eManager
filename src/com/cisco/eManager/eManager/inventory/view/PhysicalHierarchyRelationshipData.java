package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.*;

public class PhysicalHierarchyRelationshipData
{
    private static Logger logger = Logger.getLogger(PhysicalHierarchyRelationshipData.class);
    private ManagedObjectId m_hostId;
    private ManagedObjectId m_physicalHierarchyId;

    public PhysicalHierarchyRelationshipData(ManagedObjectId hostId,
					     ManagedObjectId physicalHierarchyId)
        throws Exception
    {
        if (!hostId.getManagedObjectType().equals(ManagedObjectIdType.HostAgent))
        {
            throw new Exception ("id must be of type " + ManagedObjectIdType.HostAgent);
        }

        if (!physicalHierarchyId.getManagedObjectType().equals(ManagedObjectIdType.PhysicalHierarchyContainer))
        {
            throw new Exception ("physicals view container id must be of type " + ManagedObjectIdType.PhysicalHierarchyContainer);
        }

	m_hostId = hostId;
	m_physicalHierarchyId = physicalHierarchyId;
    }

    public ManagedObjectId hostId()
    {
        return m_hostId;
    }

    public ManagedObjectId physicalHierarchyId()
    {
        return m_physicalHierarchyId;
    }

    public String toString()
    {
        return "HostId:" + m_hostId + " PhysicalHierarchyId:" + m_physicalHierarchyId;
    }
}

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

public class SolutionHierarchyRelationshipData
{
    private static Logger logger = Logger.getLogger(SolutionHierarchyRelationshipData.class);
    private ManagedObjectId m_solutionId;
    private ManagedObjectId m_solutionHierarchyId;

    public SolutionHierarchyRelationshipData(ManagedObjectId solutionId,
					     ManagedObjectId solutionHierarchyId)
        throws Exception
    {
        if (solutionId == null ||
	    !solutionId.getManagedObjectType().equals(ManagedObjectIdType.Solution))
        {
            throw new Exception ("id must be of type " + ManagedObjectIdType.Solution);
        }

        if (solutionHierarchyId == null ||
	    !solutionHierarchyId.getManagedObjectType().equals(ManagedObjectIdType.SolutionHierarchyContainer))
        {
            throw new Exception ("solution view container id must be of type " + ManagedObjectIdType.SolutionHierarchyContainer);
        }

	m_solutionId = solutionId;
	m_solutionHierarchyId = solutionHierarchyId;
    }

    public ManagedObjectId solutionId()
    {
        return m_solutionId;
    }

    public ManagedObjectId solutionHierarchyId()
    {
        return m_solutionHierarchyId;
    }

    public String toString()
    {
        return "SolutionId:" + m_solutionId + " SolutionHierarchyId:" + m_solutionHierarchyId;
    }
}

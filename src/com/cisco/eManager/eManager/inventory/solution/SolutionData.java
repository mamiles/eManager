package com.cisco.eManager.eManager.inventory.solution;

import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedList;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

/**
 * @author wstubb
 * @version 1.0
 */
public class SolutionData
{
    private ManagedObjectId m_id;
    private String m_name;
    private Collection m_appInstanceIds;

    public SolutionData (ManagedObjectId id,
			 String name,
			 Collection appInstanceIds) throws Exception
    {
	Iterator iter;
	ManagedObjectId appInstanceId;

	if (!id.getManagedObjectType().equals (ManagedObjectIdType.Solution)) {
	    throw new Exception ("Solution id must be of type ManagedObjectIdType.Solution:" + id);
	}

	if (name == null ||
	    name.trim().length() == 0) {
	    throw new Exception ("Malformed solution name: " + name);
	}

	m_id = id;
	m_name = name.trim();
	if (appInstanceIds == null) {
	    m_appInstanceIds = new LinkedList();
	} else {
	    iter = appInstanceIds.iterator();
	    while (iter.hasNext()) {
		appInstanceId = (ManagedObjectId) iter.next();
		if (!appInstanceId.getManagedObjectType().equals (ManagedObjectIdType.ApplicationInstance)) {
		    throw new Exception ("Malformed application instance managed object id: " + appInstanceId);
		}
	    }

	    m_appInstanceIds = appInstanceIds;
	}
    }

    /**
     * @return String
     * @roseuid 3F54BD6B03E3
     */
    public String name()
    {
     return m_name;
    }

    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectKey
     * @roseuid 3F54B1D90221
     */
    public ManagedObjectId id()
    {
     return m_id;
    }

    public Collection appInstanceIds()
    {
        return m_appInstanceIds;
    }

    public String toString()
    {
        return null;
    }
}

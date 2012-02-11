package com.cisco.eManager.eManager.inventory.solution;

import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

/**
 * @author wstubb
 * @version 1.0
 */
public class SolutionId
{
    private static Logger logger = Logger.getLogger(SolutionId.class);
    private ManagedObjectId m_id;
    private String m_name;

    public SolutionId(ManagedObjectId id)
        throws Exception
    {
        String reason = null;
        if (id == null)
        {
            reason = "SolutionId cannot be constructed from a null " +
                     "ManagedObjectId";
            logger.error(reason);
            throw new Exception(reason);
        }
        if (!id.getManagedObjectType().equals(ManagedObjectIdType.Solution))
        {
            reason = "SolutionId can only be constructed from " +
                     "ManagedObjectId of type ManagedObjectIdType.Solution";
            logger.error(reason);
            throw new Exception(reason);
        }
        m_id = id;
        m_name = null;
    }

    public SolutionId(String name)
        throws Exception
    {
        String reason = null;
        if (name == null)
        {
            reason = "SolutionId cannot be constructed from a null " +
                     "ManagedObjectId";
            logger.error(reason);
            throw new Exception(reason);
        }
        if (name.equals(""))
        {
            reason = "SolutionId can only be constructed from " +
                     "ManagedObjectId of type ManagedObjectIdType.Solution";
            logger.error(reason);
            throw new Exception(reason);
        }
        m_id = null;
        m_name = name;
    }

    public ManagedObjectId id()
    {
        return m_id;
    }

    public String name()
    {
        return m_name;
    }

    public String toString()
    {
        return null;
    }
}

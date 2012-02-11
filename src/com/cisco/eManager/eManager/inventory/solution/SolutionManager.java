//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\solution\\SolutionManager.java

package com.cisco.eManager.eManager.inventory.solution;

import java.util.*;
import org.apache.log4j.Logger;
import COM.TIBCO.hawk.console.hawkeye.*;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.database.DatabaseInterface;
//import com.cisco.eManager.eManager.inventory.view.ContainerNode;
import com.cisco.eManager.eManager.network.*;
import com.cisco.eManager.common.database.*;

public class SolutionManager
    extends Observable
{
    private static Logger logger = Logger.getLogger(SolutionManager.class);
    private static SolutionManager s_instance;
    private DatabaseInterface m_db;
    private List m_solutions;

    public static synchronized SolutionManager instance()
    {
        if (s_instance == null)
        {
            s_instance = new SolutionManager();
        }
        return s_instance;
    }

    private SolutionManager()
    {
        logger.debug("enter");
        try
        {
            m_db = DatabaseInterface.instance();
        }
        catch (EmanagerDatabaseException e)
        {
            logger.error("exception caught while initializing the DB: " + e);
            m_db = null;
        }
        m_solutions = Collections.synchronizedList(new LinkedList());
        initializeCacheFromDb();
    }

    public synchronized Solution createSolution(String name,
                                                Collection appInstanceIds)
        throws Exception
    {
        logger.debug("enter");
        SolutionId solutionId = new SolutionId(name);
        Solution solution = get(solutionId);
        if (solution == null)
        {
            SolutionData solutionData = null;
            try
            {
                solutionData = m_db.createSolution(name, appInstanceIds);
            }
            catch (Exception e)
            {
                logger.error("exception caught while creating a solution: " + e);
                return null;
            }
            solution = new Solution(solutionData);
            m_solutions.add(solution);
            logger.info("solution created: name = " + solution.name());
            logger.debug("notifying observers creation");
            BasicSolutionNotification ntfcnObj =
                new BasicSolutionNotification(
                                           BasicSolutionNotificationType.CREATE,
                                           solution);
            setChanged();
            notifyObservers(ntfcnObj);
            logger.debug("observers notified of creation");
        }
        else
        {
            logger.info("solution \"" + solution.name() + "\" already exists");
        }
        return solution;
    }

    public synchronized void deleteSolution(SolutionId solutionId)
    {
        logger.debug("enter");
        logger.debug("deleting solution with ID " + solutionId.toString());
        Solution solution = null;
        ManagedObjectId id = solutionIdToId(solutionId);
        if ( id == null )
        {
            logger.debug("solution not found in collection");
            return;
        }

        Iterator iter = m_solutions.iterator();
        while (iter.hasNext())
        {
            solution = (Solution)iter.next();
            if (solution.id().equals(id))
            {
                logger.debug("solution found: removing it");
                BasicSolutionNotification sn =
                    new BasicSolutionNotification(
                        BasicSolutionNotificationType.PRE_DELETE,
                        solution);
                setChanged();
                notifyObservers(sn);

                try
                {
                    m_db.removeSolution(solution);
                    logger.debug("solution removed from db");
                }
                catch (EmanagerDatabaseException ex)
                {
                    logger.error("exception caught while removing solution " +
                                 "from DB: " + ex);
                    logger.error("notifying observers that solution wasn't " +
                                 "really deleted");
                    sn = new BasicSolutionNotification(
                                         BasicSolutionNotificationType.UNDELETE,
                                         solution);
                    setChanged();
                    notifyObservers(sn);
                    return;
                }

                iter.remove();
                logger.debug("solution deleted");
                sn = new BasicSolutionNotification(
                                      BasicSolutionNotificationType.POST_DELETE,
                                      solution);
                setChanged();
                notifyObservers(sn);
                logger.info("solution deleted: name = " + solution.name());
                return;
            }
        }
        logger.debug("solution not found in collection");
    }

    public Solution get(SolutionId solutionId)
    {
        if ( solutionId.name() != null )
        {
            return getByName(solutionId.name());
        }
        else
        {
            return getById(solutionId.id());
        }
    }

    private Solution getById(ManagedObjectId id)
    {
        logger.debug("finding solution with ID " + id);
        Solution solution = null;
        Iterator iter = m_solutions.iterator();
        while (iter.hasNext())
        {
            solution = (Solution)iter.next();
            if (solution.id().equals(id))
            {
                return solution;
            }
        }
        return null;
    }

    private Solution getByName(String name)
    {
        logger.debug("finding solution with name " + name);
        Solution solution = null;
        Iterator iter = m_solutions.iterator();
        while (iter.hasNext())
        {
            solution = (Solution)iter.next();
            if (solution.name().equals(name))
            {
                return solution;
            }
        }
        return null;
    }

    public synchronized Solution[] allSolutions()
    {
        int solutionCount = m_solutions.size();
        Solution[] solutions = new Solution[solutionCount];
        Iterator iter = m_solutions.iterator();
        for (int i = 0; i < solutionCount; i++)
        {
            solutions[i] = (Solution)iter.next();
        }
        return solutions;
    }

    private void initializeCacheFromDb()
    {
        logger.debug("enter");
        Collection solutionDatas = null;

        try
        {
            solutionDatas = m_db.retrieveSolutions();
        }
        catch (EmanagerDatabaseException e)
        {
            logger.error("exception caught while initializing cache from db: " +
                         e);
            return;
        }

        Iterator iter = solutionDatas.iterator();
        SolutionData solutionData = null;
        Solution solution = null;
        BasicSolutionNotification sn = null;
        while (iter.hasNext())
        {
            solutionData = (SolutionData)iter.next();
            solution = new Solution(solutionData);
            m_solutions.add(solution);
            logger.debug("solution restored from database: " +
                         solution.toString());
            sn = new BasicSolutionNotification(
                                          BasicSolutionNotificationType.RESTORE,
                                          solution);
            setChanged();
            notifyObservers(sn);
        }
    }

    public ManagedObjectId solutionIdToId(SolutionId solutionId)
    {
        if ( solutionId.id() == null )
        {
            Solution solution = getByName(solutionId.name());
            if ( solution == null )
            {
                return null;
            }
            else
            {
                return solution.id();
            }
        }
        else
        {
            return solutionId.id();
        }
    }
}

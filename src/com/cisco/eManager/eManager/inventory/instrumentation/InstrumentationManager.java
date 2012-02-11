//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\instrumentation\\InstrumentationManager.java

package com.cisco.eManager.eManager.inventory.instrumentation;

import java.util.*;
import org.apache.log4j.Logger;
import COM.TIBCO.hawk.console.hawkeye.*;
import COM.TIBCO.hawk.talon.*;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.appType.*;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.network.*;
import com.cisco.eManager.common.inventory.ManagedObjectId;


public class InstrumentationManager
    extends Observable
{
    private static Logger logger = Logger.getLogger(InstrumentationManager.class);
    private static InstrumentationManager s_instance;
    private long m_idKeyValue;
    private List m_instrumentations;

    /**
     * @roseuid 3F4D0865009E
     */
    private InstrumentationManager()
    {
        logger.debug("enter");
        m_idKeyValue = 0;
        m_instrumentations = Collections.synchronizedList(new LinkedList());
    }

    /**
     * @param networkId
     * @param appInstanceId
     * @return com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation
     * @roseuid 3F0998730251
     */
    public synchronized Instrumentation
        createInstrumentation(MicroAgentID uAgentId,
                              ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        AppInstance ai = AppInstanceManager.instance().find(appInstanceId);
        Host host = ai.host();
        MicroagentId networkId =
            new MicroagentId(host.networkId().raw(), uAgentId);
        Instrumentation newInstrumentation = find(networkId);
        if (newInstrumentation == null)
        {
            newInstrumentation =
                new Instrumentation(++m_idKeyValue, networkId, appInstanceId);
            m_instrumentations.add(newInstrumentation);
            InstrumentationCreation ntfcnObj =
                new InstrumentationCreation(newInstrumentation);
            setChanged();
            notifyObservers(ntfcnObj);
        }
        return newInstrumentation;
    }

    /**
     * @return
     * com.cisco.eManager.eManager.inventory.instrumentation.InstrumentationManager
     * @roseuid 3F1FF5F40319
     */
    public static synchronized InstrumentationManager instance()
    {
        if (s_instance == null)
        {
            s_instance = new InstrumentationManager();
        }
        return s_instance;
    }

    /**
     * @param id
     * @roseuid 3F215CC6025C
     */
    public synchronized void deleteInstrumentation(ManagedObjectId id)
    {
        logger.debug("enter");
        logger.debug("deleting instrumentation with ID " + id);
        Instrumentation instrumentation = null;
        Iterator iter = m_instrumentations.iterator();
        while (iter.hasNext())
        {
            instrumentation = (Instrumentation)iter.next();
            if (id.equals(instrumentation.id()))
            {
                iter.remove();
                logger.debug("instrumentation removed from collection");
                InstrumentationDeletion ntfcnObj =
                    new InstrumentationDeletion(instrumentation);
                setChanged();
                notifyObservers(ntfcnObj);
                return;
            }
        }
        logger.debug("unable to locate instrumentation in collection");
        return;
    }

    /**
     * @param id
     * @return com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation
     * @roseuid 3F3A7D550304
     */
    public synchronized Instrumentation find(ManagedObjectId id)
    {
        logger.debug("enter");
        logger.debug("searching for instrumentation with ID " + id);
        Instrumentation instrumentation = null;
        Iterator iter = m_instrumentations.iterator();
        while (iter.hasNext())
        {
            instrumentation = (Instrumentation)iter.next();
            if (instrumentation.id().equals(id))
            {
                logger.debug("instrumentation found");
                return instrumentation;
            }
        }
        logger.debug("unable to find instrumentation");
        return null;
    }

    public synchronized Instrumentation find (ManagedObjectId appInstanceId,
					      String uagentName)
    {
        Iterator iter;
        Collection uagents;
        Instrumentation instrumentation;

	if (appInstanceId == null ||
	    uagentName == null) {
	    return null;
	}

        iter = m_instrumentations.iterator();
        while (iter.hasNext())
        {
            instrumentation = (Instrumentation)iter.next();
            if (instrumentation.appInstanceId().equals(appInstanceId) &&
                instrumentation.networkId().name().equals(uagentName)) {
                return instrumentation;
            }
        }

        return null;
    }

    public synchronized Instrumentation find (AgentId agentId,
                                              String uagentName,
                                              String uagentInstance)
    {
        Iterator iter;
        Collection uagents;
        Instrumentation instrumentation;

	if (agentId == null ||
	    uagentName == null ||
	    uagentInstance == null) {
	    return null;
	}

        uagents = new LinkedList();
        iter = m_instrumentations.iterator();
        while (iter.hasNext())
        {
            instrumentation = (Instrumentation)iter.next();
            if (instrumentation.networkId().agentId().equals(agentId) &&
                instrumentation.networkId().name().equals(uagentName) &&
                instrumentation.networkId().instanceId().equals(uagentInstance)) {
                return instrumentation;
            }
        }

        return null;
    }

    /**
     * @return java.util.Vector
     * @roseuid 3F3A7D5E0068
     */
    public List instrumentations()
    {
        return m_instrumentations;
    }

    /**
     * @param networkId
     * @return com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation
     * @roseuid 3F6E00D50222
     */
    public synchronized Instrumentation find(MicroagentId networkId)
    {
        logger.debug("enter");
        logger.debug("searching for instrumentation with network ID " +
                     networkId);
        Instrumentation instrumentation = null;
        Iterator iter = m_instrumentations.iterator();
        while (iter.hasNext())
        {
            instrumentation = (Instrumentation)iter.next();
            if (instrumentation.networkId().equals(networkId) == true)
            {
                logger.debug("instrumentation found");
                return instrumentation;
            }
        }
        logger.debug("unable to find instrumentation");
        return null;
    }

    public synchronized List deleteByAppInstance(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        logger.debug("deleting instrumentation for appInstance " +
                     appInstanceId);
        Iterator iter = m_instrumentations.iterator();
        Instrumentation inst = null;
        List deletedInstrumentations = new LinkedList();
        while (iter.hasNext())
        {
            inst = (Instrumentation)iter.next();
            if (inst.appInstanceId().equals(appInstanceId))
            {
                deletedInstrumentations.add(inst);
                iter.remove();
                logger.debug("instrumentation with ID " + inst.id() +
                             " removed");
            }
        }
        logger.debug("removed " + deletedInstrumentations.size() +
                     " instrumentations");
        return deletedInstrumentations;
    }

    public synchronized List findByAppInstance(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        logger.debug("finding instrumentation for appInstance " + appInstanceId);
        Iterator iter = m_instrumentations.iterator();
        Instrumentation inst = null;
        List instrumentations = new LinkedList();
        while (iter.hasNext())
        {
            inst = (Instrumentation)iter.next();
            if (inst.appInstanceId().equals(appInstanceId))
            {
                instrumentations.add(inst);
                logger.debug("instrumentation with ID " + inst.id() +
                             " added to collection");
            }
        }
        logger.debug(instrumentations.size() + " found");
        return instrumentations;
    }

    public synchronized List findByName(String name)
    {
        logger.debug("enter");
        logger.debug("finding instrumentation by name " + name);
        Iterator iter = m_instrumentations.iterator();
        Instrumentation inst = null;
        List instrumentations = new LinkedList();
        while (iter.hasNext())
        {
            inst = (Instrumentation)iter.next();
            if (inst.registration().equals(name))
            {
                instrumentations.add(inst);
                logger.debug("instrumentation with ID " + inst.id() +
                             " added to collection");
            }
        }
        logger.debug(instrumentations.size() + " found");
        return instrumentations;
    }
}

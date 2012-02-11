//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\instrumentation\\Instrumentation.java

package com.cisco.eManager.eManager.inventory.instrumentation;

import java.util.*;
import org.apache.log4j.Logger;
import COM.TIBCO.hawk.console.hawkeye.AgentManager;
import COM.TIBCO.hawk.talon.MicroAgentDescriptor;
import COM.TIBCO.hawk.talon.MethodDescriptor;
import COM.TIBCO.hawk.talon.MicroAgentID;
import COM.TIBCO.hawk.talon.MicroAgentException;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.network.*;

public class Instrumentation
{
    private static Logger logger = Logger.getLogger(Instrumentation.class);
    private List m_methods;
    private MicroagentId m_networkId;
    private MicroAgentID m_microAgentID;
    private ManagedObjectId m_appInstanceId;
    private ManagedObjectId m_id;
    private boolean m_running;

    /**
     * @param networkId
     * @param name
     * @param idKeyValue
     * @param appInstanceId
     * @roseuid 3F4D086401A1
     */
    Instrumentation(long idKeyValue,
                    MicroagentId networkId,
                    ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        m_networkId = networkId;
        m_appInstanceId = appInstanceId;
        m_id = new ManagedObjectId(ManagedObjectIdType.Instrumentation,
                                   idKeyValue);
        activate();

        m_methods = Collections.synchronizedList(new LinkedList());
        retrieveInstrumentationMethods();
    }

    public boolean isActive()
    {
        return m_running;
    }

    public void activate()
    {
        logger.debug("enter");
        m_running = true;
    }

    public void deactivate()
    {
        logger.debug("enter");
        m_running = false;
    }

    /**
     * @return com.cisco.eManager.eManager.inventory.instrumentation.Id
     * @roseuid 3F3A7CDB02BA
     */
    public ManagedObjectId id()
    {
        return m_id;
    }

    /**
     * @return a vector of Method objects
     * @roseuid 3F2848880336
     */
    public synchronized List methods()
    {
        List methods;
        Iterator iter;
        methods = new LinkedList();
        synchronized (m_methods)
        {
            iter = m_methods.iterator();
            while (iter.hasNext())
            {
                methods.add(iter.next());
            }
        }

        return methods;
    }

    public synchronized Method method(String methodName)
    {
        logger.debug("enter");
        logger.debug("searching for method \"" + methodName + "\"");
        Method method = null;
        Iterator iter;
        synchronized (m_methods)
        {
            iter = m_methods.iterator();
            while (iter.hasNext())
            {
                method = (Method)iter.next();
                logger.debug("examining method \"" + method.name() + "\"");
                if ( method.name().equals(methodName) )
                {
                    logger.debug("method found");
                    return method;
                }
            }
        }
        logger.debug("method not found");
        return null;
    }

    /**
     * @return java.lang.String
     * @roseuid 3F28487C003F
     */
    public String registration()
    {
        return m_networkId.name();
    }

    /**
     * @return com.cisco.eManager.eManager.network.MicroagentId
     * @roseuid 3F60D66A0162
     */
    public MicroagentId networkId()
    {
        return m_networkId;
    }

    /**
     * @return com.cisco.eManager.eManager.inventory.appInstance.AppInstance
     * @roseuid 3F6E00C7009B
     */
    public AppInstance appInstance()
    {
        AppInstance appInstance =
            AppInstanceManager.instance().find(m_appInstanceId);
        return appInstance;
    }

    public ManagedObjectId appInstanceId()
    {
        return m_appInstanceId;
    }

    /**
     * @param msg
     * @return String
     * @roseuid 3F6E00C700E1
     */
    public static String appTypeName(AddMicroAgent msg)
    {
        logger.debug("enter");
        // given a TIB/Hawk message reporting the presence of a microagent,
        // invoke a method against it to obtain the appTypeName that it is
        // supposed to expose
        return null;
    }

    /**
     * @param msg
     * @return String
     * @roseuid 3F6E00C800E3
     */
    public static String appInstanceId(AddMicroAgent msg)
    {
        logger.debug("enter");
        // given a TIB/Hawk message reporting the presence of a microagent,
        // invoke a method against it to obtain the appInstanceId that it is
        // supposed to expose
        return null;
    }

    private synchronized void retrieveInstrumentationMethods()
    {
        Host host;
        Method method;
        AppInstance appInstance;
        MethodDescriptor methodDescriptors[];
        MicroAgentDescriptor microAgentDescriptor;

        appInstance = appInstance();
        if (appInstance == null)
        {
            logger.error("no appInstance for this instrumentation");
            return;
        }

        host = appInstance.host();
        microAgentDescriptor = null;
        try
        {
            microAgentDescriptor = host.agentManager().describe(m_networkId.raw());
        }
        catch (MicroAgentException e)
        {
            String logString;

            logString = new String("Error retrieving the instrumentation for microagent-" +
                                   m_networkId.displayName() +
                                   " on host-" +
                                   host.name() +
                                   " : " +
                                   e.getMessage());

            logger.error(logString);
            // fix
            // decide what we want to do here.
            return;
        }

        methodDescriptors = microAgentDescriptor.getMethodDescriptors();
        synchronized (m_methods)
        {
            for (int i = 0; i < methodDescriptors.length; i++)
            {
                method = new Method(methodDescriptors[i], this);
                m_methods.add(method);
            }
        }
    }

    public boolean equals(Object object)
    {
        if (object instanceof Instrumentation)
        {
            Instrumentation instr;

            instr = (Instrumentation)object;
            if (m_microAgentID.equals(instr.m_microAgentID) == true)
            {
                return true;
            }
        }
        return false;
    }
}

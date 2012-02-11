//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\appInstance\\AppInstance.java

package com.cisco.eManager.eManager.inventory.appInstance;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.AppInstanceMgmtMode;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.common.inventory.Transport;
import com.cisco.eManager.eManager.inventory.appType.*;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.*;
import com.cisco.eManager.eManager.inventory.instrumentation.*;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.inventory.procSeq.*;
import com.cisco.eManager.eManager.network.*;
import com.cisco.eManager.eManager.process.ProcessManager;
import com.cisco.eManager.common.exception.*;

public class AppInstance
{
    private static Logger logger = Logger.getLogger(AppInstance.class);
    private AppInstanceData m_data;

    AppInstance(AppInstanceData data)
    {
        m_data = data;
    }

    /**
     * @return the primary key for an instance of this class.  It uniquely identifies
     * one instance from all others within eManager.
     * @roseuid 3F2844B60128
     */
    public ManagedObjectId id()
    {
        return m_data.id();
    }

    /**
     * @return the collection of associated Instrumentation objects
     * @roseuid 3F2844DE000E
     */
    public List instrumentations()
    {
        logger.debug("enter");
        List instrumentations =
            InstrumentationManager.instance().findByAppInstance(m_data.id());
        return instrumentations;
    }

    public Instrumentation instrumentation(String instrumentationName)
    {
        logger.debug("enter");
        logger.debug("searching for instrumentation with name \"" +
                     instrumentationName + "\"");
        List instrumentations = instrumentations();
        Iterator iter = instrumentations.iterator();
        Instrumentation instrumentation = null;
        while (iter.hasNext())
        {
            instrumentation = (Instrumentation)iter.next();
            logger.debug("examining instrumentation \"" +
                         instrumentation.registration() + "\"");
            if (instrumentation.registration().equals(instrumentationName))
            {
                logger.debug("instrumentation found");
                return instrumentation;
            }
        }
        logger.debug("instrumentation not found");
        return null;
    }

    /**
     * @return com.cisco.eManager.eManager.inventory.host.Host
     * @roseuid 3F2846A3015D
     */
    public ManagedObjectId hostId()
    {
        return m_data.hostId();
    }

    /**
     * @return com.cisco.eManager.eManager.inventory.procSeq.AppsGroup
     * @roseuid 3F327CAF00E4
     */
    public AppsGroup processGroups()
    {
        return null;
    }

    /**
     * An application instance is either managed or not managed.  This method tells
     * you if it is managed.
     * @return boolean (true or false), where true indicates that this application
     * instance is currently "managed" by eManager
     * @roseuid 3F4A1D060275
     */
    public boolean managed()
    {
        return m_data.mgmtMode().isManaged();
    }

    public boolean running()
    {
        List instrumentations = instrumentations();
        if ( instrumentations.isEmpty() )
        {
            // an appInstance with no instrumentations is not process-sequencer-
            // enabled and cannot be deemed to be "not running" (we just don't
            // know!...)
            return true;
        }

        Iterator iter = instrumentations.iterator();
        Instrumentation instrumentation = null;
        while (iter.hasNext())
        {
            instrumentation = (Instrumentation)iter.next();
            if ( instrumentation.isActive() )
            {
                logger.debug("appInstance \"" + name() + "\" is running - " +
                             "instrumentation \"" +
                             instrumentation.registration() + "\" is active");
                return true;
            }
        }
        return false;
    }

    /**
     * @return the application instance ID provided during registration or by eManager
     * itself when not provided during registration.  This string must be unique
     * across all application instances across all application types.
     * @roseuid 3F4E3170035A
     */
    public String name()
    {
        return m_data.name();
    }

    synchronized void manage()
    {
        m_data.mgmtMode(AppInstanceMgmtMode.MANAGED);
        MgmtPolicyManager.instance().loadMgmtPolicies(appType(), host());
    }

    synchronized void unmanage()
    {
        m_data.mgmtMode(AppInstanceMgmtMode.UNMANAGED);
        MgmtPolicyManager.instance().unloadMgmtPolicies(appType(), host());
    }

    public synchronized void start()
        throws Exception
    {
        if ( !managed() )
        {
            String reason = "unable to start an unmanaged appInstance";
            logger.info(reason);
            throw new Exception(reason);
        }
        logger.info("starting appInstance \"" + name() + "\"");
        MgmtPolicyManager.instance().loadMgmtPolicies(appType(), host());
    }

    public synchronized void stop()
        throws Exception
    {
        if ( !managed() )
        {
            String reason = "unable to stop an unmanaged appInstance";
            logger.info(reason);
            throw new Exception(reason);
        }
        logger.info("stopping appInstance \"" + name() + "\"");
        MgmtPolicyManager.instance().unloadMgmtPolicies(appType(), host());
    }

    /**
     * @return com.cisco.eManager.eManager.inventory.host.Host
     * @roseuid 3F68EE3C0285
     */
    public Host host()
    {
        return HostManager.instance().find(m_data.hostId());
    }

    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectId
     * @roseuid 3F68EE3D00A6
     */
    public ManagedObjectId appTypeId()
    {
        return m_data.appTypeId();
    }

    /**
     * @return com.cisco.eManager.eManager.inventory.appType.AppType
     * @roseuid 3F68EE3D02D7
     */
    public AppType appType()
    {
        return AppTypeManager.instance().find(m_data.appTypeId());
    }

    public String propertyFile()
    {
        return m_data.propertyFile();
    }

    synchronized void propertyFile(String propertyFile)
    {
        m_data.propertyFile(propertyFile);
    }

    synchronized public String unixPrompt()
    {
        return m_data.transportUnixPrompt();
    }

    synchronized void unixPrompt(String unixPrompt)
    {
        m_data.transportUnixPrompt(unixPrompt);
    }

    synchronized void logfileDirectories(String logfileDirectories)
    {
        m_data.logfileDirectories(logfileDirectories);
    }

    public String logfileDirectories()
    {
        return m_data.logfileDirectories();
    }

    public void logfileDirectorySet(String[] logfileDirectories)
    {
        StringBuffer lds = new StringBuffer();
        int count = logfileDirectories.length;
        for (int i = 0; i < count; i++)
        {
            if ( i > 0 )
            {
                lds.append(';');
            }
            lds.append(logfileDirectories[i].trim());
            logger.debug("lds = " + lds.toString());
        }
        logfileDirectories(lds.toString());
    }

    public String[] logfileDirectorySet()
    {
        String lds = logfileDirectories();
        logger.debug("lds = " + lds);
        StringTokenizer st = new StringTokenizer(lds, ";");
        int size = st.countTokens();
        String[] retval = new String[size];
        for (int i = 0; i < size; i++)
        {
            retval[i] = st.nextToken();
        }
        return retval;
    }

    synchronized void logfilePassword(String logfilePassword)
    {
        m_data.transportPassword(logfilePassword);
    }

    public String logfilePassword()
    {
        return m_data.transportPassword();
    }

    synchronized void logfileUsername(String logfileUsername)
    {
        m_data.transportUsername(logfileUsername);
    }

    public String logfileUsername()
    {
        return m_data.transportUsername();
    }

    synchronized void logfileTransport(Transport xport)
    {
        m_data.logfileTransport(xport);
    }

    public Transport logfileTransport()
    {
        return m_data.logfileTransport();
    }

    public String toString()
    {
	return m_data.toString();
    }
}

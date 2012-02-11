//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\host\\HostManager.java

package com.cisco.eManager.eManager.inventory.host;

import java.util.*;
import org.apache.log4j.Logger;
import COM.TIBCO.hawk.console.hawkeye.*;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.eManager.database.DatabaseInterface;
import com.cisco.eManager.eManager.inventory.view.ContainerNode;
import com.cisco.eManager.eManager.network.*;
import com.cisco.eManager.common.database.*;

public class HostManager
    extends Observable
{
    private static Logger logger = Logger.getLogger(HostManager.class);
    private static HostManager s_instance;
    private DatabaseInterface m_db;
    private List m_hosts;

    /**
     * @return com.cisco.eManager.eManager.inventory.host.HostManager
     * @roseuid 3F1FF54C028B
     */
    public static synchronized HostManager instance()
    {
        if (s_instance == null)
        {
            s_instance = new HostManager();
        }
        return s_instance;
    }

    /**
     * @roseuid 3F4D083D0367
     */
    private HostManager()
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
        m_hosts = Collections.synchronizedList(new LinkedList());
        initializeCacheFromDb();
    }

    /**
     * @param networkId
     * @return com.cisco.eManager.eManager.inventory.host.Host
     * @roseuid 3F0AE89000FA
     */
    public synchronized Host createHost(AgentInstance agentInstance,
                                        AgentManager agentManager)
    {
        logger.debug("enter");
        AgentId networkId = new AgentId(agentInstance.getAgentID());
        Host newHost = findByIpAddress(agentInstance.getIPAddress());
        if (newHost == null)
        {
            HostData hostData = null;
            try
            {
                hostData =
                    m_db.createAgent(agentInstance.getIPAddress(),
                                     agentInstance.getAgentID().getName(),
                                     agentInstance.getAgentID().getHawkDomain());
            }
            catch (EmanagerDatabaseException e)
            {
                logger.error("exception caught while creating a host: " + e);
                return null;
            }
            newHost = new Host(hostData);
            newHost.activate(agentInstance, agentManager);
            m_hosts.add(newHost);
            logger.info("host created: name = " + newHost.name());
            logger.debug("notifying observers creation");
            HostNotification ntfcnObj =
                new HostNotification(HostNotificationType.CREATE, newHost);
            setChanged();
            notifyObservers(ntfcnObj);
            logger.debug("observers notified of creation");
        }
        else
        {
            logger.info("host \"" + newHost.name() + "\" already exists");
            activateHost(newHost, agentInstance, agentManager);
        }
        return newHost;
    }

    public void activateHost(Host host,
                             AgentInstance agentInstance,
                             AgentManager agentManager)
    {
        host.activate(agentInstance, agentManager);
        logger.debug("notifying observers of activation");
        HostNotification ntfcnObj =
            new HostNotification(HostNotificationType.ACTIVATE, host);
        setChanged();
        notifyObservers(ntfcnObj);
        logger.debug("observers notified of activation");
    }

    /**
     * @param hostId
     * @roseuid 3F214EBD01D4
     */
    public synchronized void deleteHost(ManagedObjectId hostId)
    {
        logger.debug("enter");
        logger.debug("deleting host with ID " + hostId);
        Host host = null;
        Iterator iter = m_hosts.iterator();
        while (iter.hasNext())
        {
            host = (Host)iter.next();
            if (host.id().equals(hostId))
            {
                logger.debug("host found: removing it");
                HostNotification hn =
                    new HostNotification(HostNotificationType.PRE_DELETE, host);
                setChanged();
                notifyObservers(hn);
                try
                {
                    m_db.removeAgent(host);
                    logger.debug("host removed from db");
                }
                catch (EmanagerDatabaseException ex)
                {
                    logger.error("exception caught while removing host " +
                                 "from DB: " + ex);
                    logger.error("notifying observers that host wasn't " +
                                 "really deleted");
                    hn = new HostNotification(HostNotificationType.UNDELETE,
                                              host);
                    setChanged();
                    notifyObservers(hn);
                    return;
                }
                iter.remove();
                logger.debug("host deleted");
                hn = new HostNotification(HostNotificationType.DELETE, host);
                setChanged();
                notifyObservers(hn);
                logger.info("host deleted: name = " + host.name());
                return;
            }
        }
        logger.debug("host not found in collection");
    }

    /**
     * @param hostId
     * @return com.cisco.eManager.eManager.inventory.host.Host
     * @roseuid 3F3A781B031B
     */
    public synchronized Host find(ManagedObjectId hostId)
    {
        logger.debug("finding host with ID " + hostId);
        Host host = null;
        Iterator iter = m_hosts.iterator();
        while (iter.hasNext())
        {
            host = (Host)iter.next();
            if (host.id().equals(hostId))
            {
                return host;
            }
        }
        return null;
    }

    public synchronized Host findByIpAddress(String ipAddress)
    {
        logger.debug("finding host with IP address " + ipAddress);
        Host host = null;
        Iterator iter = m_hosts.iterator();
        while (iter.hasNext())
        {
            host = (Host)iter.next();
            if (host.ipAddress().equals(ipAddress))
            {
                return host;
            }
        }
        return null;
    }

    /**
     * @return java.util.Vector
     * @roseuid 3F3A78230150
     */
    public List hosts()
    {
        return m_hosts;
    }

    public synchronized Host[] allHosts()
    {
        int hostCount = m_hosts.size();
        Host[] hosts = new Host[hostCount];
        Iterator iter = m_hosts.iterator();
        for (int i = 0; i < hostCount; i++)
        {
            hosts[i] = (Host)iter.next();
        }
        return hosts;
    }

    public synchronized Host find(String name)
    {
        logger.debug("enter");
        Host host = null;
        Iterator iter = m_hosts.iterator();
        while (iter.hasNext())
        {
            host = (Host)iter.next();
            if (name.equals(host.name()))
            {
                return host;
            }
        }
        return null;
    }

    private void initializeCacheFromDb()
    {
        logger.debug("enter");
        Collection hostDatas = null;
        try
        {
            hostDatas = m_db.retrieveAgents();
        }
        catch (EmanagerDatabaseException e)
        {
            logger.error("exception caught while initializing cache from db: " +
                         e);
            return;
        }
        Iterator iter = hostDatas.iterator();
        HostData hostData = null;
        Host host = null;
        HostNotification hn = null;
        while (iter.hasNext())
        {
            hostData = (HostData)iter.next();
            host = new Host(hostData);
            m_hosts.add(host);
            logger.debug("added \"" + host.name() + "\" to the cache");
            logger.debug("added \"" + host.id().getManagedObjectKey() + "\" to the cache");
            logger.debug("added \"" + host.ipAddress() + "\" to the cache");
            hn = new HostNotification(HostNotificationType.RESTORE, host);
            setChanged();
            notifyObservers(hn);
        }
    }
}

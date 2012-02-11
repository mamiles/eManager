//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\mgmtPolicy\\MgmtPolicyManager.java

package com.cisco.eManager.eManager.inventory.mgmtPolicy;

import java.util.*;
import org.apache.log4j.Logger;
import COM.TIBCO.hawk.talon.*;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.InventoryGlobals;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.appType.*;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.inventory.instrumentation.*;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.*;
import com.cisco.eManager.eManager.network.ManagementPolicyId;

import com.cisco.eManager.eManager.database.DatabaseInterface;
import com.cisco.eManager.common.database.EmanagerDatabaseException;

public class MgmtPolicyManager
    extends Observable
{
    private static Logger logger = Logger.getLogger(MgmtPolicyManager.class);
    private static MgmtPolicyManager s_instance;
    private DatabaseInterface m_db;
    private List m_mgmtPolicies;

    /**
     * @return com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicyManager
     * @roseuid 3F1FF70B0130
     */
    public static synchronized MgmtPolicyManager instance()
    {
        logger.debug("enter");
        if (s_instance == null)
        {
            try
            {
                s_instance = new MgmtPolicyManager();
            }
            catch (Exception e)
            {
                logger.fatal("exception thrown in ctor: " + e);
                s_instance = null;
            }
        }
        return s_instance;
    }

    /**
     * @roseuid 3F4D088E0257
     */
    private MgmtPolicyManager()
        throws Exception
    {
        logger.debug("enter");
        try
        {
            m_db = DatabaseInterface.instance();
        }
        catch (EmanagerDatabaseException e)
        {
            String reason = "exception caught while initializing the DB: " + e;
            logger.error(reason);
            throw new Exception(reason);
        }
        m_mgmtPolicies = Collections.synchronizedList(new LinkedList());
        initializeCacheFromDb();
    }

    /**
     * @param mpId
     * @param path
     * @param hostId
     * @param appTypeId
     * @param networkId
     * @return com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy
     * @roseuid 3F0495EA026E
     */
    public synchronized MgmtPolicy createPolicy(ManagementPolicyId networkId,
                                                String path,
                                                ManagedObjectId hostId,
                                                ManagedObjectId appTypeId,
                                                boolean loaded)
    {
        logger.debug("enter");
        String reason = null;
        MgmtPolicy newMgmtPolicy;
        ManagedObjectId id = null;
        MgmtPolicyData data = null;

        newMgmtPolicy = find(networkId.name(), hostId);
        if (newMgmtPolicy == null)
        {
            // create in db first (this gets us the mgmtPolicy ID)
            try
            {
                id = m_db.createManagementPolicy(networkId.name(),
                                                 path,
                                                 appTypeId,
                                                 hostId);
            }
            catch (Exception e)
            {
                reason = "exception caught while creating mgmtPolicy in DB: " +
                         e;
                logger.error(reason);
                return null;
            }

            try
            {
                data = new MgmtPolicyData(id,
                                          networkId.name(),
                                          path,
                                          appTypeId,
                                          hostId);
            }
            catch (Exception ex)
            {
                logger.error("exception caught creating mgmtPolicy data: " +
                             ex);
                return null;
            }

            newMgmtPolicy = new MgmtPolicy(data);
            try
            {
                newMgmtPolicy.activate(networkId);
                newMgmtPolicy.setLoaded(true);
            }
            catch (Exception ex1)
            {
                reason = "exception caught while activating mgmtPolicy: " + ex1;
                logger.error(reason);
            }
            m_mgmtPolicies.add(newMgmtPolicy);
            PolicyCreation ntfcnObj = new PolicyCreation(newMgmtPolicy);
            setChanged();
            notifyObservers(ntfcnObj);
        }
        else
        {
            logger.info("MgmtPolicy \"" + newMgmtPolicy.name() +
                        "\" already exists.");
            activate(newMgmtPolicy, networkId);
        }
        return newMgmtPolicy;
    }

    public void activate(MgmtPolicy mgmtPolicy, ManagementPolicyId policyId)
    {
        logger.debug("enter");
        String reason = null;
        // We assume it is loaded at this point
        try
        {
            mgmtPolicy.activate(policyId);
            mgmtPolicy.setLoaded(true);
        }
        catch (Exception ex)
        {
            reason = "exception caught while activating mgmtPolicy: " + ex;
            logger.error(reason);
            return;
        }
        logger.debug("notifying objservers of activation: " + mgmtPolicy.name());
        MgmtPolicyActivation ntfcnObj = new MgmtPolicyActivation(mgmtPolicy);
        setChanged();
        notifyObservers(ntfcnObj);
    }

    /**
     * @param mgmtPolicyId
     * @roseuid 3F215D7A0125
     */
    public synchronized void deletePolicy(ManagedObjectId mgmtPolicyId)
    {
        logger.debug("enter");
        logger.debug("removing mgmtPolicy from collection: mgmtPolicyId = " +
                     mgmtPolicyId);
        MgmtPolicy mgmtPolicy = null;
        Iterator iter = m_mgmtPolicies.iterator();
        while (iter.hasNext())
        {
            mgmtPolicy = (MgmtPolicy)iter.next();
            if (mgmtPolicy.id().equals(mgmtPolicyId))
            {
                iter.remove();
                logger.debug("mgmtPolicy removed from collection");
                PolicyDeletion ntfcnObj = new PolicyDeletion(mgmtPolicy);
                setChanged();
                notifyObservers(ntfcnObj);
                try
                {
                    DatabaseInterface.instance().removeManagementPolicy(mgmtPolicy);
                }
                catch (EmanagerDatabaseException e)
                {
                    logger.error("Error encountered removing persistent MgmtPolicy:" + mgmtPolicy.id());
                }

                return;
            }
        }

        logger.debug("mgmtPolicy not found in collection");
        return;
    }

    /**
     * @param mgmtPolicyId
     * @return com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy
     * @roseuid 3F3A608A008F
     */
    public synchronized MgmtPolicy find(ManagedObjectId mgmtPolicyId)
    {
        logger.debug("enter");
        logger.debug("finding mgmtPolicy in collection: mgmtPolicyId = " +
                     mgmtPolicyId);
        MgmtPolicy mgmtPolicy = null;
        Iterator iter = m_mgmtPolicies.iterator();
        while (iter.hasNext())
        {
            mgmtPolicy = (MgmtPolicy)iter.next();
            if (mgmtPolicy.id().equals(mgmtPolicyId))
            {
                logger.debug("mgmtPolicy found in collection - returning it");
                return mgmtPolicy;
            }
        }
        logger.debug("mgmtPolicy not found in collection");
        return null;
    }

    public synchronized MgmtPolicy find(ManagementPolicyId networkId)
    {
        logger.debug("enter");
        logger.debug("finding mgmtPolicy in collection: networkId = " +
                     networkId);
        String mpName = networkId.name();
        Host host = HostManager.instance().find(networkId.agentId().name());
        if ( host == null )
        {
            logger.warn("unable to find host: " + networkId.agentId().name());
	    return null;
        }

        return find(mpName, host.id());
    }

    /**
     * @param name the management policy name - there can be one of these per host
     * @return java.util.Vector
     * @roseuid 3F3A7F9402CF
     */
    public synchronized List mgmtPolicies(String name)
    {
        logger.debug("enter");
        logger.debug("finding management policies in collection: name = " +
                     name);
        List mgmtPolicies = new LinkedList();
        MgmtPolicy mgmtPolicy = null;
        Iterator iter = m_mgmtPolicies.iterator();
        while (iter.hasNext())
        {
            mgmtPolicy = (MgmtPolicy)iter.next();
            if (mgmtPolicy.name().equals(name))
            {
                mgmtPolicies.add(mgmtPolicy);
            }
        }
        logger.debug(mgmtPolicies.size() + " management policies found");
        return mgmtPolicies;
    }

    public synchronized MgmtPolicy[] mgmtPolicies()
    {
        int mgmtPolicyCount = m_mgmtPolicies.size();
        MgmtPolicy[] mgmtPolicies = new MgmtPolicy[mgmtPolicyCount];
        Iterator iter = m_mgmtPolicies.iterator();
        for (int i = 0; i < mgmtPolicyCount; i++)
        {
            mgmtPolicies[i] = (MgmtPolicy)iter.next();
        }
        return mgmtPolicies;
    }

    /**
     * @param managementPolicyId
     * @param networkId
     * @return com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy
     * @roseuid 3F60C70D00E6
     */
    /*
    public synchronized MgmtPolicy find(ManagementPolicyId networkId)
    {
        logger.debug("enter");
        logger.debug("finding mgmtPolicy in collection: networkId = " +
                     networkId);
        MgmtPolicy mgmtPolicy = null;
        Iterator iter = m_mgmtPolicies.iterator();
        while (iter.hasNext())
        {
            mgmtPolicy = (MgmtPolicy)iter.next();
            if (networkId.equals(mgmtPolicy.networkId()))
            {
                logger.debug("mgmtPolicy found in collection - returning it");
                return mgmtPolicy;
            }
        }
        logger.debug("mgmtPolicy not found in collection");
        return null;
    }
*/

    /**
     * @param name
     * @param hostId
     * @return com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy
     * @roseuid 3F6720D602C2
     */
    public synchronized MgmtPolicy find(String name, ManagedObjectId hostId)
    {
        logger.debug("enter");
        logger.debug("finding management policies in collection: name = " +
                     name + ", hostId = " + hostId);
        MgmtPolicy mgmtPolicy = null;
        Iterator iter = m_mgmtPolicies.iterator();
        while (iter.hasNext())
        {
            mgmtPolicy = (MgmtPolicy)iter.next();
            if ((mgmtPolicy.name().equals(name)) &&
                (mgmtPolicy.host().id().equals(hostId)))
            {
                logger.debug("found management policy");
                return mgmtPolicy;
            }
        }
        logger.debug("management policy not found");
        return null;
    }

    /**
     * @param mp
     * @param mpId
     * @roseuid 3F6720D70206
     */
    public void setNetworkId(MgmtPolicy mgmtPolicy, ManagementPolicyId mpId)
    {
        ManagementPolicyId oldId;

        // fix
        // need to address fact that managementpolicyid and mgmtpolicy both
        // have name members.  Need to make sure they are consistent.

        oldId = mgmtPolicy.networkId();

        mgmtPolicy.setNetworkId(mpId);

        try
        {
            DatabaseInterface.instance().updateManagementPolicy(mgmtPolicy);
        }
        catch (EmanagerDatabaseException e)
        {
            logger.error("Exception encountered changing network id on MgmtPolicy:" + mgmtPolicy.id());
            mgmtPolicy.setNetworkId(oldId);
        }

        return;
    }

    /**
     * @param hostId
     * @param appTypeId
     * @return java.util.Vector
     * @roseuid 3F68EEB80037
     */
    public synchronized List find(ManagedObjectId hostId, ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        logger.debug("finding management policies in collection: hostId = " +
                     hostId + ", appTypeId = " + appTypeId);
        List mgmtPolicies = new LinkedList();
        MgmtPolicy mgmtPolicy = null;
        Iterator iter = m_mgmtPolicies.iterator();
        while (iter.hasNext())
        {
            mgmtPolicy = (MgmtPolicy)iter.next();
            if ((mgmtPolicy.host().id().equals(hostId)) &&
                (mgmtPolicy.appType().id().equals(appTypeId)))
            {
                mgmtPolicies.add(mgmtPolicy);
            }
        }
        logger.debug(mgmtPolicies.size() + " management policies found");
        return mgmtPolicies;
    }

    /**
     * @param appTypeId
     * @return java.util.Vector
     * @roseuid 3F68EEB900A6
     */
    public synchronized List findByAppTypeId(ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        logger.debug("finding management policies in collection: appTypeId = " +
                     appTypeId);
        List mgmtPolicies = new LinkedList();
        MgmtPolicy mgmtPolicy = null;
        Iterator iter = m_mgmtPolicies.iterator();
        while (iter.hasNext())
        {
            mgmtPolicy = (MgmtPolicy)iter.next();
            if (mgmtPolicy.appType().id().equals(appTypeId))
            {
                mgmtPolicies.add(mgmtPolicy);
            }
        }
        logger.debug(mgmtPolicies.size() + " management policies found");
        return mgmtPolicies;
    }

    public synchronized MgmtPolicy[] findByAppTypeId_preferred(ManagedObjectId appTypeId)
    {
        List mgmtPolicyList = findByAppTypeId(appTypeId);
        int mgmtPolicyCount = mgmtPolicyList.size();
        Iterator iter = mgmtPolicyList.iterator();
        MgmtPolicy[] mgmtPolicies = new MgmtPolicy[mgmtPolicyCount];
        for (int i = 0; i < mgmtPolicyCount; i++)
        {
            mgmtPolicies[i] = (MgmtPolicy)iter.next();
        }
        return mgmtPolicies;
    }

    /**
     * @param hostId
     * @return java.util.Vector
     * @roseuid 3F68EEBA00E4
     */
    public synchronized List findByHostId(ManagedObjectId hostId)
    {
        logger.debug("enter");
        logger.debug("finding management policies in collection: hostId = " +
                     hostId);
        List mgmtPolicies = new LinkedList();
        MgmtPolicy mgmtPolicy = null;
        Iterator iter = m_mgmtPolicies.iterator();
        while (iter.hasNext())
        {
            mgmtPolicy = (MgmtPolicy)iter.next();
            if (mgmtPolicy.host().id().equals(hostId))
            {
                mgmtPolicies.add(mgmtPolicy);
            }
        }
        logger.debug(mgmtPolicies.size() + " management policies found");
        return mgmtPolicies;
    }

    public synchronized MgmtPolicy[] findByHostId_preferred(ManagedObjectId hostId)
    {
        List mgmtPolicyList = findByAppTypeId(hostId);
        int mgmtPolicyCount = mgmtPolicyList.size();
        Iterator iter = mgmtPolicyList.iterator();
        MgmtPolicy[] mgmtPolicies = new MgmtPolicy[mgmtPolicyCount];
        for (int i = 0; i < mgmtPolicyCount; i++)
        {
            mgmtPolicies[i] = (MgmtPolicy)iter.next();
        }
        return mgmtPolicies;
    }

    public void setLoaded(MgmtPolicy mgmtPolicy, boolean loaded)
    {
        logger.debug("enter");
        mgmtPolicy.setLoaded(loaded);
        return;
    }

    public void mapToAppType(MgmtPolicy mgmtPolicy, AppType appType)
    {
        String reason = null;
        ManagedObjectId oldAppTypeId = mgmtPolicy.appTypeId();
        mgmtPolicy.setAppTypeId(appType.id());
        try
        {
            m_db.updateManagementPolicy(mgmtPolicy);
        }
        catch (EmanagerDatabaseException e)
        {
            mgmtPolicy.setAppTypeId(oldAppTypeId);
            reason = "exception caught while updating mgmtPolicy in DB: " + e;
            logger.error(reason);
        }
    }

    public void setPathname(MgmtPolicy mgmtPolicy, String pathname)
    {
        String reason = null;
        String oldPath = mgmtPolicy.pathname();
        mgmtPolicy.setPathname(pathname);
        try
        {
            m_db.updateManagementPolicy(mgmtPolicy);
        }
        catch (EmanagerDatabaseException e)
        {
            mgmtPolicy.setPathname(oldPath);
            reason = "exception caught while updating mgmtPolicy in DB: " + e;
            logger.error(reason);
        }
    }

    private void initializeCacheFromDb()
    {
        Iterator iter;
        MgmtPolicy mgmtPolicy;
        MgmtPolicyData policyData;
        Collection mgmtPolicies;

        logger.debug("enter");

        mgmtPolicies = null;
        try
        {
            mgmtPolicies = DatabaseInterface.instance().retrieveManagementPolicies();
        }
        catch (EmanagerDatabaseException e)
        {
            logger.error("exception caught while initializing cache from db: " +
                         e);
            return;
        }
        if (mgmtPolicies != null &&
            mgmtPolicies.size() != 0)
        {
            iter = mgmtPolicies.iterator();
            while (iter.hasNext())
            {
                policyData = (MgmtPolicyData)iter.next();
                mgmtPolicy = new MgmtPolicy(policyData);
                m_mgmtPolicies.add(mgmtPolicy);
                logger.debug("added \"" + mgmtPolicy.name() + "\" to the cache");
                MgmtPolicyRestoration ntfcnObj = new MgmtPolicyRestoration(mgmtPolicy);
                setChanged();
                notifyObservers(ntfcnObj);
            }
        }
    }

    public void loadMgmtPolicies(AppType appType, Host host)
    {
        logger.debug("enter");
        // make sure each of this appType's mgmtPolicies on this host are loaded
        // - first, get the method through which we do the loading
        // - then, do the work
        HostResourcesAppInstance hostResourcesAppInstance =
            host.hostResources();
        if ( hostResourcesAppInstance == null )
        {
            logger.warn("unable to locate HostResourcesAppInstance");
            return;
        }

        List mgmtPolicies = find(host.id(), appType.id());
        Iterator iter = mgmtPolicies.iterator();
        MgmtPolicy mgmtPolicy = null;
        while ( iter.hasNext() )
        {
            mgmtPolicy = (MgmtPolicy)iter.next();
            if ( !mgmtPolicy.isLoaded() )
            {
                //load(mgmtPolicy, mgmtPolicyLoadMethod);
                hostResourcesAppInstance.loadMgmtPolicy(mgmtPolicy.pathname());
            }
        }
    }

    public void unloadMgmtPolicies(AppType appType, Host host)
    {
        logger.debug("enter");
        // if this appType has no active (managed and running) appInstances on
        // this host, then make sure all of its associated mgmtPolicies are
        // unloaded on that host
        boolean unload = true; // we'll unload unless we can find a reason not to
        List appInstances = host.appInstances(appType.id());
        Iterator iter = appInstances.iterator();
        AppInstance appInstance = null;
        while (iter.hasNext())
        {
            appInstance = (AppInstance)iter.next();
            if ( appInstance.managed() && appInstance.running() )
            {
                logger.debug("keeping " + appType.name() + " mgmtPolicies " +
                             "on " + host.name() + " loaded because " +
                             "of appInstance " + appInstance.name());
                unload = false;
                break;
            }
        }

        if ( unload )
        {
            HostResourcesAppInstance hostResourcesAppInstance =
                host.hostResources();
            if ( hostResourcesAppInstance == null )
            {
                logger.warn("unable to locate HostResourcesAppInstance");
                return;
            }

            List mgmtPolicies = find(host.id(), appType.id());
            iter = mgmtPolicies.iterator();
            MgmtPolicy mgmtPolicy = null;
            while ( iter.hasNext() )
            {
                mgmtPolicy = (MgmtPolicy)iter.next();
                if ( mgmtPolicy.isLoaded() )
                {
                    hostResourcesAppInstance.unloadMgmtPolicy(mgmtPolicy.name());
                }
            }
        }
    }
}
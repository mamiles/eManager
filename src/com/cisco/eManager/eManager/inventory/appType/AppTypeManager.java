//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\appType\\AppTypeManager.java

package com.cisco.eManager.eManager.inventory.appType;

import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.appType.*;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.*;

import com.cisco.eManager.eManager.database.DatabaseInterface;

import com.cisco.eManager.common.database.EmanagerDatabaseException;

import com.cisco.eManager.eManager.network.*;
import java.util.*;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import org.apache.log4j.Logger;

public class AppTypeManager extends Observable
{
    private static Logger logger = Logger.getLogger(AppTypeManager.class);
    private static AppTypeManager s_instance = null;
    private DatabaseInterface m_db;
    private HostManager m_hm;
    private List m_appTypes;


    /**
     * @roseuid 3F4D081E01B3
     */
    private AppTypeManager()
        throws Exception
    {
        logger.debug("enter");

        m_db = DatabaseInterface.instance();

        m_appTypes = Collections.synchronizedList(new LinkedList());
	initializeCacheFromDb();
    }

    /**
     * @param name
     * @param version
     * @return com.cisco.eManager.eManager.inventory.appType.AppType
     * @roseuid 3F049854025B
     */
    public synchronized AppType createAppType(String name, String version)
    {
        AppType newAppType;
	ManagedObjectId id;

        logger.debug("enter");

	newAppType = find(name);
        if ( newAppType == null )
        {
            newAppType = new AppType(0, name, version);
	    try {
		id = m_db.createApplicationType (newAppType);

		newAppType = new AppType (id.getManagedObjectKey(),
					  newAppType.name(),
					  newAppType.version());

		m_appTypes.add(newAppType);
		AppTypeCreation ntfcnObj = new AppTypeCreation(newAppType);
		setChanged();
		notifyObservers(ntfcnObj);
	    }
	    catch (EmanagerDatabaseException e) {
		logger.error ("Exception encountered creating persistent new AppType: " + e.getMessage());
		newAppType = null;
	    }
	}

        return newAppType;
    }

    /**
     * @return eManagerLayer.InventoryMgmt.ApplicationTypeManager
     * @roseuid 3F0AE07302AC
     */
    public static synchronized AppTypeManager instance()
    {
        if (s_instance == null)
        {
            try
            {
                s_instance = new AppTypeManager();
            }
            catch (Exception e)
            {
                logger.fatal("Exception thrown in ctor: e = " + e);
                s_instance = null;
            }
        }
        return s_instance;
    }

    /**
     * @param appTypeId
     * @roseuid 3F21510102B5
     */
    public synchronized void deleteAppType(ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        logger.debug("deleting appType with ID " + appTypeId);
        Iterator iter = m_appTypes.iterator();
        AppType appType = null;
        while ((appType = (AppType)iter.next()) != null)
        {
            if (appType.id().equals(appTypeId))
            {
                logger.debug("appType found: removing it");
                iter.remove();
                AppTypeDeletion ntfcnObj = new AppTypeDeletion(appType);
                setChanged();
                notifyObservers(ntfcnObj);

		try {
		    m_db.removeApplicationType(appType);
		}
		catch (EmanagerDatabaseException e) {
		    logger.error ("Error encountered removing persistent AppType:" + appType.id());
		}

                return;
            }
        }

	logger.debug ("AppType not found in collection");
	return;
    }

    /**
     * @param appTypeId
     * @return com.cisco.eManager.eManager.inventory.appType.AppType
     * @roseuid 3F3A741B019A
     */
    public synchronized AppType find(ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        logger.debug("finding appType with ID " + appTypeId);
        Iterator iter = m_appTypes.iterator();
        AppType appType = null;
        while (iter.hasNext())
        {
            appType = (AppType)iter.next();
            if (appType.id().equals(appTypeId))
            {
                logger.debug("appType found: returning it");
                break;
            }
        }
        if (appType == null)
        {
            logger.debug("appType not found: returning null");
        }
        return appType;
    }

    /**
     * @return java.util.Vector
     * @roseuid 3F3A747201FF
     */
    public List appTypes()
    {
        logger.debug("enter");
        return m_appTypes;
    }

    public synchronized AppType[] allAppTypes()
    {
        int appTypeCount = m_appTypes.size();
        AppType[] appTypes = new AppType[appTypeCount];
        Iterator iter = m_appTypes.iterator();
        for (int i = 0; i < appTypeCount; i++)
        {
            appTypes[i] = (AppType)iter.next();
        }
        return appTypes;
    }

    /**
     * @param hostId
     * @return java.util.Vector
     * @roseuid 3F3A8A020013
     */
    public synchronized List appTypes(ManagedObjectId hostId)
    {
        logger.debug("enter");
        List appTypes = new LinkedList();
        AppType appType = null;
        List hosts = null;
        Host host = null;
        Iterator hostIter = null;
        Iterator iter = m_appTypes.iterator();
        while (iter.hasNext())
        {
            appType = (AppType)iter.next();
            hosts = appType.hosts();
            hostIter = hosts.iterator();
            host = null;
            boolean found = false;
            while (hostIter.hasNext())
            {
                host = (Host)hostIter.next();
                if (host.id().equals(hostId))
                {
                    found = true;
                    break;
                }
            }
            if (found == true)
            {
                // this app type has among its associated hosts the host
                // specified by hostId (the argument)
                appTypes.add(appType);
            }
        }
        return appTypes;
    }

    /**
     * @param mpId
     * @return com.cisco.eManager.eManager.inventory.appType.AppType
     * @roseuid 3F60C6FF037B
     */
    public synchronized AppType find(ManagementPolicyId mpId)
    {
        logger.debug("enter");
        AppType appType = null;
        MgmtPolicy mp = MgmtPolicyManager.instance().find(mpId);
        if ( mp != null )
        {
            appType = mp.appType();
        }
        return appType;
    }

    /**
     * @param name
     * @return com.cisco.eManager.eManager.inventory.appType.AppType
     * @roseuid 3F6E00AD00A8
     */
    public synchronized AppType find(String name)
    {
        logger.debug("enter");
        AppType appType = null;
        Iterator iter = m_appTypes.iterator();
        while (iter.hasNext())
        {
            appType = (AppType)iter.next();
            if ( appType.name().equals(name) )
            {
                return appType;
            }
        }
        return null;
    }

    public synchronized void setVersion(AppType appType, String version)
    {
	String oldVersion;

	oldVersion = appType.version();
        appType.version(version);

	try {
	    m_db.updateApplicationType(appType);
	}
	catch (EmanagerDatabaseException e) {
	    logger.error ("Exception encountered changing version on AppType:" + appType.id());
	    appType.version (oldVersion);
	}

	return;
    }

    private void initializeCacheFromDb()
    {
        Iterator iter;
        AppType appType;
        AppTypeData appTypeData;
        Collection appTypes;

        logger.debug("enter");

        appTypes = null;
        try{
            appTypes = DatabaseInterface.instance().retrieveApplicationTypes();
        }
        catch (EmanagerDatabaseException e){
            logger.error("exception caught while initializing cache from db: " +
                         e);
            return;
        }
        if (appTypes != null &&
            appTypes.size() != 0){
            iter = appTypes.iterator();
            while (iter.hasNext()){
                appTypeData = (AppTypeData)iter.next();
                appType = new AppType (appTypeData);
                m_appTypes.add(appType);
                logger.debug("added \"" + appType.name() + "\" to the cache");
                AppTypeRestoration ntfcnObj = new AppTypeRestoration(appType);
                setChanged();
                notifyObservers(ntfcnObj);
            }
        }
    }
}

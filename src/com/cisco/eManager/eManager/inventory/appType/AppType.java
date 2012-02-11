//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\appType\\AppType.java

package com.cisco.eManager.eManager.inventory.appType;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.*;
import com.cisco.eManager.eManager.inventory.appType.*;

public class AppType
{
    private static Logger logger = Logger.getLogger(AppType.class);

    /**
     * this AppType instance's version
     */
    private String m_version;

    /**
     * this AppType instance's name (e.x. CNOTE)
     */
    private String m_name;

    /**
     * the primary key which distinguishes this AppType instance from all others
     * within eManager
     */
    private ManagedObjectId m_id;

    /**
     * @param name
     * @param version
     * @param idKeyValue
     * @roseuid 3F4D081D01BC
     */
    AppType(long idKeyValue, String name, String version)
    {
        logger.debug("enter");
        m_id = new ManagedObjectId(ManagedObjectIdType.ApplicationType,
                                   idKeyValue);
        m_name = name;
        m_version = version;
    }

    public AppType (AppTypeData appTypeData)
    {
        m_id = appTypeData.id();
        m_name = appTypeData.name();
        m_version = appTypeData.version();
    }

    /**
     * @return the primary key which distinguishes this AppType instance from all
     * others within eManager
     * @roseuid 3F28448A003D
     */
    public ManagedObjectId id()
    {
        return m_id;
    }

    /**
     * @param hostId
     * @return the set of associated MgmtPolicy instances
     * @roseuid 3F28463B01C9
     */
    public List mgmtPolicies(ManagedObjectId hostId)
    {
        return MgmtPolicyManager.instance().find(hostId, m_id);
    }

    /**
     * @return the set of associated AppInstance objects
     * @roseuid 3F2846770159
     */
    public List appInstances()
    {
        return AppInstanceManager.instance().appInstancesByAppType(m_id);
    }

    /**
     * @return this AppType instance's version
     * @roseuid 3F284B210332
     */
    public String version()
    {
        return m_version;
    }

    synchronized void version(String version)
    {
        m_version = version;
    }

    /**
     * @return the hosts upon which this AppType is loaded (loading an AppInstance on
     * a host implicitly defines the AppType to the host)
     * @roseuid 3F3A735F0327
     */
    public List hosts()
    {
        logger.debug("enter");
        List hosts = new LinkedList();
        List appInstances = appInstances();
        if ( appInstances.size() > 0 )
        {
            // we have a collection of appInstances which are of this appType
            // - each appInstance runs on a host
            // - build a list of hosts, avoiding duplicates...
            Iterator iter = appInstances.iterator();
            Host host = null;
            while (iter.hasNext())
            {
                host = ((AppInstance)iter.next()).host();
                if ( !hosts.contains(host) )
                {
                    hosts.add(host);
                    logger.debug("host " + host.id() + " added to set of hosts");
                }
            }
        }
        logger.debug(hosts.size() + " hosts found mapping to this appType");
        return hosts;
    }

    /**
     * @return this AppType instance's name (e.x. CNOTE)
     * @roseuid 3F3A71BF00E5
     */
    public String name()
    {
        return m_name;
    }

    /**
     * @return java.util.Vector
     * @roseuid 3F68EE6D020E
     */
    public List mgmtPolicies()
    {
        return MgmtPolicyManager.instance().findByAppTypeId(m_id);
    }
}

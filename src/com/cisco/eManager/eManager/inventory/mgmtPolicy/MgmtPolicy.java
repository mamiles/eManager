//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\mgmtPolicy\\MgmtPolicy.java

package com.cisco.eManager.eManager.inventory.mgmtPolicy;

import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.inventory.appType.*;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.*;
import com.cisco.eManager.eManager.network.ManagementPolicyId;

public class MgmtPolicy
{
    private static Logger logger = Logger.getLogger(MgmtPolicy.class);
    private MgmtPolicyData m_data;
    private ManagementPolicyId m_networkId;
    private boolean m_loaded;
    //private String m_name;
    //private String m_pathname;
    //private ManagedObjectId m_id;
    //private ManagedObjectId m_appTypeId;
    //private ManagedObjectId m_hostId;

    /**
     * @param networkId
     * @param name
     * @param pathname
     * @param idKeyValue
     * @param appTypeId
     * @roseuid 3F4D088D022D
     */
/*    MgmtPolicy(long idKeyValue,
               ManagementPolicyId networkId,
               ManagedObjectId appTypeId,
               String pathname,
               boolean loaded)
        throws Exception
    {
        m_id = new ManagedObjectId(ManagedObjectIdType.MgmtPolicy, idKeyValue);
        m_networkId = networkId;
        m_appTypeId = appTypeId;
        String reason = null;
        Host host = HostManager.instance().find(networkId.agentId().name());
        if ( host == null )
        {
            reason = "unable to find host associated with networkId, " +
                     "unable to create mgmtPolicy";
            logger.error(reason);
            throw new Exception(reason);
        }
        m_name = networkId.name();
        m_pathname = pathname;
        m_loaded = loaded;
    }

    MgmtPolicy(MgmtPolicyData data)
    {
        m_id = data.id();
        m_appTypeId = data.appTypeId();
        m_pathname = data.path();
        m_name = data.name();
	m_networkId = null;
	m_loaded = false;
    }
*/
    MgmtPolicy(MgmtPolicyData data)
    {
        m_data = data;
        m_networkId = null;
        m_loaded = false;
    }

    public void activate(ManagementPolicyId networkId)
        throws Exception
    {
        String reason = null;
        if ( !networkId.name().equals(m_data.name()) )
        {
            reason = "networkId/name mismatch - cannot activate";
            logger.error(reason);
            throw new Exception(reason);
        }
	m_networkId = networkId;
    }

    public boolean isActive()
    {
	return m_networkId != null;
    }

    /**
     * @return com.cisco.eManager.eManager.inventory.appType.AppType
     * @roseuid 3F58F3E5013C
     */
    public AppType appType()
    {
        return AppTypeManager.instance().find(m_data.appTypeId());
    }

    /**
     * @return com.cisco.eManager.eManager.inventory.host.Host
     * @roseuid 3F58F3D902C5
     */
    public Host host()
    {
        //return HostManager.instance().find(m_networkId.agentId().name());
        return HostManager.instance().find(m_data.hostId());
    }

    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectKey
     * @roseuid 3F3A7F4403AA
     */
    public ManagedObjectId id()
    {
        return m_data.id();
    }

    /**
     * @return java.lang.String
     * @roseuid 3F28499C0128
     */
    public String name()
    {
        return m_data.name();
    }

    /**
     * @return com.cisco.eManager.eManager.network.ManagementPolicyId
     * @roseuid 3F60D89001D2
     */
    public ManagementPolicyId networkId()
    {
        return m_networkId;
    }

    /**
     * @return a vector of Rule objects
     * @roseuid 3F2849A303D2
     */
    public String pathname()
    {
        return m_data.path();
    }

    /**
     * @param mpId
     * @roseuid 3F6720B603C1
     */
    synchronized void setNetworkId(ManagementPolicyId mpId)
    {
        m_networkId = mpId;
    }

    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectId
     * @roseuid 3F6E00DA037E
     */
    public ManagedObjectId appTypeId()
    {
        return m_data.appTypeId();
    }

    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectId
     * @roseuid 3F6E00DA03E2
     */
    public ManagedObjectId hostId()
    {
        return m_data.hostId();
    }

    public boolean isLoaded()
    {
        return m_loaded;
    }

    synchronized void setLoaded(boolean loaded)
    {
        m_loaded = loaded;
    }

    synchronized void setAppTypeId(ManagedObjectId appTypeId)
    {
        m_data.appTypeId(appTypeId);
    }

    synchronized void setPathname(String pathname)
    {
        m_data.path(pathname);
    }
}

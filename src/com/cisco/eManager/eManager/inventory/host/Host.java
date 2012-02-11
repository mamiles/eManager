
package com.cisco.eManager.eManager.inventory.host;

import java.util.*;
import org.apache.log4j.Logger;
import COM.TIBCO.hawk.console.hawkeye.*;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.InventoryGlobals;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.appType.*;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.*;
import com.cisco.eManager.eManager.network.*;

public class Host
{
    private static Logger logger = Logger.getLogger(Host.class);
    private AgentInstance m_agentInstance;
    private AgentManager m_agentManager;
    private AgentId m_networkId;
    private HostData m_data;

    Host(HostData data)
    {
        logger.debug("enter");
        m_data = data;
        m_agentInstance = null;
        m_networkId = null;
        m_agentManager = null;
    }

    void activate(AgentInstance ai, AgentManager am)
    {
        m_agentInstance = ai;
        m_networkId = new AgentId(ai.getAgentID());
        m_agentManager = am;
    }

    public boolean isActive()
    {
        return (m_agentInstance != null);
    }

    /**
     * @return a vector of AppInstance objects
     * @roseuid 3F28473103C9
     */
    public List appInstances()
    {
        logger.debug("enter");
        List appInstances =
            AppInstanceManager.instance().appInstancesByHost(m_data.id());
        return appInstances;
    }

    public List appInstances(ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        List appInstances =
            AppInstanceManager.instance().find(m_data.id(), appTypeId);
        return appInstances;
    }

    /**
     * @return java.util.Vector
     * @roseuid 3F58F474013A
     */
    public List appTypes()
    {
        logger.debug("enter");
        List appTypes = new Vector();
        AppType anAppType = null;
        List mgmtPolicies = mgmtPolicies();
        MgmtPolicy mgmtPolicy = null;
        Iterator iter = mgmtPolicies.iterator();
        while (iter.hasNext())
        {
            mgmtPolicy = (MgmtPolicy)iter.next();
            anAppType = mgmtPolicy.appType();
            if (!appTypes.contains(anAppType))
            {
                appTypes.add(anAppType);
            }
        }
        return appTypes;
    }

    public String domain()
    {
        return m_data.domain();
    }

    public String hostname()
    {
        return m_data.hostname();
    }

    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectKey
     * @roseuid 3F3A77BB0316
     */
    public ManagedObjectId id()
    {
        return m_data.id();
    }

    public String ipAddress()
    {
        return m_data.ipAddress();
    }

    /**
     * @param appTypeId
     * @return java.util.Vector
     * @roseuid 3F3A7B3F02CD
     */
    public List mgmtPolicies(ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        return MgmtPolicyManager.instance().find(m_data.id(), appTypeId);
    }

    /**
     * @return java.lang.String
     * @roseuid 3F2847150197
     */
    public String name()
    {
        return m_data.hostname();
    }

    public String dnsName()
    {
        if ( m_networkId == null )
        {
            return null;
        }
        return m_networkId.dns();
    }

    /**
     * @return com.cisco.eManager.eManager.network.AgentId
     * @roseuid 3F60D5AB016E
     */
    public AgentId networkId()
    {
        return m_networkId;
    }

    /**
     * @return java.util.Vector
     * @roseuid 3F68EE8102E0
     */
    public List mgmtPolicies()
    {
        logger.debug("enter");
        return MgmtPolicyManager.instance().findByHostId(m_data.id());
    }

    public AgentInstance agentInstance()
    {
        return m_agentInstance;
    }

    public AgentManager agentManager()
    {
        return m_agentManager;
    }

    public HostResourcesAppInstance hostResources()
    {
        List appInstances = appInstances();
        Iterator iter = appInstances.iterator();
        AppInstance appInstance = null;
        while (iter.hasNext())
        {
            appInstance = (AppInstance)iter.next();
            if ( appInstance instanceof HostResourcesAppInstance )
            {
                return (HostResourcesAppInstance)appInstance;
            }
        }
        return null;
    }
}

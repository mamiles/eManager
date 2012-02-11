
package com.cisco.eManager.eManager.inventory.appInstance;

import java.util.*;
import org.apache.log4j.Logger;
import COM.TIBCO.hawk.talon.*;
import com.cisco.eManager.eManager.inventory.InventoryGlobals;
import com.cisco.eManager.eManager.inventory.instrumentation.*;

public class HostResourcesAppInstance extends AppInstance
{
    private static Logger logger =
        Logger.getLogger(HostResourcesAppInstance.class);
    private Instrumentation m_mgmtPolicyEngine;
    private Method m_loadMgmtPolicyFromFile;
    private Method m_unloadMgmtPolicy;

    HostResourcesAppInstance(AppInstanceData data)
    {
        super(data);
        m_loadMgmtPolicyFromFile = null;
        m_unloadMgmtPolicy = null;
    }

    public void loadMgmtPolicy(String mgmtPolicyPathname)
    {
        logger.debug("loading mgmtPolicy from file \"" + mgmtPolicyPathname +
                     "\"");
        String pathname = mgmtPolicyPathname.trim();
        if ( pathname == null )
        {
            logger.info("unable to load mgmtPolicy - pathname is null");
            return;
        }
        if ( pathname.length() < 1 )
        {
            logger.info("unable to load mgmtPolicy - pathname is \"\"");
            return;
        }

        Method method = loadMgmtPolicyFromFile();
        if ( method == null )
        {
            logger.warn("unable to load mgmtPolicy - required method is not " +
                        "available");
            return;
        }
        DataElement args[] = {new DataElement("File", pathname)};
        method.invoke(args);
        logger.debug("loaded mgmtPolicy");
    }

    public void unloadMgmtPolicy(String mgmtPolicyName)
    {
        logger.debug("unloading mgmtPolicy \"" + mgmtPolicyName + "\"");
        Method method = unloadMgmtPolicy();
        if ( method == null )
        {
            logger.warn("unable to unload mgmtPolicy - required method is " +
                        "not available");
            return;
        }
        DataElement args[] = {new DataElement("RuleBaseName", mgmtPolicyName)};
        method.invoke(args);
        logger.debug("unloaded mgmtPolicy");
    }

    private Instrumentation mgmtPolicyEngine()
    {
        if ( m_mgmtPolicyEngine == null )
        {
            m_mgmtPolicyEngine =
                instrumentation(
                    InventoryGlobals.sysRuleBaseEngineInstrumentationName());
        }
        return m_mgmtPolicyEngine;
    }

    private Method loadMgmtPolicyFromFile()
    {
        if ( m_loadMgmtPolicyFromFile == null )
        {
            Instrumentation mgmtPolicyEngine = mgmtPolicyEngine();
            if ( mgmtPolicyEngine == null )
            {
                logger.warn("unable to find method " +
                            "\"loadMgmtPolicyFromFile\" " +
                            "because unable to find instrumentation " +
                            "\"mgmtPolicyEngine\"");
            }
            else
            {
                m_loadMgmtPolicyFromFile =
                    mgmtPolicyEngine.method("loadRuleBaseFromFile");
            }
        }
        return m_loadMgmtPolicyFromFile;
    }

    private Method unloadMgmtPolicy()
    {
        if ( m_unloadMgmtPolicy == null )
        {
            Instrumentation mgmtPolicyEngine = mgmtPolicyEngine();
            if ( mgmtPolicyEngine == null )
            {
                logger.warn("unable to find method " +
                            "\"unloadMgmtPolicy\" " +
                            "because unable to find instrumentation " +
                            "\"mgmtPolicyEngine\"");
            }
            else
            {
                m_unloadMgmtPolicy =
                    mgmtPolicyEngine.method("unloadRuleBase");
            }
        }
        return m_unloadMgmtPolicy;
    }
}
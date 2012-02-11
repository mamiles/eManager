//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\InventoryManager.java

package com.cisco.eManager.eManager.inventory;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.*;
import COM.TIBCO.hawk.console.hawkeye.*;
import COM.TIBCO.hawk.talon.*;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.Transport;
import com.cisco.eManager.common.register.deRegistration.*;
import com.cisco.eManager.common.register.registration.*;
import com.cisco.eManager.common.register.reRegistration.*;
import com.cisco.eManager.common.register.solutionRegistration.*;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.appType.*;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.inventory.instrumentation.*;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.*;
import com.cisco.eManager.eManager.inventory.solution.*;
import com.cisco.eManager.eManager.inventory.view.*;
import com.cisco.eManager.eManager.network.*;
import com.cisco.eManager.eManager.process.ProcessHawkConsole;
import com.cisco.eManager.eManager.process.ProcessManager;
import com.cisco.eManager.eManager.util.WatchdogRulebaseNameHelper;

public class InventoryManager
    extends Observable
{
    private static InventoryManager s_instance = null;
    private static Logger logger = Logger.getLogger(InventoryManager.class);
    private AppInstanceManager m_aim;
    private ImAppInstanceObserver m_imaio;
    private AppInstanceObserver m_aio;
    private AppTypeManager m_atm;
    private AppTypeObserver m_ato;
    private HostManager m_hm;
    private HostObserver m_ho;
    private InstrumentationManager m_im;
    private MgmtPolicyManager m_mpm;
    private SolutionManager m_sm;
//    private AppViewManager m_avm;
//    private HostViewManager m_hvm;
//    private InventoryObserver m_io;
//    private InventoryNotificationDistributor m_ind;
//    private TibRvNotificationSender m_trns;

    private final String eManagerAppTypeName = "eManager";

    private com.cisco.eManager.eManager.inventory.xml.Applications
        m_predefinedApps;

    private JFrame debuggerFrame;
    private JTabbedPane debuggerTabbedPane;

    /**
     * @roseuid 3F4D03E401F2
     */
    private InventoryManager()
        throws Exception
    {
        logger.debug("enter");

        m_aim = AppInstanceManager.instance();
        m_atm = AppTypeManager.instance();
        m_hm = HostManager.instance();
        m_im = InstrumentationManager.instance();
        m_mpm = MgmtPolicyManager.instance();
        m_sm = SolutionManager.instance();

//        m_avm = AppViewManager.instance();
//        m_hvm = HostViewManager.instance();

//        m_imaio = new ImAppInstanceObserver();
//        m_aio = new AppInstanceObserver();
//        m_ato = new AppTypeObserver();
//        m_ho = new HostObserver();

        initializePredefinedApps();

        Container container;
        debuggerFrame = new JFrame("Inventory Debugger");
        container = debuggerFrame.getContentPane();
        debuggerTabbedPane = new JTabbedPane();
        container.add(debuggerTabbedPane, BorderLayout.CENTER);
    }

    private void initializePredefinedApps()
    {
        logger.debug("enter");
        m_predefinedApps =
            new com.cisco.eManager.eManager.inventory.xml.Applications();

        String eMgrDir = System.getProperty("EMANAGER_ROOT");
        String filename = eMgrDir + "/config/inventoryManager/Applications.xml";
        FileInputStream applicationDefStream = null;
        try
        {
            applicationDefStream = new FileInputStream(filename);
        }
        catch (FileNotFoundException e)
        {
            logger.error("Property File: " + filename + " is not found");
            return;
        }
        catch (IOException e)
        {
            logger.error("Caught IOException while processing property file: " +
                         filename);
            return;
        }

        if ( applicationDefStream == null )
        {
            return;
        }

        InputStreamReader inputStream =
            new InputStreamReader(applicationDefStream);

        try
        {
            m_predefinedApps =
                (com.cisco.eManager.eManager.inventory.xml.Applications)
                com.cisco.eManager.eManager.inventory.xml.Applications.unmarshalApplications(
                inputStream);
        }
        catch (MarshalException ex)
        {
            logger.error("MarshalException caught while unmarshaling xml: " +
                         ex);
            return;
        }
        catch (ValidationException ex)
        {
            logger.error("ValidationException caught while unmarshaling xml: " +
                         ex);
            return;
        }
        return;
    }

    private void registerEmanagerApp()
    {
        logger.debug("enter");
        com.cisco.eManager.common.register.registration.Registration eMgrReg
           = new com.cisco.eManager.common.register.registration.Registration();

        String eMgrDir = System.getProperty("EMANAGER_ROOT");
        String filename = eMgrDir + "/config/inventoryManager/eManagerRegistration.xml";
        FileInputStream eMgrRegStream = null;
        try
        {
            eMgrRegStream = new FileInputStream(filename);
        }
        catch (FileNotFoundException e)
        {
            logger.error("eManager registration file \"" + filename +
                         "\" is not found");
            return;
        }
        catch (IOException e)
        {
            logger.error("Caught IOException while processing eManager " +
                         "registration file: " + filename);
            return;
        }

        if ( eMgrRegStream == null )
        {
            return;
        }

        InputStreamReader inputStream = new InputStreamReader(eMgrRegStream);

        try
        {
            eMgrReg =
                (com.cisco.eManager.common.register.registration.Registration)
                com.cisco.eManager.common.register.registration.Registration.unmarshalRegistration(
                inputStream);
        }
        catch (MarshalException ex)
        {
            logger.error("MarshalException caught while unmarshaling xml: " +
                         ex);
            return;
        }
        catch (ValidationException ex)
        {
            logger.error("ValidationException caught while unmarshaling xml: " +
                         ex);
            return;
        }
        newRegistration(eMgrReg);
        return;
    }

    public JFrame getDebuggerFrame()
    {
        return debuggerFrame;
    }

    public JTabbedPane getDebuggerPane()
    {
        return debuggerTabbedPane;
    }

    /**
     * @param msg
     * @roseuid 3F04968C004D
     */
    public synchronized void newMgmtPolicy(AddMgmtPolicy msg)
    {
        logger.debug("enter");
        logger.debug("network message: " + msg);

        Host host = m_hm.find(msg.agentId().name());
        if (host == null)
        {
            logger.error("unable to find host with agentId " + msg.agentId());
            return;
        }

        newMgmtPolicy(host, msg.name());
    }

    private synchronized void newMgmtPolicy(Host host, String mgmtPolicyName)
    {
        logger.debug("enter");
        logger.debug("creating new mgmtPolicy " + mgmtPolicyName);

        //
        // the following is a temporary (?) workaround in support of dynamic
        // management policies - that is, policies created on the fly by the
        // PS subsystem for the purpose of raising/clearing PS events via the
        // TIB/Hawk subsystem...
        //
        if (WatchdogRulebaseNameHelper.instance().validWatchdogRulebaseName(
                mgmtPolicyName))
        {
            logger.info("mgmtPolicy \"" + mgmtPolicyName + "\" conforms to " +
                        "the watchdog dynamic management policy template - " +
                        "processing is complete");
            return;
        }
        //
        // the above is a temporary (?) workaround in support of dynamic
        // management policies - that is, policies created on the fly by the
        // PS subsystem for the purpose of raising/clearing PS events via the
        // TIB/Hawk subsystem...
        //


        ImAppInstanceCreation appInstanceCreation = null;
        ImAppTypeCreation appTypeCreation = null;
        ImMgmtPolicyCreation mgmtPolicyCreation = null;

        if (host == null)
        {
            logger.error("unable to find host " + host.name());
            return;
        }

        // try to find this exact mgmtPolicy (this policy on this host)
        // we haven't yet discovered this management policy, but perhaps it has
        // been created in advance via registration
        // - look for it by name+host
        ManagementPolicyId mpId =
            new ManagementPolicyId(host.networkId(), mgmtPolicyName);
        boolean isLoaded = true;
        MgmtPolicy mgmtPolicy = m_mpm.find(mgmtPolicyName, host.id());
        if (mgmtPolicy != null)
        {
            m_mpm.setNetworkId(mgmtPolicy, mpId);
            m_mpm.setLoaded(mgmtPolicy, true);
            ImMgmtPolicyLoad mgmtPolicyLoad = new ImMgmtPolicyLoad(mgmtPolicy);
            setChanged();
            notifyObservers(mgmtPolicyLoad);
            return;
        }

        // try to find this mgmtPolicy on any host (use its AppType if we find
        // one...)
        List mgmtPolicies = m_mpm.mgmtPolicies(mgmtPolicyName);
        AppType appType = null;
        if (!mgmtPolicies.isEmpty())
        {
            // this mgmtPolicy exists (just on another host)
            // - use the first mgmtPolicy's associated AppType rather than
            //   creating one from scratch
            appType = ((MgmtPolicy)(mgmtPolicies.get(0))).appType();
        }
        else
        {
            // this is the first mgmtPolicy by this name on any host
            String appTypeName = getAppTypeName(mpId.name());
            appType = m_atm.createAppType(appTypeName, "");
            appTypeCreation = new ImAppTypeCreation(appType);

            String appInstanceName = getAppInstanceName(host, appType);
            String logfileUsername = "";
            String logfilePassword = "";
            String logfileDirectories = "";
            String appInstancePropertyFile = "";
            String unixPrompt = "=8^0";
            Transport logfileTransport = Transport.SSH;
            AppInstance appInstance =
                m_aim.createAppInstance(appType.id(),
                                        host.id(),
                                        appInstanceName,
                                        logfileUsername,
                                        logfilePassword,
                                        logfileDirectories,
                                        logfileTransport,
                                        appInstancePropertyFile,
                                        unixPrompt);
            appInstanceCreation = new ImAppInstanceCreation(appInstance);
        }
        String mpPath = "";
        MgmtPolicy createdMgmtPolicy =
            m_mpm.createPolicy(mpId, mpPath, host.id(), appType.id(), isLoaded);
        mgmtPolicyCreation = new ImMgmtPolicyCreation(createdMgmtPolicy);

        // notify observers
        if (appInstanceCreation != null)
        {
            setChanged();
            notifyObservers(appInstanceCreation);
        }
        if (appTypeCreation != null)
        {
            setChanged();
            notifyObservers(appTypeCreation);
        }
        if (mgmtPolicyCreation != null)
        {
            setChanged();
            notifyObservers(mgmtPolicyCreation);
        }

        String newAppTypeName = null;
        if ( appTypeCreation != null )
        {
            newAppTypeName = appTypeCreation.appType().name();
        }
        else if ( appInstanceCreation != null )
        {
            newAppTypeName = appInstanceCreation.appInstance().appType().name();
        }
        if ( newAppTypeName != null )
        {
            if ( newAppTypeName.equals(eManagerAppTypeName) )
            {
                registerEmanagerApp();
            }
        }
    }

    /**
     * @param msg
     * @roseuid 3F099DEA0307
     */
    public synchronized void newInstrumentation(AddMicroAgent msg)
    {
        logger.debug("enter");
        logger.debug("adding instrumentation " + msg.instrumentationId());

        Host host = m_hm.find(msg.agentId().name());
        if (host == null)
        {
            logger.error("unable to find host with agentId " + msg.agentId());
            return;
        }

        MicroAgentID uAgentId = msg.instrumentationId().raw();

        newInstrumentation(host, uAgentId);
    }

    private synchronized void newInstrumentation(Host host, MicroAgentID uAgentId)
    {
        logger.debug("enter");
        logger.debug("adding instrumentation for " + uAgentId + " on host " +
                     host);

        // instrumentation tells us appType and appInstance
        // 1) verify that the instrumentation doesn't already exist in our cache
        // 2) instrumentation tells us appType and appInstance - obtain that info
        //    - look for "known" instrumentations (process sequencer, watchdog,
        //      system stand-alone) and in non-plug-n-play fashion, map those to
        //      "known" appInstances and appTypes
        // 3) create (if necessary) the named appType
        // 4) create (if necessary) the named appInstance
        // 5) create the instrumentation
        // 6) notify observers

        AppType appType = null;
        ImAppTypeCreation appTypeCreation = null;
        AppInstance appInstance = null;
        ImAppInstanceCreation appInstanceCreation = null;
        Instrumentation instrumentation = null;
        ImInstrumentationCreation instrumentationCreation = null;

        Instrumentation instr =
            m_im.find(new MicroagentId(host.networkId().raw(), uAgentId));
        if (instr != null)
        {
            logger.debug("instrumentation already exists in cache");
            instr.activate();
            return;
        }

        String appTypeName = getAppTypeName(host, uAgentId);
        if ( ( appTypeName == null ) || ( appTypeName.equals("") ) )
        {
            logger.info("host \"" + host.name() + "\" and uAgent \"" +
                        uAgentId.getName() + "\" don't translate to any " +
                        "appTypeName - returning without creating a new " +
                        "appType/appInstance");
            return;
        }

        String appInstanceName = null;

        String logfileUsername = "";
        String logfilePassword = "";
        String logfileDirectories = "";
        Transport logfileTransport = Transport.SSH;
        String appInstancePropertyFile = "";
        String unixPrompt = ":)";

        // if appType doesn't already exist
        // then create appType and appInstance (instance won't exist either)
        // else if appType exists but appInstance doesn't
        // then create appInstance
        appType = m_atm.find(appTypeName);
        if (appType == null)
        {
            appType = m_atm.createAppType(appTypeName, "");
            appTypeCreation = new ImAppTypeCreation(appType);
            appInstanceName = getAppInstanceName(host, uAgentId, appType);
            if ( ( appInstanceName == null ) || ( appInstanceName.equals("") ) )
            {
                logger.info("unable to generate an appInstance name from " +
                            "host \"" + host.name() + "\" and uAgent \"" +
                            uAgentId.getName() + "\" - returning without " +
                            "creating a new appInstance");
                return;
            }
            appInstance = m_aim.createAppInstance(appType.id(),
                                                  host.id(),
                                                  appInstanceName,
                                                  logfileUsername,
                                                  logfilePassword,
                                                  logfileDirectories,
                                                  logfileTransport,
                                                  appInstancePropertyFile,
                                                  unixPrompt);
            appInstanceCreation = new ImAppInstanceCreation(appInstance);
        }
        else
        {
            appInstanceName = getAppInstanceName(host, uAgentId, appType);
            if ( ( appInstanceName == null ) || ( appInstanceName.equals("") ) )
            {
                logger.info("unable to generate an appInstance name from " +
                            "host \"" + host.name() + "\" and uAgent \"" +
                            uAgentId.getName() + "\" - returning without " +
                            "creating a new appInstance");
                return;
            }
            appInstance = m_aim.find1(appInstanceName, appType.id());
            if (appInstance == null)
            {
                appInstance =
                    m_aim.createAppInstance(appType.id(),
                                            host.id(),
                                            appInstanceName,
                                            logfileUsername,
                                            logfilePassword,
                                            logfileDirectories,
                                            logfileTransport,
                                            appInstancePropertyFile,
                                            unixPrompt);
                appInstanceCreation = new ImAppInstanceCreation(appInstance);
            }
        }

        // finally, go ahead and create the instrumentation...
        instrumentation = m_im.createInstrumentation(uAgentId,
            appInstance.id());
        instrumentationCreation = new ImInstrumentationCreation(instrumentation);

        // ...and notify the observers
        if (appTypeCreation != null)
        {
            setChanged();
            notifyObservers(appTypeCreation);
        }
        if (appInstanceCreation != null)
        {
            setChanged();
            notifyObservers(appInstanceCreation);
        }
        if (instrumentationCreation != null)
        {
            setChanged();
            notifyObservers(instrumentationCreation);
        }

        String newAppTypeName = null;
        if ( appTypeCreation != null )
        {
            newAppTypeName = appTypeCreation.appType().name();
        }
        else if ( appInstanceCreation != null )
        {
            newAppTypeName = appInstanceCreation.appInstance().appType().name();
        }
        if ( newAppTypeName != null )
        {
            if ( newAppTypeName.equals(eManagerAppTypeName) )
            {
                registerEmanagerApp();
            }
        }
    }

    /**
     * @param msg
     * @roseuid 3F0AE84A0398
     */
    public synchronized void newHost(AddAgent msg)
    {
        logger.debug("enter");
        logger.debug("processing host " + msg.agentId());
        logger.debug("processing host " + msg.agentInstance().getIPAddress());
        logger.debug("processing host " + msg.agentInstance().getAgentID().getName());

        // a host is reporting in
        // - we may or may not know of this host
        // - if we don't, create one in our persistent datastore
        // - if we do, we're done already

        Host host = m_hm.findByIpAddress(msg.agentInstance().getIPAddress());
        ImHostCreation hostCreation = null;

        if (host == null)
        {
            logger.debug("unable to find host - creating one now");
            host = m_hm.createHost(msg.agentInstance(), msg.agentManager());
            hostCreation = new ImHostCreation(host);
            setChanged();
            notifyObservers(hostCreation);
        }
        else
        {
            logger.debug("already have host in persistent datastore");
            m_hm.activateHost(host, msg.agentInstance(), msg.agentManager());
            ImHostActivation ha = new ImHostActivation(host);
            setChanged();
            notifyObservers(ha);
        }

        // one last thing - get the host's (the agent's) instrumentations and
        // mgmtPolicies
        AgentInstance ai = msg.agentInstance();
        MicroAgentID[] uAgentIds = ai.getStatusMicroAgents();
        for (int i = 0; i < uAgentIds.length; i++)
        {
            newInstrumentation(host, uAgentIds[i]);
        }

        RuleBaseStatus[] rulebaseIds = ai.getStatusRuleBases();
        for (int i = 0; i < rulebaseIds.length; i++)
        {
            newMgmtPolicy(host, rulebaseIds[i].getName());
        }
        return;
    }

    /**
     * @return com.cisco.eManager.eManager.inventory.InventoryManager
     * @roseuid 3F1FF69803BF
     */
    public synchronized static InventoryManager instance()
    {
        logger.debug("enter");
        if (s_instance == null)
        {
            try
            {
                s_instance = new InventoryManager();
                s_instance.m_imaio = new ImAppInstanceObserver();
//                s_instance.m_ind = InventoryNotificationDistributor.instance();
//                s_instance.m_trns = TibRvNotificationSender.instance();
//                s_instance.m_io = new InventoryObserver();
            }
            catch (Exception ex)
            {
                logger.error("exception caught from InventoryManager(): " +
                             ex);
                s_instance = null;
                return null;
            }
        }
        return s_instance;
    }

    /**
     * @param msg
     * @roseuid 3F214E0E0237
     */
    public synchronized void deleteHost(RemoveAgent msg)
    {
        logger.debug("enter");
        logger.debug("deleting host " + msg.agentId());
        Host host = null;
        ImHostDeletion hostDeletion = null;

        if ((host = m_hm.find(msg.agentId().name())) == null)
        {
            logger.debug("host not found in persistent datastore");
            return;
        }

        List appTypes = m_atm.appTypes(host.id());
        if (!appTypes.isEmpty())
        {
            logger.debug("cannot delete host because of associated appTypes");
            return;
        }

        m_hm.deleteHost(host.id());
        logger.debug("host deleted");
        hostDeletion = new ImHostDeletion(host);
        setChanged();
        notifyObservers(hostDeletion);
        return;
    }

    /**
     * @roseuid 3F214E1C00E2
     */
    public synchronized void deleteAppInstance(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        logger.debug("deleting appInstance with id " + appInstanceId);

        // a user would request the deletion of an appInstance when its
        // existence within the scope of eManager no longer makes sense.  For
        // now let's assume that implies the appInstance is not running, and
        // that if it is running then we should reject the deletion.

        AppInstance appInstance = null;

        if ((appInstance = m_aim.find(appInstanceId)) == null)
        {
            // cannot find the appInstance to delete it
            logger.debug("appInstance not found in persistent datastore");
            return;
        }

        /** @todo integrate with ProcessManager to determine runstate of
         * appInstance before deleting it */
        if (false)
        {
            logger.debug("cannot delete an executing appInstance");
            return;
        }

        List deletedInstrumentations = m_im.deleteByAppInstance(appInstanceId);

        m_aim.deleteAppInstance(appInstanceId);

        // notify observers of deletions
        if (deletedInstrumentations != null)
        {
            Iterator iter = deletedInstrumentations.iterator();
            Instrumentation instrumentation = null;
            ImInstrumentationDeletion instrumentationDeletion = null;
            while (iter.hasNext())
            {
                instrumentation = (Instrumentation)iter.next();
                instrumentationDeletion =
                    new ImInstrumentationDeletion(instrumentation);
                setChanged();
                notifyObservers(instrumentationDeletion);
            }
        }

        if (appInstance != null)
        {
            ImAppInstanceDeletion appInstanceDeletion =
                new ImAppInstanceDeletion(appInstance);
            setChanged();
            notifyObservers(appInstanceDeletion);
        }
    }

    /**
     * @roseuid 3F214E2700C0
     */
    public synchronized void deleteAppType(ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        logger.debug("deleting appInstance with id " + appTypeId);

        // a user would request the deletion of an appType when its
        // existence within the scope of eManager no longer makes sense.  For
        // now let's assume that implies any associated appInstances have
        // already been removed, and if any still exist we should reject the
        // request.

        AppType appType = null;

        if ((appType = m_atm.find(appTypeId)) == null)
        {
            // cannot find the appType to delete it
            logger.debug("appType not found in persistent datastore");
            return;
        }

        // if appType has associated appInstances...
        List appInstances = appType.appInstances();
        if (!appInstances.isEmpty())
        {
            logger.debug(
                "cannot delete an appType with associated appInstances");
            return;
        }

        // unload the appType's management policies
        List mgmtPolicies = appType.mgmtPolicies();
        Iterator iter = mgmtPolicies.iterator();
        MgmtPolicy mgmtPolicy = null;
        while (iter.hasNext())
        {
            mgmtPolicy = (MgmtPolicy)iter.next();
            m_mpm.deletePolicy(mgmtPolicy.id());
        }

        m_atm.deleteAppType(appTypeId);

        // notify observers...
        // notify observers of deletions
        if (mgmtPolicies != null)
        {
            iter = mgmtPolicies.iterator();
            mgmtPolicy = null;
            ImMgmtPolicyDeletion mgmtPolicyDeletion = null;
            while (iter.hasNext())
            {
                mgmtPolicy = (MgmtPolicy)iter.next();
                mgmtPolicyDeletion = new ImMgmtPolicyDeletion(mgmtPolicy);
                setChanged();
                notifyObservers(mgmtPolicyDeletion);
            }
        }

        if (appType != null)
        {
            ImAppTypeDeletion appTypeDeletion = new ImAppTypeDeletion(appType);
            setChanged();
            notifyObservers(appTypeDeletion);
        }
    }

    /**
     * @param msg
     * @roseuid 3F259602006F
     */
    public synchronized void processNetworkMessage(AgentMessage msg)
    {
        logger.debug("enter");

        if (msg instanceof AddAgent)
        {
            newHost((AddAgent)msg);
        }
        else if (msg instanceof AddMgmtPolicy)
        {
            newMgmtPolicy((AddMgmtPolicy)msg);
        }
        else if (msg instanceof AddMicroAgent)
        {
            newInstrumentation((AddMicroAgent)msg);
        }
        else if (msg instanceof RemoveAgent)
        {
            deleteHost((RemoveAgent)msg);
        }
        else if (msg instanceof RemoveMgmtpolicy)
        {
            deleteMgmtPolicy((RemoveMgmtpolicy)msg);
        }
        else if (msg instanceof RemoveMicroAgent)
        {
            deleteInstrumentation((RemoveMicroAgent)msg);
        }
        else
        {
            logger.error("unrecognized network message: " + msg);
        }
    }

    /**
     * @roseuid 3F424B7501EA
     */
    public synchronized String newRegistration(Registration airm)
    {
        logger.debug("enter");
        logger.debug("registration: " + airm.toString());

        String results = null; // null results == successful registration

        // we're being told about an appType, appInstance, & mgmtPolicies
        // we may already know about these
        // - make sure they reflect the details presented in this registration
        //
        // a: registration covers fresh ground (no inventory data for this app)
        // b: inventory data already exists
        //    1) rulebase discovery -> appType, appInstance implicitly created
        //       under eManager-generated names
        //    2) uAgent discovery -> appType/appInstance created according to
        //       (supposedly) correct names provided by properly implemented
        //       uAgents
        //    3) both 1) and 2), with the appType from 1) needing to be
        //       collected under appType/appInstance created in 2)
        //    4) this registration is redundant and should be rejected
        //
        // to sort through the above, start with the mgmtPolicy, which indicates
        // its origin (rulebase discovery or registration) via its path property
        // (the path is empty for mgmtPolicies which has not yet been registered)
        //
        // if mgmtPolicy exists by that name
        //   not a:
        //   maybe b:1
        //   maybe b:3
        //   maybe b:4
        //   if mgmtPolicy has path info
        //     b:4
        //     respond w "cannot re-register via appRegistration - use appReRegistration"
        //   else
        //     either b:1 or b:3
        //     if appType/appInstance already exist
        //       b:3
        //     else
        //       b:1
        //       create appType and appInstance
        //       remap rulebase
        // else
        //   maybe a:
        //   not b:1
        //   maybe b:2
        //   not b:3
        //   not b:4
        //
        // we may have already discovered the following
        // - mgmtPolicy discovery would lead to an appType, an appInstance, and
        //   and a mgmtPolicy being created in our datastore
        //   - appType named after mgmtPolicy name (see generateImplicitAppTypeName())
        //   - appInstance named with generateImplicitAppInstanceName()
        // - instrumentation discovery leads to an appType and an appInstance
        //   being created in our datastore
        //   - assuming instrumentation and this registration are correctly
        //     implemented and fielded, the discovered appType and appInstance
        //     will match what's identified in this registration
        // - morph eManager-defined type and instance objects into those
        //   specified in this registration

        Host regHost = null;

        AppType regAppType = null;
        Collection newAppTypes = new LinkedList();
        Collection deletedAppTypes = new LinkedList();

        AppInstance regAppInstance = null;
        Collection newAppInstances = new LinkedList();
        Collection deletedAppInstances = new LinkedList();

        regHost = m_hm.find(airm.getHostName());
        if (regHost == null)
        {
            results = "host \"" + airm.getHostName() +
                      "\" is not known to eManager";
            logger.debug(results);
            return results;
        }

        regAppType = m_atm.find(airm.getAppType());
        if (regAppType == null)
        {
            logger.debug("appType " + airm.getAppType() +
                         " not found - creating one");
            regAppType = m_atm.createAppType(airm.getAppType(),
                                             airm.getAppVersion());
            newAppTypes.add(regAppType);
        }
        else
        {
            logger.debug("appType " + airm.getAppType() + " found");
            m_atm.setVersion(regAppType, airm.getAppVersion());
        }

        String regAppInstanceName = airm.getAppInstance();
        if ( ( regAppInstanceName == null ) || ( regAppInstanceName.equals("") ) )
        {
            results = "must provide an appInstance ID in registration";
            logger.info(results);
            return results;
        }

        regAppInstance = m_aim.find1(regAppInstanceName, regAppType.id());
        Transport transport = new Transport(airm.getLogging().getTransport());
        if (regAppInstance == null)
        {
            logger.debug("appInstance " + airm.getAppInstance() +
                         " not found - creating one");
            regAppInstance =
                m_aim.createAppInstance(
                    regAppType.id(),
                    regHost.id(),
                    regAppInstanceName,
                    airm.getLogging().getUserId(),
                    airm.getLogging().getPassword(),
                    airm.getLogging().getLogDirectories().getLogDirectory(),
                    transport,
                    airm.getProcessPropertyFile(),
                    airm.getLogging().getUnixPrompt());
            newAppInstances.add(regAppInstance);
        }
        else
        {
            logger.debug("appInstance " + regAppInstance.name() + " found");

            String processPropertyFile = airm.getProcessPropertyFile();
            if ( processPropertyFile != null )
            {
                m_aim.setPropertyFile(regAppInstance,
                                      airm.getProcessPropertyFile());
            }

            m_aim.setLogfileDirectories(
                regAppInstance,
                airm.getLogging().getLogDirectories().getLogDirectory());

            String password = airm.getLogging().getPassword();
            if ( password != null )
            {
                m_aim.setLogfilePassword(regAppInstance,
                                         airm.getLogging().getPassword());
            }

            String username = airm.getLogging().getUserId();
            if ( username != null )
            {
                m_aim.setLogfileUsername(regAppInstance,
                                         airm.getLogging().getUserId());
            }

            String xport = airm.getLogging().getTransport();
            if ( xport != null )
            {
                m_aim.setLogfileTransport(regAppInstance, transport);
            }

            String unixPrompt = airm.getLogging().getUnixPrompt();
            if ( unixPrompt != null )
            {
                m_aim.setUnixPrompt(regAppInstance,
                                    airm.getLogging().getUnixPrompt());
            }
        }

        // now process the management policies
        String[] mgmtPolicyPaths = airm.getAppMgmtPolicies().getAppMgmtPolicy();
        String mgmtPolicyName = null;
        String mgmtPolicyPathname = null;
        List mgmtPolicies = null;
        MgmtPolicy regMgmtPolicy = null;
        for (int i = 0; i < mgmtPolicyPaths.length; i++)
        {
            mgmtPolicyPathname = mgmtPolicyPaths[i];
            mgmtPolicyName = mgmtPolicyFromPath(mgmtPolicyPathname);
            mgmtPolicies = m_mpm.mgmtPolicies(mgmtPolicyName);
            Iterator iter = mgmtPolicies.iterator();
            if (!iter.hasNext())
            {
                // there are no preexisting mgmtPolicies of the same name
                // - just create one, mapping it to this registration's appType
                ManagementPolicyId mpId =
                    new ManagementPolicyId(regHost.networkId(), mgmtPolicyName);
                regMgmtPolicy =
                    m_mpm.createPolicy(mpId,
                                       mgmtPolicyPaths[i],
                                       regHost.id(),
                                       regAppType.id(),
                                       false);
            }
            else
            {
                // there exists one or more preexisting mgmtPolicy instances
                // by the name provided in this registration
                // - has it been loaded on one or more hosts?  (there will be
                //   one or more implicit appType/appInstance pairs)
                // - has another appInstance already been registered?
                //
                // for each mgmtPolicy
                // - map it to this registration's appType (if not already)
                // - clean up implicit appInstances and the implicit appType as
                //   appropriate
                Vector derivedAppInstances = null;
                Host derivedHost = null;
                AppType derivedAppType = null;
                AppInstance derivedAppInstance = null;
                while (iter.hasNext())
                {
                    regMgmtPolicy = (MgmtPolicy)iter.next();
                    logger.debug("processing mgmtPolicy " + regMgmtPolicy.name());
                    derivedAppType = regMgmtPolicy.appType();
                    if (regAppType.equals(derivedAppType))
                    {
                        logger.debug("mgmtPolicy already maps to appType " +
                                     regMgmtPolicy.name());
                    }
                    else
                    {
                        // mgmtPolicy currently maps to another app type
                        // - must be an implicit appType
                        logger.debug("mgmtPolicy currently maps to appType " +
                                     derivedAppType.name());
                        // we're looking at an implicit appType with one or more
                        // implicit appInstances (one on this host, x on x other
                        // hosts)
                        // - if the mgmtPolicy's host differs from the
                        //   registration host and there exists no appropriate
                        //   implicit appInstance, we need to create a new
                        //   implicit appInstance
                        // - already have the implicit appType (derivedAppType)
                        // - get the implicit appInstance
                        // - reassign the mgmtPolicy to the registration appType
                        // - delete the implicit appInstance
                        // - if the implicit appType has no more implicit
                        //   appInstances then delete it
                        derivedHost = regMgmtPolicy.host();
                        String implicitAppInstanceName = null;
                        if (!regHost.id().equals(derivedHost.id()))
                        {
                            // mgmtPolicy currently maps to another app type
                            // - must be an implicit appType
                            // mgmtPolicy associated with another host
                            // - and in support of an implicit appInstance on
                            //   that host
                            logger.debug(
                                "mgmtPolicy is associated with a different host: "
                                + derivedHost.name());
                            // registering an app instance and possibly type on
                            // one host. but app types span hosts, and since
                            // this registration may be replacing an implicit
                            // appType with this registration's explicit appType
                            // we might need to create a new implicit appInstance
                            // on this host (which, if we're here, is not the
                            // host of the registration)
                            implicitAppInstanceName =
                                getAppInstanceName(regHost, regAppType);
                            AppInstance implicitAppInstance =
                                m_aim.find(implicitAppInstanceName,
                                           regHost.id());
                            if (implicitAppInstance == null)
                            {
                                // mgmtPolicy currently maps to another app type
                                // - must be an implicit appType
                                // mgmtPolicy associated with another host
                                // - and in support of an implicit appInstance on
                                //   that host
                                // the new explicit appType has no default
                                //   appInstance to replace the implicit appInstance
                                //   which will be deleted due to the implicit
                                //   appType going away

                                String implicitAiLogfileUsername = "";
                                String implicitAiLogfilePassword = "";
                                String implicitAiLogfileDirectories = "";
                                String implicitAiPropertyFile = "";
                                Transport implicitAiLogfileTransport =
                                    new Transport();
                                String implicitAiUnixPrompt = "";

                                AppInstance newAppInstance =
                                    m_aim.createAppInstance(
                                                   regAppType.id(),
                                                   regMgmtPolicy.hostId(),
                                                   implicitAppInstanceName,
                                                   implicitAiLogfileUsername,
                                                   implicitAiLogfilePassword,
                                                   implicitAiLogfileDirectories,
                                                   implicitAiLogfileTransport,
                                                   implicitAiPropertyFile,
                                                   implicitAiUnixPrompt);

                                newAppInstances.add(newAppInstance);
                            }
                        }
                        implicitAppInstanceName =
                            getAppInstanceName(derivedHost, derivedAppType);
                        derivedAppInstance =
                            m_aim.find(implicitAppInstanceName,
                                       derivedHost.id());
                        if (derivedAppInstance != null)
                        {
                            m_aim.deleteAppInstance(derivedAppInstance.id());
                            deletedAppInstances.add(derivedAppInstance);
                        }
                        m_mpm.mapToAppType(regMgmtPolicy, regAppType);
                        logger.debug("mgmtPolicy " + regMgmtPolicy.name() +
                                     " mapped to appType " + regAppType.name());

                        // time to clean up the current implicit appType?...
                        List tmpAppInstances =
                            m_aim.appInstancesByAppType(derivedAppType.id());
                        if (tmpAppInstances.size() < 1)
                        {
                            // this appType has no more appInstances
                            // - delete it
                            logger.debug("deleting appType " +
                                         derivedAppType.name());
                            m_atm.deleteAppType(derivedAppType.id());
                            logger.debug("appType " + derivedAppType.name() +
                                         " deleted");
                        }
                    } // if registration appType is not the same as this appType
                    m_mpm.setPathname(regMgmtPolicy, mgmtPolicyPathname);
                } // while loop to process each mgmtPolicyInstance by this name
            } // else there are one or more mgmtPolicy instances already
        } // for each mgmtPolicyPath in the registration

        // register this appInstance with the process sequencer
        results = registerWithProcessSequencer(airm);

        // notify users of consolidation
        if (!newAppInstances.isEmpty() || !deletedAppInstances.isEmpty())
        {
            logger.debug("notifying users");
            ImAppInstanceConsolidation aiConsolidation =
                new ImAppInstanceConsolidation(newAppInstances,
                                               deletedAppInstances);
            setChanged();
            notifyObservers(aiConsolidation);
        }

        return results;
    }

    public synchronized String newReregistration(ReRegistration airm)
    {
        logger.debug("enter");
        logger.debug("reregistration: " + airm.toString());

        // note that registration and reregistration messages carry the same
        // data and therefore use the same class

        String results = null; // null results == successful registration

        return reregisterWithProcessSequencer(airm);
    }

    public synchronized String newDeregistration(DeRegistration aidm)
    {
        logger.debug("enter");
        logger.debug("deregistration: " + aidm.toString());

        String results = null; // null results == successful registration
        String appTypeName = aidm.getAppType();
        String appInstanceName = aidm.getAppInstance();
        String hostName = aidm.getHostName();
        String version = aidm.getAppVersion();
        Host host = m_hm.find(hostName);
        if ( host == null )
        {
            results = "unable to deregister " + appTypeName + " / " +
                      appInstanceName + " - cannot locate host " + hostName;
            logger.info(results);
            return results;
        }

        AppInstance appInstance = m_aim.find(appInstanceName, host.id());
        if ( appInstance == null )
        {
            results = "unable to deregister " + appTypeName + " / " +
                      appInstanceName + " - cannot locate appType " + appTypeName;
            logger.info(results);
            return results;
        }

        results = deregisterWithProcessSequencer(aidm);

        if ( results == null )
        {
            deleteAppInstance(appInstance.id());
        }

        return results;
    }

    public synchronized String newSolutionRegistration(SolutionRegistration srm)
    {
        logger.debug("enter");
        logger.debug("solution registration: " + srm.toString());

        String results = null; // null results == successful registration
        Collection appInstanceIds = new LinkedList();

        String solutionName = srm.getSolutionName();
        if ( solutionName.equals("") )
        {
            results = "\"\" is not a valid solution name";
            logger.info(results);
            return results;
        }

        SolutionId solutionId = null;
        try
        {
            solutionId = new SolutionId(solutionName);
        }
        catch (Exception ex)
        {
            results = "\"" + solutionName + "\" does not translate into a " +
                      "valid solutionId";
            logger.info(results);
            return results;
        }
        Solution solution = m_sm.get(solutionId);
        if ( solution != null )
        {
            // solution by this name already exists
            results = "solution \"" + solutionName + "\" already exists";
            logger.info(results);
            return results;
        }

        Components solutionComponents = srm.getComponents();
        Component[] componentSet = solutionComponents.getComponent();
        if ( componentSet.length < 1 )
        {
            results = "solution must consist of one or more components";
            logger.info(results);
            return results;
        }

        for (int i = 0; i < componentSet.length; i++)
        {
            String appTypeName = componentSet[i].getAppType();
            AppType appType = m_atm.find(appTypeName);
            if ( appType == null )
            {
                results = "unable to locate appType \"" + appTypeName + "\"";
                logger.info(results);
                return results;
            }

            String appInstanceName = componentSet[i].getAppInstance();
            AppInstance appInstance = m_aim.find1(appInstanceName, appType.id());
            if ( appInstance == null )
            {
                results = "unable to locate appInstance \"" + appInstanceName +
                          "\"";
                logger.info(results);
                return results;
            }

            appInstanceIds.add(appInstance.id());
        }

        try
        {
            solution = m_sm.createSolution(solutionName, appInstanceIds);
        }
        catch (Exception ex1)
        {
            results = "Exception caught while creating new solution \"" +
                      solutionName + "\":" + ex1.toString();
            logger.error(results);
            return results;
        }

        if ( results == null )
        {
            registerSolutionWithProcessSequencer(srm);
        }
        return results;
    }

    protected void notifyAppInstanceRestoration(AppInstance appInstance)
    {
	ImAppInstanceRestoration msg;

	msg = new ImAppInstanceRestoration (appInstance);
	setChanged();
	notifyObservers (msg);
    }

    protected void notifyAppInstanceManagementState (AppInstance appInstance)
    {
	Object msg;

	if (appInstance.managed() == true) {
	    msg = new ImAppInstanceManage (appInstance);
	    setChanged();
	    notifyObservers (msg);
	    logger.debug("appInstance set to 'managed'");
	} else {
	    msg = new ImAppInstanceUnmanage (appInstance);
	    setChanged();
	    notifyObservers (msg);
            logger.debug("appInstance set to 'unmanaged'");
	}
    }

    /**
     * @roseuid 3F48F59203AD
     */
    public void manageAppInstance(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        logger.debug("managing appInstance " + appInstanceId.toString());
        AppInstance appInstance = m_aim.find(appInstanceId);
        if (appInstance == null)
        {
            logger.debug("unable to locate appInstance in persistent datastore");
            return;
        }

        if (appInstance.managed())
        {
            logger.debug("appInstance " + appInstance.name() +
                         " is already in managed state");
        }
        else
        {
            m_aim.manage(appInstanceId);
	    notifyAppInstanceManagementState(appInstance);
        }
    }

    /**
     * @roseuid 3F48F59D00EB
     */
    public void unmanageAppInstance(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        logger.debug("unmanaging appInstance " + appInstanceId);
        AppInstance appInstance = m_aim.find(appInstanceId);
        if (appInstance == null)
        {
            logger.debug("unable to locate appInstance in persistent datastore");
            return;
        }

        if (appInstance.managed())
        {
            m_aim.unmanage(appInstanceId);
	    notifyAppInstanceManagementState (appInstance);
        }
        else
        {
            logger.debug("appInstance already in unmanaged state");
        }
    }

    /**
     * @param msg
     * @roseuid 3F60C6F1004F
     */
    public synchronized void deleteInstrumentation(RemoveMicroAgent msg)
    {
        logger.debug("enter");
        logger.debug("instrumentation terminated: ID = " +
                     msg.instrumentationId());

        Host host = m_hm.find(msg.agentId().name());
        if (host == null)
        {
            logger.error("unable to find host with agentId " + msg.agentId());
            return;
        }

        MicroAgentID uAgentId = msg.instrumentationId().raw();
        Instrumentation instrumentation =
            m_im.find(new MicroagentId(host.networkId().raw(), uAgentId));
        if (instrumentation == null)
        {
            logger.debug("instrumentation not cached");
            return;
        }

        instrumentation.deactivate();
    }

    /**
     * @param msg
     * @roseuid 3F60C6F10154
     */
    public synchronized void deleteMgmtPolicy(RemoveMgmtpolicy msg)
    {
        logger.debug("enter");
        logger.debug("management policy unloaded: ID = " + msg.mgmtPolicyId());
        // we don't remove this from our persistent datastore
        // instead, we remove when actually deleting the associated appType
        // just change the state here
        Host host = m_hm.findByIpAddress(msg.agentInstance().getIPAddress());
        if ( host == null )
        {
            logger.warn("unable to locate host in inventory");
            return;
        }

        MgmtPolicy mgmtPolicy = m_mpm.find(msg.mgmtPolicyId().name(), host.id());
        if (mgmtPolicy == null)
        {
            logger.debug("mgmtPolicy not found in persistent datastore");
            return;
        }

        m_mpm.setLoaded(mgmtPolicy, false);
        ImMgmtPolicyUnload mgmtPolicyUnload =
            new ImMgmtPolicyUnload(mgmtPolicy);
        setChanged();
        notifyObservers(mgmtPolicyUnload);
    }

    private String mgmtPolicyFromPath(String path)
    {
        logger.debug("extracting mgmtPolicyName from pathname \"" + path + "\"");
        String filename = null;
        String mgmtPolicyName = null;

        // remove the pathname directory elements...
        int slashIndex = path.lastIndexOf('/');
        if (slashIndex == -1)
        {
            filename = path;
        }
        filename = path.substring(slashIndex + 1);

        // remove the filename suffix
        int extensionSeparatorIndex = filename.lastIndexOf('.');
        if ( extensionSeparatorIndex == -1 )
        {
            mgmtPolicyName = filename;
        }
        mgmtPolicyName = filename.substring(0,extensionSeparatorIndex);

        logger.debug("returning mgmtPolicyName \"" + mgmtPolicyName + "\"");
        return mgmtPolicyName;
    }

    private String registerWithProcessSequencer(Registration air)
    {
        logger.debug("registering " + air.getAppType() + " / " +
                     air.getAppInstance());
        String results = null; // null results == good results
        if ( air.getProcessPropertyFile() != null )
        {
            ProcessManager.instance().register(air.getAppType(),
                                               air.getAppInstance(),
                                               air.getProcessPropertyFile());
        }
        return results;
    }

    private String reregisterWithProcessSequencer(ReRegistration airr)
    {
        logger.debug("reregistering application " + airr.getAppType() + " / " +
                     airr.getAppInstance());
        String results = null; // null results == good results
        if ( airr.getProcessPropertyFile() != null )
        {
            ProcessManager.instance().register(airr.getAppType(),
                                               airr.getAppInstance(),
                                               airr.getProcessPropertyFile());
        }
        return results;
    }

    private String deregisterWithProcessSequencer(DeRegistration aidm)
    {
        String appType = aidm.getAppType();
        String appInstance = aidm.getAppInstance();
        logger.debug("deregistering " + appType + " / " + appInstance);
        String response = null; // null == good
        ProcessManager.instance().unregister(appType, appInstance);
        return response;
    }

    private String registerSolutionWithProcessSequencer(
        SolutionRegistration srm)
    {
        logger.debug("registering a solution with the process sequencer: " +
                     srm.getSolutionName());
        String results = null; // null results == good results
        StringWriter xmlStream = new StringWriter();
        try
        {
            srm.marshal(xmlStream);
        }
        catch (MarshalException ex)
        {
            results = "MarshalException caught while converting message back " +
                      "into xml for ProcessManager: " + ex;
        }
        catch (ValidationException ex)
        {
            results = "ValidationException caught while converting message " +
                      "back into xml for ProcessManager: " + ex;
        }

        if ( results == null )
        {
            ProcessManager.instance().registerSolution(xmlStream.toString());
        }
        else
        {
            logger.error(results);
        }
        return results;
    }

    /**
     *
     * @param host
     * @param uAgentId
     * @return
     */
    private String getAppTypeName(Host host, MicroAgentID uAgentId)
    {
        logger.debug("enter");
        String uAgentName = uAgentId.getName();
        logger.debug("uAgentName = " + uAgentName);
        String appTypeName = null;

        appTypeName = getAppTypeNameFromPredefinesByInstrumentation(uAgentName);
        if ( appTypeName == null )
        {
            logger.debug("attempting to derive appType name from " +
                         "instrumentation name (applies to watchdog " +
                         "instrumentation only)");
            appTypeName = appTypeNameFromWdInstrumentation(uAgentName);
        }
        if ( appTypeName == null )
        {
            logger.debug("attempting to obtain appType information from the uAgent");
            MethodInvocation mi =
                new MethodInvocation("getApplicationType", null);
            MicroAgentData results = null;
            try
            {
                results = host.agentManager().invoke(uAgentId, mi);
                Object resultData = results.getData();
                if ( resultData != null )
                {
                    if ( resultData instanceof CompositeData )
                    {
                        CompositeData cd = (CompositeData)resultData;
                        //DataElement[] data = cd.getDataElements();
                        //appTypeName = (String)data[0].getValue();
                        appTypeName = (String)cd.getValue("ApplicationType");
                        logger.debug("appTypeName obtained via AMI: " +
                                     appTypeName);
                    }
                    else if ( resultData instanceof MicroAgentException )
                    {
                        MicroAgentException mae = (MicroAgentException)resultData;
                        try
                        {
                            logger.info("MicroAgentException returned by " +
                                        "invocation of method " +
                                        "\"getApplicationType\": " +
                                        mae.getException());
                        }
                        catch (IOException ex1)
                        {
                            logger.error("IOException caught while " +
                                         "retrieving exception from " +
                                         "microagent response: " + ex1);
                        }
                        catch (ClassNotFoundException ex1)
                        {
                            logger.error("ClassNotFoundException caught " +
                                         "while retrieving exception from " +
                                         "microagent response: " + ex1);
                        }
                    }
                }
            }
            catch (MicroAgentException ex)
            {
                logger.warn("exception caught while attempting to retrieve " +
                            "appType name: " + ex);
            }
        }
        logger.debug("returning appTypeName " + appTypeName);
        return appTypeName;
    }

    private String getAppTypeName(String mgmtPolicyName)
    {
        logger.debug("enter");
        logger.debug("mgmtPolicyName = " + mgmtPolicyName);
        String appTypeName = null;
        appTypeName = getAppTypeNameFromPredefinesByRulebase(mgmtPolicyName);
        if ( appTypeName == null )
        {
            appTypeName = implicitAppTypeName1(mgmtPolicyName);
        }
        logger.debug("returning appTypeName " + appTypeName);
        return appTypeName;
    }

    private String
        getAppInstanceName(Host host, MicroAgentID uAgentId, AppType appType)
    {
        logger.debug("enter");
        String hostName = host.name();
        String uAgentName = uAgentId.getName();
        //String appTypeName = appType.name();
        logger.debug("hostName = " + hostName);
        logger.debug("uAgentName = " + uAgentName);
        //logger.debug("appTypeName = " + appTypeName);
        String appInstanceName = null;

        appInstanceName =
                getAppInstanceNameFromPredefinesByInstrumentation(hostName,
                                                                  uAgentName);
        if ( appInstanceName == null )
        {
            logger.debug("attempting to derive appInstance name from " +
                         "instrumentation name (applies to watchdog " +
                         "instrumentation only)");
            appInstanceName = appInstanceNameFromWdInstrumentation(uAgentName);
        }
        if ( appInstanceName == null )
        {
            logger.debug("attempting to obtain appInstance information from " +
                         "the uAgent");
            MethodInvocation mi =
                new MethodInvocation("getApplicationInstanceId", null);
            MicroAgentData results = null;
            try
            {
                results = host.agentManager().invoke(uAgentId, mi);
                Object resultData = results.getData();
                if ( resultData != null )
                {
                    if ( resultData instanceof CompositeData )
                    {
                        CompositeData cd = (CompositeData)resultData;
                        appInstanceName =
                            (String)cd.getValue("ApplicationInstanceId");
                        logger.debug("appInstanceName obtained via AMI: " +
                                     appInstanceName);
                    }
                    else if ( resultData instanceof MicroAgentException )
                    {
                        MicroAgentException mae = (MicroAgentException)resultData;
                        try
                        {
                            logger.info("MicroAgentException returned by " +
                                        "invocation of method " +
                                        "\"getApplicationInstanceId\": " +
                                        mae.getException());
                        }
                        catch (IOException ex1)
                        {
                            logger.error("IOException caught while " +
                                         "retrieving exception from " +
                                         "microagent response: " + ex1);
                        }
                        catch (ClassNotFoundException ex1)
                        {
                            logger.error("ClassNotFoundException caught " +
                                         "while retrieving exception from " +
                                         "microagent response: " + ex1);
                        }
                    }
                }
            }
            catch (MicroAgentException ex)
            {
                logger.warn("exception caught while attempting to retrieve " +
                            "appInstance ID: " + ex);
            }
        }
        logger.debug("returning appInstanceName " + appInstanceName);
        return appInstanceName;
    }

    private String getAppInstanceName(Host host, AppType appType)
    {
        logger.debug("enter");
        String hostName = host.name();
        logger.debug("hostName = " + hostName);
        String appTypeName = appType.name();
        logger.debug("appTypeName = " + appTypeName);

        String appInstanceName =
            getAppInstanceNameFromPredefinesByAppType(hostName, appTypeName);
        if ( appInstanceName == null )
        {
            appInstanceName = implicitAppInstanceName1(host, appType);
        }
        logger.debug("returning appInstanceName " + appInstanceName);
        return appInstanceName;
    }

    private String getAppTypeNameFromPredefinesByInstrumentation(String uAgentName)
    {
        logger.debug("enter");
        logger.debug("uAgentName = " + uAgentName);

        if ( m_predefinedApps != null )
        {
            com.cisco.eManager.eManager.inventory.xml.AppType[] pdAppTypes =
                m_predefinedApps.getAppType();
            int appTypeCount = pdAppTypes.length;
            com.cisco.eManager.eManager.inventory.xml.Instrumentation[]
                pdInstrumentations = null;
            int instCount;
            for (int i = 0; i < appTypeCount; i++)
            {
                logger.debug("examining appType " + pdAppTypes[i].getName());
                pdInstrumentations =
                    pdAppTypes[i].getAppInstance().getInstrumentation();
                instCount = pdInstrumentations.length;
                for (int j = 0; j < instCount; j++)
                {
                    logger.debug("examining instrumentation named " +
                                 pdInstrumentations[j].getName());
                    if ( uAgentName.startsWith(pdInstrumentations[j].getName()) )
                    {
                        logger.debug("instrumentation located: returning " +
                                     "appTypeName " + pdAppTypes[i].getName());
                        return pdAppTypes[i].getName();
                    }
                }
            }
        }
        logger.debug("returning null");
        return null;
    }

    private String getAppTypeNameFromPredefinesByRulebase(String rulebaseName)
    {
        logger.debug("enter");
        logger.debug("rulebaseName = " + rulebaseName);
        if ( m_predefinedApps != null )
        {
            com.cisco.eManager.eManager.inventory.xml.AppType[] pdAppTypes =
                m_predefinedApps.getAppType();
            int appTypeCount = pdAppTypes.length;
            String[] pdMgmtPolicyNames = null;
            int mgmPolicyCount;
            for (int i = 0; i < appTypeCount; i++)
            {
                logger.debug("examining appType " + pdAppTypes[i].getName());
                pdMgmtPolicyNames = pdAppTypes[i].getRulebaseName();
                mgmPolicyCount = pdMgmtPolicyNames.length;
                for (int j = 0; j < mgmPolicyCount; j++)
                {
                    logger.debug("examining rulebase " + pdMgmtPolicyNames[j]);
                    if ( rulebaseName.equals(pdMgmtPolicyNames[j]) )
                    {
                        logger.debug("rulebase located: returning appTypeName " +
                                     pdAppTypes[i].getName());
                        return pdAppTypes[i].getName();
                    }
                }
            }
        }
        logger.debug("returning null");
        return null;
    }

    private String
        getAppInstanceNameFromPredefinesByInstrumentation(
            String hostName,
            String uAgentName)
    {
        logger.debug("enter");
        logger.debug("hostName = " + hostName);
        logger.debug("uAgentName = " + uAgentName);
        if ( m_predefinedApps != null )
        {
            com.cisco.eManager.eManager.inventory.xml.AppType[] pdAppTypes =
                m_predefinedApps.getAppType();
            int appTypeCount = pdAppTypes.length;
            com.cisco.eManager.eManager.inventory.xml.Instrumentation[]
                pdInstrumentations = null;
            int instCount;
            for (int i = 0; i < appTypeCount; i++)
            {
                logger.debug("examining appType " + pdAppTypes[i].getName());
                pdInstrumentations =
                    pdAppTypes[i].getAppInstance().getInstrumentation();
                instCount = pdInstrumentations.length;
                for (int j = 0; j < instCount; j++)
                {
                    logger.debug("examining instrumentation " +
                                 pdInstrumentations[j].getName());
                    if (uAgentName.startsWith(pdInstrumentations[j].getName()))
                    {
                        String appInstanceName =
                            pdAppTypes[i].getAppInstance().getId() + "-" +
                            hostName;
                        logger.debug("appType located: returning " +
                                     "appInstanceName " + appInstanceName);
                        return appInstanceName;
                    }
                }
            }
        }
        logger.debug("returning null");
        return null;
    }

    private String getAppInstanceNameFromPredefinesByAppType(String hostName,
                                                             String appTypeName)
    {
        logger.debug("enter");
        logger.debug("hostName = " + hostName);
        logger.debug("appTypeName = " + appTypeName);
        if ( m_predefinedApps != null )
        {
            com.cisco.eManager.eManager.inventory.xml.AppType[] pdAppTypes =
                m_predefinedApps.getAppType();
            int appTypeCount = pdAppTypes.length;
            for (int i = 0; i < appTypeCount; i++)
            {
                logger.debug("examining appType " + pdAppTypes[i].getName());
                if ( pdAppTypes[i].getName().equals(appTypeName) )
                {
                    String appInstanceName =
                        pdAppTypes[i].getAppInstance().getId() + "-" + hostName;
                    logger.debug("appType located: returning appInstanceName " +
                                 appInstanceName);
                    return appInstanceName;
                }
            }
        }
        logger.debug("returning null");
        return null;
    }

    private String implicitAppTypeName1(String name)
    {
        return name;
    }

    private String implicitAppInstanceName1(Host host, AppType appType)
    {
        return appType.name() + "-" + host.name();
    }

    private AppInstance findProcessSequencer(Host host)
    {
        AppType psAppType = m_atm.find(InventoryGlobals.psAppTypeName());
        if ( psAppType == null )
        {
            logger.warn("unable to locate process sequencer on new appInstance's host");
            return null;
        }

        List psAis = m_aim.find(host.id(), psAppType.id());
        if ( psAis.isEmpty() )
        {
            logger.warn("unable to locate process sequencer on new appInstance's host");
            return null;
        }
        // there should be at most one item in the list - just grab the first
        AppInstance psAi = (AppInstance)psAis.get(0);
        return psAi;
    }

    private String regLogDirectoriesToString(
        com.cisco.eManager.common.register.registration.LogDirectories ld)
    {
        logger.debug("enter");
        String[] logDirs = ld.getLogDirectory();
        StringBuffer lds = new StringBuffer();
        int count = logDirs.length;
        //logger.debug("log dir count: " + count);
        for (int i = 0; i < count; i++)
        {
            //logger.debug("appending " + logDirs[i]);
            lds.append(logDirs[i]);
            lds.append(';');
        }
        //logger.debug("log dirs: " + lds);
        return lds.toString();
    }

    private String reregLogDirectoriesToString(
        com.cisco.eManager.common.register.reRegistration.LogDirectories ld)
    {
        String[] logDirs = ld.getLogDirectory();
        StringBuffer lds = new StringBuffer();
        int count = logDirs.length;
        for (int i = 0; i < count; i++)
        {
            lds.append(logDirs[i]);
            lds.append(';');
        }
        return lds.toString();
    }

    private boolean isWdInstrumentation(String instrumentationName)
    {
        return instrumentationName.startsWith(
            ProcessHawkConsole.WATCHDOG_MICRO_AGENT_NAME);
    }

    private String appTypeNameFromWdInstrumentation(String instrumentationName)
    {
        String appTypeName = null;
        if ( isWdInstrumentation(instrumentationName) )
        {
            StringTokenizer tk = new StringTokenizer(instrumentationName, "-");
            if ( tk.countTokens() == 3 )
            {
                String wdPrefix = tk.nextToken();
                appTypeName = tk.nextToken();
                logger.debug("appTypeName \"" + appTypeName + "\" extracted " +
                             "from watchdog uAgent name \"" +
                             instrumentationName + "\"");
            }
            else
            {
                logger.warn("malformed watchdog uAgent name: " +
                            instrumentationName);
            }
        }
        else
        {
            logger.debug("instrumentation \"" + instrumentationName +
                         "\" is not a watchdog instrumentation");
        }
        return appTypeName;
    }

    private String appInstanceNameFromWdInstrumentation(String instrumentationName)
    {
        String appInstanceName = null;
        if ( isWdInstrumentation(instrumentationName) )
        {
            StringTokenizer tk = new StringTokenizer(instrumentationName, "-");
            if ( tk.countTokens() == 3 )
            {
                String wdPrefix = tk.nextToken();
                String appTypeName = tk.nextToken();
                appInstanceName = tk.nextToken();
                logger.debug("appInstanceName \"" + appInstanceName +
                             "\" extracted from watchdog uAgent name \"" +
                             instrumentationName + "\"");
            }
            else
            {
                logger.warn("malformed watchdog uAgent name: " +
                            instrumentationName);
            }
        }
        else
        {
            logger.debug("instrumentation \"" + instrumentationName +
                         "\" is not a watchdog instrumentation");
        }
        return appInstanceName;
    }
}

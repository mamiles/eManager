package com.cisco.eManager.eManager;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.cisco.eManager.eManager.inventory.*;
import com.cisco.eManager.eManager.inventory.network.*;
import com.cisco.eManager.eManager.inventory.view.*;
import com.cisco.eManager.eManager.network.*;
import com.cisco.eManager.eManager.process.ProcessManager;
import com.cisco.eManager.eManager.log.LogManager;
import com.tibco.tibrv.*;

public class EManagerController
    extends HttpServlet
{
    private Logger logger = Logger.getLogger(EManagerController.class);

    private String eManagerDir = null;
    private String catalinaHome = null;
    private String tomcatPort = null;

    private String m_hawkDomain = null;
    private String m_hawkService = null;
    private String m_hawkNetwork = null;
    private String m_hawkDaemon = null;

    // Inventory components
    // - core inventory
    private InventoryManager m_im;
    private ImAppInstanceObserver m_imAio;
    // - view management
    private AppViewManager m_avm;
    private HostViewManager m_hvm;
    private SolutionViewManager m_svm;
    private AppInstanceObserver m_aio;
    private AppTypeObserver m_ato;
    private HostObserver m_ho;
    private SolutionObserver m_so;
    // - NBAPI notification componets
    private InventoryNotificationDistributor m_ind;
    private InventoryObserver m_io;
    private TibRvNotificationSender m_trns;

    public EManagerController()
        throws Exception
    {}

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);

        Enumeration e = config.getInitParameterNames();
        String param = null;
        String value = null;
        ServletContext context = config.getServletContext();
        while (e.hasMoreElements())
        {
            param = (String)e.nextElement();
            value = config.getInitParameter(param);
            System.setProperty(param, value);
        }

        eManagerDir = System.getProperty("EMANAGER_ROOT");
        if (eManagerDir == null)
        {
            throw new ServletException("EMANAGER_ROOT is not set");
        }

        catalinaHome = System.getProperty("CATALINA_HOME");
        if (catalinaHome == null)
        {
            throw new ServletException("CATALINA_HOME is not set");
        }

        tomcatPort = System.getProperty("TOMCAT_PORT");
        if (tomcatPort == null)
        {
            throw new ServletException("TOMCAT_PORT is not set");
        }

        initLog4j();
        initComponents();
    }

    private void initLog4j()
    {
        String log4jConfigFile = eManagerDir + "/config/log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);
    }

    private void initComponents()
        throws ServletException
    {
        logger.debug("Initializing eManager components....");
        try
        {
            // initialize misc. obj, i.e. Tibco listerner, ...

            // initialize eventManager

            // initialize inventoryManager
            initializeInventoryManager();

            // initialize processManager
            ProcessManager.instance();

            // initialize the network listeners
            initializeNetwork();

            // initialize Log Manager
            LogManager.instance();

        }
        catch (Exception ex)
        {
            logger.error("Unable to initialize eManager components", ex);
            throw new ServletException(ex);
        }

        logger.info("eManager Serve initialized");
    }

    private void initializeInventoryManager()
    {
        // 1 - create inventory objects
        //   a: create core inventory objects
        //   b: create inventory view objects (these depend upon core inventory)
        //   c: create inventory's NBAPI objects (these depend upon rest of inventory)
        // 2 - restore inventory from DB (this will generate NBAPI notifications)
        // 3 - turn on processing of incoming messages from network/users

        // 1.a: core inventory
        try
        {
            m_im = InventoryManager.instance(); // takes care of core inventory
            m_imAio = new ImAppInstanceObserver();
        }
        catch (Exception ex2)
        {
            logger.error("Exception caught while initializing core inventory " +
                         "management componets: " + ex2);
        }
        // 1.b: views
        try
        {
            m_avm = AppViewManager.instance();
            m_hvm = HostViewManager.instance();
            m_svm = SolutionViewManager.instance();

            m_aio = new AppInstanceObserver();
            m_ato = new AppTypeObserver();
            m_ho = new HostObserver();
            m_so = new SolutionObserver();
        }
        catch (Exception ex)
        {
            logger.error("Exception caught while initializing view " +
                         "management: " + ex);
        }
        // 1.c: NBAPI notifications
        try
        {
            m_ind = InventoryNotificationDistributor.instance();
            m_trns = TibRvNotificationSender.instance();
            m_io = new InventoryObserver();
        }
        catch (Exception ex)
        {
            logger.error("Exception caught while initializing inventory " +
                         "NBAPI notification objects: " + ex);
        }

        // 2: restore from DB
        /** @todo break out restoration (from db) from initialization and launch from here */

        // 3
        MsgProcessor mp = new MsgProcessor();
        mp.start();
    }

    private void initializeNetwork()
    {
        AgentListener agentListener = null;
        MessageListener deregistrationListener = null;
        MessageListener registrationListener = null;
        MessageListener reregistrationListener = null;
        MessageListener solutionListener = null;
	TibcoListener msgListener = null;

        try
        {
            agentListener = new AgentListener();
            agentListener.start();
        }
        catch (Exception e)
        {
            logger.error("exception caught while initializing the " +
                         "agentListener: " + e);
        }

        try
        {
            logger.debug("Start TibcoListener...");
            msgListener = new TibcoListener();
            msgListener.start();
        }
        catch (Exception e)
        {
            logger.error("exception caught while initializing the TibcoListener" + e);
        }

        try
        {
            deregistrationListener =
                new MessageListener("app deregistration thread",
                                    MessageListener.MODE_APP_DEREGISTRATION);
            deregistrationListener.start();
        }
        catch (Exception e)
        {
            logger.error("exception caught while initializing the " +
                         "appInstance deregistration listener: " + e);
        }

        try
        {
            registrationListener =
                new MessageListener("app registration thread",
                                    MessageListener.MODE_APP_REGISTRATION);
            registrationListener.start();
        }
        catch (Exception e)
        {
            logger.error("exception caught while initializing the " +
                         "appInstance registration listener: " + e);
        }

        try
        {
            reregistrationListener =
                new MessageListener("app registration thread",
                                    MessageListener.MODE_APP_REREGISTRATION);
            reregistrationListener.start();
        }
        catch (Exception e)
        {
            logger.error("exception caught while initializing the " +
                         "appInstance reregistration listener: " + e);
        }

        try
        {
            solutionListener =
                new MessageListener("solution registraiton thread",
                                    MessageListener.MODE_SOLUTION_REGISTRATION);
            solutionListener.start();
        }
        catch (Exception e)
        {
            logger.error("exception caught while initializing the " +
                         "solution registration listener: " + e);
        }
    }
}

package com.cisco.eManager.eManager;

import java.util.*;

import org.apache.log4j.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.cisco.eManager.eManager.inventory.*;
import com.cisco.eManager.eManager.inventory.network.*;
import com.cisco.eManager.eManager.network.*;
import com.cisco.eManager.eManager.process.ProcessManager;
import com.cisco.eManager.eManager.log.LogManager;

public class EManagerMain
{
    private Logger logger = Logger.getLogger(EManagerMain.class);

    private String eManagerDir = null;
    private String catalinaHome = null;
    private String tomcatPort = null;

    private String m_hawkDomain = null;
    private String m_hawkService = null;
    private String m_hawkNetwork = null;
    private String m_hawkDaemon = null;

    public EManagerMain()
    {}

    public void init()
	    throws Exception
    {

        eManagerDir = System.getProperty("EMANAGER_ROOT");
        if (eManagerDir == null)
        {
            throw new Exception("EMANAGER_ROOT is not set");
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
	    throws Exception
    {
        logger.debug("Initializing eManager components....");
        try
        {
            // initialize misc. obj, i.e. Tibco listerner, ...

            // initialize eventManager

            // initialize inventoryManager
            logger.debug("initialize Inventory Manager");
            initializeInventoryManager();

            // initialize processManager
            ProcessManager.instance();

            // initialize the network listeners
            logger.debug("initialize Network Listeners");
            initializeNetwork();

            // initialize Log Manager
            LogManager.instance();

        }
        catch (Exception ex)
        {
            logger.error("Unable to initialize eManager components", ex);
            throw new Exception (ex);
        }

        logger.info("eManager Serve initialized");
    }

    private void initializeInventoryManager()
    {
        InventoryManager im = InventoryManager.instance();
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
            logger.debug("Start AgentListener...");
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
            logger.debug("Start DeRegistrationListener...");
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
            logger.debug("Start RegistrationListener...");
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
            logger.debug("Start ReRegistrationListener...");
            reregistrationListener =
                new MessageListener("app reregistration thread",
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
            logger.debug("Start SolutionRegistrationListener...");
            solutionListener =
                new MessageListener("solution registration thread",
                                    MessageListener.MODE_SOLUTION_REGISTRATION);
            solutionListener.start();
        }
        catch (Exception e)
        {
            logger.error("exception caught while initializing the " +
                         "solution registration listener: " + e);
        }
    }

    public void startRun()
    {
	    logger.debug("eManager server is running");
	    while(true) {
                try
                {
                    Thread.sleep(1000000);
                }
                catch (InterruptedException ex)
                {
                    logger.debug("thread interrupted: " + ex);
                }
	    }
    }



    public static void main (String[] args)
    {
	    EManagerMain emgr = new EManagerMain();
	    try {
	    	emgr.init();
	    	emgr.startRun();
	    } catch (Exception e) {
		    System.out.println("Exception caught while initializing eManager server.." + e);
	    }


	    System.exit(1);
    }

}


//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\database\\JNDIRegistryManager.java

package com.cisco.eManager.eManager.database;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.cisco.eManager.common.database.EmanagerDatabaseException;
import com.cisco.eManager.common.database.EmanagerDatabaseStatusCode;

import com.cisco.eManager.eManager.util.GlobalProperties;

public class JNDIRegistryManager implements Remote, Serializable
{
    private static JNDIRegistryManager jndiRegistryManager = null;

    private String registryHost = null;
    private String bindName = null;
    private String bindString = null;
    private Context jndiContext = null;
    private int registryPort;

    private static Logger      logger = Logger.getLogger(JNDIRegistryManager.class);

    /**
     * @roseuid 3F5767EE02C4
     */
    private JNDIRegistryManager() throws EmanagerDatabaseException
    {
	String propertyValue;
	Properties systemProperties;

	systemProperties = GlobalProperties.instance().getProperties();

	bindName = systemProperties.getProperty (DatabaseGlobals.JNDIRegistryBindingNameKey);
        if (bindName == null) {
            bindName = DatabaseGlobals.JNDIRegistryBindingNameDefault;
	}
	logger.log(Priority.INFO,
		   DatabaseGlobals.JNDIRegistryBindingNameValueMsg +
		   bindName);

        propertyValue = systemProperties.getProperty (DatabaseGlobals.JNDIRegistryPortKey);
        if (propertyValue == null) {
            registryPort = DatabaseGlobals.JNDIRegistryPortDefault;
            logger.log(Priority.INFO,
                       "Using Default JNDIRegistryPort value: " +
                       Integer.toString(registryPort));
        } else {
            try
            {
                registryPort = Integer.parseInt(propertyValue);
                logger.log(Priority.INFO,
                           "Using JNDIRegistryPort value: " +
                           propertyValue);
            }
            catch (NumberFormatException e)
            {
                registryPort = DatabaseGlobals.JNDIRegistryPortDefault;
                logger.log(Priority.ERROR,
                           "Format exception converting JNDIRegistryPort from the " +
                           "properties.  Using the default: " +
                           Integer.toString(registryPort));
            }
        }

        registryHost = systemProperties.getProperty (DatabaseGlobals.DatabaseConnectionDatabaseHostKey);
        if (registryHost == null) {
            registryHost = DatabaseGlobals.DatabaseConnectionDatabaseHostDefault;
        }
        logger.log(Priority.INFO,
                   DatabaseGlobals.RegistryHostValueMsg +
                   registryHost);

        bindString = "//" + registryHost + ":" + registryPort + "/" + bindName;
	initializeRegistry();
	initializeJNDIContext();
    }

    /**
     * @return com.cisco.eManager.eManager.database.JNDIRegistryManager
     * @roseuid 3F3AA68E00BF
     */
    public static JNDIRegistryManager instance() throws EmanagerDatabaseException
    {
	if (jndiRegistryManager == null) {
	    jndiRegistryManager = new JNDIRegistryManager();
	}

	return jndiRegistryManager;
    }

    /**
     * @roseuid 3F3AA75202AB
     */
    private void initializeRegistry() throws EmanagerDatabaseException
    {
	logger.log(Priority.DEBUG, "Initializing the Registry engine.");

	try {
	    Registry reg = LocateRegistry.getRegistry(registryPort);
            if (reg != null) {
                if (reg.lookup(bindName) != null) {
                   // EmanagerDatabaseException e;

		logger.log(Priority.FATAL, "A registry is unexpectedly already running.");
                // e = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.RegistryUnexpectedlyRunning,
		//				   EmanagerDatabaseStatusCode.RegistryUnexpectedlyRunning.getStatusCodeDescription());
                //  throw e;
            }

                Naming.rebind(bindString, this);
                logger.log(Priority.INFO, "Bound to local Registry.");
            }

	    return;
	}
	catch (Exception rEx) {
	}

	// We didn't find a local registry, let's start one.
	try {
	    logger.log(Priority.DEBUG, "Initializing a new local Registry.");
	    LocateRegistry.createRegistry(registryPort);
	    Naming.rebind(bindString, this);
	    logger.log(Priority.DEBUG, "System Bound to local Registry.");

	}
	catch (Exception rEx) {
	    String logString;
	    EmanagerDatabaseException e;

	    logString =
		EmanagerDatabaseStatusCode.UnableToStartRegistry.getStatusCodeDescription() +
		" - " +
		bindString;
	    e = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.RegistryUnexpectedlyRunning,
					       logString);
	    throw e;
	}
    }

    /**
     * @roseuid 3F3AA75E0190
     */
    private void initializeJNDIContext() throws EmanagerDatabaseException
    {
	logger.log(Priority.DEBUG, "Enter");

	Hashtable env = new Hashtable(11);
	env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
	env.put(Context.OBJECT_FACTORIES, "com.sybase.jdbc2.jdbc.SybObjectFactory");
	env.put(Context.PROVIDER_URL, "rmi://localhost:" + registryPort + "/");

	try {
	    jndiContext = new InitialContext(env);
	}
	catch (NamingException e) {
	    String logString;
	    EmanagerDatabaseException eee;

	    logString =
		EmanagerDatabaseStatusCode.UnableToCreateJNDIContext.getStatusCodeDescription() +
		" - " +
		e.getMessage();

	    logger.log(Priority.FATAL, logString);
	    eee = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.UnableToCreateJNDIContext,
						 logString);
	    throw eee;
	}

	return;
    }

    /**
     * @return Context
     * @roseuid 3F3AA7DB0313
     */
    public Context getJNDIContext()
    {
	return jndiContext;
    }
}

//Source file: /vob/emanager/src/com/cisco/eManager/eManager/util/GlobalProperties.java

package com.cisco.eManager.eManager.util;

import java.lang.System;
import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;

public class GlobalProperties
{
    private static Logger logger = Logger.getLogger(GlobalProperties.class);
    /**
     * The following attribute represents the singleton instance
     * of the GlobalProperties object.
     */
    static private GlobalProperties globalProperties = null;

    /**
     * The following attribute represents the initial start time,
     * in milleseconds, of the application.  The time is represented
     * as the number of milleseconds since 1 jan 1970, UTC.
     */
    static private long systemStartTime = System.currentTimeMillis();

    static public String CarriageReturn = "\n";

    private ThreadGroup debuggerThreadGroup = null;

    /**
     * @roseuid 3F54D88103E2
     */
    private GlobalProperties()
    {
        loadConfig("em.properties");

        try {
            debuggerThreadGroup = new ThreadGroup ("Debugger Threads");
        }
        catch (SecurityException e) {
	    // fix
            // message
            debuggerThreadGroup = null;
        }
    }

    static public GlobalProperties instance()
    {
        if (globalProperties == null)
        {
            globalProperties = new GlobalProperties();
        }

        return globalProperties;
    }

    public ThreadGroup getDebuggerThreadGroup()
    {
	return debuggerThreadGroup;
    }

    public Properties getProperties()
    {
        return System.getProperties();
    }

    /**
     *
     * @return The amount of uptime, in seconds, for the agent.
     */
    public static long getSystemUptime()
    {
        long currentTime;
        long upTime;

        currentTime = System.currentTimeMillis();
        upTime = (currentTime - systemStartTime) / 1000;
        return upTime;
    }

    public void loadConfig(String propFile)
    {

        String eMgrDir = System.getProperty("EMANAGER_ROOT");
        String filename = eMgrDir + "/config/" + propFile;
        Properties p = new Properties(System.getProperties());
        try
        {
            FileInputStream propFileStream =
                new FileInputStream(filename);
            p.load(propFileStream);
            System.setProperties(p);
        }
        catch (FileNotFoundException e)
        {
            logger.error("Property File: " + filename + " is not found");
        }
        catch (IOException e)
        {
            logger.error("Caught IOException while processing property file: " +
                         filename);
        }
    }

}

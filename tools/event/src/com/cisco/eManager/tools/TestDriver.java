package com.cisco.eManager.tools;

import java.util.Iterator;
import java.util.Collection;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import COM.TIBCO.hawk.console.hawkeye.*;

import org.apache.log4j.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.cisco.eManager.common.database.EmanagerDatabaseException;

import com.cisco.eManager.eManager.database.DatabaseInterface;

public class TestDriver
{
    private Logger logger = Logger.getLogger(TestDriver.class);

    private String eManagerDir = null;

    public TestDriver ()
    {
        eManagerDir = System.getProperty("EMANAGER_ROOT");
        if (eManagerDir == null) {
            eManagerDir = "d:/vws/root/mjmatch-main-snapshot/emanager";
        }

        String log4jConfigFile = eManagerDir + "/config/log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);
    }

    public void run()
    {
        try {
            DatabaseInterface.instance().heartbeat();
            DatabaseInterface.instance().heartbeat();
            DatabaseInterface.instance().heartbeat();
        }
        catch (EmanagerDatabaseException e) {

        }

        System.exit(1);
    }

    public static void main (String args[])
    {
        TestDriver driver;

        driver = new TestDriver();

        driver.run();
    }
}

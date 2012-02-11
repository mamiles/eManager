/*********************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.	Possessions and use of this
 * program must conform strictly to the license agreement between the user
 * and Cisco Systems, Inc., and receipt or possession does not convey
 * any rights to divulge, reproduce, or allow others to use this program
 * without specific written authorization of Cisco Systems, Inc.
 *
 * Copyright (c) 2001 by Cisco Systems, Inc.
 * All rights reserved.
 *
 *********************************************************************/
package com.cisco.eManager.eManager.processSequencer.watchdog.servers;
import com.cisco.eManager.eManager.processSequencer.watchdog.ProcessExecutor;
//import com.cisco.eManager.eManager.database.DatabaseInterface;
//import com.cisco.eManager.common.database.EmanagerDatabaseException;
import java.util.logging.Level;
import java.io.*;
import com.cisco.eManager.eManager.processSequencer.common.DCPLib;
import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;
import org.apache.log4j.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public class WDEManagerDB extends ProcessExecutor
{
    private String mShutdownCmd;

    public WDEManagerDB(String name)
    {
        super (name);
        String eManagerDir = System.getProperty("em.home");
        String log4jConfigFile = eManagerDir + "/config/log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);
    }

    public Object heartbeat () throws Exception {
//        try {
//            mLogger.finest("WDEManagerDB prepairing to get DatabaseInterface");
//            DatabaseInterface database = DatabaseInterface.instance();
//            mLogger.finest("WDEManagerDB calling database heartbeat()");
//            database.heartbeat();
//        } catch (EmanagerDatabaseException dbex) {
//            mLogger.severe("EmanagerDatabaseException thrown during heartbeat to database");
//            throw new Exception(dbex.getMessage());
//        }
        return null;
    }
}
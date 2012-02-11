package com.cisco.eManager.eManager.database;

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

import com.cisco.eManager.common.event.AbstractEventMessage;
import com.cisco.eManager.common.event.EventType;
import com.cisco.eManager.common.event.EventSeverity;
import com.cisco.eManager.common.event.EmanagerEventMessage;
import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EventSearchCriteria;
import com.cisco.eManager.common.event.EventDeletionCriteria;
import com.cisco.eManager.common.event.TibcoEventSearchCriteria;
import com.cisco.eManager.common.event.TibcoEventDeletionCriteria;
import com.cisco.eManager.common.event.DateSearchCriteria;
import com.cisco.eManager.common.event.NumericSearchCriteria;
import com.cisco.eManager.common.event.StringSearchCriteria;
import com.cisco.eManager.common.event.AcknowledgementSearchCriteria;

import com.cisco.eManager.eManager.event.TibcoEventMessage;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.common.inventory.Transport;
import com.cisco.eManager.common.inventory.AppInstanceMgmtMode;

import com.cisco.eManager.eManager.database.DatabaseInterface;
import com.cisco.eManager.eManager.inventory.host.Host;
import com.cisco.eManager.eManager.inventory.host.HostData;
import com.cisco.eManager.eManager.inventory.appType.AppType;
import com.cisco.eManager.eManager.inventory.appType.AppTypeData;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceData;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicyData;
import com.cisco.eManager.eManager.inventory.view.ContainerNodeData;


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
        /*
        AbstractEventMessage eventMessage = null;

        try {
            DatabaseInterface.instance().heartbeat();
            DatabaseInterface.instance().heartbeat();
            DatabaseInterface.instance().heartbeat();
        }
        catch (EmanagerDatabaseException e) {

        }

	eventMessage = new TibcoEventMessage (EventType.PostType,
					      new Date(),
					      EventSeverity.Low,
					      1,
					      null,
					      new ManagedObjectId (ManagedObjectId.ApplicationInstanceType, 1),
					      new ManagedObjectId (ManagedObjectId.MgmtPolicyType, 1),
					      null,
					      "Rule text",
					      "Test (a > b)",
					      "Tibco error message");

        try {
            DatabaseInterface.instance().createNewEvent(eventMessage);
        }
        catch (EmanagerDatabaseException e) {

        }

	eventMessage = new EmanagerEventMessage (EventType.PostType,
						 new ManagedObjectId (ManagedObjectId.ApplicationInstanceType, 1),
						 new Date(),
						 0,
						 EventSeverity.Low,
						 "Sample error event.");

        try {
            DatabaseInterface.instance().createNewEvent(eventMessage);
        }
        catch (EmanagerDatabaseException e) {

        }
        */


        System.exit(1);
    }

    public static void main (String args[])
    {
        TestDriver driver;

        driver = new TestDriver();

        driver.run();
    }
}

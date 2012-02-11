package com.cisco.eManager.tools;

import java.util.*;
import java.io.*;
import COM.TIBCO.hawk.console.hawkeye.*;
import org.exolab.castor.xml.*;

import org.apache.log4j.*;

import com.cisco.eManager.common.audit2.*;

public class AuditLogXmlMsgs
{
    private Logger logger = Logger.getLogger(AuditLogXmlMsgs.class);

    private String eManagerDir = null;

    public AuditLogXmlMsgs ()
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
        String outString;
        AuditMgrMsg msg;
        StringWriter strWriter;

	RetrieveAuditLogEntries retrieveEntries;

	IdSearchCriteria idSearchCriteria;
	Domains domain;
	Actions action;
	SubjectSearchCriteria subjectSearchCriteria;
	TimeSearchCriteria timeSearchCriteria;
	UserIdSearchCriteria userIdSearchCriteria;

	retrieveEntries = new RetrieveAuditLogEntries();

	idSearchCriteria = new IdSearchCriteria();
	idSearchCriteria.setStart (1);
	idSearchCriteria.setEnd (999);
	retrieveEntries.setIdSearchCriteria (idSearchCriteria);

	domain = new Domains();
	domain.setInventory (1);
	retrieveEntries.addDomains (domain);
	domain = new Domains();
	domain.setEvent (1);
	retrieveEntries.addDomains (domain);

	action = new Actions();
	action.setCreate (1);
	retrieveEntries.addActions (action);
	action = new Actions();
	action.setUpdate (1);
	retrieveEntries.addActions (action);

	subjectSearchCriteria = new SubjectSearchCriteria();
	subjectSearchCriteria.setSearchString ("APP");
	retrieveEntries.setSubjectSearchCriteria (subjectSearchCriteria);

	timeSearchCriteria = new TimeSearchCriteria();
	timeSearchCriteria.setStartDate (new Date());
	timeSearchCriteria.setEndDate (new Date());
	retrieveEntries.setTimeSearchCriteria (timeSearchCriteria);

	userIdSearchCriteria = new UserIdSearchCriteria();
	userIdSearchCriteria.setSearchString ("mjmat");
	retrieveEntries.setUserIdSearchCriteria (userIdSearchCriteria);

	msg = new AuditMgrMsg();
	msg.setRetrieveAuditLogEntries (retrieveEntries);
        strWriter = new StringWriter();
	try {
	    msg.marshal (strWriter);
	    System.out.println ("Success.");

	    outString = strWriter.toString();

	    msg = AuditMgrMsg.unmarshal(new StringReader(outString));
	    System.out.println ("Success.");
	}
	catch (Exception e) {

	}

	DeleteAuditLogEntries deleteEntries;

	deleteEntries = new DeleteAuditLogEntries();

	idSearchCriteria = new IdSearchCriteria();
	idSearchCriteria.setStart (1);
	idSearchCriteria.setEnd (999);
	deleteEntries.setIdSearchCriteria (idSearchCriteria);

	domain = new Domains();
	domain.setInventory (1);
	deleteEntries.addDomains (domain);
	domain = new Domains();
	domain.setEvent (1);
	deleteEntries.addDomains (domain);

	action = new Actions();
	action.setCreate (1);
	deleteEntries.addActions (action);
	action = new Actions();
	action.setUpdate (1);
	deleteEntries.addActions (action);

	subjectSearchCriteria = new SubjectSearchCriteria();
	subjectSearchCriteria.setSearchString ("APP");
	deleteEntries.setSubjectSearchCriteria (subjectSearchCriteria);

	timeSearchCriteria = new TimeSearchCriteria();
	timeSearchCriteria.setStartDate (new Date());
	timeSearchCriteria.setEndDate (new Date());
	deleteEntries.setTimeSearchCriteria (timeSearchCriteria);

	userIdSearchCriteria = new UserIdSearchCriteria();
	userIdSearchCriteria.setSearchString ("mjmat");
	deleteEntries.setUserIdSearchCriteria (userIdSearchCriteria);

	msg = new AuditMgrMsg();
	msg.setDeleteAuditLogEntries (deleteEntries);
        strWriter = new StringWriter();
	try {
	    msg.marshal (strWriter);
	    System.out.println ("Success.");

	    outString = strWriter.toString();

	    msg = AuditMgrMsg.unmarshal(new StringReader(outString));
	    System.out.println ("Success.");
	}
	catch (Exception e) {

	}


        System.exit(1);
    }

    public static void main (String args[])
    {
        AuditLogXmlMsgs driver;

        driver = new AuditLogXmlMsgs();

        driver.run();
    }
}

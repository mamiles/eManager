package com.cisco.eManager.tools;

import java.util.*;
import java.io.*;
import COM.TIBCO.hawk.console.hawkeye.*;
import org.exolab.castor.xml.*;

import org.apache.log4j.*;

import com.cisco.eManager.common.event2.*;

public class EventXmlMsgs
{
    private Logger logger = Logger.getLogger(EventXmlMsgs.class);

    private String eManagerDir = null;

    public EventXmlMsgs ()
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
	EventMgrMsg msg;
        String outString;
	StringWriter strWriter;
	com.cisco.eManager.common.event2.AcknowledgeEvent xackEvent;
	com.cisco.eManager.common.event2.EventId xeventId;
        com.cisco.eManager.common.event2.ObjectType idType;
        com.cisco.eManager.common.event2.ObjectType mgmtPolicyType;
	com.cisco.eManager.common.event2.Acknowledgement xack;

	xack = new com.cisco.eManager.common.event2.Acknowledgement();
	xack.setUserId ("mjmatch");
	xack.setDate (new Date());
	xack.setComment ("ack comment");

	idType = new com.cisco.eManager.common.event2.ObjectType();
	idType.setEvent (1);

	mgmtPolicyType = new com.cisco.eManager.common.event2.ObjectType();
	mgmtPolicyType.setMgmtPolicy (1);

	xeventId = new com.cisco.eManager.common.event2.EventId();
	xeventId.setObjectType (idType);
	xeventId.setObjectKey (1);

	xackEvent = new com.cisco.eManager.common.event2.AcknowledgeEvent();
	xackEvent.setEventId (xeventId);
	xackEvent.setAcknowledgement (xack);

	msg = new EventMgrMsg();
	msg.setAcknowledgeEvent (xackEvent);
        strWriter = new StringWriter();
	try {
	    msg.marshal (strWriter);
	    System.out.println ("Success.");
	}
	catch (MarshalException ex) {
	}
	catch (ValidationException ex) {

	}

	outString = strWriter.toString();

	try {
	    msg = EventMgrMsg.unmarshal(new StringReader(outString));
	    System.out.println ("Success.");
	}
	catch (Exception e) {

	}

	UnacknowledgeEvent xunackEvent;

	xeventId = new com.cisco.eManager.common.event2.EventId();
	xeventId.setObjectType (idType);
	xeventId.setObjectKey (1);

	xunackEvent = new UnacknowledgeEvent();
	xunackEvent.setEventId (xeventId);
	xunackEvent.setUserId ("mjmatch");

	msg = new EventMgrMsg();
	msg.setUnacknowledgeEvent (xunackEvent);
        strWriter = new StringWriter();
	try {
	    msg.marshal (strWriter);
	    System.out.println ("Success.");
	}
	catch (MarshalException ex) {
	}
	catch (ValidationException ex) {

	}

	outString = strWriter.toString();

	try {
	    msg = EventMgrMsg.unmarshal(new StringReader(outString));
	    System.out.println ("Success.");
	}
	catch (Exception e) {

	}

	GetEventDetails xgetEDet;

	xgetEDet = new GetEventDetails();
	xgetEDet.setObjectType (idType);
	xgetEDet.setObjectKey (1);
	msg = new EventMgrMsg();
	msg.setGetEventDetails (xgetEDet);
        strWriter = new StringWriter();
	try {
	    msg.marshal (strWriter);
	    System.out.println ("Success.");

	    outString = strWriter.toString();

	    msg = EventMgrMsg.unmarshal(new StringReader(outString));
	    System.out.println ("Success.");
	}
	catch (Exception e) {

	}

	PostDateSearchCriteria pcriteria;
	ClearDateSearchCriteria ccriteria;
	EmanagerEventIdSearchCriteria eidcriteria;
	AcknowledgementSearchCriteria ackcriteria;
	ManagedObjects mObjects;
	Severities severities;
	RetrieveEvents xrEvents;
	TibcoSearchCriteria tsc;

	tsc = new TibcoSearchCriteria();
	TibcoEventIdSearchCriteria teisc;
	teisc = new TibcoEventIdSearchCriteria();
	teisc.setStart (1);
	teisc.setEnd (5);
	tsc.setTibcoEventIdSearchCriteria (teisc);

	RuleSearchCriteria rsc;
	rsc = new RuleSearchCriteria();
	rsc.setSearchString ("rule_name");
	tsc.setRuleSearchCriteria (rsc);

	TestSearchCriteria tesc;
	tesc = new TestSearchCriteria();
	tesc.setSearchString ("test_text");
	tsc.setTestSearchCriteria (tesc);

	ManagementPolicyIds mpids;
	mpids = new ManagementPolicyIds();
	mpids.setObjectType (mgmtPolicyType);
	mpids.setObjectKey (1);
	tsc.addManagementPolicyIds (mpids);

	mpids = new ManagementPolicyIds();
	mpids.setObjectType (mgmtPolicyType);
	mpids.setObjectKey (5);
	tsc.addManagementPolicyIds (mpids);

	xrEvents = new RetrieveEvents();
        xrEvents.setTibcoSearchCriteria(tsc);
        xrEvents.setProcessSequencerSearchCriteria(null);

	pcriteria = new PostDateSearchCriteria();
	pcriteria.setStartDate (new Date());
	pcriteria.setEndDate (new Date());
	xrEvents.setPostDateSearchCriteria (pcriteria);

	ccriteria = new ClearDateSearchCriteria();
	ccriteria.setStartDate (new Date());
	ccriteria.setEndDate (new Date());
	xrEvents.setClearDateSearchCriteria (ccriteria);

	eidcriteria = new EmanagerEventIdSearchCriteria();
	eidcriteria.setStart (1);
	eidcriteria.setEnd (3);
	xrEvents.setEmanagerEventIdSearchCriteria(eidcriteria);

	ackcriteria = new AcknowledgementSearchCriteria();
	UserIdSearchCriteria uidsc;
	AckTimeSearchCriteria atsc;
	uidsc = new UserIdSearchCriteria();
	uidsc.setSearchString ("mjma");
	atsc= new AckTimeSearchCriteria();
	atsc.setStartDate (new Date());
	atsc.setEndDate (new Date());
	ackcriteria.setUserIdSearchCriteria (uidsc);
	ackcriteria.setAckTimeSearchCriteria (atsc);
	xrEvents.setAcknowledgementSearchCriteria (ackcriteria);

	mObjects = new ManagedObjects();
	mObjects.setObjectType (idType);
	mObjects.setObjectKey (1);
	xrEvents.addManagedObjects (mObjects);
	mObjects = new ManagedObjects();
	mObjects.setObjectType (idType);
	mObjects.setObjectKey (2);
	xrEvents.addManagedObjects (mObjects);

	severities = new Severities();
	severities.setLow (1);
	xrEvents.addSeverities (severities);
	severities = new Severities();
	severities.setMedium (1);
	xrEvents.addSeverities (severities);
	severities = new Severities();
	severities.setHigh (1);
	xrEvents.addSeverities (severities);

	msg = new EventMgrMsg();
	msg.setRetrieveEvents (xrEvents);
        strWriter = new StringWriter();
	try {
	    msg.marshal (strWriter);
	    System.out.println ("Success.");

	    outString = strWriter.toString();

	    msg = EventMgrMsg.unmarshal(new StringReader(outString));
	    System.out.println ("Success.");
	}
	catch (Exception e) {

	}

	DeleteEvents deEvents;

	deEvents = new DeleteEvents();

	pcriteria = new PostDateSearchCriteria();
	pcriteria.setStartDate (new Date());
	pcriteria.setEndDate (new Date());
	deEvents.setPostDateSearchCriteria (pcriteria);

	ccriteria = new ClearDateSearchCriteria();
	ccriteria.setStartDate (new Date());
	ccriteria.setEndDate (new Date());
	deEvents.setClearDateSearchCriteria (ccriteria);

	eidcriteria = new EmanagerEventIdSearchCriteria();
	eidcriteria.setStart (1);
	eidcriteria.setEnd (3);
	deEvents.setEmanagerEventIdSearchCriteria(eidcriteria);

	ackcriteria = new AcknowledgementSearchCriteria();
	uidsc = new UserIdSearchCriteria();
	uidsc.setSearchString ("mjma");
	atsc= new AckTimeSearchCriteria();
	atsc.setStartDate (new Date());
	atsc.setEndDate (new Date());
	ackcriteria.setUserIdSearchCriteria (uidsc);
	ackcriteria.setAckTimeSearchCriteria (atsc);
	deEvents.setAcknowledgementSearchCriteria (ackcriteria);

	mObjects = new ManagedObjects();
	mObjects.setObjectType (idType);
	mObjects.setObjectKey (1);
	xrEvents.addManagedObjects (mObjects);
	mObjects = new ManagedObjects();
	mObjects.setObjectType (idType);
	mObjects.setObjectKey (2);
	deEvents.addManagedObjects (mObjects);

	severities = new Severities();
	severities.setLow (1);
	deEvents.addSeverities (severities);
	severities = new Severities();
	severities.setMedium (1);
	deEvents.addSeverities (severities);
	severities = new Severities();
	severities.setHigh (1);
	deEvents.addSeverities (severities);

	msg = new EventMgrMsg();
	msg.setDeleteEvents (deEvents);
        strWriter = new StringWriter();
	try {
	    msg.marshal (strWriter);
	    System.out.println ("Success.");

	    outString = strWriter.toString();

	    msg = EventMgrMsg.unmarshal(new StringReader(outString));
	    System.out.println ("Success.");
	}
	catch (Exception e) {

	}

	RegisterSNMPClient rclient;

	rclient = new RegisterSNMPClient();
	rclient.setHost ("MJMATCH-W2K1");
	rclient.setCommunity ("public");
	rclient.setPort (162);

	msg = new EventMgrMsg();
	msg.setRegisterSNMPClient (rclient);
        strWriter = new StringWriter();
	try {
	    msg.marshal (strWriter);
	    System.out.println ("Success.");

	    outString = strWriter.toString();

	    msg = EventMgrMsg.unmarshal(new StringReader(outString));
	    System.out.println ("Success.");
	}
	catch (Exception e) {

	}

	UnregisterSNMPClient uclient;

	uclient = new UnregisterSNMPClient();
	uclient.setHost ("MJMATCH-W2K1");
	uclient.setCommunity ("public");
	uclient.setPort (162);

	msg = new EventMgrMsg();
	msg.setUnregisterSNMPClient (uclient);
        strWriter = new StringWriter();
	try {
	    msg.marshal (strWriter);
	    System.out.println ("Success.");

	    outString = strWriter.toString();

	    msg = EventMgrMsg.unmarshal(new StringReader(outString));
	    System.out.println ("Success.");
	}
	catch (Exception e) {

	}

	msg = new EventMgrMsg();
	msg.setRetrieveSNMPClients ("");
        strWriter = new StringWriter();
	try {
	    msg.marshal (strWriter);
	    System.out.println ("Success.");

	    outString = strWriter.toString();

	    msg = EventMgrMsg.unmarshal(new StringReader(outString));
	    System.out.println ("Success.");
	}
	catch (Exception e) {

	}

        System.exit(1);
    }

    public static void main (String args[])
    {
        EventXmlMsgs driver;

        driver = new EventXmlMsgs();

        driver.run();
    }
}

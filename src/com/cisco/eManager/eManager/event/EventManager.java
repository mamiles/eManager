package com.cisco.eManager.eManager.event;

import javax.swing.*;

import org.exolab.castor.xml.*;

import org.apache.log4j.*;

import com.tibco.tibrv.TibrvException;

import java.util.Date;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Observer;
import java.util.Observable;
import java.util.Properties;

import java.awt.BorderLayout;
import java.awt.Container;

import java.io.StringWriter;
import java.io.StringReader;

import com.cisco.eManager.common.event.EventType;
import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.TibcoEventDetails;
import com.cisco.eManager.common.event.ProcessSequencerEventDetails;
import com.cisco.eManager.common.event.EventSearchCriteria;
import com.cisco.eManager.common.event.EventDeletionCriteria;
import com.cisco.eManager.common.event.AcknowledgementEvent;
import com.cisco.eManager.common.event.EventAcknowledgement;
import com.cisco.eManager.common.event.UnacknowledgementEvent;
import com.cisco.eManager.common.event.NumericSearchCriteria;
import com.cisco.eManager.common.event.SyncMessage;
import com.cisco.eManager.common.event.SynchronizationPriority;
import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.common.event2.EventMgrMsg;
import com.cisco.eManager.common.event2.EventMgrResp;
import com.cisco.eManager.common.event2.GetEventDetailsResp;
import com.cisco.eManager.common.event2.RetrieveEvents;
import com.cisco.eManager.common.event2.RetrieveEventsResp;
import com.cisco.eManager.common.event2.DeleteEvents;
import com.cisco.eManager.common.event2.AcknowledgeEvent;
import com.cisco.eManager.common.event2.Acknowledgement;
import com.cisco.eManager.common.event2.UnacknowledgeEvent;
import com.cisco.eManager.common.event2.RegisterSNMPClient;
import com.cisco.eManager.common.event2.UnregisterSNMPClient;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

import com.cisco.eManager.eManager.audit.AuditManager;
import com.cisco.eManager.common.audit.AuditDomain;
import com.cisco.eManager.common.audit.AuditAction;
import com.cisco.eManager.common.audit.EmanagerAuditException;

import com.cisco.eManager.common.util.StatusResp;
import com.cisco.eManager.common.util.Rc;
import com.cisco.eManager.common.util.AccessType;

import com.cisco.eManager.common.database.EmanagerDatabaseException;

import com.cisco.eManager.eManager.inventory.InventoryManager;
import com.cisco.eManager.eManager.inventory.ImMgmtPolicyUnload;
import com.cisco.eManager.eManager.inventory.ImMgmtPolicyLoad;
import com.cisco.eManager.eManager.inventory.ImAppInstanceCreation;
import com.cisco.eManager.eManager.inventory.ImAppInstanceRestoration;
import com.cisco.eManager.eManager.inventory.ImAppInstanceConsolidation;
import com.cisco.eManager.eManager.inventory.ImAppInstanceManage;
import com.cisco.eManager.eManager.inventory.ImAppInstanceUnmanage;
import com.cisco.eManager.eManager.inventory.ImAppInstanceDeletion;
import com.cisco.eManager.eManager.inventory.ImHostActivation;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager;
import com.cisco.eManager.eManager.inventory.host.Host;
import com.cisco.eManager.eManager.inventory.host.HostManager;

import com.cisco.eManager.eManager.util.GlobalProperties;

import com.cisco.eManager.eManager.audit.AuditGlobals;

import com.cisco.eManager.eManager.database.DatabaseInterface;

import com.cisco.eManager.eManager.network.ClientTibcoMessageHandler;

import com.cisco.eManager.eManager.tibco.TibcoManager;

import com.cisco.eManager.eManager.snmp.SNMPManager;
import com.cisco.eManager.eManager.snmp.TrapClient;

import com.cisco.eManager.common.snmp.EmanagerSnmpException;
import com.cisco.eManager.common.snmp.EmanagerSnmpStatusCode;

public class EventManager implements ClientTibcoMessageHandler, Observer
{
    private static Logger logger = Logger.getLogger(EventManager.class);

    /**
     * This is the parent thread group of all thread groups related to event
     * management in the eManager layer.
     */
    private ThreadGroup eventThreadGroup = null;
    private static EventManager eventManager = null;

    private ThreadGroup appInstanceRemovalWorkerThreadGroup;

    private JFrame debuggerFrame;
    private JTabbedPane debuggerTabbedPane;
    private List mgmtPolicyDeactivationTimers;
    private long managementPolicyDeactivationTimeout;
    private List appInstanceRemovalWorkers;

    // constants
    private static String AppInstanceRemovalThreadGroupName = "App Instance Removal Workers";
    private static String TimerThreadName = "MgmtPolicyDeactivationTimer - ";

    /**
     * @roseuid 3F4E5F870164
     */
    private EventManager()
    {
        String propertyValue;
        Properties systemProperties;
        Container container;

        try {
	    ThreadGroup eventThreadGroup;

	    eventThreadGroup = EventGlobals.instance().getEventThreadGroup();
	    if (eventThreadGroup != null) {
		appInstanceRemovalWorkerThreadGroup = new ThreadGroup (eventThreadGroup, AppInstanceRemovalThreadGroupName);
	    } else {
		appInstanceRemovalWorkerThreadGroup = new ThreadGroup (AppInstanceRemovalThreadGroupName);
	    }
        }
        catch (SecurityException e) {
	    logger.error ("Unexpectedly unable to create the event related thread groups.");

            eventThreadGroup = null;
	    appInstanceRemovalWorkerThreadGroup = null;
        }

        systemProperties = GlobalProperties.instance().getProperties();

        propertyValue = systemProperties.getProperty (EventGlobals.ManagementPolicyDeactivationTimeoutKey);
        if (propertyValue == null) {
            managementPolicyDeactivationTimeout = EventGlobals.ManagementPolicyDeactivationTimeoutDefault;
            logger.log(Priority.INFO,
                       "Using Default ManagementPolicyDeactivationTimeoutKey value: " +
                       Long.toString(EventGlobals.ManagementPolicyDeactivationTimeoutDefault));
        } else {
            try
            {
                managementPolicyDeactivationTimeout = Long.parseLong(propertyValue);
                logger.log(Priority.INFO,
                           "Using  ManagementPolicyDeactivationTimeout value: " +
                           propertyValue);
            }
            catch (NumberFormatException e)
            {
                managementPolicyDeactivationTimeout = EventGlobals.ManagementPolicyDeactivationTimeoutDefault;
                logger.log(Priority.ERROR,
                           "Format exception converting ManagementPolicyDeactivationTimeout from the " +
                           "properties.  Using the default: " +
                           Long.toString(managementPolicyDeactivationTimeout));

            }
        }

        mgmtPolicyDeactivationTimers = Collections.synchronizedList(new LinkedList());
        appInstanceRemovalWorkers = Collections.synchronizedList(new LinkedList());
        InventoryManager.instance().addObserver(this);

        debuggerFrame = new JFrame("Event Debugger");
        container = debuggerFrame.getContentPane();
        debuggerTabbedPane = new JTabbedPane();
        container.add(debuggerTabbedPane, BorderLayout.CENTER);

	// Fix
	// workaround for initialization
	Collection hosts;
	Collection appInstances;
	Iterator iter1;
	Iterator iter2;
	AppInstance appInstance;
	hosts = HostManager.instance().hosts();
	iter1 = hosts.iterator();
	while (iter1.hasNext()) {
	    appInstances = AppInstanceManager.instance().appInstancesByHost ( ( (Host) iter1.next()).id());
	    iter2 = appInstances.iterator();
	    while (iter2.hasNext()) {
		appInstance = (AppInstance) iter2.next();
		EventMessageQueue.instance().addApplicationInstance (appInstance);
		EventCache.instance().addApplicationInstanceCache (appInstance);
		SyncMessageQueue.instance().queueMessage (new SyncMessage(appInstance.hostId(),
									  SynchronizationPriority.High,
									  true) );
	    }
	}
    }

    public JFrame getEventDebuggerFrame()
    {
        return debuggerFrame;
    }

    public JTabbedPane getEventDebuggerPane()
    {
        return debuggerTabbedPane;
    }

    /**
     * @param eventId
     * @param acknowledgementComment
     * @roseuid 3F46474A0280
     */
    public void acknowledgeEvent(ManagedObjectId eventId,
				 String userId,
				 Date ackDate,
				 String acknowledgementComment) throws EmanagerDatabaseException, EmanagerEventException
    {
	if (eventId != null &&
	    eventId.getManagedObjectType() == ManagedObjectIdType.Event) {
	    if (userId != null &&
		userId.trim().length() != 0) {
		if (ackDate != null) {
		    EventAcknowledgement acknowledgement;
		    AcknowledgementEvent acknowledgementEvent;

		    acknowledgement = new EventAcknowledgement (userId.trim(),
								ackDate,
								acknowledgementComment);
		    EventRepository.instance().acknowledgeEvent (eventId, acknowledgement);
		    acknowledgementEvent = new AcknowledgementEvent (eventId, acknowledgement);
		    try {
			TibcoManager.instance().broadcastMessage (acknowledgementEvent);
		    }
		    catch (TibrvException e) {
			logger.error ("Tibrv exception sending acknowledgement event: " + e);
		    }
		} else {
		    EmanagerEventException e;

		    e = new EmanagerEventException (EmanagerEventStatusCode.MalformedDate,
						    EmanagerEventStatusCode.MalformedDate.getStatusCodeDescription() +
						    ackDate);
		throw e;
		}
	    } else {
		    EmanagerEventException e;

		    e = new EmanagerEventException (EmanagerEventStatusCode.MalformedUserName,
						    EmanagerEventStatusCode.MalformedUserName.getStatusCodeDescription() +
						    userId);
		throw e;
	    }
	} else {
		    EmanagerEventException e;

		    e = new EmanagerEventException (EmanagerEventStatusCode.MalformedEventObjectId,
						    EmanagerEventStatusCode.MalformedEventObjectId.getStatusCodeDescription() +
						    eventId);
		throw e;
	}
    }

    /**
     * @param eventId
     * @roseuid 3F4647BD03C6
     */
    public void unacknowledgeEvent(ManagedObjectId eventId,
				   String userId) throws EmanagerDatabaseException, EmanagerEventException
    {
	if (eventId != null &&
	    eventId.getManagedObjectType() == ManagedObjectIdType.Event) {
	    if (userId != null &&
		userId.trim().length() != 0) {
		UnacknowledgementEvent unackEvent;

		EventRepository.instance().unacknowledgeEvent (eventId, userId);
		unackEvent = new UnacknowledgementEvent (eventId, userId.trim());
		try {
		    TibcoManager.instance().broadcastMessage (unackEvent);
		}
		catch (TibrvException e) {
		    logger.error ("Tibrv exception sending unacknowledgement event: " + e);
		}
	    } else {
		EmanagerEventException e;

		e = new EmanagerEventException (EmanagerEventStatusCode.MalformedUserName,
						EmanagerEventStatusCode.MalformedUserName.getStatusCodeDescription() +
						userId);
		throw e;
	    }
	} else {
	    EmanagerEventException e;

	    e = new EmanagerEventException (EmanagerEventStatusCode.MalformedEventObjectId,
					    EmanagerEventStatusCode.MalformedEventObjectId.getStatusCodeDescription() +
					    eventId);
	    throw e;
	}
    }

    /**
     * @param eventId
     * @return com.cisco.eManager.common.event.EmanagerEventDetails
     * @roseuid 3F46480D00F0
     */
    public EmanagerEventDetails getEventDetails(ManagedObjectId eventId) throws EmanagerDatabaseException, EmanagerEventException
    {
	if (eventId != null) {
	    if (eventId.getManagedObjectType() == ManagedObjectIdType.Event) {
		return EventRepository.instance().getEventDetails (eventId);
	    } else {
		EmanagerEventException e;

		e = new EmanagerEventException (EmanagerEventStatusCode.MalformedEventObjectId,
						EmanagerEventStatusCode.MalformedEventObjectId.getStatusCodeDescription() +
						eventId);
		throw e;
	    }
	} else {
		EmanagerEventException e;

		e = new EmanagerEventException (EmanagerEventStatusCode.MalformedEventObjectId,
						EmanagerEventStatusCode.MalformedEventObjectId.getStatusCodeDescription() +
						eventId);
		throw e;
	}
    }

    /**
     * @param eventSearchCriteria
     * @return Collection
     * @roseuid 3F4648B00271
     */
    public Collection retrieveEvents(EventSearchCriteria eventSearchCriteria) throws EmanagerDatabaseException
    {
	return DatabaseInterface.instance().retrieveEvents (eventSearchCriteria);
    }

    protected void clearEvent (ManagedObjectId eventObjectId)
    {
        Collection events;
	EmanagerEventDetails details;

	if (eventObjectId.getManagedObjectType() != ManagedObjectIdType.Event) {
	    return;
	}

	// First retrieve the event from the EventCache
	details = EventRepository.instance().clearEvent (eventObjectId);

        if (details != null) {
            events = new LinkedList();
            events.add (details);
            try {
                SNMPManager.instance().broadcastClearEvents(events);
                TibcoManager.instance().broadcastClearEvents(events);
            }
            catch (TibrvException e) {
                logger.error ("Error sending Tibco message when clearing event: " +
                              e.getMessage());
            }
        }
    }

    protected void clearApplicationInstanceOutstandingEvents (ManagedObjectId appInstanceId)
    {
        Collection clearedEvents;

        clearedEvents = EventRepository.instance().clearOutstandingApplicationInstanceEvents(appInstanceId);

        if (clearedEvents != null) {
            try {
                SNMPManager.instance().broadcastClearEvents(clearedEvents);
                TibcoManager.instance().broadcastClearEvents(clearedEvents);
            }
            catch (TibrvException e) {
                logger.error ("Error sending Tibco message when clearing event: " +
                              e.getMessage());
            }
        }
    }

    /**
     * @param criteria
     * @roseuid 3F4648F8035B
     */
    public void deleteEvents(EventDeletionCriteria criteria) throws EmanagerDatabaseException
    {
	DatabaseInterface.instance().removeEvents (criteria);
    }

    /**
     * @param host
     * @param community
     * @param port
     * @roseuid 3F4649FF01F1
     */
    public void registerSNMPClient(String host, String community, int port) throws EmanagerSnmpException
    {
        TrapClient trapClient;

        trapClient = new TrapClient (host, community, port);

        SNMPManager.instance().addTrapClient(trapClient);

        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Event,
						     AuditAction.Create,
						     trapClient.toString(),
						     new Date(),
						     "mjmatch",
						     "Added snmp client " + trapClient);
        }
	catch (EmanagerAuditException e) {
	    logger.warn ("Error writing audit log message associated with adding snmp client:" + e);
	}
	catch (EmanagerDatabaseException e) {
	    logger.warn ("Error writing audit log message associated with adding snmp client:" + e);
	}
    }

    /**
     * @param host
     * @param community
     * @param port
     * @roseuid 3F464B5A0326
     */
    public void unregisterSNMPClient(String host, String community, int port) throws EmanagerSnmpException
    {
        TrapClient trapClient;

        trapClient = new TrapClient (host, community, port);
        SNMPManager.instance().removeTrapClient (trapClient);

	try {
	    AuditManager.instance().addAuditLogEntry (AuditDomain.Event,
						      AuditAction.Delete,
						      trapClient.toString(),
						      new Date(),
						      "mjmatch",
						      "Deleted snmp client " + trapClient);
        }
	catch (EmanagerAuditException e) {
	    logger.warn ("Error writing audit log message associated with deleting snmp client:" + e);
	}
	catch (EmanagerDatabaseException e) {
	    logger.warn ("Error writing audit log message associated with deleting snmp client:" + e);
	}
    }

    public Collection retrieveSNMPClients()
    {
        return SNMPManager.instance().retrieveTrapClients ();
    }

    /**
     * @return com.cisco.eManager.eManager.event.EventManager
     * @roseuid 3F53929D009E
     */
    public synchronized static EventManager instance()
    {
        if (eventManager == null) {
	    eventManager = new EventManager();
	}

        return eventManager;
    }

    public String handleRequest(String xmlStream,
				String userName,
				AccessType accessType) {
        StringWriter response = new StringWriter();
        EventMgrMsg msg = null;
        EventMgrResp resp = new EventMgrResp();
	StatusResp statusResponse;
	Rc rc;

	String failureMessage = "";

	rc = new Rc();
	statusResponse = new StatusResp();
	statusResponse.setRc (rc);


        try
        {
            msg = EventMgrMsg.unmarshal(new StringReader(xmlStream));

            if (msg.getGetEventDetails() != null) {
                ManagedObjectId eventObjectId;
                EmanagerEventDetails details;
                GetEventDetailsResp getEventDetailsResp;
                com.cisco.eManager.common.event2.ManagedObjectIdType mObjectId;

                mObjectId = msg.getGetEventDetails();
                eventObjectId = new ManagedObjectId (mObjectId);
		try{
		    getEventDetailsResp = new GetEventDetailsResp();
		    details = this.getEventDetails(eventObjectId);
                    if (details != null) {
                        if (details.getClass().getName().equals("EmanagerEventDetails")) {
                            com.cisco.eManager.common.event2.EmanagerEventDetails eventDetails;

                            eventDetails = new com.cisco.eManager.common.event2.EmanagerEventDetails();
                            details.populateTransportObject(eventDetails);
                            getEventDetailsResp.setEmanagerEventDetails(eventDetails);
                        } else if (details instanceof TibcoEventDetails){
                            com.cisco.eManager.common.event2.TibcoEventDetails eventDetails;

                            eventDetails = new com.cisco.eManager.common.event2.TibcoEventDetails();
                            ( (TibcoEventDetails) details).populateTransportObject(eventDetails);
                            getEventDetailsResp.setTibcoEventDetails(eventDetails);
                        }
                        else if (details instanceof ProcessSequencerEventDetails){
                            com.cisco.eManager.common.event2.ProcessSequencerEventDetails eventDetails;

                            eventDetails = new com.cisco.eManager.common.event2.
                                           ProcessSequencerEventDetails();
                            ( (ProcessSequencerEventDetails)details).populateTransportObject(eventDetails);
                            getEventDetailsResp.setProcessSequencerEventDetails(eventDetails);
                        }
                        else{
			    com.cisco.eManager.common.event2.EmanagerEventDetails eventDetails;

			    eventDetails = new com.cisco.eManager.common.event2.EmanagerEventDetails();
                            details.populateTransportObject(eventDetails);
                            getEventDetailsResp.setEmanagerEventDetails(eventDetails);
			}

			rc.setSuccess(1);
			resp.setGetEventDetailsResp(getEventDetailsResp);
		    }
		    else{
			rc.setWarning(1);
			statusResponse.setDescription("Event not found.");
                        resp.setNoDataResponse ("");
		    }
		}
		catch (EmanagerDatabaseException e) {
		    rc.setFailure (1);
		    statusResponse.setDescription ("Database exception encountered: " + e.getMessage());
                    resp.setNoDataResponse ("");
		}
		catch (EmanagerEventException e) {
		    rc.setFailure (1);
		    statusResponse.setDescription ("Emanager exception encountered: " + e.getMessage());
                    resp.setNoDataResponse ("");
		}
            } else if (msg.getRetrieveEvents() != null) {
		Iterator              iter;
		Collection            eventDetailList;
                RetrieveEvents        retrieveEventsMsg;
		EventSearchCriteria   eventSearchCriteria;
		RetrieveEventsResp    detailsResponse;
		EmanagerEventDetails  details;

		try {
		    retrieveEventsMsg = msg.getRetrieveEvents();
		    eventSearchCriteria = new EventSearchCriteria (retrieveEventsMsg);
		    eventDetailList = retrieveEvents (eventSearchCriteria);
		    if (eventDetailList != null &&
			eventDetailList.size() != 0) {
			iter = eventDetailList.iterator();
			while (iter.hasNext()) {
			    detailsResponse = new RetrieveEventsResp();
			    details = (EmanagerEventDetails) iter.next();
			    if (details.getClass().getName().equals("EmanagerEventDetails")) {
				com.cisco.eManager.common.event2.EmanagerEventDetails eventDetails;

				eventDetails = new com.cisco.eManager.common.event2.EmanagerEventDetails();
				details.populateTransportObject (eventDetails);
				detailsResponse.setEmanagerEventDetails (eventDetails);
			    } else if (details instanceof TibcoEventDetails) {
				com.cisco.eManager.common.event2.TibcoEventDetails eventDetails;

				eventDetails = new com.cisco.eManager.common.event2.TibcoEventDetails();
				( (TibcoEventDetails) details).populateTransportObject (eventDetails);
				detailsResponse.setTibcoEventDetails (eventDetails);
			    } else if (details instanceof ProcessSequencerEventDetails) {
				com.cisco.eManager.common.event2.ProcessSequencerEventDetails eventDetails;

				eventDetails = new com.cisco.eManager.common.event2.ProcessSequencerEventDetails();
				( (ProcessSequencerEventDetails) details).populateTransportObject (eventDetails);
				detailsResponse.setProcessSequencerEventDetails (eventDetails);
			    } else {
				com.cisco.eManager.common.event2.EmanagerEventDetails eventDetails;

				eventDetails = new com.cisco.eManager.common.event2.EmanagerEventDetails();
				details.populateTransportObject (eventDetails);
				detailsResponse.setEmanagerEventDetails (eventDetails);
			    }

			    resp.addRetrieveEventsResp (detailsResponse);
			}
		    } else {
                        statusResponse.setDescription ("No events found.");
                        resp.setNoDataResponse ("");
                    }

		    rc.setSuccess (1);
		}
		catch (EmanagerDatabaseException e) {
		    rc.setFailure (1);
		    statusResponse.setDescription ("Database exception encountered: " + e.getMessage());
                    resp.setNoDataResponse ("");
		}
		catch (EmanagerEventException e) {
		    rc.setFailure (1);
		    statusResponse.setDescription ("Emanager exception encountered: " + e.getMessage());
                    resp.setNoDataResponse ("");
		}
            } else if (msg.getDeleteEvents() != null) {
		DeleteEvents deleteEventsMsg;
		EventDeletionCriteria eventDeletionCriteria;

                eventDeletionCriteria = null;
		try {
		    deleteEventsMsg = msg.getDeleteEvents();
		    eventDeletionCriteria = new EventDeletionCriteria (deleteEventsMsg);
		    if (accessType == AccessType.WRITE) {
			this.deleteEvents(eventDeletionCriteria);
			rc.setSuccess (1);
			resp.setNoDataResponse ("");
		    } else {
                        rc.setFailure (1);
                        statusResponse.setDescription ("Authorization failure: Insufficient permissions.");
                        resp.setNoDataResponse ("");
                        failureMessage = AuditGlobals.AuthorizationFailureIndicator;
		    }
		}
		catch (EmanagerDatabaseException e) {
		    rc.setFailure (1);
		    statusResponse.setDescription ("Database exception encountered: " + e.getMessage());
                    resp.setNoDataResponse ("");
                    failureMessage = AuditGlobals.ExecutionFailureIndicator;
                }
		catch (EmanagerEventException e) {
		    rc.setFailure (1);
		    statusResponse.setDescription ("Emanager exception encountered: " + e.getMessage());
                    resp.setNoDataResponse ("");
                    failureMessage = AuditGlobals.ExecutionFailureIndicator;
		}

		AuditManager.instance().addAuditLogEntry (AuditDomain.Event,
							  AuditAction.Delete,
							  AuditGlobals.EventSubjectKey + "=many",
							  new Date(),
							  userName,
							  failureMessage + "Criteria:" + eventDeletionCriteria.toString());
            } else if (msg.getAcknowledgeEvent() != null) {
		Acknowledgement ack;
		AcknowledgeEvent ackEventMsg;
		ManagedObjectId eventObjectId;

		eventObjectId = null;
		try {
		    ackEventMsg = msg.getAcknowledgeEvent();
		    ack = ackEventMsg.getAcknowledgement();

		    eventObjectId = new ManagedObjectId (ackEventMsg.getEventId());
		    if (ack != null) {
			if (accessType == AccessType.WRITE) {
			    acknowledgeEvent (eventObjectId,
					      ack.getUserId(),
					      ack.getDate(),
					      ack.getComment());
			    rc.setSuccess (1);
			    resp.setNoDataResponse ("");
			} else {
			    rc.setFailure (1);
			    statusResponse.setDescription ("Authorization failure: Insufficient permissions.");
			    resp.setNoDataResponse ("");
			    failureMessage = AuditGlobals.AuthorizationFailureIndicator;
			}
		    } else {
                        resp.setNoDataResponse ("");
			rc.setFailure (1);
			statusResponse.setDescription ("Malformed acknowledgement.  Missing acknowledgement data. ");
			failureMessage = AuditGlobals.ExecutionFailureIndicator;
		    }
		}
		catch (EmanagerDatabaseException e) {
                    resp.setNoDataResponse ("");
		    rc.setFailure (1);
		    statusResponse.setDescription ("Database exception encountered: " + e.getMessage());
		    failureMessage = AuditGlobals.ExecutionFailureIndicator;
		}
		catch (EmanagerEventException e) {
                    resp.setNoDataResponse ("");
		    rc.setFailure (1);
		    statusResponse.setDescription ("Emanager exception encountered: " + e.getMessage());
		    failureMessage = AuditGlobals.ExecutionFailureIndicator;
		}

                if (eventObjectId != null) {
                    AuditManager.instance().addAuditLogEntry (AuditDomain.Event,
							      AuditAction.Update,
							      AuditGlobals.EventSubjectKey + "=" + eventObjectId.getManagedObjectKey(),
							      new Date(),
							      userName,
							      failureMessage + "Acknowledgement");
		} else {
                    AuditManager.instance().addAuditLogEntry (AuditDomain.Event,
							      AuditAction.Update,
							      AuditGlobals.EventSubjectKey + "=null",
							      new Date(),
							      userName,
							      failureMessage + "Acknowledgement");
		}
            } else if (msg.getUnacknowledgeEvent() != null) {
		ManagedObjectId eventObjectId;
		UnacknowledgeEvent unAckMsg;

                eventObjectId = null;
		try {
		    unAckMsg = msg.getUnacknowledgeEvent();
		    eventObjectId = new ManagedObjectId (unAckMsg.getEventId());
		    if (accessType == AccessType.WRITE) {
			unacknowledgeEvent (eventObjectId,
					    unAckMsg.getUserId());
			rc.setSuccess (1);
			resp.setNoDataResponse ("");
		    } else {
			rc.setFailure (1);
			statusResponse.setDescription ("Authorization failure: Insufficient permissions.");
			resp.setNoDataResponse ("");
			failureMessage = AuditGlobals.AuthorizationFailureIndicator;
		    }
		}
		catch (EmanagerDatabaseException e) {
                    resp.setNoDataResponse ("");
		    rc.setFailure (1);
		    statusResponse.setDescription ("Database exception encountered: " + e.getMessage());
		    failureMessage = AuditGlobals.AuthorizationFailureIndicator;
		}
		catch (EmanagerEventException e) {
                    resp.setNoDataResponse ("");
		    rc.setFailure (1);
		    statusResponse.setDescription ("Emanager exception encountered: " + e.getMessage());
		    failureMessage = AuditGlobals.AuthorizationFailureIndicator;
		}

		if (eventObjectId != null) {
		    AuditManager.instance().addAuditLogEntry (AuditDomain.Event,
							      AuditAction.Update,
							      AuditGlobals.EventSubjectKey + "=" + eventObjectId.getManagedObjectKey(),
							      new Date(),
							      userName,
							      failureMessage + "Unacknowledgement");
		} else {
		    AuditManager.instance().addAuditLogEntry (AuditDomain.Event,
							      AuditAction.Update,
							      AuditGlobals.EventSubjectKey + "=null",
							      new Date(),
							      userName,
							      failureMessage + "Unacknowledgement");
		}
            } else if (msg.getRegisterSNMPClient() != null) {
		RegisterSNMPClient regSNMPMsg;

		regSNMPMsg = null;
		try {
		    regSNMPMsg = msg.getRegisterSNMPClient();
		    if (accessType == AccessType.WRITE) {
			registerSNMPClient (regSNMPMsg.getHost(),
					    regSNMPMsg.getCommunity(),
					    regSNMPMsg.getPort());
			rc.setSuccess (1);
			resp.setNoDataResponse ("");
		    } else {
			rc.setFailure (1);
			statusResponse.setDescription ("Authorization failure: Insufficient permissions.");
			resp.setNoDataResponse ("");
			failureMessage = AuditGlobals.AuthorizationFailureIndicator;
		    }
		}
		catch (EmanagerSnmpException e) {
                    resp.setNoDataResponse ("");
		    rc.setFailure (1);
		    statusResponse.setDescription ("SNMP exception encountered: " + e.getMessage());
		    failureMessage = AuditGlobals.ExecutionFailureIndicator;
		}

		if (regSNMPMsg != null) {
		    AuditManager.instance().addAuditLogEntry (AuditDomain.Event,
							      AuditAction.Create,
							      AuditGlobals.SNMPClientSubjectKey + "=" + regSNMPMsg.getHost() + ";" + regSNMPMsg.getCommunity() + ";" + regSNMPMsg.getPort(),
							      new Date(),
							      userName,
							      failureMessage);
		} else {
		    AuditManager.instance().addAuditLogEntry (AuditDomain.Event,
							      AuditAction.Create,
							      AuditGlobals.SNMPClientSubjectKey + "=null",
							      new Date(),
							      userName,
							      failureMessage);
		}
            } else if (msg.getUnregisterSNMPClient() != null) {
		UnregisterSNMPClient regSNMPMsg;

		regSNMPMsg = null;
		try {
		    regSNMPMsg = msg.getUnregisterSNMPClient();
		    if (accessType == AccessType.WRITE) {
			unregisterSNMPClient (regSNMPMsg.getHost(),
					      regSNMPMsg.getCommunity(),
					      regSNMPMsg.getPort());
                    rc.setSuccess (1);
                    resp.setNoDataResponse ("");
		    } else {
			rc.setFailure (1);
			statusResponse.setDescription ("Authorization failure: Insufficient permissions.");
			resp.setNoDataResponse ("");
			failureMessage = AuditGlobals.AuthorizationFailureIndicator;
		    }
		}
		catch (EmanagerSnmpException e) {
                    resp.setNoDataResponse ("");
		    rc.setFailure (1);
		    statusResponse.setDescription ("SNMP exception encountered: " + e.getMessage());
		    failureMessage = AuditGlobals.ExecutionFailureIndicator;
		}

		if (regSNMPMsg != null) {
		    AuditManager.instance().addAuditLogEntry (AuditDomain.Event,
							      AuditAction.Delete,
							      AuditGlobals.SNMPClientSubjectKey + "=" + regSNMPMsg.getHost() + ";" + regSNMPMsg.getCommunity() + ";" + regSNMPMsg.getPort(),
							      new Date(),
							      userName,
							      failureMessage);
		} else {
		    AuditManager.instance().addAuditLogEntry (AuditDomain.Event,
							      AuditAction.Delete,
							      AuditGlobals.SNMPClientSubjectKey + "=null",
							      new Date(),
							      userName,
							      failureMessage);
		}
            } else if (msg.getRetrieveSNMPClients() != null) {
		Collection clients;

		// fix here!!!
		clients = retrieveSNMPClients();
		if (clients != null &&
		    clients.size() != 0) {
		    Iterator iter;
		    TrapClient trapClient;
		    com.cisco.eManager.common.event2.SnmpClients transportClient;

		    iter = clients.iterator();
		    while (iter.hasNext()) {
			trapClient = (TrapClient) iter.next();
			transportClient = new com.cisco.eManager.common.event2.SnmpClients();
			trapClient.populateTransportObject (transportClient);
			resp.addSnmpClients (transportClient);
		    }
		} else {
		    statusResponse.setDescription ("No audit log entries found.");
		    resp.setNoDataResponse ("");
		}

		rc.setSuccess (1);
            } else {
                resp.setNoDataResponse ("");
		rc.setWarning (1);
		statusResponse.setDescription ("No request encountered.");
            }

	    statusResponse.marshal (response);
	    resp.marshal (response);
        }
        catch (ValidationException ex)
        {
            logger.error("XML Validation Exception: " + ex.getMessage());
            rc = new Rc();
            resp = new EventMgrResp();
            response = new StringWriter();
            statusResponse = new StatusResp();
            rc.setFailure (1);
            resp.setNoDataResponse ("");
            statusResponse.setRc(rc);
            statusResponse.setDescription ("Validation exception encountered w/xml message: " +
                                           ex.getMessage());
            try {
                statusResponse.marshal (response);
                resp.marshal(response);
            }
            catch (ValidationException ex1) {
            }
            catch (MarshalException ex1) {
            }
        }
        catch (MarshalException ex)
        {
            logger.error("XML Marshal Exception: " + ex.getMessage());
            rc = new Rc();
            resp = new EventMgrResp();
            response = new StringWriter();
            statusResponse = new StatusResp();
            rc.setFailure (1);
            resp.setNoDataResponse ("");
            statusResponse.setRc(rc);
            statusResponse.setDescription ("Marshal exception encountered w/xml message: " +
                                           ex.getMessage());
            try {
                statusResponse.marshal (response);
                resp.marshal(response);
            }
            catch (ValidationException ex1) {
            }
            catch (MarshalException ex1) {
            }
        }
        catch (Exception ex) {
            logger.error("Exception: " + ex.getMessage());
            rc = new Rc();
            resp = new EventMgrResp();
            response = new StringWriter();
            statusResponse = new StatusResp();
            rc.setFailure (1);
            resp.setNoDataResponse ("");
            statusResponse.setRc(rc);
            statusResponse.setDescription ("Exception encountered: " + ex.getMessage());
            try {
                statusResponse.marshal (response);
                resp.marshal(response);
            }
            catch (ValidationException ex1) {
            }
            catch (MarshalException ex1) {
            }
        }

        return response.toString();
    }

    /**
     * @param managementPolicyId
     * @roseuid 3F2D6DCD02AD
     */
    public void managementPolicyUnexpectedlyExpired(MgmtPolicy mgmtPolicy)
    {
        logger.debug ("Enter");

        synchronized (mgmtPolicyDeactivationTimers)
        {
            MgmtPolicyDeactivationTimer timer;

            timer = new MgmtPolicyDeactivationTimer(mgmtPolicy);
            mgmtPolicyDeactivationTimers.add(timer);
            timer.start();
        }
    }

    /**
     * @param managementPolicyId
     * @roseuid 3F2D6E4400C3
     */
    public void managementPolicyActivated(MgmtPolicy mgmtPolicy)
    {
        Iterator iter;
        MgmtPolicyDeactivationTimer timer;

        logger.debug ("Enter");

        synchronized (mgmtPolicyDeactivationTimers) {
            iter = mgmtPolicyDeactivationTimers.iterator();
            while (iter.hasNext()) {
                timer = (MgmtPolicyDeactivationTimer) iter.next();
                if (timer.getMgmtPolicy().equals(mgmtPolicy)) {
                    timer.setActivationMessageReceived(true);
                }
            }
        }
    }

    protected void removeMgmtPolicyDeactivationTimer (MgmtPolicyDeactivationTimer timer)
    {
        synchronized (mgmtPolicyDeactivationTimers) {
            mgmtPolicyDeactivationTimers.remove (timer);
        }
    }

    public void manageApplicationInstance (AppInstance appInstance)
    {
	Iterator iter;
	ManagedObjectId appInstanceId;
	AppInstanceRemovalWorker worker;

        logger.debug ("Enter");

	appInstanceId = appInstance.id();
        synchronized (appInstanceRemovalWorkers) {
            iter = appInstanceRemovalWorkers.iterator();
            while (iter.hasNext()) {
                worker = (AppInstanceRemovalWorker) iter.next();
		worker.stopAppInstanceWorker (appInstance.id());
            }
	}

        // make sure the event message queue is there
        EventMessageQueue.instance().addApplicationInstance(appInstance);
        EventCache.instance().addApplicationInstanceCache(appInstance);

	SyncMessageQueue.instance().queueMessage (new SyncMessage(appInstance.hostId(),
								  SynchronizationPriority.Medium,
								  true) );
    }

    public void createApplicationInstance (AppInstance appInstance)
    {
	EventMessageQueue.instance().addApplicationInstance(appInstance);
	EventCache.instance().addApplicationInstanceCache (appInstance);
	SyncMessageQueue.instance().queueMessage (new SyncMessage(appInstance.hostId(),
								  SynchronizationPriority.High,
								  true) );
    }

    /**
     * @param applicationInstanceId
     * @roseuid 3F2D821000AC
     */
    public void removeApplicationInstance(boolean appInstanceDeletion,
					  AppInstance appInstance)
    {
	Thread thread;
        Iterator iter;
	ManagedObjectId applicationInstanceId;
        AppInstanceRemovalWorker worker;

        logger.debug ("Enter");

	applicationInstanceId = appInstance.id();
        synchronized (appInstanceRemovalWorkers) {
            iter = appInstanceRemovalWorkers.iterator();
            while (iter.hasNext()) {
                worker = (AppInstanceRemovalWorker) iter.next();
                if (worker.getApplicationInstanceId().equals(applicationInstanceId)) {
                    return;
                }
            }

	    worker = new AppInstanceRemovalWorker (appInstanceDeletion, appInstance);
	    if (appInstanceRemovalWorkerThreadGroup != null) {
		thread = new Thread (appInstanceRemovalWorkerThreadGroup, worker);
	    } else {
		thread = new Thread (worker);
	    }

	    appInstanceRemovalWorkers.add (worker);
	    thread.start();
        }
    }

    protected void removeAppInstanceRemovalWorker (AppInstanceRemovalWorker worker)
    {
        synchronized (appInstanceRemovalWorkers) {
            appInstanceRemovalWorkers.remove(worker);
        }
    }

    /**
     * @param arg0
     * @param arg1
     */
    public void update(Observable arg0, Object arg1)
    {
        if (arg1 instanceof ImMgmtPolicyUnload) {
            ImMgmtPolicyUnload message;

            message = (ImMgmtPolicyUnload) arg1;
            managementPolicyUnexpectedlyExpired (message.mgmtPolicy());
        } else if (arg1 instanceof ImMgmtPolicyLoad) {
            ImMgmtPolicyLoad message;

            message = (ImMgmtPolicyLoad) arg1;
            managementPolicyActivated (message.mgmtPolicy());
        } else if (arg1 instanceof ImAppInstanceCreation ||
		   arg1 instanceof ImAppInstanceRestoration) {
	    AppInstance appInstance;

	    if (arg1 instanceof ImAppInstanceCreation) {
		ImAppInstanceCreation message = (ImAppInstanceCreation)arg1;
		appInstance = message.appInstance();
	    } else {
		ImAppInstanceRestoration message = (ImAppInstanceRestoration)arg1;
		appInstance = message.appInstance();
	    }

	    createApplicationInstance (appInstance);
	}else if (arg1 instanceof ImHostActivation) {
            ImHostActivation message = (ImHostActivation) arg1;
            Host host = message.host();
            SyncMessageQueue.instance().queueMessage (new SyncMessage(host.id(),
								      SynchronizationPriority.High,
								      true) );
        } else if (arg1 instanceof ImAppInstanceManage) {
            ImAppInstanceManage message = (ImAppInstanceManage) arg1;
            AppInstance appInstance = message.appInstance();
	    manageApplicationInstance (appInstance);
        } else if (arg1 instanceof ImAppInstanceUnmanage) {
            ImAppInstanceUnmanage message = (ImAppInstanceUnmanage) arg1;
            AppInstance appInstance = message.appInstance();
	    removeApplicationInstance (false, appInstance);
	} else if (arg1 instanceof ImAppInstanceDeletion) {
            ImAppInstanceDeletion message = (ImAppInstanceDeletion) arg1;
            AppInstance appInstance = message.appInstance();
            removeApplicationInstance (true, appInstance);
        } else if (arg1 instanceof ImAppInstanceConsolidation) {
	    Iterator iter;
	    Collection appInstances;
	    ImAppInstanceConsolidation message;

	    message= (ImAppInstanceConsolidation) arg1;
	    appInstances = message.consolidatedAppInstances();
	    iter = appInstances.iterator();
	    while (iter.hasNext()) {
		removeApplicationInstance (true, (AppInstance) iter.next());
	    }

	    appInstances = message.resultingAppInstance();
	    iter = appInstances.iterator();
	    while (iter.hasNext()) {
		createApplicationInstance ((AppInstance) iter.next());
	    }
	}
    }

    private class MgmtPolicyDeactivationTimer extends Thread
    {
        private boolean activationMessageReceived;
        private MgmtPolicy mgmtPolicy;

        public MgmtPolicyDeactivationTimer (MgmtPolicy mgmtPolicy)
        {
            super (TimerThreadName + mgmtPolicy.name());
            this.mgmtPolicy = mgmtPolicy;
            activationMessageReceived = false;
        }

        public MgmtPolicy getMgmtPolicy()
        {
            return mgmtPolicy;
        }

        public void setActivationMessageReceived (boolean activationMessageReceived)
        {
            this.activationMessageReceived = activationMessageReceived;
        }

        public boolean getActivationMessageReceived ()
        {
            return activationMessageReceived;
        }

        public void run()
        {
            logger.debug ("Enter");

            try {

                try {
                    Thread.sleep(managementPolicyDeactivationTimeout);
                }
                catch (InterruptedException e) {
                }

                synchronized (mgmtPolicyDeactivationTimers) {
                    mgmtPolicyDeactivationTimers.remove(this);
                }

                if (getActivationMessageReceived() == false) {
                    logger.log(Priority.INFO,
                               "ManagementPolicy " +
                               mgmtPolicy.name() +
                               " unexpectedly unloaded.  Synchronization requested.");

                    SyncMessageQueue.instance().queueMessage(new SyncMessage (mgmtPolicy.hostId(),
                        SynchronizationPriority.High,
                        true));
                }

            }
            catch (Exception e) {

            }

            EventManager.instance().removeMgmtPolicyDeactivationTimer(this);
            logger.debug ("Exit");
        }
    }
}

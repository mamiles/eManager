package com.cisco.eManager.common.event;

import com.tibco.tibrv.*;

import java.io.StringWriter;

import java.util.Date;

import com.cisco.eManager.common.event.AbstractTibcoEventSearchCriteria;
import com.cisco.eManager.common.event.AbstractTibcoEventSearchCriteria.*;
import com.cisco.eManager.common.event.EventSeverity;
import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.common.event2.TibcoEventDetailsType;
import com.cisco.eManager.common.event2.TibcoEventNotificationType;
import com.cisco.eManager.common.event2.NotificationType;

import com.cisco.eManager.common.inventory.ManagedObjectId;

import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.MarshalException;

public class TibcoEventDetails extends EmanagerEventDetails
{
    private long tibcoEventId;
    private String ruleText;
    private String ruleTestText;
    private String instrumentationName;
    private ManagedObjectId managementPolicyId;

    // fix
    // Workaround for SOAP
    public TibcoEventDetails()
    {

    }

    /**
     * @param emanagerEventId
     * @param postTime
     * @param clearTime
     * @param severity
     * @param managedObjectId
     * @param acknowledgement
     * @param tibcoEventId
     * @param ruleText
     * @param ruleTest
     * @param microagentName
     * @param managementPolicyId
     * @param displayText
     * @roseuid 3F4E5D560313
     */
    public TibcoEventDetails(long emanagerEventId,
			     Date postTime,
			     Date clearTime,
			     EventSeverity severity,
			     ManagedObjectId managedObjectId,
			     EventAcknowledgement acknowledgement,
			     long tibcoEventId,
			     String ruleText,
			     String ruleTest,
			     String instrumentationName,
			     ManagedObjectId managementPolicyId,
			     String displayText)
    {
        super (emanagerEventId, postTime, clearTime, severity, managedObjectId, acknowledgement,
               displayText);

         this.tibcoEventId = tibcoEventId;
         this.ruleText = ruleText;
         this.ruleTestText = ruleTest;
         this.instrumentationName = instrumentationName;
         this.managementPolicyId = managementPolicyId;

    }

    public TibcoEventDetails (long emanagerEventId,
				Date postTime,
				Date clearTime,
				EventSeverity severity,
				ManagedObjectId managedObjectId,
				EventAcknowledgement acknowledgement,
				String displayText,
                                String databaseEventValueString)
      {
          super (emanagerEventId, postTime, clearTime, severity, managedObjectId, acknowledgement,
                 displayText);

          TibcoEventSpecificData eventData;

          eventData = AbstractTibcoEventSearchCriteria.parseDatabaseValues(databaseEventValueString);
          tibcoEventId = eventData.eventId;
          ruleText = eventData.ruleText;
          ruleTestText = eventData.testText;
          managementPolicyId = eventData.managementPolicyId;
          instrumentationName = eventData.instrumentationName;
      }

    /**
     * Access method for the managementPolicyId property.
     *
     * @return   the current value of the managementPolicyId property
     */
    public ManagedObjectId getManagementPolicyId()
    {
        return managementPolicyId;
    }

    public long getTibcoEventId()
    {
        return tibcoEventId;
    }

    public String getRuleText()
    {
        return ruleText;
    }

    public String getRuleTestText()
    {
        return ruleTestText;
    }

    public String getInstrumentationName()
    {
        return instrumentationName;
    }

    public String toString()
    {
        return super.toString() + ";" +
            "tibcoEventId=" + tibcoEventId + ";" +
            "ruleText=" + ruleText + ";" +
            "ruleTestText=" + ruleTestText + ";" +
            "instrumentationName=" + instrumentationName + ";" +
            "managementPolicyId=" + managementPolicyId;
    }

    public TibcoEventDetailsType populateTransportObject(TibcoEventDetailsType object)
    {
	com.cisco.eManager.common.event2.MgmtPolicyId mgmtPolicy;

	mgmtPolicy = new com.cisco.eManager.common.event2.MgmtPolicyId();

	super.populateTransportObject (object);

	object.setTibcoEventId (getTibcoEventId());
	object.setRule (getRuleText());
	object.setTest (getRuleTestText());
	object.setInstrumentationName (getInstrumentationName());
	if (getManagementPolicyId() != null) {
	    getManagementPolicyId().populateEventTransportObject (mgmtPolicy);
	}
	object.setMgmtPolicyId (mgmtPolicy);

	return object;
    }

    protected TibcoEventNotificationType populateNotificationObject (NotificationType type,
								     TibcoEventNotificationType event)
    {
	super.populateNotificationObject (type, event);

	event.setTibcoEventId (getTibcoEventId());
	event.setRule (getRuleText());
	event.setTest (getRuleTestText());
	if (getManagementPolicyId() != null) {
	    com.cisco.eManager.common.event2.MgmtPolicyId transportMgmtPolicyId;

	    transportMgmtPolicyId = new com.cisco.eManager.common.event2.MgmtPolicyId();
	    getManagementPolicyId().populateEventTransportObject (transportMgmtPolicyId);
	    event.setMgmtPolicyId(transportMgmtPolicyId);
	}
        event.setInstrumentationName(getInstrumentationName());

        return event;
    }

    public TibrvMsg getTibcoBroadcastEventMessage(NotificationType type) throws EmanagerEventException
    {
	TibrvMsg tibcoMsg;
        String objectPath;
	StringWriter stringWriter;
	com.cisco.eManager.common.event2.EventNotification eventNotification;
	com.cisco.eManager.common.event2.EventNotificationMsg eventNotificationMsg;
	com.cisco.eManager.common.event2.TibcoEventNotification tibcoEventNotification;

	stringWriter = new StringWriter();

	tibcoEventNotification = new com.cisco.eManager.common.event2.TibcoEventNotification();
	eventNotification = new com.cisco.eManager.common.event2.EventNotification();
	eventNotificationMsg = new com.cisco.eManager.common.event2.EventNotificationMsg();

	eventNotificationMsg.setEventAcknowledgementNotification(null);
	eventNotificationMsg.setEventUnacknowledgementNotification(null);
	eventNotificationMsg.setEventNotification (eventNotification);

	eventNotification.setEmanagerEventNotification (null);
	eventNotification.setTibcoEventNotification (tibcoEventNotification);
	eventNotification.setProcessSequencerEventNotification (null);

	populateNotificationObject (type, tibcoEventNotification);

	try {
	    eventNotificationMsg.marshal (stringWriter);

	    tibcoMsg = new TibrvMsg();
	    tibcoMsg.setSendSubject(getTibcoEventMessageSubject());
	    tibcoMsg.add(getTibcoEventFieldName(), stringWriter.toString());
	}
	catch (TibrvException e) {
	    EmanagerEventException exception;

	    exception =
		new EmanagerEventException (EmanagerEventStatusCode.TibrvMessageCreationError,
					    EmanagerEventStatusCode.TibrvMessageCreationError.getStatusCodeDescription() +
					    "  " +
					    e.getMessage());
	    throw exception;
	}
	catch (ValidationException e) {
	    EmanagerEventException exception;

	    exception =
		new EmanagerEventException (EmanagerEventStatusCode.TibrvMessageCreationError,
					    EmanagerEventStatusCode.TibrvMessageCreationError.getStatusCodeDescription() +
					    "  " +
					    e.getMessage());
	    throw exception;
	}
	catch (MarshalException e) {
	    EmanagerEventException exception;

	    exception =
		new EmanagerEventException (EmanagerEventStatusCode.TibrvMessageCreationError,
					    EmanagerEventStatusCode.TibrvMessageCreationError.getStatusCodeDescription() +
					    "  " +
					    e.getMessage());
	    throw exception;
	}

	return tibcoMsg;
    }
}

package com.cisco.eManager.common.event;

import com.tibco.tibrv.*;

import java.io.StringWriter;

import java.util.Date;

import com.cisco.eManager.common.event.EventSeverity;
import com.cisco.eManager.common.event.EventAcknowledgement;
import com.cisco.eManager.common.event.ProcessSequencerEventId;
import com.cisco.eManager.common.inventory.ManagedObjectId;

import com.cisco.eManager.common.event2.ProcessSequencerEventDetailsType;
import com.cisco.eManager.common.event2.ProcessSequencerNotificationType;
import com.cisco.eManager.common.event2.NotificationType;

import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.MarshalException;

public class ProcessSequencerEventDetails extends EmanagerEventDetails
{
    private ProcessSequencerEventId processSequencerEventId;

    // fix
    // Workaround for SOAP
    public ProcessSequencerEventDetails()
    {
        super ();
    }

    /**
     * @param emanagerEventId
     * @param postTime
     * @param clearTime
     * @param severity
     * @param managedObjectId
     * @param acknowledgement
     * @param displayText
     * @param processSequencerEventId
     * @roseuid 3F550FA000BF
     */
    public ProcessSequencerEventDetails(long emanagerEventId,
					Date postTime,
					Date clearTime,
					EventSeverity severity,
					ManagedObjectId managedObjectId,
					EventAcknowledgement acknowledgement,
					String displayText,
					ProcessSequencerEventId processSequencerEventId)
    {
	super (emanagerEventId, postTime, clearTime, severity, managedObjectId, acknowledgement, displayText);

	this.processSequencerEventId = processSequencerEventId;
    }

    public ProcessSequencerEventDetails (long emanagerEventId,
				Date postTime,
				Date clearTime,
				EventSeverity severity,
				ManagedObjectId managedObjectId,
				EventAcknowledgement acknowledgement,
				String displayText,
                                String databaseEventValueString)
      {
          super (emanagerEventId, postTime, clearTime, severity, managedObjectId, acknowledgement, displayText);

          // fix
          // need to update for process sequencer messages
      }

    /**
     * Access method for the processSequencerEventId property.
     *
     * @return   the current value of the processSequencerEventId property
     */
    public ProcessSequencerEventId getProcessSequencerEventId()
    {
        return processSequencerEventId;
    }

    /**
     * Sets the value of the processSequencerEventId property.
     *
     * @param aProcessSequencerEventId the new value of the processSequencerEventId property
     */
    public void setProcessSequencerEventId(ProcessSequencerEventId aProcessSequencerEventId)
    {
        processSequencerEventId = aProcessSequencerEventId;
    }

    public ProcessSequencerEventDetailsType populateTransportObject(ProcessSequencerEventDetailsType object)
    {
	com.cisco.eManager.common.event2.ProcessSequencerId psId;

	psId = new com.cisco.eManager.common.event2.ProcessSequencerId();

	super.populateTransportObject (object);

	getProcessSequencerEventId().populateTransportObject (psId);
	object.setProcessSequencerId (psId);

	return object;
    }

    protected ProcessSequencerNotificationType populateNotificationObject (NotificationType type,
									   ProcessSequencerNotificationType event)
    {
	com.cisco.eManager.common.event2.ProcessSequencerId psId;

	psId = new com.cisco.eManager.common.event2.ProcessSequencerId();
	getProcessSequencerEventId().populateTransportObject (psId);

	super.populateNotificationObject (type, event);

	event.setProcessSequencerId (psId);

        return event;
    }

    public TibrvMsg getTibcoBroadcastEventMessage(NotificationType type) throws EmanagerEventException
    {
	TibrvMsg tibcoMsg;
        String objectPath;
	StringWriter stringWriter;
	com.cisco.eManager.common.event2.EventNotification eventNotification;
	com.cisco.eManager.common.event2.EventNotificationMsg eventNotificationMsg;
	com.cisco.eManager.common.event2.ProcessSequencerEventNotification psEventNotification;

	stringWriter = new StringWriter();

	psEventNotification = new com.cisco.eManager.common.event2.ProcessSequencerEventNotification();
	eventNotification = new com.cisco.eManager.common.event2.EventNotification();
	eventNotificationMsg = new com.cisco.eManager.common.event2.EventNotificationMsg();

	eventNotificationMsg.setEventAcknowledgementNotification(null);
	eventNotificationMsg.setEventUnacknowledgementNotification(null);
	eventNotificationMsg.setEventNotification (eventNotification);

	eventNotification.setEmanagerEventNotification (null);
	eventNotification.setTibcoEventNotification (null);
	eventNotification.setProcessSequencerEventNotification (psEventNotification);

	populateNotificationObject (type, psEventNotification);

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

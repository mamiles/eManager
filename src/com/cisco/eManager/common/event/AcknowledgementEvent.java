package com.cisco.eManager.common.event;

import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.MarshalException;

import java.io.StringWriter;

import com.cisco.eManager.common.event.XMLDeliverableMessage;
import com.cisco.eManager.common.event.EventAcknowledgement;
import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

public class AcknowledgementEvent extends AbstractEventEvent implements XMLDeliverableMessage
{
    private EventAcknowledgement acknowledgement;

    // Workaround for SOAP
    // fix
    public AcknowledgementEvent ()
    {
        super (new ManagedObjectId (ManagedObjectIdType.Event, 0));
    }

    /**
     * @param eventId
     * @param acknowledgement
     * @roseuid 3F4E5D0E0014
     */
    public AcknowledgementEvent(ManagedObjectId eventId, EventAcknowledgement acknowledgement)
    {
	super (eventId);

	this.acknowledgement = acknowledgement;
    }

    /**
     * Access method for the acknowledgement property.
     *
     * @return   the current value of the acknowledgement property
     */
    public EventAcknowledgement getAcknowledgement()
    {
        return acknowledgement;
    }

    /**
     * Sets the value of the acknowledgement property.
     *
     * @param aAcknowledgement the new value of the acknowledgement property
     */
    public void setAcknowledgement(EventAcknowledgement aAcknowledgement)
    {
        acknowledgement = aAcknowledgement;
    }

    public String getXMLMessage() throws EmanagerEventException
    {
	String xmlString;
	StringWriter stringWriter;

	com.cisco.eManager.common.event2.Acknowledgement tAck;
	com.cisco.eManager.common.event2.EventId ackEventId;
	com.cisco.eManager.common.event2.EventAcknowledgementNotification eventAckNot;
	com.cisco.eManager.common.event2.EventNotificationMsg eventNotificationMsg;

	stringWriter = new StringWriter();

	ackEventId = new com.cisco.eManager.common.event2.EventId();
	tAck = new com.cisco.eManager.common.event2.Acknowledgement();
	eventAckNot = new com.cisco.eManager.common.event2.EventAcknowledgementNotification();
	eventNotificationMsg = new com.cisco.eManager.common.event2.EventNotificationMsg();

	eventNotificationMsg.setEventAcknowledgementNotification(eventAckNot);
	eventAckNot.setEventId (ackEventId);
	eventAckNot.setAcknowledgement (tAck);

	acknowledgement.populateTransportObject (tAck);
	emanagerEventId.populateEventTransportObject (ackEventId);

	try {
	    eventNotificationMsg.marshal (stringWriter);

	    xmlString = stringWriter.toString();
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

	return xmlString;
    }
}

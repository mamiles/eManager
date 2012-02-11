package com.cisco.eManager.common.event;

import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.MarshalException;

import java.io.StringWriter;

import com.cisco.eManager.common.event.XMLDeliverableMessage;

import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

public class UnacknowledgementEvent extends AbstractEventEvent implements XMLDeliverableMessage
{
    private String userId;

    // fix
    // Workaround for SOAP
    public UnacknowledgementEvent()
    {
        super (new ManagedObjectId (ManagedObjectIdType.Event, 0));
    }

    /**
     * @param emanagerEventId
     * @param userId
     * @roseuid 3F4EDD3B0111
     */
    public UnacknowledgementEvent(ManagedObjectId emanagerEventId, String userId)
    {
        super (emanagerEventId);

        this.userId = userId;
    }

    /**
     * Access method for the userId property.
     *
     * @return   the current value of the userId property
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     *
     * @param aUserId the new value of the userId property
     */
    public void setUserId(String aUserId)
    {
        userId = aUserId;
    }

    public String getXMLMessage() throws EmanagerEventException
    {
	String xmlString;
	StringWriter stringWriter;

	com.cisco.eManager.common.event2.EventId unackEventId;
	com.cisco.eManager.common.event2.EventUnacknowledgementNotification eventUnackNot;
	com.cisco.eManager.common.event2.EventNotificationMsg eventNotificationMsg;

	stringWriter = new StringWriter();

	unackEventId = new com.cisco.eManager.common.event2.EventId();
	eventUnackNot = new com.cisco.eManager.common.event2.EventUnacknowledgementNotification();
	eventNotificationMsg = new com.cisco.eManager.common.event2.EventNotificationMsg();

	eventNotificationMsg.setEventUnacknowledgementNotification(eventUnackNot);
	eventUnackNot.setEventId (unackEventId);

	eventUnackNot.setUserId (userId);
	emanagerEventId.populateEventTransportObject (unackEventId);

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

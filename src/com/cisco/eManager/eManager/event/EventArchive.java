package com.cisco.eManager.eManager.event;

import org.apache.log4j.*;

import java.util.Date;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Collection;

import java.lang.Integer;

import com.cisco.eManager.common.database.EmanagerDatabaseException;
import com.cisco.eManager.common.database.EmanagerDatabaseStatusCode;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

import com.cisco.eManager.common.event.EventSearchCriteria;
import com.cisco.eManager.common.event.NumericSearchCriteria;
import com.cisco.eManager.common.event.EventDeletionCriteria;
import com.cisco.eManager.common.event.EventAcknowledgement;
import com.cisco.eManager.common.event.AbstractEventMessage;
import com.cisco.eManager.common.event.EmanagerEventMessage;
import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.TibcoEventDetails;
import com.cisco.eManager.common.event.ProcessSequencerEventMessage;
import com.cisco.eManager.common.event.ProcessSequencerEventDetails;
import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.eManager.event.TibcoEventMessage;

import com.cisco.eManager.eManager.database.DatabaseInterface;

public class EventArchive
{
    private static Logger      logger = Logger.getLogger(EventArchive.class);

    static private EventArchive eventArchive = null;
    // debug
    /*
    private Integer sequenceNumber = new Integer (1);
    private synchronized Integer getNextSequenceNumber()
    {
	Integer currentInteger;

	currentInteger = sequenceNumber;
	sequenceNumber = new Integer (sequenceNumber.intValue() + 1);
	return currentInteger;
    }
    */

    /**
     * @roseuid 3F575D2101AF
     */
    private EventArchive()
    {

    }

    static public EventArchive instance()
    {
        if (eventArchive == null) {
            eventArchive = new EventArchive();
        }

        return eventArchive;
    }

    /*
    public List getOutstandingHostEvents (ManagedObjectId hostObjectId)
    {
        List events;

        events = EventCache.instance().getOutstandingHostEvents (hostObjectId);
        return events;
    }
*/

    /**
     * @param eventDeletionSpecification
     * @roseuid 3F2E6A3D0356
     */
    public void removeArchiveEvents(EventDeletionCriteria criteria) throws EmanagerDatabaseException
    {
        DatabaseInterface.instance().removeEvents(criteria);
    }

    public Collection retrieveEvents (EventSearchCriteria criteria) throws EmanagerDatabaseException
    {
        return DatabaseInterface.instance().retrieveEvents(criteria);
    }

    /**
     * @param eventKey
     * @return com.cisco.eManager.common.event.AbstractEventMessage
     * @roseuid 3F2E6ECD03AF
     */
    public EmanagerEventDetails getEventDetails(ManagedObjectId eventId) throws EmanagerDatabaseException, EmanagerEventException
    {
	Iterator iter;
	Collection events;
	EventSearchCriteria criteria;

	if (eventId.getManagedObjectType() != ManagedObjectIdType.Event) {
	    EmanagerEventException e;

	    e = new EmanagerEventException (EmanagerEventStatusCode.MalformedEventObjectId,
					    EmanagerEventStatusCode.MalformedEventObjectId.getStatusCodeDescription() +
					    eventId.toString());
	    throw e;
	}

	criteria = new EventSearchCriteria();
	criteria.setEmanagerEventId (new NumericSearchCriteria (new Long (eventId.getManagedObjectKey()),
								new Long (eventId.getManagedObjectKey())));

	events = DatabaseInterface.instance().retrieveEvents (criteria);

	if (events.size() == 0) {
	    return null;
	}

	iter = events.iterator();
	return (EmanagerEventDetails) iter.next();
    }

    /**
     * @param eventMessage
     * @return com.cisco.eManager.common.event.EmanagerEventMessage
     * @roseuid 3F2E6F8A00CC
     */
    public synchronized EmanagerEventDetails addEvent(AbstractEventMessage eventMessage) throws EmanagerDatabaseException, EmanagerEventException
    {
	ManagedObjectId eventObjectId;
        EmanagerEventDetails eventDetails;

	eventDetails = null;
        eventObjectId = DatabaseInterface.instance().createEvent (eventMessage);

        if (eventMessage instanceof EmanagerEventMessage) {
	    EmanagerEventMessage emanagerEventMessage = (EmanagerEventMessage) eventMessage;
            eventDetails = new EmanagerEventDetails (eventObjectId.getManagedObjectKey(),
						     emanagerEventMessage.getEventTime(),
						     (Date) null,
						     emanagerEventMessage.getSeverity(),
						     emanagerEventMessage.getManagedObjectId(),
						     (EventAcknowledgement) null,
						     emanagerEventMessage.getDisplayText());
        }else if (eventMessage instanceof TibcoEventMessage) {
            ManagedObjectId associatedManagedObjectId = null;
	    TibcoEventMessage tibcoEventMessage = (TibcoEventMessage) eventMessage;

	    eventDetails = new TibcoEventDetails (eventObjectId.getManagedObjectKey(),
						  tibcoEventMessage.getEventTime(),
						  (Date) null,
						  tibcoEventMessage.getSeverity(),
						  tibcoEventMessage.getManagedObjectId(),
						  (EventAcknowledgement) null,
						  tibcoEventMessage.getTibcoEventId(),
						  tibcoEventMessage.getRuleText(),
						  tibcoEventMessage.getRuleTestText(),
						  tibcoEventMessage.getMicroagentId().name(),
						  tibcoEventMessage.getManagementPolicyManagedObjectId(),
						  tibcoEventMessage.getDisplayText());
	} else if (eventMessage instanceof ProcessSequencerEventMessage) {
	    ProcessSequencerEventMessage psEventMessage = (ProcessSequencerEventMessage) eventMessage;
	    eventDetails = new ProcessSequencerEventDetails (eventObjectId.getManagedObjectKey(),
							     psEventMessage.getEventTime(),
							     (Date) null,
							     psEventMessage.getSeverity(),
							     psEventMessage.getManagedObjectId(),
							     (EventAcknowledgement) null,
							     psEventMessage.getDisplayText(),
							     psEventMessage.getProcessSequencerEventId());
	} else {
	    EmanagerEventException e;

	    e = new EmanagerEventException (EmanagerEventStatusCode.UnknownEventMessageType,
					    EmanagerEventStatusCode.UnknownEventMessageType.getStatusCodeDescription() +
					    eventMessage.getClass().getName());
	    throw e;
	}

        return eventDetails;
    }

    public void updateEvents (Collection eventDetailsList) throws EmanagerDatabaseException
    {
        DatabaseInterface.instance().updateEvents (eventDetailsList);
    }

    public void updateEvent (EmanagerEventDetails eventDetails) throws EmanagerDatabaseException
    {
	Collection events;

	events = new LinkedList();
	events.add (eventDetails);
	updateEvents (events);
    }

    public void clearEvent (ManagedObjectId eventObjectId,
                            Date clearDate) throws EmanagerDatabaseException
    {
        return;
    }
}

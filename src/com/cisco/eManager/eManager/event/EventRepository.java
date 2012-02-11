package com.cisco.eManager.eManager.event;

import org.apache.log4j.*;

import java.util.Date;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import com.cisco.eManager.common.event.EventType;
import com.cisco.eManager.common.event.AbstractEventMessage;
import com.cisco.eManager.common.event.EmanagerEventMessage;
import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.EventSearchCriteria;
import com.cisco.eManager.common.event.NumericSearchCriteria;
import com.cisco.eManager.common.event.EventDeletionCriteria;
import com.cisco.eManager.common.event.EventAcknowledgement;
import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

import com.cisco.eManager.common.database.EmanagerDatabaseException;

import com.cisco.eManager.eManager.event.EventCache;
import com.cisco.eManager.eManager.event.EventArchive;

public class EventRepository
{
    private static Logger      logger = Logger.getLogger(EventRepository.class);

    static private EventRepository eventRepository = null;
    private EventArchive eventArchive;
    private EventCache outstandingEventCache;

    /**
     * @roseuid 3F575EFD0326
     */
    private EventRepository()
    {
        // Initialize the cache.
        EventCache.instance();
    }

    static public EventRepository instance()
    {
        if (eventRepository == null) {
            eventRepository = new EventRepository();
        }

        return eventRepository;
    }

    public Collection processEvent (AbstractEventMessage eventMessage)
    {
        Collection associatedEventDetails = null;

        if (eventMessage.getEventType() == EventType.PostType ||
            eventMessage.getEventType() == EventType.ClearType) {
            associatedEventDetails = EventCache.instance().processEvent (eventMessage);
        }

        return associatedEventDetails;
    }

    /**
     * @param host The host of interest.
     * @param hostObjectId
     * @return The function returns a collection of EventCacheMessage objects.  The
     * event order is arbitrary.
     * @roseuid 3F2565610174
     */
    public Collection getHostAgentEvents(ManagedObjectId hostObjectId)
    {
     return null;
    }

    /**
     * @param eventDeletionCriteria
     * @roseuid 3F25D5B100DC
     */
    public void removeArchivedEvents(EventDeletionCriteria criteria) throws EmanagerDatabaseException
    {
	EventArchive.instance().removeArchiveEvents (criteria);
    }

    /**
     * @param hostObjectId
     * @return Date
     * @roseuid 3F298D6F02B7
     */
    public Date getLatestHostAgentEventGenerationTime(ManagedObjectId hostObjectId)
    {
     return null;
    }

    /**
     * @param host
     * @param hostAgentObjectId
     * @return Collection of EventCacheMessage objects, in arbitrary order.
     * @roseuid 3F2C5AD9005B
     */
    public Collection getOutstandingHostAgentEvents(ManagedObjectId hostAgentObjectId)
    {
        List events;

        events = EventCache.instance().getOutstandingHostEvents (hostAgentObjectId);
        return events;
    }

    protected EmanagerEventDetails clearEvent (ManagedObjectId eventObjectId)
    {
	EmanagerEventDetails details;

	details = null;
	// First clear the cache
	details = EventCache.instance().clearEvent (eventObjectId);
	if (details != null) {
	    details.setClearTime (new Date());

	    try {
		EventArchive.instance().updateEvent (details);
	    }
	    catch (EmanagerDatabaseException e) {
		logger.error ("Error clearing outstanding event: " + e.getMessage());
	    }
	}

	return details;
    }

    protected Collection clearOutstandingApplicationInstanceEvents (ManagedObjectId appInstanceId)
    {
        Iterator iter;
	Collection clearedEvents;
        EmanagerEventDetails details;

        clearedEvents = EventCache.instance().clearOutstandingApplicationInstanceEvents(appInstanceId);
        if (clearedEvents != null) {
            iter = clearedEvents.iterator();
            while (iter.hasNext()) {
                details = (EmanagerEventDetails) iter.next();
                details.setClearTime(new Date());
                try {
                    EventArchive.instance().updateEvent (details);
                }
                catch (EmanagerDatabaseException e) {
                    logger.error ("Error clearing outstanding event: " + e.getMessage());
                }
            }
        }

        return clearedEvents;
    }

    /**
     * @param emanagerEventId
     * @return Detailed event information of either TibcoEventMessage or
     * EmanagerEventMessage type.
     * @roseuid 3F2D4CE5028B
     */
    public EmanagerEventDetails  getEventDetails(ManagedObjectId emanagerEventId) throws EmanagerDatabaseException,
        EmanagerEventException
    {
	return EventArchive.instance().getEventDetails (emanagerEventId);
    }

    public synchronized void unacknowledgeEvent(ManagedObjectId emanagerEventId, String userId) throws EmanagerEventException, EmanagerDatabaseException
    {
	Iterator iter;
	Collection events;
	NumericSearchCriteria numCriteria;
	EventSearchCriteria criteria;
	EmanagerEventDetails details;

	if (emanagerEventId.getManagedObjectType() != ManagedObjectIdType.Event) {
	    EmanagerEventException e;

	    e = new EmanagerEventException (EmanagerEventStatusCode.MalformedEventObjectId,
					    EmanagerEventStatusCode.MalformedEventObjectId.getStatusCodeDescription() +
					    emanagerEventId.toString());

	    throw e;
	}

	criteria = new EventSearchCriteria();
	numCriteria = new NumericSearchCriteria (new Long (emanagerEventId.getManagedObjectKey()),
						 new Long (emanagerEventId.getManagedObjectKey()));
	criteria.setEmanagerEventId (numCriteria);
	events = EventArchive.instance().retrieveEvents (criteria);
	if (events.size() == 0) {
	    EmanagerEventException e;

	    e = new EmanagerEventException (EmanagerEventStatusCode.UnableToFindEvent,
					    EmanagerEventStatusCode.UnableToFindEvent.getStatusCodeDescription() +
					    emanagerEventId.toString());

	    throw e;
	}

	iter = events.iterator();
	details = (EmanagerEventDetails) iter.next();
	details.setAcknowledgement((EventAcknowledgement) null);
        events = new LinkedList();
        events.add(details);
	EventArchive.instance().updateEvents (events);
	if (details.getClearTime() == null) {
	    EventCache.instance().acknowledgeEvent (emanagerEventId, (EventAcknowledgement) null);
	}
    }

    /**
     * @param emanagerEventId
     * @param acknowledgement
     * @roseuid 3F2E6C8D0299
     */
    public synchronized void acknowledgeEvent(ManagedObjectId emanagerEventId, EventAcknowledgement acknowledgement) throws EmanagerEventException, EmanagerDatabaseException
    {
	Iterator iter;
	Collection events;
	NumericSearchCriteria numCriteria;
	EventSearchCriteria criteria;
	EmanagerEventDetails details;

	if (emanagerEventId.getManagedObjectType() != ManagedObjectIdType.Event) {
	    EmanagerEventException e;

	    e = new EmanagerEventException (EmanagerEventStatusCode.MalformedEventObjectId,
					    EmanagerEventStatusCode.MalformedEventObjectId.getStatusCodeDescription() +
					    emanagerEventId.toString());

	    throw e;
	}

	criteria = new EventSearchCriteria();
	numCriteria = new NumericSearchCriteria (new Long (emanagerEventId.getManagedObjectKey()),
						 new Long (emanagerEventId.getManagedObjectKey()));
	criteria.setEmanagerEventId (numCriteria);
	events = EventArchive.instance().retrieveEvents (criteria);
	if (events.size() == 0) {
	    EmanagerEventException e;

	    e = new EmanagerEventException (EmanagerEventStatusCode.UnableToFindEvent,
					    EmanagerEventStatusCode.UnableToFindEvent.getStatusCodeDescription() +
					    emanagerEventId.toString());

	    throw e;
	}

	iter = events.iterator();
	details = (EmanagerEventDetails) iter.next();
	if (details.getAcknowledgement() != null) {
	    EmanagerEventException e;

	    e = new EmanagerEventException (EmanagerEventStatusCode.EventAlreadyAcknowledged,
					    EmanagerEventStatusCode.EventAlreadyAcknowledged.getStatusCodeDescription() +
					    emanagerEventId.toString());

	    throw e;
	}

	details.setAcknowledgement (acknowledgement);
        events = new LinkedList();
        events.add (details);
	EventArchive.instance().updateEvents (events);
	if (details.getClearTime() == null) {
	    EventCache.instance().acknowledgeEvent (emanagerEventId, acknowledgement);
	}
    }
}

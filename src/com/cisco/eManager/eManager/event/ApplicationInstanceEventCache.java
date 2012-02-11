package com.cisco.eManager.eManager.event;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.cisco.eManager.common.database.EmanagerDatabaseException;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.TibcoEventDetails;
import com.cisco.eManager.common.event.ProcessSequencerEventDetails;
import com.cisco.eManager.common.event.ProcessSequencerEventId;
import com.cisco.eManager.common.event.AbstractEventMessage;
import com.cisco.eManager.common.event.EmanagerEventMessage;
import com.cisco.eManager.common.event.ProcessSequencerEventMessage;

import com.cisco.eManager.eManager.event.TibcoEventMessage;
import com.cisco.eManager.eManager.event.EventArchive;
import com.cisco.eManager.eManager.event.EventGlobals;

import com.cisco.eManager.eManager.util.GlobalProperties;

/**
 * @author wstubb
 * @version 1.0
 */
public class ApplicationInstanceEventCache
{
    private ManagedObjectId applicationInstanceId;
    private List events;

    /**
     * @param applicationInstanceObjectId
     */
    public ApplicationInstanceEventCache(ManagedObjectId applicationInstanceObjectId)
    {
        this.applicationInstanceId = applicationInstanceObjectId;
        events = Collections.synchronizedList(new LinkedList());
    }

    public ManagedObjectId getApplicationInstanceEventCacheId()
    {
        return applicationInstanceId;
    }

    /**
     * Access method for the events property.
     *
     * @return   the current value of the events property
     */
    public List getEvents()
    {
        return events;
    }

    /**
     * @param event
     */
    public void addEvent(EmanagerEventDetails event)
    {
        synchronized (events) {
            events.add(event);
        }
    }

    public Collection clearEvents()
    {
	Iterator iter;
	Collection returnEvents;

	returnEvents = new LinkedList();
	synchronized (events) {
	    iter = events.iterator();
	    while (iter.hasNext()) {
		returnEvents.add (iter.next());
	    }
	    
	    events.clear();
	}

	return returnEvents;
    }

    /**
     * @param emanagerEventId
     */
    public Collection clearEvents(AbstractEventMessage event)
    {
        Iterator iter;
        EmanagerEventMessage emanagerEvent = null;
        TibcoEventMessage tibcoEvent = null;
        ProcessSequencerEventMessage processSequencerEvent = null;
        EmanagerEventDetails eventDetails;
        Collection associatedEvents = new LinkedList();

        if (event instanceof TibcoEventMessage) {
            tibcoEvent = (TibcoEventMessage) event;
        } else if (event instanceof EmanagerEventMessage) {
            emanagerEvent = (EmanagerEventMessage) event;
        } else if (event instanceof ProcessSequencerEventMessage) {
            processSequencerEvent = (ProcessSequencerEventMessage) event;
        }

        synchronized (events) {
            iter = events.iterator();
            while (iter.hasNext()) {
                eventDetails = (EmanagerEventDetails) iter.next();
                if (event instanceof EmanagerEventMessage) {
                    if (eventDetails instanceof EmanagerEventDetails) {
                        if (emanagerEvent.getEmanagerEventId() == ( ((EmanagerEventDetails)eventDetails).getObjectId().getManagedObjectKey())) {
                            eventDetails.setClearTime(event.getEventTime());
                            associatedEvents.add(eventDetails);
                        }
                    }
                } else if (event instanceof TibcoEventMessage) {
                    if (eventDetails instanceof TibcoEventDetails) {
                        if (tibcoEvent.getTibcoEventId() == ( (TibcoEventDetails) eventDetails).getTibcoEventId()) {
                            eventDetails.setClearTime(event.getEventTime());
                            associatedEvents.add(eventDetails);
                        }
                    }
                } else if (event instanceof ProcessSequencerEventMessage) {
                    if (eventDetails instanceof ProcessSequencerEventDetails) {
                        if (processSequencerEvent.getProcessSequencerEventId().getEventId() ==
                            ( (ProcessSequencerEventDetails) eventDetails).getProcessSequencerEventId().getEventId()) {
                            associatedEvents.add(eventDetails);
                        }
                    }
                }
            }

            if (associatedEvents.size() > 0) {
                iter = associatedEvents.iterator();
                while (iter.hasNext()) {
                    events.remove(iter.next());
                }
            }
        }

        return associatedEvents;
    }

    public EmanagerEventDetails find (ManagedObjectId eventId)
    {
	Iterator iter;
	EmanagerEventDetails details;

	if (eventId.getManagedObjectType() != ManagedObjectIdType.Event) {
	    return null;
	}

	synchronized (events) {
	    iter = events.iterator();
	    while (iter.hasNext()) {
		details = (EmanagerEventDetails) iter.next();
		if (details.getObjectId().equals (eventId)) {
		    return details;
		}
	    }
	}

	return null;
    }

    public EmanagerEventDetails clearEvent (ManagedObjectId eventId)
    {
	Iterator iter;
	EmanagerEventDetails details;

	if (eventId.getManagedObjectType() != ManagedObjectIdType.Event) {
	    return null;
	}

	synchronized (events) {
	    iter = events.iterator();
	    while (iter.hasNext()) {
		details = (EmanagerEventDetails) iter.next();
		if (details.getObjectId().equals (eventId)) {
		    events.remove (details);
		    return details;
		}
	    }
	}

	return null;
    }

    public String toString()
    {
        Iterator iter;
        StringBuffer strBuf;
        EmanagerEventDetails event;

        strBuf = new StringBuffer();

        strBuf.append ("        App Instance Cache " + applicationInstanceId + GlobalProperties.CarriageReturn);
        strBuf.append ("        ------------------" + GlobalProperties.CarriageReturn);

        synchronized (events) {
            iter = events.iterator();
            while (iter.hasNext()) {
                event = (EmanagerEventDetails) iter.next();
                strBuf.append ("            " + event.toString() + GlobalProperties.CarriageReturn);
            }
        }

        return strBuf.toString();
    }
}

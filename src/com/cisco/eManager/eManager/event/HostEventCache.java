package com.cisco.eManager.eManager.event;

import org.apache.log4j.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.cisco.eManager.common.event.EmanagerEventMessage;
import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.TibcoEventDetails;
import com.cisco.eManager.common.event.ProcessSequencerEventDetails;
import com.cisco.eManager.common.event.AbstractEventMessage;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager;

import com.cisco.eManager.eManager.event.ApplicationInstanceEventCache;
import com.cisco.eManager.eManager.event.TibcoEventMessage;

import com.cisco.eManager.eManager.util.GlobalProperties;

public class HostEventCache
{
    private static Logger      logger = Logger.getLogger(HostEventCache.class);

    private ManagedObjectId hostObjectId;
    private List applicationInstanceCaches;

    /**
     * @param hostObjectId
     */
    public HostEventCache(ManagedObjectId hostObjectId)
    {
        this.hostObjectId = hostObjectId;
        applicationInstanceCaches = Collections.synchronizedList(new ArrayList());
    }

    public ManagedObjectId getHostEventCacheId()
    {
        return hostObjectId;
    }

    /**
     * @param event
     */
    public void addEvent(EmanagerEventDetails event)
    {
        ApplicationInstanceEventCache appCache;

        appCache = getApplicationInstanceEventCache (event);
        if (appCache == null) {
	    AppInstance appInstance;

	    appInstance = AppInstanceManager.instance().find (event.getManagedObjectId());
	    if (appInstance != null) {
		appCache = addApplicationInstanceCache (appInstance);
	    } else {
		logger.error ("Internal error.  Application associated with the event was not found: " + event);
	    }
        }

        appCache.addEvent(event);
    }

    /**
     * @param emanagerEventId
     */
    public Collection clearEvents(AbstractEventMessage event)
    {
        Collection associatedEvents;
        ApplicationInstanceEventCache appCache;

        appCache = getApplicationInstanceEventCache (event);
        if (appCache != null) {
            associatedEvents = appCache.clearEvents(event);
        } else {
            associatedEvents = new LinkedList();
        }

        return associatedEvents;
    }

    public EmanagerEventDetails find (ManagedObjectId event)
    {
	Iterator iter;
	EmanagerEventDetails details;
	ApplicationInstanceEventCache appCache;

	if (event.getManagedObjectType() != ManagedObjectIdType.Event) {
	    return null;
	}

	synchronized (applicationInstanceCaches) {
	    iter = applicationInstanceCaches.iterator();
	    while (iter.hasNext()) {
		appCache = (ApplicationInstanceEventCache) iter.next();
		details = appCache.find (event);
		if (details != null) {
		    return details;
		}
	    }
	}

	return null;
    }

    public EmanagerEventDetails clearEvent (ManagedObjectId event)
    {
	Iterator iter;
	EmanagerEventDetails details;
	ApplicationInstanceEventCache appCache;

	if (event.getManagedObjectType() != ManagedObjectIdType.Event) {
	    return null;
	}

	synchronized (applicationInstanceCaches) {
	    iter = applicationInstanceCaches.iterator();
	    while (iter.hasNext()) {
		appCache = (ApplicationInstanceEventCache) iter.next();
		details = appCache.clearEvent (event);
		if (details != null) {
		    return details;
		}
	    }
	}

	return null;
    }

    public Collection clearOutstandingApplicationInstanceEvents (ManagedObjectId appInstanceId)
    {
	Iterator iter;
	Collection events;
	ApplicationInstanceEventCache appCache;

	if (appInstanceId.getManagedObjectType() != ManagedObjectIdType.ApplicationInstance) {
	    logger.error ("Malformed application instance object id encountered: " + appInstanceId);
	}

	synchronized (applicationInstanceCaches) {
	    iter = applicationInstanceCaches.iterator();
	    while (iter.hasNext()) {
		appCache = (ApplicationInstanceEventCache) iter.next();
		if (appCache.getApplicationInstanceEventCacheId().equals (appInstanceId)) {
		    events = appCache.clearEvents();
		    return events;
		}
	    }
	}

	return null;
    }

    public ApplicationInstanceEventCache addApplicationInstanceCache (AppInstance appInstance)
    {
        Iterator iter;
        ApplicationInstanceEventCache appCache;

        appCache = getApplicationInstanceEventCache (appInstance.id());
        if (appCache == null) {
            appCache = new ApplicationInstanceEventCache(appInstance.id());
            synchronized (applicationInstanceCaches) {
                applicationInstanceCaches.add(appCache);
            }
        }

        return appCache;
    }

    // Do we clear out all of the events when the cache is deleted?
    public void deleteApplicationInstanceCache (ManagedObjectId appInstanceObjectId)
    {
        ApplicationInstanceEventCache appCache;

        appCache = getApplicationInstanceEventCache (appInstanceObjectId);
        if (appCache != null) {
            synchronized (applicationInstanceCaches) {
                applicationInstanceCaches.remove(appCache);
            }
        }
    }

    public int numberApplicationInstanceCaches()
    {
        synchronized (applicationInstanceCaches) {
            return applicationInstanceCaches.size();
        }
    }

    public List getEvents()
    {
        List       hostEvents;
        Collection appCacheEvents;
        Iterator iter;
        Iterator iter2;
        EmanagerEventDetails event;
        ApplicationInstanceEventCache appCache;

        hostEvents = new LinkedList();
        synchronized (applicationInstanceCaches) {
            iter = applicationInstanceCaches.iterator();
            while (iter.hasNext()) {
                appCache = (ApplicationInstanceEventCache) iter.next();
                appCacheEvents = appCache.getEvents();
                synchronized (appCacheEvents) {
                    iter2 = appCacheEvents.iterator();
                    while (iter2.hasNext()) {
                        event = (EmanagerEventDetails) iter2.next();
                        hostEvents.add(event);
                    }
                }
            }
        }

        return hostEvents;
    }

    /**
     * @param hostManagedObjectKey
     * @return Collection of EventCacheMessages in arbitrary order.
     */
    public Collection getApplicationInstanceEvents(ManagedObjectId applicationInstanceObjectId)
    {
        return null;
    }

    private ApplicationInstanceEventCache getApplicationInstanceEventCache (AbstractEventMessage event)
    {
        ManagedObjectId appInstanceId;

        appInstanceId = null;
        if (event instanceof EmanagerEventMessage) {
            EmanagerEventMessage emanagerEvent;

            emanagerEvent = (EmanagerEventMessage) event;
            appInstanceId = emanagerEvent.getManagedObjectId();
        } else if (event instanceof TibcoEventMessage) {
            TibcoEventMessage tibcoEventMessage;

            tibcoEventMessage = (TibcoEventMessage) event;
            appInstanceId = tibcoEventMessage.getManagedObjectId();
        }

        return getApplicationInstanceEventCache (appInstanceId);
    }

    private ApplicationInstanceEventCache getApplicationInstanceEventCache (EmanagerEventDetails event)
    {
        ManagedObjectId appInstanceId;
        ApplicationInstanceEventCache appEventCache;

        appInstanceId = null;
        appEventCache = null;
        if (event instanceof EmanagerEventDetails) {
            appInstanceId = event.getManagedObjectId();
        } else if (event instanceof TibcoEventDetails) {
            appInstanceId = event.getManagedObjectId();
        } else if (event instanceof ProcessSequencerEventDetails) {
            appInstanceId = event.getManagedObjectId();
        }

        if (appInstanceId != null) {
            appEventCache = getApplicationInstanceEventCache (appInstanceId);
        }

        return appEventCache;
    }

    private ApplicationInstanceEventCache getApplicationInstanceEventCache (ManagedObjectId applicationInstanceObjectId)
    {
        Iterator iter;
        ApplicationInstanceEventCache appEventCache;

        if (applicationInstanceObjectId == null)
            return null;

        appEventCache = null;
        synchronized (applicationInstanceCaches) {
            iter = applicationInstanceCaches.iterator();
            while (iter.hasNext()) {
                appEventCache = (ApplicationInstanceEventCache) iter.next();
                if (appEventCache.getApplicationInstanceEventCacheId().equals (applicationInstanceObjectId) == true) {
                    return appEventCache;
                }
            }
        }
        return null;
    }

    public String toString()
    {
        Iterator iter;
        StringBuffer strBuf;
        ApplicationInstanceEventCache appCache;

        strBuf = new StringBuffer();

        strBuf.append("    Host Event Cache " + hostObjectId.toString() + GlobalProperties.CarriageReturn);
        strBuf.append("    ----------------" + GlobalProperties.CarriageReturn);

        synchronized (applicationInstanceCaches) {
            iter = applicationInstanceCaches.iterator();
            while (iter.hasNext()) {
                appCache = (ApplicationInstanceEventCache) iter.next();
                strBuf.append (appCache.toString());
            }
        }

        return strBuf.toString();
    }
}


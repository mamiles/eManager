package com.cisco.eManager.eManager.event;

import org.apache.log4j.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.TextArea;
import java.awt.Font;
import java.awt.Dimension;

import javax.swing.*;

import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Properties;

import com.cisco.eManager.common.event.EventType;
import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.EmanagerEventMessage;
import com.cisco.eManager.common.event.AbstractEventMessage;
import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

import com.cisco.eManager.common.database.EmanagerDatabaseException;

import com.cisco.eManager.common.event.EventSeverity;
import com.cisco.eManager.common.event.DateSearchCriteria;
import com.cisco.eManager.common.event.EventSearchCriteria;
import com.cisco.eManager.common.event.EventAcknowledgement;

import com.cisco.eManager.eManager.event.HostEventCache;
import com.cisco.eManager.eManager.event.EventArchive;
import com.cisco.eManager.eManager.event.EventGlobals;

import com.cisco.eManager.eManager.inventory.InventoryManager;
import com.cisco.eManager.eManager.inventory.ImAppInstanceCreation;
import com.cisco.eManager.eManager.inventory.ImAppInstanceDeletion;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager;
import com.cisco.eManager.eManager.inventory.host.Host;

import com.cisco.eManager.eManager.database.DatabaseInterface;

import com.cisco.eManager.eManager.util.GlobalProperties;

public class EventCache
{
    private static Logger      logger = Logger.getLogger(EventCache.class);

    static private EventCache eventCache = null;
    private List hostEventCaches;
    private JFrame cacheFrame;

    private EventCache()
    {
        String propertyValue;
        Iterator   iter;
        Collection outstandingEvents;
        Properties systemProperties;
        DateSearchCriteria clearDateCriteria;
        EventSearchCriteria searchCriteria;
        EmanagerEventDetails eventDetails;

        cacheFrame = null;
        hostEventCaches = Collections.synchronizedList(new ArrayList());

        clearDateCriteria = new DateSearchCriteria();
        searchCriteria = new EventSearchCriteria();
        searchCriteria.setClearDate(clearDateCriteria);

        outstandingEvents = null;

        try {
            outstandingEvents = DatabaseInterface.instance().retrieveEvents(searchCriteria);
        }
        catch (EmanagerDatabaseException e) {
            outstandingEvents = null;
            logger.fatal("Unable to retrieve the oustanding events on startup.");
        }

        // clear out the event since we'll sync on host discovery
        if (outstandingEvents != null &&
            !outstandingEvents.isEmpty()) {
            iter = outstandingEvents.iterator();
            while (iter.hasNext()){
                eventDetails = (EmanagerEventDetails)iter.next();
                eventDetails.setClearTime(new Date());
            }

            try {
                EventArchive.instance().updateEvents(outstandingEvents);
            }
            catch (EmanagerDatabaseException e) {
                logger.error("Error clearing outstanding events on startup." + e.getMessage());
            }
        }

        systemProperties = GlobalProperties.instance().getProperties();

        propertyValue = systemProperties.getProperty (EventGlobals.DisplayEventDebuggerFrames);
        if (propertyValue != null) {
            propertyValue = propertyValue.toUpperCase();
            if (propertyValue.startsWith("Y") == true) {
                logger.info("Displaying the EventCache debugger.");
                startDebuggerThread();
            }
        }

        // initialize dependencies
        EventArchive.instance();
    }

    static public EventCache instance()
    {
        if (eventCache == null) {
            eventCache = new EventCache();
        }

        return eventCache;
    }

    public Collection processEvent (AbstractEventMessage event)
    {
        EmanagerEventDetails eventDetails;
        Collection associatedEventDetails = null;

        if (event.getEventType().equals(EventType.PostType)) {
            associatedEventDetails  = new LinkedList();
            try {
                eventDetails = EventArchive.instance().addEvent(event);
		if (eventDetails.getSeverity() == EventSeverity.Informational) {
		    // We don't cache these events.  They are cleared immediately.
		    eventDetails.setClearTime ( eventDetails.getPostTime() );
		    EventArchive.instance().updateEvent (eventDetails);
		} else {
		    // We process the events of the other severity
		    addEvent(eventDetails);
		}

                associatedEventDetails.add(eventDetails);
            }
            catch (EmanagerDatabaseException e) {
		logger.error ("Error processing post event message: " + e.getMessage());
            }
            catch (EmanagerEventException e) {
		logger.error ("Error processing post event message: " + e.getMessage());
            }
        } else if (event.getEventType().equals(EventType.ClearType)) {
	    // We'll process the event even if it is a clear event
	    // because there may be other events of a different severity
	    // related to the informational clear event.
            associatedEventDetails = clearEvents (event);
            try {
                EventArchive.instance().updateEvents(associatedEventDetails);
            }
            catch (EmanagerDatabaseException e) {
		logger.error ("Error processing clear event message: " + e.getMessage());
            }
        }

        return associatedEventDetails;
    }

    public synchronized void acknowledgeEvent (ManagedObjectId eventId,
                                               EventAcknowledgement acknowledgement)
    {
        EmanagerEventDetails details;

	if (eventId.getManagedObjectType() != ManagedObjectIdType.Event) {
	    logger.error ("Malformed event object id encountered in acknowledgement: " + eventId);
	}

	details = retrieveEvent (eventId);
	if (details != null) {
	    details.setAcknowledgement(acknowledgement);
	}
    }

    public EmanagerEventDetails retrieveEvent (ManagedObjectId eventObjectId)
    {
        Iterator iter;
        HostEventCache hostCache;
        EmanagerEventDetails details;

	if (eventObjectId.getManagedObjectType() != ManagedObjectIdType.Event) {
	    logger.error ("Malformed event object id encountered: " + eventObjectId);
	}

	synchronized (hostEventCaches) {
	    iter = hostEventCaches.iterator();
	    while (iter.hasNext()) {
		hostCache = (HostEventCache) iter.next();
		details = hostCache.find (eventObjectId);
		if (details != null) {
		    return details;
		}
	    }
	}

	return null;
    }

    public EmanagerEventDetails clearEvent (ManagedObjectId eventObjectId)
    {
        Iterator iter;
        HostEventCache hostCache;
	EmanagerEventDetails details;

	if (eventObjectId.getManagedObjectType() != ManagedObjectIdType.Event) {
	    logger.error ("Malformed event object id encountered: " + eventObjectId);
	}

	synchronized (hostEventCaches) {
	    iter = hostEventCaches.iterator();
	    while (iter.hasNext()) {
		hostCache = (HostEventCache) iter.next();
		details = hostCache.clearEvent (eventObjectId);
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
        HostEventCache hostCache;
	Collection events;

	if (appInstanceId.getManagedObjectType() != ManagedObjectIdType.ApplicationInstance) {
	    logger.error ("Malformed application instance object id encountered: " + appInstanceId);
	}

	synchronized (hostEventCaches) {
	    iter = hostEventCaches.iterator();
	    while (iter.hasNext()) {
		hostCache = (HostEventCache) iter.next();
		events = hostCache.clearOutstandingApplicationInstanceEvents (appInstanceId);
		if (events != null) {
		    return events;
		}
	    }
	}

	return null;
    }


    private void addEvent (EmanagerEventDetails event)
    {
        AppInstance     appInstance;
        ManagedObjectId associatedObjectId;
        HostEventCache  hostEventCache;
        ManagedObjectId hostAgentObjectId = null;

        associatedObjectId = event.getManagedObjectId();
        // At this point we assume it is an application instance
        appInstance = AppInstanceManager.instance().find(associatedObjectId);
        if (appInstance != null) {
            hostEventCache = getHostEventCache (appInstance.hostId());
            if (hostEventCache == null) {
                hostEventCache = createHostEventCache (appInstance.host());
                hostEventCache.addApplicationInstanceCache(appInstance);
            }

            hostEventCache.addEvent(event);
        }
    }

    public Collection clearEvents (AbstractEventMessage event)
    {
        AppInstance appInstance;
        ManagedObjectId appInstanceId;
        Collection associatedEventDetails;
        HostEventCache hostEventCache;
        ManagedObjectId hostAgentObjectId = null;

        appInstanceId = null;
        if (event instanceof TibcoEventMessage) {
            TibcoEventMessage tibcoEventMessage;

            tibcoEventMessage = (TibcoEventMessage) event;
            appInstanceId = tibcoEventMessage.getManagedObjectId();
        } else if (event instanceof EmanagerEventMessage) {
            EmanagerEventMessage emanagerEvent;

            emanagerEvent = (EmanagerEventMessage) event;
            appInstanceId = emanagerEvent.getManagedObjectId();
        }//  else if (event instanceof ProcessSequencerEventMessage) {

        if (appInstanceId != null) {
            appInstance = AppInstanceManager.instance().find(appInstanceId);
            if (appInstance != null) {
                hostAgentObjectId = appInstance.hostId();
            }
        }

        hostEventCache = getHostEventCache (hostAgentObjectId);
        if (hostEventCache != null) {
            associatedEventDetails = hostEventCache.clearEvents(event);
        } else {
            associatedEventDetails = new LinkedList();
        }

        return associatedEventDetails;
    }

    public List getOutstandingHostEvents (ManagedObjectId hostAgentObjectId)
    {
        List events;
        HostEventCache hostEventCache;

        hostEventCache = getHostEventCache (hostAgentObjectId);
        if (hostEventCache != null) {
            events = hostEventCache.getEvents();
        } else {
            events = new LinkedList();
        }

        return events;
    }

    public Collection getOutstandingApplicationInstanceEvents (ManagedObjectId hostAgentId)
    {
        List events;
        HostEventCache hostEventCache;

        hostEventCache = getHostEventCache (hostAgentId);
        if (hostEventCache != null) {
            events = hostEventCache.getEvents();
        } else {
            events = new LinkedList();
        }

        return events;

    }

    private HostEventCache getHostEventCache (ManagedObjectId hostAgentObjectId)
    {
        Iterator iter;
        HostEventCache hostEventCache;

        synchronized (hostEventCaches) {
            iter = hostEventCaches.iterator();
            while (iter.hasNext()) {
                hostEventCache = (HostEventCache) iter.next();
                if (hostEventCache.getHostEventCacheId().equals (hostAgentObjectId)) {
                    return hostEventCache;
                }
            }
        }

        return null;
    }

    private HostEventCache createHostEventCache (Host host)
    {
        HostEventCache hostEventCache;

        hostEventCache = getHostEventCache (host.id());
        if (hostEventCache == null) {
            hostEventCache = new HostEventCache (host.id());
            synchronized (hostEventCaches) {
                hostEventCaches.add (hostEventCache);
            }
        }

        return hostEventCache;
    }

    protected void deleteApplicationInstanceCache (AppInstance appInstance)
    {
	HostEventCache hostEventCache;

	hostEventCache = getHostEventCache (appInstance.hostId());
	if (hostEventCache != null) {
	    hostEventCache.deleteApplicationInstanceCache (appInstance.id());
	    if (hostEventCache.numberApplicationInstanceCaches() == 0) {
		synchronized (hostEventCaches) {
		    hostEventCaches.remove(hostEventCache);
		}
	    }
	}
    }

    protected void addApplicationInstanceCache (AppInstance appInstance)
    {
	HostEventCache hostEventCache;

	hostEventCache = getHostEventCache (appInstance.hostId());
	if (hostEventCache == null) {
	    hostEventCache = createHostEventCache (appInstance.host());
	}

	hostEventCache.addApplicationInstanceCache (appInstance);
    }

    public String toString()
    {
        Iterator iter;
        HostEventCache hostCache;

        StringBuffer strBuf;

        strBuf = new StringBuffer();

        strBuf.append("Event Cache" + GlobalProperties.CarriageReturn);
        strBuf.append("-----------" + GlobalProperties.CarriageReturn);

        synchronized (hostEventCaches) {
            iter = hostEventCaches.iterator();
            while (iter.hasNext()) {
                hostCache = (HostEventCache) iter.next();
                strBuf.append (hostCache.toString());
            }
        }

        return strBuf.toString();
    }

    private void startDebuggerThread()
    {
        Thread t;
        ThreadGroup parentThreadGroup;
        MessageQueueDebugger debugger;

        parentThreadGroup = EventGlobals.instance().getEventThreadGroup();

        debugger = new MessageQueueDebugger();
        if (parentThreadGroup == null) {
            t = new Thread (debugger, "Event Cache Debugger");
        } else {
            t = new Thread (parentThreadGroup, debugger, "Event Cache Debugger");
        }

        t.start();
    }

    private class MessageQueueDebugger extends JFrame implements Runnable, ActionListener
    {
	private Logger logger = Logger.getLogger(MessageQueueDebugger.class);

        JTextArea textArea;

        public MessageQueueDebugger ()
        {
        }

        private void buildDisplay()
        {
            Font font;
            JFrame frame;
            JButton refresh;
            JPanel panel;
            JPanel debuggerPanel;

            frame = EventManager.instance().getEventDebuggerFrame();
            debuggerPanel = new JPanel();
            debuggerPanel.setLayout(new BorderLayout());

            refresh = new JButton ("Refresh");
            refresh.addActionListener(this);

            panel = new JPanel();
            panel.setLayout(new FlowLayout());
            panel.add (refresh);
            debuggerPanel.add (panel, BorderLayout.NORTH);

            //Create a text area.
            textArea = new JTextArea("");

            textArea.setFont(new Font("Lucida Console", Font.PLAIN, 12));
            JScrollPane areaScrollPane = new JScrollPane(textArea);
            areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            areaScrollPane.setPreferredSize(new Dimension(250, 250));
            areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Event Cache Content"),
                BorderFactory.createEmptyBorder(5,5,5,5)),
                areaScrollPane.getBorder()));

            debuggerPanel.add(areaScrollPane, BorderLayout.CENTER);

            EventManager.instance().getEventDebuggerPane().add("Event Cache", debuggerPanel);

            frame.pack();

	    try {
		frame.setVisible(true);
	    }
	    catch (Exception e) {
		logger.warn ("Unexpectedly unable to open the EventCache debugger frame: " + e);
		logger.warn ("The debugger thread will exit.");
		System.exit (1);
	    }
        }

        public void run()
        {
            buildDisplay();

        }

        public void actionPerformed (ActionEvent e)
        {
            textArea.setText(EventCache.instance().toString());
        }
    }
}

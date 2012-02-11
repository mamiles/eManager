package com.cisco.eManager.eManager.event;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.log4j.*;

import com.tibco.tibrv.*;

import com.cisco.eManager.common.event.EventType;
import com.cisco.eManager.common.event.EventSeverity;
import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.EmanagerEventMessage;
import com.cisco.eManager.common.event.AbstractEventMessage;
import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.eManager.event.AbstractWorker;
import com.cisco.eManager.eManager.event.EventProcessingWorkerListener;
import com.cisco.eManager.eManager.event.EventRepository;
import com.cisco.eManager.eManager.event.EventMessageQueue;

import com.cisco.eManager.eManager.snmp.SNMPManager;

import com.cisco.eManager.eManager.tibco.TibcoManager;

public class EventProcessingWorker extends AbstractWorker
{
    private Thread               eventProcessThread = null;
    private AbstractEventMessage event = null;
    private String               threadName;
    private static Integer       threadIndex;
    private static ThreadGroup   threadGroup;
    private static List          listeners;
    private static List          workers;

    private static Logger      logger = Logger.getLogger(EventProcessingWorker.class);

    // constants
    private static final String EventProcessingGroupName = "Event Processor";
    private static final String EventProcessThreadName = "Event Processor Thread ";

    /**
     * Static constructor.
     */
    static {
	listeners = Collections.synchronizedList (new ArrayList());
	workers = Collections.synchronizedList (new ArrayList());
	threadGroup = null;
	threadIndex = new Integer (1);
    }

    /**
     * @roseuid 3F539250005C
     */
    private EventProcessingWorker()
    {
	super (new State (State.Idle), new State (State.Idle), false);

        try {
            TibcoManager.instance();
        }
        catch (TibrvException e) {
            logger.error ("Error instantiating tibco manager: " +
                          e.getMessage());
        }
    }

    /**
     * @return com.cisco.eManager.eManager.event.EventProcessingWorker
     * @roseuid 3F427D1E001E
     */
    static public EventProcessingWorker startWorker() throws EmanagerEventException
    {
        String                threadName;
        Thread                newThread = null;
        EventProcessingWorker worker = null;

	synchronized (threadIndex) {
	    worker = new EventProcessingWorker();

	    threadGroup = getThreadGroup();
            threadName = getThreadName();
	    try {
		if (threadGroup == null) {
                    newThread = new Thread(worker, threadName);
                } else {
                    newThread = new Thread(threadGroup, worker, threadName);
                }

                logger.info ("New EventProcessingWorker created: " + newThread.getName());
	    }
	    catch (SecurityException e) {
                EmanagerEventException eee;
                String logString;

                logString = EmanagerEventStatusCode.UnableToStartEventProcessingWorker.getStatusCodeDescription() +
                            e.getMessage();

                logger.error(logString);
                eee = new EmanagerEventException (EmanagerEventStatusCode.UnableToStartEventProcessingWorker,
                                                  logString);
                throw eee;
	    }

	    newThread.start();
	}

	return worker;
    }

    private static synchronized String getThreadName()
    {
        String threadName;

        threadName = new String (EventProcessThreadName + threadIndex.toString());
        threadIndex = new Integer (threadIndex.intValue() + 1);
        return threadName;
    }

    /**
     * @roseuid 3F53B0A60217
     */
    public void run()
    {
	// Record our thread identifier
	eventProcessThread = Thread.currentThread();

        logger.debug ("New SyncProcessingWorker started: ");

	synchronized (workers) {
	    workers.add (this);
	}

	// continue on with processing
        while (getStopWorker () == false) {
            AbstractEventMessage eventMessage;

	    try {
		eventMessage = EventMessageQueue.instance().getNextMessage(true);
		if (eventMessage != null) {
		    logger.debug ("Event processing started - " +
				  eventMessage.toString());

		    synchronized (this) {
			setCurrentState(State.Processing);
			setEvent(eventMessage);
		    }

		    processEvent ();

		    notifyEventProcessingCompleteListeners (eventMessage);

		    logger.debug ("Event processing ended - " +
				  eventMessage.toString());

		    synchronized (this) {
			setCurrentState (State.Idle);
			setEvent (null);
		    }
		}
            }
	    catch (RuntimeException e) {
		logger.error ("Runtime exception encountered: " + e.getMessage());
		synchronized (this) {
		    setCurrentState (State.Idle);
		    setEvent (null);
		}
	    }
        }
    }

    protected synchronized void setCurrentState (State state)
    {
        if (state.equals(State.Idle) &&
            getDesiredState().equals(State.Stop) ) {
            super.setCurrentState(State.Stop);
        } else {
            super.setCurrentState(state);
        }
    }

    public static List getWorkers()
    {
	return workers;
    }

    static private void createThreadGroup()
    {
	if (threadGroup == null) {
	    ThreadGroup parentThreadGroup = null;

	    parentThreadGroup = EventGlobals.instance().getEventThreadGroup();

	    try {
		if (parentThreadGroup == null)
		    threadGroup = new ThreadGroup (EventProcessingGroupName);
		else
		    threadGroup = new ThreadGroup (parentThreadGroup, EventProcessingGroupName);
	    }
	    catch (SecurityException e) {
                String logString;

                logString = "Unable to create EventProcessingWorker thread group: " +
                            EventProcessingGroupName;
                logger.error(logString);
	    }
	}
    }

    /**
     * Access method for the threadGroup property.
     *
     * @return   the current value of the threadGroup property
     */
    static protected ThreadGroup getThreadGroup()
    {
        createThreadGroup();

	return threadGroup;
    }

    protected Thread getThread()
    {
	return eventProcessThread;
    }

    /**
     * Access method for the event property.
     *
     * @return   the current value of the event property
     */
    public synchronized AbstractEventMessage getEvent()
    {
        return event;
    }

    /**
     * Sets the value of the event property.
     *
     * @param aEvent the new value of the event property
     */
    public synchronized void setEvent(AbstractEventMessage aEvent)
    {
        event = aEvent;
    }

    private void processEvent ()
    {
        Collection processedEvents;

        if (event != null) {
            if (event.getEventType().equals(EventType.PostType) == true ||
                event.getEventType().equals(EventType.ClearType) == true) {
                processedEvents = EventRepository.instance().processEvent(event);
		
		// We don't broadcast informational events
		if (processedEvents != null &&
		    processedEvents.size() > 0) {
		    Iterator iter;
		    Collection broadcastEvents;
		    EmanagerEventDetails eventDetails;

		    broadcastEvents = new LinkedList();
		    iter = processedEvents.iterator();
		    while (iter.hasNext()) {
			eventDetails = (EmanagerEventDetails) iter.next();
			if (eventDetails.getSeverity() != EventSeverity.Informational) {
			    broadcastEvents.add (eventDetails);
			}
		    }
		    
		    if (broadcastEvents.size() > 0) {
			try {
			    if (event.getEventType().equals(EventType.PostType) == true){
				SNMPManager.instance().broadcastPostEvents(broadcastEvents);
				TibcoManager.instance().broadcastPostEvents(broadcastEvents);
			    } else{
				SNMPManager.instance().broadcastClearEvents(broadcastEvents);
				TibcoManager.instance().broadcastClearEvents(broadcastEvents);
			    }
			}
			catch (TibrvException e) {
			    // should never get an error here.  It would be caught in
			    // above broadcast method.
			    logger.error ("Unexpectd tibrv exception sending event notification: " +
					  e.getMessage());
			}
		    }
		}
            }
        }
    }

    /**
     * @param emanagerEventMessage
     * @roseuid 3F2552A901DD
     */
    public void notifyEventProcessingCompleteListeners(AbstractEventMessage eventMessage)
    {
        Iterator iter;
        EventProcessingWorkerListener listener;

        synchronized (listeners) {
            iter = listeners.iterator();
            while (iter.hasNext()) {
                listener = (EventProcessingWorkerListener) iter.next();
                listener.eventProcessingComplete(eventMessage);
            }
        }
    }

    public void notifyStateChangeListeners()
    {
        Iterator iter;
        EventProcessingWorkerListener listener;

        synchronized (listeners) {
            iter = listeners.iterator();
            while (iter.hasNext()) {
                listener = (EventProcessingWorkerListener) iter.next();
                listener.eventProcessingWorkerStateChanged(this);
            }
        }
    }

    /**
     * @param listener
     * @roseuid 3F43B0810350
     */
    public static void addListener(EventProcessingWorkerListener listener)
    {
        synchronized (listeners) {
            if (listeners.contains(listener) == false) {
                listeners.add(listener);
            }
        }
    }

    /**
     * @param listener
     * @roseuid 3F43B0B300FB
     */
    public static void removeListener(EventProcessingWorkerListener listener)
    {
        synchronized (listeners) {
            listeners.remove (listener);
        }
    }
}

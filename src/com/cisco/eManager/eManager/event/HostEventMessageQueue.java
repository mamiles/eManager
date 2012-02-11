package com.cisco.eManager.eManager.event;

import org.apache.log4j.*;

import java.util.*;

import com.cisco.eManager.common.inventory.ManagedObjectId;

import com.cisco.eManager.common.event.*;
import com.cisco.eManager.eManager.event.State;
import com.cisco.eManager.eManager.event.HostEventMessageQueueListener;
import com.cisco.eManager.eManager.event.EventMessageQueue;

import com.cisco.eManager.eManager.util.GlobalProperties;

public class HostEventMessageQueue implements EventProcessingWorkerListener
{
    private static Logger logger = Logger.getLogger(HostEventMessageQueue.class);

    private Boolean bufferHostRetransmittedEvents;
    private State desiredState;
    private State currentState;
    private Collection listeners;
    private Date lastEventGenerationTime;
    private ManagedObjectId hostObjectId;
    private SortedSet eventMessageQueue;
    private SortedSet hostRetransmittedEventBuffer;
    private Collection applicationInstances;

    /**
     * @param hostObjectId

     * @roseuid 3F4F927403A6
     */
    public HostEventMessageQueue(ManagedObjectId hostObjectId)
    {
        bufferHostRetransmittedEvents = new Boolean (false);
        lastEventGenerationTime = new Date(0);
        desiredState = new State (State.Idle);
        currentState = new State (State.Idle);
        this.hostObjectId = hostObjectId;
        listeners = Collections.synchronizedCollection (new LinkedList());
        eventMessageQueue = Collections.synchronizedSortedSet (new TreeSet());
        hostRetransmittedEventBuffer = Collections.synchronizedSortedSet (new TreeSet());
	applicationInstances = Collections.synchronizedCollection (new LinkedList());
    }

    /**
     * Access method for the desiredState property.
     *
     * @return   the current value of the desiredState property
     */
    public State getDesiredState()
    {
        return desiredState;
    }

    /**
     * Sets the value of the desiredState property.
     *
     * @param aDesiredState the new value of the desiredState property
     */
    public void setDesiredState(State aDesiredState)
    {
        desiredState = aDesiredState;
        if (aDesiredState == State.Processing ||
            aDesiredState == State.Idle) {
            if (currentState == State.Stop) {
                currentState = State.Processing;
            }
        } else {
            if (currentState != State.Stop) {
                if (EventMessageQueue.instance().isWorkerCurrentlyProcessingHostAgentEventQueueEvent (hostObjectId)
                    == false) {
                    currentState = State.Stop;
                }
            }
        }
    }

    /**
     * Access method for the hostObjectId property.
     *
     * @return   the current value of the hostObjectId property
     */
    public ManagedObjectId getHostObjectId()
    {
        return hostObjectId;
    }

    /**
     * The following function will register the object to receive notification when
     * a new event is added to the agent.
     * @param listener
     * @roseuid 3F216094031D
     */
    public void addHostEventMessageQueueListener(HostEventMessageQueueListener listener)
    {
        synchronized (listeners) {
            if (listeners.contains (listener) == false) {
                listeners.add (listener);
            }
	}
    }

    /**
     * The following function will de-register an object from receiving host event
     * information.
     * @param listener
     * @roseuid 3F2160AD026C
     */
    public void removeHostEventMessageQueueListener(HostEventMessageQueueListener listener)
    {
        synchronized (listeners) {
            listeners.remove (listener);
	}
    }

    /**
     * @param eventMessage
     * @return Returns true if the event was queued.  false if the event was thrown
     * away.
     * @roseuid 3F2536C1008A
     */
    public boolean queueMessage(AbstractEventMessage eventMessage)
    {
        if (eventMessage.getEventType().equals(EventType.RetransmitType) == true) {
            if (bufferHostRetransmittedEvents.booleanValue() == true) {
                synchronized (hostRetransmittedEventBuffer) {
                    hostRetransmittedEventBuffer.add(eventMessage);
                }
            }
        } else {
            synchronized (eventMessageQueue) {
                eventMessageQueue.add(eventMessage);
            }
        }

        return true;
    }

    /**
     * @return Boolean
     * @roseuid 3F253AE400D9
     */
    public Boolean getBufferHostRetransmittedEvents()
    {
        return bufferHostRetransmittedEvents;
    }

    /**
     * @param inBufferHostRepostEvents
     * @roseuid 3F253B0F0138
     */
    public void setBufferHostRetransmittedEvents(Boolean inBufferHostRepostEvents)
    {
        if (bufferHostRetransmittedEvents.booleanValue() == false &&
            inBufferHostRepostEvents.booleanValue() == true) {
            synchronized (hostRetransmittedEventBuffer) {
                hostRetransmittedEventBuffer.clear();
            }
        }

        bufferHostRetransmittedEvents = inBufferHostRepostEvents;
    }

    public List getBufferedRetransmittedEvents()
    {
        List bufferedEvents;
        Iterator iter;
        AbstractEventMessage event;

        bufferedEvents = new LinkedList();
        synchronized (hostRetransmittedEventBuffer) {
            iter = hostRetransmittedEventBuffer.iterator();
            while (iter.hasNext()) {
                event = (AbstractEventMessage) iter.next();
                bufferedEvents.add(event);
            }
        }

        return bufferedEvents;
    }

    /**
     * @param date
     * @roseuid 3F253BAC00F4
     */
    protected void setLastEventGenerationTime(Date date)
    {
        lastEventGenerationTime = date;
    }

    /**
     * @return Date
     * @roseuid 3F253ED2036C
     */
    protected Date getLastEventGenerationTime()
    {
        return lastEventGenerationTime;
    }

    /**
     * @return com.cisco.eManager.eManager.event.State
     * @roseuid 3F256491015B
     */
    public State getCurrentState()
    {
        return currentState;
    }

    /**
     * @param managementPolicyId
     * @return boolean
     * @roseuid 3F2E646A0289
     */
    public boolean isWorkerCurrentlyProcessingManagementPolicyEvent(ManagedObjectId managementPolicyId)
    {
        List workers;

        workers = EventProcessingWorker.getWorkers();

        synchronized (workers) {
            Iterator iter;
            EventProcessingWorker worker;
            AbstractEventMessage event;

            iter = workers.iterator();
            while (iter.hasNext()) {
                worker = (EventProcessingWorker) iter.next();
                synchronized (worker) {
                    if (worker.getCurrentState() == State.Processing) {
                        event = worker.getEvent();
                        if (event != null &&
                            event instanceof TibcoEventMessage) {
                            if ( ((TibcoEventMessage)event).getManagementPolicyManagedObjectId().equals (managementPolicyId)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * @return Returns the next event message in the queue.  Null if there are no
     * @roseuid 3F29827802C8
     */
    public AbstractEventMessage getNextMessage()
    {
        AbstractEventMessage eventMessage;

        // what if we are buffering, what effect will that have?

        synchronized (eventMessageQueue) {
            if (eventMessageQueue.isEmpty() == false)
            {
                eventMessage = (AbstractEventMessage)eventMessageQueue.first();
                if (eventMessage instanceof TibcoEventMessage)
                {
                    if (isWorkerCurrentlyProcessingManagementPolicyEvent(((
                        TibcoEventMessage)eventMessage).
                        getManagementPolicyManagedObjectId()) == false)
                    {
                        eventMessageQueue.remove(eventMessage);
                        return eventMessage;
                    }
                }
            }
        }

        return null;
    }

    /**
     * @param eventMessage
     * @roseuid 3F29839D03C3
     */
    public void pushMessage(AbstractEventMessage eventMessage)
    {
        // We need to figure out if we are bufferring and what effect
        // that will have

        queueMessage (eventMessage);
    }

    /**
     * @param date
     * @roseuid 3F2C5C4F0026
     */
    public void removeOldTibcoEventMesssages(Date date)
    {
        Collection removedEvents;
        Iterator iter;
        AbstractEventMessage event;

        removedEvents = new LinkedList();
        synchronized (eventMessageQueue) {
            iter = eventMessageQueue.iterator();
            while (iter.hasNext()) {
                event = (AbstractEventMessage) iter.next();
                if (event instanceof TibcoEventMessage) {
                    if (event.getEventTime ().before (date) == true ||
                        event.getEventTime().equals(date) == true) {
                        removedEvents.add (event);
                    }
                }
            }

            if (removedEvents.size() > 0) {
                iter = removedEvents.iterator();
                while (iter.hasNext()){
                    eventMessageQueue.remove(iter.next());
                }
            }
        }
    }

    /**
     * @param state
     * @roseuid 3F2E8A7B00DD
     */
    protected synchronized void setCurrentState(State state)
    {
        currentState = state;
        notifyHostEventMessageQueueStateChangeListeners();
    }

    /**
     * @roseuid 3F2E8B2D0054
     */
    public void notifyHostEventMessageQueueStateChangeListeners()
    {
        Iterator iter;
        HostEventMessageQueueListener listener;
        synchronized (listeners) {
            iter = listeners.iterator();
            while (iter.hasNext()) {
                listener = (HostEventMessageQueueListener) iter.next();
                listener.hostEventMessageQueueStateChanged(this);
            }
        }
    }

    /**
     * @roseuid 3F4E5F990052
     */
    public void eventProcessingWorkerStateChanged()
    {
        // noop
    }

    /**
     * @param event
     * @roseuid 3F4E5F99005C
     */
    public void eventProcessingComplete(AbstractEventMessage event)
    {
        if (getDesiredState().equals(State.Stop) == true &&
            getCurrentState().equals(State.Stop) == false) {
            // if (event_related_to_host_object == true)
            setCurrentState (State.Stop);
        }
    }

    public void addApplicationInstance(ManagedObjectId applicationInstanceId)
    {
	ManagedObjectId appInstanceId;

	synchronized (applicationInstances) {
	    if (applicationInstances.contains (applicationInstanceId) == false) {
		applicationInstances.add (applicationInstanceId);
	    }
	}
    }

    /**
     * @param applicationInstanceObjectId
     * @roseuid 3F2D847E0057
     */
    public void removeApplicationInstanceEvents(ManagedObjectId applicationInstanceObjectId)
    {
        Collection removedEvents;
        Iterator iter;
        AbstractEventMessage event;

        removedEvents = new LinkedList();
        synchronized (eventMessageQueue) {
            iter = eventMessageQueue.iterator();
            while (iter.hasNext()) {
                event = (AbstractEventMessage) iter.next();
                if (event instanceof TibcoEventMessage) {
                    // We need to remove app instance tibco events
                }
            }

            iter = removedEvents.iterator();
            while (iter.hasNext()) {
                eventMessageQueue.remove (iter.next());
            }
        }
    }

    public void deleteApplicationInstance(ManagedObjectId applicationInstanceId)
    {
        try {
            synchronized (applicationInstances){
                applicationInstances.remove(applicationInstanceId);
            }
        }
        catch (Exception e) {
	    logger.warn ("Unexpected exception encountered: " + e);
        }

        removeApplicationInstanceEvents(applicationInstanceId);
    }

    public int numberApplicationInstancesOnHost()
    {
	synchronized (applicationInstances) {
	    return applicationInstances.size();
	}
    }

    public void eventProcessingWorkerStateChanged (EventProcessingWorker worker)
    {

    }

    public String toString()
    {
        Iterator iter;
        StringBuffer strBuf;
        ManagedObjectId objectId;
        AbstractEventMessage eventMessage;

        strBuf = new StringBuffer();

        strBuf.append ("Host Cache Object Id:" + hostObjectId + GlobalProperties.CarriageReturn);
        strBuf.append ("---------------------" + GlobalProperties.CarriageReturn);
        strBuf.append ("                       Desired State: " + desiredState + GlobalProperties.CarriageReturn);
        strBuf.append ("                       Current State: " + currentState + GlobalProperties.CarriageReturn);
        strBuf.append ("          Last Event Generation Time: " + lastEventGenerationTime  + GlobalProperties.CarriageReturn);
        strBuf.append ("    Buffer Host Retransmitted Events: " + bufferHostRetransmittedEvents + GlobalProperties.CarriageReturn);
        strBuf.append (GlobalProperties.CarriageReturn);
        strBuf.append ("    Event Message Queue" + GlobalProperties.CarriageReturn);
        strBuf.append ("    -------------------" + GlobalProperties.CarriageReturn);

        synchronized (eventMessageQueue)
        {
            iter = eventMessageQueue.iterator();
            while (iter.hasNext()) {
                eventMessage = (AbstractEventMessage) iter.next();
                strBuf.append ("        " + eventMessage + GlobalProperties.CarriageReturn);
            }
        }

        strBuf.append (GlobalProperties.CarriageReturn);
        strBuf.append ("    Host Retransmit Event Buffer" + GlobalProperties.CarriageReturn);
        strBuf.append ("    ----------------------------" + GlobalProperties.CarriageReturn);
        synchronized (hostRetransmittedEventBuffer) {
            iter = hostRetransmittedEventBuffer.iterator();
            while (iter.hasNext()) {
                eventMessage = (AbstractEventMessage) iter.next();
                strBuf.append ("        " + eventMessage + GlobalProperties.CarriageReturn);
            }
        }

        strBuf.append (GlobalProperties.CarriageReturn);
        strBuf.append ("    Application Instances" + GlobalProperties.CarriageReturn);
        strBuf.append ("    ---------------------" + GlobalProperties.CarriageReturn);
        synchronized (applicationInstances) {
            iter = applicationInstances.iterator();
            while (iter.hasNext()) {
                objectId = (ManagedObjectId) iter.next();
                strBuf.append ("        " + objectId + GlobalProperties.CarriageReturn);
            }
        }

        return strBuf.toString();
    }
}

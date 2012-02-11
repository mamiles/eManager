package com.cisco.eManager.eManager.event;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.TextArea;
import java.awt.Font;
import java.awt.Dimension;

import javax.swing.*;

import java.util.*;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observer;
import java.util.Observable;
import java.util.Properties;
import java.util.Iterator;

import java.lang.StringBuffer;

import org.apache.log4j.*;

import com.cisco.eManager.common.event.AbstractEventMessage;
import com.cisco.eManager.common.inventory.ManagedObjectId;

import com.cisco.eManager.eManager.util.GlobalProperties;

import com.cisco.eManager.common.event.ProcessSequencerEventMessage;

import com.cisco.eManager.eManager.event.TibcoEventMessage;
import com.cisco.eManager.eManager.event.EventMessageQueueListener;
import com.cisco.eManager.eManager.event.HostEventMessageQueueListener;
import com.cisco.eManager.eManager.event.SyncProcessingWorkerListener;

import com.cisco.eManager.eManager.network.ManagementPolicyId;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy;
import com.cisco.eManager.eManager.inventory.host.Host;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicyManager;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager;
import com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation;
import com.cisco.eManager.eManager.inventory.instrumentation.InstrumentationManager;
import com.cisco.eManager.eManager.inventory.appType.AppType;
import com.cisco.eManager.eManager.inventory.appType.AppTypeManager;

import com.cisco.eManager.eManager.util.WatchdogRulebaseNameHelper;
import com.cisco.eManager.eManager.util.WatchdogRulebaseNameHelper.WatchdogRulebaseNameComponents;

import com.cisco.eManager.common.event.*;
import com.cisco.eManager.eManager.event.*;
import com.cisco.eManager.eManager.inventory.*;

/**
 * The mechanism for handling all incomig events.
 *
 * @author mjmatch
 */
public class EventMessageQueue implements EventProcessingWorkerListener, HostEventMessageQueueListener, SyncProcessingWorkerListener
{
    private static Logger      logger = Logger.getLogger(EventMessageQueue.class);

    static private EventMessageQueue eventMessageQueue = null;
    private Collection listeners;
    private Boolean workerLock = new Boolean (true);

    /**
     * The following attribute reflects the desired number of event processing workers.
     * Over time, the system will work to add or remove workers as requried to achieve
     * the desired number of workers.
     */
    private Integer numberEventProcessingWorkers = new Integer (1);
    private List agentEventMessageQueues;
    private int lastAgentMessageQueueIndex;
    /**
     * @roseuid 3F4E5F8903CA
     */
    private EventMessageQueue()
    {
	String     propertyValue;
	Properties systemProperties;

	agentEventMessageQueues = Collections.synchronizedList (new ArrayList());
	listeners = Collections.synchronizedCollection (new LinkedList());
	lastAgentMessageQueueIndex = 0;

	systemProperties = GlobalProperties.instance().getProperties();

	propertyValue = systemProperties.getProperty (EventGlobals.NumberEventProcessingWorkersKey);
	if (propertyValue == null) {
            numberEventProcessingWorkers = new Integer(EventGlobals.NumberEventProcessingWorkersDefault);
            logger.info ("Using Default numberEventProcessingWorkers value: " +
			 numberEventProcessingWorkers.toString());
	} else {
	    try {
                numberEventProcessingWorkers = new Integer (propertyValue);
                logger.info ("Using NumberEventProcessingWorkers value: " +
			     propertyValue);
 	    }
	    catch (NumberFormatException e) {
                numberEventProcessingWorkers = new Integer (EventGlobals.NumberEventProcessingWorkersDefault);
                logger.error ("Format exception converting NumberEventProcessingWorkers from the " +
			      "properties.  Using the default: " +
			      numberEventProcessingWorkers.toString());
	    }
	}

        propertyValue = systemProperties.getProperty (EventGlobals.DisplayEventDebuggerFrames);
        if (propertyValue != null) {
            propertyValue = propertyValue.toUpperCase();
            if (propertyValue.startsWith("Y") == true) {
                logger.info("Displaying the EventMessageQueue debugger.");
                startDebuggerThread();
            }
        }

	// create event processing workers
	startWorkers();
        SyncProcessingWorker.addListener(this);

        // Make sure our EventRepository is ready
        EventRepository.instance();
    }

    /**
     * @param host
     * @return com.cisco.eManager.eManager.event.HostEventMessageQueue
     * @roseuid 3F2195D80116
     */
    protected HostEventMessageQueue getAgentEventMessageQueue(ManagedObjectId hostObjectId)
    {
        logger.debug("Enter");

        HostEventMessageQueue returnQueue;

        synchronized (agentEventMessageQueues) {
            Iterator iter;
            HostEventMessageQueue agentQueue = null;

            returnQueue = null;
            iter = agentEventMessageQueues.iterator();
            while (iter.hasNext()) {
                agentQueue = (HostEventMessageQueue) iter.next();
                if (agentQueue.getHostObjectId().equals(hostObjectId) == true) {
                    returnQueue = agentQueue;
                    break;
                }
            }
        }

        logger.debug("Exit");

        return returnQueue;
    }

    /**
     * @param eventMessage
     * @roseuid 3F21A2DF03B7
     */
    public void queueMessage(AbstractEventMessage eventMessage)
    {
        logger.log(Priority.DEBUG, "Enter");

        synchronized (workerLock) {
            synchronized (agentEventMessageQueues)
		{
		    Iterator iter;
		    ManagedObjectId agentObjectId;
		    HostEventMessageQueue agentQueue;
		    AppInstance appInstance;

		    appInstance = null;
		    agentObjectId = null;
		    if (eventMessage instanceof TibcoEventMessage) {
			TibcoEventMessage tibcoEventMessage;
			ManagedObjectId managementPolicyObjectId;
			ManagementPolicyId managementPolicyId;
			MgmtPolicy mgmtPolicy;
			WatchdogRulebaseNameComponents watchdogRulebaseNameComponents;

			tibcoEventMessage = (TibcoEventMessage)eventMessage;

			// Check to see if the event was generated by the watchdog
			// rulebase.  If the event is assoc. w/the watchdog rulebase,
			// the application instance id will already be set.  If not,
			// it is not assoc. w/the watchdog rulebase.
			if (tibcoEventMessage.getManagedObjectId() == null) {
			    // This is an event generated by a non-watchdog rulebase
			    managementPolicyId = tibcoEventMessage.
				getManagementPolicyId();
			    mgmtPolicy = MgmtPolicyManager.instance().find(managementPolicyId);
			    if (mgmtPolicy != null) {
				tibcoEventMessage.setManagementPolicyManagedObjectId(mgmtPolicy.id());
				if (mgmtPolicy.host() != null &&
				    mgmtPolicy.appType() != null) {
				    List appInstances;
				    
				    appInstances = AppInstanceManager.instance().find(mgmtPolicy.host().id(), 
											  mgmtPolicy.appType().id());
				    if (appInstances.isEmpty() == false) {
					appInstance = (AppInstance) appInstances.get(0);
				    }
				}

				if (appInstance != null &&
				    appInstance.managed() == true) {
				    agentObjectId = mgmtPolicy.host().id();
				    tibcoEventMessage.setManagedObjectId(appInstance.id());
				}
			    }
			} else {
			    // This event was generated by a watchdog rulebase.
			    appInstance = AppInstanceManager.instance().find (tibcoEventMessage.getManagedObjectId());
			    if (appInstance != null &&
				appInstance.managed() == true) {
				if (appInstance.host() != null) {
				    agentObjectId = appInstance.host().id();
				}
			    }
			}
		    } else if (eventMessage instanceof ProcessSequencerEventMessage) {
			Host host;
			ManagedObjectId appInstanceManagedObjectId;
			ProcessSequencerEventMessage procSeqEventMessage;

			procSeqEventMessage = (ProcessSequencerEventMessage) eventMessage;
			appInstanceManagedObjectId = procSeqEventMessage.getManagedObjectId();
			appInstance = AppInstanceManager.instance().find(appInstanceManagedObjectId);
			if (appInstance != null &&
			    appInstance.managed() == true) {
			    host = appInstance.host();
			    if (host != null) {
				agentObjectId = host.id();
			    }
			}
		    } else if (eventMessage instanceof EmanagerEventMessage) {
			Host host;
			ManagedObjectId appInstanceManagedObjectId;
			EmanagerEventMessage emanagerEventMessage;

			emanagerEventMessage = (EmanagerEventMessage) eventMessage;
			appInstanceManagedObjectId = emanagerEventMessage.getManagedObjectId();
			appInstance = AppInstanceManager.instance().find(appInstanceManagedObjectId);
			if (appInstance != null &&
			    appInstance.managed() == true) {
			    host = appInstance.host();
			    if (host != null) {
				agentObjectId = host.id();
			    }
			}

		    } else {
			// we only support receiving the above messages at this point
		    }

		    if (agentObjectId != null)
			{
			    agentQueue = getAgentEventMessageQueue(agentObjectId);
			    if (agentQueue == null)
				{
				    addApplicationInstance(appInstance);
				    agentQueue = getAgentEventMessageQueue(agentObjectId);
				}

			    if (agentQueue != null)
				{
				    synchronized (agentQueue)
					{
					    agentQueue.queueMessage(eventMessage);
					    wakeAllWorkers();
					}
				}
			}
		}
        }

        logger.log(Priority.DEBUG, "Exit");
    }

    /**
     * @param listener
     * @roseuid 3F257DD5031A
     */
    public void addEventMessageQueueListener(EventMessageQueueListener listener)
    {
	synchronized (listeners) {
	    if (listeners.contains (listener) == false) {
		listeners.add (listener);
	    }
	}
    }

    /**
     * @param listener
     * @roseuid 3F257DE20002
     */
    public void removeEventMessageQueueListener(EventMessageQueueListener listener)
    {
	synchronized (listeners) {
	    listeners.remove (listener);
	}
    }

    /**
     * @param host
     * @roseuid 3F26A5270163
     */
    public void notifyAgentEventMessaeQueueDeletedListeners(ManagedObjectId agentId)
    {
	synchronized (listeners) {
	    EventMessageQueueListener listener;
	    Iterator iter = listeners.iterator();

	    while (iter.hasNext()) {
		listener = (EventMessageQueueListener) iter.next();
		listener.hostEventMessageQueueDeleted(agentId);
	    }
	}
    }

    /**
     * @param eventMessage
     * @roseuid 3F2979E402A9
     */
    public void notifyAgentEventMessageQueueAddedListeners(HostEventMessageQueue messageQueue)
    {
	synchronized (listeners) {
	    EventMessageQueueListener listener;
	    Iterator iter = listeners.iterator();

	    while (iter.hasNext()) {
		listener = (EventMessageQueueListener) iter.next();
		listener.hostEventMessageQueueAdded (messageQueue);
	    }
	}
    }

    /**
     * @param sleepUntilAvailable If true, the thread will sleep until an event
     * message is available.  If false, the thread will not sleep and return null if
     * an event is not available.
     * @return Returns the next event message in the queue.  Null if there are no
     * @roseuid 3F297F8A02F0
     */
    public synchronized AbstractEventMessage getNextMessage(boolean sleepUntilAvailable)
    {
        int index = 0;
        int startingIndex = 0;
        boolean foundHostQueue = false;
        boolean resetIndex = false;
        TibcoEventMessage tibcoEvent = null;
        HostEventMessageQueue hostEventMessageQueue = null;
        AbstractEventMessage  event;

        logger.debug ("Enter.");

        event = null;

        try {
            synchronized (workerLock) {
                do {

                    synchronized (agentEventMessageQueues) {
                        logger.debug("agentEventMessageQueueSize: " + agentEventMessageQueues.size());

                        if (agentEventMessageQueues.size() != 0) {
                            resetIndex = false;
                            startingIndex = lastAgentMessageQueueIndex;
                            if (startingIndex > (agentEventMessageQueues.size() - 1)){
                                startingIndex = 0;
                            }

                            index = startingIndex;

                            while (event == null &&
                                   !(index == startingIndex && resetIndex == true)){

                                logger.debug ("Checking queue index: " + index);

                                try{
                                    hostEventMessageQueue = (HostEventMessageQueue)
                                        agentEventMessageQueues.get(index);

                                    if (hostEventMessageQueue.getDesiredState() !=
                                        State.Stop){
                                        event = hostEventMessageQueue.getNextMessage();
                                        if (event != null){
                                            // We don't check event generation time of
                                            // sync generated events.
					    
					    /*
					      For now we won't check for out of order events.
                                            if (event.getSyncGenerated() == false) {
                                                // We need to check the event generation time and determine if
                                                // it is in the proper chronological sequence;
                                                if (event.getEventTime().before(
                                                    hostEventMessageQueue.
                                                    getLastEventGenerationTime())){
                                                    SyncMessageQueue.instance().
                                                        queueMessage(new SyncMessage(hostEventMessageQueue.
                                                        getHostObjectId(),
                                                        SynchronizationPriority.Medium,
                                                        false));

                                                    event = tibcoEvent = null;
                                                } else {
                                                    hostEventMessageQueue.
                                                        setLastEventGenerationTime(event.getEventTime());
                                                }
                                            }
					    */

					    hostEventMessageQueue.setLastEventGenerationTime(event.getEventTime());
                                        }
                                    }
                                }
                                catch (IndexOutOfBoundsException e){
                                    logger.equals(e.getMessage());
                                    e.printStackTrace();
                                }

                                // We need to roll over the index
                                index++;
                                if (index > (agentEventMessageQueues.size() - 1)){
                                    index = 0;
                                    resetIndex = true;
                                }
                            }
                        }
                    }

                    if (sleepUntilAvailable == true &&
                        event == null)
                    {
                        try
                        {
                            logger.debug ("Sleep waiting for next EventMessage.");
                            workerLock.wait(20000);
                            logger.debug ("Thread woke up.");
                        }
                        catch (InterruptedException e)
                        {
                        }
                    }
                }
                while (sleepUntilAvailable == true &&
                       event == null);
            }
        }
        catch (NoSuchElementException e) {
            System.out.println("*** NoSuchElementException thrown.");
        }

        logger.debug ("Exit.");

	return event;
    }

    /**
     * @param applicationObjectId
     * @roseuid 3F2D84D300DB
     */
    public void removeApplicationInstanceEvents(AppInstance appInstance)
    {
        Host host;
        HostEventMessageQueue messageQueue;

        appInstance = AppInstanceManager.instance().find(appInstance.id());
        if (appInstance != null) {
            host = appInstance.host();
            messageQueue = getAgentEventMessageQueue (host.id());
            if (messageQueue != null) {
                messageQueue.removeApplicationInstanceEvents(appInstance.id());
            }
        }
    }


    public boolean isWorkerCurrentlyProcessingHostAgentEventQueueEvent(ManagedObjectId hostAgentQueueId)
    {
        Iterator   iter;
        Collection workers;
        AppInstance appInstance;
        ManagedObjectId appInstanceId;
        EventProcessingWorker worker;
        AbstractEventMessage event;

        workers = EventProcessingWorker.getWorkers();
        synchronized (workers) {
            iter = workers.iterator();
            while (iter.hasNext())
            {
                worker = (EventProcessingWorker)iter.next();
                synchronized (worker)
                {
                    if (worker.getCurrentState().equals(State.Processing) &&
                        worker.getEvent() != null)
                    {
                        appInstance = null;
                        appInstanceId = null;
                        event = worker.getEvent();
                        if (event instanceof EmanagerEventMessage)
                        {
                            appInstanceId = ((EmanagerEventMessage)event).getManagedObjectId();
                            if (appInstance == null)
                            {
                                return true;
                            }
                            else
                            {
                                appInstance = AppInstanceManager.instance().find(appInstanceId);
                                if (appInstance == null)
                                {
                                    return true;
                                }
                                else
                                {
                                    if (appInstance.hostId().equals(hostAgentQueueId))
                                    {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
    /**
     * @roseuid 3F2E84220212
     */
    public void wakeSingleWorker()
    {
	synchronized (workerLock) {
	    workerLock.notify();
	}
    }

    /**
     * @roseuid 3F2E842B031A
     */
    public void wakeAllWorkers()
    {
        logger.debug("Enter");

	synchronized (workerLock) {
	    workerLock.notifyAll();
	}

        logger.debug("Exit");
    }

    public void sleepUntilQueueStateChange()
    {
	synchronized (workerLock) {
            try {
                workerLock.wait();
            }
            catch (InterruptedException e) {
            }
        }
    }

    /**
     * The following routine will put the thread asleep until the
     * HostEventMessageQueue has reached the desired state.  The function will throw
     * an exception on a timeout.
     * @param state
     * @param hostEventMessageQueue
     * @roseuid 3F2E8D3403E1
     */
    public void waitForHostEventMessageQueueState(State state, HostEventMessageQueue hostEventMessageQueue)
    {

    }

    /**
     * @roseuid 3F4E5F8A0050
     */
    public void eventProcessingWorkerStateChanged()
    {

    }

    /**
     * @param event
     * @roseuid 3F4E5F8A0064
     */
    public void eventProcessingComplete(AbstractEventMessage event)
    {

    }

    /**
     * @param hostEventMessageQueue
     * @roseuid 3F4E5F8A0096
     */
    public void agentMessageQueueStateChanged(HostEventMessageQueue hostEventMessageQueue)
    {

    }

    protected void addApplicationInstance (AppInstance appInstance)
    {
        Host host;
        HostEventMessageQueue agentQueue;

        logger.debug ("Enter");

        host = appInstance.host();
        agentQueue = getAgentEventMessageQueue (host.id());
        if (agentQueue == null) {
            agentQueue = new HostEventMessageQueue (host.id());
            synchronized (agentEventMessageQueues) {
                agentEventMessageQueues.add(agentQueue);
            }
        }

        agentQueue.addApplicationInstance (appInstance.id());

        logger.debug ("Exit");
    }

    protected void deleteApplicationInstance (AppInstance appInstance)
    {
        Host host;
        HostEventMessageQueue agentQueue;

        logger.debug ("Enter.");

        host = appInstance.host();
        agentQueue = getAgentEventMessageQueue (host.id());
        if (agentQueue != null) {
            agentQueue.deleteApplicationInstance(appInstance.id());
            if (agentQueue.numberApplicationInstancesOnHost() == 0) {
                synchronized (agentEventMessageQueues) {
                    agentEventMessageQueues.remove(agentQueue);
                }
            }
        }
    }

    /**
     * @return com.cisco.eManager.eManager.event.EventMessageQueue
     * @roseuid 3F53527B01F6
     */
    public synchronized static EventMessageQueue instance()
    {
	if (eventMessageQueue == null) {
	    eventMessageQueue = new EventMessageQueue();
	}

	return eventMessageQueue;
    }

    /**
     * The following routine will reconcile the number of active event processing
     * workers against the desired number in the number
     * EventProcessingWorkers attribute.  The routine contains functionality to
     * start new workers as necessary.  The functionalty to stop workers as necessary
     * is in the It will start and stop the workers as
     * necessary.
     * @roseuid 3F5361A1002C
     */
    private void startWorkers()
    {
	Collection workers;
	EventProcessingWorker eventProcessingWorker;

	workers = EventProcessingWorker.getWorkers();

	synchronized (workers) {
            int numberWorkers;
            int currentNumberWorkers;

            currentNumberWorkers = workers.size();
            numberWorkers = numberEventProcessingWorkers.intValue();
	    if (numberWorkers > currentNumberWorkers) {
		// We need to start some workers
		while (numberWorkers > currentNumberWorkers) {
                    try {
                        EventProcessingWorker.startWorker();
                        currentNumberWorkers++;
                    }
                    catch (EmanagerEventException e) {
                        String logString;

                        logString = "Exiting.  " +
                                    EmanagerEventStatusCode.UnableToStartEventProcessingWorker.getStatusCodeDescription();
                        logger.fatal (logString);
                        System.exit(1);
                    }
                }

		// I still need to figure out how to stop the required number of workers.
	    }
	}
    }

    public void hostEventMessageQueueStateChanged(HostEventMessageQueue hostEventMessageQueue)
    {

    }

    public void eventProcessingWorkerStateChanged (EventProcessingWorker worker)
    {

    }

    public void eventSyncProcessingWorkerSyncStarted(SyncProcessingWorker worker, SyncMessage syncMessage)
    {
        // noop
    }

    public void eventSyncProcessingWorkerSyncEnded(SyncProcessingWorker worker, SyncReport SyncReport)
    {
        wakeAllWorkers();
    }

    private void startDebuggerThread()
    {
        Thread t;
        ThreadGroup parentThreadGroup;
        MessageQueueDebugger debugger;

        parentThreadGroup = EventGlobals.instance().getEventThreadGroup();

        debugger = new MessageQueueDebugger();
        if (parentThreadGroup == null) {
            t = new Thread (debugger, "Event Message Queue Debugger");
        } else {
            t = new Thread (parentThreadGroup, debugger, "Event Message Queue Debugger");
        }

        t.start();
    }

    public String toString()
    {
        Iterator iter;
        StringBuffer strBuf;
        HostEventMessageQueue hostQueue;

        strBuf = new StringBuffer();

        strBuf.append("Event Message Queue" + GlobalProperties.CarriageReturn);
        strBuf.append("-------------------" + GlobalProperties.CarriageReturn);

        synchronized (agentEventMessageQueues) {
            iter = agentEventMessageQueues.iterator();
            while (iter.hasNext()) {
                hostQueue = (HostEventMessageQueue) iter.next();
                strBuf.append(hostQueue.toString());
                strBuf.append (GlobalProperties.CarriageReturn);
                strBuf.append (GlobalProperties.CarriageReturn);
            }
        }

        return strBuf.toString();
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
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Event Message Queue Content"),
                BorderFactory.createEmptyBorder(5,5,5,5)),
                areaScrollPane.getBorder()));

            debuggerPanel.add(areaScrollPane, BorderLayout.CENTER);

            EventManager.instance().getEventDebuggerPane().add("Event Queue", debuggerPanel);

            frame.pack();

	    try {
		frame.setVisible(true);
	    }
	    catch (Exception e) {
		logger.warn ("Unexpectedly unable to open the EventMessageQueue debugger frame: " + e);
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
            textArea.setText(EventMessageQueue.instance().toString());
        }
    }
}

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

import java.util.Date;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import java.util.Properties;

import org.apache.log4j.*;

import com.cisco.eManager.common.event.EventType;
import com.cisco.eManager.common.event.SyncMessage;
import com.cisco.eManager.common.event.AbstractEventMessage;
import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.TibcoEventDetails;
import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.eManager.event.SyncReport;
import com.cisco.eManager.eManager.event.EventCache;
import com.cisco.eManager.eManager.event.HostEventCache;
import com.cisco.eManager.eManager.event.SyncMessageQueue;
import com.cisco.eManager.eManager.event.SyncProcessingWorkerListener;
import com.cisco.eManager.eManager.event.HostEventMessageQueueListener;

import com.cisco.eManager.eManager.inventory.InventoryGlobals;
import com.cisco.eManager.eManager.inventory.host.Host;
import com.cisco.eManager.eManager.inventory.host.HostManager;
import com.cisco.eManager.eManager.inventory.appType.AppType;
import com.cisco.eManager.eManager.inventory.appType.AppTypeManager;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicyManager;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager;
import com.cisco.eManager.eManager.inventory.instrumentation.Method;
import com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation;
import com.cisco.eManager.eManager.inventory.instrumentation.InstrumentationManager;

import com.cisco.eManager.eManager.util.GlobalProperties;

import COM.TIBCO.hawk.console.hawkeye.AlertState;
import COM.TIBCO.hawk.console.hawkeye.AgentInstance;
import COM.TIBCO.hawk.console.hawkeye.RuleBaseStatus;
import COM.TIBCO.hawk.talon.DataElement;
import COM.TIBCO.hawk.talon.MethodInvocation;
import COM.TIBCO.hawk.talon.MicroAgentException;

public class SyncProcessingWorker extends AbstractWorker implements HostEventMessageQueueListener
{
    private static Logger      logger = Logger.getLogger(SyncProcessingWorker.class);

    private Thread             syncProcessThread = null;
    private SyncMessage        syncMessage;
    private SyncReport         syncReport;
    private Boolean            workerSleepLock = new Boolean (true);
    private HostEventMessageQueue messageQueue;
    private Host               host;
    private Date               agentRetransmitTime;
    private String             syncState;

    private static long        hostAgentMessageQueueStopTimeout;
    private static long        tibcoAgentEventRetransmitTimeout;
    private static long        managementPolicySyncLoadingDelay;
    private static ThreadGroup threadGroup;
    private static Integer     threadIndex;
    private static List        listeners;
    private static List        workers;
    private static String      ManagementPolicyMicroAgent;
    private static EventDebugger debugger;

    // constants
    private static final String SyncProcessingGroupName = "Sync Processor";
    private static final String SyncProcessThreadName = "Sync Processor Thread ";

    /**
     * Static constructor.
     */
    static {
	listeners = Collections.synchronizedList (new ArrayList());
	workers = Collections.synchronizedList (new ArrayList());
	threadGroup = null;
	threadIndex = new Integer (1);
        debugger = null;
    }

    /**
     * @roseuid 3F576408011A
     */
    public SyncProcessingWorker()
    {
	super (new State (State.Idle), new State (State.Idle), false);

        String propertyValue;
        Properties systemProperties;

        this.syncMessage = syncMessage;
        systemProperties = GlobalProperties.instance().getProperties();

        propertyValue = systemProperties.getProperty (EventGlobals.HostAgentMessageQueueStopTimeoutKey);
        if (propertyValue == null) {
            hostAgentMessageQueueStopTimeout = EventGlobals.HostAgentMessageQueueStopTimeoutDefault;
            logger.info (EventGlobals.HostAgentMessageQueueStopTimeoutValueMsg +
			Long.toString(hostAgentMessageQueueStopTimeout));
        } else {
            try
            {
                hostAgentMessageQueueStopTimeout = Long.parseLong(propertyValue);
                logger.info (EventGlobals.HostAgentMessageQueueStopTimeoutValueMsg +
			     Long.toString(hostAgentMessageQueueStopTimeout));
            }
            catch (NumberFormatException e)
            {
                hostAgentMessageQueueStopTimeout = EventGlobals.HostAgentMessageQueueStopTimeoutDefault;
                logger.error ("Format exception converting HostAgentMessageQueueStopTimeout from the " +
			      "properties.  Using the default: " +
			      Long.toString(hostAgentMessageQueueStopTimeout));
            }
        }


        propertyValue = systemProperties.getProperty (EventGlobals.TibcoAgentEventRetransmitTimeoutKey);
        if (propertyValue == null) {
            tibcoAgentEventRetransmitTimeout = EventGlobals.TibcoAgentEventRetransmitTimeoutDefault;
            logger.info ("Using Default TibcoAgentEventRetransmitTimeout value: " +
			 Long.toString(tibcoAgentEventRetransmitTimeout));
        } else {
            try
            {
                tibcoAgentEventRetransmitTimeout = Long.parseLong(propertyValue);
                logger.info ("Using TibcoAgentEventRetransmitTimeout value: " +
			     propertyValue);
            }
            catch (NumberFormatException e)
            {
                tibcoAgentEventRetransmitTimeout = EventGlobals.TibcoAgentEventRetransmitTimeoutDefault;
                logger.error ("Format exception converting TibcoAgentEventRetransmitTimeout from the " +
			      "properties.  Using the default: " +
			      Long.toString(tibcoAgentEventRetransmitTimeout));
            }
        }

        propertyValue = systemProperties.getProperty (EventGlobals.ManagementPolicySyncLoadingDelayKey);
        if (propertyValue == null) {
            managementPolicySyncLoadingDelay = EventGlobals.ManagementPolicySyncLoadingDelayDefault;
            logger.info ("Using Default ManagementPolicySyncLoadingDelay value: " +
			 Long.toString(managementPolicySyncLoadingDelay));
        } else {
            try
            {
                managementPolicySyncLoadingDelay = Long.parseLong(propertyValue);
                logger.info ("Using managementPolicySyncLoadingDelay value: " +
			     propertyValue);
            }
            catch (NumberFormatException e)
            {
                managementPolicySyncLoadingDelay = EventGlobals.ManagementPolicySyncLoadingDelayDefault;
                logger.error ("Format exception converting ManagementPolicySyncLoadingDelay from the " +
			      "properties.  Using the default: " +
			      Long.toString(managementPolicySyncLoadingDelay));
            }
        }

        propertyValue = systemProperties.getProperty (EventGlobals.DisplayEventDebuggerFrames);
        if (propertyValue != null) {
            propertyValue = propertyValue.toUpperCase();
            if (propertyValue.startsWith("Y") == true &&
                debugger == null) {
                logger.info("Displaying the Sync Processing Worker debugger.");
                startDebuggerThread();
            }
        }

        syncReport = null;
        messageQueue = null;
        syncMessage = null;
    }

    /**
     * @return com.cisco.eManager.eManager.event.EventProcessingWorker
     * @roseuid 3F427D1E001E
     */
    static public SyncProcessingWorker startWorker() throws EmanagerEventException
    {
        String               threadName;
        Thread               newThread = null;
        SyncProcessingWorker worker = null;

	synchronized (threadIndex) {
	    worker = new SyncProcessingWorker();

	    threadGroup = getThreadGroup();
            threadName = getThreadName();
	    try {
		if (threadGroup == null) {
                    newThread = new Thread(worker, threadName);
                } else{
                    newThread = new Thread(threadGroup, worker, threadName);
                }

                logger.info ("New EventProcessingWorker created: " + newThread.getName());
	    }
	    catch (SecurityException e) {
                EmanagerEventException eee;
                String logString;

                logString = EmanagerEventStatusCode.UnableToStartSyncProcessingWorker.getStatusCodeDescription() +
                            e.getMessage();

                logger.error (logString);
                eee = new EmanagerEventException (EmanagerEventStatusCode.UnableToStartSyncProcessingWorker,
                                                  logString);
                throw eee;
	    }

	    newThread.start();
	}

	return worker;
    }

    private static String getThreadName()
    {
	String threadName;

        threadName = new String (SyncProcessThreadName + threadIndex.toString());
        threadIndex = new Integer (threadIndex.intValue() + 1);
        return threadName;
    }

    /**
     * @roseuid 3F53B0A60217
     */
    public void run()
    {
	// Record our thread identifier
	syncProcessThread = Thread.currentThread();

        logger.debug ("New SyncProcessingWorker started: " + syncProcessThread.getName());

	synchronized (workers) {
	    workers.add (this);
	}

	// continue on with processing
        while (getStopWorker () == false) {
            SyncMessage syncMessage;

            syncMessage = SyncMessageQueue.instance().getNextMessage(true);

            if (syncMessage != null) {
                logger.debug ("Sync started - " +
			      syncMessage.toString());

                synchronized (this) {
                    setCurrentState(State.Processing);
                    setSyncMessage(syncMessage);
                }

		notifyListenersSyncStarted (syncMessage);

                try {
                    initializeSyncVariables();

                    processSyncMessage();
                }
                catch (EmanagerEventException e) {
                    logger.warn("Unexpected exception encountered.  Aborting synchronization." +
                                e);
                }

                if (messageQueue != null) {
                    messageQueue.setBufferHostRetransmittedEvents(new Boolean(false));
                    messageQueue.setDesiredState(State.Idle);
                }

		notifyListenersSyncEnded (syncReport);

                synchronized (this) {
                    setCurrentState (State.Idle);
                    setSyncMessage (null);
                }

                clearVariables();

                logger.debug ("Sync ended - " +
                           syncMessage.toString());
            }
        }
    }

    protected synchronized void setCurrentState (State state)
    {
        super.setCurrentState(state);
    }

    /**
     * Access method for the syncMessage property.
     *
     * @return   the current value of the syncMessage property
     */
    public synchronized SyncMessage getSyncMessage()
    {
        return syncMessage;
    }

    /**
     * Sets the value of the syncMessage property.
     *
     * @param aSyncMessage the new value of the syncMessage property
     */
    public synchronized void setSyncMessage(SyncMessage aSyncMessage)
    {
        syncMessage = aSyncMessage;
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
		    threadGroup = new ThreadGroup (SyncProcessingGroupName);
		else
		    threadGroup = new ThreadGroup (parentThreadGroup, SyncProcessingGroupName);
	    }
	    catch (SecurityException e) {
                String logString;

                logString = "Unable to create SyncProcessingWorker thread group: " +
                            SyncProcessingGroupName;
                logger.error (logString);
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
	return syncProcessThread;
    }

    public static long getHostAgentMessageQueueStopTimeout() {
        return hostAgentMessageQueueStopTimeout;
    }

    public static void sethostAgentMessageQueueStopTimeout(long timeout) {
        hostAgentMessageQueueStopTimeout = timeout;
    }

    public static long getTibcoAgentEventRetransmitTimeout() {
        return tibcoAgentEventRetransmitTimeout;
    }

    private void clearVariables()
    {
        syncReport = null;
        host = null;
        messageQueue = null;
        syncMessage = null;
        syncState = null;
    }

    private void initializeSyncVariables()throws EmanagerEventException
    {
        syncReport = new SyncReport (syncMessage.getHostAgentObjectId());

        syncState = "Starting Sync";

        host = HostManager.instance().find(syncMessage.getHostAgentObjectId());
        if (host != null) {
            if (host.isActive() == false) {
                String logMsg;

                logMsg = "  Subject host is not active:" + host.id();
                syncReport.addLogEntry(logMsg);
                syncReport.setStatus(SyncStatus.Failure);
                throw new EmanagerEventException (EmanagerEventStatusCode.EventSynchronizationFailure,
                                                  EmanagerEventStatusCode.EventSynchronizationFailure.getStatusCodeDescription() +
                                                  logMsg);
            }

            messageQueue = EventMessageQueue.instance().getAgentEventMessageQueue(host.id());
            if (messageQueue == null) {
                String logString;
                EmanagerEventException e;

                logString = EmanagerEventStatusCode.UnableToFindHostEventMessageQueue.getStatusCodeDescription() +
                            host.name();
                logger.error (this.workerIdentifier() + logString);
                syncReport.setStatus(SyncStatus.Failure);
                syncReport.addLogEntry(logString);
                e = new EmanagerEventException (EmanagerEventStatusCode.UnableToFindHostEventMessageQueue,
                                                logString);
                throw e;
            }
        } else {
            EmanagerEventException e;
            String logString;

            logString = EmanagerEventStatusCode.UnableToFindHost.getStatusCodeDescription() +
                        syncMessage.getHostAgentObjectId().toString();
            logger.error (this.workerIdentifier() + logString);
            syncReport.setStatus(SyncStatus.Failure);
            syncReport.addLogEntry(logString);
            logger.error (logString);
            e = new EmanagerEventException (EmanagerEventStatusCode.UnableToFindHost,
                                            logString);
            throw e;
        }
    }

    private void processSyncMessage() throws EmanagerEventException
    {
        Collection retransmittedTibcoMessages;

        configureHostEventMessageQueue();

        // We would broadcast the notification message here for starting the sync

        try {
            if (syncMessage.getSyncManagementPolicies() == true){
                synchronizeManagementPolicies();

                try{
                    syncState = "Sleeping For Management Loading Delay";
                    Thread.sleep(managementPolicySyncLoadingDelay);
                }
                catch (InterruptedException e){
                }
            }
        }
        catch (EmanagerEventException e) {
            // We'll continue.  Maybe we can just sync the events.
        }

        // before we retransmit the events, which is very time
        // consuming, we'll get the current event state and make
        // sure there is at least one outstanding event before we
        // retransmit.
        if (getAgentEventState() != AlertState.NO_ALERT) {

            retransmitAgentEvents();

            try {
                syncState = "Sleeping for retransmitted events";
                long timeout = tibcoAgentEventRetransmitTimeout;
                Thread.sleep(tibcoAgentEventRetransmitTimeout);
            }
            catch (InterruptedException e) {
            }
        }

        reconcileEvents ();
    }


    private void configureHostEventMessageQueue() throws EmanagerEventException
    {
        synchronized (workerSleepLock) {
            messageQueue.addHostEventMessageQueueListener(this);
            messageQueue.setDesiredState(State.Stop);
            try {
                workerSleepLock.wait(hostAgentMessageQueueStopTimeout);
            }
            catch (InterruptedException e) {
            }

            messageQueue.removeHostEventMessageQueueListener(this);

            if (messageQueue.getCurrentState() != State.Stop) {
                EmanagerEventException e;
                String logString;

                // we need to clean up.
                messageQueue.setDesiredState(State.Idle);
                messageQueue.removeHostEventMessageQueueListener(this);

                logString = EmanagerEventStatusCode.UnableToStopHostEventMessageQueue.getStatusCodeDescription() +
                            messageQueue.toString();
                logger.fatal (logString);
                syncReport.setStatus(SyncStatus.Failure);
                syncReport.addLogEntry(logString);
                e = new EmanagerEventException (EmanagerEventStatusCode.UnableToStopHostEventMessageQueue,
                                                logString);
                throw e;
            }
        }

        messageQueue.setBufferHostRetransmittedEvents(Boolean.TRUE);
    }

    private void synchronizeManagementPolicies() throws EmanagerEventException
    {
        boolean managedInstanceFound;
        boolean policyLoaded;
        Iterator iter;
        Iterator iter2;
        List emanagerPolicyList;
        List appInstanceList;
        MgmtPolicy mgmtPolicy;
        Instrumentation rulebaseEngineMicroagent;
        AppInstance systemAppInstance;
        Method loadManagementPolicyMethod;
        AppType appType;
        AppInstance appInstance;
        Collection appInstances;
        AppInstanceManager appInstanceManager;
        RuleBaseStatus ruleBaseStatus[];

        logger.debug ("Enter");

        appType = AppTypeManager.instance().find(InventoryGlobals.sysAppTypeName());
        if (appType == null) {
            EmanagerEventException e;
            String logString;

            logString = EmanagerEventStatusCode.UnableToFindSystemApplicationType.getStatusCodeDescription() +
                        " (" + InventoryGlobals.sysAppTypeName() + ")";

            logger.error (logString);
            syncReport.setStatus(SyncStatus.Warnings);
            syncReport.addLogEntry(logString);
            e = new EmanagerEventException (EmanagerEventStatusCode.UnableToFindSystemApplicationType,
                                            logString);
            throw e;
        }

        appInstanceList = AppInstanceManager.instance().find(host.id(), appType.id());

        if (appInstanceList.size() > 0) {
            systemAppInstance = (AppInstance) appInstanceList.get(0);
        } else {
            EmanagerEventException e;
            String logString;

            logString = EmanagerEventStatusCode.UnableToFindSystemApplicationInstance.getStatusCodeDescription() +
                        " (" + InventoryGlobals.sysAppInstanceName() + ")";

            logger.error (logString);
            syncReport.setStatus(SyncStatus.Warnings);
            syncReport.addLogEntry(logString);
            e = new EmanagerEventException (EmanagerEventStatusCode.UnableToFindSystemApplicationInstance,
                                            logString);
            throw e;
        }

        rulebaseEngineMicroagent =
            systemAppInstance.instrumentation(InventoryGlobals.sysRuleBaseEngineInstrumentationName());
        if (rulebaseEngineMicroagent == null) {
            EmanagerEventException e;
            String logString;

            logString = EmanagerEventStatusCode.UnableToFindSystemRulebaseMicroagent.getStatusCodeDescription() +
                        " (" + InventoryGlobals.sysRuleBaseEngineInstrumentationName() + ")";

            logger.error (logString);
            syncReport.setStatus(SyncStatus.Warnings);
            syncReport.addLogEntry(logString);
            logger.error (logString);
            e = new EmanagerEventException (EmanagerEventStatusCode.UnableToFindSystemRulebaseMicroagent,
                                            logString);
            throw e;
        }

        loadManagementPolicyMethod = rulebaseEngineMicroagent.method("loadRuleBaseFromFile");
        if (loadManagementPolicyMethod == null) {
            EmanagerEventException e;
            String logString;

            logString = EmanagerEventStatusCode.UnableToFindRulebaseMicroagentRulebaseLoadMethod.getStatusCodeDescription() +
                        "loadRuleBaseFromFile";

            logger.error (logString);
            syncReport.setStatus(SyncStatus.Warnings);
            syncReport.addLogEntry(logString);
            e = new EmanagerEventException (EmanagerEventStatusCode.UnableToFindRulebaseMicroagentRulebaseLoadMethod,
                                            logString);
            throw e;
        }

        emanagerPolicyList = host.mgmtPolicies();
        ruleBaseStatus = host.agentInstance().getStatusRuleBases();
        iter = emanagerPolicyList.iterator();
        while (iter.hasNext()) {
            mgmtPolicy = (MgmtPolicy) iter.next();

            // Check to see if the mgmt policy is loaded
            policyLoaded = false;
            for (int x = 0; x < ruleBaseStatus.length; x++) {
                if (ruleBaseStatus[x].getName().equals (mgmtPolicy.name())) {
                    policyLoaded = true;
                    break;
                }
            }

            MgmtPolicyManager.instance().setLoaded(mgmtPolicy, policyLoaded);

            if (policyLoaded == false &&
                mgmtPolicy.pathname() != null &&
                mgmtPolicy.pathname().trim().length() != 0) {

                // We need to make sure that the associated Application Type has
                // managed apps under it.
                appType = mgmtPolicy.appType();
                appInstances = AppInstanceManager.instance().find(host.id(), appType.id());

                managedInstanceFound = false;
                iter2 = appInstances.iterator();
                while (iter2.hasNext()) {
                    appInstance = (AppInstance) iter2.next();
                    if (appInstance.managed() == true) {
                        managedInstanceFound = true;
                        break;
                    }
                }

                if (managedInstanceFound == true) {
                    DataElement args[] = {new DataElement("File", mgmtPolicy.pathname().trim())};
                    MethodInvocation methodInvocation;

                    methodInvocation =
                        new MethodInvocation(loadManagementPolicyMethod.name(),
                                             args);

                    try{
                        String logString;
                        host.agentManager().invoke(rulebaseEngineMicroagent.networkId().raw(),
                            methodInvocation);

                        logString = this.workerIdentifier() +
                                    "Loaded ManagementPolicy " +
                                    mgmtPolicy.name();
                        logger.info (logString);
                    }
                    catch (MicroAgentException e){
                        String logString;

                        logString = "MicroAgentException encountered error loading managementpolicy:" +
                                    mgmtPolicy.name() +
                                    " at " +
                                    mgmtPolicy.pathname() +
                                    ": " +
                                    e.getMessage();

                        logger.error (logString);
                        syncReport.setStatus(SyncStatus.Warnings);
                        syncReport.addLogEntry(logString);
                    }
                }
            }
        }
    }

    private int getAgentEventState()
    {
        AgentInstance agentInstance;

        syncState = "Retrieving the current agent event state";

        agentInstance = host.agentInstance();
        return agentInstance.getRuleBaseEngineState();
    }

    private void retransmitAgentEvents()
    {
        AgentInstance agentInstance;
        long alertIds[] = {};

        syncState = "Issuing agent retransmission request";

        agentInstance = host.agentInstance();
        agentRetransmitTime = new Date (System.currentTimeMillis());
        agentInstance.retransmitAlerts(alertIds);
    }

    /**
     * @roseuid 3F26825800A6
     */
    private void reconcileEvents()
    {
        Date   latestAgentEvent;
        Date   earliestMissingEvent;
        Object object;
        Iterator iter;
        Iterator iter2;
        List agentEvents;
        Collection cacheEvents;
        List cacheReconcileEventList;
        List agentReconcileEventList;
        List newAgentEvents;
        AbstractEventMessage abstractEventMessage;
        TibcoEventMessage tibcoEventMessage;
        TibcoEventDetails tibcoEventDetails;
        ReconcileMessageEntry reconcileMessageEntry;
        ReconcileDetailsEntry reconcileDetailsEntry;

        logger.debug ("Enter");

        syncState = "Reconciling events";

        cacheReconcileEventList = new LinkedList();
        agentReconcileEventList = new LinkedList();
        newAgentEvents = new LinkedList();
        agentEvents = messageQueue.getBufferedRetransmittedEvents();
        cacheEvents = EventRepository.instance().getOutstandingHostAgentEvents(syncMessage.getHostAgentObjectId());

        iter = cacheEvents.iterator();
        while (iter.hasNext()) {
            object = iter.next();
            if (object instanceof TibcoEventDetails) {
                cacheReconcileEventList.add(new ReconcileDetailsEntry ( (TibcoEventDetails) object));
            }
        }

        iter = agentEvents.iterator();
        while (iter.hasNext()) {
            agentReconcileEventList.add(new ReconcileMessageEntry ((AbstractEventMessage) iter.next()));
        }

        latestAgentEvent = null;
        earliestMissingEvent = null;
        iter = agentReconcileEventList.iterator();
        while (iter.hasNext()) {
            reconcileMessageEntry = (ReconcileMessageEntry) iter.next();
            if (reconcileMessageEntry.getEvent() instanceof TibcoEventMessage) {
                tibcoEventMessage = (TibcoEventMessage) reconcileMessageEntry.getEvent();

                if (latestAgentEvent == null ||
                    latestAgentEvent.before(tibcoEventMessage.getEventTime()) ) {
                    latestAgentEvent = tibcoEventMessage.getEventTime();
                }

                iter2 = cacheReconcileEventList.iterator();
                while (iter2.hasNext())
                {
                    reconcileDetailsEntry = (ReconcileDetailsEntry)iter2.next();
                    if (reconcileDetailsEntry.getEvent() instanceof TibcoEventDetails) {
                        tibcoEventDetails = (TibcoEventDetails) reconcileDetailsEntry.getEvent();
                        if (tibcoEventMessage.getTibcoEventId() == tibcoEventDetails.getTibcoEventId()) {
                            reconcileMessageEntry.setFlag(true);
                            reconcileDetailsEntry.setFlag(true);
                        }
                    }
                }
            }
        }

        if (agentReconcileEventList.size() != 0) {
            iter = agentReconcileEventList.iterator();
            while (iter.hasNext()) {
                reconcileMessageEntry = (ReconcileMessageEntry) iter.next();
                if (reconcileMessageEntry.getFlag() == false) {
                    // Need to post these events
                    abstractEventMessage = reconcileMessageEntry.getEvent();
                    abstractEventMessage.setEventType(EventType.PostType);
                    abstractEventMessage.setSyncGenerated(true);
                    syncReport.addEvent(abstractEventMessage);
                    if (earliestMissingEvent == null ||
                        earliestMissingEvent.after(abstractEventMessage.getEventTime()) ) {
                        earliestMissingEvent = abstractEventMessage.getEventTime();
                    }

                    if (reconcileMessageEntry.getEvent() instanceof TibcoEventMessage) {
                        logger.info ("Reconciled tibco event.  Added Tibco event id - " +
                                     ((TibcoEventMessage)abstractEventMessage).getTibcoEventId());
                    }
                }
            }
        }

        iter = cacheReconcileEventList.iterator();
        while (iter.hasNext()) {
            reconcileDetailsEntry = (ReconcileDetailsEntry)iter.next();
            if (reconcileDetailsEntry.getFlag() == false) {
                abstractEventMessage = null;
                if (reconcileDetailsEntry.getEvent() instanceof TibcoEventDetails) {
                    tibcoEventDetails = (TibcoEventDetails)reconcileDetailsEntry.getEvent();

                    try{
                        abstractEventMessage = new TibcoEventMessage(tibcoEventDetails);
                    }
                    catch (EmanagerEventException e){
                        String logString;

                        logString = "Error encountered transforming event details " +
                                    "to TibcoEventMessage. " +
                                    e.getMessage();

                        logger.error(logString);
                        syncReport.setStatus(SyncStatus.Warnings);
                        syncReport.addLogEntry(logString);
                        continue;
                    }

                    logger.info ("Reconciled tibco event.  Cleared Tibco event id - " +
                                 ((TibcoEventMessage)abstractEventMessage).getTibcoEventId());
                }

                abstractEventMessage.setEventType(EventType.ClearType);
                abstractEventMessage.setEventTime(latestAgentEvent);
                abstractEventMessage.setSyncGenerated(true);
                syncReport.addEvent(abstractEventMessage);
            }
        }

        if (latestAgentEvent != null) {
            messageQueue.removeOldTibcoEventMesssages(latestAgentEvent);
        }

        /*
        if (earliestMissingEvent != null) {
            messageQueue.setLastEventGenerationTime(earliestMissingEvent);
        } else if (latestAgentEvent != null) {
            messageQueue.setLastEventGenerationTime(latestAgentEvent);
        }
*/
        if (latestAgentEvent != null) {
            messageQueue.setLastEventGenerationTime(latestAgentEvent);
        } else {
            messageQueue.setLastEventGenerationTime(agentRetransmitTime);
        }

        iter = syncReport.getEvents().iterator();
        while (iter.hasNext()) {
            messageQueue.queueMessage((AbstractEventMessage) iter.next());
        }
    }

    /**
     * @param listener
     * @roseuid 3F272F30008B
     */
    public static void addListener(SyncProcessingWorkerListener listener)
    {
        synchronized (listeners) {
            if (listeners.contains(listener) == false) {
                listeners.add(listener);
            }
        }
    }

    /**
     * @param listener
     * @roseuid 3F272F420326
     */
    public static void removeListener(SyncProcessingWorkerListener listener)
    {
        synchronized (listeners) {
            listeners.remove (listener);
        }
    }

    private void notifyListenersSyncStarted (SyncMessage syncMessage)
    {
	Iterator iter;
	SyncProcessingWorkerListener listener;

	synchronized (listeners) {
	    iter = listeners.iterator();
	    while (iter.hasNext()) {
		listener = (SyncProcessingWorkerListener) iter.next();
		listener.eventSyncProcessingWorkerSyncStarted (this, syncMessage);
	    }
	}
    }

    private void notifyListenersSyncEnded (SyncReport syncReport)
    {
	Iterator iter;
	SyncProcessingWorkerListener listener;

	synchronized (listeners) {
	    iter = listeners.iterator();
	    while (iter.hasNext()) {
		listener = (SyncProcessingWorkerListener) iter.next();
		listener.eventSyncProcessingWorkerSyncEnded (this, syncReport);
	    }
	}
    }

    public void notifyStateChangeListeners()
    {
        // noop
    }

    // only called in one case. to stop queue in step above.
    // Assume only one host queue registered at a time.
    public void hostEventMessageQueueStateChanged (HostEventMessageQueue messageQueue)
    {
        if (messageQueue.getCurrentState() == State.Stop) {
            synchronized (workerSleepLock) {
                workerSleepLock.notifyAll();
            }
        }
    }

    public String toString()
    {
        StringBuffer strBuf;

        strBuf = new StringBuffer();

        strBuf.append ("Thread: " + syncProcessThread.getName());
        strBuf.append (" - SyncMessage: "  + syncMessage);
        strBuf.append (" - Desired State: " + this.getDesiredState());
        strBuf.append (" - Current State: " + this.getCurrentState());
        strBuf.append (" - Description: " + syncState);
        return strBuf.toString();
    }

    public static String getString()
    {
        Iterator iter;
        StringBuffer strBuf;
        SyncProcessingWorker worker;

        strBuf = new StringBuffer();

        strBuf.append ("Host Agent Message Queue Stop Timeout: " + hostAgentMessageQueueStopTimeout +
                       GlobalProperties.CarriageReturn);
        strBuf.append ("Tibco Agent Event Retransmit Timeout: " + getTibcoAgentEventRetransmitTimeout() +
                       GlobalProperties.CarriageReturn);
        strBuf.append ("Management Policy Sync Loading Delay: " + managementPolicySyncLoadingDelay +
                       GlobalProperties.CarriageReturn);

        strBuf.append (GlobalProperties.CarriageReturn);
        strBuf.append ("Sync Workers" + GlobalProperties.CarriageReturn);
        strBuf.append ("------------" + GlobalProperties.CarriageReturn);

        synchronized (workers) {
            iter = workers.iterator();
            while (iter.hasNext()) {
                worker = (SyncProcessingWorker)iter.next();
                strBuf.append ("    " + worker.toString());
                strBuf.append (GlobalProperties.CarriageReturn);
            }
        }

        return strBuf.toString();
    }

    private abstract class ReconcileEntry
    {
        private boolean flag;

        public  ReconcileEntry()
        {
            flag = false;
        }

        public boolean getFlag()
        {
            return flag;
        }

        public void setFlag (boolean flag)
        {
            this.flag = flag;
        }
    }

    private class ReconcileDetailsEntry extends ReconcileEntry
    {
        private EmanagerEventDetails event;

        public ReconcileDetailsEntry (EmanagerEventDetails event)
        {
            this.event = event;
        }

        public EmanagerEventDetails getEvent()
        {
            return event;
        }
    }

    private class ReconcileMessageEntry extends ReconcileEntry
    {
        private AbstractEventMessage event;

        public ReconcileMessageEntry (AbstractEventMessage event)
        {
            this.event = event;
        }

        public AbstractEventMessage getEvent()
        {
            return event;
        }
    }

    private void startDebuggerThread()
    {
        Thread t;
        ThreadGroup parentThreadGroup;

        parentThreadGroup = EventGlobals.instance().getEventThreadGroup();

        debugger = new EventDebugger();
        if (parentThreadGroup == null) {
            t = new Thread (debugger, "Sync Worker Debugger");
        } else {
            t = new Thread (parentThreadGroup, debugger, "Sync Worker Debugger");
        }

        t.start();
    }

    private class EventDebugger extends JFrame implements Runnable, ActionListener
    {
	private Logger logger = Logger.getLogger(EventDebugger.class);

        JTextArea textArea;

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
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Sync Workers"),
                BorderFactory.createEmptyBorder(5,5,5,5)),
                areaScrollPane.getBorder()));

            debuggerPanel.add(areaScrollPane, BorderLayout.CENTER);

            EventManager.instance().getEventDebuggerPane().add("Sync Workers", debuggerPanel);

            frame.pack();

	    try {
		frame.setVisible(true);
	    }
	    catch (Exception e) {
		logger.warn ("Unexpectedly unable to open the Sync Processing Worker debugger frame: " + e);
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
            textArea.setText(SyncProcessingWorker.getString());
        }
    }

}

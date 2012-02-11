package com.cisco.eManager.eManager.event;

import org.apache.log4j.*;

public class EventGlobals {
    private static Logger      logger = Logger.getLogger(EventGlobals.class);

    static public String NumberEventProcessingWorkersKey = "number.event.processing.workers";
    static public int   NumberEventProcessingWorkersDefault = 1;
    static public String NumberSyncProcessingWorkersKey = "number.sync.processing.workers";
    static public int   NumberSyncProcessingWorkersDefault = 1;
    static public String ManagementPolicyDeactivationTimeoutKey = "event.management.policy.deactivation.timeout";
    static public long ManagementPolicyDeactivationTimeoutDefault = 10000;
    static public String ManagementPolicySyncLoadingDelayKey = "event.management.policy.sync.loading.delay";
    static public long ManagementPolicySyncLoadingDelayDefault = 15000;
    static public String HostAgentMessageQueueStopTimeoutKey = "event.host.agent.message.queue.stop.timeout";
    static public long HostAgentMessageQueueStopTimeoutDefault = 15000;
    static public String HostAgentMessageQueueStopTimeoutValueMsg = "The HostAgentMessageQueueStopTimeoutValue is: ";
    static public String TibcoAgentEventRetransmitTimeoutKey = "sync.tibco.agent.event.retransmit.timeout";
    static public long TibcoAgentEventRetransmitTimeoutDefault = 60000;
    static public String DisplayEventDebuggerFrames = "display.event.debugger.frames";

    private static EventGlobals eventGlobals;
    private static ThreadGroup eventThreadGroup;

    private static String EventThreadGroupName = "Event Threads";

    private EventGlobals ()
    {
	try {
	    eventThreadGroup = new ThreadGroup (EventThreadGroupName);
	}
	catch (SecurityException e) {
	    logger.error ("Unexpectedly unable to create the event related thread groups.");

            eventThreadGroup = null;
        }
    }

    public static synchronized EventGlobals instance()
    {
	if (eventGlobals == null) {
	    eventGlobals = new EventGlobals();
	}

	return eventGlobals;
    }

    /**
     * Access method for the eventThreadGroup property.
     *
     * @return   the current value of the eventThreadGroup property
     */
    public ThreadGroup getEventThreadGroup()
    {
	return eventThreadGroup;
    }
}

package com.cisco.eManager.eManager.event;

import org.apache.log4j.*;

import java.util.Properties;

import com.cisco.eManager.common.inventory.ManagedObjectId;

import com.cisco.eManager.eManager.event.State;
import com.cisco.eManager.eManager.event.HostEventMessageQueueListener;
import com.cisco.eManager.eManager.event.EventMessageQueue;
import com.cisco.eManager.eManager.event.HostEventMessageQueue;
import com.cisco.eManager.eManager.event.EventCache;
import com.cisco.eManager.eManager.event.EventGlobals;
import com.cisco.eManager.eManager.event.EventManager;

import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager;

import com.cisco.eManager.eManager.util.GlobalProperties;

public class AppInstanceRemovalWorker extends AbstractWorker implements HostEventMessageQueueListener, Runnable
{
    private static Logger      logger = Logger.getLogger(AppInstanceRemovalWorker.class);

    private boolean               appInstanceDeletion;
    private Boolean               workerSleepLock;
    private AppInstance           appInstance;
    private ManagedObjectId       appInstanceId;
    private HostEventMessageQueue hostMsgQueue;
    private static long        hostAgentMessageQueueStopTimeout;
    Thread                        workerThread;

    // Constants
    private final String ThreadName = "App Instance Removal Worker - ";

    public AppInstanceRemovalWorker (boolean appInstanceDeletion,
				     AppInstance appInstance)
    {
        String propertyValue;
        Properties systemProperties;

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

	workerSleepLock = new Boolean(true);
	this.appInstanceDeletion = appInstanceDeletion;
	this.appInstance = appInstance;
	this.appInstanceId = appInstance.id();
	setDesiredState (State.Processing);
	setCurrentState (State.Processing);
    }

    public ManagedObjectId getApplicationInstanceId()
    {
	return appInstanceId;
    }

    private void stopHostEventMessageQueue()
    {
	synchronized (workerSleepLock) {
	    hostMsgQueue.addHostEventMessageQueueListener (this);
	    hostMsgQueue.setDesiredState (State.Stop);
	    try {
		workerSleepLock.wait (hostAgentMessageQueueStopTimeout);
	    }
	    catch (InterruptedException e) {
	    }

	    hostMsgQueue.removeHostEventMessageQueueListener (this);

	    if (hostMsgQueue.getCurrentState() != State.Stop) {
		logger.error ("Unexpectedly unable to stop the host event messsage queue: " +
			      appInstance.hostId());
		logger.error ("Will continue to try to remove the application instance from the error processing system: " +
			      appInstanceId);
	    }
	}
    }

    private synchronized void finishProcessing()
    {
	if (getStopWorker() != true) {
	    if (appInstanceDeletion == true) {
		EventMessageQueue.instance().deleteApplicationInstance (appInstance);
	    } else {
		EventMessageQueue.instance().removeApplicationInstanceEvents (appInstance);
	    }

	    hostMsgQueue.setDesiredState (State.Idle);
	    setCurrentState (State.Idle);
	    EventManager.instance().clearApplicationInstanceOutstandingEvents (appInstanceId);
            if (appInstanceDeletion == true) {
                EventCache.instance().deleteApplicationInstanceCache(appInstance);
            }
	} else {
	    logger.debug ("Application instance removal aborted.");
	}
    }

    // this must be synchronized because the manage application code
    // requires it.  We must synchronize with the
    // finish processing routine above.
    public synchronized boolean stopAppInstanceWorker(ManagedObjectId appInstanceId)
    {
	if (appInstanceId.equals (this.appInstanceId)) {
	    setStopWorker (true);
            return true;
	}

        return false;
    }

    public void hostEventMessageQueueStateChanged (HostEventMessageQueue messageQueue)
    {
        if (messageQueue.getCurrentState() == State.Stop) {
            synchronized (workerSleepLock) {
                workerSleepLock.notifyAll();
            }
        }
    }

    public void notifyStateChangeListeners()
    {
        // noop
    }

    protected Thread getThread()
    {
	return workerThread;
    }

    public void run()
    {
	logger.debug ("Enter");

        try {

            // We can just let the running synchronizations complete.
            // Any reconciled events will be placed on the event message queue
            // and when they see that the app is unmanaged, they will be dropped.

            workerThread = Thread.currentThread();
            workerThread.setName (ThreadName + appInstanceId);

            // We need to stop the host message queue.
            // Remove app instance events.
            // restart the host message queue.
            // Clear any oustanding events.

            hostMsgQueue = EventMessageQueue.instance().getAgentEventMessageQueue (appInstance.hostId());
            if (hostMsgQueue != null) {
                stopHostEventMessageQueue();

                finishProcessing();
            } else {
                logger.error ("Unexpectedly unable to find host event message queue " +
                              appInstance.hostId() +
                              " associated with application instance " +
                              appInstanceId +
                              ".  Unable to clean up associated events.");
            }
        }
        catch (Exception e) {

        }

        EventManager.instance().removeAppInstanceRemovalWorker(this);
	logger.debug ("Exit");
    }
}

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\event\\EventProcessingWorkerListener.java

package com.cisco.eManager.eManager.event;

import com.cisco.eManager.common.event.AbstractEventMessage;
import com.cisco.eManager.eManager.event.EventProcessingWorker;

public interface EventProcessingWorkerListener
{

    /**
     * @roseuid 3F2683B70244
     */
    public void eventProcessingWorkerStateChanged(EventProcessingWorker worker);

    /**
     * @param event
     * @roseuid 3F2E836C000B
     */
    public void eventProcessingComplete(AbstractEventMessage event);
}

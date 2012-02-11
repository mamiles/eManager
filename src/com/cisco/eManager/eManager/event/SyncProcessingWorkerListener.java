//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\event\\SyncWorkerListener.java

package com.cisco.eManager.eManager.event;

import com.cisco.eManager.eManager.event.SyncReport;
import com.cisco.eManager.common.event.SyncMessage;

public interface SyncProcessingWorkerListener
{

    /**
     * @roseuid 3F272F8201AB
     */
    public void eventSyncProcessingWorkerSyncStarted(SyncProcessingWorker worker, SyncMessage syncMessage);

    /**
     * @param eventSyncReport
     * @roseuid 3F27301C012B
     */
    public void eventSyncProcessingWorkerSyncEnded(SyncProcessingWorker worker, SyncReport SyncReport);
}

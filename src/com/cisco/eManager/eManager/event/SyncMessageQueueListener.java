//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\event\\SyncMessageQueueListener.java

package com.cisco.eManager.eManager.event;

import com.cisco.eManager.common.event.SyncMessage;

public interface SyncMessageQueueListener
{

    /**
     * @roseuid 3F21A0240196
     */
    public void eventSyncMessageQueueMessageAdded(SyncMessage syncMessage);

    /**
     * @param eventSyncReport
     * @roseuid 3F27307A0393
     */
    public void eventSyncMessageQueueSyncCompleted(SyncReport syncReport);

    /**
     * @roseuid 3F2730B7025A
     */
    public void eventSyncMessageQueueSyncStarted(SyncMessage syncMessage);
}

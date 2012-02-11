package com.cisco.eManager.eManager.event;

import com.cisco.eManager.eManager.event.HostEventMessageQueue;

public interface HostEventMessageQueueListener
{

    /**
     * @param hostEventMessageQueue
     */
    public void hostEventMessageQueueStateChanged(HostEventMessageQueue hostEventMessageQueue);
}

package com.cisco.eManager.eManager.network;

import com.tibco.tibrv.*;

public interface ClientTibcoMessageQueueListener
{
    public void clientTibcoMessageAdded(TibrvMsg message);
}

package com.cisco.eManager.eManager.network;

import com.tibco.tibrv.*;

public interface ClientTibcoMessageWorkerListener
{
    public void clientTibcoMessageProcessingComplete(TibrvMsg message);
}

//Source file: /vob/emanager/src/com/cisco/eManager/eManager/event/EventMessageQueueListener.java

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\event\\EventMessageQueueListener.java

package com.cisco.eManager.eManager.event;

import com.cisco.eManager.eManager.*;
import com.cisco.eManager.common.inventory.ManagedObjectId;

public interface EventMessageQueueListener
{

    /**
     * The following function will notify listeners when a new host event queue
     * is added.
     * @roseuid 3F215A3D0257
     */
    public void hostEventMessageQueueAdded(HostEventMessageQueue hostEventMessageQueue);

    /**
     * The following function will notify listeners when a new host event queue
     * is deleted.
     * @roseuid 3F2196820092
     */
    public void hostEventMessageQueueDeleted(ManagedObjectId hostAgentId);
}

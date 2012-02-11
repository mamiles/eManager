//Source file: /vob/emanager/src/com/cisco/eManager/common/event/SyncMessage.java

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\common\\event\\SyncMessage.java

package com.cisco.eManager.common.event;

import com.cisco.eManager.common.event.SynchronizationPriority;
import com.cisco.eManager.common.inventory.ManagedObjectId;

public class SyncMessage
{
    private ManagedObjectId hostAgentObjectId;
    private SynchronizationPriority priority;
    private boolean syncManagementPolicies;

    // fix
    // Workaround for SOAP
    public SyncMessage()
    {

    }

    /**
     * @param hostAgentbjectId
     * @param priority
     * @param syncManagementPolicies
     * @roseuid 3F4E5D500351
     */
    public SyncMessage(ManagedObjectId hostAgentObjectId,
		       SynchronizationPriority priority,
		       boolean syncManagementPolicies)
    {
	this.hostAgentObjectId = hostAgentObjectId;
	this.priority = priority;
	this.syncManagementPolicies = syncManagementPolicies;
    }

    /**
     * Access method for the hostAgentObjectId property.
     *
     * @return   the current value of the hostAgentObjectId property
     */
    public ManagedObjectId getHostAgentObjectId()
    {
        return hostAgentObjectId;
    }

    /**
     * Access method for the syncManagementPolicies property.
     *
     * @return   the current value of the syncManagementPolicies property
     */
    public boolean  getSyncManagementPolicies()
    {
        return syncManagementPolicies;
    }

    /**
     * Access method to set the syncManagementPolicies property.
     *
     * @param syncManagementPolicies
     */
    public void setSyncManagementPolicies(boolean syncManagementPolicies)
    {
        this.syncManagementPolicies = syncManagementPolicies;
    }

    /**
     * Access method for the priority property.
     *
     * @return   the current value of the priority property
     */
    public SynchronizationPriority getPriority()
    {
        return priority;
    }

    public String toString ()
    {
        return "Host:" + hostAgentObjectId + " Priority:" + priority + " SyncPolicies:" + syncManagementPolicies;
    }
}

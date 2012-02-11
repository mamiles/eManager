package com.cisco.eManager.eManager.inventory;

import java.util.Collection;
import com.cisco.eManager.common.inventory.ManagedObjectId;

public interface InventoryNotificationSenderInterface
{
    // Application Instance Interface
    public void appInstanceCreated (ManagedObjectId appInstanceId);
    public void appInstanceRestored (ManagedObjectId appInstanceId);
    public void appInstanceManaged (ManagedObjectId appInstanceId);
    public void appInstanceUnmanaged (ManagedObjectId appInstanceId);
    public void appInstanceDeleted (ManagedObjectId appInstanceId);
    public void appInstancesConsolidated (Collection newAppInstanceIds,
					  Collection deletedAppInstanceIds);

    // Application Type Interface
    public void appTypeCreated (ManagedObjectId appInstanceId);
    public void appTypeRestored (ManagedObjectId appInstanceId);
    public void appTypeDeleted (ManagedObjectId appInstanceId);

    // Host Interface
    public void hostCreated (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void hostRestored (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void hostDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id);

    // Instrumentation Interface
    public void instrumentationCreated (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void instrumentationRestored (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void instrumentationDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id);

    // MgmtPolicy Interface
    public void mgmtPolicyCreated (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void mgmtPolicyRestored (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void mgmtPolicyDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id);

    // Solution Interface
    public void solutionCreated (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void solutionRestored (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void solutionDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id);

    // App View Interface
    public void applicationViewCreated (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void applicationViewRestored (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void applicationViewDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void applicationViewMoved (com.cisco.eManager.common.inventory.ManagedObjectId nodeId,
				      com.cisco.eManager.common.inventory.ManagedObjectId oldParentId,
				      com.cisco.eManager.common.inventory.ManagedObjectId newParentId);

    // Phys View Interface
    public void physicalViewCreated (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void physicalViewRestored (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void physicalViewDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void physicalViewMoved (com.cisco.eManager.common.inventory.ManagedObjectId nodeId,
				   com.cisco.eManager.common.inventory.ManagedObjectId oldParentId,
				   com.cisco.eManager.common.inventory.ManagedObjectId newParentId);

    // Soln View Interface
    public void solutionViewCreated (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void solutionViewRestored (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void solutionViewDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id);
    public void solutionViewMoved (com.cisco.eManager.common.inventory.ManagedObjectId nodeId,
				   com.cisco.eManager.common.inventory.ManagedObjectId oldParentId,
				   com.cisco.eManager.common.inventory.ManagedObjectId newParentId);
}

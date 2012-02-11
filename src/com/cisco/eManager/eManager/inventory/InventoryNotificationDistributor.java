//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\InventoryNotificationDistributor.java

package com.cisco.eManager.eManager.inventory;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.appType.*;
import com.cisco.eManager.eManager.inventory.host.*;
import com.cisco.eManager.eManager.inventory.instrumentation.*;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.*;
import com.cisco.eManager.eManager.inventory.view.*;

public class InventoryNotificationDistributor
    implements InventoryNotificationSenderInterface
{
    private static InventoryNotificationDistributor s_instance = null;
    private static Logger logger =
        Logger.getLogger(InventoryNotificationDistributor.class);
    private Collection m_senders;

    public static synchronized InventoryNotificationDistributor instance()
    {
        if ( s_instance == null )
        {
            s_instance = new InventoryNotificationDistributor();
        }
        return s_instance;
    }

    private InventoryNotificationDistributor()
    {
        logger.debug("enter");

        m_senders = new LinkedList();
    }

    public void attach(InventoryNotificationSenderInterface sender)
    {
        m_senders.add(sender);
    }

    public void detach(InventoryNotificationSenderInterface sender)
    {
        m_senders.remove(sender);
    }

    public void appInstanceCreated(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.appInstanceCreated(appInstanceId);
        }
    }

    public void appInstanceRestored(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.appInstanceRestored(appInstanceId);
        }
    }

    public void appInstanceManaged(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.appInstanceManaged(appInstanceId);
        }
    }

    public void appInstanceUnmanaged(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.appInstanceUnmanaged(appInstanceId);
        }
    }

    public void appInstanceDeleted(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.appInstanceDeleted(appInstanceId);
        }
    }

    public void appInstancesConsolidated(Collection newAppInstanceIds,
                                         Collection deletedAppInstanceIds)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.appInstancesConsolidated(newAppInstanceIds,
                                         deletedAppInstanceIds);
        }
    }

    // Application Type Interface
    public void appTypeCreated(ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.appTypeCreated(appTypeId);
        }
    }

    public void appTypeRestored(ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.appTypeRestored(appTypeId);
        }
    }

    public void appTypeDeleted(ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.appTypeDeleted(appTypeId);
        }
    }

    // Host Interface
    public void hostCreated(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.hostCreated(id);
        }
    }

    public void hostRestored(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.hostRestored(id);
        }
    }

    public void hostDeleted(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.hostDeleted(id);
        }
    }

    // Instrumentation Interface
    public void instrumentationCreated(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.instrumentationCreated(id);
        }
    }

    public void instrumentationRestored(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.instrumentationRestored(id);
        }
    }

    public void instrumentationDeleted(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.instrumentationDeleted(id);
        }
    }

    // MgmtPolicy Interface
    public void mgmtPolicyCreated(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.mgmtPolicyCreated(id);
        }
    }

    public void mgmtPolicyRestored(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.mgmtPolicyRestored(id);
        }
    }

    public void mgmtPolicyDeleted(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.mgmtPolicyDeleted(id);
        }
    }

    // Solution Interface
    public void solutionCreated(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.solutionCreated(id);
        }
    }

    public void solutionRestored(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.solutionRestored(id);
        }
    }

    public void solutionDeleted(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.solutionDeleted(id);
        }
    }

    // App View Interface
    public void applicationViewCreated(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.applicationViewCreated(id);
        }
    }

    public void applicationViewRestored(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.applicationViewRestored(id);
        }
    }

    public void applicationViewDeleted(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.applicationViewDeleted(id);
        }
    }

    public void applicationViewMoved(ManagedObjectId nodeId,
                                     ManagedObjectId oldParentId,
                                     ManagedObjectId newParentId)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.applicationViewMoved(nodeId, oldParentId, newParentId);
        }
    }

    // Phys View Interface
    public void physicalViewCreated(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.physicalViewCreated(id);
        }
    }

    public void physicalViewRestored(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.physicalViewRestored(id);
        }
    }

    public void physicalViewDeleted(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.physicalViewDeleted(id);
        }
    }

    public void physicalViewMoved(ManagedObjectId nodeId,
                                  ManagedObjectId oldParentId,
                                  ManagedObjectId newParentId)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.physicalViewMoved(nodeId, oldParentId, newParentId);
        }
    }

    // Soln View Interface
    public void solutionViewCreated(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.solutionViewCreated(id);
        }
    }

    public void solutionViewRestored(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.solutionViewRestored(id);
        }
    }

    public void solutionViewDeleted(ManagedObjectId id)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.solutionViewDeleted(id);
        }
    }

    public void solutionViewMoved(ManagedObjectId nodeId,
                                  ManagedObjectId oldParentId,
                                  ManagedObjectId newParentId)
    {
        logger.debug("enter");
        Iterator iter = m_senders.iterator();
        InventoryNotificationSenderInterface ins = null;
        while (iter.hasNext())
        {
            ins = (InventoryNotificationSenderInterface)iter.next();
            ins.solutionViewMoved(nodeId, oldParentId, newParentId);
        }
    }
}

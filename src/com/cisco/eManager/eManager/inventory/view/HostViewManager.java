//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\HostViewManager.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.database.EmanagerDatabaseException;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.eManager.database.DatabaseInterface;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.host.*;

public class HostViewManager
    extends ViewManager
{
    private static Logger logger = Logger.getLogger(HostViewManager.class);
    private static HostViewManager s_instance = null;
    private HostManager m_hm;
    private AppInstanceManager m_aim;

    /**
     * @return com.cisco.eManager.eManager.inventory.view.HostViewManager
     * @roseuid 3F0AE6010256
     */
    public static synchronized HostViewManager instance()
        throws Exception
    {
        if (s_instance == null)
        {
            s_instance = new HostViewManager();
        }
        return s_instance;
    }

    /**
     * @roseuid 3F4D08F30308
     */
    private HostViewManager()
        throws Exception
    {
        super("HostView");
        logger.debug("enter");
        m_hm = HostManager.instance();
        m_aim = AppInstanceManager.instance();
        initialize();
    }

    protected final ContainerNodeData
        createContainerNodeData(String name, ManagedObjectId parentId)
    {
        ContainerNodeData nodeData =
            new ContainerNodeData(null, name, parentId);
        ManagedObjectId newNodeId = null;
        try
        {
            newNodeId = m_db.createPhysicalHierarchyNode(nodeData);
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught while creating containerNodeData: " +
                         ex);
        }

        if (newNodeId == null)
        {
            logger.error("unable to create a container node data");
            return null;
        }
        nodeData.id(newNodeId);
        return nodeData;
    }

    protected final void removeContainerNodeData(AbstractContainerNode acn)
    {
        logger.debug("removing db data for node " + acn.fdn());

        if ( acn instanceof ContainerNode )
        {
            try
            {
                m_db.removePhysicalHierarchyNode(acn.id());
            }
            catch (EmanagerDatabaseException ex)
            {
                logger.error("exception caught while removing " +
                             "containerNodeData: " + ex);
                return;
            }
        }
        else if ( acn instanceof HostNode )
        {
            Collection links = null;
            try
            {
                links =
                    m_db.retrievePhysicalHierarchyRelationshipPhysContainerNodes(acn.id());
            }
            catch (EmanagerDatabaseException ex1)
            {
                logger.error("exception caught while finding  " +
                             "host-to-parent links: " + ex1);
                return;
            }
            Iterator iter = links.iterator();
            PhysicalHierarchyRelationshipData phrd = null;
            ManagedObjectId nodeParentId = acn.parent().id();
            ManagedObjectId linkPhysicalHierarchyId = null;
            while ( iter.hasNext() )
            {
                phrd = (PhysicalHierarchyRelationshipData)iter.next();
                linkPhysicalHierarchyId = phrd.physicalHierarchyId();
                if ( linkPhysicalHierarchyId.equals(nodeParentId) )
                {
                    logger.debug("removing physicalHierarchyRelationshipData" +
                                 ": parentId = " +
                                 phrd.physicalHierarchyId().toString() +
                                 ", hostId = " + phrd.hostId().toString());
                    try
                    {
                        m_db.removePhysicalHierarchyRelationshipNode(phrd);
                    }
                    catch (EmanagerDatabaseException ex2)
                    {
                        logger.error("exception caught while removing  " +
                                     "host-to-parent link: " + ex2);
                        return;
                    }
                    break;
                }
            }
        }
    }

    /**
     * @param hostCreation
     * @roseuid 3F0AE622009B
     */
    synchronized void newHost(Host newHost)
    {
        logger.debug("enter");
        logger.info("creating a new host node for host " + newHost.name());
        addHostToView(newHost);
    }

    synchronized void activatedHost(Host activatedHost)
    {
        logger.debug("enter");
        logger.info("making sure activated host (" + activatedHost.name() +
                    ") is in host view");
        addHostToView(activatedHost);
    }

    synchronized void restoredHost(Host restoredHost)
    {
        logger.debug("enter");
        logger.info("making sure restored host (" + restoredHost.name() +
                    ") is in host view");
        addHostToView(restoredHost);
    }

    synchronized void deletedHost(Host deletedHost)
    {
        logger.debug("enter");
        logger.info("removing host (" + deletedHost.name() + ") from view");
    }

    synchronized void undeletedHost(Host undeletedHost)
    {
        logger.debug("enter");
        logger.info("adding host (" + undeletedHost.name() + ") back to view");
    }

    private synchronized void addHostToView(Host host)
    {
        logger.debug("enter");

        Node node = find(host.id());
        HostNode hostNode = null;
        if (node == null)
        {
            try
            {
                hostNode = createHostNode(host, m_discoveryNode);
            }
            catch (Exception e)
            {
                logger.error("exception caught while creating a new HostNode " +
                             "under the discovery node: e = " + e);
                return;
            }
        }
        else
        {
            hostNode = (HostNode)node;
        }
    }

    /**
     * @roseuid 3F0AE6390094
     */
    public synchronized void newAppInstance(AppInstanceCreation aic)
    {
        logger.debug("enter");
        AppInstance newAppInstance = aic.appInstance();
        logger.debug("handling creation of new appInstance " +
                     newAppInstance.name());
        Node appInstanceNode = find(newAppInstance.id());
        if (appInstanceNode != null)
        {
            logger.debug("appInstance already exists in the host view (node " +
                         appInstanceNode.fdn() + ")");
            return;
        }

        Host host = newAppInstance.host();
        Node parentNode = find(host.id());
        HostNode hostNode = null;
        if (parentNode == null)
        {
            // not sure if this can be reached, but we know what to do if it
            // is...
            // - create an host node
            // - hand a new appInstance node under it
            logger.warn("handling a new appInstance where the appInstance's " +
                        "associated host is not yet in the tree!");
            try
            {
                hostNode = createHostNode(host, m_discoveryNode);
            }
            catch (Exception e)
            {
                logger.error("exception caught while creating an app type " +
                             "node under the discovery node on behalf of " +
                             "the new app instance node: e = " + e);
                return;
            }
        }
        else
        {
            if (!(parentNode instanceof HostNode))
            {
                logger.error("new app instance's associated app type found " +
                             "associated under a node which is not of type " +
                             "HostNode");
                return;
            }
            hostNode = (HostNode)parentNode;
        }

        try
        {
            createAppInstanceNode(newAppInstance, hostNode);
        }
        catch (Exception e)
        {
            logger.error("exception caught while creating a new HostNode " +
                         "under the discovery node: e = " + e);
        }
    }

    /**
     * @roseuid 3F0AE644022B
     */
    public synchronized void delHost(Host deletedHost)
    {
        logger.debug("enter");
        logger.info("deleting host " + deletedHost.name());
        Node node = find(deletedHost.id());
        if (node == null)
        {
            logger.warn("unable to locate node for host");
            return;
        }
        logger.debug("found node in view: " + node.fdn());

        if (!(node instanceof HostNode))
        {
            logger.warn("associated node in view is not of type HostNode");
        }

        try
        {
            removeNode(node);
        }
        catch (Exception e)
        {
            logger.error("exception caught while attempting to remove node " +
                         "from view: " + e);
        }
    }

    private synchronized HostNode createHostNode(Host host,
                                                 ContainerNode parent)
        throws Exception
    {
        logger.debug("enter");
        logger.info("creating hostNode " + host.hostname() + " under parent " +
                    parent.fdn());

        // the host and the parent node (in this view, type ContainerNode)
        // already exist
        // - first create the relationship link between the two
        // - then create the HostNode for this new host

        PhysicalHierarchyRelationshipData phrd =
            new PhysicalHierarchyRelationshipData(host.id(), parent.id());
        logger.debug("creating physicalHierarchyRelationshipData" +
                     ": parentId = " + phrd.physicalHierarchyId().toString() +
                     ", hostId = " + phrd.hostId().toString());
        try
        {
            m_db.createPhysicalHierarchyRelationshipNode(phrd);
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught while creating host-to-parent " +
                         "link: " + ex);
            return null;
        }

        HostNode newHostNode = null;
        try
        {
            newHostNode = new HostNode(host, parent);
        }
        catch (Exception e)
        {
            logger.error("exception caught while creating a new host node: " +
                         e);
            throw e;
        }

        NodeNotification ntfcnObj =
            new NodeNotification(NodeNotificationType.CREATE, newHostNode);
        setChanged();
        notifyObservers(ntfcnObj);

        logger.info("hostNode created: " + newHostNode.fdn());

        return newHostNode;
    }

    private synchronized HostNode restoreHostNode(Host host,
                                                  ContainerNode parent)
    {
        logger.debug("enter");
        logger.info("creating hostNode " + host.hostname() + " under parent " +
                    parent.fdn());

        // the host and the parent node (in this view, type ContainerNode)
        // already exist
        // also, the db data (type PhysicalHierarchyRelationshipData) already
        // exists (hence the difference between this method and
        // createHostNode()...)

        HostNode hostNode = null;
        try
        {
            hostNode = new HostNode(host, parent);
        }
        catch (Exception e)
        {
            logger.error("exception caught while creating a new host node: " +
                         e);
        }

        if ( hostNode != null )
        {
            NodeNotification ntfcnObj =
                new NodeNotification(NodeNotificationType.CREATE, hostNode);
            setChanged();
            notifyObservers(ntfcnObj);

            logger.info("hostNode created: " + hostNode.fdn());
        }
        else
        {
            logger.warn("unable to restore hostNode from db");
        }

        return hostNode;
    }

    protected final Collection getContainerNodesFromDb()
    {
        logger.debug("enter");
        Collection nodes = null;
        try
        {
            nodes = m_db.retrievePhysicalHierarchyNodes();
        }
        catch (EmanagerDatabaseException e)
        {
            logger.error("exception caught while retrieving nodes from db: " +
                         e);
            return null;
        }
        return nodes;
    }

    protected final void finalizeViewRestoration()
    {
        logger.debug("enter");
        Collection links = null;
        try
        {
            links = m_db.retrievePhysicalHierarchyRelationshipNodes();
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught retrieving links from DB: " + ex);
        }

        Iterator iter = links.iterator();
        Node node = null;
        ContainerNode containerNode = null;
        Host host = null;
        HostNode hostNode = null;
        while (iter.hasNext())
        {
            PhysicalHierarchyRelationshipData linkData =
                (PhysicalHierarchyRelationshipData)iter.next();
            node = find(linkData.physicalHierarchyId());
            if ( node instanceof ContainerNode )
            {
                containerNode = (ContainerNode)node;
                host = m_hm.find(linkData.hostId());
                if ( host == null )
                {
                    logger.error("physicalHierarchyRelationshipData links to " +
                                 "host which cannot be found!");
                    logger.error("id = " + linkData.hostId().toString());
                }
                else
                {
                    hostNode = restoreHostNode(host, containerNode);

                    if ( hostNode != null )
                    {
                        // now flesh out appInstances under host node...
                        restoreAppInstances(hostNode);
                    }
                }
            }
            else
            {
                logger.error("physicalHierarchyRelationshipData links to " +
                             "non-container node in physical view!");
                logger.error("id = " +
                             linkData.physicalHierarchyId().toString());
            }
        }
    }

    private void restoreAppInstances(HostNode hostNode)
    {
        logger.debug("enter");
        if ( hostNode == null )
        {
            logger.warn("null host node");
            return;
        }

        List appInstances = hostNode.host().appInstances();
        Iterator iter = appInstances.iterator();
        AppInstance appInstance = null;
        while (iter.hasNext())
        {
            appInstance = (AppInstance)iter.next();
            try
            {
                createAppInstanceNode(appInstance, hostNode);
            }
            catch (Exception ex)
            {
                logger.error("exception caught while building out a host " +
                             "node's appInstance nodes: " + ex);
            }
        }
    }

    protected void moveNodeInDb(AbstractContainerNode newParent, Node node)
        throws Exception
    {
        logger.debug("enter");
        logger.debug("moving data for node " + node.fdn() + " to new parent " +
                     newParent.fdn());
        String reason = null;

        // node is either a ContainerNode, a HostNode, or an AppInstanceNode
        // - ContainerNode: just update the ContainerNodeData to point to the
        //   new parent
        // - HostNode: update the PhysicalHierarchyRelationshipData to point to
        //   the new parent ContainerNode
        // - AppInstanceNode: cannot be relocated, but then ViewManager filters
        //   these out anyway, so we need not worry about these...

        if ( node instanceof HostNode )
        {
            HostNode hostNode = (HostNode)node;

            PhysicalHierarchyRelationshipData link =
                new PhysicalHierarchyRelationshipData(hostNode.id(),
                                                      hostNode.parent().id());
            m_db.removePhysicalHierarchyRelationshipNode(link);

            link = new PhysicalHierarchyRelationshipData(hostNode.id(),
                                                         newParent.id());
            m_db.createPhysicalHierarchyRelationshipNode(link);
        }
        else if ( node instanceof ContainerNode )
        {
            ContainerNodeData nodeData =
                m_db.retrievePhysicalHierarchyNode(node.id());
            nodeData.parentId(newParent.id());
            m_db.updatePhysicalHierarchyNode(nodeData);
        }
        else
        {
            reason = "unexpected node type received: " +
                     node.getClass().getName();
            logger.error(reason);
            throw new Exception(reason);
        }
    }

    protected final String rootNodeName()
    {
        return "/";
    }

    protected final String discoveryNodeName()
    {
        return "Discovery";
    }
}
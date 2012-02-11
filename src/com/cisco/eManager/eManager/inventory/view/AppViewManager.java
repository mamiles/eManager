//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\AppViewManager.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.database.EmanagerDatabaseException;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.appType.*;

public class AppViewManager
    extends ViewManager
{
    private static Logger logger = Logger.getLogger(AppViewManager.class);
    private static AppViewManager s_instance = null;
    private AppInstanceManager m_aim;
    private AppTypeManager m_atm;

    /**
     * @return com.cisco.eManager.eManager.inventory.view.AppViewManager
     * @roseuid 3F0AE3550177
     */
    public static synchronized AppViewManager instance()
        throws Exception
    {
        if (s_instance == null)
        {
            s_instance = new AppViewManager();
        }
        return s_instance;
    }

    /**
     * @roseuid 3F4D08F00340
     */
    private AppViewManager()
        throws Exception
    {
        super("AppView");
        logger.debug("enter");
        m_atm = AppTypeManager.instance();
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
            newNodeId = m_db.createApplicationHierarchyNode(nodeData);
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
                m_db.removeApplicationHierarchyNode(acn.id());
            }
            catch (EmanagerDatabaseException ex)
            {
                logger.error("exception caught while removing " +
                             "containerNodeData: " + ex);
                return;
            }
        }
        else if ( acn instanceof AppTypeNode )
        {
            Collection links = null;
            try
            {
                links =
                    m_db.retrieveApplicationHierarchyRelationshipAppContainerNodes(acn.id());
            }
            catch (EmanagerDatabaseException ex1)
            {
                logger.error("exception caught while finding  " +
                             "appType-to-parent links: " + ex1);
                return;
            }
            Iterator iter = links.iterator();
            ApplicationHierarchyRelationshipData ahrd = null;
            ManagedObjectId nodeParentId = acn.parent().id();
            ManagedObjectId linkApplicationHierarchyId = null;
            while ( iter.hasNext() )
            {
                ahrd = (ApplicationHierarchyRelationshipData)iter.next();
                linkApplicationHierarchyId = ahrd.appHierarchyId();
                if ( linkApplicationHierarchyId.equals(nodeParentId) )
                {
                    logger.debug("removing " +
                                 "applicationHierarchyRelationshipData: " +
                                 "parentId = " +
                                 ahrd.appHierarchyId().toString() +
                                 ", appTypeId = " +
                                 ahrd.appTypeId().toString());
                    try
                    {
                        m_db.removeApplicationHierarchyRelationshipNode(ahrd);
                    }
                    catch (EmanagerDatabaseException ex2)
                    {
                        logger.debug("exception caught while removing  " +
                                     "appType-to-parent link: " + ex2);
                        return;
                    }
                    break;
                }
            }
        }
    }

    /**
     * @param appTypeCreation
     * @roseuid 3F0AE5340175
     */
    synchronized void newAppType(AppTypeCreation appTypeCreation)
    {
        logger.debug("enter");
        AppType newAppType = appTypeCreation.appType();
        logger.info("creating a new app type node for app type " +
                    newAppType.name());

        // first, see if this appType is already in the apps tree
        // if it is, we're done
        // if not, add it under the discovery node
        Node appTypeNode = find(newAppType.id());
        if (appTypeNode != null)
        {
            logger.debug("appType with ID " + newAppType.id().toString() +
                         " already exists in the apps view - appType name = " +
                         newAppType.name());
            return;
        }

        try
        {
            createAppTypeNode(newAppType, m_discoveryNode);
        }
        catch (Exception e)
        {
            logger.error("exception caught while creating a new AppTypeNode " +
                         "under the discovery node: e = " + e);
        }
    }

    /**
     * @param appInstanceCreation
     * @roseuid 3F0AE55900BA
     */
    synchronized void newAppInstance(AppInstanceCreation appInstanceCreation)
    {
        logger.debug("enter");
        AppInstance newAppInstance = appInstanceCreation.appInstance();
        logger.debug("handling creation of new appInstance " +
                     newAppInstance.name());
        Node appInstanceNode = find(newAppInstance.id());
        if (appInstanceNode != null)
        {
            logger.debug("appInstance already exists in the apps view (node " +
                         appInstanceNode.fdn() + ")");
            return;
        }

        AppType appType = newAppInstance.appType();
        Node parentNode = find(appType.id());
        AppTypeNode appTypeNode = null;
        if (parentNode == null)
        {
            // not sure if this can be reached, but we know what to do if it
            // is...
            // - create an appType node
            // - hand a new appInstance node under it
            logger.warn("handling a new appInstance where the appInstance's " +
                        "associated appType is not yet in the tree!");
            try
            {
                appTypeNode = createAppTypeNode(appType, m_discoveryNode);
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
            if (!(parentNode instanceof AppTypeNode))
            {
                logger.error("new app instance's associated app type found " +
                             "associated under a node which is not of type " +
                             "AppTypeNode");
                return;
            }
            appTypeNode = (AppTypeNode)parentNode;
        }

        try
        {
            createAppInstanceNode(newAppInstance, appTypeNode);
        }
        catch (Exception e)
        {
            logger.error("exception caught while creating a new AppTypeNode " +
                         "under the discovery node: e = " + e);
        }
    }

    /**
     * @param appTypeDeletion
     * @roseuid 3F0AE56E0345
     */
    public synchronized void delAppType(AppTypeDeletion appTypeDeletion)
    {
        logger.debug("enter");
        AppType deletedAppType = appTypeDeletion.appType();
        logger.info("deleting appType " + deletedAppType.name());
        Node node = find(deletedAppType.id());
        if (node == null)
        {
            logger.warn("unable to locate node for appType");
            return;
        }
        logger.debug("found node in view: " + node.fdn());

        if (!(node instanceof AppTypeNode))
        {
            logger.warn("associated node in view is not of type AppTypeNode");
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

    private synchronized AppTypeNode
        createAppTypeNode(AppType appType, AbstractContainerNode parent)
        throws Exception
    {
        logger.debug("enter");
        logger.info("creating appTypeNode " + appType.name() +
                    " under parent " + parent.fdn());

        // the appType and the parent node (in this view, type ContainerNode)
        // already exist
        // - first create the relationship link between the two
        // - then create the AppTypeNode for this new appType

        ApplicationHierarchyRelationshipData ahrd =
            new ApplicationHierarchyRelationshipData(appType.id(), parent.id());
        logger.debug("creating applicationHierarchyRelationshipData" +
                     ": parentId = " + ahrd.appHierarchyId().toString() +
                     ", appTypeId = " + ahrd.appTypeId().toString());
        try
        {
            m_db.createApplicationHierarchyRelationshipNode(ahrd);
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught while creating appType-to-parent " +
                         "link: " + ex);
            return null;
        }

        AppTypeNode newAppTypeNode = null;
        try
        {
            newAppTypeNode = new AppTypeNode(appType, parent);
        }
        catch (Exception e)
        {
            logger.error("exception caught while creating a new app type " +
                         "node: " + e);
            throw e;
        }

        NodeNotification ntfcnObj =
            new NodeNotification(NodeNotificationType.CREATE, newAppTypeNode);
        setChanged();
        notifyObservers(ntfcnObj);

        logger.info("appTypeNode created: " + newAppTypeNode.fdn());

        return newAppTypeNode;
    }

    private synchronized AppTypeNode
        restoreAppTypeNode(AppType appType, AbstractContainerNode parent)
    {
        logger.debug("enter");
        logger.info("restoring appTypeNode " + appType.name() +
                    " under parent " + parent.fdn());

        // the appType and the parent node (in this view, type ContainerNode)
        // already exist
        // also, the db data (type ApplicationHierarchyRelationshipData) already
        // exists (hence the difference between this method and
        // createAppTypeNode()...)

        AppTypeNode appTypeNode = null;
        try
        {
            appTypeNode = new AppTypeNode(appType, parent);
        }
        catch (Exception e)
        {
            logger.error("exception caught while creating a new app type " +
                         "node: " + e);
        }

        if ( appTypeNode != null )
        {

            NodeNotification ntfcnObj =
                new NodeNotification(NodeNotificationType.CREATE, appTypeNode);
            setChanged();
            notifyObservers(ntfcnObj);

            logger.info("appTypeNode restored: " + appTypeNode.fdn());
        }
        else
        {
            logger.warn("unable to restore appTypeNode from db");
        }
        return appTypeNode;
    }

    protected final Collection getContainerNodesFromDb()
    {
        logger.debug("enter");
        Collection nodes = null;
        try
        {
            nodes = m_db.retrieveApplicationHierarchyNodes();
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
            links = m_db.retrieveApplicationHierarchyRelationshipNodes();
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught retrieving links from DB: " + ex);
        }

        Iterator iter = links.iterator();
        Node node = null;
        ContainerNode containerNode = null;
        AppType appType = null;
        AppTypeNode appTypeNode = null;
        while (iter.hasNext())
        {
            ApplicationHierarchyRelationshipData linkData =
                (ApplicationHierarchyRelationshipData)iter.next();
            node = find(linkData.appHierarchyId());
            if (node instanceof ContainerNode)
            {
                containerNode = (ContainerNode)node;
                appType = m_atm.find(linkData.appTypeId());
                if (appType == null)
                {
                    logger.error("physicalHierarchyRelationshipData links to " +
                                 "host which cannot be found!");
                    logger.error("id = " + linkData.appTypeId().toString());
                }
                else
                {
                    appTypeNode = restoreAppTypeNode(appType, containerNode);

                    if ( appTypeNode != null )
                    {
                        // now flesh out appInstances under host node...
                        restoreAppInstances(appTypeNode);
                    }
                }
            }
            else
            {
                logger.error("applicationHierarchyRelationshipData links to " +
                             "non-container node in application view!");
                logger.error("id = " +
                             linkData.appHierarchyId().toString());
            }
        }
    }

    private void restoreAppInstances(AppTypeNode appTypeNode)
    {
        logger.debug("enter");
        if (appTypeNode == null)
        {
            logger.warn("null host node");
            return;
        }

        List appInstances = appTypeNode.appType().appInstances();
        Iterator iter = appInstances.iterator();
        AppInstance appInstance = null;
        while (iter.hasNext())
        {
            appInstance = (AppInstance)iter.next();
            try
            {
                createAppInstanceNode(appInstance, appTypeNode);
            }
            catch (Exception ex)
            {
                logger.error("exception caught while building out an appType " +
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
        // - AppTypeNode: update the ApplicationHierarchyRelationshipData to
        //   point to the new parent ContainerNode
        // - AppInstanceNode: cannot be relocated, but then ViewManager filters
        //   these out anyway, so we need not worry about these...

        if ( node instanceof AppTypeNode )
        {
            AppTypeNode appTypeNode = (AppTypeNode)node;

            ApplicationHierarchyRelationshipData link =
                new ApplicationHierarchyRelationshipData(
                        appTypeNode.id(),
                        appTypeNode.parent().id());
            m_db.removeApplicationHierarchyRelationshipNode(link);

            link = new ApplicationHierarchyRelationshipData(appTypeNode.id(),
                                                            newParent.id());
            m_db.createApplicationHierarchyRelationshipNode(link);
        }
        else if ( node instanceof ContainerNode )
        {
            ContainerNodeData nodeData =
                m_db.retrieveApplicationHierarchyNode(node.id());
            nodeData.parentId(newParent.id());
            m_db.updateApplicationHierarchyNode(nodeData);
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
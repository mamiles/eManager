//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\ViewManager.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.eManager.database.DatabaseInterface;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.common.database.*;

public abstract class ViewManager
    extends Observable
{
    private static Logger logger = Logger.getLogger(ViewManager.class);
    protected DatabaseInterface m_db;
    protected ContainerNode m_root;
    protected ContainerNode m_discoveryNode;
    private String m_viewName;
    private String m_logPrefix;

    /**
     * @roseuid 3F4D08F600E6
     */
    protected ViewManager(String viewName)
        throws Exception
    {
        m_viewName = viewName;
        m_logPrefix = "[" + m_viewName + "]: ";
        logger.debug(m_logPrefix + "enter");

        String reason = null;

        try
        {
            m_db = DatabaseInterface.instance();
        }
        catch (EmanagerDatabaseException ex)
        {
            reason = "exception caught while initializing DB: " + ex;
            logger.error(m_logPrefix + reason);
            return;
        }
    }

    protected void initialize()
        throws Exception
    {
        initializeCacheFromDb();
    }

    public ContainerNode root()
    {
        return m_root;
    }

    protected abstract ContainerNodeData
        createContainerNodeData(String name, ManagedObjectId parentId);

    protected abstract void removeContainerNodeData(AbstractContainerNode acn);

    protected abstract String rootNodeName();

    protected abstract String discoveryNodeName();

    protected abstract Collection getContainerNodesFromDb();

    protected abstract void finalizeViewRestoration();

    protected abstract void moveNodeInDb(AbstractContainerNode newParent,
                                         Node node)
        throws Exception;

    public synchronized ContainerNode
        createContainerNode(String name, AbstractContainerNode parent)
        throws Exception
    {
        logger.debug(m_logPrefix + "enter");
        logger.info(m_logPrefix + "creating container " + name +
                    " under parent " + parent.fdn());
        ContainerNodeData nodeData = createContainerNodeData(name, parent.id());
        ContainerNode newContainerNode = null;
        try
        {
            newContainerNode = new ContainerNode(nodeData, parent);
        }
        catch (Exception e)
        {
            logger.error(m_logPrefix + "exception caught while creating a new container " +
                         "node: " + e);
            throw e;
        }

        NodeNotification ntfcnObj =
            new NodeNotification(NodeNotificationType.CREATE, newContainerNode);
        setChanged();
        notifyObservers(ntfcnObj);

        logger.info(m_logPrefix + "containerNode created: " +
                    newContainerNode.fdn());

        return newContainerNode;
    }

    protected synchronized AppInstanceNode
        createAppInstanceNode(AppInstance appInstance,
                              AbstractContainerNode parent)
        throws Exception
    {
        logger.debug(m_logPrefix + "enter");
        logger.info(m_logPrefix + "creating appInstanceNode " +
                    appInstance.name() + " under parent " + parent.fdn());

        AppInstanceNode newAppInstanceNode = null;
        try
        {
            newAppInstanceNode = new AppInstanceNode(appInstance, parent);
        }
        catch (Exception e)
        {
            logger.error(m_logPrefix + "exception caught while creating a " +
                         "new app instance node: " + e);
            throw e;
        }

        NodeNotification ntfcnObj =
            new NodeNotification(NodeNotificationType.CREATE, newAppInstanceNode);
        setChanged();
        notifyObservers(ntfcnObj);

        logger.info(m_logPrefix + "appInstanceNode created: " +
                    newAppInstanceNode.fdn());

        return newAppInstanceNode;
    }

    /**
     * @roseuid 3F2149870031
     */
    public synchronized void moveNode(Node node,
                                      AbstractContainerNode newParent)
        throws Exception
    {
        logger.debug("enter");
        /*
         rules for relocation:
         1) cannot relocate a node which is not "relocatable" (a node with a
            fixed parent - ex. an appInstance cannot be assigned to an appType
            or to another host by manipulation in the view: an appInstance is
            what it is and it is where it is...)
         2) cannot relocate a node to a new parent which is itself already a
            descendent (that would break the subtree being relocated out of the
            tree itself)
         3) the new parent must be able to "parent" a node of the type being
            relocated
         4) cannot relocate the discovery node
         5) cannot relocate the root node (see #2)
         6) cannot relocate to the discovery node (it's in the sfs!)
         */

        String reason = null;
        String nodeFdn = node.fdn();
        String newParentFdn = newParent.fdn();

        // rule 6
        if ( newParent.equals(m_discoveryNode) )
        {
            reason = "cannot relocate node to the discovery node";
            logger.error(m_logPrefix + reason);
            throw new Exception(reason);
        }

        // rule #5
        if (node.parent() == null)
        {
            reason = "node has no parent: cannot relocate root node";
            logger.error(m_logPrefix + reason);
            throw new Exception(reason);
        }

        // rule #1
        if (!node.relocatable())
        {
            reason = "node selected for move is not directly relocatable " +
                     "(cannot change its parent)";
            logger.error(m_logPrefix + reason);
            throw new Exception(reason);
        }

        // rule #2
        if (newParentFdn.startsWith(nodeFdn))
        {
            reason = "cannot relocate a node under one of its descendents";
            logger.error(m_logPrefix + reason);
            throw new Exception(reason);
        }

        // rule #3
        if (!newParent.canHaveChild(node))
        {
            reason = "target parent connot contain node";
            logger.error(m_logPrefix + reason);
            throw new Exception(reason);
        }

        // rule #4
        if (nodeFdn.equals(m_discoveryNode.fdn()))
        {
            reason = "cannot relocate the discovery node";
            logger.error(m_logPrefix + reason);
            throw new Exception(reason);
        }

        logger.info("moving node " + nodeFdn + " to new parent " +
                    newParentFdn);

        AbstractContainerNode oldParent = (AbstractContainerNode)node.parent();

        moveNodeInDb(newParent, node); // concrete view must do this part

        Node.detach(node);
        Node.attach(newParent, node);

        NodeMoveNotification ntfcn =
            new NodeMoveNotification(node, oldParent, newParent);
        setChanged();
        notifyObservers(ntfcn);

        logger.debug("node relocated");
    }

    public synchronized void
        delAppInstance(AppInstanceDeletion appInstanceDeletion)
    {
        logger.debug(m_logPrefix + "enter");
        AppInstance deletedAppInstance =
            appInstanceDeletion.deletedAppInstance();
        logger.info(m_logPrefix + "deleting appInstance " +
                    deletedAppInstance.name());
        Node node = find(deletedAppInstance.id());
        if (node == null)
        {
            logger.warn(m_logPrefix + "unable to locate node for appInstance");
            return;
        }
        logger.debug(m_logPrefix + "found node in view: " + node.fdn());

        if (!(node instanceof AppInstanceNode))
        {
            logger.warn(m_logPrefix + "associated node in view is not of type " +
                        "AppInstanceNode");
            return;
        }

        try
        {
            removeNode(node);
        }
        catch (Exception e)
        {
            logger.error(m_logPrefix + "exception caught while attempting to remove node " +
                         "from view: " + e);
        }
    }

    /**
     * @roseuid 3F1ED9D302EE
     */
    public synchronized final void removeNode(Node node)
        throws Exception
    {
        logger.debug(m_logPrefix + "enter");
        logger.info(m_logPrefix + "removing node " + node.fdn());
        String reason = null;

        // container nodes of all kinds are captured in our db, if it is one
        // then we'll have to remove the persistent data associated with it.
        // also, container nodes can have childre, but we don't support removal
        // of nodes which currently have children.  handle both below...
        if (node instanceof AbstractContainerNode)
        {
            // it's a container, so first verify that it has no children
            AbstractContainerNode acn = (AbstractContainerNode)node;
            if (!acn.children().isEmpty())
            {
                reason = "cannot delete a node which still has one or more " +
                         "children";
                logger.error(m_logPrefix + reason);
                throw new Exception(reason);
            }

            // remove associated data from db
            removeContainerNodeData(acn);
        }

        AbstractContainerNode parent = (AbstractContainerNode)node.parent();
        if (parent == null)
        {
            // if this node has no parent, then I guess we're done (but this
            // doesn't really feel right...)
            logger.warn(m_logPrefix + "node to be removed has no parent - " +
                        "there is nothing left to do...");
            return;
        }

        try
        {
            parent.removeChild(node);
        }
        catch (Exception e)
        {
            logger.error(m_logPrefix + "exception caught while removing node " +
                         "from view: " + e);
            return;
        }

        logger.info(m_logPrefix + "node removed: " + node.fdn());

        NodeNotification ntfcnObj =
            new NodeNotification(NodeNotificationType.DELETE, node);
        setChanged();
        notifyObservers(ntfcnObj);
    }

    public List find(String name)
    {
        logger.debug(m_logPrefix + "enter");
        logger.debug(m_logPrefix + "searching for nodes with name \"" + name + "\"");
        if (name == null)
        {
            logger.debug(m_logPrefix + "null name received - returning null");
            return null;
        }
        List nodes = findNodesInSubtree(m_root, name);
        return nodes;
    }

    public Node find(ManagedObjectId id)
    {
        logger.debug(m_logPrefix + "enter");
        logger.debug(m_logPrefix + "searching for node with ID " + id.toString());

        if (id == null)
        {
            logger.debug(m_logPrefix + "null ID received - returning null");
            return null;
        }

        Node node = findNodeInSubtree(m_root, id);
        if (node == null)
        {
            logger.debug(m_logPrefix + "node not found");
        }
        else
        {
            logger.debug(m_logPrefix + "node found: " + node.fdn());
        }
        return node;
    }

    public Node find(NodePath path)
    {
        logger.debug(m_logPrefix + "enter");
        logger.debug(m_logPrefix + "searching for node with path " + path.toString());

        if (path == null)
        {
            logger.debug(m_logPrefix + "null path received - returning null");
            return null;
        }

        Node node = findNodeInSubtree(m_root, path);
        if (node == null)
        {
            logger.debug(m_logPrefix + "node not found");
        }
        else
        {
            logger.debug(m_logPrefix + "node found: " + node.fdn());
        }
        return node;
    }

    public static boolean nodeCanHaveChild(Node node, Node childNode)
    {
        if (!node.canHaveChildren())
        {
            return false;
        }

        if (!node.canHaveChild(childNode))
        {
            return false;
        }

        return true;
    }

    public static List findNodesInSubtree(Node subtreeRoot, String name)
    {
        logger.debug("enter: subtreeRoot = " + subtreeRoot.name());
        List nodes = new LinkedList();
        logger.debug("comparing subtreeRoot " + subtreeRoot.name() +
                     " to name " + name);
        if (subtreeRoot.name().equals(name))
        {
            nodes.add(subtreeRoot);
        }
        if (subtreeRoot instanceof AbstractContainerNode)
        {
            List children = ((AbstractContainerNode)subtreeRoot).children();
            Iterator iter = children.iterator();
            List matches = null;
            Iterator matchesIter = null;
            while (iter.hasNext())
            {
                matches = findNodesInSubtree((Node)iter.next(), name);
                matchesIter = matches.iterator();
                while (matchesIter.hasNext())
                {
                    nodes.add((Node)matchesIter.next());
                }
            }
        }
        return nodes;
    }

    public static Node findNodeInSubtree(Node subtreeRoot, ManagedObjectId id)
    {
        logger.debug("enter: subtreeRoot = " + subtreeRoot.name());
        if (subtreeRoot.id().equals(id))
        {
            return subtreeRoot;
        }

        if (subtreeRoot instanceof AbstractContainerNode)
        {
            List children = ((AbstractContainerNode)subtreeRoot).children();
            Iterator iter = children.iterator();
            Iterator matchesIter = null;
            Node node;
            while (iter.hasNext())
            {
                node = findNodeInSubtree((Node)iter.next(), id);
                if (node != null)
                {
                    return node;
                }
                // else keep looking
            }
        }
        return null;
    }

    public static Node findNodeInSubtree(Node subtreeRoot, NodePath path)
    {
        logger.debug("enter: subtreeRoot = " + subtreeRoot.name());
        NodePath subtreeRootNodePath = new NodePath(subtreeRoot.componentFdn());
        if ( subtreeRootNodePath.equals(path) )
        {
            return subtreeRoot;
        }

        if (subtreeRoot instanceof AbstractContainerNode)
        {
            List children = ((AbstractContainerNode)subtreeRoot).children();
            Iterator iter = children.iterator();
            Iterator matchesIter = null;
            Node node = null;
            Node newSubtreeRoot = null;
            String pathStr = path.toString();
            while (iter.hasNext())
            {
                newSubtreeRoot = (Node)iter.next();
                if ( pathStr.startsWith(newSubtreeRoot.fdn()) )
                {
                    // if the specified node exists, it exists down this path...
                    return (findNodeInSubtree(newSubtreeRoot, path));
                }
                // else keep looking
            }
        }
        return null;
    }

    private void initializeCacheFromDb()
        throws Exception
    {
        logger.debug(m_logPrefix + "enter");
        Collection nodes = getContainerNodesFromDb();
        if (nodes == null)
        {
            logger.error("unable to retrieve nodes from DB");
            return;
        }

        // this is a bit trickier than rebuilding other caches, which are just
        // lists.  this is a tree which needs to be built up from the collection
        // obtained above

        String discoveryNodeName = discoveryNodeName();
        if (nodes.size() < 1)
        {
            // this is an empty collection
            // - create a root node and a "Discovery" node
            ContainerNodeData rootNodeData =
                createContainerNodeData(rootNodeName(), null);
            m_root = new ContainerNode(rootNodeData, null);

            if (discoveryNodeName != null)
            {
                ContainerNodeData discoveryNodeData =
                    createContainerNodeData(discoveryNodeName, m_root.id());
                m_discoveryNode =
                    new ContainerNode(discoveryNodeData, m_root);
            }
            else
            {
                m_discoveryNode = m_root;
            }
            return;
        }

        m_root = findRoot(nodes);
        if (m_root == null)
        {
            logger.error("no root node exists for nodes in DB");
            return;
        }

        // using the container nodes from the db, build out the tree
        buildSubtree(m_root, nodes);

        // now establish easy link to the discovery node
        if (discoveryNodeName == null)
        {
            m_discoveryNode = m_root;
        }
        else
        {
            List discNodes = find(discoveryNodeName);
            Iterator iter = discNodes.iterator();
            Node discNode = null;
            while (iter.hasNext())
            {
                discNode = (Node)iter.next();
                if (discNode.parent().equals(m_root))
                {
                    m_discoveryNode = (ContainerNode)discNode;
                    break;
                }
            }
            if ( m_discoveryNode == null )
            {
                // create one
                m_discoveryNode = createContainerNode(discoveryNodeName, m_root);
            }
        }

        // now build out the rest of the view (this is view-specific)
        finalizeViewRestoration();
        return;
    }

    private ContainerNode findRoot(Collection nodes)
        throws Exception
    {
        // the root node is the node which points to itself as its parent
        Iterator iter = nodes.iterator();
        ContainerNodeData nodeData = null;
        ContainerNode rootNode = null;
        while (iter.hasNext())
        {
            nodeData = (ContainerNodeData)iter.next();
            if (nodeData.id().equals(nodeData.parentId()))
            {
                iter.remove();
                rootNode = new ContainerNode(nodeData, null);
                return rootNode;
            }
        }
        return null;
    }

    private void buildSubtree(AbstractContainerNode subtreeRoot,
                              Collection containerNodeDatas)
        throws Exception
    {
        logger.debug("enter: subtreeRoot = " + subtreeRoot.fdn());
        Iterator iter = containerNodeDatas.iterator();
        ContainerNodeData containerNodeData = null;
        ContainerNode containerNode = null;
        while (iter.hasNext())
        {
            containerNodeData = (ContainerNodeData)iter.next();
            if (containerNodeData.parentId().equals(subtreeRoot.id()))
            {
                // remove this data from the collection and add a child node for
                // it to the subtreeRoot
                containerNode = new ContainerNode(containerNodeData, subtreeRoot);

                // it makes me nervous to do this from within the iteration...
                iter.remove();
            }
        }

        // now, each new child of this subtreeRoot is potentially a subtreeRoot
        // itself... (that is, if there are more unclaimed nodes)
        if (!containerNodeDatas.isEmpty())
        {
            List children = subtreeRoot.children();
            iter = children.iterator();
            Node childNode = null;
            AbstractContainerNode newSubtreeRoot = null;
            while (iter.hasNext())
            {
                childNode = (Node)iter.next();
                if ( childNode instanceof AbstractContainerNode )
                {
                    newSubtreeRoot = (AbstractContainerNode)childNode;
                    buildSubtree(newSubtreeRoot, containerNodeDatas);
                }
            }
        }
    }
}

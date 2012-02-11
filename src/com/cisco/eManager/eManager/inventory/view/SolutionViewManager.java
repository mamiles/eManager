//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\SolutionViewManager.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.database.EmanagerDatabaseException;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.database.DatabaseInterface;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.solution.*;

public class SolutionViewManager
    extends ViewManager
{
    private static Logger logger = Logger.getLogger(SolutionViewManager.class);
    private static SolutionViewManager s_instance = null;
    private SolutionManager m_sm;
    private AppInstanceManager m_aim;
    private int tempIdCounter;

    public static synchronized SolutionViewManager instance()
        throws Exception
    {
        if (s_instance == null)
        {
            s_instance = new SolutionViewManager();
        }
        return s_instance;
    }

    private SolutionViewManager()
        throws Exception
    {
        super("SolutionView");
        logger.debug("enter");
        m_sm = SolutionManager.instance();
        m_aim = AppInstanceManager.instance();
        tempIdCounter = 0;
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
            newNodeId = m_db.createSolutionHierarchyNode(nodeData);
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
                m_db.removeSolutionHierarchyNode(acn.id());
            }
            catch (EmanagerDatabaseException ex)
            {
                logger.error("exception caught while removing " +
                             "containerNodeData: " + ex);
                return;
            }
        }
        else if ( acn instanceof SolutionNode )
        {
            Collection links = null;
            try
            {
                links =
                    m_db.retrieveSolutionHierarchyRelationshipPhysContainerNodes(acn.id());
            }
            catch (EmanagerDatabaseException ex1)
            {
                logger.error("exception caught while finding  " +
                             "solution-to-parent links: " + ex1);
                return;
            }
            Iterator iter = links.iterator();
            SolutionHierarchyRelationshipData shrd = null;
            ManagedObjectId nodeParentId = acn.parent().id();
            ManagedObjectId linkSolutionHierarchyId = null;
            while ( iter.hasNext() )
            {
                shrd = (SolutionHierarchyRelationshipData)iter.next();
                linkSolutionHierarchyId = shrd.solutionHierarchyId();
                if ( linkSolutionHierarchyId.equals(nodeParentId) )
                {
                    logger.debug("removing solutionHierarchyRelationshipData" +
                                 ": parentId = " +
                                 shrd.solutionHierarchyId().toString() +
                                 ", solutionId = " + shrd.solutionId().toString());
                    try
                    {
                        m_db.removeSolutionHierarchyRelationshipNode(shrd);
                    }
                    catch (EmanagerDatabaseException ex2)
                    {
                        logger.error("exception caught while removing  " +
                                     "solution-to-parent link: " + ex2);
                        return;
                    }
                    break;
                }
            }
        }
    }

    synchronized void newSolution(Solution newSolution)
    {
        logger.debug("enter");
        logger.info("creating a new solution node for solution " + newSolution.name());
        addSolutionToView(newSolution);
    }

    synchronized void restoredSolution(Solution restoredSolution)
    {
        logger.debug("enter");
        logger.info("making sure restored solution (" + restoredSolution.name() +
                    ") is in solution view");
        addSolutionToView(restoredSolution);
    }

    synchronized void deletingSolution(Solution solution)
    {
        logger.debug("enter");
        Node node = find(solution.id());
        if ( node == null )
        {
            logger.info("cannot find solution node to delete it");
            return;
        }
        logger.info("removing solution \"" + node.name() + "\" from view");
        try
        {
            removeNode(node);
        }
        catch (Exception ex)
        {
            logger.error("Exception caught while removing solution from " +
                         "solution view: " + ex);
        }
    }

    synchronized void undeletedSolution(Solution undeletedSolution)
    {
        logger.debug("enter");
        logger.info("adding solution (" + undeletedSolution.name() + ") back to view");
        addSolutionToView(undeletedSolution);
    }

    private synchronized void addSolutionToView(Solution solution)
    {
        logger.debug("enter");

        Node node = find(solution.id());
        SolutionNode solutionNode = null;
        if (node == null)
        {
            try
            {
                solutionNode = createSolutionNode(solution, m_discoveryNode);
            }
            catch (Exception e)
            {
                logger.error("exception caught while creating a new SolutionNode " +
                             "under the discovery node: e = " + e);
                return;
            }
        }
        else
        {
            solutionNode = (SolutionNode)node;
        }
    }

    private synchronized SolutionNode createSolutionNode(Solution solution,
                                                         ContainerNode parent)
        throws Exception
    {
        logger.debug("enter");
        logger.info("creating solutionNode " + solution.name() +
                    " under parent " + parent.fdn());

        // the solution and the parent node (in this view, type ContainerNode)
        // already exist
        // - first create the relationship link between the two
        // - then create the SolutionNode for this new solution

        SolutionHierarchyRelationshipData shrd =
            new SolutionHierarchyRelationshipData(solution.id(), parent.id());
        logger.debug("creating solutionHierarchyRelationshipData" +
                     ": parentId = " + shrd.solutionHierarchyId().toString() +
                     ", solutionId = " + shrd.solutionId().toString());

        try
        {
            m_db.createSolutionHierarchyRelationshipNode(shrd);
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught while creating solution-to-parent " +
                         "link: " + ex);
            return null;
        }

        SolutionNode newSolutionNode = null;
        try
        {
            newSolutionNode = new SolutionNode(solution, parent);
        }
        catch (Exception e)
        {
            logger.error("exception caught while creating a new solution node: " +
                         e);
            throw e;
        }

        NodeNotification ntfcnObj =
            new NodeNotification(NodeNotificationType.CREATE, newSolutionNode);
        setChanged();
        notifyObservers(ntfcnObj);

        logger.info("solutionNode created: " + newSolutionNode.fdn());

        return newSolutionNode;
    }

    private synchronized SolutionNode restoreSolutionNode(Solution solution,
                                                          ContainerNode parent)
    {
        logger.debug("enter");
        logger.info("creating solutionNode " + solution.name() +
                    " under parent " + parent.fdn());

        // the solution and the parent node (in this view, type ContainerNode)
        // already exist
        // also, the db data (type SolutionHierarchyRelationshipData) already
        // exists (hence the difference between this method and
        // createSolutionNode()...)

        SolutionNode solutionNode = null;
        try
        {
            solutionNode = new SolutionNode(solution, parent);
        }
        catch (Exception e)
        {
            logger.error("exception caught while creating a new solution node: " +
                         e);
        }

        if ( solutionNode != null )
        {
            NodeNotification ntfcnObj =
                new NodeNotification(NodeNotificationType.CREATE, solutionNode);
            setChanged();
            notifyObservers(ntfcnObj);

            logger.info("solutionNode created: " + solutionNode.fdn());
        }
        else
        {
            logger.warn("unable to restore solutionNode from db");
        }

        return solutionNode;
    }

    protected final Collection getContainerNodesFromDb()
    {
        logger.debug("enter");
        Collection nodes = null;
        try
        {
            nodes = m_db.retrieveSolutionHierarchyNodes();
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
            links = m_db.retrieveSolutionHierarchyRelationshipNodes();
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught retrieving links from DB: " + ex);
        }

        Iterator iter = links.iterator();
        Node node = null;
        ContainerNode containerNode = null;
        Solution solution = null;
        SolutionNode solutionNode = null;
        while (iter.hasNext())
        {
            SolutionHierarchyRelationshipData linkData =
                (SolutionHierarchyRelationshipData)iter.next();
            node = find(linkData.solutionHierarchyId());
            if ( node instanceof ContainerNode )
            {
                containerNode = (ContainerNode)node;
                SolutionId solutionId = null;
                try
                {
                    solutionId = new SolutionId(linkData.solutionId());
                }
                catch (Exception ex1)
                {
                    logger.error("Exception caught while creating a " +
                                 "SolutionId instance from " +
                                 "linkData.solutionId(): " + ex1);
                    continue;
                }
                solution = m_sm.get(solutionId);
                if ( solution == null )
                {
                    logger.error("solutionHierarchyRelationshipData links to " +
                                 "solution which cannot be found!");
                    logger.error("id = " + linkData.solutionId().toString());
                }
                else
                {
                    solutionNode = restoreSolutionNode(solution, containerNode);

                    if ( solutionNode != null )
                    {
                        // now flesh out appInstances under solution node...
                        restoreAppInstances(solutionNode);
                    }
                }
            }
            else
            {
                logger.error("solutionHierarchyRelationshipData links to " +
                             "non-container node in solution view!");
                logger.error("id = " +
                             linkData.solutionHierarchyId().toString());
            }
        }
    }

    private void restoreAppInstances(SolutionNode solutionNode)
    {
        logger.debug("enter");
        if ( solutionNode == null )
        {
            logger.warn("null solution node");
            return;
        }

        Solution solution = solutionNode.solution();
        AppInstance[] appInstances = solution.appInstances();
        int count = appInstances.length;
        for (int i = 0; i < count; i++)
        {
            try
            {
                createAppInstanceNode(appInstances[i], solutionNode);
            }
            catch (Exception ex)
            {
                logger.error("exception caught while building out a solution " +
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

        // node is either a ContainerNode, a SolutionNode, or an AppInstanceNode
        // - ContainerNode: just update the ContainerNodeData to point to the
        //   new parent
        // - SolutionNode: update the SolutionHierarchyRelationshipData to point to
        //   the new parent ContainerNode
        // - AppInstanceNode: cannot be relocated, but then ViewManager filters
        //   these out anyway, so we need not worry about these...

        if ( node instanceof SolutionNode )
        {
            SolutionNode solutionNode = (SolutionNode)node;

            SolutionHierarchyRelationshipData link =
                new SolutionHierarchyRelationshipData(solutionNode.id(),
                                                      solutionNode.parent().id());
            m_db.removeSolutionHierarchyRelationshipNode(link);

            link = new SolutionHierarchyRelationshipData(solutionNode.id(),
                                                         newParent.id());
            m_db.createSolutionHierarchyRelationshipNode(link);
        }
        else if ( node instanceof ContainerNode )
        {
            ContainerNodeData nodeData =
                m_db.retrieveSolutionHierarchyNode(node.id());
            nodeData.parentId(newParent.id());
            m_db.updateSolutionHierarchyNode(nodeData);
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
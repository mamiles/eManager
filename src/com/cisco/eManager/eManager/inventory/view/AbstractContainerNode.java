//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\container\\AbstractContainerNode.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import com.cisco.eManager.common.inventory.*;
import org.apache.log4j.Logger;

public abstract class AbstractContainerNode
    extends Node
{
    private static Logger logger =
        Logger.getLogger(AbstractContainerNode.class);
    protected List m_children;

    AbstractContainerNode()
        throws Exception
    {
        m_children = new LinkedList();
    }

    public final boolean canHaveChildren()
    {
        return true;
    }

    /**
     * @param node
     * @throws java.lang.Exception
     * @roseuid 3F872D57006B
     */
    public final void addChild(Node node)
        throws Exception
    {
        String reason = null;
        if (node == null)
        {
            reason = "null child node - cannot add";
            logger.error(reason);
            throw new Exception(reason);
        }
        if (!canHaveChild(node))
        {
            reason = "cannot add this type of child node to this node";
            logger.error(reason);
            throw new Exception(reason);
        }
        Iterator iter = m_children.iterator();
        Node childNode = null;
        while (iter.hasNext())
        {
            childNode = (Node)iter.next();
            if ( node.id().equals(childNode.id()) )
            {
                logger.warn("an abstract container node cannot have the same " +
                            "child more than one time!");
                return;
            }
        }
        m_children.add(node);
    }

    public void removeChild(Node node)
        throws Exception
    {
        String reason = null;
        if (node == null)
        {
            reason = "null child node - cannot remove";
            logger.error(reason);
            throw new Exception(reason);
        }

        if ( m_children.remove(node) )
        {
            logger.debug("the child node has been removed from this node");
        }
        else
        {
            logger.warn("the specified node is not a child of this node");
        }
    }

    /**
     * @return java.util.List
     * @roseuid 3F872D590321
     */
    public List children()
    {
        return m_children;
    }
}

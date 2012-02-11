//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\node\\Node.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

public abstract class Node
{
    private static Logger logger = Logger.getLogger(Node.class);
    private Node m_parent;

    Node()
    {
    }

    /**
     * @return java.lang.String
     * @roseuid 3F09CFF003D0
     */
    public abstract String name();

    public abstract ManagedObjectId id();

    /**
     * @return boolean
     * @roseuid 3F872D8B01C6
     */
    public abstract boolean relocatable();

    /**
     * @return boolean
     * @roseuid 3F872D8B0216
     */
    public abstract boolean canHaveChildren();

    /**
     * @param node
     * @return boolean
     * @roseuid 3F872D8B025C
     */
    public abstract boolean canHaveChild(Node node);

    /**
     * @return com.cisco.eManager.eManager.inventory.view.node.Node
     * @roseuid 3F872D8C0023
     */
    public Node parent()
    {
        return m_parent;
    }

    /**
     * @param newParent
     * @throws java.lang.Exception
     * @roseuid 3F872D8C007D
     */
    public void parent(Node newParent)
        throws Exception
    {
        String reason = null;
        if (!this.relocatable())
        {
            reason = "cannot change the parent of an unrelocatable node";
            throw new Exception(reason);
        }
        if (!(newParent instanceof ContainerNode))
        {
            reason = "newParent must be of type ContainerNode";
            logger.error(reason);
            throw new Exception(reason);
        }
        m_parent = newParent;
    }

    static void attach(AbstractContainerNode parent, Node child)
        throws Exception
    {
        String reason = null;
        if (!parent.canHaveChild(child))
        {
            reason = "supplied \"child\" cannot be a child to supplied " +
                     "\"parent\"";
            logger.error(reason);
            throw new Exception(reason);
        }

        parent.addChild(child);
        child.m_parent = parent;
    }

    static void detach(Node node)
    {
        AbstractContainerNode parent = (AbstractContainerNode)node.parent();
        Iterator iter = parent.children().iterator();
        Node tmpNode = null;
        while ( iter.hasNext() )
        {
            tmpNode = (Node)iter.next();
            if ( tmpNode.equals(node) )
            {
                logger.debug("child node found - removing child from parent");
                iter.remove();
                node.m_parent = null;
                return;
            }
        }
        logger.warn("child node not found - returning without updating " +
                    "parent node");
    }

    public String[] componentFdn()
    {
        String[] cfdn = null;
        if ( m_parent == null )
        {
            cfdn = new String[1];
            cfdn[0] = name();
        }
        else
        {
            String[] parentComponentFdn = m_parent.componentFdn();
            cfdn = new String[parentComponentFdn.length + 1];
            for (int i = 0; i < parentComponentFdn.length; i++)
            {
                cfdn[i] = parentComponentFdn[i];
            }
            cfdn[parentComponentFdn.length] = name();
        }
        return cfdn;
    }

    public String fdn()
    {
        String fdn = null;
        String parentFdn = null;
        if ( m_parent == null )
        {
            fdn = name();
        }
        else
        {
            parentFdn = m_parent.fdn();
            if (parentFdn.equals("/"))
            {
                fdn = parentFdn + name();
            }
            else
            {
                fdn = m_parent.fdn() + "/" + name();
            }
        }
        return fdn;
    }
}
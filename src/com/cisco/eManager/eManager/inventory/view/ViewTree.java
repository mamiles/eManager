//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\ViewTree.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.eManager.inventory.view.*;

public abstract class ViewTree
{
    private static Logger logger = Logger.getLogger(ViewTree.class);
    private ContainerNode m_root;
    private long m_idKeyValue;

    /**
     * @roseuid 3F4D08F90126
     */
    protected ViewTree(String name, int treeId)
    {
        logger.debug("enter");
        m_idKeyValue = 0;
    }

    /**
     * @roseuid 3F1ECFC0019D
     */
    public ContainerNode root()
    {
        return m_root;
    }

    public Node find(String fdn)
    {
        // change fdn from String to NodePath (a local class)
        logger.debug("enter");
        return null;
    }
    public void add(Node parent, Node newChild)
    {
        // first verify that this new node won't collide with any of its sibling
        // nodes... (ex. that its name is unique under this particular parent)
        logger.debug("enter");
    }
    public void move(Node node, Node newParent)
    {
        logger.debug("enter");
    }
    public void remove(Node node)
    {
        logger.debug("enter");
    }
}

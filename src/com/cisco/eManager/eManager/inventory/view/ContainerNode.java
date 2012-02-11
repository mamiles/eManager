//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\container\\ContainerNode.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import com.cisco.eManager.common.inventory.*;
import org.apache.log4j.Logger;

public final class ContainerNode
    extends AbstractContainerNode
{
    private static Logger logger = Logger.getLogger(ContainerNode.class);
    private ContainerNodeData m_data;

    ContainerNode(ContainerNodeData data, AbstractContainerNode parent)
        throws Exception
    {
        m_data = data;
        if ( parent != null )
        {
            Node.attach(parent, this);
        }
    }

    public ManagedObjectId id()
    {
        return m_data.id();
    }

    public String name()
    {
        return m_data.name();
    }

    /**
     * @return boolean
     * @roseuid 3F872D5500CD
     */
    public final boolean relocatable()
    {
        return true;
    }

    /**
     * @return boolean
     * @roseuid 3F872D550113
     */
    public final boolean canHaveChild(Node node)
    {
        if ( node instanceof AbstractContainerNode )
        {
            return true;
        }
        return false;
    }

    public final ManagedObjectId dataId()
    {
        return m_data.id();
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

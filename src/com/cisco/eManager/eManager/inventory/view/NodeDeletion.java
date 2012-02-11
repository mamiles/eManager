//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\NodeDeletion.java

package com.cisco.eManager.eManager.inventory.view;

import com.cisco.eManager.common.inventory.ManagedObjectId;

public class NodeDeletion
{
    private Node m_node;

    /**
     * @param deletedNode
     * @roseuid 3F6DFFE60228
     */
    NodeDeletion(Node deletedNode)
    {
        m_node = deletedNode;
    }

    /**
     * @return com.cisco.eManager.eManager.inventory.Node.Node
     * @roseuid 3F6DFFE90219
     */
    public Node Node()
    {
        return m_node;
    }
}

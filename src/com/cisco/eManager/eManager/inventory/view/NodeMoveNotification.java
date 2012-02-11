//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\NodeMoveNotification.java

package com.cisco.eManager.eManager.inventory.view;


public class NodeMoveNotification extends NodeNotification
{
    private AbstractContainerNode m_oldParent;
    private AbstractContainerNode m_newParent;

    NodeMoveNotification(Node node,
                         AbstractContainerNode oldParent,
                         AbstractContainerNode newParent)
    {
        super(NodeNotificationType.OTHER, node);
        m_oldParent = oldParent;
        m_newParent = newParent;
    }

    public AbstractContainerNode oldParent()
    {
        return m_oldParent;
    }

    public AbstractContainerNode newParent()
    {
        return m_newParent;
    }
}
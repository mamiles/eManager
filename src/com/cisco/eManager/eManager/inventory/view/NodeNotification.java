//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\NodeNotification.java

package com.cisco.eManager.eManager.inventory.view;


public class NodeNotification
{
    private NodeNotificationType m_ntfcnType;
    private Node m_node;

    NodeNotification(NodeNotificationType type, Node node)
    {
        m_ntfcnType = type;
        m_node = node;
    }

    public NodeNotificationType ntfcnType()
    {
        return m_ntfcnType;
    }

    public Node node()
    {
        return m_node;
    }
}
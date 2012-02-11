//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\NodeNotificationType.java

package com.cisco.eManager.eManager.inventory.view;


/**
 * @author wstubb
 * @version 1.0
 */
public class NodeNotificationType
{
    private static int s_OTHER = 0; // node notification type defined via extension of NodeNotification class
    private static int s_CREATE = 1;
    private static int s_DELETE = 2;

    public static final NodeNotificationType OTHER =
        new NodeNotificationType(s_OTHER);
    public static final NodeNotificationType CREATE =
        new NodeNotificationType(s_CREATE);
    public static final NodeNotificationType DELETE =
        new NodeNotificationType(s_DELETE);

    private int m_type;

    private NodeNotificationType(int type)
    {
        m_type = type;
    }

    public boolean equals(NodeNotificationType nnt)
    {
        return (m_type == nnt.m_type);
    }
}
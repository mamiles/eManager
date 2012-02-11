//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\common\\inventory\\HostNotificationType.java

package com.cisco.eManager.eManager.inventory.host;


/**
 * @author wstubb
 * @version 1.0
 */
public class HostNotificationType
{
    private static int s_ACTIVATE = 1;
    private static int s_CREATE = 2;
    private static int s_DELETE = 3;
    private static int s_RESTORE = 4;
    private static int s_UNDELETE = 5;
    private static int s_PRE_DELETE = 6;

    public static final HostNotificationType CREATE =
        new HostNotificationType(s_CREATE);
    public static final HostNotificationType ACTIVATE =
        new HostNotificationType(s_ACTIVATE);
    public static final HostNotificationType RESTORE =
        new HostNotificationType(s_RESTORE);
    public static final HostNotificationType DELETE =
        new HostNotificationType(s_DELETE);
    public static final HostNotificationType UNDELETE =
        new HostNotificationType(s_UNDELETE);
    public static final HostNotificationType PRE_DELETE =
        new HostNotificationType(s_PRE_DELETE);

    private int m_type;

    private HostNotificationType(int type)
    {
        m_type = type;
    }

    public boolean isActivate()
    {
        return (m_type == s_ACTIVATE);
    }

    public boolean isCreate()
    {
        return (m_type == s_CREATE);
    }

    public boolean isDelete()
    {
        return (m_type == s_DELETE);
    }

    public boolean isRestore()
    {
        return (m_type == s_RESTORE);
    }

    public boolean isUndelete()
    {
        return (m_type == s_UNDELETE);
    }

    public boolean isPreDelete()
    {
        return (m_type == s_PRE_DELETE);
    }
}

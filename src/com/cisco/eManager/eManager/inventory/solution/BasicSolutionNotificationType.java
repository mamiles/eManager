//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\solution\\BasicSolutionNotificationType.java

package com.cisco.eManager.eManager.inventory.solution;


/**
 * @author wstubb
 * @version 1.0
 */
public class BasicSolutionNotificationType
{
    private static int s_CREATE = 1;
    private static int s_PRE_DELETE = 2;
    private static int s_DELETE = 3;
    private static int s_UNDELETE = 4;
    private static int s_RESTORE = 5;

    public static final BasicSolutionNotificationType CREATE =
        new BasicSolutionNotificationType(s_CREATE);
    public static final BasicSolutionNotificationType PRE_DELETE =
        new BasicSolutionNotificationType(s_PRE_DELETE);
    public static final BasicSolutionNotificationType POST_DELETE =
        new BasicSolutionNotificationType(s_DELETE);
    public static final BasicSolutionNotificationType UNDELETE =
        new BasicSolutionNotificationType(s_UNDELETE);
    public static final BasicSolutionNotificationType RESTORE =
        new BasicSolutionNotificationType(s_RESTORE);

    private int m_type;

    private BasicSolutionNotificationType(int type)
    {
        m_type = type;
    }

    public boolean equals(BasicSolutionNotificationType bsnt)
    {
        return (m_type == bsnt.m_type);
    }
}
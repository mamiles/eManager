//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\solution\\BasicSolutionNotification.java

package com.cisco.eManager.eManager.inventory.solution;


public class BasicSolutionNotification
{
    private BasicSolutionNotificationType m_ntfcnType;
    private Solution m_solution;

    BasicSolutionNotification(BasicSolutionNotificationType type,
                              Solution solution)
    {
        m_ntfcnType = type;
        m_solution = solution;
    }

    public BasicSolutionNotificationType ntfcnType()
    {
        return m_ntfcnType;
    }

    public Solution solution()
    {
        return m_solution;
    }
}

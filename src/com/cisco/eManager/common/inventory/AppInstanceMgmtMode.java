//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\common\\inventory\\AppInstanceMgmtMode.java

package com.cisco.eManager.common.inventory;


/**
 * @author wstubb
 * @version 1.0
 */
public class AppInstanceMgmtMode
{
    private boolean m_mode;
    public static AppInstanceMgmtMode MANAGED = new AppInstanceMgmtMode (true);
    public static AppInstanceMgmtMode UNMANAGED = new AppInstanceMgmtMode (false);

    public AppInstanceMgmtMode()
    {
        // need a default ctor for wsdl
        m_mode = true;
    }

    /**
     * @param mode true == managed
     * false == unmanaged
     * @roseuid 3F62213000B7
     */
    private AppInstanceMgmtMode(boolean mode)
    {
        m_mode = mode;
    }

    /**
     * @return true == managed
     * false == unmanaged
     * @roseuid 3F6221D300EE
     */
    public boolean isManaged()
    {
        return m_mode;
    }

    public String toString()
    {
        if (isManaged()) {
            return "Managed";
        }

        return "Unmanaged";
    }
}

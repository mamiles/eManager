//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\common\\inventory\\MethodArg.java

package com.cisco.eManager.common.inventory;


/**
 * @author wstubb
 * @version 1.0
 */
public class MethodArg
{
    private String m_name;
    private String m_value;

    public MethodArg()
    {
    }

    public MethodArg(String name, String value)
    {
        m_name = name;
        m_value = value;
    }

    public String name()
    {
        return m_name;
    }

    public String value()
    {
        return m_value;
    }
}

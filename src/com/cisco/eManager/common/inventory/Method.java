//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\common\\inventory\\Method.java

package com.cisco.eManager.common.inventory;


/**
 * @author wstubb
 * @version 1.0
 */
public class Method
{
    private String m_name;
    private Instrumentation m_inst;
    private String[] m_argNames;
    private String[] m_argValues;

    public Method()
    {
    }

    public Method(String name,
                  String[] argNames,
                  String[] argValues,
                  Instrumentation instr)
    {
        m_name = name;
        m_argNames = argNames;
        m_argValues = argValues;
        m_inst = instr;
    }

    public Instrumentation instrumentation()
    {
        return m_inst;
    }

    public String name()
    {
        return m_name;
    }

    public String[] argNames()
    {
        return m_argNames;
    }

    public String[] argValues()
    {
        return m_argValues;
    }

    public void argValues(String[] argValues)
    {
        m_argValues = argValues;
    }
}
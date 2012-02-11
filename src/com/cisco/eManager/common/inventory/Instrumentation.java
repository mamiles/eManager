//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\common\\inventory\\Instrumentation.java

package com.cisco.eManager.common.inventory;


/**
 * @author wstubb
 * @version 1.0
 */
public class Instrumentation
{
    private ManagedObjectId m_id;
    private Method[] m_methods;

    /**
     * @roseuid 3F561E560270
     */
    public Instrumentation()
    {
    }

    /**
     * @return int
     * @roseuid 3F54B20A02D6
     */
    public ManagedObjectId id()
    {
        return m_id;
    }

    public Method[] methods()
    {
        return m_methods;
    }
}

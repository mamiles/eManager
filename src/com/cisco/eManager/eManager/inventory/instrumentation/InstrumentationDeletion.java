//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\instrumentation\\InstrumentationDeletion.java

package com.cisco.eManager.eManager.inventory.instrumentation;

import com.cisco.eManager.common.inventory.ManagedObjectId;

public class InstrumentationDeletion 
{
    private Instrumentation m_instrumentation;
    
    /**
     * @param deletedInstrumentation
     * @roseuid 3F4D086903A7
     */
    InstrumentationDeletion(Instrumentation deletedInstrumentation) 
    {
        m_instrumentation = deletedInstrumentation;     
    }
    
    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectKey
     * @roseuid 3F215D560366
     */
    public Instrumentation instrumentation() 
    {
        return m_instrumentation;     
    }
}

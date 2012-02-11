//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImInstrumentationDeletion.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation;

public class ImInstrumentationDeletion 
{
    private Instrumentation m_instrumentation;
    
    /**
     * @param deletedInstrumentation
     * @roseuid 3F6E000002BC
     */
    ImInstrumentationDeletion(Instrumentation deletedInstrumentation) 
    {
        m_instrumentation = deletedInstrumentation;     
    }
    
    /**
     * @return com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation
     * @roseuid 3F6E000201ED
     */
    public Instrumentation instrumentation() 
    {
        return m_instrumentation;     
    }
}

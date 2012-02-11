//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImInstrumentationCreation.java

package com.cisco.eManager.eManager.inventory;

import com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation;

public class ImInstrumentationCreation 
{
    private Instrumentation m_instrumentation;
    
    /**
     * @param createdInstrumentation
     * @roseuid 3F6DFFF503BB
     */
    ImInstrumentationCreation(Instrumentation createdInstrumentation) 
    {
        m_instrumentation = createdInstrumentation;     
    }
    
    /**
     * @return com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation
     * @roseuid 3F6DFFF60362
     */
    public Instrumentation instrumentation() 
    {
        return m_instrumentation;     
    }
}

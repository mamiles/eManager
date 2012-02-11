//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\instrumentation\\Method.java

package com.cisco.eManager.eManager.inventory.instrumentation;

import org.apache.log4j.Logger;
import COM.TIBCO.hawk.talon.*;
import com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation;

public class Method
{
    private static Logger logger = Logger.getLogger(Method.class);
    Instrumentation m_instrumentation;
    MethodDescriptor m_methodDescriptor;

    /**
     * @roseuid 3F4D086B0025
     */
    public Method(MethodDescriptor methodDescriptor,
                  Instrumentation instrumentation)
    {
        logger.debug("creating new instance of method");
        logger.debug("instrumentation = " + instrumentation);
        logger.debug("methodDescriptor = " + methodDescriptor);
        m_instrumentation = instrumentation;
        m_methodDescriptor = methodDescriptor;
    }

    public MethodDescriptor methodDescriptor()
    {
        return m_methodDescriptor;
    }

    public String name()
    {
        return m_methodDescriptor.getName();
    }

    public MicroAgentData invoke(DataElement[] args)
    {
        MicroAgentData data = null;
        MethodInvocation methodInvocation = new MethodInvocation(name(), args);
        try
        {
            data =
                m_instrumentation.appInstance().host().agentManager().invoke(
                        m_instrumentation.networkId().raw(),
                        methodInvocation);
        }
        catch (MicroAgentException e)
        {
            logger.debug("exception caught: " + e.toString());
            e.printStackTrace();
        }
        return data;
    }

    public boolean equals(Object object)
    {
        if (object instanceof Method)
        {
            if (m_instrumentation.equals(((Method)object).m_instrumentation) &&
                m_methodDescriptor.equals(((Method)object).m_methodDescriptor))
            {
                return true;
            }
        }
        return false;
    }
}
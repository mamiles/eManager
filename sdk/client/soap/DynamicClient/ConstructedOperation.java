/*
 * Operation.java
 *
 * Created on November 5, 2003, 2:57 PM
 */

package DynamicClient;
import java.util.LinkedList;

/**
 *
 * @author  rchuang
 */
public class ConstructedOperation {
    
    /** Creates a new instance of Operation */
    public ConstructedOperation() 
    {
        
    }
    
    public ConstructedOperation(String name, 
                                LinkedList parameters, 
                                LinkedList parameterOrder, 
                                Parameter returnObject, 
                                String faultName, 
                                Parameter faultParameter)
    {
        m_name = name;
        m_parameterOrder = parameterOrder;
        m_parameters = parameters;
        m_returnParameter = returnObject;
        m_faultName = faultName;
        m_faultParameter = faultParameter;
    }
    
    public void dump()
    {
        System.out.println("ConstructedOperation:");
        System.out.println("name = " + m_name);
        System.out.println("parameter order:");
        for (int i = 0; i < m_parameterOrder.size(); i++)
        {
            
            String paramOrder = (String) m_parameterOrder.get(i);
            
            System.out.println("[" + i + "] = " + paramOrder );  //object.getClass().getName()); /
        }
        System.out.println("parameters: ");
        for (int i = 0; i< m_parameters.size(); i++)
        {
            Parameter param = (Parameter) m_parameters.get(i);
            param.dump();
        }
        System.out.println("returnObject: " );
        m_returnParameter.dump();
        System.out.println("faultName: " + m_faultName);
        System.out.println("fault parameter: ");
        m_faultParameter.dump();
        
    }
    public String       m_name = null;
    public LinkedList   m_parameterOrder = null;
    public LinkedList   m_parameters = null;
    public Parameter    m_returnParameter = null;
    public String       m_faultName = null;
    public Parameter    m_faultParameter = null;
}

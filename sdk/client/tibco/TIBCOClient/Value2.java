/*
 * Value.java
 *
 * Created on December 19, 2003, 9:32 AM
 */

package TIBCOClient;

/**
 *
 * @author  rchuang
 */
public class Value2 {
    
    /** Creates a new instance of Value */
    public Value2() 
    {
        m_name = null;
        m_content = null;
    }
    
    public Value2(String name, String content)
    {
        m_name = name;
        m_content = content;
    }
    
    public String toString()
    {
        return ( "[" + m_name + ", " + m_content + "]");
    }
    
    public  String  m_name;
    public  String  m_content;
}

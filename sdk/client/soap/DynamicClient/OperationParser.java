/*
 * OperationParser.java
 *
 * Created on November 18, 2003, 5:08 PM
 */

package DynamicClient;
import java.util.LinkedList;
import java.net.URL;
import java.net.MalformedURLException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
/**
 *
 * @author  rchuang
 */
public class OperationParser extends DefaultHandler {
    
    /** Creates a new instance of OperationParser */
    public OperationParser() {
    }
        
    public OperationParser( String portType )
    {
        m_portType = portType;
        m_operations = new LinkedList();
    }
 
    public void startElement( String namespaceURI,
                                String lname,
                                String qname,
                                Attributes attrs)
    {
        if (lname.equalsIgnoreCase("portType") 
            && attrs != null)
        {
            m_isInPort = true;
 
        }
        if (m_isInPort
            && lname.equalsIgnoreCase("operation")
            && attrs != null)
        {
            m_isInOperation = true;
            for (int i = 0; i < attrs.getLength(); i++)
            {
                String attrName = attrs.getLocalName(i);
                if (attrName.equalsIgnoreCase("name"))
                {
                    String opName = attrs.getValue(i);
                    System.out.println("operation name = " + opName);
                    m_operations.add(opName);
                }
            }
        }
    }
    public void endElement( String uri,
                            String lname,
                            String qname)
    {
        if (lname.equalsIgnoreCase("portType"))
        {
            m_isInPort = false;
        }
        if (lname.equalsIgnoreCase("operation"))
        {
            m_isInOperation = false;
        }
    }
    
    boolean     m_isInPort;
    boolean     m_isInOperation;
    String      m_portType;
    LinkedList   m_operations;
}

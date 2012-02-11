/*
 * MessageParser.java
 *
 * Created on November 14, 2003, 5:00 PM
 */

package DynamicClient;
import java.net.URL;
import java.net.MalformedURLException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
/**
 *
 * @author  rchuang
 */
public class MessageParser extends DefaultHandler {
    
    /** Creates a new instance of WSDLParser */
    public MessageParser() 
    {
    }
    
    public MessageParser(String portName, String operationName)
    {

            m_port = portName;
            m_operation = operationName;    
            m_foundOperation = false;

    }

    /**
     * Input: m_operation
     *
     * Output: 
     *      m_request
     *      m_response
     */
    public void startElement( String namespaceURI,
                                    String lname,
                                    String qname,
                                    Attributes attrs)
    {
            
            if (lname.equalsIgnoreCase("portType") && attrs != null)
            {
                m_isInPort = true;
            }
            if ( m_isInPort
                && lname.equalsIgnoreCase("operation") 
                && attrs != null)
            {
                m_isInOperation = true;
                String opName = attrs.getValue( "name" );
                //System.out.println("PP.SE: name = " + opName);
                if (opName.equals(m_operation))
                {                                        
                    m_foundOperation = true;
                }
            }
            if ( m_isInPort
                && m_isInOperation
                && m_foundOperation
                && lname.equalsIgnoreCase("input")
                && attrs != null)
            {
                for (int i = 0; i < attrs.getLength(); i++)
                {
                    String attrName = attrs.getLocalName(i);
                    if (attrName.equalsIgnoreCase("name"))
                    {
                        m_request = attrs.getValue(i);
                        //System.out.println("Port: " + m_port + ", Operation: " + m_operation + ", request message = " + m_request );
                    }
                }
            }
            if ( m_isInPort
                && m_isInOperation
                && m_foundOperation
                && lname.equalsIgnoreCase("output")
                && attrs != null)
            {
                for (int i = 0; i < attrs.getLength(); i++)
                {
                    String attrName = attrs.getLocalName(i);
                    if (attrName.equalsIgnoreCase("name"))
                    {
                        m_response = attrs.getValue(i);
                        //System.out.println("Port: " + m_port + ", Operation: " + m_operation + ", response message = " + m_response );
                    }
                }
            }         
            if (m_isInPort
                && m_isInOperation
                && m_foundOperation
                && lname.equalsIgnoreCase("fault")
                && attrs != null)
            {
                for (int i = 0; i < attrs.getLength(); i++)
                {
                    String attrName = attrs.getLocalName(i);
                    if (attrName.equalsIgnoreCase("name"))
                    {
                        m_fault = attrs.getValue(i);
                        //System.out.println("Port: " + m_port + ", Operation: " + m_operation + ", fault message = " + m_fault );
                        
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
                m_foundOperation = false;
            }
    }
        

        boolean     m_isInPort;
        boolean     m_isInOperation;
        boolean     m_foundOperation;
        String      m_port;
        String      m_operation;
        String      m_request;
        String      m_response;
        String      m_fault;
    
}

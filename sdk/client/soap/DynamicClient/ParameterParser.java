/*
 * ParameterParser.java
 *
 * Created on November 19, 2003, 10:37 AM
 */

package DynamicClient;
import java.util.ArrayList;
import java.util.LinkedList;
import java.net.URL;
import java.net.MalformedURLException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author  rchuang
 */
public class ParameterParser extends org.xml.sax.helpers.DefaultHandler {
    
    /** Creates a new instance of ParameterParser */
    public ParameterParser() {
    }
    
    public ParameterParser (String messageName )
    {
        m_message       = messageName;
        m_isInMessage   = false;
        m_parameters    = new LinkedList();
    }
    
    /*
     * Input: m_message
     * Output:
     *  m_parameters
     */
     public void startElement( String namespaceURI,
                                String  lname,
                                String  qname,
                                Attributes  attrs)
     {
         if (lname.equalsIgnoreCase("message") 
            && attrs != null)
         {
             m_isInMessage = true;
             String messageName = attrs.getValue("name");
             if (messageName.equals(m_message))
             {
                 m_foundMatchingMessage = true;
             }
         }
      
         if ( lname.equalsIgnoreCase("part")
            && m_foundMatchingMessage )
         {
             String parameterName = attrs.getValue("name");
             String parameterType = attrs.getValue("type");
             Parameter parameter = new Parameter( parameterName, parameterType );
             m_parameters.add(parameter);
             
         }
     }
    
     public void endElement( String uri,
                            String lname,
                            String qname)
     {
         if (lname.equalsIgnoreCase("message"))
         {
             m_isInMessage = false;
             m_foundMatchingMessage = false;
         }
        
     }
    // input
    String      m_message;
    
    // internal use
    boolean     m_isInMessage;
    boolean     m_foundMatchingMessage;
    
    // output
    LinkedList   m_parameters;
    
}

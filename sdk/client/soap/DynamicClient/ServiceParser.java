/*
 * PortParser.java
 *
 * Created on November 20, 2003, 10:26 AM
 */

package DynamicClient;
import java.util.LinkedList;

/**
 *
 * @author  rchuang
 */
public class ServiceParser extends org.xml.sax.helpers.DefaultHandler {
    
    /** Creates a new instance of PortParser */
    public ServiceParser() {
        m_isInService = false;        
        m_ports = new LinkedList();        
    }
    
    public ServiceParser(String service)
    {
        m_service = service;
        m_isInService = false;
        m_isInPort  = false;
        m_ports = new LinkedList();
        m_serviceLocation = "";
    }
    
    public ServiceParser(String service, String port)
    {
        m_service = service;
        m_port = port;
        m_isInService = false;
        m_isInPort = false;
        m_ports = new LinkedList();        
        m_serviceLocation = "";
    }
    public void startDocument() {
    }
    
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) 
    {
        if (localName.equalsIgnoreCase("service") && attributes != null)
        {
            m_isInService = true;
            m_service = attributes.getValue("name");
        }
        if (localName.equalsIgnoreCase("port")
            && m_isInService)
        {
            m_isInPort = true;
            m_port = attributes.getValue("name");
            m_ports.add(m_port);
        }
        if (localName.equalsIgnoreCase("address"))
        {
            m_serviceLocation = attributes.getValue("location");
        }
    }
    
    public void endDocument() {
    }
    
    public void endElement(String uri, String localName, String qName) 
    {
        if (localName.equalsIgnoreCase("service"))
        {
            m_isInService = false;
        }
        if (localName.equalsIgnoreCase("port"))
        {
            m_isInPort = false;
        }
    }
    
    boolean     m_isInService;
    boolean     m_isInPort;
    String      m_service;
    String      m_port;
    LinkedList  m_ports;
    String      m_serviceLocation;
    
}

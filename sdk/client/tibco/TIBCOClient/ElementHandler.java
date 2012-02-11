/*
 * ElementHandler.java
 *
 * Created on December 18, 2003, 11:18 AM
 */

package TIBCOClient;
import java.util.LinkedList;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.IOException;
import org.xml.sax.SAXException;

/**
 *
 * @author  rchuang
 */
public class ElementHandler extends org.xml.sax.helpers.DefaultHandler {
    
    /** Creates a new instance of ElementHandler */
    
    public ElementHandler() 
    {
        m_value = new Value2();
        m_elements = new LinkedList();
        m_values = new ArrayList();
        m_out = new PrintWriter(System.out);
        m_textBuffer = null;
        
    }
    
    public void startElement( String namespaceURI,
                                String lname,
                                String qname,
                                Attributes attrs)
        throws SAXException
    {
        
        if (m_value.m_name != null)
        {
            m_value.m_content = null;
            m_values.add(m_value);
            
            m_value = new Value2();
        }
        //System.out.println("sE: ");    
        //echoText();
        m_elements.add(lname);
        
        
        m_value.m_name = lname.trim();
        //System.out.println("lname = " + lname);
        
    }
    
    public void endElement(String uri,
                            String lname,
                            String qname)
         throws SAXException
    {
        //System.out.println("eE: ");
        if (m_textBuffer != null)
        {
            m_value.m_content = m_textBuffer.toString().trim();
        }
        //System.out.println("eE: m_value = " + m_value.toString());
        
        if ( m_value.m_name != null )
            m_values.add(m_value);
        
        m_value = new Value2();
        
        echoText();
    }
    
    public void characters( char[] ch,
                            int start,
                            int length)
    {
        String s = new String(ch, start, length);
        if ( m_textBuffer == null)
        {
            m_textBuffer = new StringBuffer(s);
        }
        else
        {
            m_textBuffer.append(s);
        }
    }
    
    public void processingInstruction( String target,
                                        String data )
    {
        System.out.println("pI: target = " + target);
        System.out.println("pI: data = " + data);
    }
    
    private void echoText()
        throws SAXException
    {
        if (m_textBuffer == null)
            return;
        String s = "" + m_textBuffer;
        emit(s);
        m_textBuffer = null;
    }
    
    private void emit ( String s)
        //throws SAXException
    {
        if (s != null && m_out != null)
        {
            //try
            //{
                m_out.write(s);
                m_out.flush();
            //}
            //catch (IOException ioe)
            //{
            //    throw new SAXException("I/O error", ioe);
            //}
        }
    }
    
    PrintWriter         m_out;
    StringBuffer        m_textBuffer;
    Value2               m_value;
    LinkedList          m_elements;
    ArrayList           m_values;   // type = Value2
    
}

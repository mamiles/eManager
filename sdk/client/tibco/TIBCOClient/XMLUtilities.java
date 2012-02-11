/*
 * XMLUtilities.java
 *
 * Created on December 22, 2003, 8:47 PM
 */

package TIBCOClient;

import java.util.ArrayList;
import java.io.File;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

//import org.xml.sax.*;
/**
 *
 * @author  rchuang
 */
public class XMLUtilities {
    
    /** Creates a new instance of XMLUtilities */
    public XMLUtilities() 
    {
        _globalCount = 0;
    }
    
    /**
     *  Finds the first Element whose tag matches elementTagName
     *
     *  Future: specify the nth occurrence of the Element
     */
    public Element findElement( String elementTagName, Node startNode)
    {
        Node baseNode;
        if (startNode instanceof Document)
        {
            Document doc = (Document) startNode;
            baseNode = doc.getDocumentElement();
        }
        else
        {
            baseNode = startNode;
        }
        if (baseNode instanceof Element)
        {
            Element currentElement = (Element) baseNode;
            String currentElementTagName = currentElement.getTagName();
            
            if (currentElementTagName.equals(elementTagName ))
            {
                debug("fE: Found match: " + elementTagName);
                
                return currentElement;
            }
            
            NodeList children = baseNode.getChildNodes();
            for ( int i = 0; i < children.getLength(); i++)
            {
                Node child = children.item(i);
                if (child instanceof Element)
                {
                    Element foundElement = findElement( elementTagName, child);
                    if (foundElement != null)
                    {
                        return foundElement;
                    }
                }
                else if (child instanceof Text)
                {
                    debug("fE: Text = " + baseNode.getNodeValue());
                }
                else
                {
                    debug("fE: Not Element or Text: " + baseNode.getNodeName());
                }
                
            }
        }
        return null;
    }
    
    public boolean setElementText( Document doc,
                                    Value2 newValue)
    {
        boolean textIsSet = false;
        
        String chosenElementName = newValue.m_name;
        String chosenElementValue = newValue.m_content;
        
        Element chosenElement = findElement( chosenElementName, doc);
        
        if (chosenElement != null)
        {
            NodeList children = chosenElement.getChildNodes();
            for (int i = 0; i < children.getLength(); i++)
            {
                Node child = children.item(i);
                if (child.getNodeType() == Node.TEXT_NODE)
                {
                    
                    debug("current Text value = " + child.getNodeValue());
                    chosenElement.removeChild(child);
                    Text text = doc.createTextNode( chosenElementValue);
                    // remove existing Text child node if it exists
                    //Node currentText = (Text) chosenElement.removeChild();
                    chosenElement.appendChild( text );
                    textIsSet = true;                    
                }
            }

        }
        else
            emitError("did not find element");
        
        
        return textIsSet;
        
    }
    /**
     * @param doc   the Document whose Element's Text children are  be updated from "values"
     * @param element the current Element being recursively processed
     * @param values an ArrayList of Value2.  NV pairs of (Element tag name, child Text node value)
     * @param _globalCount
     *
     */
    public void setAllElementText( Document doc, Element element, ArrayList values)
    {
        String elementTag = element.getTagName();
        debug("sAET: tag = " + elementTag);
        _globalCount++;
        debug("sAET: _globalCount = " + _globalCount);
          
        NodeList children = element.getChildNodes();
        boolean elementHasText = false;
        
        Value2 newValue     = (Value2) values.get(_globalCount);
        String newTag       = newValue.m_name;        
        String newContent   = newValue.m_content;
        Text newText        = doc.createTextNode( newContent );
        debug("sAET: new text = " + newText.getNodeValue());
        
        // Case 1: Original Element had a Text child
        for (int i = 0; i < children.getLength(); i++)
        {
            Node child = children.item(i);
            
            if (child.getNodeType() == Node.TEXT_NODE)
            {
                elementHasText = true;
                debug("sAET: [" + i + "] current text = " + child.getNodeValue());
                

                if ( newTag.equals(elementTag))
                {
                    element.removeChild( child);                     
                    element.appendChild( newText);
                }
                else
                    System.err.println("sAET: Element tags do not match!");
            }
        }
        
        // Case 2: Original Element did not have a Text child, but a new Text has been specified
        if (!elementHasText 
            && (newContent != null))
        {
            element.appendChild( newText );
        }
        
        
        for (int j = 0; j < children.getLength(); j++)
        {
            Node child = children.item(j);
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                Element childElement = (Element) child;
                setAllElementText(doc, childElement, values);
            }
        }
       
    }
    
    public int preorderTraverseDocument( Element element, int depth, int count)
    {
        String elementTag = element.getTagName();
        depth++;
        debug("pTD: depth = " + depth + ", count = " + count + ": tag = " + elementTag);
        
        debug("pTD: _globalCount = " + _globalCount);
        _globalCount++;
        NodeList children = element.getChildNodes();
        for (int i = 0 ; i < children.getLength(); i++)
        {
            Node child = children.item(i);

            if (child.getNodeType() == Node.ELEMENT_NODE)
            
            {
                
                Element nextElement = (Element) child;
                
                NodeList grandchildren = nextElement.getChildNodes();
                for (int j = 0; j < grandchildren.getLength(); j++)
                {
                    Node grandchild = grandchildren.item(j);
                    if (grandchild.getNodeType() == Node.TEXT_NODE)
                    {
                        Text textNode = (Text) grandchild;
                        debug("pTD: Text = " + textNode.getNodeValue());
                    }
                }
                
                
                //bug("pTD: nextElement = " + nextElement.getTagName());
                count++;
                //debug("pTD: count before: " + count);
                int finalCount = preorderTraverseDocument( nextElement, depth, count);
                //debug("pTD: finalCount = " + finalCount);
            }
            else
            {
                //debug("pTD: not entity");
            }
        }
        return count;
    }
    
    public String documentToString( Document doc)
    {
        String stringifiedDoc = null;
        
        if (doc != null)
        {
            try
            {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();

                DocumentType docType = doc.getDoctype();
                debug("dTS: docType = " + docType);
                if (docType != null)
                {
                    String systemId = docType.getSystemId();
                    String publicId = docType.getPublicId();
                    if (systemId != null )
                    {
                        debug( "dTS: systemId = " + systemId);
                        transformer.setOutputProperty("doctype-system", systemId);
                    }
                    if (publicId != null )
                    {
                        debug( " dTS: publicId = " + publicId);
                        transformer.setOutputProperty("doctype-public", publicId);
                    }
                }

                DOMSource domSource = new DOMSource(doc);

                StreamResult streamResult = new StreamResult();
                StringWriter stringWriter = new StringWriter();
                streamResult.setWriter( stringWriter );
                transformer.transform( domSource, streamResult);

                stringifiedDoc = stringWriter.toString();
                debug (stringifiedDoc);
            }
            catch (TransformerConfigurationException tce)
            {
                tce.printStackTrace();
            }
            catch( TransformerException te)
            {
                te.printStackTrace();
            }
        }
        else
            emit_err("");
        
        return stringifiedDoc;
    }
    public void emit_err( String output)
    {
        System.err.println(output);
    }
    
    public Document fileToDocument (File docFile)
    {
        Document doc = null;
        
        if (docFile.exists())
        {
            try
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
                documentBuilder.setErrorHandler( new TibcoMessageErrorHandler());
                doc = documentBuilder.parse(docFile);
            }
            catch (ParserConfigurationException pce)
            {
                pce.printStackTrace();
            }
            catch (FactoryConfigurationError fce)
            {
                fce.printStackTrace();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
            catch (SAXException se)
            {
                se.printStackTrace();
            }
        }
        return doc;
    }
   
    public void documentToFile( Document doc, String outFileName)
    {
        try
        {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DocumentType docType = doc.getDoctype();
            if (docType != null)
            {
                String systemId = docType.getSystemId();
                String publicId = docType.getPublicId();
                if (systemId != null)
                    transformer.setOutputProperty("doctype-system", systemId);
                if (publicId != null)
                    transformer.setOutputProperty("doctype-public", publicId);
            }
            DOMSource domSource = new DOMSource(doc);
            
            String outFile = outFileName + ".xml";
            
            FileOutputStream fileOutputStream = new FileOutputStream( outFile );
            StreamResult streamResult = new StreamResult( fileOutputStream );
            transformer.transform( domSource, streamResult);
        }
        catch (DOMException de)
        {
            de.printStackTrace();
        }
        catch (TransformerException te)
        {
            te.printStackTrace();
        }
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
               
    }
    
    public Document stringToDocument (String docString )
    {
        Document resultDoc = stringToDocument( docString, true);
        return resultDoc;
    }
    
    public Document stringToDocument (String docString, boolean validate )
    {
        Document doc = null;
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(validate);
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            documentBuilder.setErrorHandler(
                new org.xml.sax.ErrorHandler()
                {
                    public void fatalError( SAXParseException spe)
                        throws SAXParseException
                    {
                    };
                    
                    public void error(SAXParseException spe)
                        throws SAXParseException
                    {
                        throw spe;
                    };
                    
                    public void warning(SAXParseException spe)
                        throws SAXParseException
                    {
                        System.err.println(" Warning: "
                            + ", line " + spe.getLineNumber()
                            + ", uri " + spe.getSystemId());
                        
                        System.err.println(" " + spe.getMessage());
                    }
                }
            );
            ByteArrayInputStream bais = new ByteArrayInputStream( docString.getBytes());
            doc = documentBuilder.parse( bais);
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
         
        return doc;
        
    }
    
    
    public void debug (String output)
    {
        //if (m_debug.equals("TRUE"))            
            //System.out.println(output);
    }
    
    public void emitError( String output)
    {
        System.err.println(output);
    }
    
    private class TibcoMessageErrorHandler implements org.xml.sax.ErrorHandler
    {
        public void fatalError( SAXParseException spe)
            throws SAXException
        {
        }
        
        public void error ( SAXParseException spe)
            throws SAXException
        {
            throw spe;
        }
        
        public void warning( SAXParseException spe)
            throws SAXParseException
        {
            System.err.println("** Warning" 
                + ", LINE " + spe.getLineNumber()
                + ", uri " + spe.getSystemId());
            System.err.println(" " + spe.getMessage());
        }
    };
          
    
    int _globalCount;
        
    
}

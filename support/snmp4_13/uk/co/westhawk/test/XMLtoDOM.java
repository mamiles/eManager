//
// NAME
//      $RCSfile: XMLtoDOM.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.5 $
// CREATED
//      $Date: 2002/10/10 15:13:58 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//
/*
 * Copyright (C) 2000, 2001, 2002 by Westhawk Ltd (www.westhawk.co.uk)
 *
 * Permission to use, copy, modify, and distribute this software
 * for any purpose and without fee is hereby granted, provided
 * that the above copyright notices appear in all copies and that
 * both the copyright notice and this permission notice appear in
 * supporting documentation.
 * This software is provided "as is" without express or implied
 * warranty.
 * author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 */
package uk.co.westhawk.test;
                    
import java.io.*;
import java.util.*;
import java.net.*;

import org.w3c.dom.*;
import org.xml.sax.*;
import org.apache.xerces.parsers.*;

/**
 * The class XMLtoDOM creates a DOM (Document Object Model) from XML
 * input. This XML input is received via a URL.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.5 $ $Date: 2002/10/10 15:13:58 $
 */
public class XMLtoDOM 
{
    private static final String     version_id =
        "@(#)$Id: XMLtoDOM.java,v 1.5 2002/10/10 15:13:58 birgit Exp $ Copyright Westhawk Ltd";

    final static String WARNING       = "Warning";
    final static String ERROR         = "Error";
    final static String FATAL_ERROR   = "Fatal Error";

    private ErrorStorer ef;

    // The document say there is a
    // org.apache.xerces.parsers.RevalidatingDOMParser, but it isn't in
    // the jar file???
    private DOMParser   parser;
    private String      url;
    private PrintWriter writer;

/**
 *  The constructor.
 */
public XMLtoDOM()
{
    parser = new DOMParser();
    ef = new ErrorStorer();
    parser.setErrorHandler(ef);
    writer = new PrintWriter(new OutputStreamWriter(System.out), true);
}

/**
 * Sets the writer to be used for all output.
 * @param w The writer
 */
public void setWriter(PrintWriter w)
{
    writer = w;
}

/**
 * Translates a URL into a DOM document. To get the data from the
 * servlet the 'get' method is used. 
 * The URL should have the following format:
 * <pre>
 *      http://host/servlet?k1=v1&amp;k2=v2&amp;k3=v3
 * </pre>
 * 
 * @param l the URL 
 *
 * @return the DOM document
 */
public Document getDocument(URL u) 
{
    Document doc = null;
    url = u.toString();
    if (url != null) 
    {
        try 
        {
            ef.resetErrors();
            ef.setURL(url);
            Reader reader = readFromUrl(u);
            if (reader != null)
            {
                InputSource input = new InputSource(reader);
                parser.parse(input);
                doc = parser.getDocument();
                reader.close();
            }
        } 
        catch (Exception exc) 
        {
             writer.println("XMLtoDOM.getDocument(): " + url
                  + " Error: Invalid XML document could not get ROOT "
                  + exc.getMessage());
        }
    }
    return doc;
}

/**
 * Reads the answer from a URL via the HTTP GET method.
 * @return the input stream from the URL to read the answer
 */
private Reader readFromUrl (URL url)
{
    InputStreamReader inStream = null;

    try
    {
        URLConnection conn = url.openConnection();

        // No cache may be used, else we might get an old value !!
        conn.setUseCaches(false);

        // After long fiddling with Netscape, I found out that the
        // content-type has to be set, else the servlet somehow cannot
        // decode the parameters!!!!
        /*
        conn.setRequestProperty("Content-type",
                            "application/x-www-form-urlencoded");
        */

        // Since I only do a GET, I only need Input.
        conn.setDoInput(true);
        conn.setDoOutput(false);

        // Read the answer
        inStream = new InputStreamReader (conn.getInputStream());
    }
    catch (IOException exc)
    {
        writer.println("XMLtoDOM.readFromUrl(): IOException: " +
        exc.getMessage());
    }
    catch (Exception exc)
    {
        writer.println("XMLtoDOM.readFromUrl(): Exception: " + exc.getMessage());
        exc.printStackTrace(writer);
    }
    return inStream;
}



/**
 * The ErrorStorer maps Nodes to errors. It receives a reference
 * to the ErrorTreeFactory in the Constructor.
 *
 * <p>
 * When error is called, it asks the
 * ErrorTreeFactory for the current node, and uses this as the
 * "key" of a Hashtable, with the error as a value. The error
 * value is wrapped up nicely in an ParseError object.
 * </p>
 *
 * <p>
 * It is used in the XML Tutorial to illustrate how to implement
 * the ErrorListener to provide error storage for later reference.
 * </p>
 */
class ErrorStorer implements ErrorHandler
{
    //
    // Data
    //
    Hashtable errorNodes = null;
    String url = null;

/**
 * Constructor
 */
public ErrorStorer()
{
}

protected boolean errorHandlingEnabled()
{
    return true;
}

/**
 * Sets the url so it can be mentioned in any error message.
 */
void setURL(String u)
{
    url = u;
}

/**
 * The client is is allowed to get a reference to the Hashtable,
 * and so could corrupt it, or add to it...
 */
public Hashtable getErrorNodes()
{
    return errorNodes;
}

/**
 * The ParseError object for the node key is returned.
 * If the node doesn't have errors, null is returned.
 */
public Object getError(Node node)
{
    if (errorNodes == null)
        return null;
    return errorNodes.get(node);
}

/**
 * Reset the error storage.
 */
public void resetErrors()
{
    if (errorNodes != null)
        errorNodes.clear();
}

/**
 * Prints a warning message.
 */
public void warning(SAXParseException exc)
{
    handleError(exc, XMLtoDOM.WARNING);
}

/**
 * Prints a error message.
 */
public void error(SAXParseException exc)
{
    handleError(exc, XMLtoDOM.ERROR);
}

/**
 * Prints a fatal error message.
 */
public void fatalError(SAXParseException exc) throws SAXException
{
    handleError(exc, XMLtoDOM.FATAL_ERROR);
}

/**
 * Prints a message.
 */
protected void handleError(SAXParseException exc, String type)
{
    writer.println("ErrorStorer.handleError(): ");

    String str = "\t" + type
        + " at line number " + exc.getLineNumber()
        + ": " + exc.getMessage()
        + " (" + url + ")";

    writer.println(str);

}

} // end class ErrorStorer


} // end class XMLtoDOM


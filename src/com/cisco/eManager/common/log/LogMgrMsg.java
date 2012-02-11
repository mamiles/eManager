/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.log;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Log file managment messages
 * 
 * @version $Revision$ $Date$
**/
public class LogMgrMsg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private GetLogFilesFromDirectories _getLogFilesFromDirectories;

    private GetLogURL _getLogURL;


      //----------------/
     //- Constructors -/
    //----------------/

    public LogMgrMsg() {
        super();
    } //-- com.cisco.eManager.common.log.LogMgrMsg()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'getLogFilesFromDirectories'.
     * 
     * @return the value of field 'getLogFilesFromDirectories'.
    **/
    public GetLogFilesFromDirectories getGetLogFilesFromDirectories()
    {
        return this._getLogFilesFromDirectories;
    } //-- GetLogFilesFromDirectories getGetLogFilesFromDirectories() 

    /**
     * Returns the value of field 'getLogURL'.
     * 
     * @return the value of field 'getLogURL'.
    **/
    public GetLogURL getGetLogURL()
    {
        return this._getLogURL;
    } //-- GetLogURL getGetLogURL() 

    /**
    **/
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'getLogFilesFromDirectories'.
     * 
     * @param getLogFilesFromDirectories the value of field
     * 'getLogFilesFromDirectories'.
    **/
    public void setGetLogFilesFromDirectories(GetLogFilesFromDirectories getLogFilesFromDirectories)
    {
        this._getLogFilesFromDirectories = getLogFilesFromDirectories;
    } //-- void setGetLogFilesFromDirectories(GetLogFilesFromDirectories) 

    /**
     * Sets the value of field 'getLogURL'.
     * 
     * @param getLogURL the value of field 'getLogURL'.
    **/
    public void setGetLogURL(GetLogURL getLogURL)
    {
        this._getLogURL = getLogURL;
    } //-- void setGetLogURL(GetLogURL) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.log.LogMgrMsg unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.log.LogMgrMsg) Unmarshaller.unmarshal(com.cisco.eManager.common.log.LogMgrMsg.class, reader);
    } //-- com.cisco.eManager.common.log.LogMgrMsg unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

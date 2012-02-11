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
 * Response to Log Management request
 * 
 * @version $Revision$ $Date$
**/
public class LogMgrResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private GetLogFilesFromDirectoriesResp _getLogFilesFromDirectoriesResp;

    private GetLogURLResp _getLogURLResp;


      //----------------/
     //- Constructors -/
    //----------------/

    public LogMgrResp() {
        super();
    } //-- com.cisco.eManager.common.log.LogMgrResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'getLogFilesFromDirectoriesResp'.
     * 
     * @return the value of field 'getLogFilesFromDirectoriesResp'.
    **/
    public GetLogFilesFromDirectoriesResp getGetLogFilesFromDirectoriesResp()
    {
        return this._getLogFilesFromDirectoriesResp;
    } //-- GetLogFilesFromDirectoriesResp getGetLogFilesFromDirectoriesResp() 

    /**
     * Returns the value of field 'getLogURLResp'.
     * 
     * @return the value of field 'getLogURLResp'.
    **/
    public GetLogURLResp getGetLogURLResp()
    {
        return this._getLogURLResp;
    } //-- GetLogURLResp getGetLogURLResp() 

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
     * Sets the value of field 'getLogFilesFromDirectoriesResp'.
     * 
     * @param getLogFilesFromDirectoriesResp the value of field
     * 'getLogFilesFromDirectoriesResp'.
    **/
    public void setGetLogFilesFromDirectoriesResp(GetLogFilesFromDirectoriesResp getLogFilesFromDirectoriesResp)
    {
        this._getLogFilesFromDirectoriesResp = getLogFilesFromDirectoriesResp;
    } //-- void setGetLogFilesFromDirectoriesResp(GetLogFilesFromDirectoriesResp) 

    /**
     * Sets the value of field 'getLogURLResp'.
     * 
     * @param getLogURLResp the value of field 'getLogURLResp'.
    **/
    public void setGetLogURLResp(GetLogURLResp getLogURLResp)
    {
        this._getLogURLResp = getLogURLResp;
    } //-- void setGetLogURLResp(GetLogURLResp) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.log.LogMgrResp unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.log.LogMgrResp) Unmarshaller.unmarshal(com.cisco.eManager.common.log.LogMgrResp.class, reader);
    } //-- com.cisco.eManager.common.log.LogMgrResp unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

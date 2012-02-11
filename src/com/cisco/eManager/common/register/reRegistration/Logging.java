/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cisco.eManager.common.register.reRegistration;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Logging.
 * 
 * @version $Revision$ $Date$
 */
public class Logging implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _logDirectories
     */
    private com.cisco.eManager.common.register.reRegistration.LogDirectories _logDirectories;

    /**
     * Field _transport
     */
    private java.lang.String _transport;

    /**
     * Field _userId
     */
    private java.lang.String _userId;

    /**
     * Field _password
     */
    private java.lang.String _password;

    /**
     * Field _unixPrompt
     */
    private java.lang.String _unixPrompt;


      //----------------/
     //- Constructors -/
    //----------------/

    public Logging() {
        super();
    } //-- com.cisco.eManager.common.register.reRegistration.Logging()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'logDirectories'.
     * 
     * @return the value of field 'logDirectories'.
     */
    public com.cisco.eManager.common.register.reRegistration.LogDirectories getLogDirectories()
    {
        return this._logDirectories;
    } //-- com.cisco.eManager.common.register.reRegistration.LogDirectories getLogDirectories() 

    /**
     * Returns the value of field 'password'.
     * 
     * @return the value of field 'password'.
     */
    public java.lang.String getPassword()
    {
        return this._password;
    } //-- java.lang.String getPassword() 

    /**
     * Returns the value of field 'transport'.
     * 
     * @return the value of field 'transport'.
     */
    public java.lang.String getTransport()
    {
        return this._transport;
    } //-- java.lang.String getTransport() 

    /**
     * Returns the value of field 'unixPrompt'.
     * 
     * @return the value of field 'unixPrompt'.
     */
    public java.lang.String getUnixPrompt()
    {
        return this._unixPrompt;
    } //-- java.lang.String getUnixPrompt() 

    /**
     * Returns the value of field 'userId'.
     * 
     * @return the value of field 'userId'.
     */
    public java.lang.String getUserId()
    {
        return this._userId;
    } //-- java.lang.String getUserId() 

    /**
     * Method isValid
     */
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
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'logDirectories'.
     * 
     * @param logDirectories the value of field 'logDirectories'.
     */
    public void setLogDirectories(com.cisco.eManager.common.register.reRegistration.LogDirectories logDirectories)
    {
        this._logDirectories = logDirectories;
    } //-- void setLogDirectories(com.cisco.eManager.common.register.reRegistration.LogDirectories) 

    /**
     * Sets the value of field 'password'.
     * 
     * @param password the value of field 'password'.
     */
    public void setPassword(java.lang.String password)
    {
        this._password = password;
    } //-- void setPassword(java.lang.String) 

    /**
     * Sets the value of field 'transport'.
     * 
     * @param transport the value of field 'transport'.
     */
    public void setTransport(java.lang.String transport)
    {
        this._transport = transport;
    } //-- void setTransport(java.lang.String) 

    /**
     * Sets the value of field 'unixPrompt'.
     * 
     * @param unixPrompt the value of field 'unixPrompt'.
     */
    public void setUnixPrompt(java.lang.String unixPrompt)
    {
        this._unixPrompt = unixPrompt;
    } //-- void setUnixPrompt(java.lang.String) 

    /**
     * Sets the value of field 'userId'.
     * 
     * @param userId the value of field 'userId'.
     */
    public void setUserId(java.lang.String userId)
    {
        this._userId = userId;
    } //-- void setUserId(java.lang.String) 

    /**
     * Method unmarshalLogging
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalLogging(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.register.reRegistration.Logging) Unmarshaller.unmarshal(com.cisco.eManager.common.register.reRegistration.Logging.class, reader);
    } //-- java.lang.Object unmarshalLogging(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

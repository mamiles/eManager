/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cisco.eManager.common.inventory2;

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
 * Class IMsgFindInstrumentations.
 * 
 * @version $Revision$ $Date$
 */
public class IMsgFindInstrumentations implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * zero or one match possible
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _instrumentationId;

    /**
     * zero or one match possible
     */
    private java.lang.String _instrumentationName;

    /**
     * many matches possible
     */
    private com.cisco.eManager.common.inventory2.AppInstanceId _appInstanceId;


      //----------------/
     //- Constructors -/
    //----------------/

    public IMsgFindInstrumentations() {
        super();
    } //-- com.cisco.eManager.common.inventory2.IMsgFindInstrumentations()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'appInstanceId'. The field
     * 'appInstanceId' has the following description: many matches
     * possible
     * 
     * @return the value of field 'appInstanceId'.
     */
    public com.cisco.eManager.common.inventory2.AppInstanceId getAppInstanceId()
    {
        return this._appInstanceId;
    } //-- com.cisco.eManager.common.inventory2.AppInstanceId getAppInstanceId() 

    /**
     * Returns the value of field 'instrumentationId'. The field
     * 'instrumentationId' has the following description: zero or
     * one match possible
     * 
     * @return the value of field 'instrumentationId'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getInstrumentationId()
    {
        return this._instrumentationId;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getInstrumentationId() 

    /**
     * Returns the value of field 'instrumentationName'. The field
     * 'instrumentationName' has the following description: zero or
     * one match possible
     * 
     * @return the value of field 'instrumentationName'.
     */
    public java.lang.String getInstrumentationName()
    {
        return this._instrumentationName;
    } //-- java.lang.String getInstrumentationName() 

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
     * Sets the value of field 'appInstanceId'. The field
     * 'appInstanceId' has the following description: many matches
     * possible
     * 
     * @param appInstanceId the value of field 'appInstanceId'.
     */
    public void setAppInstanceId(com.cisco.eManager.common.inventory2.AppInstanceId appInstanceId)
    {
        this._appInstanceId = appInstanceId;
    } //-- void setAppInstanceId(com.cisco.eManager.common.inventory2.AppInstanceId) 

    /**
     * Sets the value of field 'instrumentationId'. The field
     * 'instrumentationId' has the following description: zero or
     * one match possible
     * 
     * @param instrumentationId the value of field
     * 'instrumentationId'.
     */
    public void setInstrumentationId(com.cisco.eManager.common.inventory2.ManagedObjectId instrumentationId)
    {
        this._instrumentationId = instrumentationId;
    } //-- void setInstrumentationId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Sets the value of field 'instrumentationName'. The field
     * 'instrumentationName' has the following description: zero or
     * one match possible
     * 
     * @param instrumentationName the value of field
     * 'instrumentationName'.
     */
    public void setInstrumentationName(java.lang.String instrumentationName)
    {
        this._instrumentationName = instrumentationName;
    } //-- void setInstrumentationName(java.lang.String) 

    /**
     * Method unmarshalIMsgFindInstrumentations
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalIMsgFindInstrumentations(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.IMsgFindInstrumentations) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.IMsgFindInstrumentations.class, reader);
    } //-- java.lang.Object unmarshalIMsgFindInstrumentations(java.io.Reader) 

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

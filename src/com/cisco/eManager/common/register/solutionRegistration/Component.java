/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cisco.eManager.common.register.solutionRegistration;

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
 * Class Component.
 * 
 * @version $Revision$ $Date$
 */
public class Component implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _appType
     */
    private java.lang.String _appType;

    /**
     * Field _appInstance
     */
    private java.lang.String _appInstance;

    /**
     * Field _appVersion
     */
    private java.lang.String _appVersion;


      //----------------/
     //- Constructors -/
    //----------------/

    public Component() {
        super();
    } //-- com.cisco.eManager.common.register.solutionRegistration.Component()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'appInstance'.
     * 
     * @return the value of field 'appInstance'.
     */
    public java.lang.String getAppInstance()
    {
        return this._appInstance;
    } //-- java.lang.String getAppInstance() 

    /**
     * Returns the value of field 'appType'.
     * 
     * @return the value of field 'appType'.
     */
    public java.lang.String getAppType()
    {
        return this._appType;
    } //-- java.lang.String getAppType() 

    /**
     * Returns the value of field 'appVersion'.
     * 
     * @return the value of field 'appVersion'.
     */
    public java.lang.String getAppVersion()
    {
        return this._appVersion;
    } //-- java.lang.String getAppVersion() 

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
     * Sets the value of field 'appInstance'.
     * 
     * @param appInstance the value of field 'appInstance'.
     */
    public void setAppInstance(java.lang.String appInstance)
    {
        this._appInstance = appInstance;
    } //-- void setAppInstance(java.lang.String) 

    /**
     * Sets the value of field 'appType'.
     * 
     * @param appType the value of field 'appType'.
     */
    public void setAppType(java.lang.String appType)
    {
        this._appType = appType;
    } //-- void setAppType(java.lang.String) 

    /**
     * Sets the value of field 'appVersion'.
     * 
     * @param appVersion the value of field 'appVersion'.
     */
    public void setAppVersion(java.lang.String appVersion)
    {
        this._appVersion = appVersion;
    } //-- void setAppVersion(java.lang.String) 

    /**
     * Method unmarshalComponent
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalComponent(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.register.solutionRegistration.Component) Unmarshaller.unmarshal(com.cisco.eManager.common.register.solutionRegistration.Component.class, reader);
    } //-- java.lang.Object unmarshalComponent(java.io.Reader) 

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

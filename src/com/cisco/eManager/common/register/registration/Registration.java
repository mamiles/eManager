/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cisco.eManager.common.register.registration;

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
 * Class Registration.
 * 
 * @version $Revision$ $Date$
 */
public class Registration implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _hostName
     */
    private java.lang.String _hostName;

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

    /**
     * Field _appMgmtPolicies
     */
    private com.cisco.eManager.common.register.registration.AppMgmtPolicies _appMgmtPolicies;

    /**
     * Field _processPropertyFile
     */
    private java.lang.String _processPropertyFile;

    /**
     * Field _logging
     */
    private com.cisco.eManager.common.register.registration.Logging _logging;


      //----------------/
     //- Constructors -/
    //----------------/

    public Registration() {
        super();
    } //-- com.cisco.eManager.common.register.registration.Registration()


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
     * Returns the value of field 'appMgmtPolicies'.
     * 
     * @return the value of field 'appMgmtPolicies'.
     */
    public com.cisco.eManager.common.register.registration.AppMgmtPolicies getAppMgmtPolicies()
    {
        return this._appMgmtPolicies;
    } //-- com.cisco.eManager.common.register.registration.AppMgmtPolicies getAppMgmtPolicies() 

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
     * Returns the value of field 'hostName'.
     * 
     * @return the value of field 'hostName'.
     */
    public java.lang.String getHostName()
    {
        return this._hostName;
    } //-- java.lang.String getHostName() 

    /**
     * Returns the value of field 'logging'.
     * 
     * @return the value of field 'logging'.
     */
    public com.cisco.eManager.common.register.registration.Logging getLogging()
    {
        return this._logging;
    } //-- com.cisco.eManager.common.register.registration.Logging getLogging() 

    /**
     * Returns the value of field 'processPropertyFile'.
     * 
     * @return the value of field 'processPropertyFile'.
     */
    public java.lang.String getProcessPropertyFile()
    {
        return this._processPropertyFile;
    } //-- java.lang.String getProcessPropertyFile() 

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
     * Sets the value of field 'appMgmtPolicies'.
     * 
     * @param appMgmtPolicies the value of field 'appMgmtPolicies'.
     */
    public void setAppMgmtPolicies(com.cisco.eManager.common.register.registration.AppMgmtPolicies appMgmtPolicies)
    {
        this._appMgmtPolicies = appMgmtPolicies;
    } //-- void setAppMgmtPolicies(com.cisco.eManager.common.register.registration.AppMgmtPolicies) 

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
     * Sets the value of field 'hostName'.
     * 
     * @param hostName the value of field 'hostName'.
     */
    public void setHostName(java.lang.String hostName)
    {
        this._hostName = hostName;
    } //-- void setHostName(java.lang.String) 

    /**
     * Sets the value of field 'logging'.
     * 
     * @param logging the value of field 'logging'.
     */
    public void setLogging(com.cisco.eManager.common.register.registration.Logging logging)
    {
        this._logging = logging;
    } //-- void setLogging(com.cisco.eManager.common.register.registration.Logging) 

    /**
     * Sets the value of field 'processPropertyFile'.
     * 
     * @param processPropertyFile the value of field
     * 'processPropertyFile'.
     */
    public void setProcessPropertyFile(java.lang.String processPropertyFile)
    {
        this._processPropertyFile = processPropertyFile;
    } //-- void setProcessPropertyFile(java.lang.String) 

    /**
     * Method unmarshalRegistration
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalRegistration(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.register.registration.Registration) Unmarshaller.unmarshal(com.cisco.eManager.common.register.registration.Registration.class, reader);
    } //-- java.lang.Object unmarshalRegistration(java.io.Reader) 

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

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
 * 
 * 
 * @version $Revision$ $Date$
**/
public class LogFileChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private AppTypeAppInstName _appTypeAppInstName;

    private AppInstanceId _appInstanceId;

    private AppViewAppInstanceFDN _appViewAppInstanceFDN;

    private PhysicalViewAppInstanceFDN _physicalViewAppInstanceFDN;


      //----------------/
     //- Constructors -/
    //----------------/

    public LogFileChoice() {
        super();
    } //-- com.cisco.eManager.common.log.LogFileChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'appInstanceId'.
     * 
     * @return the value of field 'appInstanceId'.
    **/
    public AppInstanceId getAppInstanceId()
    {
        return this._appInstanceId;
    } //-- AppInstanceId getAppInstanceId() 

    /**
     * Returns the value of field 'appTypeAppInstName'.
     * 
     * @return the value of field 'appTypeAppInstName'.
    **/
    public AppTypeAppInstName getAppTypeAppInstName()
    {
        return this._appTypeAppInstName;
    } //-- AppTypeAppInstName getAppTypeAppInstName() 

    /**
     * Returns the value of field 'appViewAppInstanceFDN'.
     * 
     * @return the value of field 'appViewAppInstanceFDN'.
    **/
    public AppViewAppInstanceFDN getAppViewAppInstanceFDN()
    {
        return this._appViewAppInstanceFDN;
    } //-- AppViewAppInstanceFDN getAppViewAppInstanceFDN() 

    /**
     * Returns the value of field 'physicalViewAppInstanceFDN'.
     * 
     * @return the value of field 'physicalViewAppInstanceFDN'.
    **/
    public PhysicalViewAppInstanceFDN getPhysicalViewAppInstanceFDN()
    {
        return this._physicalViewAppInstanceFDN;
    } //-- PhysicalViewAppInstanceFDN getPhysicalViewAppInstanceFDN() 

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
     * Sets the value of field 'appInstanceId'.
     * 
     * @param appInstanceId the value of field 'appInstanceId'.
    **/
    public void setAppInstanceId(AppInstanceId appInstanceId)
    {
        this._appInstanceId = appInstanceId;
    } //-- void setAppInstanceId(AppInstanceId) 

    /**
     * Sets the value of field 'appTypeAppInstName'.
     * 
     * @param appTypeAppInstName the value of field
     * 'appTypeAppInstName'.
    **/
    public void setAppTypeAppInstName(AppTypeAppInstName appTypeAppInstName)
    {
        this._appTypeAppInstName = appTypeAppInstName;
    } //-- void setAppTypeAppInstName(AppTypeAppInstName) 

    /**
     * Sets the value of field 'appViewAppInstanceFDN'.
     * 
     * @param appViewAppInstanceFDN the value of field
     * 'appViewAppInstanceFDN'.
    **/
    public void setAppViewAppInstanceFDN(AppViewAppInstanceFDN appViewAppInstanceFDN)
    {
        this._appViewAppInstanceFDN = appViewAppInstanceFDN;
    } //-- void setAppViewAppInstanceFDN(AppViewAppInstanceFDN) 

    /**
     * Sets the value of field 'physicalViewAppInstanceFDN'.
     * 
     * @param physicalViewAppInstanceFDN the value of field
     * 'physicalViewAppInstanceFDN'.
    **/
    public void setPhysicalViewAppInstanceFDN(PhysicalViewAppInstanceFDN physicalViewAppInstanceFDN)
    {
        this._physicalViewAppInstanceFDN = physicalViewAppInstanceFDN;
    } //-- void setPhysicalViewAppInstanceFDN(PhysicalViewAppInstanceFDN) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.log.LogFileChoice unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.log.LogFileChoice) Unmarshaller.unmarshal(com.cisco.eManager.common.log.LogFileChoice.class, reader);
    } //-- com.cisco.eManager.common.log.LogFileChoice unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

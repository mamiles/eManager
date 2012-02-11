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
 * Class AiMsgGetAppInstances.
 * 
 * @version $Revision$ $Date$
 */
public class AiMsgGetAppInstances implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _appInstanceId
     */
    private com.cisco.eManager.common.inventory2.AppInstanceId _appInstanceId;

    /**
     * Field _hostId
     */
    private com.cisco.eManager.common.inventory2.HostId _hostId;

    /**
     * Field _appTypeId
     */
    private com.cisco.eManager.common.inventory2.AppTypeId _appTypeId;

    /**
     * Field _all
     */
    private java.lang.String _all;


      //----------------/
     //- Constructors -/
    //----------------/

    public AiMsgGetAppInstances() {
        super();
    } //-- com.cisco.eManager.common.inventory2.AiMsgGetAppInstances()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'all'.
     * 
     * @return the value of field 'all'.
     */
    public java.lang.String getAll()
    {
        return this._all;
    } //-- java.lang.String getAll() 

    /**
     * Returns the value of field 'appInstanceId'.
     * 
     * @return the value of field 'appInstanceId'.
     */
    public com.cisco.eManager.common.inventory2.AppInstanceId getAppInstanceId()
    {
        return this._appInstanceId;
    } //-- com.cisco.eManager.common.inventory2.AppInstanceId getAppInstanceId() 

    /**
     * Returns the value of field 'appTypeId'.
     * 
     * @return the value of field 'appTypeId'.
     */
    public com.cisco.eManager.common.inventory2.AppTypeId getAppTypeId()
    {
        return this._appTypeId;
    } //-- com.cisco.eManager.common.inventory2.AppTypeId getAppTypeId() 

    /**
     * Returns the value of field 'hostId'.
     * 
     * @return the value of field 'hostId'.
     */
    public com.cisco.eManager.common.inventory2.HostId getHostId()
    {
        return this._hostId;
    } //-- com.cisco.eManager.common.inventory2.HostId getHostId() 

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
     * Sets the value of field 'all'.
     * 
     * @param all the value of field 'all'.
     */
    public void setAll(java.lang.String all)
    {
        this._all = all;
    } //-- void setAll(java.lang.String) 

    /**
     * Sets the value of field 'appInstanceId'.
     * 
     * @param appInstanceId the value of field 'appInstanceId'.
     */
    public void setAppInstanceId(com.cisco.eManager.common.inventory2.AppInstanceId appInstanceId)
    {
        this._appInstanceId = appInstanceId;
    } //-- void setAppInstanceId(com.cisco.eManager.common.inventory2.AppInstanceId) 

    /**
     * Sets the value of field 'appTypeId'.
     * 
     * @param appTypeId the value of field 'appTypeId'.
     */
    public void setAppTypeId(com.cisco.eManager.common.inventory2.AppTypeId appTypeId)
    {
        this._appTypeId = appTypeId;
    } //-- void setAppTypeId(com.cisco.eManager.common.inventory2.AppTypeId) 

    /**
     * Sets the value of field 'hostId'.
     * 
     * @param hostId the value of field 'hostId'.
     */
    public void setHostId(com.cisco.eManager.common.inventory2.HostId hostId)
    {
        this._hostId = hostId;
    } //-- void setHostId(com.cisco.eManager.common.inventory2.HostId) 

    /**
     * Method unmarshalAiMsgGetAppInstances
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAiMsgGetAppInstances(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.AiMsgGetAppInstances) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.AiMsgGetAppInstances.class, reader);
    } //-- java.lang.Object unmarshalAiMsgGetAppInstances(java.io.Reader) 

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

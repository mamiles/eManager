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
 * Class AppInstanceResp.
 * 
 * @version $Revision$ $Date$
 */
public class AppInstanceResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _aiRspGetAppInstances
     */
    private com.cisco.eManager.common.inventory2.AiRspGetAppInstances _aiRspGetAppInstances;

    /**
     * Field _aiRspManage
     */
    private com.cisco.eManager.common.inventory2.AiRspManage _aiRspManage;

    /**
     * Field _aiRspUnmanage
     */
    private com.cisco.eManager.common.inventory2.AiRspUnmanage _aiRspUnmanage;


      //----------------/
     //- Constructors -/
    //----------------/

    public AppInstanceResp() {
        super();
    } //-- com.cisco.eManager.common.inventory2.AppInstanceResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'aiRspGetAppInstances'.
     * 
     * @return the value of field 'aiRspGetAppInstances'.
     */
    public com.cisco.eManager.common.inventory2.AiRspGetAppInstances getAiRspGetAppInstances()
    {
        return this._aiRspGetAppInstances;
    } //-- com.cisco.eManager.common.inventory2.AiRspGetAppInstances getAiRspGetAppInstances() 

    /**
     * Returns the value of field 'aiRspManage'.
     * 
     * @return the value of field 'aiRspManage'.
     */
    public com.cisco.eManager.common.inventory2.AiRspManage getAiRspManage()
    {
        return this._aiRspManage;
    } //-- com.cisco.eManager.common.inventory2.AiRspManage getAiRspManage() 

    /**
     * Returns the value of field 'aiRspUnmanage'.
     * 
     * @return the value of field 'aiRspUnmanage'.
     */
    public com.cisco.eManager.common.inventory2.AiRspUnmanage getAiRspUnmanage()
    {
        return this._aiRspUnmanage;
    } //-- com.cisco.eManager.common.inventory2.AiRspUnmanage getAiRspUnmanage() 

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
     * Sets the value of field 'aiRspGetAppInstances'.
     * 
     * @param aiRspGetAppInstances the value of field
     * 'aiRspGetAppInstances'.
     */
    public void setAiRspGetAppInstances(com.cisco.eManager.common.inventory2.AiRspGetAppInstances aiRspGetAppInstances)
    {
        this._aiRspGetAppInstances = aiRspGetAppInstances;
    } //-- void setAiRspGetAppInstances(com.cisco.eManager.common.inventory2.AiRspGetAppInstances) 

    /**
     * Sets the value of field 'aiRspManage'.
     * 
     * @param aiRspManage the value of field 'aiRspManage'.
     */
    public void setAiRspManage(com.cisco.eManager.common.inventory2.AiRspManage aiRspManage)
    {
        this._aiRspManage = aiRspManage;
    } //-- void setAiRspManage(com.cisco.eManager.common.inventory2.AiRspManage) 

    /**
     * Sets the value of field 'aiRspUnmanage'.
     * 
     * @param aiRspUnmanage the value of field 'aiRspUnmanage'.
     */
    public void setAiRspUnmanage(com.cisco.eManager.common.inventory2.AiRspUnmanage aiRspUnmanage)
    {
        this._aiRspUnmanage = aiRspUnmanage;
    } //-- void setAiRspUnmanage(com.cisco.eManager.common.inventory2.AiRspUnmanage) 

    /**
     * Method unmarshalAppInstanceResp
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAppInstanceResp(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.AppInstanceResp) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.AppInstanceResp.class, reader);
    } //-- java.lang.Object unmarshalAppInstanceResp(java.io.Reader) 

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

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
 * Class AppInstanceMsg.
 * 
 * @version $Revision$ $Date$
 */
public class AppInstanceMsg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _aiMsgGetAppInstances
     */
    private com.cisco.eManager.common.inventory2.AiMsgGetAppInstances _aiMsgGetAppInstances;

    /**
     * Field _aiMsgManage
     */
    private com.cisco.eManager.common.inventory2.AppInstanceId _aiMsgManage;

    /**
     * Field _aiMsgUnmanage
     */
    private com.cisco.eManager.common.inventory2.AppInstanceId _aiMsgUnmanage;


      //----------------/
     //- Constructors -/
    //----------------/

    public AppInstanceMsg() {
        super();
    } //-- com.cisco.eManager.common.inventory2.AppInstanceMsg()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'aiMsgGetAppInstances'.
     * 
     * @return the value of field 'aiMsgGetAppInstances'.
     */
    public com.cisco.eManager.common.inventory2.AiMsgGetAppInstances getAiMsgGetAppInstances()
    {
        return this._aiMsgGetAppInstances;
    } //-- com.cisco.eManager.common.inventory2.AiMsgGetAppInstances getAiMsgGetAppInstances() 

    /**
     * Returns the value of field 'aiMsgManage'.
     * 
     * @return the value of field 'aiMsgManage'.
     */
    public com.cisco.eManager.common.inventory2.AppInstanceId getAiMsgManage()
    {
        return this._aiMsgManage;
    } //-- com.cisco.eManager.common.inventory2.AppInstanceId getAiMsgManage() 

    /**
     * Returns the value of field 'aiMsgUnmanage'.
     * 
     * @return the value of field 'aiMsgUnmanage'.
     */
    public com.cisco.eManager.common.inventory2.AppInstanceId getAiMsgUnmanage()
    {
        return this._aiMsgUnmanage;
    } //-- com.cisco.eManager.common.inventory2.AppInstanceId getAiMsgUnmanage() 

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
     * Sets the value of field 'aiMsgGetAppInstances'.
     * 
     * @param aiMsgGetAppInstances the value of field
     * 'aiMsgGetAppInstances'.
     */
    public void setAiMsgGetAppInstances(com.cisco.eManager.common.inventory2.AiMsgGetAppInstances aiMsgGetAppInstances)
    {
        this._aiMsgGetAppInstances = aiMsgGetAppInstances;
    } //-- void setAiMsgGetAppInstances(com.cisco.eManager.common.inventory2.AiMsgGetAppInstances) 

    /**
     * Sets the value of field 'aiMsgManage'.
     * 
     * @param aiMsgManage the value of field 'aiMsgManage'.
     */
    public void setAiMsgManage(com.cisco.eManager.common.inventory2.AppInstanceId aiMsgManage)
    {
        this._aiMsgManage = aiMsgManage;
    } //-- void setAiMsgManage(com.cisco.eManager.common.inventory2.AppInstanceId) 

    /**
     * Sets the value of field 'aiMsgUnmanage'.
     * 
     * @param aiMsgUnmanage the value of field 'aiMsgUnmanage'.
     */
    public void setAiMsgUnmanage(com.cisco.eManager.common.inventory2.AppInstanceId aiMsgUnmanage)
    {
        this._aiMsgUnmanage = aiMsgUnmanage;
    } //-- void setAiMsgUnmanage(com.cisco.eManager.common.inventory2.AppInstanceId) 

    /**
     * Method unmarshalAppInstanceMsg
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAppInstanceMsg(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.AppInstanceMsg) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.AppInstanceMsg.class, reader);
    } //-- java.lang.Object unmarshalAppInstanceMsg(java.io.Reader) 

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

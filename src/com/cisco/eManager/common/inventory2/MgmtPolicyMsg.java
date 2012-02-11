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
 * Class MgmtPolicyMsg.
 * 
 * @version $Revision$ $Date$
 */
public class MgmtPolicyMsg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mpMsgGetMgmtPolicies
     */
    private com.cisco.eManager.common.inventory2.MpMsgGetMgmtPolicies _mpMsgGetMgmtPolicies;


      //----------------/
     //- Constructors -/
    //----------------/

    public MgmtPolicyMsg() {
        super();
    } //-- com.cisco.eManager.common.inventory2.MgmtPolicyMsg()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'mpMsgGetMgmtPolicies'.
     * 
     * @return the value of field 'mpMsgGetMgmtPolicies'.
     */
    public com.cisco.eManager.common.inventory2.MpMsgGetMgmtPolicies getMpMsgGetMgmtPolicies()
    {
        return this._mpMsgGetMgmtPolicies;
    } //-- com.cisco.eManager.common.inventory2.MpMsgGetMgmtPolicies getMpMsgGetMgmtPolicies() 

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
     * Sets the value of field 'mpMsgGetMgmtPolicies'.
     * 
     * @param mpMsgGetMgmtPolicies the value of field
     * 'mpMsgGetMgmtPolicies'.
     */
    public void setMpMsgGetMgmtPolicies(com.cisco.eManager.common.inventory2.MpMsgGetMgmtPolicies mpMsgGetMgmtPolicies)
    {
        this._mpMsgGetMgmtPolicies = mpMsgGetMgmtPolicies;
    } //-- void setMpMsgGetMgmtPolicies(com.cisco.eManager.common.inventory2.MpMsgGetMgmtPolicies) 

    /**
     * Method unmarshalMgmtPolicyMsg
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalMgmtPolicyMsg(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.MgmtPolicyMsg) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.MgmtPolicyMsg.class, reader);
    } //-- java.lang.Object unmarshalMgmtPolicyMsg(java.io.Reader) 

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

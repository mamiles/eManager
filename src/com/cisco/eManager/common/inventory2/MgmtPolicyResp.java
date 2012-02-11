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
 * Class MgmtPolicyResp.
 * 
 * @version $Revision$ $Date$
 */
public class MgmtPolicyResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mpRspGetMgmtPolicies
     */
    private com.cisco.eManager.common.inventory2.MpRspGetMgmtPolicies _mpRspGetMgmtPolicies;


      //----------------/
     //- Constructors -/
    //----------------/

    public MgmtPolicyResp() {
        super();
    } //-- com.cisco.eManager.common.inventory2.MgmtPolicyResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'mpRspGetMgmtPolicies'.
     * 
     * @return the value of field 'mpRspGetMgmtPolicies'.
     */
    public com.cisco.eManager.common.inventory2.MpRspGetMgmtPolicies getMpRspGetMgmtPolicies()
    {
        return this._mpRspGetMgmtPolicies;
    } //-- com.cisco.eManager.common.inventory2.MpRspGetMgmtPolicies getMpRspGetMgmtPolicies() 

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
     * Sets the value of field 'mpRspGetMgmtPolicies'.
     * 
     * @param mpRspGetMgmtPolicies the value of field
     * 'mpRspGetMgmtPolicies'.
     */
    public void setMpRspGetMgmtPolicies(com.cisco.eManager.common.inventory2.MpRspGetMgmtPolicies mpRspGetMgmtPolicies)
    {
        this._mpRspGetMgmtPolicies = mpRspGetMgmtPolicies;
    } //-- void setMpRspGetMgmtPolicies(com.cisco.eManager.common.inventory2.MpRspGetMgmtPolicies) 

    /**
     * Method unmarshalMgmtPolicyResp
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalMgmtPolicyResp(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.MgmtPolicyResp) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.MgmtPolicyResp.class, reader);
    } //-- java.lang.Object unmarshalMgmtPolicyResp(java.io.Reader) 

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

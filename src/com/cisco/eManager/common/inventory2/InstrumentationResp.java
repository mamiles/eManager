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
 * Class InstrumentationResp.
 * 
 * @version $Revision$ $Date$
 */
public class InstrumentationResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _iRspFindInstrumentations
     */
    private com.cisco.eManager.common.inventory2.IRspFindInstrumentations _iRspFindInstrumentations;

    /**
     * Field _iRspGetMethods
     */
    private com.cisco.eManager.common.inventory2.IRspGetMethods _iRspGetMethods;

    /**
     * Field _iRspInvokeMethod
     */
    private com.cisco.eManager.common.inventory2.IRspInvokeMethod _iRspInvokeMethod;


      //----------------/
     //- Constructors -/
    //----------------/

    public InstrumentationResp() {
        super();
    } //-- com.cisco.eManager.common.inventory2.InstrumentationResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'iRspFindInstrumentations'.
     * 
     * @return the value of field 'iRspFindInstrumentations'.
     */
    public com.cisco.eManager.common.inventory2.IRspFindInstrumentations getIRspFindInstrumentations()
    {
        return this._iRspFindInstrumentations;
    } //-- com.cisco.eManager.common.inventory2.IRspFindInstrumentations getIRspFindInstrumentations() 

    /**
     * Returns the value of field 'iRspGetMethods'.
     * 
     * @return the value of field 'iRspGetMethods'.
     */
    public com.cisco.eManager.common.inventory2.IRspGetMethods getIRspGetMethods()
    {
        return this._iRspGetMethods;
    } //-- com.cisco.eManager.common.inventory2.IRspGetMethods getIRspGetMethods() 

    /**
     * Returns the value of field 'iRspInvokeMethod'.
     * 
     * @return the value of field 'iRspInvokeMethod'.
     */
    public com.cisco.eManager.common.inventory2.IRspInvokeMethod getIRspInvokeMethod()
    {
        return this._iRspInvokeMethod;
    } //-- com.cisco.eManager.common.inventory2.IRspInvokeMethod getIRspInvokeMethod() 

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
     * Sets the value of field 'iRspFindInstrumentations'.
     * 
     * @param iRspFindInstrumentations the value of field
     * 'iRspFindInstrumentations'.
     */
    public void setIRspFindInstrumentations(com.cisco.eManager.common.inventory2.IRspFindInstrumentations iRspFindInstrumentations)
    {
        this._iRspFindInstrumentations = iRspFindInstrumentations;
    } //-- void setIRspFindInstrumentations(com.cisco.eManager.common.inventory2.IRspFindInstrumentations) 

    /**
     * Sets the value of field 'iRspGetMethods'.
     * 
     * @param iRspGetMethods the value of field 'iRspGetMethods'.
     */
    public void setIRspGetMethods(com.cisco.eManager.common.inventory2.IRspGetMethods iRspGetMethods)
    {
        this._iRspGetMethods = iRspGetMethods;
    } //-- void setIRspGetMethods(com.cisco.eManager.common.inventory2.IRspGetMethods) 

    /**
     * Sets the value of field 'iRspInvokeMethod'.
     * 
     * @param iRspInvokeMethod the value of field 'iRspInvokeMethod'
     */
    public void setIRspInvokeMethod(com.cisco.eManager.common.inventory2.IRspInvokeMethod iRspInvokeMethod)
    {
        this._iRspInvokeMethod = iRspInvokeMethod;
    } //-- void setIRspInvokeMethod(com.cisco.eManager.common.inventory2.IRspInvokeMethod) 

    /**
     * Method unmarshalInstrumentationResp
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalInstrumentationResp(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.InstrumentationResp) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.InstrumentationResp.class, reader);
    } //-- java.lang.Object unmarshalInstrumentationResp(java.io.Reader) 

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

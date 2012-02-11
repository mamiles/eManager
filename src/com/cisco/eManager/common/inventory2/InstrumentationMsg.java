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
 * Class InstrumentationMsg.
 * 
 * @version $Revision$ $Date$
 */
public class InstrumentationMsg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _iMsgFindInstrumentations
     */
    private com.cisco.eManager.common.inventory2.IMsgFindInstrumentations _iMsgFindInstrumentations;

    /**
     * Field _iMsgGetMethods
     */
    private com.cisco.eManager.common.inventory2.IMsgGetMethods _iMsgGetMethods;

    /**
     * Field _iMsgInvokeMethod
     */
    private com.cisco.eManager.common.inventory2.IMsgInvokeMethod _iMsgInvokeMethod;


      //----------------/
     //- Constructors -/
    //----------------/

    public InstrumentationMsg() {
        super();
    } //-- com.cisco.eManager.common.inventory2.InstrumentationMsg()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'iMsgFindInstrumentations'.
     * 
     * @return the value of field 'iMsgFindInstrumentations'.
     */
    public com.cisco.eManager.common.inventory2.IMsgFindInstrumentations getIMsgFindInstrumentations()
    {
        return this._iMsgFindInstrumentations;
    } //-- com.cisco.eManager.common.inventory2.IMsgFindInstrumentations getIMsgFindInstrumentations() 

    /**
     * Returns the value of field 'iMsgGetMethods'.
     * 
     * @return the value of field 'iMsgGetMethods'.
     */
    public com.cisco.eManager.common.inventory2.IMsgGetMethods getIMsgGetMethods()
    {
        return this._iMsgGetMethods;
    } //-- com.cisco.eManager.common.inventory2.IMsgGetMethods getIMsgGetMethods() 

    /**
     * Returns the value of field 'iMsgInvokeMethod'.
     * 
     * @return the value of field 'iMsgInvokeMethod'.
     */
    public com.cisco.eManager.common.inventory2.IMsgInvokeMethod getIMsgInvokeMethod()
    {
        return this._iMsgInvokeMethod;
    } //-- com.cisco.eManager.common.inventory2.IMsgInvokeMethod getIMsgInvokeMethod() 

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
     * Sets the value of field 'iMsgFindInstrumentations'.
     * 
     * @param iMsgFindInstrumentations the value of field
     * 'iMsgFindInstrumentations'.
     */
    public void setIMsgFindInstrumentations(com.cisco.eManager.common.inventory2.IMsgFindInstrumentations iMsgFindInstrumentations)
    {
        this._iMsgFindInstrumentations = iMsgFindInstrumentations;
    } //-- void setIMsgFindInstrumentations(com.cisco.eManager.common.inventory2.IMsgFindInstrumentations) 

    /**
     * Sets the value of field 'iMsgGetMethods'.
     * 
     * @param iMsgGetMethods the value of field 'iMsgGetMethods'.
     */
    public void setIMsgGetMethods(com.cisco.eManager.common.inventory2.IMsgGetMethods iMsgGetMethods)
    {
        this._iMsgGetMethods = iMsgGetMethods;
    } //-- void setIMsgGetMethods(com.cisco.eManager.common.inventory2.IMsgGetMethods) 

    /**
     * Sets the value of field 'iMsgInvokeMethod'.
     * 
     * @param iMsgInvokeMethod the value of field 'iMsgInvokeMethod'
     */
    public void setIMsgInvokeMethod(com.cisco.eManager.common.inventory2.IMsgInvokeMethod iMsgInvokeMethod)
    {
        this._iMsgInvokeMethod = iMsgInvokeMethod;
    } //-- void setIMsgInvokeMethod(com.cisco.eManager.common.inventory2.IMsgInvokeMethod) 

    /**
     * Method unmarshalInstrumentationMsg
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalInstrumentationMsg(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.InstrumentationMsg) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.InstrumentationMsg.class, reader);
    } //-- java.lang.Object unmarshalInstrumentationMsg(java.io.Reader) 

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

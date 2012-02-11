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
 * Class SearchCriteria.
 * 
 * @version $Revision$ $Date$
 */
public class SearchCriteria implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _methodName
     */
    private java.lang.String _methodName;

    /**
     * Field _all
     */
    private java.lang.String _all;


      //----------------/
     //- Constructors -/
    //----------------/

    public SearchCriteria() {
        super();
    } //-- com.cisco.eManager.common.inventory2.SearchCriteria()


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
     * Returns the value of field 'methodName'.
     * 
     * @return the value of field 'methodName'.
     */
    public java.lang.String getMethodName()
    {
        return this._methodName;
    } //-- java.lang.String getMethodName() 

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
     * Sets the value of field 'methodName'.
     * 
     * @param methodName the value of field 'methodName'.
     */
    public void setMethodName(java.lang.String methodName)
    {
        this._methodName = methodName;
    } //-- void setMethodName(java.lang.String) 

    /**
     * Method unmarshalSearchCriteria
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSearchCriteria(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.SearchCriteria) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.SearchCriteria.class, reader);
    } //-- java.lang.Object unmarshalSearchCriteria(java.io.Reader) 

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

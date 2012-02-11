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
 * Class IMsgGetMethods.
 * 
 * @version $Revision$ $Date$
 */
public class IMsgGetMethods implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _instrumentationId
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _instrumentationId;

    /**
     * Field _searchCriteria
     */
    private com.cisco.eManager.common.inventory2.SearchCriteria _searchCriteria;


      //----------------/
     //- Constructors -/
    //----------------/

    public IMsgGetMethods() {
        super();
    } //-- com.cisco.eManager.common.inventory2.IMsgGetMethods()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'instrumentationId'.
     * 
     * @return the value of field 'instrumentationId'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getInstrumentationId()
    {
        return this._instrumentationId;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getInstrumentationId() 

    /**
     * Returns the value of field 'searchCriteria'.
     * 
     * @return the value of field 'searchCriteria'.
     */
    public com.cisco.eManager.common.inventory2.SearchCriteria getSearchCriteria()
    {
        return this._searchCriteria;
    } //-- com.cisco.eManager.common.inventory2.SearchCriteria getSearchCriteria() 

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
     * Sets the value of field 'instrumentationId'.
     * 
     * @param instrumentationId the value of field
     * 'instrumentationId'.
     */
    public void setInstrumentationId(com.cisco.eManager.common.inventory2.ManagedObjectId instrumentationId)
    {
        this._instrumentationId = instrumentationId;
    } //-- void setInstrumentationId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Sets the value of field 'searchCriteria'.
     * 
     * @param searchCriteria the value of field 'searchCriteria'.
     */
    public void setSearchCriteria(com.cisco.eManager.common.inventory2.SearchCriteria searchCriteria)
    {
        this._searchCriteria = searchCriteria;
    } //-- void setSearchCriteria(com.cisco.eManager.common.inventory2.SearchCriteria) 

    /**
     * Method unmarshalIMsgGetMethods
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalIMsgGetMethods(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.IMsgGetMethods) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.IMsgGetMethods.class, reader);
    } //-- java.lang.Object unmarshalIMsgGetMethods(java.io.Reader) 

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

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
 * Class ManagedObjectId.
 * 
 * @version $Revision$ $Date$
 */
public class ManagedObjectId implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _objectKey
     */
    private long _objectKey;

    /**
     * keeps track of state for field: _objectKey
     */
    private boolean _has_objectKey;

    /**
     * Field _objectType
     */
    private com.cisco.eManager.common.inventory2.ObjectType _objectType;


      //----------------/
     //- Constructors -/
    //----------------/

    public ManagedObjectId() {
        super();
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'objectKey'.
     * 
     * @return the value of field 'objectKey'.
     */
    public long getObjectKey()
    {
        return this._objectKey;
    } //-- long getObjectKey() 

    /**
     * Returns the value of field 'objectType'.
     * 
     * @return the value of field 'objectType'.
     */
    public com.cisco.eManager.common.inventory2.ObjectType getObjectType()
    {
        return this._objectType;
    } //-- com.cisco.eManager.common.inventory2.ObjectType getObjectType() 

    /**
     * Method hasObjectKey
     */
    public boolean hasObjectKey()
    {
        return this._has_objectKey;
    } //-- boolean hasObjectKey() 

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
     * Sets the value of field 'objectKey'.
     * 
     * @param objectKey the value of field 'objectKey'.
     */
    public void setObjectKey(long objectKey)
    {
        this._objectKey = objectKey;
        this._has_objectKey = true;
    } //-- void setObjectKey(long) 

    /**
     * Sets the value of field 'objectType'.
     * 
     * @param objectType the value of field 'objectType'.
     */
    public void setObjectType(com.cisco.eManager.common.inventory2.ObjectType objectType)
    {
        this._objectType = objectType;
    } //-- void setObjectType(com.cisco.eManager.common.inventory2.ObjectType) 

    /**
     * Method unmarshalManagedObjectId
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalManagedObjectId(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.ManagedObjectId.class, reader);
    } //-- java.lang.Object unmarshalManagedObjectId(java.io.Reader) 

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

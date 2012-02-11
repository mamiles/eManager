/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.event2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public abstract class ManagedObjectIdType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private ObjectType _objectType;

    private long _objectKey;

    /**
     * keeps track of state for field: _objectKey
    **/
    private boolean _has_objectKey;


      //----------------/
     //- Constructors -/
    //----------------/

    public ManagedObjectIdType() {
        super();
    } //-- com.cisco.eManager.common.event2.ManagedObjectIdType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'objectKey'.
     * 
     * @return the value of field 'objectKey'.
    **/
    public long getObjectKey()
    {
        return this._objectKey;
    } //-- long getObjectKey() 

    /**
     * Returns the value of field 'objectType'.
     * 
     * @return the value of field 'objectType'.
    **/
    public ObjectType getObjectType()
    {
        return this._objectType;
    } //-- ObjectType getObjectType() 

    /**
    **/
    public boolean hasObjectKey()
    {
        return this._has_objectKey;
    } //-- boolean hasObjectKey() 

    /**
    **/
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
     * 
     * 
     * @param out
    **/
    public abstract void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * 
     * 
     * @param handler
    **/
    public abstract void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * Sets the value of field 'objectKey'.
     * 
     * @param objectKey the value of field 'objectKey'.
    **/
    public void setObjectKey(long objectKey)
    {
        this._objectKey = objectKey;
        this._has_objectKey = true;
    } //-- void setObjectKey(long) 

    /**
     * Sets the value of field 'objectType'.
     * 
     * @param objectType the value of field 'objectType'.
    **/
    public void setObjectType(ObjectType objectType)
    {
        this._objectType = objectType;
    } //-- void setObjectType(ObjectType) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

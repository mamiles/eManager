/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.log;

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
 * ManagedObjectId - LogMgr supports only AppInstanceType
 * 
 * @version $Revision$ $Date$
**/
public abstract class MyManagedObjectId implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private long _objectKey;

    /**
     * keeps track of state for field: _objectKey
    **/
    private boolean _has_objectKey;

    private ObjectType _objectType;

    private java.lang.String _searchFilter;


      //----------------/
     //- Constructors -/
    //----------------/

    public MyManagedObjectId() {
        super();
    } //-- com.cisco.eManager.common.log.MyManagedObjectId()


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
     * Returns the value of field 'searchFilter'.
     * 
     * @return the value of field 'searchFilter'.
    **/
    public java.lang.String getSearchFilter()
    {
        return this._searchFilter;
    } //-- java.lang.String getSearchFilter() 

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
     * Sets the value of field 'searchFilter'.
     * 
     * @param searchFilter the value of field 'searchFilter'.
    **/
    public void setSearchFilter(java.lang.String searchFilter)
    {
        this._searchFilter = searchFilter;
    } //-- void setSearchFilter(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

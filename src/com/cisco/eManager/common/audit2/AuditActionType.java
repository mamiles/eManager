/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.audit2;

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
public abstract class AuditActionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private int _create;

    /**
     * keeps track of state for field: _create
    **/
    private boolean _has_create;

    private int _read;

    /**
     * keeps track of state for field: _read
    **/
    private boolean _has_read;

    private int _update;

    /**
     * keeps track of state for field: _update
    **/
    private boolean _has_update;

    private int _delete;

    /**
     * keeps track of state for field: _delete
    **/
    private boolean _has_delete;


      //----------------/
     //- Constructors -/
    //----------------/

    public AuditActionType() {
        super();
    } //-- com.cisco.eManager.common.audit2.AuditActionType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'create'.
     * 
     * @return the value of field 'create'.
    **/
    public int getCreate()
    {
        return this._create;
    } //-- int getCreate() 

    /**
     * Returns the value of field 'delete'.
     * 
     * @return the value of field 'delete'.
    **/
    public int getDelete()
    {
        return this._delete;
    } //-- int getDelete() 

    /**
     * Returns the value of field 'read'.
     * 
     * @return the value of field 'read'.
    **/
    public int getRead()
    {
        return this._read;
    } //-- int getRead() 

    /**
     * Returns the value of field 'update'.
     * 
     * @return the value of field 'update'.
    **/
    public int getUpdate()
    {
        return this._update;
    } //-- int getUpdate() 

    /**
    **/
    public boolean hasCreate()
    {
        return this._has_create;
    } //-- boolean hasCreate() 

    /**
    **/
    public boolean hasDelete()
    {
        return this._has_delete;
    } //-- boolean hasDelete() 

    /**
    **/
    public boolean hasRead()
    {
        return this._has_read;
    } //-- boolean hasRead() 

    /**
    **/
    public boolean hasUpdate()
    {
        return this._has_update;
    } //-- boolean hasUpdate() 

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
     * Sets the value of field 'create'.
     * 
     * @param create the value of field 'create'.
    **/
    public void setCreate(int create)
    {
        this._create = create;
        this._has_create = true;
    } //-- void setCreate(int) 

    /**
     * Sets the value of field 'delete'.
     * 
     * @param delete the value of field 'delete'.
    **/
    public void setDelete(int delete)
    {
        this._delete = delete;
        this._has_delete = true;
    } //-- void setDelete(int) 

    /**
     * Sets the value of field 'read'.
     * 
     * @param read the value of field 'read'.
    **/
    public void setRead(int read)
    {
        this._read = read;
        this._has_read = true;
    } //-- void setRead(int) 

    /**
     * Sets the value of field 'update'.
     * 
     * @param update the value of field 'update'.
    **/
    public void setUpdate(int update)
    {
        this._update = update;
        this._has_update = true;
    } //-- void setUpdate(int) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.util;

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
public abstract class ResponseType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private int _success;

    /**
     * keeps track of state for field: _success
    **/
    private boolean _has_success;

    private int _warning;

    /**
     * keeps track of state for field: _warning
    **/
    private boolean _has_warning;

    private int _failure;

    /**
     * keeps track of state for field: _failure
    **/
    private boolean _has_failure;


      //----------------/
     //- Constructors -/
    //----------------/

    public ResponseType() {
        super();
    } //-- com.cisco.eManager.common.util.ResponseType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'failure'.
     * 
     * @return the value of field 'failure'.
    **/
    public int getFailure()
    {
        return this._failure;
    } //-- int getFailure() 

    /**
     * Returns the value of field 'success'.
     * 
     * @return the value of field 'success'.
    **/
    public int getSuccess()
    {
        return this._success;
    } //-- int getSuccess() 

    /**
     * Returns the value of field 'warning'.
     * 
     * @return the value of field 'warning'.
    **/
    public int getWarning()
    {
        return this._warning;
    } //-- int getWarning() 

    /**
    **/
    public boolean hasFailure()
    {
        return this._has_failure;
    } //-- boolean hasFailure() 

    /**
    **/
    public boolean hasSuccess()
    {
        return this._has_success;
    } //-- boolean hasSuccess() 

    /**
    **/
    public boolean hasWarning()
    {
        return this._has_warning;
    } //-- boolean hasWarning() 

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
     * Sets the value of field 'failure'.
     * 
     * @param failure the value of field 'failure'.
    **/
    public void setFailure(int failure)
    {
        this._failure = failure;
        this._has_failure = true;
    } //-- void setFailure(int) 

    /**
     * Sets the value of field 'success'.
     * 
     * @param success the value of field 'success'.
    **/
    public void setSuccess(int success)
    {
        this._success = success;
        this._has_success = true;
    } //-- void setSuccess(int) 

    /**
     * Sets the value of field 'warning'.
     * 
     * @param warning the value of field 'warning'.
    **/
    public void setWarning(int warning)
    {
        this._warning = warning;
        this._has_warning = true;
    } //-- void setWarning(int) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

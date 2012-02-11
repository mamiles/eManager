/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.process;

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
public abstract class SolutionStatusInfo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _solutionName;

    private java.lang.String _appType;

    private java.lang.String _appInstance;

    private java.lang.String _state;


      //----------------/
     //- Constructors -/
    //----------------/

    public SolutionStatusInfo() {
        super();
    } //-- com.cisco.eManager.common.process.SolutionStatusInfo()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'appInstance'.
     * 
     * @return the value of field 'appInstance'.
    **/
    public java.lang.String getAppInstance()
    {
        return this._appInstance;
    } //-- java.lang.String getAppInstance() 

    /**
     * Returns the value of field 'appType'.
     * 
     * @return the value of field 'appType'.
    **/
    public java.lang.String getAppType()
    {
        return this._appType;
    } //-- java.lang.String getAppType() 

    /**
     * Returns the value of field 'solutionName'.
     * 
     * @return the value of field 'solutionName'.
    **/
    public java.lang.String getSolutionName()
    {
        return this._solutionName;
    } //-- java.lang.String getSolutionName() 

    /**
     * Returns the value of field 'state'.
     * 
     * @return the value of field 'state'.
    **/
    public java.lang.String getState()
    {
        return this._state;
    } //-- java.lang.String getState() 

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
     * Sets the value of field 'appInstance'.
     * 
     * @param appInstance the value of field 'appInstance'.
    **/
    public void setAppInstance(java.lang.String appInstance)
    {
        this._appInstance = appInstance;
    } //-- void setAppInstance(java.lang.String) 

    /**
     * Sets the value of field 'appType'.
     * 
     * @param appType the value of field 'appType'.
    **/
    public void setAppType(java.lang.String appType)
    {
        this._appType = appType;
    } //-- void setAppType(java.lang.String) 

    /**
     * Sets the value of field 'solutionName'.
     * 
     * @param solutionName the value of field 'solutionName'.
    **/
    public void setSolutionName(java.lang.String solutionName)
    {
        this._solutionName = solutionName;
    } //-- void setSolutionName(java.lang.String) 

    /**
     * Sets the value of field 'state'.
     * 
     * @param state the value of field 'state'.
    **/
    public void setState(java.lang.String state)
    {
        this._state = state;
    } //-- void setState(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

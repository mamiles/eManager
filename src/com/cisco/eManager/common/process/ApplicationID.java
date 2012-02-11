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
public abstract class ApplicationID implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _applicationType;

    private java.lang.String _applicationInstance;


      //----------------/
     //- Constructors -/
    //----------------/

    public ApplicationID() {
        super();
    } //-- com.cisco.eManager.common.process.ApplicationID()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'applicationInstance'.
     * 
     * @return the value of field 'applicationInstance'.
    **/
    public java.lang.String getApplicationInstance()
    {
        return this._applicationInstance;
    } //-- java.lang.String getApplicationInstance() 

    /**
     * Returns the value of field 'applicationType'.
     * 
     * @return the value of field 'applicationType'.
    **/
    public java.lang.String getApplicationType()
    {
        return this._applicationType;
    } //-- java.lang.String getApplicationType() 

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
     * Sets the value of field 'applicationInstance'.
     * 
     * @param applicationInstance the value of field
     * 'applicationInstance'.
    **/
    public void setApplicationInstance(java.lang.String applicationInstance)
    {
        this._applicationInstance = applicationInstance;
    } //-- void setApplicationInstance(java.lang.String) 

    /**
     * Sets the value of field 'applicationType'.
     * 
     * @param applicationType the value of field 'applicationType'.
    **/
    public void setApplicationType(java.lang.String applicationType)
    {
        this._applicationType = applicationType;
    } //-- void setApplicationType(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

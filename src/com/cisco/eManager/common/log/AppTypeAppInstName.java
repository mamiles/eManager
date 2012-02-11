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
 * 
 * 
 * @version $Revision$ $Date$
**/
public class AppTypeAppInstName implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _appTypeName;

    private java.lang.String _appInstanceName;


      //----------------/
     //- Constructors -/
    //----------------/

    public AppTypeAppInstName() {
        super();
    } //-- com.cisco.eManager.common.log.AppTypeAppInstName()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'appInstanceName'.
     * 
     * @return the value of field 'appInstanceName'.
    **/
    public java.lang.String getAppInstanceName()
    {
        return this._appInstanceName;
    } //-- java.lang.String getAppInstanceName() 

    /**
     * Returns the value of field 'appTypeName'.
     * 
     * @return the value of field 'appTypeName'.
    **/
    public java.lang.String getAppTypeName()
    {
        return this._appTypeName;
    } //-- java.lang.String getAppTypeName() 

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
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'appInstanceName'.
     * 
     * @param appInstanceName the value of field 'appInstanceName'.
    **/
    public void setAppInstanceName(java.lang.String appInstanceName)
    {
        this._appInstanceName = appInstanceName;
    } //-- void setAppInstanceName(java.lang.String) 

    /**
     * Sets the value of field 'appTypeName'.
     * 
     * @param appTypeName the value of field 'appTypeName'.
    **/
    public void setAppTypeName(java.lang.String appTypeName)
    {
        this._appTypeName = appTypeName;
    } //-- void setAppTypeName(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.log.AppTypeAppInstName unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.log.AppTypeAppInstName) Unmarshaller.unmarshal(com.cisco.eManager.common.log.AppTypeAppInstName.class, reader);
    } //-- com.cisco.eManager.common.log.AppTypeAppInstName unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

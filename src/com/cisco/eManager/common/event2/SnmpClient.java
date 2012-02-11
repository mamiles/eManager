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
public abstract class SnmpClient implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _host;

    private java.lang.String _community;

    private int _port;

    /**
     * keeps track of state for field: _port
    **/
    private boolean _has_port;


      //----------------/
     //- Constructors -/
    //----------------/

    public SnmpClient() {
        super();
    } //-- com.cisco.eManager.common.event2.SnmpClient()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'community'.
     * 
     * @return the value of field 'community'.
    **/
    public java.lang.String getCommunity()
    {
        return this._community;
    } //-- java.lang.String getCommunity() 

    /**
     * Returns the value of field 'host'.
     * 
     * @return the value of field 'host'.
    **/
    public java.lang.String getHost()
    {
        return this._host;
    } //-- java.lang.String getHost() 

    /**
     * Returns the value of field 'port'.
     * 
     * @return the value of field 'port'.
    **/
    public int getPort()
    {
        return this._port;
    } //-- int getPort() 

    /**
    **/
    public boolean hasPort()
    {
        return this._has_port;
    } //-- boolean hasPort() 

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
     * Sets the value of field 'community'.
     * 
     * @param community the value of field 'community'.
    **/
    public void setCommunity(java.lang.String community)
    {
        this._community = community;
    } //-- void setCommunity(java.lang.String) 

    /**
     * Sets the value of field 'host'.
     * 
     * @param host the value of field 'host'.
    **/
    public void setHost(java.lang.String host)
    {
        this._host = host;
    } //-- void setHost(java.lang.String) 

    /**
     * Sets the value of field 'port'.
     * 
     * @param port the value of field 'port'.
    **/
    public void setPort(int port)
    {
        this._port = port;
        this._has_port = true;
    } //-- void setPort(int) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

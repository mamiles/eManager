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
 * Class Transport.
 * 
 * @version $Revision$ $Date$
 */
public class Transport implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ssh
     */
    private int _ssh;

    /**
     * keeps track of state for field: _ssh
     */
    private boolean _has_ssh;

    /**
     * Field _telnet
     */
    private int _telnet;

    /**
     * keeps track of state for field: _telnet
     */
    private boolean _has_telnet;


      //----------------/
     //- Constructors -/
    //----------------/

    public Transport() {
        super();
    } //-- com.cisco.eManager.common.inventory2.Transport()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'ssh'.
     * 
     * @return the value of field 'ssh'.
     */
    public int getSsh()
    {
        return this._ssh;
    } //-- int getSsh() 

    /**
     * Returns the value of field 'telnet'.
     * 
     * @return the value of field 'telnet'.
     */
    public int getTelnet()
    {
        return this._telnet;
    } //-- int getTelnet() 

    /**
     * Method hasSsh
     */
    public boolean hasSsh()
    {
        return this._has_ssh;
    } //-- boolean hasSsh() 

    /**
     * Method hasTelnet
     */
    public boolean hasTelnet()
    {
        return this._has_telnet;
    } //-- boolean hasTelnet() 

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
     * Sets the value of field 'ssh'.
     * 
     * @param ssh the value of field 'ssh'.
     */
    public void setSsh(int ssh)
    {
        this._ssh = ssh;
        this._has_ssh = true;
    } //-- void setSsh(int) 

    /**
     * Sets the value of field 'telnet'.
     * 
     * @param telnet the value of field 'telnet'.
     */
    public void setTelnet(int telnet)
    {
        this._telnet = telnet;
        this._has_telnet = true;
    } //-- void setTelnet(int) 

    /**
     * Method unmarshalTransport
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTransport(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.Transport) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.Transport.class, reader);
    } //-- java.lang.Object unmarshalTransport(java.io.Reader) 

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

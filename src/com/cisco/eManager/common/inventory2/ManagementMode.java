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
 * Class ManagementMode.
 * 
 * @version $Revision$ $Date$
 */
public class ManagementMode implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _managed
     */
    private boolean _managed;

    /**
     * keeps track of state for field: _managed
     */
    private boolean _has_managed;

    /**
     * Field _unmanaged
     */
    private boolean _unmanaged;

    /**
     * keeps track of state for field: _unmanaged
     */
    private boolean _has_unmanaged;


      //----------------/
     //- Constructors -/
    //----------------/

    public ManagementMode() {
        super();
    } //-- com.cisco.eManager.common.inventory2.ManagementMode()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'managed'.
     * 
     * @return the value of field 'managed'.
     */
    public boolean getManaged()
    {
        return this._managed;
    } //-- boolean getManaged() 

    /**
     * Returns the value of field 'unmanaged'.
     * 
     * @return the value of field 'unmanaged'.
     */
    public boolean getUnmanaged()
    {
        return this._unmanaged;
    } //-- boolean getUnmanaged() 

    /**
     * Method hasManaged
     */
    public boolean hasManaged()
    {
        return this._has_managed;
    } //-- boolean hasManaged() 

    /**
     * Method hasUnmanaged
     */
    public boolean hasUnmanaged()
    {
        return this._has_unmanaged;
    } //-- boolean hasUnmanaged() 

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
     * Sets the value of field 'managed'.
     * 
     * @param managed the value of field 'managed'.
     */
    public void setManaged(boolean managed)
    {
        this._managed = managed;
        this._has_managed = true;
    } //-- void setManaged(boolean) 

    /**
     * Sets the value of field 'unmanaged'.
     * 
     * @param unmanaged the value of field 'unmanaged'.
     */
    public void setUnmanaged(boolean unmanaged)
    {
        this._unmanaged = unmanaged;
        this._has_unmanaged = true;
    } //-- void setUnmanaged(boolean) 

    /**
     * Method unmarshalManagementMode
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalManagementMode(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.ManagementMode) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.ManagementMode.class, reader);
    } //-- java.lang.Object unmarshalManagementMode(java.io.Reader) 

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

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
 * Class MgmtPolicyState.
 * 
 * @version $Revision$ $Date$
 */
public class MgmtPolicyState implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _loaded
     */
    private int _loaded;

    /**
     * keeps track of state for field: _loaded
     */
    private boolean _has_loaded;

    /**
     * Field _unloaded
     */
    private int _unloaded;

    /**
     * keeps track of state for field: _unloaded
     */
    private boolean _has_unloaded;


      //----------------/
     //- Constructors -/
    //----------------/

    public MgmtPolicyState() {
        super();
    } //-- com.cisco.eManager.common.inventory2.MgmtPolicyState()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'loaded'.
     * 
     * @return the value of field 'loaded'.
     */
    public int getLoaded()
    {
        return this._loaded;
    } //-- int getLoaded() 

    /**
     * Returns the value of field 'unloaded'.
     * 
     * @return the value of field 'unloaded'.
     */
    public int getUnloaded()
    {
        return this._unloaded;
    } //-- int getUnloaded() 

    /**
     * Method hasLoaded
     */
    public boolean hasLoaded()
    {
        return this._has_loaded;
    } //-- boolean hasLoaded() 

    /**
     * Method hasUnloaded
     */
    public boolean hasUnloaded()
    {
        return this._has_unloaded;
    } //-- boolean hasUnloaded() 

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
     * Sets the value of field 'loaded'.
     * 
     * @param loaded the value of field 'loaded'.
     */
    public void setLoaded(int loaded)
    {
        this._loaded = loaded;
        this._has_loaded = true;
    } //-- void setLoaded(int) 

    /**
     * Sets the value of field 'unloaded'.
     * 
     * @param unloaded the value of field 'unloaded'.
     */
    public void setUnloaded(int unloaded)
    {
        this._unloaded = unloaded;
        this._has_unloaded = true;
    } //-- void setUnloaded(int) 

    /**
     * Method unmarshalMgmtPolicyState
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalMgmtPolicyState(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.MgmtPolicyState) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.MgmtPolicyState.class, reader);
    } //-- java.lang.Object unmarshalMgmtPolicyState(java.io.Reader) 

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

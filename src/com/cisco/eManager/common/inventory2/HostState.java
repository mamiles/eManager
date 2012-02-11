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
 * Class HostState.
 * 
 * @version $Revision$ $Date$
 */
public class HostState implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _down
     */
    private int _down;

    /**
     * keeps track of state for field: _down
     */
    private boolean _has_down;

    /**
     * Field _up
     */
    private int _up;

    /**
     * keeps track of state for field: _up
     */
    private boolean _has_up;


      //----------------/
     //- Constructors -/
    //----------------/

    public HostState() {
        super();
    } //-- com.cisco.eManager.common.inventory2.HostState()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'down'.
     * 
     * @return the value of field 'down'.
     */
    public int getDown()
    {
        return this._down;
    } //-- int getDown() 

    /**
     * Returns the value of field 'up'.
     * 
     * @return the value of field 'up'.
     */
    public int getUp()
    {
        return this._up;
    } //-- int getUp() 

    /**
     * Method hasDown
     */
    public boolean hasDown()
    {
        return this._has_down;
    } //-- boolean hasDown() 

    /**
     * Method hasUp
     */
    public boolean hasUp()
    {
        return this._has_up;
    } //-- boolean hasUp() 

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
     * Sets the value of field 'down'.
     * 
     * @param down the value of field 'down'.
     */
    public void setDown(int down)
    {
        this._down = down;
        this._has_down = true;
    } //-- void setDown(int) 

    /**
     * Sets the value of field 'up'.
     * 
     * @param up the value of field 'up'.
     */
    public void setUp(int up)
    {
        this._up = up;
        this._has_up = true;
    } //-- void setUp(int) 

    /**
     * Method unmarshalHostState
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalHostState(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.HostState) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.HostState.class, reader);
    } //-- java.lang.Object unmarshalHostState(java.io.Reader) 

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

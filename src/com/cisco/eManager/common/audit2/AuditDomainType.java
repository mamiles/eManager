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
public abstract class AuditDomainType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private int _inventory;

    /**
     * keeps track of state for field: _inventory
    **/
    private boolean _has_inventory;

    private int _event;

    /**
     * keeps track of state for field: _event
    **/
    private boolean _has_event;

    private int _process;

    /**
     * keeps track of state for field: _process
    **/
    private boolean _has_process;

    private int _emanager;

    /**
     * keeps track of state for field: _emanager
    **/
    private boolean _has_emanager;


      //----------------/
     //- Constructors -/
    //----------------/

    public AuditDomainType() {
        super();
    } //-- com.cisco.eManager.common.audit2.AuditDomainType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'emanager'.
     * 
     * @return the value of field 'emanager'.
    **/
    public int getEmanager()
    {
        return this._emanager;
    } //-- int getEmanager() 

    /**
     * Returns the value of field 'event'.
     * 
     * @return the value of field 'event'.
    **/
    public int getEvent()
    {
        return this._event;
    } //-- int getEvent() 

    /**
     * Returns the value of field 'inventory'.
     * 
     * @return the value of field 'inventory'.
    **/
    public int getInventory()
    {
        return this._inventory;
    } //-- int getInventory() 

    /**
     * Returns the value of field 'process'.
     * 
     * @return the value of field 'process'.
    **/
    public int getProcess()
    {
        return this._process;
    } //-- int getProcess() 

    /**
    **/
    public boolean hasEmanager()
    {
        return this._has_emanager;
    } //-- boolean hasEmanager() 

    /**
    **/
    public boolean hasEvent()
    {
        return this._has_event;
    } //-- boolean hasEvent() 

    /**
    **/
    public boolean hasInventory()
    {
        return this._has_inventory;
    } //-- boolean hasInventory() 

    /**
    **/
    public boolean hasProcess()
    {
        return this._has_process;
    } //-- boolean hasProcess() 

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
     * Sets the value of field 'emanager'.
     * 
     * @param emanager the value of field 'emanager'.
    **/
    public void setEmanager(int emanager)
    {
        this._emanager = emanager;
        this._has_emanager = true;
    } //-- void setEmanager(int) 

    /**
     * Sets the value of field 'event'.
     * 
     * @param event the value of field 'event'.
    **/
    public void setEvent(int event)
    {
        this._event = event;
        this._has_event = true;
    } //-- void setEvent(int) 

    /**
     * Sets the value of field 'inventory'.
     * 
     * @param inventory the value of field 'inventory'.
    **/
    public void setInventory(int inventory)
    {
        this._inventory = inventory;
        this._has_inventory = true;
    } //-- void setInventory(int) 

    /**
     * Sets the value of field 'process'.
     * 
     * @param process the value of field 'process'.
    **/
    public void setProcess(int process)
    {
        this._process = process;
        this._has_process = true;
    } //-- void setProcess(int) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

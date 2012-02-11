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
public abstract class EventSeverityType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private int _informational;

    /**
     * keeps track of state for field: _informational
    **/
    private boolean _has_informational;

    private int _low;

    /**
     * keeps track of state for field: _low
    **/
    private boolean _has_low;

    private int _medium;

    /**
     * keeps track of state for field: _medium
    **/
    private boolean _has_medium;

    private int _high;

    /**
     * keeps track of state for field: _high
    **/
    private boolean _has_high;


      //----------------/
     //- Constructors -/
    //----------------/

    public EventSeverityType() {
        super();
    } //-- com.cisco.eManager.common.event2.EventSeverityType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'high'.
     * 
     * @return the value of field 'high'.
    **/
    public int getHigh()
    {
        return this._high;
    } //-- int getHigh() 

    /**
     * Returns the value of field 'informational'.
     * 
     * @return the value of field 'informational'.
    **/
    public int getInformational()
    {
        return this._informational;
    } //-- int getInformational() 

    /**
     * Returns the value of field 'low'.
     * 
     * @return the value of field 'low'.
    **/
    public int getLow()
    {
        return this._low;
    } //-- int getLow() 

    /**
     * Returns the value of field 'medium'.
     * 
     * @return the value of field 'medium'.
    **/
    public int getMedium()
    {
        return this._medium;
    } //-- int getMedium() 

    /**
    **/
    public boolean hasHigh()
    {
        return this._has_high;
    } //-- boolean hasHigh() 

    /**
    **/
    public boolean hasInformational()
    {
        return this._has_informational;
    } //-- boolean hasInformational() 

    /**
    **/
    public boolean hasLow()
    {
        return this._has_low;
    } //-- boolean hasLow() 

    /**
    **/
    public boolean hasMedium()
    {
        return this._has_medium;
    } //-- boolean hasMedium() 

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
     * Sets the value of field 'high'.
     * 
     * @param high the value of field 'high'.
    **/
    public void setHigh(int high)
    {
        this._high = high;
        this._has_high = true;
    } //-- void setHigh(int) 

    /**
     * Sets the value of field 'informational'.
     * 
     * @param informational the value of field 'informational'.
    **/
    public void setInformational(int informational)
    {
        this._informational = informational;
        this._has_informational = true;
    } //-- void setInformational(int) 

    /**
     * Sets the value of field 'low'.
     * 
     * @param low the value of field 'low'.
    **/
    public void setLow(int low)
    {
        this._low = low;
        this._has_low = true;
    } //-- void setLow(int) 

    /**
     * Sets the value of field 'medium'.
     * 
     * @param medium the value of field 'medium'.
    **/
    public void setMedium(int medium)
    {
        this._medium = medium;
        this._has_medium = true;
    } //-- void setMedium(int) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

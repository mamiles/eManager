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
public abstract class NumericSearchCriteriaType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private long _start;

    /**
     * keeps track of state for field: _start
    **/
    private boolean _has_start;

    private long _end;

    /**
     * keeps track of state for field: _end
    **/
    private boolean _has_end;


      //----------------/
     //- Constructors -/
    //----------------/

    public NumericSearchCriteriaType() {
        super();
    } //-- com.cisco.eManager.common.audit2.NumericSearchCriteriaType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteEnd()
    {
        this._has_end= false;
    } //-- void deleteEnd() 

    /**
    **/
    public void deleteStart()
    {
        this._has_start= false;
    } //-- void deleteStart() 

    /**
     * Returns the value of field 'end'.
     * 
     * @return the value of field 'end'.
    **/
    public long getEnd()
    {
        return this._end;
    } //-- long getEnd() 

    /**
     * Returns the value of field 'start'.
     * 
     * @return the value of field 'start'.
    **/
    public long getStart()
    {
        return this._start;
    } //-- long getStart() 

    /**
    **/
    public boolean hasEnd()
    {
        return this._has_end;
    } //-- boolean hasEnd() 

    /**
    **/
    public boolean hasStart()
    {
        return this._has_start;
    } //-- boolean hasStart() 

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
     * Sets the value of field 'end'.
     * 
     * @param end the value of field 'end'.
    **/
    public void setEnd(long end)
    {
        this._end = end;
        this._has_end = true;
    } //-- void setEnd(long) 

    /**
     * Sets the value of field 'start'.
     * 
     * @param start the value of field 'start'.
    **/
    public void setStart(long start)
    {
        this._start = start;
        this._has_start = true;
    } //-- void setStart(long) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

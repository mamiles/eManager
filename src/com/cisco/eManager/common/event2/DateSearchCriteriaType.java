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
import java.util.Date;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public abstract class DateSearchCriteriaType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.Date _startDate;

    private java.util.Date _endDate;


      //----------------/
     //- Constructors -/
    //----------------/

    public DateSearchCriteriaType() {
        super();
    } //-- com.cisco.eManager.common.event2.DateSearchCriteriaType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'endDate'.
     * 
     * @return the value of field 'endDate'.
    **/
    public java.util.Date getEndDate()
    {
        return this._endDate;
    } //-- java.util.Date getEndDate() 

    /**
     * Returns the value of field 'startDate'.
     * 
     * @return the value of field 'startDate'.
    **/
    public java.util.Date getStartDate()
    {
        return this._startDate;
    } //-- java.util.Date getStartDate() 

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
     * Sets the value of field 'endDate'.
     * 
     * @param endDate the value of field 'endDate'.
    **/
    public void setEndDate(java.util.Date endDate)
    {
        this._endDate = endDate;
    } //-- void setEndDate(java.util.Date) 

    /**
     * Sets the value of field 'startDate'.
     * 
     * @param startDate the value of field 'startDate'.
    **/
    public void setStartDate(java.util.Date startDate)
    {
        this._startDate = startDate;
    } //-- void setStartDate(java.util.Date) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

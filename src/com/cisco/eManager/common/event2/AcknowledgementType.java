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
public abstract class AcknowledgementType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _userId;

    private java.util.Date _date;

    private java.lang.String _comment;


      //----------------/
     //- Constructors -/
    //----------------/

    public AcknowledgementType() {
        super();
    } //-- com.cisco.eManager.common.event2.AcknowledgementType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'comment'.
     * 
     * @return the value of field 'comment'.
    **/
    public java.lang.String getComment()
    {
        return this._comment;
    } //-- java.lang.String getComment() 

    /**
     * Returns the value of field 'date'.
     * 
     * @return the value of field 'date'.
    **/
    public java.util.Date getDate()
    {
        return this._date;
    } //-- java.util.Date getDate() 

    /**
     * Returns the value of field 'userId'.
     * 
     * @return the value of field 'userId'.
    **/
    public java.lang.String getUserId()
    {
        return this._userId;
    } //-- java.lang.String getUserId() 

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
     * Sets the value of field 'comment'.
     * 
     * @param comment the value of field 'comment'.
    **/
    public void setComment(java.lang.String comment)
    {
        this._comment = comment;
    } //-- void setComment(java.lang.String) 

    /**
     * Sets the value of field 'date'.
     * 
     * @param date the value of field 'date'.
    **/
    public void setDate(java.util.Date date)
    {
        this._date = date;
    } //-- void setDate(java.util.Date) 

    /**
     * Sets the value of field 'userId'.
     * 
     * @param userId the value of field 'userId'.
    **/
    public void setUserId(java.lang.String userId)
    {
        this._userId = userId;
    } //-- void setUserId(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

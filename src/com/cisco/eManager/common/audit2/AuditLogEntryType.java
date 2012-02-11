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
public abstract class AuditLogEntryType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private Id _id;

    private Domain _domain;

    private Action _action;

    private java.lang.String _subject;

    private java.util.Date _time;

    private java.lang.String _userId;

    private java.lang.String _description;


      //----------------/
     //- Constructors -/
    //----------------/

    public AuditLogEntryType() {
        super();
    } //-- com.cisco.eManager.common.audit2.AuditLogEntryType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'action'.
     * 
     * @return the value of field 'action'.
    **/
    public Action getAction()
    {
        return this._action;
    } //-- Action getAction() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
    **/
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'domain'.
     * 
     * @return the value of field 'domain'.
    **/
    public Domain getDomain()
    {
        return this._domain;
    } //-- Domain getDomain() 

    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'id'.
    **/
    public Id getId()
    {
        return this._id;
    } //-- Id getId() 

    /**
     * Returns the value of field 'subject'.
     * 
     * @return the value of field 'subject'.
    **/
    public java.lang.String getSubject()
    {
        return this._subject;
    } //-- java.lang.String getSubject() 

    /**
     * Returns the value of field 'time'.
     * 
     * @return the value of field 'time'.
    **/
    public java.util.Date getTime()
    {
        return this._time;
    } //-- java.util.Date getTime() 

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
     * Sets the value of field 'action'.
     * 
     * @param action the value of field 'action'.
    **/
    public void setAction(Action action)
    {
        this._action = action;
    } //-- void setAction(Action) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
    **/
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'domain'.
     * 
     * @param domain the value of field 'domain'.
    **/
    public void setDomain(Domain domain)
    {
        this._domain = domain;
    } //-- void setDomain(Domain) 

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
    **/
    public void setId(Id id)
    {
        this._id = id;
    } //-- void setId(Id) 

    /**
     * Sets the value of field 'subject'.
     * 
     * @param subject the value of field 'subject'.
    **/
    public void setSubject(java.lang.String subject)
    {
        this._subject = subject;
    } //-- void setSubject(java.lang.String) 

    /**
     * Sets the value of field 'time'.
     * 
     * @param time the value of field 'time'.
    **/
    public void setTime(java.util.Date time)
    {
        this._time = time;
    } //-- void setTime(java.util.Date) 

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

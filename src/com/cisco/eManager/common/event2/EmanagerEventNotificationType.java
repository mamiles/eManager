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
public abstract class EmanagerEventNotificationType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private NotificationType _notificationType;

    private EventId _eventId;

    private Severity _severity;

    private java.util.Date _time;

    private ManagedObjectId _managedObjectId;

    private java.lang.String _displayText;


      //----------------/
     //- Constructors -/
    //----------------/

    public EmanagerEventNotificationType() {
        super();
    } //-- com.cisco.eManager.common.event2.EmanagerEventNotificationType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'displayText'.
     * 
     * @return the value of field 'displayText'.
    **/
    public java.lang.String getDisplayText()
    {
        return this._displayText;
    } //-- java.lang.String getDisplayText() 

    /**
     * Returns the value of field 'eventId'.
     * 
     * @return the value of field 'eventId'.
    **/
    public EventId getEventId()
    {
        return this._eventId;
    } //-- EventId getEventId() 

    /**
     * Returns the value of field 'managedObjectId'.
     * 
     * @return the value of field 'managedObjectId'.
    **/
    public ManagedObjectId getManagedObjectId()
    {
        return this._managedObjectId;
    } //-- ManagedObjectId getManagedObjectId() 

    /**
     * Returns the value of field 'notificationType'.
     * 
     * @return the value of field 'notificationType'.
    **/
    public NotificationType getNotificationType()
    {
        return this._notificationType;
    } //-- NotificationType getNotificationType() 

    /**
     * Returns the value of field 'severity'.
     * 
     * @return the value of field 'severity'.
    **/
    public Severity getSeverity()
    {
        return this._severity;
    } //-- Severity getSeverity() 

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
     * Sets the value of field 'displayText'.
     * 
     * @param displayText the value of field 'displayText'.
    **/
    public void setDisplayText(java.lang.String displayText)
    {
        this._displayText = displayText;
    } //-- void setDisplayText(java.lang.String) 

    /**
     * Sets the value of field 'eventId'.
     * 
     * @param eventId the value of field 'eventId'.
    **/
    public void setEventId(EventId eventId)
    {
        this._eventId = eventId;
    } //-- void setEventId(EventId) 

    /**
     * Sets the value of field 'managedObjectId'.
     * 
     * @param managedObjectId the value of field 'managedObjectId'.
    **/
    public void setManagedObjectId(ManagedObjectId managedObjectId)
    {
        this._managedObjectId = managedObjectId;
    } //-- void setManagedObjectId(ManagedObjectId) 

    /**
     * Sets the value of field 'notificationType'.
     * 
     * @param notificationType the value of field 'notificationType'
    **/
    public void setNotificationType(NotificationType notificationType)
    {
        this._notificationType = notificationType;
    } //-- void setNotificationType(NotificationType) 

    /**
     * Sets the value of field 'severity'.
     * 
     * @param severity the value of field 'severity'.
    **/
    public void setSeverity(Severity severity)
    {
        this._severity = severity;
    } //-- void setSeverity(Severity) 

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
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

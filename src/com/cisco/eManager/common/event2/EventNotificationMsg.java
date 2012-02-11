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
 * Event Mgr notification messages
 * 
 * @version $Revision$ $Date$
**/
public class EventNotificationMsg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private EventAcknowledgementNotification _eventAcknowledgementNotification;

    private EventUnacknowledgementNotification _eventUnacknowledgementNotification;

    private EventNotification _eventNotification;


      //----------------/
     //- Constructors -/
    //----------------/

    public EventNotificationMsg() {
        super();
    } //-- com.cisco.eManager.common.event2.EventNotificationMsg()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field
     * 'eventAcknowledgementNotification'.
     * 
     * @return the value of field 'eventAcknowledgementNotification'
    **/
    public EventAcknowledgementNotification getEventAcknowledgementNotification()
    {
        return this._eventAcknowledgementNotification;
    } //-- EventAcknowledgementNotification getEventAcknowledgementNotification() 

    /**
     * Returns the value of field 'eventNotification'.
     * 
     * @return the value of field 'eventNotification'.
    **/
    public EventNotification getEventNotification()
    {
        return this._eventNotification;
    } //-- EventNotification getEventNotification() 

    /**
     * Returns the value of field
     * 'eventUnacknowledgementNotification'.
     * 
     * @return the value of field
     * 'eventUnacknowledgementNotification'.
    **/
    public EventUnacknowledgementNotification getEventUnacknowledgementNotification()
    {
        return this._eventUnacknowledgementNotification;
    } //-- EventUnacknowledgementNotification getEventUnacknowledgementNotification() 

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
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'eventAcknowledgementNotification'.
     * 
     * @param eventAcknowledgementNotification the value of field
     * 'eventAcknowledgementNotification'.
    **/
    public void setEventAcknowledgementNotification(EventAcknowledgementNotification eventAcknowledgementNotification)
    {
        this._eventAcknowledgementNotification = eventAcknowledgementNotification;
    } //-- void setEventAcknowledgementNotification(EventAcknowledgementNotification) 

    /**
     * Sets the value of field 'eventNotification'.
     * 
     * @param eventNotification the value of field
     * 'eventNotification'.
    **/
    public void setEventNotification(EventNotification eventNotification)
    {
        this._eventNotification = eventNotification;
    } //-- void setEventNotification(EventNotification) 

    /**
     * Sets the value of field
     * 'eventUnacknowledgementNotification'.
     * 
     * @param eventUnacknowledgementNotification the value of field
     * 'eventUnacknowledgementNotification'.
    **/
    public void setEventUnacknowledgementNotification(EventUnacknowledgementNotification eventUnacknowledgementNotification)
    {
        this._eventUnacknowledgementNotification = eventUnacknowledgementNotification;
    } //-- void setEventUnacknowledgementNotification(EventUnacknowledgementNotification) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.event2.EventNotificationMsg unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.event2.EventNotificationMsg) Unmarshaller.unmarshal(com.cisco.eManager.common.event2.EventNotificationMsg.class, reader);
    } //-- com.cisco.eManager.common.event2.EventNotificationMsg unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

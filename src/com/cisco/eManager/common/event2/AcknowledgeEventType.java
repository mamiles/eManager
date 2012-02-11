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
public abstract class AcknowledgeEventType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private EventId _eventId;

    private Acknowledgement _acknowledgement;


      //----------------/
     //- Constructors -/
    //----------------/

    public AcknowledgeEventType() {
        super();
    } //-- com.cisco.eManager.common.event2.AcknowledgeEventType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'acknowledgement'.
     * 
     * @return the value of field 'acknowledgement'.
    **/
    public Acknowledgement getAcknowledgement()
    {
        return this._acknowledgement;
    } //-- Acknowledgement getAcknowledgement() 

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
     * Sets the value of field 'acknowledgement'.
     * 
     * @param acknowledgement the value of field 'acknowledgement'.
    **/
    public void setAcknowledgement(Acknowledgement acknowledgement)
    {
        this._acknowledgement = acknowledgement;
    } //-- void setAcknowledgement(Acknowledgement) 

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
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

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
public class EventNotification implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private EmanagerEventNotification _emanagerEventNotification;

    private TibcoEventNotification _tibcoEventNotification;

    private ProcessSequencerEventNotification _processSequencerEventNotification;


      //----------------/
     //- Constructors -/
    //----------------/

    public EventNotification() {
        super();
    } //-- com.cisco.eManager.common.event2.EventNotification()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'emanagerEventNotification'.
     * 
     * @return the value of field 'emanagerEventNotification'.
    **/
    public EmanagerEventNotification getEmanagerEventNotification()
    {
        return this._emanagerEventNotification;
    } //-- EmanagerEventNotification getEmanagerEventNotification() 

    /**
     * Returns the value of field
     * 'processSequencerEventNotification'.
     * 
     * @return the value of field
     * 'processSequencerEventNotification'.
    **/
    public ProcessSequencerEventNotification getProcessSequencerEventNotification()
    {
        return this._processSequencerEventNotification;
    } //-- ProcessSequencerEventNotification getProcessSequencerEventNotification() 

    /**
     * Returns the value of field 'tibcoEventNotification'.
     * 
     * @return the value of field 'tibcoEventNotification'.
    **/
    public TibcoEventNotification getTibcoEventNotification()
    {
        return this._tibcoEventNotification;
    } //-- TibcoEventNotification getTibcoEventNotification() 

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
     * Sets the value of field 'emanagerEventNotification'.
     * 
     * @param emanagerEventNotification the value of field
     * 'emanagerEventNotification'.
    **/
    public void setEmanagerEventNotification(EmanagerEventNotification emanagerEventNotification)
    {
        this._emanagerEventNotification = emanagerEventNotification;
    } //-- void setEmanagerEventNotification(EmanagerEventNotification) 

    /**
     * Sets the value of field 'processSequencerEventNotification'.
     * 
     * @param processSequencerEventNotification the value of field
     * 'processSequencerEventNotification'.
    **/
    public void setProcessSequencerEventNotification(ProcessSequencerEventNotification processSequencerEventNotification)
    {
        this._processSequencerEventNotification = processSequencerEventNotification;
    } //-- void setProcessSequencerEventNotification(ProcessSequencerEventNotification) 

    /**
     * Sets the value of field 'tibcoEventNotification'.
     * 
     * @param tibcoEventNotification the value of field
     * 'tibcoEventNotification'.
    **/
    public void setTibcoEventNotification(TibcoEventNotification tibcoEventNotification)
    {
        this._tibcoEventNotification = tibcoEventNotification;
    } //-- void setTibcoEventNotification(TibcoEventNotification) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.event2.EventNotification unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.event2.EventNotification) Unmarshaller.unmarshal(com.cisco.eManager.common.event2.EventNotification.class, reader);
    } //-- com.cisco.eManager.common.event2.EventNotification unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

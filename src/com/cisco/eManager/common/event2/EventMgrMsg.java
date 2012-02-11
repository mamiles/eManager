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
 * Event Mgr request messages
 * 
 * @version $Revision$ $Date$
**/
public class EventMgrMsg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private AcknowledgeEvent _acknowledgeEvent;

    private UnacknowledgeEvent _unacknowledgeEvent;

    private GetEventDetails _getEventDetails;

    private RetrieveEvents _retrieveEvents;

    private DeleteEvents _deleteEvents;

    private RegisterSNMPClient _registerSNMPClient;

    private UnregisterSNMPClient _unregisterSNMPClient;

    private java.lang.String _retrieveSNMPClients;


      //----------------/
     //- Constructors -/
    //----------------/

    public EventMgrMsg() {
        super();
    } //-- com.cisco.eManager.common.event2.EventMgrMsg()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'acknowledgeEvent'.
     * 
     * @return the value of field 'acknowledgeEvent'.
    **/
    public AcknowledgeEvent getAcknowledgeEvent()
    {
        return this._acknowledgeEvent;
    } //-- AcknowledgeEvent getAcknowledgeEvent() 

    /**
     * Returns the value of field 'deleteEvents'.
     * 
     * @return the value of field 'deleteEvents'.
    **/
    public DeleteEvents getDeleteEvents()
    {
        return this._deleteEvents;
    } //-- DeleteEvents getDeleteEvents() 

    /**
     * Returns the value of field 'getEventDetails'.
     * 
     * @return the value of field 'getEventDetails'.
    **/
    public GetEventDetails getGetEventDetails()
    {
        return this._getEventDetails;
    } //-- GetEventDetails getGetEventDetails() 

    /**
     * Returns the value of field 'registerSNMPClient'.
     * 
     * @return the value of field 'registerSNMPClient'.
    **/
    public RegisterSNMPClient getRegisterSNMPClient()
    {
        return this._registerSNMPClient;
    } //-- RegisterSNMPClient getRegisterSNMPClient() 

    /**
     * Returns the value of field 'retrieveEvents'.
     * 
     * @return the value of field 'retrieveEvents'.
    **/
    public RetrieveEvents getRetrieveEvents()
    {
        return this._retrieveEvents;
    } //-- RetrieveEvents getRetrieveEvents() 

    /**
     * Returns the value of field 'retrieveSNMPClients'.
     * 
     * @return the value of field 'retrieveSNMPClients'.
    **/
    public java.lang.String getRetrieveSNMPClients()
    {
        return this._retrieveSNMPClients;
    } //-- java.lang.String getRetrieveSNMPClients() 

    /**
     * Returns the value of field 'unacknowledgeEvent'.
     * 
     * @return the value of field 'unacknowledgeEvent'.
    **/
    public UnacknowledgeEvent getUnacknowledgeEvent()
    {
        return this._unacknowledgeEvent;
    } //-- UnacknowledgeEvent getUnacknowledgeEvent() 

    /**
     * Returns the value of field 'unregisterSNMPClient'.
     * 
     * @return the value of field 'unregisterSNMPClient'.
    **/
    public UnregisterSNMPClient getUnregisterSNMPClient()
    {
        return this._unregisterSNMPClient;
    } //-- UnregisterSNMPClient getUnregisterSNMPClient() 

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
     * Sets the value of field 'acknowledgeEvent'.
     * 
     * @param acknowledgeEvent the value of field 'acknowledgeEvent'
    **/
    public void setAcknowledgeEvent(AcknowledgeEvent acknowledgeEvent)
    {
        this._acknowledgeEvent = acknowledgeEvent;
    } //-- void setAcknowledgeEvent(AcknowledgeEvent) 

    /**
     * Sets the value of field 'deleteEvents'.
     * 
     * @param deleteEvents the value of field 'deleteEvents'.
    **/
    public void setDeleteEvents(DeleteEvents deleteEvents)
    {
        this._deleteEvents = deleteEvents;
    } //-- void setDeleteEvents(DeleteEvents) 

    /**
     * Sets the value of field 'getEventDetails'.
     * 
     * @param getEventDetails the value of field 'getEventDetails'.
    **/
    public void setGetEventDetails(GetEventDetails getEventDetails)
    {
        this._getEventDetails = getEventDetails;
    } //-- void setGetEventDetails(GetEventDetails) 

    /**
     * Sets the value of field 'registerSNMPClient'.
     * 
     * @param registerSNMPClient the value of field
     * 'registerSNMPClient'.
    **/
    public void setRegisterSNMPClient(RegisterSNMPClient registerSNMPClient)
    {
        this._registerSNMPClient = registerSNMPClient;
    } //-- void setRegisterSNMPClient(RegisterSNMPClient) 

    /**
     * Sets the value of field 'retrieveEvents'.
     * 
     * @param retrieveEvents the value of field 'retrieveEvents'.
    **/
    public void setRetrieveEvents(RetrieveEvents retrieveEvents)
    {
        this._retrieveEvents = retrieveEvents;
    } //-- void setRetrieveEvents(RetrieveEvents) 

    /**
     * Sets the value of field 'retrieveSNMPClients'.
     * 
     * @param retrieveSNMPClients the value of field
     * 'retrieveSNMPClients'.
    **/
    public void setRetrieveSNMPClients(java.lang.String retrieveSNMPClients)
    {
        this._retrieveSNMPClients = retrieveSNMPClients;
    } //-- void setRetrieveSNMPClients(java.lang.String) 

    /**
     * Sets the value of field 'unacknowledgeEvent'.
     * 
     * @param unacknowledgeEvent the value of field
     * 'unacknowledgeEvent'.
    **/
    public void setUnacknowledgeEvent(UnacknowledgeEvent unacknowledgeEvent)
    {
        this._unacknowledgeEvent = unacknowledgeEvent;
    } //-- void setUnacknowledgeEvent(UnacknowledgeEvent) 

    /**
     * Sets the value of field 'unregisterSNMPClient'.
     * 
     * @param unregisterSNMPClient the value of field
     * 'unregisterSNMPClient'.
    **/
    public void setUnregisterSNMPClient(UnregisterSNMPClient unregisterSNMPClient)
    {
        this._unregisterSNMPClient = unregisterSNMPClient;
    } //-- void setUnregisterSNMPClient(UnregisterSNMPClient) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.event2.EventMgrMsg unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.event2.EventMgrMsg) Unmarshaller.unmarshal(com.cisco.eManager.common.event2.EventMgrMsg.class, reader);
    } //-- com.cisco.eManager.common.event2.EventMgrMsg unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

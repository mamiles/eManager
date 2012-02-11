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
public class RetrieveEventsResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private EmanagerEventDetails _emanagerEventDetails;

    private TibcoEventDetails _tibcoEventDetails;

    private ProcessSequencerEventDetails _processSequencerEventDetails;


      //----------------/
     //- Constructors -/
    //----------------/

    public RetrieveEventsResp() {
        super();
    } //-- com.cisco.eManager.common.event2.RetrieveEventsResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'emanagerEventDetails'.
     * 
     * @return the value of field 'emanagerEventDetails'.
    **/
    public EmanagerEventDetails getEmanagerEventDetails()
    {
        return this._emanagerEventDetails;
    } //-- EmanagerEventDetails getEmanagerEventDetails() 

    /**
     * Returns the value of field 'processSequencerEventDetails'.
     * 
     * @return the value of field 'processSequencerEventDetails'.
    **/
    public ProcessSequencerEventDetails getProcessSequencerEventDetails()
    {
        return this._processSequencerEventDetails;
    } //-- ProcessSequencerEventDetails getProcessSequencerEventDetails() 

    /**
     * Returns the value of field 'tibcoEventDetails'.
     * 
     * @return the value of field 'tibcoEventDetails'.
    **/
    public TibcoEventDetails getTibcoEventDetails()
    {
        return this._tibcoEventDetails;
    } //-- TibcoEventDetails getTibcoEventDetails() 

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
     * Sets the value of field 'emanagerEventDetails'.
     * 
     * @param emanagerEventDetails the value of field
     * 'emanagerEventDetails'.
    **/
    public void setEmanagerEventDetails(EmanagerEventDetails emanagerEventDetails)
    {
        this._emanagerEventDetails = emanagerEventDetails;
    } //-- void setEmanagerEventDetails(EmanagerEventDetails) 

    /**
     * Sets the value of field 'processSequencerEventDetails'.
     * 
     * @param processSequencerEventDetails the value of field
     * 'processSequencerEventDetails'.
    **/
    public void setProcessSequencerEventDetails(ProcessSequencerEventDetails processSequencerEventDetails)
    {
        this._processSequencerEventDetails = processSequencerEventDetails;
    } //-- void setProcessSequencerEventDetails(ProcessSequencerEventDetails) 

    /**
     * Sets the value of field 'tibcoEventDetails'.
     * 
     * @param tibcoEventDetails the value of field
     * 'tibcoEventDetails'.
    **/
    public void setTibcoEventDetails(TibcoEventDetails tibcoEventDetails)
    {
        this._tibcoEventDetails = tibcoEventDetails;
    } //-- void setTibcoEventDetails(TibcoEventDetails) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.event2.RetrieveEventsResp unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.event2.RetrieveEventsResp) Unmarshaller.unmarshal(com.cisco.eManager.common.event2.RetrieveEventsResp.class, reader);
    } //-- com.cisco.eManager.common.event2.RetrieveEventsResp unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

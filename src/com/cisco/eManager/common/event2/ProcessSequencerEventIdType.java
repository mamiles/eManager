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
public abstract class ProcessSequencerEventIdType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private long _processSequencerId;

    /**
     * keeps track of state for field: _processSequencerId
    **/
    private boolean _has_processSequencerId;

    private long _processSequencerEventId;

    /**
     * keeps track of state for field: _processSequencerEventId
    **/
    private boolean _has_processSequencerEventId;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProcessSequencerEventIdType() {
        super();
    } //-- com.cisco.eManager.common.event2.ProcessSequencerEventIdType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'processSequencerEventId'.
     * 
     * @return the value of field 'processSequencerEventId'.
    **/
    public long getProcessSequencerEventId()
    {
        return this._processSequencerEventId;
    } //-- long getProcessSequencerEventId() 

    /**
     * Returns the value of field 'processSequencerId'.
     * 
     * @return the value of field 'processSequencerId'.
    **/
    public long getProcessSequencerId()
    {
        return this._processSequencerId;
    } //-- long getProcessSequencerId() 

    /**
    **/
    public boolean hasProcessSequencerEventId()
    {
        return this._has_processSequencerEventId;
    } //-- boolean hasProcessSequencerEventId() 

    /**
    **/
    public boolean hasProcessSequencerId()
    {
        return this._has_processSequencerId;
    } //-- boolean hasProcessSequencerId() 

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
     * Sets the value of field 'processSequencerEventId'.
     * 
     * @param processSequencerEventId the value of field
     * 'processSequencerEventId'.
    **/
    public void setProcessSequencerEventId(long processSequencerEventId)
    {
        this._processSequencerEventId = processSequencerEventId;
        this._has_processSequencerEventId = true;
    } //-- void setProcessSequencerEventId(long) 

    /**
     * Sets the value of field 'processSequencerId'.
     * 
     * @param processSequencerId the value of field
     * 'processSequencerId'.
    **/
    public void setProcessSequencerId(long processSequencerId)
    {
        this._processSequencerId = processSequencerId;
        this._has_processSequencerId = true;
    } //-- void setProcessSequencerId(long) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

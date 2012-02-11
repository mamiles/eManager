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
public abstract class ProcessSequencerEventDetailsType extends com.cisco.eManager.common.event2.EmanagerEventDetailsType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private ProcessSequencerId _processSequencerId;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProcessSequencerEventDetailsType() {
        super();
    } //-- com.cisco.eManager.common.event2.ProcessSequencerEventDetailsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'processSequencerId'.
     * 
     * @return the value of field 'processSequencerId'.
    **/
    public ProcessSequencerId getProcessSequencerId()
    {
        return this._processSequencerId;
    } //-- ProcessSequencerId getProcessSequencerId() 

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
     * Sets the value of field 'processSequencerId'.
     * 
     * @param processSequencerId the value of field
     * 'processSequencerId'.
    **/
    public void setProcessSequencerId(ProcessSequencerId processSequencerId)
    {
        this._processSequencerId = processSequencerId;
    } //-- void setProcessSequencerId(ProcessSequencerId) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

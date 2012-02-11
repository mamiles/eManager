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
public abstract class AbstractProcessSequencerSearchCriteriaType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private PsEventIdSearchCriteria _psEventIdSearchCriteria;

    private int _psId;

    /**
     * keeps track of state for field: _psId
    **/
    private boolean _has_psId;


      //----------------/
     //- Constructors -/
    //----------------/

    public AbstractProcessSequencerSearchCriteriaType() {
        super();
    } //-- com.cisco.eManager.common.event2.AbstractProcessSequencerSearchCriteriaType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deletePsId()
    {
        this._has_psId= false;
    } //-- void deletePsId() 

    /**
     * Returns the value of field 'psEventIdSearchCriteria'.
     * 
     * @return the value of field 'psEventIdSearchCriteria'.
    **/
    public PsEventIdSearchCriteria getPsEventIdSearchCriteria()
    {
        return this._psEventIdSearchCriteria;
    } //-- PsEventIdSearchCriteria getPsEventIdSearchCriteria() 

    /**
     * Returns the value of field 'psId'.
     * 
     * @return the value of field 'psId'.
    **/
    public int getPsId()
    {
        return this._psId;
    } //-- int getPsId() 

    /**
    **/
    public boolean hasPsId()
    {
        return this._has_psId;
    } //-- boolean hasPsId() 

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
     * Sets the value of field 'psEventIdSearchCriteria'.
     * 
     * @param psEventIdSearchCriteria the value of field
     * 'psEventIdSearchCriteria'.
    **/
    public void setPsEventIdSearchCriteria(PsEventIdSearchCriteria psEventIdSearchCriteria)
    {
        this._psEventIdSearchCriteria = psEventIdSearchCriteria;
    } //-- void setPsEventIdSearchCriteria(PsEventIdSearchCriteria) 

    /**
     * Sets the value of field 'psId'.
     * 
     * @param psId the value of field 'psId'.
    **/
    public void setPsId(int psId)
    {
        this._psId = psId;
        this._has_psId = true;
    } //-- void setPsId(int) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

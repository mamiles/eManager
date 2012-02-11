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
public abstract class EventSearchCriteriaType extends com.cisco.eManager.common.event2.AbstractSearchCriteriaType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private TibcoSearchCriteria _tibcoSearchCriteria;

    private ProcessSequencerSearchCriteria _processSequencerSearchCriteria;


      //----------------/
     //- Constructors -/
    //----------------/

    public EventSearchCriteriaType() {
        super();
    } //-- com.cisco.eManager.common.event2.EventSearchCriteriaType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'processSequencerSearchCriteria'.
     * 
     * @return the value of field 'processSequencerSearchCriteria'.
    **/
    public ProcessSequencerSearchCriteria getProcessSequencerSearchCriteria()
    {
        return this._processSequencerSearchCriteria;
    } //-- ProcessSequencerSearchCriteria getProcessSequencerSearchCriteria() 

    /**
     * Returns the value of field 'tibcoSearchCriteria'.
     * 
     * @return the value of field 'tibcoSearchCriteria'.
    **/
    public TibcoSearchCriteria getTibcoSearchCriteria()
    {
        return this._tibcoSearchCriteria;
    } //-- TibcoSearchCriteria getTibcoSearchCriteria() 

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
     * Sets the value of field 'processSequencerSearchCriteria'.
     * 
     * @param processSequencerSearchCriteria the value of field
     * 'processSequencerSearchCriteria'.
    **/
    public void setProcessSequencerSearchCriteria(ProcessSequencerSearchCriteria processSequencerSearchCriteria)
    {
        this._processSequencerSearchCriteria = processSequencerSearchCriteria;
    } //-- void setProcessSequencerSearchCriteria(ProcessSequencerSearchCriteria) 

    /**
     * Sets the value of field 'tibcoSearchCriteria'.
     * 
     * @param tibcoSearchCriteria the value of field
     * 'tibcoSearchCriteria'.
    **/
    public void setTibcoSearchCriteria(TibcoSearchCriteria tibcoSearchCriteria)
    {
        this._tibcoSearchCriteria = tibcoSearchCriteria;
    } //-- void setTibcoSearchCriteria(TibcoSearchCriteria) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

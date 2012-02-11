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
public abstract class EventDeletionCriteriaType extends com.cisco.eManager.common.event2.AbstractSearchCriteriaType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private TibcoDeletionCriteria _tibcoDeletionCriteria;

    private ProcessSequencerCriteria _processSequencerCriteria;


      //----------------/
     //- Constructors -/
    //----------------/

    public EventDeletionCriteriaType() {
        super();
    } //-- com.cisco.eManager.common.event2.EventDeletionCriteriaType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'processSequencerCriteria'.
     * 
     * @return the value of field 'processSequencerCriteria'.
    **/
    public ProcessSequencerCriteria getProcessSequencerCriteria()
    {
        return this._processSequencerCriteria;
    } //-- ProcessSequencerCriteria getProcessSequencerCriteria() 

    /**
     * Returns the value of field 'tibcoDeletionCriteria'.
     * 
     * @return the value of field 'tibcoDeletionCriteria'.
    **/
    public TibcoDeletionCriteria getTibcoDeletionCriteria()
    {
        return this._tibcoDeletionCriteria;
    } //-- TibcoDeletionCriteria getTibcoDeletionCriteria() 

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
     * Sets the value of field 'processSequencerCriteria'.
     * 
     * @param processSequencerCriteria the value of field
     * 'processSequencerCriteria'.
    **/
    public void setProcessSequencerCriteria(ProcessSequencerCriteria processSequencerCriteria)
    {
        this._processSequencerCriteria = processSequencerCriteria;
    } //-- void setProcessSequencerCriteria(ProcessSequencerCriteria) 

    /**
     * Sets the value of field 'tibcoDeletionCriteria'.
     * 
     * @param tibcoDeletionCriteria the value of field
     * 'tibcoDeletionCriteria'.
    **/
    public void setTibcoDeletionCriteria(TibcoDeletionCriteria tibcoDeletionCriteria)
    {
        this._tibcoDeletionCriteria = tibcoDeletionCriteria;
    } //-- void setTibcoDeletionCriteria(TibcoDeletionCriteria) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

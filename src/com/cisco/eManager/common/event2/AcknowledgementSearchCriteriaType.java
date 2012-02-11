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
public abstract class AcknowledgementSearchCriteriaType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private UserIdSearchCriteria _userIdSearchCriteria;

    private AckTimeSearchCriteria _ackTimeSearchCriteria;


      //----------------/
     //- Constructors -/
    //----------------/

    public AcknowledgementSearchCriteriaType() {
        super();
    } //-- com.cisco.eManager.common.event2.AcknowledgementSearchCriteriaType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'ackTimeSearchCriteria'.
     * 
     * @return the value of field 'ackTimeSearchCriteria'.
    **/
    public AckTimeSearchCriteria getAckTimeSearchCriteria()
    {
        return this._ackTimeSearchCriteria;
    } //-- AckTimeSearchCriteria getAckTimeSearchCriteria() 

    /**
     * Returns the value of field 'userIdSearchCriteria'.
     * 
     * @return the value of field 'userIdSearchCriteria'.
    **/
    public UserIdSearchCriteria getUserIdSearchCriteria()
    {
        return this._userIdSearchCriteria;
    } //-- UserIdSearchCriteria getUserIdSearchCriteria() 

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
     * Sets the value of field 'ackTimeSearchCriteria'.
     * 
     * @param ackTimeSearchCriteria the value of field
     * 'ackTimeSearchCriteria'.
    **/
    public void setAckTimeSearchCriteria(AckTimeSearchCriteria ackTimeSearchCriteria)
    {
        this._ackTimeSearchCriteria = ackTimeSearchCriteria;
    } //-- void setAckTimeSearchCriteria(AckTimeSearchCriteria) 

    /**
     * Sets the value of field 'userIdSearchCriteria'.
     * 
     * @param userIdSearchCriteria the value of field
     * 'userIdSearchCriteria'.
    **/
    public void setUserIdSearchCriteria(UserIdSearchCriteria userIdSearchCriteria)
    {
        this._userIdSearchCriteria = userIdSearchCriteria;
    } //-- void setUserIdSearchCriteria(UserIdSearchCriteria) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

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
public abstract class TibcoEventNotificationType extends com.cisco.eManager.common.event2.EmanagerEventNotificationType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private long _tibcoEventId;

    /**
     * keeps track of state for field: _tibcoEventId
    **/
    private boolean _has_tibcoEventId;

    private java.lang.String _rule;

    private java.lang.String _test;

    private java.lang.String _instrumentationName;

    private MgmtPolicyId _mgmtPolicyId;


      //----------------/
     //- Constructors -/
    //----------------/

    public TibcoEventNotificationType() {
        super();
    } //-- com.cisco.eManager.common.event2.TibcoEventNotificationType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'instrumentationName'.
     * 
     * @return the value of field 'instrumentationName'.
    **/
    public java.lang.String getInstrumentationName()
    {
        return this._instrumentationName;
    } //-- java.lang.String getInstrumentationName() 

    /**
     * Returns the value of field 'mgmtPolicyId'.
     * 
     * @return the value of field 'mgmtPolicyId'.
    **/
    public MgmtPolicyId getMgmtPolicyId()
    {
        return this._mgmtPolicyId;
    } //-- MgmtPolicyId getMgmtPolicyId() 

    /**
     * Returns the value of field 'rule'.
     * 
     * @return the value of field 'rule'.
    **/
    public java.lang.String getRule()
    {
        return this._rule;
    } //-- java.lang.String getRule() 

    /**
     * Returns the value of field 'test'.
     * 
     * @return the value of field 'test'.
    **/
    public java.lang.String getTest()
    {
        return this._test;
    } //-- java.lang.String getTest() 

    /**
     * Returns the value of field 'tibcoEventId'.
     * 
     * @return the value of field 'tibcoEventId'.
    **/
    public long getTibcoEventId()
    {
        return this._tibcoEventId;
    } //-- long getTibcoEventId() 

    /**
    **/
    public boolean hasTibcoEventId()
    {
        return this._has_tibcoEventId;
    } //-- boolean hasTibcoEventId() 

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
     * Sets the value of field 'instrumentationName'.
     * 
     * @param instrumentationName the value of field
     * 'instrumentationName'.
    **/
    public void setInstrumentationName(java.lang.String instrumentationName)
    {
        this._instrumentationName = instrumentationName;
    } //-- void setInstrumentationName(java.lang.String) 

    /**
     * Sets the value of field 'mgmtPolicyId'.
     * 
     * @param mgmtPolicyId the value of field 'mgmtPolicyId'.
    **/
    public void setMgmtPolicyId(MgmtPolicyId mgmtPolicyId)
    {
        this._mgmtPolicyId = mgmtPolicyId;
    } //-- void setMgmtPolicyId(MgmtPolicyId) 

    /**
     * Sets the value of field 'rule'.
     * 
     * @param rule the value of field 'rule'.
    **/
    public void setRule(java.lang.String rule)
    {
        this._rule = rule;
    } //-- void setRule(java.lang.String) 

    /**
     * Sets the value of field 'test'.
     * 
     * @param test the value of field 'test'.
    **/
    public void setTest(java.lang.String test)
    {
        this._test = test;
    } //-- void setTest(java.lang.String) 

    /**
     * Sets the value of field 'tibcoEventId'.
     * 
     * @param tibcoEventId the value of field 'tibcoEventId'.
    **/
    public void setTibcoEventId(long tibcoEventId)
    {
        this._tibcoEventId = tibcoEventId;
        this._has_tibcoEventId = true;
    } //-- void setTibcoEventId(long) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

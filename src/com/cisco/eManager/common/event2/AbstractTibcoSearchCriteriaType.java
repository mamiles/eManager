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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public abstract class AbstractTibcoSearchCriteriaType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private TibcoEventIdSearchCriteria _tibcoEventIdSearchCriteria;

    private RuleSearchCriteria _ruleSearchCriteria;

    private TestSearchCriteria _testSearchCriteria;

    private InstrumentationSearchCriteria _instrumentationSearchCriteria;

    private java.util.ArrayList _managementPolicyIdsList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AbstractTibcoSearchCriteriaType() {
        super();
        _managementPolicyIdsList = new ArrayList();
    } //-- com.cisco.eManager.common.event2.AbstractTibcoSearchCriteriaType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vManagementPolicyIds
    **/
    public void addManagementPolicyIds(ManagementPolicyIds vManagementPolicyIds)
        throws java.lang.IndexOutOfBoundsException
    {
        _managementPolicyIdsList.add(vManagementPolicyIds);
    } //-- void addManagementPolicyIds(ManagementPolicyIds) 

    /**
     * 
     * 
     * @param index
     * @param vManagementPolicyIds
    **/
    public void addManagementPolicyIds(int index, ManagementPolicyIds vManagementPolicyIds)
        throws java.lang.IndexOutOfBoundsException
    {
        _managementPolicyIdsList.add(index, vManagementPolicyIds);
    } //-- void addManagementPolicyIds(int, ManagementPolicyIds) 

    /**
    **/
    public void clearManagementPolicyIds()
    {
        _managementPolicyIdsList.clear();
    } //-- void clearManagementPolicyIds() 

    /**
    **/
    public java.util.Enumeration enumerateManagementPolicyIds()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_managementPolicyIdsList.iterator());
    } //-- java.util.Enumeration enumerateManagementPolicyIds() 

    /**
     * Returns the value of field 'instrumentationSearchCriteria'.
     * 
     * @return the value of field 'instrumentationSearchCriteria'.
    **/
    public InstrumentationSearchCriteria getInstrumentationSearchCriteria()
    {
        return this._instrumentationSearchCriteria;
    } //-- InstrumentationSearchCriteria getInstrumentationSearchCriteria() 

    /**
     * 
     * 
     * @param index
    **/
    public ManagementPolicyIds getManagementPolicyIds(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _managementPolicyIdsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (ManagementPolicyIds) _managementPolicyIdsList.get(index);
    } //-- ManagementPolicyIds getManagementPolicyIds(int) 

    /**
    **/
    public ManagementPolicyIds[] getManagementPolicyIds()
    {
        int size = _managementPolicyIdsList.size();
        ManagementPolicyIds[] mArray = new ManagementPolicyIds[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ManagementPolicyIds) _managementPolicyIdsList.get(index);
        }
        return mArray;
    } //-- ManagementPolicyIds[] getManagementPolicyIds() 

    /**
    **/
    public int getManagementPolicyIdsCount()
    {
        return _managementPolicyIdsList.size();
    } //-- int getManagementPolicyIdsCount() 

    /**
     * Returns the value of field 'ruleSearchCriteria'.
     * 
     * @return the value of field 'ruleSearchCriteria'.
    **/
    public RuleSearchCriteria getRuleSearchCriteria()
    {
        return this._ruleSearchCriteria;
    } //-- RuleSearchCriteria getRuleSearchCriteria() 

    /**
     * Returns the value of field 'testSearchCriteria'.
     * 
     * @return the value of field 'testSearchCriteria'.
    **/
    public TestSearchCriteria getTestSearchCriteria()
    {
        return this._testSearchCriteria;
    } //-- TestSearchCriteria getTestSearchCriteria() 

    /**
     * Returns the value of field 'tibcoEventIdSearchCriteria'.
     * 
     * @return the value of field 'tibcoEventIdSearchCriteria'.
    **/
    public TibcoEventIdSearchCriteria getTibcoEventIdSearchCriteria()
    {
        return this._tibcoEventIdSearchCriteria;
    } //-- TibcoEventIdSearchCriteria getTibcoEventIdSearchCriteria() 

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
     * 
     * 
     * @param vManagementPolicyIds
    **/
    public boolean removeManagementPolicyIds(ManagementPolicyIds vManagementPolicyIds)
    {
        boolean removed = _managementPolicyIdsList.remove(vManagementPolicyIds);
        return removed;
    } //-- boolean removeManagementPolicyIds(ManagementPolicyIds) 

    /**
     * Sets the value of field 'instrumentationSearchCriteria'.
     * 
     * @param instrumentationSearchCriteria the value of field
     * 'instrumentationSearchCriteria'.
    **/
    public void setInstrumentationSearchCriteria(InstrumentationSearchCriteria instrumentationSearchCriteria)
    {
        this._instrumentationSearchCriteria = instrumentationSearchCriteria;
    } //-- void setInstrumentationSearchCriteria(InstrumentationSearchCriteria) 

    /**
     * 
     * 
     * @param index
     * @param vManagementPolicyIds
    **/
    public void setManagementPolicyIds(int index, ManagementPolicyIds vManagementPolicyIds)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _managementPolicyIdsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _managementPolicyIdsList.set(index, vManagementPolicyIds);
    } //-- void setManagementPolicyIds(int, ManagementPolicyIds) 

    /**
     * 
     * 
     * @param managementPolicyIdsArray
    **/
    public void setManagementPolicyIds(ManagementPolicyIds[] managementPolicyIdsArray)
    {
        //-- copy array
        _managementPolicyIdsList.clear();
        for (int i = 0; i < managementPolicyIdsArray.length; i++) {
            _managementPolicyIdsList.add(managementPolicyIdsArray[i]);
        }
    } //-- void setManagementPolicyIds(ManagementPolicyIds) 

    /**
     * Sets the value of field 'ruleSearchCriteria'.
     * 
     * @param ruleSearchCriteria the value of field
     * 'ruleSearchCriteria'.
    **/
    public void setRuleSearchCriteria(RuleSearchCriteria ruleSearchCriteria)
    {
        this._ruleSearchCriteria = ruleSearchCriteria;
    } //-- void setRuleSearchCriteria(RuleSearchCriteria) 

    /**
     * Sets the value of field 'testSearchCriteria'.
     * 
     * @param testSearchCriteria the value of field
     * 'testSearchCriteria'.
    **/
    public void setTestSearchCriteria(TestSearchCriteria testSearchCriteria)
    {
        this._testSearchCriteria = testSearchCriteria;
    } //-- void setTestSearchCriteria(TestSearchCriteria) 

    /**
     * Sets the value of field 'tibcoEventIdSearchCriteria'.
     * 
     * @param tibcoEventIdSearchCriteria the value of field
     * 'tibcoEventIdSearchCriteria'.
    **/
    public void setTibcoEventIdSearchCriteria(TibcoEventIdSearchCriteria tibcoEventIdSearchCriteria)
    {
        this._tibcoEventIdSearchCriteria = tibcoEventIdSearchCriteria;
    } //-- void setTibcoEventIdSearchCriteria(TibcoEventIdSearchCriteria) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

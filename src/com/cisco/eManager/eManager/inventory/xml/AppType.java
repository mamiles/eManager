/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cisco.eManager.eManager.inventory.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class AppType.
 * 
 * @version $Revision$ $Date$
 */
public class AppType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _rulebaseNameList
     */
    private java.util.Vector _rulebaseNameList;

    /**
     * Field _appInstance
     */
    private com.cisco.eManager.eManager.inventory.xml.AppInstance _appInstance;


      //----------------/
     //- Constructors -/
    //----------------/

    public AppType() {
        super();
        _rulebaseNameList = new Vector();
    } //-- com.cisco.eManager.eManager.inventory.xml.AppType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRulebaseName
     * 
     * @param vRulebaseName
     */
    public void addRulebaseName(java.lang.String vRulebaseName)
        throws java.lang.IndexOutOfBoundsException
    {
        _rulebaseNameList.addElement(vRulebaseName);
    } //-- void addRulebaseName(java.lang.String) 

    /**
     * Method addRulebaseName
     * 
     * @param index
     * @param vRulebaseName
     */
    public void addRulebaseName(int index, java.lang.String vRulebaseName)
        throws java.lang.IndexOutOfBoundsException
    {
        _rulebaseNameList.insertElementAt(vRulebaseName, index);
    } //-- void addRulebaseName(int, java.lang.String) 

    /**
     * Method enumerateRulebaseName
     */
    public java.util.Enumeration enumerateRulebaseName()
    {
        return _rulebaseNameList.elements();
    } //-- java.util.Enumeration enumerateRulebaseName() 

    /**
     * Returns the value of field 'appInstance'.
     * 
     * @return the value of field 'appInstance'.
     */
    public com.cisco.eManager.eManager.inventory.xml.AppInstance getAppInstance()
    {
        return this._appInstance;
    } //-- com.cisco.eManager.eManager.inventory.xml.AppInstance getAppInstance() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Method getRulebaseName
     * 
     * @param index
     */
    public java.lang.String getRulebaseName(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _rulebaseNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_rulebaseNameList.elementAt(index);
    } //-- java.lang.String getRulebaseName(int) 

    /**
     * Method getRulebaseName
     */
    public java.lang.String[] getRulebaseName()
    {
        int size = _rulebaseNameList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_rulebaseNameList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getRulebaseName() 

    /**
     * Method getRulebaseNameCount
     */
    public int getRulebaseNameCount()
    {
        return _rulebaseNameList.size();
    } //-- int getRulebaseNameCount() 

    /**
     * Method isValid
     */
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
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeAllRulebaseName
     */
    public void removeAllRulebaseName()
    {
        _rulebaseNameList.removeAllElements();
    } //-- void removeAllRulebaseName() 

    /**
     * Method removeRulebaseName
     * 
     * @param index
     */
    public java.lang.String removeRulebaseName(int index)
    {
        java.lang.Object obj = _rulebaseNameList.elementAt(index);
        _rulebaseNameList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeRulebaseName(int) 

    /**
     * Sets the value of field 'appInstance'.
     * 
     * @param appInstance the value of field 'appInstance'.
     */
    public void setAppInstance(com.cisco.eManager.eManager.inventory.xml.AppInstance appInstance)
    {
        this._appInstance = appInstance;
    } //-- void setAppInstance(com.cisco.eManager.eManager.inventory.xml.AppInstance) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method setRulebaseName
     * 
     * @param index
     * @param vRulebaseName
     */
    public void setRulebaseName(int index, java.lang.String vRulebaseName)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _rulebaseNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _rulebaseNameList.setElementAt(vRulebaseName, index);
    } //-- void setRulebaseName(int, java.lang.String) 

    /**
     * Method setRulebaseName
     * 
     * @param rulebaseNameArray
     */
    public void setRulebaseName(java.lang.String[] rulebaseNameArray)
    {
        //-- copy array
        _rulebaseNameList.removeAllElements();
        for (int i = 0; i < rulebaseNameArray.length; i++) {
            _rulebaseNameList.addElement(rulebaseNameArray[i]);
        }
    } //-- void setRulebaseName(java.lang.String) 

    /**
     * Method unmarshalAppType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAppType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.eManager.inventory.xml.AppType) Unmarshaller.unmarshal(com.cisco.eManager.eManager.inventory.xml.AppType.class, reader);
    } //-- java.lang.Object unmarshalAppType(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

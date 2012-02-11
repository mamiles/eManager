/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cisco.eManager.common.register.reRegistration;

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
 * Class AppMgmtPolicies.
 * 
 * @version $Revision$ $Date$
 */
public class AppMgmtPolicies implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _appMgmtPolicyList
     */
    private java.util.Vector _appMgmtPolicyList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AppMgmtPolicies() {
        super();
        _appMgmtPolicyList = new Vector();
    } //-- com.cisco.eManager.common.register.reRegistration.AppMgmtPolicies()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAppMgmtPolicy
     * 
     * @param vAppMgmtPolicy
     */
    public void addAppMgmtPolicy(java.lang.String vAppMgmtPolicy)
        throws java.lang.IndexOutOfBoundsException
    {
        _appMgmtPolicyList.addElement(vAppMgmtPolicy);
    } //-- void addAppMgmtPolicy(java.lang.String) 

    /**
     * Method addAppMgmtPolicy
     * 
     * @param index
     * @param vAppMgmtPolicy
     */
    public void addAppMgmtPolicy(int index, java.lang.String vAppMgmtPolicy)
        throws java.lang.IndexOutOfBoundsException
    {
        _appMgmtPolicyList.insertElementAt(vAppMgmtPolicy, index);
    } //-- void addAppMgmtPolicy(int, java.lang.String) 

    /**
     * Method enumerateAppMgmtPolicy
     */
    public java.util.Enumeration enumerateAppMgmtPolicy()
    {
        return _appMgmtPolicyList.elements();
    } //-- java.util.Enumeration enumerateAppMgmtPolicy() 

    /**
     * Method getAppMgmtPolicy
     * 
     * @param index
     */
    public java.lang.String getAppMgmtPolicy(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appMgmtPolicyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_appMgmtPolicyList.elementAt(index);
    } //-- java.lang.String getAppMgmtPolicy(int) 

    /**
     * Method getAppMgmtPolicy
     */
    public java.lang.String[] getAppMgmtPolicy()
    {
        int size = _appMgmtPolicyList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_appMgmtPolicyList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getAppMgmtPolicy() 

    /**
     * Method getAppMgmtPolicyCount
     */
    public int getAppMgmtPolicyCount()
    {
        return _appMgmtPolicyList.size();
    } //-- int getAppMgmtPolicyCount() 

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
     * Method removeAllAppMgmtPolicy
     */
    public void removeAllAppMgmtPolicy()
    {
        _appMgmtPolicyList.removeAllElements();
    } //-- void removeAllAppMgmtPolicy() 

    /**
     * Method removeAppMgmtPolicy
     * 
     * @param index
     */
    public java.lang.String removeAppMgmtPolicy(int index)
    {
        java.lang.Object obj = _appMgmtPolicyList.elementAt(index);
        _appMgmtPolicyList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeAppMgmtPolicy(int) 

    /**
     * Method setAppMgmtPolicy
     * 
     * @param index
     * @param vAppMgmtPolicy
     */
    public void setAppMgmtPolicy(int index, java.lang.String vAppMgmtPolicy)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appMgmtPolicyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _appMgmtPolicyList.setElementAt(vAppMgmtPolicy, index);
    } //-- void setAppMgmtPolicy(int, java.lang.String) 

    /**
     * Method setAppMgmtPolicy
     * 
     * @param appMgmtPolicyArray
     */
    public void setAppMgmtPolicy(java.lang.String[] appMgmtPolicyArray)
    {
        //-- copy array
        _appMgmtPolicyList.removeAllElements();
        for (int i = 0; i < appMgmtPolicyArray.length; i++) {
            _appMgmtPolicyList.addElement(appMgmtPolicyArray[i]);
        }
    } //-- void setAppMgmtPolicy(java.lang.String) 

    /**
     * Method unmarshalAppMgmtPolicies
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAppMgmtPolicies(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.register.reRegistration.AppMgmtPolicies) Unmarshaller.unmarshal(com.cisco.eManager.common.register.reRegistration.AppMgmtPolicies.class, reader);
    } //-- java.lang.Object unmarshalAppMgmtPolicies(java.io.Reader) 

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

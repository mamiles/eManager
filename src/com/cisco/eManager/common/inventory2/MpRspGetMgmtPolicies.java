/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cisco.eManager.common.inventory2;

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
 * Class MpRspGetMgmtPolicies.
 * 
 * @version $Revision$ $Date$
 */
public class MpRspGetMgmtPolicies implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mgmtPoliciesList
     */
    private java.util.Vector _mgmtPoliciesList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MpRspGetMgmtPolicies() {
        super();
        _mgmtPoliciesList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.MpRspGetMgmtPolicies()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addMgmtPolicies
     * 
     * @param vMgmtPolicies
     */
    public void addMgmtPolicies(com.cisco.eManager.common.inventory2.MgmtPolicy vMgmtPolicies)
        throws java.lang.IndexOutOfBoundsException
    {
        _mgmtPoliciesList.addElement(vMgmtPolicies);
    } //-- void addMgmtPolicies(com.cisco.eManager.common.inventory2.MgmtPolicy) 

    /**
     * Method addMgmtPolicies
     * 
     * @param index
     * @param vMgmtPolicies
     */
    public void addMgmtPolicies(int index, com.cisco.eManager.common.inventory2.MgmtPolicy vMgmtPolicies)
        throws java.lang.IndexOutOfBoundsException
    {
        _mgmtPoliciesList.insertElementAt(vMgmtPolicies, index);
    } //-- void addMgmtPolicies(int, com.cisco.eManager.common.inventory2.MgmtPolicy) 

    /**
     * Method enumerateMgmtPolicies
     */
    public java.util.Enumeration enumerateMgmtPolicies()
    {
        return _mgmtPoliciesList.elements();
    } //-- java.util.Enumeration enumerateMgmtPolicies() 

    /**
     * Method getMgmtPolicies
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.MgmtPolicy getMgmtPolicies(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mgmtPoliciesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.MgmtPolicy) _mgmtPoliciesList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.MgmtPolicy getMgmtPolicies(int) 

    /**
     * Method getMgmtPolicies
     */
    public com.cisco.eManager.common.inventory2.MgmtPolicy[] getMgmtPolicies()
    {
        int size = _mgmtPoliciesList.size();
        com.cisco.eManager.common.inventory2.MgmtPolicy[] mArray = new com.cisco.eManager.common.inventory2.MgmtPolicy[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.MgmtPolicy) _mgmtPoliciesList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.MgmtPolicy[] getMgmtPolicies() 

    /**
     * Method getMgmtPoliciesCount
     */
    public int getMgmtPoliciesCount()
    {
        return _mgmtPoliciesList.size();
    } //-- int getMgmtPoliciesCount() 

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
     * Method removeAllMgmtPolicies
     */
    public void removeAllMgmtPolicies()
    {
        _mgmtPoliciesList.removeAllElements();
    } //-- void removeAllMgmtPolicies() 

    /**
     * Method removeMgmtPolicies
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.MgmtPolicy removeMgmtPolicies(int index)
    {
        java.lang.Object obj = _mgmtPoliciesList.elementAt(index);
        _mgmtPoliciesList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.MgmtPolicy) obj;
    } //-- com.cisco.eManager.common.inventory2.MgmtPolicy removeMgmtPolicies(int) 

    /**
     * Method setMgmtPolicies
     * 
     * @param index
     * @param vMgmtPolicies
     */
    public void setMgmtPolicies(int index, com.cisco.eManager.common.inventory2.MgmtPolicy vMgmtPolicies)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mgmtPoliciesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _mgmtPoliciesList.setElementAt(vMgmtPolicies, index);
    } //-- void setMgmtPolicies(int, com.cisco.eManager.common.inventory2.MgmtPolicy) 

    /**
     * Method setMgmtPolicies
     * 
     * @param mgmtPoliciesArray
     */
    public void setMgmtPolicies(com.cisco.eManager.common.inventory2.MgmtPolicy[] mgmtPoliciesArray)
    {
        //-- copy array
        _mgmtPoliciesList.removeAllElements();
        for (int i = 0; i < mgmtPoliciesArray.length; i++) {
            _mgmtPoliciesList.addElement(mgmtPoliciesArray[i]);
        }
    } //-- void setMgmtPolicies(com.cisco.eManager.common.inventory2.MgmtPolicy) 

    /**
     * Method unmarshalMpRspGetMgmtPolicies
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalMpRspGetMgmtPolicies(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.MpRspGetMgmtPolicies) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.MpRspGetMgmtPolicies.class, reader);
    } //-- java.lang.Object unmarshalMpRspGetMgmtPolicies(java.io.Reader) 

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

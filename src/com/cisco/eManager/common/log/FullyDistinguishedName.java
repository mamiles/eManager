/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.log;

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
public abstract class FullyDistinguishedName implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _elementNameList;


      //----------------/
     //- Constructors -/
    //----------------/

    public FullyDistinguishedName() {
        super();
        _elementNameList = new ArrayList();
    } //-- com.cisco.eManager.common.log.FullyDistinguishedName()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vElementName
    **/
    public void addElementName(java.lang.String vElementName)
        throws java.lang.IndexOutOfBoundsException
    {
        _elementNameList.add(vElementName);
    } //-- void addElementName(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vElementName
    **/
    public void addElementName(int index, java.lang.String vElementName)
        throws java.lang.IndexOutOfBoundsException
    {
        _elementNameList.add(index, vElementName);
    } //-- void addElementName(int, java.lang.String) 

    /**
    **/
    public void clearElementName()
    {
        _elementNameList.clear();
    } //-- void clearElementName() 

    /**
    **/
    public java.util.Enumeration enumerateElementName()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_elementNameList.iterator());
    } //-- java.util.Enumeration enumerateElementName() 

    /**
     * 
     * 
     * @param index
    **/
    public java.lang.String getElementName(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _elementNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_elementNameList.get(index);
    } //-- java.lang.String getElementName(int) 

    /**
    **/
    public java.lang.String[] getElementName()
    {
        int size = _elementNameList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_elementNameList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getElementName() 

    /**
    **/
    public int getElementNameCount()
    {
        return _elementNameList.size();
    } //-- int getElementNameCount() 

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
     * @param vElementName
    **/
    public boolean removeElementName(java.lang.String vElementName)
    {
        boolean removed = _elementNameList.remove(vElementName);
        return removed;
    } //-- boolean removeElementName(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vElementName
    **/
    public void setElementName(int index, java.lang.String vElementName)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _elementNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _elementNameList.set(index, vElementName);
    } //-- void setElementName(int, java.lang.String) 

    /**
     * 
     * 
     * @param elementNameArray
    **/
    public void setElementName(java.lang.String[] elementNameArray)
    {
        //-- copy array
        _elementNameList.clear();
        for (int i = 0; i < elementNameArray.length; i++) {
            _elementNameList.add(elementNameArray[i]);
        }
    } //-- void setElementName(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

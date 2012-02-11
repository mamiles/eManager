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
 * Class FullyDistinguishedName.
 * 
 * @version $Revision$ $Date$
 */
public class FullyDistinguishedName implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _elementNameList
     */
    private java.util.Vector _elementNameList;


      //----------------/
     //- Constructors -/
    //----------------/

    public FullyDistinguishedName() {
        super();
        _elementNameList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.FullyDistinguishedName()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addElementName
     * 
     * @param vElementName
     */
    public void addElementName(java.lang.String vElementName)
        throws java.lang.IndexOutOfBoundsException
    {
        _elementNameList.addElement(vElementName);
    } //-- void addElementName(java.lang.String) 

    /**
     * Method addElementName
     * 
     * @param index
     * @param vElementName
     */
    public void addElementName(int index, java.lang.String vElementName)
        throws java.lang.IndexOutOfBoundsException
    {
        _elementNameList.insertElementAt(vElementName, index);
    } //-- void addElementName(int, java.lang.String) 

    /**
     * Method enumerateElementName
     */
    public java.util.Enumeration enumerateElementName()
    {
        return _elementNameList.elements();
    } //-- java.util.Enumeration enumerateElementName() 

    /**
     * Method getElementName
     * 
     * @param index
     */
    public java.lang.String getElementName(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _elementNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_elementNameList.elementAt(index);
    } //-- java.lang.String getElementName(int) 

    /**
     * Method getElementName
     */
    public java.lang.String[] getElementName()
    {
        int size = _elementNameList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_elementNameList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getElementName() 

    /**
     * Method getElementNameCount
     */
    public int getElementNameCount()
    {
        return _elementNameList.size();
    } //-- int getElementNameCount() 

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
     * Method removeAllElementName
     */
    public void removeAllElementName()
    {
        _elementNameList.removeAllElements();
    } //-- void removeAllElementName() 

    /**
     * Method removeElementName
     * 
     * @param index
     */
    public java.lang.String removeElementName(int index)
    {
        java.lang.Object obj = _elementNameList.elementAt(index);
        _elementNameList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeElementName(int) 

    /**
     * Method setElementName
     * 
     * @param index
     * @param vElementName
     */
    public void setElementName(int index, java.lang.String vElementName)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _elementNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _elementNameList.setElementAt(vElementName, index);
    } //-- void setElementName(int, java.lang.String) 

    /**
     * Method setElementName
     * 
     * @param elementNameArray
     */
    public void setElementName(java.lang.String[] elementNameArray)
    {
        //-- copy array
        _elementNameList.removeAllElements();
        for (int i = 0; i < elementNameArray.length; i++) {
            _elementNameList.addElement(elementNameArray[i]);
        }
    } //-- void setElementName(java.lang.String) 

    /**
     * Method unmarshalFullyDistinguishedName
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalFullyDistinguishedName(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.FullyDistinguishedName) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.FullyDistinguishedName.class, reader);
    } //-- java.lang.Object unmarshalFullyDistinguishedName(java.io.Reader) 

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

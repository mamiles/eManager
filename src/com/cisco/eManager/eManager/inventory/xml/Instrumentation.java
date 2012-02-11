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
 * Class Instrumentation.
 * 
 * @version $Revision$ $Date$
 */
public class Instrumentation implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _methodList
     */
    private java.util.Vector _methodList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Instrumentation() {
        super();
        _methodList = new Vector();
    } //-- com.cisco.eManager.eManager.inventory.xml.Instrumentation()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addMethod
     * 
     * @param vMethod
     */
    public void addMethod(com.cisco.eManager.eManager.inventory.xml.Method vMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        _methodList.addElement(vMethod);
    } //-- void addMethod(com.cisco.eManager.eManager.inventory.xml.Method) 

    /**
     * Method addMethod
     * 
     * @param index
     * @param vMethod
     */
    public void addMethod(int index, com.cisco.eManager.eManager.inventory.xml.Method vMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        _methodList.insertElementAt(vMethod, index);
    } //-- void addMethod(int, com.cisco.eManager.eManager.inventory.xml.Method) 

    /**
     * Method enumerateMethod
     */
    public java.util.Enumeration enumerateMethod()
    {
        return _methodList.elements();
    } //-- java.util.Enumeration enumerateMethod() 

    /**
     * Method getMethod
     * 
     * @param index
     */
    public com.cisco.eManager.eManager.inventory.xml.Method getMethod(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _methodList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.eManager.inventory.xml.Method) _methodList.elementAt(index);
    } //-- com.cisco.eManager.eManager.inventory.xml.Method getMethod(int) 

    /**
     * Method getMethod
     */
    public com.cisco.eManager.eManager.inventory.xml.Method[] getMethod()
    {
        int size = _methodList.size();
        com.cisco.eManager.eManager.inventory.xml.Method[] mArray = new com.cisco.eManager.eManager.inventory.xml.Method[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.eManager.inventory.xml.Method) _methodList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.eManager.inventory.xml.Method[] getMethod() 

    /**
     * Method getMethodCount
     */
    public int getMethodCount()
    {
        return _methodList.size();
    } //-- int getMethodCount() 

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
     * Method removeAllMethod
     */
    public void removeAllMethod()
    {
        _methodList.removeAllElements();
    } //-- void removeAllMethod() 

    /**
     * Method removeMethod
     * 
     * @param index
     */
    public com.cisco.eManager.eManager.inventory.xml.Method removeMethod(int index)
    {
        java.lang.Object obj = _methodList.elementAt(index);
        _methodList.removeElementAt(index);
        return (com.cisco.eManager.eManager.inventory.xml.Method) obj;
    } //-- com.cisco.eManager.eManager.inventory.xml.Method removeMethod(int) 

    /**
     * Method setMethod
     * 
     * @param index
     * @param vMethod
     */
    public void setMethod(int index, com.cisco.eManager.eManager.inventory.xml.Method vMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _methodList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _methodList.setElementAt(vMethod, index);
    } //-- void setMethod(int, com.cisco.eManager.eManager.inventory.xml.Method) 

    /**
     * Method setMethod
     * 
     * @param methodArray
     */
    public void setMethod(com.cisco.eManager.eManager.inventory.xml.Method[] methodArray)
    {
        //-- copy array
        _methodList.removeAllElements();
        for (int i = 0; i < methodArray.length; i++) {
            _methodList.addElement(methodArray[i]);
        }
    } //-- void setMethod(com.cisco.eManager.eManager.inventory.xml.Method) 

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
     * Method unmarshalInstrumentation
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalInstrumentation(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.eManager.inventory.xml.Instrumentation) Unmarshaller.unmarshal(com.cisco.eManager.eManager.inventory.xml.Instrumentation.class, reader);
    } //-- java.lang.Object unmarshalInstrumentation(java.io.Reader) 

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

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
 * Class IRspGetMethods.
 * 
 * @version $Revision$ $Date$
 */
public class IRspGetMethods implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _methodsList
     */
    private java.util.Vector _methodsList;


      //----------------/
     //- Constructors -/
    //----------------/

    public IRspGetMethods() {
        super();
        _methodsList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.IRspGetMethods()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addMethods
     * 
     * @param vMethods
     */
    public void addMethods(com.cisco.eManager.common.inventory2.Method vMethods)
        throws java.lang.IndexOutOfBoundsException
    {
        _methodsList.addElement(vMethods);
    } //-- void addMethods(com.cisco.eManager.common.inventory2.Method) 

    /**
     * Method addMethods
     * 
     * @param index
     * @param vMethods
     */
    public void addMethods(int index, com.cisco.eManager.common.inventory2.Method vMethods)
        throws java.lang.IndexOutOfBoundsException
    {
        _methodsList.insertElementAt(vMethods, index);
    } //-- void addMethods(int, com.cisco.eManager.common.inventory2.Method) 

    /**
     * Method enumerateMethods
     */
    public java.util.Enumeration enumerateMethods()
    {
        return _methodsList.elements();
    } //-- java.util.Enumeration enumerateMethods() 

    /**
     * Method getMethods
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.Method getMethods(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _methodsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.Method) _methodsList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.Method getMethods(int) 

    /**
     * Method getMethods
     */
    public com.cisco.eManager.common.inventory2.Method[] getMethods()
    {
        int size = _methodsList.size();
        com.cisco.eManager.common.inventory2.Method[] mArray = new com.cisco.eManager.common.inventory2.Method[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.Method) _methodsList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.Method[] getMethods() 

    /**
     * Method getMethodsCount
     */
    public int getMethodsCount()
    {
        return _methodsList.size();
    } //-- int getMethodsCount() 

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
     * Method removeAllMethods
     */
    public void removeAllMethods()
    {
        _methodsList.removeAllElements();
    } //-- void removeAllMethods() 

    /**
     * Method removeMethods
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.Method removeMethods(int index)
    {
        java.lang.Object obj = _methodsList.elementAt(index);
        _methodsList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.Method) obj;
    } //-- com.cisco.eManager.common.inventory2.Method removeMethods(int) 

    /**
     * Method setMethods
     * 
     * @param index
     * @param vMethods
     */
    public void setMethods(int index, com.cisco.eManager.common.inventory2.Method vMethods)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _methodsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _methodsList.setElementAt(vMethods, index);
    } //-- void setMethods(int, com.cisco.eManager.common.inventory2.Method) 

    /**
     * Method setMethods
     * 
     * @param methodsArray
     */
    public void setMethods(com.cisco.eManager.common.inventory2.Method[] methodsArray)
    {
        //-- copy array
        _methodsList.removeAllElements();
        for (int i = 0; i < methodsArray.length; i++) {
            _methodsList.addElement(methodsArray[i]);
        }
    } //-- void setMethods(com.cisco.eManager.common.inventory2.Method) 

    /**
     * Method unmarshalIRspGetMethods
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalIRspGetMethods(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.IRspGetMethods) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.IRspGetMethods.class, reader);
    } //-- java.lang.Object unmarshalIRspGetMethods(java.io.Reader) 

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

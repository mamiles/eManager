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
 * Class Method.
 * 
 * @version $Revision$ $Date$
 */
public class Method implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _parameterList
     */
    private java.util.Vector _parameterList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Method() {
        super();
        _parameterList = new Vector();
    } //-- com.cisco.eManager.eManager.inventory.xml.Method()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addParameter
     * 
     * @param vParameter
     */
    public void addParameter(com.cisco.eManager.eManager.inventory.xml.Parameter vParameter)
        throws java.lang.IndexOutOfBoundsException
    {
        _parameterList.addElement(vParameter);
    } //-- void addParameter(com.cisco.eManager.eManager.inventory.xml.Parameter) 

    /**
     * Method addParameter
     * 
     * @param index
     * @param vParameter
     */
    public void addParameter(int index, com.cisco.eManager.eManager.inventory.xml.Parameter vParameter)
        throws java.lang.IndexOutOfBoundsException
    {
        _parameterList.insertElementAt(vParameter, index);
    } //-- void addParameter(int, com.cisco.eManager.eManager.inventory.xml.Parameter) 

    /**
     * Method enumerateParameter
     */
    public java.util.Enumeration enumerateParameter()
    {
        return _parameterList.elements();
    } //-- java.util.Enumeration enumerateParameter() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

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
     * Method getParameter
     * 
     * @param index
     */
    public com.cisco.eManager.eManager.inventory.xml.Parameter getParameter(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.eManager.inventory.xml.Parameter) _parameterList.elementAt(index);
    } //-- com.cisco.eManager.eManager.inventory.xml.Parameter getParameter(int) 

    /**
     * Method getParameter
     */
    public com.cisco.eManager.eManager.inventory.xml.Parameter[] getParameter()
    {
        int size = _parameterList.size();
        com.cisco.eManager.eManager.inventory.xml.Parameter[] mArray = new com.cisco.eManager.eManager.inventory.xml.Parameter[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.eManager.inventory.xml.Parameter) _parameterList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.eManager.inventory.xml.Parameter[] getParameter() 

    /**
     * Method getParameterCount
     */
    public int getParameterCount()
    {
        return _parameterList.size();
    } //-- int getParameterCount() 

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
     * Method removeAllParameter
     */
    public void removeAllParameter()
    {
        _parameterList.removeAllElements();
    } //-- void removeAllParameter() 

    /**
     * Method removeParameter
     * 
     * @param index
     */
    public com.cisco.eManager.eManager.inventory.xml.Parameter removeParameter(int index)
    {
        java.lang.Object obj = _parameterList.elementAt(index);
        _parameterList.removeElementAt(index);
        return (com.cisco.eManager.eManager.inventory.xml.Parameter) obj;
    } //-- com.cisco.eManager.eManager.inventory.xml.Parameter removeParameter(int) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

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
     * Method setParameter
     * 
     * @param index
     * @param vParameter
     */
    public void setParameter(int index, com.cisco.eManager.eManager.inventory.xml.Parameter vParameter)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _parameterList.setElementAt(vParameter, index);
    } //-- void setParameter(int, com.cisco.eManager.eManager.inventory.xml.Parameter) 

    /**
     * Method setParameter
     * 
     * @param parameterArray
     */
    public void setParameter(com.cisco.eManager.eManager.inventory.xml.Parameter[] parameterArray)
    {
        //-- copy array
        _parameterList.removeAllElements();
        for (int i = 0; i < parameterArray.length; i++) {
            _parameterList.addElement(parameterArray[i]);
        }
    } //-- void setParameter(com.cisco.eManager.eManager.inventory.xml.Parameter) 

    /**
     * Method unmarshalMethod
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalMethod(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.eManager.inventory.xml.Method) Unmarshaller.unmarshal(com.cisco.eManager.eManager.inventory.xml.Method.class, reader);
    } //-- java.lang.Object unmarshalMethod(java.io.Reader) 

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

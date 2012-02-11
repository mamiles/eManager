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
 * Class AppInstance.
 * 
 * @version $Revision$ $Date$
 */
public class AppInstance implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _instrumentationList
     */
    private java.util.Vector _instrumentationList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AppInstance() {
        super();
        _instrumentationList = new Vector();
    } //-- com.cisco.eManager.eManager.inventory.xml.AppInstance()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addInstrumentation
     * 
     * @param vInstrumentation
     */
    public void addInstrumentation(com.cisco.eManager.eManager.inventory.xml.Instrumentation vInstrumentation)
        throws java.lang.IndexOutOfBoundsException
    {
        _instrumentationList.addElement(vInstrumentation);
    } //-- void addInstrumentation(com.cisco.eManager.eManager.inventory.xml.Instrumentation) 

    /**
     * Method addInstrumentation
     * 
     * @param index
     * @param vInstrumentation
     */
    public void addInstrumentation(int index, com.cisco.eManager.eManager.inventory.xml.Instrumentation vInstrumentation)
        throws java.lang.IndexOutOfBoundsException
    {
        _instrumentationList.insertElementAt(vInstrumentation, index);
    } //-- void addInstrumentation(int, com.cisco.eManager.eManager.inventory.xml.Instrumentation) 

    /**
     * Method enumerateInstrumentation
     */
    public java.util.Enumeration enumerateInstrumentation()
    {
        return _instrumentationList.elements();
    } //-- java.util.Enumeration enumerateInstrumentation() 

    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'id'.
     */
    public java.lang.String getId()
    {
        return this._id;
    } //-- java.lang.String getId() 

    /**
     * Method getInstrumentation
     * 
     * @param index
     */
    public com.cisco.eManager.eManager.inventory.xml.Instrumentation getInstrumentation(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instrumentationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.eManager.inventory.xml.Instrumentation) _instrumentationList.elementAt(index);
    } //-- com.cisco.eManager.eManager.inventory.xml.Instrumentation getInstrumentation(int) 

    /**
     * Method getInstrumentation
     */
    public com.cisco.eManager.eManager.inventory.xml.Instrumentation[] getInstrumentation()
    {
        int size = _instrumentationList.size();
        com.cisco.eManager.eManager.inventory.xml.Instrumentation[] mArray = new com.cisco.eManager.eManager.inventory.xml.Instrumentation[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.eManager.inventory.xml.Instrumentation) _instrumentationList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.eManager.inventory.xml.Instrumentation[] getInstrumentation() 

    /**
     * Method getInstrumentationCount
     */
    public int getInstrumentationCount()
    {
        return _instrumentationList.size();
    } //-- int getInstrumentationCount() 

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
     * Method removeAllInstrumentation
     */
    public void removeAllInstrumentation()
    {
        _instrumentationList.removeAllElements();
    } //-- void removeAllInstrumentation() 

    /**
     * Method removeInstrumentation
     * 
     * @param index
     */
    public com.cisco.eManager.eManager.inventory.xml.Instrumentation removeInstrumentation(int index)
    {
        java.lang.Object obj = _instrumentationList.elementAt(index);
        _instrumentationList.removeElementAt(index);
        return (com.cisco.eManager.eManager.inventory.xml.Instrumentation) obj;
    } //-- com.cisco.eManager.eManager.inventory.xml.Instrumentation removeInstrumentation(int) 

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id)
    {
        this._id = id;
    } //-- void setId(java.lang.String) 

    /**
     * Method setInstrumentation
     * 
     * @param index
     * @param vInstrumentation
     */
    public void setInstrumentation(int index, com.cisco.eManager.eManager.inventory.xml.Instrumentation vInstrumentation)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instrumentationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _instrumentationList.setElementAt(vInstrumentation, index);
    } //-- void setInstrumentation(int, com.cisco.eManager.eManager.inventory.xml.Instrumentation) 

    /**
     * Method setInstrumentation
     * 
     * @param instrumentationArray
     */
    public void setInstrumentation(com.cisco.eManager.eManager.inventory.xml.Instrumentation[] instrumentationArray)
    {
        //-- copy array
        _instrumentationList.removeAllElements();
        for (int i = 0; i < instrumentationArray.length; i++) {
            _instrumentationList.addElement(instrumentationArray[i]);
        }
    } //-- void setInstrumentation(com.cisco.eManager.eManager.inventory.xml.Instrumentation) 

    /**
     * Method unmarshalAppInstance
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAppInstance(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.eManager.inventory.xml.AppInstance) Unmarshaller.unmarshal(com.cisco.eManager.eManager.inventory.xml.AppInstance.class, reader);
    } //-- java.lang.Object unmarshalAppInstance(java.io.Reader) 

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

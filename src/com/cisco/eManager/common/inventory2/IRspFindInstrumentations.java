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
 * Class IRspFindInstrumentations.
 * 
 * @version $Revision$ $Date$
 */
public class IRspFindInstrumentations implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _instrumentationsList
     */
    private java.util.Vector _instrumentationsList;


      //----------------/
     //- Constructors -/
    //----------------/

    public IRspFindInstrumentations() {
        super();
        _instrumentationsList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.IRspFindInstrumentations()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addInstrumentations
     * 
     * @param vInstrumentations
     */
    public void addInstrumentations(com.cisco.eManager.common.inventory2.Instrumentation vInstrumentations)
        throws java.lang.IndexOutOfBoundsException
    {
        _instrumentationsList.addElement(vInstrumentations);
    } //-- void addInstrumentations(com.cisco.eManager.common.inventory2.Instrumentation) 

    /**
     * Method addInstrumentations
     * 
     * @param index
     * @param vInstrumentations
     */
    public void addInstrumentations(int index, com.cisco.eManager.common.inventory2.Instrumentation vInstrumentations)
        throws java.lang.IndexOutOfBoundsException
    {
        _instrumentationsList.insertElementAt(vInstrumentations, index);
    } //-- void addInstrumentations(int, com.cisco.eManager.common.inventory2.Instrumentation) 

    /**
     * Method enumerateInstrumentations
     */
    public java.util.Enumeration enumerateInstrumentations()
    {
        return _instrumentationsList.elements();
    } //-- java.util.Enumeration enumerateInstrumentations() 

    /**
     * Method getInstrumentations
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.Instrumentation getInstrumentations(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instrumentationsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.Instrumentation) _instrumentationsList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.Instrumentation getInstrumentations(int) 

    /**
     * Method getInstrumentations
     */
    public com.cisco.eManager.common.inventory2.Instrumentation[] getInstrumentations()
    {
        int size = _instrumentationsList.size();
        com.cisco.eManager.common.inventory2.Instrumentation[] mArray = new com.cisco.eManager.common.inventory2.Instrumentation[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.Instrumentation) _instrumentationsList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.Instrumentation[] getInstrumentations() 

    /**
     * Method getInstrumentationsCount
     */
    public int getInstrumentationsCount()
    {
        return _instrumentationsList.size();
    } //-- int getInstrumentationsCount() 

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
     * Method removeAllInstrumentations
     */
    public void removeAllInstrumentations()
    {
        _instrumentationsList.removeAllElements();
    } //-- void removeAllInstrumentations() 

    /**
     * Method removeInstrumentations
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.Instrumentation removeInstrumentations(int index)
    {
        java.lang.Object obj = _instrumentationsList.elementAt(index);
        _instrumentationsList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.Instrumentation) obj;
    } //-- com.cisco.eManager.common.inventory2.Instrumentation removeInstrumentations(int) 

    /**
     * Method setInstrumentations
     * 
     * @param index
     * @param vInstrumentations
     */
    public void setInstrumentations(int index, com.cisco.eManager.common.inventory2.Instrumentation vInstrumentations)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instrumentationsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _instrumentationsList.setElementAt(vInstrumentations, index);
    } //-- void setInstrumentations(int, com.cisco.eManager.common.inventory2.Instrumentation) 

    /**
     * Method setInstrumentations
     * 
     * @param instrumentationsArray
     */
    public void setInstrumentations(com.cisco.eManager.common.inventory2.Instrumentation[] instrumentationsArray)
    {
        //-- copy array
        _instrumentationsList.removeAllElements();
        for (int i = 0; i < instrumentationsArray.length; i++) {
            _instrumentationsList.addElement(instrumentationsArray[i]);
        }
    } //-- void setInstrumentations(com.cisco.eManager.common.inventory2.Instrumentation) 

    /**
     * Method unmarshalIRspFindInstrumentations
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalIRspFindInstrumentations(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.IRspFindInstrumentations) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.IRspFindInstrumentations.class, reader);
    } //-- java.lang.Object unmarshalIRspFindInstrumentations(java.io.Reader) 

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

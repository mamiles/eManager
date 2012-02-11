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
 * Class IRspInvokeMethod.
 * 
 * @version $Revision$ $Date$
 */
public class IRspInvokeMethod implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _resultsList
     */
    private java.util.Vector _resultsList;


      //----------------/
     //- Constructors -/
    //----------------/

    public IRspInvokeMethod() {
        super();
        _resultsList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.IRspInvokeMethod()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addResults
     * 
     * @param vResults
     */
    public void addResults(com.cisco.eManager.common.inventory2.NvPair vResults)
        throws java.lang.IndexOutOfBoundsException
    {
        _resultsList.addElement(vResults);
    } //-- void addResults(com.cisco.eManager.common.inventory2.NvPair) 

    /**
     * Method addResults
     * 
     * @param index
     * @param vResults
     */
    public void addResults(int index, com.cisco.eManager.common.inventory2.NvPair vResults)
        throws java.lang.IndexOutOfBoundsException
    {
        _resultsList.insertElementAt(vResults, index);
    } //-- void addResults(int, com.cisco.eManager.common.inventory2.NvPair) 

    /**
     * Method enumerateResults
     */
    public java.util.Enumeration enumerateResults()
    {
        return _resultsList.elements();
    } //-- java.util.Enumeration enumerateResults() 

    /**
     * Method getResults
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.NvPair getResults(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _resultsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.NvPair) _resultsList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.NvPair getResults(int) 

    /**
     * Method getResults
     */
    public com.cisco.eManager.common.inventory2.NvPair[] getResults()
    {
        int size = _resultsList.size();
        com.cisco.eManager.common.inventory2.NvPair[] mArray = new com.cisco.eManager.common.inventory2.NvPair[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.NvPair) _resultsList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.NvPair[] getResults() 

    /**
     * Method getResultsCount
     */
    public int getResultsCount()
    {
        return _resultsList.size();
    } //-- int getResultsCount() 

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
     * Method removeAllResults
     */
    public void removeAllResults()
    {
        _resultsList.removeAllElements();
    } //-- void removeAllResults() 

    /**
     * Method removeResults
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.NvPair removeResults(int index)
    {
        java.lang.Object obj = _resultsList.elementAt(index);
        _resultsList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.NvPair) obj;
    } //-- com.cisco.eManager.common.inventory2.NvPair removeResults(int) 

    /**
     * Method setResults
     * 
     * @param index
     * @param vResults
     */
    public void setResults(int index, com.cisco.eManager.common.inventory2.NvPair vResults)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _resultsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _resultsList.setElementAt(vResults, index);
    } //-- void setResults(int, com.cisco.eManager.common.inventory2.NvPair) 

    /**
     * Method setResults
     * 
     * @param resultsArray
     */
    public void setResults(com.cisco.eManager.common.inventory2.NvPair[] resultsArray)
    {
        //-- copy array
        _resultsList.removeAllElements();
        for (int i = 0; i < resultsArray.length; i++) {
            _resultsList.addElement(resultsArray[i]);
        }
    } //-- void setResults(com.cisco.eManager.common.inventory2.NvPair) 

    /**
     * Method unmarshalIRspInvokeMethod
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalIRspInvokeMethod(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.IRspInvokeMethod) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.IRspInvokeMethod.class, reader);
    } //-- java.lang.Object unmarshalIRspInvokeMethod(java.io.Reader) 

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

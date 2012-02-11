/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cisco.eManager.common.register.registration;

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
 * Class LogDirectories.
 * 
 * @version $Revision$ $Date$
 */
public class LogDirectories implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _logDirectoryList
     */
    private java.util.Vector _logDirectoryList;


      //----------------/
     //- Constructors -/
    //----------------/

    public LogDirectories() {
        super();
        _logDirectoryList = new Vector();
    } //-- com.cisco.eManager.common.register.registration.LogDirectories()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addLogDirectory
     * 
     * @param vLogDirectory
     */
    public void addLogDirectory(java.lang.String vLogDirectory)
        throws java.lang.IndexOutOfBoundsException
    {
        _logDirectoryList.addElement(vLogDirectory);
    } //-- void addLogDirectory(java.lang.String) 

    /**
     * Method addLogDirectory
     * 
     * @param index
     * @param vLogDirectory
     */
    public void addLogDirectory(int index, java.lang.String vLogDirectory)
        throws java.lang.IndexOutOfBoundsException
    {
        _logDirectoryList.insertElementAt(vLogDirectory, index);
    } //-- void addLogDirectory(int, java.lang.String) 

    /**
     * Method enumerateLogDirectory
     */
    public java.util.Enumeration enumerateLogDirectory()
    {
        return _logDirectoryList.elements();
    } //-- java.util.Enumeration enumerateLogDirectory() 

    /**
     * Method getLogDirectory
     * 
     * @param index
     */
    public java.lang.String getLogDirectory(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _logDirectoryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_logDirectoryList.elementAt(index);
    } //-- java.lang.String getLogDirectory(int) 

    /**
     * Method getLogDirectory
     */
    public java.lang.String[] getLogDirectory()
    {
        int size = _logDirectoryList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_logDirectoryList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getLogDirectory() 

    /**
     * Method getLogDirectoryCount
     */
    public int getLogDirectoryCount()
    {
        return _logDirectoryList.size();
    } //-- int getLogDirectoryCount() 

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
     * Method removeAllLogDirectory
     */
    public void removeAllLogDirectory()
    {
        _logDirectoryList.removeAllElements();
    } //-- void removeAllLogDirectory() 

    /**
     * Method removeLogDirectory
     * 
     * @param index
     */
    public java.lang.String removeLogDirectory(int index)
    {
        java.lang.Object obj = _logDirectoryList.elementAt(index);
        _logDirectoryList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeLogDirectory(int) 

    /**
     * Method setLogDirectory
     * 
     * @param index
     * @param vLogDirectory
     */
    public void setLogDirectory(int index, java.lang.String vLogDirectory)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _logDirectoryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _logDirectoryList.setElementAt(vLogDirectory, index);
    } //-- void setLogDirectory(int, java.lang.String) 

    /**
     * Method setLogDirectory
     * 
     * @param logDirectoryArray
     */
    public void setLogDirectory(java.lang.String[] logDirectoryArray)
    {
        //-- copy array
        _logDirectoryList.removeAllElements();
        for (int i = 0; i < logDirectoryArray.length; i++) {
            _logDirectoryList.addElement(logDirectoryArray[i]);
        }
    } //-- void setLogDirectory(java.lang.String) 

    /**
     * Method unmarshalLogDirectories
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalLogDirectories(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.register.registration.LogDirectories) Unmarshaller.unmarshal(com.cisco.eManager.common.register.registration.LogDirectories.class, reader);
    } //-- java.lang.Object unmarshalLogDirectories(java.io.Reader) 

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

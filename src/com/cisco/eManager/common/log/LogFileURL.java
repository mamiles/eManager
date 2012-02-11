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
public abstract class LogFileURL implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _logFileURL;

    private java.util.ArrayList _splitLogFileURLList;


      //----------------/
     //- Constructors -/
    //----------------/

    public LogFileURL() {
        super();
        _splitLogFileURLList = new ArrayList();
    } //-- com.cisco.eManager.common.log.LogFileURL()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vSplitLogFileURL
    **/
    public void addSplitLogFileURL(java.lang.String vSplitLogFileURL)
        throws java.lang.IndexOutOfBoundsException
    {
        _splitLogFileURLList.add(vSplitLogFileURL);
    } //-- void addSplitLogFileURL(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vSplitLogFileURL
    **/
    public void addSplitLogFileURL(int index, java.lang.String vSplitLogFileURL)
        throws java.lang.IndexOutOfBoundsException
    {
        _splitLogFileURLList.add(index, vSplitLogFileURL);
    } //-- void addSplitLogFileURL(int, java.lang.String) 

    /**
    **/
    public void clearSplitLogFileURL()
    {
        _splitLogFileURLList.clear();
    } //-- void clearSplitLogFileURL() 

    /**
    **/
    public java.util.Enumeration enumerateSplitLogFileURL()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_splitLogFileURLList.iterator());
    } //-- java.util.Enumeration enumerateSplitLogFileURL() 

    /**
     * Returns the value of field 'logFileURL'.
     * 
     * @return the value of field 'logFileURL'.
    **/
    public java.lang.String getLogFileURL()
    {
        return this._logFileURL;
    } //-- java.lang.String getLogFileURL() 

    /**
     * 
     * 
     * @param index
    **/
    public java.lang.String getSplitLogFileURL(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _splitLogFileURLList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_splitLogFileURLList.get(index);
    } //-- java.lang.String getSplitLogFileURL(int) 

    /**
    **/
    public java.lang.String[] getSplitLogFileURL()
    {
        int size = _splitLogFileURLList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_splitLogFileURLList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getSplitLogFileURL() 

    /**
    **/
    public int getSplitLogFileURLCount()
    {
        return _splitLogFileURLList.size();
    } //-- int getSplitLogFileURLCount() 

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
     * @param vSplitLogFileURL
    **/
    public boolean removeSplitLogFileURL(java.lang.String vSplitLogFileURL)
    {
        boolean removed = _splitLogFileURLList.remove(vSplitLogFileURL);
        return removed;
    } //-- boolean removeSplitLogFileURL(java.lang.String) 

    /**
     * Sets the value of field 'logFileURL'.
     * 
     * @param logFileURL the value of field 'logFileURL'.
    **/
    public void setLogFileURL(java.lang.String logFileURL)
    {
        this._logFileURL = logFileURL;
    } //-- void setLogFileURL(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vSplitLogFileURL
    **/
    public void setSplitLogFileURL(int index, java.lang.String vSplitLogFileURL)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _splitLogFileURLList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _splitLogFileURLList.set(index, vSplitLogFileURL);
    } //-- void setSplitLogFileURL(int, java.lang.String) 

    /**
     * 
     * 
     * @param splitLogFileURLArray
    **/
    public void setSplitLogFileURL(java.lang.String[] splitLogFileURLArray)
    {
        //-- copy array
        _splitLogFileURLList.clear();
        for (int i = 0; i < splitLogFileURLArray.length; i++) {
            _splitLogFileURLList.add(splitLogFileURLArray[i]);
        }
    } //-- void setSplitLogFileURL(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

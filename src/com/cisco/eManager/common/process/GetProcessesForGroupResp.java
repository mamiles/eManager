/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.process;

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
public class GetProcessesForGroupResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _processNameList;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetProcessesForGroupResp() {
        super();
        _processNameList = new ArrayList();
    } //-- com.cisco.eManager.common.process.GetProcessesForGroupResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vProcessName
    **/
    public void addProcessName(java.lang.String vProcessName)
        throws java.lang.IndexOutOfBoundsException
    {
        _processNameList.add(vProcessName);
    } //-- void addProcessName(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vProcessName
    **/
    public void addProcessName(int index, java.lang.String vProcessName)
        throws java.lang.IndexOutOfBoundsException
    {
        _processNameList.add(index, vProcessName);
    } //-- void addProcessName(int, java.lang.String) 

    /**
    **/
    public void clearProcessName()
    {
        _processNameList.clear();
    } //-- void clearProcessName() 

    /**
    **/
    public java.util.Enumeration enumerateProcessName()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_processNameList.iterator());
    } //-- java.util.Enumeration enumerateProcessName() 

    /**
     * 
     * 
     * @param index
    **/
    public java.lang.String getProcessName(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _processNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_processNameList.get(index);
    } //-- java.lang.String getProcessName(int) 

    /**
    **/
    public java.lang.String[] getProcessName()
    {
        int size = _processNameList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_processNameList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getProcessName() 

    /**
    **/
    public int getProcessNameCount()
    {
        return _processNameList.size();
    } //-- int getProcessNameCount() 

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
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * 
     * 
     * @param vProcessName
    **/
    public boolean removeProcessName(java.lang.String vProcessName)
    {
        boolean removed = _processNameList.remove(vProcessName);
        return removed;
    } //-- boolean removeProcessName(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vProcessName
    **/
    public void setProcessName(int index, java.lang.String vProcessName)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _processNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _processNameList.set(index, vProcessName);
    } //-- void setProcessName(int, java.lang.String) 

    /**
     * 
     * 
     * @param processNameArray
    **/
    public void setProcessName(java.lang.String[] processNameArray)
    {
        //-- copy array
        _processNameList.clear();
        for (int i = 0; i < processNameArray.length; i++) {
            _processNameList.add(processNameArray[i]);
        }
    } //-- void setProcessName(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.process.GetProcessesForGroupResp unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.process.GetProcessesForGroupResp) Unmarshaller.unmarshal(com.cisco.eManager.common.process.GetProcessesForGroupResp.class, reader);
    } //-- com.cisco.eManager.common.process.GetProcessesForGroupResp unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

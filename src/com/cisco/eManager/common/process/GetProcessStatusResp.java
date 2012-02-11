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
public class GetProcessStatusResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _processInfoObjList;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetProcessStatusResp() {
        super();
        _processInfoObjList = new ArrayList();
    } //-- com.cisco.eManager.common.process.GetProcessStatusResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vProcessInfoObj
    **/
    public void addProcessInfoObj(ProcessInfoObj vProcessInfoObj)
        throws java.lang.IndexOutOfBoundsException
    {
        _processInfoObjList.add(vProcessInfoObj);
    } //-- void addProcessInfoObj(ProcessInfoObj) 

    /**
     * 
     * 
     * @param index
     * @param vProcessInfoObj
    **/
    public void addProcessInfoObj(int index, ProcessInfoObj vProcessInfoObj)
        throws java.lang.IndexOutOfBoundsException
    {
        _processInfoObjList.add(index, vProcessInfoObj);
    } //-- void addProcessInfoObj(int, ProcessInfoObj) 

    /**
    **/
    public void clearProcessInfoObj()
    {
        _processInfoObjList.clear();
    } //-- void clearProcessInfoObj() 

    /**
    **/
    public java.util.Enumeration enumerateProcessInfoObj()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_processInfoObjList.iterator());
    } //-- java.util.Enumeration enumerateProcessInfoObj() 

    /**
     * 
     * 
     * @param index
    **/
    public ProcessInfoObj getProcessInfoObj(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _processInfoObjList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (ProcessInfoObj) _processInfoObjList.get(index);
    } //-- ProcessInfoObj getProcessInfoObj(int) 

    /**
    **/
    public ProcessInfoObj[] getProcessInfoObj()
    {
        int size = _processInfoObjList.size();
        ProcessInfoObj[] mArray = new ProcessInfoObj[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ProcessInfoObj) _processInfoObjList.get(index);
        }
        return mArray;
    } //-- ProcessInfoObj[] getProcessInfoObj() 

    /**
    **/
    public int getProcessInfoObjCount()
    {
        return _processInfoObjList.size();
    } //-- int getProcessInfoObjCount() 

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
     * @param vProcessInfoObj
    **/
    public boolean removeProcessInfoObj(ProcessInfoObj vProcessInfoObj)
    {
        boolean removed = _processInfoObjList.remove(vProcessInfoObj);
        return removed;
    } //-- boolean removeProcessInfoObj(ProcessInfoObj) 

    /**
     * 
     * 
     * @param index
     * @param vProcessInfoObj
    **/
    public void setProcessInfoObj(int index, ProcessInfoObj vProcessInfoObj)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _processInfoObjList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _processInfoObjList.set(index, vProcessInfoObj);
    } //-- void setProcessInfoObj(int, ProcessInfoObj) 

    /**
     * 
     * 
     * @param processInfoObjArray
    **/
    public void setProcessInfoObj(ProcessInfoObj[] processInfoObjArray)
    {
        //-- copy array
        _processInfoObjList.clear();
        for (int i = 0; i < processInfoObjArray.length; i++) {
            _processInfoObjList.add(processInfoObjArray[i]);
        }
    } //-- void setProcessInfoObj(ProcessInfoObj) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.process.GetProcessStatusResp unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.process.GetProcessStatusResp) Unmarshaller.unmarshal(com.cisco.eManager.common.process.GetProcessStatusResp.class, reader);
    } //-- com.cisco.eManager.common.process.GetProcessStatusResp unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

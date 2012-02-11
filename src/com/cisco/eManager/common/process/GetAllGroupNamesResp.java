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
public class GetAllGroupNamesResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _groupNameList;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetAllGroupNamesResp() {
        super();
        _groupNameList = new ArrayList();
    } //-- com.cisco.eManager.common.process.GetAllGroupNamesResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vGroupName
    **/
    public void addGroupName(java.lang.String vGroupName)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupNameList.add(vGroupName);
    } //-- void addGroupName(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vGroupName
    **/
    public void addGroupName(int index, java.lang.String vGroupName)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupNameList.add(index, vGroupName);
    } //-- void addGroupName(int, java.lang.String) 

    /**
    **/
    public void clearGroupName()
    {
        _groupNameList.clear();
    } //-- void clearGroupName() 

    /**
    **/
    public java.util.Enumeration enumerateGroupName()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_groupNameList.iterator());
    } //-- java.util.Enumeration enumerateGroupName() 

    /**
     * 
     * 
     * @param index
    **/
    public java.lang.String getGroupName(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_groupNameList.get(index);
    } //-- java.lang.String getGroupName(int) 

    /**
    **/
    public java.lang.String[] getGroupName()
    {
        int size = _groupNameList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_groupNameList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getGroupName() 

    /**
    **/
    public int getGroupNameCount()
    {
        return _groupNameList.size();
    } //-- int getGroupNameCount() 

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
     * @param vGroupName
    **/
    public boolean removeGroupName(java.lang.String vGroupName)
    {
        boolean removed = _groupNameList.remove(vGroupName);
        return removed;
    } //-- boolean removeGroupName(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vGroupName
    **/
    public void setGroupName(int index, java.lang.String vGroupName)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _groupNameList.set(index, vGroupName);
    } //-- void setGroupName(int, java.lang.String) 

    /**
     * 
     * 
     * @param groupNameArray
    **/
    public void setGroupName(java.lang.String[] groupNameArray)
    {
        //-- copy array
        _groupNameList.clear();
        for (int i = 0; i < groupNameArray.length; i++) {
            _groupNameList.add(groupNameArray[i]);
        }
    } //-- void setGroupName(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.process.GetAllGroupNamesResp unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.process.GetAllGroupNamesResp) Unmarshaller.unmarshal(com.cisco.eManager.common.process.GetAllGroupNamesResp.class, reader);
    } //-- com.cisco.eManager.common.process.GetAllGroupNamesResp unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

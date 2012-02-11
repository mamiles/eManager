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
 * Class Applications.
 * 
 * @version $Revision$ $Date$
 */
public class Applications implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _appTypeList
     */
    private java.util.Vector _appTypeList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Applications() {
        super();
        _appTypeList = new Vector();
    } //-- com.cisco.eManager.eManager.inventory.xml.Applications()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAppType
     * 
     * @param vAppType
     */
    public void addAppType(com.cisco.eManager.eManager.inventory.xml.AppType vAppType)
        throws java.lang.IndexOutOfBoundsException
    {
        _appTypeList.addElement(vAppType);
    } //-- void addAppType(com.cisco.eManager.eManager.inventory.xml.AppType) 

    /**
     * Method addAppType
     * 
     * @param index
     * @param vAppType
     */
    public void addAppType(int index, com.cisco.eManager.eManager.inventory.xml.AppType vAppType)
        throws java.lang.IndexOutOfBoundsException
    {
        _appTypeList.insertElementAt(vAppType, index);
    } //-- void addAppType(int, com.cisco.eManager.eManager.inventory.xml.AppType) 

    /**
     * Method enumerateAppType
     */
    public java.util.Enumeration enumerateAppType()
    {
        return _appTypeList.elements();
    } //-- java.util.Enumeration enumerateAppType() 

    /**
     * Method getAppType
     * 
     * @param index
     */
    public com.cisco.eManager.eManager.inventory.xml.AppType getAppType(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appTypeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.eManager.inventory.xml.AppType) _appTypeList.elementAt(index);
    } //-- com.cisco.eManager.eManager.inventory.xml.AppType getAppType(int) 

    /**
     * Method getAppType
     */
    public com.cisco.eManager.eManager.inventory.xml.AppType[] getAppType()
    {
        int size = _appTypeList.size();
        com.cisco.eManager.eManager.inventory.xml.AppType[] mArray = new com.cisco.eManager.eManager.inventory.xml.AppType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.eManager.inventory.xml.AppType) _appTypeList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.eManager.inventory.xml.AppType[] getAppType() 

    /**
     * Method getAppTypeCount
     */
    public int getAppTypeCount()
    {
        return _appTypeList.size();
    } //-- int getAppTypeCount() 

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
     * Method removeAllAppType
     */
    public void removeAllAppType()
    {
        _appTypeList.removeAllElements();
    } //-- void removeAllAppType() 

    /**
     * Method removeAppType
     * 
     * @param index
     */
    public com.cisco.eManager.eManager.inventory.xml.AppType removeAppType(int index)
    {
        java.lang.Object obj = _appTypeList.elementAt(index);
        _appTypeList.removeElementAt(index);
        return (com.cisco.eManager.eManager.inventory.xml.AppType) obj;
    } //-- com.cisco.eManager.eManager.inventory.xml.AppType removeAppType(int) 

    /**
     * Method setAppType
     * 
     * @param index
     * @param vAppType
     */
    public void setAppType(int index, com.cisco.eManager.eManager.inventory.xml.AppType vAppType)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appTypeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _appTypeList.setElementAt(vAppType, index);
    } //-- void setAppType(int, com.cisco.eManager.eManager.inventory.xml.AppType) 

    /**
     * Method setAppType
     * 
     * @param appTypeArray
     */
    public void setAppType(com.cisco.eManager.eManager.inventory.xml.AppType[] appTypeArray)
    {
        //-- copy array
        _appTypeList.removeAllElements();
        for (int i = 0; i < appTypeArray.length; i++) {
            _appTypeList.addElement(appTypeArray[i]);
        }
    } //-- void setAppType(com.cisco.eManager.eManager.inventory.xml.AppType) 

    /**
     * Method unmarshalApplications
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalApplications(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.eManager.inventory.xml.Applications) Unmarshaller.unmarshal(com.cisco.eManager.eManager.inventory.xml.Applications.class, reader);
    } //-- java.lang.Object unmarshalApplications(java.io.Reader) 

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

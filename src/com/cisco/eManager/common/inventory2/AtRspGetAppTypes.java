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
 * Class AtRspGetAppTypes.
 * 
 * @version $Revision$ $Date$
 */
public class AtRspGetAppTypes implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _appTypesList
     */
    private java.util.Vector _appTypesList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AtRspGetAppTypes() {
        super();
        _appTypesList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.AtRspGetAppTypes()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAppTypes
     * 
     * @param vAppTypes
     */
    public void addAppTypes(com.cisco.eManager.common.inventory2.AppType vAppTypes)
        throws java.lang.IndexOutOfBoundsException
    {
        _appTypesList.addElement(vAppTypes);
    } //-- void addAppTypes(com.cisco.eManager.common.inventory2.AppType) 

    /**
     * Method addAppTypes
     * 
     * @param index
     * @param vAppTypes
     */
    public void addAppTypes(int index, com.cisco.eManager.common.inventory2.AppType vAppTypes)
        throws java.lang.IndexOutOfBoundsException
    {
        _appTypesList.insertElementAt(vAppTypes, index);
    } //-- void addAppTypes(int, com.cisco.eManager.common.inventory2.AppType) 

    /**
     * Method enumerateAppTypes
     */
    public java.util.Enumeration enumerateAppTypes()
    {
        return _appTypesList.elements();
    } //-- java.util.Enumeration enumerateAppTypes() 

    /**
     * Method getAppTypes
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.AppType getAppTypes(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appTypesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.AppType) _appTypesList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.AppType getAppTypes(int) 

    /**
     * Method getAppTypes
     */
    public com.cisco.eManager.common.inventory2.AppType[] getAppTypes()
    {
        int size = _appTypesList.size();
        com.cisco.eManager.common.inventory2.AppType[] mArray = new com.cisco.eManager.common.inventory2.AppType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.AppType) _appTypesList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.AppType[] getAppTypes() 

    /**
     * Method getAppTypesCount
     */
    public int getAppTypesCount()
    {
        return _appTypesList.size();
    } //-- int getAppTypesCount() 

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
     * Method removeAllAppTypes
     */
    public void removeAllAppTypes()
    {
        _appTypesList.removeAllElements();
    } //-- void removeAllAppTypes() 

    /**
     * Method removeAppTypes
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.AppType removeAppTypes(int index)
    {
        java.lang.Object obj = _appTypesList.elementAt(index);
        _appTypesList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.AppType) obj;
    } //-- com.cisco.eManager.common.inventory2.AppType removeAppTypes(int) 

    /**
     * Method setAppTypes
     * 
     * @param index
     * @param vAppTypes
     */
    public void setAppTypes(int index, com.cisco.eManager.common.inventory2.AppType vAppTypes)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appTypesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _appTypesList.setElementAt(vAppTypes, index);
    } //-- void setAppTypes(int, com.cisco.eManager.common.inventory2.AppType) 

    /**
     * Method setAppTypes
     * 
     * @param appTypesArray
     */
    public void setAppTypes(com.cisco.eManager.common.inventory2.AppType[] appTypesArray)
    {
        //-- copy array
        _appTypesList.removeAllElements();
        for (int i = 0; i < appTypesArray.length; i++) {
            _appTypesList.addElement(appTypesArray[i]);
        }
    } //-- void setAppTypes(com.cisco.eManager.common.inventory2.AppType) 

    /**
     * Method unmarshalAtRspGetAppTypes
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAtRspGetAppTypes(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.AtRspGetAppTypes) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.AtRspGetAppTypes.class, reader);
    } //-- java.lang.Object unmarshalAtRspGetAppTypes(java.io.Reader) 

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

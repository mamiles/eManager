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
 * Class AppType.
 * 
 * @version $Revision$ $Date$
 */
public class AppType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _id;

    /**
     * Field _localAppTypeList
     */
    private java.util.Vector _localAppTypeList;

    /**
     * Field _version
     */
    private java.lang.String _version;


      //----------------/
     //- Constructors -/
    //----------------/

    public AppType() {
        super();
        _localAppTypeList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.AppType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addLocalAppType
     * 
     * @param vLocalAppType
     */
    public void addLocalAppType(com.cisco.eManager.common.inventory2.LocalAppType vLocalAppType)
        throws java.lang.IndexOutOfBoundsException
    {
        _localAppTypeList.addElement(vLocalAppType);
    } //-- void addLocalAppType(com.cisco.eManager.common.inventory2.LocalAppType) 

    /**
     * Method addLocalAppType
     * 
     * @param index
     * @param vLocalAppType
     */
    public void addLocalAppType(int index, com.cisco.eManager.common.inventory2.LocalAppType vLocalAppType)
        throws java.lang.IndexOutOfBoundsException
    {
        _localAppTypeList.insertElementAt(vLocalAppType, index);
    } //-- void addLocalAppType(int, com.cisco.eManager.common.inventory2.LocalAppType) 

    /**
     * Method enumerateLocalAppType
     */
    public java.util.Enumeration enumerateLocalAppType()
    {
        return _localAppTypeList.elements();
    } //-- java.util.Enumeration enumerateLocalAppType() 

    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'id'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getId()
    {
        return this._id;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getId() 

    /**
     * Method getLocalAppType
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.LocalAppType getLocalAppType(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _localAppTypeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.LocalAppType) _localAppTypeList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.LocalAppType getLocalAppType(int) 

    /**
     * Method getLocalAppType
     */
    public com.cisco.eManager.common.inventory2.LocalAppType[] getLocalAppType()
    {
        int size = _localAppTypeList.size();
        com.cisco.eManager.common.inventory2.LocalAppType[] mArray = new com.cisco.eManager.common.inventory2.LocalAppType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.LocalAppType) _localAppTypeList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.LocalAppType[] getLocalAppType() 

    /**
     * Method getLocalAppTypeCount
     */
    public int getLocalAppTypeCount()
    {
        return _localAppTypeList.size();
    } //-- int getLocalAppTypeCount() 

    /**
     * Returns the value of field 'version'.
     * 
     * @return the value of field 'version'.
     */
    public java.lang.String getVersion()
    {
        return this._version;
    } //-- java.lang.String getVersion() 

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
     * Method removeAllLocalAppType
     */
    public void removeAllLocalAppType()
    {
        _localAppTypeList.removeAllElements();
    } //-- void removeAllLocalAppType() 

    /**
     * Method removeLocalAppType
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.LocalAppType removeLocalAppType(int index)
    {
        java.lang.Object obj = _localAppTypeList.elementAt(index);
        _localAppTypeList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.LocalAppType) obj;
    } //-- com.cisco.eManager.common.inventory2.LocalAppType removeLocalAppType(int) 

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(com.cisco.eManager.common.inventory2.ManagedObjectId id)
    {
        this._id = id;
    } //-- void setId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method setLocalAppType
     * 
     * @param index
     * @param vLocalAppType
     */
    public void setLocalAppType(int index, com.cisco.eManager.common.inventory2.LocalAppType vLocalAppType)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _localAppTypeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _localAppTypeList.setElementAt(vLocalAppType, index);
    } //-- void setLocalAppType(int, com.cisco.eManager.common.inventory2.LocalAppType) 

    /**
     * Method setLocalAppType
     * 
     * @param localAppTypeArray
     */
    public void setLocalAppType(com.cisco.eManager.common.inventory2.LocalAppType[] localAppTypeArray)
    {
        //-- copy array
        _localAppTypeList.removeAllElements();
        for (int i = 0; i < localAppTypeArray.length; i++) {
            _localAppTypeList.addElement(localAppTypeArray[i]);
        }
    } //-- void setLocalAppType(com.cisco.eManager.common.inventory2.LocalAppType) 

    /**
     * Sets the value of field 'version'.
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(java.lang.String version)
    {
        this._version = version;
    } //-- void setVersion(java.lang.String) 

    /**
     * Method unmarshalAppType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAppType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.AppType) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.AppType.class, reader);
    } //-- java.lang.Object unmarshalAppType(java.io.Reader) 

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

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
 * a collection of appInstances of a specific appType
 * 
 * @version $Revision$ $Date$
 */
public class AppBundle implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _appTypeId
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _appTypeId;

    /**
     * Field _appInstanceIdList
     */
    private java.util.Vector _appInstanceIdList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AppBundle() {
        super();
        _appInstanceIdList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.AppBundle()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAppInstanceId
     * 
     * @param vAppInstanceId
     */
    public void addAppInstanceId(com.cisco.eManager.common.inventory2.ManagedObjectId vAppInstanceId)
        throws java.lang.IndexOutOfBoundsException
    {
        _appInstanceIdList.addElement(vAppInstanceId);
    } //-- void addAppInstanceId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method addAppInstanceId
     * 
     * @param index
     * @param vAppInstanceId
     */
    public void addAppInstanceId(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vAppInstanceId)
        throws java.lang.IndexOutOfBoundsException
    {
        _appInstanceIdList.insertElementAt(vAppInstanceId, index);
    } //-- void addAppInstanceId(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method enumerateAppInstanceId
     */
    public java.util.Enumeration enumerateAppInstanceId()
    {
        return _appInstanceIdList.elements();
    } //-- java.util.Enumeration enumerateAppInstanceId() 

    /**
     * Method getAppInstanceId
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getAppInstanceId(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appInstanceIdList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) _appInstanceIdList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getAppInstanceId(int) 

    /**
     * Method getAppInstanceId
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId[] getAppInstanceId()
    {
        int size = _appInstanceIdList.size();
        com.cisco.eManager.common.inventory2.ManagedObjectId[] mArray = new com.cisco.eManager.common.inventory2.ManagedObjectId[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.ManagedObjectId) _appInstanceIdList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId[] getAppInstanceId() 

    /**
     * Method getAppInstanceIdCount
     */
    public int getAppInstanceIdCount()
    {
        return _appInstanceIdList.size();
    } //-- int getAppInstanceIdCount() 

    /**
     * Returns the value of field 'appTypeId'.
     * 
     * @return the value of field 'appTypeId'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getAppTypeId()
    {
        return this._appTypeId;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getAppTypeId() 

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
     * Method removeAllAppInstanceId
     */
    public void removeAllAppInstanceId()
    {
        _appInstanceIdList.removeAllElements();
    } //-- void removeAllAppInstanceId() 

    /**
     * Method removeAppInstanceId
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId removeAppInstanceId(int index)
    {
        java.lang.Object obj = _appInstanceIdList.elementAt(index);
        _appInstanceIdList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) obj;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId removeAppInstanceId(int) 

    /**
     * Method setAppInstanceId
     * 
     * @param index
     * @param vAppInstanceId
     */
    public void setAppInstanceId(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vAppInstanceId)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appInstanceIdList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _appInstanceIdList.setElementAt(vAppInstanceId, index);
    } //-- void setAppInstanceId(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method setAppInstanceId
     * 
     * @param appInstanceIdArray
     */
    public void setAppInstanceId(com.cisco.eManager.common.inventory2.ManagedObjectId[] appInstanceIdArray)
    {
        //-- copy array
        _appInstanceIdList.removeAllElements();
        for (int i = 0; i < appInstanceIdArray.length; i++) {
            _appInstanceIdList.addElement(appInstanceIdArray[i]);
        }
    } //-- void setAppInstanceId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Sets the value of field 'appTypeId'.
     * 
     * @param appTypeId the value of field 'appTypeId'.
     */
    public void setAppTypeId(com.cisco.eManager.common.inventory2.ManagedObjectId appTypeId)
    {
        this._appTypeId = appTypeId;
    } //-- void setAppTypeId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method unmarshalAppBundle
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAppBundle(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.AppBundle) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.AppBundle.class, reader);
    } //-- java.lang.Object unmarshalAppBundle(java.io.Reader) 

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

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
 * Class Solution.
 * 
 * @version $Revision$ $Date$
 */
public class Solution implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _id;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _appInstanceIdsList
     */
    private java.util.Vector _appInstanceIdsList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Solution() {
        super();
        _appInstanceIdsList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.Solution()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAppInstanceIds
     * 
     * @param vAppInstanceIds
     */
    public void addAppInstanceIds(com.cisco.eManager.common.inventory2.ManagedObjectId vAppInstanceIds)
        throws java.lang.IndexOutOfBoundsException
    {
        _appInstanceIdsList.addElement(vAppInstanceIds);
    } //-- void addAppInstanceIds(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method addAppInstanceIds
     * 
     * @param index
     * @param vAppInstanceIds
     */
    public void addAppInstanceIds(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vAppInstanceIds)
        throws java.lang.IndexOutOfBoundsException
    {
        _appInstanceIdsList.insertElementAt(vAppInstanceIds, index);
    } //-- void addAppInstanceIds(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method enumerateAppInstanceIds
     */
    public java.util.Enumeration enumerateAppInstanceIds()
    {
        return _appInstanceIdsList.elements();
    } //-- java.util.Enumeration enumerateAppInstanceIds() 

    /**
     * Method getAppInstanceIds
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getAppInstanceIds(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appInstanceIdsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) _appInstanceIdsList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getAppInstanceIds(int) 

    /**
     * Method getAppInstanceIds
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId[] getAppInstanceIds()
    {
        int size = _appInstanceIdsList.size();
        com.cisco.eManager.common.inventory2.ManagedObjectId[] mArray = new com.cisco.eManager.common.inventory2.ManagedObjectId[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.ManagedObjectId) _appInstanceIdsList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId[] getAppInstanceIds() 

    /**
     * Method getAppInstanceIdsCount
     */
    public int getAppInstanceIdsCount()
    {
        return _appInstanceIdsList.size();
    } //-- int getAppInstanceIdsCount() 

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
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

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
     * Method removeAllAppInstanceIds
     */
    public void removeAllAppInstanceIds()
    {
        _appInstanceIdsList.removeAllElements();
    } //-- void removeAllAppInstanceIds() 

    /**
     * Method removeAppInstanceIds
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId removeAppInstanceIds(int index)
    {
        java.lang.Object obj = _appInstanceIdsList.elementAt(index);
        _appInstanceIdsList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) obj;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId removeAppInstanceIds(int) 

    /**
     * Method setAppInstanceIds
     * 
     * @param index
     * @param vAppInstanceIds
     */
    public void setAppInstanceIds(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vAppInstanceIds)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appInstanceIdsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _appInstanceIdsList.setElementAt(vAppInstanceIds, index);
    } //-- void setAppInstanceIds(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method setAppInstanceIds
     * 
     * @param appInstanceIdsArray
     */
    public void setAppInstanceIds(com.cisco.eManager.common.inventory2.ManagedObjectId[] appInstanceIdsArray)
    {
        //-- copy array
        _appInstanceIdsList.removeAllElements();
        for (int i = 0; i < appInstanceIdsArray.length; i++) {
            _appInstanceIdsList.addElement(appInstanceIdsArray[i]);
        }
    } //-- void setAppInstanceIds(com.cisco.eManager.common.inventory2.ManagedObjectId) 

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
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method unmarshalSolution
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSolution(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.Solution) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.Solution.class, reader);
    } //-- java.lang.Object unmarshalSolution(java.io.Reader) 

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

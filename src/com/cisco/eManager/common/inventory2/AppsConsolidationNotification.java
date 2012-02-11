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
 * Class AppsConsolidationNotification.
 * 
 * @version $Revision$ $Date$
 */
public class AppsConsolidationNotification implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _newAppInstancesList
     */
    private java.util.Vector _newAppInstancesList;

    /**
     * Field _deletedAppInstancesList
     */
    private java.util.Vector _deletedAppInstancesList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AppsConsolidationNotification() {
        super();
        _newAppInstancesList = new Vector();
        _deletedAppInstancesList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.AppsConsolidationNotification()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDeletedAppInstances
     * 
     * @param vDeletedAppInstances
     */
    public void addDeletedAppInstances(com.cisco.eManager.common.inventory2.ManagedObjectId vDeletedAppInstances)
        throws java.lang.IndexOutOfBoundsException
    {
        _deletedAppInstancesList.addElement(vDeletedAppInstances);
    } //-- void addDeletedAppInstances(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method addDeletedAppInstances
     * 
     * @param index
     * @param vDeletedAppInstances
     */
    public void addDeletedAppInstances(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vDeletedAppInstances)
        throws java.lang.IndexOutOfBoundsException
    {
        _deletedAppInstancesList.insertElementAt(vDeletedAppInstances, index);
    } //-- void addDeletedAppInstances(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method addNewAppInstances
     * 
     * @param vNewAppInstances
     */
    public void addNewAppInstances(com.cisco.eManager.common.inventory2.ManagedObjectId vNewAppInstances)
        throws java.lang.IndexOutOfBoundsException
    {
        _newAppInstancesList.addElement(vNewAppInstances);
    } //-- void addNewAppInstances(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method addNewAppInstances
     * 
     * @param index
     * @param vNewAppInstances
     */
    public void addNewAppInstances(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vNewAppInstances)
        throws java.lang.IndexOutOfBoundsException
    {
        _newAppInstancesList.insertElementAt(vNewAppInstances, index);
    } //-- void addNewAppInstances(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method enumerateDeletedAppInstances
     */
    public java.util.Enumeration enumerateDeletedAppInstances()
    {
        return _deletedAppInstancesList.elements();
    } //-- java.util.Enumeration enumerateDeletedAppInstances() 

    /**
     * Method enumerateNewAppInstances
     */
    public java.util.Enumeration enumerateNewAppInstances()
    {
        return _newAppInstancesList.elements();
    } //-- java.util.Enumeration enumerateNewAppInstances() 

    /**
     * Method getDeletedAppInstances
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getDeletedAppInstances(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _deletedAppInstancesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) _deletedAppInstancesList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getDeletedAppInstances(int) 

    /**
     * Method getDeletedAppInstances
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId[] getDeletedAppInstances()
    {
        int size = _deletedAppInstancesList.size();
        com.cisco.eManager.common.inventory2.ManagedObjectId[] mArray = new com.cisco.eManager.common.inventory2.ManagedObjectId[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.ManagedObjectId) _deletedAppInstancesList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId[] getDeletedAppInstances() 

    /**
     * Method getDeletedAppInstancesCount
     */
    public int getDeletedAppInstancesCount()
    {
        return _deletedAppInstancesList.size();
    } //-- int getDeletedAppInstancesCount() 

    /**
     * Method getNewAppInstances
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getNewAppInstances(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _newAppInstancesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) _newAppInstancesList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getNewAppInstances(int) 

    /**
     * Method getNewAppInstances
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId[] getNewAppInstances()
    {
        int size = _newAppInstancesList.size();
        com.cisco.eManager.common.inventory2.ManagedObjectId[] mArray = new com.cisco.eManager.common.inventory2.ManagedObjectId[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.ManagedObjectId) _newAppInstancesList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId[] getNewAppInstances() 

    /**
     * Method getNewAppInstancesCount
     */
    public int getNewAppInstancesCount()
    {
        return _newAppInstancesList.size();
    } //-- int getNewAppInstancesCount() 

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
     * Method removeAllDeletedAppInstances
     */
    public void removeAllDeletedAppInstances()
    {
        _deletedAppInstancesList.removeAllElements();
    } //-- void removeAllDeletedAppInstances() 

    /**
     * Method removeAllNewAppInstances
     */
    public void removeAllNewAppInstances()
    {
        _newAppInstancesList.removeAllElements();
    } //-- void removeAllNewAppInstances() 

    /**
     * Method removeDeletedAppInstances
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId removeDeletedAppInstances(int index)
    {
        java.lang.Object obj = _deletedAppInstancesList.elementAt(index);
        _deletedAppInstancesList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) obj;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId removeDeletedAppInstances(int) 

    /**
     * Method removeNewAppInstances
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId removeNewAppInstances(int index)
    {
        java.lang.Object obj = _newAppInstancesList.elementAt(index);
        _newAppInstancesList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) obj;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId removeNewAppInstances(int) 

    /**
     * Method setDeletedAppInstances
     * 
     * @param index
     * @param vDeletedAppInstances
     */
    public void setDeletedAppInstances(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vDeletedAppInstances)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _deletedAppInstancesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _deletedAppInstancesList.setElementAt(vDeletedAppInstances, index);
    } //-- void setDeletedAppInstances(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method setDeletedAppInstances
     * 
     * @param deletedAppInstancesArray
     */
    public void setDeletedAppInstances(com.cisco.eManager.common.inventory2.ManagedObjectId[] deletedAppInstancesArray)
    {
        //-- copy array
        _deletedAppInstancesList.removeAllElements();
        for (int i = 0; i < deletedAppInstancesArray.length; i++) {
            _deletedAppInstancesList.addElement(deletedAppInstancesArray[i]);
        }
    } //-- void setDeletedAppInstances(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method setNewAppInstances
     * 
     * @param index
     * @param vNewAppInstances
     */
    public void setNewAppInstances(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vNewAppInstances)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _newAppInstancesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _newAppInstancesList.setElementAt(vNewAppInstances, index);
    } //-- void setNewAppInstances(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method setNewAppInstances
     * 
     * @param newAppInstancesArray
     */
    public void setNewAppInstances(com.cisco.eManager.common.inventory2.ManagedObjectId[] newAppInstancesArray)
    {
        //-- copy array
        _newAppInstancesList.removeAllElements();
        for (int i = 0; i < newAppInstancesArray.length; i++) {
            _newAppInstancesList.addElement(newAppInstancesArray[i]);
        }
    } //-- void setNewAppInstances(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method unmarshalAppsConsolidationNotification
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAppsConsolidationNotification(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.AppsConsolidationNotification) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.AppsConsolidationNotification.class, reader);
    } //-- java.lang.Object unmarshalAppsConsolidationNotification(java.io.Reader) 

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

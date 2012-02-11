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
 * eManager Inventory Notification
 * 
 * @version $Revision$ $Date$
 */
public class InventoryNotification implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _inventoryChangesList
     */
    private java.util.Vector _inventoryChangesList;


      //----------------/
     //- Constructors -/
    //----------------/

    public InventoryNotification() {
        super();
        _inventoryChangesList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.InventoryNotification()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addInventoryChanges
     * 
     * @param vInventoryChanges
     */
    public void addInventoryChanges(com.cisco.eManager.common.inventory2.InventoryChangeNotification vInventoryChanges)
        throws java.lang.IndexOutOfBoundsException
    {
        _inventoryChangesList.addElement(vInventoryChanges);
    } //-- void addInventoryChanges(com.cisco.eManager.common.inventory2.InventoryChangeNotification) 

    /**
     * Method addInventoryChanges
     * 
     * @param index
     * @param vInventoryChanges
     */
    public void addInventoryChanges(int index, com.cisco.eManager.common.inventory2.InventoryChangeNotification vInventoryChanges)
        throws java.lang.IndexOutOfBoundsException
    {
        _inventoryChangesList.insertElementAt(vInventoryChanges, index);
    } //-- void addInventoryChanges(int, com.cisco.eManager.common.inventory2.InventoryChangeNotification) 

    /**
     * Method enumerateInventoryChanges
     */
    public java.util.Enumeration enumerateInventoryChanges()
    {
        return _inventoryChangesList.elements();
    } //-- java.util.Enumeration enumerateInventoryChanges() 

    /**
     * Method getInventoryChanges
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.InventoryChangeNotification getInventoryChanges(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _inventoryChangesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.InventoryChangeNotification) _inventoryChangesList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.InventoryChangeNotification getInventoryChanges(int) 

    /**
     * Method getInventoryChanges
     */
    public com.cisco.eManager.common.inventory2.InventoryChangeNotification[] getInventoryChanges()
    {
        int size = _inventoryChangesList.size();
        com.cisco.eManager.common.inventory2.InventoryChangeNotification[] mArray = new com.cisco.eManager.common.inventory2.InventoryChangeNotification[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.InventoryChangeNotification) _inventoryChangesList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.InventoryChangeNotification[] getInventoryChanges() 

    /**
     * Method getInventoryChangesCount
     */
    public int getInventoryChangesCount()
    {
        return _inventoryChangesList.size();
    } //-- int getInventoryChangesCount() 

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
     * Method removeAllInventoryChanges
     */
    public void removeAllInventoryChanges()
    {
        _inventoryChangesList.removeAllElements();
    } //-- void removeAllInventoryChanges() 

    /**
     * Method removeInventoryChanges
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.InventoryChangeNotification removeInventoryChanges(int index)
    {
        java.lang.Object obj = _inventoryChangesList.elementAt(index);
        _inventoryChangesList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.InventoryChangeNotification) obj;
    } //-- com.cisco.eManager.common.inventory2.InventoryChangeNotification removeInventoryChanges(int) 

    /**
     * Method setInventoryChanges
     * 
     * @param index
     * @param vInventoryChanges
     */
    public void setInventoryChanges(int index, com.cisco.eManager.common.inventory2.InventoryChangeNotification vInventoryChanges)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _inventoryChangesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _inventoryChangesList.setElementAt(vInventoryChanges, index);
    } //-- void setInventoryChanges(int, com.cisco.eManager.common.inventory2.InventoryChangeNotification) 

    /**
     * Method setInventoryChanges
     * 
     * @param inventoryChangesArray
     */
    public void setInventoryChanges(com.cisco.eManager.common.inventory2.InventoryChangeNotification[] inventoryChangesArray)
    {
        //-- copy array
        _inventoryChangesList.removeAllElements();
        for (int i = 0; i < inventoryChangesArray.length; i++) {
            _inventoryChangesList.addElement(inventoryChangesArray[i]);
        }
    } //-- void setInventoryChanges(com.cisco.eManager.common.inventory2.InventoryChangeNotification) 

    /**
     * Method unmarshalInventoryNotification
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalInventoryNotification(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.InventoryNotification) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.InventoryNotification.class, reader);
    } //-- java.lang.Object unmarshalInventoryNotification(java.io.Reader) 

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

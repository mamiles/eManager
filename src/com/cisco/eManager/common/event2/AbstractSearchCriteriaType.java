/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.event2;

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
public abstract class AbstractSearchCriteriaType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private PostDateSearchCriteria _postDateSearchCriteria;

    private ClearDateSearchCriteria _clearDateSearchCriteria;

    private EmanagerEventIdSearchCriteria _emanagerEventIdSearchCriteria;

    private AcknowledgementSearchCriteria _acknowledgementSearchCriteria;

    private java.util.ArrayList _managedObjectsList;

    private java.util.ArrayList _severitiesList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AbstractSearchCriteriaType() {
        super();
        _managedObjectsList = new ArrayList();
        _severitiesList = new ArrayList();
    } //-- com.cisco.eManager.common.event2.AbstractSearchCriteriaType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vManagedObjects
    **/
    public void addManagedObjects(ManagedObjects vManagedObjects)
        throws java.lang.IndexOutOfBoundsException
    {
        _managedObjectsList.add(vManagedObjects);
    } //-- void addManagedObjects(ManagedObjects) 

    /**
     * 
     * 
     * @param index
     * @param vManagedObjects
    **/
    public void addManagedObjects(int index, ManagedObjects vManagedObjects)
        throws java.lang.IndexOutOfBoundsException
    {
        _managedObjectsList.add(index, vManagedObjects);
    } //-- void addManagedObjects(int, ManagedObjects) 

    /**
     * 
     * 
     * @param vSeverities
    **/
    public void addSeverities(Severities vSeverities)
        throws java.lang.IndexOutOfBoundsException
    {
        _severitiesList.add(vSeverities);
    } //-- void addSeverities(Severities) 

    /**
     * 
     * 
     * @param index
     * @param vSeverities
    **/
    public void addSeverities(int index, Severities vSeverities)
        throws java.lang.IndexOutOfBoundsException
    {
        _severitiesList.add(index, vSeverities);
    } //-- void addSeverities(int, Severities) 

    /**
    **/
    public void clearManagedObjects()
    {
        _managedObjectsList.clear();
    } //-- void clearManagedObjects() 

    /**
    **/
    public void clearSeverities()
    {
        _severitiesList.clear();
    } //-- void clearSeverities() 

    /**
    **/
    public java.util.Enumeration enumerateManagedObjects()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_managedObjectsList.iterator());
    } //-- java.util.Enumeration enumerateManagedObjects() 

    /**
    **/
    public java.util.Enumeration enumerateSeverities()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_severitiesList.iterator());
    } //-- java.util.Enumeration enumerateSeverities() 

    /**
     * Returns the value of field 'acknowledgementSearchCriteria'.
     * 
     * @return the value of field 'acknowledgementSearchCriteria'.
    **/
    public AcknowledgementSearchCriteria getAcknowledgementSearchCriteria()
    {
        return this._acknowledgementSearchCriteria;
    } //-- AcknowledgementSearchCriteria getAcknowledgementSearchCriteria() 

    /**
     * Returns the value of field 'clearDateSearchCriteria'.
     * 
     * @return the value of field 'clearDateSearchCriteria'.
    **/
    public ClearDateSearchCriteria getClearDateSearchCriteria()
    {
        return this._clearDateSearchCriteria;
    } //-- ClearDateSearchCriteria getClearDateSearchCriteria() 

    /**
     * Returns the value of field 'emanagerEventIdSearchCriteria'.
     * 
     * @return the value of field 'emanagerEventIdSearchCriteria'.
    **/
    public EmanagerEventIdSearchCriteria getEmanagerEventIdSearchCriteria()
    {
        return this._emanagerEventIdSearchCriteria;
    } //-- EmanagerEventIdSearchCriteria getEmanagerEventIdSearchCriteria() 

    /**
     * 
     * 
     * @param index
    **/
    public ManagedObjects getManagedObjects(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _managedObjectsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (ManagedObjects) _managedObjectsList.get(index);
    } //-- ManagedObjects getManagedObjects(int) 

    /**
    **/
    public ManagedObjects[] getManagedObjects()
    {
        int size = _managedObjectsList.size();
        ManagedObjects[] mArray = new ManagedObjects[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ManagedObjects) _managedObjectsList.get(index);
        }
        return mArray;
    } //-- ManagedObjects[] getManagedObjects() 

    /**
    **/
    public int getManagedObjectsCount()
    {
        return _managedObjectsList.size();
    } //-- int getManagedObjectsCount() 

    /**
     * Returns the value of field 'postDateSearchCriteria'.
     * 
     * @return the value of field 'postDateSearchCriteria'.
    **/
    public PostDateSearchCriteria getPostDateSearchCriteria()
    {
        return this._postDateSearchCriteria;
    } //-- PostDateSearchCriteria getPostDateSearchCriteria() 

    /**
     * 
     * 
     * @param index
    **/
    public Severities getSeverities(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _severitiesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Severities) _severitiesList.get(index);
    } //-- Severities getSeverities(int) 

    /**
    **/
    public Severities[] getSeverities()
    {
        int size = _severitiesList.size();
        Severities[] mArray = new Severities[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Severities) _severitiesList.get(index);
        }
        return mArray;
    } //-- Severities[] getSeverities() 

    /**
    **/
    public int getSeveritiesCount()
    {
        return _severitiesList.size();
    } //-- int getSeveritiesCount() 

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
    public abstract void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * 
     * 
     * @param handler
    **/
    public abstract void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * 
     * 
     * @param vManagedObjects
    **/
    public boolean removeManagedObjects(ManagedObjects vManagedObjects)
    {
        boolean removed = _managedObjectsList.remove(vManagedObjects);
        return removed;
    } //-- boolean removeManagedObjects(ManagedObjects) 

    /**
     * 
     * 
     * @param vSeverities
    **/
    public boolean removeSeverities(Severities vSeverities)
    {
        boolean removed = _severitiesList.remove(vSeverities);
        return removed;
    } //-- boolean removeSeverities(Severities) 

    /**
     * Sets the value of field 'acknowledgementSearchCriteria'.
     * 
     * @param acknowledgementSearchCriteria the value of field
     * 'acknowledgementSearchCriteria'.
    **/
    public void setAcknowledgementSearchCriteria(AcknowledgementSearchCriteria acknowledgementSearchCriteria)
    {
        this._acknowledgementSearchCriteria = acknowledgementSearchCriteria;
    } //-- void setAcknowledgementSearchCriteria(AcknowledgementSearchCriteria) 

    /**
     * Sets the value of field 'clearDateSearchCriteria'.
     * 
     * @param clearDateSearchCriteria the value of field
     * 'clearDateSearchCriteria'.
    **/
    public void setClearDateSearchCriteria(ClearDateSearchCriteria clearDateSearchCriteria)
    {
        this._clearDateSearchCriteria = clearDateSearchCriteria;
    } //-- void setClearDateSearchCriteria(ClearDateSearchCriteria) 

    /**
     * Sets the value of field 'emanagerEventIdSearchCriteria'.
     * 
     * @param emanagerEventIdSearchCriteria the value of field
     * 'emanagerEventIdSearchCriteria'.
    **/
    public void setEmanagerEventIdSearchCriteria(EmanagerEventIdSearchCriteria emanagerEventIdSearchCriteria)
    {
        this._emanagerEventIdSearchCriteria = emanagerEventIdSearchCriteria;
    } //-- void setEmanagerEventIdSearchCriteria(EmanagerEventIdSearchCriteria) 

    /**
     * 
     * 
     * @param index
     * @param vManagedObjects
    **/
    public void setManagedObjects(int index, ManagedObjects vManagedObjects)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _managedObjectsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _managedObjectsList.set(index, vManagedObjects);
    } //-- void setManagedObjects(int, ManagedObjects) 

    /**
     * 
     * 
     * @param managedObjectsArray
    **/
    public void setManagedObjects(ManagedObjects[] managedObjectsArray)
    {
        //-- copy array
        _managedObjectsList.clear();
        for (int i = 0; i < managedObjectsArray.length; i++) {
            _managedObjectsList.add(managedObjectsArray[i]);
        }
    } //-- void setManagedObjects(ManagedObjects) 

    /**
     * Sets the value of field 'postDateSearchCriteria'.
     * 
     * @param postDateSearchCriteria the value of field
     * 'postDateSearchCriteria'.
    **/
    public void setPostDateSearchCriteria(PostDateSearchCriteria postDateSearchCriteria)
    {
        this._postDateSearchCriteria = postDateSearchCriteria;
    } //-- void setPostDateSearchCriteria(PostDateSearchCriteria) 

    /**
     * 
     * 
     * @param index
     * @param vSeverities
    **/
    public void setSeverities(int index, Severities vSeverities)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _severitiesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _severitiesList.set(index, vSeverities);
    } //-- void setSeverities(int, Severities) 

    /**
     * 
     * 
     * @param severitiesArray
    **/
    public void setSeverities(Severities[] severitiesArray)
    {
        //-- copy array
        _severitiesList.clear();
        for (int i = 0; i < severitiesArray.length; i++) {
            _severitiesList.add(severitiesArray[i]);
        }
    } //-- void setSeverities(Severities) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

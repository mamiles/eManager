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
 * Class AppInstance.
 * 
 * @version $Revision$ $Date$
 */
public class AppInstance implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _id;

    /**
     * Field _appTypeId
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _appTypeId;

    /**
     * Field _hostId
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _hostId;

    /**
     * Field _instrumentationIdsList
     */
    private java.util.Vector _instrumentationIdsList;

    /**
     * Field _logfileDirectoriesList
     */
    private java.util.Vector _logfileDirectoriesList;

    /**
     * Field _logfilePassword
     */
    private java.lang.String _logfilePassword;

    /**
     * Field _logfileTransport
     */
    private com.cisco.eManager.common.inventory2.Transport _logfileTransport;

    /**
     * Field _logfileUsername
     */
    private java.lang.String _logfileUsername;

    /**
     * Field _managementMode
     */
    private com.cisco.eManager.common.inventory2.ManagementMode _managementMode;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _propertyFile
     */
    private java.lang.String _propertyFile;

    /**
     * Field _unixPrompt
     */
    private java.lang.String _unixPrompt;


      //----------------/
     //- Constructors -/
    //----------------/

    public AppInstance() {
        super();
        _instrumentationIdsList = new Vector();
        _logfileDirectoriesList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.AppInstance()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addInstrumentationIds
     * 
     * @param vInstrumentationIds
     */
    public void addInstrumentationIds(com.cisco.eManager.common.inventory2.ManagedObjectId vInstrumentationIds)
        throws java.lang.IndexOutOfBoundsException
    {
        _instrumentationIdsList.addElement(vInstrumentationIds);
    } //-- void addInstrumentationIds(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method addInstrumentationIds
     * 
     * @param index
     * @param vInstrumentationIds
     */
    public void addInstrumentationIds(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vInstrumentationIds)
        throws java.lang.IndexOutOfBoundsException
    {
        _instrumentationIdsList.insertElementAt(vInstrumentationIds, index);
    } //-- void addInstrumentationIds(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method addLogfileDirectories
     * 
     * @param vLogfileDirectories
     */
    public void addLogfileDirectories(java.lang.String vLogfileDirectories)
        throws java.lang.IndexOutOfBoundsException
    {
        _logfileDirectoriesList.addElement(vLogfileDirectories);
    } //-- void addLogfileDirectories(java.lang.String) 

    /**
     * Method addLogfileDirectories
     * 
     * @param index
     * @param vLogfileDirectories
     */
    public void addLogfileDirectories(int index, java.lang.String vLogfileDirectories)
        throws java.lang.IndexOutOfBoundsException
    {
        _logfileDirectoriesList.insertElementAt(vLogfileDirectories, index);
    } //-- void addLogfileDirectories(int, java.lang.String) 

    /**
     * Method enumerateInstrumentationIds
     */
    public java.util.Enumeration enumerateInstrumentationIds()
    {
        return _instrumentationIdsList.elements();
    } //-- java.util.Enumeration enumerateInstrumentationIds() 

    /**
     * Method enumerateLogfileDirectories
     */
    public java.util.Enumeration enumerateLogfileDirectories()
    {
        return _logfileDirectoriesList.elements();
    } //-- java.util.Enumeration enumerateLogfileDirectories() 

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
     * Returns the value of field 'hostId'.
     * 
     * @return the value of field 'hostId'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getHostId()
    {
        return this._hostId;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getHostId() 

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
     * Method getInstrumentationIds
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getInstrumentationIds(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instrumentationIdsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) _instrumentationIdsList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getInstrumentationIds(int) 

    /**
     * Method getInstrumentationIds
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId[] getInstrumentationIds()
    {
        int size = _instrumentationIdsList.size();
        com.cisco.eManager.common.inventory2.ManagedObjectId[] mArray = new com.cisco.eManager.common.inventory2.ManagedObjectId[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.ManagedObjectId) _instrumentationIdsList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId[] getInstrumentationIds() 

    /**
     * Method getInstrumentationIdsCount
     */
    public int getInstrumentationIdsCount()
    {
        return _instrumentationIdsList.size();
    } //-- int getInstrumentationIdsCount() 

    /**
     * Method getLogfileDirectories
     * 
     * @param index
     */
    public java.lang.String getLogfileDirectories(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _logfileDirectoriesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_logfileDirectoriesList.elementAt(index);
    } //-- java.lang.String getLogfileDirectories(int) 

    /**
     * Method getLogfileDirectories
     */
    public java.lang.String[] getLogfileDirectories()
    {
        int size = _logfileDirectoriesList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_logfileDirectoriesList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getLogfileDirectories() 

    /**
     * Method getLogfileDirectoriesCount
     */
    public int getLogfileDirectoriesCount()
    {
        return _logfileDirectoriesList.size();
    } //-- int getLogfileDirectoriesCount() 

    /**
     * Returns the value of field 'logfilePassword'.
     * 
     * @return the value of field 'logfilePassword'.
     */
    public java.lang.String getLogfilePassword()
    {
        return this._logfilePassword;
    } //-- java.lang.String getLogfilePassword() 

    /**
     * Returns the value of field 'logfileTransport'.
     * 
     * @return the value of field 'logfileTransport'.
     */
    public com.cisco.eManager.common.inventory2.Transport getLogfileTransport()
    {
        return this._logfileTransport;
    } //-- com.cisco.eManager.common.inventory2.Transport getLogfileTransport() 

    /**
     * Returns the value of field 'logfileUsername'.
     * 
     * @return the value of field 'logfileUsername'.
     */
    public java.lang.String getLogfileUsername()
    {
        return this._logfileUsername;
    } //-- java.lang.String getLogfileUsername() 

    /**
     * Returns the value of field 'managementMode'.
     * 
     * @return the value of field 'managementMode'.
     */
    public com.cisco.eManager.common.inventory2.ManagementMode getManagementMode()
    {
        return this._managementMode;
    } //-- com.cisco.eManager.common.inventory2.ManagementMode getManagementMode() 

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
     * Returns the value of field 'propertyFile'.
     * 
     * @return the value of field 'propertyFile'.
     */
    public java.lang.String getPropertyFile()
    {
        return this._propertyFile;
    } //-- java.lang.String getPropertyFile() 

    /**
     * Returns the value of field 'unixPrompt'.
     * 
     * @return the value of field 'unixPrompt'.
     */
    public java.lang.String getUnixPrompt()
    {
        return this._unixPrompt;
    } //-- java.lang.String getUnixPrompt() 

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
     * Method removeAllInstrumentationIds
     */
    public void removeAllInstrumentationIds()
    {
        _instrumentationIdsList.removeAllElements();
    } //-- void removeAllInstrumentationIds() 

    /**
     * Method removeAllLogfileDirectories
     */
    public void removeAllLogfileDirectories()
    {
        _logfileDirectoriesList.removeAllElements();
    } //-- void removeAllLogfileDirectories() 

    /**
     * Method removeInstrumentationIds
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId removeInstrumentationIds(int index)
    {
        java.lang.Object obj = _instrumentationIdsList.elementAt(index);
        _instrumentationIdsList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) obj;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId removeInstrumentationIds(int) 

    /**
     * Method removeLogfileDirectories
     * 
     * @param index
     */
    public java.lang.String removeLogfileDirectories(int index)
    {
        java.lang.Object obj = _logfileDirectoriesList.elementAt(index);
        _logfileDirectoriesList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeLogfileDirectories(int) 

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
     * Sets the value of field 'hostId'.
     * 
     * @param hostId the value of field 'hostId'.
     */
    public void setHostId(com.cisco.eManager.common.inventory2.ManagedObjectId hostId)
    {
        this._hostId = hostId;
    } //-- void setHostId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

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
     * Method setInstrumentationIds
     * 
     * @param index
     * @param vInstrumentationIds
     */
    public void setInstrumentationIds(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vInstrumentationIds)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instrumentationIdsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _instrumentationIdsList.setElementAt(vInstrumentationIds, index);
    } //-- void setInstrumentationIds(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method setInstrumentationIds
     * 
     * @param instrumentationIdsArray
     */
    public void setInstrumentationIds(com.cisco.eManager.common.inventory2.ManagedObjectId[] instrumentationIdsArray)
    {
        //-- copy array
        _instrumentationIdsList.removeAllElements();
        for (int i = 0; i < instrumentationIdsArray.length; i++) {
            _instrumentationIdsList.addElement(instrumentationIdsArray[i]);
        }
    } //-- void setInstrumentationIds(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method setLogfileDirectories
     * 
     * @param index
     * @param vLogfileDirectories
     */
    public void setLogfileDirectories(int index, java.lang.String vLogfileDirectories)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _logfileDirectoriesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _logfileDirectoriesList.setElementAt(vLogfileDirectories, index);
    } //-- void setLogfileDirectories(int, java.lang.String) 

    /**
     * Method setLogfileDirectories
     * 
     * @param logfileDirectoriesArray
     */
    public void setLogfileDirectories(java.lang.String[] logfileDirectoriesArray)
    {
        //-- copy array
        _logfileDirectoriesList.removeAllElements();
        for (int i = 0; i < logfileDirectoriesArray.length; i++) {
            _logfileDirectoriesList.addElement(logfileDirectoriesArray[i]);
        }
    } //-- void setLogfileDirectories(java.lang.String) 

    /**
     * Sets the value of field 'logfilePassword'.
     * 
     * @param logfilePassword the value of field 'logfilePassword'.
     */
    public void setLogfilePassword(java.lang.String logfilePassword)
    {
        this._logfilePassword = logfilePassword;
    } //-- void setLogfilePassword(java.lang.String) 

    /**
     * Sets the value of field 'logfileTransport'.
     * 
     * @param logfileTransport the value of field 'logfileTransport'
     */
    public void setLogfileTransport(com.cisco.eManager.common.inventory2.Transport logfileTransport)
    {
        this._logfileTransport = logfileTransport;
    } //-- void setLogfileTransport(com.cisco.eManager.common.inventory2.Transport) 

    /**
     * Sets the value of field 'logfileUsername'.
     * 
     * @param logfileUsername the value of field 'logfileUsername'.
     */
    public void setLogfileUsername(java.lang.String logfileUsername)
    {
        this._logfileUsername = logfileUsername;
    } //-- void setLogfileUsername(java.lang.String) 

    /**
     * Sets the value of field 'managementMode'.
     * 
     * @param managementMode the value of field 'managementMode'.
     */
    public void setManagementMode(com.cisco.eManager.common.inventory2.ManagementMode managementMode)
    {
        this._managementMode = managementMode;
    } //-- void setManagementMode(com.cisco.eManager.common.inventory2.ManagementMode) 

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
     * Sets the value of field 'propertyFile'.
     * 
     * @param propertyFile the value of field 'propertyFile'.
     */
    public void setPropertyFile(java.lang.String propertyFile)
    {
        this._propertyFile = propertyFile;
    } //-- void setPropertyFile(java.lang.String) 

    /**
     * Sets the value of field 'unixPrompt'.
     * 
     * @param unixPrompt the value of field 'unixPrompt'.
     */
    public void setUnixPrompt(java.lang.String unixPrompt)
    {
        this._unixPrompt = unixPrompt;
    } //-- void setUnixPrompt(java.lang.String) 

    /**
     * Method unmarshalAppInstance
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAppInstance(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.AppInstance) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.AppInstance.class, reader);
    } //-- java.lang.Object unmarshalAppInstance(java.io.Reader) 

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

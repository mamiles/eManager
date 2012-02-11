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
 * Class Host.
 * 
 * @version $Revision$ $Date$
 */
public class Host implements java.io.Serializable {


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
     * Field _appBundleList
     */
    private java.util.Vector _appBundleList;

    /**
     * Field _dnsName
     */
    private java.lang.String _dnsName;

    /**
     * Field _domain
     */
    private java.lang.String _domain;

    /**
     * Field _ipAddress
     */
    private java.lang.String _ipAddress;

    /**
     * Field _state
     */
    private com.cisco.eManager.common.inventory2.HostState _state;


      //----------------/
     //- Constructors -/
    //----------------/

    public Host() {
        super();
        _appBundleList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.Host()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAppBundle
     * 
     * @param vAppBundle
     */
    public void addAppBundle(com.cisco.eManager.common.inventory2.AppBundle vAppBundle)
        throws java.lang.IndexOutOfBoundsException
    {
        _appBundleList.addElement(vAppBundle);
    } //-- void addAppBundle(com.cisco.eManager.common.inventory2.AppBundle) 

    /**
     * Method addAppBundle
     * 
     * @param index
     * @param vAppBundle
     */
    public void addAppBundle(int index, com.cisco.eManager.common.inventory2.AppBundle vAppBundle)
        throws java.lang.IndexOutOfBoundsException
    {
        _appBundleList.insertElementAt(vAppBundle, index);
    } //-- void addAppBundle(int, com.cisco.eManager.common.inventory2.AppBundle) 

    /**
     * Method enumerateAppBundle
     */
    public java.util.Enumeration enumerateAppBundle()
    {
        return _appBundleList.elements();
    } //-- java.util.Enumeration enumerateAppBundle() 

    /**
     * Method getAppBundle
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.AppBundle getAppBundle(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appBundleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.AppBundle) _appBundleList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.AppBundle getAppBundle(int) 

    /**
     * Method getAppBundle
     */
    public com.cisco.eManager.common.inventory2.AppBundle[] getAppBundle()
    {
        int size = _appBundleList.size();
        com.cisco.eManager.common.inventory2.AppBundle[] mArray = new com.cisco.eManager.common.inventory2.AppBundle[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.AppBundle) _appBundleList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.AppBundle[] getAppBundle() 

    /**
     * Method getAppBundleCount
     */
    public int getAppBundleCount()
    {
        return _appBundleList.size();
    } //-- int getAppBundleCount() 

    /**
     * Returns the value of field 'dnsName'.
     * 
     * @return the value of field 'dnsName'.
     */
    public java.lang.String getDnsName()
    {
        return this._dnsName;
    } //-- java.lang.String getDnsName() 

    /**
     * Returns the value of field 'domain'.
     * 
     * @return the value of field 'domain'.
     */
    public java.lang.String getDomain()
    {
        return this._domain;
    } //-- java.lang.String getDomain() 

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
     * Returns the value of field 'ipAddress'.
     * 
     * @return the value of field 'ipAddress'.
     */
    public java.lang.String getIpAddress()
    {
        return this._ipAddress;
    } //-- java.lang.String getIpAddress() 

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
     * Returns the value of field 'state'.
     * 
     * @return the value of field 'state'.
     */
    public com.cisco.eManager.common.inventory2.HostState getState()
    {
        return this._state;
    } //-- com.cisco.eManager.common.inventory2.HostState getState() 

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
     * Method removeAllAppBundle
     */
    public void removeAllAppBundle()
    {
        _appBundleList.removeAllElements();
    } //-- void removeAllAppBundle() 

    /**
     * Method removeAppBundle
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.AppBundle removeAppBundle(int index)
    {
        java.lang.Object obj = _appBundleList.elementAt(index);
        _appBundleList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.AppBundle) obj;
    } //-- com.cisco.eManager.common.inventory2.AppBundle removeAppBundle(int) 

    /**
     * Method setAppBundle
     * 
     * @param index
     * @param vAppBundle
     */
    public void setAppBundle(int index, com.cisco.eManager.common.inventory2.AppBundle vAppBundle)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appBundleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _appBundleList.setElementAt(vAppBundle, index);
    } //-- void setAppBundle(int, com.cisco.eManager.common.inventory2.AppBundle) 

    /**
     * Method setAppBundle
     * 
     * @param appBundleArray
     */
    public void setAppBundle(com.cisco.eManager.common.inventory2.AppBundle[] appBundleArray)
    {
        //-- copy array
        _appBundleList.removeAllElements();
        for (int i = 0; i < appBundleArray.length; i++) {
            _appBundleList.addElement(appBundleArray[i]);
        }
    } //-- void setAppBundle(com.cisco.eManager.common.inventory2.AppBundle) 

    /**
     * Sets the value of field 'dnsName'.
     * 
     * @param dnsName the value of field 'dnsName'.
     */
    public void setDnsName(java.lang.String dnsName)
    {
        this._dnsName = dnsName;
    } //-- void setDnsName(java.lang.String) 

    /**
     * Sets the value of field 'domain'.
     * 
     * @param domain the value of field 'domain'.
     */
    public void setDomain(java.lang.String domain)
    {
        this._domain = domain;
    } //-- void setDomain(java.lang.String) 

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
     * Sets the value of field 'ipAddress'.
     * 
     * @param ipAddress the value of field 'ipAddress'.
     */
    public void setIpAddress(java.lang.String ipAddress)
    {
        this._ipAddress = ipAddress;
    } //-- void setIpAddress(java.lang.String) 

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
     * Sets the value of field 'state'.
     * 
     * @param state the value of field 'state'.
     */
    public void setState(com.cisco.eManager.common.inventory2.HostState state)
    {
        this._state = state;
    } //-- void setState(com.cisco.eManager.common.inventory2.HostState) 

    /**
     * Method unmarshalHost
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalHost(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.Host) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.Host.class, reader);
    } //-- java.lang.Object unmarshalHost(java.io.Reader) 

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

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
 * an appType on a specific host
 * 
 * @version $Revision$ $Date$
 */
public class LocalAppType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _hostId
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _hostId;

    /**
     * Field _mgmtPolicyIdList
     */
    private java.util.Vector _mgmtPolicyIdList;

    /**
     * Field _appInstanceIdList
     */
    private java.util.Vector _appInstanceIdList;


      //----------------/
     //- Constructors -/
    //----------------/

    public LocalAppType() {
        super();
        _mgmtPolicyIdList = new Vector();
        _appInstanceIdList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.LocalAppType()


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
     * Method addMgmtPolicyId
     * 
     * @param vMgmtPolicyId
     */
    public void addMgmtPolicyId(com.cisco.eManager.common.inventory2.ManagedObjectId vMgmtPolicyId)
        throws java.lang.IndexOutOfBoundsException
    {
        _mgmtPolicyIdList.addElement(vMgmtPolicyId);
    } //-- void addMgmtPolicyId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method addMgmtPolicyId
     * 
     * @param index
     * @param vMgmtPolicyId
     */
    public void addMgmtPolicyId(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vMgmtPolicyId)
        throws java.lang.IndexOutOfBoundsException
    {
        _mgmtPolicyIdList.insertElementAt(vMgmtPolicyId, index);
    } //-- void addMgmtPolicyId(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method enumerateAppInstanceId
     */
    public java.util.Enumeration enumerateAppInstanceId()
    {
        return _appInstanceIdList.elements();
    } //-- java.util.Enumeration enumerateAppInstanceId() 

    /**
     * Method enumerateMgmtPolicyId
     */
    public java.util.Enumeration enumerateMgmtPolicyId()
    {
        return _mgmtPolicyIdList.elements();
    } //-- java.util.Enumeration enumerateMgmtPolicyId() 

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
     * Returns the value of field 'hostId'.
     * 
     * @return the value of field 'hostId'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getHostId()
    {
        return this._hostId;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getHostId() 

    /**
     * Method getMgmtPolicyId
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getMgmtPolicyId(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mgmtPolicyIdList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) _mgmtPolicyIdList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getMgmtPolicyId(int) 

    /**
     * Method getMgmtPolicyId
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId[] getMgmtPolicyId()
    {
        int size = _mgmtPolicyIdList.size();
        com.cisco.eManager.common.inventory2.ManagedObjectId[] mArray = new com.cisco.eManager.common.inventory2.ManagedObjectId[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.ManagedObjectId) _mgmtPolicyIdList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId[] getMgmtPolicyId() 

    /**
     * Method getMgmtPolicyIdCount
     */
    public int getMgmtPolicyIdCount()
    {
        return _mgmtPolicyIdList.size();
    } //-- int getMgmtPolicyIdCount() 

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
     * Method removeAllMgmtPolicyId
     */
    public void removeAllMgmtPolicyId()
    {
        _mgmtPolicyIdList.removeAllElements();
    } //-- void removeAllMgmtPolicyId() 

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
     * Method removeMgmtPolicyId
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId removeMgmtPolicyId(int index)
    {
        java.lang.Object obj = _mgmtPolicyIdList.elementAt(index);
        _mgmtPolicyIdList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) obj;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId removeMgmtPolicyId(int) 

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
     * Sets the value of field 'hostId'.
     * 
     * @param hostId the value of field 'hostId'.
     */
    public void setHostId(com.cisco.eManager.common.inventory2.ManagedObjectId hostId)
    {
        this._hostId = hostId;
    } //-- void setHostId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method setMgmtPolicyId
     * 
     * @param index
     * @param vMgmtPolicyId
     */
    public void setMgmtPolicyId(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vMgmtPolicyId)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mgmtPolicyIdList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _mgmtPolicyIdList.setElementAt(vMgmtPolicyId, index);
    } //-- void setMgmtPolicyId(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method setMgmtPolicyId
     * 
     * @param mgmtPolicyIdArray
     */
    public void setMgmtPolicyId(com.cisco.eManager.common.inventory2.ManagedObjectId[] mgmtPolicyIdArray)
    {
        //-- copy array
        _mgmtPolicyIdList.removeAllElements();
        for (int i = 0; i < mgmtPolicyIdArray.length; i++) {
            _mgmtPolicyIdList.addElement(mgmtPolicyIdArray[i]);
        }
    } //-- void setMgmtPolicyId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method unmarshalLocalAppType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalLocalAppType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.LocalAppType) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.LocalAppType.class, reader);
    } //-- java.lang.Object unmarshalLocalAppType(java.io.Reader) 

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

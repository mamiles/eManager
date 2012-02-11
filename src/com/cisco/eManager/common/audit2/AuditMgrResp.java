/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.audit2;

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
public class AuditMgrResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _auditLogEntriesList;

    private java.lang.String _noDataResponse;


      //----------------/
     //- Constructors -/
    //----------------/

    public AuditMgrResp() {
        super();
        _auditLogEntriesList = new ArrayList();
    } //-- com.cisco.eManager.common.audit2.AuditMgrResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vAuditLogEntries
    **/
    public void addAuditLogEntries(AuditLogEntries vAuditLogEntries)
        throws java.lang.IndexOutOfBoundsException
    {
        _auditLogEntriesList.add(vAuditLogEntries);
    } //-- void addAuditLogEntries(AuditLogEntries) 

    /**
     * 
     * 
     * @param index
     * @param vAuditLogEntries
    **/
    public void addAuditLogEntries(int index, AuditLogEntries vAuditLogEntries)
        throws java.lang.IndexOutOfBoundsException
    {
        _auditLogEntriesList.add(index, vAuditLogEntries);
    } //-- void addAuditLogEntries(int, AuditLogEntries) 

    /**
    **/
    public void clearAuditLogEntries()
    {
        _auditLogEntriesList.clear();
    } //-- void clearAuditLogEntries() 

    /**
    **/
    public java.util.Enumeration enumerateAuditLogEntries()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_auditLogEntriesList.iterator());
    } //-- java.util.Enumeration enumerateAuditLogEntries() 

    /**
     * 
     * 
     * @param index
    **/
    public AuditLogEntries getAuditLogEntries(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _auditLogEntriesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (AuditLogEntries) _auditLogEntriesList.get(index);
    } //-- AuditLogEntries getAuditLogEntries(int) 

    /**
    **/
    public AuditLogEntries[] getAuditLogEntries()
    {
        int size = _auditLogEntriesList.size();
        AuditLogEntries[] mArray = new AuditLogEntries[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (AuditLogEntries) _auditLogEntriesList.get(index);
        }
        return mArray;
    } //-- AuditLogEntries[] getAuditLogEntries() 

    /**
    **/
    public int getAuditLogEntriesCount()
    {
        return _auditLogEntriesList.size();
    } //-- int getAuditLogEntriesCount() 

    /**
     * Returns the value of field 'noDataResponse'.
     * 
     * @return the value of field 'noDataResponse'.
    **/
    public java.lang.String getNoDataResponse()
    {
        return this._noDataResponse;
    } //-- java.lang.String getNoDataResponse() 

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
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * 
     * 
     * @param vAuditLogEntries
    **/
    public boolean removeAuditLogEntries(AuditLogEntries vAuditLogEntries)
    {
        boolean removed = _auditLogEntriesList.remove(vAuditLogEntries);
        return removed;
    } //-- boolean removeAuditLogEntries(AuditLogEntries) 

    /**
     * 
     * 
     * @param index
     * @param vAuditLogEntries
    **/
    public void setAuditLogEntries(int index, AuditLogEntries vAuditLogEntries)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _auditLogEntriesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _auditLogEntriesList.set(index, vAuditLogEntries);
    } //-- void setAuditLogEntries(int, AuditLogEntries) 

    /**
     * 
     * 
     * @param auditLogEntriesArray
    **/
    public void setAuditLogEntries(AuditLogEntries[] auditLogEntriesArray)
    {
        //-- copy array
        _auditLogEntriesList.clear();
        for (int i = 0; i < auditLogEntriesArray.length; i++) {
            _auditLogEntriesList.add(auditLogEntriesArray[i]);
        }
    } //-- void setAuditLogEntries(AuditLogEntries) 

    /**
     * Sets the value of field 'noDataResponse'.
     * 
     * @param noDataResponse the value of field 'noDataResponse'.
    **/
    public void setNoDataResponse(java.lang.String noDataResponse)
    {
        this._noDataResponse = noDataResponse;
    } //-- void setNoDataResponse(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.audit2.AuditMgrResp unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.audit2.AuditMgrResp) Unmarshaller.unmarshal(com.cisco.eManager.common.audit2.AuditMgrResp.class, reader);
    } //-- com.cisco.eManager.common.audit2.AuditMgrResp unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

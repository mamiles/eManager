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
 * Event Mgr response messages
 * 
 * @version $Revision$ $Date$
**/
public class EventMgrResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private GetEventDetailsResp _getEventDetailsResp;

    private java.util.ArrayList _retrieveEventsRespList;

    private java.util.ArrayList _snmpClientsList;

    private java.lang.String _noDataResponse;


      //----------------/
     //- Constructors -/
    //----------------/

    public EventMgrResp() {
        super();
        _retrieveEventsRespList = new ArrayList();
        _snmpClientsList = new ArrayList();
    } //-- com.cisco.eManager.common.event2.EventMgrResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vRetrieveEventsResp
    **/
    public void addRetrieveEventsResp(RetrieveEventsResp vRetrieveEventsResp)
        throws java.lang.IndexOutOfBoundsException
    {
        _retrieveEventsRespList.add(vRetrieveEventsResp);
    } //-- void addRetrieveEventsResp(RetrieveEventsResp) 

    /**
     * 
     * 
     * @param index
     * @param vRetrieveEventsResp
    **/
    public void addRetrieveEventsResp(int index, RetrieveEventsResp vRetrieveEventsResp)
        throws java.lang.IndexOutOfBoundsException
    {
        _retrieveEventsRespList.add(index, vRetrieveEventsResp);
    } //-- void addRetrieveEventsResp(int, RetrieveEventsResp) 

    /**
     * 
     * 
     * @param vSnmpClients
    **/
    public void addSnmpClients(SnmpClients vSnmpClients)
        throws java.lang.IndexOutOfBoundsException
    {
        _snmpClientsList.add(vSnmpClients);
    } //-- void addSnmpClients(SnmpClients) 

    /**
     * 
     * 
     * @param index
     * @param vSnmpClients
    **/
    public void addSnmpClients(int index, SnmpClients vSnmpClients)
        throws java.lang.IndexOutOfBoundsException
    {
        _snmpClientsList.add(index, vSnmpClients);
    } //-- void addSnmpClients(int, SnmpClients) 

    /**
    **/
    public void clearRetrieveEventsResp()
    {
        _retrieveEventsRespList.clear();
    } //-- void clearRetrieveEventsResp() 

    /**
    **/
    public void clearSnmpClients()
    {
        _snmpClientsList.clear();
    } //-- void clearSnmpClients() 

    /**
    **/
    public java.util.Enumeration enumerateRetrieveEventsResp()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_retrieveEventsRespList.iterator());
    } //-- java.util.Enumeration enumerateRetrieveEventsResp() 

    /**
    **/
    public java.util.Enumeration enumerateSnmpClients()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_snmpClientsList.iterator());
    } //-- java.util.Enumeration enumerateSnmpClients() 

    /**
     * Returns the value of field 'getEventDetailsResp'.
     * 
     * @return the value of field 'getEventDetailsResp'.
    **/
    public GetEventDetailsResp getGetEventDetailsResp()
    {
        return this._getEventDetailsResp;
    } //-- GetEventDetailsResp getGetEventDetailsResp() 

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
     * 
     * 
     * @param index
    **/
    public RetrieveEventsResp getRetrieveEventsResp(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _retrieveEventsRespList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (RetrieveEventsResp) _retrieveEventsRespList.get(index);
    } //-- RetrieveEventsResp getRetrieveEventsResp(int) 

    /**
    **/
    public RetrieveEventsResp[] getRetrieveEventsResp()
    {
        int size = _retrieveEventsRespList.size();
        RetrieveEventsResp[] mArray = new RetrieveEventsResp[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (RetrieveEventsResp) _retrieveEventsRespList.get(index);
        }
        return mArray;
    } //-- RetrieveEventsResp[] getRetrieveEventsResp() 

    /**
    **/
    public int getRetrieveEventsRespCount()
    {
        return _retrieveEventsRespList.size();
    } //-- int getRetrieveEventsRespCount() 

    /**
     * 
     * 
     * @param index
    **/
    public SnmpClients getSnmpClients(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _snmpClientsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (SnmpClients) _snmpClientsList.get(index);
    } //-- SnmpClients getSnmpClients(int) 

    /**
    **/
    public SnmpClients[] getSnmpClients()
    {
        int size = _snmpClientsList.size();
        SnmpClients[] mArray = new SnmpClients[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (SnmpClients) _snmpClientsList.get(index);
        }
        return mArray;
    } //-- SnmpClients[] getSnmpClients() 

    /**
    **/
    public int getSnmpClientsCount()
    {
        return _snmpClientsList.size();
    } //-- int getSnmpClientsCount() 

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
     * @param vRetrieveEventsResp
    **/
    public boolean removeRetrieveEventsResp(RetrieveEventsResp vRetrieveEventsResp)
    {
        boolean removed = _retrieveEventsRespList.remove(vRetrieveEventsResp);
        return removed;
    } //-- boolean removeRetrieveEventsResp(RetrieveEventsResp) 

    /**
     * 
     * 
     * @param vSnmpClients
    **/
    public boolean removeSnmpClients(SnmpClients vSnmpClients)
    {
        boolean removed = _snmpClientsList.remove(vSnmpClients);
        return removed;
    } //-- boolean removeSnmpClients(SnmpClients) 

    /**
     * Sets the value of field 'getEventDetailsResp'.
     * 
     * @param getEventDetailsResp the value of field
     * 'getEventDetailsResp'.
    **/
    public void setGetEventDetailsResp(GetEventDetailsResp getEventDetailsResp)
    {
        this._getEventDetailsResp = getEventDetailsResp;
    } //-- void setGetEventDetailsResp(GetEventDetailsResp) 

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
     * @param index
     * @param vRetrieveEventsResp
    **/
    public void setRetrieveEventsResp(int index, RetrieveEventsResp vRetrieveEventsResp)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _retrieveEventsRespList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _retrieveEventsRespList.set(index, vRetrieveEventsResp);
    } //-- void setRetrieveEventsResp(int, RetrieveEventsResp) 

    /**
     * 
     * 
     * @param retrieveEventsRespArray
    **/
    public void setRetrieveEventsResp(RetrieveEventsResp[] retrieveEventsRespArray)
    {
        //-- copy array
        _retrieveEventsRespList.clear();
        for (int i = 0; i < retrieveEventsRespArray.length; i++) {
            _retrieveEventsRespList.add(retrieveEventsRespArray[i]);
        }
    } //-- void setRetrieveEventsResp(RetrieveEventsResp) 

    /**
     * 
     * 
     * @param index
     * @param vSnmpClients
    **/
    public void setSnmpClients(int index, SnmpClients vSnmpClients)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _snmpClientsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _snmpClientsList.set(index, vSnmpClients);
    } //-- void setSnmpClients(int, SnmpClients) 

    /**
     * 
     * 
     * @param snmpClientsArray
    **/
    public void setSnmpClients(SnmpClients[] snmpClientsArray)
    {
        //-- copy array
        _snmpClientsList.clear();
        for (int i = 0; i < snmpClientsArray.length; i++) {
            _snmpClientsList.add(snmpClientsArray[i]);
        }
    } //-- void setSnmpClients(SnmpClients) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.event2.EventMgrResp unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.event2.EventMgrResp) Unmarshaller.unmarshal(com.cisco.eManager.common.event2.EventMgrResp.class, reader);
    } //-- com.cisco.eManager.common.event2.EventMgrResp unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

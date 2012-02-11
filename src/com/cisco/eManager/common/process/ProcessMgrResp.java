/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.process;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Process Sequencer Responses
 * 
 * @version $Revision$ $Date$
**/
public class ProcessMgrResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private GetProcessStatusForResp _getProcessStatusForResp;

    private GetProcessStatusResp _getProcessStatusResp;

    private java.lang.String _statusResp;

    private java.lang.String _getGroupStateResp;

    private GetSolutionStatusResp _getSolutionStatusResp;

    private GetAllGroupNamesResp _getAllGroupNamesResp;

    private GetProcessesForGroupResp _getProcessesForGroupResp;

    private boolean _getHealthResp;

    /**
     * keeps track of state for field: _getHealthResp
    **/
    private boolean _has_getHealthResp;

    private boolean _getSolutionHealthResp;

    /**
     * keeps track of state for field: _getSolutionHealthResp
    **/
    private boolean _has_getSolutionHealthResp;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProcessMgrResp() {
        super();
    } //-- com.cisco.eManager.common.process.ProcessMgrResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'getAllGroupNamesResp'.
     * 
     * @return the value of field 'getAllGroupNamesResp'.
    **/
    public GetAllGroupNamesResp getGetAllGroupNamesResp()
    {
        return this._getAllGroupNamesResp;
    } //-- GetAllGroupNamesResp getGetAllGroupNamesResp() 

    /**
     * Returns the value of field 'getGroupStateResp'.
     * 
     * @return the value of field 'getGroupStateResp'.
    **/
    public java.lang.String getGetGroupStateResp()
    {
        return this._getGroupStateResp;
    } //-- java.lang.String getGetGroupStateResp() 

    /**
     * Returns the value of field 'getHealthResp'.
     * 
     * @return the value of field 'getHealthResp'.
    **/
    public boolean getGetHealthResp()
    {
        return this._getHealthResp;
    } //-- boolean getGetHealthResp() 

    /**
     * Returns the value of field 'getProcessStatusForResp'.
     * 
     * @return the value of field 'getProcessStatusForResp'.
    **/
    public GetProcessStatusForResp getGetProcessStatusForResp()
    {
        return this._getProcessStatusForResp;
    } //-- GetProcessStatusForResp getGetProcessStatusForResp() 

    /**
     * Returns the value of field 'getProcessStatusResp'.
     * 
     * @return the value of field 'getProcessStatusResp'.
    **/
    public GetProcessStatusResp getGetProcessStatusResp()
    {
        return this._getProcessStatusResp;
    } //-- GetProcessStatusResp getGetProcessStatusResp() 

    /**
     * Returns the value of field 'getProcessesForGroupResp'.
     * 
     * @return the value of field 'getProcessesForGroupResp'.
    **/
    public GetProcessesForGroupResp getGetProcessesForGroupResp()
    {
        return this._getProcessesForGroupResp;
    } //-- GetProcessesForGroupResp getGetProcessesForGroupResp() 

    /**
     * Returns the value of field 'getSolutionHealthResp'.
     * 
     * @return the value of field 'getSolutionHealthResp'.
    **/
    public boolean getGetSolutionHealthResp()
    {
        return this._getSolutionHealthResp;
    } //-- boolean getGetSolutionHealthResp() 

    /**
     * Returns the value of field 'getSolutionStatusResp'.
     * 
     * @return the value of field 'getSolutionStatusResp'.
    **/
    public GetSolutionStatusResp getGetSolutionStatusResp()
    {
        return this._getSolutionStatusResp;
    } //-- GetSolutionStatusResp getGetSolutionStatusResp() 

    /**
     * Returns the value of field 'statusResp'.
     * 
     * @return the value of field 'statusResp'.
    **/
    public java.lang.String getStatusResp()
    {
        return this._statusResp;
    } //-- java.lang.String getStatusResp() 

    /**
    **/
    public boolean hasGetHealthResp()
    {
        return this._has_getHealthResp;
    } //-- boolean hasGetHealthResp() 

    /**
    **/
    public boolean hasGetSolutionHealthResp()
    {
        return this._has_getSolutionHealthResp;
    } //-- boolean hasGetSolutionHealthResp() 

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
     * Sets the value of field 'getAllGroupNamesResp'.
     * 
     * @param getAllGroupNamesResp the value of field
     * 'getAllGroupNamesResp'.
    **/
    public void setGetAllGroupNamesResp(GetAllGroupNamesResp getAllGroupNamesResp)
    {
        this._getAllGroupNamesResp = getAllGroupNamesResp;
    } //-- void setGetAllGroupNamesResp(GetAllGroupNamesResp) 

    /**
     * Sets the value of field 'getGroupStateResp'.
     * 
     * @param getGroupStateResp the value of field
     * 'getGroupStateResp'.
    **/
    public void setGetGroupStateResp(java.lang.String getGroupStateResp)
    {
        this._getGroupStateResp = getGroupStateResp;
    } //-- void setGetGroupStateResp(java.lang.String) 

    /**
     * Sets the value of field 'getHealthResp'.
     * 
     * @param getHealthResp the value of field 'getHealthResp'.
    **/
    public void setGetHealthResp(boolean getHealthResp)
    {
        this._getHealthResp = getHealthResp;
        this._has_getHealthResp = true;
    } //-- void setGetHealthResp(boolean) 

    /**
     * Sets the value of field 'getProcessStatusForResp'.
     * 
     * @param getProcessStatusForResp the value of field
     * 'getProcessStatusForResp'.
    **/
    public void setGetProcessStatusForResp(GetProcessStatusForResp getProcessStatusForResp)
    {
        this._getProcessStatusForResp = getProcessStatusForResp;
    } //-- void setGetProcessStatusForResp(GetProcessStatusForResp) 

    /**
     * Sets the value of field 'getProcessStatusResp'.
     * 
     * @param getProcessStatusResp the value of field
     * 'getProcessStatusResp'.
    **/
    public void setGetProcessStatusResp(GetProcessStatusResp getProcessStatusResp)
    {
        this._getProcessStatusResp = getProcessStatusResp;
    } //-- void setGetProcessStatusResp(GetProcessStatusResp) 

    /**
     * Sets the value of field 'getProcessesForGroupResp'.
     * 
     * @param getProcessesForGroupResp the value of field
     * 'getProcessesForGroupResp'.
    **/
    public void setGetProcessesForGroupResp(GetProcessesForGroupResp getProcessesForGroupResp)
    {
        this._getProcessesForGroupResp = getProcessesForGroupResp;
    } //-- void setGetProcessesForGroupResp(GetProcessesForGroupResp) 

    /**
     * Sets the value of field 'getSolutionHealthResp'.
     * 
     * @param getSolutionHealthResp the value of field
     * 'getSolutionHealthResp'.
    **/
    public void setGetSolutionHealthResp(boolean getSolutionHealthResp)
    {
        this._getSolutionHealthResp = getSolutionHealthResp;
        this._has_getSolutionHealthResp = true;
    } //-- void setGetSolutionHealthResp(boolean) 

    /**
     * Sets the value of field 'getSolutionStatusResp'.
     * 
     * @param getSolutionStatusResp the value of field
     * 'getSolutionStatusResp'.
    **/
    public void setGetSolutionStatusResp(GetSolutionStatusResp getSolutionStatusResp)
    {
        this._getSolutionStatusResp = getSolutionStatusResp;
    } //-- void setGetSolutionStatusResp(GetSolutionStatusResp) 

    /**
     * Sets the value of field 'statusResp'.
     * 
     * @param statusResp the value of field 'statusResp'.
    **/
    public void setStatusResp(java.lang.String statusResp)
    {
        this._statusResp = statusResp;
    } //-- void setStatusResp(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.process.ProcessMgrResp unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.process.ProcessMgrResp) Unmarshaller.unmarshal(com.cisco.eManager.common.process.ProcessMgrResp.class, reader);
    } //-- com.cisco.eManager.common.process.ProcessMgrResp unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

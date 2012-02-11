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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * eManager Inventory Msgs
 * 
 * @version $Revision$ $Date$
 */
public class InventoryMgrMsg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _applicationViewMsg
     */
    private com.cisco.eManager.common.inventory2.ApplicationViewMsg _applicationViewMsg;

    /**
     * Field _physicalViewMsg
     */
    private com.cisco.eManager.common.inventory2.PhysicalViewMsg _physicalViewMsg;

    /**
     * Field _solutionViewMsg
     */
    private com.cisco.eManager.common.inventory2.SolutionViewMsg _solutionViewMsg;

    /**
     * Field _appInstanceMsg
     */
    private com.cisco.eManager.common.inventory2.AppInstanceMsg _appInstanceMsg;

    /**
     * Field _appTypeMsg
     */
    private com.cisco.eManager.common.inventory2.AppTypeMsg _appTypeMsg;

    /**
     * Field _hostMsg
     */
    private com.cisco.eManager.common.inventory2.HostMsg _hostMsg;

    /**
     * Field _instrumentationMsg
     */
    private com.cisco.eManager.common.inventory2.InstrumentationMsg _instrumentationMsg;

    /**
     * Field _mgmtPolicyMsg
     */
    private com.cisco.eManager.common.inventory2.MgmtPolicyMsg _mgmtPolicyMsg;

    /**
     * Field _solutionMsg
     */
    private com.cisco.eManager.common.inventory2.SolutionMsg _solutionMsg;


      //----------------/
     //- Constructors -/
    //----------------/

    public InventoryMgrMsg() {
        super();
    } //-- com.cisco.eManager.common.inventory2.InventoryMgrMsg()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'appInstanceMsg'.
     * 
     * @return the value of field 'appInstanceMsg'.
     */
    public com.cisco.eManager.common.inventory2.AppInstanceMsg getAppInstanceMsg()
    {
        return this._appInstanceMsg;
    } //-- com.cisco.eManager.common.inventory2.AppInstanceMsg getAppInstanceMsg() 

    /**
     * Returns the value of field 'appTypeMsg'.
     * 
     * @return the value of field 'appTypeMsg'.
     */
    public com.cisco.eManager.common.inventory2.AppTypeMsg getAppTypeMsg()
    {
        return this._appTypeMsg;
    } //-- com.cisco.eManager.common.inventory2.AppTypeMsg getAppTypeMsg() 

    /**
     * Returns the value of field 'applicationViewMsg'.
     * 
     * @return the value of field 'applicationViewMsg'.
     */
    public com.cisco.eManager.common.inventory2.ApplicationViewMsg getApplicationViewMsg()
    {
        return this._applicationViewMsg;
    } //-- com.cisco.eManager.common.inventory2.ApplicationViewMsg getApplicationViewMsg() 

    /**
     * Returns the value of field 'hostMsg'.
     * 
     * @return the value of field 'hostMsg'.
     */
    public com.cisco.eManager.common.inventory2.HostMsg getHostMsg()
    {
        return this._hostMsg;
    } //-- com.cisco.eManager.common.inventory2.HostMsg getHostMsg() 

    /**
     * Returns the value of field 'instrumentationMsg'.
     * 
     * @return the value of field 'instrumentationMsg'.
     */
    public com.cisco.eManager.common.inventory2.InstrumentationMsg getInstrumentationMsg()
    {
        return this._instrumentationMsg;
    } //-- com.cisco.eManager.common.inventory2.InstrumentationMsg getInstrumentationMsg() 

    /**
     * Returns the value of field 'mgmtPolicyMsg'.
     * 
     * @return the value of field 'mgmtPolicyMsg'.
     */
    public com.cisco.eManager.common.inventory2.MgmtPolicyMsg getMgmtPolicyMsg()
    {
        return this._mgmtPolicyMsg;
    } //-- com.cisco.eManager.common.inventory2.MgmtPolicyMsg getMgmtPolicyMsg() 

    /**
     * Returns the value of field 'physicalViewMsg'.
     * 
     * @return the value of field 'physicalViewMsg'.
     */
    public com.cisco.eManager.common.inventory2.PhysicalViewMsg getPhysicalViewMsg()
    {
        return this._physicalViewMsg;
    } //-- com.cisco.eManager.common.inventory2.PhysicalViewMsg getPhysicalViewMsg() 

    /**
     * Returns the value of field 'solutionMsg'.
     * 
     * @return the value of field 'solutionMsg'.
     */
    public com.cisco.eManager.common.inventory2.SolutionMsg getSolutionMsg()
    {
        return this._solutionMsg;
    } //-- com.cisco.eManager.common.inventory2.SolutionMsg getSolutionMsg() 

    /**
     * Returns the value of field 'solutionViewMsg'.
     * 
     * @return the value of field 'solutionViewMsg'.
     */
    public com.cisco.eManager.common.inventory2.SolutionViewMsg getSolutionViewMsg()
    {
        return this._solutionViewMsg;
    } //-- com.cisco.eManager.common.inventory2.SolutionViewMsg getSolutionViewMsg() 

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
     * Sets the value of field 'appInstanceMsg'.
     * 
     * @param appInstanceMsg the value of field 'appInstanceMsg'.
     */
    public void setAppInstanceMsg(com.cisco.eManager.common.inventory2.AppInstanceMsg appInstanceMsg)
    {
        this._appInstanceMsg = appInstanceMsg;
    } //-- void setAppInstanceMsg(com.cisco.eManager.common.inventory2.AppInstanceMsg) 

    /**
     * Sets the value of field 'appTypeMsg'.
     * 
     * @param appTypeMsg the value of field 'appTypeMsg'.
     */
    public void setAppTypeMsg(com.cisco.eManager.common.inventory2.AppTypeMsg appTypeMsg)
    {
        this._appTypeMsg = appTypeMsg;
    } //-- void setAppTypeMsg(com.cisco.eManager.common.inventory2.AppTypeMsg) 

    /**
     * Sets the value of field 'applicationViewMsg'.
     * 
     * @param applicationViewMsg the value of field
     * 'applicationViewMsg'.
     */
    public void setApplicationViewMsg(com.cisco.eManager.common.inventory2.ApplicationViewMsg applicationViewMsg)
    {
        this._applicationViewMsg = applicationViewMsg;
    } //-- void setApplicationViewMsg(com.cisco.eManager.common.inventory2.ApplicationViewMsg) 

    /**
     * Sets the value of field 'hostMsg'.
     * 
     * @param hostMsg the value of field 'hostMsg'.
     */
    public void setHostMsg(com.cisco.eManager.common.inventory2.HostMsg hostMsg)
    {
        this._hostMsg = hostMsg;
    } //-- void setHostMsg(com.cisco.eManager.common.inventory2.HostMsg) 

    /**
     * Sets the value of field 'instrumentationMsg'.
     * 
     * @param instrumentationMsg the value of field
     * 'instrumentationMsg'.
     */
    public void setInstrumentationMsg(com.cisco.eManager.common.inventory2.InstrumentationMsg instrumentationMsg)
    {
        this._instrumentationMsg = instrumentationMsg;
    } //-- void setInstrumentationMsg(com.cisco.eManager.common.inventory2.InstrumentationMsg) 

    /**
     * Sets the value of field 'mgmtPolicyMsg'.
     * 
     * @param mgmtPolicyMsg the value of field 'mgmtPolicyMsg'.
     */
    public void setMgmtPolicyMsg(com.cisco.eManager.common.inventory2.MgmtPolicyMsg mgmtPolicyMsg)
    {
        this._mgmtPolicyMsg = mgmtPolicyMsg;
    } //-- void setMgmtPolicyMsg(com.cisco.eManager.common.inventory2.MgmtPolicyMsg) 

    /**
     * Sets the value of field 'physicalViewMsg'.
     * 
     * @param physicalViewMsg the value of field 'physicalViewMsg'.
     */
    public void setPhysicalViewMsg(com.cisco.eManager.common.inventory2.PhysicalViewMsg physicalViewMsg)
    {
        this._physicalViewMsg = physicalViewMsg;
    } //-- void setPhysicalViewMsg(com.cisco.eManager.common.inventory2.PhysicalViewMsg) 

    /**
     * Sets the value of field 'solutionMsg'.
     * 
     * @param solutionMsg the value of field 'solutionMsg'.
     */
    public void setSolutionMsg(com.cisco.eManager.common.inventory2.SolutionMsg solutionMsg)
    {
        this._solutionMsg = solutionMsg;
    } //-- void setSolutionMsg(com.cisco.eManager.common.inventory2.SolutionMsg) 

    /**
     * Sets the value of field 'solutionViewMsg'.
     * 
     * @param solutionViewMsg the value of field 'solutionViewMsg'.
     */
    public void setSolutionViewMsg(com.cisco.eManager.common.inventory2.SolutionViewMsg solutionViewMsg)
    {
        this._solutionViewMsg = solutionViewMsg;
    } //-- void setSolutionViewMsg(com.cisco.eManager.common.inventory2.SolutionViewMsg) 

    /**
     * Method unmarshalInventoryMgrMsg
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalInventoryMgrMsg(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.InventoryMgrMsg) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.InventoryMgrMsg.class, reader);
    } //-- java.lang.Object unmarshalInventoryMgrMsg(java.io.Reader) 

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

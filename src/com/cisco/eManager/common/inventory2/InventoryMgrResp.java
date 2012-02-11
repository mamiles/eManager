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
 * eManager Inventory Responses
 * 
 * @version $Revision$ $Date$
 */
public class InventoryMgrResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _applicationViewResp
     */
    private com.cisco.eManager.common.inventory2.ApplicationViewResp _applicationViewResp;

    /**
     * Field _physicalViewResp
     */
    private com.cisco.eManager.common.inventory2.PhysicalViewResp _physicalViewResp;

    /**
     * Field _solutionViewResp
     */
    private com.cisco.eManager.common.inventory2.SolutionViewResp _solutionViewResp;

    /**
     * Field _appInstanceResp
     */
    private com.cisco.eManager.common.inventory2.AppInstanceResp _appInstanceResp;

    /**
     * Field _appTypeResp
     */
    private com.cisco.eManager.common.inventory2.AppTypeResp _appTypeResp;

    /**
     * Field _hostResp
     */
    private com.cisco.eManager.common.inventory2.HostResp _hostResp;

    /**
     * Field _instrumentationResp
     */
    private com.cisco.eManager.common.inventory2.InstrumentationResp _instrumentationResp;

    /**
     * Field _mgmtPolicyResp
     */
    private com.cisco.eManager.common.inventory2.MgmtPolicyResp _mgmtPolicyResp;

    /**
     * Field _solutionResp
     */
    private com.cisco.eManager.common.inventory2.SolutionResp _solutionResp;


      //----------------/
     //- Constructors -/
    //----------------/

    public InventoryMgrResp() {
        super();
    } //-- com.cisco.eManager.common.inventory2.InventoryMgrResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'appInstanceResp'.
     * 
     * @return the value of field 'appInstanceResp'.
     */
    public com.cisco.eManager.common.inventory2.AppInstanceResp getAppInstanceResp()
    {
        return this._appInstanceResp;
    } //-- com.cisco.eManager.common.inventory2.AppInstanceResp getAppInstanceResp() 

    /**
     * Returns the value of field 'appTypeResp'.
     * 
     * @return the value of field 'appTypeResp'.
     */
    public com.cisco.eManager.common.inventory2.AppTypeResp getAppTypeResp()
    {
        return this._appTypeResp;
    } //-- com.cisco.eManager.common.inventory2.AppTypeResp getAppTypeResp() 

    /**
     * Returns the value of field 'applicationViewResp'.
     * 
     * @return the value of field 'applicationViewResp'.
     */
    public com.cisco.eManager.common.inventory2.ApplicationViewResp getApplicationViewResp()
    {
        return this._applicationViewResp;
    } //-- com.cisco.eManager.common.inventory2.ApplicationViewResp getApplicationViewResp() 

    /**
     * Returns the value of field 'hostResp'.
     * 
     * @return the value of field 'hostResp'.
     */
    public com.cisco.eManager.common.inventory2.HostResp getHostResp()
    {
        return this._hostResp;
    } //-- com.cisco.eManager.common.inventory2.HostResp getHostResp() 

    /**
     * Returns the value of field 'instrumentationResp'.
     * 
     * @return the value of field 'instrumentationResp'.
     */
    public com.cisco.eManager.common.inventory2.InstrumentationResp getInstrumentationResp()
    {
        return this._instrumentationResp;
    } //-- com.cisco.eManager.common.inventory2.InstrumentationResp getInstrumentationResp() 

    /**
     * Returns the value of field 'mgmtPolicyResp'.
     * 
     * @return the value of field 'mgmtPolicyResp'.
     */
    public com.cisco.eManager.common.inventory2.MgmtPolicyResp getMgmtPolicyResp()
    {
        return this._mgmtPolicyResp;
    } //-- com.cisco.eManager.common.inventory2.MgmtPolicyResp getMgmtPolicyResp() 

    /**
     * Returns the value of field 'physicalViewResp'.
     * 
     * @return the value of field 'physicalViewResp'.
     */
    public com.cisco.eManager.common.inventory2.PhysicalViewResp getPhysicalViewResp()
    {
        return this._physicalViewResp;
    } //-- com.cisco.eManager.common.inventory2.PhysicalViewResp getPhysicalViewResp() 

    /**
     * Returns the value of field 'solutionResp'.
     * 
     * @return the value of field 'solutionResp'.
     */
    public com.cisco.eManager.common.inventory2.SolutionResp getSolutionResp()
    {
        return this._solutionResp;
    } //-- com.cisco.eManager.common.inventory2.SolutionResp getSolutionResp() 

    /**
     * Returns the value of field 'solutionViewResp'.
     * 
     * @return the value of field 'solutionViewResp'.
     */
    public com.cisco.eManager.common.inventory2.SolutionViewResp getSolutionViewResp()
    {
        return this._solutionViewResp;
    } //-- com.cisco.eManager.common.inventory2.SolutionViewResp getSolutionViewResp() 

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
     * Sets the value of field 'appInstanceResp'.
     * 
     * @param appInstanceResp the value of field 'appInstanceResp'.
     */
    public void setAppInstanceResp(com.cisco.eManager.common.inventory2.AppInstanceResp appInstanceResp)
    {
        this._appInstanceResp = appInstanceResp;
    } //-- void setAppInstanceResp(com.cisco.eManager.common.inventory2.AppInstanceResp) 

    /**
     * Sets the value of field 'appTypeResp'.
     * 
     * @param appTypeResp the value of field 'appTypeResp'.
     */
    public void setAppTypeResp(com.cisco.eManager.common.inventory2.AppTypeResp appTypeResp)
    {
        this._appTypeResp = appTypeResp;
    } //-- void setAppTypeResp(com.cisco.eManager.common.inventory2.AppTypeResp) 

    /**
     * Sets the value of field 'applicationViewResp'.
     * 
     * @param applicationViewResp the value of field
     * 'applicationViewResp'.
     */
    public void setApplicationViewResp(com.cisco.eManager.common.inventory2.ApplicationViewResp applicationViewResp)
    {
        this._applicationViewResp = applicationViewResp;
    } //-- void setApplicationViewResp(com.cisco.eManager.common.inventory2.ApplicationViewResp) 

    /**
     * Sets the value of field 'hostResp'.
     * 
     * @param hostResp the value of field 'hostResp'.
     */
    public void setHostResp(com.cisco.eManager.common.inventory2.HostResp hostResp)
    {
        this._hostResp = hostResp;
    } //-- void setHostResp(com.cisco.eManager.common.inventory2.HostResp) 

    /**
     * Sets the value of field 'instrumentationResp'.
     * 
     * @param instrumentationResp the value of field
     * 'instrumentationResp'.
     */
    public void setInstrumentationResp(com.cisco.eManager.common.inventory2.InstrumentationResp instrumentationResp)
    {
        this._instrumentationResp = instrumentationResp;
    } //-- void setInstrumentationResp(com.cisco.eManager.common.inventory2.InstrumentationResp) 

    /**
     * Sets the value of field 'mgmtPolicyResp'.
     * 
     * @param mgmtPolicyResp the value of field 'mgmtPolicyResp'.
     */
    public void setMgmtPolicyResp(com.cisco.eManager.common.inventory2.MgmtPolicyResp mgmtPolicyResp)
    {
        this._mgmtPolicyResp = mgmtPolicyResp;
    } //-- void setMgmtPolicyResp(com.cisco.eManager.common.inventory2.MgmtPolicyResp) 

    /**
     * Sets the value of field 'physicalViewResp'.
     * 
     * @param physicalViewResp the value of field 'physicalViewResp'
     */
    public void setPhysicalViewResp(com.cisco.eManager.common.inventory2.PhysicalViewResp physicalViewResp)
    {
        this._physicalViewResp = physicalViewResp;
    } //-- void setPhysicalViewResp(com.cisco.eManager.common.inventory2.PhysicalViewResp) 

    /**
     * Sets the value of field 'solutionResp'.
     * 
     * @param solutionResp the value of field 'solutionResp'.
     */
    public void setSolutionResp(com.cisco.eManager.common.inventory2.SolutionResp solutionResp)
    {
        this._solutionResp = solutionResp;
    } //-- void setSolutionResp(com.cisco.eManager.common.inventory2.SolutionResp) 

    /**
     * Sets the value of field 'solutionViewResp'.
     * 
     * @param solutionViewResp the value of field 'solutionViewResp'
     */
    public void setSolutionViewResp(com.cisco.eManager.common.inventory2.SolutionViewResp solutionViewResp)
    {
        this._solutionViewResp = solutionViewResp;
    } //-- void setSolutionViewResp(com.cisco.eManager.common.inventory2.SolutionViewResp) 

    /**
     * Method unmarshalInventoryMgrResp
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalInventoryMgrResp(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.InventoryMgrResp) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.InventoryMgrResp.class, reader);
    } //-- java.lang.Object unmarshalInventoryMgrResp(java.io.Reader) 

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

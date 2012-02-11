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
 * Class InventoryChangeNotification.
 * 
 * @version $Revision$ $Date$
 */
public class InventoryChangeNotification implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _appInstanceChange
     */
    private com.cisco.eManager.common.inventory2.AppInstanceChange _appInstanceChange;

    /**
     * Field _appTypeChange
     */
    private com.cisco.eManager.common.inventory2.AppTypeChange _appTypeChange;

    /**
     * Field _hostChange
     */
    private com.cisco.eManager.common.inventory2.HostChange _hostChange;

    /**
     * Field _instrumentationChange
     */
    private com.cisco.eManager.common.inventory2.InstrumentationChange _instrumentationChange;

    /**
     * Field _mgmtPolicyChange
     */
    private com.cisco.eManager.common.inventory2.MgmtPolicyChange _mgmtPolicyChange;

    /**
     * Field _solutionChange
     */
    private com.cisco.eManager.common.inventory2.SolutionChange _solutionChange;

    /**
     * Field _viewChange
     */
    private com.cisco.eManager.common.inventory2.ViewChange _viewChange;


      //----------------/
     //- Constructors -/
    //----------------/

    public InventoryChangeNotification() {
        super();
    } //-- com.cisco.eManager.common.inventory2.InventoryChangeNotification()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'appInstanceChange'.
     * 
     * @return the value of field 'appInstanceChange'.
     */
    public com.cisco.eManager.common.inventory2.AppInstanceChange getAppInstanceChange()
    {
        return this._appInstanceChange;
    } //-- com.cisco.eManager.common.inventory2.AppInstanceChange getAppInstanceChange() 

    /**
     * Returns the value of field 'appTypeChange'.
     * 
     * @return the value of field 'appTypeChange'.
     */
    public com.cisco.eManager.common.inventory2.AppTypeChange getAppTypeChange()
    {
        return this._appTypeChange;
    } //-- com.cisco.eManager.common.inventory2.AppTypeChange getAppTypeChange() 

    /**
     * Returns the value of field 'hostChange'.
     * 
     * @return the value of field 'hostChange'.
     */
    public com.cisco.eManager.common.inventory2.HostChange getHostChange()
    {
        return this._hostChange;
    } //-- com.cisco.eManager.common.inventory2.HostChange getHostChange() 

    /**
     * Returns the value of field 'instrumentationChange'.
     * 
     * @return the value of field 'instrumentationChange'.
     */
    public com.cisco.eManager.common.inventory2.InstrumentationChange getInstrumentationChange()
    {
        return this._instrumentationChange;
    } //-- com.cisco.eManager.common.inventory2.InstrumentationChange getInstrumentationChange() 

    /**
     * Returns the value of field 'mgmtPolicyChange'.
     * 
     * @return the value of field 'mgmtPolicyChange'.
     */
    public com.cisco.eManager.common.inventory2.MgmtPolicyChange getMgmtPolicyChange()
    {
        return this._mgmtPolicyChange;
    } //-- com.cisco.eManager.common.inventory2.MgmtPolicyChange getMgmtPolicyChange() 

    /**
     * Returns the value of field 'solutionChange'.
     * 
     * @return the value of field 'solutionChange'.
     */
    public com.cisco.eManager.common.inventory2.SolutionChange getSolutionChange()
    {
        return this._solutionChange;
    } //-- com.cisco.eManager.common.inventory2.SolutionChange getSolutionChange() 

    /**
     * Returns the value of field 'viewChange'.
     * 
     * @return the value of field 'viewChange'.
     */
    public com.cisco.eManager.common.inventory2.ViewChange getViewChange()
    {
        return this._viewChange;
    } //-- com.cisco.eManager.common.inventory2.ViewChange getViewChange() 

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
     * Sets the value of field 'appInstanceChange'.
     * 
     * @param appInstanceChange the value of field
     * 'appInstanceChange'.
     */
    public void setAppInstanceChange(com.cisco.eManager.common.inventory2.AppInstanceChange appInstanceChange)
    {
        this._appInstanceChange = appInstanceChange;
    } //-- void setAppInstanceChange(com.cisco.eManager.common.inventory2.AppInstanceChange) 

    /**
     * Sets the value of field 'appTypeChange'.
     * 
     * @param appTypeChange the value of field 'appTypeChange'.
     */
    public void setAppTypeChange(com.cisco.eManager.common.inventory2.AppTypeChange appTypeChange)
    {
        this._appTypeChange = appTypeChange;
    } //-- void setAppTypeChange(com.cisco.eManager.common.inventory2.AppTypeChange) 

    /**
     * Sets the value of field 'hostChange'.
     * 
     * @param hostChange the value of field 'hostChange'.
     */
    public void setHostChange(com.cisco.eManager.common.inventory2.HostChange hostChange)
    {
        this._hostChange = hostChange;
    } //-- void setHostChange(com.cisco.eManager.common.inventory2.HostChange) 

    /**
     * Sets the value of field 'instrumentationChange'.
     * 
     * @param instrumentationChange the value of field
     * 'instrumentationChange'.
     */
    public void setInstrumentationChange(com.cisco.eManager.common.inventory2.InstrumentationChange instrumentationChange)
    {
        this._instrumentationChange = instrumentationChange;
    } //-- void setInstrumentationChange(com.cisco.eManager.common.inventory2.InstrumentationChange) 

    /**
     * Sets the value of field 'mgmtPolicyChange'.
     * 
     * @param mgmtPolicyChange the value of field 'mgmtPolicyChange'
     */
    public void setMgmtPolicyChange(com.cisco.eManager.common.inventory2.MgmtPolicyChange mgmtPolicyChange)
    {
        this._mgmtPolicyChange = mgmtPolicyChange;
    } //-- void setMgmtPolicyChange(com.cisco.eManager.common.inventory2.MgmtPolicyChange) 

    /**
     * Sets the value of field 'solutionChange'.
     * 
     * @param solutionChange the value of field 'solutionChange'.
     */
    public void setSolutionChange(com.cisco.eManager.common.inventory2.SolutionChange solutionChange)
    {
        this._solutionChange = solutionChange;
    } //-- void setSolutionChange(com.cisco.eManager.common.inventory2.SolutionChange) 

    /**
     * Sets the value of field 'viewChange'.
     * 
     * @param viewChange the value of field 'viewChange'.
     */
    public void setViewChange(com.cisco.eManager.common.inventory2.ViewChange viewChange)
    {
        this._viewChange = viewChange;
    } //-- void setViewChange(com.cisco.eManager.common.inventory2.ViewChange) 

    /**
     * Method unmarshalInventoryChangeNotification
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalInventoryChangeNotification(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.InventoryChangeNotification) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.InventoryChangeNotification.class, reader);
    } //-- java.lang.Object unmarshalInventoryChangeNotification(java.io.Reader) 

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

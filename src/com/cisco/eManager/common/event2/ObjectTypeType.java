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
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public abstract class ObjectTypeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private int _hostAgent;

    /**
     * keeps track of state for field: _hostAgent
    **/
    private boolean _has_hostAgent;

    private int _applicationType;

    /**
     * keeps track of state for field: _applicationType
    **/
    private boolean _has_applicationType;

    private int _applicationInstance;

    /**
     * keeps track of state for field: _applicationInstance
    **/
    private boolean _has_applicationInstance;

    private int _physicalHierarchyContainer;

    /**
     * keeps track of state for field: _physicalHierarchyContainer
    **/
    private boolean _has_physicalHierarchyContainer;

    private int _applicationHierarchyContainer;

    /**
     * keeps track of state for field: _applicationHierarchyContaine
    **/
    private boolean _has_applicationHierarchyContainer;

    private int _mgmtPolicy;

    /**
     * keeps track of state for field: _mgmtPolicy
    **/
    private boolean _has_mgmtPolicy;

    private int _instrumentation;

    /**
     * keeps track of state for field: _instrumentation
    **/
    private boolean _has_instrumentation;

    private int _userAccount;

    /**
     * keeps track of state for field: _userAccount
    **/
    private boolean _has_userAccount;

    private int _event;

    /**
     * keeps track of state for field: _event
    **/
    private boolean _has_event;

    private int _auditLog;

    /**
     * keeps track of state for field: _auditLog
    **/
    private boolean _has_auditLog;

    private int _securityRole;

    /**
     * keeps track of state for field: _securityRole
    **/
    private boolean _has_securityRole;

    private int _solution;

    /**
     * keeps track of state for field: _solution
    **/
    private boolean _has_solution;

    private int _solutionHierarchyContainer;

    /**
     * keeps track of state for field: _solutionHierarchyContainer
    **/
    private boolean _has_solutionHierarchyContainer;


      //----------------/
     //- Constructors -/
    //----------------/

    public ObjectTypeType() {
        super();
    } //-- com.cisco.eManager.common.event2.ObjectTypeType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'applicationHierarchyContainer'.
     * 
     * @return the value of field 'applicationHierarchyContainer'.
    **/
    public int getApplicationHierarchyContainer()
    {
        return this._applicationHierarchyContainer;
    } //-- int getApplicationHierarchyContainer() 

    /**
     * Returns the value of field 'applicationInstance'.
     * 
     * @return the value of field 'applicationInstance'.
    **/
    public int getApplicationInstance()
    {
        return this._applicationInstance;
    } //-- int getApplicationInstance() 

    /**
     * Returns the value of field 'applicationType'.
     * 
     * @return the value of field 'applicationType'.
    **/
    public int getApplicationType()
    {
        return this._applicationType;
    } //-- int getApplicationType() 

    /**
     * Returns the value of field 'auditLog'.
     * 
     * @return the value of field 'auditLog'.
    **/
    public int getAuditLog()
    {
        return this._auditLog;
    } //-- int getAuditLog() 

    /**
     * Returns the value of field 'event'.
     * 
     * @return the value of field 'event'.
    **/
    public int getEvent()
    {
        return this._event;
    } //-- int getEvent() 

    /**
     * Returns the value of field 'hostAgent'.
     * 
     * @return the value of field 'hostAgent'.
    **/
    public int getHostAgent()
    {
        return this._hostAgent;
    } //-- int getHostAgent() 

    /**
     * Returns the value of field 'instrumentation'.
     * 
     * @return the value of field 'instrumentation'.
    **/
    public int getInstrumentation()
    {
        return this._instrumentation;
    } //-- int getInstrumentation() 

    /**
     * Returns the value of field 'mgmtPolicy'.
     * 
     * @return the value of field 'mgmtPolicy'.
    **/
    public int getMgmtPolicy()
    {
        return this._mgmtPolicy;
    } //-- int getMgmtPolicy() 

    /**
     * Returns the value of field 'physicalHierarchyContainer'.
     * 
     * @return the value of field 'physicalHierarchyContainer'.
    **/
    public int getPhysicalHierarchyContainer()
    {
        return this._physicalHierarchyContainer;
    } //-- int getPhysicalHierarchyContainer() 

    /**
     * Returns the value of field 'securityRole'.
     * 
     * @return the value of field 'securityRole'.
    **/
    public int getSecurityRole()
    {
        return this._securityRole;
    } //-- int getSecurityRole() 

    /**
     * Returns the value of field 'solution'.
     * 
     * @return the value of field 'solution'.
    **/
    public int getSolution()
    {
        return this._solution;
    } //-- int getSolution() 

    /**
     * Returns the value of field 'solutionHierarchyContainer'.
     * 
     * @return the value of field 'solutionHierarchyContainer'.
    **/
    public int getSolutionHierarchyContainer()
    {
        return this._solutionHierarchyContainer;
    } //-- int getSolutionHierarchyContainer() 

    /**
     * Returns the value of field 'userAccount'.
     * 
     * @return the value of field 'userAccount'.
    **/
    public int getUserAccount()
    {
        return this._userAccount;
    } //-- int getUserAccount() 

    /**
    **/
    public boolean hasApplicationHierarchyContainer()
    {
        return this._has_applicationHierarchyContainer;
    } //-- boolean hasApplicationHierarchyContainer() 

    /**
    **/
    public boolean hasApplicationInstance()
    {
        return this._has_applicationInstance;
    } //-- boolean hasApplicationInstance() 

    /**
    **/
    public boolean hasApplicationType()
    {
        return this._has_applicationType;
    } //-- boolean hasApplicationType() 

    /**
    **/
    public boolean hasAuditLog()
    {
        return this._has_auditLog;
    } //-- boolean hasAuditLog() 

    /**
    **/
    public boolean hasEvent()
    {
        return this._has_event;
    } //-- boolean hasEvent() 

    /**
    **/
    public boolean hasHostAgent()
    {
        return this._has_hostAgent;
    } //-- boolean hasHostAgent() 

    /**
    **/
    public boolean hasInstrumentation()
    {
        return this._has_instrumentation;
    } //-- boolean hasInstrumentation() 

    /**
    **/
    public boolean hasMgmtPolicy()
    {
        return this._has_mgmtPolicy;
    } //-- boolean hasMgmtPolicy() 

    /**
    **/
    public boolean hasPhysicalHierarchyContainer()
    {
        return this._has_physicalHierarchyContainer;
    } //-- boolean hasPhysicalHierarchyContainer() 

    /**
    **/
    public boolean hasSecurityRole()
    {
        return this._has_securityRole;
    } //-- boolean hasSecurityRole() 

    /**
    **/
    public boolean hasSolution()
    {
        return this._has_solution;
    } //-- boolean hasSolution() 

    /**
    **/
    public boolean hasSolutionHierarchyContainer()
    {
        return this._has_solutionHierarchyContainer;
    } //-- boolean hasSolutionHierarchyContainer() 

    /**
    **/
    public boolean hasUserAccount()
    {
        return this._has_userAccount;
    } //-- boolean hasUserAccount() 

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
     * Sets the value of field 'applicationHierarchyContainer'.
     * 
     * @param applicationHierarchyContainer the value of field
     * 'applicationHierarchyContainer'.
    **/
    public void setApplicationHierarchyContainer(int applicationHierarchyContainer)
    {
        this._applicationHierarchyContainer = applicationHierarchyContainer;
        this._has_applicationHierarchyContainer = true;
    } //-- void setApplicationHierarchyContainer(int) 

    /**
     * Sets the value of field 'applicationInstance'.
     * 
     * @param applicationInstance the value of field
     * 'applicationInstance'.
    **/
    public void setApplicationInstance(int applicationInstance)
    {
        this._applicationInstance = applicationInstance;
        this._has_applicationInstance = true;
    } //-- void setApplicationInstance(int) 

    /**
     * Sets the value of field 'applicationType'.
     * 
     * @param applicationType the value of field 'applicationType'.
    **/
    public void setApplicationType(int applicationType)
    {
        this._applicationType = applicationType;
        this._has_applicationType = true;
    } //-- void setApplicationType(int) 

    /**
     * Sets the value of field 'auditLog'.
     * 
     * @param auditLog the value of field 'auditLog'.
    **/
    public void setAuditLog(int auditLog)
    {
        this._auditLog = auditLog;
        this._has_auditLog = true;
    } //-- void setAuditLog(int) 

    /**
     * Sets the value of field 'event'.
     * 
     * @param event the value of field 'event'.
    **/
    public void setEvent(int event)
    {
        this._event = event;
        this._has_event = true;
    } //-- void setEvent(int) 

    /**
     * Sets the value of field 'hostAgent'.
     * 
     * @param hostAgent the value of field 'hostAgent'.
    **/
    public void setHostAgent(int hostAgent)
    {
        this._hostAgent = hostAgent;
        this._has_hostAgent = true;
    } //-- void setHostAgent(int) 

    /**
     * Sets the value of field 'instrumentation'.
     * 
     * @param instrumentation the value of field 'instrumentation'.
    **/
    public void setInstrumentation(int instrumentation)
    {
        this._instrumentation = instrumentation;
        this._has_instrumentation = true;
    } //-- void setInstrumentation(int) 

    /**
     * Sets the value of field 'mgmtPolicy'.
     * 
     * @param mgmtPolicy the value of field 'mgmtPolicy'.
    **/
    public void setMgmtPolicy(int mgmtPolicy)
    {
        this._mgmtPolicy = mgmtPolicy;
        this._has_mgmtPolicy = true;
    } //-- void setMgmtPolicy(int) 

    /**
     * Sets the value of field 'physicalHierarchyContainer'.
     * 
     * @param physicalHierarchyContainer the value of field
     * 'physicalHierarchyContainer'.
    **/
    public void setPhysicalHierarchyContainer(int physicalHierarchyContainer)
    {
        this._physicalHierarchyContainer = physicalHierarchyContainer;
        this._has_physicalHierarchyContainer = true;
    } //-- void setPhysicalHierarchyContainer(int) 

    /**
     * Sets the value of field 'securityRole'.
     * 
     * @param securityRole the value of field 'securityRole'.
    **/
    public void setSecurityRole(int securityRole)
    {
        this._securityRole = securityRole;
        this._has_securityRole = true;
    } //-- void setSecurityRole(int) 

    /**
     * Sets the value of field 'solution'.
     * 
     * @param solution the value of field 'solution'.
    **/
    public void setSolution(int solution)
    {
        this._solution = solution;
        this._has_solution = true;
    } //-- void setSolution(int) 

    /**
     * Sets the value of field 'solutionHierarchyContainer'.
     * 
     * @param solutionHierarchyContainer the value of field
     * 'solutionHierarchyContainer'.
    **/
    public void setSolutionHierarchyContainer(int solutionHierarchyContainer)
    {
        this._solutionHierarchyContainer = solutionHierarchyContainer;
        this._has_solutionHierarchyContainer = true;
    } //-- void setSolutionHierarchyContainer(int) 

    /**
     * Sets the value of field 'userAccount'.
     * 
     * @param userAccount the value of field 'userAccount'.
    **/
    public void setUserAccount(int userAccount)
    {
        this._userAccount = userAccount;
        this._has_userAccount = true;
    } //-- void setUserAccount(int) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

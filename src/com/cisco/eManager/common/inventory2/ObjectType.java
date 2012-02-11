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
 * Class ObjectType.
 * 
 * @version $Revision$ $Date$
 */
public class ObjectType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _appInstanceType
     */
    private int _appInstanceType;

    /**
     * keeps track of state for field: _appInstanceType
     */
    private boolean _has_appInstanceType;

    /**
     * Field _applicationHierarchyContainerType
     */
    private int _applicationHierarchyContainerType;

    /**
     * keeps track of state for field:
     * _applicationHierarchyContainerType
     */
    private boolean _has_applicationHierarchyContainerType;

    /**
     * Field _appTypeType
     */
    private int _appTypeType;

    /**
     * keeps track of state for field: _appTypeType
     */
    private boolean _has_appTypeType;

    /**
     * Field _auditLogType
     */
    private int _auditLogType;

    /**
     * keeps track of state for field: _auditLogType
     */
    private boolean _has_auditLogType;

    /**
     * Field _eventType
     */
    private int _eventType;

    /**
     * keeps track of state for field: _eventType
     */
    private boolean _has_eventType;

    /**
     * Field _hostAgentType
     */
    private int _hostAgentType;

    /**
     * keeps track of state for field: _hostAgentType
     */
    private boolean _has_hostAgentType;

    /**
     * Field _instrumentationType
     */
    private int _instrumentationType;

    /**
     * keeps track of state for field: _instrumentationType
     */
    private boolean _has_instrumentationType;

    /**
     * Field _mgmtPolicyType
     */
    private int _mgmtPolicyType;

    /**
     * keeps track of state for field: _mgmtPolicyType
     */
    private boolean _has_mgmtPolicyType;

    /**
     * Field _physicalHierarchyContainerType
     */
    private int _physicalHierarchyContainerType;

    /**
     * keeps track of state for field:
     * _physicalHierarchyContainerType
     */
    private boolean _has_physicalHierarchyContainerType;

    /**
     * Field _securityRoleType
     */
    private int _securityRoleType;

    /**
     * keeps track of state for field: _securityRoleType
     */
    private boolean _has_securityRoleType;

    /**
     * Field _solutionHierarchyContainerType
     */
    private int _solutionHierarchyContainerType;

    /**
     * keeps track of state for field:
     * _solutionHierarchyContainerType
     */
    private boolean _has_solutionHierarchyContainerType;

    /**
     * Field _solutionType
     */
    private int _solutionType;

    /**
     * keeps track of state for field: _solutionType
     */
    private boolean _has_solutionType;

    /**
     * Field _userAccountType
     */
    private int _userAccountType;

    /**
     * keeps track of state for field: _userAccountType
     */
    private boolean _has_userAccountType;


      //----------------/
     //- Constructors -/
    //----------------/

    public ObjectType() {
        super();
    } //-- com.cisco.eManager.common.inventory2.ObjectType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'appInstanceType'.
     * 
     * @return the value of field 'appInstanceType'.
     */
    public int getAppInstanceType()
    {
        return this._appInstanceType;
    } //-- int getAppInstanceType() 

    /**
     * Returns the value of field 'appTypeType'.
     * 
     * @return the value of field 'appTypeType'.
     */
    public int getAppTypeType()
    {
        return this._appTypeType;
    } //-- int getAppTypeType() 

    /**
     * Returns the value of field
     * 'applicationHierarchyContainerType'.
     * 
     * @return the value of field
     * 'applicationHierarchyContainerType'.
     */
    public int getApplicationHierarchyContainerType()
    {
        return this._applicationHierarchyContainerType;
    } //-- int getApplicationHierarchyContainerType() 

    /**
     * Returns the value of field 'auditLogType'.
     * 
     * @return the value of field 'auditLogType'.
     */
    public int getAuditLogType()
    {
        return this._auditLogType;
    } //-- int getAuditLogType() 

    /**
     * Returns the value of field 'eventType'.
     * 
     * @return the value of field 'eventType'.
     */
    public int getEventType()
    {
        return this._eventType;
    } //-- int getEventType() 

    /**
     * Returns the value of field 'hostAgentType'.
     * 
     * @return the value of field 'hostAgentType'.
     */
    public int getHostAgentType()
    {
        return this._hostAgentType;
    } //-- int getHostAgentType() 

    /**
     * Returns the value of field 'instrumentationType'.
     * 
     * @return the value of field 'instrumentationType'.
     */
    public int getInstrumentationType()
    {
        return this._instrumentationType;
    } //-- int getInstrumentationType() 

    /**
     * Returns the value of field 'mgmtPolicyType'.
     * 
     * @return the value of field 'mgmtPolicyType'.
     */
    public int getMgmtPolicyType()
    {
        return this._mgmtPolicyType;
    } //-- int getMgmtPolicyType() 

    /**
     * Returns the value of field 'physicalHierarchyContainerType'.
     * 
     * @return the value of field 'physicalHierarchyContainerType'.
     */
    public int getPhysicalHierarchyContainerType()
    {
        return this._physicalHierarchyContainerType;
    } //-- int getPhysicalHierarchyContainerType() 

    /**
     * Returns the value of field 'securityRoleType'.
     * 
     * @return the value of field 'securityRoleType'.
     */
    public int getSecurityRoleType()
    {
        return this._securityRoleType;
    } //-- int getSecurityRoleType() 

    /**
     * Returns the value of field 'solutionHierarchyContainerType'.
     * 
     * @return the value of field 'solutionHierarchyContainerType'.
     */
    public int getSolutionHierarchyContainerType()
    {
        return this._solutionHierarchyContainerType;
    } //-- int getSolutionHierarchyContainerType() 

    /**
     * Returns the value of field 'solutionType'.
     * 
     * @return the value of field 'solutionType'.
     */
    public int getSolutionType()
    {
        return this._solutionType;
    } //-- int getSolutionType() 

    /**
     * Returns the value of field 'userAccountType'.
     * 
     * @return the value of field 'userAccountType'.
     */
    public int getUserAccountType()
    {
        return this._userAccountType;
    } //-- int getUserAccountType() 

    /**
     * Method hasAppInstanceType
     */
    public boolean hasAppInstanceType()
    {
        return this._has_appInstanceType;
    } //-- boolean hasAppInstanceType() 

    /**
     * Method hasAppTypeType
     */
    public boolean hasAppTypeType()
    {
        return this._has_appTypeType;
    } //-- boolean hasAppTypeType() 

    /**
     * Method hasApplicationHierarchyContainerType
     */
    public boolean hasApplicationHierarchyContainerType()
    {
        return this._has_applicationHierarchyContainerType;
    } //-- boolean hasApplicationHierarchyContainerType() 

    /**
     * Method hasAuditLogType
     */
    public boolean hasAuditLogType()
    {
        return this._has_auditLogType;
    } //-- boolean hasAuditLogType() 

    /**
     * Method hasEventType
     */
    public boolean hasEventType()
    {
        return this._has_eventType;
    } //-- boolean hasEventType() 

    /**
     * Method hasHostAgentType
     */
    public boolean hasHostAgentType()
    {
        return this._has_hostAgentType;
    } //-- boolean hasHostAgentType() 

    /**
     * Method hasInstrumentationType
     */
    public boolean hasInstrumentationType()
    {
        return this._has_instrumentationType;
    } //-- boolean hasInstrumentationType() 

    /**
     * Method hasMgmtPolicyType
     */
    public boolean hasMgmtPolicyType()
    {
        return this._has_mgmtPolicyType;
    } //-- boolean hasMgmtPolicyType() 

    /**
     * Method hasPhysicalHierarchyContainerType
     */
    public boolean hasPhysicalHierarchyContainerType()
    {
        return this._has_physicalHierarchyContainerType;
    } //-- boolean hasPhysicalHierarchyContainerType() 

    /**
     * Method hasSecurityRoleType
     */
    public boolean hasSecurityRoleType()
    {
        return this._has_securityRoleType;
    } //-- boolean hasSecurityRoleType() 

    /**
     * Method hasSolutionHierarchyContainerType
     */
    public boolean hasSolutionHierarchyContainerType()
    {
        return this._has_solutionHierarchyContainerType;
    } //-- boolean hasSolutionHierarchyContainerType() 

    /**
     * Method hasSolutionType
     */
    public boolean hasSolutionType()
    {
        return this._has_solutionType;
    } //-- boolean hasSolutionType() 

    /**
     * Method hasUserAccountType
     */
    public boolean hasUserAccountType()
    {
        return this._has_userAccountType;
    } //-- boolean hasUserAccountType() 

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
     * Sets the value of field 'appInstanceType'.
     * 
     * @param appInstanceType the value of field 'appInstanceType'.
     */
    public void setAppInstanceType(int appInstanceType)
    {
        this._appInstanceType = appInstanceType;
        this._has_appInstanceType = true;
    } //-- void setAppInstanceType(int) 

    /**
     * Sets the value of field 'appTypeType'.
     * 
     * @param appTypeType the value of field 'appTypeType'.
     */
    public void setAppTypeType(int appTypeType)
    {
        this._appTypeType = appTypeType;
        this._has_appTypeType = true;
    } //-- void setAppTypeType(int) 

    /**
     * Sets the value of field 'applicationHierarchyContainerType'.
     * 
     * @param applicationHierarchyContainerType the value of field
     * 'applicationHierarchyContainerType'.
     */
    public void setApplicationHierarchyContainerType(int applicationHierarchyContainerType)
    {
        this._applicationHierarchyContainerType = applicationHierarchyContainerType;
        this._has_applicationHierarchyContainerType = true;
    } //-- void setApplicationHierarchyContainerType(int) 

    /**
     * Sets the value of field 'auditLogType'.
     * 
     * @param auditLogType the value of field 'auditLogType'.
     */
    public void setAuditLogType(int auditLogType)
    {
        this._auditLogType = auditLogType;
        this._has_auditLogType = true;
    } //-- void setAuditLogType(int) 

    /**
     * Sets the value of field 'eventType'.
     * 
     * @param eventType the value of field 'eventType'.
     */
    public void setEventType(int eventType)
    {
        this._eventType = eventType;
        this._has_eventType = true;
    } //-- void setEventType(int) 

    /**
     * Sets the value of field 'hostAgentType'.
     * 
     * @param hostAgentType the value of field 'hostAgentType'.
     */
    public void setHostAgentType(int hostAgentType)
    {
        this._hostAgentType = hostAgentType;
        this._has_hostAgentType = true;
    } //-- void setHostAgentType(int) 

    /**
     * Sets the value of field 'instrumentationType'.
     * 
     * @param instrumentationType the value of field
     * 'instrumentationType'.
     */
    public void setInstrumentationType(int instrumentationType)
    {
        this._instrumentationType = instrumentationType;
        this._has_instrumentationType = true;
    } //-- void setInstrumentationType(int) 

    /**
     * Sets the value of field 'mgmtPolicyType'.
     * 
     * @param mgmtPolicyType the value of field 'mgmtPolicyType'.
     */
    public void setMgmtPolicyType(int mgmtPolicyType)
    {
        this._mgmtPolicyType = mgmtPolicyType;
        this._has_mgmtPolicyType = true;
    } //-- void setMgmtPolicyType(int) 

    /**
     * Sets the value of field 'physicalHierarchyContainerType'.
     * 
     * @param physicalHierarchyContainerType the value of field
     * 'physicalHierarchyContainerType'.
     */
    public void setPhysicalHierarchyContainerType(int physicalHierarchyContainerType)
    {
        this._physicalHierarchyContainerType = physicalHierarchyContainerType;
        this._has_physicalHierarchyContainerType = true;
    } //-- void setPhysicalHierarchyContainerType(int) 

    /**
     * Sets the value of field 'securityRoleType'.
     * 
     * @param securityRoleType the value of field 'securityRoleType'
     */
    public void setSecurityRoleType(int securityRoleType)
    {
        this._securityRoleType = securityRoleType;
        this._has_securityRoleType = true;
    } //-- void setSecurityRoleType(int) 

    /**
     * Sets the value of field 'solutionHierarchyContainerType'.
     * 
     * @param solutionHierarchyContainerType the value of field
     * 'solutionHierarchyContainerType'.
     */
    public void setSolutionHierarchyContainerType(int solutionHierarchyContainerType)
    {
        this._solutionHierarchyContainerType = solutionHierarchyContainerType;
        this._has_solutionHierarchyContainerType = true;
    } //-- void setSolutionHierarchyContainerType(int) 

    /**
     * Sets the value of field 'solutionType'.
     * 
     * @param solutionType the value of field 'solutionType'.
     */
    public void setSolutionType(int solutionType)
    {
        this._solutionType = solutionType;
        this._has_solutionType = true;
    } //-- void setSolutionType(int) 

    /**
     * Sets the value of field 'userAccountType'.
     * 
     * @param userAccountType the value of field 'userAccountType'.
     */
    public void setUserAccountType(int userAccountType)
    {
        this._userAccountType = userAccountType;
        this._has_userAccountType = true;
    } //-- void setUserAccountType(int) 

    /**
     * Method unmarshalObjectType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalObjectType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.ObjectType) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.ObjectType.class, reader);
    } //-- java.lang.Object unmarshalObjectType(java.io.Reader) 

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

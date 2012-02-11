package com.cisco.eManager.eManager.event;

import java.util.Date;

import com.cisco.eManager.common.event.AbstractEventMessage;
import com.cisco.eManager.common.event.EventType;
import com.cisco.eManager.common.event.EventSeverity;
import com.cisco.eManager.common.event.TibcoEventDetails;
import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.common.inventory.ManagedObjectId;

import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicyManager;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager;
import com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation;
import com.cisco.eManager.eManager.inventory.instrumentation.InstrumentationManager;

import com.cisco.eManager.eManager.network.MicroagentId;
import com.cisco.eManager.eManager.network.ManagementPolicyId;

public class TibcoEventMessage extends AbstractEventMessage implements Comparable
{
    private long tibcoEventId;
    private String ruleText;
    private String ruleTestText;
    private MicroagentId microagentId;
    private ManagementPolicyId managementPolicyId;
    private ManagedObjectId    managementPolicyManagedObjectId;
    private ManagedObjectId    managedObjectId;

    public TibcoEventMessage (TibcoEventMessage object)
    {
        super (object);

        tibcoEventId = object.tibcoEventId;
        ruleText = object.ruleText;
        ruleTestText = object.ruleTestText;
        microagentId = object.microagentId;
        managementPolicyId = object.managementPolicyId;
        managementPolicyManagedObjectId = object.managementPolicyManagedObjectId;
        managedObjectId = object.managedObjectId;
    }

    /**
     * @param eventType
     * @param eventTime
     * @param eventSeverity
     * @param tibcoEventId
     * @param managementPolicyId
     * @param microagentId
     * @param rule
     * @param test
     * @roseuid 3F4E5FBF0289
     */
    public TibcoEventMessage(EventType eventType,
                             Date eventTime,
                             EventSeverity eventSeverity,
                             long tibcoEventId,
                             ManagementPolicyId managementPolicyId,
                             MicroagentId microagentId,
                             String ruleText,
                             String ruleTestText,
                             String displayText)
    {
        super(eventType, eventTime, eventSeverity, displayText);
        this.ruleText = ruleText;
        this.tibcoEventId = tibcoEventId;
        this.ruleTestText = ruleTestText;
        this.microagentId = microagentId;
        this.managementPolicyId = managementPolicyId;
        this.managementPolicyManagedObjectId = null;
        this.managedObjectId = null;
    }

    /**
     * @param eventType
     * @param eventTime
     * @param eventSeverity
     * @param tibcoEventId
     * @param managementPolicyId
     * @param microagentId
     * @param rule
     * @param test
     * @roseuid 3F4E5FBF0289
     */
    public TibcoEventMessage(EventType eventType,
                             Date eventTime,
                             EventSeverity eventSeverity,
                             int tibcoEventId,
                             ManagementPolicyId managementPolicyId,
                             ManagedObjectId managedObjectId,
                             ManagedObjectId managementPolicyManagedObjectId,
                             MicroagentId microagentId,
                             String rule,
                             String test,
                             String displayText)
    {
        super(eventType, eventTime, eventSeverity, displayText);
        this.ruleText = ruleText;
        this.tibcoEventId = tibcoEventId;
        this.ruleTestText = ruleTestText;
        this.microagentId = microagentId;
        this.managementPolicyId = managementPolicyId;
        this.managementPolicyManagedObjectId = managementPolicyManagedObjectId;
	this.managedObjectId = managedObjectId;
    }

    public TibcoEventMessage (TibcoEventDetails tibcoEventDetails) throws EmanagerEventException
    {
        super(EventType.PostType,
              tibcoEventDetails.getPostTime(),
              tibcoEventDetails.getSeverity(),
              tibcoEventDetails.getDisplayText());

        AppInstance appInstance;
        MgmtPolicy mgmtPolicy;

        mgmtPolicy = MgmtPolicyManager.instance().find(tibcoEventDetails.getManagementPolicyId());
        if (mgmtPolicy == null) {
            EmanagerEventException exception;
            EmanagerEventStatusCode statusCode;

            statusCode = EmanagerEventStatusCode.ManagementPolicyNotFound;
            exception =
                new EmanagerEventException (statusCode,
                                            statusCode.getStatusCodeDescription());
            throw exception;
        }

        appInstance = AppInstanceManager.instance().find(tibcoEventDetails.getManagedObjectId());
        if (appInstance == null) {
            EmanagerEventException exception;
            EmanagerEventStatusCode statusCode;

            statusCode = EmanagerEventStatusCode.AssociatedManagementObjectNotFound;
            exception =
                new EmanagerEventException (statusCode,
                                            statusCode.getStatusCodeDescription());
            throw exception;
        }

	if (tibcoEventDetails.getInstrumentationName() != null) {
	    Instrumentation instrumentation;
	    instrumentation = InstrumentationManager.instance().find (appInstance.id(), tibcoEventDetails.getInstrumentationName());
	    if (instrumentation != null) {
		this.microagentId = instrumentation.networkId();
	    }
	}
        this.ruleText = tibcoEventDetails.getRuleText();
        this.ruleTestText = tibcoEventDetails.getRuleTestText();
        this.tibcoEventId = tibcoEventDetails.getTibcoEventId();
        this.managementPolicyManagedObjectId = tibcoEventDetails.getManagementPolicyId();
        this.managedObjectId = tibcoEventDetails.getManagedObjectId();
        this.managementPolicyId = mgmtPolicy.networkId();
    }

    /**
     * Access method for the ruleText property.
     *
     * @return   the current value of the ruleText property
     */
    public String getRuleText()
    {
        return ruleText;
    }

    /**
     * Access method for the tibcoEventId property.
     *
     * @return   the current value of the tibcoEventId property
     */
    public long getTibcoEventId()
    {
        return tibcoEventId;
    }

    /**
     * Access method for the ruleTestText property.
     *
     * @return   the current value of the ruleTestText property
     */
    public String getRuleTestText()
    {
        return ruleTestText;
    }

    /**
     * Access method for the microagentId property.
     *
     * @return   the current value of the microagentId property
     */
    public MicroagentId getMicroagentId()
    {
        return microagentId;
    }

    /**
     * Sets the value of the microagentId property.
     *
     * @param aMicroagentId the new value of the microagentId property
     */
    public void setMicroagentId(MicroagentId aMicroagentId)
    {
        microagentId = aMicroagentId;
    }

    /**
     * Access method for the managementPolicyId property.
     *
     * @return   the current value of the managementPolicyId property
     */
    public ManagementPolicyId getManagementPolicyId()
    {
        return managementPolicyId;
    }

    public ManagedObjectId getManagedObjectId()
    {
        return managedObjectId;
    }

    public void setManagedObjectId(ManagedObjectId managedObjectId)
    {
        this.managedObjectId = managedObjectId;
    }

    public ManagedObjectId getManagementPolicyManagedObjectId()
    {
        return managementPolicyManagedObjectId;
    }

    public void setManagementPolicyManagedObjectId(ManagedObjectId managementPolicyManagedObjectId)
    {
        this.managementPolicyManagedObjectId = managementPolicyManagedObjectId;
    }

    public int compareTo (Object object)
    {
	int value;

	value = super.compareTo (object);
	if (value == 0) {
	    if (object instanceof TibcoEventMessage) {
		TibcoEventMessage tem;

		tem = (TibcoEventMessage) object;
		if (managementPolicyId != null &&
		    tem.managementPolicyId != null) {
		    value = managementPolicyId.agentId().name().compareTo (tem.managementPolicyId.agentId().name());
		    if (value == 0) {
			value = managementPolicyId.name().compareTo (tem.managementPolicyId.name());
			if (value == 0) {
			    if (tibcoEventId > tem.tibcoEventId) {
				value = 1;
			    } else if (tibcoEventId < tem.tibcoEventId) {
				value = -1;
			    } else {
				value = 0;
			    }
			}
		    }
		}
	    } else {
		value = 1;
	    }
	}

	return value;
    }

    public boolean equals (Object object)
    {
        if (object instanceof TibcoEventMessage) {
            if (super.equals(object) == true) {
                TibcoEventMessage tObject;

                tObject = (TibcoEventMessage) object;
                if (tibcoEventId == tObject.tibcoEventId &&
                    ruleText.equals(tObject.ruleText) &&
                    ruleTestText.equals(tObject.ruleTestText) &&
                    microagentId.equals(tObject.microagentId) &&
                    managementPolicyId.equals(tObject.managementPolicyId) &&
                    managedObjectId.equals(tObject.managedObjectId)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String toString()
    {
        StringBuffer strBuf;

        strBuf = new StringBuffer();

        strBuf.append ("Type:" + getEventType() + " ");
        strBuf.append ("Severity:" + getSeverity() + " ");
        strBuf.append ("Time:" + getEventTime() + " ");
        strBuf.append ("TibcoId:" + tibcoEventId + " ");
        strBuf.append ("MicroAgentId:" + microagentId + " ");
        strBuf.append ("MgmtObjectId:" + managedObjectId + " ");
        strBuf.append ("MgmtPolicyId:" + managementPolicyManagedObjectId + " ");
        strBuf.append ("MgmtPolicyNetId:" + managementPolicyId + " ");
        strBuf.append ("RuleText:" + ruleText + " ");
        strBuf.append ("TestText:" + ruleTestText + " ");
        strBuf.append ("DisplayText:" + getDisplayText());

        return strBuf.toString();
    }
}

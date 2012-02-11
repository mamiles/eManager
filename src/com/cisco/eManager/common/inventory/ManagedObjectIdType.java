package com.cisco.eManager.common.inventory;

import com.cisco.eManager.common.event2.ObjectTypeType;

public class ManagedObjectIdType
{
    private int idType;

    public static final ManagedObjectIdType HostAgent = new ManagedObjectIdType (1);
    public static final ManagedObjectIdType ApplicationType = new ManagedObjectIdType (2);
    public static final ManagedObjectIdType ApplicationInstance = new ManagedObjectIdType (3);
    public static final ManagedObjectIdType PhysicalHierarchyContainer = new ManagedObjectIdType (4);
    public static final ManagedObjectIdType ApplicationHierarchyContainer = new ManagedObjectIdType (5);
    public static final ManagedObjectIdType MgmtPolicy = new ManagedObjectIdType (6);
    public static final ManagedObjectIdType Instrumentation = new ManagedObjectIdType (7);
    public static final ManagedObjectIdType UserAccount = new ManagedObjectIdType (8);
    public static final ManagedObjectIdType Event = new ManagedObjectIdType (9);
    public static final ManagedObjectIdType AuditLog = new ManagedObjectIdType (10);
    public static final ManagedObjectIdType SecurityRole = new ManagedObjectIdType (11);
    public static final ManagedObjectIdType Solution = new ManagedObjectIdType (12);
    public static final ManagedObjectIdType SolutionHierarchyContainer = new ManagedObjectIdType (13);

    // fix
    // fix. remove this.  here for soap debug only
    public ManagedObjectIdType ()
    {
	idType = ManagedObjectIdType.HostAgent.intValue();
    }

    /**
     * @param eventSeverity
     * @roseuid 3F4EDA0A01E9
     */
    private ManagedObjectIdType(int idType)
    {
	this.idType = idType;
    }

    public static ManagedObjectIdType getManagedObjectIdType (ObjectTypeType type)
    {
	if (type.hasHostAgent()) {
	    return ManagedObjectIdType.HostAgent;
	} else if (type.hasApplicationType()) {
	    return ManagedObjectIdType.ApplicationType;
	} else if (type.hasApplicationInstance()) {
	    return ManagedObjectIdType.ApplicationInstance;
	} else if (type.hasPhysicalHierarchyContainer()) {
	    return ManagedObjectIdType.PhysicalHierarchyContainer;
	} else if (type.hasMgmtPolicy()) {
	    return ManagedObjectIdType.MgmtPolicy;
	} else if (type.hasInstrumentation()) {
	    return ManagedObjectIdType.Instrumentation;
	} else if (type.hasEvent()) {
	    return ManagedObjectIdType.Event;
	} else if (type.hasUserAccount()) {
	    return ManagedObjectIdType.UserAccount;
	} else if (type.hasSecurityRole()) {
	    return ManagedObjectIdType.SecurityRole;
	} else if (type.hasSolution()) {
	    return ManagedObjectIdType.Solution;
	} else if (type.hasSolutionHierarchyContainer()) {
	    return ManagedObjectIdType.SolutionHierarchyContainer;
	}

	return ManagedObjectIdType.AuditLog;
    }

    /**
     * Access method for the eventSeverity property.
     *
     * @return   the current value of the eventSeverity property
     */
    public int intValue()
    {
        return idType;
    }

    public boolean equals (Object object)
    {
        if (object instanceof ManagedObjectIdType) {
            if ( ( (ManagedObjectIdType) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        if (idType == ManagedObjectIdType.ApplicationHierarchyContainer.intValue()) {
            return "ApplicationHierarchyContainer";
        } else if (idType == ManagedObjectIdType.ApplicationInstance.intValue()) {
            return "ApplicationInstance";
        } else if (idType == ManagedObjectIdType.ApplicationType.intValue()) {
            return "ApplicationType";
        } else if (idType == ManagedObjectIdType.AuditLog.intValue()) {
            return "AuditLog";
        } else if (idType == ManagedObjectIdType.Event.intValue()) {
            return "Event";
        } else if (idType == ManagedObjectIdType.HostAgent.intValue()) {
            return "HostAgent";
        } else if (idType == ManagedObjectIdType.Instrumentation.intValue()) {
            return "Instrumentation";
        } else if (idType == ManagedObjectIdType.MgmtPolicy.intValue()) {
            return "MgmtPolicy";
        } else if (idType == ManagedObjectIdType.PhysicalHierarchyContainer.intValue()) {
            return "PhysicalHierarchyContainer";
        } else if (idType == ManagedObjectIdType.UserAccount.intValue()) {
            return "UserAccount";
        } else if (idType == ManagedObjectIdType.SecurityRole.intValue()) {
            return "SecurityRole";
        } else if (idType == ManagedObjectIdType.Solution.intValue()) {
            return "Solution";
        } else if (idType == ManagedObjectIdType.SolutionHierarchyContainer.intValue()) {
            return "SolutionHierarchyContainer";
        }

        return "Unknown";
    }

    public ObjectTypeType populateEventTransportObject (ObjectTypeType object)
    {
        if (idType == ManagedObjectIdType.ApplicationHierarchyContainer.intValue()) {
	    object.setApplicationHierarchyContainer (1);
        } else if (idType == ManagedObjectIdType.ApplicationInstance.intValue()) {
            object.setApplicationInstance (1);
        } else if (idType == ManagedObjectIdType.ApplicationType.intValue()) {
            object.setApplicationType (1);
        } else if (idType == ManagedObjectIdType.AuditLog.intValue()) {
            object.setAuditLog (1);
        } else if (idType == ManagedObjectIdType.Event.intValue()) {
            object.setEvent (1);
        } else if (idType == ManagedObjectIdType.HostAgent.intValue()) {
            object.setHostAgent (1);
        } else if (idType == ManagedObjectIdType.Instrumentation.intValue()) {
            object.setInstrumentation (1);
        } else if (idType == ManagedObjectIdType.MgmtPolicy.intValue()) {
            object.setMgmtPolicy (1);
        } else if (idType == ManagedObjectIdType.PhysicalHierarchyContainer.intValue()) {
            object.setPhysicalHierarchyContainer (1);
        } else if (idType == ManagedObjectIdType.UserAccount.intValue()) {
            object.setUserAccount (1);
        } else if (idType == ManagedObjectIdType.SecurityRole.intValue()) {
            object.setSecurityRole (1);
        } else if (idType == ManagedObjectIdType.Solution.intValue()) {
            object.setSolution (1);
        } else if (idType == ManagedObjectIdType.SolutionHierarchyContainer.intValue()) {
            object.setSolutionHierarchyContainer (1);
        }

	return object;
    }

    public com.cisco.eManager.common.audit2.ObjectTypeType populateAuditTransportObject (com.cisco.eManager.common.audit2.ObjectTypeType object)
    {
        if (idType == ManagedObjectIdType.ApplicationHierarchyContainer.intValue()) {
	    object.setApplicationHierarchyContainer (1);
        } else if (idType == ManagedObjectIdType.ApplicationInstance.intValue()) {
            object.setApplicationInstance (1);
        } else if (idType == ManagedObjectIdType.ApplicationType.intValue()) {
            object.setApplicationType (1);
        } else if (idType == ManagedObjectIdType.AuditLog.intValue()) {
            object.setAuditLog (1);
        } else if (idType == ManagedObjectIdType.Event.intValue()) {
            object.setEvent (1);
        } else if (idType == ManagedObjectIdType.HostAgent.intValue()) {
            object.setHostAgent (1);
        } else if (idType == ManagedObjectIdType.Instrumentation.intValue()) {
            object.setInstrumentation (1);
        } else if (idType == ManagedObjectIdType.MgmtPolicy.intValue()) {
            object.setMgmtPolicy (1);
        } else if (idType == ManagedObjectIdType.PhysicalHierarchyContainer.intValue()) {
            object.setPhysicalHierarchyContainer (1);
        } else if (idType == ManagedObjectIdType.UserAccount.intValue()) {
            object.setUserAccount (1);
        } else if (idType == ManagedObjectIdType.SecurityRole.intValue()) {
            object.setSecurityRole (1);
        } else if (idType == ManagedObjectIdType.Solution.intValue()) {
            object.setSolution (1);
        } else if (idType == ManagedObjectIdType.SolutionHierarchyContainer.intValue()) {
            object.setSolutionHierarchyContainer (1);
        }

	return object;
    }

    public com.cisco.eManager.common.inventory2.ObjectType getInventoryTransportObject()
    {
	com.cisco.eManager.common.inventory2.ObjectType transportType;

	transportType = new com.cisco.eManager.common.inventory2.ObjectType();

        if (idType == ManagedObjectIdType.ApplicationHierarchyContainer.intValue()) {
	    transportType.setApplicationHierarchyContainerType (1);
        } else if (idType == ManagedObjectIdType.ApplicationInstance.intValue()) {
            transportType.setAppInstanceType (1);
        } else if (idType == ManagedObjectIdType.ApplicationType.intValue()) {
            transportType.setAppTypeType (1);
        } else if (idType == ManagedObjectIdType.AuditLog.intValue()) {
            transportType.setAuditLogType (1);
        } else if (idType == ManagedObjectIdType.Event.intValue()) {
            transportType.setEventType (1);
        } else if (idType == ManagedObjectIdType.HostAgent.intValue()) {
            transportType.setHostAgentType (1);
        } else if (idType == ManagedObjectIdType.Instrumentation.intValue()) {
            transportType.setInstrumentationType (1);
        } else if (idType == ManagedObjectIdType.MgmtPolicy.intValue()) {
            transportType.setMgmtPolicyType (1);
        } else if (idType == ManagedObjectIdType.PhysicalHierarchyContainer.intValue()) {
            transportType.setPhysicalHierarchyContainerType (1);
        } else if (idType == ManagedObjectIdType.UserAccount.intValue()) {
            transportType.setUserAccountType (1);
        } else if (idType == ManagedObjectIdType.SecurityRole.intValue()) {
            transportType.setSecurityRoleType (1);
        } else if (idType == ManagedObjectIdType.Solution.intValue()) {
            transportType.setSolutionType (1);
        } else if (idType == ManagedObjectIdType.SolutionHierarchyContainer.intValue()) {
            transportType.setSolutionHierarchyContainerType (1);
        }

	return transportType;
    }
}

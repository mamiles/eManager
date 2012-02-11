package com.cisco.eManager.common.audit;

import com.cisco.eManager.common.audit2.AuditActionType;

public class AuditAction
{
    private int auditAction;

    public static final AuditAction Create = new AuditAction (0);
    public static final AuditAction Read = new AuditAction (1);
    public static final AuditAction Update = new AuditAction (2);
    public static final AuditAction Delete = new AuditAction (3);

    // fix
    // fix. remove this.  here for soap debug only
    public AuditAction ()
    {
	auditAction = AuditAction.Create.intValue();
    }

    /**
     * @param eventSeverity
     */
    private AuditAction(int auditAction)
    {
	this.auditAction = auditAction;
    }

    public static AuditAction getAuditAction (AuditActionType action)
    {
	if (action.hasCreate()) {
	    return AuditAction.Create;
	} else if (action.hasRead()) {
	    return AuditAction.Read;
	} else if (action.hasUpdate()) {
	    return AuditAction.Update;
	}

	return AuditAction.Delete;
    }


    /**
     * Access method for the auditAction property.
     *
     * @return   the current value of the auditAction property
     */
    public int intValue()
    {
        return auditAction;
    }

    public boolean equals (Object object)
    {
        if (object instanceof AuditAction) {
            if ( ( (AuditAction) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        if (auditAction == AuditAction.Create.intValue()) {
            return "Create";
        } else if (auditAction == AuditAction.Read.intValue()) {
            return "Read";
        } else if (auditAction == AuditAction.Update.intValue()) {
            return "Update";
        } else if (auditAction == AuditAction.Delete.intValue()) {
            return "Delete";
        }

        return "Unknown";
    }

    public AuditActionType populateTransportObject (AuditActionType tobject)
    {
	if (this == AuditAction.Create) {
	    tobject.setCreate (1);
	} else if (this == AuditAction.Read) {
	    tobject.setRead (1);
	} else if (this == AuditAction.Update) {
	    tobject.setUpdate (1);
	} else {
	    tobject.setDelete (1);
	}

	return tobject;
    }
}

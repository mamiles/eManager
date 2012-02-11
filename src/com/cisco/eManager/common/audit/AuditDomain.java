package com.cisco.eManager.common.audit;

import com.cisco.eManager.common.audit2.AuditDomainType;

public class AuditDomain
{
    private int auditDomain;

    public static final AuditDomain Inventory = new AuditDomain (0);
    public static final AuditDomain Event = new AuditDomain (1);
    public static final AuditDomain Process = new AuditDomain (2);
    public static final AuditDomain Emanager = new AuditDomain (3);

    // fix
    // fix. remove this.  here for soap debug only
    public AuditDomain ()
    {
	auditDomain = AuditDomain.Inventory.intValue();
    }

    /**
     * @param eventSeverity
     */
    private AuditDomain(int auditDomain)
    {
	this.auditDomain = auditDomain;
    }

    public static AuditDomain getAuditDomain (AuditDomainType domain)
    {
	if (domain.hasInventory()) {
	    return AuditDomain.Inventory;
	} else if (domain.hasEvent()) {
	    return AuditDomain.Event;
	} else if (domain.hasProcess()) {
	    return AuditDomain.Process;
	}

	return AuditDomain.Emanager;
    }


    /**
     * Access method for the auditDomain property.
     *
     * @return   the current value of the auditDomain property
     */
    public int intValue()
    {
        return auditDomain;
    }

    public boolean equals (Object object)
    {
        if (object instanceof AuditDomain) {
            if ( ( (AuditDomain) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        if (auditDomain == AuditDomain.Inventory.intValue()) {
            return "Inventory";
        } else if (auditDomain == AuditDomain.Event.intValue()) {
            return "Event";
        } else if (auditDomain == AuditDomain.Process.intValue()) {
            return "Process";
        } else if (auditDomain == AuditDomain.Emanager.intValue()) {
            return "Emanager";
        }

        return "Unknown";
    }

    public AuditDomainType populateTransportObject (AuditDomainType tobject)
    {
	if (this == AuditDomain.Inventory) {
	    tobject.setInventory (1);
	} else if (this == AuditDomain.Event) {
	    tobject.setEvent (1);
	} else if (this == AuditDomain.Process) {
	    tobject.setProcess (1);
	} else {
	    tobject.setEmanager (1);
	}

	return tobject;
    }
}

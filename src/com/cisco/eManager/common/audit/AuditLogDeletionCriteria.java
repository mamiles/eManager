package com.cisco.eManager.common.audit;

import java.util.Collection;

import com.cisco.eManager.common.audit.AbstractSearchCriteria;

import com.cisco.eManager.common.audit2.AuditLogDeletionCriteriaType;

import com.cisco.eManager.common.event.EmanagerEventException;

public class AuditLogDeletionCriteria extends AbstractSearchCriteria
{
    /**
     */
    public AuditLogDeletionCriteria()
    {

    }

    public AuditLogDeletionCriteria (NumericSearchCriteria auditLogId,
				     Collection domain,
				     Collection action,
				     StringSearchCriteria subject,
				     DateSearchCriteria time,
				     StringSearchCriteria userId)
    {
	super (auditLogId, domain, action, subject, time, userId);
    }

    public AuditLogDeletionCriteria (AuditLogDeletionCriteriaType transportSearchCriteria) throws EmanagerEventException
    {
	super (transportSearchCriteria);
    }

    public String toString()
    {
	return super.toString();
    }
}

package com.cisco.eManager.common.audit;

import java.util.Collection;

import com.cisco.eManager.common.audit.AbstractSearchCriteria;

import com.cisco.eManager.common.audit2.AuditLogSearchCriteriaType;

import com.cisco.eManager.common.event.EmanagerEventException;

public class AuditLogSearchCriteria extends AbstractSearchCriteria
{
    /**
     */
    public AuditLogSearchCriteria()
    {

    }

    public AuditLogSearchCriteria (NumericSearchCriteria auditLogId,
				   Collection domain,
				   Collection action,
				   StringSearchCriteria subject,
				   DateSearchCriteria time,
				   StringSearchCriteria userId)
    {
	super (auditLogId, domain, action, subject, time, userId);
    }

    public AuditLogSearchCriteria (AuditLogSearchCriteriaType transportSearchCriteria) throws EmanagerEventException
    {
	super (transportSearchCriteria);
    }
}

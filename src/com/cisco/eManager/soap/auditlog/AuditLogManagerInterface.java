package com.cisco.eManager.soap.auditlog;

public interface AuditLogManagerInterface
{


    public String xmlMessage(String sessionID, String xmlString);

	/*
    public AuditLogEntry addAuditLogEntry (AuditDomain domain,
					   AuditAction action,
					   String subject,
					   Date time,
					   String userId,
					   String description) throws EmanagerAuditException, 
								      EmanagerDatabaseException;

    public Collection retrieveAuditLogEntries(AuditLogSearchCriteria criteria) throws EmanagerDatabaseException;

    public void deleteAuditLogEntries(AuditLogDeletionCriteria criteria) throws EmanagerDatabaseException;
    */

}

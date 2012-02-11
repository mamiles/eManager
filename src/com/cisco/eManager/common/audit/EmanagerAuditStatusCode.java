package com.cisco.eManager.common.audit;

import com.cisco.eManager.common.EmanagerStatusCode;

public class EmanagerAuditStatusCode extends EmanagerStatusCode
{
    public static final EmanagerAuditStatusCode TibrvMessageCreationError =
        new EmanagerAuditStatusCode(1, "Error creating Tibrv message.");
    public static final EmanagerAuditStatusCode IllegalNumericSearchCriteria =
        new EmanagerAuditStatusCode(2, "Illegal NumericSearchCriteria values.");
    public static final EmanagerAuditStatusCode IllegalDateSearchCriteria =
        new EmanagerAuditStatusCode(3, "Illegal DateSearchCriteria values.");
    public static final EmanagerAuditStatusCode IllegalAcknowledgementSearchCriteria =
        new EmanagerAuditStatusCode(4, "Illegal AcknowledgementSearchCriteria values.");
    public static final EmanagerAuditStatusCode MalformedAuditObjectId =
        new EmanagerAuditStatusCode(5, "Malformed audit object id:");
    public static final EmanagerAuditStatusCode UnknownEventMessageType =
        new EmanagerAuditStatusCode(6, "Unknown event message type:");
    // public static final EmanagerAuditStatusCode MalformedDate =
    //  new EmanagerAuditStatusCode(7, "Malformed date encountered:");
    // public static final EmanagerAuditStatusCode MalformedUserName =
    // new EmanagerAuditStatusCode(8, "Malformed user name:");
    public static final EmanagerAuditStatusCode MalformedDomain =
        new EmanagerAuditStatusCode(7, "Malformed audit domain:");
    public static final EmanagerAuditStatusCode MalformedAction =
        new EmanagerAuditStatusCode(8, "Malformed audit action:");
    public static final EmanagerAuditStatusCode MalformedSubject =
        new EmanagerAuditStatusCode(9, "Malformed audit subject:");
    public static final EmanagerAuditStatusCode MalformedTime =
        new EmanagerAuditStatusCode(10, "Malformed audit time:");
    public static final EmanagerAuditStatusCode MalformedUserId =
        new EmanagerAuditStatusCode(11, "Malformed audit user id:");
    public static final EmanagerAuditStatusCode MalformedDescription =
        new EmanagerAuditStatusCode(12, "Malformed audit description:");

    // fix
    // this is for SOAP.
    public EmanagerAuditStatusCode()
    {
    }

    private EmanagerAuditStatusCode(int    statusCode,
                                    String statusCodeString)
    {
	super (statusCode, statusCodeString);
    }

    public boolean equals (Object object)
    {
        if (object instanceof EmanagerAuditStatusCode) {
            if ( ( (EmanagerAuditStatusCode) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }
}

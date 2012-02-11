package com.cisco.eManager.common.event;

import com.cisco.eManager.common.EmanagerStatusCode;

public class EmanagerEventStatusCode extends EmanagerStatusCode
{
    public static final EmanagerEventStatusCode ManagementPolicyNotFound =
        new EmanagerEventStatusCode(1, "The management policy was not found.");
    public static final EmanagerEventStatusCode AssociatedManagementObjectNotFound =
        new EmanagerEventStatusCode(2, "The associated managed object not found.");
    public static final EmanagerEventStatusCode TibrvMessageCreationError =
        new EmanagerEventStatusCode(3, "Error creating Tibrv message.");
    public static final EmanagerEventStatusCode UnableToFindHostEventMessageQueue =
        new EmanagerEventStatusCode(4, "Unexpectedly unable to find HostEventMessageQueue.");
    public static final EmanagerEventStatusCode UnableToFindHost =
        new EmanagerEventStatusCode(5, "Unexpectedly unable to find Host.");
    public static final EmanagerEventStatusCode UnableToStopHostEventMessageQueue =
        new EmanagerEventStatusCode(6, "Unable to stop HostEventMessageQueue.");
    public static final EmanagerEventStatusCode UnableToFindSystemApplicationInstance =
        new EmanagerEventStatusCode(7, "Unable to find the <System> ApplicationInstance.");
    public static final EmanagerEventStatusCode UnableToFindSystemRulebaseMicroagent =
        new EmanagerEventStatusCode(8, "Unable to find the <System> RuleBase Microagent.");
    public static final EmanagerEventStatusCode UnableToFindRulebaseMicroagentRulebaseLoadMethod =
        new EmanagerEventStatusCode(8, "Unable to find the RuleBase Microagent load method.");
    public static final EmanagerEventStatusCode UnableToFindLoadManagementPolicy =
        new EmanagerEventStatusCode(8, "Unable to load the management policy into the agent.");
    public static final EmanagerEventStatusCode UnableToStartSyncProcessingWorker =
        new EmanagerEventStatusCode(9, "Unable to start SyncProcessingWorker.");
    public static final EmanagerEventStatusCode UnableToStartEventProcessingWorker =
        new EmanagerEventStatusCode(10, "Unable to start EventProcessingWorker.");
    public static final EmanagerEventStatusCode IllegalNumericSearchCriteria =
        new EmanagerEventStatusCode(11, "Illegal NumericSearchCriteria values.");
    public static final EmanagerEventStatusCode IllegalDateSearchCriteria =
        new EmanagerEventStatusCode(12, "Illegal DateSearchCriteria values.");
    public static final EmanagerEventStatusCode IllegalAcknowledgementSearchCriteria =
        new EmanagerEventStatusCode(13, "Illegal AcknowledgementSearchCriteria values.");
    public static final EmanagerEventStatusCode EventSynchronizationFailure =
        new EmanagerEventStatusCode(14, "Event synchronization failed.");
    public static final EmanagerEventStatusCode MalformedEventObjectId =
        new EmanagerEventStatusCode(15, "Malformed event object id:");
    public static final EmanagerEventStatusCode UnknownEventMessageType =
        new EmanagerEventStatusCode(16, "Unknown event message type:");
    public static final EmanagerEventStatusCode MalformedUserObjectId =
        new EmanagerEventStatusCode(17, "Malformed user object id:");
    public static final EmanagerEventStatusCode UnableToFindEvent =
        new EmanagerEventStatusCode(18, "Unable to find event:");
    public static final EmanagerEventStatusCode EventAlreadyAcknowledged =
        new EmanagerEventStatusCode(19, "The event has already been acknowledged.");
    public static final EmanagerEventStatusCode MalformedDate =
        new EmanagerEventStatusCode(20, "Malformed date encountered:");
    public static final EmanagerEventStatusCode MalformedUserName =
        new EmanagerEventStatusCode(21, "Malformed user name:");
    public static final EmanagerEventStatusCode UnableToFindSystemApplicationType =
        new EmanagerEventStatusCode(22, "Unable to find the <System> ApplicationType.");

    // fix
    // this is for SOAP.
    public EmanagerEventStatusCode()
    {
    }

    private EmanagerEventStatusCode(int    statusCode,
                                    String statusCodeString)
    {
	super (statusCode, statusCodeString);
    }

    public boolean equals (Object object)
    {
        if (object instanceof EmanagerEventStatusCode) {
            if ( ( (EmanagerEventStatusCode) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }
}

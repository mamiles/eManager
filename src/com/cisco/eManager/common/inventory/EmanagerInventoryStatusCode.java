package com.cisco.eManager.common.inventory;

import com.cisco.eManager.common.EmanagerStatusCode;

public class EmanagerInventoryStatusCode
    extends EmanagerStatusCode
{
    public static final EmanagerInventoryStatusCode ManagementPolicyNotFound =
        new EmanagerInventoryStatusCode(
            1,
            "The management policy was not found.");

    public static final EmanagerInventoryStatusCode
        AssociatedManagementObjectNotFound =
            new EmanagerInventoryStatusCode(
                2,
                "The associated managed object not found.");

    public static final EmanagerInventoryStatusCode TibrvMessageCreationError =
        new EmanagerInventoryStatusCode(3, "Error creating Tibrv message.");

    public static final EmanagerInventoryStatusCode UnableToFindHost =
        new EmanagerInventoryStatusCode(4, "Unexpectedly unable to find Host.");

    public static final EmanagerInventoryStatusCode
        UnableToFindSystemApplicationInstance =
            new EmanagerInventoryStatusCode(
                5,
                "Unable to <System> ApplicationInstance.");

    public static final EmanagerInventoryStatusCode
        UnableToFindSystemRulebaseMicroagent =
            new EmanagerInventoryStatusCode(
                6,
                "Unable to <System> RuleBase Microagent.");

    public static final EmanagerInventoryStatusCode
        UnableToFindRulebaseMicroagentRulebaseLoadMethod =
            new EmanagerInventoryStatusCode(
                7,
                "Unable to find the RuleBase Microagent load method.");

    public static final EmanagerInventoryStatusCode
        UnableToFindLoadManagementPolicy =
            new EmanagerInventoryStatusCode(
                8,
                "Unable to load the management policy into the agent.");

    public static final EmanagerInventoryStatusCode
        InvalidLogLevel =
            new EmanagerInventoryStatusCode(9,"Invalid log level.");

    public static final EmanagerInventoryStatusCode
        MethodNotImplemented =
            new EmanagerInventoryStatusCode(10,"Method not implemented.");

    public static final EmanagerInventoryStatusCode
        InvalidManagedObjectIdNoObjectKey =
            new EmanagerInventoryStatusCode(11,"Invalid ManagedObjectId construction.  No key.");

    // fix
    // this is for SOAP.
    public EmanagerInventoryStatusCode()
    {
    }

    private EmanagerInventoryStatusCode(int statusCode, String statusCodeString)
    {
        super(statusCode, statusCodeString);
    }

    public boolean equals(Object object)
    {
        if (object instanceof EmanagerInventoryStatusCode)
        {
            if (((EmanagerInventoryStatusCode)object).intValue() ==
                this.intValue())
            {
                return true;
            }
        }
        return false;
    }
}

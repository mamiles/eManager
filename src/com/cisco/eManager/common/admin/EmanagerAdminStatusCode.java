package com.cisco.eManager.common.admin;

import com.cisco.eManager.common.EmanagerStatusCode;

public class EmanagerAdminStatusCode extends EmanagerStatusCode
{
    public static final EmanagerAdminStatusCode MalformedUserAccountName =
        new EmanagerAdminStatusCode(1, "Malformed user account name:");
    public static final EmanagerAdminStatusCode MalformedUserAccountPassword =
        new EmanagerAdminStatusCode(2, "Malformed user account password:");
    public static final EmanagerAdminStatusCode MalformedUserAccountPasswordKey =
        new EmanagerAdminStatusCode(3, "Malformed user account password key:");
    public static final EmanagerAdminStatusCode MalformedUserAccountSecurityRoleId =
        new EmanagerAdminStatusCode(4, "Malformed user account security role id:");
    public static final EmanagerAdminStatusCode MalformedSecurityRoleName =
        new EmanagerAdminStatusCode(5, "Malformed security role name:");
   

    public static final EmanagerAdminStatusCode UserIdNotFound =
        new EmanagerAdminStatusCode(6, "Invalid UserId:");
    public static final EmanagerAdminStatusCode InvalidPassword =
        new EmanagerAdminStatusCode(7, "Invalid password");
    public static final EmanagerAdminStatusCode InvalidSessionId =
        new EmanagerAdminStatusCode(8, "Invalid sessionId or session is expired:");
    public static final EmanagerAdminStatusCode UserAccountNotFound =
        new EmanagerAdminStatusCode(9, "UserAccount is not found for userId:");
    public static final EmanagerAdminStatusCode UserIdNotMatched =
        new EmanagerAdminStatusCode(10, "UserId form login session and from database are not matched");
    public static final EmanagerAdminStatusCode NotAuthorized =
        new EmanagerAdminStatusCode(11, "Action is not authorized to perform by userId: ");
    public static final EmanagerAdminStatusCode UnknownError =
        new EmanagerAdminStatusCode(100, "Unknown Exception");

    // fix
    // this is for SOAP.
    public EmanagerAdminStatusCode()
    {
    }

    private EmanagerAdminStatusCode(int    statusCode,
                                    String statusCodeString)
    {
	super (statusCode, statusCodeString);
    }

    public boolean equals (Object object)
    {
        if (object instanceof EmanagerAdminStatusCode) {
            if ( ( (EmanagerAdminStatusCode) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }
}

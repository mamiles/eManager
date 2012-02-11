package com.cisco.eManager.common.database;

import com.cisco.eManager.common.EmanagerStatusCode;

public class EmanagerDatabaseStatusCode extends EmanagerStatusCode
{
    public static final EmanagerDatabaseStatusCode RegistryUnexpectedlyRunning =
        new EmanagerDatabaseStatusCode(1, "The registry is unexpectedly not running.");
    public static final EmanagerDatabaseStatusCode UnableToStartRegistry =
        new EmanagerDatabaseStatusCode(2, "Error starting the registry.");
    public static final EmanagerDatabaseStatusCode UnableToCreateJNDIContext =
        new EmanagerDatabaseStatusCode(3, "Error creating the JNDI context.");
    public static final EmanagerDatabaseStatusCode UnableToBindJNDIContext =
        new EmanagerDatabaseStatusCode(4, "Unable to bind to the JNDI context.");
    public static final EmanagerDatabaseStatusCode DatabaseJNDILookupFailure =
        new EmanagerDatabaseStatusCode(5, "Unable to lookup JNDI context.");
    public static final EmanagerDatabaseStatusCode DatabaseConnectionFailure =
        new EmanagerDatabaseStatusCode(6, "Database connection failure.");
    public static final EmanagerDatabaseStatusCode UnableToGetPooledConnection =
        new EmanagerDatabaseStatusCode(7, "Unable to get a pooled database connection.");
    public static final EmanagerDatabaseStatusCode DatabaseSQLExceptionEncountered =
        new EmanagerDatabaseStatusCode(8, "Database SQL Error encountered.");
    public static final EmanagerDatabaseStatusCode IllegalHostContainer =
        new EmanagerDatabaseStatusCode(9, "Illegal container type applied to host.");
    public static final EmanagerDatabaseStatusCode IllegalApplicationTypeContainer =
        new EmanagerDatabaseStatusCode(10, "Illegal container type applied to application type.");
    public static final EmanagerDatabaseStatusCode IllegalRemoveEventQualifier =
        new EmanagerDatabaseStatusCode(11, "Illegal remove event qualifier.  You may not remove uncleared events.");
    public static final EmanagerDatabaseStatusCode FoundMixedObjectTypes =
        new EmanagerDatabaseStatusCode(12, "Unexpectedly encountered a mix of object types.");
    public static final EmanagerDatabaseStatusCode IllegalColumnData =
        new EmanagerDatabaseStatusCode(13, "Illegal column data encountered: ");

    // fix
    // this is for SOAP.
    public EmanagerDatabaseStatusCode()
    {
    }

    private EmanagerDatabaseStatusCode(int statusCode,
				       String statusCodeString)
    {
	super (statusCode, statusCodeString);
    }

    public boolean equals (Object object)
    {
        if (object instanceof EmanagerDatabaseStatusCode) {
            if ( ( (EmanagerDatabaseStatusCode) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }
}

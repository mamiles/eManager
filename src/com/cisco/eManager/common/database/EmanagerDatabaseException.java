package com.cisco.eManager.common.database;

import java.lang.Exception;

import com.cisco.eManager.common.EmanagerException;
import com.cisco.eManager.common.database.EmanagerDatabaseStatusCode;

public class EmanagerDatabaseException extends EmanagerException
{
    // fix
    // SOAP Workaround
    public EmanagerDatabaseException()
    {

    }

    public EmanagerDatabaseException (EmanagerDatabaseStatusCode statusCode,
				      String description)
    {
	super (description, statusCode);
    }
}

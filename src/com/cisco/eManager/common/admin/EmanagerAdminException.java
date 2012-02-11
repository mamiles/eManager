package com.cisco.eManager.common.admin;

import java.lang.Exception;

import com.cisco.eManager.common.EmanagerException;
import com.cisco.eManager.common.admin.EmanagerAdminStatusCode;

public class EmanagerAdminException extends EmanagerException
{
    // fix
    // SOAP Workaround
    public EmanagerAdminException()
    {

    }

    public EmanagerAdminException (EmanagerAdminStatusCode statusCode,
                                   String description)
    {
	super (description, statusCode);
    }
}

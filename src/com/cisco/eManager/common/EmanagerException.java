package com.cisco.eManager.common;

import java.lang.Exception;

public abstract class EmanagerException extends Exception implements java.io.Serializable
{
    private EmanagerStatusCode statusCode;

    // fix
    // SOAP Workaround
    public EmanagerException ()
    {
	statusCode = null;
    }

    public EmanagerException (String message,
			      EmanagerStatusCode statusCode)
    {
	super (message);

	this.statusCode = statusCode;
    }

    public EmanagerStatusCode getStatusCode()
    {
	return statusCode;
    }
}

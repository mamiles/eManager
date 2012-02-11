package com.cisco.eManager.common.event;

import java.lang.Exception;

import com.cisco.eManager.common.EmanagerException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

public class EmanagerEventException extends EmanagerException
{
    // fix
    // SOAP Workaround
    public EmanagerEventException()
    {

    }

    public EmanagerEventException (EmanagerEventStatusCode statusCode,
                                   String description)
    {
	super (description, statusCode);
    }
}

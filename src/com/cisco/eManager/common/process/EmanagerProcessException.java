package com.cisco.eManager.common.process;

import java.lang.Exception;

import com.cisco.eManager.common.EmanagerException;
import com.cisco.eManager.common.process.EmanagerProcessStatusCode;

public class EmanagerProcessException extends EmanagerException
{
    // fix
    // SOAP Workaround
    public EmanagerProcessException()
    {

    }

    public EmanagerProcessException (EmanagerProcessStatusCode statusCode,
                                   String description)
    {
	super (description, statusCode);
    }
}

package com.cisco.eManager.eManager.network;

import java.lang.Exception;

import com.cisco.eManager.common.EmanagerException;
import com.cisco.eManager.eManager.network.EmanagerNetworkStatusCode;

public class EmanagerNetworkException extends EmanagerException
{
    // fix
    // SOAP Workaround
    public EmanagerNetworkException()
    {

    }

    public EmanagerNetworkException (EmanagerNetworkStatusCode statusCode,
				     String description)
    {
	super (description, statusCode);
    }
}

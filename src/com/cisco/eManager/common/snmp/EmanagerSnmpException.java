package com.cisco.eManager.common.snmp;

import java.lang.Exception;

import com.cisco.eManager.common.EmanagerException;
import com.cisco.eManager.common.snmp.EmanagerSnmpStatusCode;

public class EmanagerSnmpException extends EmanagerException
{
    // fix
    // SOAP Workaround
    public EmanagerSnmpException()
    {

    }

    public EmanagerSnmpException (EmanagerSnmpStatusCode statusCode,
				  String description)
    {
	super (description, statusCode);
    }
}

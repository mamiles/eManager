package com.cisco.eManager.common.audit;

import java.lang.Exception;

import com.cisco.eManager.common.EmanagerException;
import com.cisco.eManager.common.audit.EmanagerAuditStatusCode;

public class EmanagerAuditException extends EmanagerException
{
    // fix
    // SOAP Workaround
    public EmanagerAuditException()
    {

    }

    public EmanagerAuditException (EmanagerAuditStatusCode statusCode,
                                   String description)
    {
	super (description, statusCode);
    }
}

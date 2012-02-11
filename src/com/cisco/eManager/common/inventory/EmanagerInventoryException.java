package com.cisco.eManager.common.inventory;

import java.lang.Exception;
import com.cisco.eManager.common.EmanagerException;
import com.cisco.eManager.common.inventory.EmanagerInventoryStatusCode;

public class EmanagerInventoryException extends EmanagerException
{
    // fix
    // SOAP Workaround
    public EmanagerInventoryException()
    {
    }

    public EmanagerInventoryException(EmanagerInventoryStatusCode statusCode,
                                      String description)
    {
	super (description, statusCode);
    }
}

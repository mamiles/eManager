package com.cisco.eManager.common.log;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

import java.lang.Exception;

import com.cisco.eManager.common.EmanagerException;
import com.cisco.eManager.common.log.EmanagerLogStatusCode;

public class EmanagerLogException extends EmanagerException
{
    // fix
    // SOAP Workaround
    public EmanagerLogException()
    {

    }

    public EmanagerLogException (EmanagerLogStatusCode statusCode,
                                   String description)
    {
        super (description, statusCode);
    }
}

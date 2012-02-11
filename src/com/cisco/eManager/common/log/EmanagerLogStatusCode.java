package com.cisco.eManager.common.log;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

import com.cisco.eManager.common.EmanagerStatusCode;

public class EmanagerLogStatusCode extends EmanagerStatusCode
{
    public static final EmanagerLogStatusCode TBD =
        new EmanagerLogStatusCode(1, "TBD.");


    // fix
    // this is for SOAP.
    public EmanagerLogStatusCode()
    {
    }

    private EmanagerLogStatusCode(int    statusCode,
                                    String statusCodeString)
    {
        super (statusCode, statusCodeString);
    }

    public boolean equals (Object object)
    {
        if (object instanceof EmanagerLogStatusCode) {
            if ( ( (EmanagerLogStatusCode) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }
}

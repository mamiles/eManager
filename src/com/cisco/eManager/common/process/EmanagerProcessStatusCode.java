package com.cisco.eManager.common.process;

import com.cisco.eManager.common.EmanagerStatusCode;

public class EmanagerProcessStatusCode extends EmanagerStatusCode
{
    public static final EmanagerProcessStatusCode TBD =
        new EmanagerProcessStatusCode(1, "TBD.");


    // fix
    // this is for SOAP.
    public EmanagerProcessStatusCode()
    {
    }

    private EmanagerProcessStatusCode(int    statusCode,
                                    String statusCodeString)
    {
	super (statusCode, statusCodeString);
    }

    public boolean equals (Object object)
    {
        if (object instanceof EmanagerProcessStatusCode) {
            if ( ( (EmanagerProcessStatusCode) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }
}

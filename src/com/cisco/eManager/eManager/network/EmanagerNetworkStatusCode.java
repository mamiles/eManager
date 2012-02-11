package com.cisco.eManager.eManager.network;

import com.cisco.eManager.common.EmanagerStatusCode;

public class EmanagerNetworkStatusCode extends EmanagerStatusCode
{
    public static final EmanagerNetworkStatusCode UnableToStartClientTibcoMsgProcessingWorker =
        new EmanagerNetworkStatusCode(1, "Unable to start ClientTibcoMessageProcessingWorker.");

    // fix
    // this is for SOAP.
    public EmanagerNetworkStatusCode()
    {
    }

    private EmanagerNetworkStatusCode(int    statusCode,
				      String statusCodeString)
    {
	super (statusCode, statusCodeString);
    }

    public boolean equals (Object object)
    {
        if (object instanceof EmanagerNetworkStatusCode) {
            if ( ( (EmanagerNetworkStatusCode) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }
}

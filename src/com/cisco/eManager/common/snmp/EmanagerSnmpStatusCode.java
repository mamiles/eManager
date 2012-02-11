package com.cisco.eManager.common.snmp;

import com.cisco.eManager.common.EmanagerStatusCode;

public class EmanagerSnmpStatusCode extends EmanagerStatusCode
{
    public static final EmanagerSnmpStatusCode UnableToParseTrapClientRegistrationString =
        new EmanagerSnmpStatusCode(1, "Unable to parse the trap client registration string.");
    public static final EmanagerSnmpStatusCode ExceptionCreatingSNMPContext =
        new EmanagerSnmpStatusCode(2, "Exception encountered creeatng SNMP context.");
    public static final EmanagerSnmpStatusCode ExceptionSendingSnmpTrap =
        new EmanagerSnmpStatusCode(3, "Exception sending SNMP trap.");
    public static final EmanagerSnmpStatusCode InvalidSNMPPort =
        new EmanagerSnmpStatusCode(4, "Invalid SNMP port:");
    public static final EmanagerSnmpStatusCode MalformedSNMPCommunity =
        new EmanagerSnmpStatusCode(5, "Malformed SNMP community encountered:");
    public static final EmanagerSnmpStatusCode MalformedSNMPHost =
        new EmanagerSnmpStatusCode(6, "Malformed SNMP host encountered:");
    public static final EmanagerSnmpStatusCode ExceptionCreatingTrapClient =
        new EmanagerSnmpStatusCode(7, "Exception creating the TrapClient:");

    // fix
    // this is for SOAP.
    public EmanagerSnmpStatusCode()
    {
    }

    private EmanagerSnmpStatusCode(int statusCode,
				       String statusCodeString)
    {
	super (statusCode, statusCodeString);
    }

    public boolean equals (Object object)
    {
        if (object instanceof EmanagerSnmpStatusCode) {
            if ( ( (EmanagerSnmpStatusCode) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }
}

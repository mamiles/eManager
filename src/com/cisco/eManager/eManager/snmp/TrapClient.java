package com.cisco.eManager.eManager.snmp;

import org.apache.log4j.*;

import java.io.IOException;

import uk.co.westhawk.snmp.stack.SnmpContextv2c;

import com.cisco.eManager.common.snmp.EmanagerSnmpException;
import com.cisco.eManager.common.snmp.EmanagerSnmpStatusCode;

public class TrapClient extends com.cisco.eManager.common.snmp.TrapClient
{
    private static Logger      logger = Logger.getLogger(TrapClient.class);

    private SnmpContextv2c snmpContext;

    private static String SocketType = "Standard";

    public TrapClient (String clientRegistrationString) throws EmanagerSnmpException
    {
	super (clientRegistrationString);

	try{
	    snmpContext = new SnmpContextv2c(getHost(), getPort(), SocketType);
	    snmpContext.setCommunity(getCommunity());
	    return;
	}
	catch (IOException e){
	    String logString;
	    EmanagerSnmpException ese;

	    logString =
		EmanagerSnmpStatusCode.ExceptionCreatingSNMPContext.
		getStatusCodeDescription() +
		": " +
		clientRegistrationString +
		" - " +
		e.getMessage();

	    logger.log(Priority.ERROR, logString);
	    ese = new EmanagerSnmpException(EmanagerSnmpStatusCode.ExceptionCreatingSNMPContext,
					    logString);
	    throw ese;
	}
    }

    /**
     * @param host
     * @param community
     * @param port
     * @roseuid 3F3D216F01D2
     */
    public TrapClient(String host, String community, int port) throws EmanagerSnmpException
    {
	super (host, community, port);

	try {
	    snmpContext = new SnmpContextv2c(getHost(), getPort(), SocketType);
	    snmpContext.setCommunity (getCommunity());
	}
	catch (IOException e) {
	    String logString;
	    EmanagerSnmpException ese;

	    logString =
		EmanagerSnmpStatusCode.ExceptionCreatingSNMPContext.getStatusCodeDescription() +
		": " +
		host + ":" + community + ":" + Integer.toString (port) +
		" - " +
		e.getMessage();

	    logger.log(Priority.ERROR, logString);
	    ese = new EmanagerSnmpException (EmanagerSnmpStatusCode.ExceptionCreatingSNMPContext,
					     logString);
	    throw ese;
	}
    }

    public boolean equals (Object object)
    {
        if (object instanceof TrapClient) {
            TrapClient client;

            client = (TrapClient) object;
	    return super.equals (client);
        }

        return false;
    }

    /**
     * @roseuid 3F3D220202D1
     */
    public SnmpContextv2c getSNMPContextv2c()
    {
        return snmpContext;
    }

    public String toString()
    {
        return super.toString();
    }
}

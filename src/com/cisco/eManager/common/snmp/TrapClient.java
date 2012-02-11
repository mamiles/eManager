package com.cisco.eManager.common.snmp;

import org.apache.log4j.*;

import java.util.StringTokenizer;

import java.io.IOException;

import com.cisco.eManager.common.snmp.EmanagerSnmpException;
import com.cisco.eManager.common.snmp.EmanagerSnmpStatusCode;

import com.cisco.eManager.common.event2.SnmpClient;

import com.cisco.eManager.eManager.snmp.SNMPGlobals;

public class TrapClient
{
    private static Logger      logger = Logger.getLogger(TrapClient.class);

    private String host;
    private String community;
    private int    port;

    public TrapClient (String clientRegistrationString) throws EmanagerSnmpException
    {
	String portString;
	StringTokenizer tokenizer;

	tokenizer = new StringTokenizer (clientRegistrationString, SNMPGlobals.SNMPTrapRecipientsHostPortSeparator);
	host = tokenizer.nextToken();
	if (host != null &&
            host.trim().length() != 0) {
	    community = tokenizer.nextToken();
	    if (community != null &&
                community.trim().length() != 0) {
		portString = tokenizer.nextToken();
		if (portString != null &&
                    portString.trim().length() != 0) {
		    try {
			port = Integer.valueOf (portString).intValue();
                        if (port <= 0) {
                            EmanagerSnmpException e;

                            e = new EmanagerSnmpException (EmanagerSnmpStatusCode.InvalidSNMPPort,
                                                           EmanagerSnmpStatusCode.InvalidSNMPPort.getStatusCodeDescription() +
                                                           port);
                            throw e;
                        }
		    }
		    catch (NumberFormatException e) {
                        EmanagerSnmpException ese;

                        ese = new EmanagerSnmpException (EmanagerSnmpStatusCode.InvalidSNMPPort,
                                                       EmanagerSnmpStatusCode.InvalidSNMPPort.getStatusCodeDescription() +
                                                       port);
                        throw ese;
		    }
		} else {
                    EmanagerSnmpException e;

                    e = new EmanagerSnmpException (EmanagerSnmpStatusCode.InvalidSNMPPort,
                                                   EmanagerSnmpStatusCode.InvalidSNMPPort.getStatusCodeDescription() +
                                                   portString);
                    throw e;

                }
	    } else {
                EmanagerSnmpException e;

                e = new EmanagerSnmpException (EmanagerSnmpStatusCode.MalformedSNMPCommunity,
                                               EmanagerSnmpStatusCode.MalformedSNMPCommunity.getStatusCodeDescription() +
                                               host);
                throw e;
            }
        } else {
            EmanagerSnmpException e;

            e = new EmanagerSnmpException (EmanagerSnmpStatusCode.MalformedSNMPHost,
                                           EmanagerSnmpStatusCode.MalformedSNMPHost.getStatusCodeDescription() +
                                           host);
            throw e;
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
        if (host == null ||
            host.trim().length() == 0){
            EmanagerSnmpException e;

            e = new EmanagerSnmpException(EmanagerSnmpStatusCode.MalformedSNMPHost,
                                          EmanagerSnmpStatusCode.MalformedSNMPHost.
                                          getStatusCodeDescription() +
                                          host);
            throw e;
        }

        if (community == null ||
            community.trim().length() == 0){
            EmanagerSnmpException e;

            e = new EmanagerSnmpException(EmanagerSnmpStatusCode.MalformedSNMPCommunity,
                                          EmanagerSnmpStatusCode.MalformedSNMPCommunity.
                                          getStatusCodeDescription() +
                                          host);
            throw e;
        }

        if (port < 1){
            EmanagerSnmpException e;

            e = new EmanagerSnmpException (EmanagerSnmpStatusCode.InvalidSNMPPort,
                                           EmanagerSnmpStatusCode.InvalidSNMPPort.getStatusCodeDescription() +
                                           port);
            throw e;
        }

        this.host = host;
        this.community = community;
        this.port = port;
    }

    public String getHost()
    {
        return host;
    }

    public String getCommunity()
    {
        return community;
    }

    public int getPort()
    {
        return port;
    }

    public boolean equals (Object object)
    {
        if (object instanceof TrapClient) {
            TrapClient client;

            client = (TrapClient) object;
            if (host.equals(client.getHost())           &&
                community.equals(client.getCommunity()) &&
                port == client.getPort()) {
                return true;
            }
        }

        return false;
    }

    public SnmpClient populateTransportObject (SnmpClient transportObject)
    {
	transportObject.setHost (host);
	transportObject.setCommunity (community);
	transportObject.setPort (port);

	return transportObject;
    }

    public String toString()
    {
        return host + ":" + community + ":" + Integer.toString(port);
    }
}

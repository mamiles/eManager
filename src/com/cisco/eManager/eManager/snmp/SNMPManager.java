//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\snmp\\SNMPManager.java

package com.cisco.eManager.eManager.snmp;

import org.apache.log4j.*;

import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Properties;

import java.io.IOException;

import uk.co.westhawk.snmp.pdu.OneTrapPduv2;
import uk.co.westhawk.snmp.stack.PduException;

import com.cisco.eManager.common.snmp.EmanagerSnmpException;
import com.cisco.eManager.common.snmp.EmanagerSnmpStatusCode;

import com.cisco.eManager.common.event.EmanagerEventDetails;

import com.cisco.eManager.common.event.EventType;

import com.cisco.eManager.eManager.util.GlobalProperties;

public class SNMPManager
{
    private static Logger      logger = Logger.getLogger(SNMPManager.class);

    private static SNMPManager snmpManager = null;
    private Collection trapClients;

    /**
     * @roseuid 3F3B053902A0
     */
    protected SNMPManager()
    {
	Properties properties;

	properties = GlobalProperties.instance().getProperties();
        trapClients = Collections.synchronizedCollection(new LinkedList());

	initializeTrapRecipients (properties);
    }

    /**
     * @return com.cisco.eManager.eManager.snmp.SNMPManager
     * @roseuid 3F3B051D02EF
     */
    public static SNMPManager instance()
    {
        if (snmpManager == null) {
            snmpManager = new SNMPManager();
        }

        return snmpManager;
    }

    private void initializeTrapRecipients (Properties properties)
    {
	String trapRecipientList;

	trapRecipientList = properties.getProperty (SNMPGlobals.SNMPTrapRecipientsKey);
	if (trapRecipientList != null) {
	    String clientDesignationString;
	    StringTokenizer tokenizer;
	    TrapClient trapClient;

	    tokenizer = new StringTokenizer (trapRecipientList, SNMPGlobals.SNMPTrapRecipientsDelimeter);
	    while (tokenizer.hasMoreTokens()) {
		clientDesignationString = tokenizer.nextToken();
		try {
		    trapClient = new TrapClient (clientDesignationString);
		    addTrapClient (trapClient);
                    logger.info(SNMPGlobals.SNMPTrapRecipientAddedMsg +
                                " - " +
                                clientDesignationString);
		}
		catch (EmanagerSnmpException e) {
                    logger.warn("Unable to add trap client: " +
                                clientDesignationString +
                                " - " +
                                e);
		}
	    }
	}
    }

    /**
     * @roseuid 3F3B05550390
     */
    public void addTrapClient(TrapClient client) throws EmanagerSnmpException
    {
        synchronized (trapClients) {
            if (clientIsRegistered (client) == false) {
                trapClients.add(client);
                logger.info(SNMPGlobals.SNMPTrapRecipientAddedMsg +
                            " - " +
                            client.toString());
            }
        }
    }

    /**
     * @roseuid 3F3B0567006B
     */
    public void removeTrapClient(TrapClient client)
    {
        synchronized (trapClients) {
            trapClients.remove(client);
        }
    }

    public Collection retrieveTrapClients()
    {
	Iterator iter;
	LinkedList clients;

	synchronized (trapClients) {
	    clients = new LinkedList(trapClients);
	}

	return clients;

    }

    private boolean clientIsRegistered (TrapClient client)
    {
        Iterator iter;
        TrapClient iterClient;

        synchronized (trapClients) {
            iter = trapClients.iterator();
            while (iter.hasNext()) {
                iterClient = (TrapClient) iter.next();
                if (iterClient.equals(client) == true) {
                    return true;
                }
            }
        }

        return false;
    }

    public void broadcastPostEvents (Collection events) {
	broadcastEvents (events, EventType.PostType);
    }

    public void broadcastClearEvents (Collection events) {
	broadcastEvents (events, EventType.ClearType);
    }

    private void broadcastEvents (Collection events,
				  EventType eventType)
    {
        Iterator eventIter;
        Iterator clientIter;
        EmanagerEventDetails event;
        TrapClient client;
        OneTrapPduv2 trapPdu;

        eventIter = events.iterator();
        while (eventIter.hasNext()) {
            event = (EmanagerEventDetails) eventIter.next();
            synchronized (trapClients) {
                clientIter = trapClients.iterator();
                while (clientIter.hasNext()) {
                    client = (TrapClient) clientIter.next();
		    if (eventType.equals (EventType.PostType) == true) {
			trapPdu = event.getPostTrapEventPdu (client.getSNMPContextv2c());
		    } else {
			trapPdu = event.getClearTrapEventPdu (client.getSNMPContextv2c());
		    }
                    try {
                        trapPdu.send();
                    }
                    catch (PduException e) {
                        String logString;

                        logString =
                            EmanagerSnmpStatusCode.ExceptionSendingSnmpTrap.getStatusCodeDescription() +
                            " " +
                            e.getMessage();

                        logger.log(Priority.ERROR, logString);
                    }
                    catch (IOException e) {
                        String logString;

                        logString =
                            EmanagerSnmpStatusCode.ExceptionSendingSnmpTrap.getStatusCodeDescription() +
                            " " +
                            e.getMessage();

                        logger.log(Priority.ERROR, logString);
                    }
                }
            }
        }
    }
}

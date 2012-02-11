// NAME
//      $RCSfile: UsmDiscoveryBean.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.11 $
// CREATED
//      $Date: 2002/10/22 16:34:23 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 2000, 2001, 2002 by Westhawk Ltd (www.westhawk.co.uk)
 *
 * Permission to use, copy, modify, and distribute this software
 * for any purpose and without fee is hereby granted, provided
 * that the above copyright notices appear in all copies and that
 * both the copyright notice and this permission notice appear in
 * supporting documentation.
 * This software is provided "as is" without express or implied
 * warranty.
 * author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 */

package uk.co.westhawk.snmp.beans;

import uk.co.westhawk.snmp.stack.*;
import uk.co.westhawk.snmp.pdu.*;
import java.util.*;

/**
 * <p>
 * This bean performs the SNMPv3 USM discovery process.
 * </p>
 *
 * <p>
 * The process consists of two steps: first the SNMP engine ID has to be
 * discovered, second the timeline details of the SNMP engine ID have to
 * be discovered. For the last step the username of the principal is
 * needed. 
 * </p>
 *
 * <p>
 * See <a href="http://ietf.org/rfc/rfc2574.txt">RFC 2574</a>.
 * </p>
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.11 $ $Date: 2002/10/22 16:34:23 $
 */
public class UsmDiscoveryBean 
{
    private static final String     version_id =
        "@(#)$Id: UsmDiscoveryBean.java,v 1.11 2002/10/22 16:34:23 birgit Exp $ Copyright Westhawk Ltd";

    private SnmpContextv3Pool context;
    private String userName = null; 
    private String userAuthPassword = null;
    private String userPrivPassword = null;
    private int authProtocol;

/**
 * Constructor.
 *
 * @param host The host to discover
 * @param port The port to discover
 * @see SnmpContextv3Pool#SnmpContextv3Pool(String, int)
 * @see #startDiscovery()
 */
public UsmDiscoveryBean(String host, int port) 
throws java.io.IOException
{
    this(host, port, SnmpContextBasisFace.STANDARD_SOCKET);
}

/**
 * Constructor.
 *
 * @param host The host to discover
 * @param port The port to discover
 * @param typeSocketA The type of socket to use. 
 * @see SnmpContextv3Pool#SnmpContextv3Pool(String, int, String)
 * @see #startDiscovery()
 */
public UsmDiscoveryBean(String host, int port, String typeSocketA) 
throws java.io.IOException
{
    context = new SnmpContextv3Pool(host, port, typeSocketA);
}

/**
 * Sets the user's authentication details.
 * With these details the time line details will be retrieved.
 * If the user details are not set, only the engine ID will be
 * discovered.
 * 
 * <p>
 * The time line details only need to be known when the user want to
 * send authenticated message to this SNMP engine.
 * </p>
 *
 * @param newUserName The user name
 * @param newUserPassword The user authentication password
 * @param protocol The user authentication protocol
 *
 * @see SnmpContextv3#setUserName(String)
 * @see SnmpContextv3#setUserAuthenticationPassword(String)
 * @see SnmpContextv3#setAuthenticationProtocol(int)
 */
public void setAuthenticationDetails(String newUserName, String newUserPassword,
    int protocol)
{
    userName = newUserName;
    userAuthPassword = newUserPassword;
    authProtocol = protocol;
}

/**
 * Sets the user's privacy details.
 * With these details the time line details will be retrieved if needed.
 * 
 * <p>
 * The time line details only need to be known when the user want to
 * send authenticated message to this SNMP engine.
 * </p>
 *
 * @param newUserPassword The user privacy password
 *
 * @see SnmpContextv3#setUserPrivacyPassword(String)
 */
public void setPrivacyDetails(String newUserPassword)
{
    userPrivPassword = newUserPassword;
}

/**
 * Starts the discovery. This method will send a Pdu to discover the
 * SNMP engine ID. Set the user details before calling this method, if
 * you want the time line details to be discovered as well. 
 *
 * <p>
 * This is a blocking call! It will return when it has done the whole
 * discovery.
 * </p>
 *
 * @see #setAuthenticationDetails(String, String, int)
 * @see #discoveryEngineId
 * @see #discoveryTimeLine
 */
public void startDiscovery()
throws PduException, java.io.IOException
{
    try
    {
        discoveryEngineId();
    }
    catch (PduException exc)
    {
        // You shouldn't really get an exception when doing engineID
        // discovery.
        throw new PduException("Engine ID discovery: " 
            + exc.getMessage());
    }

    try
    {
        discoveryTimeLine();
    }
    catch (PduException exc)
    {
        // You can get an exception for authPriv. I noticed that when
        // testing against MG-SOFT.
        // Only throw the exception if the timeline was not discovered.

        TimeWindow tWindow = TimeWindow.getCurrent();
        String engineId = tWindow.getSnmpEngineId(context.getHost(), 
                            context.getPort()); 
        if (tWindow.isTimeLineKnown(engineId) == false)
        {
            throw new PduException("Timeline discovery: " 
                + exc.getMessage());
        }
    }
}

/**
 * Starts the discovery of the SNMP engine ID. 
 *
 * <p>
 * This is a blocking call! It will return when it has done the whole
 * discovery.
 * </p>
 *
 * @see #startDiscovery
 * @see #discoveryTimeLine
 */
protected void discoveryEngineId()
throws PduException, java.io.IOException
{
    DiscoveryPdu pdu;

    TimeWindow tWindow = TimeWindow.getCurrent();
    String engineId = tWindow.getSnmpEngineId(context.getHost(), 
                        context.getPort()); 

    // Just check again to be sure
    if (engineId == null)
    {
        if (AsnObject.debug > 4)
        {
            System.out.println("Starting discovery Engine ID ...");
        }
        context.setUserName("");
        context.setUseAuthentication(false);
        context.setUsePrivacy(false);
        context.setContextEngineId(new byte[0]);
        context.setContextName("");

        pdu = new DiscoveryPdu(context);
        pdu.send();
        pdu.waitForSelf();
        varbind [] vars = pdu.getResponseVarbinds();
    }
}

/**
 * Starts the discovery of the Time line.
 *
 * <p>
 * This is a blocking call! It will return when it has done the whole
 * discovery.
 * </p>
 *
 * @see #startDiscovery
 * @see #discoveryEngineId
 */
protected void discoveryTimeLine()
throws PduException, java.io.IOException
{
    DiscoveryPdu pdu;

    TimeWindow tWindow = TimeWindow.getCurrent();
    String engineId = tWindow.getSnmpEngineId(context.getHost(), 
                        context.getPort()); 

    // The engineId should be known by now.
    // Only do timeline discovery if it is not known yet.
    if (tWindow.isTimeLineKnown(engineId) == false && userName != null)
    {
        if (AsnObject.debug > 4)
        {
            System.out.println("Starting discovery Timeline ...");
        }
        context.setUserName(userName);
        context.setUserAuthenticationPassword(userAuthPassword);
        context.setUseAuthentication(true);
        context.setAuthenticationProtocol(authProtocol);
        context.setContextEngineId(new byte[0]);
        context.setContextName("");

        if (userPrivPassword != null)
        {
            context.setUsePrivacy(true);
            context.setUserPrivacyPassword(userPrivPassword);
        }
        else
        {
            context.setUsePrivacy(false);
        }

        pdu = new DiscoveryPdu(context);
        pdu.send();
        pdu.waitForSelf();
        varbind [] vars = pdu.getResponseVarbinds();

        if (AsnObject.debug > 4)
        {
            System.out.println("Did discovery time line");
        }
    }
}


}

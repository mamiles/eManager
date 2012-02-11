// NAME
//      $RCSfile: SendTrap.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.3 $
// CREATED
//      $Date: 2002/11/04 17:55:09 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 2001, 2002 by Westhawk Ltd (www.westhawk.co.uk)
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
 
package uk.co.westhawk.examplev1;

import java.awt.*; 
import javax.swing.*;
import java.util.*;
import java.net.*;
import uk.co.westhawk.snmp.stack.*;    
import uk.co.westhawk.snmp.pdu.*;    

/**
 * <p>
 * The SendTrap application sends a trap using the OneTrapPduv1. 
 * </p>
 *
 * <p>
 * The host, port, oid and community name can be configured 
 * in the properties file. 
 * The name of the properties file can be passed as first argument to
 * this application. If there is no such argument, it will look for
 * <code>SendTrap.properties</code>. If this file does not exist, the
 * application will use default parameters.
 * </p>
 *
 * @see uk.co.westhawk.snmp.pdu.OneTrapPduv1
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.3 $ $Date: 2002/11/04 17:55:09 $
 */
public class SendTrap extends JComponent 
{
    private static final String     version_id =
        "@(#)$Id: SendTrap.java,v 1.3 2002/11/04 17:55:09 birgit Exp $ Copyright Westhawk Ltd";

    public final static String ENTERPRISE = "enterprise";
    public final static String SPECIFIC = "specific";
    public final static String TIMETICS = "timetics";

    public final static String sysObjectID = "1.3.6.1.2.1.1.2";
    public final static String sysUpTime   = "1.3.6.1.2.1.1.3";
    public byte [] address = {1, 1, 1, 1};

    private SnmpContext context;
    private OneTrapPduv1 pdu;
    private Util util;

/**
 * Constructor.
 *
 * @param propertiesFilename The name of the properties file. Can be
 * null.
 */
public SendTrap(String propertiesFilename)
{
    AsnObject.setDebug(6); 
    util = new Util(propertiesFilename, this.getClass().getName());
}


public void init () 
{
    String host = util.getHost();
    int port = util.getPort(DefaultTrapContext.DEFAULT_TRAP_PORT);
    String socketType = util.getSocketType();
    String oid = util.getOid(sysUpTime);
    String community = util.getCommunity();

    try
    {
        address = InetAddress.getLocalHost().getAddress();
    }
    catch (UnknownHostException exc) { }

    String enterprise = util.getProperty(ENTERPRISE, "1.1");
    int specific = util.getIntParameter(SPECIFIC, 0);
    long timeticks = util.getIntParameter(TIMETICS, 1000);

    try 
    {
        context = new SnmpContext(host, port, socketType);
        context.setCommunity(community);

        pdu = new OneTrapPduv1(context);
        pdu.setIpAddress(address);
        pdu.setEnterprise(enterprise);
        pdu.setSpecificTrap(specific);
        pdu.setTimeTicks(timeticks);

        System.out.println(pdu.toString());
        pdu.send();
    }
    catch (java.io.IOException exc)
    {
        System.out.println("IOException " + exc.getMessage());
        System.exit(0);
    }
    catch(uk.co.westhawk.snmp.stack.PduException exc)
    {
        System.out.println("PduException " + exc.getMessage());
        System.exit(0);
    }
    //System.exit(0);
}



public static void main (String [] args)
{
    String propFileName = null;
    if (args.length > 0)
    {
        propFileName = args[0];
    }
    SendTrap trap = new SendTrap(propFileName);
    trap.init();
}

}

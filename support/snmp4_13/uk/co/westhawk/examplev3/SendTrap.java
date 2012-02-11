// NAME
//      $RCSfile: SendTrap.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.4 $
// CREATED
//      $Date: 2002/11/04 17:55:13 $
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
 
package uk.co.westhawk.examplev3;

import java.awt.*; 
import java.util.*;
import java.net.*;
import uk.co.westhawk.snmp.stack.*;    
import uk.co.westhawk.snmp.pdu.*;    
import uk.co.westhawk.snmp.util.*;    

/**
 * <p>
 * The SendTrap application sends a trap using the OneTrapPduv2. 
 * </p>
 *
 * <p>
 * The host, port, oid, etc can be configured 
 * in the properties file. 
 * The value for sysUpTime and snmpTrapOID are hard coded.
 * </p>
 * <p>
 * The name of the properties file can be passed as first argument to
 * this application. If there is no such argument, it will look for
 * <code>SendTrap.properties</code>. If this file does not exist, the
 * application will use default parameters.
 * </p>
 *
 * @see uk.co.westhawk.snmp.pdu.OneTrapPduv2
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.4 $ $Date: 2002/11/04 17:55:13 $
 */
public class SendTrap 
{
    private static final String     version_id =
        "@(#)$Id: SendTrap.java,v 1.4 2002/11/04 17:55:13 birgit Exp $ Copyright Westhawk Ltd";

    public final static String sysUpTime   = "1.3.6.1.2.1.1.3.0";
    public final static String sysContact  = "1.3.6.1.2.1.1.4.0";

    /**
     * The authoritative identification of the notification currently 
     * being sent. This variable occurs as the second varbind in every 
     * SNMPv2-Trap-PDU and InformRequest-PDU.
     */
    public final static String snmpTrapOID = "1.3.6.1.6.3.1.1.4.1.0";

    public final static String coldStart              = "1.3.6.1.6.3.1.1.5.1";
    public final static String warmStart              = "1.3.6.1.6.3.1.1.5.2";
    public final static String linkDown               = "1.3.6.1.6.3.1.1.5.3";
    public final static String linkUp                 = "1.3.6.1.6.3.1.1.5.4";
    public final static String authenticationFailure  = "1.3.6.1.6.3.1.1.5.5";
    public final static String egpNeighborLoss        = "1.3.6.1.6.3.1.1.5.6";


    private SnmpContextv3 context;
    private OneTrapPduv2 pdu;
    private Util        util;


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
    byte[] engineId = util.getContextEngineId();
    String contextName = util.getContextName();
    String userName = util.getUserName();
    int auth = util.getUseAuth();
    String authPassw = util.getUserAuthPassword();
    int proto = util.getAuthProcotol();
    int priv = util.getUsePriv();
    String privPassw = util.getUserPrivPassword();

    try 
    {
        context = new SnmpContextv3(host, port, socketType);
        context.setUserName(userName);
        context.setUseAuthentication((auth==1));
        context.setUserAuthenticationPassword(authPassw);
        context.setAuthenticationProtocol(proto);
        context.setContextEngineId(engineId);
        context.setContextName(contextName);
        context.setUsePrivacy((priv == 1));
        context.setUserPrivacyPassword(privPassw);

        pdu = new OneTrapPduv2(context);
        pdu.addOid(sysUpTime, new AsnUnsInteger(5));
        pdu.addOid(snmpTrapOID, new AsnObjectId(warmStart));

        System.out.println(pdu.toString());
        pdu.send();
    }
    catch (java.io.IOException exc)
    {
        System.out.println("IOException " + exc.getMessage());
    }
    catch(uk.co.westhawk.snmp.stack.PduException exc)
    {
        System.out.println("PduException " + exc.getMessage());
    }
    //System.exit(0);
}


public static void main(String[] args)
{
    String propFileName = null;
    if (args.length > 0)
    {
        propFileName = args[0];
    }
    SendTrap application = new SendTrap(propFileName);
    application.init();
}


}

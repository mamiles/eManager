// NAME
//      $RCSfile: ReceiveTrap.java,v $
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
import uk.co.westhawk.snmp.event.*;    
import uk.co.westhawk.snmp.util.*;    

/**
 * <p>
 * The ReceiveTrap receives traps. 
 * </p>
 *
 * <p>
 * The host, port, etc can be configured 
 * in the properties file. 
 * The name of the properties file can be passed as first argument to
 * this application. If there is no such argument, it will look for
 * <code>ReceiveTrap.properties</code>. If this file does not exist, the
 * application will use default parameters.
 * </p>
 *
 * <p>
 * On UNIX and Linux you will have to run this application as root.
 * </p>
 *
 * @see uk.co.westhawk.snmp.stack.SnmpContextv3
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.4 $ $Date: 2002/11/04 17:55:13 $
 */
public class ReceiveTrap implements TrapListener, Observer
{
    private static final String     version_id =
        "@(#)$Id: ReceiveTrap.java,v 1.4 2002/11/04 17:55:13 birgit Exp $ Copyright Westhawk Ltd";

    private SnmpContextv3 context, context2;
    private Util        util;


/**
 * Constructor.
 *
 * @param propertiesFilename The name of the properties file. Can be
 * null.
 */
public ReceiveTrap(String propertiesFilename)
{
    AsnObject.setDebug(6);
    //AsnObject.setDebug(15);
    util = new Util(propertiesFilename, this.getClass().getName());
}

public void init () 
{
    String host = util.getHost();
    int port = util.getPort(SnmpContextBasisFace.DEFAULT_PORT);
    String socketType = util.getSocketType();
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
        DefaultTrapContext defTrap =
        DefaultTrapContext.getInstance(DefaultTrapContext.DEFAULT_TRAP_PORT, socketType);
        defTrap.addUnhandledTrapListener(this);

        context = new SnmpContextv3(host, port, socketType);
        context.setUserName(userName);
        context.setUseAuthentication((auth==1));
        context.setUserAuthenticationPassword(authPassw);
        context.setAuthenticationProtocol(proto);
        context.setContextEngineId(engineId);
        context.setContextName(contextName);
        context.setUsePrivacy((priv == 1));
        context.setUserPrivacyPassword(privPassw);
        context.addTrapListener(this);

        System.out.println("ReceiveTrap.init(): " 
            + context.toString());

        // send a wrong community name, this hopefully causes a
        // authenticationFailure trap. 

        context2 = (SnmpContextv3)context.clone();
        String name = context2.getContextName();
        name += "_bla"; 
        context2.setContextName(name);

        OneGetNextPdu pdu = new OneGetNextPdu(context2, 
            "1.3.6.1.2.1.1.3", this);
    }
    catch (java.io.IOException exc)
    {
        System.out.println("ReceiveTrap.init(): IOException " 
            + exc.getMessage());
        exc.printStackTrace();
        System.exit(0);
    }
    catch(CloneNotSupportedException exc) {}
    catch(PduException exc) {}
}

public void update(Observable obs, Object ov)
{
    Pdu pdu = (Pdu) obs;
    System.out.println("ReceiveTrap.update(): " + pdu.toString());
}

public void destroy() 
{ 
    if (context != null)
    {
        context.destroy();
    }
    if (context2 != null)
    {
        context2.destroy();
    }
}

public void trapReceived(TrapEvent evt)
{
    if (evt.isDecoded())
    {
        Pdu trapPdu = evt.getPdu();
        System.out.println("ReceiveTrap.trapReceived():"
            + " received decoded trap " 
            + trapPdu.toString());
    }
    else
    {
        int version = evt.getVersion();
        String host = evt.getHostAddress();
        System.out.println("ReceiveTrap.trapReceived():"
            + " received undecoded trap " 
            + SnmpUtilities.getSnmpVersionString(version)
            + " from host " + host);
    }
}


public static void main(String[] args)
{
    String propFileName = null;
    if (args.length > 0)
    {
        propFileName = args[0];
    }
    ReceiveTrap trap = new ReceiveTrap(propFileName);
    trap.init();
}

}

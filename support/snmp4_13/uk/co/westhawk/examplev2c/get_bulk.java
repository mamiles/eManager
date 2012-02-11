// NAME
//      $RCSfile: get_bulk.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.2 $
// CREATED
//      $Date: 2002/10/22 16:39:18 $
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
 
package uk.co.westhawk.examplev2c;

import java.awt.Graphics;
import java.awt.Event;
import java.util.*;
import java.net.*;
import uk.co.westhawk.snmp.stack.*;    
import uk.co.westhawk.snmp.pdu.*;    

/**
 * <p>
 * The get_bulk application shows how to use the SNMPv2 GetBulk request.
 * </p>
 *
 * <p>
 * The host, port, community, non-repeaters and max-repetitions name can 
 * be configured 
 * in the properties file. 
 * A number of OIDs can be configured in the properties file as well by using
 * any OIDS in the range OID0 to OID9.
 * </p>
 * <p>
 * The name of the properties file can be passed as first argument to
 * this application. If there is no such argument, it will look for
 * <code>get_bulk.properties</code>. If this file does not exist, the
 * application will use default parameters.
 * </p>
 *
 * @see uk.co.westhawk.snmp.pdu.OneGetBulkPdu
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.2 $ $Date: 2002/10/22 16:39:18 $
 */
public class get_bulk implements Observer
{
    private static final String     version_id =
        "@(#)$Id: get_bulk.java,v 1.2 2002/10/22 16:39:18 birgit Exp $ Copyright Westhawk Ltd";

    public final static String NON = "non";
    public final static String MAX = "max";

    public final static String system        = "1.3.6.1.2.1.1";
    public final static String sysUpTime     = "1.3.6.1.2.1.1.3";
    public final static String ipNetToMediaNetAddress = "1.3.6.1.2.1.4.22.1.3";
    public final static String ipNetToMediaType = "1.3.6.1.2.1.4.22.1.4";
    public final static String ipRouteTable = "1.3.6.1.2.1.4.21.1";

    private SnmpContextv2c context;
    private OneGetBulkPdu pdu;
    private Util        util;


/**
 * Constructor.
 *
 * @param propertiesFilename The name of the properties file. Can be
 * null.
 */
public get_bulk(String propertiesFilename)
{
    util = new Util(propertiesFilename, this.getClass().getName());
}

public void init () 
{
    //AsnObject.setDebug(15);
    AsnObject.setDebug(1);

    String host = util.getHost();
    int port = util.getPort(SnmpContextBasisFace.DEFAULT_PORT);
    String socketType = util.getSocketType();
    String community = util.getCommunity();

    int non_rep = util.getIntParameter(NON, 0);
    int max_rep = util.getIntParameter(MAX, 0);

    try 
    {
        context = new SnmpContextv2c(host, port, socketType);
        context.setCommunity(community);

        pdu = new OneGetBulkPdu(context);
        pdu.addObserver(this);
        pdu.setNonRepeaters(non_rep);
        pdu.setMaxRepetitions(max_rep);

        System.err.println("Setting host " + host 
            + ", non_rep " + non_rep 
            + ", max_rep " + max_rep);

        for (int i=0; i<10; i++)
        {
            String oid = util.getProperty(Util.OID + i);
            if (oid != null)
            {
                System.err.println("Adding OID " + oid);
                pdu.addOid(oid);
            }
        }

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
}

/**
 * Implementing the Observer interface. Receiving the response from 
 * the Pdu. See update1() and update2() for the actual implementation.
 *
 * @see #update1
 * @see #update2
 */
public void update(Observable obs, Object ov)
{
    update1(obs, ov);
    update2(obs, ov);
    System.exit(0);
}

/**
 * A possible update implementation.
 * This method uses the Vector of the OneGetBulkPdu.
 *
 * @param obs the OneGetBulkPdu variable
 * @param ov the Vector of varbinds
 *
 * @see uk.co.westhawk.snmp.pdu.OneGetBulkPdu
 * @see uk.co.westhawk.snmp.pdu.OneGetBulkPdu#getVarbinds
 * @see uk.co.westhawk.snmp.stack.varbind
 * @see java.util.Vector
 */
public void update1(Observable obs, Object ov)
{
    if (pdu.getErrorStatus() == AsnObject.SNMP_ERR_NOERROR)
    {
        Vector vars = (Vector) ov;
        int sz = vars.size();
        System.out.println("update1(): " + sz + " varbinds");
        for (int i=0; i<sz; i++)
        {
            varbind var = (varbind) vars.elementAt(i);
            System.out.println(i + " " + var.toString());
        }
    }
    else
    {
        System.out.println("update1(): " + pdu.getErrorStatusString());
    }
    context.destroy();
}

/**
 * A possible update implementation.
 * This method uses the response varbind array of Pdu.
 *
 * @param obs the OneGetBulkPdu variable
 * @param ov the Vector of varbinds
 *
 * @see uk.co.westhawk.snmp.pdu.OneGetBulkPdu
 * @see uk.co.westhawk.snmp.stack.varbind
 * @see uk.co.westhawk.snmp.stack.Pdu#getResponseVarbinds
 */
public void update2(Observable obs, Object ov)
{
    if (pdu.getErrorStatus() == AsnObject.SNMP_ERR_NOERROR)
    {
        try
        {
            varbind[] vars = pdu.getResponseVarbinds();
            int sz = vars.length;
            System.out.println("update2(): " + sz + " varbinds");
            for (int i=0; i<sz; i++)
            {
                varbind var = (varbind) vars[i];
                System.out.println(i + " " + var.toString());
            }
        }
        catch(uk.co.westhawk.snmp.stack.PduException exc)
        {
            System.out.println("update2(): PduException " + exc.getMessage());
        }

    }
    else
    {
        System.out.println("update2(): " + pdu.getErrorStatusString());
    }
    context.destroy();
}


public static void main(String[] args)
{
    String propFileName = null;
    if (args.length > 0)
    {
        propFileName = args[0];
    }
    get_bulk application = new get_bulk(propFileName);
    application.init();
}

}

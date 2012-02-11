// NAME
//      $RCSfile: test_threads.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.2 $
// CREATED
//      $Date: 2002/10/22 16:34:15 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 1996, 1997 by Westhawk Ltd (www.westhawk.co.uk)
 * Copyright (C) 1998, 1999, 2000, 2001, 2002 by Westhawk Ltd (www.westhawk.co.uk)
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

import java.awt.Graphics;
import java.awt.Event;
import java.util.*;
import java.net.*;

import uk.co.westhawk.snmp.stack.*;    
import uk.co.westhawk.snmp.pdu.*;    

/**
 * <p>
 * The test_threads application does a MIB tree walk on various hosts.
 * It will start with the OID as configured in the properties file.
 * </p>
 *
 * <p>
 * The hosts, port, oid and community name can be configured 
 * in the properties file. 
 * The name of the properties file can be passed as first argument to
 * this application. If there is no such argument, it will look for
 * <code>test_threads.properties</code>. If this file does not exist, the
 * application will use default parameters.
 * </p>
 *
 * @see uk.co.westhawk.snmp.pdu.OneGetNextPdu
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.2 $ $Date: 2002/10/22 16:34:15 $
 */
public class test_threads implements Observer
{
    private static final String     version_id =
        "@(#)$Id: test_threads.java,v 1.2 2002/10/22 16:34:15 birgit Exp $ Copyright Westhawk Ltd";

    public final static String HOSTS = "hosts";
    private static int      retries[] = {250, 500, 1000, 1000};

    public  SnmpContext     contexts[];

    private String          hosts[];
    private int             nrHost=0;
    private SnmpContext     context;

    private int             port;
    private String          community;
    private String          socketType;
    private String          oid;

    private OneGetNextPdu   pdu;
    private Util            util;

/**
 * Constructor.
 *
 * @param propertiesFilename The name of the properties file. Can be
 * null.
 */
public test_threads(String propertiesFilename)
{
    //AsnObject.setDebug(15);
    util = new Util(propertiesFilename, this.getClass().getName());

    String hostsProp = util.getProperty(HOSTS);
    port = util.getPort(SnmpContextBasisFace.DEFAULT_PORT);
    community = util.getCommunity();
    socketType = util.getSocketType();
    oid = util.getOid("1");

    StringTokenizer tok = new StringTokenizer(hostsProp, ",");
    int count = tok.countTokens();
    hosts = new String[count];
    contexts = new SnmpContext[count];

    int n=0;
    while (tok.hasMoreTokens())
    {
        try
        {
            String h = tok.nextToken();
            hosts[n] = h.trim();
System.out.println(n + " " + hosts[n]);
            n++;
        }
        catch (NoSuchElementException exc) { }
    }
}


public void init () 
{
    nrHost=0;
    try 
    {
        contexts[nrHost] = new SnmpContext(hosts[nrHost], port, socketType);
        startWalk(contexts[nrHost]);
    }
    catch (java.io.IOException exc)
    {
        System.out.println("IOException " + exc.getMessage());
        System.exit(0);
    }
    catch (PduException exc)
    {
        System.out.println("PduException " + exc.getMessage());
        System.exit(0);
    }
}

public void startWalk(SnmpContext cont)
throws java.io.IOException, PduException
{
    context = cont;
    context.setCommunity(community);

    System.out.println("startWalk: " 
          + context.getHost() + " " + context.getPort());
    pdu = new OneGetNextPdu(context);
    pdu.addObserver(this);
    pdu.addOid(oid);
    pdu.setRetryIntervals(retries);
    pdu.send();
}

/**
 * Implementing the Observer interface. Receiving the response from 
 * the Pdu.
 *
 * @param obs the OneGetNextPdu variable
 * @param ov the varbind
 *
 * @see uk.co.westhawk.snmp.pdu.OneGetNextPdu
 * @see uk.co.westhawk.snmp.stack.varbind
 */
public void update(Observable obs, Object ov)
{
    try 
    {
        if (pdu.getErrorStatus() == AsnObject.SNMP_ERR_NOERROR)
        {
            varbind var = (varbind) ov;
            System.out.println(var.toString());

            pdu = new OneGetNextPdu(context);
            pdu.addObserver(this);
            pdu.addOid(var.getOid());
            pdu.send();
        }
        else
        {
            contexts[nrHost].destroy();
            if (nrHost < hosts.length-1)
            {
                nrHost++;
                contexts[nrHost] = new SnmpContext(hosts[nrHost], 
                    port, socketType);
                startWalk(contexts[nrHost]);
            }
            else
            {
                System.gc();
                System.out.println("gc");
            }
        }
    }
    catch (java.io.IOException exc)
    {
        System.out.println("update(): IOException " + exc.getMessage());
        exc.printStackTrace();
    }
    catch (PduException exc)
    {
        System.out.println("update(): PduException " + exc.getMessage());
        exc.printStackTrace();
    }
}


public static void main(String [] args)
{
    String propFileName = null;
    if (args.length > 0)
    {
        propFileName = args[0];
    }
    test_threads t = new test_threads(propFileName);
    t.init();

    while (true) 
    {
        try 
        { 
            // 1.5 minutes
            Thread.sleep(90000); 
        } 
        catch (Exception x) 
        {
            ;
        }

        System.out.println("tick");
        for (int i=0; i<t.contexts.length; i++)
        {
            if (t.contexts[i] != null)
            {
                System.out.println(t.contexts[i].getHost() 
                    + "\t" + t.contexts[i].getDebugString());
            }
        }
    }
}

}

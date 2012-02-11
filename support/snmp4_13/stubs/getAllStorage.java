// NAME
//      $RCSfile: getAllStorage.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.2 $
// CREATED
//      $Date: 2002/11/04 17:56:52 $
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

import uk.co.westhawk.snmp.stack.*;
import uk.co.westhawk.snmp.pdu.*;

import java.awt.*; 
import java.util.*;
import java.text.*;
import java.io.*;
import java.net.*;

/**
 * <p>
 * The getAllStorage application tests if the StorageGetNextPdu, that I
 * created using XSLT, works. It resembles getAllInterfaces.java 
 * </p>
 *
 * @see uk.co.westhawk.snmp.pdu.StorageGetNextPdu
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.2 $ $Date: 2002/11/04 17:56:52 $
 */
public class getAllStorage implements Observer, Runnable
{
    private static final String     version_id =
        "@(#)$Id: getAllStorage.java,v 1.2 2002/11/04 17:56:52 birgit Exp $ Copyright Westhawk Ltd";

    /**
     * Use 10 (sec) as interval 
     */
    public final static long sleepTime = 15000;

    private StorageGetNextPdu pdu;

    private String host;
    private int port;
    private SnmpContext context;

    private boolean mayLoopStart;
    private Util util;


/**
 * Constructor.
 *
 * @param propertiesFilename The name of the properties file. Can be
 * null.
 */
public getAllStorage(String propertiesFilename)
{
    util = new Util(propertiesFilename, this.getClass().getName());
}

public void init ()
{
    //AsnObject.setDebug(15);
    host = util.getHost();
    port = util.getPort(SnmpContextBasisFace.DEFAULT_PORT);
    String socketType = util.getSocketType();
    String community = util.getCommunity();

    try
    {
        context = new SnmpContext(host, port, socketType);
        context.setCommunity(community);

        mayLoopStart = true;
    }
    catch (IOException exc)
    {
        System.out.println("IOException: " + exc.getMessage());
        System.exit(0);
    }
}

public void start()
{
    Thread me = new Thread(this);
    me.setPriority(Thread.MIN_PRIORITY);
    me.start();
}


public void run()
{
    while (context != null)
    {
        if (mayLoopStart == true)
        {
            mayLoopStart = false;

            try
            {
                pdu = new StorageGetNextPdu(context, this);
            }
            catch(java.io.IOException exc)
            {
                System.out.println("run(): IOException " + exc.getMessage());
            }
            catch(uk.co.westhawk.snmp.stack.PduException exc)
            {
                System.out.println("run(): PduException " + exc.getMessage());
            }
        }

        try
        {
            System.out.println(" .. sleeping ..");
            Thread.sleep(sleepTime);
        } 
        catch (InterruptedException ix)
        {
            ;
        }
    }
}

/**
 * Implementing the Observer interface. Receiving the response from 
 * the Pdu.
 *
 * @param obs the StorageGetNextPdu variable
 * @param ov the array of varbind (not used)
 *
 * @see uk.co.westhawk.snmp.pdu.StorageGetNextPdu
 * @see uk.co.westhawk.snmp.stack.varbind
 */
public void update(Observable obs, Object ov)
{
    StorageGetNextPdu prev;

    if (pdu.getErrorStatus() == AsnObject.SNMP_ERR_NOERROR)
    {
        prev = pdu;
        System.out.println(pdu.toString());

        pdu = new StorageGetNextPdu(context);
        pdu.addObserver(this);
        pdu.addOids(prev);

        try
        {
            pdu.send();
        }
        catch(java.io.IOException exc)
        {
            System.out.println("update(): IOException " + exc.getMessage());
        }
        catch(uk.co.westhawk.snmp.stack.PduException exc)
        {
            System.out.println("update(): PduException " + exc.getMessage());
        }
    }
    else
    {
        mayLoopStart = true;
    }
}
    
/**
 * Main. To use a properties file different from 
 * <code>getAllStorage.properties</code>, pass the name as first argument.
 */
public static void main(String[] args)
{
    String propFileName = null;
    if (args.length > 0)
    {
        propFileName = args[0];
    }
    getAllStorage application = new getAllStorage(propFileName);
    application.init();
    application.start();
}

}

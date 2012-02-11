// NAME
//      $RCSfile: InterfaceSpeed.java,v $
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

import uk.co.westhawk.snmp.stack.*;    
import uk.co.westhawk.snmp.pdu.*;    
import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*;
import java.util.*;

import java.net.*;

/**
 * <p>
 * The InterfaceSpeed application asks for one interface. It will display its
 * speed in a label. It uses the InterfacePdu to get its information.
 * </p>
 *
 * <p>
 * The host can be configured 
 * in the properties file, 
 * the rest of the parameters is hard coded.
 * The name of the properties file can be passed as first argument to
 * this application. If there is no such argument, it will look for
 * <code>InterfaceSpeed.properties</code>. If this file does not exist, the
 * application will use default parameters.
 * </p>
 *
 * @see uk.co.westhawk.snmp.pdu.InterfacePdu
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.2 $ $Date: 2002/10/22 16:34:15 $
 */
public class InterfaceSpeed extends JComponent implements Observer, Runnable
{
    private static final String     version_id =
        "@(#)$Id: InterfaceSpeed.java,v 1.2 2002/10/22 16:34:15 birgit Exp $ Copyright Westhawk Ltd";

    /**
     * Use "public" as community name
     */
    public final static String community = "public";
    /**
     * Use 161 as port no
     */
    public final static int port = SnmpContextBasisFace.DEFAULT_PORT;
    /**
     * Use 5000 (msec) as interval 
     */
    public final static int interval = 5000;
    /**
     * Use 3 as interface index
     */
    public final static int intfIndex = 3;

    private SnmpContext context;
    JLabel h;
    JLabel v;

    InterfacePdu up=null; 
    InterfacePdu prev=null;
    Util util;

    /**
     * Constructor.
     *
     * @param propertiesFilename The name of the properties file. Can be
     * null.
     */
    public InterfaceSpeed(String propertiesFilename)
    {
        util = new Util(propertiesFilename, this.getClass().getName());
    }

    public void init () 
    {
        // get the host name
        String host = util.getHost();

        h = new JLabel("Host " + host + ", Interface " 
              + intfIndex + " speed");
        try 
        {
            context = new SnmpContext(host, port);
            context.setCommunity(community);

            setLayout(new GridLayout(2, 1));
            add(h);
            v = new JLabel(" unknown ");
            add(v);
        }
        catch (java.io.IOException exc)
        {
            System.out.println("IOException " + exc.getMessage());
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
        // We might not get an error, since it is not sure if an
        // interface exists with this intfIndex as index!
        InterfacePdu up2;
        while (context != null) 
        {
            try 
            {
                up2 = new InterfacePdu(context, this, intfIndex);
                Thread.sleep(interval);
            } 
            catch (InterruptedException exc) 
            {
                ;
            }
            catch (java.io.IOException exc) 
            {
                System.out.println("run(): IOException " 
                    + exc.getMessage());
            }
            catch (PduException exc) 
            {
                System.out.println("run(): PduException " 
                    + exc.getMessage());
            }
        }
    }

 
    /**
     * Implementing the Observer interface. Receiving the response from 
     * the Pdu.
     *
     * @param obs the InterfacePdu variable
     * @param ov the varbind
     *
     * @see uk.co.westhawk.snmp.pdu.InterfacePdu
     * @see uk.co.westhawk.snmp.stack.varbind
     */
    public void update(Observable obs, Object ov)
    {
        up = (InterfacePdu) obs;
        if (prev != null)
        {
            // TODO invokeLater
            v.setText(up.getSpeed(prev)+ " bytes/sec ");
        }

        // keep the previous...
        prev = up; 
    }

    public static void main(String[] args)
    {
        String propFileName = null;
        if (args.length > 0)
        {
            propFileName = args[0];
        }
        InterfaceSpeed application = new InterfaceSpeed(propFileName);
        application.init();
        application.start();

        JFrame frame = new JFrame();
        frame.setTitle(application.getClass().getName());
        frame.getContentPane().add(application, BorderLayout.CENTER);

        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        frame.setBounds(50, 50, 400, 100);
        frame.setVisible(true);
    }
}

// NAME
//      $RCSfile: Interfaces.java,v $
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
 * The Interfaces application will ask all interfaces once for their
 * operational state. It uses the InterfacePdu to get its information.
 * </p>
 *
 * <p>
 * The host can be configured 
 * in the properties file, 
 * the rest of the parameters is hard coded.
 * The name of the properties file can be passed as first argument to
 * this application. If there is no such argument, it will look for
 * <code>Interfaces.properties</code>. If this file does not exist, the
 * application will use default parameters.
 * </p>
 *
 * @see uk.co.westhawk.snmp.pdu.InterfacePdu
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.2 $ $Date: 2002/10/22 16:34:15 $
 */
public class Interfaces extends JComponent implements Observer
{
    private static final String     version_id =
        "@(#)$Id: Interfaces.java,v 1.2 2002/10/22 16:34:15 birgit Exp $ Copyright Westhawk Ltd";

    /**
     * Use "public" as community name
     */
    public final static String community = "public";
    /**
     * Use 161 as port no
     */
    public final static int port = SnmpContextBasisFace.DEFAULT_PORT;

    private SnmpContext context;
    JLabel h;
    JLabel v[];
    int ifCount = 0;
    Util util;

    /**
     * Constructor.
     *
     * @param propertiesFilename The name of the properties file. Can be
     * null.
     */
    public Interfaces(String propertiesFilename)
    {
        util = new Util(propertiesFilename, this.getClass().getName());
    }

    public void init () 
    {
        String host = util.getHost();

        h = new JLabel("Interface status of host " + host);
        try 
        {
            // create the context
            context = new SnmpContext(host, port);
            context.setCommunity(community);

            // ask for the number of current interfaces
            ifCount = InterfacePdu.getNumIfs(context);

            setLayout(new GridLayout(ifCount+1, 1));
            add(h);
            v = new JLabel[ifCount];
            for (int i=0; i< ifCount; i++) {
              v[i] = new JLabel("unknown "+i);
              add(v[i]);
            }
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

    public void start()
    {
        InterfacePdu up;

        // Send a request to all interfaces
        // This is not completely correct, since the interface indexes
        // may not be consecutive !!
        if (context != null) {
            for(int i=1;i<=ifCount;i++) {
                try
                {
                    up = new InterfacePdu(context, this, i);
                }
                catch (java.io.IOException exc) 
                {
                    System.out.println("start(): IOException " 
                        + exc.getMessage());
                }
                catch (PduException exc) 
                {
                    System.out.println("start(): PduException " 
                        + exc.getMessage());
                }
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
        // I've got the answer, display it
        // TODO: invokeLater
        InterfacePdu fi = (InterfacePdu) obs;
        v[fi.getIndex()-1].setText(fi.getDescription() + " " + fi.getOperStatusString());
    }

    public static void main(String[] args)
    {
        String propFileName = null;
        if (args.length > 0)
        {
            propFileName = args[0];
        }
        Interfaces application = new Interfaces(propFileName);
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

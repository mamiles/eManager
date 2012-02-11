// NAME
//      $RCSfile: set_one.java,v $
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
 * Copyright (C) 1996, 1997, 1998, 1999, 2000, 2001, 2002 by Westhawk Ltd (www.westhawk.co.uk)
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
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;

/**
 * <p>
 * The set_one application will display the parameters, as configured in the
 * properties file. It will retrieve the specified MIB variable. 
 * </p>
 *
 * <p>
 * The name of the properties file can be passed as first argument to
 * this application. If there is no such argument, it will look for
 * <code>set_one.properties</code>. If this file does not exist, the
 * application will use default parameters.
 * </p>
 *
 * <p>
 * The user can set the required OID and perform a Get or GetNext
 * request. 
 * </p>
 *
 * <p>
 * The user can also set a MIB variable by performing a Set request.
 * By default the value is set as a String type (using AsnOctets),
 * unless the value is a number (AsnInteger will then be used). 
 * </p>
 *
 * @see uk.co.westhawk.snmp.pdu.OneGetPdu
 * @see uk.co.westhawk.snmp.pdu.OneSetPdu
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.2 $ $Date: 2002/10/22 16:34:15 $
 */
public class set_one extends JComponent
      implements Observer, ActionListener
{
    private static final String     version_id =
        "@(#)$Id: set_one.java,v 1.2 2002/10/22 16:34:15 birgit Exp $ Copyright Westhawk Ltd";

    /**
     * sysContact is used as default oid
     */
    public final static String sysContact = "1.3.6.1.2.1.1.4.0";

    private String host;
    private int port;
    private String community;
    private String oid;
    private String value;
    private String socketType;

    private SnmpContext context;
    private JTextField   thost, toid, tcom, tport, tvalue;
    private JButton      getButton, setButton, getNextButton;
    private JLabel       lmessage;

    private Pdu         pdu;
    private boolean     pduInFlight;
    private Util        util;


/**
 * Constructor.
 *
 * @param propertiesFilename The name of the properties file. Can be
 * null.
 */
public set_one(String propertiesFilename)
{
    util = new Util(propertiesFilename, this.getClass().getName());
}

public void init () 
{
    //AsnObject.setDebug(15);
    host = util.getHost();
    port = util.getPort(SnmpContextBasisFace.DEFAULT_PORT);
    socketType = util.getSocketType();
    oid = util.getOid(sysContact);
    community = util.getCommunity();

    pduInFlight = false;
    makeLayout(host, oid, port, community);
    sendGetRequest(host, port, community, oid);
}

public void actionPerformed(ActionEvent evt)
{
    Object src = evt.getSource();
    host = thost.getText();
    port = Integer.valueOf(tport.getText()).intValue();
    community = tcom.getText();
    oid = toid.getText();

    if (context != null)
    {
        context.destroy();
    }
    try
    {
        context = new SnmpContext(host, port, socketType);
        context.setCommunity(community);

        if (src == getButton)
        {
            pdu = new OneGetPdu(context);
            pdu.addOid(oid);
        }
        else if (src == getNextButton)
        {
            pdu = new OneGetNextPdu(context);
            pdu.addOid(oid);
        }
        else if (src == setButton)
        {
            OneSetPdu setPdu = new OneSetPdu(context);
            String value = tvalue.getText();
            AsnObject obj;
            if (Util.isNumber(value))
            {
                obj = new AsnInteger(Util.getNumber(value));
            }
            else
            {
                obj = new AsnOctets(value);
                //testSubOid(oid, (AsnOctets) obj, false);
                //testSubOid2(oid);
            }
            setPdu.addOid(oid, obj);

            pdu = setPdu;
        }
        sendRequest(pdu);
    }
    catch (Exception exc)
    {
        exc.printStackTrace();
        setErrorMessage("Exception: " + exc.getMessage());
    }
}

private void testSubOid(String oid, AsnOctets obj, boolean length_implied)
{
    long [] subOid = obj.toSubOid(length_implied);
    System.err.println("SubOid of '" + obj.toString() 
        + "' (length_implied = " + length_implied + "):");
    for (int i=0; i<subOid.length; i++)
    {
        if (i > 0)
        {
            System.err.print(".");
        }
        System.err.print(subOid[i]);
    }
    System.err.println();

    AsnObjectId asnOID = new AsnObjectId(oid);
    asnOID.add(subOid);
    System.err.println(asnOID);
}

private void testSubOid2(String oid)
{
    try
    {
        InetAddress inetA = InetAddress.getLocalHost();
        byte [] ip = inetA.getAddress();

        AsnOctets obj = new AsnOctets(ip, SnmpConstants.IPADDRESS);
        testSubOid(oid, obj, true);
    }
    catch (java.net.UnknownHostException exc)
    {
        System.err.println(exc.getMessage());
    }
}

private void sendGetRequest(String host, int port, String community, 
  String oid)
{
    if (context != null)
    {
        context.destroy();
    }
    try
    {
        context = new SnmpContext(host, port, socketType);
        context.setCommunity(community);

        pdu = new OneGetPdu(context);
        pdu.addOid(oid);
        sendRequest(pdu);
    }
    catch (java.io.IOException exc)
    {
        exc.printStackTrace();
        setErrorMessage("IOException: " + exc.getMessage());
    }
}

private void sendRequest(Pdu pdu)
{
    boolean hadError = false;

    setButton.setEnabled(false);
    getButton.setEnabled(false);
    getNextButton.setEnabled(false);
    try
    {
        if (!pduInFlight)
        {
            pduInFlight = true;
            setMessage("Sending request ..: ");

            tvalue.setText("");
            pdu.addObserver(this);
            pdu.send();
        }
        else
        {
            setErrorMessage("Pdu still in flight");
        }
    }
    catch (PduException exc)
    {
        exc.printStackTrace();
        setErrorMessage("PduException: " + exc.getMessage());
        hadError = true;
    }
    catch (java.io.IOException exc)
    {
        exc.printStackTrace();
        setErrorMessage("IOException: " + exc.getMessage());
        hadError = true;
    }

    if (hadError == true)
    {
        pduInFlight = false;
        setButton.setEnabled(true);
        getButton.setEnabled(true);
        getNextButton.setEnabled(true);
    }
}


/**
 * Implementing the Observer interface. Receiving the response from 
 * the Pdu.
 *
 * @param obs the Pdu variable
 * @param ov the varbind
 *
 * @see uk.co.westhawk.snmp.pdu.OneGetPdu
 * @see uk.co.westhawk.snmp.pdu.OneSetPdu
 * @see uk.co.westhawk.snmp.stack.varbind
 */
public void update(Observable obs, Object ov)
{
    pduInFlight = false;

    ScreenOnUpdate upd = new ScreenOnUpdate(obs, ov);
    SwingUtilities.invokeLater(upd);
}

public void setErrorMessage(String message)
{
    setMessage(message, true);
}

public void setMessage(String message)
{
    setMessage(message, false);
}

public void setMessage(String message, boolean isError)
{
    lmessage.setText(message);
    Color c = Color.white;
    if (isError)
    {
        c = Color.red;
    }
    lmessage.setBackground(c);
}

private void makeLayout(String host, String oid, int port, String com)
{
    JLabel lhost, loid, lport, lcom, lvalue;

    lhost   = new JLabel("Host: ");
    lport   = new JLabel("Port: ");
    lcom    = new JLabel("Community: ");
    loid    = new JLabel("OID: ");
    lvalue  = new JLabel("Value: ");
    lmessage = new JLabel("");
    lmessage.setOpaque(true);

    thost   = new JTextField(host);
    tport   = new JTextField(String.valueOf(port));
    tcom    = new JTextField(com);
    toid    = new JTextField(oid);
    tvalue  = new JTextField();

    setButton = new JButton ("Set");
    getButton = new JButton ("Get");
    getNextButton = new JButton ("GetNext");

    this.setLayout(new GridLayout(7, 2));

    add(lhost); add(thost);
    add(lport); add(tport);
    add(lcom);  add(tcom);
    add(loid);  add(toid);
    add(lvalue); add(tvalue);
    add(setButton); add(getButton);
    add(getNextButton); add(lmessage);

    setButton.addActionListener(this);
    getButton.addActionListener(this);
    getNextButton.addActionListener(this);
    lmessage.setBackground(Color.white);
}

public static void main(String[] args)
{
    String propFileName = null;
    if (args.length > 0)
    {
        propFileName = args[0];
    }
    set_one application = new set_one(propFileName);
    application.init();

    JFrame frame = new JFrame();
    frame.setTitle(application.getClass().getName());
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(application, BorderLayout.CENTER);

    frame.addWindowListener(new WindowAdapter()
    {
        public void windowClosing(WindowEvent e)
        {
            System.exit(0);
        }
    });
    frame.setBounds(50, 50, 500, 200);
    frame.setVisible(true);

}

class ScreenOnUpdate implements Runnable 
{
    Observable obs;
    Object ov;

    public ScreenOnUpdate(Observable ob, Object obj)
    {
        obs = ob;
        ov = obj;
    }

    public void run()
    {
        setMessage("Received answer");
        if (pdu.getErrorStatus() != AsnObject.SNMP_ERR_NOERROR)
        {
            setErrorMessage(pdu.getErrorStatusString());
        }
        else
        {
            varbind var = (varbind) ov;
            if (var != null)
            {
                AsnObjectId oid = var.getOid();
                toid.setText(oid.toString());

                AsnObject obj = var.getValue();
                tvalue.setText(obj.toString());
            }
        }
        setButton.setEnabled(true);
        getButton.setEnabled(true);
        getNextButton.setEnabled(true);
    }
}

}

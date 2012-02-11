//
// NAME
//      $RCSfile: TrapTestSuite.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.10 $
// CREATED
//      $Date: 2002/11/04 11:29:06 $
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
package uk.co.westhawk.test;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;
import javax.swing.*;

import org.w3c.dom.*;
import uk.co.westhawk.snmp.event.*;
import uk.co.westhawk.snmp.stack.*;
import uk.co.westhawk.snmp.pdu.*;


/**
 * The class TrapTestSuite tests the trap functionality of this stack.
 * It performs all the tests according to <code>trap.xml</code>.
 * This class can be used as applet and application. If run as applet
 * the XML_FILE should be passed as applet parameter.
 *
 * <p>
 * I've configured all the SNMP agents involved so that they send a trap
 * when there is an authentication failure (like a wrong community name). 
 * The xml file lists all the agents I'm testing against. 
 * I'm going to send every one of these agents a request with a wrong 
 * authentication, and then see if I can handle the trap.
 * </p>
 *
 * <p>
 * Netscape seems to destroy the applet after half a minute, I don't
 * know how to solve this.
 * On Unix and Linux systems this applet or application has to run as
 * <em>root</em>.
 * </p>
 *
 * @see SnmpTarget
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.10 $ $Date: 2002/11/04 11:29:06 $
 */
public class TrapTestSuite extends Applet 
    implements PropertyChangeListener, Runnable, TrapListener 
{
    private static final String     version_id =
        "@(#)$Id: TrapTestSuite.java,v 1.10 2002/11/04 11:29:06 birgit Exp $ Copyright Westhawk Ltd";

    /** 
     * Name of the XML file. Can be overwritten in main and in the html
     * file.
     */
    public static String XML_FILE = "trap.xml";

    public static final String DOC_HEADER =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    public static final String DOC_TYPE =
        "<!DOCTYPE tests SYSTEM \"./trap.dtd\">";

    public static final String TRAPS = "traps";
    public static final String TEST = "test";
    public static final String DEFAULT = "default";

    public final static String sysUpTime = "1.3.6.1.2.1.1.3";

    private Node defaultNode;
    private Vector contextList;
    private Vector testList;
    private PrintWriter writer;
    private boolean testStarted;
    private boolean testInFlight;

    private XMLtoDOM xmlToDom;
    private SnmpTarget target;
    private Thread me;
    private DefaultTrapContext trapContext = null;

    private int testNo = 0;
    boolean isStandAlone = false;


/**
 * The constructor.
 */
public TrapTestSuite()
{
    //AsnObject.setDebug(6);
    //AsnObject.setDebug(15);

    writer = new PrintWriter(new OutputStreamWriter(System.out), true);
    testStarted = false;
    testInFlight = false;
    xmlToDom = new XMLtoDOM();
    xmlToDom.setWriter(writer);

    target = new SnmpTarget();
    target.setWriter(writer);
    target.addPropertyChangeListener(this);
}

/**
 * Initialises the applet. It looks for the XML_FILE, reads it and
 * analyses it.
 *
 * @see #analyseDocument
 */
public void init()
{
    AppletContext context = null;
    URL documentURL = null;
    Document testDoc = null;
    try
    {
        context = this.getAppletContext();

        String xml_file = this.getParameter("XML_FILE");
        if (xml_file != null)
        {
            try
            {
                documentURL = new URL(xml_file);
            }
            catch (MalformedURLException exc) { }
        }
        else
        {
            writer.println("TrapTestSuite.init():"
                + " Missing parameter XML_FILE");
        }


    }
    catch (NullPointerException exc)
    {
        // we are in an application
        documentURL = this.getClass().getResource(XML_FILE);
    }

    if (documentURL == null)
    {
        writer.println("TrapTestSuite.init():" 
            + " Cannot find " + XML_FILE);
    }
    else
    {
        writer.println("TrapTestSuite.init(): XML_FILE " + documentURL);
        testDoc = xmlToDom.getDocument(documentURL);

        analyseDocument(testDoc);
    }
}

/**
 * Starts the applet. All the contexts are created first. Next the
 * thread which will run the tests is created.
 * @see #run
 */
public void start()
{
    if (testStarted == false && defaultNode != null 
            && 
        testList != null && testList.size() > 0)
    {
        testStarted = true;

        try
        {
            trapContext = target.createTrapContext(defaultNode);
            trapContext.addUnhandledTrapListener(this);

            int sz = testList.size();
            contextList = new Vector(sz);
            for (int i=0; i<sz; i++)
            {
                Node testNode = (Node) testList.elementAt(i);
                SnmpContextBasisFace context = target.createContext(testNode);
                contextList.addElement(context);
                context.addTrapListener(this);
            }
            
            testNo = contextList.size()-1;
            if (testNo < 0)
            {
                testNo = 0;
            }

            me = new Thread(this, "TrapTestSuite");
            me.setPriority(Thread.MIN_PRIORITY);
            me.start();
        }
        catch (IOException exc)
        {
            // the xml file should have the proper settings ..
            writer.println("TrapTestSuite.start(): IOException " 
                + exc.getMessage());
            exc.printStackTrace(writer);
        }
    }
}

/**
 * Runs the test. It launches one test, waits for it to be finished and
 * starts the next one.
 */
public void run()
{
    while (testNo >= 0)
    {
        if (testInFlight == false)
        {
            nextTest();
        }
        try
        {
            me.sleep(1000);
        }
        catch (InterruptedException exc) {}
    }

    writer.println("waiting for the last test to finish ..");
    while (testInFlight == true)
    {
        try
        {
            me.sleep(3000);
        }
        catch (InterruptedException exc) {}
    }

    writer.println("** Finished all Tests. **");
}

/**
 * Receives a trap event.
 */
public void trapReceived(TrapEvent evt)
{
    if (evt.isDecoded())
    {
        Pdu pdu = evt.getPdu();
        writer.println("\ntrapReceived():" 
            + " received decoded trap " + pdu.toString());
    }
    else 
    {
        int version = evt.getVersion(); 
        String host = evt.getHostAddress(); 
        writer.println("\ntrapReceived():"
            + " received unhandled undecoded trap v " + version
            + " from host " + host);
    }
}

/**
 * Receives the property change event, indicating that the test has finished.
 */
public void propertyChange(PropertyChangeEvent evt)
{
    testInFlight = false;
}

/**
 * Send a request with a wrong authentication to force a trap.
 */
private void nextTest()
{
    writer.println("\nTrapTestSuite.nextTest(): Starting test " + testNo);
    testInFlight = true;

    SnmpContextBasisFace wrongContext = null;
    SnmpContextBasisFace context = (SnmpContextBasisFace) contextList.elementAt(testNo);
    try
    {
        if (context instanceof SnmpContext)
        {
            SnmpContext c = (SnmpContext) ((SnmpContext)context).clone();
            String com = c.getCommunity();
            com += "_bla";
            c.setCommunity(com);
            wrongContext = c;
        }
        else if (context instanceof SnmpContextv2c)
        {
            SnmpContextv2c c = (SnmpContextv2c) ((SnmpContextv2c)context).clone();
            String com = c.getCommunity();
            com += "_bla";
            c.setCommunity(com);
            wrongContext = c;
        }
        else if (context instanceof SnmpContextv3)
        {
            SnmpContextv3 c = (SnmpContextv3) ((SnmpContextv3)context).clone();
            String name = c.getContextName();
            name += "_bla";
            c.setContextName(name);
            wrongContext = c;
        }

        target.performGetNextRequest(wrongContext, sysUpTime);
    }
    catch (CloneNotSupportedException exc)
    {
        writer.println("TrapTestSuite.nextTest(): CloneNotSupportedException " 
            + exc.getMessage());
    }
    testNo--;
}

/**
 * Builds a list of all tests in the document.
 * The tests itself are performed later.
 *
 * @param doc The DOM document.
 */
public void analyseDocument(Document doc)
{
    if (doc != null)
    {
        Node testsNode = Util.getTopElementNode(doc);
        if (testsNode != null)
        {
            NodeList childNodes = testsNode.getChildNodes();
            int l = childNodes.getLength();

            testList = new Vector(l);
            for (int i=0; i<l; i++)
            {
                Node childNode = childNodes.item(i);
                short type = childNode.getNodeType();
                String childName = childNode.getNodeName();
                if (type == Node.ELEMENT_NODE)
                {
                    if (childName.equals(TEST))
                    {
                        testList.addElement(childNode);
                    }
                    else if (childName.equals(DEFAULT))
                    {
                        defaultNode = childNode;
                    }
                }
            }

        }
        else
        {
            writer.println("Cannot find any node with name " + TRAPS);
        }

    }
}


/**
 * The main method in order to run this as an application.
 */
public static void main(String[] argv)
{
    try
    {
        int len = argv.length;
        if (len > 0)
        {
            TrapTestSuite.XML_FILE = argv[0];
        }

        TrapTestSuite testS = new TrapTestSuite();
        testS.isStandAlone = true;
        JFrame frame = new JFrame("TrapTestSuite Test");
        uk.co.westhawk.tablelayout.TableLayout tLayout =
              new uk.co.westhawk.tablelayout.TableLayout();

        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        java.awt.Dimension dim = new java.awt.Dimension(500, 150);
        frame.setSize(dim);
        frame.setLocation(50, 50);
        frame.getContentPane().setLayout(tLayout);
        frame.setVisible(true);

        testS.init();
        testS.start();
    }
    catch (Exception exc)
    {
        exc.printStackTrace();
        usage();
    }
}

/**
 * Prints the usage of this application.
 */
public static void usage()
{
    System.err.println("Usage:");
    System.err.println("\t TrapTestSuite [<xml file>]");
}

}


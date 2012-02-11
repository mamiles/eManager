//
// NAME
//      $RCSfile: TestSuite.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.12 $
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
import uk.co.westhawk.snmp.stack.*;


/**
 * The class TestSuite performs all the tests according to
 * <code>test.xml</code>.
 * This class can be used as applet and application. If run as applet
 * the XML_FILE should be passed as applet parameter.
 *
 * <p>
 * Netscape seems to destroy the applet after half a minute, I don't
 * know how to solve this.
 * </p>
 *
 * @see SnmpTarget
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.12 $ $Date: 2002/11/04 11:29:06 $
 */
public class TestSuite extends Applet 
    implements PropertyChangeListener, Runnable
{
    private static final String     version_id =
        "@(#)$Id: TestSuite.java,v 1.12 2002/11/04 11:29:06 birgit Exp $ Copyright Westhawk Ltd";

    /** 
     * Name of the XML file. Can be overwritten in main and in the html
     * file.
     */
    public static String XML_FILE = "test.xml";

    public static final String DOC_HEADER =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    public static final String DOC_TYPE =
        "<!DOCTYPE tests SYSTEM \"./test.dtd\">";

    public static final String TESTS = "tests";
    public static final String TEST = "test";

    private Vector testList;
    private PrintWriter writer;
    private boolean testStarted;
    private boolean testInFlight;

    private XMLtoDOM xmlToDom;
    private SnmpTarget target;
    private Thread me;

    private int testNo = 0;
    boolean isStandAlone = false;


/**
 * The constructor.
 */
public TestSuite()
{
    //AsnObject.setDebug(15);
    AsnObject.setDebug(0);

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
            writer.println("TestSuite.init():"
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
        writer.println("TestSuite.init():" 
            + " Cannot find " + XML_FILE);
    }
    else
    {
        writer.println("TestSuite.init(): XML_FILE " + documentURL);
        testDoc = xmlToDom.getDocument(documentURL);

        analyseDocument(testDoc);
    }
}

/**
 * Starts the applet. It starts the thread which will run the tests.
 * @see #run
 */
public void start()
{
    if (testStarted == false && testList != null && testList.size() > 0)
    {
        testStarted = true;
        me = new Thread(this, "TestSuite");
        me.setPriority(Thread.MIN_PRIORITY);
        me.start();
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

    if (isStandAlone == true)
    {
        System.exit(0);
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
 * Performs the next test. It takes the next node from the list and
 * passes it to the SnmpTarget to perform it.
 */
private void nextTest()
{
    writer.println("\nTestSuite.nextTest(): Starting test " + testNo);
    testInFlight = true;

    Node testNode = (Node) testList.elementAt(testNo);
    target.performTest(testNode);
    testNo--;
}

/**
 * Builds a (node) list of all tests in the document.
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
                if (type == Node.ELEMENT_NODE && childName.equals(TEST))
                {
                    testList.addElement(childNode);
                }
            }

            testNo = testList.size()-1;
            if (testNo < 0)
            {
                testNo = 0;
            }
        }
        else
        {
            writer.println("Cannot find any node with name " + TESTS);
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
            TestSuite.XML_FILE = argv[0];
        }

        TestSuite testS = new TestSuite();
        testS.isStandAlone = true;
        JFrame frame = new JFrame("TestSuite Test");
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
    System.err.println("\t TestSuite [<xml file>]");
}

}


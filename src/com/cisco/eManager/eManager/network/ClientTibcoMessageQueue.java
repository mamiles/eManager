package com.cisco.eManager.eManager.network;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.TextArea;
import java.awt.Font;
import java.awt.Dimension;

import javax.swing.*;

import java.util.*;

import com.tibco.tibrv.*;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observer;
import java.util.Observable;
import java.util.Properties;
import java.util.Iterator;

import java.lang.StringBuffer;

import org.apache.log4j.*;

import com.cisco.eManager.common.event.AbstractEventMessage;

import com.cisco.eManager.eManager.util.GlobalProperties;

import com.cisco.eManager.eManager.network.ClientTibcoMessageProcessingWorker;
import com.cisco.eManager.eManager.network.ClientTibcoMessageQueueListener;
import com.cisco.eManager.eManager.network.NetworkGlobals;


/**
 * The mechanism for handling all incomig events.
 *
 * @author mjmatch
 */
public class ClientTibcoMessageQueue
{
    private static Logger      logger = Logger.getLogger(ClientTibcoMessageQueue.class);

    static private ClientTibcoMessageQueue clientTibcoMessageQueue = null;
    private Collection listeners;
    private Boolean workerLock = new Boolean (true);

    /**
     * The following attribute reflects the desired number of msg processing workers.
     * Over time, the system will work to add or remove workers as requried to achieve
     * the desired number of workers.
     */
    private Integer numberWorkers = new Integer (1);
    private List messageQueue;

    /**
     * @roseuid 3F4E5F8903CA
     */
    private ClientTibcoMessageQueue()
    {
	String     propertyValue;
	Properties systemProperties;

	messageQueue = Collections.synchronizedList (new LinkedList());
	listeners = Collections.synchronizedCollection (new LinkedList());

	systemProperties = GlobalProperties.instance().getProperties();

	propertyValue = systemProperties.getProperty (NetworkGlobals.NumberClientTibcoMsgProcessingWorkersKey);
	if (propertyValue == null) {
            numberWorkers = new Integer(NetworkGlobals.NumberClientTibcoMsgProcessingWorkersDefault);
            logger.info ("Using Default NumberClientTibcoMsgProcessingWorkers value: " +
			 numberWorkers.toString());
	} else {
	    try {
                numberWorkers = new Integer (propertyValue);
                logger.info ("Using NumberClientTibcoMsgProcessingWorkers value: " +
			     propertyValue);
 	    }
	    catch (NumberFormatException e) {
                numberWorkers = new Integer (NetworkGlobals.NumberClientTibcoMsgProcessingWorkersDefault);
                logger.error ("Format exception converting NumberClientTibcoMsgProcessingWorkers from the " +
			      "properties.  Using the default: " +
			      numberWorkers.toString());
	    }
	}

        propertyValue = systemProperties.getProperty (NetworkGlobals.DisplayNetworkClientTibcoMsgDebuggerFrames);
        if (propertyValue != null) {
            propertyValue = propertyValue.toUpperCase();
            if (propertyValue.startsWith("Y") == true) {
                logger.info("Displaying the ClientTibcoMessageQueue debugger.");
                startDebuggerThread();
            }
        }

	// create event processing workers
	startWorkers();
    }

    /**
     * @param eventMessage
     * @roseuid 3F21A2DF03B7
     */
    public void queueMessage(TibrvMsg message)
    {
        logger.log(Priority.DEBUG, "Enter");

        synchronized (workerLock) {
            synchronized (messageQueue)
            {
		messageQueue.add (message);
		notifyListenersMsgAdded (message);
		wakeAllWorkers();
            }
        }

        logger.log(Priority.DEBUG, "Exit");
    }

    /**
     * @param listener
     * @roseuid 3F257DD5031A
     */
    public void addClientTibcoMessageQueueListener(ClientTibcoMessageQueueListener listener)
    {
	synchronized (listeners) {
	    if (listeners.contains (listener) == false) {
		listeners.add (listener);
	    }
	}
    }

    /**
     * @param listener
     */
    public void removeClientTibcoMessageQueueListener(ClientTibcoMessageQueueListener listener)
    {
	synchronized (listeners) {
	    listeners.remove (listener);
	}
    }

    private void notifyListenersMsgAdded (TibrvMsg message)
    {
	Iterator iter;

	synchronized (listeners) {
	    iter = listeners.iterator();
	    while (iter.hasNext()) {
		( (ClientTibcoMessageQueueListener)iter.next()).clientTibcoMessageAdded (message);
	    }
	}
    }

    /**
     * @param sleepUntilAvailable If true, the thread will sleep until an
     * message is available.  If false, the thread will not sleep and return null if
     * a msg is not available.
     * @return Returns the next message in the queue.  Null if there are no
     */
    public synchronized TibrvMsg getNextMessage(boolean sleepUntilAvailable)
    {
        logger.debug ("Enter.");

	TibrvMsg msg;

        msg = null;

	synchronized (workerLock) {
	    do {
		synchronized (messageQueue) {
		    if (messageQueue.size() != 0) {
			msg = (TibrvMsg) messageQueue.remove(0);
		    }
		}

		if (sleepUntilAvailable == true &&
		    msg == null) {
		    try {
			logger.debug ("Sleep waiting for next Client Tibco Message.");
			workerLock.wait(20000);
			logger.debug ("Thread woke up.");
		    }
		    catch (InterruptedException e)
			{
                        }
		}
	    }
	    while (sleepUntilAvailable == true &&
		   msg == null);
	}

	logger.debug ("Exit.");

	return msg;
    }

    public void wakeSingleWorker()
    {
	synchronized (workerLock) {
	    workerLock.notify();
	}
    }

    public void wakeAllWorkers()
    {
        logger.debug("Enter");

	synchronized (workerLock) {
	    workerLock.notifyAll();
	}

        logger.debug("Exit");
    }

    /**
     * @return com.cisco.eManager.eManager.event.EventMessageQueue
     */
    public synchronized static ClientTibcoMessageQueue instance()
    {
	if (clientTibcoMessageQueue == null) {
	    clientTibcoMessageQueue = new ClientTibcoMessageQueue();
	}

	return clientTibcoMessageQueue;
    }

    /**
     * The following routine will reconcile the number of active client msg processing
     * workers against the desired number in the number
     * ClientTibcoMsgProcessingWorkers attribute.  The routine contains functionality to
     * start new workers as necessary.  The functionalty to stop workers as necessary
     * is in the It will start and stop the workers as
     * necessary.
     */
    private void startWorkers()
    {
	Collection workers;
	ClientTibcoMessageProcessingWorker worker;

	workers = ClientTibcoMessageProcessingWorker.getWorkers();

	synchronized (workers) {
            int numWorkers;
            int currentNumberWorkers;

            currentNumberWorkers = workers.size();
            numWorkers = numberWorkers.intValue();
	    if (numWorkers > currentNumberWorkers) {
		// We need to start some workers
		while (numWorkers > currentNumberWorkers) {
                    try {
                        ClientTibcoMessageProcessingWorker.startWorker();
                        currentNumberWorkers++;
                    }
                    catch (EmanagerNetworkException e) {
                        String logString;

                        logString = "Exiting.  " +
                                    EmanagerNetworkStatusCode.UnableToStartClientTibcoMsgProcessingWorker.getStatusCodeDescription();
                        logger.fatal (logString);
                        System.exit(1);
                    }
                }

		// I still need to figure out how to stop the required number of workers.
	    }
	}
    }

    private void startDebuggerThread()
    {
        Thread t;
        ThreadGroup parentThreadGroup;
        TibcoClientMsgQueueDebugger debugger;

        parentThreadGroup = NetworkGlobals.instance().getThreadGroup();

        debugger = new TibcoClientMsgQueueDebugger();
        if (parentThreadGroup == null) {
            t = new Thread (debugger, "Tibco Client Message Queue Debugger");
        } else {
            t = new Thread (parentThreadGroup, debugger, "Tibco Client Message Queue Debugger");
        }

        t.start();
    }

    public String toString()
    {
        Iterator iter;
        StringBuffer strBuf;

        strBuf = new StringBuffer();

        synchronized (messageQueue) {
            iter = messageQueue.iterator();
            while (iter.hasNext()) {
                iter.next().toString();
                strBuf.append (GlobalProperties.CarriageReturn);
            }
        }

        return strBuf.toString();
    }

    private class TibcoClientMsgQueueDebugger extends JFrame implements Runnable, ActionListener
    {
	private Logger logger = Logger.getLogger(TibcoClientMsgQueueDebugger.class);

        JTextArea textArea;

        public TibcoClientMsgQueueDebugger ()
        {
        }

        private void buildDisplay()
        {
            Font font;
            JFrame frame;
            JButton refresh;
            JPanel panel;
            JPanel debuggerPanel;

            frame = NetworkGlobals.instance().getDebuggerFrame();
            debuggerPanel = new JPanel();
            debuggerPanel.setLayout(new BorderLayout());

            refresh = new JButton ("Refresh");
            refresh.addActionListener(this);

            panel = new JPanel();
            panel.setLayout(new FlowLayout());
            panel.add (refresh);
            debuggerPanel.add (panel, BorderLayout.NORTH);

            //Create a text area.
            textArea = new JTextArea("");

            textArea.setFont(new Font("Lucida Console", Font.PLAIN, 12));
            JScrollPane areaScrollPane = new JScrollPane(textArea);
            areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            areaScrollPane.setPreferredSize(new Dimension(250, 250));
            areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Tibco Client Message Queue Content"),
                BorderFactory.createEmptyBorder(5,5,5,5)),
                areaScrollPane.getBorder()));

            debuggerPanel.add(areaScrollPane, BorderLayout.CENTER);

            NetworkGlobals.instance().getDebuggerPane().add("Tib Cli. Msg Q", debuggerPanel);

            frame.pack();
	    
	    try {
		frame.setVisible(true);
	    }
	    catch (Exception e) {
		logger.warn ("Unexpectedly unable to open the Client Tibco Message Queue debugger frame: " + e);
		logger.warn ("The debugger thread will exit.");
		System.exit (1);
	    }
        }

        public void run()
        {
            buildDisplay();

        }

        public void actionPerformed (ActionEvent e)
        {
            textArea.setText(ClientTibcoMessageQueue.instance().toString());
        }
    }
}

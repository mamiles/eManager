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

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.*;

import com.tibco.tibrv.*;

import com.cisco.eManager.eManager.event.State;
import com.cisco.eManager.eManager.event.AbstractWorker;

import com.cisco.eManager.eManager.util.GlobalProperties;

import com.cisco.eManager.eManager.network.NetworkGlobals;
import com.cisco.eManager.eManager.network.ClientTibcoMessageWorkerListener;
import com.cisco.eManager.eManager.network.ClientTibcoMessageQueue;
import com.cisco.eManager.eManager.network.TibcoMsgHandler;

public class ClientTibcoMessageProcessingWorker extends AbstractWorker
{
    private Thread               processThread = null;
    private TibrvMsg             message = null;
    private String               threadName;
    private static Integer       threadIndex;
    private static ThreadGroup   threadGroup;
    private static List          listeners;
    private static List          workers;
    private static CliTibMsgProcDebugger debugger;

    private static Logger      logger = Logger.getLogger(ClientTibcoMessageProcessingWorker.class);

    // constants
    private static final String ProcessingGroupName = "Client Tibco Msg Processor";
    private static final String ProcessThreadName = "Client Tibco Msg Processor Thread ";

    /**
     * Static constructor.
     */
    static {
	listeners = Collections.synchronizedList (new ArrayList());
	workers = Collections.synchronizedList (new ArrayList());
	threadGroup = null;
	threadIndex = new Integer (1);
	debugger = null;
    }

    /**
     */
    private ClientTibcoMessageProcessingWorker()
    {
	super (new State (State.Idle), new State (State.Idle), false);

        String propertyValue;
        Properties systemProperties;

        systemProperties = GlobalProperties.instance().getProperties();

        propertyValue = systemProperties.getProperty (NetworkGlobals.DisplayNetworkClientTibcoMsgDebuggerFrames);
        if (propertyValue != null) {
            propertyValue = propertyValue.toUpperCase();
            if (propertyValue.startsWith("Y") == true &&
                debugger == null) {
                logger.info("Displaying the Client Tibco Message Processing Worker debugger.");
                startDebuggerThread();
            }
        }
    }

    /**
     */
    static public ClientTibcoMessageProcessingWorker startWorker() throws EmanagerNetworkException
    {
        String                             threadName;
        Thread                             newThread = null;
        ClientTibcoMessageProcessingWorker worker = null;

	synchronized (threadIndex) {
	    worker = new ClientTibcoMessageProcessingWorker();

	    threadGroup = getThreadGroup();
            threadName = getThreadName();
	    try {
		if (threadGroup == null) {
                    newThread = new Thread(worker, threadName);
                } else {
                    newThread = new Thread(threadGroup, worker, threadName);
                }

                logger.info ("New ClientTibcoMessageProcessingWorker created: " + newThread.getName());
	    }
	    catch (SecurityException e) {
                EmanagerNetworkException eee;
                String logString;

                logString = EmanagerNetworkStatusCode.UnableToStartClientTibcoMsgProcessingWorker.getStatusCodeDescription() +
                            e.getMessage();

                logger.error(logString);
                eee = new EmanagerNetworkException (EmanagerNetworkStatusCode.UnableToStartClientTibcoMsgProcessingWorker,
						    logString);
                throw eee;
	    }

	    newThread.start();
	}

	return worker;
    }

    private static synchronized String getThreadName()
    {
        String threadName;

        threadName = new String (ProcessThreadName + threadIndex.toString());
        threadIndex = new Integer (threadIndex.intValue() + 1);
        return threadName;
    }

    /**
     */
    public void run()
    {
	// Record our thread identifier
	processThread = Thread.currentThread();

        logger.debug ("New ClientTibcoMessageProcessingWorker started: ");

	synchronized (workers) {
	    workers.add (this);
	}

	// continue on with processing
        while (getStopWorker () == false) {
            TibrvMsg newMsg;

	    try {
		newMsg = ClientTibcoMessageQueue.instance().getNextMessage(true);
		if (newMsg != null) {
		    logger.debug ("Client Tibco message processing started - " +
				  newMsg.toString());

		    synchronized (this) {
			setCurrentState(State.Processing);
			setMessage(newMsg);
		    }

		    processMessage ();

		    notifyMessageProcessingCompleteListeners (newMsg);

		    logger.debug ("Client Tibco message processing ended - " +
				  newMsg.toString());

		    synchronized (this) {
			setCurrentState (State.Idle);
			setMessage (null);
		    }
		}
            }
	    catch (RuntimeException e) {
		logger.error ("Runtime exception encountered: " + e);
		synchronized (this) {
		    setCurrentState (State.Idle);
		    setMessage (null);
		}
	    }
        }
    }

    protected synchronized void setCurrentState (State state)
    {
        if (state.equals(State.Idle) &&
            getDesiredState().equals(State.Stop) ) {
            super.setCurrentState(State.Stop);
        } else {
            super.setCurrentState(state);
        }
    }

    public static List getWorkers()
    {
	return workers;
    }

    static synchronized private void createThreadGroup()
    {
	if (threadGroup == null) {
	    ThreadGroup parentThreadGroup = null;

	    parentThreadGroup = NetworkGlobals.instance().getThreadGroup();

	    try {
		if (parentThreadGroup == null)
		    threadGroup = new ThreadGroup (ProcessingGroupName);
		else
		    threadGroup = new ThreadGroup (parentThreadGroup, ProcessingGroupName);
	    }
	    catch (SecurityException e) {
                String logString;

                logString = "Unable to create ClientTibcoMsgProcessingWorker thread group: " +
                            ProcessingGroupName;
                logger.error(logString);
	    }
	}
    }

    /**
     * Access method for the threadGroup property.
     *
     * @return   the current value of the threadGroup property
     */
    static protected ThreadGroup getThreadGroup()
    {
        createThreadGroup();

	return threadGroup;
    }

    protected Thread getThread()
    {
	return processThread;
    }

    /**
     * Access method for the event property.
     *
     * @return   the current value of the message property
     */
    public synchronized TibrvMsg getMessage()
    {
        return message;
    }

    /**
     * Sets the value of the event property.
     *
     * @param aEvent the new value of the message property
     */
    public synchronized void setMessage(TibrvMsg message)
    {
        this.message = message;
    }

    private void processMessage ()
    {
        logger.debug ("processMessage started: ");
	TibcoMsgHandler.instance().handleMessage(message);

    }

    /**
     * @param emanagerEventMessage
     * @roseuid 3F2552A901DD
     */
    public void notifyMessageProcessingCompleteListeners(TibrvMsg tibMsg)
    {
        Iterator iter;
        ClientTibcoMessageWorkerListener listener;

        synchronized (listeners) {
            iter = listeners.iterator();
            while (iter.hasNext()) {
                listener = (ClientTibcoMessageWorkerListener) iter.next();
                listener.clientTibcoMessageProcessingComplete(tibMsg);
            }
        }
    }


    public void notifyStateChangeListeners()
    {
        // noop
    }

    /**
     * @param listener
     */
    public static void addListener(ClientTibcoMessageWorkerListener listener)
    {
        synchronized (listeners) {
            if (listeners.contains(listener) == false) {
                listeners.add(listener);
            }
        }
    }

    /**
     * @param listener
     * @roseuid 3F43B0B300FB
     */
    public static void removeListener(ClientTibcoMessageWorkerListener listener)
    {
        synchronized (listeners) {
            listeners.remove (listener);
        }
    }

    public String toString()
    {
        StringBuffer strBuf;

        strBuf = new StringBuffer();

        strBuf.append ("Worker:" + processThread.getName());
        strBuf.append (" - Desired State: " + this.getDesiredState());
        strBuf.append (" - Current State: " + this.getCurrentState());
        strBuf.append (" - TibrvMsg: "  + message);
        return strBuf.toString();
    }


    public static String getString()
    {
        Iterator iter;
        StringBuffer strBuf;
        ClientTibcoMessageProcessingWorker worker;

        strBuf = new StringBuffer();

        synchronized (workers) {
            iter = workers.iterator();
            while (iter.hasNext()) {
                worker = (ClientTibcoMessageProcessingWorker)iter.next();
                strBuf.append ("    " + worker.toString());
            }
        }

        return strBuf.toString();
    }

    private void startDebuggerThread()
    {
        Thread t;
        ThreadGroup parentThreadGroup;

        parentThreadGroup = NetworkGlobals.instance().getThreadGroup();

        debugger = new CliTibMsgProcDebugger();
        if (parentThreadGroup == null) {
            t = new Thread (debugger, "Client Tibco Msg Worker Debugger");
        } else {
            t = new Thread (parentThreadGroup, debugger, "Client Tibco Msg Worker Debugger");
        }

        t.start();
    }

    private class CliTibMsgProcDebugger extends JFrame implements Runnable, ActionListener
    {
	private Logger logger = Logger.getLogger(CliTibMsgProcDebugger.class);

        JTextArea textArea;

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
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Client Tibco Msg Workers"),
                BorderFactory.createEmptyBorder(5,5,5,5)),
                areaScrollPane.getBorder()));

            debuggerPanel.add(areaScrollPane, BorderLayout.CENTER);

            NetworkGlobals.instance().getDebuggerPane().add("Cli Tib Msg Workers", debuggerPanel);

            frame.pack();

	    try {
		frame.setVisible(true);
	    }
	    catch (Exception e) {
		logger.warn ("Unexpectedly unable to open the ClientTibcoMsgProcessingWorker debugger frame: " + e);
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
            textArea.setText(ClientTibcoMessageProcessingWorker.getString());
        }
    }
}

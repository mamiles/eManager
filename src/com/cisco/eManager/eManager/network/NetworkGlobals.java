package com.cisco.eManager.eManager.network;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.*;

import org.apache.log4j.*;

public class NetworkGlobals {
    private static Logger      logger = Logger.getLogger(NetworkGlobals.class);

    static public String NumberClientTibcoMsgProcessingWorkersKey = "number.client.tibco.msg.processing.workers";
    static public int   NumberClientTibcoMsgProcessingWorkersDefault = 1;
    static public String DisplayNetworkClientTibcoMsgDebuggerFrames = "display.network.client.tibco.msg.debugger.frames";

    static private NetworkGlobals networkGlobals = null;
    static private ThreadGroup networkThreadGroup = null;
    static private JFrame debuggerFrame = null;
    static private JTabbedPane debuggerTabbedPane = null;

    private NetworkGlobals ()
    {
        Container container;

	try {
	    networkThreadGroup = new ThreadGroup ("Network Threads");
	}
	catch (SecurityException e) {
	    logger.error ("Error creating the network thread group: " + e.getMessage());
	    networkThreadGroup = null;
	}
        debuggerFrame = new JFrame("Network Debugger");
        container = debuggerFrame.getContentPane();
        debuggerTabbedPane = new JTabbedPane();
        container.add(debuggerTabbedPane, BorderLayout.CENTER);
    }

    /**
     * @return com.cisco.eManager.eManager.event.EventManager
     * @roseuid 3F53929D009E
     */
    public synchronized static NetworkGlobals instance()
    {
        if (networkGlobals == null) {
	    networkGlobals = new NetworkGlobals();
	}

        return networkGlobals;
    }

    /**
     * Access method for the eventThreadGroup property.
     *
     * @return   the current value of the eventThreadGroup property
     */
    public ThreadGroup getThreadGroup()
    {
	return networkThreadGroup;
    }

    public JFrame getDebuggerFrame()
    {
        return debuggerFrame;
    }

    public JTabbedPane getDebuggerPane()
    {
        return debuggerTabbedPane;
    }
}

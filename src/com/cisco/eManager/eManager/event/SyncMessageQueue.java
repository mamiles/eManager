//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\event\\SyncMessageQueue.java

package com.cisco.eManager.eManager.event;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.Component;
import java.awt.TextArea;
import java.awt.Font;
import java.awt.Dimension;

import javax.swing.*;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Properties;
import java.lang.Long;

import org.apache.log4j.*;

import com.cisco.eManager.common.event.SyncMessage;
import com.cisco.eManager.common.event.SynchronizationPriority;
import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.common.inventory.ManagedObjectId;

import com.cisco.eManager.eManager.event.SyncReport;
import com.cisco.eManager.eManager.event.EventMessageQueueListener;
import com.cisco.eManager.eManager.event.SyncProcessingWorker;
import com.cisco.eManager.eManager.event.SyncProcessingWorkerListener;
import com.cisco.eManager.eManager.event.EventGlobals;

import com.cisco.eManager.eManager.inventory.InventoryManager;
import com.cisco.eManager.eManager.inventory.ImMgmtPolicyUnload;
import com.cisco.eManager.eManager.inventory.ImMgmtPolicyLoad;
import com.cisco.eManager.eManager.inventory.ImAppInstanceCreation;
import com.cisco.eManager.eManager.inventory.ImHostActivation;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.host.Host;
import com.cisco.eManager.eManager.inventory.host.HostManager;

import com.cisco.eManager.eManager.util.GlobalProperties;

public class SyncMessageQueue implements SyncProcessingWorkerListener, EventMessageQueueListener
{
    private static Logger      logger = Logger.getLogger(SyncMessageQueue.class);

    private static SyncMessageQueue syncMessageQueue = null;
    private Integer numberSyncProcessingWorkers = new Integer (1);
    private Collection listeners;
    private Boolean workerLock = new Boolean (true);
    private Boolean queueLock = new Boolean (true);
    private List lowPrioritySyncMessageQueue;
    private List mediumPrioritySyncMessageQueue;
    private List highPrioritySyncMessageQueue;
    private Collection syncWorkers;

    /**
     * @roseuid 3F5CE3BE015B
     */
    private SyncMessageQueue()
    {
        String propertyValue;
        Properties systemProperties;

        systemProperties = GlobalProperties.instance().getProperties();

        propertyValue = systemProperties.getProperty (EventGlobals.NumberSyncProcessingWorkersKey);
        if (propertyValue == null) {
            numberSyncProcessingWorkers = new Integer(EventGlobals.NumberSyncProcessingWorkersDefault);
            logger.log(Priority.INFO,
                       "Using Default numberSyncProcessingWorkers value: " +
                       numberSyncProcessingWorkers.toString());
        } else {
            try {
                numberSyncProcessingWorkers = new Integer (propertyValue);
                logger.log(Priority.INFO,
                            "Using NumberSyncProcessingWorkers value: " +
                            propertyValue);
             }
            catch (NumberFormatException e) {
                numberSyncProcessingWorkers = new Integer (EventGlobals.NumberSyncProcessingWorkersDefault);
                logger.log(Priority.ERROR,
                           "Format exception converting NumberSyncProcessingWorkers from the " +
                           "properties.  Using the default: " +
                           numberSyncProcessingWorkers.toString());
            }
        }

        propertyValue = systemProperties.getProperty (EventGlobals.DisplayEventDebuggerFrames);
        if (propertyValue != null) {
            propertyValue = propertyValue.toUpperCase();
            if (propertyValue.startsWith("Y") == true) {
                logger.info("Displaying the SyncMessageQueue debugger.");
                startDebuggerThread();
            }
        }

        lowPrioritySyncMessageQueue = Collections.synchronizedList(new LinkedList());
        mediumPrioritySyncMessageQueue = Collections.synchronizedList(new LinkedList());
        highPrioritySyncMessageQueue = Collections.synchronizedList(new LinkedList());

        listeners = Collections.synchronizedCollection(new LinkedList());
        startWorkers();
    }

    public static synchronized SyncMessageQueue instance()
    {
        if (syncMessageQueue == null) {
            syncMessageQueue = new SyncMessageQueue();
        }

        return syncMessageQueue;
    }

    private void startWorkers()
    {
        Collection workers;
        SyncProcessingWorker syncProcessingWorker;

        workers = SyncProcessingWorker.getWorkers();

        synchronized (workers) {
            int numberWorkers;
            int currentNumberWorkers;

            currentNumberWorkers = workers.size();
            numberWorkers = numberSyncProcessingWorkers.intValue();
            if (numberWorkers > currentNumberWorkers) {
                while (numberWorkers > currentNumberWorkers) {
                    try {
                        SyncProcessingWorker.startWorker();
                        currentNumberWorkers++;
                    }
                    catch (EmanagerEventException e) {
                        String logString;

                        logString = "Exiting.  " +
                                    EmanagerEventStatusCode.UnableToStartSyncProcessingWorker.getStatusCodeDescription();
                        logger.log(Priority.FATAL, logString);
                        System.exit(1);
                    }
                }

                // I still need to figure out how to stop the required number of workers.
            }
        }
    }

    /**
     * @param listener
     * @roseuid 3F219FF0000C
     */
    public void addEventSyncQueueMessageListener(SyncMessageQueueListener listener)
    {
        logger.log(Priority.DEBUG, "Adding listener: " + listener.toString());

        synchronized (listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    /**
     * @param listener
     * @roseuid 3F21A014036A
     */
    public void removeEventSyncMessageQueueListener(SyncMessageQueueListener listener)
    {
        logger.log(Priority.DEBUG, "Removing listener: " + listener.toString());

        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    /**
     * At this point we make the simplifying assumption that there is only one
     * synchronization worker so we don't have to worry about having two agent
     * synchronizations going on at the same time.
     *
     * @param sleepUntilAvailable
     * @param sleepUntilAvailable If true, the thread will sleep until an event sync
     * message is available.  If not, the routine will return null if no sync messages
     * are available.
     * @return com.cisco.eManager.common.event.SyncMessage
     * @roseuid 3F21A065025F
     */
    public SyncMessage getNextMessage(boolean sleepUntilAvailable)
    {
        SyncMessage syncMessage;

        logger.log(Priority.DEBUG, "Enter. Thread:" + Thread.currentThread().getName());

        syncMessage = null;

	synchronized (workerLock) {
	    do {
		synchronized (queueLock) {
		    if (!highPrioritySyncMessageQueue.isEmpty()) {
			syncMessage = (SyncMessage)highPrioritySyncMessageQueue.remove(0);
		    } else if (!mediumPrioritySyncMessageQueue.isEmpty()) {
			syncMessage = (SyncMessage)mediumPrioritySyncMessageQueue.remove(0);
		    } else if (!lowPrioritySyncMessageQueue.isEmpty()) {
			syncMessage = (SyncMessage)lowPrioritySyncMessageQueue.remove(0);
		    }
		}

		if (sleepUntilAvailable == true &&
		    syncMessage == null) {
		    try {
                        logger.log(Priority.DEBUG, "Sleep waiting for next SyncMessage. Thread:" +
                                   Thread.currentThread().getName());
			workerLock.wait();
		    }
		    catch (InterruptedException e) {
		    }
		}
	    } while (sleepUntilAvailable == true &&
		     syncMessage == null);
	}

        logger.log(Priority.DEBUG, "Exit. Thread:" + Thread.currentThread().getName());

	return syncMessage;
    }

    private SyncMessage getHostSyncMessage (ManagedObjectId hostObjectId,
                                            Collection syncMessageQueue)
    {
        Iterator iter;
        SyncMessage syncMessage;

        synchronized (syncMessageQueue) {
            iter = syncMessageQueue.iterator();
            while (iter.hasNext()) {
                syncMessage = (SyncMessage) iter.next();
                if (syncMessage.getHostAgentObjectId().equals(hostObjectId)) {
                    return syncMessage;
                }
            }
        }

        return null;
    }

    /**
     * @param eventSyncMessage
     * @roseuid 3F255DF703A8
     */
    public void queueMessage(SyncMessage eventSyncMessage)
    {
        boolean     syncMessageFound;
        SyncMessage queuedSyncMessage;

        logger.log(Priority.DEBUG, "Enter. Thread:" + Thread.currentThread().getName());

        syncMessageFound = false;
        synchronized (workerLock) {
            synchronized (queueLock)
            {
                queuedSyncMessage =
                    getHostSyncMessage(eventSyncMessage.getHostAgentObjectId(), lowPrioritySyncMessageQueue);
                if (queuedSyncMessage != null)
                {
                    if (eventSyncMessage.getPriority().equals(SynchronizationPriority.Low))
                    {
                        syncMessageFound = true;
                        if (eventSyncMessage.getSyncManagementPolicies() == true)
                        {
                            queuedSyncMessage.setSyncManagementPolicies(true);
                        }
                    }
                    else
                    {
                        if (queuedSyncMessage.getSyncManagementPolicies() == true)
                        {
                            eventSyncMessage.setSyncManagementPolicies(true);
                        }
                        lowPrioritySyncMessageQueue.remove(queuedSyncMessage);
                    }
                }
                else
                {
                    queuedSyncMessage =
                        getHostSyncMessage(eventSyncMessage.getHostAgentObjectId(),
                                           mediumPrioritySyncMessageQueue);
                    if (queuedSyncMessage != null)
                    {
                        if (eventSyncMessage.getPriority().equals(SynchronizationPriority.Low) ||
                            eventSyncMessage.getPriority().equals(SynchronizationPriority.Medium))
                        {
                            syncMessageFound = true;
                            if (eventSyncMessage.getSyncManagementPolicies() == true)
                            {
                                queuedSyncMessage.setSyncManagementPolicies(true);
                            }
                        }
                        else
                        {
                            if (queuedSyncMessage.getSyncManagementPolicies() == true)
                            {
                                eventSyncMessage.setSyncManagementPolicies(true);
                            }
                            mediumPrioritySyncMessageQueue.remove(queuedSyncMessage);
                        }
                    }
                    else
                    {
                        queuedSyncMessage =
                            getHostSyncMessage(eventSyncMessage.getHostAgentObjectId(),
                                               highPrioritySyncMessageQueue);
                        if (queuedSyncMessage != null)
                        {
                            syncMessageFound = true;
                            if (eventSyncMessage.getSyncManagementPolicies() == true)
                            {
                                queuedSyncMessage.setSyncManagementPolicies(true);
                            }
                        }
                    }
                }

                if (syncMessageFound == false)
                {
                    if (eventSyncMessage.getPriority().equals(SynchronizationPriority.Low))
                    {
                        lowPrioritySyncMessageQueue.add(eventSyncMessage);
                    }
                    else if (eventSyncMessage.getPriority().equals(SynchronizationPriority.Medium))
                    {
                        mediumPrioritySyncMessageQueue.add(eventSyncMessage);
                    }
                    else if (eventSyncMessage.getPriority().equals(SynchronizationPriority.High))
                    {
                        highPrioritySyncMessageQueue.add(eventSyncMessage);
                    }
                }
            }

            wakeAllWorkers();
        }

	notifyNewMessageAddedListeners (eventSyncMessage);

        logger.log(Priority.DEBUG, "Exit. Thread:" + Thread.currentThread().getName());
    }

    /**
     * @param hostAgentObjectId
     * @roseuid 3F2D82E60281
     */
    public void removeHostAgentSynchronizationRequests(ManagedObjectId hostAgentObjectId)
    {
	Iterator iter;
	SyncMessage syncMessage;
	Collection removedMessages;

	synchronized (queueLock) {
	    syncMessage = getHostSyncMessage (hostAgentObjectId,
					      lowPrioritySyncMessageQueue);
	    if (syncMessage != null) {
		lowPrioritySyncMessageQueue.remove (syncMessage);
	    }

	    syncMessage = getHostSyncMessage (hostAgentObjectId,
					      mediumPrioritySyncMessageQueue);
	    if (syncMessage != null) {
		mediumPrioritySyncMessageQueue.remove (syncMessage);
	    }

	    syncMessage = getHostSyncMessage (hostAgentObjectId,
					      highPrioritySyncMessageQueue);
	    if (syncMessage != null) {
		highPrioritySyncMessageQueue.remove (syncMessage);
	    }
	}
    }

    /**
     * @roseuid 3F2E90340309
     */
    public void wakeSingleWorker()
    {
	synchronized (workerLock) {
	    workerLock.notify();
	}
    }

    /**
     * @roseuid 3F2E903D01EA
     */
    public void wakeAllWorkers()
    {
	synchronized (workerLock) {
	    workerLock.notifyAll();
	}
    }

    /**
     * @param syncMessage
     * @roseuid 3F2E90BE00C8
     */
    public void notifyNewMessageAddedListeners(SyncMessage syncMessage)
    {
	Iterator iter;
	SyncMessageQueueListener listener;
	synchronized (listeners) {
	    iter = listeners.iterator();
	    while (iter.hasNext()) {
		listener = (SyncMessageQueueListener) iter.next();
		listener.eventSyncMessageQueueMessageAdded (syncMessage);
	    }
	}
    }

    public void eventSyncProcessingWorkerSyncStarted(SyncProcessingWorker worker,
        SyncMessage syncMessage)
    {
	// noop at this point
    }

    public void eventSyncProcessingWorkerSyncEnded (SyncProcessingWorker worker,
        SyncReport syncReport)
    {
	wakeAllWorkers();
    }

    public void hostEventMessageQueueAdded(HostEventMessageQueue hostEventMessageQueue)
    {
        // noop
    }

    public void hostEventMessageQueueDeleted(ManagedObjectId hostAgentId)
    {
        removeHostAgentSynchronizationRequests (hostAgentId);
    }

    public String toString()
    {
        Iterator iter;
        StringBuffer strBuf;
        SyncMessage message;

        strBuf = new StringBuffer();

        strBuf.append ("Sync Message Queue" + GlobalProperties.CarriageReturn);
        strBuf.append ("------------------" + GlobalProperties.CarriageReturn);

        strBuf.append ("    High Priority Queue" + GlobalProperties.CarriageReturn);
        strBuf.append ("    -------------------" + GlobalProperties.CarriageReturn);
        synchronized (highPrioritySyncMessageQueue) {
            iter = highPrioritySyncMessageQueue.iterator();
            while (iter.hasNext()) {
                message = (SyncMessage) iter.next();
                strBuf.append ("        " + message.toString() + GlobalProperties.CarriageReturn);
            }
        }

        strBuf.append (GlobalProperties.CarriageReturn);
        strBuf.append ("    Medium Priority Queue" + GlobalProperties.CarriageReturn);
        strBuf.append ("    -------------------" + GlobalProperties.CarriageReturn);
        synchronized (mediumPrioritySyncMessageQueue) {
            iter = mediumPrioritySyncMessageQueue.iterator();
            while (iter.hasNext()) {
                message = (SyncMessage) iter.next();
                strBuf.append ("        " + message.toString() + GlobalProperties.CarriageReturn);
            }
        }

        strBuf.append (GlobalProperties.CarriageReturn);
        strBuf.append ("    Low Priority Queue" + GlobalProperties.CarriageReturn);
        strBuf.append ("    -------------------" + GlobalProperties.CarriageReturn);
        synchronized (lowPrioritySyncMessageQueue) {
            iter = lowPrioritySyncMessageQueue.iterator();
            while (iter.hasNext()) {
                message = (SyncMessage) iter.next();
                strBuf.append ("        " + message.toString() + GlobalProperties.CarriageReturn);
            }
        }

        return strBuf.toString();
    }

    private void startDebuggerThread()
    {
        Thread t;
        ThreadGroup parentThreadGroup;
        MessageQueueDebugger debugger;

        parentThreadGroup = EventGlobals.instance().getEventThreadGroup();

        debugger = new MessageQueueDebugger();
        if (parentThreadGroup == null) {
            t = new Thread (debugger, "Sync Queue Debugger");
        } else {
            t = new Thread (parentThreadGroup, debugger, "Sync Queue Debugger");
        }

        t.start();
    }

    private class MessageQueueDebugger extends JFrame implements Runnable, ActionListener
    {
	private Logger logger = Logger.getLogger(MessageQueueDebugger.class);

	JButton syncQRefresh;
        JTextArea textArea;
	JComboBox severityComboBox;
	String HighSeverity = "High";
	String LowSeverity = "Low";
	String MediumSeverity = "Medium";
	JComboBox hostComboBox;
	JButton hostRefresh;
	JCheckBox syncPolicies;
	JButton   sync;

        public MessageQueueDebugger ()
        {
        }

        private void buildDisplay()
        {
            Font font;
            JFrame frame;
            JPanel panel;
            JPanel debuggerPanel;
            JPanel panel2;

            frame = EventManager.instance().getEventDebuggerFrame();

	    // Create the Sync Queue Content Panel
            debuggerPanel = new JPanel();
            debuggerPanel.setLayout(new BorderLayout());

            syncQRefresh = new JButton ("Refresh");
            syncQRefresh.addActionListener(this);

            panel = new JPanel();
            panel.setLayout(new FlowLayout());
            panel.add (syncQRefresh);
            debuggerPanel.add (panel, BorderLayout.NORTH);

            //Create a text area.
            textArea = new JTextArea("");

            textArea.setFont(new Font("Lucida Console", Font.PLAIN, 12));
            JScrollPane areaScrollPane = new JScrollPane(textArea);
            areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            areaScrollPane.setPreferredSize(new Dimension(250, 250));
            areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Sync Queue Content"),
                BorderFactory.createEmptyBorder(5,5,5,5)),
                areaScrollPane.getBorder()));

            debuggerPanel.add(areaScrollPane, BorderLayout.CENTER);

            EventManager.instance().getEventDebuggerPane().add("Sync Queue", debuggerPanel);

	    // Create the Sync Mgmt Panel
            panel2 = new JPanel (new BorderLayout());
	    debuggerPanel = new JPanel();
	    debuggerPanel.setLayout (new BoxLayout(debuggerPanel, BoxLayout.Y_AXIS));
            panel2.add(debuggerPanel, BorderLayout.CENTER);

	    severityComboBox = new JComboBox();
            severityComboBox.addActionListener(this);
	    severityComboBox.addItem (LowSeverity);
	    severityComboBox.addItem (MediumSeverity);
	    severityComboBox.addItem (HighSeverity);
            panel = new JPanel();
            panel.setLayout (new FlowLayout());
            panel.add(severityComboBox);
	    debuggerPanel.add (panel);

	    panel = new JPanel();
            panel.setLayout (new FlowLayout());
	    panel.add (new JLabel ("Sync Mgmt Policies"));
	    syncPolicies = new JCheckBox();
            syncPolicies.addActionListener(this);
	    panel.add (syncPolicies);
	    debuggerPanel.add (panel);

	    panel = new JPanel();
	    panel.setLayout (new FlowLayout());
	    hostRefresh = new JButton ("Refresh");
            hostRefresh.addActionListener(this);
	    panel.add (hostRefresh);
	    hostComboBox = new JComboBox();
            hostComboBox.addActionListener(this);
	    hostComboBox.setRenderer (new HostRenderer());
	    panel.add (hostComboBox);
	    debuggerPanel.add (panel);

	    sync = new JButton ("Apply Sync");
            sync.addActionListener(this);
	    debuggerPanel.add (sync);

            EventManager.instance().getEventDebuggerPane().add("Sync Mgmt", panel2);

            frame.pack();

	    try {
		frame.setVisible(true);
	    }
	    catch (Exception e) {
		logger.warn ("Unexpectedly unable to open the SyncMessageQueue debugger frame: " + e);
		logger.warn ("The debugger thread will exit.");
		System.exit (1);
	    }
        }

        public void run()
        {
            buildDisplay();

        }

	private void populateHostComboBox()
	{
	    Host hosts[];

	    hostComboBox.removeAllItems();

	    hosts = HostManager.instance().allHosts();
	    for (int i = 0; i < hosts.length; i++) {
		hostComboBox.addItem (hosts[i]);
	    }
	}

	private void submitSync()
	{
	    SynchronizationPriority syncPriority;
	    String syncSeverity;
	    Host host;

	    host = (Host) hostComboBox.getSelectedItem();
	    if (host == null) {
		return;
	    }

	    syncSeverity = (String) severityComboBox.getSelectedItem();
	    if (syncSeverity.equals (LowSeverity)) {
		syncPriority = SynchronizationPriority.Low;
	    } else if (syncSeverity.equals (MediumSeverity)) {
		syncPriority = SynchronizationPriority.Medium;
	    } else {
		syncPriority = SynchronizationPriority.High;
	    }

	    queueMessage (new SyncMessage (host.id(),
					   syncPriority,
					   syncPolicies.isSelected()));
	}

        public void actionPerformed (ActionEvent e)
        {
	    if (e.getSource() == syncQRefresh) {
		textArea.setText(SyncMessageQueue.instance().toString());
	    } else if (e.getSource() == hostRefresh) {
		populateHostComboBox();
	    } else if (e.getSource() == sync) {
		submitSync();
	    }
        }
    }

    private class HostRenderer extends JLabel implements ListCellRenderer
    {
        public HostRenderer()
        {
            setHorizontalAlignment(RIGHT);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
        {
            if (value == null) {
                setText ("");
            } else if (value instanceof Host) {
                setText ( ( (Host) value).hostname() );
            } else {
                setText (value.toString());
            }

            return this;
        }
    }
}

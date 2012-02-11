//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\appInstance\\AppInstanceManager.java

package com.cisco.eManager.eManager.inventory.appInstance;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.Component;
import java.awt.TextArea;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Dimension;

import javax.swing.*;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.database.EmanagerDatabaseException;
import com.cisco.eManager.common.inventory.AppInstanceMgmtMode;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.common.inventory.Transport;
import com.cisco.eManager.eManager.inventory.InventoryGlobals;
import com.cisco.eManager.eManager.inventory.InventoryManager;
import com.cisco.eManager.eManager.inventory.appType.*;
import com.cisco.eManager.eManager.database.DatabaseInterface;
import com.cisco.eManager.eManager.network.AgentId;

import com.cisco.eManager.eManager.util.GlobalProperties;

import com.cisco.eManager.eManager.database.DatabaseInterface;

public class AppInstanceManager
    extends Observable
{
    private static Logger logger = Logger.getLogger(AppInstanceManager.class);
    private static AppInstanceManager s_instance;
    private DatabaseInterface m_db;
    private List m_appInstances;

    /**
     * public static eManagerLayer.InventoryMgmt.ApplicationInstanceFactory instance()
     * @return eManagerLayer.InventoryMgmt.ApplicationInstanceFactory
     * @roseuid 3F09D14E029A
     */
    public synchronized static AppInstanceManager instance()
    {
        logger.debug("enter");

        if (s_instance == null)
        {
            try
            {
                s_instance = new AppInstanceManager();
            }
            catch (Exception e)
            {
                logger.fatal("Exception thrown in ctor: " + e);
                s_instance = null;
            }
        }
        return s_instance;
    }

    private AppInstanceManager()
        throws Exception
    {
        logger.debug("enter");

	String propertyValue;
	Properties systemProperties;

	systemProperties = GlobalProperties.instance().getProperties();

        try
        {
            m_db = DatabaseInterface.instance();
        }
        catch (EmanagerDatabaseException e)
        {
            String reason = "exception caught while initializing the DB: " + e;
            logger.error(reason);
            throw new Exception(reason);
        }
        m_appInstances = Collections.synchronizedList(new LinkedList());
        initializeCacheFromDb();

        propertyValue = systemProperties.getProperty (InventoryGlobals.displayInventoryDebuggerFrames());
        if (propertyValue != null) {
            propertyValue = propertyValue.toUpperCase();
            if (propertyValue.startsWith("Y") == true) {
                logger.info("Displaying the AppInstanceManager debugger.");
                startDebuggerThread();
            }
        }
    }

    /**
     * @param appTypeId
     * @param hostId
     * @param name
     * @param logfileUsername
     * @param logfilePassword
     * @param logfileDirectories
     * @param logfileTransport
     * @return com.cisco.eManager.eManager.inventory.appInstance.AppInstance
     * @roseuid 3F04996B0149
     */
    public synchronized AppInstance
        createAppInstance(ManagedObjectId appTypeId,
                          ManagedObjectId hostId,
                          String name,
                          String logfileUsername,
                          String logfilePassword,
                          String logfileDirectories,
                          Transport logfileTransport,
                          String propertyFile,
                          String unixPrompt)
    {
        AppInstance newAppInstance = find1(name, appTypeId);
        logger.debug("logfileDirectories: " + logfileDirectories);
        if (newAppInstance == null)
        {
            ManagedObjectId tmpAppInstanceId =
                new ManagedObjectId(ManagedObjectIdType.ApplicationInstance, 0);
            AppInstanceData appInstanceData = null;
            try
            {
                appInstanceData =
                    new AppInstanceData(tmpAppInstanceId,
                                        hostId,
                                        appTypeId,
                                        name,
                                        AppInstanceMgmtMode.MANAGED,
                                        logfileDirectories,
                                        logfileTransport,
                                        logfileUsername,
                                        logfilePassword,
                                        unixPrompt,
                                        propertyFile);
            }
            catch (Exception ex)
            {
                logger.error("exception caught while processing appInstance " +
                             "properties: " + ex);
                return null;
            }
            // create the appInstance in the db, and capture the resulting data
            // (it will have a correct appInstance ID in it)
            try
            {
                appInstanceData = m_db.createApplicationInstance(appInstanceData);
            }
            catch (EmanagerDatabaseException ex1)
            {
                logger.error("exception caught while creating a new " +
                             "appInstance in the DB: " + ex1);
                return null;
            }
            newAppInstance = createAppInstance(appInstanceData);

            m_appInstances.add(newAppInstance);
            AppInstanceCreation ntfcnObj =
                new AppInstanceCreation(newAppInstance);
            setChanged();
            notifyObservers(ntfcnObj);
        }
        return newAppInstance;
    }

    public synchronized AppInstance
        createAppInstance(ManagedObjectId appTypeId,
                          ManagedObjectId hostId,
                          String name,
                          String logfileUsername,
                          String logfilePassword,
                          String[] logfileDirectories,
                          Transport logfileTransport,
                          String propertyFile,
                          String unixPrompt)
    {
        AppInstance newAppInstance = find1(name, appTypeId);
        if (newAppInstance == null)
        {
            ManagedObjectId tmpAppInstanceId =
                new ManagedObjectId(ManagedObjectIdType.ApplicationInstance, 0);
            AppInstanceData appInstanceData = null;

            StringBuffer lds = new StringBuffer();
            if ( logfileDirectories != null )
            {
                int count = logfileDirectories.length;
                for (int i = 0; i < count; i++)
                {
                    if (i > 0)
                    {
                        lds.append(';');
                    }
                    lds.append(logfileDirectories[i].trim());
                    logger.debug("lds = " + lds.toString());
                }
            }

            try
            {
                appInstanceData =
                    new AppInstanceData(tmpAppInstanceId,
                                        hostId,
                                        appTypeId,
                                        name,
                                        AppInstanceMgmtMode.MANAGED,
                                        lds.toString(),
                                        logfileTransport,
                                        logfileUsername,
                                        logfilePassword,
                                        unixPrompt,
                                        propertyFile);
            }
            catch (Exception ex)
            {
                logger.error("exception caught while processing appInstance " +
                             "properties: " + ex);
                return null;
            }
            // create the appInstance in the db, and capture the resulting data
            // (it will have a correct appInstance ID in it)
            try
            {
                appInstanceData = m_db.createApplicationInstance(appInstanceData);
            }
            catch (EmanagerDatabaseException ex1)
            {
                logger.error("exception caught while creating a new " +
                             "appInstance in the DB: " + ex1);
                return null;
            }
            newAppInstance = createAppInstance(appInstanceData);

            m_appInstances.add(newAppInstance);
            AppInstanceCreation ntfcnObj =
                new AppInstanceCreation(newAppInstance);
            setChanged();
            notifyObservers(ntfcnObj);
        }
        return newAppInstance;
    }

    private AppInstance createAppInstance(AppInstanceData data)
    {
        AppInstance newAppInstance = null;
        AppType appType = AppTypeManager.instance().find(data.appTypeId());
        if ( appType.name().equals(InventoryGlobals.sysAppTypeName()) )
        {
            logger.debug("creating HostResourcesAppInstance");
            newAppInstance = new HostResourcesAppInstance(data);
        }
        else
        {
            newAppInstance = new AppInstance(data);
        }
        return newAppInstance;
    }

    /**
     * @param appInstanceId
     * @roseuid 3F214FFA01F8
     */
    public synchronized void deleteAppInstance(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        logger.debug("requesting the removal of appInstance with ID " +
                     appInstanceId);
        Iterator iter = m_appInstances.iterator();
        AppInstance appInstance = null;
        while (iter.hasNext())
        {
            appInstance = (AppInstance)iter.next();
            if (appInstance.id().equals(appInstanceId))
            {
                logger.debug("appInstance found: removing it");
                try
                {
                    m_db.removeApplicationInstance(appInstance);
                }
                catch (EmanagerDatabaseException ex)
                {
                    logger.error("exception caught while removing " +
                                 "appInstance from DB: " + ex);
                    return;
                }
                iter.remove();
                AppInstanceDeletion ntfcnObj =
                    new AppInstanceDeletion(appInstance);
                setChanged();
                notifyObservers(ntfcnObj);
                break;
            }
        }
    }

    /**
     * @param appInstanceId
     * @return com.cisco.eManager.eManager.inventory.appInstance.AppInstance
     * @roseuid 3F3A705F0257
     */
    public synchronized AppInstance find(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        logger.debug("searching for appInstance with ID " + appInstanceId);
        Iterator iter = m_appInstances.iterator();
        AppInstance appInstance = null;
        while (iter.hasNext())
        {
            appInstance = (AppInstance)iter.next();
            if (appInstance.id().equals(appInstanceId))
            {
                logger.debug("appInstance found: returning it");
                return appInstance;
            }
        }
        logger.debug("unable to find appInstance: returning null");
        return null;
    }

    public synchronized List find(ManagedObjectId hostId, ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        logger.debug("searching for appInstances with Host ID: " + hostId +
                     "   App Type ID: " + appTypeId);

        List appInstances = new LinkedList();
        AppInstance appInstance = null;
        Iterator iter = m_appInstances.iterator();
        while (iter.hasNext())
        {
            {
                appInstance = (AppInstance)iter.next();
                if (appInstance.hostId().equals(hostId) &&
                    appInstance.appTypeId().equals(appTypeId))
                {
                    logger.debug("found appInstance " + appInstance.name() +
                                 ": adding it to the list");
                    appInstances.add(appInstance);
                }
            }
        }

        return appInstances;
    }

    /**
     * @param hostId
     * @param appTypeId
     * @return java.util.Vector
     * @roseuid 3F3A70A5034D
     */
    public synchronized List appInstancesByAppType(ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        logger.debug("searching for appInstances of type ID " + appTypeId);

        AppInstance appInstance = null;
        List appInstances = new LinkedList();
        Iterator iter = m_appInstances.iterator();
        while (iter.hasNext())
        {
            appInstance = (AppInstance)iter.next();
            if (appInstance.appTypeId().equals(appTypeId))
            {
                logger.debug("appInstance found for appType " + appTypeId +
                             ": appInstanceId = " + appInstance.id());
                appInstances.add(appInstance);
            }
        }
        logger.debug("found " + appInstances.size() + " appInstances of type " +
                     appTypeId);
        return appInstances;
    }

    public synchronized AppInstance[] appInstances()
    {
        int appInstanceCount = m_appInstances.size();
        logger.debug("returning " + appInstanceCount + " appInstances");
        AppInstance[] appInstances = new AppInstance[appInstanceCount];
        Iterator iter = m_appInstances.iterator();
        for (int i = 0; i < appInstanceCount; i++)
        {
            logger.debug("appInstance " + i);
            Object aiObj = iter.next();
            logger.debug("object: " + aiObj.getClass().getName());
            appInstances[i] = (AppInstance)aiObj;
        }
        return appInstances;
    }

    /**
     * @param appInstanceId
     * @roseuid 3F48F6540383
     */
    public synchronized void manage(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        logger.debug("managing appInstance with ID " + appInstanceId);

        AppInstance appInstance = find(appInstanceId);
        if (appInstance != null)
        {
            logger.debug("appInstance found: changing its management mode");
            appInstance.manage();
            try
            {
                m_db.updateApplicationInstance(appInstance);
            }
            catch (EmanagerDatabaseException ex)
            {
                logger.error("exception caught while updating appInstance " +
                             "to the \"managed\" mode: " + ex);
                appInstance.unmanage();
                return;
            }
            AppInstanceManage ntfcnObj = new AppInstanceManage(appInstance);
            setChanged();
            notifyObservers(ntfcnObj);
            return;
        }
        logger.debug("unable to find appInstance");
        return;
    }

    /**
     * @param appInstanceId
     * @roseuid 3F48F66101B5
     */
    public synchronized void unmanage(ManagedObjectId appInstanceId)
    {
        logger.debug("enter");
        logger.debug("unmanaging appInstance with ID " + appInstanceId);

        AppInstance appInstance = find(appInstanceId);
        if (appInstance != null)
        {
            logger.debug("appInstance found: changing its management mode");
            appInstance.unmanage();
            try
            {
                m_db.updateApplicationInstance(appInstance);
            }
            catch (EmanagerDatabaseException ex)
            {
                logger.error("exception caught while updating appInstance " +
                             "to the \"unmanaged\" mode: " + ex);
                appInstance.manage();
                return;
            }
            AppInstanceUnmanage ntfcnObj = new AppInstanceUnmanage(appInstance);
            setChanged();
            notifyObservers(ntfcnObj);
            return;
        }
        logger.debug("unable to find appInstance");
        return;
    }

    /**
     * @param appInstanceName
     * @param hostId
     * @return com.cisco.eManager.eManager.inventory.appInstance.AppInstance
     * @roseuid 3F671F8A00C6
     */
    public synchronized AppInstance find(String appInstanceName,
                                         ManagedObjectId hostId)
    {
        logger.debug("enter");
        logger.debug("searching for appInstance with name " + appInstanceName +
                     " on host " + hostId);

        AppInstance appInstance = null;
        Iterator iter = m_appInstances.iterator();
        while (iter.hasNext())
        {
            appInstance = (AppInstance)iter.next();
            if (appInstance.name().equals(appInstanceName) &&
                appInstance.hostId().equals(hostId))
            {
                logger.debug("appInstance found: returning it");
                return appInstance;
            }
        }
        logger.debug("unable to find appInstance: returning null");
        return null;
    }

    public synchronized AppInstance find1(String appInstanceName,
                                          ManagedObjectId appTypeId)
    {
        logger.debug("enter");
        logger.debug("searching for appInstance with name " + appInstanceName +
                     " of type " + appTypeId.toString());

        AppInstance appInstance = null;
        Iterator iter = m_appInstances.iterator();
        while (iter.hasNext())
        {
            appInstance = (AppInstance)iter.next();
            if (appInstance.name().equals(appInstanceName) &&
                appInstance.appTypeId().equals(appTypeId))
            {
                logger.debug("appInstance found: returning it");
                return appInstance;
            }
        }
        logger.debug("unable to find appInstance: returning null");
        return null;
    }

    /**
     * @param hostId
     * @return java.util.Vector
     * @roseuid 3F68EE59024B
     */
    public synchronized List appInstancesByHost(ManagedObjectId hostId)
    {
        logger.debug("enter");
        logger.debug("searching for appInstances on host with ID " + hostId);

        AppInstance appInstance = null;
        List appInstances = new LinkedList();
        Iterator iter = m_appInstances.iterator();
        while (iter.hasNext())
        {
            appInstance = (AppInstance)iter.next();
            if (appInstance.hostId().equals(hostId))
            {
                logger.debug("appInstance found on host " + hostId +
                             ": appInstanceId = " + appInstance.id());
                appInstances.add(appInstance);
            }
        }
        logger.debug("found " + appInstances.size() + " appInstances on host " +
                     hostId);
        return appInstances;
    }

    /**
     * @param name
     * @return com.cisco.eManager.eManager.inventory.appInstance.AppInstance
     * @roseuid 3F6E009202EE
     */
/*
    public synchronized AppInstance find(String name)
    {
        logger.debug("enter");
        logger.debug("looking for appInstance with name " + name);
        AppInstance appInstance = null;
        Iterator iter = m_appInstances.iterator();
        while (iter.hasNext())
        {
            appInstance = (AppInstance)iter.next();
            //logger.debug("examining appInstance " + appInstance.name());
            if (appInstance.name().equals(name))
            {
                logger.debug("appInstance " + name + " found");
                return appInstance;
            }
        }
        logger.debug("appInstance " + name + " not found - return null");
        return null;
    }
*/
    public synchronized void setPropertyFile(AppInstance appInstance,
                                             String propertyFile)
    {
        String oldVal = appInstance.propertyFile();
        appInstance.propertyFile(propertyFile);
        try
        {
            m_db.updateApplicationInstance(appInstance);
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught: " + ex);
            appInstance.propertyFile(oldVal);
        }
        return;
    }

    public synchronized void setUnixPrompt(AppInstance appInstance,
                                           String unixPrompt)
    {
        String oldVal = appInstance.unixPrompt();
        appInstance.unixPrompt(unixPrompt);
        try
        {
            m_db.updateApplicationInstance(appInstance);
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught: " + ex);
            appInstance.unixPrompt(oldVal);
        }
        return;
    }

    public synchronized void setLogfileDirectories(AppInstance appInstance,
                                                   String lfd)
    {
        String oldVal = appInstance.logfileDirectories();
        appInstance.logfileDirectories(lfd);
        try
        {
            m_db.updateApplicationInstance(appInstance);
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught: " + ex);
            appInstance.logfileDirectories(oldVal);
        }
        return;
    }

    public synchronized void setLogfileDirectories(AppInstance appInstance,
                                                   String[] lfd)
    {
        String[] oldVal = appInstance.logfileDirectorySet();
        appInstance.logfileDirectorySet(lfd);
        try
        {
            m_db.updateApplicationInstance(appInstance);
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught: " + ex);
            appInstance.logfileDirectorySet(oldVal);
        }
        return;
    }

    public synchronized void setLogfilePassword(AppInstance appInstance,
                                                String lfp)
    {
        String oldVal = appInstance.logfilePassword();
        appInstance.logfilePassword(lfp);
        try
        {
            m_db.updateApplicationInstance(appInstance);
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught: " + ex);
            appInstance.logfilePassword(oldVal);
        }
        return;
    }

    public synchronized void setLogfileUsername(AppInstance appInstance,
                                                String lfu)
    {
        String oldVal = appInstance.logfileUsername();
        appInstance.logfileUsername(lfu);
        try
        {
            m_db.updateApplicationInstance(appInstance);
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught: " + ex);
            appInstance.logfileUsername(oldVal);
        }
        return;
    }

    public synchronized void setLogfileTransport(AppInstance appInstance,
                                                 Transport lft)
    {
        Transport oldVal = appInstance.logfileTransport();
        appInstance.logfileTransport(lft);
        try
        {
            m_db.updateApplicationInstance(appInstance);
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("exception caught: " + ex);
            appInstance.logfileTransport(oldVal);
        }
        return;
    }

    private void initializeCacheFromDb()
    {
        logger.debug("enter");
        Collection appInstanceDatas = null;
        try
        {
            appInstanceDatas = m_db.retrieveApplicationInstances();
        }
        catch (EmanagerDatabaseException e)
        {
            logger.error("exception caught while initializing cache from db: " +
                         e);
            return;
        }
        Iterator iter = appInstanceDatas.iterator();
        AppInstanceData appInstanceData = null;
        AppInstance appInstance = null;
        while (iter.hasNext())
        {
            appInstanceData = (AppInstanceData)iter.next();
            appInstance = createAppInstance(appInstanceData);
            m_appInstances.add(appInstance);
            logger.debug("added \"" + appInstance.name() + "\" to the cache");
            AppInstanceRestoration ntfcnObj = new AppInstanceRestoration(appInstance);
            setChanged();
            notifyObservers(ntfcnObj);
        }
    }

    public String toString()
    {
        Iterator iter;
        StringBuffer strBuf;

        strBuf = new StringBuffer();

        synchronized (m_appInstances) {
            iter = m_appInstances.iterator();
            while (iter.hasNext()) {
                strBuf.append( ( (AppInstance) iter.next()).toString() );
                strBuf.append (GlobalProperties.CarriageReturn);
            }
        }

        return strBuf.toString();
    }

    private void startDebuggerThread()
    {
        Thread t;
        ThreadGroup parentThreadGroup;
        Debugger debugger;

        debugger = new Debugger();
	t = new Thread (debugger, "Inventory App Instance Debugger");

        parentThreadGroup = GlobalProperties.instance().getDebuggerThreadGroup();

        debugger = new Debugger();
        if (parentThreadGroup == null) {
            t = new Thread (debugger, "App Instance Debugger");
        } else {
            t = new Thread (parentThreadGroup, debugger, "App Instance Debugger");
        }

        t.start();
    }

    private class Debugger extends JFrame implements Runnable, ActionListener
    {
	private Logger      logger = Logger.getLogger(Debugger.class);

        private JTextArea textArea;
        private JButton   refresh;
        private JButton   apply;
        private JButton   refreshMgr;
        private JComboBox commands;
        private String ManageCommand = "Manage";
        private String UnmanageCommand = "Unmanage";
        private String DeleteCommand = "Delete";
        private String StartCommand = "Start";
        private String StopCommand = "Stop";
        private JComboBox appInstancesComboBox;

        public Debugger ()
        {
        }

        private void buildDisplay()
        {
            Font font;
            JFrame frame;
            JPanel panel;
            JPanel panel2;
            JPanel debuggerPanel;

            InventoryManager im = null;
            try
            {
                im = InventoryManager.instance();
            }
            catch (Exception ex)
            {
                System.err.println("exception caught from " +
                                   "InventoryManager.instance(): " + ex);
            }

            frame = im.getDebuggerFrame();

            debuggerPanel = new JPanel();
            debuggerPanel.setLayout(new BorderLayout());

            //
            // Create the Status Area Panel
            //
            refresh = new JButton("Refresh");
            refresh.addActionListener(this);

            panel = new JPanel();
            panel.setLayout(new FlowLayout());
            panel.add(refresh);
            debuggerPanel.add(panel, BorderLayout.NORTH);

            //Create a text area.
            textArea = new JTextArea("");

            textArea.setFont(new Font("Lucida Console", Font.PLAIN, 12));
            JScrollPane areaScrollPane = new JScrollPane(textArea);
            areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            areaScrollPane.setPreferredSize(new Dimension(250, 250));
            areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("App Instance State"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                areaScrollPane.getBorder()));

            debuggerPanel.add(areaScrollPane, BorderLayout.CENTER);

            im.getDebuggerPane().add("App Inst. State", debuggerPanel);

            //
            //  Create Manager Panel
            //
            debuggerPanel = new JPanel();
            debuggerPanel.setLayout(new BorderLayout());

            apply = new JButton("Apply");
            apply.addActionListener(this);

            commands = new JComboBox();
            commands.addItem(ManageCommand);
            commands.addItem(UnmanageCommand);
            commands.addItem(StartCommand);
            commands.addItem(StopCommand);
            commands.addItem(DeleteCommand);

            panel = new JPanel();
            panel.setLayout(new FlowLayout());
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("App Instance Commands"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                panel.getBorder()));

            panel.add(commands);
            panel.add(apply);

            debuggerPanel.add(panel, BorderLayout.NORTH);

            panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("App Instances"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                panel.getBorder()));

            refreshMgr = new JButton("Refresh");
            refreshMgr.addActionListener(this);

            panel2 = new JPanel();
            panel2.setLayout(new FlowLayout());
            panel2.add(refreshMgr);

            panel.add(panel2, BorderLayout.NORTH);

            appInstancesComboBox = new JComboBox();
            appInstancesComboBox.setRenderer(new AppInstanceRenderer());

            panel2 = new JPanel();
            panel2.setLayout (new FlowLayout());
            panel2.add (appInstancesComboBox);

            panel.add (panel2, BorderLayout.CENTER);

            debuggerPanel.add (panel, BorderLayout.CENTER);

            im.getDebuggerPane().add("App Inst. Mgr", debuggerPanel);

            frame.pack();

	    try {
		frame.setVisible(true);
	    }
	    catch (Exception e) {
		logger.warn ("Unexpectedly unable to open the AppInstanceManager debugger frame: " + e);
		logger.warn ("The debugger thread will exit.");
		System.exit (1);
	    }
        }

        private void populateAppInstancesComboBox()
        {
            Iterator iter;

            appInstancesComboBox.removeAllItems();

            synchronized (m_appInstances) {
                iter = m_appInstances.iterator();
                while (iter.hasNext()) {
                    appInstancesComboBox.addItem (iter.next());
                }
            }
        }

        public void run()
        {
            buildDisplay();

        }

        public void actionPerformed (ActionEvent e)
        {
            if (e.getSource() == refresh) {
                textArea.setText(AppInstanceManager.instance().toString());
            } else if (e.getSource() == apply) {
                AppInstance appInstance;

                appInstance = (AppInstance) appInstancesComboBox.getSelectedItem();
                if (appInstance != null) {
                    String command;

                    command = (String) commands.getSelectedItem();
                    if (command != null) {
                        if (command.equals(ManageCommand)) {
                            InventoryManager.instance().manageAppInstance(appInstance.id());
                        } else if (command.equals(UnmanageCommand)) {
                            InventoryManager.instance().unmanageAppInstance(appInstance.id());
                        } else if (command.equals(StartCommand)) {
                            try
                            {
                                appInstance.start();
                            }
                            catch (Exception ex)
                            {
                                logger.info("Exception caught: " + ex);
                            }
                        } else if (command.equals(StopCommand)) {
                            try
                            {
                                appInstance.stop();
                            }
                            catch (Exception ex)
                            {
                                logger.info("Exception caught: " + ex);
                            }

                        } else if (command.equals(DeleteCommand)) {
                            InventoryManager.instance().deleteAppInstance(appInstance.id());
                        }
                    }
                }
            } else if (e.getSource() == refreshMgr) {
                populateAppInstancesComboBox();
            }
        }
    }

    private class AppInstanceRenderer extends JLabel implements ListCellRenderer
    {
        public AppInstanceRenderer()
        {
            setHorizontalAlignment(CENTER);
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
            } else if (value instanceof AppInstance) {
                setText ( ( (AppInstance) value).name() );
            } else {
                setText (value.toString());
            }

            return this;
        }
    }
}

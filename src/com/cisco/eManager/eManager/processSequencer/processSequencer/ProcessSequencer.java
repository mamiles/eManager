package com.cisco.eManager.eManager.processSequencer.processSequencer;

import com.cisco.eManager.eManager.processSequencer.common.*;
import com.cisco.eManager.eManager.processSequencer.common.logging.*;
import com.cisco.eManager.common.register.solutionRegistration.*;
import org.exolab.castor.xml.*;
import java.io.*;
import java.util.*;
import java.util.Properties;
import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;
import COM.TIBCO.hawk.console.hawkeye.*;
import COM.TIBCO.hawk.talon.*;
import java.util.logging.*;

/**
 * <p>Title: Process Sequencer / Watchdog</p>
 * <p>Description: eManger</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems</p>
 * @author Marvin Miles
 * @version 1.0
 *
 */

public class ProcessSequencer {

    private final String WATCHDOG_MICRO_AGENT_NAME = "com.cisco.eManager.eManager.processSequencer.watchdog.WD-";
    public static long startTime;
    public static ProcessSequencer processSequencer;
    protected CiscoLogger mLogger;
    protected String mDomain;
    protected String mService;
    protected String mNetwork;
    protected String mDaemon;
    protected String emHome;
    private ProcessSequencerAMI mProcessSequencerAMI = null;
    private TibrvQueue mRvQueue = null;

    protected TIBHawkConsole mConsole;
    protected AgentManager   mAgentMgr;
    protected TibcoManager mTibcoManager = null;

    private final String SOLUTION_REGISTRATION = "SolutionRegistration";

    public ProcessSequencer() {
        mLogger = CiscoLogger.getCiscoLogger("processSequencer");
        mLogger.info("Process Sequencer starting up now");
        setupConfig();
        try {
            mRvQueue = new TibrvQueue();
            //mTibcoManager = TibcoManager.instance();
        }
        catch (TibrvException e) {
            mLogger.severe("TibrvException: " + e);
            e.printStackTrace();
        }
        try {
            mProcessSequencerAMI = new ProcessSequencerAMI(this, mService,
                mNetwork, mDaemon, mRvQueue);
        }
        catch (AmiException e) {
            mLogger.severe("AmiException: " + e);
            e.printStackTrace();
        }

        // Setup the Console and initialize the agent manager
        mConsole = new TIBHawkConsole(mDomain,mService,mNetwork,mDaemon);
        mAgentMgr = mConsole.getAgentManager();
        try {
            mAgentMgr.initialize();
        }
        catch( Exception e ) {
            mLogger.severe("ERROR while initializing AgentManager: " + e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        try {
            Tibrv.open(Tibrv.IMPL_NATIVE);
            processSequencer = new ProcessSequencer();
            processSequencer.execute();
            Tibrv.close();
        }
        catch (TibrvException e) {
            System.err.println("Failed to open Tibrv in native implementation:");
            e.printStackTrace();
            System.exit(0);
        }
        System.exit(0);
    }

    public void execute() {
        TibrvTransport transport = null;

        try {
            mLogger.finest("Create TibrvRvdTranport");
            transport = new TibrvRvdTransport(mService, mNetwork, mDaemon);
        }
        catch (TibrvException e) {
            mLogger.severe("Failed to create TibrvRvdTransport\n Exception: " + e.getMessage());
            e.printStackTrace(System.out);
            System.exit(0);
        }

        while (true) {
            try {
                mRvQueue.dispatch();
            }
            catch (java.lang.Throwable caughtThrowable) {
                break;
            }
        }
    }

    public String getEMHome() {
        return emHome;
    }

    public void register(String appType, String appInst, String appPropFile) throws
        PSRuntimeException {
        String appName;
        FileOutputStream fos;
        BufferedOutputStream bos;
        String localAppPropertiesFile;

        if (appType.equals("eManager") & appInst.equals("app")) {
            mLogger.severe("PROTECTED: Can not register appType: eManager appInst: app");
            throw new PSRuntimeException("PROTECTED: Can not register appType: eManager appInst: app");
        }

        mLogger.info("Prepairing to register " + appType + "." + appInst + " Property File: " +
                     appPropFile);

        try {
            appName = validateAppName(appType, appInst);
        }
        catch (PSRuntimeException ex) {
            throw new PSRuntimeException(ex.getMessage());
        }

        // Remove the registration if it exists
        unregister(appType, appInst);

        // Copy the application property file to the processSequencer directory
        localAppPropertiesFile = emHome + "/config/processSequencer/" + appName +
            ".properties";
        File appPropertyFile = new File(appPropFile);
        if (appPropertyFile.canRead()) {
            try {
                FileInputStream fis = new FileInputStream(appPropFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                fos = new FileOutputStream(localAppPropertiesFile);
                bos = new BufferedOutputStream(fos);
                int b;
                while ( (b = bis.read()) != -1) {
                    bos.write(b);
                }
                bis.close();
                bos.close();
            }
            catch (IOException e) {
                unregister(appType, appInst);
                mLogger.severe(e.toString());
                throw new PSRuntimeException(
                    "Can not copy application Property File: " + appPropFile);
            }
        }
        else {
            mLogger.severe("Can not read application Property File: " +
                           appPropFile);
            throw new PSRuntimeException(
                "Can not read application Property File: " + appPropFile);
        }

        // Create the Watchdog Binary for the application
        createAppWatchdogHardLink(appName);

        mLogger.finest("Checking to see if Watchdog binary link was created successfully");
        File wdBin = new File(emHome + "/bin/solaris/WD." + appName);
        if (!wdBin.exists()) {
            unregister(appType, appInst);
            mLogger.severe("Error creating Watchdog Binary hard link.");
            throw new PSRuntimeException("Error creating Watchdog Binary hard link.");
        }
        else {
            mLogger.info("Registration successfull");
        }
    }

    public void registerSolution(String xmlString) {
        mLogger.finest("entered");
        UnmarshalSolution xmlThread = new UnmarshalSolution(xmlString);
        xmlThread.start();
        try {
            xmlThread.join(60000);
        }
        catch (InterruptedException ex1) {
            mLogger.severe("SolutionXml interrupted while unmarshaling xmlString");
            throw new PSRuntimeException("SolutionXml interrupted while unmarshaling xmlString");
        }
        com.cisco.eManager.common.register.solutionRegistration.SolutionRegistration reg = xmlThread.getSolutionRegistration();

        mLogger.fine("Solution XML String: " + xmlString);
        mLogger.info("Prepairing to register solution: " + reg.getSolutionName());

        String appName;
        Components comps = reg.getComponents();
        Enumeration enum = comps.enumerateComponent();
        while (enum.hasMoreElements()) {
            Component comp = (Component)enum.nextElement();
            mLogger.finest("Prepairing to validate appType: " + comp.getAppType() + " and appInst: " + comp.getAppInstance());
            appName = validateAppName(comp.getAppType(), comp.getAppInstance());

            if (isRegistered(appName)) {
                String localAppPropertiesFile = emHome + "/config/processSequencer/" + appName +
                                         ".properties";
                File appPropertyFile = new File(localAppPropertiesFile);
                Properties appProperties = new Properties();
                if (appPropertyFile.canRead()) {
                    try {
                        FileInputStream fis = new FileInputStream(appPropertyFile);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        appProperties.load(bis);
                        bis.close();
                        Object solution = appProperties.setProperty("solution.name", reg.getSolutionName());
                        if ( solution != null ) {
                            mLogger.info("Appliation: " + appName + " already registered to: " + solution);
                        }
                        mLogger.info("Appliation: " + appName + " now registered to: " + reg.getSolutionName());
                        FileOutputStream fos = new FileOutputStream(appPropertyFile);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        appProperties.store(bos, "Properties file for " + appName);
                        bos.close();
                    }
                    catch (IOException e) {
                        mLogger.severe(e.toString());
                        throw new PSRuntimeException(
                            "Can not read/write application Property File: " + localAppPropertiesFile);
                    }
                }
                else {
                    mLogger.severe("Can not read application Properties file: " + localAppPropertiesFile);
                    throw new PSRuntimeException(
                            "Can not read application Property File: " + localAppPropertiesFile);
                }
            }
            else {
                mLogger.severe("Watchdog application (" + appName + ") not registered");
                throw new PSRuntimeException("Watchdog application (" + appName + ") not registered");
            }

        }
    }

    public void unregister(String appType, String appInst) {
        mLogger.info("Prepairing to unregister " + appType + "." + appInst);
        String appName;

        if (appType.equals("eManager") & appInst.equals("app")) {
            mLogger.severe("PROTECTED: Can not unregister appType: eManager appInst: app");
            throw new PSRuntimeException("PROTECTED: Can not unregister appType: eManager appInst: app");
        }

        try {
            appName = validateAppName(appType, appInst);
        }
        catch (PSRuntimeException ex) {
            throw new PSRuntimeException(ex.getMessage());
        }
        removeAppWatchdogHardLink(appName);
        File localPropertyFile = new File(emHome + "/config/processSequencer/" +
                                          appName + ".properties");
        if (localPropertyFile.exists()) {
            localPropertyFile.delete();
        }
    }

    private boolean isRegistered(String appName) {
        File localPropertyFile = new File(emHome + "/config/processSequencer/" +
                                          appName + ".properties");
        return (localPropertyFile.exists());
    }

    public void startApplication(String appType, String appInst) {
        mLogger.info("Prepairing to Start application: " + appType + "." + appInst);
        String appName;
        try {
            appName = validateAppName(appType, appInst);
        }
        catch (PSRuntimeException ex) {
            throw new PSRuntimeException(ex.getMessage());
        }
        if (!isRegistered(appName)) {
            mLogger.severe("Watchdog application (" + appName + ") not registered");
            throw new PSRuntimeException("Watchdog application (" + appName + ") not registered");
        }
        try
        {
            mLogger.fine("Calling TibcoManager.instance().manageApplication(appType, appInst)");
            TibcoManager.instance().manageApplication(appType, appInst);
            mLogger.fine("Returning TibcoManager.instance().manageApplication(appType, appInst)");
        }
        catch (TibrvException ex2)
        {
            mLogger.severe("TibrvException trying send message to manage application");
        }

        String command = emHome + "/bin/startwd " + appType + " " + appInst;

        ExecuteNativeProcessThread script = new ExecuteNativeProcessThread(command);
        script.start();
        try {
            script.join(60000);
        }
        catch (InterruptedException ex1) {
            mLogger.severe("Script interrupted while starting Watchdog application: " + appName);
            throw new PSRuntimeException("Script interrupted while starting Watchdog application: " +
                                         appName);
        }
        int rc = script.getExitValue();

        if (rc == 2) {
            mLogger.severe("Watchdog application (" + appName + ") already running");
            throw new PSRuntimeException("Watchdog application (" + appName + ") already running");
        }
        if (rc != 0) {
            mLogger.severe("Error starting Watchdog application: " + appName);
            throw new PSRuntimeException("Error starting Watchdog application: " + appName);
        }
        else {
            mLogger.info(appName + " now Started");
        }
    }

    public void stopApplication(String appType, String appInst) {
        mLogger.info("Prepairing to Stop application: " + appType + "." + appInst);
        if (appType.equals("eManager") & appInst.equals("app")) {
            mLogger.severe("Can't bring down eManager.app from Process Sequencer");
            throw new PSRuntimeException("Can't bring down eManager.app from Process Sequencer");
        }
        String appName;
        try {
            appName = validateAppName(appType, appInst);
        }
        catch (PSRuntimeException ex) {
            throw new PSRuntimeException(ex.getMessage());
        }
        if (!isRegistered(appName)) {
            mLogger.severe("Watchdog application (" + appName + ") not registered");
            throw new PSRuntimeException("Watchdog application (" + appName + ") not registered");
        }
        try
        {
            mLogger.fine("Calling TibcoManager.instance().unmanageApplication(appType, appInst)");
            TibcoManager.instance().unmanageApplication(appType, appInst);
        }
        catch (TibrvException ex2)
        {
            mLogger.severe("TibrvException trying send message to unmanage application");
        }

        String command = emHome + "/bin/stopwd " + appType + " " + appInst;

        ExecuteNativeProcessThread script = new ExecuteNativeProcessThread(command);
        script.start();
        try {
            script.join(60000);
        }
        catch (InterruptedException ex1) {
            mLogger.severe("Script interrupted while stopping Watchdog application: " + appName);
            throw new PSRuntimeException("Script interrupted while stopping Watchdog application: " +
                                         appName);
        }

        if (script.getExitValue() != 0) {
            mLogger.severe("Watchdog not running or Error stopping Watchdog application: " +
                           appName);
            throw new PSRuntimeException(
                "Watchdog not running or Error stopping Watchdog application: " + appName);
        }
        else {
            mLogger.info(appName + " now Stoppped");
        }
    }

    private void setupConfig() {
        mService = DCPLib.getSystemProperty("tibhawk.service", "7474");
        mLogger.finest("TibHawk UDP Service: " + mService);
        mNetwork = DCPLib.getSystemProperty("tibhawk.network", null);
        mLogger.finest(
            "TibHawk network to use for outbound session communications: " +
            mNetwork);
        mDaemon = DCPLib.getSystemProperty("tibhawk.daemon", "tcp:7474");
        mLogger.finest(
            "TIBCO Rendezvous daemon to handle communication for the session: " +
            mDaemon);
        mDomain = DCPLib.getSystemProperty("tibhawk.domain", "default");
        mLogger.finest("TibHawk Domain on which the console is to communicate: " + mDomain);
        emHome = System.getProperty("em.home");
        mLogger.finest("eManager Home directory (EM_HOME):" + emHome);
    }

    private String validateAppName(String appType, String appInst) throws
        PSRuntimeException {
        String appTypeError = "appType: " + appType +
            " must only contain a-z, A-Z, 0-9, _, or - characters";
        for (int i = 1; i < appType.length(); i++) {
            if (! ( (appType.charAt(i) >= 'a' && appType.charAt(i) <= 'z') ||
                   (appType.charAt(i) >= 'A' && appType.charAt(i) <= 'Z') ||
                   (appType.charAt(i) >= '0' && appType.charAt(i) <= '9') ||
                   appType.charAt(i) == '_' || appType.charAt(i) == '-')) {
                mLogger.severe(appTypeError);
                throw new PSRuntimeException(appTypeError);
            }
        }

        String appInstError = "appInst: " + appInst +
            " must only contain a-z, A-Z, 0-9, _, or - characters";
        for (int i = 1; i < appInst.length(); i++) {
            if (! ( (appInst.charAt(i) >= 'a' && appInst.charAt(i) <= 'z') ||
                   (appInst.charAt(i) >= 'A' && appInst.charAt(i) <= 'Z') ||
                   (appInst.charAt(i) >= '0' && appInst.charAt(i) <= '9') ||
                   appInst.charAt(i) == '_' || appInst.charAt(i) == '-')) {
                mLogger.severe(appInstError);
                throw new PSRuntimeException(appInstError);
            }
        }
        return appType + "." + appInst;
    }

    private void createAppWatchdogHardLink(String appName) {
        String wdBin = emHome + "/bin/solaris/nWatchDog";
        String wdLink = emHome + "/bin/solaris/WD." + appName;
        String command = "/usr/bin/ln " + wdBin + " " + wdLink;
        ExecuteNativeProcessThread script = new ExecuteNativeProcessThread(command);
        script.start();
        try {
            script.join(60000);
        }
        catch (InterruptedException ex1) {
            mLogger.severe("Script interrupted while createing WatchDog Binary: " + wdLink);
            throw new PSRuntimeException("Script interrupted while createing WatchDog Binary: " +
                                         wdLink);
        }
        if (script.getExitValue() != 0) {
            mLogger.severe("Error createing WatchDog Binary: " + wdLink);
            throw new PSRuntimeException("Error createing WatchDog Binary: " +
                                         wdLink);
        }
    }

    private void removeAppWatchdogHardLink(String appName) {
        String wdLink = emHome + "/bin/solaris/WD." + appName;
        File wdFile = new File(wdLink);
        if (wdFile.exists()) {
            wdFile.delete();
        }
    }

    public String getLogLevelName() {
        return mLogger.getLevel().getName();
    }

    public void setLogLevel(String name) {
        mLogger.info("Setting log leverl to: " + name);
        if (name.equalsIgnoreCase("ALL")) {
            mLogger.setLevel(Level.ALL);
        }
        else if (name.equalsIgnoreCase("CONFIG")) {
            mLogger.setLevel(Level.CONFIG);
        }
        else if (name.equalsIgnoreCase("FINE")) {
            mLogger.setLevel(Level.FINE);
        }
        else if (name.equalsIgnoreCase("FINER")) {
            mLogger.setLevel(Level.FINER);
        }
        else if (name.equalsIgnoreCase("FINEST")) {
            mLogger.setLevel(Level.FINEST);
        }
        else if (name.equalsIgnoreCase("INFO")) {
            mLogger.setLevel(Level.INFO);
        }
        else if (name.equalsIgnoreCase("OFF")) {
            mLogger.setLevel(Level.OFF);
        }
        else if (name.equalsIgnoreCase("SEVERE")) {
            mLogger.setLevel(Level.SEVERE);
        }
        else if (name.equalsIgnoreCase("WARNING")) {
            mLogger.setLevel(Level.WARNING);
        }
        else {
            mLogger.info("Error setting level.");
            throw new PSRuntimeException("Error setting level.  Level must be SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL, or OFF");
        }
    }

    private MicroAgentID[] getWatchdogMicroAgentID(String appType, String appInst) {
        MicroAgentID[] maids = null;

        try {
            maids = mAgentMgr.getMicroAgentIDs(WATCHDOG_MICRO_AGENT_NAME + appType + "-" + appInst, 1);
            if( maids.length == 0 ) {
                mLogger.severe("ERROR: Unable to find any TIBCO Hawk Micro Agents for Watchdog WD-" + appType + "-" + appInst);
                throw new PSRuntimeException("ERROR: Unable to find any TIBCO Hawk Micro Agents for Watchdog WD-" + appType + "-" + appInst);
            }
            mLogger.fine(maids[0].toString());
        }
        catch( MicroAgentException mae ) {
            mLogger.severe("ERROR while performing getMicroAgentIDs: " + mae);
            throw new PSRuntimeException("ERROR while performing getMicroAgentIDs: " + mae.getMessage());
        }
        return maids;
    }

    private String invokeGetEMHome(MicroAgentID[] maids) {
        String em_home = null;
        String methName = "getEMHome";
        MethodInvocation mi = new MethodInvocation(methName, null);
        try {
            MicroAgentData m = mAgentMgr.invoke(maids[0], mi);
            Object maData = m.getData();
            if( maData != null ) {
                mLogger.fine("Results of Method Invocation");
                if( maData instanceof CompositeData ) {
                    CompositeData compData = (CompositeData)maData;
                    DataElement[] data = compData.getDataElements();
                    em_home = (String)data[0].getValue();
                }
                else if( maData instanceof MicroAgentException ) {
                    MicroAgentException exc = (MicroAgentException)maData;
                    mLogger.severe("Exception returned from method: " + exc);
                    throw new PSRuntimeException("Exception returned from method: " + exc);
                }
                else if( maData == null ) {
                    mLogger.severe("Method Invocation returned NO data ");
                    throw new PSRuntimeException("Method Invocation returned NO data ");
                }
                else {
                    mLogger.severe("Method Invocation returned data of UNKNOWN TYPE");
                }
            }
        }
        catch( MicroAgentException me ) {
            mLogger.severe("ERROR while performing a method invocation: " + me);
            throw new PSRuntimeException("ERROR while performing a method invocation: " + me.getMessage());
        }
        mLogger.info("em_home: " + em_home);
        return em_home;

    }

    private Boolean invokeGetHealth(MicroAgentID[] maids) {
        mLogger.info("invokeGetHealth");
        Boolean isHealthy = new Boolean(false);
        String methName = "getHealth";
        MethodInvocation mi = new MethodInvocation(methName, null);
        try {
            MicroAgentData m = mAgentMgr.invoke(maids[0], mi);
            Object maData = m.getData();
            if( maData != null ) {
                mLogger.fine("Results of Method Invocation");
                if( maData instanceof CompositeData ) {
                    CompositeData compData = (CompositeData)maData;
                    DataElement[] data = compData.getDataElements();
                    isHealthy = (Boolean)data[0].getValue();
                }
                else if (maData instanceof TabularData) {
                    // logger.debug("Result is TabularData");
                    TabularData tabData = (TabularData)maData;
                    DataElement [][] table = tabData.getAllDataElements();
                    if (table != null) {
                        for (int i=0; i<table.length; i++)
                        {
                            Vector row = new Vector();
                            for (int j=0; j<table[i].length; j++)
                            {
                                if (table[i][j].getName().equals("isHealthy")) {
                                    isHealthy = (Boolean)table[i][j].getValue();
                                    return isHealthy;
                                }
                            }

                        }
                    }
                }
                else if( maData instanceof MicroAgentException ) {
                    MicroAgentException exc = (MicroAgentException)maData;
                    mLogger.severe("Exception returned from method: " + exc);
                    throw new PSRuntimeException("Exception returned from method: " + exc);
                }
                else if( maData == null ) {
                    mLogger.severe("Method Invocation returned NO data ");
                    throw new PSRuntimeException("Method Invocation returned NO data ");
                }
                else {
                    mLogger.severe("Method Invocation returned data of UNKNOWN TYPE");
                }
            }
        }
        catch( MicroAgentException me ) {
            mLogger.severe("ERROR while performing a method invocation: " + me);
            throw new PSRuntimeException("ERROR while performing a method invocation: " + me.getMessage());
        }
        return isHealthy;
    }

    private String invokeGetApplicationState(MicroAgentID[] maids) {
        mLogger.info("invokeGetApplicationState");
        String applicationState = null;
        String methName = "getApplicationState";
        MethodInvocation mi = new MethodInvocation(methName, null);
        try {
            MicroAgentData m = mAgentMgr.invoke(maids[0], mi);
            Object maData = m.getData();
            if( maData != null ) {
                mLogger.fine("Results of Method Invocation");
                if( maData instanceof CompositeData ) {
                    CompositeData compData = (CompositeData)maData;
                    DataElement[] data = compData.getDataElements();
                    applicationState = (String)data[0].getValue();
                    mLogger.info("applicationState=" + applicationState);
                }
                else if( maData instanceof MicroAgentException ) {
                    MicroAgentException exc = (MicroAgentException)maData;
                    mLogger.severe("Exception returned from method: " + exc);
                    throw new PSRuntimeException("Exception returned from method: " + exc);
                }
                else if( maData == null ) {
                    mLogger.severe("Method Invocation returned NO data ");
                    throw new PSRuntimeException("Method Invocation returned NO data ");
                }
                else {
                    mLogger.severe("Method Invocation returned data of UNKNOWN TYPE");
                }
            }
        }
        catch( MicroAgentException me ) {
            mLogger.severe("ERROR while performing a method invocation: " + me);
            throw new PSRuntimeException("ERROR while performing a method invocation: " + me.getMessage());
        }
        return applicationState;
    }



    public String getEManagerWatchdogHome() {
        String em_home = null;
        MicroAgentID[] maids = getWatchdogMicroAgentID("eManager", "app");
        em_home = invokeGetEMHome(maids);
        return em_home;
    }

    private SolutionRegistration getSolutionComponents(String solutionName) {
        mLogger.fine("getSolutionComponents for: " + solutionName);
        SolutionRegistration my_solutionRegistration = new SolutionRegistration();
        my_solutionRegistration.solutionName(solutionName);

        String regDirName = emHome + "/config/processSequencer";
        File appPropRegDir = new File(regDirName);
        if (appPropRegDir.isDirectory()) {
            String[] appPropertyNames = appPropRegDir.list(new OnlyProperties());
            if (appPropertyNames != null) {
                for (int i=0; i < appPropertyNames.length; i++ ) {
                    if (isPartOfSolution(appPropertyNames[i], solutionName)) {
                        SolutionRegistrationComponent emanagerComp = new SolutionRegistrationComponent();
                        StringTokenizer st = new StringTokenizer(appPropertyNames[i], ".");
                        emanagerComp.setAppType(st.nextToken());
                        emanagerComp.setAppInstance(st.nextToken());
                        mLogger.fine("New SolutionRegistrationComponent - appType: " + emanagerComp.getAppType() +
                                     " appInst: " + emanagerComp.getAppInstance());
                        my_solutionRegistration.addSolutionComponent(emanagerComp);
                    }
                }
            }
            else {
                mLogger.severe("No properties files found in: " + regDirName);
            }
        }
        else {
            mLogger.severe("This is not a directory: " + regDirName);
        }
        if (my_solutionRegistration.solutionComponents().length == 0) {
            mLogger.severe("No applications registered for solution: " + solutionName);
            throw new PSRuntimeException("No applications registered for solution: " + solutionName);
        }
        return my_solutionRegistration;
    }

    private boolean isPartOfSolution(String propertyName, String solutionName) {
        mLogger.fine("is application: " + propertyName + " part of solution: " + solutionName);
        String localAppPropertiesFile = emHome + "/config/processSequencer/" + propertyName;
        File appPropertyFile = new File(localAppPropertiesFile);
        Properties appProperties = new Properties();
        if (appPropertyFile.canRead()) {
            try {
                FileInputStream fis = new FileInputStream(appPropertyFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                appProperties.load(bis);
                bis.close();
                String propValue = appProperties.getProperty("solution.name");
                mLogger.fine("Property: solution.name=" + propValue);
                if (propValue != null) {
                    if (propValue.equals(solutionName))
                    {
                        mLogger.fine("Return: true");
                        return true;
                    }
                    else
                    {
                        mLogger.fine("Return: false");
                        return false;
                    }
                }
                else {
                    mLogger.fine("Return: false");
                    return false;
                }
            }
            catch (IOException e) {
                mLogger.severe(e.toString());
                throw new PSRuntimeException(
                    "Can not read application Property File: " + localAppPropertiesFile);
            }
        }
        else {
            mLogger.severe("Can not read application Properties file: " + localAppPropertiesFile);
            throw new PSRuntimeException(
                "Can not read application Property File: " + localAppPropertiesFile);
        }
    }

    private class OnlyProperties implements FilenameFilter {
        public boolean accept(File appPropRegDir, String nme) {
            if (nme.endsWith(".properties")) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public Boolean getSolutionHealth(String solutionName) {
        mLogger.info("getSolutionHealth for: " + solutionName);
        Boolean isSolutionHealthy = new Boolean(false);
        SolutionRegistration my_solutionRegistration = getSolutionComponents(solutionName);
        Object [] objcomp = my_solutionRegistration.solutionComponents();
        SolutionRegistrationComponent[] comp = new SolutionRegistrationComponent[objcomp.length];
        for (int i = 0; i < comp.length; i++) {
            comp[i]=(SolutionRegistrationComponent)(objcomp[i]);
            mLogger.finest("Process request for appType: " + comp[i].getAppType() + " appInst: " +
                           comp[i].getAppInstance());
            MicroAgentID[] maids = getWatchdogMicroAgentID(comp[i].getAppType(), comp[i].getAppInstance());
            isSolutionHealthy = invokeGetHealth(maids);
            if (!isSolutionHealthy.booleanValue()) {
                return isSolutionHealthy;
            }
        }
        return isSolutionHealthy;
    }

    public void startSolution(String solutionName) {
        mLogger.info("startSolution for: " + solutionName);
        SolutionRegistration my_solutionRegistration = getSolutionComponents(solutionName);
        Object [] objcomp = my_solutionRegistration.solutionComponents();
        SolutionRegistrationComponent[] comp = new SolutionRegistrationComponent[objcomp.length];
        for (int i = 0; i < comp.length; i++) {
            comp[i]=(SolutionRegistrationComponent)(objcomp[i]);
            mLogger.finest("Process request for appType: " + comp[i].getAppType() + " appInst: " +
                           comp[i].getAppInstance());
            startApplication(comp[i].getAppType(), comp[i].getAppInstance());
        }
    }

    public void stopSolution(String solutionName) {
        mLogger.info("stopSolution for: " + solutionName);
        SolutionRegistration my_solutionRegistration = getSolutionComponents(solutionName);
        Object [] objcomp = my_solutionRegistration.solutionComponents();
        SolutionRegistrationComponent[] comp = new SolutionRegistrationComponent[objcomp.length];
        for (int i = 0; i < comp.length; i++) {
            comp[i]=(SolutionRegistrationComponent)(objcomp[i]);
            mLogger.finest("Process request for appType: " + comp[i].getAppType() + " appInst: " +
                           comp[i].getAppInstance());
            stopApplication(comp[i].getAppType(), comp[i].getAppInstance());
        }
    }

    public SolutionRegistrationComponent[] getSolutionStatus(String solutionName) {
        mLogger.info("getSolutionStatus for: " + solutionName);
        SolutionRegistration my_solutionRegistration = getSolutionComponents(solutionName);
        Object [] objcomp = my_solutionRegistration.solutionComponents();
        SolutionRegistrationComponent[] comp = new SolutionRegistrationComponent[objcomp.length];
        for (int i = 0; i < comp.length; i++) {
            comp[i]=(SolutionRegistrationComponent)(objcomp[i]);
            mLogger.finest("Process request for appType: " + comp[i].getAppType() + " appInst: " +
                           comp[i].getAppInstance());
            MicroAgentID[] maids = getWatchdogMicroAgentID(comp[i].getAppType(), comp[i].getAppInstance());
            comp[i].setState(invokeGetApplicationState(maids));
        }
        return comp;
    }

}

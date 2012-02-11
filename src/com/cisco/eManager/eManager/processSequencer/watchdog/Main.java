package com.cisco.eManager.eManager.processSequencer.watchdog;

/**
 * <p>Title: Process Sequencer / Watchdog</p>
 * <p>Description: eManger</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems</p>
 * @author Marvin Miles
 * @version 1.0
 */

import java.util.Properties;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;

import com.cisco.eManager.eManager.processSequencer.common.*;
import com.cisco.eManager.eManager.processSequencer.common.logging.*;
import com.cisco.eManager.eManager.util.WatchdogRulebaseNameHelper;

import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;

/**
 * Main class for Watchdog. Typically the native code creates
 * a JVM and starts it up by executing the main method of this
 * class. <p>
 *
 * This class checks the configuration to find out the type
 * of the local installation  and instantiates MasterWatchdog or
 * Watchdog accordingly. It then directs the watchdog instance to
 * start its servers.<p>
 *
 * This class then, waits for the watchdog to be stopped, at
 * which point, it asks the Watchdog instance to shutdown.
 *
 * @author Rama Taraniganty
 * @see WatchdogImpl
 * @see MasterWatchdogImpl
 */
public final class Main {

    public static long startTime;

    private WatchdogManager wdMgr;
    private Watchdog wdog;
    private String key;

    protected CiscoLogger mLogger;
    protected String mService;
    protected String mNetwork;
    protected String mDaemon;
    protected String emHome;
    private WatchdogManagerAMI mWatchdogManagerAMI = null;
    private WatchdogHawkConsole mConsole = null;
    private TibrvQueue mRvQueue = null;
    private final String WDMicroAgentName = "com.cisco.eManager.eManager.processSequencer.watchdog.WD-";


    public static void main(String[] args) {
        try {
            startTime = System.currentTimeMillis();
            new Main(new Args(args));
        }
        catch (Throwable ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public Main(Args args) throws Exception {
        mLogger = CiscoLogger.getCiscoLogger("watchdog");
        mLogger.info("Watchdog: ==== Invoked Startup ====");

        String hostName = PSInetAddress.getLocalHost().getHostName();

        setupConfiguration(hostName, args);

        try {
            Tibrv.open(Tibrv.IMPL_NATIVE);
        }
        catch (TibrvException e) {
            mLogger.severe("Failed to open Tibrv in native implementation:");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            mRvQueue = new TibrvQueue();
        }
        catch (TibrvException e) {
            mLogger.severe("TibrvException: " + e);
            e.printStackTrace();
        }

        try {
            wdog = wdMgr = new MasterWatchdogImpl();
            key = WDConstants.WD_SERVICE;
            ( (WatchdogImpl) wdog).setName(key);
        }
        catch (Exception ex) {
            handleException("Create MasterWatchdog", ex);
            System.exit(1);
        }

        try {
            mWatchdogManagerAMI = new WatchdogManagerAMI(wdMgr, mService,
                mNetwork, mDaemon, mRvQueue, LogUtil.getAppType(),LogUtil.getAppInst());
        }
        catch (AmiException e) {
            mLogger.severe("AmiException: " + e);
            e.printStackTrace();
        }

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

        // Create TibHawk Console
        WatchdogHawkManager hawkMgr = WatchdogHawkManager.instance();
        hawkMgr.start();

        ShutdownThread sht = new ShutdownThread();
        sht.start();

        try {
            ( (WatchdogImpl) wdog).startServers(false);
        }
        catch (Exception ex) {
            handleException("Start servers", ex);
        }

        mLogger.finest("Start Servers executed completed");

        while (true) {
            try {
                mRvQueue.dispatch();
            }
            catch (java.lang.Throwable caughtThrowable) {
                break;
            }
        }

    }

    private void setupConfiguration(String hostName, Args args) throws
        Exception {

        ConfigStore cfgStore = ConfigStore.getInstance();

        if (args.shouldConsolidateConfig()) {
            int index = hostName.indexOf(".");
            if (index != -1) {
                hostName = hostName.substring(0, index);

            }
            Properties props = cfgStore.readConfigForHost(hostName);

            if (props == null) {
                props = new Properties();
            }
            Properties bootProps = cfgStore.readBootConfig();

            props.putAll(bootProps);

            File tempPropsFile = File.createTempFile("app", ".properties",
                new File(LogUtil.getLogLocation(
                props)));
            tempPropsFile.deleteOnExit();

            FileOutputStream fos = new FileOutputStream(tempPropsFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos, 2048);
            props.store(bos, "Consolidated config file");
            bos.flush();
            bos.close();
            fos.close();

            System.getProperties().put("app.properties.file",
                                       tempPropsFile.getAbsolutePath());
            System.getProperties().remove("em.boot.dir");
        }

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
        emHome = System.getProperty("em.home");
        mLogger.finest("eManager Home directory (EM_HOME):" + emHome);

    }

    private void handleException(String at, Exception ex) {
        String msg = "@ " + at + " : ";
        String exMsg = ex.getMessage();
        if (exMsg == null) {
            msg += "Exception - " + ex.getClass().getName();
        }
        msg += exMsg;
        error(msg);
        ex.printStackTrace();
    }

    private void warning(String msg) {
        System.out.println("Warning: " + msg);
        System.out.flush();
    }

    private void info(String msg) {
        System.out.println("Info: " + msg);
        System.out.flush();
    }

    private void error(String msg) {
        System.out.println("Error: " + msg);
        System.out.println();
    }

    class ShutdownThread
        extends Thread {
        public ShutdownThread() {
            super("Shutdown Thread");
            setDaemon(true);
            setPriority(getPriority() + 1);
        }

        public void run() {

            int sigs[] = {
                UnixStdlib.SIGTERM, UnixStdlib.SIGUSR1};

            int signal = -1;
            while (true) {
                signal = UnixStdlib.waitForSignal(sigs);

                if (signal == UnixStdlib.SIGTERM ||
                    signal == UnixStdlib.SIGUSR1) {
                    new Exception(signal + "").printStackTrace();
                    break;
                }
            }

            // Unload and delete the watchdog rulebase
            WatchdogHawkManager hawkMgr = WatchdogHawkManager.instance();
            String wdRulebaseName = WatchdogRulebaseNameHelper.instance().
                                    generateWatchdogRulebaseName(LogUtil.getAppType(),LogUtil.getAppInst());
            hawkMgr.deleteRulebase(wdRulebaseName);


            String msgSub = WDConstants.WD_EVENT_PREFIX +
                WDConstants.WD_EVENT_STOP;
            try {
                TibrvMsg stopMsg = new MsgCommon(WDConstants.WD_EVENT_VERSION).
                    toTibrvMsg();
                stopMsg.setSendSubject(msgSub);
                try {
                    stopMsg.add(WDConstants.WD_HOSTNAME,
                                PSInetAddress.getLocalHost().getHostName());
                }
                catch (Exception e) {
                    error("In getting hostName:" + e);
                }
                EventUtils.getDefaultTransport(msgSub).send(stopMsg);
                Tibrv.close();
            }
            catch (TibrvException te) {
                error("In sending stop event - caught TibrvException:" + te);
            }

            info("Shutting down servers...");
            try {
                if (signal == UnixStdlib.SIGTERM) {
                    ( (MasterWatchdogImpl) wdog).shutdownAll();
                }
                else {
                    ( (MasterWatchdogImpl) wdog).shutdown(false, false);
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            info("Shutdown complete...");
            System.exit(0);
        }
    }

    private static class Args {
        private boolean mConsolidateConfig = false;
        private String[] args;

        Args(String args[]) throws Exception {
            this.args = args;

            for (int i = 0; i < args.length; ++i) {
                if (args[i].equalsIgnoreCase("-configConsolidate")) {
                    mConsolidateConfig = true;
                }
                else {
                    throw new IllegalArgumentException(WDExUtil.formatMessage(
                        WDExConstants.ILLEGAL_CMD_ARG,
                        WDUtil.toArray(args[i])));
                }
            }
        }

        String[] getOrigArgs() {
            return args;
        }

        boolean shouldConsolidateConfig() {
            return mConsolidateConfig;
        }
    }
}

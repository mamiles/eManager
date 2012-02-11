package com.cisco.eManager.eManager.processSequencer.watchdog;

import com.cisco.eManager.eManager.processSequencer.common.*;
import com.cisco.eManager.eManager.processSequencer.common.logging.*;
import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;
import java.util.*;
import java.util.logging.*;

/**
 * <p>Title: Watchdog Manager AMI</p>
 * <p>Description: This provides the AMI interface into Watchdog </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisc Systems </p>
 * @author Marvin Miles
 * @version 1.0
 */

public class WatchdogManagerAMI {
    private WatchdogManager mWatchdogManager = null;
    private AmiSession mAMI = null;
    protected CiscoLogger mLogger;

    public WatchdogManagerAMI(
        WatchdogManager inWatchdogManager,
        String rvService,
        String rvNetwork,
        String rvDaemon,
        TibrvQueue rvQueue,
        String appType,
        String appInst) throws AmiException {

        mLogger = CiscoLogger.getCiscoLogger("watchdog");
        mLogger.fine("WatchdogManagerAMI constructor...");
        mWatchdogManager = inWatchdogManager;

        // Create an AMI API session.
        mAMI = new AmiSession(rvService, rvNetwork, rvDaemon, rvQueue,
                              "com.cisco.eManager.eManager.processSequencer.watchdog.WD-"
                              + appType + "-" + appInst,
                              "WD-" + appType + "-" + appInst,
                              "This Watchdog controls a specific application and allows you to " +
                              "get the status of processes as well as start and stop process or groups ", null);

        // Add Receiver methods to session.
        mAMI.addMethod(new methodGetEMHome());
        mAMI.addMethod(new methodApplicationType(appType,appInst));
        mAMI.addMethod(new methodApplicationInstanceId(appType,appInst));
        mAMI.addMethod(new methodGetWDErrorStatus());
        mAMI.addMethod(new methodGetProcessStatus());
        mAMI.addMethod(new methodGetAllGroupNames());
        mAMI.addMethod(new methodGetHealth());
        mAMI.addMethod(new methodGetLogLevel());
        mAMI.addMethod(new methodSetLogLevel());
        mAMI.addMethod(new methodGetProcessesForGroup());
        mAMI.addMethod(new methodStopProcess());
        mAMI.addMethod(new methodStopGroup());
        mAMI.addMethod(new methodStopAll());
        mAMI.addMethod(new methodStartProcess());
        mAMI.addMethod(new methodStartGroup());
        mAMI.addMethod(new methodStartAll());
        mAMI.addMethod(new methodRestartGroup());
        mAMI.addMethod(new methodRestartProcess());
        mAMI.addMethod(new methodStartCommand());
        mAMI.addMethod(new methodStopCommand());
        mAMI.addMethod(new methodRestartCommand());
        mAMI.addMethod(new methodRestartAll());
        mAMI.addMethod(new methodGetGroupState());
        mAMI.addMethod(new methodGetApplicationState());


        mAMI.addMethods(mAMI);

        mAMI.createCommonMethods(
            "Watchdog",
            "1.0.0",
            "Wed 09/10/2003",
            1,
            0,
            0);

        // Annouce our existence.
        mAMI.announce();
        mLogger.fine("Constructor completed...");
    }

    public void terminate() throws AmiException {
        mAMI.stop();
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

    protected static String[] tokenizeString(String str, String delim) {
        if (str == null || str.trim().equals("")) {
            return null;
        }

        if (str.indexOf(",") != -1) {
            StringTokenizer st = new StringTokenizer(str, delim);
            String[] ret = new String[st.countTokens()];
            int i = 0;
            while (st.hasMoreTokens()) {
                ret[i++] = st.nextToken();
            }
            return ret;
        }
        return (new String[] {str});
    }

    protected static TargetComponentData buildTCDBasedOnServers(boolean waitFlag,
        String[] args) {
        TargetComponentData tcd = new TargetComponentData();
        tcd.setWaitFlag(waitFlag);

        if (args.length == 1) {
            System.out.println("arg0:" + args[0]);
            String serverName = args[0];
            if (serverName.equals("all")) {
                tcd.addAllServers();
            }
            else {
                String servers[] = tokenizeString(serverName, ",");
                tcd.addServers(servers);
            }
        }
        else if (args.length == 2 && args[0].equals("group")) {
            System.out.println("arg1:" + args[1]);
            String groupName = args[1];
            String groups[] = tokenizeString(groupName, ",");
            tcd.addServerGroups(groups);
        }
        else if (args.length == 0) {
            tcd.addAllServers();
        }
        else {
            tcd.addServers(args);
        }

        return tcd;
    }

    class methodGetLogLevel
        extends AmiMethod {
        public AmiParameterList getArguments() {
            return (null);
        }

        public methodGetLogLevel() {
            super("getLogLevel",
                  "This method returns the Logging Level for Watchdog.",
                  AmiConstants.METHOD_TYPE_INFO);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("LogLevel",
                "The current Watchdog Log Level.  Log messages below this level will not be logged.",
                ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;

            try {
                theValues = new AmiParameterList();
                theValues.addElement(
                    new AmiParameter("LogLevel",
                                     new String(getLogLevelName())));
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);

        }
    }

    class methodApplicationType
        extends AmiMethod {
        String appType = null;
        String appInst = null;

        public AmiParameterList getArguments() {
            return (null);
        }
        public methodApplicationType(String type,String inst) {
            super("getApplicationType",
                  "This method returns the Application Type (applicaiton name).",
                  AmiConstants.METHOD_TYPE_INFO);
            appType = type;
            appInst = inst;
        }

        public methodApplicationType() {
            super("getApplicationType",
                  "This method returns the Application Type (applicaiton name).",
                  AmiConstants.METHOD_TYPE_INFO);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("ApplicationType",
                "Application Type ", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;

            try {
                theValues = new AmiParameterList();
                theValues.addElement(
                    new AmiParameter("ApplicationType",
                              new String(appType)));
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);

        }
    }

    class methodApplicationInstanceId
        extends AmiMethod {
        String appType = null;
        String appInst = null;

        public AmiParameterList getArguments() {
            return (null);
        }

        public methodApplicationInstanceId(String type,String inst) {
            super("getApplicationInstanceId",
                  "This method returns the Application Type (applicaiton name).",
                  AmiConstants.METHOD_TYPE_INFO);
            appType = type;
            appInst = inst;
        }

        public methodApplicationInstanceId() {
            super("getApplicationInstanceId",
                  "This method returns the Application Type (applicaiton name).",
                  AmiConstants.METHOD_TYPE_INFO);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("ApplicationInstanceId",
                "Application Instance Id ", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;

            try {
                theValues = new AmiParameterList();
                theValues.addElement(
                    new AmiParameter("ApplicationInstanceId",
                              new String("app")));
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);

        }
    }

    class methodSetLogLevel
        extends AmiMethod {
        private final String[] sLegalChoices
            = {
            "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER",
            "FINEST", "ALL", "OFF"};

        public AmiParameterList getReturns() {
            return (null);
        }

        public methodSetLogLevel() {
            super("setLogLevel",
                  "This method sets the Watchdog Log Level.",
                  AmiConstants.METHOD_TYPE_ACTION);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("LogLevel",
                                      "The new Log Level name.", "");
            theArg.setLegalChoices(sLegalChoices);
            theArgs.addElement(theArg);

            return (theArgs);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            try {
                String theLogLevel = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                setLogLevel(theLogLevel);
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodGetEMHome
        extends AmiMethod {
        public AmiParameterList getArguments() {
            return (null);
        }

        public methodGetEMHome() {
            super("getEMHome",
                  "This method returns the eManager Home directory.",
                  AmiConstants.METHOD_TYPE_INFO);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("EMHome",
                "The eManager Home directory ", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;

            try {
                theValues = new AmiParameterList();
                theValues.addElement(
                    new AmiParameter("EMHome",
                                     new String( ( (MasterWatchdogImpl) mWatchdogManager).getEMHome())));
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);

        }
    }

    class methodGetWDErrorStatus
        extends AmiMethod {
        public AmiParameterList getArguments() {
            return (null);
        }

        public methodGetWDErrorStatus() {
            super("getWDErrorStatus",
                  "This method returns error status of the process running under Watchdog.",
                  AmiConstants.METHOD_TYPE_INFO);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("Status",
                "Get the current status of the processes", true));
            theReturns.addElement(new AmiParameter("Description",
                "Failure Description", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;

            try {
                theValues = new AmiParameterList();
                HostData hostData = HostData.ALL_HOSTS;
                String errorDescription = ( (MasterWatchdogImpl) mWatchdogManager).getErrorDescription(hostData);
                boolean status;
                if (errorDescription != null) {
                    status = false;
                }
                else {
                    status = true;
                    errorDescription = "";
                }
                theValues.addElement(new AmiParameter("Status", new Boolean(status)));
                theValues.addElement(new AmiParameter("Description", new String(errorDescription)));
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);
        }
    }


    class methodGetProcessStatus
        extends AmiMethod {

        public methodGetProcessStatus() {
            super("getProcessStatus",
                  "This method will return the status of each process managed by Watchdog.",
                  AmiConstants.METHOD_TYPE_INFO, 90000, "ProcessName");
        }

        public AmiParameterList getArguments() {
            return (null);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("Hostname", "Hostname the process is running on", ""));
            theReturns.addElement(new AmiParameter("ProcessName", "Logical name of the process.", ""));
            theReturns.addElement(new AmiParameter("PID", "Unix process id of the ProcessName.", 0L));
            theReturns.addElement(new AmiParameter("State", "State of the Process.", ""));
            theReturns.addElement(new AmiParameter("Generation", "Number of times this server has been (re)started.", 0));
            theReturns.addElement(new AmiParameter("ExecTime", "Time at which this generation was exec'ed (in milli seconds)", 0L));
            theReturns.addElement(new AmiParameter("SuccessfulHeartbeats", "Number of successful heartbeats", 0));
            theReturns.addElement(new AmiParameter("MissedHeartbeats", "Number of missed heartbeats", 0));
            theReturns.addElement(new AmiParameter("HeartbeatResult", "The value returned by the last heartbeat call", ""));
            theReturns.addElement(new AmiParameter("UsesNativeLogging", "If this is true, watchdog performs the job of transferring " +
                "this process's stdout and stdin to a log file. ", true));
            theReturns.addElement(new AmiParameter("IsMonitor", "If true this watchdog server just monitors an external " +
                "service and has not 'exec'ed the process. Useful for services that live longer than watchdog itself.", false));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                HostData hostData = HostData.ALL_HOSTS;
                WDServerStatus status[] = ( (MasterWatchdogImpl) mWatchdogManager).getStatus(hostData);

                theValues = new AmiParameterList();

                for (int i = 0; i < status.length; ++i) {
                    if (status[i] != null) {
                        Object err = status[i].getError();
                        if (err != null) {
                            if (err instanceof Throwable) {
                                throw (new AmiException(AmiErrors.AMI_REPLY_ERR, ( (Throwable) err).getMessage()));
                            }
                            else {
                                throw (new AmiException(AmiErrors.AMI_REPLY_ERR, (String) err));
                            }
                        }
                        else {
                            ServerStatus processStatus[] = status[i].getServerStatus();
                            for (int j = 0; j < processStatus.length; ++j) {
                                theValues.addElement(new AmiParameter("Hostname", new String(status[i].getName())));
                                theValues.addElement(new AmiParameter("ProcessName", new String(processStatus[j].getName())));
                                theValues.addElement(new AmiParameter("PID", new Long(processStatus[j].getPid())));
                                theValues.addElement(new AmiParameter("State", new String(processStatus[j].getState())));
                                theValues.addElement(new AmiParameter("Generation", new Integer(processStatus[j].getGeneration())));
                                theValues.addElement(new AmiParameter("ExecTime", new Long(processStatus[j].getExecTime())));
                                theValues.addElement(new AmiParameter("SuccessfulHeartbeats", new Integer(processStatus[j].getSuccessfulHeartbeats())));
                                theValues.addElement(new AmiParameter("MissedHeartbeats", new Integer(processStatus[j].getMissedHeartbeats())));
                                if (processStatus[j].getHbResult()instanceof String) {
                                    theValues.addElement(new AmiParameter("HeartbeatResult", new String( (String) processStatus[j].getHbResult())));
                                }
                                else {
                                    theValues.addElement(new AmiParameter("HeartbeatResult", new String("")));
                                }
                                theValues.addElement(new AmiParameter("UsesNativeLogging", new Boolean(processStatus[j].isLogNative())));
                                theValues.addElement(new AmiParameter("IsMonitor", new Boolean(processStatus[j].isMonitor())));
                            }
                        }
                    }
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);
        }

    }

    class methodGetAllGroupNames
        extends AmiMethod {

        public methodGetAllGroupNames() {
            super("getAllGroupNames",
                  "This method will return an list of all groups defined within Watchdog.",
                  AmiConstants.METHOD_TYPE_INFO, 90000, "GroupName");
        }

        public AmiParameterList getArguments() {
            return (null);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("GroupName", "Groups defined within Watchdog", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                HostData hostData = HostData.ALL_HOSTS;
                OperationStatus status[] = ( (MasterWatchdogImpl) mWatchdogManager).getGroups(hostData);

                theValues = new AmiParameterList();

                if (status.length == 0) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, "Error in getting groups"));
                }
                for (int i = 0; i < status.length; ++i) {
                    if (!status[i].isSuccess()) {
                        mLogger.severe("There was an error in retrieving groups on host " + status[i].getHostName());
                        continue;
                    }
                    Object obj = status[i].getResult();
                    if (obj == null) {
                        theValues.addElement(new AmiParameter("GroupName", new String("<<null>>")));
                    }
                    else if (obj instanceof List) {
                        theValues.addElement(new AmiParameter("GroupName", new String(obj.toString())));
                    }
                    else if (obj instanceof Object[]) {
                        for (int j = 0; j < ( (Object[]) obj).length; ++j) {
                            theValues.addElement(new AmiParameter("GroupName", new String( ( (Object[]) obj)[j].toString())));
                        }
                    }
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);
        }
    }

    class methodGetHealth
        extends AmiMethod {

        public methodGetHealth() {
            super("getHealth",
                  "This method check the health of all the process and return TRUE they are up and running.",
                  AmiConstants.METHOD_TYPE_INFO, 90000);
        }

        public AmiParameterList getArguments() {
            return (null);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("isHealthy", "Check all process and return TRUE they are up and running", false));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                HostData hostData = HostData.ALL_HOSTS;
                OperationStatus status[] = ( (MasterWatchdogImpl) mWatchdogManager).getHealth(hostData);

                theValues = new AmiParameterList();

                if (status.length == 0) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, "Error in retrieval"));
                }
                for (int i = 0; i < status.length; ++i) {
                    if (!status[i].isSuccess()) {
                        mLogger.severe("There was an error in checking health on host " + status[i].getHostName());
                        continue;
                    }
                    Object obj = status[i].getResult();
                    if (obj == null) {
                        theValues.addElement(new AmiParameter("isHealthy", new Boolean(false)));
                    }
                    else if (obj instanceof Boolean) {
                        theValues.addElement(new AmiParameter("isHealthy", new Boolean( ( (Boolean) obj).booleanValue())));
                    }
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);
        }

    }

    class methodGetProcessesForGroup
        extends AmiMethod {

        public methodGetProcessesForGroup() {
            super("getGetProcessesForGroup",
                  "This method will return an list of process names for a given group.",
                  AmiConstants.METHOD_TYPE_ACTION_INFO, 90000, "ProcessName");
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("GroupName", "Group Name", "");
            theArgs.addElement(theArg);
            return (theArgs);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("ProcessName", "Process Names for group", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                String groupName = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                HostData hostData = HostData.ALL_HOSTS;
                OperationStatus status[] = ( (MasterWatchdogImpl) mWatchdogManager).getGroup(hostData, groupName);

                theValues = new AmiParameterList();

                if (status.length == 0) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, "Error in getting groups"));
                }
                for (int i = 0; i < status.length; ++i) {
                    if (!status[i].isSuccess()) {
                        mLogger.severe("There was an error in retrieving servers in group " + groupName + " on host " + status[i].getHostName());
                        continue;
                    }
                    Object obj = status[i].getResult();
                    if (obj == null) {
                        theValues.addElement(new AmiParameter("ProcessName", new String("<<null>>")));
                    }
                    else if (obj instanceof List) {
                        theValues.addElement(new AmiParameter("ProcessName", new String(obj.toString())));
                    }
                    else if (obj instanceof Object[]) {
                        for (int j = 0; j < ( (Object[]) obj).length; ++j) {
                            theValues.addElement(new AmiParameter("ProcessName", new String( ( (Object[]) obj)[j].toString())));
                        }
                    }
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);
        }
    }

    class methodStopProcess
        extends AmiMethod {

        public methodStopProcess() {
            super("stopProcess",
                  "This method will stop a given process.",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("ProcessName", "Process Name to stop", "");
            theArgs.addElement(theArg);
            return (theArgs);
        }

        public AmiParameterList getReturns() {
            return (null);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                String processName = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                TargetComponentData tcd = new TargetComponentData();
                tcd.setWaitFlag(true);
                String servers[] = tokenizeString(processName, ",");
                tcd.addServers(servers);
                MultiOpStatus[] result = null;
                HostData hostData = HostData.ALL_HOSTS;
                try {
                    result = ( (MasterWatchdogImpl) mWatchdogManager).stop(hostData, tcd);
                }
                catch (Exception exception) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, exception.getMessage()));
                }
                if (!result[0].isSuccess()) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, result[0].toString()));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodStartProcess
        extends AmiMethod {

        public methodStartProcess() {
            super("startProcess",
                  "This method will start a given process.",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("ProcessName", "Process Name to start", "");
            theArgs.addElement(theArg);
            return (theArgs);
        }

        public AmiParameterList getReturns() {
            return (null);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                String processName = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                TargetComponentData tcd = new TargetComponentData();
                tcd.setWaitFlag(true);
                String servers[] = tokenizeString(processName, ",");
                tcd.addServers(servers);
                MultiOpStatus[] result = null;
                HostData hostData = HostData.ALL_HOSTS;
                try {
                    result = ( (MasterWatchdogImpl) mWatchdogManager).start(hostData, tcd);
                }
                catch (Exception exception) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, exception.getMessage()));
                }
                if (!result[0].isSuccess()) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, result[0].toString()));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodRestartProcess
        extends AmiMethod {

        public methodRestartProcess() {
            super("restartProcess",
                  "This method will restart a given process.",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("ProcessName", "Process Name to start", "");
            theArgs.addElement(theArg);
            return (theArgs);
        }

        public AmiParameterList getReturns() {
            return (null);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                String processName = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                TargetComponentData tcd = new TargetComponentData();
                tcd.setWaitFlag(true);
                String servers[] = tokenizeString(processName, ",");
                tcd.addServers(servers);
                MultiOpStatus[] result = null;
                HostData hostData = HostData.ALL_HOSTS;
                try {
                    result = ( (MasterWatchdogImpl) mWatchdogManager).restart(hostData, tcd);
                }
                catch (Exception exception) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, exception.getMessage()));
                }
                if (!result[0].isSuccess()) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, result[0].toString()));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodStopGroup
        extends AmiMethod {

        public methodStopGroup() {
            super("stopGroup",
                  "This method will stop a given group.",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("GroupName", "Group Name to stop", "");
            theArgs.addElement(theArg);
            return (theArgs);
        }

        public AmiParameterList getReturns() {
            return (null);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                String groupName = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                TargetComponentData tcd = new TargetComponentData();
                tcd.setWaitFlag(true);
                String group[] = tokenizeString(groupName, ",");
                tcd.addServerGroups(group);
                MultiOpStatus[] result = null;
                HostData hostData = HostData.ALL_HOSTS;
                try {
                    result = ( (MasterWatchdogImpl) mWatchdogManager).stop(hostData, tcd);
                }
                catch (Exception exception) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, exception.getMessage()));
                }
                if (!result[0].isSuccess()) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, result[0].toString()));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodStartGroup
        extends AmiMethod {

        public methodStartGroup() {
            super("startGroup",
                  "This method will start a given group.",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("GroupName", "Group Name to start", "");
            theArgs.addElement(theArg);
            return (theArgs);
        }

        public AmiParameterList getReturns() {
            return (null);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                String groupName = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                TargetComponentData tcd = new TargetComponentData();
                tcd.setWaitFlag(true);
                String group[] = tokenizeString(groupName, ",");
                tcd.addServerGroups(group);
                MultiOpStatus[] result = null;
                HostData hostData = HostData.ALL_HOSTS;
                try {
                    result = ( (MasterWatchdogImpl) mWatchdogManager).start(hostData, tcd);
                }
                catch (Exception exception) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, exception.getMessage()));
                }
                if (!result[0].isSuccess()) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, result[0].toString()));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodRestartGroup
        extends AmiMethod {

        public methodRestartGroup() {
            super("restartGroup",
                  "This method will restart a given group.",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("GroupName", "Group Name to restart", "");
            theArgs.addElement(theArg);
            return (theArgs);
        }

        public AmiParameterList getReturns() {
            return (null);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                String groupName = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                TargetComponentData tcd = new TargetComponentData();
                tcd.setWaitFlag(true);
                String group[] = tokenizeString(groupName, ",");
                tcd.addServerGroups(group);
                MultiOpStatus[] result = null;
                HostData hostData = HostData.ALL_HOSTS;
                try {
                    result = ( (MasterWatchdogImpl) mWatchdogManager).restart(hostData, tcd);
                }
                catch (Exception exception) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, exception.getMessage()));
                }
                if (!result[0].isSuccess()) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, result[0].toString()));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodStartAll
        extends AmiMethod {

        public methodStartAll() {
            super("startAll",
                  "This method will start all Processes for the application",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            return (null);
        }

        public AmiParameterList getReturns() {
            return (null);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                TargetComponentData tcd = new TargetComponentData();
                tcd.setWaitFlag(true);
                tcd.addAllServers();
                MultiOpStatus[] result = null;
                HostData hostData = HostData.ALL_HOSTS;
                try {
                    result = ( (MasterWatchdogImpl) mWatchdogManager).start(hostData, tcd);
                }
                catch (Exception exception) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, exception.getMessage()));
                }
                if (!result[0].isSuccess()) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, result[0].toString()));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodStopAll
        extends AmiMethod {

        public methodStopAll() {
            super("stopAll",
                  "This method will stop all Processes for the application",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            return (null);
        }

        public AmiParameterList getReturns() {
            return (null);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                TargetComponentData tcd = new TargetComponentData();
                tcd.setWaitFlag(true);
                tcd.addAllServers();
                MultiOpStatus[] result = null;
                HostData hostData = HostData.ALL_HOSTS;
                try {
                    result = ( (MasterWatchdogImpl) mWatchdogManager).stop(hostData, tcd);
                }
                catch (Exception exception) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, exception.getMessage()));
                }
                if (!result[0].isSuccess()) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, result[0].toString()));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodStartCommand
        extends AmiMethod {

        public methodStartCommand() {
            super("startCommand",
                  "This method will start processes given a command line argument: [all | service_name | group <groupname>]",
                  AmiConstants.METHOD_TYPE_ACTION_INFO, 90000, "Result");
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("Command", "[all | service_name | group <groupname>]", "");
            theArgs.addElement(theArg);
            return (theArgs);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("Result", "Result from command.", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                theValues = new AmiParameterList();
                String command = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                TargetComponentData tcd = buildTCDBasedOnServers(true, command.split("\\s"));
                MultiOpStatus[] result = null;
                HostData hostData = HostData.ALL_HOSTS;
                try {
                    result = ( (MasterWatchdogImpl) mWatchdogManager).start(hostData, tcd);
                }
                catch (Exception exception) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, exception.getMessage()));
                }
                for(int i=0; i < result.length; ++i) {
                    theValues.addElement(new AmiParameter("Result", new String(result[i].toString())));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);
        }
    }

    class methodStopCommand
        extends AmiMethod {

        public methodStopCommand() {
            super("stopCommand",
                  "This method will stop processes given a command line argument: [all | service_name | group <groupname>]",
                  AmiConstants.METHOD_TYPE_ACTION_INFO, 90000, "Result");
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("Command", "[all | service_name | group <groupname>]", "");
            theArgs.addElement(theArg);
            return (theArgs);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("Result", "Result from command.", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                theValues = new AmiParameterList();
                String command = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                TargetComponentData tcd = buildTCDBasedOnServers(true, command.split("\\s"));
                MultiOpStatus[] result = null;
                HostData hostData = HostData.ALL_HOSTS;
                try {
                    result = ( (MasterWatchdogImpl) mWatchdogManager).stop(hostData, tcd);
                }
                catch (Exception exception) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, exception.getMessage()));
                }
                for(int i=0; i < result.length; ++i) {
                    theValues.addElement(new AmiParameter("Result", new String(result[i].toString())));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);
        }
    }

    class methodRestartCommand
        extends AmiMethod {

        public methodRestartCommand() {
            super("restartCommand",
                  "This method will restart processes given a command line argument: [all | service_name | group <groupname>]",
                  AmiConstants.METHOD_TYPE_ACTION_INFO, 90000, "Result");
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("Command", "[all | service_name | group <groupname>]", "");
            theArgs.addElement(theArg);
            return (theArgs);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("Result", "Result from command.", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                theValues = new AmiParameterList();
                String command = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                TargetComponentData tcd = buildTCDBasedOnServers(true, command.split("\\s"));
                MultiOpStatus[] result = null;
                HostData hostData = HostData.ALL_HOSTS;
                try {
                    result = ( (MasterWatchdogImpl) mWatchdogManager).restart(hostData, tcd);
                }
                catch (Exception exception) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, exception.getMessage()));
                }
                for(int i=0; i < result.length; ++i) {
                    theValues.addElement(new AmiParameter("Result", new String(result[i].toString())));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);
        }
    }

    class methodRestartAll
        extends AmiMethod {

        public methodRestartAll() {
            super("restartAll",
                  "This method will restart all Processes for the application",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            return (null);
        }

        public AmiParameterList getReturns() {
            return (null);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            try {
                TargetComponentData tcd = new TargetComponentData();
                tcd.setWaitFlag(true);
                tcd.addAllServers();
                MultiOpStatus[] result = null;
                HostData hostData = HostData.ALL_HOSTS;
                try {
                    result = ( (MasterWatchdogImpl) mWatchdogManager).restart(hostData, tcd);
                }
                catch (Exception exception) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, exception.getMessage()));
                }
                if (!result[0].isSuccess()) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, result[0].toString()));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodGetApplicationState
        extends AmiMethod {
        public AmiParameterList getArguments() {
            return (null);
        }

        public methodGetApplicationState() {
            super("getApplicationState",
                  "This method returns the state of the application based on the state from each process.",
                  AmiConstants.METHOD_TYPE_INFO);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("ApplicationState",
                "The Application State ", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            String currentState = null;
            try {
                HostData hostData = HostData.ALL_HOSTS;
                WDServerStatus status[] = ( (MasterWatchdogImpl) mWatchdogManager).getStatus(hostData);

                theValues = new AmiParameterList();

                for (int i = 0; i < status.length; ++i) {
                    if (status[i] != null) {
                        Object err = status[i].getError();
                        if (err != null) {
                            if (err instanceof Throwable) {
                                throw (new AmiException(AmiErrors.AMI_REPLY_ERR, ( (Throwable) err).getMessage()));
                            }
                            else {
                                throw (new AmiException(AmiErrors.AMI_REPLY_ERR, (String) err));
                            }
                        }
                        else {
                            ServerStatus processStatus[] = status[i].getServerStatus();
                            for (int j = 0; j < processStatus.length; ++j) {
                                if (currentState == null) {
                                    if      (processStatus[j].getState().equals("started")) {
                                        currentState = "up";
                                    }
                                    else if (processStatus[j].getState().equals("disabled") ||
                                             processStatus[j].getState().equals("disabled_dependent")) {
                                        currentState = "down";
                                    }
                                    else if (processStatus[j].getState().equals("stop_depends") ||
                                             processStatus[j].getState().equals("stopping_gently") ||
                                             processStatus[j].getState().equals("stopping_hard")) {
                                        currentState = "transitioning_down";
                                    }
                                    else if (processStatus[j].getState().equals("start_depends") ||
                                             processStatus[j].getState().equals("starting") ||
                                             processStatus[j].getState().equals("restart_delay") ||
                                             processStatus[j].getState().equals("stopped")) {
                                        currentState = "transitioning_up";
                                    }
                                }
                                else if (currentState.equals("up")) {
                                    if (processStatus[j].getState().equals("start_depends") ||
                                        processStatus[j].getState().equals("starting") ||
                                        processStatus[j].getState().equals("restart_delay") ||
                                        processStatus[j].getState().equals("stopped")) {
                                        currentState = "transitioning_up";
                                    }
                                    else if (!processStatus[j].getState().equals("started")) {
                                        currentState = "unknown";
                                    }
                                }
                                else if (currentState.equals("down")) {
                                    if (processStatus[j].getState().equals("stop_depends") ||
                                        processStatus[j].getState().equals("stopping_gently") ||
                                        processStatus[j].getState().equals("stopping_hard")) {
                                        currentState = "transitioning_down";
                                    }
                                    else if (!processStatus[j].getState().equals("down")) {
                                        currentState = "unknown";
                                    }
                                }
                                else if (currentState.equals("transitioning_up")) {
                                    if (processStatus[j].getState().equals("start_depends") ||
                                        processStatus[j].getState().equals("started") ||
                                        processStatus[j].getState().equals("starting") ||
                                        processStatus[j].getState().equals("restart_delay") ||
                                        processStatus[j].getState().equals("stopped")) {
                                        currentState = "transitioning_up";
                                    }
                                    else {
                                        currentState = "unknown";
                                    }
                                }
                                else if (currentState.equals("transitioning_down")) {
                                    if (processStatus[j].getState().equals("stop_depends") ||
                                        processStatus[j].getState().equals("down") ||
                                        processStatus[j].getState().equals("stopping_gently") ||
                                        processStatus[j].getState().equals("stopping_hard")) {
                                        currentState = "transitioning_down";
                                    }
                                    else {
                                        currentState = "unknown";
                                    }
                                }
                            }
                        }
                    }
                }
                theValues.addElement(
                    new AmiParameter("ApplicationState", new String(currentState)));
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);

        }
    }

    class methodGetGroupState
        extends AmiMethod {

        public methodGetGroupState() {
            super("getGroupState",
                  "This method returns the state of the group based on the state from each process in the group.",
                  AmiConstants.METHOD_TYPE_ACTION_INFO);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("GroupName", "Group Name", "");
            theArgs.addElement(theArg);
            return (theArgs);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("GroupState",
                "The Group State ", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;
            String currentState = null;
            try {

                String groupName = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                HostData hostData = HostData.ALL_HOSTS;
                OperationStatus status[] = ( (MasterWatchdogImpl) mWatchdogManager).getGroup(hostData, groupName);
                List processNames = new LinkedList();
                theValues = new AmiParameterList();

                if (status.length == 0) {
                    throw (new AmiException(AmiErrors.AMI_REPLY_ERR, "Error in getting groups"));
                }
                for (int i = 0; i < status.length; ++i) {
                    if (!status[i].isSuccess()) {
                        mLogger.severe("There was an error in retrieving servers in group " + groupName + " on host " + status[i].getHostName());
                        continue;
                    }
                    Object obj = status[i].getResult();
                    if (obj == null) {
                        processNames.add(new String("<<null>>"));
                    }
                    else if (obj instanceof List) {
                        processNames.add(new String(obj.toString()));
                    }
                    else if (obj instanceof Object[]) {
                        for (int j = 0; j < ( (Object[]) obj).length; ++j) {
                            processNames.add(new String( ( (Object[]) obj)[j].toString()));
                          }
                    }
                }

                WDServerStatus wdstatus[] = ( (MasterWatchdogImpl) mWatchdogManager).getStatus(hostData);

                for (int i = 0; i < wdstatus.length; ++i) {
                    if (wdstatus[i] != null) {
                        Object err = wdstatus[i].getError();
                        if (err != null) {
                            if (err instanceof Throwable) {
                                throw (new AmiException(AmiErrors.AMI_REPLY_ERR, ( (Throwable) err).getMessage()));
                            }
                            else {
                                throw (new AmiException(AmiErrors.AMI_REPLY_ERR, (String) err));
                            }
                        }
                        else {
                            ServerStatus processStatus[] = wdstatus[i].getServerStatus();
                            for (int j = 0; j < processStatus.length; ++j) {
                                if (processNames.contains(processStatus[j].getName())) {
                                    if (currentState == null)
                                    {
                                        if (processStatus[j].getState().equals("started"))
                                        {
                                            currentState = "up";
                                        }
                                        else if (processStatus[j].getState().equals("disabled") ||
                                                 processStatus[j].getState().equals("disabled_dependent"))
                                        {
                                            currentState = "down";
                                        }
                                        else if (processStatus[j].getState().equals("stop_depends") ||
                                                 processStatus[j].getState().equals("stopping_gently") ||
                                                 processStatus[j].getState().equals("stopping_hard"))
                                        {
                                            currentState = "transitioning_down";
                                        }
                                        else if (processStatus[j].getState().equals("start_depends") ||
                                                 processStatus[j].getState().equals("starting") ||
                                                 processStatus[j].getState().equals("restart_delay") ||
                                                 processStatus[j].getState().equals("stopped"))
                                        {
                                            currentState = "transitioning_up";
                                        }
                                    }
                                    else if (currentState.equals("up"))
                                    {
                                        if (processStatus[j].getState().equals("start_depends") ||
                                            processStatus[j].getState().equals("starting") ||
                                            processStatus[j].getState().equals("restart_delay") ||
                                            processStatus[j].getState().equals("stopped"))
                                        {
                                            currentState = "transitioning_up";
                                        }
                                        else if (!processStatus[j].getState().equals("started"))
                                        {
                                            currentState = "unknown";
                                        }
                                    }
                                    else if (currentState.equals("down"))
                                    {
                                        if (processStatus[j].getState().equals("stop_depends") ||
                                            processStatus[j].getState().equals("stopping_gently") ||
                                            processStatus[j].getState().equals("stopping_hard"))
                                        {
                                            currentState = "transitioning_down";
                                        }
                                        else if (!processStatus[j].getState().equals("down"))
                                        {
                                            currentState = "unknown";
                                        }
                                    }
                                    else if (currentState.equals("transitioning_up"))
                                    {
                                        if (processStatus[j].getState().equals("start_depends") ||
                                            processStatus[j].getState().equals("started") ||
                                            processStatus[j].getState().equals("starting") ||
                                            processStatus[j].getState().equals("restart_delay") ||
                                            processStatus[j].getState().equals("stopped"))
                                        {
                                            currentState = "transitioning_up";
                                        }
                                        else
                                        {
                                            currentState = "unknown";
                                        }
                                    }
                                    else if (currentState.equals("transitioning_down"))
                                    {
                                        if (processStatus[j].getState().equals("stop_depends") ||
                                            processStatus[j].getState().equals("down") ||
                                            processStatus[j].getState().equals("stopping_gently") ||
                                            processStatus[j].getState().equals("stopping_hard"))
                                        {
                                            currentState = "transitioning_down";
                                        }
                                        else
                                        {
                                            currentState = "unknown";
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                theValues.addElement(
                    new AmiParameter("GroupState", new String(currentState)));
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);

        }
    }



}

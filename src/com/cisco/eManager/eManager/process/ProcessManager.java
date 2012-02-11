package com.cisco.eManager.eManager.process;

import com.cisco.eManager.eManager.util.GlobalProperties;
import com.cisco.eManager.eManager.processSequencer.common.*;
import com.cisco.eManager.common.util.*;
import org.apache.log4j.Logger;
import java.io.*;
import java.util.*;
import java.net.InetAddress;
import java.util.Properties;
import com.tibco.tibrv.*;
import COM.TIBCO.hawk.talon.*;
import com.cisco.eManager.common.process.*;
import com.cisco.eManager.common.exception.PsAPIException;
import com.cisco.eManager.common.inventory.LogLevel;
import com.cisco.eManager.common.inventory.EmanagerInventoryException;
import com.cisco.eManager.common.admin.EmanagerAdminException;
import com.cisco.eManager.eManager.admin.AdminManager;
import com.cisco.eManager.eManager.admin.UserSession;
import org.exolab.castor.xml.*;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.*;

import com.cisco.eManager.eManager.audit.AuditManager;
import com.cisco.eManager.eManager.audit.AuditGlobals;
import com.cisco.eManager.common.audit.AuditDomain;
import com.cisco.eManager.common.audit.AuditAction;
import com.cisco.eManager.common.audit.EmanagerAuditException;
import com.cisco.eManager.common.database.EmanagerDatabaseException;

/**
 * <p>Title: Process Manager</p>
 * <p>Description: eManger</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems</p>
 * @author Marvin Miles
 * @version 1.0
 *
 */

public class ProcessManager extends UnicastRemoteObject
    implements ProcessManagerInterface
{

    private static ProcessManager instance = null;
    private static Logger mLogger = Logger.getLogger(ProcessManager.class);
    protected String mDomain = null;
    protected String mService = null;
    protected String mNetwork = null;
    protected String mDaemon = null;
    protected String emHome = null;
    protected TibrvQueue mRvQueue = null;
    protected ProcessHawkConsole mConsole = null;

    private ProcessManager() throws RemoteException {
        super();
        mLogger.debug("Process Manager now started.");
        setupConfig();
        try {
            Naming.rebind(ProcessManagerInterface.LOOKUPNAME, this);
        }
        catch (Exception e) {
            if (e instanceof RemoteException)
                throw (RemoteException)e;
            else
                throw new RemoteException(e.getMessage());
        }

        mConsole = ProcessHawkConsole.instance();
    }

    public static ProcessManager instance() {
        mLogger.debug("enter");

        //if (System.getSecurityManager() == null) {
        //    System.setSecurityManager(new RMISecurityManager());
        //}


        if (instance == null)
        {
            try
            {
                instance = new ProcessManager();
            }
            catch (RemoteException ex)
            {
                mLogger.error("RemoteException while creating ProcessManager. " + ex);
            }
        }
        return instance;
    }

    private void setupConfig() {
        GlobalProperties globalProp = GlobalProperties.instance();
        Properties emProp = globalProp.getProperties();

        mService = emProp.getProperty("SYSTEM.tibhawk.service", "7474");
        mLogger.debug("TibHawk UDP Service: " + mService);
        mNetwork = emProp.getProperty("SYSTEM.tibhawk.network", null);
        mLogger.debug(
            "TibHawk network to use for outbound session communications: " +
            mNetwork);
        mDaemon = emProp.getProperty("SYSTEM.tibhawk.daemon", "tcp:7474");
        mLogger.debug(
            "TIBCO Rendezvous daemon to handle communication for the session: " +
            mDaemon);
        mDomain = emProp.getProperty("SYSTEM.tibhawk.domain", "default");
        mLogger.debug("TibHawk Domain on which the console is to communicate: " + mDomain);
        emHome = System.getProperty("EMANAGER_ROOT");
        mLogger.debug("eManager Home directory (EMANAGER_ROOT):" + emHome);
    }

    private String getLocalHost() {
        try
        {
            InetAddress host = InetAddress.getLocalHost();
            String localHost = host.getHostName();
            mLogger.debug("LocalHost: " + localHost);
            return localHost;
        }
        catch (UnknownHostException ex)
        {
            mLogger.error("Local Hostname could not be found: " + ex);
            throw new PSRuntimeException("Local Hostname could not be found: " + ex);
        }
    }

    private MicroAgentID getWatchdogMicroAgentID(String appType, String appInst) {
        int tryCount = 0;
        MicroAgentID watchdogMaid = null;
        while (true)
        {
            watchdogMaid = mConsole.getWatchdogMicroAgentID(appType, appInst);
            if (watchdogMaid == null)
            {
                mLogger.debug("Waiting to get Watchdog Micro Agent...");
                try
                {
                    Thread.sleep(3000);
                }
                catch (InterruptedException ex2)
                {
                    mLogger.error("InterruptedException while sleeping 3000ms: " + ex2);
                }
                tryCount++;
            }
            else
            {
                break;
            }
            if (tryCount >= 20)
            {
                mLogger.error("Watchdog MicroAgent not found");
                break;
            }
        }
        if (watchdogMaid == null) {
            mLogger.error("Could not find Micro Agent for Watchdog on Local Host, eManager may be down.");
            throw new PSRuntimeException("ERROR: Could not find Micro Agent for Watchdog on Local Host, eManager may be down.");
        }

        return watchdogMaid;
    }

    private MicroAgentID getProcessMicroAgentID() {
        int tryCount = 0;
        MicroAgentID processSequencerMaId = null;
        while (true)
        {
            processSequencerMaId = mConsole.getProcessMicroAgentID();
            if (processSequencerMaId == null)
            {
                mLogger.debug("Waiting to get Process Sequencer Micro Agent...");
                try
                {
                    Thread.sleep(3000);
                }
                catch (InterruptedException ex2)
                {
                    mLogger.error("InterruptedException while sleeping 3000ms: " + ex2);
                }
                tryCount++;
            }
            else
            {
                break;
            }
            if (tryCount >= 20)
            {
                mLogger.error("Process Sequencer MicroAgent not found");
                break;
            }
        }
        if (processSequencerMaId == null) {
            mLogger.error("Could not find Micro Agent for Process Sequencer on Local Host, eManager may be down.");
            throw new PSRuntimeException("ERROR: Could not find Micro Agent for Process Sequencer on Local Host, eManager may be down.");
        }

        return processSequencerMaId;
    }

    private LinkedList invokeMethod(MicroAgentID maID, String methodName, LinkedList args) throws PsAPIException {
        LinkedList result = null;
        mLogger.debug("invoke method: " + methodName + " from: " + maID.toString());

        DataElement [] arguments = null;
        if (args.size() > 0) {
            arguments = new DataElement[args.size()];
            for (int i=0; i<args.size(); i++) {
                TibDataElement tde = (TibDataElement)args.get(i);
                arguments[i] = new DataElement(tde.name, tde.value);
            }
        }

        MethodInvocation mi = new MethodInvocation(methodName, arguments);
        result = new LinkedList();
        try {
            MicroAgentData mad = mConsole.invoke(maID, mi);
            Object maData = mad.getData();
            if (maData instanceof CompositeData) {
                mLogger.debug("Result is CompositeData");
                CompositeData compData = (CompositeData)maData;
                DataElement [] de = compData.getDataElements();
                for (int i=0; i<de.length; i++)
                {
                    TibDataElement tibde = new TibDataElement(
                        de[i].getName(),
                        de[i].getValue());
                    result.add(tibde);
                }
            } else if (maData instanceof TabularData) {
                mLogger.debug("Result is TabularData");
                TabularData tabData = (TabularData)maData;
                DataElement [][] table = tabData.getAllDataElements();
                if (table != null) {
                    for (int i=0; i<table.length; i++)
                    {
                        LinkedList row = new LinkedList();
                        for (int j=0; j<table[i].length; j++)
                        {
                            TibDataElement tibde = new TibDataElement(
                                table[i][j].getName(),
                                table[i][j].getValue());
                            row.add(tibde);
                        }
                        result.add(row);
                    }
                }
            } else if (maData instanceof MicroAgentException) {
                MicroAgentException exc = (MicroAgentException)maData;
                mLogger.error("Result is MicroAgentException while executing: " + methodName);
                mLogger.error("Exception: " + exc);
                throw new PsAPIException(exc.getMessage());
            } else if (maData == null) {
                mLogger.debug("Method: " + methodName + " returns no data");
            } else {
                mLogger.info("Mthod: " + methodName +" returns data of UNKNOWN TYPE");
            }
        } catch (MicroAgentException e) {
            mLogger.error("invoke method: " + methodName + " caught MicroAgentException:");
            mLogger.error(e);
            throw new PsAPIException(e.getMessage());
        }
        return result;
    }

    public StatusResp successStatus() {
        StatusResp status = new StatusResp();
        Rc rc = new Rc();
        rc.setSuccess(0);
        status.setRc(rc);
        status.setDescription("Successful");
        return status;
    }

    public StatusResp failureStatus(int rcode, String desc) {
        StatusResp status = new StatusResp();
        Rc rc = new Rc();
        rc.setFailure(rcode);
        status.setRc(rc);
        status.setDescription(desc);
        return status;
    }

    public String handleRequest(String xmlStream, String userId, AccessType access) {
        StringWriter response = new StringWriter();
        ProcessMgrMsg msg = null;
        ProcessMgrResp resp = new ProcessMgrResp();

        try
        {
            msg = ProcessMgrMsg.unmarshal(new StringReader(xmlStream));

            // getProcessStatus
            GetProcessStatus xmlProcessStatus = msg.getGetProcessStatus();
            if (xmlProcessStatus != null) {
                GetProcessStatusResp psResp = new GetProcessStatusResp();
                ProcessInfoObj[] psObj = getProcessStatus(xmlProcessStatus.getApplicationType(),
                    xmlProcessStatus.getApplicationInstance());
                for (int i = 0; i < psObj.length; i++) {
                    psResp.addProcessInfoObj(psObj[i]);
                }
                resp.setGetProcessStatusResp(psResp);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // ProcessInfoRec getProcessStatusFor(String applicationType, String applicationInstance, String processName)
            GetProcessStatusFor xmlGetProcessStatusFor = msg.getGetProcessStatusFor();
            if (xmlGetProcessStatusFor != null) {
                GetProcessStatusForResp psForResp = new GetProcessStatusForResp();
                psForResp.setProcessInfoRec(getProcessStatusFor(xmlGetProcessStatusFor.getApplicationType(),
                    xmlGetProcessStatusFor.getApplicationInstance(), xmlGetProcessStatusFor.getProcessName()));
                resp.setGetProcessStatusForResp(psForResp);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // String getGroupState(String applicationType, String applicationInstance, String groupName)
            GetGroupState xmlGetGroupState = msg.getGetGroupState();
            if (xmlGetGroupState != null) {
                resp.setGetGroupStateResp(getGroupState(xmlGetGroupState.getApplicationType(),
                    xmlGetGroupState.getApplicationInstance(),
                    xmlGetGroupState.getGroupName()));
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // SolutionStatusInfoObj[] getSolutionStatus(String solutionName)
            String xmlGetSolutionStatus = msg.getGetSolutionStatus();
            if (xmlGetSolutionStatus != null) {
                GetSolutionStatusResp ssResp = new GetSolutionStatusResp();
                SolutionStatusInfoObj[] ssObj = getSolutionStatus(xmlGetSolutionStatus);
                for (int i = 0; i < ssObj.length; i++) {
                    ssResp.addSolutionStatusInfoObj(ssObj[i]);
                }
                resp.setGetSolutionStatusResp(ssResp);
                successStatus().marshal(response);
                return response.toString();
            }

            // String[] getAllGroupNames(String applicationType, String applicationInstance)
            GetAllGroupNames xmlGetAllGroupNames = msg.getGetAllGroupNames();
            if (xmlGetAllGroupNames != null) {
                GetAllGroupNamesResp grpNamesRsp = new GetAllGroupNamesResp();
                String[] grpNames = getAllGroupNames(xmlGetAllGroupNames.getApplicationType(),
                    xmlGetAllGroupNames.getApplicationInstance());
                for (int i = 0; i < grpNames.length; i++) {
                    grpNamesRsp.addGroupName(grpNames[i]);
                }
                resp.setGetAllGroupNamesResp(grpNamesRsp);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // String[] getProcessesForGroup(String applicationType, String applicationInstance, String groupName)
            GetProcesssesForGroup xmlGetProcesssesForGroup = msg.getGetProcesssesForGroup();
            if (xmlGetProcesssesForGroup != null) {
                GetProcessesForGroupResp proForGrpResp = new GetProcessesForGroupResp();
                String[] processes =  getProcessesForGroup(xmlGetProcesssesForGroup.getApplicationType(),
                    xmlGetProcesssesForGroup.getApplicationInstance(),
                    xmlGetProcesssesForGroup.getGroupName());
                for (int i = 0; i < processes.length; i++) {
                    proForGrpResp.addProcessName(processes[i]);
                }
                resp.setGetProcessesForGroupResp(proForGrpResp);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // boolean getHealth(String applicationType, String applicationInstance)
            GetHealth xmlGetHealth = msg.getGetHealth();
            if (xmlGetHealth != null) {
                resp.setGetHealthResp(getHealth(xmlGetHealth.getApplicationType(),
                                                xmlGetHealth.getApplicationInstance()));
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // boolean getSolutionHealth(String solutionName)
            String xmlSolutionHealth = msg.getGetSolutionHealth();
            if (xmlSolutionHealth != null) {
                resp.setGetSolutionHealthResp(getSolutionHealth(xmlSolutionHealth));
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void startProcess(String applicationType, String applicationInstance, String processName)
            StartProcess xmlStartProcess = msg.getStartProcess();
            if (xmlStartProcess != null) {
                startProcess(xmlStartProcess.getApplicationType(),
                             xmlStartProcess.getApplicationInstance(),
                             xmlStartProcess.getProcessName(), userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // String startCommand(String applicationType, String applicationInstance, String command)
            StartCommand xmlStartCommand = msg.getStartCommand();
            if (xmlStartCommand != null) {
                String startCommandStatus = startCommand(xmlStartCommand.getApplicationType(),
                                             xmlStartCommand.getApplicationInstance(),
                                             xmlStartCommand.getCommand(), userId, access);
                resp.setStatusResp(startCommandStatus);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void startGroup(String applicationType, String applicationInstance, String groupName)
            StartGroup xmlStartGroup = msg.getStartGroup();
            if (xmlStartGroup != null) {
                startGroup(xmlStartGroup.getApplicationType(),
                           xmlStartGroup.getApplicationInstance(),
                           xmlStartGroup.getGroupName(), userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void startAll(String applicationType, String applicationInstance)
            StartAll xmlStartAll = msg.getStartAll();
            if (xmlStartAll != null) {
                startAll(xmlStartAll.getApplicationType(),
                         xmlStartAll.getApplicationInstance(), userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void stopProcess(String applicationType, String applicationInstance, String processName)
            StopProcess xmlStopProcess = msg.getStopProcess();
            if (xmlStopProcess != null) {
                stopProcess(xmlStopProcess.getApplicationType(),
                            xmlStopProcess.getApplicationInstance(),
                            xmlStopProcess.getProcessName(), userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // String stopCommand(String applicationType, String applicationInstance, String command)
            StopCommand xmlStopCommand = msg.getStopCommand();
            if (xmlStopCommand != null) {
                String stopCommandStatus = stopCommand(xmlStopCommand.getApplicationType(),
                    xmlStopCommand.getApplicationInstance(),
                    xmlStopCommand.getCommand(), userId, access);
                resp.setStatusResp(stopCommandStatus);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void stopGroup(String applicationType, String applicationInstance, String groupName)
            StopGroup xmlStopGroup = msg.getStopGroup();
            if (xmlStopGroup != null) {
                stopGroup(xmlStopGroup.getApplicationType(),
                          xmlStopGroup.getApplicationInstance(),
                          xmlStopGroup.getGroupName(), userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void stopAll(String applicationType, String applicationInstance)
            StopAll xmlStopAll = msg.getStopAll();
            if (xmlStopAll != null) {
                stopAll(xmlStopAll.getApplicationType(),
                        xmlStopAll.getApplicationInstance(), userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void restartProcess(String applicationType, String applicationInstance, String processName)
            RestartProcess xmlRestartProcess = msg.getRestartProcess();
            if (xmlRestartProcess != null) {
                restartProcess(xmlRestartProcess.getApplicationType(),
                               xmlRestartProcess.getApplicationInstance(),
                               xmlRestartProcess.getProcessName(), userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void restartGroup(String applicationType, String applicationInstance, String groupName)
            RestartGroup xmlRestartGroup = msg.getRestartGroup();
            if (xmlRestartGroup != null) {
                restartGroup(xmlRestartGroup.getApplicationType(),
                             xmlRestartGroup.getApplicationInstance(),
                             xmlRestartGroup.getGroupName(), userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // String restartCommand(String applicationType, String applicationInstance, String command)
            RestartCommand xmlRestartCommand = msg.getRestartCommand();
            if (xmlRestartCommand != null) {
                String restartCommandStatus = restartCommand(xmlRestartCommand.getApplicationType(),
                    xmlRestartCommand.getApplicationInstance(),
                    xmlRestartCommand.getCommand(), userId, access);
                resp.setStatusResp(restartCommandStatus);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void restartAll(String applicationType, String applicationInstance)
            RestartAll xmlRestartAll = msg.getRestartAll();
            if (xmlRestartAll != null) {
                restartAll(xmlRestartAll.getApplicationType(),
                           xmlRestartAll.getApplicationInstance(), userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void startApplication(String applicationType, String applicationInstance)
            StartApplication xmlStartApplication = msg.getStartApplication();
            if (xmlStartApplication != null) {
                startApplication(xmlStartApplication.getApplicationType(),
                                 xmlStartApplication.getApplicationInstance(), userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void startSolution(String solutionName)
            String xmlStartSolution = msg.getStartSolution();
            if (xmlStartSolution != null) {
                startSolution(xmlStartSolution, userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void stopApplication(String applicationType, String applicationInstance)
            StopApplication xmlStopApplication = msg.getStopApplication();
            if (xmlStopApplication != null) {
                stopApplication(xmlStopApplication.getApplicationType(),
                                xmlStopApplication.getApplicationInstance(), userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // void stopSolution(String solutionName)
            String xmlStopSolution = msg.getStopSolution();
            if (xmlStopSolution != null) {
                stopSolution(xmlStopSolution, userId, access);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            // getGroupState
            GetGroupState xmlGroupState = msg.getGetGroupState();
            if (xmlGroupState != null) {
                String groupState = getGroupState(xmlGroupState.getApplicationType(),
                                                  xmlGroupState.getApplicationInstance(),
                                                  xmlGroupState.getGroupName());
                resp.setGetGroupStateResp(groupState);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

        }
        catch (ValidationException ex)
        {
            mLogger.error("XML Validation Exception: " + ex.getMessage());
            try {
                failureStatus(1, "XML Validation Exception: " + ex.getMessage()).marshal(response);
                resp.marshal(response);
                return response.toString();
            }
            catch (ValidationException ex1) {
            }
            catch (MarshalException ex1) {
            }
        }
        catch (MarshalException ex)
        {
            mLogger.error("XML Marshal Exception: " + ex.getMessage());
            try {
                failureStatus(1, "XML Marshal Exception: " + ex.getMessage()).marshal(response);
                resp.marshal(response);
                return response.toString();
            }
            catch (ValidationException ex1) {
            }
            catch (MarshalException ex1) {
            }
        }
        catch (PsAPIException ex) {
            mLogger.error("API Exception: " + ex.getMessage());
            try {
                failureStatus(1, "API Exception: " + ex.getMessage()).marshal(response);
                resp.marshal(response);
                return response.toString();
            }
            catch (ValidationException ex1) {
            }
            catch (MarshalException ex1) {
            }
        }
        catch (Exception ex) {
            mLogger.error("Exception: " + ex.getMessage());
            try {
                failureStatus(1, "Exception: " + ex.getMessage()).marshal(response);
                resp.marshal(response);
                return response.toString();
            }
            catch (ValidationException ex1) {
            }
            catch (MarshalException ex1) {
            }
        }

        return response.toString();
    }

    public AccessType login(String userid, String password) throws EmanagerAdminException {
        mLogger.debug("enter");
        mLogger.debug("getting instance of AdminManager");
        AdminManager am = AdminManager.instance();
        mLogger.debug("getting sessionId by logging on to AdminManager");
        if ( am != null) {
            String sessionId = am.login(userid, password);
            mLogger.debug("getting UserSession");
            UserSession userSession = am.isAuthenticated(sessionId);
            if (userSession != null)
                return userSession.getAccessType();
        }
        return AccessType.READ;
    }

    public String getProcessSequencerEMHome() {
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        result = invokeMethod(getProcessMicroAgentID(), "getEMHome", args);
        TibDataElement tibde = null;
        try {
            tibde = (TibDataElement)result.getFirst();
        } catch (NoSuchElementException e) {
            mLogger.error("No data returned");
        }

//        return tibde.getStringValue();
        return tibde.toXml();
    }

    /**
     * Returns an array of ProcessInfo structures for all process defined within
     * Process Sequencer on the local host.  The state is populated as well as the process
     * id if it is known.
     * @param applicationType
     * @param applicationInstance
     * @return ProcessInfo[] array
     * @throws PsAPIException if falure occures getting status
     */
    public ProcessInfoObj[] getProcessStatus(String applicationType, String applicationInstance) throws PsAPIException {
        mLogger.debug("entered");
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance), "getProcessStatus", args);

        mLogger.debug(result.size() + " process returned from getProcessStatus method");

        LinkedList processInfo = new LinkedList();

        for (int i = 0; i<result.size(); i++) {
            ProcessInfoObj data = new ProcessInfoObj();
            LinkedList info = (LinkedList)result.get(i);
            //mLogger.debug(info.size() + " returned from row");
            for (int j = 0; j<info.size(); j++) {
                TibDataElement tibde = (TibDataElement)info.get(j);
                //mLogger.debug("processing: " + tibde.getName());
                if (tibde.getName().equals("Hostname")) {data.setHostname(tibde.getStringValue());}
                else if (tibde.getName().equals("ProcessName")) {data.setProcessName(tibde.getStringValue());}
                else if (tibde.getName().equals("PID")) {data.setProcessId(((Long)tibde.getValue()).longValue());}
                else if (tibde.getName().equals("State")) {data.setState(tibde.getStringValue());}
                else if (tibde.getName().equals("Generation")) {data.setStartGeneration(((Integer)tibde.getValue()).intValue());}
                else if (tibde.getName().equals("ExecTime")) {
                    Date dt = new Date(((Long)tibde.getValue()).longValue());
                    data.setExecTime( dt );
                }
                else if (tibde.getName().equals("SuccessfulHeartbeats")) {data.setSuccessfulHeartbeats(((Integer)tibde.getValue()).intValue());}
                else if (tibde.getName().equals("MissedHeartbeats")) {data.setMissedHeartbeats(((Integer)tibde.getValue()).intValue());}
                else if (tibde.getName().equals("HeartbeatResult")) {data.setHeartbeatResult(tibde.getStringValue());}
                else if (tibde.getName().equals("UsesNativeLogging")) {data.setUsesNativeLogging(((Boolean)tibde.getValue()).toString());}
                else if (tibde.getName().equals("IsMonitor")) {data.setIsMonitor(((Boolean)tibde.getValue()).booleanValue());}
                else {
                    mLogger.error("Unknown TibDataElement returned");
                }
            }
            processInfo.add(data);
        }
        mLogger.debug("Finished getting Process Status. Retuning...");
        ProcessInfoObj[] pi = new ProcessInfoObj[processInfo.size()];
        for (int p = 0; p < processInfo.size(); p++) {
            pi[p] = (ProcessInfoObj)processInfo.get(p);
        }
        return pi;
    }

    /**
     * This method will return the status (ProcessInfo structure) for the specified
     * Process name.
     * @param applicationType
     * @param applicationInstance
     * @param processName  The logical name defined in Process Sequencer.
     * @return ProcessInfo
     * @throws PsAPIException if falure occures getting status or if the
     * processName is not found.
     */
    public ProcessInfoRec getProcessStatusFor(String applicationType, String applicationInstance, String processName) throws PsAPIException {
        mLogger.debug("entered");
        ProcessInfoObj[] process =  getProcessStatus(applicationType, applicationInstance);
        ProcessInfoRec rec = new ProcessInfoRec();

        for (int i = 0; i < process.length; i++ ) {
            if (process[i].getProcessName().equalsIgnoreCase(processName)) {
                rec.setExecTime(process[i].getExecTime());
                rec.setHeartbeatResult(process[i].getHeartbeatResult());
                rec.setHostname(process[i].getHostname());
                rec.setIsMonitor(process[i].getIsMonitor());
                rec.setMissedHeartbeats(process[i].getMissedHeartbeats());
                rec.setProcessId(process[i].getProcessId());
                rec.setProcessName(process[i].getProcessName());
                rec.setStartGeneration(process[i].getStartGeneration());
                rec.setState(process[i].getState());
                rec.setSuccessfulHeartbeats(process[i].getSuccessfulHeartbeats());
                rec.setUsesNativeLogging(process[i].getUsesNativeLogging());
                return rec;
            }
        }
        throw new PsAPIException("No process found by name: " + processName);
    }


    /**
     * This method will return the status for a given Group Name.  The values can be one of "up" "down"
     * "Transitioning up" "Transitioning down".
     * @param applicationType
     * @param applicationInstance
     * @param groupName Group Name
     * @return String - Group Status Name
     * @throws PsAPIException if falure occures getting status or if the
     * groupName is not found.
     */
    public String getGroupState(String applicationType, String applicationInstance, String groupName) throws PsAPIException {
        mLogger.debug("entered");
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("GroupName", groupName));
        result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance), "getGroupState", args);
        mLogger.debug("returned from getGroupState method");
        String GroupState = (String)((TibDataElement)result.getFirst()).getValue();
        return GroupState;
    }

    /**
     * This will return an array of SolutionStatusInfo object.  You will know the status (health) of each application that makes
     * up the solution.
     * @param solutionName
     * @return
     * @throws PsAPIException
     */
    public SolutionStatusInfoObj[] getSolutionStatus(String solutionName) throws PsAPIException {
        mLogger.debug("entered");
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("SolutionName", solutionName));
        result = invokeMethod(getProcessMicroAgentID(), "getSolutionStatus", args);
        mLogger.debug("returned from getSolutionStatus method");
        LinkedList list = new LinkedList();
        for (int i = 0; i<result.size(); i++) {
            SolutionStatusInfoObj data = new SolutionStatusInfoObj();
            LinkedList info = (LinkedList)result.get(i);
            //mLogger.debug(info.size() + " returned from row");
            for (int j = 0; j<info.size(); j++) {
                TibDataElement tibde = (TibDataElement)info.get(j);
                //mLogger.debug("processing: " + tibde.getName());
                if (tibde.getName().equals("ApplicatonName")) {
                    String appName = tibde.getStringValue();
                    StringTokenizer st = new StringTokenizer(appName, ".");
                    data.setAppType((String)st.nextElement());
                    data.setAppInstance((String)st.nextElement());
                }
                else if (tibde.getName().equals("ApplicationState")) {
                    data.setState(tibde.getStringValue());}

                else {
                    mLogger.error("Unknown TibDataElement returned");
                }
            }
            list.add(data);
        }
        SolutionStatusInfoObj[] ssi = new SolutionStatusInfoObj[list.size()];
        for (int p = 0; p < list.size(); p++) {
            ssi[p] = (SolutionStatusInfoObj)list.get(p);
        }
        return ssi;

    }

    /**
     * This method will return an array of all groups defined within Process Sequencer.
     * @param applicationType
     * @param applicationInstance
     * @return String[] array of group names
     * @throws PsAPIException if falues occures getting group names
     */
    public String[] getAllGroupNames(String applicationType, String applicationInstance) throws PsAPIException {
        mLogger.debug("entered");
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance), "getAllGroupNames", args);
        mLogger.debug("returned from getAllGroupNames method");
        LinkedList list = new LinkedList();
        for (int i = 0; i<result.size(); i++) {
            String data = new String();
            LinkedList info = (LinkedList)result.get(i);
            //mLogger.debug(info.size() + " returned from row");
            for (int j = 0; j<info.size(); j++) {
                TibDataElement tibde = (TibDataElement)info.get(j);
                //mLogger.debug("processing: " + tibde.getName());
                if (tibde.getName().equals("GroupName")) {
                    data = tibde.getStringValue();
                }
                else {
                    mLogger.error("Unknown TibDataElement returned");
                }
            }
            list.add(data);
        }
        String[] groupNames = new String[list.size()];
        for (int p = 0; p < list.size(); p++) {
            groupNames[p] = (String)list.get(p);
        }
        return groupNames;
    }

    /**
     * This method will return an array of process names for a given group.
     * @param applicationType
     * @param applicationInstance
     * @param groupName Group Name
     * @return String[] of process names
     * @throws PsAPIException
     */
    public String[] getProcessesForGroup(String applicationType, String applicationInstance, String groupName) throws PsAPIException {
        mLogger.debug("entered");
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("GroupName", groupName));
        result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance), "getGetProcessesForGroup", args);
        mLogger.debug("returned from getGetProcessesForGroup method");
        LinkedList list = new LinkedList();
        for (int i = 0; i<result.size(); i++) {
            String data = new String();
            LinkedList info = (LinkedList)result.get(i);
            //mLogger.debug(info.size() + " returned from row");
            for (int j = 0; j<info.size(); j++) {
                TibDataElement tibde = (TibDataElement)info.get(j);
                //mLogger.debug("processing: " + tibde.getName());
                if (tibde.getName().equals("ProcessName")) {
                    data = tibde.getStringValue();
                }
                else {
                    mLogger.error("Unknown TibDataElement returned");
                }
            }
            list.add(data);
        }
        String[] processNames = new String[list.size()];
        for (int p = 0; p < list.size(); p++) {
            processNames[p] = (String)list.get(p);
        }
        return processNames;

    }

    /**
     * This method will return the health of the Process Sequencer server and the process
     * managed by Process Sequencer.  If all process are up and running then true is returned
     * else the value will be false.
     * @param applicationType
     * @param applicationInstance
     * @return boolean
     * @throws PsAPIException
     */
    public boolean getHealth(String applicationType, String applicationInstance) throws PsAPIException {
        mLogger.debug("entered");
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance), "getHealth", args);
        mLogger.debug("returned from getHealth method");
        Boolean health = (Boolean)((TibDataElement)result.getFirst()).getValue();
        return health.booleanValue();
    }

    /**
     * This method will return the health of the solution.  True then all applications are up and running,
     * false and one or more application is down or is having a problem.
     * @param solutionName
     * @return
     * @throws PsAPIException
     */
    public boolean getSolutionHealth(String solutionName) throws PsAPIException {
        mLogger.debug("entered");
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("SolutionName", solutionName));
        result = invokeMethod(getProcessMicroAgentID(), "getSolutionHealth", args);
        mLogger.debug(result.size() + " returned from getSolutionHealth method");
        LinkedList list = (LinkedList)result.getFirst();
        Boolean health = (Boolean)((TibDataElement)list.getFirst()).getValue();
        return health.booleanValue();
    }

    /**
     * This method will start the process specified.  The process may not start if it
     * dependent on another process.  A PsAPIException will be generated if it
     * can't start because it is dependent on something else  but the state of the process
     * will be changed to disabled_dependent.
     * @param applicationType
     * @param applicationInstance
     * @param processName Process Name
     * @return There is nothing returned
     * @throws PsAPIException if the process fails to start or there is an error
     * processing the command or the processName is invalid.
     */
    public void startProcess(String applicationType, String applicationInstance, String processName, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
            mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                          "ProcessName=" + processName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking startProcess method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with startProcess method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with startProcess method:" + e);
             }
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("ProcessName", processName));
        try {
            result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance),
                                  "startProcess", args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod startProcess failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                          "ProcessName=" + processName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking startProcess method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with startProcess method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with startProcess method:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod startProcess failed: " + ex);
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                     AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                     "ProcessName=" + processName,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod startProcess");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with startProcess method:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with startProcess method:" + e);
        }

        mLogger.debug("returned from startProcess method");
    }

    /**
     * This method will start processes given a command line argument: [all | service_name | group <groupname>]
     * @param applicationType
     * @param applicationInstance
     * @param command
     * @return Result string showing the status of the command.
     * @throws PsAPIException
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    public String startCommand(String applicationType, String applicationInstance, String command, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
            mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking startCommand method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with application startCommand:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with application startCommand:" + e);
             }
            throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("Command", command));
        try {
            result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance),
                                  "startCommand", args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod startCommand failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking startCommand method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with application startCommand:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with application startCommand:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod startCommand failed: " + ex);
        }

        mLogger.debug("returned from startCommand method");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i<result.size(); i++) {
            LinkedList info = (LinkedList)result.get(i);
            //mLogger.debug(info.size() + " returned from row");
            for (int j = 0; j < info.size(); j++) {
                TibDataElement tibde = (TibDataElement)info.get(j);
                //mLogger.debug("processing: " + tibde.getName());
                if (tibde.getName().equals("Result"))
                {
                    sb.append(tibde.getStringValue());
                }
            }
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                     AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod startCommand");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with application startCommand:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with application startCommand:" + e);
        }

        return sb.toString();
    }

    /**
     * This method will start all process belonging to the group specified.  No
     * WatchdogAPIExeception will be returned if one process can't start.  You will need
     * to call getGroupStatus(groupName) to find out if the command was successfull.
     * @param applicationType
     * @param applicationInstance
     * @param groupName Group Name
     * @return nothing returned
     * @throws PsAPIException if there was an error starting the group or
     * the group name was invalid.
     */
    public void startGroup(String applicationType, String applicationInstance, String groupName, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                          "GroupName=" + groupName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking startGroup method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with startGroup method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with startGroup method:" + e);
             }
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("GroupName", groupName));
        try {
            result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance), "startGroup",
                                  args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod startGroup failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                          "GroupName=" + groupName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking startGroup method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with startGroup method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with startGroup method:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod startApplication failed: " + ex);
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                     AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                     "GroupName=" + groupName,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod startGroup");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with startGroup method:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with startGroup method:" + e);
        }

        mLogger.debug("returned from startGroup method");
    }

    /**
     * This method will start all processes defined in watchdog for a given host.
     * @param applicationType
     * @param applicationInstance
     * @return nothing returned
     * @throws PsAPIException if there was an error changing all the process to
     * starting or if there was an internal error.
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    public void startAll(String applicationType, String applicationInstance, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance), "startAll", args);
        mLogger.debug("returned from startAll method");
    }

    /**
     * This method will stop the specified processName.
     * @param applicationType
     * @param applicationInstance
     * @param processName Process Name
     * @return nothing
     * @throws PsAPIException if there was error stoping the process.
     */
    public void stopProcess(String applicationType, String applicationInstance, String processName, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                          "ProcessName=" + processName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking stopProcess method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with stopProcess method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with stopProcess method:" + e);
             }
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("ProcessName", processName));
        try {
            result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance),
                                  "stopProcess", args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod stopProcess failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                          "ProcessName=" + processName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking stopProcess method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with stopProcess method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with stopProcess method:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod stopProcess failed: " + ex);
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                     AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                     "ProcessName=" + processName,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod stopProcess");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with stopProcess method:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with stopProcess method:" + e);
        }

        mLogger.debug("returned from stopProcess method");
    }

    /**
     * This method will stop processes given a command line argument: [all | service_name | group <groupname>]
     * @param applicationType
     * @param applicationInstance
     * @param command
     * @return Result string show the status of the command
     * @throws PsAPIException
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    public String stopCommand(String applicationType, String applicationInstance, String command, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking stopCommand method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with stopCommand method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with stopCommand method:" + e);
             }
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("Command", command));
        try {
            result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance),
                                  "stopCommand", args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod stopCommand failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking stopCommand method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with stopCommand method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with stopCommand method:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod stopCommand failed: " + ex);
        }

        mLogger.debug("returned from stopCommand method");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i<result.size(); i++) {
            LinkedList info = (LinkedList)result.get(i);
            //mLogger.debug(info.size() + " returned from row");
            for (int j = 0; j < info.size(); j++) {
                TibDataElement tibde = (TibDataElement)info.get(j);
                //mLogger.debug("processing: " + tibde.getName());
                if (tibde.getName().equals("Result"))
                {
                    sb.append(tibde.getStringValue());
                }
            }
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                     AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod stopCommand");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with stopCommand method:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with stopCommand method:" + e);
        }

        return sb.toString();
    }

    /**
     * This method will stop the specified groupName.
     * @param applicationType
     * @param applicationInstance
     * @param groupName Group Name
     * @return nothing
     * @throws PsAPIException if there was an error stoping the group
     */
    public void stopGroup(String applicationType, String applicationInstance, String groupName, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                          "GroupName=" + groupName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking stopGroup method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with stopGroup method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with stopGroup method:" + e);
             }
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("GroupName", groupName));
        try {
            result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance), "stopGroup",
                                  args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod stopGroup failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                          "GroupName=" + groupName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking stopGroup method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with stopGroup method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with stopGroup method:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod stopGroup failed: " + ex);
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                     AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                     "GroupName=" + groupName,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod stopGroup");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with stopGroup method:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with stopGroup method:" + e);
        }

        mLogger.debug("returned from stopGroup method");
    }

    /**
     * This method will stop all process defined in Process Sequencer for a given host, however
     * Process Sequencer its self will stay running.
     * @param applicationType
     * @param applicationInstance
     * @return nothing
     * @throws PsAPIException if there is an error stopping all the processes.
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    public void stopAll(String applicationType, String applicationInstance, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance), "stopAll", args);
        mLogger.debug("returned from stopAll method");
    }

    /**
     * This method will restart the specified processName.  This command is simular to
     * issuing a stop waiting for a short time and issueing a start command for the process.
     * @param applicationType
     * @param applicationInstance
     * @param processName Process name
     * @return nothing
     * @throws PsAPIException if there is an error restarting the process.
     */
    public void restartProcess(String applicationType, String applicationInstance, String processName, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                          "ProcessName=" + processName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking restartProcess method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with restartProcess method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with restartProcess method:" + e);
             }
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("ProcessName", processName));
        try {
            result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance),
                                  "restartProcess", args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod restartProcess failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                          "ProcessName=" + processName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking restartProcess method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with restartProcess method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with restartProcess method:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod restartProcess failed: " + ex);
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                     AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                     "ProcessName=" + processName,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod restartProcess");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with restartProcess method:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with restartProcess method:" + e);
        }
        mLogger.debug("returned from restartProcess method");
    }

    /**
     * This methog will restart the specified groupName.  This command is simular to
     * issuing a stop for the group, waiting, and then issuing a start command for the
     * group.
     * @param applicationType
     * @param applicationInstance
     * @param groupName Group Name
     * @return nothing
     * @throws PsAPIException if there is an error restarting the group.
     */
    public void restartGroup(String applicationType, String applicationInstance, String groupName, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                          "GroupName=" + groupName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking restartGroup method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with restartGroup method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with restartGroup method:" + e);
             }
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("GroupName", groupName));
        try {
            result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance),
                                  "restartGroup", args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod restartGroup failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                          "GroupName=" + groupName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking restartGroup method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with restartGroup method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with restartGroup method:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod restartGroup failed: " + ex);
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                     AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance + ";" +
                                                     "GroupName=" + groupName,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod restartGroup");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with restartGroup method:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with restartGroup method:" + e);
        }

        mLogger.debug("returned from restartGroup method");
    }

    /**
     * This method will restart processes given a command line argument: [all | service_name | group <groupname>]
     * @param applicationType
     * @param applicationInstance
     * @param command
     * @return Result string showing the status of the restart command
     * @throws PsAPIException
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    public String restartCommand(String applicationType, String applicationInstance, String command, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking restartCommand method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with restartCommand method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with restartCommand method:" + e);
             }
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("Command", command));
        try {
            result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance),
                                  "restartCommand", args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod restartCommand failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking restartCommand method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with restartCommand method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with restartCommand method:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod restartCommand failed: " + ex);
        }
        mLogger.debug("returned from restartCommand method");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i<result.size(); i++) {
            LinkedList info = (LinkedList)result.get(i);
            //mLogger.debug(info.size() + " returned from row");
            for (int j = 0; j < info.size(); j++) {
                TibDataElement tibde = (TibDataElement)info.get(j);
                //mLogger.debug("processing: " + tibde.getName());
                if (tibde.getName().equals("Result"))
                {
                    sb.append(tibde.getStringValue());
                }
            }
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                     AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod startApplication");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with startApplication method:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with startApplication method:" + e);
        }
        return sb.toString();
    }

    /**
     * This will restart all process defined in Process Sequencer.  This command is simular to
     * issuing a stopAll(), waiting a  short time and issueing startAll().
     * @param applicationType
     * @param applicationInstance
     * @return nothing
     * @throws PsAPIException
     */
    public void restartAll(String applicationType, String applicationInstance, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance), "restartAll", args);
        mLogger.debug("returned from restartAll method");
    }

    /**
     * This will get the Log Level for the Application Watchdog Process
     * @param applicationType
     * @param applicationInstance
     * @return LogLevel
     * @throws PsAPIException
     */
    public LogLevel getLogLevel(String applicationType, String applicationInstance) throws PsAPIException {
        mLogger.debug("entered");
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance), "getLogLevel", args);
        mLogger.debug("returned from getLogLevel method");
        LogLevel log;
        String logLevel = (String)((TibDataElement)result.getFirst()).getValue();
        log = new LogLevel();
        try {
            log.fromString(logLevel);
        }
        catch (EmanagerInventoryException e) {
            throw new PsAPIException("getLogLevel returned invalid LogLevel");
        }
        return log;
    }

    /**
     * This will get the Log Level for the Application Watchdog Process
     * @param applicationType
     * @param applicationInstance
     * @return xml stream LogLevel
     * @throws PsAPIException
     */
    public String getXmlLogLevel(String applicationType, String applicationInstance) throws PsAPIException {
        mLogger.debug("entered");
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<LogLevel>");
        xmlBuf.append(getLogLevel(applicationType, applicationInstance));
        xmlBuf.append("</LogLevel>");
        return xmlBuf.toString();
    }

    /**
     * This method sets the Watchdog Log Level
     * Valid values: "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER",
     *      "FINEST"
     * @param applicationType
     * @param applicationInstance
     * @param logLevel
     * @throws PsAPIException
     */
    public void setLogLevel(String applicationType, String applicationInstance, LogLevel logLevel) throws PsAPIException {
        mLogger.debug("entered");
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("LogLevel", logLevel.toString()));
        result = invokeMethod(getWatchdogMicroAgentID(applicationType, applicationInstance), "setLogLevel", args);
        mLogger.debug("returned from setLogLevel method");
    }

    /**
     * This method registers the application to Watchdog with a properties file.  The application
     * must be registered before the application can be started or managed.
     * @param applicationType
     * @param applicationInstance
     * @throws PsAPIException
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    public void register(String applicationType, String applicationInstance, String applicationProperyFile) throws PsAPIException {
        mLogger.debug("entered");
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("AppType", applicationType));
        args.add(new TibDataElement("AppInst", applicationInstance));
        args.add(new TibDataElement("AppPropFile", applicationProperyFile));
        result = invokeMethod(getProcessMicroAgentID(), "register", args);
        mLogger.debug("returned from register method");
    }

    /**
     * This method will register a Solution to Process Sequencer.  Each application must first
     * be registerd before you can register the solution otherwise an exception is thrown.
     * @param solutionDefinition XML string that looks something like this:
     *
     * <Solution>
     *   <SolutionName>solution-name</SolutionName>
     *   <Components>
     *      <Component>
     *         <AppType>CNOTE</AppType>
     *         <AppInstance>cnote1</AppInstance>
     *         <AppVersion>1.0</AppVersion>
     *         <HostName>hostname.cisco.com</HostName>
     *      </Component>
     *   </Components>
     * </Solution>
     *
     * @throws PsAPIException
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    public void registerSolution(String solutionDefinition) throws PsAPIException {
        mLogger.debug("entered");
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("SolutionRegisterXML", solutionDefinition));
        result = invokeMethod(getProcessMicroAgentID(), "registerSolution", args);
        mLogger.debug("returned from registerSolution method");
    }

    /**
     * This method removes the registration from Watchdog for a application and removes the properties file.
     * @param applicationType
     * @param applicationInstance
     * @throws PsAPIException
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    public void unregister(String applicationType, String applicationInstance) throws PsAPIException {
        mLogger.debug("entered");
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("AppType", applicationType));
        args.add(new TibDataElement("AppInst", applicationInstance));
        result = invokeMethod(getProcessMicroAgentID(), "unregister", args);
        mLogger.debug("returned from unregister method");
    }

    /**
     * This method will start the specific application using Watchdog.
     * @param applicationType
     * @param applicationInstance
     * @throws PsAPIException
     */
    public void startApplication(String applicationType, String applicationInstance, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking startApplication method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with startApplication method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with startApplication method:" + e);
             }
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("AppType", applicationType));
        args.add(new TibDataElement("AppInst", applicationInstance));
        try {
            result = invokeMethod(getProcessMicroAgentID(), "startApplication", args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod startApplication failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking startApplication method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with startApplication method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with startApplication method:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod startApplication failed: " + ex);
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                     AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod startApplication");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with startApplication method:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with startApplication method:" + e);
        }

        mLogger.debug("returned from startApplication method");
    }

    /**
     * This method will allow you to start a solution (multiple applications under the solution).
     * If one or more of the applications are already running then only the ones not running will be
     * started.
     * @param solutionName
     * @throws PsAPIException
     */
    public void startSolution(String solutionName, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          "SolutionName=" + solutionName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking startSolution method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with startSolution method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with startSolution method:" + e);
             }
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("SolutionName", solutionName));
        try {
            result = invokeMethod(getProcessMicroAgentID(), "startSolution", args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod startSolution failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          "SolutionName=" + solutionName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking startSolution method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with startSolution method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with startSolution method:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod startSolution failed: " + ex);
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     "SolutionName=" + solutionName,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod startSolution");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with startSolution method:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with startSolution method:" + e);
        }

        mLogger.debug("returned from startSolution method");
    }

    /**
     * This method will stop the specific application using Watchdog.
     * @param applicationType
     * @param applicationInstance
     * @throws PsAPIException
     */
    public void stopApplication(String applicationType, String applicationInstance, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking stopApplication method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with stopApplication method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with stopApplication method:" + e);
             }
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("AppType", applicationType));
        args.add(new TibDataElement("AppInst", applicationInstance));
        try {
            result = invokeMethod(getProcessMicroAgentID(), "stopApplication", args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod stopApplication failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                          AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking stopApplication method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with stopApplication method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with stopApplication method:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod stopApplication failed: " + ex);
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     AuditGlobals.AppTypeSubjectKey + "=" + applicationType + ";" +
                                                     AuditGlobals.AppInstanceSubjectKey + "=" + applicationInstance,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod stopApplication");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with stopApplication method:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with stopApplication method:" + e);
        }

        mLogger.debug("returned from stopApplication method");
    }

    /**
     * This method will allow you to stop a solution and bring down all applications that make up the
     * solution.
     * @param solutionName
     * @throws PsAPIException
     */
    public void stopSolution(String solutionName, String userId, AccessType access) throws PsAPIException {
        mLogger.debug("entered");
        if (access.isReadAccess()) {
             mLogger.error("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
             try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          "SolutionName=" + solutionName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.AuthorizationFailureIndicator + "while invoking stopSolution method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with stopSolution method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with stopSolution method:" + e);
             }
             throw new PsAPIException("AUTHORIZATION FAILURE: The user " + userId + " does not have permission for this command");
        }
        LinkedList result = new LinkedList();
        LinkedList args = new LinkedList();
        args.add(new TibDataElement("SolutionName", solutionName));
        try {
            result = invokeMethod(getProcessMicroAgentID(), "stopSolution", args);
        }
        catch (Exception ex) {
            mLogger.error("EXECUTION FAILURE: InvokeMethod stopSolution failed: " + ex);
            try {
                 AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                          AuditAction.Update,
                                                          "SolutionName=" + solutionName,
                                                          new Date(),
                                                          userId,
                                                          AuditGlobals.ExecutionFailureIndicator + "while invoking stopSolution method");
             }
             catch (EmanagerAuditException e) {
                 mLogger.warn ("Error writing audit log message associated with stopSolution method:" + e);
             }
             catch (EmanagerDatabaseException e) {
                 mLogger.warn ("Error writing audit log message associated with stopSolution method:" + e);
             }
             throw new PsAPIException("EXECUTION FAILURE: InvokeMethod stopSolution failed: " + ex);
        }
        try {
            AuditManager.instance().addAuditLogEntry(AuditDomain.Process,
                                                     AuditAction.Update,
                                                     "SolutionName=" + solutionName,
                                                     new Date(),
                                                     userId,
                                                     "InvokedMethod stopSolution");
        }
        catch (EmanagerAuditException e) {
            mLogger.warn ("Error writing audit log message associated with stopSolution method:" + e);
        }
        catch (EmanagerDatabaseException e) {
            mLogger.warn ("Error writing audit log message associated with stopSolution method:" + e);
        }

        mLogger.debug("returned from stopSolution method");
    }


    public static void main(String[] args) {
        String eManagerDir = System.getProperty("EMANAGER_ROOT");
        String log4jConfigFile = eManagerDir + "/config/log4j.properties";
        org.apache.log4j.PropertyConfigurator.configure(log4jConfigFile);

        ProcessManager pm = instance();
        String emHome = pm.getProcessSequencerEMHome();
        mLogger.info("ProcessSequencer Home: " + emHome);

        while (true) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                mLogger.error("ProcessManager::main catch exception: " + e.getMessage());
                Thread.currentThread().interrupted();
            }
        }

        /*
        try {

            ProcessInfo[] pi = pm.getProcessStatus("eManager", "app");
            //ProcessInfo[] pi = new ProcessInfo[obj.length];
            mLogger.debug("Getting ready to call toString()");
            for (int i = 0; i<pi.length; i++) {
                //pi[i]=obj[i];
                mLogger.info(pi[i].toString());
            }

            mLogger.info(pm.getXmlProcessStatus("eManager", "app"));

            mLogger.info(pm.getGroupState("eManager", "app", "eManagerApp"));
            mLogger.info(pm.getXmlGroupState("eManager", "app", "eManagerApp"));
            mLogger.info(pm.getProcessStatusFor("eManager", "app", "processSequencer"));
            mLogger.info(pm.getXmlProcessStatusFor("eManager", "app", "processSequencer"));
            mLogger.info(pm.getXmlSolutionStatus("Service Appliance"));
            mLogger.info(pm.getXmlAllGroupNames("eManager", "app"));
            mLogger.info(pm.getXmlProcessesForGroup("eManager", "app", "eManagerApp"));
            mLogger.info(pm.getXmlHealth("eManager", "app"));
            mLogger.info(pm.getXmlSolutionHealth("Service Appliance"));

            String xml = "<processMgrMsg>" +
                         "	<getGroupState>" +
                         "		<applicationType>eManager</applicationType>" +
                         "		<applicationInstance>app</applicationInstance>" +
                         "		<groupName>eManager</groupName>" +
                         "	</getGroupState>" +
                         "</processMgrMsg>";
            mLogger.info(pm.handleRequest(xml, "mamiles", AccessType.WRITE));
            xml =        "<processMgrMsg>" +
                         "	<getProcessStatus>" +
                         "		<applicationType>eManager</applicationType>" +
                         "		<applicationInstance>app</applicationInstance>" +
                         "	</getProcessStatus>" +
                         "</processMgrMsg>";
            mLogger.info(pm.handleRequest(xml, "mamiles", AccessType.WRITE));

        }
        catch (PsAPIException e) {
            mLogger.error(e.getMessage());
        }
        */

        //System.exit(0);
    }

}

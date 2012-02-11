package com.cisco.eManager.eManager.log;

import com.cisco.eManager.eManager.util.GlobalProperties;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.log.*;
import com.cisco.eManager.common.util.*;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.appInstance.*;
import com.cisco.eManager.eManager.inventory.view.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.io.*;
import java.io.File;
import org.exolab.castor.xml.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public class LogManager implements LogManagerInterface
{
    private static LogManager instance = null;
    private static Logger mLogger = Logger.getLogger(LogManager.class);
    private String logCache = null;
    private String logUrl = null;
    private String hostName = null;
    private String catalinaHome = null;
    private String webappsDir = null;
    private String tomcatPort = null;
    private LocalLog localLog;
    private String emHome = null;

    public static LogManager instance() {
        mLogger.debug("enter");
        if (instance == null)
        {
            instance = new LogManager();
        }
        return instance;
    }

    private LogManager()
    {
        mLogger.debug("Log Manager now started.");
        setupConfig();

        logCache = new String(catalinaHome + "/webapps/emanager/logDisplay");
        webappsDir = new String(catalinaHome + "/webapps/emanager/WEB-INF");
        localLog = new LocalLog(webappsDir);
        hostName = getLocalHostName();
        logUrl = "http://" + hostName + ":" + tomcatPort + "/emanager/logDisplay";

        try {
            new PurgeServerCache(catalinaHome);
        }
        catch (LogManagerException ex) {
            mLogger.error(ex);
        }
        mLogger.info("Component configuration initialized successfully...");
    }

    private void setupConfig() {
        GlobalProperties globalProp = GlobalProperties.instance();
        Properties emProp = globalProp.getProperties();

        emHome = System.getProperty("EMANAGER_ROOT");
        mLogger.debug("eManager Home directory (EMANAGER_ROOT):" + emHome);
        catalinaHome = System.getProperty("CATALINA_HOME");
        mLogger.debug("CATALINA_HOME:" + catalinaHome);
        tomcatPort = System.getProperty("TOMCAT_PORT");
        mLogger.debug("TOMCAT_PORT:" + catalinaHome);
    }


    private StatusResp successStatus() {
        StatusResp status = new StatusResp();
        Rc rc = new Rc();
        rc.setSuccess(0);
        status.setRc(rc);
        status.setDescription("Successful");
        return status;
    }

    private StatusResp failureStatus(int rcode, String desc) {
        StatusResp status = new StatusResp();
        Rc rc = new Rc();
        rc.setFailure(rcode);
        status.setRc(rc);
        status.setDescription(desc);
        return status;
    }

    public String handleRequest(String xmlStream, String userId, AccessType access) {
        StringWriter response = new StringWriter();
        LogMgrMsg msg = null;
        LogMgrResp resp = new LogMgrResp();

        try
        {
            msg = LogMgrMsg.unmarshal(new StringReader(xmlStream));

            GetLogFilesFromDirectories xmlGetLogFilesFromDirectories = msg.getGetLogFilesFromDirectories();
            if (xmlGetLogFilesFromDirectories != null) {
                GetLogFilesFromDirectoriesResp logFilesResp = new GetLogFilesFromDirectoriesResp();
                logFilesResp = getLogFilesFromDirectories(xmlGetLogFilesFromDirectories);
                resp.setGetLogFilesFromDirectoriesResp(logFilesResp);
                successStatus().marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            GetLogURL xmlGetLogURL = msg.getGetLogURL();
            if (xmlGetLogURL != null) {
                GetLogURLResp logURLResp = new GetLogURLResp();
                logURLResp = getLogURL(xmlGetLogURL);
                resp.setGetLogURLResp(logURLResp);
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
        catch (LogManagerException ex) {
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


    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectories(GetLogFilesFromDirectories parm)
        throws LogManagerException {
        GetLogFilesFromDirectoriesResp resp = new GetLogFilesFromDirectoriesResp();

        GetLogFilesFromDirectoriesChoice choice = parm.getGetLogFilesFromDirectoriesChoice();

        AppInstanceId id = choice.getAppInstanceId();
        if (id != null) {
            resp = getLogFilesFromDirectories(id.getObjectKey(), id.getObjectType(), parm.getSearchFilter());
        }

        AppTypeAppInstName name = choice.getAppTypeAppInstName();
        if (name != null) {
            resp = getLogFilesFromDirectories(name.getAppTypeName(), name.getAppInstanceName(), parm.getSearchFilter());
        }

        AppViewAppInstanceFDN viewFDN = choice.getAppViewAppInstanceFDN();
        if (viewFDN != null) {
            resp = getLogFilesFromDirectoriesAppViewFDN(viewFDN.getElementName(), parm.getSearchFilter());
        }

        PhysicalViewAppInstanceFDN phyFDN = choice.getPhysicalViewAppInstanceFDN();
        if (phyFDN != null) {
            resp = getLogFilesFromDirectoriesPhyViewFDN(phyFDN.getElementName(), parm.getSearchFilter());
        }

        return resp;
    }

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectories(long objectKey, ObjectType objType, String searchFilter) throws LogManagerException  {
        GetLogFilesFromDirectoriesResp resp = new GetLogFilesFromDirectoriesResp();
        ManagedObjectIdType managedObjectType = null;
        List fileNameAttr = new LinkedList();

        if (objType.hasAppInstanceType()) {
            managedObjectType = ManagedObjectIdType.ApplicationInstance;
        }
        else {
            mLogger.error("The Object Type must be of type ApplicationInstance");
            throw new LogManagerException("The Object Type must be of type ApplicationInstance");
        }
        AppInstanceManager appInstMgr = AppInstanceManager.instance();
        ManagedObjectId moid = new ManagedObjectId(managedObjectType, objectKey);
        AppInstance appInst = appInstMgr.find(moid);

        List logDirs = getListFromArray(appInst.logfileDirectorySet());

        // Determine if we are local or remote to the AppInstance
        if (getShortHostName(appInst.host().hostname()).equals(hostName) || getShortHostName(appInst.host().hostname()).equals("localhost")) {
            mLogger.debug("Log Files are Local");
            if (searchFilter != null) {
                if (searchFilter.equals(""))
                {
                    mLogger.debug("Filter: '' - Calling localLog.getFileList(logDirs)");
                    fileNameAttr = localLog.getFileList(logDirs);
                }
                else
                {
                    mLogger.debug("Calling localLog.getFileListWithString(logDirs, filterString)");
                    fileNameAttr = localLog.getFileListWithString(logDirs, searchFilter);
                }
            }
            else {
                mLogger.debug("Filter: null - Calling localLog.getFileList(logDirs)");
                fileNameAttr = localLog.getFileList(logDirs);
            }
            Iterator it = fileNameAttr.iterator();
            while (it.hasNext()) {
                FileNameAttr fna = (FileNameAttr) it.next();
                resp.addLogFiles(populateLogFiles(fna, appInst));
            }
        }
        else {
            mLogger.debug("Log Files are Remote");
            if (appInst.logfileTransport().isSsh()) {
                mLogger.info("Secure Shell being used");
                SshLog ssh = new SshLog(webappsDir,
                                        appInst.host().hostname(),
                                        appInst.logfileUsername(),
                                        appInst.logfilePassword(),
                                        appInst.unixPrompt());
                if (searchFilter != null) {
                    if (searchFilter.equals(""))
                    {
                        mLogger.debug("Filter: '' - Calling ssh.getFileList(logDirs)");
                        fileNameAttr = ssh.getFileList(logDirs);
                    }
                    else
                    {
                        mLogger.debug("Calling ssh.getFileListWithString(logDirs, searchFilter)");
                        fileNameAttr = ssh.getFileListWithString(logDirs, searchFilter);
                    }
                }
                else {
                    mLogger.debug("Filter: null - Calling ssh.getFileList(logDirs)");
                    fileNameAttr = ssh.getFileList(logDirs);
                }
                Iterator itsh = fileNameAttr.iterator();
                while (itsh.hasNext()) {
                    FileNameAttr fnaa = (FileNameAttr) itsh.next();
                    resp.addLogFiles(populateLogFiles(fnaa, appInst));
                }
            }
            else if (appInst.logfileTransport().isTelnet()) {
                TelnetLog telnet = new TelnetLog(appInst.host().hostname(),
                                                 appInst.logfileUsername(),
                                                 appInst.logfilePassword(),
                                                 appInst.unixPrompt());
                if (searchFilter.equals("") || searchFilter == null) {
                    mLogger.debug("Calling telnet.getFileList(logDirs)");
                    fileNameAttr = telnet.getFileList(logDirs);
                }
                else {
                    mLogger.debug("Calling telnet.getFileListWithString(logDirs, searchFilter)");
                    fileNameAttr = telnet.getFileListWithString(logDirs, searchFilter);
                }
                Iterator itt = fileNameAttr.iterator();
                while (itt.hasNext()) {
                    FileNameAttr fnaa = (FileNameAttr) itt.next();
                    resp.addLogFiles(populateLogFiles(fnaa, appInst));
                }
            }
        }
        mLogger.debug("Ready to return List of LogFile objects");
        return resp;
    }

    private LogFiles populateLogFiles(FileNameAttr fna, AppInstance appInst) {
        LogFiles logFiles = new LogFiles();
        logFiles.setLogPath(fna.getFileNamePath());
        logFiles.setLogName(fna.getShortFileName());
        logFiles.setLogSize(fna.getSizeString());
        logFiles.setDateTime(fna.getChangeDate());
        logFiles.setHostName(appInst.host().hostname());
        LogFileChoice key = new LogFileChoice();
        AppTypeAppInstName appTypeAppInstName = new AppTypeAppInstName();
        appTypeAppInstName.setAppTypeName(appInst.appType().name());
        appTypeAppInstName.setAppInstanceName(appInst.name());
        key.setAppTypeAppInstName(appTypeAppInstName);
        logFiles.setLogFileChoice(key);
        return logFiles;
    }

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectories(String appTypeName, String appInstanceName, String searchFilter) throws LogManagerException {
        GetLogFilesFromDirectoriesResp resp = new GetLogFilesFromDirectoriesResp();
        AppInstanceManager appInstMgr = AppInstanceManager.instance();
        AppInstance[] appInst = appInstMgr.appInstances();
        for (int i = 0; i < appInst.length; i++) {
            if (appTypeName.equals(appInst[i].appType().name()) & appInstanceName.equals(appInst[i].name())) {
                mLogger.debug("Found AppInstance: " + appInstanceName + " AppType: " + appTypeName);
                ObjectType ot = new ObjectType();
                ot.setAppInstanceType(appInst[i].appTypeId().getManagedObjectType().intValue());
                resp = getLogFilesFromDirectories(appInst[i].appTypeId().getManagedObjectKey(), ot, searchFilter);
                return resp;
            }
        }
        mLogger.error("Application Type Name: " + appTypeName + " Application Instance Name: " + appInstanceName + " Not Found");
        throw new LogManagerException("Application Type Name: " + appTypeName + " Application Instance Name: " + appInstanceName + " Not Found");
    }

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectoriesAppViewFDN(String[] appViewElementNames, String searchFilter) throws LogManagerException {
        GetLogFilesFromDirectoriesResp resp = new GetLogFilesFromDirectoriesResp();
        AppViewManager appViewMgr = null;
        try
        {
            appViewMgr = AppViewManager.instance();
        }
        catch (Exception ex)
        {
            mLogger.error("Exception getting access to AppViewManager " + ex);
            throw new LogManagerException("Exception getting access to AppViewManager " + ex);
        }
        NodePath np = new NodePath(appViewElementNames);
        Node node = appViewMgr.find(np);
        if (node != null) {
            if (node instanceof AppInstanceNode) {
                mLogger.debug("Found id of FDN");
                ObjectType ot = new ObjectType();
                ot.setAppInstanceType(node.id().getManagedObjectType().intValue());
                resp = getLogFilesFromDirectories(node.id().getManagedObjectKey(), ot, searchFilter);
                return resp;
            }
            else {
                mLogger.error("The AppView FDN: " + np.toString() + " Not Found or a instance of AppInstanceNode");
                throw new LogManagerException("The AppView FDN: " + np.toString() + " Not Found or a instance of AppInstanceNode");
            }
        }
        else {
            mLogger.error("The AppView FDN: " + np.toString() + " Not Found");
            throw new LogManagerException("The AppView FDN: " + np.toString() + " Not Found");
        }
    }

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectoriesPhyViewFDN(String[] physicalViewElementNames, String searchFilter) throws LogManagerException {
        GetLogFilesFromDirectoriesResp resp = new GetLogFilesFromDirectoriesResp();
        HostViewManager hostViewMgr = null;
        try
        {
            hostViewMgr = HostViewManager.instance();
        }
        catch (Exception ex)
        {
            mLogger.error("Exception getting access to HostViewManager " + ex);
            throw new LogManagerException("Exception getting access to HostViewManager " + ex);
        }
        NodePath np = new NodePath(physicalViewElementNames);
        Node node = hostViewMgr.find(np);
        if (node != null) {
            if (node instanceof AppInstanceNode) {
                mLogger.debug("Found id of FDN");
                ObjectType ot = new ObjectType();
                ot.setAppInstanceType(node.id().getManagedObjectType().intValue());
                resp = getLogFilesFromDirectories(node.id().getManagedObjectKey(), ot, searchFilter);
                return resp;
            }
            else {
                mLogger.error("The HostView FDN: " + np.toString() + " Not Found or a instance of AppInstanceNode");
                throw new LogManagerException("The HostView FDN: " + np.toString() + " Not Found or a instance of AppInstanceNode");
            }
        }
        else {
            mLogger.error("The HostView FDN: " + np.toString() + " Not Found");
            throw new LogManagerException("The HostView FDN: " + np.toString() + " Not Found");
        }
    }


    public GetLogURLResp getLogURL(GetLogURL getLogURL)
        throws LogManagerException {
        GetLogURLResp resp = new GetLogURLResp();

        LogFileChoice choice = getLogURL.getLogFileChoice();

        AppInstanceId id = choice.getAppInstanceId();
        if (id != null) {
            resp = getLogURL(id.getObjectKey(), id.getObjectType(), getLogURL.getLogPath(), getLogURL.getLogName());
        }

        AppTypeAppInstName name = choice.getAppTypeAppInstName();
        if (name != null) {
            resp = getLogURL(name.getAppTypeName(), name.getAppInstanceName(), getLogURL.getLogPath(), getLogURL.getLogName());
        }

        AppViewAppInstanceFDN viewFDN = choice.getAppViewAppInstanceFDN();
        if (viewFDN != null) {
            resp = getLogURLAppViewFDN(viewFDN.getElementName(), getLogURL.getLogPath(), getLogURL.getLogName());
        }

        PhysicalViewAppInstanceFDN phyFDN = choice.getPhysicalViewAppInstanceFDN();
        if (phyFDN != null) {
            resp = getLogURLPhyViewFDN(phyFDN.getElementName(), getLogURL.getLogPath(), getLogURL.getLogName());
        }

        return resp;
    }

    public GetLogURLResp getLogURL(long objectKey, ObjectType objType, String logPath, String logName) throws LogManagerException {
        GetLogURLResp resp = new GetLogURLResp();
        ManagedObjectIdType managedObjectType = null;
        long[] remoteCksumAndSize = {0, 0};
        long[] localCksumAndSize = {0, 0};
        String filePathString = null;
        String fileSplitDir = null;
        File cacheDir;

        if (objType.hasAppInstanceType()) {
            managedObjectType = ManagedObjectIdType.ApplicationInstance;
        }
        else {
            mLogger.error("The Object Type must be of type ApplicationInstance");
            throw new LogManagerException("The Object Type must be of type ApplicationInstance");
        }
        AppInstanceManager appInstMgr = AppInstanceManager.instance();
        ManagedObjectId moid = new ManagedObjectId(managedObjectType, objectKey);
        AppInstance appInst = appInstMgr.find(moid);

        // Determine if we are local or remote to the AppInstance
        if (getShortHostName(appInst.host().hostname()).equals(hostName) || getShortHostName(appInst.host().hostname()).equals("localhost")) {
            filePathString = logPath + "/" + logName;
            fileSplitDir = logCache + "/" + appInst.appType().name() + "/" + appInst.name() + logPath + "/" + logName;
            File localFile = new File(filePathString);
            if (localFile.length() > 2097152) {
                List splitFileNames = fileSplitWithDir(filePathString, fileSplitDir, logName);
                Iterator splitIt = splitFileNames.iterator();
                while (splitIt.hasNext()) {
                    resp.addSplitLogFileURL(logUrl + "/" + appInst.appType().name() + "/" + appInst.name() + logPath + "/" + splitIt.next());
                }
            }
            cacheDir = new File(logCache + "/" + appInst.appType().name() + "/" + appInst.name() + logPath);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File link = new File(fileSplitDir + ".txt");
            if (link.exists()) {
                link.delete();
            }
            localLog.createLink(filePathString, fileSplitDir + ".txt");
            resp.setLogFileURL(logUrl + "/" + appInst.appType().name() + "/" + appInst.name() + logPath + "/" + logName + ".txt");
            return resp;
        }
        else {
            if (appInst.logfileTransport().isSsh()) {
                mLogger.info("Secure Shell being used.");
                filePathString = logCache + "/" + appInst.appType().name() + "/" + appInst.name() + logPath + "/" + logName;
                File cacheFile = new File(filePathString + ".txt");
                SshLog ssh = new SshLog(webappsDir,
                                        appInst.host().hostname(),
                                        appInst.logfileUsername(),
                                        appInst.logfilePassword(),
                                        appInst.unixPrompt());
                if (cacheFile.exists() && cacheFile.isFile()) {
                    remoteCksumAndSize = ssh.getCheckSumAndSize(logPath + "/" + logName);
                    localCksumAndSize = localLog.getCheckSumAndSize(filePathString + ".txt");
                    if (remoteCksumAndSize[0] != localCksumAndSize[0] || remoteCksumAndSize[1] != localCksumAndSize[1]) {
                        cacheFile.delete();
                        ssh.getRemoteFile(logPath + "/" + logName, filePathString + ".txt");
                    }
                }
                else {
                    cacheDir = new File(logCache + "/" + appInst.appType().name() + "/" + appInst.name() + logPath);
                    cacheDir.mkdirs();
                    ssh.getRemoteFile(logPath + "/" + logName, filePathString + ".txt");
                }
                if (cacheFile.length() > 2097152) {
                    List splitFileNames = fileSplit(filePathString, logName);
                    Iterator splitIt = splitFileNames.iterator();
                    while (splitIt.hasNext()) {
                        resp.addSplitLogFileURL(logUrl + "/" + appInst.appType().name() + "/" + appInst.name() + logPath + "/" + splitIt.next());
                    }
                }
                resp.setLogFileURL(logUrl + "/" + appInst.appType().name() + "/" + appInst.name() + logPath + "/" + logName + ".txt");
                return resp;
            }
            else {
                filePathString = logCache + "/" + appInst.appType().name() + "/" + appInst.name() + logPath + "/" + logName;
                File cacheFile = new File(filePathString + ".txt");
                FtpLog ftpLog = new FtpLog(appInst.host().hostname(), appInst.logfileUsername(), appInst.logfilePassword());
                if (cacheFile.exists() && cacheFile.isFile()) {
                    TelnetLog telnet = new TelnetLog(appInst.host().hostname(),
                        appInst.logfileUsername(),
                        appInst.logfilePassword(),
                        appInst.unixPrompt());
                    remoteCksumAndSize = telnet.getCheckSumAndSize(logPath + "/" + logName);
                    localCksumAndSize = localLog.getCheckSumAndSize(filePathString + ".txt");
                    if (remoteCksumAndSize[0] != localCksumAndSize[0] || remoteCksumAndSize[1] != localCksumAndSize[1]) {
                        cacheFile.delete();
                        ftpLog.getRemoteFile(logPath + "/" + logName, filePathString + ".txt");
                    }
                }
                else {
                    cacheDir = new File(logCache + "/" + appInst.appType().name() + "/" + appInst.name() + logPath);
                    cacheDir.mkdirs();
                    ftpLog.getRemoteFile(logPath + "/" + logName, filePathString + ".txt");
                }
                if (cacheFile.length() > 2097152) {
                    List splitFileNames = fileSplit(filePathString, logName);
                    Iterator splitIt = splitFileNames.iterator();
                    while (splitIt.hasNext()) {
                        resp.addSplitLogFileURL(logUrl + "/" + appInst.appType().name() + "/" + appInst.name() + logPath + "/" + splitIt.next());
                    }
                }
                resp.setLogFileURL(logUrl + "/" + appInst.appType().name() + "/" + appInst.name() + logPath + "/" + logName + ".txt");
                return resp;
            }
        }
    }

    public GetLogURLResp getLogURL(String appTypeName, String appInstanceName, String logPath, String logName) throws LogManagerException {
        GetLogURLResp resp = new GetLogURLResp();
        AppInstanceManager appInstMgr = AppInstanceManager.instance();
        AppInstance[] appInst = appInstMgr.appInstances();
        for (int i = 0; i < appInst.length; i++) {
            if (appTypeName.equals(appInst[i].appType().name()) & appInstanceName.equals(appInst[i].name())) {
                mLogger.debug("Found AppInstance: " + appInstanceName + " AppType: " + appTypeName);
                ObjectType ot = new ObjectType();
                ot.setAppInstanceType(appInst[i].appTypeId().getManagedObjectType().intValue());
                resp = getLogURL(appInst[i].appTypeId().getManagedObjectKey(), ot, logPath, logName);
                return resp;
            }
        }
        mLogger.error("Application Type Name: " + appTypeName + " Application Instance Name: " + appInstanceName + " Not Found");
        throw new LogManagerException("Application Type Name: " + appTypeName + " Application Instance Name: " + appInstanceName + " Not Found");
    }

    public GetLogURLResp getLogURLAppViewFDN(String[] appViewElementNames, String logPath, String logName) throws LogManagerException {
        GetLogURLResp resp = new GetLogURLResp();
        AppViewManager appViewMgr = null;
        try
        {
            appViewMgr = AppViewManager.instance();
        }
        catch (Exception ex)
        {
            mLogger.error("Exception getting access to AppViewManager " + ex);
            throw new LogManagerException("Exception getting access to AppViewManager " + ex);
        }
        NodePath np = new NodePath(appViewElementNames);
        Node node = appViewMgr.find(np);
        if (node != null) {
            if (node instanceof AppInstanceNode) {
                mLogger.debug("Found id of FDN");
                ObjectType ot = new ObjectType();
                ot.setAppInstanceType(node.id().getManagedObjectType().intValue());
                resp = getLogURL(node.id().getManagedObjectKey(), ot, logPath, logName);
                return resp;
            }
            else {
                mLogger.error("The HostView FDN: " + np.toString() + " Not Found or a instance of AppInstanceNode");
                throw new LogManagerException("The AppView FDN: " + np.toString() + " Not Found or a instance of AppInstanceNode");
            }
        }
        else {
            mLogger.error("The HostView FDN: " + np.toString() + " Not Found");
            throw new LogManagerException("The AppView FDN: " + np.toString() + " Not Found");
        }

    }

    public GetLogURLResp getLogURLPhyViewFDN(String[] phyViewElementNames, String logPath, String logName) throws LogManagerException {
        GetLogURLResp resp = new GetLogURLResp();
        HostViewManager hostViewMgr = null;
        try
        {
            hostViewMgr = HostViewManager.instance();
        }
        catch (Exception ex)
        {
            mLogger.error("Exception getting access to HostViewManager " + ex);
            throw new LogManagerException("Exception getting access to HostViewManager " + ex);
        }
        NodePath np = new NodePath(phyViewElementNames);
        Node node = hostViewMgr.find(np);
        if (node != null) {
            if (node instanceof AppInstanceNode) {
                mLogger.debug("Found id of FDN");
                ObjectType ot = new ObjectType();
                ot.setAppInstanceType(node.id().getManagedObjectType().intValue());
                resp = getLogURL(node.id().getManagedObjectKey(), ot, logPath, logName);
                return resp;
            }
            else {
                mLogger.error("The HostView FDN: " + np.toString() + " Not Found or a instance of AppInstanceNode");
                throw new LogManagerException("The HostView FDN: " + np.toString() + " Not Found or a instance of AppInstanceNode");
            }
        }
        else {
            mLogger.error("The HostView FDN: " + np.toString() + " Not Found");
            throw new LogManagerException("The HostView FDN: " + np.toString() + " Not Found");
        }

    }

   private List fileSplit(String filePathString, String fileNameString) {
       // Split file "filePathString + ".txt"" into directory filePathString
       FileOutputStream fos;
       BufferedOutputStream bos;
       List splitFileNames = new LinkedList();

       try {
           File dirFile = new File(filePathString);
           if (dirFile.exists() && dirFile.isDirectory()) {
               File[] dirList = dirFile.listFiles();
               for (int i = 0; i < dirList.length; i++) {
                   String delFileName = dirList[i].getAbsolutePath();
                   if (dirList[i].delete()) {
                       mLogger.debug("Deleted file: " + delFileName);
                   }
                   else {
                       mLogger.debug("Failed to delete file: " + delFileName);
                   }
               }
           }
           else {
               mLogger.debug("Making Directory: " + filePathString);
               dirFile.mkdirs();
           }

           FileInputStream fis = new FileInputStream(filePathString + ".txt");
           BufferedInputStream bis = new BufferedInputStream(fis);
           int splitNo = 1;
           splitFileNames.add(fileNameString + ".split" + splitNo + ".txt");
           fos = new FileOutputStream(filePathString + File.pathSeparator + fileNameString + ".split" + splitNo + ".txt");
           bos = new BufferedOutputStream(fos);
           splitNo++;
           int byteCount = 1;
           int b;
           while ( (b = bis.read()) != -1) {
               if (byteCount++ > 1048576) {
                   bos.flush();
                   bos.close();
                   splitFileNames.add(fileNameString + ".split" + splitNo + ".txt");
                   fos = new FileOutputStream(filePathString + File.pathSeparator + fileNameString + ".split" + splitNo + ".txt");
                   bos = new BufferedOutputStream(fos);
                   splitNo++;
                   byteCount = 1;
               }
               bos.write(b);
           }
           bis.close();
           bos.close();
       }
       catch (IOException e) {
           mLogger.error(e);
       }
       return splitFileNames;
   }

   private List fileSplitWithDir(String filePathString, String fileSplitDir, String fileNameString) {
       // Split file "filePathString" into directory fileSplitDir - File to split fileNameString
       FileOutputStream fos;
       BufferedOutputStream bos;
       List splitFileNames = new LinkedList();

       try {
           File dirFile = new File(fileSplitDir);
           if (dirFile.exists() && dirFile.isDirectory()) {
               File[] dirList = dirFile.listFiles();
               for (int i = 0; i < dirList.length; i++) {
                   String delFileName = dirList[i].getAbsolutePath();
                   if (dirList[i].delete()) {
                       mLogger.debug("Deleted file: " + delFileName);
                   }
                   else {
                       mLogger.debug("Failed to delete file: " + delFileName);
                   }
               }
           }
           else {
               mLogger.debug("Making Directory: " + fileSplitDir);
               dirFile.mkdirs();
           }

           FileInputStream fis = new FileInputStream(filePathString);
           BufferedInputStream bis = new BufferedInputStream(fis);
           int splitNo = 1;
           splitFileNames.add(fileNameString + ".split" + splitNo + ".txt");
           fos = new FileOutputStream(fileSplitDir + File.pathSeparator + fileNameString + ".split" + splitNo + ".txt");
           bos = new BufferedOutputStream(fos);
           splitNo++;
           int byteCount = 1;
           int b;
           while ( (b = bis.read()) != -1) {
               if (byteCount++ > 1048576) {
                   bos.flush();
                   bos.close();
                   splitFileNames.add(fileNameString + ".split" + splitNo + ".txt");
                   fos = new FileOutputStream(fileSplitDir + File.pathSeparator + fileNameString + ".split" + splitNo + ".txt");
                   bos = new BufferedOutputStream(fos);
                   splitNo++;
                   byteCount = 1;
               }
               bos.write(b);
           }
           bis.close();
           bos.close();
       }
       catch (IOException e) {
           mLogger.error(e);
       }
       return splitFileNames;
   }

   private String getLocalHostName() {
       String shortName = null;
       try {
           String localHost = InetAddress.getLocalHost().getHostName();
           StringTokenizer st = new StringTokenizer(localHost, ".");
           if (st.hasMoreTokens())
           {
               shortName = st.nextToken();
           }
           else
           {
               shortName = localHost;
           }
       }
       catch (UnknownHostException uh) {
           mLogger.error("unknown local host");
       }
       return shortName;
   }

   private String getShortHostName(String hostname) {
       String shortName = null;
       StringTokenizer st = new StringTokenizer(hostname, ".");
       if (st.hasMoreTokens())
       {
           shortName = st.nextToken();
       }
       else
       {
           shortName = hostname;
       }
       return shortName;
   }

   private List getListFromArray(String[] array) {
       List list = new LinkedList();
       for (int i = 0; i < array.length; i++) {
           list.add(array[i]);
       }
       return list;
   }

}

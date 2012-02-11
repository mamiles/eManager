package com.cisco.eManager.eManager.log;

import com.cisco.eManager.common.log.*;
import com.cisco.eManager.common.util.AccessType;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public interface LogManagerInterface
{

    public String handleRequest(String xmlStream, String userId, AccessType access);

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectories(GetLogFilesFromDirectories parm)
        throws LogManagerException;

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectories(long objectKey, ObjectType objType,
        String searchFilter)
        throws LogManagerException;

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectories(String appTypeName,
        String appInstanceName, String searchFilter)
        throws LogManagerException;

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectoriesAppViewFDN(String[] appViewElementNames,
        String searchFilter)
        throws LogManagerException;

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectoriesPhyViewFDN(String[]
        physicalViewElementNames, String searchFilter)
        throws LogManagerException;

    public GetLogURLResp getLogURL(GetLogURL getLogURL)
        throws LogManagerException;

    public GetLogURLResp getLogURL(long objectKey, ObjectType objType, String logPath, String logName)
        throws LogManagerException;

    public GetLogURLResp getLogURL(String appTypeName, String appInstanceName, String logPath, String logName)
        throws LogManagerException;

    public GetLogURLResp getLogURLAppViewFDN(String[] appViewElementNames, String logPath, String logName)
        throws LogManagerException;

    public GetLogURLResp getLogURLPhyViewFDN(String[] phyViewElementNames, String logPath, String logName)
        throws LogManagerException;

}

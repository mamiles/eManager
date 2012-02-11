package com.cisco.eManager.soap.log;

/*
import java.util.Date;
import java.util.Collection;

import com.cisco.eManager.common.log.*;

import com.cisco.eManager.common.audit.AuditDomain;
import com.cisco.eManager.common.audit.AuditAction;
import com.cisco.eManager.common.audit.AuditLogEntry;
import com.cisco.eManager.common.audit.AuditLogSearchCriteria;
import com.cisco.eManager.common.audit.AuditLogDeletionCriteria;
import com.cisco.eManager.common.audit.EmanagerAuditException;

import com.cisco.eManager.common.database.EmanagerDatabaseException;
*/

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
    public String xmlMessage(String sessionID, String xmlString);

    /*
    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectories(GetLogFilesFromDirectories parm)
        throws EmanagerLogException;

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectories(long objectKey, ObjectType objType,
        String searchFilter)
        throws EmanagerLogException;

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectories(String appTypeName,
        String appInstanceName, String searchFilter)
        throws EmanagerLogException;

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectoriesAppViewFDN(String[] appViewElementNames,
        String searchFilter)
        throws EmanagerLogException;

    public GetLogFilesFromDirectoriesResp getLogFilesFromDirectoriesPhyViewFDN(String[]
        physicalViewElementNames, String searchFilter)
        throws EmanagerLogException;

    public GetLogURLResp getLogURL(GetLogURL getLogURL)
        throws EmanagerLogException;

    public GetLogURLResp getLogURL(long objectKey, ObjectType objType, String logPath, String logName)
        throws EmanagerLogException;

    public GetLogURLResp getLogURL(String appTypeName, String appInstanceName, String logPath, String logName)
        throws EmanagerLogException;

    public GetLogURLResp getLogURLAppViewFDN(String[] appViewElementNames, String logPath, String logName)
        throws EmanagerLogException;

    public GetLogURLResp getLogURLPhyViewFDN(String[] phyViewElementNames, String logPath, String logName)
        throws EmanagerLogException;
*/

}

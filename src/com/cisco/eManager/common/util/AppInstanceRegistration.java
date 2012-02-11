package com.cisco.eManager.common.util;

import java.util.*;
import com.cisco.eManager.common.inventory.*;

public class AppInstanceRegistration
{
    // field names
    private final String HOST_DNS = "HostName";
    private final String APP_NAME = "AppType";
    private final String APP_INSTANCE_ID = "AppInstance";
    private final String VERSION = "AppVersion";
    private final String MGMT_POLICY_PATHS = "AppMgmtPolicies";
    private final String MGMT_POLICY_PATH = "AppMgmtPolicy";
    private final String APP_INSTANCE_PROPERTY_FILE = "ProcessPropertyFile";
    private final String LOGGING = "Logging";
    private final String LOGFILE_DIRECTORIES = "LogDirectories";
    private final String LOGFILE_DIRECTORY = "LogDirectory";
    private final String TRANSPORT = "Transport";
    private final String USERNAME = "UserId";
    private final String PASSWORD = "Password";
    private final String UNIX_PROMPT = "UnixPrompt";

    // properties
    private String m_hostDns = null;
    private String m_appName = null;
    private String m_appInstanceId = null;
    private String m_version = null;
    private Vector m_mgmtPolicyPaths = null;
    private String m_appInstancePropertyFile = null;
    private Vector m_logfileDirectories = null;
    private Transport m_transport = null;
    private String m_username = null;
    private String m_password = null;
    private String m_unixPrompt = null;

    public AppInstanceRegistration()
    {
        // ctor
    }

    public void hostDns(String hostDns)
    {
        m_hostDns = hostDns.trim();
    }

    public String hostDns()
    {
        return m_hostDns;
    }

    public void appName(String appName)
    {
        m_appName = appName.trim();
    }

    public String appName()
    {
        return m_appName;
    }

    public void appInstanceId(String appInstanceId)
    {
        m_appInstanceId = appInstanceId.trim();
    }

    public String appInstanceId()
    {
        return m_appInstanceId;
    }

    public void version(String version)
    {
        m_version = version.trim();
    }

    public String version()
    {
        return m_version;
    }

    public void addMgmtPolicyPath(String mgmtPolicyPath)
    {
        m_mgmtPolicyPaths.add(mgmtPolicyPath.trim());
    }

    public String[] mgmtPolicyPaths()
    {
        return (String[])m_mgmtPolicyPaths.toArray();
    }

    public void appInstancePropertyFile(String appInstancePropertyFile)
    {
        m_appInstancePropertyFile = appInstancePropertyFile.trim();
    }

    public String appInstancePropertyFile()
    {
        return m_appInstancePropertyFile;
    }

    public void addLogfileDirectory(String logfileDirectory)
    {
        m_logfileDirectories.add(logfileDirectory.trim());
    }

    public String[] logfileDirectories()
    {
        return (String[])m_logfileDirectories.toArray();
    }

    public void transport(Transport transport)
    {
        m_transport = transport;
    }

    public Transport transport()
    {
        return m_transport;
    }

    public void username(String username)
    {
        m_username = username.trim();
    }

    public String username()
    {
        return m_username;
    }

    public void password(String password)
    {
        m_password = password.trim();
    }

    public String password()
    {
        return m_password;
    }

    public void unixPrompt(String unixPrompt)
    {
        m_unixPrompt = unixPrompt.trim();
    }

    public String unixPrompt()
    {
        return m_unixPrompt;
    }

    public String toXml()
    {
        // generate an xml stream describing this object
        StringBuffer xmlBuf = new StringBuffer();
        Iterator iter = null;

        // hostDns
        xmlBuf.append(XmlHelper.toXml(HOST_DNS, m_hostDns));

        // appName
        xmlBuf.append(XmlHelper.toXml(APP_NAME, m_appName));

        // appInstanceId
        xmlBuf.append(XmlHelper.toXml(APP_INSTANCE_ID, m_appInstanceId));

        // version
        xmlBuf.append(XmlHelper.toXml(VERSION, m_version));

        // mgmtPolicyPaths
        xmlBuf.append(XmlHelper.startTag(MGMT_POLICY_PATHS));
        iter = m_mgmtPolicyPaths.iterator();
        while (iter.hasNext())
        {
            xmlBuf.append(XmlHelper.toXml(MGMT_POLICY_PATH,
                                          (String)iter.next()));
        }
        xmlBuf.append(XmlHelper.endTag(MGMT_POLICY_PATHS));

        // appInstancePropertyFile
        xmlBuf.append(XmlHelper.toXml(APP_INSTANCE_PROPERTY_FILE,
                                      m_appInstancePropertyFile));

        // logging
        xmlBuf.append(XmlHelper.startTag(LOGGING));

        iter = m_logfileDirectories.iterator();
        while (iter.hasNext())
        {
            xmlBuf.append(XmlHelper.toXml(LOGFILE_DIRECTORY,
                                          (String)iter.next()));
        }

        // transport
        xmlBuf.append(XmlHelper.toXml(TRANSPORT, m_transport.toString()));

        // username
        xmlBuf.append(XmlHelper.toXml(USERNAME, m_username));

        // password
        xmlBuf.append(XmlHelper.toXml(PASSWORD, m_password));

        // unix prompt
        xmlBuf.append(XmlHelper.toXml(UNIX_PROMPT, m_unixPrompt));

        xmlBuf.append(XmlHelper.endTag(LOGGING));

        return xmlBuf.toString();
    }

    public void fromXml(String xmlString)
    {
        // populate this object from an xml string
        StringBuffer xmlStringBuffer = new StringBuffer(xmlString);

        // hostDns
        m_hostDns = XmlHelper.subString(xmlStringBuffer, HOST_DNS);

        // appName
        m_appName = XmlHelper.subString(xmlStringBuffer, APP_NAME);

        // appInstanceId
        m_appInstanceId = XmlHelper.subString(xmlStringBuffer, APP_INSTANCE_ID);

        // version
        m_version = XmlHelper.subString(xmlStringBuffer, VERSION);

        // mgmtPolicyPaths
        StringBuffer mgmtPolicyPaths =
            new StringBuffer(XmlHelper.subString(xmlStringBuffer,
                                                 MGMT_POLICY_PATHS));
        String mgmtPolicyPath = null;
        while (mgmtPolicyPaths.length() > 0)
        {
            mgmtPolicyPath = XmlHelper.subString(mgmtPolicyPaths,
                                                 MGMT_POLICY_PATH);
            if (mgmtPolicyPath != null)
            {
                if ( mgmtPolicyPath.length() > 0 )
                {
                    m_mgmtPolicyPaths.add(mgmtPolicyPath);
                }
            }
            else
            {
                break;
            }
        }

        // appGroupNames
        m_appInstancePropertyFile =
            XmlHelper.subString(xmlStringBuffer, APP_INSTANCE_PROPERTY_FILE);

        // logging
        StringBuffer logging =
            new StringBuffer(XmlHelper.subString(xmlStringBuffer, LOGGING));
        if (logging.length() > 0)
        {
            String logfileDirectory = null;
            while ((logfileDirectory =
                    XmlHelper.subString(logging, LOGFILE_DIRECTORY)) != null)
            {
                if (logfileDirectory != null)
                {
                    if ( logfileDirectory.length() > 0 )
                    {
                        m_logfileDirectories.add(logfileDirectory);
                    }
                }
                else
                {
                    break;
                }
            }

            // transport
            m_transport.fromString(
                XmlHelper.subString(logging, TRANSPORT));

            // username
            m_username = XmlHelper.subString(logging, USERNAME);

            // password
            m_password = XmlHelper.subString(logging, PASSWORD);

            // unix prompt
            m_unixPrompt = XmlHelper.subString(logging, UNIX_PROMPT);
        }
    }
}
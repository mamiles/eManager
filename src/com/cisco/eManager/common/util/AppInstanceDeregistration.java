package com.cisco.eManager.common.util;

import java.util.*;
import com.cisco.eManager.common.inventory.*;

public class AppInstanceDeregistration
{
    // xml field names
    private final String APP_TYPE = "AppType";
    private final String APP_INSTANCE = "AppInstance";
    private final String VERSION = "AppVersion";
    private final String HOST = "HostName";

    // properties
    private String m_appType = null;
    private String m_appInstance = null;
    private String m_version = null;
    private String m_host = null;

    public AppInstanceDeregistration()
    {
    }

    public void appType(String appType)
    {
        m_appType = appType.trim();
    }

    public String appType()
    {
        return m_appType;
    }

    public void appInstance(String appInstance)
    {
        m_appInstance = appInstance.trim();
    }

    public String appInstance()
    {
        return m_appInstance;
    }

    public void version(String version)
    {
        m_version = version.trim();
    }

    public String version()
    {
        return m_version;
    }

    public void host(String host)
    {
        m_host = host.trim();
    }

    public String host()
    {
        return m_host;
    }

    public String toXml()
    {
        // generate an xml stream describing this object
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append(XmlHelper.toXml(APP_TYPE, m_appType));
        xmlBuf.append(XmlHelper.toXml(APP_INSTANCE, m_appInstance));
        xmlBuf.append(XmlHelper.toXml(VERSION, m_version));
        xmlBuf.append(XmlHelper.toXml(HOST, m_host));
        return xmlBuf.toString();
    }

    public void fromXml(String xmlString)
    {
        StringBuffer xmlStringBuffer = new StringBuffer(xmlString);
        m_appType = XmlHelper.subString(xmlStringBuffer, APP_TYPE);
        m_appInstance = XmlHelper.subString(xmlStringBuffer, APP_INSTANCE);
        m_version = XmlHelper.subString(xmlStringBuffer, VERSION);
        m_host = XmlHelper.subString(xmlStringBuffer, HOST);
    }
}

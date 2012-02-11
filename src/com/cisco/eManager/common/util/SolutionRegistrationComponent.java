package com.cisco.eManager.common.util;

import java.util.*;
import com.cisco.eManager.common.inventory.*;

public class SolutionRegistrationComponent
{
    // xml field names
    private final String APP_TYPE = "AppType";
    private final String APP_INSTANCE = "AppInstance";
    private final String VERSION = "Version";

    private String m_appType = null;
    private String m_appInstance = null;
    private String m_version = null;

    public SolutionRegistrationComponent()
    {

    }

    public String appType()
    {
        return m_appType;
    }

    public void appType(String appType)
    {
        m_appType = appType.trim();
    }

    public String appInstance()
    {
        return m_appInstance;
    }

    public void appInstance(String appInstance)
    {
        m_appInstance = appInstance.trim();
    }

    public String version()
    {
        return m_version;
    }

    public void version(String version)
    {
        m_version = version.trim();
    }

    public String toXml()
    {
        // generate an xml stream describing this object
        StringBuffer xmlBuf = new StringBuffer();

        xmlBuf.append(XmlHelper.toXml(APP_TYPE, m_appType));
        xmlBuf.append(XmlHelper.toXml(APP_INSTANCE, m_appInstance));
        xmlBuf.append(XmlHelper.toXml(VERSION, m_version));

        return xmlBuf.toString();
    }

    public void fromXml(String xmlString)
    {
        StringBuffer xmlStringBuffer = new StringBuffer(xmlString);
        m_appType = XmlHelper.subString(xmlStringBuffer, APP_TYPE);
        m_appInstance = XmlHelper.subString(xmlStringBuffer, APP_INSTANCE);
        m_version = XmlHelper.subString(xmlStringBuffer, VERSION);
    }
}
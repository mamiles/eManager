package com.cisco.eManager.eManager.processSequencer.processSequencer;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public class SolutionRegistrationComponent
{
    // xml field names
    private final String APP_TYPE = "AppType";
    private final String APP_INSTANCE = "AppInstance";
    private final String VERSION = "Version";

    private String m_appType = null;
    private String m_appInstance = null;
    private String m_version = null;
    private String m_state = null;

    public SolutionRegistrationComponent()
    {

    }

    public String getAppType()
    {
        return m_appType;
    }

    public void setAppType(String appType)
    {
        m_appType = appType.trim();
    }

    public String getAppInstance()
    {
        return m_appInstance;
    }

    public void setAppInstance(String appInstance)
    {
        m_appInstance = appInstance.trim();
    }

    public String getVersion()
    {
        return m_version;
    }

    public void setVersion(String version)
    {
        m_version = version.trim();
    }

    public String getState()
    {
        return m_state;
    }

    public void setState(String state)
    {
        m_state = state;
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

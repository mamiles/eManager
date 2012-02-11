package com.cisco.eManager.common.util;

import java.util.*;
import com.cisco.eManager.common.inventory.*;

public class AppInstanceRegistrationMsg extends RegistrationMsg
{
    // subject this message should be posted under
    public final static String SUBJECT =
        "cisco.mgmt.emanager.inventory.appRegistration";

    // xml field names
    private final String APP_INSTANCE_REGISTRATION = "Registration";

    // properties
    private AppInstanceRegistration m_appInstanceRegistration = null;

    public AppInstanceRegistrationMsg()
    {
        // ctor
    }

    public void appInstanceRegistration(AppInstanceRegistration air)
    {
        m_appInstanceRegistration = air;
    }

    public AppInstanceRegistration appInstanceRegistration()
    {
        return m_appInstanceRegistration;
    }

    public String toXml()
    {
        // generate an xml stream describing this object
        StringBuffer xmlBuf = new StringBuffer();

        xmlBuf.append(XmlHelper.toXml(APP_INSTANCE_REGISTRATION,
                                      m_appInstanceRegistration.toXml()));
        return xmlBuf.toString();
    }

    public void fromXml(String xmlString)
    {
        StringBuffer xmlStringBuffer = new StringBuffer(xmlString);
        String appInstanceRegistrationXml =
            XmlHelper.subString(xmlStringBuffer, APP_INSTANCE_REGISTRATION);

        if (appInstanceRegistrationXml != null)
        {
            if (appInstanceRegistrationXml.length() > 0)
            {
                m_appInstanceRegistration = new AppInstanceRegistration();
                m_appInstanceRegistration.fromXml(appInstanceRegistrationXml);
            }
        }
    }
}
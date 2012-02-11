package com.cisco.eManager.common.util;

import java.util.*;
import com.cisco.eManager.common.inventory.*;

public class AppInstanceDeregistrationMsg extends RegistrationMsg
{
    // subject this message should be posted under
    public final static String SUBJECT =
        "cisco.mgmt.emanager.inventory.appDeregistration";

    // xml field names
    private final String APP_INSTANCE_DEREGISTRATION = "DeRegistration";

    // properties
    private AppInstanceDeregistration m_appInstanceDeregistration = null;

    public AppInstanceDeregistrationMsg()
    {
        // ctor
    }

    public void appInstanceDeregistration(AppInstanceDeregistration adr)
    {
        m_appInstanceDeregistration = adr;
    }

    public AppInstanceDeregistration appInstanceDeregistration()
    {
        return m_appInstanceDeregistration;
    }

    public String toXml()
    {
        // generate an xml stream describing this object
        StringBuffer xmlBuf = new StringBuffer();

        xmlBuf.append(XmlHelper.toXml(APP_INSTANCE_DEREGISTRATION,
                                      m_appInstanceDeregistration.toXml()));
        return xmlBuf.toString();
    }

    public void fromXml(String xmlString)
    {
        StringBuffer xmlStringBuffer = new StringBuffer(xmlString);
        String appInstanceDeregistrationXml =
            XmlHelper.subString(xmlStringBuffer, APP_INSTANCE_DEREGISTRATION);

        if (appInstanceDeregistrationXml != null)
        {
            if (appInstanceDeregistrationXml.length() > 0)
            {
                m_appInstanceDeregistration = new AppInstanceDeregistration();
                m_appInstanceDeregistration.fromXml(appInstanceDeregistrationXml);
            }
        }
    }
}

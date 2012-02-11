package com.cisco.eManager.common.util;

import java.util.*;
import com.cisco.eManager.common.inventory.*;

public class SolutionRegistrationMsg extends RegistrationMsg
{
    // subject this message should be posted under
    public final static String SUBJECT =
        "cisco.mgmt.emanager.inventory.solutionReregistration";

    // xml field names
    private final String SOLUTION_REGISTRATION = "SolutionRegistration";

    // properties
    private SolutionRegistration m_solutionRegistration = null;

    public SolutionRegistrationMsg()
    {
        // ctor
    }

    public void solutionRegistration(SolutionRegistration sr)
    {
        m_solutionRegistration = sr;
    }

    public SolutionRegistration solutionRegistration()
    {
        return m_solutionRegistration;
    }

    public String toXml()
    {
        // generate an xml stream describing this object
        StringBuffer xmlBuf = new StringBuffer();

        xmlBuf.append(XmlHelper.toXml(SOLUTION_REGISTRATION,
                                      m_solutionRegistration.toXml()));
        return xmlBuf.toString();
    }

    public void fromXml(String xmlString)
    {
        StringBuffer xmlStringBuffer = new StringBuffer(xmlString);
        String solutionRegistrationXml =
            XmlHelper.subString(xmlStringBuffer, SOLUTION_REGISTRATION);

        if (solutionRegistrationXml != null)
        {
            if (solutionRegistrationXml.length() > 0)
            {
                m_solutionRegistration = new SolutionRegistration();
                m_solutionRegistration.fromXml(solutionRegistrationXml);
            }
        }
    }
}

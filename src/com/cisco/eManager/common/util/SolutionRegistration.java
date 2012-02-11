package com.cisco.eManager.common.util;

import java.util.*;
import com.cisco.eManager.common.inventory.*;

public class SolutionRegistration
{
    // xml field names
    private final String SOLUTION_NAME = "SolutionName";
    private final String SOLUTION_COMPONENTS = "Components";
    private final String SOLUTION_COMPONENT = "Component";

    // properties
    private String m_solutionName = null;
    private Vector m_solutionComponents = null;

    public SolutionRegistration()
    {
        m_solutionComponents = new Vector();
    }

    public void solutionName(String solutionName)
    {
        m_solutionName = solutionName.trim();
    }

    public String solutionName()
    {
        return m_solutionName;
    }

    public void
        addSolutionComponent(SolutionRegistrationComponent solutionComponent)
    {
        m_solutionComponents.add(solutionComponent);
    }

    public SolutionRegistrationComponent[] solutionComponents()
    {
        return (SolutionRegistrationComponent[])m_solutionComponents.toArray();
    }

    public String toXml()
    {
        // generate an xml stream describing this object
        StringBuffer xmlBuf = new StringBuffer();
        Iterator iter = null;

        xmlBuf.append(XmlHelper.toXml(SOLUTION_NAME, m_solutionName));

        xmlBuf.append(XmlHelper.startTag(SOLUTION_COMPONENTS));
        iter = m_solutionComponents.iterator();
        while (iter.hasNext())
        {
            xmlBuf.append(XmlHelper.toXml(
                SOLUTION_COMPONENT,
                ((SolutionRegistrationComponent)iter.next()).toXml()));
        }
        xmlBuf.append(XmlHelper.endTag(SOLUTION_COMPONENTS));

        return xmlBuf.toString();
    }

    public void fromXml(String xmlString)
    {
        StringBuffer xmlStringBuffer = new StringBuffer(xmlString);

        // solutionName
        m_solutionName = XmlHelper.subString(xmlStringBuffer, SOLUTION_NAME);

        String componentsString =
            XmlHelper.subString(xmlStringBuffer, SOLUTION_COMPONENTS);
        fromXmlComponents(componentsString);
    }

    private void fromXmlComponents(String xmlString)
    {
        StringBuffer xmlStringBuffer = new StringBuffer(xmlString);
        SolutionRegistrationComponent sc = null;
        String componentString =
            XmlHelper.subString(xmlStringBuffer, SOLUTION_COMPONENT);

        while ( ( componentString != null ) && ( componentString.length() > 0) )
        {
            sc = new SolutionRegistrationComponent();
            sc.fromXml(componentString);
            m_solutionComponents.add(sc);

            componentString =
                XmlHelper.subString(xmlStringBuffer, SOLUTION_COMPONENT);
        }
    }
}

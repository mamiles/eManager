package com.cisco.eManager.eManager.processSequencer.processSequencer;

import java.util.*;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public class XmlHelper
{
    public static String startTag(String tagData)
    {
        return "<" + tagData + ">";
    }

    public static String endTag(String tagData)
    {
        return "</" + tagData + ">";
    }

    public static String subString(StringBuffer sourceString, String fieldName)
    {
        String subString = null;
        String startTag = startTag(fieldName);
        String endTag = endTag(fieldName);

        int indexOfStartTag = -1;
        int lengthOfStartTag = endTag.length();
        int indexOfEndTag = -1;
        int lengthOfEndTag = endTag.length();
        int indexOfSubString = -1;

        indexOfStartTag = sourceString.indexOf(startTag);
        if (indexOfStartTag < 0)
        {
            return null;
        }

        indexOfSubString = indexOfStartTag + lengthOfStartTag -1;

        indexOfEndTag = sourceString.indexOf(endTag);
        if (indexOfEndTag < indexOfSubString)
        {
            return null;
        }

        if (indexOfEndTag == indexOfSubString)
        {
            return "";
        }

        subString = sourceString.substring(indexOfSubString, indexOfEndTag);
        sourceString.delete(indexOfStartTag, indexOfEndTag + endTag.length());

        return subString.trim();

    }

    public static String toXml(String fieldName, String fieldValue)
    {
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append(startTag(fieldName));
        xmlBuf.append(fieldValue);
        xmlBuf.append(endTag(fieldName));
        return xmlBuf.toString();
    }
}

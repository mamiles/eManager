package com.cisco.eManager.eManager.process;
import com.cisco.eManager.common.util.*;
import java.io.*;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public class TibDataElement
    implements Serializable
{
    public String name;
    public Object value;

    public TibDataElement(String sName, Object sValue)
    {
        name = sName;
        value = sValue;

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Object getValue()
    {
        return value;
    }
    public String getStringValue() {
        return value.toString();
    }

    public void setValue(Object value)
    {
        this.value = value;
    }
    public String toXml()
    {
        // generate an xml stream describing this object
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append(XmlHelper.toXml(this.name, value.toString()));
        return xmlBuf.toString();
    }

}

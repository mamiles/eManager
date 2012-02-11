package com.cisco.eManager.eManager.log;

import java.util.StringTokenizer;
import java.lang.Integer;
import java.io.File;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public class FileNameAttr
{
    private String attributes;
    private int numInodes;
    private String ownerName;
    private String groupName;
    private int size;
    private String changeDate;
    private String fileName;

    /**
     *  Constructor that creates a default FileNameAttr object
     **/
    public FileNameAttr()
    {
        this("", 0, "", "", 0, "", "");
    }

    /**
     *  Constructor to enter all the values FileNameAttr object
     **/
    public FileNameAttr(String attributes, int numInodes, String ownerName, String groupName, int size,
                        String changeDate, String fileName)
    {
        setAttributes(attributes);
        setNumInodes(numInodes);
        setOwnerName(ownerName);
        setGroupName(groupName);
        setSize(size);
        setChangeDate(changeDate);
        setFileName(fileName);
    }

    public void setAttributes(String attributes)
    {
        this.attributes = attributes;
    }

    public String getAttributes()
    {
        return attributes;
    }

    public void setNumInodes(String numInodes)
    {
        this.numInodes = Integer.parseInt(numInodes);
    }

    public void setNumInodes(int numInodes)
    {
        this.numInodes = numInodes;
    }

    public int getNumInodes()
    {
        return numInodes;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setSize(String size)
    {
        this.size = Integer.parseInt(size);
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public int getSize()
    {
        return size;
    }

    public String getSizeString()
    {
        Integer fileSize = new Integer(size);
        return fileSize.toString();

    }

    public void setChangeDate(String changeDate)
    {
        this.changeDate = changeDate;
    }

    public String getChangeDate()
    {
        return changeDate;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getFileNamePath()
    {
        StringTokenizer st = new StringTokenizer(fileName, File.pathSeparator);
        String pathName = "";
        int j = st.countTokens() - 1;
        for (int i = 0; i < j; i++)
        {
            pathName = pathName + File.pathSeparator + st.nextToken();
        }
        return pathName;
    }

    public String getShortFileName()
    {
        StringTokenizer st = new StringTokenizer(fileName, File.pathSeparator);
        String pathName = "";
        int j = st.countTokens() - 1;
        for (int i = 0; i < j; i++)
        {
            pathName = pathName + File.pathSeparator + st.nextToken();
        }
        return st.nextToken();
    }

    public String toString()
    {
        return getAttributes() + " size: " + getSize() + " Date: " + getChangeDate() + " Filename: " +
            getFileName();
        // return getAttributes() + " " + getNumInodes() + " " + getOwnerName() + " " + getGroupName() + " " + getSize() + " " + getChangeDate() + " " + getFileName();
    }

}
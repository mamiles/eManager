package com.cisco.eManager.common.util;


public class AccessType implements java.io.Serializable
{
    private int m_accessType;

    private static int ACCESS_READ = 1;
    private static int ACCESS_WRITE = 2;

    public static AccessType READ = new AccessType(ACCESS_READ);
    public static AccessType WRITE = new AccessType(ACCESS_WRITE);

    private AccessType(int  accessType)
    {
        m_accessType = accessType;
    }

    public boolean isReadAccess()
    {
	    return (m_accessType == ACCESS_READ);
    }

    public boolean isWriteAccess()
    {
	    return (m_accessType == ACCESS_WRITE);
    }
}

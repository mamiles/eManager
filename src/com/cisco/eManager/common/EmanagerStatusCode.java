package com.cisco.eManager.common;

public abstract class EmanagerStatusCode implements java.io.Serializable
{
    private int statusCode;
    private String statusCodeDescription;

    // fix
    // this is for SOAP.
    public EmanagerStatusCode()
    {
    }

    public EmanagerStatusCode(int statusCode,
			       String statusCodeString)
    {
	this.statusCode = statusCode;
        this.statusCodeDescription = statusCodeString;
    }

    public int intValue()
    {
	return statusCode;
    }

    public String getStatusCodeDescription()
    {
        return statusCodeDescription;
    }

    public abstract boolean equals (Object object);
}

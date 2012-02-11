package com.cisco.eManager.common.admin;

import org.apache.log4j.*;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

public class SecurityRole
{
    private static Logger logger = Logger.getLogger(SecurityRole.class);
    
    private ManagedObjectId id;
    private String name;

    // fix 
    // SOAP workaround
    public SecurityRole()
    {
    }

    public SecurityRole (long securityRoleKey,
			 String name)
    {
	this.id = new ManagedObjectId (ManagedObjectIdType.SecurityRole,
				       securityRoleKey);
	this.name = name;
    }

    public ManagedObjectId getId()
    {
	return id;
    }

    public String getName()
    {
	return name;
    }

    public void setName (String name)
    {
	this.name = name;
    }

    public String toString()
    {
	return "securityRoleId=" + id + ";" +
	    "name=" + name;
    }
}

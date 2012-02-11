package com.cisco.eManager.common.admin;

import org.apache.log4j.*;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

public class UserAccount
{
    private static Logger logger = Logger.getLogger(UserAccount.class);
    
    private ManagedObjectId id;
    private String name;
    private String password;
    private String passwordKey;
    private ManagedObjectId securityRoleId;

    // fix 
    // SOAP workaround
    public UserAccount()
    {
    }

    public UserAccount (long userAccountKey,
			String name,
			String password,
			String passwordKey,
			ManagedObjectId securityRoleId)
    {
	this.id = new ManagedObjectId (ManagedObjectIdType.UserAccount,
				       userAccountKey);
	this.name = name;
	this.password = password;
	this.passwordKey = passwordKey;
	this.securityRoleId = securityRoleId;
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

    public String getPassword()
    {
	return password;
    }

    public void setPassword (String password)
    {
	this.password = password;
    }

    public String getPasswordKey()
    {
	return passwordKey;
    }

    public void setPasswordKey (String passwordKey)
    {
	this.passwordKey = passwordKey;
    }
    
    public ManagedObjectId getSecurityRoleId()
    {
	return securityRoleId;
    }

    public void setSecurityRoleId(ManagedObjectId securityRoleId)
    {
	this.securityRoleId = securityRoleId;
    }

    public String toString()
    {
	return "userAccountId=" + id + ";" +
	    "name=" + name + ";" +
	    "securityRoleId=" + securityRoleId;
    }
}

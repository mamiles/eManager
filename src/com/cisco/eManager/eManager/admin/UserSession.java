package com.cisco.eManager.eManager.admin;

import com.cisco.eManager.common.admin.UserAccount;
import com.cisco.eManager.common.util.AccessType;
import com.cisco.eManager.common.inventory.ManagedObjectId;

public class UserSession
{

	public static long defaultTimeout = 1800; // 30 min
	private long lastAccessTime;
	private long timeout;
	private AccessType accessType;
	private String username;
	private boolean administratorRole = false;
	private String sessionId;
	private ManagedObjectId userAccountId;


	public UserSession(UserAccount account)
	{
		userAccountId = account.getId();
		username = account.getName();
		timeout = defaultTimeout;
		accessType = translateUserRoleToAccessType(account.getSecurityRoleId());
		sessionId = CryptoHelper.genCryptoMessage(username);
		lastAccessTime = System.currentTimeMillis();
	}

	public ManagedObjectId getUserAccountId()
	{
		return userAccountId;
	}

	public void setTimeout(long value)
	{
		timeout = value;
	}

	public long getTimeout()
	{
		return timeout;
	}

	public void updateLastAccessTime()
	{
		lastAccessTime = System.currentTimeMillis();
	}

	public long getLastAccessTime()
	{
		return lastAccessTime;
	}
	public String getName()
	{
		return username;
	}
	public AccessType getAccessType()
	{
		return accessType;
	}

	public boolean isAdministratorRole()
	{
		return administratorRole;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	private AccessType translateUserRoleToAccessType(ManagedObjectId userRoleId)
	{
		AccessType accessType;

		if (userRoleId.equals(AdminGlobals.AdministratorSecurityRoleId)) {
			accessType = AccessType.WRITE;
			administratorRole = true;
		} else if (userRoleId.equals(AdminGlobals.OperatorSecurityRoleId)) {
			return AccessType.WRITE;
		}
		else {
			accessType =  AccessType.READ;
		}

		return accessType;
	}

}





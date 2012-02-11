package com.cisco.eManager.eManager.admin;

import org.apache.log4j.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Hashtable; 

import com.cisco.eManager.common.admin.UserAccount;
import com.cisco.eManager.common.admin.SecurityRole;
import com.cisco.eManager.common.admin.EmanagerAdminException;
import com.cisco.eManager.common.admin.EmanagerAdminStatusCode;

import com.cisco.eManager.common.database.EmanagerDatabaseException;
import com.cisco.eManager.eManager.database.DatabaseInterface;

public class AdminCache
{
	private static Logger logger = Logger.getLogger(AdminCache.class);

	private Hashtable userAccountTbl = new Hashtable();
	private Hashtable userSessionTbl = new Hashtable();

	private static AdminCache adminCache = null;

	public synchronized static AdminCache instance()
	{
		if (adminCache == null)
			adminCache = new AdminCache();
		return adminCache;
	}

	private AdminCache()
	{
		logger.debug("Initializing AdminCache....");
		try {
			
			logger.debug("Retrieving UserAccount from database...");
			Collection collection = DatabaseInterface.instance().retrieveUserAccounts();

			if (collection != null) {
				if (!collection.isEmpty()) {
					Iterator iter = collection.iterator(); 
					while (iter.hasNext()) {
						UserAccount account = (UserAccount)iter.next(); 
						logger.debug("userId: " + account.getName());
						userAccountTbl.put(account.getName(), account);
					}
				} else { // the database is empty - create default admin account
					logger.debug("Database is empty - create default admin account");
					UserAccount account = DatabaseInterface.instance().createUserAccount("admin","cisco123","cisco123", AdminGlobals.AdministratorSecurityRoleId);
					userAccountTbl.put(account.getName(), account);
				}
			}
		} catch (EmanagerDatabaseException e) {
			logger.error("Caught EmanagerDatabaseException during initializing AdminCache: " + e);
		} 
	}


	public synchronized void removeUserAccountFromCache(String username)
	{
		logger.debug("Remove user account: " + username + " from AdminCache");
		Object obj = userAccountTbl.remove(username);
		if (obj == null) {
			logger.debug("username: " + username + " is not in the AdminCache for removal");
		}

		return;
	}

	public synchronized void addUserAccountToCache(UserAccount account)
	{
		if (account == null) {
			logger.debug("Cannot add a null UserAccount to AdminCache");
			return;
		}

		logger.debug("Add user account: " + account.getName() + " to AdminCache");
		userAccountTbl.put(account.getName(), account);

		return;
	}

	public synchronized void updateUserAccountInCache(UserAccount account)
	{
		if (account == null) {
			logger.debug("Cannot update a null UserAccount to AdminCache");
			return;
		}

		logger.debug("Update user account: " + account.getName() + " in AdminCache");
		Object obj = userAccountTbl.put(account.getName(), account);
		if (obj == null) 
			logger.info("Try to update account: " + account.getName() + " , which is not in AdminCache..."); 

		return;
	}



	public synchronized void addUserSessionToCache(UserSession session)
	{ 
		logger.debug("Add UserSession: " + session.getName() + " to AdminCache");
		userSessionTbl.put(session.getSessionId(), session);

	}

	public synchronized void removeUserSessionFromCache(String sessionID)
	{ 
		logger.debug("Remove UserSession: " + sessionID + " from AdminCache");
		userSessionTbl.remove(sessionID);

	}
	public synchronized void updateUserSessionInCache(UserSession session)
	{ 
		logger.debug("Update UserSession: " + session.getName() + " in AdminCache");
		userSessionTbl.put(session.getSessionId(), session);
	}


	public UserSession getUserSession(String sessionID)
	{
		return (UserSession)userSessionTbl.get(sessionID);
	}

	public UserAccount getUserAccount(String username)
	{
		return (UserAccount)userAccountTbl.get(username);
	}


}
  	


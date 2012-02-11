package com.cisco.eManager.eManager.admin;

import org.apache.log4j.*;

import java.util.Collection;
import java.util.Iterator;

import com.cisco.eManager.common.admin.UserAccount;
import com.cisco.eManager.common.admin.SecurityRole;
import com.cisco.eManager.common.admin.EmanagerAdminException;
import com.cisco.eManager.common.admin.EmanagerAdminStatusCode;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

import com.cisco.eManager.common.database.EmanagerDatabaseException;

import com.cisco.eManager.eManager.database.DatabaseInterface;


public class AdminManager
{
    private static Logger logger = Logger.getLogger(AdminManager.class);

    public final static String ADMINISTRATOR = "administrator";
    public final static String OPERATOR = "operator";
    public final static String GUEST = "guest";

    private static AdminManager adminManager = null;

    /**
     * @roseuid 3F4E5F870164
     */
    private AdminManager()
    {
    }

    public Collection retrieveUserAccounts () throws EmanagerDatabaseException
    {
	return DatabaseInterface.instance().retrieveUserAccounts();
    }

    public UserAccount retrieveUserAccount (ManagedObjectId userAccountId) throws EmanagerDatabaseException
    {
	if (userAccountId == null ||
	    userAccountId.getManagedObjectType() != ManagedObjectIdType.UserAccount) {
	    return null;
	}

	return DatabaseInterface.instance().retrieveUserAccount (userAccountId);
    }

    public void  deleteUserAccount (ManagedObjectId userAccountId) throws EmanagerDatabaseException
    {
	if (userAccountId == null ||
	    userAccountId.getManagedObjectType() != ManagedObjectIdType.UserAccount) {
	    return;
	}

	DatabaseInterface.instance().removeUserAccount (userAccountId);
    }

    public void updateUserAccount (UserAccount userAccount) throws EmanagerDatabaseException
    {
	if (userAccount == null) {
	    return;
	}

	DatabaseInterface.instance().updateUserAccount (userAccount);
    }

    public UserAccount createUserAccount (String name,
					  String password,
					  String passwordKey,
					  ManagedObjectId securityRoleId) throws EmanagerAdminException,
										 EmanagerDatabaseException
    {
	if (name == null ||
	    name.trim().length() == 0) {
	    EmanagerAdminException e;

	    e = new EmanagerAdminException (EmanagerAdminStatusCode.MalformedUserAccountName,
					    EmanagerAdminStatusCode.MalformedUserAccountName.getStatusCodeDescription() +
					    name);
	    throw e;
	}

	if (password == null ||
	    password.trim().length() == 0) {
	    EmanagerAdminException e;

	    e = new EmanagerAdminException (EmanagerAdminStatusCode.MalformedUserAccountPassword,
					    EmanagerAdminStatusCode.MalformedUserAccountPassword.getStatusCodeDescription() +
					    password);
	    throw e;
	}

	if (passwordKey == null ||
	    passwordKey.trim().length() == 0) {
	    EmanagerAdminException e;

	    e = new EmanagerAdminException (EmanagerAdminStatusCode.MalformedUserAccountPasswordKey,
					    EmanagerAdminStatusCode.MalformedUserAccountPasswordKey.getStatusCodeDescription() +
					    passwordKey);
	    throw e;
	}

	if (securityRoleId.getManagedObjectType() != ManagedObjectIdType.SecurityRole) {
	    EmanagerAdminException e;

	    e = new EmanagerAdminException (EmanagerAdminStatusCode.MalformedUserAccountSecurityRoleId,
					    EmanagerAdminStatusCode.MalformedUserAccountSecurityRoleId.getStatusCodeDescription() +
					    securityRoleId);
	    throw e;
	}

	return DatabaseInterface.instance().createUserAccount (name.trim(),
							       password.trim(),
							       passwordKey.trim(),
							       securityRoleId);
    }

    public Collection retrieveSecurityRoles () throws EmanagerDatabaseException
    {
	return DatabaseInterface.instance().retrieveSecurityRoles();
    }

    public SecurityRole retrieveSecurityRole (ManagedObjectId securityRoleId) throws EmanagerDatabaseException
    {
	if (securityRoleId == null ||
	    securityRoleId.getManagedObjectType() != ManagedObjectIdType.SecurityRole) {
	    return null;
	}

	return DatabaseInterface.instance().retrieveSecurityRole(securityRoleId);
    }

    public void  deleteSecurityRole (ManagedObjectId securityRoleId) throws EmanagerDatabaseException
    {
	if (securityRoleId == null ||
	    securityRoleId.getManagedObjectType() != ManagedObjectIdType.SecurityRole) {
	    return;
	}

	DatabaseInterface.instance().removeSecurityRole(securityRoleId);
    }

    public void updateSecurityRole (SecurityRole securityRole) throws EmanagerDatabaseException
    {
	if (securityRole == null) {
	    return;
	}

	DatabaseInterface.instance().updateSecurityRole(securityRole);
    }

    public SecurityRole createSecurityRole (String name) throws EmanagerAdminException,
								EmanagerDatabaseException
    {
	if (name == null ||
	    name.trim().length() == 0) {
	    EmanagerAdminException e;

	    e = new EmanagerAdminException (EmanagerAdminStatusCode.MalformedSecurityRoleName,
					    EmanagerAdminStatusCode.MalformedSecurityRoleName.getStatusCodeDescription() +
					    name);
	    throw e;
	}

	return DatabaseInterface.instance().createSecurityRole(name);
    }

    public synchronized static AdminManager instance()
    {
        if (adminManager == null) {
	    adminManager = new AdminManager();
	}

        return adminManager;
    }

    // handle requests from NBAPI - especially SOAP
    public String login(String userid, String password)
	    throws EmanagerAdminException
    {

	    logger.debug("AdminManager::login(" + userid + ")");
	    UserAccount account = AdminCache.instance().getUserAccount(userid);
	    if (account == null) {
		    logger.error("Cound not find userId: " + userid + " from AdminCache");
		    EmanagerAdminException e = new EmanagerAdminException (
			    EmanagerAdminStatusCode.UserIdNotFound,
			    EmanagerAdminStatusCode.UserIdNotFound.getStatusCodeDescription() + userid);
		    throw e;
	    }

	    if (!password.equals(account.getPassword())) {
		    logger.error("Invalid password: " + password + ":" + account.getPassword());
		    EmanagerAdminException e = new EmanagerAdminException (
			    EmanagerAdminStatusCode.InvalidPassword,
			    EmanagerAdminStatusCode.InvalidPassword.getStatusCodeDescription());
		    throw e;
	    }

	    // user is authenticated - create UserSession for this user
	    UserSession session = new UserSession(account);
	    AdminCache.instance().addUserSessionToCache(session);
	    return session.getSessionId();
    }

    public void logout(String sessionID)
    {
	    logger.debug("AdminManager::logout()");
	    AdminCache.instance().removeUserSessionFromCache(sessionID);
    }

    public UserSession isAuthenticated(String sessionID)
	    throws EmanagerAdminException
	  
    { 
	    logger.debug("AdminManager::isAuthenticated");
	    UserSession session = AdminCache.instance().getUserSession(sessionID);
	    if (session == null) {
		    logger.error(sessionID + " is not valid sessionID");
		    EmanagerAdminException e = new EmanagerAdminException (
			    EmanagerAdminStatusCode.InvalidSessionId,
			    EmanagerAdminStatusCode.InvalidSessionId.getStatusCodeDescription() + sessionID);
		    throw e;
	    }

	    if ( (System.currentTimeMillis() - session.getLastAccessTime()) > 1000*session.getTimeout()) {
		    logger.error ("This session is timout");
		    // remove it from the cache
		    AdminCache.instance().removeUserSessionFromCache(sessionID);
		    EmanagerAdminException e = new EmanagerAdminException (
			    EmanagerAdminStatusCode.InvalidSessionId,
			    EmanagerAdminStatusCode.InvalidSessionId.getStatusCodeDescription() + sessionID);
		    throw e;
	    }

	    // update the lastAccessTime for this session
	    session.updateLastAccessTime();
	    AdminCache.instance().updateUserSessionInCache(session);

	    return session;
    }

    private UserAccount retrieveUserAccountFromName(String userId)
    {

	    logger.debug("AdminManager::retrieveUserAccountFromName");
	    UserAccount account = null;
	    
	    account = AdminCache.instance().getUserAccount(userId);
	    if (account == null) {
		    logger.debug("Cound not find UserAccount for userId: " + userId + " from AdminCache");
		    logger.debug("Try the search the account from database");
		    account = retrieveUserAccountFromNameFromDatabase(userId);
	    }

	   if (account != null) {
		   AdminCache.instance().addUserAccountToCache(account);
	   }

	   return account;

    }

    private UserAccount retrieveUserAccountFromNameFromDatabase(String userId)
    {
	    logger.debug("AdminManager::retrieveUserAccountFromNameFromDatabase");

	    UserAccount account = null;

	    try {
		    Collection collection = DatabaseInterface.instance().retrieveUserAccounts();
		    if (collection != null && !collection.isEmpty()) {
			    Iterator iter = collection.iterator(); 
			    while (iter.hasNext()) {
				    UserAccount element = (UserAccount)iter.next(); 
				    if (element.getName().equals(userId))
					    return element;
			    }
		    }
	    } catch (EmanagerDatabaseException e) {
		    logger.error("Caught EmanagerDatabaseException: " + e);
	    }
	    return account;
    }



    public void updateUserPassword(String sessionID, String oldPassword, String newPassword)
	    throws EmanagerAdminException, EmanagerDatabaseException
    {
	    logger.debug("AdminManager::updateUserPassword");

	    // shoud not fail since we already checked whether this user is authenticated.
	    UserSession session = AdminCache.instance().getUserSession(sessionID);
	    
	    UserAccount account = retrieveUserAccountFromName(session.getName());
	    if (account == null) { // we cannot retrieve the accouunt from anywhere - throw exception now
		    logger.debug("Cleanup this UserSession because we cannot find the UserAccount from both cache & database");
		    AdminCache.instance().removeUserSessionFromCache(sessionID);

		    EmanagerAdminException e = new EmanagerAdminException (
			    EmanagerAdminStatusCode.UserAccountNotFound,
			    EmanagerAdminStatusCode.UserAccountNotFound.getStatusCodeDescription() + session.getName());
		    throw e;
	    }

	    if (!session.getName().equals(account.getName())) {
		    logger.error("ALERT: Strange circumstance: userId from login session and from database are not the matched");
		    EmanagerAdminException e = new EmanagerAdminException (
			    EmanagerAdminStatusCode.UserIdNotMatched,
			    EmanagerAdminStatusCode.UserIdNotMatched.getStatusCodeDescription());
		    throw e;
	    }
	    if (!account.getPassword().equals(oldPassword)) {
		    logger.error("Invalid pasword " + oldPassword);
		    EmanagerAdminException e = new EmanagerAdminException (
			    EmanagerAdminStatusCode.InvalidPassword,
			    EmanagerAdminStatusCode.InvalidPassword.getStatusCodeDescription());
		    throw e;
	    }

	    if (account.getPassword().equals(newPassword)) {
		    logger.info("Try to update the password with the same value? - just return");
		    return;
	    } else {
		    account.setPassword(newPassword);
		    logger.debug("Update password for account: " + account.getName());
		    updateUserAccount(account);
		    // also also update the cache
		    AdminCache.instance().updateUserAccountInCache(account);
	    }
	    
    }
    
    public void resetUserPassword(String userId, String newPassword)
	    throws EmanagerAdminException, EmanagerDatabaseException
    {
	    logger.debug("AdminManager::resetUserPassword");

	    // retrieve the user account
	    UserAccount account = retrieveUserAccountFromName(userId);

	    if (account == null) {
		    logger.error("Could not find UserAccount with userId: " + userId);
		    EmanagerAdminException e = new EmanagerAdminException (
			    EmanagerAdminStatusCode.UserAccountNotFound,
			    EmanagerAdminStatusCode.UserAccountNotFound.getStatusCodeDescription() + userId);
		    throw e;
	    }

	    account.setPassword(newPassword);
	    logger.debug("Update password for account: " + account.getName());
	    updateUserAccount(account);
	    // also also update the cache
	    AdminCache.instance().updateUserAccountInCache(account);
    }

    public void updateUserTimeoutSession(String sessionID, long timeoutValue)
    {

	    logger.debug("AdminManager::updateUserTimeoutSession");
	    UserSession session = AdminCache.instance().getUserSession(sessionID);
	    session.setTimeout(timeoutValue);

	    AdminCache.instance().updateUserSessionInCache(session);
    }

    public void addUserAccount(String username, String password, String role)
	    throws EmanagerAdminException, EmanagerDatabaseException
    {
	    logger.debug("AdminManager::addUserAccount");
	    ManagedObjectId securityRoleId;
	    if (role.equals(ADMINISTRATOR)) 
		    securityRoleId = AdminGlobals.AdministratorSecurityRoleId;
	    else if (role.equals(OPERATOR))
		    securityRoleId = AdminGlobals.OperatorSecurityRoleId;
	    else if (role.equals(GUEST))
		    securityRoleId = AdminGlobals.GuestSecurityRoleId;
	    else {
		    logger.error("Invalid role name: " + role);
		    EmanagerAdminException e = new EmanagerAdminException (
			EmanagerAdminStatusCode.MalformedSecurityRoleName,
			EmanagerAdminStatusCode.MalformedSecurityRoleName.getStatusCodeDescription() + role);
		    throw e;
	    }

	    UserAccount account = createUserAccount(username, password, password, securityRoleId);

	    // update this account into AdminCache
	    logger.debug("add UserAccount: " + username + " to AdminCache");
	    AdminCache.instance().addUserAccountToCache(account);
	    
    }

    public void removeUserAccount(String username) 
	    throws EmanagerAdminException, EmanagerDatabaseException
    {

	    logger.debug("AdminManager::removeUserAccount");
	    UserAccount account = retrieveUserAccountFromName(username);
	    if (account != null) {
		    ManagedObjectId accountId = account.getId();
		    AdminCache.instance().removeUserAccountFromCache(username);
		    deleteUserAccount(account.getId());
		    // Do I need to delete the associated UserSession if this user is log-in?

	    } else {
		    EmanagerAdminException e = new EmanagerAdminException (
			    EmanagerAdminStatusCode.UserAccountNotFound,
			    EmanagerAdminStatusCode.UserAccountNotFound.getStatusCodeDescription() + username);
		    throw e;
	    }

    }
}

/**
 * AdminManagerSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.admin;

import com.cisco.eManager.common.admin.EmanagerAdminException;
import com.cisco.eManager.common.admin.EmanagerAdminStatusCode;
import com.cisco.eManager.common.database.EmanagerDatabaseException;
import com.cisco.eManager.eManager.admin.UserSession;
import org.apache.axis.AxisFault;

import org.apache.log4j.*;

public class AdminManagerSoapBindingImpl implements 
	com.cisco.eManager.soap.admin.AdminManager
{

	private com.cisco.eManager.eManager.admin.AdminManager _adminMgr = null;

	private static Logger logger = Logger.getLogger(AdminManagerSoapBindingImpl.class);

	public AdminManagerSoapBindingImpl()
	{
		logger.debug("AdminManagerSoapBindingImpl() constructor");

		_adminMgr = com.cisco.eManager.eManager.admin.AdminManager.instance();
		
	}

	public String login(String userId, String password)
		throws java.rmi.RemoteException
	{
		logger.debug("--->login()");
		String sessionToken = null;
		
		try {
			sessionToken = _adminMgr.login(userId, password);

		} catch (EmanagerAdminException e) {
			logger.debug("Caught: EmanagerAdminException: " + e);
			throw AxisFault.makeFault(e);
		}

		return sessionToken;
	}

	public void updateUserPassword (String sessionID, String oldPassword, String newPassword)
		throws java.rmi.RemoteException
	{

		logger.debug(" ---> updateUserPassword");
		try {
	    		UserSession session = _adminMgr.isAuthenticated(sessionID);
			_adminMgr.updateUserPassword(sessionID, oldPassword, newPassword);

		} catch (EmanagerAdminException e) {
			logger.debug("Caught: EmanagerAdminException: " + e);
			throw AxisFault.makeFault(e);
		} catch (EmanagerDatabaseException e) {
			logger.debug("Caught: EmanagerDatabaseException: " + e);
			throw AxisFault.makeFault(e);
		}

	}

	public void resetUserPassword (String sessionID, String userid, String newPassword)
		throws java.rmi.RemoteException
	{
		logger.debug("-->resetUserPassword");
		try {
	    		UserSession session = _adminMgr.isAuthenticated(sessionID);
			if (!session.isAdministratorRole())
			{
				EmanagerAdminException e = new EmanagerAdminException (
					EmanagerAdminStatusCode.NotAuthorized,
					EmanagerAdminStatusCode.NotAuthorized.getStatusCodeDescription() + session.getName());
				throw e;
			}
			_adminMgr.resetUserPassword(userid, newPassword);
		} catch (EmanagerAdminException e) {
			logger.debug("Caught: EmanagerAdminException: " + e);
			throw AxisFault.makeFault(e);
		} catch (EmanagerDatabaseException e) {
			logger.debug("Caught: EmanagerDatabaseException: " + e);
			throw AxisFault.makeFault(e);
		}

	}


	public void updateUserTimeoutSession (String sessionID, long timeout)
		throws java.rmi.RemoteException
	{
		logger.debug("-->updateUserTimeoutSession");
		try {
	    		UserSession session = _adminMgr.isAuthenticated(sessionID);
			_adminMgr.updateUserTimeoutSession(sessionID, timeout);
			
		} catch (EmanagerAdminException e) {
			logger.debug("Caught: EmanagerAdminException: " + e);
			throw AxisFault.makeFault(e);
		}

	}
	public void logout(String sessionID) throws java.rmi.RemoteException 
	{
		logger.debug("-->logout");
		_adminMgr.logout(sessionID);
	}
	 
	public void createUserAccount(String sessionID, String username, String password, String role)
		throws java.rmi.RemoteException
	{
		logger.debug("-->createUserAccount");
		try {
	    		UserSession session = _adminMgr.isAuthenticated(sessionID);
			if (!session.isAdministratorRole()) {
				EmanagerAdminException e = new EmanagerAdminException (
					EmanagerAdminStatusCode.NotAuthorized,
					EmanagerAdminStatusCode.NotAuthorized.getStatusCodeDescription() + session.getName());
				throw e;
			}
			_adminMgr.addUserAccount(username, password, role);
		} catch (EmanagerAdminException e) {
			logger.debug("Caught: EmanagerAdminException: " + e);
			throw AxisFault.makeFault(e);
		} catch (EmanagerDatabaseException e) {
			logger.debug("Caught: EmanagerDatabaseException: " + e);
			throw AxisFault.makeFault(e);
		}
	}
	 

	public void deleteUserAccount(String sessionID, String userid) 
		throws java.rmi.RemoteException 
	{
		logger.debug("-->deleteUserAccount");
		try {
	    		UserSession session = _adminMgr.isAuthenticated(sessionID);
			if (!session.isAdministratorRole()) {
				EmanagerAdminException e = new EmanagerAdminException (
					EmanagerAdminStatusCode.NotAuthorized,
					EmanagerAdminStatusCode.NotAuthorized.getStatusCodeDescription() + session.getName());
				throw e;
			}
			_adminMgr.removeUserAccount(userid);
			
		} catch (EmanagerAdminException e) {
			logger.debug("Caught: EmanagerAdminException: " + e);
			throw AxisFault.makeFault(e);
		} catch (EmanagerDatabaseException e) {
			logger.debug("Caught: EmanagerDatabaseException: " + e);
			throw AxisFault.makeFault(e);
		}
	}

}

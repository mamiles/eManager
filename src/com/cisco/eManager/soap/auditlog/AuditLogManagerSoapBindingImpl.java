/**
 * AuditLogManagerSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.auditlog;

import org.apache.log4j.*;
import com.cisco.eManager.eManager.audit.AuditManager;

import com.cisco.eManager.common.util.AccessType;
import com.cisco.eManager.eManager.admin.AdminManager;
import com.cisco.eManager.eManager.admin.UserSession;
import com.cisco.eManager.common.admin.EmanagerAdminException;
import org.apache.axis.AxisFault;

public class AuditLogManagerSoapBindingImpl implements 
	com.cisco.eManager.soap.auditlog.AuditLogManager
{

	private AuditManager _auditlogMgr = null;
	private AdminManager _adminMgr = null;
	private static Logger logger = Logger.getLogger(AuditLogManagerSoapBindingImpl.class);

	public AuditLogManagerSoapBindingImpl()
	{
		logger.debug("AuditLogManagerSoapBindingImpl() constructor");
		_auditlogMgr = AuditManager.instance();
		_adminMgr = AdminManager.instance();
	}


	public String xmlMessage(String sessionID, String xmlStream) 
		throws java.rmi.RemoteException 
	{
		logger.debug("--> xmlMessage(" + xmlStream + ")");
		try {
	    		UserSession session = _adminMgr.isAuthenticated(sessionID);
			return _auditlogMgr.handleRequest(xmlStream, session.getName(), session.getAccessType());
		} catch(EmanagerAdminException e) {
			logger.debug("Caught EmanagerAdminException: " + e);
			throw AxisFault.makeFault(e);
		}

	}
}

/**
 * InventoryManagerSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.inventory;

import org.apache.log4j.*;

import com.cisco.eManager.eManager.inventory.NbapiMsgHandler;
import com.cisco.eManager.common.util.AccessType;
import com.cisco.eManager.eManager.admin.AdminManager;
import com.cisco.eManager.eManager.admin.UserSession;
import com.cisco.eManager.common.admin.EmanagerAdminException;
import org.apache.axis.AxisFault;

/*
import com.cisco.eManager.soap.common.EmgrToSoap;
import com.cisco.eManager.soap.common.SoapToEmgr;
import com.cisco.eManager.soap.common.inventory.*;
*/

public class InventoryManagerSoapBindingImpl implements 
	com.cisco.eManager.soap.inventory.InventoryManager
{

	private NbapiMsgHandler _inventoryMgr = null;
	private AdminManager _adminMgr = null;
	// private com.cisco.eManager.soap.common.EmgrToSoap _emgrToSoap = null;
	// private com.cisco.eManager.soap.common.SoapToEmgr _soapToEmgr = null;

	private static Logger logger = Logger.getLogger(InventoryManagerSoapBindingImpl.class);

	public InventoryManagerSoapBindingImpl()
	{
		logger.debug("InventoryManagerSoapBindingImpl() constructor");

		try {
			_inventoryMgr = NbapiMsgHandler.instance();
			_adminMgr = AdminManager.instance();
		} catch (Exception e) {
			logger.error("It can't be. NbapiMsgHandler.instance() throws Exception....: " + e);
			e.printStackTrace(System.out);
		}
		// _emgrToSoap = com.cisco.eManager.soap.common.EmgrToSoap.instance();
		// _soapToEmgr = com.cisco.eManager.soap.common.SoapToEmgr.instance();
		
	}

	public String xmlMessage(String sessionID, String xmlStream) 
		throws java.rmi.RemoteException 
	{
		logger.debug("--> xmlMessage(" + xmlStream + ")");
		try {
	    		UserSession session = _adminMgr.isAuthenticated(sessionID);
			return _inventoryMgr.handleRequest(xmlStream, session.getName(), session.getAccessType());
		} catch(EmanagerAdminException e) {
			logger.debug("Caught EmanagerAdminException: " + e);
			throw AxisFault.makeFault(e);
		}
	}
	/*

	public String invoke(com.cisco.eManager.soap.common.inventory.Method method) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
        	return "SOAP inventory.Method() is not ready yet";
	}

 	public com.cisco.eManager.soap.common.inventory.ManagedObjectId createContainerNode(
		String name, int type, com.cisco.eManager.soap.common.inventory.ManagedObjectId parentId) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP createContainerNode() is not ready yet");
		return null;
	}

	public void deleteContainerNode(com.cisco.eManager.soap.common.inventory.ManagedObjectId nodeId) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP createContainerNode() is not ready yet");
	}

	public com.cisco.eManager.soap.common.inventory.Agent getAgent(
		com.cisco.eManager.soap.common.inventory.ManagedObjectId agentId) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP getAgent() is not ready yet");
		return null;
	}

	public com.cisco.eManager.soap.common.inventory.AppInstance getAppInstance(
		com.cisco.eManager.soap.common.inventory.ManagedObjectId appInstanceId) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP getAppInstance() is not ready yet");
		return null;
	}

	public com.cisco.eManager.soap.common.inventory.ViewContainer getAppsViewRoot() 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP getAppsViewRoot() is not ready yet");
		return null;
	}

	public com.cisco.eManager.soap.common.inventory.AppType getAppType(
		com.cisco.eManager.soap.common.inventory.ManagedObjectId appTypeId) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP getAppType() is not ready yet");
		return null;
	}

	public com.cisco.eManager.soap.common.inventory.ViewContainer getContainer(
		com.cisco.eManager.soap.common.inventory.ManagedObjectId containerId)
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP getContainer() is not ready yet");
		return null;
	}

	public com.cisco.eManager.soap.common.inventory.ViewContainer[] getContainerChildren(
		com.cisco.eManager.soap.common.inventory.ManagedObjectId containerId)
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP getContainerChildren() is not ready yet");
		return null;
	}

	public com.cisco.eManager.soap.common.inventory.ViewContainer getHostViewRoot()
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP getHostViewRoot() is not ready yet");
		return null;
	}

	public com.cisco.eManager.soap.common.inventory.Instrumentation[] getInstrumentation(
		com.cisco.eManager.soap.common.inventory.ManagedObjectId appInstanceId) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP getInstrumentation() is not ready yet");
		return null;
	}

	public com.cisco.eManager.soap.common.inventory.MgmtPolicy[] getManagementPolicies(
		com.cisco.eManager.soap.common.inventory.ManagedObjectId appTypeId, 
		com.cisco.eManager.soap.common.inventory.ManagedObjectId agentId)
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP getManagementPolicies() is not ready yet");
		return null;
	}

	public void manageAppInstance(com.cisco.eManager.soap.common.inventory.ManagedObjectId appInstanceId)
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP manageAppInstance() is not ready yet");
	}

	public void moveContainerNode(com.cisco.eManager.soap.common.inventory.ManagedObjectId nodeId, 
		com.cisco.eManager.soap.common.inventory.ManagedObjectId newParentId)
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP moveContainerNode() is not ready yet");
	}

	public void unmanageAppInstance(com.cisco.eManager.soap.common.inventory.ManagedObjectId appInstanceId)
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP unmanageAppInstance() is not ready yet");
	}

	public com.cisco.eManager.soap.common.inventory.LogLevel getLogLevel(
		com.cisco.eManager.soap.common.inventory.ManagedObjectId appInstanceId)
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP getLogLevel() is not ready yet");
		return null;
	}

	public void setLogLevel(com.cisco.eManager.soap.common.inventory.ManagedObjectId appInstanceId, 
		com.cisco.eManager.soap.common.inventory.LogLevel loglevel)
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.inventory.EmanagerInventoryException 
	{
		logger.debug("SOAP setLogLevel() is not ready yet");
	}
	*/
}

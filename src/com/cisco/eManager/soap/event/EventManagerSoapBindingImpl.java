/**
 * EventManagerSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.event;

import org.apache.log4j.*;

import com.cisco.eManager.eManager.event.EventManager;
import com.cisco.eManager.common.util.AccessType;
import com.cisco.eManager.eManager.admin.AdminManager;
import com.cisco.eManager.eManager.admin.UserSession;
import com.cisco.eManager.common.admin.EmanagerAdminException;
import org.apache.axis.AxisFault;

/*
import com.cisco.eManager.soap.common.EmgrToSoap;
import com.cisco.eManager.soap.common.SoapToEmgr;
import com.cisco.eManager.soap.common.event.*;
*/

public class EventManagerSoapBindingImpl implements com.cisco.eManager.soap.event.EventManager
{

	private EventManager _eventMgr = null;
	private AdminManager _adminMgr = null;
	// private com.cisco.eManager.soap.common.EmgrToSoap _emgrToSoap = null;
	// private com.cisco.eManager.soap.common.SoapToEmgr _soapToEmgr = null;

	private static Logger logger = Logger.getLogger(EventManagerSoapBindingImpl.class);

	public EventManagerSoapBindingImpl()
	{
		logger.debug("EventManagerSoapBindingImpl() constructor");

		_eventMgr = EventManager.instance();
		_adminMgr = AdminManager.instance();
		// _emgrToSoap = com.cisco.eManager.soap.common.EmgrToSoap.instance();
		// _soapToEmgr = com.cisco.eManager.soap.common.SoapToEmgr.instance();
		
	}

	public String xmlMessage(String sessionID, String xmlStream) 
		throws java.rmi.RemoteException 
	{
		logger.debug("--> xmlMessage(" + xmlStream + ")");
		try {
	    		UserSession session = _adminMgr.isAuthenticated(sessionID);
			return _eventMgr.handleRequest(xmlStream, session.getName(), session.getAccessType());
		} catch(EmanagerAdminException e) {
			logger.debug("Caught EmanagerAdminException: " + e);
			throw AxisFault.makeFault(e);
		}
	}

/*

	public void acknowledgeEvent(
		com.cisco.eManager.soap.common.inventory.ManagedObjectId eventId, 
		com.cisco.eManager.soap.common.event.EventAcknowledgement acknowledgement) 
		throws java.rmi.RemoteException,
		       com.cisco.eManager.soap.common.event.EmanagerEventException, 
		       com.cisco.eManager.soap.common.database.EmanagerDatabaseException 
	{
		logger.debug("SOAP API acknowledgeEvent() is not ready yet");
	}

	public void unacknowledgeEvent(
		com.cisco.eManager.soap.common.inventory.ManagedObjectId eventId, 
		com.cisco.eManager.soap.common.inventory.ManagedObjectId userId) 
		throws java.rmi.RemoteException, 
		       com.cisco.eManager.soap.common.event.EmanagerEventException, 
		       com.cisco.eManager.soap.common.database.EmanagerDatabaseException 
	{
		logger.debug("SOAP API unacknowledgeEvent() is not ready yet");
	}

	public com.cisco.eManager.soap.common.event.EmanagerEventDetails getEventDetails(
		com.cisco.eManager.soap.common.inventory.ManagedObjectId eventId) 
		throws java.rmi.RemoteException, 
		       com.cisco.eManager.soap.common.event.EmanagerEventException, 
		       com.cisco.eManager.soap.common.database.EmanagerDatabaseException 
	{
		logger.debug("SOAP API getEventDetails() is not ready yet");
		return null;
	}

	public Object[] retrieveEvents(com.cisco.eManager.soap.common.event.EventSearchCriteria eventSearchCriteria)
		throws java.rmi.RemoteException, 
		       com.cisco.eManager.soap.common.event.EmanagerEventException, 
		       com.cisco.eManager.soap.common.database.EmanagerDatabaseException 
	{
		logger.debug("SOAP API retrieveEvents() is not ready yet");
		return null;
	}

	public void deleteEvents(
		com.cisco.eManager.soap.common.event.EventDeletionCriteria criteria) 
		throws java.rmi.RemoteException, 
		       com.cisco.eManager.soap.common.event.EmanagerEventException, 
		       com.cisco.eManager.soap.common.database.EmanagerDatabaseException 
	{
		logger.debug("SOAP API deleteEvents() is not ready yet");
	}

	public void registerSNMPClient(
		String host, String community, int port) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.event.EmanagerEventException 
	{
		logger.debug("SOAP API registerSNMPClient() is not ready yet");
	}

	public void unregisterSNMPClient(
		String host, String community, int port) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.event.EmanagerEventException 
	{
		logger.debug("SOAP API unregisterSNMPClient() is not ready yet");
	}

	public void eventAcknowledgementNotification(
		com.cisco.eManager.soap.common.event.AcknowledgementEvent ack) 
		throws java.rmi.RemoteException 
	{
		logger.debug("SOAP API eventAcknowledgementNotification() is not ready yet");
	}

	public void eventUnacknowledgementNotification(
		com.cisco.eManager.soap.common.event.UnacknowledgementEvent unack) 
		throws java.rmi.RemoteException 
	{
		logger.debug("SOAP API eventUnacknowledgementNotification() is not ready yet");
	}
*/

}

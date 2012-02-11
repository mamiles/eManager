/**
 * ProcessManagerSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.process;

import org.apache.log4j.*;
import com.cisco.eManager.eManager.process.ProcessManager;
import com.cisco.eManager.common.util.AccessType;
import com.cisco.eManager.eManager.admin.AdminManager;
import com.cisco.eManager.eManager.admin.UserSession;
import com.cisco.eManager.common.admin.EmanagerAdminException;
import org.apache.axis.AxisFault;
/*
import com.cisco.eManager.soap.common.EmgrToSoap;
import com.cisco.eManager.soap.common.SoapToEmgr;
import com.cisco.eManager.soap.common.process.*;
*/

public class ProcessManagerSoapBindingImpl 
	implements com.cisco.eManager.soap.process.ProcessManager
{ 

	private ProcessManager _processMgr = null;
	private AdminManager _adminMgr = null;

	// private com.cisco.eManager.soap.common.EmgrToSoap _emgrToSoap = null;
	// private com.cisco.eManager.soap.common.SoapToEmgr _soapToEmgr = null;

	private static Logger logger = Logger.getLogger(ProcessManagerSoapBindingImpl.class);

	public ProcessManagerSoapBindingImpl()
	{
		logger.debug("ProcessManagerSoapBindingImpl() constructor");

		_processMgr = ProcessManager.instance();
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
			return _processMgr.handleRequest(xmlStream, session.getName(), session.getAccessType());
		} catch(EmanagerAdminException e) {
			logger.debug("Caught EmanagerAdminException: " + e);
			throw AxisFault.makeFault(e);
		}
	}

	/*

    	public com.cisco.eManager.soap.common.process.ProcessInfoObj[] getProcessStatus(
			String appType, String appInstance) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException 
	{
		logger.debug("getProcessStatus(" + appType + "," + appInstance + ")");
		try {
			return _emgrToSoap.getProcessInfoObjs(_processMgr.getProcessStatus(appType, appInstance));
			
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("getProcessStatus caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}

	}

    	public com.cisco.eManager.soap.common.process.ProcessInfoRec getProcessStatusFor(
			String appType, String appInstance, String processName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException 
	{
		logger.debug("getProcessStatusFor(" + appType + "," + appInstance + "," + processName + ")");
		try {
			return _emgrToSoap.getProcessInfoRec(_processMgr.getProcessStatusFor(appType, appInstance, processName));
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("getProcessStatusFor caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	

	public String getGroupState(String appType, String appInstance, String groupName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException 
	{
		logger.debug("getGroupState(" + appType + "," + appInstance + "," + groupName + ")");
		try {
			return _processMgr.getGroupState(appType, appInstance, groupName);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("getGroupState caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public com.cisco.eManager.soap.common.process.SolutionStatusInfoObj[] getSolutionStatus(String solutionName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException
	{
		logger.debug("getSolutionStatus(" + solutionName + ")");

		try {
			return _emgrToSoap.getSolutionStatusInfoObjs(_processMgr.getSolutionStatus(solutionName));
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("getSolutionStatus caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}
	
	public String[] getAllGroupNames(String appType, String appInstance) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException 
	{
		logger.debug("getAllGroupNames(" + appType + "," + appInstance + ")");

		try {
			return _processMgr.getAllGroupNames(appType, appInstance);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("getAllGroupNames caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

    	public String[] getProcessesForGroup(String appType, String appInstance, String groupName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException
	{
		logger.debug("getProcessForGroup(" + appType + "," + appInstance + "," + groupName + ")");
		try {
			return _processMgr.getProcessesForGroup(appType, appInstance, groupName);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("getProcessForGroup caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public boolean getHealth(String appType, String appInstance) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException
	{
		logger.debug("getHealth(" + appType + "," + appInstance + ")");
		try {
			return _processMgr.getHealth(appType, appInstance);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("getHealth caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public boolean getSolutionHealth(String solutionName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException 
	{
		logger.debug("getSolutionHealth(" + solutionName + ")");
		try {
			return _processMgr.getSolutionHealth(solutionName);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("getSolutionHealth caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public void startProcess(String appType, String appInstance, String processName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException 
	{
		logger.debug("startProcess(" + appType + "," + appInstance + "," + processName + ")");
		try {
			_processMgr.startProcess(appType, appInstance, processName);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("startProcess() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public String startCommand(String appType, String appInstance, String command) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException
	{
		logger.debug("startCommand(" + appType + "," + appInstance + "," + command + ")");
		try {
			return _processMgr.startCommand(appType, appInstance, command);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("startCommand() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public void startGroup(String appType, String appInstance, String groupName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException
	{
		logger.debug("startGroup(" + appType + "," + appInstance + "," + groupName + ")");
		try {
			_processMgr.startGroup(appType, appInstance, groupName);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("startGroup() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public void stopProcess(String appType, String appInstance, String processName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException 
	{
		logger.debug("stopProcess(" + appType + "," + appInstance + "," + processName + ")");
		try {
			_processMgr.stopProcess(appType, appInstance, processName);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("stopProcess() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public String stopCommand(String appType, String appInstance, String command) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException 
	{
		logger.debug("stopCommand(" + appType + "," + appInstance + "," + command + ")");
		try {
			return _processMgr.stopCommand(appType, appInstance, command);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("stopCommand() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public void stopGroup(String appType, String appInstance, String groupName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException
	{
		logger.debug("stopGroup(" + appType + "," + appInstance + "," + groupName + ")");
		try {
			_processMgr.stopGroup(appType, appInstance, groupName);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("stopGroup() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public void restartProcess(String appType, String appInstance, String processName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException
	{
		logger.debug("restartProcess(" + appType + "," + appInstance + "," + processName + ")");
		try {
			_processMgr.restartProcess(appType, appInstance, processName);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("restartProcess() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public void restartGroup(String appType, String appInstance, String groupName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException 
	{
		logger.debug("restartGroup(" + appType + "," + appInstance + "," + groupName + ")");
		try {
			_processMgr.restartGroup(appType, appInstance, groupName);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("restartGroup() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
		return;
	}

	public String restartCommand(String appType, String appInstance, String command) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException 
	{
		logger.debug("restartCommand(" + appType + "," + appInstance + "," + command + ")");
		try {
			return _processMgr.restartCommand(appType, appInstance, command);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("restartCommand() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public com.cisco.eManager.soap.common.inventory.LogLevel getLogLevel(String appType, String appInstance) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException 
	{

		logger.debug("getLogLevel(" + appType + "," + appInstance + ")");

		try {
			return _emgrToSoap.getLogLevel(_processMgr.getLogLevel(appType, appInstance));
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("getLogLevel() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}


	public void setLogLevel(String appType, String appInstance, com.cisco.eManager.soap.common.inventory.LogLevel loglevel) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException
	{
		logger.debug("setLogLevel(" + appType + "," + appInstance + ")");
		try {
			_processMgr.setLogLevel(appType, appInstance, _soapToEmgr.getLogLevel(loglevel));
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("setLogLevel() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public void startApplication(String appType, String appInstance) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException
	{
		logger.debug("startApplication(" + appType + "," + appInstance + ")");
		try {
			_processMgr.startApplication(appType, appInstance);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("startApplication() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public void startSolution(String solutionName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException
	{
		logger.debug("startSolution(" + solutionName + ")");
		try {
			_processMgr.startSolution(solutionName);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("startSolution() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	}

	public void stopApplication(String appType, String appInstance) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException
	{
		logger.debug("stopApplication(" + appType + "," + appInstance + ")");
		try {
			_processMgr.stopApplication(appType, appInstance);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("stopApplication() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
		return;
	}

	public void stopSolution(String solutionName) 
		throws java.rmi.RemoteException, com.cisco.eManager.soap.common.process.EmanagerProcessException
	{
		logger.debug("stopSolution(" + solutionName + ")");
		try {
			_processMgr.stopSolution(solutionName);
		} catch (com.cisco.eManager.common.exception.PsAPIException e) {
			logger.error("stopSolution() caught exception:" + e);
			e.printStackTrace(System.out);
			throw new com.cisco.eManager.soap.common.process.EmanagerProcessException();
		}
	} 

	*/

}

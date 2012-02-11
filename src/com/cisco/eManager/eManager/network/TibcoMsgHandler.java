package com.cisco.eManager.eManager.network;

import java.util.*;
import org.apache.log4j.*;
import com.tibco.tibrv.*;

import com.cisco.eManager.eManager.event.EventManager;
import com.cisco.eManager.eManager.inventory.NbapiMsgHandler;
import com.cisco.eManager.eManager.process.ProcessManager;
import com.cisco.eManager.eManager.log.LogManager;
import com.cisco.eManager.eManager.audit.AuditManager;
import com.cisco.eManager.common.util.XmlHelper;
import com.cisco.eManager.common.util.AccessType;
import com.cisco.eManager.eManager.util.GlobalProperties;


public class TibcoMsgHandler
{


	// move those to constant file
	public static String EVENT_SUBJECT_REQUEST = "cisco.mgmt.emanager.event.request";
	public static String EVENT_SUBJECT_RESPONSE = "cisco.mgmt.emanager.event.response";

	public static String INVENTORY_SUBJECT_REQUEST = "cisco.mgmt.emanager.inventory.request";
	public static String INVENTORY_SUBJECT_RESPONSE = "cisco.mgmt.emanager.inventory.response";

	public static String PROCESS_SUBJECT_REQUEST = "cisco.mgmt.emanager.process.request";
	public static String PROCESS_SUBJECT_RESPONSE = "cisco.mgmt.emanager.process.response";

        public static String LOG_SUBJECT_REQUEST = "cisco.mgmt.emanager.log.request";
	public static String LOG_SUBJECT_RESPONSE = "cisco.mgmt.emanager.log.response";

        public static String AUDIT_SUBJECT_REQUEST = "cisco.mgmt.emanager.audit.request";
	public static String AUDIT_SUBJECT_RESPONSE = "cisco.mgmt.emanager.audit.response";

	public static String EVENT_REQUEST = "eventMgrMsg";
	public static String INVENTORY_REQUEST = "inventoryMgrMsg";
	public static String PROCESS_REQUEST = "processMgrMsg";
        public static String LOG_REQUEST = "logMgrMsg";
        public static String AUDIT_REQUEST = "auditMgrMsg";

	public static String EMANAGER_MSG = "eManagerMessage";
	public static String MESSAGE_ID = "messageID";
	public static String EMANAGER_REQUEST = "request";
	public static String EMANAGER_RESPONSE = "response";

	public String FIELD_NAME = "DATA";

	private String m_service;
	private String m_network;
	private String m_daemon;
	private TibrvRvdTransport m_transport = null;

	private static TibcoMsgHandler _instance = null;
	private static Logger logger = Logger.getLogger(TibcoMsgHandler.class);

	private String TIBCO_USER = "TibcoRendezvous";
	private AccessType writeAccess = AccessType.WRITE;

	public static TibcoMsgHandler instance()
	{
		if (_instance == null)
			_instance = new TibcoMsgHandler();

		return _instance;
	}

	private void initTibco()
	{
		try {
			logger.debug("Opening Tibrv in native implementation...");
			Tibrv.open(Tibrv.IMPL_NATIVE);

        		logger.debug("CreateTibRvdTransport...");
			m_transport = new TibrvRvdTransport(m_service, m_network, m_daemon);

		} catch (TibrvException e) {
			logger.error("TibrvException occurred while trying to creat Tibco transport");
			e.printStackTrace(System.out);
		}
	}
	private TibcoMsgHandler()
	{

		GlobalProperties gp = GlobalProperties.instance();
		m_service = gp.getProperties().getProperty("SYSTEM.tibrv.service", "7500");
        	m_network = gp.getProperties().getProperty("SYSTEM.tibrv.network", "");
        	m_daemon = gp.getProperties().getProperty("SYSTEM.tibrv.daemon", "tcp:7500");
        	logger.debug("TibcoMsgHandler is using: service: " + m_service + ", network: " + m_network + ", daemon " + m_daemon);
		initTibco();

	}


	public void handleMessage(TibrvMsg msg)
	{
		logger.debug("Handle Message....");

		// check msg subject
		// for now we will handle 4 msg subject:
		// - cisco.mgmt.emanager.event.request
		// - cisco.mgmt.emanager.inventory.request
		// - cisco.mgmt.emanager.process.request
                // - cisco.mgmt.emanager.log.request

		String sendSubject = msg.getSendSubject();
		String respSubject = null;
		String xmlRequestMsg = null;

		try {
            		xmlRequestMsg = (String)msg.get(FIELD_NAME);
		} catch (TibrvException e) {
			logger.error("Exception caught during getting DATA from Tibco Msg: " + e);
			e.printStackTrace(System.out);
			return;
		}

                if (xmlRequestMsg == null) {
                    return;
                }

		StringBuffer xmlRequestStringBuffer = new StringBuffer(xmlRequestMsg);
		String xmlResponseMsg = null;

		if (sendSubject.equals(EVENT_SUBJECT_REQUEST)) {
			respSubject = EVENT_SUBJECT_RESPONSE;
			// extract the msg for eventMgr
			String xmlRequest =
				XmlHelper.subString(xmlRequestStringBuffer, EMANAGER_REQUEST);
			xmlResponseMsg = EventManager.instance().handleRequest(xmlRequest,TIBCO_USER, writeAccess);

		}
		else if (sendSubject.equals(INVENTORY_SUBJECT_REQUEST)) {
			respSubject = INVENTORY_SUBJECT_RESPONSE;
			// extract the msg for inventoryMgr
			String xmlRequest =
				XmlHelper.subString(xmlRequestStringBuffer, EMANAGER_REQUEST);
			xmlResponseMsg = NbapiMsgHandler.instance().handleRequest(xmlRequest,TIBCO_USER, writeAccess);
			//xmlResponseMsg = "TBD";
		}
		else if (sendSubject.equals(PROCESS_SUBJECT_REQUEST)) {
			respSubject = PROCESS_SUBJECT_RESPONSE;
			// extract the msg for processMgr
			String xmlRequest =
				XmlHelper.subString(xmlRequestStringBuffer, EMANAGER_REQUEST);
			xmlResponseMsg = ProcessManager.instance().handleRequest(xmlRequest,TIBCO_USER, writeAccess);
		}
                else if (sendSubject.equals(LOG_SUBJECT_REQUEST)) {
                        respSubject = LOG_SUBJECT_RESPONSE;
                        // extract the msg for logMgr
                        String xmlRequest =
                                XmlHelper.subString(xmlRequestStringBuffer, EMANAGER_REQUEST);
                        xmlResponseMsg = LogManager.instance().handleRequest(xmlRequest,TIBCO_USER, writeAccess);
                }
                else if (sendSubject.equals(AUDIT_SUBJECT_REQUEST)) {
                        respSubject = AUDIT_SUBJECT_RESPONSE;
                        // extract the msg for logMgr
                        String xmlRequest =
                                XmlHelper.subString(xmlRequestStringBuffer, EMANAGER_REQUEST);
                        xmlResponseMsg = AuditManager.instance().handleRequest(xmlRequest,TIBCO_USER, writeAccess);
                }
		else {
			logger.info("Do not handle msg with subject: " + sendSubject);
			return;
		}


		String xmlMsgId = XmlHelper.subString(xmlRequestStringBuffer, MESSAGE_ID);
		String xmlRespMsg = prepareResponseMsg(xmlMsgId, xmlResponseMsg);
		TibrvMsg respMsg = new TibrvMsg();

		try {
			respMsg.setSendSubject(respSubject);
			respMsg.update(FIELD_NAME, xmlRespMsg);
			logger.debug("Ready to send reply: " + respMsg.toString());
			m_transport.send(respMsg);
		} catch (TibrvException e) {
			logger.error("Exception occurred while sending msg: " + respMsg.toString());
			e.printStackTrace(System.out);
		}

	}

	private String prepareResponseMsg(String xmlMsgId, String xmlComponentRespMsg)
	{

		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append(XmlHelper.startTag(EMANAGER_MSG));
                xmlBuffer.append(GlobalProperties.CarriageReturn);
                xmlBuffer.append(XmlHelper.startTag(MESSAGE_ID));
                xmlBuffer.append(xmlMsgId);
                xmlBuffer.append(XmlHelper.endTag(MESSAGE_ID));
                xmlBuffer.append(GlobalProperties.CarriageReturn);
		xmlBuffer.append(XmlHelper.startTag(EMANAGER_RESPONSE));
                xmlBuffer.append(GlobalProperties.CarriageReturn);
		xmlBuffer.append(xmlComponentRespMsg);
                xmlBuffer.append(XmlHelper.endTag(EMANAGER_RESPONSE));
                xmlBuffer.append(GlobalProperties.CarriageReturn);
		xmlBuffer.append(XmlHelper.endTag(EMANAGER_MSG));

		return xmlBuffer.toString();

	}
}


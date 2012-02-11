package com.cisco.eManager.eManager.network;

import org.apache.log4j.*;
import com.tibco.tibrv.*;
import com.cisco.eManager.eManager.util.GlobalProperties;

public class TibcoListener extends Thread implements TibrvMsgCallback
{

	private String m_service;
	private String m_network;
	private String m_daemon;
	private String subject = "cisco.mgmt.emanager.*.*";

	private TibrvQueue m_queue = null;
	private TibrvRvdTransport m_transport = null; 
	private TibrvListener listener = null;
	private ClientTibcoMessageQueue clientTibcoMsgQueue = ClientTibcoMessageQueue.instance();

	private static Logger logger = Logger.getLogger(TibcoListener.class);

	public TibcoListener()
	{
		GlobalProperties gp = GlobalProperties.instance();
		m_service = gp.getProperties().getProperty("SYSTEM.tibrv.service", "7500");
        	m_network = gp.getProperties().getProperty("SYSTEM.tibrv.network", "");
        	m_daemon = gp.getProperties().getProperty("SYSTEM.tibrv.daemon", "tcp:7500");
        	logger.debug("TibcoListener is using: service: " + m_service + ", network: " + m_network + ", daemon " + m_daemon);

		try {
			Tibrv.open(Tibrv.IMPL_NATIVE);
		} catch (TibrvException e) {		
			logger.error("Failed to open Tibrv in native implementation");
			e.printStackTrace(System.out);
			System.exit(0);
		}

		try {
        		logger.debug("CreateTibRvdTransport...");
			m_transport = new TibrvRvdTransport(m_service, m_network, m_daemon);
		} catch (TibrvException e) {
			logger.error("Failed to create TibrvRvdTransport");
			e.printStackTrace(System.out);
			System.exit(0);
		}
					
		try {
			logger.debug("Create listener obj to listen to subject: " + subject); 
			m_queue = new TibrvQueue();
			listener = new TibrvListener(m_queue, this, m_transport, subject, null);
		} catch (TibrvException e) {
			logger.error("Failed to create Tibco Listener object to listen to subject: " + subject);
			e.printStackTrace(System.out);
			System.exit(0);
		}
	}

	public void run()
	{
		logger.debug("TibcoListener run...");
		TibrvDispatcher dispatcher = new TibrvDispatcher(m_queue);
		while (true) {
			try {
				dispatcher.join();
			} catch (InterruptedException e) {
				logger.error("Catch Exception during Tibco Dispatch");
				e.printStackTrace(System.out);
			}
		}
	}

	public void onMsg (TibrvListener listener, TibrvMsg msg)
	{
		logger.debug("Receive msg: " + msg);
		clientTibcoMsgQueue.queueMessage(msg);
	}
}

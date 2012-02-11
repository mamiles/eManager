package com.cisco.eManager.eManager.tibco;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.*;

import com.tibco.tibrv.*;

import com.cisco.eManager.common.event.XMLDeliverableMessage;
import com.cisco.eManager.common.event.AbstractEventEvent;
import com.cisco.eManager.common.event.EventType;
import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.event2.NotificationType;

import com.cisco.eManager.eManager.util.GlobalProperties;

public class TibcoManager {

    private static Logger      logger = Logger.getLogger(TibcoManager.class);

    private static TibcoManager tibcoManager = null;
    private TibrvTransport tibcoTransport;

    private static final String TibcoInventoryNotificationSubject = "cisco.mgmt.emanager.inventory.notification";
    private static final String TibcoEventMessageSubject = "cisco.mgmt.emanager.event.notification";
    private static final String TibcoEventFieldName = "Event";

    private TibcoManager() throws TibrvException
    {
        Properties properties;
        String tibcoService;
        String tibcoNetwork;
        String tibcoDaemon;

        properties = GlobalProperties.instance().getProperties();
        tibcoService = properties.getProperty(TibcoGlobals.TibcoServiceKey);
        if (tibcoService == null) {
            tibcoService = TibcoGlobals.TibcoServiceDefault;
            logger.info ("Using the following default tibco service to broadcast event information: " +
			 tibcoService);
        } else {
            logger.info ("Using the following tibco service to broadcast event information: " +
			 tibcoService);
	}

        tibcoNetwork = properties.getProperty(TibcoGlobals.TibcoNetworkKey);
	logger.info ("Using the following tibco network to broadcast event information: " +
		     tibcoNetwork);

        tibcoDaemon = properties.getProperty(TibcoGlobals.TibcoDaemonKey);
        if (tibcoDaemon == null) {
            tibcoDaemon = TibcoGlobals.TibcoDaemonDefault;
            logger.info ("Using the following default tibco daemon to broadcast event information: " +
			 tibcoDaemon);
        } else {
            logger.info ("Using the following tibco daemon to broadcast event information: " +
			 tibcoDaemon);
	}

        try {
            Tibrv.open(Tibrv.IMPL_NATIVE);
            tibcoTransport = new TibrvRvdTransport(tibcoService, tibcoNetwork, tibcoDaemon);
        }
        catch (TibrvException e)
        {
	    tibcoTransport = null;
            logger.fatal ("Fatal error establishing tibrv transport:" +
			  e.getMessage());
	    throw e;
        }
    }

    public static TibcoManager instance() throws TibrvException
    {
        if (tibcoManager == null) {
            tibcoManager = new TibcoManager();
        }

        return tibcoManager;
    }

    public void broadcastInventoryNotification (String xmlMessage)
    {
	TibrvMsg tibcoMsg;

	try {
	    tibcoMsg = new TibrvMsg();
	    tibcoMsg.setSendSubject (TibcoInventoryNotificationSubject);
	    tibcoMsg.add(TibcoEventFieldName, xmlMessage);

	    if (tibcoTransport != null) {
		try {
		    tibcoTransport.send(tibcoMsg);
		}
		catch (TibrvException e) {
		    logger.error ("Error sending Tibrv message: " +
				  e);
		}
	    }
	}
	catch (TibrvException e) {
	    logger.error ("Error creating Tibrv message: " +
			  e);
	}
    }

    public void broadcastMessage (XMLDeliverableMessage notification)
    {
	String xmlMessage;
	TibrvMsg tibcoMsg;

	try {
	    xmlMessage = notification.getXMLMessage();
	    tibcoMsg = new TibrvMsg();
	    tibcoMsg.setSendSubject (TibcoEventMessageSubject);
	    tibcoMsg.add(TibcoEventFieldName, xmlMessage);

	    try {
		if (tibcoTransport != null) {
		    tibcoTransport.send(tibcoMsg);
		}
	    }
	    catch (TibrvException e) {
		logger.error ("Error sending Tibrv message: " +
			      e);
	    }
	}
	catch (EmanagerEventException e) {
	    logger.error("Error sending  Tibrv message: " +
                             e);
	}
	catch (TibrvException e) {
	    logger.error ("Error creating Tibrv message: " +
			  e);
	}
    }

    public void broadcastPostEvents (Collection events)
    {
	broadcastEvents (events, EventType.PostType);
    }

    public void broadcastClearEvents (Collection events)
    {
	broadcastEvents (events, EventType.ClearType);
    }

    private void broadcastEvents (Collection events,
				  EventType eventType)
    {
        Iterator eventIter;
        EmanagerEventDetails event;
        TibrvMsg tibcoMsg;

        eventIter = events.iterator();
        while (eventIter.hasNext()) {
            event = (EmanagerEventDetails) eventIter.next();
            try {
                NotificationType type;
                type = new NotificationType();
		if (eventType.equals (EventType.PostType) == true) {
                    type.setPost(1);
		    tibcoMsg = event.getTibcoBroadcastEventMessage(type);
		} else {
                    type.setClear(1);
		    tibcoMsg = event.getTibcoBroadcastEventMessage(type);
		}

                try {
		    if (tibcoTransport != null) {
			tibcoTransport.send(tibcoMsg);
		    }
                }
                catch (TibrvException e) {
                    logger.error ("Error sending Tibrv message: " +
                                  e.getMessage());
                }
            }
            catch (EmanagerEventException e) {
                logger.error("Error sending  Tibrv message: " +
                             e.getMessage());
            }
        }
    }

    public void sendMessage(String subject, String xmlMessage) {
        String FIELD_NAME = "DATA";
        TibrvMsg tibcoMsg;

        try {
            tibcoMsg = new TibrvMsg();
            tibcoMsg.setSendSubject(subject);
            tibcoMsg.update(FIELD_NAME, xmlMessage);
            logger.debug("Sending msg: " + xmlMessage);

            try {
                if (tibcoTransport != null) {
                    tibcoTransport.send(tibcoMsg);
                }
            }
            catch (TibrvException e) {
                logger.error ("Error sending Tibrv message: " +  e);
            }
        }
        catch (TibrvException e) {
            logger.error ("Error creating Tibrv message: " + e);
        }
    }
}

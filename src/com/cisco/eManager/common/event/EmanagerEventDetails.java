package com.cisco.eManager.common.event;

import org.apache.log4j.*;

import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvException;

import java.io.StringWriter;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.cisco.eManager.common.event.EventAcknowledgement;
import com.cisco.eManager.common.event.EventSeverity;
import com.cisco.eManager.common.event2.EmanagerEventDetailsType;
import com.cisco.eManager.common.event2.EmanagerEventNotificationType;
import com.cisco.eManager.common.event2.NotificationType;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager;

import com.cisco.eManager.eManager.inventory.view.HostViewManager;
import com.cisco.eManager.eManager.inventory.view.Node;

import com.cisco.eManager.eManager.util.GlobalProperties;

import uk.co.westhawk.snmp.pdu.OneTrapPduv2;
import uk.co.westhawk.snmp.stack.SnmpContextv2c;
import uk.co.westhawk.snmp.stack.AsnInteger;
import uk.co.westhawk.snmp.stack.AsnUnsInteger;
import uk.co.westhawk.snmp.stack.AsnOctets;
import uk.co.westhawk.snmp.stack.AsnObjectId;

import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.MarshalException;

public class EmanagerEventDetails
{
    private static Logger      logger = Logger.getLogger(EmanagerEventDetails.class);

    private ManagedObjectId eventObjectId;
    private Date postTime;
    private String displayText;
    private Date clearTime;
    private EventAcknowledgement acknowledgement;
    private ManagedObjectId managedObjectId;
    private EventSeverity severity;

    /**
     * The authoritative identification of the notification currently
     * being sent. This variable occurs as the second varbind in every
     * SNMPv2-Trap-PDU and InformRequest-PDU.
     */
    public static final String SnmpTrapOid = "1.3.6.1.6.3.1.1.4.1.0";
    public static final String EmanagerTrapOid = "1.3.6.1.4.1.9.9.993.2.0.1";
    public static final String EventTypeOid = "1.3.6.1.4.1.9.9.993.1.1";
    public static final String EventTimeOid = "1.3.6.1.4.1.9.9.993.1.2";
    public static final String EventIdOid = "1.3.6.1.4.1.9.9.993.1.3";
    public static final String EventSeverityOid = "1.3.6.1.4.1.9.9.993.1.4";
    public static final String EventObjectOid = "1.3.6.1.4.1.9.9.993.1.5";
    public static final String EventTextOid = "1.3.6.1.4.1.9.9.993.1.6";
    public static final String SysUpTimeOid   = "1.3.6.1.2.1.1.3.0";

    public static final String TibcoEventMessageSubject = "cisco.mgmt.emanager.event.notification";
    public static final String TibcoEventFieldName = "Event";

    // fix
    // Workaround for SOAP
    public EmanagerEventDetails()
    {

    }

    /**
     * @param emanagerEventId
     * @param postTime
     * @param clearTime
     * @param severity
     * @param managedObjectId
     * @param acknowledgement
     * @param displayText
     * @roseuid 3F4ED56402F6
     */
    public EmanagerEventDetails(long emanagerEventId,
				Date postTime,
				Date clearTime,
				EventSeverity severity,
				ManagedObjectId managedObjectId,
				EventAcknowledgement acknowledgement,
				String displayText)
    {
        this.eventObjectId = new ManagedObjectId (ManagedObjectIdType.Event,
                                                  emanagerEventId);
	this.postTime = postTime;
	this.clearTime = clearTime;
	this.severity = severity;
	this.managedObjectId = managedObjectId;
	this.acknowledgement = acknowledgement;
	this.displayText = displayText;
    }

    public ManagedObjectId getObjectId()
    {
        return eventObjectId;
    }

    /**
     * Access method for the postTime property.
     *
     * @return   the current value of the postTime property
     */
    public Date getPostTime()
    {
        return postTime;
    }

    /**
     * Access method for the displayText property.
     *
     * @return   the current value of the displayText property
     */
    public String getDisplayText()
    {
        return displayText;
    }

    /**
     * Access method for the clearTime property.
     *
     * @return   the current value of the clearTime property
     */
    public Date getClearTime()
    {
        return clearTime;
    }

    /**
     * Sets the value of the clearTime property.
     *
     * @param aClearTime the new value of the clearTime property
     */
    public void setClearTime(Date aClearTime)
    {
        clearTime = aClearTime;
    }

    /**
     * Access method for the acknowledgement property.
     *
     * @return   the current value of the acknowledgement property
     */
    public EventAcknowledgement getAcknowledgement()
    {
        return acknowledgement;
    }

    public void setAcknowledgement(EventAcknowledgement ack)
    {
        acknowledgement = ack;
    }

    /**
     * Access method for the managedObjectId property.
     *
     * @return   the current value of the managedObjectId property
     */
    public ManagedObjectId getManagedObjectId()
    {
        return managedObjectId;
    }

    /**
     * Access method for the severity property.
     *
     * @return   the current value of the severity property
     */
    public EventSeverity getSeverity()
    {
        return severity;
    }

    public String getTibcoEventMessageSubject()
    {
	return TibcoEventMessageSubject;
    }

    public String getTibcoEventFieldName()
    {
	return TibcoEventFieldName;
    }

    protected EmanagerEventNotificationType populateNotificationObject (NotificationType type,
									EmanagerEventNotificationType event)
    {
	com.cisco.eManager.common.event2.EventId transportEventId;
	com.cisco.eManager.common.event2.Severity transportSeverity;
	com.cisco.eManager.common.event2.ManagedObjectId transportManagedObjectId;

	transportEventId = new com.cisco.eManager.common.event2.EventId();
	transportSeverity = new com.cisco.eManager.common.event2.Severity();
	transportManagedObjectId = new com.cisco.eManager.common.event2.ManagedObjectId();

	getObjectId().populateEventTransportObject (transportEventId);
	getSeverity().populateTransportObject (transportSeverity);
	getManagedObjectId().populateEventTransportObject (transportManagedObjectId);

	event.setNotificationType (type);
	event.setEventId (transportEventId);
	event.setSeverity (transportSeverity);
	event.setManagedObjectId (transportManagedObjectId);
	event.setDisplayText (getDisplayText());
	if (type.hasPost()) {
	    event.setTime (getPostTime());
	} else {
	    event.setTime (getClearTime());
	}

        return event;
    }

    public TibrvMsg getTibcoBroadcastEventMessage(NotificationType type) throws EmanagerEventException
    {
	TibrvMsg tibcoMsg;
        String objectPath;
	StringWriter stringWriter;
	com.cisco.eManager.common.event2.EventNotification eventNotification;
	com.cisco.eManager.common.event2.EventNotificationMsg eventNotificationMsg;
	com.cisco.eManager.common.event2.EmanagerEventNotification emanagerEventNotification;

	stringWriter = new StringWriter();

	emanagerEventNotification = new com.cisco.eManager.common.event2.EmanagerEventNotification();
	eventNotification = new com.cisco.eManager.common.event2.EventNotification();
	eventNotificationMsg = new com.cisco.eManager.common.event2.EventNotificationMsg();

	eventNotificationMsg.setEventAcknowledgementNotification(null);
	eventNotificationMsg.setEventUnacknowledgementNotification(null);
	eventNotificationMsg.setEventNotification (eventNotification);

	eventNotification.setEmanagerEventNotification (emanagerEventNotification);
	eventNotification.setTibcoEventNotification (null);
	eventNotification.setProcessSequencerEventNotification (null);

	populateNotificationObject (type, emanagerEventNotification);

	try {
	    eventNotificationMsg.marshal (stringWriter);

	    tibcoMsg = new TibrvMsg();
	    tibcoMsg.setSendSubject(getTibcoEventMessageSubject());
	    tibcoMsg.add(getTibcoEventFieldName(), stringWriter.toString());
	}
	catch (TibrvException e) {
	    EmanagerEventException exception;

	    exception =
		new EmanagerEventException (EmanagerEventStatusCode.TibrvMessageCreationError,
					    EmanagerEventStatusCode.TibrvMessageCreationError.getStatusCodeDescription() +
					    "  " +
					    e.getMessage());
	    throw exception;
	}
	catch (ValidationException e) {
	    EmanagerEventException exception;

	    exception =
		new EmanagerEventException (EmanagerEventStatusCode.TibrvMessageCreationError,
					    EmanagerEventStatusCode.TibrvMessageCreationError.getStatusCodeDescription() +
					    "  " +
					    e.getMessage());
	    throw exception;
	}
	catch (MarshalException e) {
	    EmanagerEventException exception;

	    exception =
		new EmanagerEventException (EmanagerEventStatusCode.TibrvMessageCreationError,
					    EmanagerEventStatusCode.TibrvMessageCreationError.getStatusCodeDescription() +
					    "  " +
					    e.getMessage());
	    throw exception;
	}

	return tibcoMsg;
    }

    public OneTrapPduv2 getPostTrapEventPdu(SnmpContextv2c context)
    {
	OneTrapPduv2 trapPdu;
        Calendar eventTime;
        StringBuffer eventDateAndTime;
        StringBuffer stringBuffer;
        String objectPath;
        Node appInstanceNode;
        byte dateBytes[];
        Integer intValue;

        dateBytes = new byte[8];
        eventTime = new GregorianCalendar();
        eventTime.setTime(getPostTime());
        intValue = new Integer (eventTime.get(Calendar.YEAR));
        dateBytes[0] = 0;
        dateBytes[1] = intValue.byteValue();
        intValue = new Integer(eventTime.get(Calendar.MONTH));
        dateBytes[2] = intValue.byteValue();
        intValue = new Integer (eventTime.get(Calendar.DAY_OF_MONTH));
        dateBytes[3] = intValue.byteValue();
        intValue = new Integer (eventTime.get(Calendar.HOUR));
        dateBytes[4] = intValue.byteValue();
        intValue = new Integer (eventTime.get(Calendar.MINUTE));
        dateBytes[5] = intValue.byteValue();
        intValue = new Integer (eventTime.get(Calendar.SECOND));
        dateBytes[6] = intValue.byteValue();
        intValue = new Integer (eventTime.get(Calendar.MILLISECOND)/100);
        dateBytes[7] = intValue.byteValue();

        // Need to get path
        try {
            appInstanceNode = HostViewManager.instance().find(getManagedObjectId());
        }
        catch (Exception e) {
            appInstanceNode = null;
            logger.error("Unexpected excpeption retrieving event's associated physical node: " + e);
        }

        if (appInstanceNode != null) {
            objectPath = appInstanceNode.fdn();
            if (objectPath == null) {
                AppInstance appInstance;

                appInstance = AppInstanceManager.instance().find(getManagedObjectId());
                if (appInstance != null) {
                    objectPath = appInstance.name();
                } else {
                    objectPath = "Undefined";
                }
            }
        } else {
            AppInstance appInstance;

            appInstance = AppInstanceManager.instance().find(getManagedObjectId());
            if (appInstance != null) {
                objectPath = appInstance.name();
            } else {
                objectPath = "Undefined";
            }
        }

	trapPdu = new OneTrapPduv2 (context);
        long sysuptime = GlobalProperties.getSystemUptime() * 100;
        trapPdu.addOid(SysUpTimeOid, new AsnUnsInteger(GlobalProperties.getSystemUptime() * 100) );
        trapPdu.addOid(SnmpTrapOid, new AsnObjectId(EmanagerTrapOid));
        trapPdu.addOid(EventTypeOid, new AsnInteger (EventType.PostType.intValue()));
        trapPdu.addOid(EventTimeOid, new AsnOctets (dateBytes));
        trapPdu.addOid(EventIdOid, new AsnUnsInteger (getObjectId().getManagedObjectKey()));
        trapPdu.addOid(EventSeverityOid, new AsnInteger (getSeverity().intValue()));
        trapPdu.addOid(EventObjectOid, new AsnOctets (objectPath));
        trapPdu.addOid(EventTextOid, new AsnOctets (getDisplayText()));

	return trapPdu;
    }

    public OneTrapPduv2 getClearTrapEventPdu(SnmpContextv2c context)
    {
	Date clearTime;
	OneTrapPduv2 trapPdu;
        Calendar eventTime;
        String objectPath;
        byte dateBytes[];
        Integer intValue;

	clearTime = getClearTime();
	dateBytes = new byte[8];
	if (clearTime != null) {
	    eventTime = new GregorianCalendar();
	    eventTime.setTime(clearTime);
	    intValue = new Integer (eventTime.get(Calendar.YEAR));
	    dateBytes[0] = 0;
	    dateBytes[1] = intValue.byteValue();
	    intValue = new Integer(eventTime.get(Calendar.MONTH));
	    dateBytes[2] = intValue.byteValue();
	    intValue = new Integer (eventTime.get(Calendar.DAY_OF_MONTH));
	    dateBytes[3] = intValue.byteValue();
	    intValue = new Integer (eventTime.get(Calendar.HOUR));
	    dateBytes[4] = intValue.byteValue();
	    intValue = new Integer (eventTime.get(Calendar.MINUTE));
	    dateBytes[5] = intValue.byteValue();
	    intValue = new Integer (eventTime.get(Calendar.SECOND));
	    dateBytes[6] = intValue.byteValue();
	    intValue = new Integer (eventTime.get(Calendar.MILLISECOND)/100);
	    dateBytes[7] = intValue.byteValue();
	} else {
	    for (int i = 0; i < 8; i++) {
		dateBytes[i] = 0;
	    }
	}

        AppInstance appInstance;
        appInstance = AppInstanceManager.instance().find (getManagedObjectId());
        if (appInstance != null) {
	    Node node;
	    
	    node = null;
	    try {
		node = HostViewManager.instance().find (getManagedObjectId());
	    }
	    catch (Exception e) {
		node = null;
	    }

	    if (node != null) {
		objectPath = node.fdn();
	    } else {
		objectPath = appInstance.name();
	    }
        } else {
            objectPath = new String ("Undefined");
        }

	trapPdu = new OneTrapPduv2 (context);
        long sysuptime = GlobalProperties.getSystemUptime() * 100;
        trapPdu.addOid(SysUpTimeOid, new AsnUnsInteger(GlobalProperties.getSystemUptime() * 100));
        trapPdu.addOid(SnmpTrapOid, new AsnObjectId(EmanagerTrapOid));
        trapPdu.addOid(EventTypeOid, new AsnInteger (EventType.ClearType.intValue()));
        trapPdu.addOid(EventTimeOid, new AsnOctets (dateBytes));
        trapPdu.addOid(EventIdOid, new AsnUnsInteger (getObjectId().getManagedObjectKey()));
        trapPdu.addOid(EventSeverityOid, new AsnInteger (getSeverity().intValue()));
        trapPdu.addOid(EventObjectOid, new AsnOctets (objectPath));
        trapPdu.addOid(EventTextOid, new AsnOctets (getDisplayText()));

	return trapPdu;
    }

    public String toString()
    {
        String postTimeString;
        String clearTimeString;
        SimpleDateFormat dateFormat;

        dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.SSS");

        if (postTime != null) {
            postTimeString = dateFormat.format(postTime);
        } else {
            postTimeString = "null";
        }

        if (clearTime != null) {
            clearTimeString = dateFormat.format(clearTime);
        } else {
            clearTimeString = "null";
        }

        return "eventObjectId=" + eventObjectId + ";" +
            "postTime=" + postTimeString + ";" +
            "clearTime=" + clearTimeString + ";" +
            "severity=" + severity + ";" +
            "managedObjectId=" + managedObjectId + ";" +
            "acknowledgement=" + acknowledgement + ";" +
            "displayText=" + displayText + ";";
    }

    public EmanagerEventDetailsType populateTransportObject(EmanagerEventDetailsType object)
    {
	com.cisco.eManager.common.event2.EventId objectEventId;
	com.cisco.eManager.common.event2.Severity objectSeverity;
	com.cisco.eManager.common.event2.ManagedObjectId objectMOID;
	com.cisco.eManager.common.event2.Acknowledgement objectAck;

	objectEventId = new com.cisco.eManager.common.event2.EventId();
	objectSeverity = new com.cisco.eManager.common.event2.Severity ();
	objectMOID = new com.cisco.eManager.common.event2.ManagedObjectId();
	objectAck = new com.cisco.eManager.common.event2.Acknowledgement();

	getObjectId().populateEventTransportObject (objectEventId);
	object.setEventId (objectEventId);

	object.setPostTime (getPostTime());
	object.setClearTime (getClearTime());

	getSeverity().populateTransportObject (objectSeverity);
	object.setSeverity (objectSeverity);

	object.setDisplayText (getDisplayText());

	getManagedObjectId().populateEventTransportObject (objectMOID);
	object.setManagedObjectId (objectMOID);

	if (getAcknowledgement() == null) {
	    object.setAcknowledgement (null);
	} else {
	    getAcknowledgement().populateTransportObject(objectAck);
	    object.setAcknowledgement (objectAck);
	}

	return object;
    }
}

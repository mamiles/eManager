//Source file: /vob/emanager/src/com/cisco/eManager/common/event/EmanagerEventMessage.java

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\common\\event\\EmanagerEventMessage.java

package com.cisco.eManager.common.event;

import java.util.Date;

import com.cisco.eManager.common.event.EventAcknowledgement;
import com.cisco.eManager.common.event.EventSeverity;
import com.cisco.eManager.common.event.EventType;
import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.common.inventory.ManagedObjectId;

import com.cisco.eManager.eManager.util.GlobalProperties;

import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;

import com.tibco.tibrv.*;

public class EmanagerEventMessage extends AbstractEventMessage implements Comparable
{
    private long emanagerEventId;
    private ManagedObjectId managedObjectId;

    // fix
    // Workaround for SOAP
    public EmanagerEventMessage()
    {
        super (EventType.PostType, new Date(), EventSeverity.Informational, new String (""));
    }

    public EmanagerEventMessage (EmanagerEventMessage object)
    {
        super (object);

        emanagerEventId = object.emanagerEventId;
        managedObjectId = object.managedObjectId;
    }

    /**
     * @param eventType
     * @param managedObjectId
     * @param eventTime
     * @param emanagerEventId
     * @param severity
     * @param displayText
     * @roseuid 3F4ED66602A7
     */
    public EmanagerEventMessage(EventType eventType,
				ManagedObjectId managedObjectId,
				Date eventTime,
				long emanagerEventId,
				EventSeverity severity,
				String displayText)
    {
        super (eventType, eventTime, severity, displayText);

	this.managedObjectId = managedObjectId;
	this.emanagerEventId = emanagerEventId;
    }

    /**
     * Access method for the emanagerEventId property.
     *
     * @return   the current value of the emanagerEventId property
     */
    public long getEmanagerEventId()
    {
        return emanagerEventId;
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
     * Sets the value of the managedObjectId property.
     *
     * @param aManagedObjectId the new value of the managedObjectId property
     */
    public void setManagedObjectId(ManagedObjectId aManagedObjectId)
    {
        managedObjectId = aManagedObjectId;
    }

    public int compareTo (Object object)
    {
	int value;

	value = super.compareTo (object);
	if (value == 0) {
	    if (object instanceof EmanagerEventMessage) {
		if (emanagerEventId < ( (EmanagerEventMessage) object).getEmanagerEventId()) {
		    return -1;
		} else if (emanagerEventId > ( (EmanagerEventMessage) object).getEmanagerEventId()) {
		    return 1;
		} else {
		    return 0;
		}
	    } else {
		return -1;
	    }
	}

	return value;
    }

    public boolean equals (Object object)
    {
        if (object instanceof EmanagerEventMessage) {
            if (super.equals(object)) {
                EmanagerEventMessage eObject;

                eObject = (EmanagerEventMessage) object;
                if (emanagerEventId == eObject.emanagerEventId &&
                    managedObjectId.equals(eObject.managedObjectId)) {
                    return true;
                }
            }
        }

        return false;
    }
}

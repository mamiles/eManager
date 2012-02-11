//Source file: /vob/emanager/src/com/cisco/eManager/common/event/ProcessSequencerEventMessage.java

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\common\\event\\ProcessSequencerEventMessage.java

package com.cisco.eManager.common.event;

import java.util.Date;

import com.cisco.eManager.common.event.EmanagerEventMessage;

import com.cisco.eManager.common.event.EventSeverity;
import com.cisco.eManager.common.event.EventType;
import com.cisco.eManager.common.event.ProcessSequencerEventId;
import com.cisco.eManager.common.inventory.ManagedObjectId;

public class ProcessSequencerEventMessage extends AbstractEventMessage implements Comparable
{
    private ProcessSequencerEventId processSequencerEventId;
    private ManagedObjectId managedObjectId;

    // fix
    // workaround for SOAP
    public ProcessSequencerEventMessage()
    {
        super (EventType.ClearType, new Date(), EventSeverity.Low, new String (""));
    }

    public ProcessSequencerEventMessage (ProcessSequencerEventMessage object)
    {
        super (object);

        processSequencerEventId = object.processSequencerEventId;
        managedObjectId = object.managedObjectId;
    }

    /**
     * @param eventType
     * @param eventTime
     * @param severity
     * @param managedObjectId
     * @param processSequencerEventId
     * @param displayText
     * @roseuid 3F4E5D48002E
     */
    public ProcessSequencerEventMessage(EventType eventType,
					Date eventTime,
					EventSeverity severity,
					ManagedObjectId managedObjectId,
					ProcessSequencerEventId processSequencerEventId,
					String displayText)
    {
	super (eventType, eventTime, severity, displayText);

	this.managedObjectId = managedObjectId;
	this.processSequencerEventId = processSequencerEventId;
    }

    /**
     * Access method for the processSequencerEventId property.
     *
     * @return   the current value of the processSequencerEventId property
     */
    public ProcessSequencerEventId getProcessSequencerEventId()
    {
        return processSequencerEventId;
    }

    /**
     * Access method for the managedObject property.
     *
     * @return   the current value of the managedObject property
     */
    public ManagedObjectId getManagedObjectId()
    {
        return managedObjectId;
    }

    public int compareTo (Object object)
    {
	int value;

	value = super.compareTo (object);
	if (value == 0) {
	    if (object instanceof ProcessSequencerEventMessage) {
		return processSequencerEventId.
		    compareTo ( ( (ProcessSequencerEventMessage) object).getProcessSequencerEventId());
	    } else if (object instanceof EmanagerEventMessage) {
		return 1;
	    } else {
		return -1;
	    }
	}

	return value;
    }

    public boolean equals (Object object)
    {
        if (object instanceof ProcessSequencerEventMessage) {
            if (super.equals(object)) {
                ProcessSequencerEventMessage psObject;
                psObject = (ProcessSequencerEventMessage) object;
                if (processSequencerEventId.equals(psObject.processSequencerEventId) &&
                    managedObjectId.equals(psObject.managedObjectId)) {
                    return true;
                }
            }
        }

        return false;
    }
}

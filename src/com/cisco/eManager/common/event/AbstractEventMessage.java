package com.cisco.eManager.common.event;

import java.util.Date;

import com.cisco.eManager.common.event.EventType;
import com.cisco.eManager.common.event.EventSeverity;

public abstract class AbstractEventMessage implements Comparable
{
    private String displayText;
    private Date eventTime;
    private EventType eventType;
    private EventSeverity severity;
    private boolean syncGenerated;

    public AbstractEventMessage (AbstractEventMessage object)
    {
        eventType = object.eventType;
        eventTime = object.eventTime;
        displayText = object.displayText;
        severity = object.severity;
        syncGenerated = object.syncGenerated;
    }

    /**
     * @param eventType
     * @param eventTime
     * @param severity
     * @param displayText
     * @roseuid 3F4E5CF8012A
     */
    public AbstractEventMessage(EventType eventType,
				Date eventTime,
				EventSeverity severity,
				String displayText)
    {
	this.eventType = eventType;
	this.eventTime = eventTime;
	this.severity = severity;
	this.displayText = displayText;
        this.syncGenerated = false;
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
     * Access method for the eventTime property.
     *
     * @return   the current value of the eventTime property
     */
    public Date getEventTime()
    {
        return eventTime;
    }

    public void setEventTime(Date eventTime)
    {
        this.eventTime = eventTime;
    }

    /**
     * Access method for the eventType property.
     *
     * @return   the current value of the eventType property
     */
    public EventType getEventType()
    {
        return eventType;
    }

    public void setEventType(EventType eventType)
    {
        this.eventType = eventType;
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

    public boolean getSyncGenerated ()
    {
        return syncGenerated;
    }

    public void setSyncGenerated (boolean syncGenerated)
    {
        this.syncGenerated = syncGenerated;
    }

    public int compareTo (Object object)
    {
	boolean baseClassFound;
	Class objectClasses[];


	objectClasses = object.getClass().getClasses();

	baseClassFound = false;
	for (int i = 0; i < objectClasses.length; i++) {
	    if (this.getClass().equals (objectClasses[i])) {
		baseClassFound = true;
		break;
	    }
	}

	if (baseClassFound == true) {
            return getEventTime().compareTo( ( (AbstractEventMessage)object).getEventTime());
        }

        return 0;
    }

    public boolean equals (Object object)
    {
        if (object instanceof AbstractEventMessage) {
            AbstractEventMessage aObject;

            aObject = (AbstractEventMessage) object;
            if (displayText.equals(aObject.displayText) &&
                eventTime.equals(aObject.eventTime) &&
                eventType.equals(aObject.eventType) &&
                severity.equals(aObject.severity))
                return true;
        }

        return false;
    }
}

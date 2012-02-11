package com.cisco.eManager.common.event;

import com.cisco.eManager.common.event2.EventSeverityType;

public class EventSeverity
{
    private int eventSeverity;
    public static final EventSeverity Informational = new EventSeverity (0);
    public static final EventSeverity Low = new EventSeverity (1);
    public static final EventSeverity Medium = new EventSeverity (2);
    public static final EventSeverity High = new EventSeverity (3);

    // fix
    // fix. remove this.  here for soap debug only
    public EventSeverity ()
    {
	eventSeverity = EventSeverity.Low.intValue();
    }

    /**
     * @param eventSeverity
     * @roseuid 3F4EDA0A01E9
     */
    private EventSeverity(int eventSeverity)
    {
	this.eventSeverity = eventSeverity;
    }

    public static EventSeverity getSeverity (EventSeverityType event)
    {
	if (event.hasInformational()) {
	    return EventSeverity.Informational;
	} else if (event.hasLow()) {
	    return EventSeverity.Low;
	} else if (event.hasMedium()) {
	    return EventSeverity.Medium;
	}

	return EventSeverity.High;
    }


    /**
     * Access method for the eventSeverity property.
     *
     * @return   the current value of the eventSeverity property
     */
    public int intValue()
    {
        return eventSeverity;
    }

    public boolean equals (Object object)
    {
        if (object instanceof EventSeverity) {
            if ( ( (EventSeverity) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        if (eventSeverity == EventSeverity.Informational.intValue()) {
            return "Informational";
        } else if (eventSeverity == EventSeverity.Low.intValue()) {
            return "Low";
        } else if (eventSeverity == EventSeverity.Medium.intValue()) {
            return "Medium";
        } else if (eventSeverity == EventSeverity.High.intValue()) {
            return "High";
        }

        return "Unknown";
    }

    public EventSeverityType populateTransportObject (EventSeverityType tobject)
    {
	if (this == EventSeverity.Informational) {
	    tobject.setInformational (1);
	} else if (this == EventSeverity.Low) {
	    tobject.setLow (1);
	} else if (this == EventSeverity.Medium) {
	    tobject.setMedium (1);
	} else {
	    tobject.setHigh (1);
	}

	return tobject;
    }
}

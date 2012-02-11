//Source file: /vob/emanager/src/com/cisco/eManager/common/event/EventType.java

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\common\\event\\EventType.java

package com.cisco.eManager.common.event;


public class EventType
{
    private int eventType;
    public static final EventType PostType = new EventType (1);
    public static final EventType RetransmitType = new EventType (0);
    public static final EventType ClearType = new EventType (2);

    // fix
    // fix. remove this.  here for soap debug only
    public EventType ()
    {
	eventType = EventType.PostType.intValue();
    }

    private EventType (int eventTypeValue)
    {
        this.eventType = eventTypeValue;
    }

    public int intValue()
    {
        return eventType;
    }

    public boolean equals (Object object)
    {
        if (object instanceof EventType) {
            if ( ( (EventType) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        if (this.equals(EventType.ClearType)) {
            return "Clear";
        } else if (this.equals (EventType.PostType)) {
            return "Post";
        } else {
            return "Retransmit";
        }
    }
}

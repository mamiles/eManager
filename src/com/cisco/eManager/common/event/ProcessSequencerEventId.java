package com.cisco.eManager.common.event;

import com.cisco.eManager.common.event2.ProcessSequencerEventIdType;

public class ProcessSequencerEventId implements Comparable
{
    private int processSequencerId;
    private int eventId;

    // fix
    // Workaround for SOAP
    public ProcessSequencerEventId()
    {

    }
    /**
     * @param processSequencerId
     * @param processSequencerEventId
     * @roseuid 3F4E5D440385
     */
    public ProcessSequencerEventId(int processSequencerId, int eventId)
    {
	this.processSequencerId = processSequencerId;
	this.eventId = eventId;
    }

    /**
     * Access method for the processSequencerId property.
     *
     * @return   the current value of the processSequencerId property
     */
    public int getProcessSequencerId()
    {
        return processSequencerId;
    }

    /**
     * Access method for the processSequencerEventId property.
     *
     * @return   the current value of the processSequencerEventId property
     */
    public int getEventId()
    {
        return eventId;
    }

    public int compareTo (Object object)
    {
	if (object instanceof ProcessSequencerEventId) {
	    ProcessSequencerEventId psei;

	    psei = (ProcessSequencerEventId) object;
	    if (processSequencerId > psei.processSequencerId) {
		return 1;
	    } else if (processSequencerId < psei.processSequencerId) {
		return -1;
	    } else if (eventId > psei.eventId) {
		return 1;
	    } else if (eventId < psei.eventId) {
		return -1;
	    }
	}

	return 0;
    }

    public ProcessSequencerEventIdType populateTransportObject (ProcessSequencerEventIdType object)
    {
	object.setProcessSequencerId (getProcessSequencerId());
	object.setProcessSequencerEventId (getEventId());

	return object;
    }
}

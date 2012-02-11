package com.cisco.eManager.common.event;

import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.NumericSearchCriteria;

import com.cisco.eManager.common.event2.AbstractProcessSequencerSearchCriteriaType;

public class AbstractProcessSequencerEventSearchCriteria
{
    protected NumericSearchCriteria processSequencerEventId;
    protected Integer processSequencerId;

    /**
     * @roseuid 3F550F980292
     */
    public AbstractProcessSequencerEventSearchCriteria()
    {
	processSequencerEventId = null;
	processSequencerId = null;
    }

    /**
     * @param processSequencerEventId
     * @param processSequencerId
     * @roseuid 3F4E5D04005F
     */
    public AbstractProcessSequencerEventSearchCriteria(NumericSearchCriteria processSequencerEventId, Integer processSequencerId)
    {
	this.processSequencerEventId = processSequencerEventId;
	this.processSequencerId = processSequencerId;
    }

    public AbstractProcessSequencerEventSearchCriteria (AbstractProcessSequencerSearchCriteriaType criteria) throws EmanagerEventException
    {
	if (criteria.getPsEventIdSearchCriteria() != null) {
	    processSequencerEventId = new NumericSearchCriteria (criteria.getPsEventIdSearchCriteria());
	} else {
	    processSequencerEventId = null;
	}

	if (criteria.hasPsId()) {
	    processSequencerId = new Integer (criteria.getPsId());
	} else {
	    processSequencerId = null;
	}
    }

    /**
     * Access method for the processSequencerEventId property.
     *
     * @return   the current value of the processSequencerEventId property
     */
    public NumericSearchCriteria getProcessSequencerEventId()
    {
        return processSequencerEventId;
    }

    /**
     * Sets the value of the postDate property.
     *
     * @param aNumericSearchCriteria  the new value of the processSequencerEventId property
     */
    public void setProcessSequencerEventId(NumericSearchCriteria aNumericSearchCriteria)
    {
        processSequencerEventId = aNumericSearchCriteria;
    }

    /**
     * Access method for the processSequencerId property.
     *
     * @return   the current value of the processSequencerId property
     */
    public Integer getProcessSequencerId()
    {
        return processSequencerId;
    }

    /**
     * Sets the value of the processSequencerId property.
     *
     * @param aInteger  the new value of the processSequencerId property
     */
    public void getProcessSequencerId(Integer aInteger)
    {
       processSequencerId = aInteger;
    }

    public String toString()
    {
	StringBuffer string;

        string = new StringBuffer();

	if (processSequencerEventId != null) {
	    string.append ("ProcessSequencerId:" + processSequencerEventId.toString() + "  ");
	}

	if (processSequencerId != null) {
	    string.append ("ProcessSequencer:" + processSequencerId.toString());
	}

	return string.toString();
    }
}

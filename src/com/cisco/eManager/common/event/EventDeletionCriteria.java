package com.cisco.eManager.common.event;

import com.cisco.eManager.common.event.AbstractSearchCriteria;
import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.event2.EventDeletionCriteriaType;

import com.cisco.eManager.common.inventory.EmanagerInventoryException;

public class EventDeletionCriteria extends AbstractSearchCriteria
{
    private TibcoEventDeletionCriteria tibcoDeletionCriteria = null;
    private ProcessSequencerEventDeletionCriteria processSequencerDeletionCriteria = null;

    /**
     * @roseuid 3F4E5D2D0060
     */
    public EventDeletionCriteria()
    {

    }

    /**
     * @param tibcoDeletionCriteria sets the tibcoDeletionCriteria property
     * @param processSequencerDelectionCriteria sets the processSequencerDelectionCriteria property
     */
    public EventDeletionCriteria(TibcoEventDeletionCriteria tibcoDeletionCriteria,
				 ProcessSequencerEventDeletionCriteria processSequencerDelectionCriteria)
    {
	this.tibcoDeletionCriteria = tibcoDeletionCriteria;
	this.processSequencerDeletionCriteria = processSequencerDelectionCriteria;
    }

    public EventDeletionCriteria (EventDeletionCriteriaType deleteCriteria) throws EmanagerEventException,
    EmanagerInventoryException
    {
	super (deleteCriteria);

	if (deleteCriteria.getTibcoDeletionCriteria() != null) {
	    tibcoDeletionCriteria = new TibcoEventDeletionCriteria (deleteCriteria.getTibcoDeletionCriteria());
	} else {
	    tibcoDeletionCriteria = null;
	}

	if (deleteCriteria.getProcessSequencerCriteria() != null) {
	    processSequencerDeletionCriteria = new ProcessSequencerEventDeletionCriteria(deleteCriteria.getProcessSequencerCriteria());
	} else {
	    processSequencerDeletionCriteria = null;
	}
    }

    /**
     * Access method for the tibcoDeletionCriteria property.
     *
     * @return   the current value of the tibcoDeletionCriteria property
     */
    public TibcoEventDeletionCriteria getTibcoDeletionCriteria()
    {
        return tibcoDeletionCriteria;
    }

    /**
     * Sets the value of the tibcoDeletionCriteria property.
     *
     * @param aTibcoDeletionCriteria the new value of the tibcoDeletionCriteria property
     */
    public void setTibcoDeletionCriteria(TibcoEventDeletionCriteria aTibcoDeletionCriteria)
    {
        tibcoDeletionCriteria = aTibcoDeletionCriteria;
    }

    /**
     * Access method for the processSequencerDelectionCriteria property.
     *
     * @return   the current value of the processSequencerDelectionCriteria property
     */
    public ProcessSequencerEventDeletionCriteria getProcessSequencerDeletionCriteria()
    {
        return processSequencerDeletionCriteria;
    }

    /**
     * Sets the value of the processSequencerDelectionCriteria property.
     *
     * @param aProcessSequencerDelectionCriteria the new value of the processSequencerDelectionCriteria property
     */
    public void setProcessSequencerDelectionCriteria(ProcessSequencerEventDeletionCriteria aProcessSequencerDeletionCriteria)
    {
        processSequencerDeletionCriteria = aProcessSequencerDeletionCriteria;
    }

    public String toString()
    {
	StringBuffer string;

	string = new StringBuffer();

	string.append (super.toString());

	if (tibcoDeletionCriteria != null) {
	    string.append ("  " + tibcoDeletionCriteria.toString());
	}

	if (processSequencerDeletionCriteria != null) {
	    string.append ("  " + processSequencerDeletionCriteria.toString());
	}

	return string.toString();
    }
}

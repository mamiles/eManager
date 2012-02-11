package com.cisco.eManager.common.event;

import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.event2.EventSearchCriteriaType;

import com.cisco.eManager.common.inventory.EmanagerInventoryException;

public class EventSearchCriteria extends AbstractSearchCriteria
{
    private TibcoEventSearchCriteria tibcoSearchCriteria = null;
    private ProcessSequencerEventSearchCriteria processSequencerSearchCriteria = null;

    /**
     * @roseuid 3F4E5D3001B9
     */
    public EventSearchCriteria()
    {

    }

    public EventSearchCriteria (EventSearchCriteriaType transportSearchCriteria) throws EmanagerEventException,
        EmanagerInventoryException
    {
	super (transportSearchCriteria);

	if (transportSearchCriteria.getTibcoSearchCriteria() != null) {
	    tibcoSearchCriteria = new TibcoEventSearchCriteria (transportSearchCriteria.getTibcoSearchCriteria());
	} else {
	    tibcoSearchCriteria = null;
	}

	if (transportSearchCriteria.getProcessSequencerSearchCriteria() != null) {
	    processSequencerSearchCriteria =
		new ProcessSequencerEventSearchCriteria(transportSearchCriteria.getProcessSequencerSearchCriteria());
	} else {
	    processSequencerSearchCriteria = null;
	}
    }

    /**
     * Access method for the tibcoSearchCriteria property.
     *
     * @return   the current value of the tibcoSearchCriteria property
     */
    public TibcoEventSearchCriteria getTibcoSearchCriteria()
    {
        return tibcoSearchCriteria;
    }

    /**
     * Sets the value of the tibcoSearchCriteria property.
     *
     * @param aTibcoSearchCriteria the new value of the tibcoSearchCriteria property
     */
    public void setTibcoSearchCriteria(TibcoEventSearchCriteria aTibcoSearchCriteria)
    {
        tibcoSearchCriteria = aTibcoSearchCriteria;
    }

    /**
     * Access method for the processSequencerSearchCriteria property.
     *
     * @return   the current value of the processSequencerSearchCriteria property
     */
    public ProcessSequencerEventSearchCriteria getProcessSequencerSearchCriteria()
    {
        return processSequencerSearchCriteria;
    }

    /**
     * Sets the value of the processSequencerSearchCriteria property.
     *
     * @param aProcessSequencerSearchCriteria the new value of the processSequencerSearchCriteria property
     */
    public void setProcessSequencerSearchCriteria(ProcessSequencerEventSearchCriteria aProcessSequencerSearchCriteria)
    {
        processSequencerSearchCriteria = aProcessSequencerSearchCriteria;
    }
}

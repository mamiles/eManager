package com.cisco.eManager.common.event;

import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.event2.TibcoEventDeletionCriteriaType;

import com.cisco.eManager.common.inventory.EmanagerInventoryException;

public class TibcoEventDeletionCriteria extends AbstractTibcoEventSearchCriteria
{

    /**
     * @roseuid 3F4E5D530355
     */
    public TibcoEventDeletionCriteria()
    {

    }

    public TibcoEventDeletionCriteria(TibcoEventDeletionCriteriaType deleteCriteria) throws EmanagerEventException,
    EmanagerInventoryException
    {
	super (deleteCriteria);
    }
}

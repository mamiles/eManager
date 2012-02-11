package com.cisco.eManager.common.event;

import com.cisco.eManager.common.event.AbstractTibcoEventSearchCriteria;
import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.event2.TibcoEventSearchCriteriaType;

import com.cisco.eManager.common.inventory.EmanagerInventoryException;

public class TibcoEventSearchCriteria extends AbstractTibcoEventSearchCriteria
{

    /**
     * @roseuid 3F4E5D590296
     */
    public TibcoEventSearchCriteria()
    {

    }

    public TibcoEventSearchCriteria(TibcoEventSearchCriteriaType criteria) throws EmanagerEventException,
    EmanagerInventoryException
    {
	super (criteria);
    }
}

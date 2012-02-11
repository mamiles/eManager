package com.cisco.eManager.common.event;

import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.event2.ProcessSequencerEventSearchCriteriaType;

public class ProcessSequencerEventSearchCriteria extends AbstractProcessSequencerEventSearchCriteria
{

    /**
     * @roseuid 3F4E5D4A03DE
     */
    public ProcessSequencerEventSearchCriteria()
    {

    }

    public ProcessSequencerEventSearchCriteria(ProcessSequencerEventSearchCriteriaType criteria) throws EmanagerEventException
    {
	super (criteria);
    }
}

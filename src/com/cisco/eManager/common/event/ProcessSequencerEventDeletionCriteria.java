package com.cisco.eManager.common.event;

import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.event2.ProcessSequencerEventDeletionCriteriaType;

public class ProcessSequencerEventDeletionCriteria extends AbstractProcessSequencerEventSearchCriteria 
{
    
    /**
     * @roseuid 3F4E5D3F00AD
     */
    public ProcessSequencerEventDeletionCriteria() 
    {
     
    }

    public ProcessSequencerEventDeletionCriteria (ProcessSequencerEventDeletionCriteriaType deleteCriteria) throws EmanagerEventException
    {
	super (deleteCriteria);
    }
}

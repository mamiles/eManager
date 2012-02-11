package com.cisco.eManager.common.audit;

import org.apache.log4j.*;

import java.util.Date;

import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.audit2.DateSearchCriteriaType;

public class DateSearchCriteria extends com.cisco.eManager.common.event.DateSearchCriteria
{
    private static Logger      logger = Logger.getLogger(DateSearchCriteria.class);

    /**
     * @param startDate
     * @param endDate
     * @roseuid 3F4ECD8A03A0
     */
    public DateSearchCriteria(Date startDate, Date endDate) throws EmanagerEventException
    {
	super (startDate, endDate);
    }

    public DateSearchCriteria(DateSearchCriteriaType criteria) throws EmanagerEventException
    {
        super (criteria);
    }

    // fix
    // take this away: null/null illegal
    /**
     */
    public DateSearchCriteria()
    {
	super ();
    }
}

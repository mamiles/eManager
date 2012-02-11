package com.cisco.eManager.common.event;

import org.apache.log4j.*;

import java.util.Date;

import java.text.SimpleDateFormat;

import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.common.event2.DateSearchCriteriaType;

public class DateSearchCriteria
{
    private static Logger      logger = Logger.getLogger(DateSearchCriteria.class);

    private Date startDate = null;
    private Date endDate = null;

    /**
     * @param startDate
     * @param endDate
     * @roseuid 3F4ECD8A03A0
     */
    public DateSearchCriteria(Date startDate, Date endDate) throws EmanagerEventException
    {
	this.startDate = startDate;
	this.endDate = endDate;
	checkValues();
    }

    public DateSearchCriteria (DateSearchCriteriaType transportCriteria) throws EmanagerEventException
    {
	this.startDate = transportCriteria.getStartDate();
	this.endDate = transportCriteria.getEndDate();
	checkValues();
    }

    public DateSearchCriteria (com.cisco.eManager.common.audit2.DateSearchCriteriaType transportCriteria) throws EmanagerEventException
    {
	this.startDate = transportCriteria.getStartDate();
	this.endDate = transportCriteria.getEndDate();
	checkValues();
    }

    // fix
    // take this away: null/null illegal
    /**
     * @roseuid 3F4E5D1400B3
     */
    public DateSearchCriteria()
    {
    }

    /**
     * Access method for the startDate property.
     *
     * @return   the current value of the startDate property
     */
    public Date getStartDate()
    {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     *
     * @param aStartDate the new value of the startDate property
     */
    public void setStartDate(Date aStartDate) throws EmanagerEventException
    {
        startDate = aStartDate;
	checkValues();
    }

    /**
     * Access method for the endDate property.
     *
     * @return   the current value of the endDate property
     */
    public Date getEndDate()
    {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     *
     * @param aEndDate the new value of the endDate property
     */
    public void setEndDate(Date aEndDate) throws EmanagerEventException
    {
        endDate = aEndDate;
	checkValues();
    }

    private void checkValues () throws EmanagerEventException
    {
	if (startDate == null ||
	    endDate == null) {
	    return;
	}

	if ( (startDate == null &&
	      endDate != null)       ||
	     (startDate != null &&
	      endDate == null)         )
	    {
		String logString;
		EmanagerEventException e;

		logString =
		    EmanagerEventStatusCode.IllegalDateSearchCriteria.getStatusCodeDescription() +
		    "  Both start and end date values are null.";
		logger.log(Priority.FATAL, logString);
		e = new EmanagerEventException (EmanagerEventStatusCode.IllegalDateSearchCriteria,
                                                logString);
		throw e;
	    }

	if (startDate.after (endDate)) {
	    String logString;
	    EmanagerEventException e;

	    logString =
		EmanagerEventStatusCode.IllegalDateSearchCriteria.getStatusCodeDescription() +
		"  The start date is later than the end date.";
	    logger.log(Priority.FATAL, logString);
	    e = new EmanagerEventException (EmanagerEventStatusCode.IllegalDateSearchCriteria,
                                            logString);
	    throw e;
	}

	if (endDate.before (startDate)) {
	    String logString;
	    EmanagerEventException e;

	    logString =
		EmanagerEventStatusCode.IllegalDateSearchCriteria.getStatusCodeDescription() +
		"  The end date is before the start date.";
	    logger.log(Priority.FATAL, logString);
	    e = new EmanagerEventException (EmanagerEventStatusCode.IllegalDateSearchCriteria,
                                            logString);
	    throw e;
	}
    }

    public String toString()
    {
        SimpleDateFormat dateFormat;

        dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.SSS");

	if (startDate == null) {
	    return "=null";
	}

	return dateFormat.format(startDate) + "<x<" + dateFormat.format (endDate);
    }
}

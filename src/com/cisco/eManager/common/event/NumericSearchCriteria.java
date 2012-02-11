package com.cisco.eManager.common.event;

import org.apache.log4j.*;

import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.common.event2.NumericSearchCriteriaType;

public class NumericSearchCriteria
{
    private static Logger      logger = Logger.getLogger(NumericSearchCriteria.class);

    private Long start = null;
    private Long end = null;

    /**
     * @param start
     * @param end
     * @roseuid 3F4EDCB003CF
     */
    public NumericSearchCriteria(Long start, Long end) throws EmanagerEventException
    {
	this.start = start;
	this.end = end;
	checkValues();
    }

    public NumericSearchCriteria (NumericSearchCriteriaType transportCriteria) throws EmanagerEventException
    {
	if (transportCriteria.hasStart() == true) {
	    start = new Long (transportCriteria.getStart());
	} else {
	    start = null;
	}

	if (transportCriteria.hasEnd() == true) {
	    end = new Long (transportCriteria.getEnd());
	} else {
	    end = null;
	}

	checkValues();
    }

    public NumericSearchCriteria (com.cisco.eManager.common.audit2.NumericSearchCriteriaType transportCriteria) throws EmanagerEventException
    {
	if (transportCriteria.hasStart() == true) {
	    setStart (new Long (transportCriteria.getStart()));
	} else {
	    start = null;
	}

	if (transportCriteria.hasEnd() == true) {
	    setEnd (new Long (transportCriteria.getEnd()));
	} else {
	    end = null;
	}

	checkValues();
    }

    /**
     * @roseuid 3F4E5D3C00F8
     */
    public NumericSearchCriteria()
    {

    }

    private void checkValues () throws EmanagerEventException
    {
	if (start == null ||
	    end == null) {
	    return;
	}

	if ( (start == null &&
	      end != null)       ||
	     (start != null &&
	      end == null)         )
	    {
		String logString;
		EmanagerEventException e;

		logString =
		    EmanagerEventStatusCode.IllegalNumericSearchCriteria.getStatusCodeDescription() +
		    "  Both start and end values are null.";
		logger.log(Priority.FATAL, logString);
		e = new EmanagerEventException (EmanagerEventStatusCode.IllegalNumericSearchCriteria,
                                                logString);
		throw e;
	    }

	if (start.longValue() > end.longValue()) {
	    String logString;
	    EmanagerEventException e;

	    logString =
		EmanagerEventStatusCode.IllegalNumericSearchCriteria.getStatusCodeDescription() +
		"  The start value is greater than the end value.";
	    logger.log(Priority.FATAL, logString);
	    e = new EmanagerEventException (EmanagerEventStatusCode.IllegalNumericSearchCriteria,
                                            logString);
	    throw e;
	}

	if (end.longValue() < start.longValue()) {
	    String logString;
	    EmanagerEventException e;

	    logString =
		EmanagerEventStatusCode.IllegalNumericSearchCriteria.getStatusCodeDescription() +
		"  The end value is less than the start value.";
	    logger.log(Priority.FATAL, logString);
	    e = new EmanagerEventException (EmanagerEventStatusCode.IllegalNumericSearchCriteria,
                                            logString);
	    throw e;
	}
    }

    /**
     * Access method for the start property.
     *
     * @return   the current value of the start property
     */
    public Long getStart()
    {
        return start;
    }

    /**
     * Sets the value of the start property.
     *
     * @param aStart the new value of the start property
     */
    public void setStart(Long aStart) throws EmanagerEventException
    {
        start = aStart;
	checkValues();
    }

    /**
     * Access method for the end property.
     *
     * @return   the current value of the end property
     */
    public Long getEnd()
    {
        return end;
    }

    /**
     * Sets the value of the end property.
     *
     * @param aEnd the new value of the end property
     */
    public void setEnd(Long aEnd) throws EmanagerEventException
    {
        end = aEnd;
	checkValues();
    }

    public boolean meetsCriteria (long number)
    {
	if (start == null &&
	    end == null) {
	    return true;
	}

	if (start.longValue() <= number &&
	    number <= end.longValue() ) {
	    return true;
	}

	return false;

    }

    public String toString()
    {
	return (start + "<=x<=" + end);
    }
}

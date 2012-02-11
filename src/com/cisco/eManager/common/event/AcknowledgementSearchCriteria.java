//Source file: /vob/emanager/src/com/cisco/eManager/common/event/AcknowledgementSearchCriteria.java

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\common\\event\\AcknowledgementSearchCriteria.java

package com.cisco.eManager.common.event;

import org.apache.log4j.*;

import com.cisco.eManager.common.event.StringSearchCriteria;
import com.cisco.eManager.common.event.DateSearchCriteria;

import com.cisco.eManager.common.event2.UserIdSearchCriteria;
import com.cisco.eManager.common.event2.StringSearchCriteriaType;
import com.cisco.eManager.common.event2.AcknowledgementSearchCriteriaType;

public class AcknowledgementSearchCriteria
{
    private static Logger      logger = Logger.getLogger(AcknowledgementSearchCriteria.class);

    /**
     * Collection of user ManagedObjectIds.
     */
    private StringSearchCriteria userId = null;
    private DateSearchCriteria acknowledgementTime = null;

    /**
     * @roseuid 3F4E5D1100AF
     */
    public AcknowledgementSearchCriteria()
    {

    }

    /**
     * @param userId
     * @param acknowledgementTime
     * @roseuid 3F4ECD2B019B
     */
    public AcknowledgementSearchCriteria(StringSearchCriteria userId,
                                         DateSearchCriteria acknowledgementTime) throws EmanagerEventException
    {
	this.userId = userId;
	this.acknowledgementTime = acknowledgementTime;
	checkValues();
    }

    public AcknowledgementSearchCriteria (AcknowledgementSearchCriteriaType searchCriteria)  throws EmanagerEventException
    {
	if (searchCriteria.getUserIdSearchCriteria() != null) {
	    userId = new StringSearchCriteria(searchCriteria.getUserIdSearchCriteria());
	} else {
	    userId = null;
	}

        if (searchCriteria.getAckTimeSearchCriteria() != null) {
            acknowledgementTime = new DateSearchCriteria (searchCriteria.getAckTimeSearchCriteria());
        } else {
            acknowledgementTime = null;
        }
    }

    /**
     * Access method for the userId property.
     *
     * @return   the current value of the userId property
     */
    public StringSearchCriteria getUserId()
    {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     *
     * @param aUserId the new value of the userId property
     */
    public void setUserId(StringSearchCriteria aUserId)  throws EmanagerEventException
    {
        userId = aUserId;
	checkValues();
    }

    /**
     * Access method for the acknowledgementTime property.
     *
     * @return   the current value of the acknowledgementTime property
     */
    public DateSearchCriteria getAcknowledgementTime()
    {
        return acknowledgementTime;
    }

    /**
     * Sets the value of the acknowledgementTime property.
     *
     * @param aAcknowledgementTime the new value of the acknowledgementTime property
     */
    public void setAcknowledgementTime(DateSearchCriteria aAcknowledgementTime) throws EmanagerEventException
    {
        acknowledgementTime = aAcknowledgementTime;
	checkValues();
    }

    private void checkValues() throws EmanagerEventException
    {
	if (userId == null &&
	    acknowledgementTime == null) {
	    String logString;
	    EmanagerEventException e;

	    logString =
		EmanagerEventStatusCode.IllegalAcknowledgementSearchCriteria.getStatusCodeDescription() +
		"  Both the user id and acknowledgement time are null.";
	    logger.log(Priority.FATAL, logString);
            e = new EmanagerEventException (EmanagerEventStatusCode.IllegalAcknowledgementSearchCriteria,
                                            logString);
	    throw e;
	}
    }

    public String toString()
    {
	return "userId[" + userId.toString() + "],ackTime[" + acknowledgementTime + "]";
    }
}

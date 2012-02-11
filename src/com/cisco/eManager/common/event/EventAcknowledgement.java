package com.cisco.eManager.common.event;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.text.DateFormat;

import com.cisco.eManager.common.event2.AcknowledgementType;

public class EventAcknowledgement
{
    private String userId;
    private Date date;
    private String comment;

    // fix
    // Workaround for SOAP
    public EventAcknowledgement()
    {

    }

    /**
     * @param userId
     * @param date
     * @param comment
     * @roseuid 3F4ED88E03E1
     */
    public EventAcknowledgement(String userId,
				Date date,
				String comment)
    {
	this.userId = userId;
	this.date = date;
	this.comment = comment;
    }

    /**
     * Access method for the userId property.
     *
     * @return   the current value of the userId property
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * Access method for the date property.
     *
     * @return   the current value of the date property
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Access method for the comment property.
     *
     * @return   the current value of the comment property
     */
    public String getComment()
    {
        return comment;
    }

    public String toString()
    {
        String dateString;

        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.SSS");

            dateString = dateFormat.format(date);
        } else {
            dateString = "null";
        }

        return "UserId:" + userId + "-Date:" + dateString + "-Comment:" + comment;
    }

    public AcknowledgementType populateTransportObject (AcknowledgementType object)
    {
	object.setUserId (getUserId());
	object.setDate (getDate());
	object.setComment (getComment());

	return object;
    }
}

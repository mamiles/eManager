package com.cisco.eManager.common.audit;

import org.apache.log4j.*;

import java.util.Date;

import java.text.SimpleDateFormat;

import java.io.StringWriter;

import com.cisco.eManager.common.audit.AuditDomain;
import com.cisco.eManager.common.audit.AuditAction;

import com.cisco.eManager.common.audit2.AuditLogEntryType;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

public class AuditLogEntry
{
    private static Logger      logger = Logger.getLogger(AuditLogEntry.class);

    private ManagedObjectId id;
    private AuditDomain domain;
    private AuditAction action;
    private String subject;
    private Date time;
    private String userId;
    private String description;

    // fix
    // Workaround for SOAP
    public AuditLogEntry()
    {

    }

    public AuditLogEntry (long id,
			  AuditDomain domain,
			  AuditAction action,
			  String subject,
			  Date time,
			  String userId,
			  String description)
    {
	this.id = new ManagedObjectId (ManagedObjectIdType.AuditLog,
				       id);
	if (domain != null) {
	    this.domain = domain;
	} else {
	    this.domain = AuditDomain.Emanager;
	}

	if (action != null) {
	    this.action = action;
	} else {
	    this.action = AuditAction.Create;
	}

	if (subject != null) {
	    this.subject = subject;
	} else {
	    this.subject = new String("Unknown");
	}

	if (time != null) {
	    this.time = time;
	} else {
	    this.time = new Date();
	}

	if (userId != null) {
	    this.userId = userId;
	} else {
	    this.userId = new String ("Unknown");
	}

	if (description != null) {
	    this.description = description;
	} else {
	    this.description = new String ("no comment");
	}
    }

    public AuditLogEntryType populateTransportObject (AuditLogEntryType object)
    {
	com.cisco.eManager.common.audit2.Id tid;
	com.cisco.eManager.common.audit2.Domain tdomain;
	com.cisco.eManager.common.audit2.Action taction;
	
	tid = new com.cisco.eManager.common.audit2.Id();
	tdomain = new com.cisco.eManager.common.audit2.Domain();
	taction = new com.cisco.eManager.common.audit2.Action();

	getId().populateAuditTransportObject (tid);
	getDomain().populateTransportObject (tdomain);
	getAction().populateTransportObject (taction);

	object.setId (tid);
	object.setDomain (tdomain);
	object.setAction (taction);
	object.setSubject (getSubject());
	object.setTime (getTime());
	object.setUserId (getUserId());
	object.setDescription (getDescription());

	return object;
    }

    public ManagedObjectId getId()
    {
	return id;
    }

    public AuditDomain getDomain()
    {
	return domain;
    }

    public void setDomain (AuditDomain domain)
    {
	if (domain != null) {
	    this.domain = domain;
	}
    }

    public AuditAction getAction()
    {
	return action;
    }

    public void setAction (AuditAction action)
    {
	if (action != null) {
	    this.action = action;
	}
    }

    public String getSubject()
    {
	return subject;
    }

    public void setSubject (String subject)
    {
	if (subject != null) {
	    this.subject = subject;
	}
    }

    public Date getTime()
    {
	return time;
    }

    public void setTime (Date time)
    {
	if (time != null) {
	    this.time = time;
	}
    }

    public String getUserId()
    {
	return userId;
    }

    public void setUserId (String userId)
    {
	if (userId != null) {
	    this.userId = userId;
	}
    }

    public String getDescription ()
    {
	return description;
    }

    public void setDescription (String description)
    {
	if (description != null) {
	    this.description = description;
	}
    }

    public String toString()
    {
        String timeString;
        SimpleDateFormat dateFormat;

        dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.SSS");

	timeString = dateFormat.format (time);

	return "Id=" + id + ";" +
	    "domain=" + domain + ";" +
	    "time=" + timeString + ";" +
	    "userId=" + userId + ";" +
	    "description=" + description + ";";
    }
}

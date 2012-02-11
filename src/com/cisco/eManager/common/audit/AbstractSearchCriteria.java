package com.cisco.eManager.common.audit;

import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedList;

import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.inventory.ManagedObjectId;

import com.cisco.eManager.common.audit.DateSearchCriteria;
import com.cisco.eManager.common.audit.NumericSearchCriteria;
import com.cisco.eManager.common.audit.AuditDomain;
import com.cisco.eManager.common.audit.AuditAction;
import com.cisco.eManager.common.audit.EmanagerAuditException;

import com.cisco.eManager.common.audit2.AbstractSearchCriteriaType;
import com.cisco.eManager.common.audit2.Domains;
import com.cisco.eManager.common.audit2.Actions;
import com.cisco.eManager.common.audit2.NumericSearchCriteriaType;
import com.cisco.eManager.common.audit2.StringSearchCriteriaType;
import com.cisco.eManager.common.audit2.DateSearchCriteriaType;

import com.cisco.eManager.common.inventory.EmanagerInventoryException;

public abstract class AbstractSearchCriteria
{
    private NumericSearchCriteria auditLogId = null;
    private Collection domain = null;
    private Collection action = null;
    private StringSearchCriteria subject = null;
    private DateSearchCriteria time = null;
    private StringSearchCriteria userId = null;

    /**
     * @roseuid 3F4E5D070280
     */
    public AbstractSearchCriteria()
    {

    }

    public AbstractSearchCriteria (AbstractSearchCriteriaType searchCriteria) throws EmanagerEventException
    {
	Actions actionObjects[];
	Domains domainObjects[];

	if (searchCriteria.getIdSearchCriteria() != null) {
	    this.auditLogId = new NumericSearchCriteria (searchCriteria.getIdSearchCriteria());
	} else {
	    this.auditLogId = null;
	}

	domainObjects = searchCriteria.getDomains();
	if (domainObjects != null &&
	    domainObjects.length > 0) {
	    Domains auditDomain;

	    domain = new LinkedList();
	    for (int x = 0; x < domainObjects.length; x++) {
		auditDomain = domainObjects[x];
		domain.add (AuditDomain.getAuditDomain (auditDomain));
	    }
	} else {
	    domain = null;
	}

	actionObjects = searchCriteria.getActions();
	if (actionObjects != null &&
	    actionObjects.length > 0) {
	    Actions auditAction;

	    action = new LinkedList();
	    for (int x = 0; x < actionObjects.length; x++) {
		auditAction = actionObjects[x];
		action.add (AuditAction.getAuditAction (auditAction));
	    }
	} else {
	    action = null;
	}

	if (searchCriteria.getSubjectSearchCriteria() != null) {
	    this.subject = new StringSearchCriteria (searchCriteria.getSubjectSearchCriteria());
	} else {
	    this.subject = null;
	}

	if (searchCriteria.getTimeSearchCriteria() != null) {
	    this.time = new DateSearchCriteria (searchCriteria.getTimeSearchCriteria());
	} else {
	    this.time = null;
	}

	if (searchCriteria.getUserIdSearchCriteria() != null) {
	    this.userId = new StringSearchCriteria (searchCriteria.getUserIdSearchCriteria());
	} else {
	    this.userId = null;
	}
    }

    public AbstractSearchCriteria(NumericSearchCriteria auditLogId,
				  Collection domain,
				  Collection action,
				  StringSearchCriteria subject,
				  DateSearchCriteria time,
				  StringSearchCriteria userId)
    {
	this.auditLogId = auditLogId;
	this.domain = domain;
	this.action = action;
	this.subject = subject;
	this.time = time;
	this.userId = userId;
    }

    public NumericSearchCriteria getAuditLogId()
    {
	return auditLogId;
    }

    public void setAuditLogId (NumericSearchCriteria auditLogId)
    {
	this.auditLogId = auditLogId;
    }

    public Collection getDomain ()
    {
	return domain;
    }

    public void setDomain (Collection domain)
    {
	this.domain = domain;
    }

    public Collection getAction ()
    {
	return action;
    }

    public void setAction (Collection action)
    {
	this.action = action;
    }


    public StringSearchCriteria getSubject()
    {
	return subject;
    }

    public void setSubject (StringSearchCriteria subject)
    {
	this.subject = subject;
    }

    /**
     * Access method for the postDate property.
     *
     * @return   the current value of the postDate property
     */
    public DateSearchCriteria getTime()
    {
        return time;
    }

    /**
     * Sets the value of the postDate property.
     *
     * @param aPostDate the new value of the postDate property
     */
    public void setTime(DateSearchCriteria aTime)
    {
        time = aTime;
    }

    public StringSearchCriteria getUserId()
    {
	return userId;
    }

    public void setUserId (StringSearchCriteria userId)
    {
	this.userId = userId;
    }

    public String toString()
    {
	StringBuffer string;

	string = new StringBuffer();

	if (auditLogId != null) {
	    string.append ("AuditId:" + auditLogId.toString() + "  ");
	}

	if (domain != null &&
	    domain.size() != 0) {
	    Iterator iter;
	    AuditDomain adomain;

	    string.append ("Domains:");
	    iter = domain.iterator();
	    while (iter.hasNext()) {
		adomain = (AuditDomain) iter.next();
		string.append (adomain + ",");
	    }

	    string.setCharAt(string.length() - 1, ' ');
	    string.append (" ");
	}

	if (action != null &&
	    action.size() != 0) {
	    Iterator iter;
	    AuditAction anaction;

	    string.append ("Actions:");
	    iter = action.iterator();
	    while (iter.hasNext()) {
		anaction = (AuditAction) iter.next();
		string.append (anaction + ",");
	    }

	    string.setCharAt(string.length() - 1, ' ');
	    string.append (" ");
	}

	if (subject !=  null) {
	    string.append ("Subject:" + subject + "  ");
	}

	if (time != null) {
	    string.append ("Time:" + time + "  ");
	}

	if (userId != null) {
	    string.append ("UserName:" + userId);
	}

	return string.toString();
    }
}

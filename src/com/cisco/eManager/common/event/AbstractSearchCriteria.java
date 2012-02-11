//Source file: /vob/emanager/src/com/cisco/eManager/common/event/AbstractSearchCriteria.java

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\common\\event\\AbstractSearchCriteria.java

package com.cisco.eManager.common.event;

import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedList;

import com.cisco.eManager.common.inventory.ManagedObjectId;

import com.cisco.eManager.common.event.EventSeverity;
import com.cisco.eManager.common.event.DateSearchCriteria;
import com.cisco.eManager.common.event.AcknowledgementSearchCriteria;
import com.cisco.eManager.common.event.NumericSearchCriteria;
import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.event2.AbstractSearchCriteriaType;
import com.cisco.eManager.common.event2.ManagedObjects;
import com.cisco.eManager.common.event2.Severities;

import com.cisco.eManager.common.inventory.EmanagerInventoryException;

public abstract class AbstractSearchCriteria
{
    private DateSearchCriteria postDate = null;
    private DateSearchCriteria clearDate = null;
    private NumericSearchCriteria emanagerEventId = null;
    private AcknowledgementSearchCriteria acknowledgement = null;
    private Collection managedObjectIds = null;
    private Collection severity = null;

    /**
     * @roseuid 3F4E5D070280
     */
    public AbstractSearchCriteria()
    {

    }

    public AbstractSearchCriteria (AbstractSearchCriteriaType searchCriteria) throws EmanagerEventException,
    EmanagerInventoryException
    {
	ManagedObjects managedObjects[];
	Severities severities[];

	if (searchCriteria.getPostDateSearchCriteria() != null) {
	    this.postDate = new DateSearchCriteria (searchCriteria.getPostDateSearchCriteria());
	} else {
	    this.postDate = null;
	}

        if (searchCriteria.getClearDateSearchCriteria() != null) {
            this.clearDate = new DateSearchCriteria (searchCriteria.getClearDateSearchCriteria());
        } else {
            this.clearDate = null;
        }

	if (searchCriteria.getEmanagerEventIdSearchCriteria() != null) {
	    this.emanagerEventId = new NumericSearchCriteria (searchCriteria.getEmanagerEventIdSearchCriteria());
	} else {
	    this.emanagerEventId = null;
	}

	if (searchCriteria.getAcknowledgementSearchCriteria() != null) {
	    this.acknowledgement = new AcknowledgementSearchCriteria (searchCriteria.getAcknowledgementSearchCriteria());
	} else {
	    this.acknowledgement = null;
	}

	managedObjects = searchCriteria.getManagedObjects();
	if (managedObjects != null &&
	    managedObjects.length > 0) {
	    ManagedObjects managedObject;

	    managedObjectIds = new LinkedList();
	    for (int x = 0; x < managedObjects.length; x++) {
		managedObject = managedObjects[x];
		managedObjectIds.add (new ManagedObjectId (managedObject));
	    }
	} else {
	    managedObjectIds = null;
	}

	severities = searchCriteria.getSeverities();
	if (severities != null &&
	    severities.length > 0) {
	    Severities severitie;

	    severity = new LinkedList();
	    for (int x = 0; x < severities.length; x++) {
		severitie = severities[x];
		severity.add (EventSeverity.getSeverity (severitie));
	    }
	} else {
	    severity = null;
	}
    }

    public AbstractSearchCriteria(DateSearchCriteria postDate,
				  NumericSearchCriteria emanagerEventId,
				  AcknowledgementSearchCriteria acknowledgement,
				  Collection managedObjectIds,
				  Collection severity,
				  DateSearchCriteria clearDate)
    {
	this.postDate = postDate;
	this.emanagerEventId = emanagerEventId;
	this.acknowledgement = acknowledgement;
	this.managedObjectIds = managedObjectIds;
	this.severity = severity;
	this.clearDate = clearDate;
    }

    /**
     * Access method for the postDate property.
     *
     * @return   the current value of the postDate property
     */
    public DateSearchCriteria getPostDate()
    {
        return postDate;
    }

    /**
     * Sets the value of the postDate property.
     *
     * @param aPostDate the new value of the postDate property
     */
    public void setPostDate(DateSearchCriteria aPostDate)
    {
        postDate = aPostDate;
    }

    public NumericSearchCriteria getEmanagerEventId ()
    {
	return emanagerEventId;
    }

    public void setEmanagerEventId (NumericSearchCriteria searchCriteria)
    {
	emanagerEventId = searchCriteria;
    }

    /**
     * Access method for the acknowledgement property.
     *
     * @return   the current value of the acknowledgement property
     */
    public AcknowledgementSearchCriteria getAcknowledgement()
    {
        return acknowledgement;
    }

    /**
     * Sets the value of the acknowledgement property.
     *
     * @param aAcknowledgement the new value of the acknowledgement property
     */
    public void setAcknowledgement(AcknowledgementSearchCriteria aAcknowledgement)
    {
        acknowledgement = aAcknowledgement;
    }

    /**
     * Access method for the manageObjectIds property.
     *
     * @return   the current value of the manageObjectIds property
     */
    public Collection getManagedObjectIds()
    {
        return managedObjectIds;
    }

    /**
     * Sets the value of the manageObjectIds property.
     *
     * @param aManagedObjectIds the new value of the managedObjectIds property
     */
    public void setManagedObjectIds(Collection aManagedObjectIds)
    {
        managedObjectIds = aManagedObjectIds;
    }

    /**
     * Access method for the severity property.
     *
     * @return   the current value of the severity property
     */
    public Collection getSeverity()
    {
        return severity;
    }

    /**
     * Sets the value of the severity property.
     *
     * @param aSeverity the new value of the severity property
     */
    public void setSeverity(Collection aSeverity)
    {
        severity = aSeverity;
    }

    /**
     * Access method for the clearDate property.
     *
     * @return   the current value of the clearDate property
     */
    public DateSearchCriteria getClearDate()
    {
        return clearDate;
    }

    /**
     * Sets the value of the clearDate property.
     *
     * @param aClearDate the new value of the clearDate property
     */
    public void setClearDate(DateSearchCriteria aClearDate)
    {
        clearDate = aClearDate;
    }

    public String toString()
    {
	StringBuffer string;

	string = new StringBuffer();

	if (postDate != null) {
	    string.append ("PostDate:" + postDate.toString() + "  ");
	}

	if (clearDate != null) {
	    string.append ("ClearDate:" + clearDate.toString() + "  ");
	}

	if (emanagerEventId != null) {
	    string.append ("EventId:" + emanagerEventId.toString() + "  ");
	}

	if (acknowledgement != null) {
	    string.append ("Acknowledgement:{" + acknowledgement.toString() + "}  ");
	}

	if (managedObjectIds != null &&
	    managedObjectIds.size() != 0) {
	    Iterator iter;
	    ManagedObjectId id;

	    string.append ("ManagedObjects:");
	    iter = managedObjectIds.iterator();
	    while (iter.hasNext()) {
		id = (ManagedObjectId) iter.next();
		string.append (id.toString() + ",");
	    }
	    
	    string.setCharAt(string.length() - 1, ' ');
	    string.append (" ");
	}

	if (severity != null &&
	    severity.size() != 0) {
	    Iterator iter;
	    EventSeverity aseverity;

	    string.append ("Severities:");
	    iter = severity.iterator();
	    while (iter.hasNext()) {
		aseverity = (EventSeverity) iter.next();
		string.append (aseverity.toString() + ",");
	    }

	    string.deleteCharAt(string.length() - 1);
	}

	return string.toString();
    }
}

package com.cisco.eManager.common.event;

import java.util.LinkedList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.cisco.eManager.common.event.NumericSearchCriteria;
import com.cisco.eManager.common.event.StringSearchCriteria;

import com.cisco.eManager.common.event2.AbstractTibcoSearchCriteriaType;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.common.inventory.EmanagerInventoryException;

import com.cisco.eManager.eManager.database.DatabaseGlobals;

public class AbstractTibcoEventSearchCriteria
{
    private NumericSearchCriteria tibcoEventId = null;
    private Collection managementPolicyIds = null;
    private StringSearchCriteria rule = null;
    private StringSearchCriteria test = null;
    private StringSearchCriteria instrumentation = null;

    /**
     * @roseuid 3F4E5D0A0357
     */
    public AbstractTibcoEventSearchCriteria()
    {

    }

    public AbstractTibcoEventSearchCriteria(NumericSearchCriteria tibcoEventId,
					    Collection managementPolicyIds,
					    StringSearchCriteria rule,
					    StringSearchCriteria test,
					    StringSearchCriteria instrumentation)
    {
	this.tibcoEventId = tibcoEventId;
	this.managementPolicyIds = managementPolicyIds;
        this.rule = rule;
        this.test = test;
	this.instrumentation = instrumentation;
    }

    public AbstractTibcoEventSearchCriteria (AbstractTibcoSearchCriteriaType criteria) throws EmanagerEventException,
    EmanagerInventoryException
    {
	if (criteria.getTibcoEventIdSearchCriteria() != null) {
	    tibcoEventId = new NumericSearchCriteria (criteria.getTibcoEventIdSearchCriteria());
	} else {
	    tibcoEventId = null;
	}

	if (criteria.getRuleSearchCriteria() != null) {
	    rule = new StringSearchCriteria (criteria.getRuleSearchCriteria());
	} else {
	    rule = null;
	}

	if (criteria.getTestSearchCriteria() != null) {
	    test = new StringSearchCriteria (criteria.getTestSearchCriteria());
	} else {
	    test = null;
	}

	if (criteria.getInstrumentationSearchCriteria() != null) {
	    instrumentation = new StringSearchCriteria (criteria.getInstrumentationSearchCriteria());
	} else {
	    instrumentation = null;
	}

	if (criteria.getManagementPolicyIds() == null ||
	    criteria.getManagementPolicyIds().length == 0) {
	    managementPolicyIds = null;
	} else {
	    managementPolicyIds = new LinkedList();
	    for (int x = 0; x < criteria.getManagementPolicyIds().length; x++) {
		managementPolicyIds.add (new ManagedObjectId (criteria.getManagementPolicyIds(x)));
	    }
	}
    }

    /**
     * Access method for the tibcoEventId property.
     *
     * @return   the current value of the tibcoEventId property
     */
    public NumericSearchCriteria getTibcoEventId()
    {
        return tibcoEventId;
    }

    /**
     * Sets the value of the tibcoEventId property.
     *
     * @param tibcoEventId the new value of the tibcoEventId property
     */
    public void setTibcoEventId(NumericSearchCriteria tibcoEventId)
    {
        this.tibcoEventId = tibcoEventId;
    }

    /**
     * Access method for the managementPolicy property.
     *
     * @return   the current value of the managementPolicy property
     */
    public Collection getManagementPolicyIds()
    {
        return managementPolicyIds;
    }

    /**
     * Sets the value of the managementPolicyId property.
     *
     * @param managementPolicyId the new value of the managementPolicyId property
     */
    public void setManagementPolicyIds(Collection managementPolicyIds)
    {
        this.managementPolicyIds = managementPolicyIds;
    }

    /**
     * Access method for the rule property.
     *
     * @return   the current value of the rule property
     */
    public StringSearchCriteria getRule()
    {
        return rule;
    }

    /**
     * Sets the value of the  property.
     *
     * @param  the new value of the  property
     */
    public void setRule(StringSearchCriteria rule)
    {
        this.rule = rule;
    }

    /**
     * Access method for the test property.
     *
     * @return   the current value of the test property
     */
    public StringSearchCriteria getTest()
    {
        return test;
    }

    /**
     * Sets the value of the test property.
     *
     * @param test the new value of the test property
     */
    public void setTest(StringSearchCriteria test)
    {
        this.test = test;
    }

    /**
     * Access method for the test property.
     *
     * @return   the current value of the test property
     */
    public StringSearchCriteria getInstrumentation()
    {
        return instrumentation;
    }

    /**
     * Sets the value of the test property.
     *
     * @param test the new value of the test property
     */
    public void setInstrumentation(StringSearchCriteria instrumentation)
    {
        this.instrumentation = instrumentation;
    }

    public boolean meetsCriteria (String databaseEventValuesString)
    {
	TibcoEventSpecificData eventData;

	if (tibcoEventId == null        &&
	    managementPolicyIds == null &&
            rule == null                &&
            test == null                &&
	    instrumentation == null) {
            return true;
	}

	eventData = parseDatabaseValues (databaseEventValuesString);

	if (tibcoEventId != null &&
	    eventData.eventId > -1) {
	    if (tibcoEventId.meetsCriteria (eventData.eventId) == false) {
		return false;
	    }
	}

        if (rule != null) {
            if (rule.meetsCriteria(eventData.ruleText) == false) {
                return false;
            }
        }

        if (test != null) {
            if (test.meetsCriteria(eventData.testText) == false) {
                return false;
            }
        }

        if (instrumentation != null) {
            if (instrumentation.meetsCriteria(eventData.instrumentationName) == false) {
                return false;
            }
        }

	if (managementPolicyIds != null &&
	    eventData.managementPolicyId != null) {
	    boolean found;
	    Iterator iter;
	    ManagedObjectId objectId;

	    iter = managementPolicyIds.iterator();
	    found = false;
	    while (iter.hasNext()) {
		objectId = (ManagedObjectId) iter.next();
		if (objectId.equals (eventData.managementPolicyId)) {
		    found = true;
		    break;
		}
	    }

	    if (found == false) {
		return false;
	    }
	}


	return true;
    }

    protected static TibcoEventSpecificData parseDatabaseValues (String dbString)
    {
	String key;
	String value;
	String keyValuePair;
	StringTokenizer dbStringTokenizer;
	StringTokenizer pairTokenizer;
	TibcoEventSpecificData eventData;

	eventData = new TibcoEventSpecificData();
	dbStringTokenizer = new StringTokenizer (dbString, DatabaseGlobals.DatabaseEventParmDelimeter);

	while (dbStringTokenizer.hasMoreTokens()) {
	    key = null;
	    value = null;
	    keyValuePair = dbStringTokenizer.nextToken();

	    pairTokenizer = new StringTokenizer (keyValuePair, DatabaseGlobals.DatabaseEventKeyValueSeparator);
	    if (pairTokenizer.hasMoreTokens()) {
		key = pairTokenizer.nextToken();
	    }

	    if (pairTokenizer.hasMoreTokens()) {
		value = pairTokenizer.nextToken();
	    }

	    if (key != null &&
		value != null) {
		if (key.equals (DatabaseGlobals.TibcoEventIdKey)) {
		    eventData.eventId = Long.parseLong (value);
		} else if (key.equals (DatabaseGlobals.TibcoRuleTextKey)) {
		    eventData.ruleText = value;
		} else if (key.equals (DatabaseGlobals.TibcoRuleTestKey)) {
		    eventData.testText = value;
		} else if (key.equals (DatabaseGlobals.TibcoInstrumentationNameKey)) {
		    eventData.instrumentationName = value;
		} else if (key.equals (DatabaseGlobals.TibcoEventMgmtPolicyId)) {
		    // The mgmt policy may be 0 if it is not set.  These are events
		    // generated by mgmt policy of watchdog.
                    long objectKey;

                    objectKey = Long.parseLong(value);
		    if (objectKey == 0) {
			eventData.managementPolicyId = null;
		    } else {
			eventData.managementPolicyId = new ManagedObjectId (ManagedObjectIdType.MgmtPolicy,
									    objectKey);
		    }
		}
	    }
	}

	return eventData;
    }

    public String toString()
    {
	StringBuffer string;

	string = new StringBuffer();
	if (tibcoEventId != null) {
	    string.append ("TibcoId:" + tibcoEventId.toString() + "  ");
	}

	if (managementPolicyIds != null &&
	    managementPolicyIds.size() != 0) {
	    Iterator iter;
	    ManagedObjectId id;

	    string.append ("MgmtPolicyIds:");
	    iter = managementPolicyIds.iterator();
	    while (iter.hasNext()) {
		id = (ManagedObjectId) iter.next();
		string.append (id.toString() + ",");
	    }

	    string.append ("  ");
	}

	if (rule != null) {
	    string.append ("Rule:" + rule.toString() + "  ");
	}

	if (test != null) {
	    string.append ("Test:" + test.toString());
	}

	if (instrumentation != null) {
	    string.append ("Instrumentation:" + instrumentation.toString());
	}

	return string.toString();
    }

    protected static class TibcoEventSpecificData
    {
	public long eventId = -1;
	public String ruleText = null;
	public String testText = null;
	public ManagedObjectId managementPolicyId = null;
        public String instrumentationName = null;
    }
}

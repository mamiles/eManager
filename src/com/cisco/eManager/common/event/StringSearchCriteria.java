package com.cisco.eManager.common.event;

import org.apache.log4j.*;

import com.cisco.eManager.common.event.EmanagerEventException;
import com.cisco.eManager.common.event.EmanagerEventStatusCode;

import com.cisco.eManager.common.event2.StringSearchCriteriaType;

public class StringSearchCriteria
{
    private static Logger      logger = Logger.getLogger(StringSearchCriteria.class);

    private String string = null;

    public StringSearchCriteria(String string)
    {
        setString (string);
    }

    public StringSearchCriteria()
    {
    }

    public StringSearchCriteria (StringSearchCriteriaType criteria)
    {
	string = criteria.getSearchString();
    }

    public StringSearchCriteria (com.cisco.eManager.common.audit2.StringSearchCriteriaType criteria)
    {
	string = criteria.getSearchString();
    }

    public String getString()
    {
        return string;
    }

    public void setString(String string)
    {
        if (string != null) {
            this.string = string.trim();
        } else {
            this.string = string;
        }
    }

    public boolean meetsCriteria (String matchString)
    {
        if (string == null &&
            matchString == null) {
            return true;
        }

        if (string != null &&
            matchString != null) {
            if (matchString.indexOf(string) != -1) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public String toString()
    {
	if (string == null) {
	    return "null";
	}

	return "contains " + string + "";
    }
}

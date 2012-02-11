package com.cisco.eManager.common.audit;

import org.apache.log4j.*;

import com.cisco.eManager.common.audit2.StringSearchCriteriaType;

public class StringSearchCriteria extends com.cisco.eManager.common.event.StringSearchCriteria
{
    private static Logger      logger = Logger.getLogger(StringSearchCriteria.class);

    public StringSearchCriteria(String string)
    {
	super (string);
    }

    public StringSearchCriteria (StringSearchCriteriaType criteria)
    {
	super (criteria);
    }

    public StringSearchCriteria()
    {
    }
}

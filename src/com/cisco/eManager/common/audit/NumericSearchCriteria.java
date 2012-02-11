package com.cisco.eManager.common.audit;

import org.apache.log4j.*;

import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.audit.EmanagerAuditException;
import com.cisco.eManager.common.audit.EmanagerAuditStatusCode;

import com.cisco.eManager.common.audit2.NumericSearchCriteriaType;

public class NumericSearchCriteria extends com.cisco.eManager.common.event.NumericSearchCriteria
{
    private static Logger      logger = Logger.getLogger(NumericSearchCriteria.class);

    /**
     * @param start
     * @param end
     * @roseuid 3F4EDCB003CF
     */
    public NumericSearchCriteria(Long start, Long end) throws EmanagerEventException
    {
        super (start, end);
    }

    // fix
    // take away this is illegal
    public NumericSearchCriteria()
    {

    }

    public NumericSearchCriteria (NumericSearchCriteriaType transportCriteria) throws EmanagerEventException
    {
        super (transportCriteria);
    }
}

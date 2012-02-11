package com.cisco.eManager.common.event;

import com.cisco.eManager.common.inventory.ManagedObjectId;

/**
 * The AbstractEventEvent is a base class for events passed through
 * the northbound interface over SOAP/HTTP.
 *
 * @author mjmatch
 */
public abstract class AbstractEventEvent
{
    protected ManagedObjectId emanagerEventId;
     /**
     * @param emanagerEventId
     * @roseuid 3F4E5C25039F
     */
    public AbstractEventEvent(ManagedObjectId emanagerEventId)
    {
        this.emanagerEventId = emanagerEventId;
    }

    /**
     * Access method for the emanagerEventId property.
     *
     * @return   the current value of the emanagerEventId property
     */
    public ManagedObjectId getEmanagerEventId()
    {
        return emanagerEventId;
    }

    /*
     * Add the following 2 methods to make this one bean class for SOAP
     */

    public void setEmanagerEventId(ManagedObjectId emanagerEventId)
    {
        this.emanagerEventId = emanagerEventId;
    }

    public AbstractEventEvent()
    {
    }
}

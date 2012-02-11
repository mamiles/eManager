//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\event\\EventSyncReport.java

package com.cisco.eManager.eManager.event;

import java.util.LinkedList;
import java.util.Collection;

import com.cisco.eManager.eManager.event.SyncStatus;

import com.cisco.eManager.common.event.AbstractEventMessage;

import com.cisco.eManager.common.inventory.ManagedObjectId;

import com.cisco.eManager.eManager.util.GlobalProperties;

public class SyncReport
{
    private Collection events;
    private ManagedObjectId hostObjectId;
    private StringBuffer log;
    private SyncStatus status;

    /**
     * @param hostObjectId
     * @param newEvents
     * @roseuid 3F4FB8030154
     */
    public SyncReport(ManagedObjectId hostObjectId)
    {
        events = new LinkedList();
        this.hostObjectId = hostObjectId;
        log = new StringBuffer();
        status = SyncStatus.Success;
    }

    public void addEvent(AbstractEventMessage event)
    {
        events.add(event);
    }

    /**
     * Access method for the events property.
     *
     * @return   the current value of the events property
     */
    public Collection getEvents()
    {
        return events;
    }

    /**
     * Access method for the hostObjectId property.
     *
     * @return   the current value of the hostObjectId property
     */
    public ManagedObjectId getHostObjectId()
    {
        return hostObjectId;
    }

    public void addLogEntry(String entry)
    {
        log.append(entry);
        log.append(GlobalProperties.CarriageReturn);
    }

    public String getLog()
    {
        return log.toString();
    }

    public void setStatus (SyncStatus status)
    {
        status = status;
    }

    public SyncStatus getStatus ()
    {
        return status;
    }
}

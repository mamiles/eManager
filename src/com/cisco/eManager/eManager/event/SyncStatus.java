package com.cisco.eManager.eManager.event;

public class SyncStatus
{
    private int syncStatus;

    public static final SyncStatus Success = new SyncStatus (0);
    public static final SyncStatus Warnings = new SyncStatus (1);
    public static final SyncStatus Failure = new SyncStatus (2);

    // fix
    // fix. remove this.  here for soap debug only
    public SyncStatus()
    {
        syncStatus = SyncStatus.Success.intValue();
    }

    /**
     * @param eventSeverity
     * @roseuid 3F4EDA0A01E9
     */
    private SyncStatus(int syncStatus)
    {
        this.syncStatus = syncStatus;
    }

    /**
     * Access method for the eventSeverity property.
     *
     * @return   the current value of the eventSeverity property
     */
    public int intValue()
    {
        return syncStatus;
    }

    public boolean equals (Object object)
    {
        if (object instanceof SyncStatus) {
            if ( ( (SyncStatus) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }
}
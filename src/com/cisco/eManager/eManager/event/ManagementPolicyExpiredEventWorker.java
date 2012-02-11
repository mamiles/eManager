//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\event\\ManagementPolicyExpiredEventWorker.java

package com.cisco.eManager.eManager.event;

import com.cisco.eManager.common.inventory.ManagedObjectId;

public class ManagementPolicyExpiredEventWorker extends AbstractWorker
{
    private static ThreadGroup threadGroup = null;
    private static Integer threadCountIndex = new Integer (1);
    private static Integer managementPolicyExpiredTimeout = new Integer (15);
    private ManagedObjectId managementPolicyId;

    /**
     * @param managementPolicyId
     * @roseuid 3F4E5F9E01E0
     */
    public ManagementPolicyExpiredEventWorker(ManagedObjectId managementPolicyId)
    {
        this.managementPolicyId = managementPolicyId;
    }

    /**
     * Access method for the managementPolicyExpiredTimeout property.
     *
     * @return   the current value of the managementPolicyExpiredTimeout property
     */
    public static Integer getManagementPolicyExpiredTimeout()
    {
        return managementPolicyExpiredTimeout;
    }

    /**
     * Sets the value of the managementPolicyExpiredTimeout property.
     *
     * @param aManagementPolicyExpiredTimeout the new value of the managementPolicyExpiredTimeout property
     */
    public static void setManagementPolicyExpiredTimeout(Integer aManagementPolicyExpiredTimeout)
    {
        managementPolicyExpiredTimeout = aManagementPolicyExpiredTimeout;
    }

    /**
     * @return ManagementPolicy
     * @roseuid 3F2D6180004C
     */
    public ManagedObjectId getManagementPolicy()
    {
        return managementPolicyId;
    }

    /**
     * @roseuid 3F2D63010366
     */
    public void startTimer()
    {

    }

    /**
     * @return com.cisco.eManager.eManager.event.ManagementPolicyExpiredEventWorker
     * @roseuid 3F427E5F03A8
     */
    public static ManagementPolicyExpiredEventWorker createWorker()
    {
     return null;
    }

    public Thread getThread()
    {
        Thread thread = null;

        return thread;
    }
    /**
     * @roseuid 3F575F7702E6
     */
    public void run()
    {

    }

    public void notifyStateChangeListeners()
    {
        // noop
    }
}
//ManagementPolicyExpiredEventWorker.ManagementPolicyExpiredEventWorker()

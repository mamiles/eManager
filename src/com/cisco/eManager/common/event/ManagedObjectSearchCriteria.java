//Source file: /vob/emanager/src/com/cisco/eManager/common/event/ManagedObjectSearchCriteria.java

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\common\\event\\ManagedObjectSearchCriteria.java

package com.cisco.eManager.common.event;

import com.cisco.eManager.common.inventory.ManagedObjectId;

public class ManagedObjectSearchCriteria
{
    private ManagedObjectId managedObjectId;
    private boolean includeChildren;

    // fix
    // Workaround for SOAP
    public ManagedObjectSearchCriteria()
    {

    }

    /**
     * @param managedObjectId
     * @param includeChildren
     * @roseuid 3F4E5D39014E
     */
    public ManagedObjectSearchCriteria(ManagedObjectId managedObjectId,
				       boolean includeChildren)
    {
	this.managedObjectId = managedObjectId;
	this.includeChildren = includeChildren;
    }

    /**
     * Access method for the managedObjectId property.
     *
     * @return   the current value of the managedObjectId property
     */
    public ManagedObjectId getManagedObjectId()
    {
        return managedObjectId;
    }

    /**
     * Sets the value of the managedObjectId property.
     *
     * @param aManagedObjectId the new value of the managedObjectId property
     */
    public void setManagedObjectId(ManagedObjectId aManagedObjectId)
    {
        managedObjectId = aManagedObjectId;
    }

    /**
     * Determines if the includeChildren property is true.
     *
     * @return   <code>true<code> if the includeChildren property is true
     */
    public boolean getIncludeChildren()
    {
        return includeChildren;
    }

    /**
     * Sets the value of the includeChildren property.
     *
     * @param aIncludeChildren the new value of the includeChildren property
     */
    public void setIncludeChildren(boolean aIncludeChildren)
    {
        includeChildren = aIncludeChildren;
    }
}

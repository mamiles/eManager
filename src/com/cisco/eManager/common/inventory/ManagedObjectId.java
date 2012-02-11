//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\common\\inventory\\ManagedObjectId.java

package com.cisco.eManager.common.inventory;

import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.common.inventory.EmanagerInventoryStatusCode;
import com.cisco.eManager.common.inventory.EmanagerInventoryException;

public class ManagedObjectId
{
    private ManagedObjectIdType managedObjectType;
    private long managedObjectKey;

    public ManagedObjectId()
    {
        // fix
        // the only reason to provide a default ctor is because this is part of
        // a SOAP/http interface described in wsdl...
        managedObjectType = ManagedObjectIdType.ApplicationHierarchyContainer;
        managedObjectKey = 0;
    }

    /**
     * @param managedObjectType
     * @param managedObjectKey
     * @roseuid 3F561E700119
     */
    public ManagedObjectId(ManagedObjectIdType managedObjectType, long managedObjectKey)
    {
        this.managedObjectType = managedObjectType;
        this.managedObjectKey = managedObjectKey;
    }

    public ManagedObjectId (com.cisco.eManager.common.event2.ManagedObjectIdType transportObject) throws EmanagerInventoryException
    {
	if (transportObject.hasObjectKey() == false) {
	    throw new EmanagerInventoryException (EmanagerInventoryStatusCode.InvalidManagedObjectIdNoObjectKey,
						  EmanagerInventoryStatusCode.InvalidManagedObjectIdNoObjectKey.getStatusCodeDescription());
	}

	this.managedObjectKey = transportObject.getObjectKey();
	this.managedObjectType = ManagedObjectIdType.getManagedObjectIdType (transportObject.getObjectType());
    }

    /**
     * @return int
     * @roseuid 3F60C595034B
     */
    public ManagedObjectIdType getManagedObjectType()
    {
        return this.managedObjectType;
    }

    /**
     * @return int
     * @roseuid 3F60C5960067
     */
    public long getManagedObjectKey()
    {
        return this.managedObjectKey;
    }

    public boolean equals (Object object)
    {
        if (object instanceof ManagedObjectId) {
            if (managedObjectType.equals (( (ManagedObjectId)object).getManagedObjectType()) &&
                managedObjectKey  == ( (ManagedObjectId)object).getManagedObjectKey() ) {
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        return "Type:" + managedObjectType + "-Key:" + managedObjectKey;
    }

    public com.cisco.eManager.common.event2.ManagedObjectIdType
        populateEventTransportObject (com.cisco.eManager.common.event2.ManagedObjectIdType object)
    {
	com.cisco.eManager.common.event2.ObjectType transportObjectType;

	transportObjectType = new com.cisco.eManager.common.event2.ObjectType ();

	getManagedObjectType().populateEventTransportObject (transportObjectType);
	object.setObjectType (transportObjectType);
	object.setObjectKey (getManagedObjectKey());

	return object;
    }

    public com.cisco.eManager.common.audit2.ManagedObjectIdType
        populateAuditTransportObject (com.cisco.eManager.common.audit2.ManagedObjectIdType object)
    {
	com.cisco.eManager.common.audit2.ObjectType transportObjectType;

	transportObjectType = new com.cisco.eManager.common.audit2.ObjectType ();

	getManagedObjectType().populateAuditTransportObject (transportObjectType);
	object.setObjectType (transportObjectType);
	object.setObjectKey (getManagedObjectKey());

	return object;
    }

    public com.cisco.eManager.common.inventory2.ManagedObjectId getInventoryTransportObject ()
    {
	com.cisco.eManager.common.inventory2.ManagedObjectId transportId;

	transportId = new com.cisco.eManager.common.inventory2.ManagedObjectId();

	transportId.setObjectKey(getManagedObjectKey());
	transportId.setObjectType (getManagedObjectType().getInventoryTransportObject());

	return transportId;
    }
}

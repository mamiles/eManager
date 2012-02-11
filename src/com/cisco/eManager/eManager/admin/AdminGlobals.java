package com.cisco.eManager.eManager.admin;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;

public class AdminGlobals
{
    public static final ManagedObjectId AdministratorSecurityRoleId = new ManagedObjectId (ManagedObjectIdType.SecurityRole,
											   1);

    public static final ManagedObjectId OperatorSecurityRoleId = new ManagedObjectId (ManagedObjectIdType.SecurityRole,
										      2);

    public static final ManagedObjectId GuestSecurityRoleId = new ManagedObjectId (ManagedObjectIdType.SecurityRole,
										   3);
}

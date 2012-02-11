/*********************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.  Possessions and use of this
 * program must conform strictly to the license agreement between the user
 * and Cisco Systems, Inc., and receipt or possession does not convey
 * any rights to divulge, reproduce, or allow others to use this program
 * without specific written authorization of Cisco Systems, Inc.
 *
 * Copyright (c) 2001 by Cisco Systems, Inc.
 * All rights reserved.
 *
 * NOTE: DO NOT MODIFY THIS FILE 
 *       IF YOU NEED TO MODIFY THIS FILE, YOU NEED TO:
 *       1. Regenerate the SOAP generated files
 *       2. Update InventoryManagerSoapBindingImpl.java
 *       3. Update ../common/SoapToEmgr.java
 *       4. Update ../common/EmgrToSoap.java
 *
 *       Contact chido@cisco.com for more information
 * 
 *********************************************************************/

package com.cisco.eManager.soap.inventory;

// import java.util.*;
// import com.cisco.eManager.common.inventory.*;

public interface InventoryManagerInterface
{
    public String xmlMessage(String sessionID, String xmlString);

/*
    public ManagedObjectId createContainerNode(String name,
                                               int type,
                                               ManagedObjectId parentId)
        throws EmanagerInventoryException;

    public void deleteContainerNode(ManagedObjectId nodeId)
        throws EmanagerInventoryException;

    public Agent getAgent(ManagedObjectId agentId)
        throws EmanagerInventoryException;

    public AppInstance getAppInstance(ManagedObjectId appInstanceId)
        throws EmanagerInventoryException;

    public ViewContainer getAppsViewRoot()
        throws EmanagerInventoryException;

    public AppType getAppType(ManagedObjectId appTypeId)
        throws EmanagerInventoryException;

    public ViewContainer getContainer(ManagedObjectId containerId)
        throws EmanagerInventoryException;

    public ViewContainer[] getContainerChildren(ManagedObjectId containerId)
        throws EmanagerInventoryException;

    public ViewContainer getHostViewRoot()
        throws EmanagerInventoryException;

    public Instrumentation[] getInstrumentation(ManagedObjectId appInstanceId)
        throws EmanagerInventoryException;

    public MgmtPolicy[] getManagementPolicies(ManagedObjectId appTypeId,
                                              ManagedObjectId agentId)
        throws EmanagerInventoryException;

    public void manageAppInstance(ManagedObjectId appInstanceId)
        throws EmanagerInventoryException;

    public void moveContainerNode(ManagedObjectId nodeId,
                                  ManagedObjectId newParentId)
        throws EmanagerInventoryException;

    public void unmanageAppInstance(ManagedObjectId appInstanceId)
        throws EmanagerInventoryException;

    public LogLevel getLogLevel(ManagedObjectId appInstanceId)
        throws EmanagerInventoryException;

    public void setLogLevel(ManagedObjectId appInstanceId, LogLevel logLevel)
        throws EmanagerInventoryException;

    public String invoke(Method method)
        throws EmanagerInventoryException;
*/
}

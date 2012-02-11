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
 *       2. Update EventManagerSoapBindingImpl.java
 *       3. Update ../common/SoapToEmgr.java
 *       4. Update ../common/EmgrToSoap.java
 *
 *       Contact chido@cisco.com for more information
 * 
 *********************************************************************/

package com.cisco.eManager.soap.event;

/*
import java.util.Collection;

import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.EventSearchCriteria;
import com.cisco.eManager.common.event.EventDeletionCriteria;
import com.cisco.eManager.common.event.AcknowledgementEvent;
import com.cisco.eManager.common.event.EventAcknowledgement;
import com.cisco.eManager.common.event.UnacknowledgementEvent;
import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.inventory.ManagedObjectId;

import com.cisco.eManager.common.database.EmanagerDatabaseException;
*/

public interface EventManagerInterface
{
    public String xmlMessage(String sessionID, String xmlString);
    
/*

    public void acknowledgeEvent(ManagedObjectId eventId, EventAcknowledgement acknowledgement)
        throws EmanagerEventException, EmanagerDatabaseException;

    public void unacknowledgeEvent(ManagedObjectId eventId, ManagedObjectId userId)
        throws EmanagerEventException, EmanagerDatabaseException;

    public EmanagerEventDetails getEventDetails(ManagedObjectId eventId)
        throws EmanagerEventException, EmanagerDatabaseException;

    public Collection retrieveEvents(EventSearchCriteria eventSearchCriteria)
        throws EmanagerEventException, EmanagerDatabaseException;

    public void deleteEvents(EventDeletionCriteria criteria)
        throws EmanagerEventException, EmanagerDatabaseException;

    public void registerSNMPClient(String host, String community, int port)
        throws EmanagerEventException;

    public void unregisterSNMPClient(String host, String community, int port)
        throws EmanagerEventException;
*/

}

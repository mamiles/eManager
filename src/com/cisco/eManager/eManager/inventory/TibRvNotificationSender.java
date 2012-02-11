package com.cisco.eManager.eManager.inventory;

import org.exolab.castor.xml.*;

import org.apache.log4j.*;

import com.tibco.tibrv.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.io.StringWriter;

import com.cisco.eManager.common.event.XMLDeliverableMessage;
import com.cisco.eManager.common.event.AbstractEventEvent;
import com.cisco.eManager.common.event.EventType;
import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.EmanagerEventException;

import com.cisco.eManager.common.event2.NotificationType;

import com.cisco.eManager.eManager.tibco.TibcoManager;

import com.cisco.eManager.common.inventory2.*;

import com.cisco.eManager.eManager.tibco.TibcoGlobals;

import com.cisco.eManager.eManager.util.GlobalProperties;

public class TibRvNotificationSender implements InventoryNotificationSenderInterface
{

    private static Logger      logger = Logger.getLogger(TibRvNotificationSender.class);

    private static TibRvNotificationSender TibRvNotificationSender = null;
    private TibrvTransport tibcoTransport;

    private static final String TibcoEventMessageSubject = "cisco.mgmt.emanager.event.notification";
    private static final String TibcoEventFieldName = "Event";

    private static final int AppInstanceCreated = 1;
    private static final int AppInstanceRestored = 2;
    private static final int AppInstanceManaged = 3;
    private static final int AppInstanceUnmanaged = 4;
    private static final int AppInstanceDeleted = 5;

    private static final int AppTypeCreated = 6;
    private static final int AppTypeRestored = 7;
    private static final int AppTypeDeleted = 8;

    private static final int HostCreated = 9;
    private static final int HostRestored = 10;
    private static final int HostDeleted = 11;

    private static final int InstrumentationCreated = 12;
    private static final int InstrumentationRestored = 13;
    private static final int InstrumentationDeleted = 14;

    private static final int MgmtPolicyCreated = 15;
    private static final int MgmtPolicyRestored = 16;
    private static final int MgmtPolicyDeleted = 17;

    private static final int SolutionCreated = 18;
    private static final int SolutionRestored = 19;
    private static final int SolutionDeleted = 20;

    private static final int AppViewCreated = 18;
    private static final int AppViewRestored = 19;
    private static final int AppViewDeleted = 20;
    private static final int AppViewMoved = 20;

    private static final int SolnViewCreated = 18;
    private static final int SolnViewRestored = 19;
    private static final int SolnViewDeleted = 20;
    private static final int SolnViewMoved = 20;

    private static final int PhysViewCreated = 18;
    private static final int PhysViewRestored = 19;
    private static final int PhysViewDeleted = 20;
    private static final int PhysViewMoved = 20;

    private TibRvNotificationSender() throws TibrvException
    {
        Properties properties;
        String tibcoService;
        String tibcoNetwork;
        String tibcoDaemon;

        properties = GlobalProperties.instance().getProperties();
        tibcoService = properties.getProperty(TibcoGlobals.TibcoServiceKey);
        if (tibcoService == null) {
            tibcoService = TibcoGlobals.TibcoServiceDefault;
            logger.info ("Using the following default tibco service to broadcast event information: " +
			 tibcoService);
        } else {
            logger.info ("Using the following tibco service to broadcast event information: " +
			 tibcoService);
	}

        tibcoNetwork = properties.getProperty(TibcoGlobals.TibcoNetworkKey);
	logger.info ("Using the following tibco network to broadcast event information: " +
		     tibcoNetwork);

        tibcoDaemon = properties.getProperty(TibcoGlobals.TibcoDaemonKey);
        if (tibcoDaemon == null) {
            tibcoDaemon = TibcoGlobals.TibcoDaemonDefault;
            logger.info ("Using the following default tibco daemon to broadcast event information: " +
			 tibcoDaemon);
        } else {
            logger.info ("Using the following tibco daemon to broadcast event information: " +
			 tibcoDaemon);
	}

        try {
            Tibrv.open(Tibrv.IMPL_NATIVE);
            tibcoTransport = new TibrvRvdTransport(tibcoService, tibcoNetwork, tibcoDaemon);
        }
        catch (TibrvException e)
        {
	    tibcoTransport = null;
            logger.fatal ("Fatal error establishing tibrv transport:" +
			  e.getMessage());
	    throw e;
        }

        InventoryNotificationDistributor.instance().attach(this);
    }

    public static synchronized TibRvNotificationSender instance() throws TibrvException
    {
        if (TibRvNotificationSender == null) {
            TibRvNotificationSender = new TibRvNotificationSender();
        }

        return TibRvNotificationSender;
    }

    public void appInstanceCreated (com.cisco.eManager.common.inventory.ManagedObjectId appInstanceId)
    {
	sendAppInstanceNotification (AppInstanceCreated, appInstanceId);
    }

    public void appInstanceRestored (com.cisco.eManager.common.inventory.ManagedObjectId appInstanceId)
    {
	sendAppInstanceNotification (AppInstanceRestored, appInstanceId);
    }

    public void appInstanceManaged (com.cisco.eManager.common.inventory.ManagedObjectId appInstanceId)
    {
	sendAppInstanceNotification (AppInstanceManaged, appInstanceId);
    }

    public void appInstanceUnmanaged (com.cisco.eManager.common.inventory.ManagedObjectId appInstanceId)
    {
	sendAppInstanceNotification (AppInstanceUnmanaged, appInstanceId);
    }

    public void appInstanceDeleted (com.cisco.eManager.common.inventory.ManagedObjectId appInstanceId)
    {
	sendAppInstanceNotification (AppInstanceDeleted, appInstanceId);
    }

    public void appInstancesConsolidated (Collection newAppInstanceIds,
					  Collection deletedAppInstanceIds)
    {
	String                        xmlString;
	StringWriter                  stringWriter;
	InventoryNotification         invNot;
	InventoryChangeNotification   invChgNot;
	AppInstanceChange             appInstChg;
	AppsConsolidationNotification appConsNot;

	Iterator iter;
	com.cisco.eManager.common.inventory.ManagedObjectId id;

	if (newAppInstanceIds == null ||
	    deletedAppInstanceIds == null) {
	    return;
	}

	stringWriter = new StringWriter();
	invNot = new InventoryNotification();
	invChgNot = new InventoryChangeNotification();
	appInstChg = new AppInstanceChange();
	appConsNot = new AppsConsolidationNotification();

	invNot.addInventoryChanges (invChgNot);
	invChgNot.setAppInstanceChange (appInstChg);
	appInstChg.setAppsConsolidation (appConsNot);

	iter = newAppInstanceIds.iterator();
	while (iter.hasNext()) {
	    id = (com.cisco.eManager.common.inventory.ManagedObjectId) iter.next();
	    appConsNot.addNewAppInstances (id.getInventoryTransportObject());
	}

	iter = deletedAppInstanceIds.iterator();
	while (iter.hasNext()) {
	    id = (com.cisco.eManager.common.inventory.ManagedObjectId) iter.next();
	    appConsNot.addDeletedAppInstances (id.getInventoryTransportObject());
	}

	try {
	    invNot.marshal (stringWriter);
	    xmlString = stringWriter.toString();
	}
	catch (ValidationException e) {
	    logger.warn ("Unexpected error marshalling application instance change notification message: " + e);
            return;
	}
	catch (MarshalException e) {
	    logger.warn ("Unexpected error marshalling application instance change notification message: " + e);
            return;
	}

        try {
            TibcoManager.instance().broadcastInventoryNotification(xmlString);
        }
        catch (TibrvException e) {
        }
    }

    private void sendAppInstanceNotification (int type,
					      com.cisco.eManager.common.inventory.ManagedObjectId appInstanceId)
    {
	String xmlString;
	StringWriter stringWriter;
	InventoryNotification       invNot;
	InventoryChangeNotification invChgNot;
	AppInstanceChange          appInstChg;

	if (appInstanceId == null) {
	    return;
	}

	stringWriter = new StringWriter();
	invNot = new InventoryNotification();
	invChgNot = new InventoryChangeNotification();
	appInstChg = new AppInstanceChange();

	invNot.addInventoryChanges (invChgNot);
	invChgNot.setAppInstanceChange (appInstChg);

	if (type == AppInstanceCreated) {
	    appInstChg.setCreated (appInstanceId.getInventoryTransportObject());
	} else if (type == AppInstanceRestored) {
	    appInstChg.setRestored (appInstanceId.getInventoryTransportObject());
	} else if (type == AppInstanceManaged) {
	    appInstChg.setManaged (appInstanceId.getInventoryTransportObject());
	} else if (type == AppInstanceUnmanaged) {
	    appInstChg.setUnmanaged (appInstanceId.getInventoryTransportObject());
	} else if (type == AppInstanceDeleted) {
	    appInstChg.setDeleted (appInstanceId.getInventoryTransportObject());
	} else {
	    logger.error ("Internal programming error.  Unrecognized application instance " +
			  "notification type: " + type);
	    return;
	}

	try {
	    invNot.marshal (stringWriter);
	    xmlString = stringWriter.toString();
	}
	catch (ValidationException e) {
	    logger.warn ("Unexpected error marshalling application instance change notification message: " + e);
            return;
	}
	catch (MarshalException e) {
	    logger.warn ("Unexpected error marshalling application instance change notification message: " + e);
            return;
	}

        try {
            TibcoManager.instance().broadcastInventoryNotification(xmlString);
        }
        catch (TibrvException e) {
        }
    }

    //
    // Application Type Interface
    //
    public void appTypeCreated (com.cisco.eManager.common.inventory.ManagedObjectId appInstanceId)
    {
	sendAppTypeNotification (AppTypeCreated, appInstanceId);
    }

    public void appTypeRestored (com.cisco.eManager.common.inventory.ManagedObjectId appInstanceId)
    {
	sendAppTypeNotification (AppTypeRestored, appInstanceId);
    }

    public void appTypeDeleted (com.cisco.eManager.common.inventory.ManagedObjectId appInstanceId)
    {
	sendAppTypeNotification (AppTypeDeleted, appInstanceId);
    }

    private void sendAppTypeNotification (int type,
					  com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	String xmlString;
	StringWriter stringWriter;
	InventoryNotification       invNot;
	InventoryChangeNotification invChgNot;
	AppTypeChange          chg;

	if (id == null) {
	    return;
	}

	stringWriter = new StringWriter();
	invNot = new InventoryNotification();
	invChgNot = new InventoryChangeNotification();
	chg = new AppTypeChange();

	invNot.addInventoryChanges (invChgNot);
	invChgNot.setAppTypeChange (chg);

	if (type == AppTypeCreated) {
	    chg.setCreated (id.getInventoryTransportObject());
	} else if (type == AppTypeRestored) {
	    chg.setRestored (id.getInventoryTransportObject());
	} else if (type == AppTypeDeleted) {
	    chg.setDeleted (id.getInventoryTransportObject());
	} else {
	    logger.error ("Internal programming error.  Unrecognized application type " +
			  "notification type: " + type);
	    return;
	}

	try {
	    invNot.marshal (stringWriter);
	    xmlString = stringWriter.toString();
	}
	catch (ValidationException e) {
	    logger.warn ("Unexpected error marshalling application type change notification message: " + e);
            return;
	}
	catch (MarshalException e) {
	    logger.warn ("Unexpected error marshalling application type change notification message: " + e);
            return;
	}

        try {
            TibcoManager.instance().broadcastInventoryNotification(xmlString);
        }
        catch (TibrvException e) {
        }
    }

    //
    // Host Interface
    //
    public void hostCreated (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendHostNotification (HostCreated, id);
    }

    public void hostRestored (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendHostNotification (HostRestored, id);
    }

    public void hostDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendHostNotification (HostDeleted, id);
    }

    private void sendHostNotification (int type,
				       com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	String xmlString;
	StringWriter stringWriter;
	InventoryNotification       invNot;
	InventoryChangeNotification invChgNot;
	HostChange          chg;

	if (id == null) {
	    return;
	}

	stringWriter = new StringWriter();
	invNot = new InventoryNotification();
	invChgNot = new InventoryChangeNotification();
	chg = new HostChange();

	invNot.addInventoryChanges (invChgNot);
	invChgNot.setHostChange (chg);

	if (type == HostCreated) {
	    chg.setCreated (id.getInventoryTransportObject());
	} else if (type == HostRestored) {
	    chg.setRestored (id.getInventoryTransportObject());
	} else if (type == HostDeleted) {
	    chg.setDeleted (id.getInventoryTransportObject());
	} else {
	    logger.error ("Internal programming error.  Unrecognized host " +
			  "notification type: " + type);
	    return;
	}

	try {
	    invNot.marshal (stringWriter);
	    xmlString = stringWriter.toString();
	}
	catch (ValidationException e) {
	    logger.warn ("Unexpected error marshalling host change notification message: " + e);
            return;
	}
	catch (MarshalException e) {
	    logger.warn ("Unexpected error marshalling host change notification message: " + e);
            return;
	}

        try {
            TibcoManager.instance().broadcastInventoryNotification(xmlString);
        }
        catch (TibrvException e) {
        }
    }

    //
    // Instrumentation Interface
    //
    public void instrumentationCreated (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendInstrumentationNotification (InstrumentationCreated, id);
    }

    public void instrumentationRestored (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendInstrumentationNotification (InstrumentationRestored, id);
    }

    public void instrumentationDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendInstrumentationNotification (InstrumentationDeleted, id);
    }

    private void sendInstrumentationNotification (int type,
						  com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	String xmlString;
	StringWriter stringWriter;
	InventoryNotification       invNot;
	InventoryChangeNotification invChgNot;
	InstrumentationChange          chg;

	if (id == null) {
	    return;
	}

	stringWriter = new StringWriter();
	invNot = new InventoryNotification();
	invChgNot = new InventoryChangeNotification();
	chg = new InstrumentationChange();

	invNot.addInventoryChanges (invChgNot);
	invChgNot.setInstrumentationChange (chg);

	if (type == InstrumentationCreated) {
	    chg.setCreated (id.getInventoryTransportObject());
	} else if (type == InstrumentationRestored) {
	    chg.setRestored (id.getInventoryTransportObject());
	} else if (type == InstrumentationDeleted) {
	    chg.setDeleted (id.getInventoryTransportObject());
	} else {
	    logger.error ("Internal programming error.  Unrecognized instrumentation " +
			  "notification type: " + type);
	    return;
	}

	try {
	    invNot.marshal (stringWriter);
	    xmlString = stringWriter.toString();
	}
	catch (ValidationException e) {
	    logger.warn ("Unexpected error marshalling instrumentation change notification message: " + e);
            return;
	}
	catch (MarshalException e) {
	    logger.warn ("Unexpected error marshalling instrumentation change notification message: " + e);
            return;
	}

        try {
            TibcoManager.instance().broadcastInventoryNotification(xmlString);
        }
        catch (TibrvException e) {
        }
    }

    //
    // MgmtPolicy Interface
    //
    public void mgmtPolicyCreated (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendMgmtPolicyNotification (MgmtPolicyCreated, id);
    }

    public void mgmtPolicyRestored (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendMgmtPolicyNotification (MgmtPolicyRestored, id);
    }

    public void mgmtPolicyDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendMgmtPolicyNotification (MgmtPolicyDeleted, id);
    }

    private void sendMgmtPolicyNotification (int type,
				       com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	String xmlString;
	StringWriter stringWriter;
	InventoryNotification       invNot;
	InventoryChangeNotification invChgNot;
	MgmtPolicyChange          chg;

	if (id == null) {
	    return;
	}

	stringWriter = new StringWriter();
	invNot = new InventoryNotification();
	invChgNot = new InventoryChangeNotification();
	chg = new MgmtPolicyChange();

	invNot.addInventoryChanges (invChgNot);
	invChgNot.setMgmtPolicyChange (chg);

	if (type == MgmtPolicyCreated) {
	    chg.setCreated (id.getInventoryTransportObject());
	} else if (type == MgmtPolicyRestored) {
	    chg.setRestored (id.getInventoryTransportObject());
	} else if (type == MgmtPolicyDeleted) {
	    chg.setDeleted (id.getInventoryTransportObject());
	} else {
	    logger.error ("Internal programming error.  Unrecognized mgmt policy " +
			  "notification type: " + type);
	    return;
	}

	try {
	    invNot.marshal (stringWriter);
	    xmlString = stringWriter.toString();
	}
	catch (ValidationException e) {
	    logger.warn ("Unexpected error marshalling mgmt policy change notification message: " + e);
            return;
	}
	catch (MarshalException e) {
	    logger.warn ("Unexpected error marshalling mgmt policy change notification message: " + e);
            return;
	}

        try {
            TibcoManager.instance().broadcastInventoryNotification(xmlString);
        }
        catch (TibrvException e) {
        }
    }

    //
    // Solution Interface
    //
    public void solutionCreated (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendSolutionNotification (SolutionCreated, id);
    }

    public void solutionRestored (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendSolutionNotification (SolutionRestored, id);
    }

    public void solutionDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendSolutionNotification (SolutionDeleted, id);
    }

    private void sendSolutionNotification (int type,
					   com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	String xmlString;
	StringWriter stringWriter;
	InventoryNotification       invNot;
	InventoryChangeNotification invChgNot;
	SolutionChange          chg;

	if (id == null) {
	    return;
	}

	stringWriter = new StringWriter();
	invNot = new InventoryNotification();
	invChgNot = new InventoryChangeNotification();
	chg = new SolutionChange();

	invNot.addInventoryChanges (invChgNot);
	invChgNot.setSolutionChange (chg);

	if (type == SolutionCreated) {
	    chg.setCreated (id.getInventoryTransportObject());
	} else if (type == SolutionRestored) {
	    chg.setRestored (id.getInventoryTransportObject());
	} else if (type == SolutionDeleted) {
	    chg.setDeleted (id.getInventoryTransportObject());
	} else {
	    logger.error ("Internal programming error.  Unrecognized solution " +
			  "notification type: " + type);
	    return;
	}

	try {
	    invNot.marshal (stringWriter);
	    xmlString = stringWriter.toString();
	}
	catch (ValidationException e) {
	    logger.warn ("Unexpected error marshalling solution change notification message: " + e);
            return;
	}
	catch (MarshalException e) {
	    logger.warn ("Unexpected error marshalling solution change notification message: " + e);
            return;
	}

        try {
            TibcoManager.instance().broadcastInventoryNotification(xmlString);
        }
        catch (TibrvException e) {
        }
    }

    //
    // App View Interface
    //
    public void applicationViewCreated (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendViewNotification (AppViewCreated, id);
    }

    public void applicationViewRestored (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendViewNotification (AppViewRestored, id);
    }

    public void applicationViewDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendViewNotification (AppViewDeleted, id);
    }

    public void applicationViewMoved (com.cisco.eManager.common.inventory.ManagedObjectId nodeId,
				      com.cisco.eManager.common.inventory.ManagedObjectId oldParentId,
				      com.cisco.eManager.common.inventory.ManagedObjectId newParentId)
    {
	sendViewNotification (AppViewMoved, nodeId, oldParentId, newParentId);
    }

    //
    // Phys View Interface
    //
    public void physicalViewCreated (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendViewNotification (AppViewCreated, id);
    }

    public void physicalViewRestored (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendViewNotification (AppViewRestored, id);
    }

    public void physicalViewDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendViewNotification (AppViewDeleted, id);
    }

    public void physicalViewMoved (com.cisco.eManager.common.inventory.ManagedObjectId nodeId,
				   com.cisco.eManager.common.inventory.ManagedObjectId oldParentId,
				   com.cisco.eManager.common.inventory.ManagedObjectId newParentId)
    {
	sendViewNotification (PhysViewMoved, nodeId, oldParentId, newParentId);
    }

    //
    // Soln View Interface
    //
    public void solutionViewCreated (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendViewNotification (SolnViewCreated, id);
    }

    public void solutionViewRestored (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendViewNotification (SolnViewRestored, id);
    }

    public void solutionViewDeleted (com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	sendViewNotification (SolnViewDeleted, id);
    }

    public void solutionViewMoved (com.cisco.eManager.common.inventory.ManagedObjectId nodeId,
				   com.cisco.eManager.common.inventory.ManagedObjectId oldParentId,
				   com.cisco.eManager.common.inventory.ManagedObjectId newParentId)
    {
	sendViewNotification (SolnViewMoved, nodeId, oldParentId, newParentId);
    }

    private void sendViewNotification (int type,
				       com.cisco.eManager.common.inventory.ManagedObjectId id)
    {
	String xmlString;
	StringWriter stringWriter;
	InventoryNotification       invNot;
	InventoryChangeNotification invChgNot;
	ViewChange          chg;

	if (id == null) {
	    return;
	}

	stringWriter = new StringWriter();
	invNot = new InventoryNotification();
	invChgNot = new InventoryChangeNotification();
	chg = new ViewChange();

	invNot.addInventoryChanges (invChgNot);
	invChgNot.setViewChange (chg);

	if (type == AppViewCreated  ||
	    type == AppViewRestored ||
	    type == AppViewDeleted) {
	    AppsViewChange vchg;

	    vchg = new AppsViewChange();
	    chg.setAppsViewChange (vchg);

	    if (type == AppViewCreated) {
		vchg.setCreated (id.getInventoryTransportObject());
	    } else if (type == AppViewRestored) {
		vchg.setRestored (id.getInventoryTransportObject());
	    }else {
		vchg.setDeleted (id.getInventoryTransportObject());
	    }
	} else if (type == PhysViewCreated  ||
		   type == PhysViewRestored ||
		   type == PhysViewDeleted) {
	    PhysicalViewChange vchg;

	    vchg = new PhysicalViewChange();
	    chg.setPhysicalViewChange (vchg);

	    if (type == PhysViewCreated) {
		vchg.setCreated (id.getInventoryTransportObject());
	    } else if (type == PhysViewRestored) {
		vchg.setRestored (id.getInventoryTransportObject());
	    }else {
		vchg.setDeleted (id.getInventoryTransportObject());
	    }
	} else if (type == SolnViewCreated  ||
		   type == SolnViewRestored ||
		   type == SolnViewDeleted) {
	    SolutionViewChange vchg;

	    vchg = new SolutionViewChange();
	    chg.setSolutionViewChange (vchg);

	    if (type == SolnViewCreated) {
		vchg.setCreated (id.getInventoryTransportObject());
	    } else if (type == SolnViewRestored) {
		vchg.setRestored (id.getInventoryTransportObject());
	    }else {
		vchg.setDeleted (id.getInventoryTransportObject());
	    }
	} else {
	    logger.warn ("Unexpected view type encountered: " + type);
	}

	try {
	    invNot.marshal (stringWriter);
	    xmlString = stringWriter.toString();
	}
	catch (ValidationException e) {
	    logger.warn ("Unexpected error marshalling view change notification message: " + e);
            return;
	}
	catch (MarshalException e) {
	    logger.warn ("Unexpected error marshalling view change notification message: " + e);
            return;
	}

        try {
            TibcoManager.instance().broadcastInventoryNotification(xmlString);
        }
        catch (TibrvException e) {
        }
    }

    private void sendViewNotification (int type,
				       com.cisco.eManager.common.inventory.ManagedObjectId nodeId,
				       com.cisco.eManager.common.inventory.ManagedObjectId oldParentId,
				       com.cisco.eManager.common.inventory.ManagedObjectId newParentId)
    {
	String xmlString;
	StringWriter stringWriter;
	InventoryNotification       invNot;
	InventoryChangeNotification invChgNot;
	ViewChange          chg;
	NodeMovedNotification nmn;

	if (nodeId == null      ||
	    oldParentId == null ||
	    newParentId == null) {
	    return;
	}

	stringWriter = new StringWriter();
	invNot = new InventoryNotification();
	invChgNot = new InventoryChangeNotification();
	chg = new ViewChange();
	nmn = new NodeMovedNotification();

	invNot.addInventoryChanges (invChgNot);
	invChgNot.setViewChange (chg);
	nmn.setNode (nodeId.getInventoryTransportObject());
	nmn.setOldParent (oldParentId.getInventoryTransportObject());
	nmn.setNewParent (newParentId.getInventoryTransportObject());

	if (type == AppViewMoved) {
	    AppsViewChange vchg;

	    vchg = new AppsViewChange();
	    chg.setAppsViewChange (vchg);
	    vchg.setMoved (nmn);
	} else if (type == PhysViewMoved) {
	    PhysicalViewChange vchg;

	    vchg = new PhysicalViewChange();
	    chg.setPhysicalViewChange (vchg);
	    vchg.setMoved (nmn);
	} else if (type == SolnViewMoved) {
	    SolutionViewChange vchg;

	    vchg = new SolutionViewChange();
	    chg.setSolutionViewChange (vchg);
	    vchg.setMoved (nmn);
	} else {
	    logger.warn ("Unexpected view type encountered: " + type);
	}

	try {
	    invNot.marshal (stringWriter);
	    xmlString = stringWriter.toString();
	}
	catch (ValidationException e) {
	    logger.warn ("Unexpected error marshalling view change notification message: " + e);
            return;
	}
	catch (MarshalException e) {
	    logger.warn ("Unexpected error marshalling view change notification message: " + e);
            return;
	}

        try {
            TibcoManager.instance().broadcastInventoryNotification(xmlString);
        }
        catch (TibrvException e) {
        }
    }

    public void broadcastMessage (XMLDeliverableMessage notification)
    {
	String xmlMessage;
	TibrvMsg tibcoMsg;

	try {
	    xmlMessage = notification.getXMLMessage();
	    tibcoMsg = new TibrvMsg();
	    tibcoMsg.setSendSubject (TibcoEventMessageSubject);
	    tibcoMsg.add(TibcoEventFieldName, xmlMessage);

	    try {
		if (tibcoTransport != null) {
		    tibcoTransport.send(tibcoMsg);
		}
	    }
	    catch (TibrvException e) {
		logger.error ("Error sending Tibrv message: " +
			      e);
	    }
	}
	catch (EmanagerEventException e) {
	    logger.error("Error sending  Tibrv message: " +
                             e);
	}
	catch (TibrvException e) {
	    logger.error ("Error creating Tibrv message: " +
			  e);
	}
    }

    public void broadcastPostEvents (Collection events)
    {
	broadcastEvents (events, EventType.PostType);
    }

    public void broadcastClearEvents (Collection events)
    {
	broadcastEvents (events, EventType.ClearType);
    }

    private void broadcastEvents (Collection events,
				  EventType eventType)
    {
        Iterator eventIter;
        EmanagerEventDetails event;
        TibrvMsg tibcoMsg;

        eventIter = events.iterator();
        while (eventIter.hasNext()) {
            event = (EmanagerEventDetails) eventIter.next();
            try {
                NotificationType type;
                type = new NotificationType();
		if (eventType.equals (EventType.PostType) == true) {
                    type.setPost(1);
		    tibcoMsg = event.getTibcoBroadcastEventMessage(type);
		} else {
                    type.setClear(1);
		    tibcoMsg = event.getTibcoBroadcastEventMessage(type);
		}

                try {
		    if (tibcoTransport != null) {
			tibcoTransport.send(tibcoMsg);
		    }
                }
                catch (TibrvException e) {
                    logger.error ("Error sending Tibrv message: " +
                                  e.getMessage());
                }
            }
            catch (EmanagerEventException e) {
                logger.error("Error sending  Tibrv message: " +
                             e.getMessage());
            }
        }
    }

    public void sendMessage(String subject, String xmlMessage) {
        String FIELD_NAME = "DATA";
        TibrvMsg tibcoMsg;

        try {
            tibcoMsg = new TibrvMsg();
            tibcoMsg.setSendSubject(subject);
            tibcoMsg.update(FIELD_NAME, xmlMessage);
            logger.debug("Sending msg: " + xmlMessage);

            try {
                if (tibcoTransport != null) {
                    tibcoTransport.send(tibcoMsg);
                }
            }
            catch (TibrvException e) {
                logger.error ("Error sending Tibrv message: " +  e);
            }
        }
        catch (TibrvException e) {
            logger.error ("Error creating Tibrv message: " + e);
        }
    }
}

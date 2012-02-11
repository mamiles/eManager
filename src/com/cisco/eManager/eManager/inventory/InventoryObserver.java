
package com.cisco.eManager.eManager.inventory;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.eManager.inventory.view.*;

public class InventoryObserver implements Observer
{
    private static Logger logger = Logger.getLogger(InventoryObserver.class);
    private InventoryManager m_im;
    private AppViewManager m_avm;
    private HostViewManager m_hvm;
    private SolutionViewManager m_svm;
    private InventoryNotificationDistributor m_ind;

    public InventoryObserver()
        throws Exception
    {
        logger.debug("enter");

        m_ind = InventoryNotificationDistributor.instance();

        m_im = InventoryManager.instance();
        m_im.addObserver(this);

        m_avm = AppViewManager.instance();
        m_avm.addObserver(this);

        m_hvm = HostViewManager.instance();
        m_hvm.addObserver(this);

        m_svm = SolutionViewManager.instance();
        m_svm.addObserver(this);
    }

    public void update()
    {
        logger.debug("enter");
        logger.warn("method update() not supported");
    }

    public void update(Observable subj, Object obj)
    {
        logger.debug("enter");
        if ( subj instanceof InventoryManager )
        {
            updateIm(obj);
        }
        else if ( subj instanceof AppViewManager )
        {
            updateAvm(obj);
        }
        else if ( subj instanceof HostViewManager )
        {
            updateHvm(obj);
        }
        else if ( subj instanceof SolutionViewManager )
        {
            updateSvm(obj);
        }
        else
        {
            logger.warn("subject of unexpected class received: " +
                        subj.getClass().getName());
        }
    }

    private void updateIm(Object obj)
    {
        logger.debug("enter");
        if ( obj instanceof ImAppInstanceConsolidation )
        {
            logger.debug("handling ImAppInstanceConsolidation update");
            ImAppInstanceConsolidation aic = (ImAppInstanceConsolidation)obj;
            m_ind.appInstancesConsolidated(aic.resultingAppInstance(),
                                           aic.consolidatedAppInstances());
        }
        else if ( obj instanceof ImAppInstanceCreation )
        {
            logger.debug("handling ImAppInstanceCreation update");
            ImAppInstanceCreation aic = (ImAppInstanceCreation)obj;
            m_ind.appInstanceCreated(aic.appInstance().id());
        }
        else if ( obj instanceof ImAppInstanceDeletion )
        {
            logger.debug("handling ImAppInstanceDeletion update");
            ImAppInstanceDeletion aid = (ImAppInstanceDeletion)obj;
            m_ind.appInstanceDeleted(aid.appInstance().id());
        }
        else if ( obj instanceof ImAppInstanceManage )
        {
            logger.debug("handling ImAppInstanceManage update");
            ImAppInstanceManage aim = (ImAppInstanceManage)obj;
            m_ind.appInstanceManaged(aim.appInstance().id());
        }
        else if ( obj instanceof ImAppInstanceRestoration )
        {
            logger.debug("handling ImAppInstanceRestoration update");
            ImAppInstanceRestoration air = (ImAppInstanceRestoration)obj;
            m_ind.appInstanceRestored(air.appInstance().id());
        }
        else if ( obj instanceof ImAppInstanceUnmanage )
        {
            logger.debug("handling ImAppInstanceUnmanage update");
            ImAppInstanceUnmanage aiu = (ImAppInstanceUnmanage)obj;
            m_ind.appInstanceUnmanaged(aiu.appInstance().id());
        }
        else if ( obj instanceof ImAppTypeCreation )
        {
            logger.debug("handling ImAppTypeCreation update");
            ImAppTypeCreation atc = (ImAppTypeCreation)obj;
            m_ind.appTypeCreated(atc.appType().id());
        }
        else if ( obj instanceof ImAppTypeDeletion )
        {
            logger.debug("handling ImAppTypeDeletion update");
            ImAppTypeDeletion atd = (ImAppTypeDeletion)obj;
            m_ind.appTypeDeleted(atd.appType().id());
        }
        else if ( obj instanceof ImHostCreation )
        {
            logger.debug("handling ImHostCreation update");
            ImHostCreation hc = (ImHostCreation)obj;
            m_ind.hostCreated(hc.host().id());
        }
        else if ( obj instanceof ImHostDeletion )
        {
            logger.debug("handling ImHostDeletion update");
            ImHostDeletion hd = (ImHostDeletion)obj;
            m_ind.hostDeleted(hd.host().id());
        }
        else if ( obj instanceof ImInstrumentationCreation )
        {
            logger.debug("handling ImInstrumentationCreation update");
            ImInstrumentationCreation ic = (ImInstrumentationCreation)obj;
            m_ind.instrumentationCreated(ic.instrumentation().id());
        }
        else if ( obj instanceof ImInstrumentationDeletion )
        {
            logger.debug("handling ImInstrumentationDeletion update");
            ImInstrumentationDeletion id = (ImInstrumentationDeletion)obj;
            m_ind.instrumentationDeleted(id.instrumentation().id());
        }
        else if ( obj instanceof ImMgmtPolicyCreation )
        {
            logger.debug("handling ImMgmtPolicyCreation update");
            ImMgmtPolicyCreation mpc = (ImMgmtPolicyCreation)obj;
            m_ind.mgmtPolicyCreated(mpc.mgmtPolicy().id());
        }
        else if ( obj instanceof ImMgmtPolicyDeletion )
        {
            logger.debug("handling ImMgmtPolicyDeletion update");
            ImMgmtPolicyDeletion mpd = (ImMgmtPolicyDeletion)obj;
            m_ind.mgmtPolicyDeleted(mpd.mgmtPolicy().id());
        }
        else
        {
            logger.warn("object of unexpected class received: " +
                        obj.getClass().getName());
        }
    }

    private void updateAvm(Object obj)
    {
        logger.debug("enter");
        if ( obj instanceof NodeMoveNotification )
        {
            logger.debug("handling \"node move\" update");
            NodeMoveNotification moveNtfcn = (NodeMoveNotification)obj;
            m_ind.applicationViewMoved(moveNtfcn.node().id(),
                                       moveNtfcn.oldParent().id(),
                                       moveNtfcn.newParent().id());
        }
        else if ( obj instanceof NodeNotification )
        {
            NodeNotification ntfcn = (NodeNotification)obj;
            NodeNotificationType ntfcnType = ntfcn.ntfcnType();
            if ( ntfcnType.equals(NodeNotificationType.CREATE) )
            {
                logger.debug("handling \"node create\" update");
                m_ind.applicationViewCreated(ntfcn.node().id());
            }
            else if ( ntfcnType.equals(NodeNotificationType.DELETE) )
            {
                logger.debug("handling \"node delete\" update");
                m_ind.applicationViewDeleted(ntfcn.node().id());
            }
            else
            {
                logger.warn("unrecognized NodeNotificationType - dropping " +
                            "notification");
            }
        }
        else
        {
            logger.warn("object of unexpected class received: " +
                        obj.getClass().getName());
        }
    }

    private void updateHvm(Object obj)
    {
        logger.debug("enter");
        if ( obj instanceof NodeMoveNotification )
        {
            logger.debug("handling \"node move\" update");
            NodeMoveNotification moveNtfcn = (NodeMoveNotification)obj;
            m_ind.physicalViewMoved(moveNtfcn.node().id(),
                                    moveNtfcn.oldParent().id(),
                                    moveNtfcn.newParent().id());

        }
        else if ( obj instanceof NodeNotification )
        {
            NodeNotification ntfcn = (NodeNotification)obj;
            NodeNotificationType ntfcnType = ntfcn.ntfcnType();
            if ( ntfcnType.equals(NodeNotificationType.CREATE) )
            {
                logger.debug("handling \"node create\" update");
                m_ind.physicalViewCreated(ntfcn.node().id());
            }
            else if ( ntfcnType.equals(NodeNotificationType.DELETE) )
            {
                logger.debug("handling \"node delete\" update");
                m_ind.physicalViewDeleted(ntfcn.node().id());
            }
            else
            {
                logger.warn("unrecognized NodeNotificationType - dropping " +
                            "notification");
            }
        }
        else
        {
            logger.warn("object of unexpected class received: " +
                        obj.getClass().getName());
        }
    }

    private void updateSvm(Object obj)
    {
        logger.debug("enter");
        if ( obj instanceof NodeMoveNotification )
        {
            logger.debug("handling \"node move\" update");
            NodeMoveNotification moveNtfcn = (NodeMoveNotification)obj;
            m_ind.solutionViewMoved(moveNtfcn.node().id(),
                                    moveNtfcn.oldParent().id(),
                                    moveNtfcn.newParent().id());
        }
        else if ( obj instanceof NodeNotification )
        {
            NodeNotification ntfcn = (NodeNotification)obj;
            NodeNotificationType ntfcnType = ntfcn.ntfcnType();
            if ( ntfcnType.equals(NodeNotificationType.CREATE) )
            {
                logger.debug("handling \"node create\" update");
                m_ind.solutionViewCreated(ntfcn.node().id());
            }
            else if ( ntfcnType.equals(NodeNotificationType.DELETE) )
            {
                logger.debug("handling \"node delete\" update");
                m_ind.solutionViewDeleted(ntfcn.node().id());
            }
            else
            {
                logger.warn("unrecognized NodeNotificationType - dropping " +
                            "notification");
            }
        }
        else
        {
            logger.warn("object of unexpected class received: " +
                        obj.getClass().getName());
        }
    }
}
package com.cisco.eManager.eManager.inventory;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.*;

import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;
import COM.TIBCO.hawk.console.hawkeye.*;
import COM.TIBCO.hawk.talon.*;

import com.cisco.eManager.common.audit.*;
import com.cisco.eManager.common.inventory2.*;
import com.cisco.eManager.common.util.AccessType;
import com.cisco.eManager.common.util.Rc;
import com.cisco.eManager.common.util.ResponseType;
import com.cisco.eManager.common.util.StatusResp;
import com.cisco.eManager.common.util.StatusResponse;
import com.cisco.eManager.eManager.audit.*;
import com.cisco.eManager.eManager.util.GlobalProperties;
import com.cisco.eManager.common.database.*;

/**
 * <p>Title: NBAPI Message Handler</p>
 * <p>Description: the Inventory Management subsystem's NBAPI message handler</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems</p>
 * @author Wm. Mark Stubbeman
 * @version 1.0
 *
 */

public class NbapiMsgHandler
{
    private static Logger logger = Logger.getLogger(NbapiMsgHandler.class);
    private static NbapiMsgHandler s_instance = null;

    protected String m_domain = null;
    protected String m_service = null;
    protected String m_network = null;
    protected String m_daemon = null;
    protected String m_emHome = null;
    protected TibrvQueue m_rvQueue = null;
    protected TIBHawkConsole m_console = null;
    protected AgentManager m_agentMgr = null;

    protected MicroAgentID m_processSequencerMaId = null;

    private AuditManager m_auditMgr;
    private com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager m_aim;
    private com.cisco.eManager.eManager.inventory.appType.AppTypeManager m_atm;
    private com.cisco.eManager.eManager.inventory.host.HostManager m_hm;
    private com.cisco.eManager.eManager.inventory.instrumentation.InstrumentationManager m_im;
    private com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicyManager m_mpm;
    private com.cisco.eManager.eManager.inventory.view.AppViewManager m_avm;
    private com.cisco.eManager.eManager.inventory.view.HostViewManager m_hvm;
    private com.cisco.eManager.eManager.inventory.view.SolutionViewManager m_svm;
    private com.cisco.eManager.eManager.inventory.InventoryManager m_invMgr;

    public static NbapiMsgHandler instance()
    {
        logger.debug("enter");
        if (s_instance == null)
        {
            s_instance = new NbapiMsgHandler();
        }
        return s_instance;
    }

    private NbapiMsgHandler()
    {
        logger.debug("enter");
        try
        {
            setupConfig();

            // Setup the Console and initialize the agent manager
            m_console = new TIBHawkConsole(m_domain, m_service, m_network, m_daemon);
            m_agentMgr = m_console.getAgentManager();
            m_agentMgr.initialize();

            m_auditMgr = AuditManager.instance();
            m_aim = com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager.instance();
            m_atm = com.cisco.eManager.eManager.inventory.appType.AppTypeManager.instance();
            m_hm = com.cisco.eManager.eManager.inventory.host.HostManager.instance();
            m_im = com.cisco.eManager.eManager.inventory.instrumentation.InstrumentationManager.instance();
            m_mpm = com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicyManager.instance();
            m_avm = com.cisco.eManager.eManager.inventory.view.AppViewManager.instance();
            m_hvm = com.cisco.eManager.eManager.inventory.view.HostViewManager.instance();
            m_svm = com.cisco.eManager.eManager.inventory.view.SolutionViewManager.instance();
            m_invMgr = com.cisco.eManager.eManager.inventory.InventoryManager.instance();
        }
        catch (Exception e)
        {
            logger.fatal("Exception caught: " + e);
            e.printStackTrace();
        }
    }

    private void setupConfig()
    {
        GlobalProperties globalProp = GlobalProperties.instance();
        Properties emProp = globalProp.getProperties();

        m_service = emProp.getProperty("SYSTEM.tibhawk.service", "7474");
        logger.debug("TibHawk UDP Service: " + m_service);
        m_network = emProp.getProperty("SYSTEM.tibhawk.network", null);
        logger.debug(
            "TibHawk network to use for outbound session communications: " +
            m_network);
        m_daemon = emProp.getProperty("SYSTEM.tibhawk.daemon", "tcp:7474");
        logger.debug(
            "TIBCO Rendezvous daemon to handle communication for the session: " +
            m_daemon);
        m_domain = emProp.getProperty("SYSTEM.tibhawk.domain", "default");
        logger.debug("TibHawk Domain on which the console is to communicate: " + m_domain);
        m_emHome = System.getProperty("EMANAGER_ROOT");
        logger.debug("eManager Home directory (EMANAGER_ROOT):" + m_emHome);
    }

    public String handleRequest(String xmlStream,
                                String userId,
                                AccessType userPermission)
    {
        /** @todo implement authorization, using new 'userId' and
         * 'userPermission' parameters */
        /** @todo implement audit log generation using new 'userId' parameter */
        logger.debug("enter");
        StringWriter response = new StringWriter();
        InventoryMgrMsg msg = null;
        InventoryMgrResp resp = new InventoryMgrResp();
        Rc rc = new Rc();
        StatusResp status = new StatusResp();

        // validate input and preemptively return if invalid
        // - no need to clutter up the handling which ensues with constant
        //   checking
        // - invalid input really should result in an exception thrown by this
        //   method (let's update its caller to catch an exception!) but for now
        //   just return something that communicates what's wrong
        if ( userId == null || userId.equals("") )
        {
            logger.error("userId cannot be null and cannot be \"\"");
            rc.setFailure(1);
            status.setRc(rc);
            status.setDescription("userId must be supplied");
            try
            {
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }
            catch (MarshalException ex2)
            {
                logger.error("MarshalException caught: " + ex2);
                return null;
            }
            catch (ValidationException ex2)
            {
                logger.error("ValidationException caught: " + ex2);
                return null;
            }
        }

        if ( userPermission == null )
        {
            logger.error("userPermission cannot be null");
            rc.setFailure(1);
            status.setRc(rc);
            status.setDescription("userPermission must be supplied");
            try
            {
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }
            catch (MarshalException ex2)
            {
                logger.error("MarshalException caught: " + ex2);
                return null;
            }
            catch (ValidationException ex2)
            {
                logger.error("ValidationException caught: " + ex2);
                return null;
            }
        }

        try
        {
            msg = (InventoryMgrMsg)InventoryMgrMsg.unmarshalInventoryMgrMsg(
                new StringReader(xmlStream));

            ApplicationViewMsg applicationViewMsg = msg.getApplicationViewMsg();
            if (applicationViewMsg != null)
            {
                ApplicationViewResp avResp =
                    processApplicationViewMsg(applicationViewMsg,
                                              userId,
                                              userPermission);
                resp.setApplicationViewResp(avResp);
                rc.setSuccess(0);
                status.setRc(rc);
                status.setDescription("message processed");
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            PhysicalViewMsg physicalViewMsg = msg.getPhysicalViewMsg();
            if (physicalViewMsg != null)
            {
                PhysicalViewResp pvResp =
                    processPhysicalViewMsg(physicalViewMsg,
                                           userId,
                                           userPermission);
                resp.setPhysicalViewResp(pvResp);
                rc.setSuccess(0);
                status.setRc(rc);
                status.setDescription("message processed");
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }

	    SolutionViewMsg solutionViewMsg = msg.getSolutionViewMsg();
            if (solutionViewMsg != null)
            {
                SolutionViewResp slnResp =
                    processSolutionViewMsg(solutionViewMsg,
                                           userId,
                                           userPermission);
                resp.setSolutionViewResp(slnResp);
                rc.setSuccess(0);
                status.setRc(rc);
                status.setDescription("message processed");
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            AppInstanceMsg aiMsg = msg.getAppInstanceMsg();
            if (aiMsg != null)
            {
                AppInstanceResp aiRsp =
                    processAppInstanceMsg(aiMsg, userId, userPermission);
                resp.setAppInstanceResp(aiRsp);
                rc.setSuccess(0);
                status.setRc(rc);
                status.setDescription("message processed");
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            AppTypeMsg atMsg = msg.getAppTypeMsg();
            if ( atMsg != null )
            {
                AppTypeResp atRsp =
                    processAppTypeMsg(atMsg, userId, userPermission);
                resp.setAppTypeResp(atRsp);
                rc.setSuccess(0);
                status.setRc(rc);
                status.setDescription("message processed");
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            HostMsg hMsg = msg.getHostMsg();
            if ( hMsg != null )
            {
                HostResp hRsp = processHostMsg(hMsg, userId, userPermission);
                resp.setHostResp(hRsp);
                rc.setSuccess(0);
                status.setRc(rc);
                status.setDescription("message processed");
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            InstrumentationMsg iMsg = msg.getInstrumentationMsg();
            if ( iMsg != null )
            {
                InstrumentationResp iRsp =
                    processInstrumentationMsg(iMsg, userId, userPermission);
                resp.setInstrumentationResp(iRsp);
                rc.setSuccess(0);
                status.setRc(rc);
                status.setDescription("message processed");
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            MgmtPolicyMsg mpMsg = msg.getMgmtPolicyMsg();
            if ( mpMsg != null )
            {
                MgmtPolicyResp mpRsp =
                    processMgmtPolicyMsg(mpMsg, userId, userPermission);
                resp.setMgmtPolicyResp(mpRsp);
                rc.setSuccess(0);
                status.setRc(rc);
                status.setDescription("message processed");
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }

            rc.setFailure(1);
            status.setRc(rc);
            status.setDescription("unrecognized request type");
            status.marshal(response);
            resp.marshal(response);
            return response.toString();
        }
        catch (ValidationException ex)
        {
            logger.error("XML Validation Exception: " + ex.getMessage());
            rc.setFailure(1);
            status.setRc(rc);
            status.setDescription("XML Validation Exception: " + ex.getMessage());
            try
            {
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }
            catch (ValidationException ex1)
            {
            }
            catch (MarshalException ex1)
            {
            }
        }
        catch (MarshalException ex)
        {
            logger.error("XML Marshal Exception: " + ex.getMessage());
            rc.setFailure(1);
            status.setRc(rc);
            status.setDescription("XML Marshal Exception: " + ex.getMessage());
            try
            {
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }
            catch (ValidationException ex1)
            {
            }
            catch (MarshalException ex1)
            {
            }
        }
        catch (Exception ex)
        {
            logger.error("Exception: " + ex);
            rc.setFailure(1);
            status.setRc(rc);
            status.setDescription("Exception: " + ex.getMessage());
            try
            {
                status.marshal(response);
                resp.marshal(response);
                return response.toString();
            }
            catch (ValidationException ex1)
            {
            }
            catch (MarshalException ex1)
            {
            }
        }
        /**/
        return response.toString();
    }

    private ApplicationViewResp
        processApplicationViewMsg(ApplicationViewMsg msg,
                                  String userId,
                                  AccessType userPermission)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;
        String logMessage = null;
        ApplicationViewResp resp = new ApplicationViewResp();
        String getRootMsg = msg.getAvMsgGetRoot();
        if (getRootMsg != null)
        {
            ContainerNode root = NbapiMsgHelper.toContainerNode(m_avm.root());
            AvRspGetRoot avRspGetRoot = new AvRspGetRoot();
            avRspGetRoot.setNode(root);
            resp.setAvRspGetRoot(avRspGetRoot);
            return resp;
        }

        AvMsgCreateNode createNodeMsg = msg.getAvMsgCreateNode();
        if (createNodeMsg != null)
        {
            String name = createNodeMsg.getName();
            NodeId parentId = createNodeMsg.getParentNodeId();
            if ( !userPermission.isWriteAccess() )
            {
                logMessage = AuditGlobals.AuthorizationFailureIndicator +
                             ": request to add new child node \"" + name +
                             "\" to existing node rejected";
                updateAuditLog(AuditAction.Create,
                               toAuditLogSubject(parentId, m_avm),
                               userId,
                               logMessage);
                reason = "permission denied";
                logger.info(reason + ": user " + userId + ", access " +
                            userPermission.toString());
                throw new Exception(reason);
            }
            com.cisco.eManager.eManager.inventory.view.ContainerNode newImNode
                = null;
            com.cisco.eManager.eManager.inventory.view.ContainerNode parentImNode
                = null;
            com.cisco.eManager.eManager.inventory.view.Node
                imNode = getNode(m_avm, parentId);
            if (imNode == null)
            {
                throw new Exception("parent node not found");
            }
            if (!(imNode instanceof
                  com.cisco.eManager.eManager.inventory.view.ContainerNode))
            {
                throw new Exception("parent node must be a container node");
            }

            parentImNode =
                (com.cisco.eManager.eManager.inventory.view.ContainerNode)imNode;
            try
            {
                newImNode = m_avm.createContainerNode(name, parentImNode);
            }
            catch (Exception ex)
            {
                throw new Exception("exception caught while creating new container node: " + ex);
            }
            AvRspCreateNode createNodeRsp = new AvRspCreateNode();
            createNodeRsp.setNode(NbapiMsgHelper.toContainerNode(newImNode));
            resp.setAvRspCreateNode(createNodeRsp);
            return resp;
        }

        AvMsgFindNodes findNodesMsg = msg.getAvMsgFindNodes();
        if (findNodesMsg != null)
        {
            NodeId searchRootId = findNodesMsg.getSearchRootNodeId();
            ViewSearchCriteria searchCriteria = findNodesMsg.getSearchCriteria();
            List resultSet = findNodes(m_avm, searchCriteria, searchRootId);
            AvRspFindNodes findNodesRsp = new AvRspFindNodes();
            findNodesRsp.setAppInstanceNodes(getAppInstanceNodes(resultSet));
            findNodesRsp.setAppTypeNodes(getAppTypeNodes(resultSet));
            findNodesRsp.setContainerNodes(getContainerNodes(resultSet));
            resp.setAvRspFindNodes(findNodesRsp);
            return resp;
        }

        AvMsgMoveNode moveNodeMsg = msg.getAvMsgMoveNode();
        if (moveNodeMsg != null)
        {
            NodeId nodeId = moveNodeMsg.getNodeId();
            if ( !userPermission.isWriteAccess() )
            {
                String fdnStr = toFdn(nodeId, m_avm);
                logMessage = AuditGlobals.AuthorizationFailureIndicator +
                             ": request to move node \"" + fdnStr + "\"rejected";
                updateAuditLog(AuditAction.Update,
                               toAuditLogSubject(nodeId, m_avm),
                               userId,
                               logMessage);
                reason = "permission denied";
                logger.info(reason + ": user " + userId + ", access " +
                            userPermission.toString());
                throw new Exception(reason);
            }

            NodeId newParentNodeId = moveNodeMsg.getParentNodeId();
            moveNode(m_avm, nodeId, newParentNodeId);
            AvRspMoveNode moveNodeRsp = new AvRspMoveNode();
            resp.setAvRspMoveNode(moveNodeRsp);
            return resp;
        }

        NodeId deleteNodeMsg = msg.getAvMsgDeleteNode();
        if (deleteNodeMsg != null)
        {
            if ( !userPermission.isWriteAccess() )
            {
                logMessage = AuditGlobals.AuthorizationFailureIndicator +
                             ": request to delete node rejected";
                updateAuditLog(AuditAction.Delete,
                               toAuditLogSubject(deleteNodeMsg, m_avm),
                               userId,
                               logMessage);
                reason = "permission denied";
                logger.info(reason + ": user " + userId + ", access " +
                            userPermission.toString());
                throw new Exception(reason);
            }
            deleteNode(m_avm, deleteNodeMsg);
            AvRspDeleteNode deleteNodeRsp = new AvRspDeleteNode();
            resp.setAvRspDeleteNode(deleteNodeRsp);
            return resp;
        }

        NodeId getChildrenMsg = msg.getAvMsgGetChildren();
        if (getChildrenMsg != null)
        {
            AvRspGetChildren getChildrenRsp = new AvRspGetChildren();
            getChildrenRsp.setAppInstanceNodes(
                appInstanceNodes(m_avm, getChildrenMsg));
            getChildrenRsp.setAppTypeNodes(appTypeNodes(m_avm, getChildrenMsg));
            getChildrenRsp.setContainerNodes(
                containerNodes(m_avm, getChildrenMsg));
            resp.setAvRspGetChildren(getChildrenRsp);
            return resp;
        }

        throw new Exception("unrecognized apps view request");
    }

    private PhysicalViewResp processPhysicalViewMsg(PhysicalViewMsg msg,
                                                    String userId,
                                                    AccessType userPermission)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;
        PhysicalViewResp resp = new PhysicalViewResp();
        String getRootMsg = msg.getPvMsgGetRoot();
        if (getRootMsg != null)
        {
            ContainerNode root = NbapiMsgHelper.toContainerNode(m_hvm.root());
            PvRspGetRoot avRspGetRoot = new PvRspGetRoot();
            avRspGetRoot.setNode(root);
            resp.setPvRspGetRoot(avRspGetRoot);
            return resp;
        }

        PvMsgCreateNode createNodeMsg = msg.getPvMsgCreateNode();
        if (createNodeMsg != null)
        {
            if ( !userPermission.isWriteAccess() )
            {
                reason = "permission denied";
                logger.info(reason + ": user " + userId + ", access " +
                            userPermission.toString());
                throw new Exception(reason);
            }
            String name = createNodeMsg.getName();
            NodeId parentId = createNodeMsg.getParentNodeId();
            com.cisco.eManager.eManager.inventory.view.ContainerNode
                parentImNode = null;
            com.cisco.eManager.eManager.inventory.view.ContainerNode
                newImNode = null;
            com.cisco.eManager.eManager.inventory.view.Node
                imNode = getNode(m_hvm, parentId);
            if (imNode == null)
            {
                throw new Exception("parent node not found");
            }
            if (!(imNode instanceof
                  com.cisco.eManager.eManager.inventory.view.ContainerNode))
            {
                throw new Exception("parent node must be a container node");
            }

            parentImNode =
                (com.cisco.eManager.eManager.inventory.view.ContainerNode)
                imNode;
            try
            {
                newImNode = m_hvm.createContainerNode(name, parentImNode);
            }
            catch (Exception ex)
            {
                throw new Exception("exception caught while creating new container node: " + ex);
            }
            PvRspCreateNode createNodeRsp = new PvRspCreateNode();
            createNodeRsp.setNode(NbapiMsgHelper.toContainerNode(newImNode));
            resp.setPvRspCreateNode(createNodeRsp);
            return resp;
        }

        PvMsgFindNodes findNodesMsg = msg.getPvMsgFindNodes();
        if (findNodesMsg != null)
        {
            NodeId searchRootId = findNodesMsg.getSearchRootNodeId();
            ViewSearchCriteria searchCriteria = findNodesMsg.getSearchCriteria();
            List resultSet = findNodes(m_hvm, searchCriteria, searchRootId);
            PvRspFindNodes findNodesRsp = new PvRspFindNodes();
            findNodesRsp.setAppInstanceNodes(getAppInstanceNodes(resultSet));
            findNodesRsp.setHostNodes(getHostNodes(resultSet));
            findNodesRsp.setContainerNodes(getContainerNodes(resultSet));
            resp.setPvRspFindNodes(findNodesRsp);
            return resp;
        }

        PvMsgMoveNode moveNodeMsg = msg.getPvMsgMoveNode();
        if (moveNodeMsg != null)
        {
            if ( !userPermission.isWriteAccess() )
            {
                reason = "permission denied";
                logger.info(reason + ": user " + userId + ", access " +
                            userPermission.toString());
                throw new Exception(reason);
            }
            NodeId nodeId = moveNodeMsg.getNodeId();
            NodeId newParentNodeId = moveNodeMsg.getParentNodeId();
            moveNode(m_hvm, nodeId, newParentNodeId);
            PvRspMoveNode moveNodeRsp = new PvRspMoveNode();
            resp.setPvRspMoveNode(moveNodeRsp);
            return resp;
        }

        NodeId deleteNodeMsg = msg.getPvMsgDeleteNode();
        if (deleteNodeMsg != null)
        {
            if ( !userPermission.isWriteAccess() )
            {
                reason = "permission denied";
                logger.info(reason + ": user " + userId + ", access " +
                            userPermission.toString());
                throw new Exception(reason);
            }
            deleteNode(m_hvm, deleteNodeMsg);
            PvRspDeleteNode deleteNodeRsp = new PvRspDeleteNode();
            resp.setPvRspDeleteNode(deleteNodeRsp);
            return resp;
        }

        NodeId getChildrenMsg = msg.getPvMsgGetChildren();
        if (getChildrenMsg != null)
        {
            PvRspGetChildren getChildrenRsp = new PvRspGetChildren();
            getChildrenRsp.setAppInstanceNodes(
                appInstanceNodes(m_hvm, getChildrenMsg));
            getChildrenRsp.setHostNodes(hostNodes(m_hvm, getChildrenMsg));
            getChildrenRsp.setContainerNodes(
                containerNodes(m_hvm, getChildrenMsg));
            resp.setPvRspGetChildren(getChildrenRsp);
            return resp;
        }

        throw new Exception("unrecognized physical view request");
    }

    private SolutionViewResp processSolutionViewMsg(SolutionViewMsg msg,
                                                    String userId,
                                                    AccessType userPermission)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;
        SolutionViewResp resp = new SolutionViewResp();
        String getRootMsg = msg.getSvMsgGetRoot();
        if (getRootMsg != null)
        {
            ContainerNode root = NbapiMsgHelper.toContainerNode(m_svm.root());
            SvRspGetRoot svRspGetRoot = new SvRspGetRoot();
            svRspGetRoot.setNode(root);
            resp.setSvRspGetRoot(svRspGetRoot);
            return resp;
        }

        SvMsgCreateNode createNodeMsg = msg.getSvMsgCreateNode();
        if (createNodeMsg != null)
        {
            if ( !userPermission.isWriteAccess() )
            {
                reason = "permission denied";
                logger.info(reason + ": user " + userId + ", access " +
                            userPermission.toString());
                throw new Exception(reason);
            }
            String name = createNodeMsg.getName();
            NodeId parentId = createNodeMsg.getParentNodeId();
            com.cisco.eManager.eManager.inventory.view.ContainerNode
                parentImNode = null;
            com.cisco.eManager.eManager.inventory.view.ContainerNode
                newImNode = null;
            com.cisco.eManager.eManager.inventory.view.Node
                imNode = getNode(m_svm, parentId);
            if (imNode == null)
            {
                throw new Exception("parent node not found");
            }
            if (!(imNode instanceof
                  com.cisco.eManager.eManager.inventory.view.ContainerNode))
            {
                throw new Exception("parent node must be a container node");
            }

            parentImNode =
                (com.cisco.eManager.eManager.inventory.view.ContainerNode)
                imNode;
            try
            {
                newImNode = m_svm.createContainerNode(name, parentImNode);
            }
            catch (Exception ex)
            {
                throw new Exception("exception caught while creating new container node: " + ex);
            }
            SvRspCreateNode createNodeRsp = new SvRspCreateNode();
            createNodeRsp.setNode(NbapiMsgHelper.toContainerNode(newImNode));
            resp.setSvRspCreateNode(createNodeRsp);
            return resp;
        }

        SvMsgFindNodes findNodesMsg = msg.getSvMsgFindNodes();
        if (findNodesMsg != null)
        {
            NodeId searchRootId = findNodesMsg.getSearchRootNodeId();
            ViewSearchCriteria searchCriteria = findNodesMsg.getSearchCriteria();
            List resultSet = findNodes(m_svm, searchCriteria, searchRootId);
            SvRspFindNodes findNodesRsp = new SvRspFindNodes();
            findNodesRsp.setAppInstanceNodes(getAppInstanceNodes(resultSet));
            findNodesRsp.setSolutionNodes(getSolutionNodes(resultSet));
            findNodesRsp.setContainerNodes(getContainerNodes(resultSet));
            resp.setSvRspFindNodes(findNodesRsp);
            return resp;
        }

        SvMsgMoveNode moveNodeMsg = msg.getSvMsgMoveNode();
        if (moveNodeMsg != null)
        {
            if ( !userPermission.isWriteAccess() )
            {
                reason = "permission denied";
                logger.info(reason + ": user " + userId + ", access " +
                            userPermission.toString());
                throw new Exception(reason);
            }
            NodeId nodeId = moveNodeMsg.getNodeId();
            NodeId newParentNodeId = moveNodeMsg.getParentNodeId();
            moveNode(m_svm, nodeId, newParentNodeId);
            SvRspMoveNode moveNodeRsp = new SvRspMoveNode();
            resp.setSvRspMoveNode(moveNodeRsp);
            return resp;
        }

        NodeId deleteNodeMsg = msg.getSvMsgDeleteNode();
        if (deleteNodeMsg != null)
        {
            if ( !userPermission.isWriteAccess() )
            {
                reason = "permission denied";
                logger.info(reason + ": user " + userId + ", access " +
                            userPermission.toString());
                throw new Exception(reason);
            }
            deleteNode(m_svm, deleteNodeMsg);
            SvRspDeleteNode deleteNodeRsp = new SvRspDeleteNode();
            resp.setSvRspDeleteNode(deleteNodeRsp);
            return resp;
        }

        NodeId getChildrenMsg = msg.getSvMsgGetChildren();
        if (getChildrenMsg != null)
        {
            SvRspGetChildren getChildrenRsp = new SvRspGetChildren();
            getChildrenRsp.setAppInstanceNodes(
                appInstanceNodes(m_svm, getChildrenMsg));
            getChildrenRsp.setSolutionNodes(solutionNodes(m_svm, getChildrenMsg));
            getChildrenRsp.setContainerNodes(
                containerNodes(m_svm, getChildrenMsg));
            resp.setSvRspGetChildren(getChildrenRsp);
            return resp;
        }

        throw new Exception("unrecognized solution view request");
    }

    private AppInstanceResp processAppInstanceMsg(AppInstanceMsg aiMsg,
                                                  String userId,
                                                  AccessType userPermission)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;
        AppInstanceResp resp = new AppInstanceResp();

        AiMsgGetAppInstances getAppInstancesMsg = aiMsg.getAiMsgGetAppInstances();
        if ( getAppInstancesMsg != null )
        {
            AiRspGetAppInstances getAppInstancesRsp =
                processAiMsgGetAppInstances(getAppInstancesMsg);
            resp.setAiRspGetAppInstances(getAppInstancesRsp);
            return resp;
        }

        AppInstanceId manageAppInstanceMsg = aiMsg.getAiMsgManage();
        if ( manageAppInstanceMsg != null )
        {
            if ( !userPermission.isWriteAccess() )
            {
                reason = "permission denied";
                logger.info(reason + ": user " + userId + ", access " +
                            userPermission.toString());
                throw new Exception(reason);
            }
            AiRspManage manageAppInstanceRsp =
                processAiMsgManage(manageAppInstanceMsg);
            resp.setAiRspManage(manageAppInstanceRsp);
            return resp;
        }

        AppInstanceId unmanageAppInstanceMsg = aiMsg.getAiMsgUnmanage();
        if ( unmanageAppInstanceMsg != null )
        {
            if ( !userPermission.isWriteAccess() )
            {
                reason = "permission denied";
                logger.info(reason + ": user " + userId + ", access " +
                            userPermission.toString());
                throw new Exception(reason);
            }
            AiRspUnmanage unmanageAppInstanceRsp =
                processAiMsgUnmanage(unmanageAppInstanceMsg);
            resp.setAiRspUnmanage(unmanageAppInstanceRsp);
            return resp;
        }

        throw new Exception("unrecognized AppInstance request");
    }

    private AiRspGetAppInstances processAiMsgGetAppInstances(
                                                       AiMsgGetAppInstances msg)
        throws Exception
    {
        logger.debug("enter");
        AiRspGetAppInstances rsp = new AiRspGetAppInstances();
        AppInstance[] appInstances = null;
        com.cisco.eManager.eManager.inventory.appInstance.AppInstance
            imAppInstance = null;

        String all = msg.getAll();
        if (all != null)
        {
            appInstances = NbapiMsgHelper.toAppInstances(m_aim.appInstances());
            rsp.setAppInstances(appInstances);
            return rsp;
        }

        AppInstanceId appInstanceId = msg.getAppInstanceId();
        if (appInstanceId != null)
        {
            imAppInstance = getAppInstance(appInstanceId);
            if (imAppInstance != null)
            {
                appInstances = new AppInstance[1];
                appInstances[0] = NbapiMsgHelper.toAppInstance(imAppInstance);
            }
            else
            {
                appInstances = new AppInstance[0];
            }
            rsp.setAppInstances(appInstances);
            return rsp;
        }

        AppTypeId appTypeId = msg.getAppTypeId();
        if (appTypeId != null)
        {
            List imAppInstances = getAppInstances(appTypeId);
            int size = imAppInstances.size();
            appInstances = new AppInstance[size];
            Iterator iter = imAppInstances.iterator();
            for (int i = 0; i < size; i++)
            {
                imAppInstance =
                    (com.cisco.eManager.eManager.inventory.appInstance.AppInstance)iter.next();
                appInstances[i] = NbapiMsgHelper.toAppInstance(imAppInstance);
            }
            rsp.setAppInstances(appInstances);
            return rsp;
        }

        HostId hostId = msg.getHostId();
        if (hostId != null)
        {
            List imAppInstances = getAppInstances(hostId);
            int size = imAppInstances.size();
            appInstances = new AppInstance[size];
            Iterator iter = imAppInstances.iterator();
            for (int i = 0; i < size; i++)
            {
                imAppInstance =
                    (com.cisco.eManager.eManager.inventory.appInstance.AppInstance)iter.next();
                appInstances[i] = NbapiMsgHelper.toAppInstance(imAppInstance);
            }
            rsp.setAppInstances(appInstances);
            return rsp;
        }
        throw new Exception("unrecognized search criteria");
    }

    private AiRspManage processAiMsgManage(AppInstanceId appInstanceId)
        throws Exception
    {
        logger.debug("enter");
        AiRspManage rsp = new AiRspManage();
        com.cisco.eManager.eManager.inventory.appInstance.AppInstance
            imAi = getAppInstance(appInstanceId);
        if ( imAi == null )
        {
            String reason = "unable to locate specified appInstance";
            logger.info(reason);
            throw new Exception(reason);
        }
        m_aim.manage(imAi.id());
        return rsp;
    }

    private AiRspUnmanage processAiMsgUnmanage(AppInstanceId appInstanceId)
        throws Exception
    {
        logger.debug("enter");
        AiRspUnmanage rsp = new AiRspUnmanage();
        com.cisco.eManager.eManager.inventory.appInstance.AppInstance
            imAi = getAppInstance(appInstanceId);
        if ( imAi == null )
        {
            String reason = "unable to locate specified appInstance";
            logger.info(reason);
            throw new Exception(reason);
        }
        m_aim.unmanage(imAi.id());
        return rsp;
    }

    private AppTypeResp processAppTypeMsg(AppTypeMsg atMsg,
                                          String userId,
                                          AccessType userPermission)
        throws Exception
    {
        logger.debug("enter");
        AppTypeResp resp = new AppTypeResp();

        AtMsgGetAppTypes getAppTypesMsg = atMsg.getAtMsgGetAppTypes();
        if ( getAppTypesMsg != null )
        {
            AtRspGetAppTypes getAppTypesRsp = processAtMsgGetAppTypes(getAppTypesMsg);
            resp.setAtRspGetAppTypes(getAppTypesRsp);
            return resp;
        }

        throw new Exception("unrecognized AppInstance request");
    }

    private AtRspGetAppTypes processAtMsgGetAppTypes(AtMsgGetAppTypes msg)
        throws Exception
    {
        logger.debug("enter");
        AtRspGetAppTypes rsp = new AtRspGetAppTypes();
        com.cisco.eManager.eManager.inventory.appType.AppType imAppType = null;
        AppType[] appTypes = null;

        String all = msg.getAll();
        if ( all != null )
        {
            appTypes = NbapiMsgHelper.toAppTypes(m_atm.allAppTypes());
            rsp.setAppTypes(appTypes);
            return rsp;
        }

        AppTypeId appTypeId = msg.getAppTypeId();
        if ( appTypeId != null )
        {
            imAppType = getAppType(appTypeId);
            if ( imAppType == null )
            {
                appTypes = new AppType[0];
            }
            else
            {
                appTypes = new AppType[1];
                appTypes[0] = NbapiMsgHelper.toAppType(imAppType);
            }
            rsp.setAppTypes(appTypes);
            return rsp;
        }

        HostId hostId = msg.getHostId();
        if ( hostId != null )
        {
            List imAppTypes = getAppTypes(hostId);
            int appTypeCount = imAppTypes.size();
            appTypes = new AppType[appTypeCount];
            Iterator iter = imAppTypes.iterator();
            for (int i = 0; i < appTypeCount; i++)
            {
                imAppType = (com.cisco.eManager.eManager.inventory.appType.AppType)iter.next();
                appTypes[i] = NbapiMsgHelper.toAppType(imAppType);
            }
            rsp.setAppTypes(appTypes);
            return rsp;
        }

        throw new Exception("unrecognized appType search criteria");
    }

    private HostResp processHostMsg(HostMsg hMsg,
                                    String userId,
                                    AccessType userPermission
)
        throws Exception
    {
        logger.debug("enter");
        HostResp resp = new HostResp();

        HMsgGetHosts getHostsMsg = hMsg.getHMsgGetHosts();
        if ( getHostsMsg != null )
        {
            HRspGetHosts getHostsRsp = processHMsgGetHosts(getHostsMsg);
            resp.setHRspGetHosts(getHostsRsp);
            return resp;
        }

        throw new Exception("unrecognized host request");
    }

    private HRspGetHosts processHMsgGetHosts(HMsgGetHosts msg)
        throws Exception
    {
        logger.debug("enter");
        HRspGetHosts rsp = new HRspGetHosts();
        com.cisco.eManager.eManager.inventory.host.Host imHost = null;
        Host[] hosts = null;

        String all = msg.getAll();
        if ( all != null )
        {
            hosts = NbapiMsgHelper.toHosts(m_hm.allHosts());
            rsp.setHosts(hosts);
            return rsp;
        }

        HostId hostId = msg.getHostId();
        if ( hostId != null )
        {
            imHost = getHost(hostId);
            if ( imHost == null )
            {
                hosts = new Host[0];
            }
            else
            {
                hosts = new Host[1];
                hosts[0] = NbapiMsgHelper.toHost(imHost);
            }
            rsp.setHosts(hosts);
            return rsp;
        }
        throw new Exception("unrecognized appType search criteria");
    }

    private InstrumentationResp
        processInstrumentationMsg(InstrumentationMsg iMsg,
                                  String userId,
                                  AccessType userPermission)
        throws Exception
    {
        logger.debug("enter");
        InstrumentationResp resp = new InstrumentationResp();

        IMsgFindInstrumentations findInstrumentationsMsg =
            iMsg.getIMsgFindInstrumentations();
        if ( findInstrumentationsMsg != null )
        {
            IRspFindInstrumentations findInstrumentationsRsp =
                processIMsgFindInstrumentations(findInstrumentationsMsg);
            resp.setIRspFindInstrumentations(findInstrumentationsRsp);
            return resp;
        }

        IMsgGetMethods getMethodsMsg = iMsg.getIMsgGetMethods();
        if ( getMethodsMsg != null )
        {
            IRspGetMethods getMethodsRsp = processIMsgGetMethods(getMethodsMsg);
            resp.setIRspGetMethods(getMethodsRsp);
            return resp;
        }

        throw new Exception("unrecognized instrumentation request");
    }

    private IRspFindInstrumentations
        processIMsgFindInstrumentations(IMsgFindInstrumentations msg)
        throws Exception
    {
        logger.debug("enter");
        IRspFindInstrumentations rsp = new IRspFindInstrumentations();
        Instrumentation[] instrumentations = null;
        com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation
            imInstrumentation = null;

        ManagedObjectId instrumentationId = msg.getInstrumentationId();
        if ( instrumentationId != null )
        {
            imInstrumentation =
                m_im.find(NbapiMsgHelper.toImManagedObjectId(instrumentationId));
            if ( imInstrumentation == null )
            {
                instrumentations = new Instrumentation[0];
            }
            else
            {
                instrumentations = new Instrumentation[1];
                instrumentations[0] =
                    NbapiMsgHelper.toInstrumentation(imInstrumentation);
            }
            rsp.setInstrumentations(instrumentations);
            return rsp;
        }

        String instrumentationName = msg.getInstrumentationName();
        if ( instrumentationName != null )
        {
            List imInstrumentations = m_im.findByName(instrumentationName);
            instrumentations = NbapiMsgHelper.toInstrumentations(imInstrumentations);
            rsp.setInstrumentations(instrumentations);
            return rsp;
        }

        AppInstanceId appInstanceId = msg.getAppInstanceId();
        if ( appInstanceId != null )
        {
            com.cisco.eManager.eManager.inventory.appInstance.AppInstance imAi =
                getAppInstance(appInstanceId);
            List imInstrumentations = imAi.instrumentations();
            instrumentations = NbapiMsgHelper.toInstrumentations(imInstrumentations);
            rsp.setInstrumentations(instrumentations);
            return rsp;
        }

        throw new Exception("unrecognized instrumentation search criteria");
    }

    private IRspGetMethods processIMsgGetMethods(IMsgGetMethods msg)
        throws Exception
    {
        logger.debug("enter");
        IRspGetMethods rsp = new IRspGetMethods();
        Method[] methods = null;
        com.cisco.eManager.eManager.inventory.instrumentation.Method imMethod = null;
        ManagedObjectId instrumentationId = msg.getInstrumentationId();

        com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation
            imInstrumentation =
            m_im.find(NbapiMsgHelper.toImManagedObjectId(instrumentationId));

        if ( imInstrumentation == null )
        {
            throw new Exception("instrumentation ID does not resolve to an instrumentation object");
        }

        String methodName = msg.getSearchCriteria().getMethodName();
        if ( methodName != null )
        {
            imMethod = imInstrumentation.method(methodName);
            if ( imMethod == null )
            {
                methods = new Method[0];
            }
            else
            {
                methods = new Method[1];
                methods[0] = NbapiMsgHelper.toMethod(imMethod);
            }
            rsp.setMethods(methods);
            return rsp;
        }

        String all = msg.getSearchCriteria().getAll();
        if ( all != null )
        {
            methods = NbapiMsgHelper.toMethods(imInstrumentation.methods());
            rsp.setMethods(methods);
            return rsp;
        }

        throw new Exception("unrecognized method search criteria");
    }

    private MgmtPolicyResp processMgmtPolicyMsg(MgmtPolicyMsg mpMsg,
                                                String userId,
                                                AccessType userPermission)
        throws Exception
    {
        logger.debug("enter");
        MgmtPolicyResp resp = new MgmtPolicyResp();

        MpMsgGetMgmtPolicies
            getMgmtPoliciesMsg = mpMsg.getMpMsgGetMgmtPolicies();
        if ( getMgmtPoliciesMsg != null )
        {
            MpRspGetMgmtPolicies getMgmtPoliciesRsp =
                processMpMsgGetMgmtPolicies(getMgmtPoliciesMsg);
            resp.setMpRspGetMgmtPolicies(getMgmtPoliciesRsp);
            return resp;
        }

        throw new Exception("unrecognized instrumentation request");
    }

    private MpRspGetMgmtPolicies
        processMpMsgGetMgmtPolicies(MpMsgGetMgmtPolicies msg)
        throws Exception
    {
        logger.debug("enter");
        MpRspGetMgmtPolicies rsp = new MpRspGetMgmtPolicies();
        MgmtPolicy[] mgmtPolicies = null;
        com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy imMgmtPolicy = null;
        List imMgmtPolicies = null;

        String all = msg.getAll();
        if ( all != null )
        {
            //List imMgmtPolicies = m_mpm.getAll();
            mgmtPolicies = NbapiMsgHelper.toMgmtPolicies(m_mpm.mgmtPolicies());
            rsp.setMgmtPolicies(mgmtPolicies);
            return rsp;
        }

        ManagedObjectId mgmtPolicyId = msg.getMgmtPolicyId();
        if ( mgmtPolicyId != null )
        {
            imMgmtPolicy =
                m_mpm.find(NbapiMsgHelper.toImManagedObjectId(mgmtPolicyId));
            if ( imMgmtPolicy == null )
            {
                mgmtPolicies = new MgmtPolicy[0];
            }
            else
            {
                mgmtPolicies = new MgmtPolicy[1];
                mgmtPolicies[0] = NbapiMsgHelper.toMgmtPolicy(imMgmtPolicy);
            }
            rsp.setMgmtPolicies(mgmtPolicies);
            return rsp;
        }

        AppTypeId appTypeId = msg.getAppTypeId();
        if ( appTypeId != null )
        {
            com.cisco.eManager.eManager.inventory.appType.AppType imAt =
                getAppType(appTypeId);
            mgmtPolicies = NbapiMsgHelper.toMgmtPolicies(
                m_mpm.findByAppTypeId_preferred(imAt.id()));
            rsp.setMgmtPolicies(mgmtPolicies);
            return rsp;
        }

        HostId hostId = msg.getHostId();
        if ( hostId != null )
        {
            com.cisco.eManager.eManager.inventory.host.Host imHost =
                getHost(hostId);
            mgmtPolicies = NbapiMsgHelper.toMgmtPolicies(
                m_mpm.findByHostId_preferred(imHost.id()));
            rsp.setMgmtPolicies(mgmtPolicies);
            return rsp;
        }

        throw new Exception("unrecognized management policy search criteria");
    }

    private List findNodes(
                      com.cisco.eManager.eManager.inventory.view.ViewManager vm,
                      ViewSearchCriteria sc,
                      NodeId srId)
        throws Exception
    {
        logger.debug("enter");
        List resultSet = new LinkedList();

        com.cisco.eManager.eManager.inventory.view.Node imSrNode = null;
        if ( srId == null )
        {
            imSrNode = vm.root();
        }
        else
        {
            imSrNode = getNode(vm, srId);
            if ( imSrNode == null )
            {
                String reason = "unable to locate specified search root";
                logger.info(reason);
                throw new Exception(reason);
            }
        }

        NodeId searchNodeId = sc.getNodeId();
        if ( searchNodeId != null )
        {
            com.cisco.eManager.eManager.inventory.view.Node imFoundNode = null;
            ManagedObjectId searchMoId = searchNodeId.getId();
            if ( searchMoId != null )
            {
                com.cisco.eManager.common.inventory.ManagedObjectId
                    imSearchMoId = NbapiMsgHelper.toImManagedObjectId(searchMoId);
                imFoundNode = vm.findNodeInSubtree(imSrNode, imSearchMoId);
            }
            else
            {
                FullyDistinguishedName fdn = searchNodeId.getFdn();
                if ( fdn == null )
                {
                    String reason = "search criteria's nodeId, when specified, "
                                    + "cannot be empty";
                    logger.info(reason);
                    throw new Exception(reason);
                }
                com.cisco.eManager.eManager.inventory.view.NodePath imNodePath
                    = NbapiMsgHelper.toImNodePath(fdn);
                imFoundNode = vm.findNodeInSubtree(imSrNode, imNodePath);
            }
            if (imFoundNode != null)
            {
                resultSet.add(imFoundNode);
            }
            return resultSet;
        }

        String nodeName = sc.getNodeName();
        if (nodeName != null)
        {
            resultSet = vm.findNodesInSubtree(imSrNode, nodeName);
            return resultSet;
        }

        String reason = "search criteria cannot be empty";
        logger.info(reason);
        throw new Exception(reason);
    }

    private AppInstanceNode[] getAppInstanceNodes(List resultSet)
    {
        logger.debug("enter");
        List appInstanceNodeList = new LinkedList();
        Object obj = null;
        com.cisco.eManager.eManager.inventory.view.AppInstanceNode imAppInstanceNode = null;
        Iterator iter = resultSet.iterator();
        while (iter.hasNext())
        {
            obj = iter.next();
            if (obj instanceof com.cisco.eManager.eManager.inventory.view.AppInstanceNode)
            {
                appInstanceNodeList.add(obj);
            }
        }
        int size = appInstanceNodeList.size();
        AppInstanceNode[] appInstanceNodes = new AppInstanceNode[size];
        iter = appInstanceNodeList.iterator();
        AppInstanceNode aiNode = null;
        for (int i = 0; i < size; i++)
        {
            imAppInstanceNode =
                (com.cisco.eManager.eManager.inventory.view.AppInstanceNode)iter.next();
            appInstanceNodes[i] =
                NbapiMsgHelper.toAppInstanceNode(imAppInstanceNode);
        }
        return appInstanceNodes;
    }

    private AppTypeNode[] getAppTypeNodes(List resultSet)
    {
        logger.debug("enter");
        List appTypeNodeList = new LinkedList();
        Object obj = null;
        com.cisco.eManager.eManager.inventory.view.AppTypeNode imAppTypeNode = null;
        Iterator iter = resultSet.iterator();
        while (iter.hasNext())
        {
            obj = iter.next();
            if (obj instanceof com.cisco.eManager.eManager.inventory.view.AppTypeNode)
            {
                appTypeNodeList.add(obj);
            }
        }
        int size = appTypeNodeList.size();
        AppTypeNode[] appTypeNodes = new AppTypeNode[size];
        iter = appTypeNodeList.iterator();
        AppTypeNode aiNode = null;
        for (int i = 0; i < size; i++)
        {
            imAppTypeNode =
                (com.cisco.eManager.eManager.inventory.view.AppTypeNode)iter.next();
            appTypeNodes[i] = NbapiMsgHelper.toAppTypeNode(imAppTypeNode);
        }
        return appTypeNodes;
    }

    private HostNode[] getHostNodes(List resultSet)
    {
        logger.debug("enter");
        List hostNodeList = new LinkedList();
        Object obj = null;
        com.cisco.eManager.eManager.inventory.view.HostNode imHostNode = null;
        Iterator iter = resultSet.iterator();
        while (iter.hasNext())
        {
            obj = iter.next();
            if (obj instanceof com.cisco.eManager.eManager.inventory.view.HostNode)
            {
                hostNodeList.add(obj);
            }
        }
        int size = hostNodeList.size();
        HostNode[] hostNodes = new HostNode[size];
        iter = hostNodeList.iterator();
        HostNode aiNode = null;
        for (int i = 0; i < size; i++)
        {
            imHostNode =
                (com.cisco.eManager.eManager.inventory.view.HostNode)iter.next();
            hostNodes[i] = NbapiMsgHelper.toHostNode(imHostNode);
        }
        return hostNodes;
    }

    private SolutionNode[] getSolutionNodes(List resultSet)
    {
        logger.debug("enter");
        List solutionNodeList = new LinkedList();
        Object obj = null;
        com.cisco.eManager.eManager.inventory.view.SolutionNode imSolutionNode = null;
        Iterator iter = resultSet.iterator();
        while (iter.hasNext())
        {
            obj = iter.next();
            if (obj instanceof com.cisco.eManager.eManager.inventory.view.SolutionNode)
            {
                solutionNodeList.add(obj);
            }
        }
        int size = solutionNodeList.size();
        SolutionNode[] solutionNodes = new SolutionNode[size];
        iter = solutionNodeList.iterator();
        SolutionNode aiNode = null;
        for (int i = 0; i < size; i++)
        {
            imSolutionNode =
                (com.cisco.eManager.eManager.inventory.view.SolutionNode)iter.next();
            solutionNodes[i] = NbapiMsgHelper.toSolutionNode(imSolutionNode);
        }
        return solutionNodes;
    }

    private ContainerNode[] getContainerNodes(List resultSet)
    {
        logger.debug("enter");
        List containerNodeList = new LinkedList();
        Object obj = null;
        com.cisco.eManager.eManager.inventory.view.ContainerNode imContainerNode = null;
        Iterator iter = resultSet.iterator();
        while (iter.hasNext())
        {
            obj = iter.next();
            if (obj instanceof com.cisco.eManager.eManager.inventory.view.ContainerNode)
            {
                containerNodeList.add(obj);
            }
        }
        int size = containerNodeList.size();
        ContainerNode[] containerNodes = new ContainerNode[size];
        iter = containerNodeList.iterator();
        ContainerNode aiNode = null;
        for (int i = 0; i < size; i++)
        {
            imContainerNode =
                (com.cisco.eManager.eManager.inventory.view.ContainerNode)iter.next();
            containerNodes[i] = NbapiMsgHelper.toContainerNode(imContainerNode);
        }
        return containerNodes;
    }

    private void moveNode(
        com.cisco.eManager.eManager.inventory.view.ViewManager vm,
        NodeId nodeId,
        NodeId newParentId)
        throws Exception
    {
        logger.debug("enter");
        com.cisco.eManager.eManager.inventory.view.Node
            imNode = getNode(vm, nodeId);
        if (imNode == null)
        {
            throw new Exception("node not found");
        }

        com.cisco.eManager.eManager.inventory.view.Node
            imNewParentNode = getNode(vm, newParentId);
        if (imNewParentNode == null)
        {
            throw new Exception("parent node not found");
        }

        if (!(imNewParentNode instanceof
              com.cisco.eManager.eManager.inventory.view.AbstractContainerNode))
        {
            throw new Exception("new parent node not a container node");
        }
        com.cisco.eManager.eManager.inventory.view.AbstractContainerNode acn
            = (com.cisco.eManager.eManager.inventory.view.AbstractContainerNode)
              imNewParentNode;

        vm.moveNode(imNode, acn);
    }

    private void deleteNode(
                      com.cisco.eManager.eManager.inventory.view.ViewManager vm,
                      NodeId nodeId)
        throws Exception
    {
        logger.debug("enter");
        com.cisco.eManager.eManager.inventory.view.Node
            imNode = getNode(vm, nodeId);
        if (imNode == null)
        {
            throw new Exception("node not found");
        }
        vm.removeNode(imNode);
    }

    private AppInstanceNode[] appInstanceNodes(
                      com.cisco.eManager.eManager.inventory.view.ViewManager vm,
                      NodeId parentNodeId)
    {
        logger.debug("enter");
        com.cisco.eManager.eManager.inventory.view.Node
            imNode = getNode(vm, parentNodeId);
        if (imNode == null)
        {
            return new AppInstanceNode[0];
        }
        if (!(imNode instanceof
              com.cisco.eManager.eManager.inventory.view.AbstractContainerNode))
        {
            return new AppInstanceNode[0];
        }
        if (imNode instanceof
            com.cisco.eManager.eManager.inventory.view.ContainerNode)
        {
            return new AppInstanceNode[0];
        }

        // node is either an appType node or a host node
        // - both can contain appInstance nodes
        com.cisco.eManager.eManager.inventory.view.AbstractContainerNode imAcn
            = (com.cisco.eManager.eManager.inventory.view.AbstractContainerNode)imNode;
        List children = imAcn.children();
        int size = children.size();
        AppInstanceNode[] resultSet = new AppInstanceNode[size];
        Iterator iter = children.iterator();
        com.cisco.eManager.eManager.inventory.view.AppInstanceNode imAin = null;
        for (int i = 0; i < size; i++)
        {
            imAin =
                (com.cisco.eManager.eManager.inventory.view.AppInstanceNode)
                iter.next();
            resultSet[i] = NbapiMsgHelper.toAppInstanceNode(imAin);
        }
        return resultSet;
    }

    private AppTypeNode[] appTypeNodes(
                      com.cisco.eManager.eManager.inventory.view.ViewManager vm,
                      NodeId parentNodeId)
    {
        logger.debug("enter");
        com.cisco.eManager.eManager.inventory.view.Node
            imNode = getNode(vm, parentNodeId);
        if (imNode == null)
        {
            return new AppTypeNode[0];
        }
        if (!(imNode instanceof
              com.cisco.eManager.eManager.inventory.view.ContainerNode))
        {
            return new AppTypeNode[0];
        }

        com.cisco.eManager.eManager.inventory.view.ContainerNode imCn
            = (com.cisco.eManager.eManager.inventory.view.ContainerNode)imNode;
        List children = imCn.children();
        Iterator iter = children.iterator();
        List imAppTypeNodes = new LinkedList();
        imNode = null;
        while (iter.hasNext())
        {
            imNode = (com.cisco.eManager.eManager.inventory.view.Node)iter.next();
            if (imNode instanceof com.cisco.eManager.eManager.inventory.view.AppTypeNode)
            {
                imAppTypeNodes.add(imNode);
            }
        }
        int size = imAppTypeNodes.size();
        AppTypeNode[] resultSet = new AppTypeNode[size];
        iter = imAppTypeNodes.iterator();
        com.cisco.eManager.eManager.inventory.view.AppTypeNode imAppTypeNode;
        for (int i = 0; i < size; i++)
        {
            imAppTypeNode =
                (com.cisco.eManager.eManager.inventory.view.AppTypeNode)iter.next();
            resultSet[i] = NbapiMsgHelper.toAppTypeNode(imAppTypeNode);
        }
        return resultSet;
    }

    private HostNode[] hostNodes(
                      com.cisco.eManager.eManager.inventory.view.ViewManager vm,
                      NodeId parentNodeId)
    {
        logger.debug("enter");
        com.cisco.eManager.eManager.inventory.view.Node
            imNode = getNode(vm, parentNodeId);
        if (imNode == null)
        {
            return new HostNode[0];
        }
        if (!(imNode instanceof com.cisco.eManager.eManager.inventory.view.ContainerNode))
        {
            return new HostNode[0];
        }

        com.cisco.eManager.eManager.inventory.view.ContainerNode imCn
            = (com.cisco.eManager.eManager.inventory.view.ContainerNode)imNode;
        List children = imCn.children();
        Iterator iter = children.iterator();
        List imHostNodes = new LinkedList();
        imNode = null;
        while (iter.hasNext())
        {
            imNode = (com.cisco.eManager.eManager.inventory.view.Node)iter.next();
            if (imNode instanceof com.cisco.eManager.eManager.inventory.view.HostNode)
            {
                imHostNodes.add(imNode);
            }
        }
        int size = imHostNodes.size();
        HostNode[] resultSet = new HostNode[size];
        iter = imHostNodes.iterator();
        com.cisco.eManager.eManager.inventory.view.HostNode imHostNode;
        for (int i = 0; i < size; i++)
        {
            imHostNode =
                (com.cisco.eManager.eManager.inventory.view.HostNode)iter.next();
            resultSet[i] = NbapiMsgHelper.toHostNode(imHostNode);
        }
        return resultSet;
    }

    private SolutionNode[] solutionNodes(com.cisco.eManager.eManager.inventory.view.ViewManager vm,
					 NodeId parentNodeId)
    {
        logger.debug("enter");
        com.cisco.eManager.eManager.inventory.view.Node
            imNode = getNode(vm, parentNodeId);
        if (imNode == null)
        {
            return new SolutionNode[0];
        }
        if (!(imNode instanceof com.cisco.eManager.eManager.inventory.view.ContainerNode))
        {
            return new SolutionNode[0];
        }

        com.cisco.eManager.eManager.inventory.view.ContainerNode imCn
            = (com.cisco.eManager.eManager.inventory.view.ContainerNode)imNode;
        List children = imCn.children();
        Iterator iter = children.iterator();
        List imSolutionNodes = new LinkedList();
        imNode = null;
        while (iter.hasNext())
        {
            imNode = (com.cisco.eManager.eManager.inventory.view.Node)iter.next();
            if (imNode instanceof com.cisco.eManager.eManager.inventory.view.SolutionNode)
            {
                imSolutionNodes.add(imNode);
            }
        }
        int size = imSolutionNodes.size();
        SolutionNode[] resultSet = new SolutionNode[size];
        iter = imSolutionNodes.iterator();
        com.cisco.eManager.eManager.inventory.view.SolutionNode imSolutionNode;
        for (int i = 0; i < size; i++)
        {
            imSolutionNode =
                (com.cisco.eManager.eManager.inventory.view.SolutionNode)iter.next();
            resultSet[i] = NbapiMsgHelper.toSolutionNode(imSolutionNode);
        }
        return resultSet;
    }

    private ContainerNode[] containerNodes(
                      com.cisco.eManager.eManager.inventory.view.ViewManager vm,
                      NodeId parentNodeId)
    {
        logger.debug("enter");
        com.cisco.eManager.eManager.inventory.view.Node
            imNode = getNode(vm, parentNodeId);
        if (imNode == null)
        {
            return new ContainerNode[0];
        }
        if (!(imNode instanceof
              com.cisco.eManager.eManager.inventory.view.ContainerNode))
        {
            return new ContainerNode[0];
        }

        com.cisco.eManager.eManager.inventory.view.ContainerNode imCn
            = (com.cisco.eManager.eManager.inventory.view.ContainerNode)imNode;
        List children = imCn.children();
        Iterator iter = children.iterator();
        List imContainerNodes = new LinkedList();
        imNode = null;
        while (iter.hasNext())
        {
            imNode =
                (com.cisco.eManager.eManager.inventory.view.Node)iter.next();
            if (imNode instanceof
                com.cisco.eManager.eManager.inventory.view.ContainerNode)
            {
                imContainerNodes.add(imNode);
            }
        }
        int size = imContainerNodes.size();
        ContainerNode[] resultSet = new ContainerNode[size];
        iter = imContainerNodes.iterator();
        com.cisco.eManager.eManager.inventory.view.ContainerNode
            imContainerNode;
        for (int i = 0; i < size; i++)
        {
            imContainerNode =
                (com.cisco.eManager.eManager.inventory.view.ContainerNode)
                iter.next();
            resultSet[i] = NbapiMsgHelper.toContainerNode(imContainerNode);
        }
        return resultSet;
    }

    private com.cisco.eManager.eManager.inventory.view.Node
        getNode(com.cisco.eManager.eManager.inventory.view.ViewManager vm,
                NodeId id)
    {
        logger.debug("enter");
        com.cisco.eManager.eManager.inventory.view.Node node = null;
        FullyDistinguishedName fdn = id.getFdn();
        if ( fdn != null )
        {
            node = getNode(vm, fdn);
        }
        else
        {
            ManagedObjectId moId = id.getId();
            node = vm.find(NbapiMsgHelper.toImManagedObjectId(moId));
        }
        return node;
    }

    private com.cisco.eManager.eManager.inventory.view.Node
        getNode(com.cisco.eManager.eManager.inventory.view.ViewManager vm,
                FullyDistinguishedName fdn)
    {
        logger.debug("enter");
        com.cisco.eManager.eManager.inventory.view.Node node = null;
        if ( fdn != null )
        {
            com.cisco.eManager.eManager.inventory.view.NodePath nodePath =
                NbapiMsgHelper.toImNodePath(fdn);
            node = vm.find(nodePath);
        }
        return node;
    }

    private com.cisco.eManager.eManager.inventory.appInstance.AppInstance
        getAppInstance(AppInstanceId aiId)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;
        ManagedObjectId id = aiId.getId();
        if ( id != null )
        {
            return m_aim.find(NbapiMsgHelper.toImManagedObjectId(id));
        }

        AppTypeInstanceName atin = aiId.getTypeAndName();
        if ( atin != null )
        {
            com.cisco.eManager.eManager.inventory.appType.AppType imAppType =
                getAppType(atin.getAppTypeId());
            if ( imAppType == null )
            {
                reason = "AppTypeId does not resolve to any appType";
                logger.info(reason);
                throw new Exception(reason);
            }
            return m_aim.find1(atin.getName(), imAppType.id());
        }

        FullyDistinguishedName fdn = aiId.getAvFdn();
        com.cisco.eManager.eManager.inventory.view.NodePath nodePath = null;
        com.cisco.eManager.eManager.inventory.view.Node node = null;
        com.cisco.eManager.eManager.inventory.view.AppInstanceNode aiNode = null;
        if ( fdn != null )
        {
            node = getNode(m_avm, fdn);
            if ( node == null )
            {
                logger.debug("fdn " + fdn.toString() + " does not exist in " +
                             "the apps view");
                return null;
            }

            if ( node instanceof
                 com.cisco.eManager.eManager.inventory.view.AppInstanceNode )
            {
                aiNode =
                    (com.cisco.eManager.eManager.inventory.view.AppInstanceNode)
                    node;
                return aiNode.appInstance();
            }
            else
            {
                reason = "fdn does not resolve to an appInstanceNode";
                logger.info(reason);
                throw new Exception(reason);
            }
        }

        fdn = aiId.getPvFdn();
        if ( fdn != null )
        {
            node = getNode(m_hvm, fdn);
            if ( node == null )
            {
                logger.debug("fdn " + fdn.toString() + " does not exist in " +
                             "the host view");
                return null;
            }

            if ( node instanceof
                 com.cisco.eManager.eManager.inventory.view.AppInstanceNode )
            {
                aiNode =
                    (com.cisco.eManager.eManager.inventory.view.AppInstanceNode)
                    node;
                return aiNode.appInstance();
            }
            else
            {
                reason = "fdn does not resolve to an appInstanceNode";
                logger.info(reason);
                throw new Exception(reason);
            }
        }

        reason = "appInstanceId not initialized - it does not identify anything";
        logger.info(reason);
        throw new Exception(reason);
    }

    private List getAppInstances(AppTypeId atId)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;

        com.cisco.eManager.eManager.inventory.appType.AppType imAppType =
            getAppType(atId);
        if (imAppType == null)
        {
            reason = "appTypeId does not resolve to a known appType";
            logger.info(reason);
            throw new Exception(reason);
        }

        return m_aim.appInstancesByAppType(imAppType.id());
    }

    private List getAppInstances(HostId hostId)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;
        com.cisco.eManager.eManager.inventory.host.Host imHost = getHost(hostId);
        if (imHost == null)
        {
            reason = "hostId does not resolve to a known host";
            logger.info(reason);
            throw new Exception(reason);
        }
        return imHost.appInstances();
    }

    private com.cisco.eManager.eManager.inventory.appType.AppType
        getAppType(AppTypeId atId)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;
        FullyDistinguishedName fdn = atId.getAvFdn();
        if ( fdn != null )
        {
            com.cisco.eManager.eManager.inventory.view.Node node =
                getNode(m_avm, fdn);
            if ( node == null )
            {
                logger.debug("fdn " + fdn.toString() + " does not exist in " +
                             "the apps view");
                return null;
            }

            if ( node instanceof
                 com.cisco.eManager.eManager.inventory.view.AppTypeNode )
            {
                com.cisco.eManager.eManager.inventory.view.AppTypeNode atNode =
                    (com.cisco.eManager.eManager.inventory.view.AppTypeNode)node;
                return atNode.appType();
            }
            else
            {
                reason = "fdn does not resolve to an appTypeNode";
                logger.info(reason);
                throw new Exception(reason);
            }
        }

        ManagedObjectId id = atId.getId();
        if ( id != null )
        {
            return m_atm.find(NbapiMsgHelper.toImManagedObjectId(id));
        }

        String name = atId.getName();
        if ( name != null )
        {
            return m_atm.find(name);
        }

        reason = "appTypeId is not initialized - it does not specify any appType";
        logger.info(reason);
        throw new Exception(reason);
    }

    private List getAppTypes(HostId hostId)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;
        com.cisco.eManager.eManager.inventory.host.Host imHost = getHost(hostId);
        if (imHost == null)
        {
            reason = "hostId does not resolve to a known host";
            logger.info(reason);
            throw new Exception(reason);
        }
        return imHost.appTypes();
    }

    private com.cisco.eManager.eManager.inventory.host.Host
        getHost(HostId hostId)
        throws Exception
    {
        logger.debug("enter");
        String reason = null;
        FullyDistinguishedName fdn = hostId.getPvFdn();
        if ( fdn != null )
        {
            com.cisco.eManager.eManager.inventory.view.Node node =
                getNode(m_hvm, fdn);
            if ( node == null )
            {
                logger.debug("fdn " + fdn.toString() + " does not exist in " +
                             "the host view");
                return null;
            }

            if ( node instanceof
                 com.cisco.eManager.eManager.inventory.view.HostNode )
            {
                com.cisco.eManager.eManager.inventory.view.HostNode hostNode =
                    (com.cisco.eManager.eManager.inventory.view.HostNode)node;
                return hostNode.host();
            }
            else
            {
                reason = "fdn does not resolve to a host";
                logger.info(reason);
                throw new Exception(reason);
            }
        }

        ManagedObjectId id = hostId.getId();
        if ( id != null )
        {
            return m_hm.find(NbapiMsgHelper.toImManagedObjectId(id));
        }

        String name = hostId.getName();
        if ( name != null )
        {
            return m_hm.find(name);
        }

        reason = "hostId is not initialized - it does not specify any host";
        logger.info(reason);
        throw new Exception(reason);
    }

    private void updateAuditLog(AuditAction action,
                                String subject,
                                String userId,
                                String message)
    {
        try
        {
            m_auditMgr.addAuditLogEntry(AuditDomain.Inventory,
                                        action,
                                        subject,
                                        new Date(),
                                        userId,
                                        message);
        }
        catch (EmanagerDatabaseException ex)
        {
            logger.error("EmanagerDatabaseException caught: " + ex);
        }
        catch (EmanagerAuditException ex)
        {
            logger.error("EmanagerAuditException caught: " + ex);
        }
        return;
    }

    private String toAuditLogSubject(String prefix, String object)
    {
        return prefix + "=" + object;
    }

    private String toAuditLogSubject(
                      NodeId nodeId,
                      com.cisco.eManager.eManager.inventory.view.ViewManager vm)
    {
        String subjectType = null;
        if ( vm instanceof
             com.cisco.eManager.eManager.inventory.view.AppViewManager )
        {
            subjectType = AuditGlobals.ApplicationViewSubjectKey;
        }
        else if ( vm instanceof
                  com.cisco.eManager.eManager.inventory.view.HostViewManager )
        {
            subjectType = AuditGlobals.PhysicalViewSubjectKey;
        }
        else
        {
            subjectType = AuditGlobals.SolutionViewSubjectKey;
        }

        String subjectObject = toFdn(nodeId, vm);

        return toAuditLogSubject(subjectType, subjectObject);
    }

    private String toFdn(NodeId nodeId,
                         com.cisco.eManager.eManager.inventory.view.ViewManager vm)
    {
        String fdnStr = null;
        FullyDistinguishedName fdn = nodeId.getFdn();
        if ( fdn == null )
        {
            com.cisco.eManager.eManager.inventory.view.Node imNode =
                vm.find(NbapiMsgHelper.toImManagedObjectId(nodeId.getId()));
            if ( imNode == null )
            {
                fdnStr = "<null>";
            }
            else
            {
                fdnStr = imNode.fdn();
            }
        }
        else
        {
            fdnStr = (NbapiMsgHelper.toImNodePath(fdn)).toString();
        }
        return fdnStr;
    }
}
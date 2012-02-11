package com.cisco.eManager.eManager.processSequencer.processSequencer;

import com.cisco.eManager.eManager.processSequencer.common.*;
import com.cisco.eManager.eManager.processSequencer.common.logging.*;
import com.tibco.tibrv.*;
import com.cisco.eManager.eManager.network.TibcoMsgHandler;


public class TibcoManager
{
    private static TibcoManager tibcoManager = null;
    private TibrvTransport tibcoTransport;
    private CiscoLogger mLogger;
    private String mService;
    private String mNetwork;
    private String mDaemon;

    private TibcoManager() throws TibrvException
    {
        mLogger = CiscoLogger.getCiscoLogger("processSequencer");
        setupConfig();
        try {
            Tibrv.open(Tibrv.IMPL_NATIVE);
            tibcoTransport = new TibrvRvdTransport(mService, mNetwork, mDaemon);
            mLogger.fine("The Tibco Transport was setup");
            mLogger.fine(tibcoTransport.toString());
        }
        catch (TibrvException e)
        {
            tibcoTransport = null;
            mLogger.severe("Fatal error establishing tibrv transport:" + e.getMessage());
            throw e;
        }
    }

    public static TibcoManager instance() throws TibrvException
    {
        if (tibcoManager == null) {
            tibcoManager = new TibcoManager();
        }

        return tibcoManager;
    }

    private void setupConfig() {
        mService = DCPLib.getSystemProperty("tibrv.service", "7500");
        mLogger.finest("Tibrv UDP Service: " + mService);
        mNetwork = DCPLib.getSystemProperty("tibrv.network", null);
        mLogger.finest(
            "Tibrv network to use for outbound session communications: " +
            mNetwork);
        mDaemon = DCPLib.getSystemProperty("tibrv.daemon", "tcp:7500");
        mLogger.finest("TIBCO Rendezvous daemon to handle communication for the session: " +
                       mDaemon);
    }

    public void sendMessage(String subject, String xmlMessage) {
        String FIELD_NAME = "DATA";
        TibrvMsg tibcoMsg;
        mLogger.fine("enter");
        try {
            tibcoMsg = new TibrvMsg();
            tibcoMsg.setSendSubject(subject);
            tibcoMsg.update(FIELD_NAME, xmlMessage);
            mLogger.finest("Sending msg: " + xmlMessage);

            try {
                if (tibcoTransport != null) {
                    mLogger.fine("Sending message on transport");
                    tibcoTransport.send(tibcoMsg);
                }
            }
            catch (TibrvException e) {
                mLogger.severe("Error sending Tibrv message: " +  e);
            }

        }
        catch (TibrvException e) {
            mLogger.severe("Error creating Tibrv message: " + e);
        }
    }

    public void manageApplication(String appType, String appInst) {
        mLogger.fine("entered");
        String subject = "cisco.mgmt.emanager.inventory.request";
        //String subject = TibcoMsgHandler.INVENTORY_SUBJECT_REQUEST;
        StringBuffer xmlMsg = new StringBuffer();
        xmlMsg.append("<eManagerMessage>");
        xmlMsg.append("   <messageID>173489783</messageID>");
        xmlMsg.append("   <request>");
        xmlMsg.append("     <inventoryMgrMsg>");
        xmlMsg.append("       <appInstanceMsg>");
        xmlMsg.append("         <aiMsgManage>");
        xmlMsg.append("           <typeAndName>");
        xmlMsg.append("             <appTypeId>");
        xmlMsg.append("               <name>" + appType + "</name>");
        xmlMsg.append("             </appTypeId>");
        xmlMsg.append("             <name>" + appInst + "</name>");
        xmlMsg.append("           </typeAndName>");
        xmlMsg.append("         </aiMsgManage>");
        xmlMsg.append("       </appInstanceMsg>");
        xmlMsg.append("     </inventoryMgrMsg>");
        xmlMsg.append("   </request>");
        xmlMsg.append("</eManagerMessage>");
        mLogger.info("Sending Message on subject: " + subject);
        mLogger.info("Message: " + xmlMsg.toString());
        sendMessage(subject, xmlMsg.toString());
    }

    public void unmanageApplication(String appType, String appInst) {
        mLogger.fine("entered");
        String subject = "cisco.mgmt.emanager.inventory.request";
        //String subject = TibcoMsgHandler.INVENTORY_SUBJECT_REQUEST;
        StringBuffer xmlMsg = new StringBuffer();
        xmlMsg.append("<eManagerMessage>");
        xmlMsg.append("   <messageID>173489783</messageID>");
        xmlMsg.append("   <request>");
        xmlMsg.append("     <inventoryMgrMsg>");
        xmlMsg.append("       <appInstanceMsg>");
        xmlMsg.append("         <aiMsgUnmanage>");
        xmlMsg.append("           <typeAndName>");
        xmlMsg.append("             <appTypeId>");
        xmlMsg.append("               <name>" + appType + "</name>");
        xmlMsg.append("             </appTypeId>");
        xmlMsg.append("             <name>" + appInst + "</name>");
        xmlMsg.append("           </typeAndName>");
        xmlMsg.append("         </aiMsgUnmanage>");
        xmlMsg.append("       </appInstanceMsg>");
        xmlMsg.append("     </inventoryMgrMsg>");
        xmlMsg.append("   </request>");
        xmlMsg.append("</eManagerMessage>");
        mLogger.info("Sending Message on subject: " + subject);
        mLogger.info("Message: " + xmlMsg.toString());
        sendMessage(subject, xmlMsg.toString());
    }

}

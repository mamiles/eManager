//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\network\\MsgProcessor.java

package com.cisco.eManager.eManager.inventory.network;

import com.cisco.eManager.eManager.inventory.InventoryManager;
import com.cisco.eManager.eManager.network.AgentMessage;
import org.apache.log4j.Logger;

public class MsgProcessor extends Thread
{
    private MsgQueue m_msgQueue = null;
    private static Logger logger = Logger.getLogger(MsgProcessor.class);
    private InventoryManager m_inventoryManager = null;

    /**
     * @roseuid 3F4D08A70036
     */
    public MsgProcessor()
    {
        super("IM message processor thread");
        logger.debug("enter");
        m_msgQueue = MsgQueue.instance();
        m_inventoryManager = InventoryManager.instance();
    }

    /**
     * @roseuid 3F60C71101F1
     */
    public void run()
    {
        logger.debug("enter");
        AgentMessage msg = null;
        while (true)
        {
            logger.debug("searching for next network message to process");
            msg = m_msgQueue.getNextMessage();
            logger.debug("found network message to process: " + msg);
            m_inventoryManager.processNetworkMessage(msg);
            logger.debug("network message processed");
        }
    }
}

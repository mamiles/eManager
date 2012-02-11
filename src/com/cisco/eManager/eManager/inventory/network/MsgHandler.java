//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\network\\MsgHandler.java

package com.cisco.eManager.eManager.inventory.network;

import java.util.*;
import com.cisco.eManager.eManager.network.AgentMessage;
import org.apache.log4j.Logger;

public class MsgHandler 
{
    private static MsgHandler s_instance = null;
    private static Logger logger = Logger.getLogger(MsgHandler.class);
    
    /**
     * @roseuid 3F4D08A60003
     */
    private MsgHandler() 
    {
        logger.debug("enter");     
    }
    
    /**
     * @param msg
     * @roseuid 3F26A2FE01AA
     */
    public void handleMessage(AgentMessage msg) 
    {
        logger.debug("enter");
        MsgQueue.instance().addMessage(msg);     
    }
    
    /**
     * @return com.cisco.eManager.eManager.inventory.network.MsgHandler
     * @roseuid 3F26AA7D0201
     */
    public static MsgHandler instance() 
    {
        logger.debug("enter");
        if ( s_instance == null )
        {
            s_instance = new MsgHandler();
        }
        return s_instance;     
    }
}

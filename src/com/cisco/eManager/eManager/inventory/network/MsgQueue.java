//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\network\\MsgQueue.java

package com.cisco.eManager.eManager.inventory.network;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.eManager.network.AgentMessage;

public class MsgQueue
{
    private static MsgQueue s_instance = null;
    private static Logger logger = Logger.getLogger(MsgQueue.class);
    private List m_networkMessages = null;

    /**
     * @roseuid 3F4D08A80219
     */
    private MsgQueue()
    {
        logger.debug("enter");
        m_networkMessages = Collections.synchronizedList(new LinkedList());
    }

    /**
     * @param msg
     * @roseuid 3F256D98005F
     */
    synchronized void addMessage(AgentMessage msg)
    {
        logger.debug("enter");
        m_networkMessages.add(msg);
        // notify consumer of messages that there now exists one more...
        notifyAll();
        return;
    }

    /**
     * @return AgentMessage
     * @roseuid 3F256DA6024A
     */
    synchronized AgentMessage getNextMessage()
    {
        logger.debug("enter");
        AgentMessage msg = null;
        while (m_networkMessages.isEmpty())
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                logger.debug("interrupted exception caught: " + e);
            }
        }
        try
        {
            Object obj = m_networkMessages.remove(0);
            logger.debug("object of class " + obj.getClass().getName());
            msg = (AgentMessage)obj;
        }
        catch (Exception e)
        {
            logger.error("exception caught while retrieving next message: " + e);
        }
        return msg;
    }

    /**
     * @return com.cisco.eManager.eManager.inventory.network.MsgQueue
     * @roseuid 3F60C71202EC
     */
    static MsgQueue instance()
    {
        // because this method uses the default "package" level of visibility,
        // and because it provides the only access to the queue, this
        // singleton object is effectively buried within this particular
        // package
        logger.debug("enter");

        if ( s_instance == null )
        {
            s_instance = new MsgQueue();
        }
        return s_instance;
    }
}

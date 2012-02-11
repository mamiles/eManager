//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\HostObserver.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.eManager.inventory.host.*;

public class HostObserver implements Observer
{
    private static Logger logger = Logger.getLogger(HostObserver.class);
    private HostViewManager m_hvm;

    /**
     * @roseuid 3F4D08F1037E
     */
    public HostObserver()
        throws Exception
    {
        logger.debug("enter");
        m_hvm = HostViewManager.instance();
        HostManager.instance().addObserver(this);
    }

    /**
     * @roseuid 3F0AE9D50256
     */
    public void update()
    {
        logger.debug("enter");
    }

    /**
     * @param obj
     * @param hostManager
     * @roseuid 3F4D08F20235
     */
    public void update(Observable hostManager, Object obj)
    {
        logger.debug("enter");
        if ( obj instanceof HostNotification )
        {
            HostNotification hn = (HostNotification)obj;
            Host host = hn.host();
            if ( hn.ntfcnType().isActivate() )
            {
                m_hvm.activatedHost(host);
            }
            else if ( hn.ntfcnType().isCreate() )
            {
                m_hvm.newHost(host);
            }
            else if ( hn.ntfcnType().isPreDelete() )
            {
                m_hvm.deletedHost(host);
            }
            else if ( hn.ntfcnType().isRestore() )
            {
                m_hvm.restoredHost(host);
            }
            else if ( hn.ntfcnType().isUndelete() )
            {
                m_hvm.undeletedHost(host);
            }
            else
            {
                logger.debug("notification of type not acted upon by HVM");
            }
        }
        else
        {
            logger.warn("object of unexpected class received: " +
                        obj.getClass().getName());
        }
    }
}

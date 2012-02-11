//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\AppInstanceObserver.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.eManager.inventory.appInstance.*;

public class AppInstanceObserver implements Observer
{
    private static Logger logger = Logger.getLogger(AppInstanceObserver.class);
    private AppViewManager m_avm;
    private HostViewManager m_hvm;

    /**
     * @roseuid 3F4D08ED0273
     */
    public AppInstanceObserver()
        throws Exception
    {
        logger.debug("enter");
        m_avm = AppViewManager.instance();
        m_hvm = HostViewManager.instance();
        AppInstanceManager.instance().addObserver(this);
    }

    /**
     * @roseuid 3F0ADBAC02D5
     */
    public void update()
    {
        logger.debug("enter");
    }

    /**
     * @param obj
     * @param subj
     * @roseuid 3F4D08EE0148
     */
    public void update(Observable subj, Object obj)
    {
        logger.debug("enter");
        if ( obj instanceof AppInstanceCreation )
        {
            m_avm.newAppInstance((AppInstanceCreation)obj);
            m_hvm.newAppInstance((AppInstanceCreation)obj);
        }
        else if ( obj instanceof AppInstanceDeletion )
        {
            m_avm.delAppInstance((AppInstanceDeletion)obj);
            m_hvm.delAppInstance((AppInstanceDeletion)obj);
        }
        else
        {
            logger.debug("object of unexpected class received: " +
                         obj.getClass().getName());
        }
    }
}

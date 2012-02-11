//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\AppTypeObserver.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.eManager.inventory.appType.*;

public class AppTypeObserver implements Observer
{
    private static Logger logger = Logger.getLogger(AppTypeObserver.class);
    private AppViewManager m_avm;

    /**
     * @roseuid 3F4D08EF02A8
     */
    public AppTypeObserver()
        throws Exception
    {
        logger.debug("enter");
        m_avm = AppViewManager.instance();
        AppTypeManager.instance().addObserver(this);
    }

    /**
     * @roseuid 3F0ADDA70085
     */
    public void update()
    {
        logger.debug("enter");
    }

    /**
     * @param obj
     * @param subj
     * @roseuid 3F4D08F00029
     */
    public void update(Observable subj, Object obj)
    {
        logger.debug("enter");
        if ( obj instanceof AppTypeCreation )
        {
            m_avm.newAppType((AppTypeCreation)obj);
        }
        else if ( obj instanceof AppTypeDeletion )
        {
            m_avm.delAppType((AppTypeDeletion)obj);
        }
        else
        {
            logger.warn("object of unexpected class received: " +
                        obj.getClass().getName());
        }
    }
}

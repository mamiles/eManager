//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\AppInstanceObserver.java

package com.cisco.eManager.eManager.inventory;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.eManager.inventory.InventoryManager;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceRestoration;

public class ImAppInstanceObserver implements Observer
{
    private static Logger logger = Logger.getLogger(ImAppInstanceObserver.class);

    /**
     * @roseuid 3F4D08ED0273
     */
    public ImAppInstanceObserver()
        throws Exception
    {
        logger.debug("enter");
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
        if ( obj instanceof AppInstanceRestoration )
        {
	    AppInstance appInstance;

	    appInstance = ((AppInstanceRestoration)obj).appInstance();
	    InventoryManager.instance().notifyAppInstanceRestoration (appInstance);

	    // We need to notify clients of the management state
	    InventoryManager.instance().notifyAppInstanceManagementState (appInstance);
        }
    }
}

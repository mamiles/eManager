//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\ImAppInstanceConsolidation.java

package com.cisco.eManager.eManager.inventory;

import java.util.*;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;

public class ImAppInstanceConsolidation
{
    private Collection m_consolidatedAppInstances;
    private Collection m_resultingAppInstance;

    /**
     * @param resultingAppInstance
     * @param consolidatedAppInstanceIds
     * @param consolidatedAppInstances
     * @roseuid 3F671E2802D0
     */
    ImAppInstanceConsolidation(Collection resultingAppInstance,
                               Collection consolidatedAppInstances)
    {
        m_consolidatedAppInstances = consolidatedAppInstances;
        m_resultingAppInstance = resultingAppInstance;
    }

    /**
     * @return the app instance into which one or more other app instances have been
     * consolidated (this app instance may or may not be new)
     * @roseuid 3F671E2A0016
     */
    public Collection resultingAppInstance()
    {
        return m_resultingAppInstance;
    }

    /**
     * @return a collection of app instance IDs which no longer exist due to
     * consolidation
     * @roseuid 3F671E2A00E8
     */
    public Collection consolidatedAppInstances()
    {
        return m_consolidatedAppInstances;
    }
}

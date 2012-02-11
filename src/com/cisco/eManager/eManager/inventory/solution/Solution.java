package com.cisco.eManager.eManager.inventory.solution;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceManager;
import com.cisco.eManager.eManager.inventory.solution.SolutionData;

public class Solution
{
    private static Logger logger = Logger.getLogger(Solution.class);
    private SolutionData m_data;

    Solution(SolutionData data)
    {
        m_data = data;
    }

    public ManagedObjectId id()
    {
        return m_data.id();
    }

    public String name()
    {
        return m_data.name();
    }

    public Collection appInstanceIds()
    {
	return m_data.appInstanceIds();
    }

    public AppInstance[] appInstances()
    {
        Collection appInstanceIds = appInstanceIds();
        Iterator iter = appInstanceIds.iterator();
        int count = 0;
        while (iter.hasNext())
        {
            count++;
            iter.next();
        }
        AppInstance[] appInstances = new AppInstance[count];
        iter = appInstanceIds.iterator();
        AppInstanceManager aim = AppInstanceManager.instance();
        AppInstance ai = null;
        int i = 0;
        while (iter.hasNext())
        {
            appInstances[i] = aim.find((ManagedObjectId)iter.next());
        }
        return appInstances;
    }

    public String toString()
    {
	return m_data.toString();
    }
}

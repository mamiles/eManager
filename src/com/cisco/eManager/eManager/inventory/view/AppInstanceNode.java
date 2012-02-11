//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\appInstanceNode\\AppInstanceNode.java

package com.cisco.eManager.eManager.inventory.view;

import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;

public class AppInstanceNode
    extends LeafNode
{
    private static Logger logger = Logger.getLogger(AppInstanceNode.class);
    private AppInstance m_appInstance;

    AppInstanceNode(AppInstance appInstance, AbstractContainerNode parent)
        throws Exception
    {
        m_appInstance = appInstance;
        if ( parent != null )
        {
            Node.attach(parent, this);
        }
    }

    public ManagedObjectId id()
    {
        return m_appInstance.id();
    }

    public String name()
    {
        return m_appInstance.name();
    }

    public AppInstance appInstance()
    {
        return m_appInstance;
    }
}

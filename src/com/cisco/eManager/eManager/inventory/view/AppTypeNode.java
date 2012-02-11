//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\appTypeNode\\AppTypeNode.java

package com.cisco.eManager.eManager.inventory.view;

import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.appType.AppType;

public class AppTypeNode extends AbstractContainerNode
{
    private static Logger logger = Logger.getLogger(AppTypeNode.class);
    private AppType m_appType;

    AppTypeNode(AppType appType, AbstractContainerNode parent)
        throws Exception
    {
        m_appType = appType;
        if ( parent != null )
        {
            Node.attach(parent, this);
        }
    }

    public ManagedObjectId id()
    {
        return m_appType.id();
    }

    public String name()
    {
        return m_appType.name();
    }

    public AppType appType()
    {
        return m_appType;
    }

    /**
     * @param node
     * @return boolean
     * @roseuid 3F872D46020B
     */
    public boolean canHaveChild(Node node)
    {
        if ( node instanceof AppInstanceNode )
        {
            return true;
        }
        return false;
    }

    public boolean relocatable()
    {
        return true;
    }
}

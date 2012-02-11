//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\hostNode\\HostNode.java

package com.cisco.eManager.eManager.inventory.view;

import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.host.Host;

public class HostNode extends AbstractContainerNode
{
    private static Logger logger = Logger.getLogger(HostNode.class);
    private Host m_host;

    HostNode(Host host, AbstractContainerNode parent)
        throws Exception
    {
        m_host = host;
        if ( parent != null )
        {
            Node.attach(parent, this);
        }
    }

    public ManagedObjectId id()
    {
        return m_host.id();
    }

    public String name()
    {
        return m_host.name();
    }

    public Host host()
    {
        return m_host;
    }

    /**
     * @param node
     * @return boolean
     * @roseuid 3F872D7202A6
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

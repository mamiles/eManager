//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\common\\inventory\\ViewContainer.java

package com.cisco.eManager.common.inventory;


/**
 * @author wstubb
 * @version 1.0
 */
public class ViewContainer
{
    private String m_name;
    private ManagedObjectId m_id;
    private ManagedObjectId m_parentId;

    /**
     * null unless the container is a host, appType, or appInstance
     */
    private ManagedObjectId m_dataId;

    public ViewContainer()
    {
        m_id = null;
        m_parentId = null;
        m_name = null;
        m_dataId = null;
    }

    /**
     * @param id uniquely identifies this node from all others
     * @param parentId ID of this node's parent node
     * @param dataId null unless the container is a host, appType, or appInstance
     * @param name name of the node
     * @roseuid 3F561E80039E
     */
    public ViewContainer(ManagedObjectId id, ManagedObjectId parentId, ManagedObjectId dataId, String name)
    {
        m_id = id;
        m_parentId = parentId;
        m_dataId = dataId;
        m_name = name;
    }

    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectKey
     * @roseuid 3F54B32000AF
     */
    public ManagedObjectId id()
    {
        return m_id;
    }

    /**
     * @return String
     * @roseuid 3F54BAB300F3
     */
    public String name()
    {
        return m_name;
    }

    /**
     * @return com.cisco.eManager.common.inventory.ManagedObjectKey
     * @roseuid 3F54BF2C0398
     */
    public ManagedObjectId parentId()
    {
        return m_parentId;
    }

    /**
     * @return null unless the container is a host, appType, or appInstance
     * @roseuid 3F689CCC01CD
     */
    public ManagedObjectId dataId()
    {
        return m_dataId;
    }
}

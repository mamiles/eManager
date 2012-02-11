//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\container\\ContainerNodeData.java

package com.cisco.eManager.eManager.inventory.view;

import com.cisco.eManager.common.inventory.ManagedObjectId;

public class ContainerNodeData
{
    private ManagedObjectId m_id;
    private String m_name;
    private ManagedObjectId m_parentId;

    public ContainerNodeData(ManagedObjectId id,
                             String name,
                             ManagedObjectId parentId)
    {
        m_id = id;
        m_name = name;
        m_parentId = parentId;
    }

    public ManagedObjectId id()
    {
        return m_id;
    }

    void id(ManagedObjectId newId)
    {
        m_id = newId;
    }

    public String name()
    {
        return m_name;
    }

    public ManagedObjectId parentId()
    {
        return m_parentId;
    }

    void parentId(ManagedObjectId newParentId)
    {
        m_parentId = newParentId;
    }

    public String toString()
    {
        return "Id:" + m_id + " Name:" + m_name + " Parent:" + m_parentId;
    }
}

//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\host\\HostRestoration.java

package com.cisco.eManager.eManager.inventory.mgmtPolicy;

import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy;

public class MgmtPolicyRestoration
{
    private MgmtPolicy m_mgmtPolicy;

    MgmtPolicyRestoration(MgmtPolicy mgmtPolicy)
    {
        m_mgmtPolicy = mgmtPolicy;
    }

    public MgmtPolicy mgmtPolicy()
    {
        return m_mgmtPolicy;
    }
}

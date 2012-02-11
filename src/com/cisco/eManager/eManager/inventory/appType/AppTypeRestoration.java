package com.cisco.eManager.eManager.inventory.appType;

import com.cisco.eManager.eManager.inventory.appType.AppType;

public class AppTypeRestoration
{
    private AppType m_appType;

    AppTypeRestoration(AppType appType)
    {
        m_appType = appType;
    }

    public AppType appType()
    {
        return m_appType;
    }
}

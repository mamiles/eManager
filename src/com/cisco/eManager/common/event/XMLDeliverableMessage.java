package com.cisco.eManager.common.event;

import com.cisco.eManager.common.event.EmanagerEventException;

public interface XMLDeliverableMessage
{
    public String getXMLMessage() throws EmanagerEventException;
}

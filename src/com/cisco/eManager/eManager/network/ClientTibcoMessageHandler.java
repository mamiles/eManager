package com.cisco.eManager.eManager.network;

import com.cisco.eManager.common.util.AccessType;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public interface ClientTibcoMessageHandler
{
    public String handleRequest(String xmlStream, String userId, AccessType accessType);
}

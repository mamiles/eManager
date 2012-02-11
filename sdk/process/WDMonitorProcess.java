/*********************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.	Possessions and use of this
 * program must conform strictly to the license agreement between the user
 * and Cisco Systems, Inc., and receipt or possession does not convey
 * any rights to divulge, reproduce, or allow others to use this program
 * without specific written authorization of Cisco Systems, Inc.
 *
 * Copyright (c) 2001 by Cisco Systems, Inc.
 * All rights reserved.
 *
 *********************************************************************/
package com.cisco.eManager.eManager.processSequencer.watchdog.servers;
import com.cisco.eManager.eManager.processSequencer.watchdog.ProcessMonitor;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public class WDMonitorProcess extends ProcessMonitor
{
    /**
     * Create a new ProcessMonitor server to abstract a server that does
     * not have any sort of heartbeat detection.
     *
     * @param name The server name
     */
    public WDMonitorProcess (String name) {
        super (name);
    }

    /**
     * This does nothing at all, so that the server will always appear
     * to have a heartbeat.
     *
     * @exception Exception will never actually be thrown.
     */
    public Object heartbeat () throws Exception {
            return null;
    }
}
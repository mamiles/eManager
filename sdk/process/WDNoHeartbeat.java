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

import com.cisco.eManager.eManager.processSequencer.watchdog.ProcessExecutor;

import java.util.Date;

/**
 * Used for servers that don't have any sort of heartbeat detection.
 * This allows arbitrary processes to be created quickly.
 */
public class WDNoHeartbeat extends ProcessExecutor {
	/**
	 * Create a new WDNoHeartbeat server to abstract a server that does
	 * not have any sort of heartbeat detection.
	 *
	 * @param name The server name
	 */
	public WDNoHeartbeat (String name) {
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

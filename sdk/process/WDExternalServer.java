/*********************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.  Possessions and use of this
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
import java.util.List;
import java.util.Vector;

/**
 * Used for servers that do not get started and stopped
 * using watchdog, but are just monitored from watchdog
 */
public abstract class WDExternalServer extends ProcessExecutor {

	 protected String[] cmdArgs;

	 public WDExternalServer (String name) {
		this(name, null);
	 }

	/**
	 * Create a new WDExternalServer server to abstract a server that does
	 * not have any sort of heartbeat detection.
	 *
	 * @param name The server name
	 */
	public WDExternalServer (String name, String[] args) {
		super (name);

		if(args == null || args.length == 0) {
			cmdArgs = new String[] {name};
		} else {
			cmdArgs = args;
		}
	}

	protected String getCommand() {
		return new StringBuffer("java com.cisco.eManager.eManager.processSequencer.watchdog.ext.ExternalServer ").toString().trim();
	}

	protected List getAppArgs() {
		return java.util.Arrays.asList(cmdArgs);
	}
}

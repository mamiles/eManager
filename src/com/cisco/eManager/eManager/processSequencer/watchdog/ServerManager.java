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

package com.cisco.eManager.eManager.processSequencer.watchdog;

/**
 * The interface of WatchdogImplementation that is
 * exposed to the Servers.
 */
public interface ServerManager {

	/**
	 * Adds the given event to the event pool (thread pool)
	 * which gets executed in the order it is received.
	 */
	public void addEventToPool(Runnable r);

	/**
	 * Gives the reference of the Server object corresponding
	 * to the name.
	 */
	public Server getServer(String name);

	/**
	 * Gives an Iterator object that can iterate over the list of managed Servers
	 */
	public java.util.Iterator getServers();

	/**
	 * Gives the total number of servers being managed by this Watchdog
	 */
	public int getNumServers();

}

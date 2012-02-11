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
 * A listener interface for objects interested in changes in a
 * server's state.
 */
public interface ServerStateListener {
	/**
	 * Called each time the server's state changes.
	 * @param server The server who's state changed.
	 * @param newState The server's new state.
	 */
	public void serverStateChanged (Server server, int newState);
}

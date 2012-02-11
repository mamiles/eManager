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

import java.util.Enumeration;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Abstracts a group of servers within the watchdog.
 *
 */
public class ServerGroup {

	/**
	 * The name of this server group.
	 */
	private String mName;

	/**
	 * The servers in this group.
	 */
	private TreeMap mServers;
	private Server[] mOrderedServerList;

	public ServerGroup (String name) {
		mName = name;
		mServers = new TreeMap();
	}

	public void startServers (boolean waitFlag)
		throws ServerStateChangeException, PrincipalDisabledException
	{
		buildOrderedServerList();
		for(int i= 0; i < mOrderedServerList.length ; ++i) {
			mOrderedServerList[i].startServer(false);
		}

		for(int i= 0; i < mOrderedServerList.length ; ++i) {
			mOrderedServerList[i].startServer(waitFlag);
		}
	}

	public void stopServers (boolean waitFlag)
		throws ServerStateChangeException {

		buildOrderedServerList();
		for(int i= mOrderedServerList.length -1; i >=0; --i) {
			mOrderedServerList[i].stopServer(false);
		}

		for(int i= mOrderedServerList.length -1; i >=0; --i) {
			mOrderedServerList[i].stopServer(waitFlag);
		}
	}

	public void restartServers (boolean waitFlag)
		throws ServerStateChangeException, PrincipalDisabledException
	{
		buildOrderedServerList();
		stopServers (true);
		startServers (waitFlag);
	}

	public void addServer (Server server) {
		mServers.put (server.getName(), server);
	}

	private synchronized void buildOrderedServerList() {

		if( mOrderedServerList == null ) {
			String[] serverNames = (String[]) mServers.keySet().toArray(new String[0]);
			OrderedServers os = ServerGraph.getInstance().getOrderedList(serverNames);

			serverNames = os.getList();

			mOrderedServerList = new Server[serverNames.length];
			for(int i=0; i < serverNames.length; ++i) {
				mOrderedServerList[i] = (Server) mServers.get(serverNames[i]);
			}
		}
	}

	public String[] getServerNames () {
		return (String[]) mServers.keySet().toArray(new String[0]);
	}
}

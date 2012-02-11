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

import java.io.Serializable;

/**
 * This class is used to specify the targetted components
 * for a start/restart/stop operation. The components
 * can either be both servers and groups. Instead of
 * providing more (overloaded) methods in the interface,
 * this class is used to capture the information about the
 * targets (servers/server groups) for a particular operation.
 */
public class TargetComponentData implements Serializable {

	public static final TargetComponentData ALL_SERVER_TARGET;
	static {
		ALL_SERVER_TARGET = new TargetComponentData();
		ALL_SERVER_TARGET.addAllServers();
	}

	public static final int SERVER = 100;
	public static final int SERVERGROUP  = 200;

	public static final int ALL_SERVERS = 0;

	private String[] mServers;
	private String[] mServerGroups;

	private boolean mWaitFlag = true;

	/**
	 * Default constructor.
	 */
	public TargetComponentData() { }

	/**
	 * @param type one of TargetComponentData.SERVER or TargetComponentData.SERVERGROUP
	 * @param elements the servers or groups (depending on the type parameter)
	 * @throws IllegalArgumentException if the type is invalid or if elements is null
	 */

	public TargetComponentData(int type, String[] elements)
	{
		if( elements == null) {
			throw new IllegalArgumentException(WDExUtil.formatMessage(WDExConstants.ELEMENTS_NULL));
		}

		if( type == SERVER ) {
			addServers(elements);
		} else if (type == SERVERGROUP ) {
			addServerGroups(elements);
		} else {
			throw new IllegalArgumentException(WDExUtil.formatMessage(WDExConstants.ILLEGAL_TYPE));
		}
	}

	/**
	 * Sets the waitFlag field
	 * @param waitFlag
	 */
	public void setWaitFlag(boolean waitFlag) {
		this.mWaitFlag = waitFlag;
	}

	/**
	 * Gets the waitFlag value
	 * @return the wait flag value
	 */
	public boolean getWaitFlag() {
		return mWaitFlag;
	}

	/**
	 * Adds all servers as targets.
	 * @throws IllegalArgumentException if one or more server groups are already added.
	 */
	public void addAllServers() {
		if( mServerGroups != null) {
			throw new IllegalArgumentException(WDExUtil.formatMessage(WDExConstants.GROUPS_EXIST));
		}
		mServers =  new String[0];
	}

	/**
	 * To check if all servers are targetted
	 * @return true if all servers are targetted; false otherwise
	 */
	public boolean isAllServers() {
		if(mServers != null && mServers.length == 0) return true;
		return false;
	}

	/**
	 * Adds the specified list of servers as targets.
	 * @param  servers list of servers to add (should be non-null and non-empty)
	 * @throws IllegalArgumentException if one or more server groups are already added.
	 * @throws IllegalArgumentException if servers list is null or empty
	 */
	public void addServers(String[] servers) {
		if(servers == null || servers.length == 0) {
			throw new IllegalArgumentException(WDExUtil.formatMessage(WDExConstants.NULL_OR_EMPTY,
									WDUtil.toArray("servers")));
		}

		if( mServerGroups != null) {
			throw new IllegalArgumentException(WDExUtil.formatMessage(WDExConstants.GROUPS_EXIST));
		}

		if( mServers == null || mServers.length == 0) {
			mServers = servers;
		} else {
			String tmp[] = new String[mServers.length + servers.length];
			System.arraycopy(mServers, 0, tmp, 0, mServers.length);
			System.arraycopy(servers, 0, tmp, mServers.length, servers.length);
			mServers = tmp;
		}
	}

	/**
	 * Adds the specified list of server groups as targets.
	 * @param  serverGroups list of server groups to add (should be non-null and non-empty)
	 * @throws IllegalArgumentException if one or more servers are already added.
	 * @throws IllegalArgumentException if serverGroups list is null or empty
	 */
	public void addServerGroups(String[] serverGroups) {
		if(serverGroups == null || serverGroups.length == 0) {
			throw new IllegalArgumentException(WDExUtil.formatMessage(WDExConstants.NULL_OR_EMPTY,
									WDUtil.toArray("serverGroups")));
		}

		if( mServers != null) {
			throw new IllegalArgumentException(WDExUtil.formatMessage(WDExConstants.SERVERS_EXIST));
		}

		if( mServerGroups == null || mServerGroups.length == 0) {
			mServerGroups = serverGroups;
		} else {
			String tmp[] = new String[mServerGroups.length + serverGroups.length];
			System.arraycopy(mServerGroups, 0, tmp, 0, mServerGroups.length);
			System.arraycopy(serverGroups, 0, tmp, mServerGroups.length, serverGroups.length);
			mServerGroups = tmp;
		}
	}

	/**
	 * Gives the list of servers
	 * @return the list of servers
	 */
	public String[] getServers() {
		return mServers;
	}

	/**
	 * Gives the list of serverGroups
	 * @return the list of serverGroups
	 */
	public String[] getServerGroups() {
		return mServerGroups;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer(255);
		if( mServers == null && mServerGroups == null) {
			sb.append("No targets specified");
		} else {
			if(isAllServers()) {
				sb.append("All servers...");
			} else {
				sb.append("Servers	   : ").append(WDUtil.toString(getServers()));
				sb.append("  Server Groups : ").append(WDUtil.toString(getServerGroups()));
			}
		}
		return sb.toString();
	}
}




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
 * This class is used for encapsulating the Host information
 * on which an operation has to be executed. Used in arguments
 * to methods in the WatchdogManager interface.
 *
 * @author Rama Taraniganty
 *
 * @see WatchdogManager#restart
 * @see WatchdogManager#start
 * @see WatchdogManager#stop
 * @see WatchdogManager
 */

public class HostData implements Serializable, Cloneable {

	static final long serialVersionUID = -1085823452811017293L;

	/**
	 * Represents all the hosts in the system.
	 */
	public static final HostData ALL_HOSTS = new HostData();

	private static final String[] ALL_HOSTS_ARRAY = new String[0];
	static {
		ALL_HOSTS.setAllHosts();
	}

	private String[] mHosts = null;

	/**
	 * Default constructor. Represents localhost.
	 */
	public HostData() { }

	/**
	 * Creates HostData with the single host.
	 */
	public HostData(String host) { this (new String[]{host}); }

	/**
	 * Creates HostData with the list of hosts
	 * @see HostData#setHosts
	 * @throws IllegalArgumentException if the hosts param is null or empty
	 */
	public HostData(String hosts[]) { setHosts(hosts); }

	/**
	 * Returns true if this HostData object represents all the hosts
	 */
	public boolean isAllHosts() {
		return (mHosts != null && mHosts.length == 0);
	}

	/**
	 * Returns the list of hosts represented by this HostData instance.
	 * Use isAllHosts() methods to check if this represents all the
	 * hosts in the system.
	 * #see HostData#ALL_HOSTS
	 */
	public String[] getHosts() {
		return mHosts;
	}

	/**
	 * Returns true if this HostData represents the localhost.
	 * @see HostData#HostData()
	 * @see HostData#setLocalHost()
	 */
	public boolean isLocalHost() {
		return (mHosts == null);
	}

	/**
	 * Sets the host represented by this object to localhost
	 */
	public void setLocalHost() {
		mHosts = null;
	}

	/**
	 * Sets the hosts represented by this object to the given list of hosts
	 * @throws IllegalArgumentException if the hosts param is null or empty
	 */
	public void setHosts(String[] hosts) {
		if( hosts == null || hosts.length == 0) {
			throw new IllegalArgumentException(WDExUtil.formatMessage(WDExConstants.NULL_OR_EMPTY,
													WDUtil.toArray("hosts")));
		}
		mHosts = hosts;
	}

	/**
	 * Sets the hosts represented by this object to ALL the hosts
	 * @see HostData#isAllHosts()
	 */
	public void setAllHosts() {
		mHosts = ALL_HOSTS_ARRAY;
	}

	/**
	 * Gives a stringified representation of the hosts encapsulated by this object.
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer(255);
		sb.append("HostData : \n");
		if(isAllHosts()) sb.append("Hosts : All hosts");
		else if( mHosts != null) {
			sb.append("Hosts : ").append(WDUtil.toString(mHosts));
		} else {
			sb.append("Local host");
		}
		sb.append("\n");

		return sb.toString();
	}

}




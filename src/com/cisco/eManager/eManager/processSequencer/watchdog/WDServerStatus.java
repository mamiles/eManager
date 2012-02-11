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
 * This class is used to encapsulate the status of servers
 * being managed by a watchdog.
 * @see ServerStatus
 */

public class WDServerStatus
	implements java.lang.Cloneable, java.io.Serializable
{
	static final long serialVersionUID = -2028433887240016061L;

	private String mName;
	private Object mError;
	private ServerStatus[] serverStatus;

	/**
	 * Constructs a WDServerStatus object with specified host name
	 */
	public WDServerStatus (String name) {
		this.mName = name;
	}

	/**
	 * Constructs a WDServerStatus object with specified host name
	 * and server status.
	 * @param name hostname
	 * @param ss the array of ServerStatus objects one per server
	 */
	public WDServerStatus (String name, ServerStatus[] ss) {
		this.mName = name;
		this.serverStatus = ss;
	}

	/**
	 * Sets error object (typically an exception)
	 * @param error error description or exception
	 */
	public void setError(Object error) {
		this.mError = error;
	}

	/**
	 * Gets error object (typically an exception)
	 * @return the error, if any
	 */
	public Object getError() {
		return mError;
	}

	/**
	 * Gives the actual status of all the servers
	 * @return an array of ServerStatus objects, one per server.
	 */
	public ServerStatus[] getServerStatus() {
		return serverStatus;
	}

	/**
	 * Sets the status of servers
	 * @param ss an array of ServerStatus objects, one per server.
	 */
	public void setServerStatus(ServerStatus[] ss) {
		this.serverStatus = ss;
	}

	/**
	 * @return hostname
	 */
	public String getName() {
		return mName;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(255);
		sb.append("name=").append(mName).append("\n");
		if(mError != null) sb.append("error=").append(mError).append("\n");
		else {
			if ( serverStatus != null) {
				for(int i=0; i < serverStatus.length; ++i) {
					sb.append(serverStatus[i].toString()).append("\n");
				}
			} else {
				sb.append("Server status is not specified (null)");
			}
		}

		return sb.toString();
	}
}


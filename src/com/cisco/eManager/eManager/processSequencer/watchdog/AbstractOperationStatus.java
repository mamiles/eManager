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

import java.net.InetAddress;

//for HA
import com.cisco.eManager.eManager.processSequencer.common.PSInetAddress;

/**
 * Provides some basic functionality used to
 * return the status of a remote operation
 */
public abstract class AbstractOperationStatus
	implements java.lang.Cloneable, java.io.Serializable
{
	protected String mHostName;
	protected String mOpName;
	protected Object mError;
	protected int mFailures;

	/**
	 * Constructor.
	 * @param name operation name
	 * @param host host on which this operation is executed
	 */
	protected AbstractOperationStatus (String name, String host) {
		this.mOpName = name;
		this.mHostName = host;
	}

	/**
	 * Sets the error (description or exception) that occured in performing
	 * an operation.
	 * @param error  Exception or some form of description of the error
	 */
	protected void setError(Object error)
	{
		mError = error;
		mFailures = -1;
	}

	/**
	 * Increments the failure count (usually by one)
	 * @param incr amount to increment failure count by..
	 */
	protected synchronized void addFailures(int incr)
	{
		if(mFailures >= 0) mFailures += incr;
	}

	/**
	 * Gives number of failures (usually one)
	 * @return number of failures.
	 */
	public int getFailures() {
		return mFailures;
	}

	/**
	 * Gives the error object if any
	 * @return the error object. null if the error was not set.
	 */
	public Object getError() {
		return mError;
	}

	/**
	 * Gives the operation name
	 * @return the operation name
	 */
	public String getOperationName() {
		return mOpName;
	}

	/**
	 * Gives the end result of this operation
	 * @return true if there were no errors and no failures
	 */
	public boolean isSuccess() {
		return  (mError == null && mFailures == 0);
	}

	/**
	 * Gives the host name on whic this operation was performed
	 * @return hostname
	 */

	public String getHostName() {
		return mHostName;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(255);
		sb.append("Operation=").append(mOpName).append("\n");
		sb.append("On Host=");

		if(mHostName != null) {
			sb.append(mHostName);
		} else {
			try {
				sb.append(PSInetAddress.getLocalHost().getHostName());
			} catch (Exception ex) {
				sb.append("<Unknown>");
			}
		}
		sb.append("\n");
		if(mError != null) sb.append("error=").append(mError).append("\n");
		return sb.toString();
	}
}



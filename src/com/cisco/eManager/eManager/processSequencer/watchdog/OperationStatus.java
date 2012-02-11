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
 * OperationStatus is used for returning the status
 * of an Operation that doesn't involve multiple sub-operations.
 * Examples of operations whose results can be returned
 * as OperationStatus objects are : getGroups(), getHealth()
 * getDiskStatus() etc. on the WatchdogManager interface. <p>
 *
 * The callers of the methods that return OperationStatus objects
 * will be able to work with the getter methods. Rest of the
 * interface is for internal use within Watchdog.
 *
 * @see WatchdogManager#getGroups
 * @see WatchdogManager#getHealth
 * @see WatchdogManager#getDiskStatus
 * @see WatchdogManager#getConfigProperty
 * @see MultiOpStatus
 */

public class OperationStatus
    extends AbstractOperationStatus
{
	private Object mResult;

	/**
     * @param opName name of the operation
     * @param host name of the host on which the operation was executed.
	 */
    OperationStatus (String opName, String host) {
		super(opName, host);
	}

	/**
     * @param opName name of the operation
     * @param host name of the host on which the operation was executed.
     * @param error error representation (typically an exception) that occured in executing the operation
	 */
    OperationStatus (String opName, String host, Object error) {
		super(opName, host);
		setError(error);
	}

	/**
     * Sets the result of the operation.
	 */

	void setResult(Object result) {
		this.mResult = result;
	}

	/**
     * Gives the result of the operation.
     * @see AbstractOperationStatus#getError
	 */
	public Object getResult() {
		return mResult;
	}

	/**
	 * Gives the stringfied representation of this object.
	 */
	public String toString() {
	    StringBuffer sb = new StringBuffer(255);
		sb.append(super.toString());
		if( isSuccess()) {
			if(mResult != null)
				sb.append(mResult.toString());
			else
				sb.append("null");
		}
		return sb.toString();
	}
}



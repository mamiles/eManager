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

import com.cisco.eManager.eManager.processSequencer.watchdog.WDUtil;

/**
 * Used to store the status of an individual sub-operation
 * when executed as part of an multi-part operation. For
 * example, if the overall opeartion is to restart all
 * the servers in the watchdog, then there will be one
 * OperationResult per server.<p>
 * These OperationResult objects are packaged into a
 * MultiOpStatus, which is returned to the caller of the
 * over-all operation.<p>
 *
 * @see MultiOpStatus
 */
public class OperationResult
	implements java.lang.Cloneable, java.io.Serializable
{
	static final long serialVersionUID = 2002616889092120744L;

	private String mTargetName;
	private String mTargetType;
	private Object mError;
	private Object mResult;

	/**
	 * @param ttype type of the target server or server group
	 * @param tname name of the target
	 */
	OperationResult (String ttype, String tname) {
		this.mTargetType = ttype;
		this.mTargetName = tname;
		this.mError = null;
	}

	/**
	 * @param ttype type of the target server or server group
	 * @param tname name of the target
	 * @param error error that occured in performing this operation
	 */
	public OperationResult (String ttype, String tname, Object error) {
		this.mTargetType = ttype;
		this.mTargetName = tname;
		this.mError = error;
	}

	/**
	 * Sets the result object (if the operation is successful)
	 */
	void setResult(Object result) {
		this.mResult = result;
	}

	/**
	 * Gives the result object of the operation
	 */
	public Object getResult() {
		return mResult;
	}

	/**
	 * Sets the error object
	 */
	void setError(Object error) {
		this.mError = error;
	}

	/**
	 * If there was an error in executing the Operation,
	 * this method returns the object that could be the
	 * cause of the error. Typically this is the exception
	 * Object.
	 */
	public Object getError() {
		return mError;
	}

	/**
	 * Gives the name of the target (name of the server or server group)
	 */
	public String getTargetName() {
		return mTargetName;
	}

	/**
	 * Gives the type of the target (whether it is a server or a server group)
	 */
	public String getTargetType() {
		return mTargetType;
	}

	/**
	 * Returns true if the error is not set or set to null.
	 */
	public boolean isSuccess() {
		return (mError != null);
	}

	/**
	 * Gives the stringified representation of this OperationResult.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(255);
		sb.append("Target type=").append(mTargetType).append("\n");
		sb.append("Target Name=").append(mTargetName).append("\n");
		if(mError != null) {
			sb.append("Failed").append("\n");
			sb.append("Error=").append(mError).append("\n");
			if(mError instanceof Throwable) {
				sb.append("StackTrace=").append("\n");
				sb.append(WDUtil.getStackTraceAsString((Throwable)mError)).append("\n");
			}
		} else sb.append("Success").append("\n");
		return sb.toString();
	}
}



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

import java.util.Vector;
import java.util.List;
import java.util.Iterator;

/**
 * Instances of this Object are used to return the status
 * of an operation that is made up of more than one
 * sub-operation. For example an operation that restarts
 * two servers (A and B) on a host X<p>
 *
 * If the MultiOpStatus fails "totally" the error object
 * (getError) gives the error that should point to the
 * cause of the failure. <p>
 * In all other cases, MultiOpStatus stores the results
 * of sub-operations as individual OperationResult objects
 * in a List. By traversing and observing the list the
 * caller can find out which sub-operation(s) succeded and
 * which one(s) failed (and why)<p>
 *
 * The clients of this class (clients of watchdog) should
 * only be interested in the getter methods. Setters and
 * constructors are used internally by the watchdog.
 * @see OperationResult
 */
public class MultiOpStatus
	extends AbstractOperationStatus
{
	private List mResults;

	/**
	 * @param opName name of the operation
	 * @param host host on which the operation was executed
	 */
	MultiOpStatus (String opName, String host) {
		super(opName, host);
		mResults = new Vector(2);
	}

	/**
	 * @param opName name of the operation
	 * @param host host on which the operation was executed
	 * @param error error occured in executing the operation.
	 * @see MultiOpStatus#setError
	 */
	MultiOpStatus (String opName, String host, Object error) {
		super(opName, host);
		setError(error);
		mResults = new Vector(2);
	}

	/**
	 * Adds an OperationResult object that represents a
	 * successful sub-operation.
	 * @param targetType server or server group
	 * @param targetName name of the server or server group targetted
	 * @see MultiOpStatus#addFailure
	 * @see MultiOpStatus#addResult
	 */
	void addSuccess(String targetType, String targetName) {
		mResults.add( new OperationResult(targetType, targetName));
	}

	/**
	 * Adds an OperationResult object that represents a
	 * failed sub-operation. Increments the failure count by 1.
	 * @param targetType server or server group
	 * @param targetName name of the server or server group targetted
	 * @param error error occured in executing the sub-operation
	 * @see MultiOpStatus#addSuccess
	 * @see MultiOpStatus#addResult
	 */
	void addFailure(String targetType, String targetName, Object error) {
		mResults.add( new OperationResult(targetType, targetName, error));
		addFailures(1);
	}

	/**
	 * Adds an OperationResult object. If success param is false,
	 * increments failure count by 1.
	 * @param opResult Operation result instance
	 * @param success if true, opResult is the result of a successful operation.
	 *				If false, opResult represents a failed operation.
	 * @see MultiOpStatus#addSuccess
	 * @see MultiOpStatus#addFailure
	 */
	void addResult(OperationResult opResult, boolean success) {
		mResults.add(opResult);
		if( !success) addFailures(1);
	}

	/**
	 * Returns the list of OperationResults that "compose" this
	 * MultiOpStatus.
	 * @return a list containing the OperationResult objects, each of which
	 *		 has the information about a failed or successful operation.
	 */
	public List getOperationResults() {
		return mResults;
	}

	/**
	 * Gives a Stringified representation of this Object.
	 * Can be used for display on the terminal.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(255);
		sb.append(super.toString());
		if(mResults == null) sb.append("No results set");
		else {
			Iterator iter = mResults.iterator();
			while(iter.hasNext()) {
				Object obj = iter.next();
				if(obj != null) sb.append(obj.toString()).append("\n");
				else sb.append("null").append("\n");
			}
		}
		return sb.toString();
	}
}



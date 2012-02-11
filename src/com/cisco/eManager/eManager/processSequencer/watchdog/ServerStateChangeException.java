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
 * Exception thrown during the change of server state.
 * Internal to Watchdog.
 */
public class ServerStateChangeException extends WDException
{
	private String mServer;
	private int mOperation;
	private int mCurState;

	public static final int START_OPERATION = 100;
	public static final int STOP_OPERATION  = 101;

	public ServerStateChangeException(String server, int operation, int curState)
	{
		super(server);
		this.mOperation = operation;
		this.mServer   = server;
		this.mCurState = curState;

		if( operation == START_OPERATION) {
			setDetails(WDExConstants.START_FAILED, WDUtil.toArray(server, Server.stateName(curState)));
		} else {
			setDetails(WDExConstants.STOP_FAILED,  WDUtil.toArray(server, Server.stateName(curState)));
		}
	}

	/**
	 * Gives the state of the server under consideration
	 */
	public int getState() {
		return mCurState;
	}

	/**
	 * Gives the operation during which this exception is thrown (START_OPERATION or STOP_OPERATION)
	 */
	public int getOperation() {
		return mOperation;
	}

	/**
	 * Gives the name of the server under consideration
	 */
	public String getServer() {
		return mServer;
	}
}


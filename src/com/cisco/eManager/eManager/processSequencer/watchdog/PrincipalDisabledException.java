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
 * Exception thrown during start up of a server if
 * a principal server is disabled.
 */
public class PrincipalDisabledException extends ServerStateChangeException
{
	private String mPrincipal;
	private int mPrincipalState;

	/**
     * @param server name of the server whose Principal server is disabled
	 * @param operation operation during which it was noticed that the principal is disabled
     * @param state state of the server whose Principal server is disabled
     * @param principal name of the principal server
	 * @param principalState state of the principal server (DISABLED or DISABLED_DEPENDENT)
     */
	public PrincipalDisabledException(String server, int operation, int state,
		String principal, int principalState)
	{
		super(server, operation, state);
		this.mPrincipal = principal;
		this.mPrincipalState = principalState;
		setDetails(WDExConstants.PRINCIPAL_DISABLED, WDUtil.toArray(principal, server, Server.stateName(state)));
	}

	/**
     * Gives the name of the principal server
	 */
	public String getPrincipal() {
		return mPrincipal;
	}

	/**
     * Gives the state of the principal server
	 */
	public int getPrincipalState() {
		return mPrincipalState;
	}
}


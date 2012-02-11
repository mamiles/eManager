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
 * This exception is thrown when the watchdog host name information
 * passed to a method in the WatchdogManager interface
 * is not recognized.
 */
public class UnknownWatchdogException extends WDException
{
	public UnknownWatchdogException(String name) {
		super(WDExConstants.UNKNOWN_WD, WDUtil.toArray(name));
	}
}

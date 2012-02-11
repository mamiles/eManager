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

import java.util.*;

class OrderedServers {

	private String[] mServers;
	private String[] mMissing;

	private static String[] NULL = new String[0];


	String[] getList() {
		return mServers;
	}

	String[] getMissing() {
		return mMissing;
	}

	OrderedServers(String[] srv, String[] miss) {
		mServers = srv;
		mMissing = miss;

		if( mServers == null) mServers = NULL;
		if( mMissing == null) mMissing = NULL;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Found : ").append(WDUtil.toString(mServers));
		sb.append("; Missing : ").append(WDUtil.toString(mMissing));

		return sb.toString();
	}
}

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
import java.io.Serializable;

/**
 * To be used to relay configuration change messages
 * from the master machine to the other machines
 * <p>
 * Implementation is not yet complete.
 */
public class ConfigChangeData implements Serializable {

	private static final int VERSION_CHANGE = -1;

	private Map mChanges;
	private boolean isVersionChange;

	public ConfigChangeData(String property, String value) {
		mChanges = new HashMap();
		mChanges.put(property, value);
	}

	public ConfigChangeData(Map changes) {
		mChanges = changes;
	}

	public ConfigChangeData() {
		isVersionChange = true;
	}

	public int getNumChanges() {
		if( isVersionChange  || mChanges == null) return VERSION_CHANGE;
		return mChanges.size();
	}

	public Map getChanges() {
		return mChanges;
	}

	public boolean isVersionChange() {
		return isVersionChange;
	}
}


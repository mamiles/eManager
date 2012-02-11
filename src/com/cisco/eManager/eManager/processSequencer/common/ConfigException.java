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
package com.cisco.eManager.eManager.processSequencer.common;

/**
 * This exception is thrown when the property queried
 * for, is not present.
 */

public class ConfigException extends RuntimeException {
	private String mProperty;
	private String mReason;
	//private int mCode;

	public ConfigException(String path, String reason) {
		super(reason);
		mProperty = path;
		mReason = reason;
	}

	public ConfigException(String path)
	{
		this(path, "Not found");
	}

	public String getMessage() {
		return mReason + " : " + mProperty;
	}

	public String getPropertyName() {
		return mProperty;
	}

	public String toString()
	{
		return getMessage();
	}
}



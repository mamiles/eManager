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

public class ConfigStoreException extends Exception {

	public static final int GENERAL = 0;
	public static final int NOT_INITED  = 100;
	public static final int UNSUPPORTED = 200;
	public static final int MASTER_ONLY = 300;
	public static final int NETWORK  = 400;
	public static final int CANNOT_LOCATE  = 500;
	public static final int DB_INCONSISTENT  = 600;
	public static final int BOOTSTRAP  = 700;
	public static final int CANNOT_CONNECT  = 800;

	private int mCode;

	private Throwable mCause;

	public ConfigStoreException(String reason) {
		this(reason, GENERAL);
	}

	public ConfigStoreException(String reason, int code) {
		super(reason);
		mCode = code;
	}

	public ConfigStoreException(String reason, int code, Throwable cause) {
		super(reason);
		mCode = code;
		mCause = cause;
	}

	public ConfigStoreException(String reason, Throwable cause) {
		super(reason);
		mCause = cause;
	}

	public Throwable getCause() {
		return mCause;
	}

	public int getCode() {
		return mCode;
	}

	public void setCode(int code) {
		mCode = code;
	}

	public String toString() {
		return super.getMessage() + " " + getCodeStr();
	}

	private String getCodeStr() {
		switch (mCode) {
			case DB_INCONSISTENT :
				return "Unexpected data in the database";
			case CANNOT_LOCATE :
				return "Cannot locate";
			case MASTER_ONLY :
				return "Can be executed only on master";
			case CANNOT_CONNECT :
				return "Cannot connect to the persisent store";
			case BOOTSTRAP :
				return "Error during boot strapping the config store";
			case GENERAL :
			default :
				return "General Exception";
		}
	}
}



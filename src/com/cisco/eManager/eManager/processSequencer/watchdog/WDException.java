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
import com.cisco.eManager.eManager.processSequencer.common.PSException;

import java.util.Locale;

/**
 * An extension of PSException that can provide
 * internationalized error messages using resources
 * in the Watchdog package <p>
 * Do not catch "WDException" in the code, because
 * it can go away. <p>
 * This class is local to watchdog.
 */
public class WDException extends PSException
	implements java.io.Serializable {

	protected Object[] mArgs = null;
	protected Object mCode;

	/**
 	 * Exception in the non-localizable format
	 */
	public WDException(String reason) {
		super(reason);
	}

	/**
 	 * Exception in the non-localizable format
	 */
	public WDException(String reason, Throwable th) {
		super(reason, th);
	}

	/**
 	 * Exception that is localizable, but has no args
	 */
	public WDException(Object exKey) {
		super("");
		mCode = exKey;
	}

	/**
 	 * Exception that is localizable, but has no args
	 */
	public WDException(Object exKey, Throwable th) {
		super("", th);
		mCode = exKey;
	}

	/**
 	 * Exception that is localizable with args
	 */
	public WDException(Object exKey, Object args[]) {
		super("");
		mCode = exKey;
		mArgs = args;
	}

	/**
 	 * Exception that is localizable with args and a nested exception
	 */
	public WDException(Object exKey, Object args[], Throwable th) {
		super("", th);
		mCode = exKey;
		mArgs = args;
	}

	/**
 	 * Gives the message in the default locale (if localizable).
	 * Otherwise gives the message returned by Exception.getMessage()
	 */
	public String getMessage() {
		if ( mCode == null ) return super.getMessage();
		return getMessage ( null );
	}

	/**
 	 * Gives the message using the given locale (if this exception is
	 * constructed to have a localizable message).
	 * Otherwise gives the message returned by Exception.getMessage()
	 */
	public String getMessage(String bundle, Locale locale) {
		return getMessage(locale);
	}

	/**
 	 * Gives the message for the locale provided
	 */
	public String getMessage(Locale locale) {
		if ( mCode == null ) return super.getMessage();
		return WDExUtil.formatMessage( mCode,  mArgs, locale);
	}

	/**
 	 * Sub classes can set the key and arguments to the
     * message.
	 */
	protected void setDetails(Object exKey, Object[] args) {
		mCode = exKey;
		mArgs = args;
	}
}

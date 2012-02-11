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

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.text.MessageFormat;


/**
 * This class provides a few utility methods used by
 * other watchdog classes and exceptions for formatting
 * error messages and other related tasks.
 */
public final class WDExUtil
{
	private WDExUtil() {}

	public static String formatMessage ( Object reason) {
		return formatMessage(reason, null);
	}

	public static String formatMessage ( Object reason, Object arg1) {
		return formatMessage( reason, new Object[] {arg1});
	}

	public static String formatMessage ( Object reason, Object arg1, Object arg2) {
		return formatMessage( reason, new Object[] {arg1, arg2});
	}

	public static String formatMessage ( Object reason, Object arg1, Object arg2, Object arg3) {
		return formatMessage( reason, new Object[] {arg1, arg2, arg3});
	}

	public static String formatMessage ( Object reason, Object arg1, Object arg2, Object arg3, Object arg4) {
		return formatMessage( reason, new Object[] {arg1, arg2, arg3, arg4});
	}

	public static String formatMessage( Object reason, Object[] args) {
		return formatMessage(reason, args, WDExConstants.BUNDLE, null);
	}

	public static String formatMessage( Object reason, Object[] args, Locale locale) {
		return formatMessage(reason, args, WDExConstants.BUNDLE, null);
	}

	private static String getKey(Object reason) {
		String key = null;
		if( reason instanceof Integer) {
			key = WDExConstants.EX_KEY_PREFIX + reason.toString();
		}
		return key;
	}

	private static String prefixKey(String key, String val) {
		if ( val == null ) val = "";
		return new StringBuffer(80).append(key).append(" :: ").append(val).toString();
	}

	public static String formatMessage(Object reason, Object[] args, String bundle, Locale locale) {

		if ( reason == null) return null;

		String key = getKey(reason);

		if( key == null) return reason.toString();

		ResourceBundle rb = null;
		try {
			if( locale != null) {
				rb = ResourceBundle.getBundle(bundle, locale);
			} else {
				rb = ResourceBundle.getBundle(bundle);
			}
		} catch (java.util.MissingResourceException mre) {
			return prefixKey(key, "");
		}

		try {
			String str = rb.getString(key);
			if( str != null) {
				if( args != null && args.length != 0) {
					MessageFormat mft = new MessageFormat(str);
					return prefixKey (key, mft.format(args));
				} else return prefixKey(key, str);
			} else return prefixKey(key, "");
		} catch (MissingResourceException mre) {
			return prefixKey(key, "");
		}
	}
}

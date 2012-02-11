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

import com.cisco.eManager.eManager.processSequencer.common.DCPLib;
import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;

import java.io.PrintWriter;


/**
 * Convenience methods for interacting with proeprties.
 */
public class PropertyUtils {

	/**
	 * Get an integer property and ensure it is in a legal range of
	 * values.
	 *
	 * @param name A textual name for the property.
	 * @param propertyNames A property name to try.
	 * @param minValue The minimum value for the property.
	 * @param maxValue The maximum value for the property.
	 * @param defaultValue The default value for the property.
	 * @param log Where to send warnings to.
	 * @return the value of the property
	 */
	public static int getIntProperty (String name,
						String propertyName,
						int minValue,
						int	maxValue,
						int defaultValue) {
		String[] propertyNames = new String[1];
		propertyNames[0] = propertyName;
		return getIntProperty (name, propertyNames, minValue, maxValue,
				 defaultValue, null);
	}

	/**
	 * Get an integer property and ensure it is in a legal range of
	 * values.
	 *
	 * @param name A textual name for the property.
	 * @param propertyNames A list of property names to try, in order.
	 * @param minValue The minimum value for the property.
	 * @param maxValue The maximum value for the property.
	 * @param defaultValue The default value for the property.
	 * @param log Where to send warnings to.
	 * @return the value of the property
	 */
	public static int getIntProperty (String name,
						String[] propertyNames,
						int minValue,
						int	maxValue,
						int defaultValue) {
		return getIntProperty (name, propertyNames, minValue, maxValue,
				 defaultValue, null);
	}


	/**
	 * Get an integer property and ensure it is in a legal range of
	 * values.
	 *
	 * @param name A textual name for the property.
	 * @param propertyNames A list of property names to try, in order.
	 * @param minValue The minimum value for the property.
	 * @param maxValue The maximum value for the property.
	 * @param defaultValue The default value for the property.
	 * @param logger The log to write warnings to.
	 * @return the value of the property
	 */
	public static int getIntProperty (String name,
						String[] propertyNames,
						int minValue,
						int maxValue,
						int defaultValue,
						CiscoLogger logger)
	 {
		int rc = defaultValue;

		for (int i = 0; i < propertyNames.length; i++) {
			String propertyValue = DCPLib.getProperty (propertyNames[i], null);
			if (propertyValue != null) {
				try {
					rc = Integer.parseInt (propertyValue);
					break;
				}
				catch (NumberFormatException nfe) {
					rc = defaultValue;
					if( logger != null) {
						logger.config("Invalid property setting.");
						logger.config( propertyNames[i] + " = " + propertyValue);
						logger.config( "Using default value of " + defaultValue + " instead.");
					}
				}
			}
		}

		if (rc < minValue) {
			rc = minValue;
			if(logger != null) {
				logger.config( "Setting for " + name + " is too small.");
				logger.config( "Using minimum value of " + minValue + " instead.");
			}
		} else if (rc > maxValue) {
			rc = maxValue;
			if( logger != null) {
				logger.config( "Setting for " + name + " is too large.");
				logger.config( "Using maximum value of " + maxValue + " instead.");
			}
		}

		return rc;
	}


	/**
	 * Get an integer property and ensure it is in a legal range of
	 * values.
	 *
	 * @param name A textual name for the property.
	 * @param propertyNames A list of property names to try, in order.
	 * @param minValue The minimum value for the property.
	 * @param maxValue The maximum value for the property.
	 * @param defaultValue The default value for the property.
	 * @param log The log to write warnings to.
	 * @return the value of the property
	 */
	public static int getIntProperty (String name,
						String[] propertyNames,
						int minValue,
						int maxValue,
						int defaultValue,
						PrintWriter log,
						String context)
	 {
		int rc = defaultValue;

		for (int i = 0; i < propertyNames.length; i++) {
			String propertyValue = DCPLib.getProperty (propertyNames[i], null);
			if (propertyValue != null) {
				try {
					rc = Integer.parseInt (propertyValue);
					break;
				}
				catch (NumberFormatException nfe) {
					rc = defaultValue;
					warning (log, context, "Invalid property setting.");
					warning (log, context, "	" + propertyNames[i] + " = " + propertyValue);
					warning (log, context,
						 "Using default value of " + defaultValue + " instead.");
				}
			}
		}

		if (rc < minValue) {
			rc = minValue;
			warning (log, context, "Setting for " + name + " is too small.");
			warning (log, context, "Using minimum value of " + minValue + " instead.");
		} else if (rc > maxValue) {
			rc = maxValue;
			warning (log, context, "Setting for " + name + " is too large.");
			warning (log, context, "Using maximum value of " + maxValue + " instead.");
		}

		return rc;
	}

	/**
	 * Get a long property and ensure it is in a legal range of
	 * values.
	 *
	 * @param name A textual name for the property.
	 * @param propertyNames A list of property names to try, in order.
	 * @param minValue The minimum value for the property.
	 * @param maxValue The maximum value for the property.
	 * @param defaultValue The default value for the property.
	 * @param log Where to send warnings to.
	 * @return the value of the property
	 */
	public static long getLongProperty (String name,
							String[] propertyNames,
							long minValue,
							long maxValue,
							long defaultValue) {
		return getLongProperty (name, propertyNames, minValue, maxValue,
					defaultValue, null);
	}


	/**
	 * Get a long property and ensure it is in a legal range of
	 * values.
	 *
	 * @param name A textual name for the property.
	 * @param propertyName A property name to try.
	 * @param minValue The minimum value for the property.
	 * @param maxValue The maximum value for the property.
	 * @param defaultValue The default value for the property.
	 * @param log Where to send warnings to.
	 * @return the value of the property
	 */
	public static long getLongProperty (String name,
							String propertyName,
							long minValue,
							long maxValue,
							long defaultValue) {
		String[] properties = new String[] {propertyName};
		return getLongProperty (name, properties, minValue, maxValue,
					defaultValue, null);
	}


	/**
	 * Get a long property and ensure it is in a legal range of
	 * values.
	 *
	 * @param name A textual name for the property.
	 * @param propertyNames A list of property names to try, in order.
	 * @param minValue The minimum value for the property.
	 * @param maxValue The maximum value for the property.
	 * @param defaultValue The default value for the property.
	 * @param log Where to send warnings to.
	 * @return the value of the property
	 */
	public static long getLongProperty (String name,
							String propertyName,
							long minValue,
							long maxValue,
							long defaultValue,
							CiscoLogger logger) {
		String[] properties = new String[] {propertyName};
		return getLongProperty (name, properties, minValue, maxValue,
					defaultValue, logger);
	}

	/**
	 * Get a long property and ensure it is in a legal range of
	 * values.
	 *
	 * @param name A textual name for the property.
	 * @param propertyNames A list of property names to try, in order.
	 * @param minValue The minimum value for the property.
	 * @param maxValue The maximum value for the property.
	 * @param defaultValue The default value for the property.
	 * @param logger Where to send warnings to.
	 * @return the value of the property
	 */
	public static long getLongProperty (String name,
							String[] propertyNames,
							long minValue,
							long maxValue,
							long defaultValue,
							CiscoLogger logger)
	{
		long rc = defaultValue;

		for (int i = 0; i < propertyNames.length; i++) {
			String propertyValue = DCPLib.getProperty (propertyNames[i], null);
			if (propertyValue != null) {
				try {
					rc = Long.parseLong (propertyValue);
					break;
				}
				catch (NumberFormatException nfe) {
					rc = defaultValue;
					if( logger != null) {
						logger.config( "Invalid property setting.");
						logger.config( propertyNames[i] + " = " + propertyValue);
						logger.config( "Using default value of " + defaultValue + " instead.");
					}
				}
			}
		}

		if (rc < minValue) {
			rc = minValue;
			if( logger != null) {
				logger.config( "Setting for " + name + " is too small.");
				logger.config( "Using minimum value of " + minValue + " instead.");
			}
		} else if (rc > maxValue) {
			rc = maxValue;
			if( logger != null) {
				logger.config( "Setting for " + name + " is too large.");
				logger.config( "Using maximum value of " + maxValue + " instead.");
			}
		}

		return rc;
	}

	/**
	 * Get a long property and ensure it is in a legal range of
	 * values.
	 *
	 * @param name A textual name for the property.
	 * @param propertyNames A list of property names to try, in order.
	 * @param minValue The minimum value for the property.
	 * @param maxValue The maximum value for the property.
	 * @param defaultValue The default value for the property.
	 * @param log Where to send warnings to.
	 * @return the value of the property
	 */
	public static long getLongProperty (String name,
							String[] propertyNames,
							long minValue,
							long maxValue,
							long defaultValue,
							PrintWriter log,
							String context) {
		long rc = defaultValue;

		for (int i = 0; i < propertyNames.length; i++) {
			String propertyValue = DCPLib.getProperty (propertyNames[i], null);
			if (propertyValue != null) {
				try {
					rc = Long.parseLong (propertyValue);
					break;
				}
				catch (NumberFormatException nfe) {
					rc = defaultValue;
					warning (log, context, "Invalid property setting.");
					warning (log, context,
						 "	" + propertyNames[i] + " = " + propertyValue);
					warning (log, context,
						 "Using default value of " + defaultValue + " instead.");
				}
			}
		}

		if (rc < minValue) {
			rc = minValue;
			warning (log, context, "Setting for " + name + " is too small.");
			warning (log, context,
				 "Using minimum value of " + minValue + " instead.");
		} else if (rc > maxValue) {
			rc = maxValue;
			warning (log, context, "Setting for " + name + " is too large.");
			warning (log, context,
				 "Using maximum value of " + maxValue + " instead.");
		}

		return rc;
	}

	/**
	 * Gets property given a list of names to be tried in order
	 * @param propertyNames A list of property names to try, in order.
	 */
	public static String getProperty ( String[] propertyNames, String defaultValue) {
		String rc = defaultValue;

		for (int i = 0; i < propertyNames.length; i++) {
			String propertyValue = DCPLib.getProperty (propertyNames[i], null);
			if (propertyValue != null) {
				return rc;
			}
		}

		return rc;
	}

	/**
	 * Gets an int property given a list of names to be tried in order
	 * @param propertyNames A list of property names to try, in order.
	 */
	public static int getIntProperty ( String[] propertyNames, int defaultValue, CiscoLogger log)
	{
		int rc = defaultValue;

		for (int i = 0; i < propertyNames.length; i++) {
			String propertyValue = DCPLib.getProperty (propertyNames[i], null);
			if (propertyValue != null) {
				try {
					rc = Integer.parseInt(propertyValue);
					break;
				} catch (NumberFormatException nfe) {
					if( log != null) {
						log.warning(propertyNames[i] + " is a non-integer value : " + propertyValue);
					}
				}
			}
		}

		return rc;
	}

	/**
	 * Gets an long property given a list of names to be tried in order
	 * @param propertyNames A list of property names to try, in order.
	 */
	public static long getLongProperty ( String[] propertyNames, long defaultValue, CiscoLogger log)
	{
		long rc = defaultValue;

		for (int i = 0; i < propertyNames.length; i++) {
			String propertyValue = DCPLib.getProperty (propertyNames[i], null);
			if (propertyValue != null) {
				try {
					rc = Long.parseLong(propertyValue);
					break;
				} catch (NumberFormatException nfe) {
					if( log != null) {
						log.warning(propertyNames[i] + " is a non-numeric value : " + propertyValue);
					}
				}
			}
		}

		return rc;
	}


	/**
	 * Issue a warning message.	The message is prefixed with
	 * "Warning(context): " if context information is present, otherwise
	 * it is prefixed with just "Warning: ".
	 *
	 * @param log Where to send the message to.	If this is null,
	 *	 the message is sent to System.err.
	 * @param context Context information for the warning message.
	 * @param msg The warning message.
	 */
	public static void warning (PrintWriter log, String context, String msg) {
		StringBuffer buf =
			new StringBuffer ((context == null ? 0 : context.length ())
			+ msg.length ()
			+ 15 /* "warning():".length() plus slop */);

		buf.append ("Warning");
		if (context != null) {
			buf.append ("(")
				.append (context)
				.append (")");
		}
		buf.append (":");
		buf.append (msg);

		if (log == null) {
			System.err.println (buf.toString ());
			System.err.flush ();
		} else {
			log.println (buf.toString ());
			log.flush ();
		}
	}
}

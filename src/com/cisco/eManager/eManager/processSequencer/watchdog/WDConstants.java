/*********************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.	Possessions and use of this
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
 * Defines the constants used by various classes in this
 * package.
 */
public interface WDConstants {

	/**
	 * This is used in building the property strings
	 * to look up watchdof related properties in the
	 * configuration.
	 */
	public static final String WD_PREFIX="watchdog";

	/**
	 * The string that is prefixed to the actual subject in the
	 * events sent out by watchdog.
	 */
	public static final String WD_EVENT_PREFIX = "com.cisco.eManager.eManager.processSequencer.watchdog.";

	/**
	 * Used in registering/lookup of master watchdog in the Naming service
	 */
	public static final String WD_SERVICE="Watchdog";

	/*
	 * Following constants are used in sending TIBCO messages
	 * about the state of the watchdog.
	 */
	public static final String WD_HOSTNAME = "hostname";
	public static final String WD_EVENT_STOP = "stop";
	public static final int	   WD_EVENT_VERSION = 1;
	public final static String HEARTBEAT_SUBJECT = WD_PREFIX +".heartbeat";
	public final static String HEARTBEAT_IDLER_SUBJECT = WD_PREFIX + ".heartbeat.idler";
	public final static String SNAPSHOT_SUBJECT = WD_PREFIX + ".snapshot";
	public final static String STATE_SUBJECT = WD_PREFIX + ".state";

	public final static String VERSION_INFO_FILE= ".version";

	public static final String WD_DISK_SPACE_PREFIX ="watchdog.diskspace";

	public static final String PROP_LOW_WATERMARK	 = WD_DISK_SPACE_PREFIX + ".lowWatermark";
	public static final String PROP_HIGH_WATERMARK	 = WD_DISK_SPACE_PREFIX + ".highWatermark";
	public static final String PROP_DS_EMAIL_RCPTS	     = WD_DISK_SPACE_PREFIX + ".emailRecipients";
	public static final String PROP_SLEEP_INTERVAL   = WD_DISK_SPACE_PREFIX + ".sleepInterval";
	public static final String PROP_DIRS_TO_MONITOR	 = WD_DISK_SPACE_PREFIX + ".dirsToMonitor";
	public static final String PROP_DISKS_TO_MONITOR = WD_DISK_SPACE_PREFIX + ".disksToMonitor";

	public static final String PROP_LOG_LOC	= "Logging.Defaults.logLocation";
	public static final String PROP_REP_LOC	= "repository.persistence.location";
	public static final String PROP_TMP_DIR	= "SYSTEM.tmpdir";

	public static final String SYS_EMAIL_PREFIX ="SYSTEM.email";
	public static final String PROP_EMAIL_HOST  = SYS_EMAIL_PREFIX + ".smtpHost";
	public static final String PROP_EMAIL_FROM  = SYS_EMAIL_PREFIX + ".from";


	public static final String LOGGER_NAME = "watchdog.serverStatus";
	public static final String WD_SERV_STAT_PREFIX ="watchdog.serverStatus";

	public static final String PROP_SS_EMAIL_RCPTS	= WD_SERV_STAT_PREFIX + ".emailRecipients";
	public static final String PROP_STABLE_TIME = WD_SERV_STAT_PREFIX + ".stableTime";

	public static final String DEFAULT_EMAIL_FROM = "eManager Watchdog";
}


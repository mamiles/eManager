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
 * Defines the constants used as exception keys.
 */
public interface WDExConstants {

	public static final String EX_KEY_PREFIX = "WD_";
	public static final String BUNDLE = "com.cisco.eManager.eManager.processSequencer.watchdog.WDResources";

	public static final int WD_EX_START = 100;

	public static final Integer UNKNOWN                            = new Integer ( WD_EX_START );
	public static final Integer INSUFFICIENT_CONFIG                = new Integer ( WD_EX_START + 1);
	public static final Integer INVALID_FORMAT                     = new Integer ( WD_EX_START + 2);
	public static final Integer ILLEGAL_VALUE                      = new Integer ( WD_EX_START + 3);
	public static final Integer NULL_VALUE                         = new Integer ( WD_EX_START + 4);
	public static final Integer DISKSPACE_DIFF_UNITS               = new Integer ( WD_EX_START + 5);
	public static final Integer UNSPEC_PROPERTY                    = new Integer ( WD_EX_START + 6);
	public static final Integer ILLEGAL_ARGUMENT                   = new Integer ( WD_EX_START + 7);
	public static final Integer NAMESERVER_DISCONNECT              = new Integer ( WD_EX_START + 8);
	public static final Integer INVALID_DATA                       = new Integer ( WD_EX_START + 9);
	public static final Integer CANNOT_UNREGISTER                  = new Integer ( WD_EX_START + 10);
	public static final Integer UNTERMINATED_STRING                = new Integer ( WD_EX_START + 11);
	public static final Integer STATE_TRANSITION_ERROR             = new Integer ( WD_EX_START + 12);
	public static final Integer UNKNOWN_WD                         = new Integer ( WD_EX_START + 13);
	public static final Integer UNKNOWN_HOST                       = new Integer ( WD_EX_START + 14);
	public static final Integer UNKNOWN_SERVER                     = new Integer ( WD_EX_START + 15);
	public static final Integer UNKNOWN_SERVER_GROUP               = new Integer ( WD_EX_START + 16);
	public static final Integer FAILED_OPERATION                   = new Integer ( WD_EX_START + 17);
	public static final Integer NO_CONFIG_DISKSTATUS               = new Integer ( WD_EX_START + 18);
	public static final Integer START_FAILED                       = new Integer ( WD_EX_START + 19);
	public static final Integer STOP_FAILED                        = new Integer ( WD_EX_START + 20);
	public static final Integer GROUPS_EXIST                       = new Integer ( WD_EX_START + 21);
	public static final Integer SERVERS_EXIST                      = new Integer ( WD_EX_START + 22);
	public static final Integer ILLEGAL_CMD_ARG                    = new Integer ( WD_EX_START + 23);
	public static final Integer PRINCIPAL_DISABLED                 = new Integer ( WD_EX_START + 24);
	public static final Integer ARG_NULL                           = new Integer ( WD_EX_START + 25);
	public static final Integer NULL_OR_EMPTY                      = new Integer ( WD_EX_START + 26);
	public static final Integer ILLEGAL_TYPE                       = new Integer ( WD_EX_START + 27);
	public static final Integer ELEMENTS_NULL                      = new Integer ( WD_EX_START + 28);
	public static final Integer ILLEGAL_KEY                        = new Integer ( WD_EX_START + 29);
	public static final Integer CANNOT_REGISTER                    = new Integer ( WD_EX_START + 30);
	public static final Integer WD_INACTIVE                        = new Integer ( WD_EX_START + 31);
}


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
package com.cisco.eManager.eManager.processSequencer.common.logging;

import com.cisco.eManager.eManager.processSequencer.common.DCPLib;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.File;

/**
 * Convenience methods for determing log location
 * and redirecting stdout/stderr
 */
public class LogUtil {

	public static String DEFAULT_LOG_LOCATION = "/tmp";
	public static String DEFAULT_TASKLOG_LOCATION = "/tmp/TaskLogs";

	private static final String LOG_LOC="Logging.Defaults.logLocation";
	private static final String TASKLOG_LOC="Logging.TaskLogs.logLocation";

	private static final int MAX_CHARS_PER_LINE = 80;

	private LogUtil(){}

	private static String logLocation;
	private static String taskLogLocation;

	/**
	 * Get the directory where log files should be located
	 * @return the directory where log files should be located
	 */
	public static String getLogLocation ()
	{
		return DCPLib.getProperty(LOG_LOC, DEFAULT_LOG_LOCATION);
	}

	/**
	 * Get the directory where tasklogs should be located
	 * @return the directory where the task log files should be located
	 */
	public static String getTaskLogLocation ()
	{
		return DCPLib.getProperty(TASKLOG_LOC, DEFAULT_TASKLOG_LOCATION);
	}

	public static String getLogLocation (java.util.Properties props) {
		return props.getProperty(LOG_LOC, DEFAULT_LOG_LOCATION);
	}

	/**
	 * Gives the name of this process as given by the System property "process.name"
	 * @return the name of this process as given by the System property "process.name"
	 */
	public static String	getServerName() {
		String serverName = System.getProperty("process.name");
		if(serverName == null || serverName.trim().equals("")) {
				serverName = "unknown";
		}
		return serverName;
	}

        public static String getAppType() {
            String appType = System.getProperty("appType");
            if(appType == null || appType.trim().equals("")) {
                                appType = "unknown";
                }
                return appType;
        }

        public static String getAppInst() {
            String appInst = System.getProperty("appInst");
            if(appInst == null || appInst.trim().equals("")) {
                                appInst = "unknown";
                }
                return appInst;
        }

	/**
	 * Opens a file &lt;serverName&gt;.out in the logging directory and
	 * redirects the stdout and stderr of this process to that file.
	 * @throws IOException if it fails to open the file for writing
	 */
	public static void redirectOutputStreams()
		throws IOException
	{
		String fileName = getLogLocation() + File.separator + getServerName() + ".out";
		com.cisco.eManager.eManager.processSequencer.common.IOUtil.redirectIOStreams(fileName);
	}

	/**
     * @returns the value of the property if it is "tweaked" by this
     * class. null otherwise.
     */

	public static String getProperty(String str) {
		if( str.equals(LOG_LOC)) return logLocation;
		if( str.equals(TASKLOG_LOC)) return taskLogLocation;
		return DCPLib.getProperty(str, null);
	}

	public static String splitLine(String line) {
		return splitLine(line, MAX_CHARS_PER_LINE);
	}

	/**
     * Breaks up the string to be logged into multiple lines
     * so that each line is atmost "maxChars" in length
     */
	public static String splitLine(String line, int maxChars) {

		if( line == null || line.length() <= maxChars) return line;
		int len = line.length();

		int pos = 0;
		int endPos = 0;

		StringBuffer sb = new StringBuffer(len + 16);

		while( endPos < len ) {
			endPos = (pos + maxChars > len) ? len : pos + maxChars;
			sb.append(line.substring(pos, endPos)).append("\n");
			//System.out.println(pos + " -- " + endPos);
			pos = endPos;
		}
		return sb.toString();
	}
}

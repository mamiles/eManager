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

import java.util.List;
import java.util.Vector;
import java.util.Date;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;

import java.io.PrintWriter;

/**
 * A utility class used internally by the watchdog and the
 * command-line watchdog client to print formatted status
 * information.
 */
public final class PrintHelper {

	private PrintHelper() {}

	final static String msSpaces = "                                                                                                                ";
	final static String msDots = "................................................................................................................";
	final static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss z");
	static String servStatusHeader = null;
	static {
		StringBuffer sb = new StringBuffer("Name"
			+ msSpaces.substring (4, 21)
			+ "State"
			+ msSpaces.substring (5, 16)
			+ "Gen"
			+ msSpaces.substring (3, 6)
			+ "Exec Time"
			+ msSpaces.substring (9, 26)
			+ "PID"
			+ msSpaces.substring (3, 7)
			+ "Success"
			+ msSpaces.substring (7, 9)
			+ "Missed");
			//+ msSpaces.substring (7, 9)
			//+ "Native Log");

		servStatusHeader = sb.toString();
	}

	public static void print(PrintWriter pWrtr, Object msg, Object[] objs, boolean sameLine)
	{
		if(sameLine) {
			if(msg != null) pWrtr.print(msg + " [ ");
			else pWrtr.print("[ ");
			int i=0;
			for(; i < objs.length -1; ++i) {
				pWrtr.print(objs[i] + ", ");
			}
			pWrtr.println(objs[i] + " ]");
		} else {
			if( msg != null) pWrtr.println(msg);
			for(int i=0; i < objs.length; ++i) {
				pWrtr.println((i+1) + ") " + objs[i]);
			}
			pWrtr.println();
		}
	}

	public static void print(PrintWriter pWrtr, String op, TargetComponentData tcd, MultiOpStatus[] rv)
	{
		pWrtr.println();
		pWrtr.println("Result of " + op + " for target " );
		pWrtr.println(tcd);
		for(int i=0; i < rv.length; ++i) {
			pWrtr.println();
			pWrtr.println(rv[i]);
		}
		pWrtr.println();
	}

	/**
	 * Print server status information.
	 *
	 * @param pWrtr The stream to print to.
	 * @param status The status information to print
	 */
	public static void print (PrintWriter pWrtr, WDServerStatus[] status)
	{
		for(int i=0; i< status.length; ++i) {
			print(pWrtr, status[i]);
		}
	}

	public static void print (PrintWriter pWrtr,
					WDServerStatus status)
	{

		if(status != null) {
			pWrtr.println();
			pWrtr.println("Status on machine : " + status.getName());
			Object err = status.getError();
			if(err != null) {
				pWrtr.println("There was an error in status retrieval ");
				if( err instanceof Throwable ) {
					((Throwable)err).printStackTrace(pWrtr);
				} else {
					pWrtr.println("Error : " + err);
				}
			} else {
				print(pWrtr, status.getServerStatus());
			}
			pWrtr.println();
		}
	}

	public static void print(PrintWriter pWrtr, Exception status) {
		if (status != null) {
			pWrtr.println("Exception : " + status.getMessage());
			status.printStackTrace(pWrtr);
		}
	}


	public static void print(PrintWriter pWrtr, ServerStatus[] status) {

		if (status != null) {
			pWrtr.println(servStatusHeader);

			for (int i = 0; i < status.length; i++) {
				String generationString = Integer.toString (status[i].getGeneration());
				String successfulString =
						Integer.toString (status[i].getSuccessfulHeartbeats());
				String pidString =
						status[i].getPid() == 0 ? "" : Long.toString (status[i].getPid());
				String missedString =
					Integer.toString (status[i].getMissedHeartbeats());
				String dateString = "";
				if (status[i].execTime != 0) {
					dateString =	getDateString(status[i].getExecTime());
				}
				String name  = status[i].getName().trim();
				String state = status[i].getState();

				pWrtr.println (name
					+ padding (name.length(), 20)
					+ " "
					+ state
					+ padding (state.length(), 15)
					+ " "
					+ generationString
					+ padding (generationString.length(), 5)
					+ " "
					+ dateString
					+ padding (dateString.length (), 25)
					+ " "
					+ pidString
					+ padding (pidString.length (), 6)
					+ " "
					+ successfulString
					+ padding (successfulString.length(), 8)
					+ " "
					+ missedString);
					//+ padding (missedString.length(), 7)
					//+ " "
					//+ status[i].isLogNative());
			}
		}
		pWrtr.flush ();
	}

	public static void print(PrintWriter pWrtr, String msg, HostStatus[] status) {

		if (status != null && status.length > 0) {

			for (int i = 0; i < status.length; i++) {

				String durationString ="-";
				if (status[i].getLastHeartbeatDuration() != 0) {
						durationString = Long.toString (status[i].getLastHeartbeatDuration()) + " ms";
				}

				String regString =    "-";
				if (status[i].getRegisteredAt() != 0) {
					regString = getDateString(status[i].getRegisteredAt());
				}

				String unregString =  "-";
				if (status[i].getUnregisteredAt() != 0) {
					unregString =	getDateString(status[i].getUnregisteredAt());
				}

				String lastHBString = "-";
				if (status[i].getLastHeartbeatAt() != 0) {
					lastHBString =	getDateString(status[i].getLastHeartbeatAt());
				}

				String successString= "-";
				Boolean b = status[i].getLastHeartbeatSuccessful();
				if( b != null ) {
					if( b.equals(Boolean.TRUE)) { successString = "yes"; }
					else { successString = "no"; }
				}

				String hostName = status[i].getHostName().trim();
				String ipAddr = status[i].getIPAddress();

				StringBuffer sb = new StringBuffer("-----------------------------------------------\n");
				sb.append("Host name :          ").append( hostName).append("\n");
				sb.append("IP Address :         ").append( ipAddr).append("\n");
				sb.append("Is Master? :         ").append( status[i].isMaster()).append("\n");
				sb.append("Is Active? :         ").append((status[i].isActive() ? "yes" : "no")).append("\n");
				sb.append("Registered at :      ").append(regString).append("\n");
				sb.append("Unregistered at :    ").append(unregString).append("\n");
				sb.append("Last Heartbeat at :  ").append(lastHBString).append("\n");
				sb.append("Success :            ").append(successString).append("\n");
				sb.append("Heartbeat duration : ").append(durationString).append("\n");
				sb.append("System tmp dir:      ").append(status[i].getTmpDir()).append("\n");
				sb.append("Log location :       ").append(status[i].getLogLocation()).append("\n");
				sb.append("Task log location :  ").append(status[i].getTaskLogLocation()).append("\n");
				sb.append("Image information :  ");

				String imgList[] = status[i].getImageList();
				for(int j=0; j < imgList.length; ++j) {
					sb.append(imgList[j]).append("\n");
					sb.append("                     ");
				}
				sb.append("\n");
				sb.append("-----------------------------------------------").append("\n");
				pWrtr.println(sb);

			}
		} else {
			pWrtr.println("No status to display");
		}
		pWrtr.println();
		pWrtr.flush ();
	}

	static String getDateString(long l) {
		return dateFormat.format(new Date(l));
	}

	static String padding (int len, int fieldLen) {
		return (len < fieldLen) ? msSpaces.substring (len, fieldLen) : "";
	}

	static String dotPadding (int len, int fieldLen) {
		return (len < fieldLen) ? msDots.substring (len, fieldLen) : "";
	}


}

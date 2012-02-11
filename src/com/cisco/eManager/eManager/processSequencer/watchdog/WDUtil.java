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

import java.io.*;
import com.cisco.eManager.eManager.processSequencer.common.logging.LogUtil;

/**
 * This class provides a few utility methods used by
 * other watchdog classes. All methods are static.
 */
public final class WDUtil
{
	private static final String IOR_FILE = File.separator + "wd.ior";

 	/**
	 * All methods are static. Cannot be instantiated.
	 */
	private WDUtil(){}

	/**
	 * Gives the stack trace of the throwable as a String.
	 * @param t the throwable to get the stack trace from
	 * @return stack trace as string
	 */
	public static String getStackTraceAsString(Throwable t) {
		return getStackTraceAsString(null, t);
	}

	/**
	 * Gives the stack trace of the throwable as a String.
	 * @param msg the message to be added on top of the stack trace; ignored if it is null.
	 * @param t the throwable to get the stack trace from
	 * @return stack trace as string
	 */
	public static String getStackTraceAsString(String msg, Throwable t) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
			PrintStream ps = new PrintStream(baos);
			if( msg != null) ps.println(msg);
			if(t == null) {
				t = new java.lang.Exception();
				t.fillInStackTrace();
			}
			t.printStackTrace(ps);

			ps.close();
			baos.close();

			return baos.toString();
		} catch (Throwable thr) {
			return "Error generating stack trace";
		}
	}

	/**
	 * Converts an array of Objects into readable string form.
	 * @param ar array of Objects to be stringified.
	 * @return null if ar is null. [] if ar has no elements. [ el1, el2... ] if ar has elements.
	 */
	public static String toString(Object[] ar) {
		if(ar == null) return "null";
		if(ar.length == 0) return "[]";

		StringBuffer sb = new StringBuffer(80);
		sb.append("[ ");
		for(int i=0; i< ar.length; ++i) {
			sb.append(ar[i]);
			if( i != ar.length -1) sb.append(", ");
		}
		sb.append(" ]");
		return sb.toString();
	}

	/**
	 * Replace a string with the other in the given source string.
	 * @param src the source string
	 * @param orig the string to be replaced
	 * @param sub the string to replace orig with
	 * @return the string after replacing orig with sub in src
	 */
	public static String sreplace(String src, String orig, String sub)
	{
		StringBuffer sb = new StringBuffer(src.length());

		int srcLen = src.length();
		int origLen = orig.length();

		int startIndex = 0;
		int repIndex = -1;
		while( (repIndex = src.indexOf(orig , startIndex)) != -1) {
			sb.append( src.substring(startIndex, repIndex));
			sb.append( sub );
			startIndex = repIndex + origLen;
			if( startIndex >= srcLen ) {
				break;
			}
		}

		if(startIndex < srcLen ){
			sb.append(src.substring(startIndex));
		}

		return sb.toString();

	}

	/**
	 * Gives an Object array containing the passed parameter
	 */
	public static Object[] toArray( Object a ) {
		return new Object[] { a };
	}

	/**
	 * Gives an Object array containing the passed parameters
	 */
	public static Object[] toArray( Object a, Object b) {
		return new Object[] { a, b };
	}

	/**
	 * Gives an Object array containing the passed parameters
	 */
	public static Object[] toArray( Object a, Object b, Object c) {
		return new Object[] { a, b, c };
	}

	/**
	 * Gives an Object array containing the passed parameters
	 */
	public static Object[] toArray( Object a, Object b, Object c, Object d) {
		return new Object[] { a, b, c, d };
	}


	static void writeWDToFile(String[] args, Watchdog wdog)
		throws Exception
	{
		String ior = javax.rmi.PortableRemoteObject.toStub(wdog).toString();
		String tmpDir = LogUtil.getLogLocation();

		if( tmpDir == null) {
			throw new IOException("The tmp location for writing the watchdog IOR is not set.\n" +
								"Set Logging.Defaults.logLocation in the configuration.");
		}

		File iorFile = new File (tmpDir + IOR_FILE);
		PrintStream ps = new PrintStream( new FileOutputStream(iorFile));
		ps.println(ior);
		ps.close();

		iorFile.deleteOnExit();
	}

	public static Watchdog readWDFromFile(String[] args)
		throws Exception
	{
		String tmpDir = LogUtil.getLogLocation();

		if( tmpDir == null) {
			throw new IOException("The tmp location for writing the watchdog IOR is not set.\n" +
								"Set Logging.Defaults.logLocation in the configuration.");
		}

		File file = new File(tmpDir + File.separator + "wd.ior");

		if( ! file.exists()) {
			throw new IOException(tmpDir + IOR_FILE + " file is missing.");
		}

		BufferedReader br = new BufferedReader( new FileReader(tmpDir + File.separator + "wd.ior"));
		String ior = br.readLine();

		br.close();

		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, System.getProperties());
		Object obj     = orb.string_to_object(ior);
		Watchdog wdog  = (Watchdog) javax.rmi.PortableRemoteObject.narrow( obj,
						com.cisco.eManager.eManager.processSequencer.watchdog.Watchdog.class);
		return wdog;
	}

	static void removeWDFile() {
		try {
			String tmpDir = LogUtil.getLogLocation();
			if( tmpDir != null) {
				File file = new File(tmpDir + IOR_FILE);
				if( file.exists()) {
					file.delete();
				}
			}
		} catch (Exception ex) {}
	}

}




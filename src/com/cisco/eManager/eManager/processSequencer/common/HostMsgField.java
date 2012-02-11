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
package com.cisco.eManager.eManager.processSequencer.common;

import java.io.*;
import java.util.*;

import com.tibco.tibrv.*;
import com.cisco.eManager.eManager.processSequencer.common.EventUtils;

//for HA
import com.cisco.eManager.eManager.processSequencer.common.PSInetAddress;


public final class HostMsgField {

	public static final String ALL_HOSTS= "*";
	private static final String DELIMITER = ",";

	private static String msLocalHostFQDN;
	private static String msLocalHostName;

	static {
		try {
			msLocalHostFQDN = PSInetAddress.getLocalHost().getHostName();
			int brk = msLocalHostFQDN.indexOf('.');
			if ( brk != -1 ) {
				msLocalHostName = msLocalHostFQDN.substring(0, brk);
			} else {
				msLocalHostName = msLocalHostFQDN;
			}
		} catch (java.net.UnknownHostException uhe) {
			msLocalHostName = "localhost";
			msLocalHostFQDN = "localhost";
		}
	}

	public static String getLocalHostFQDN() { return msLocalHostFQDN; }

	public static String getLocalHostName() { return msLocalHostName; }

	public static boolean isLocalHostTargetted(String[] hosts)
	{
	    if( hosts == null) return false;
		if( hosts.length == 0) return true;

		for(int i=0; i < hosts.length; ++i) {
			if( hosts[i] == null) continue;

			if( hosts[i].equals(msLocalHostName) ||
				hosts[i].equals(msLocalHostFQDN) )
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isLocalHostTargetted(TibrvMsgField hostField)
		throws Exception
	{
	   if( hostField == null) return false;

		String host = (String) hostField.data;
		if( host == null || host.equals("")) return false;

		if( host.equals(ALL_HOSTS) ) return true;

		if( host.indexOf(DELIMITER) == -1) {

			if( host.equals(msLocalHostName) ||
				host.equals(msLocalHostFQDN) )
			{
				return true;
			}
			return false;
		}

		StringTokenizer st = new StringTokenizer(host, DELIMITER);
		while(st.hasMoreTokens()) {
			String next = st.nextToken();
			if( msLocalHostFQDN.equals(next) || msLocalHostName.equals(next))
				return true;
		}

		return false;
	}

	public static final String getLocalHostTarget() {
		return getTarget(null);
	}

	public static String getTarget(String[] hosts)  {

		if( hosts == null ) return msLocalHostFQDN;

		if( hosts.length == 0) return ALL_HOSTS;

		if( hosts.length == 1) return hosts[0];

		StringBuffer sb = new StringBuffer(80);
		for( int i =0; i < hosts.length - 1 ; ++i) {
			sb.append(hosts[i]).append(DELIMITER);
		}
		sb.append(hosts[hosts.length -1]);

		return sb.toString();
	}

}

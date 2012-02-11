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


import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;
/**
 * A Name Value Pair
 */

public final class NVPair implements java.io.Serializable {

	private String mName;
	private Object mValue;

	public NVPair() { }

	public NVPair(String name, Object value) {
		this.mName = name;
		this.mValue = value;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getName() {
		return mName;
	}

	public void setValue(Object value) {
		this.mValue = value;
	}

	public Object getValue() {
		return mValue;
	}

	public void set(String name, Object value) {
		this.mName = name;
		this.mValue = value;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[ ").append(mName).append(", ").append(mValue).append(" ]");
		return sb.toString();
	}

	public boolean equals(Object o) {

		if( o instanceof NVPair ) {
			NVPair other = (NVPair) o;

			if( this == other) return true;

			if( this.mName == other.mName &&
				this.mValue == other.mValue)
			{
				return true;
			}

			if ( this.mName != null && this.mName.equals(other.mName) ) {
				if( this.mValue != null && this.mValue.equals(other.mValue)) {
					return true;
				}
			}
		}

		return false;
	}

	public static Map toMap(NVPair[] nvp) {
		return addToMap(nvp, new HashMap());
	}

	public static Map toSortedMap(NVPair[] nvp) {
		return addToMap(nvp, new TreeMap());
	}

	public static Map addToMap(NVPair[] ar, Map m) {
		if ( ar == null || ar.length == 0) return m;
		if( m == null) {
			m = new HashMap();
		}

		for(int i=0; i< ar.length; ++i) {
			m.put(ar[i].mName, ar[i].mValue);
		}

		return m;
	}

	public static NVPair[] fromMap(Map m) {
		if( m == null) return null;

		int sz = m.size();

		if( sz == 0) return new NVPair[0];

		NVPair[] ret = new NVPair[sz];
		Iterator iter = m.entrySet().iterator();

		for(int i=0; iter.hasNext(); ++i) {
			Entry entry = (Entry) iter.next();
			ret[i] = new NVPair((String)entry.getKey(), entry.getValue());
		}

		return ret;
	}
}



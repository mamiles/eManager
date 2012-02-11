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

import java.util.*;

public class LockMap {

	private Map mLocks;

	public LockMap() {
		mLocks = Collections.synchronizedMap(new HashMap());
	}

	public synchronized Object getLock(String key) {

		if( key == null) return null;

		Lock lock = (Lock) mLocks.get(key);

		if( lock == null) {
			lock = new Lock();
			mLocks.put(key, lock);
		}
		lock.incr();
		return lock;
	}

	public synchronized void releaseLock(String key) {
		if( key == null) return;

		Lock lock = (Lock) mLocks.get(key);
		if( lock != null) {
			if( lock.decr() == 0) {
				mLocks.remove(key);
			}
		}
	}

	private static class Lock {
		private Counter cou = new Counter();

		int incr() {
			return cou.incr();
		}

		int decr() {
			return cou.decr();
		}
	}

	private static class Counter {
		private int val = 0;
		synchronized int incr() { return ++val;}
		synchronized int decr() { return --val;}
	}
}




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

import java.util.*;

class ServerGraph {

	private static ServerGraph msInstance;

	public static synchronized ServerGraph getInstance() {
		if( msInstance == null ) {
			msInstance = new ServerGraph();
		}
		return msInstance;
	}

	private Map mAllServers;
	private List mOrderedList;
	private String[] mOrderedArray;

	private ServerGraph() {
		mAllServers = new HashMap();
		mOrderedList = new LinkedList();
	}

	synchronized Object addServer(String server) {
		ServerNode sn = (ServerNode) getServer(server);
		if( sn == null ) {
			sn = new ServerNode(server);
			mAllServers.put(server, sn);
		}
		return sn;
	}

	synchronized void addPrincipal(String server, String prin) {
		ServerNode sn = (ServerNode) addServer(server);
		sn.addPrincipal((ServerNode) addServer(prin));
	}

	synchronized void addDependent(String server, String dep) {
		ServerNode sn = (ServerNode) addServer(server);
		sn.addDependent((ServerNode) addServer(dep));
	}

	synchronized void sort() {

		if( mOrderedList.size() > 0) return;

		Iterator iter = mAllServers.values().iterator();

		while ( iter.hasNext()) {
			ServerNode sn = (ServerNode) iter.next();
			sn.getDepth();
		}

		mOrderedList.addAll(mAllServers.values());
		Collections.sort(mOrderedList);

		//System.out.println(mOrderedList);
	}

	synchronized OrderedServers getOrderedListOfPrincipals(String[] unsortedList) {

		OrderedServers os = getOrderedList(unsortedList);
		String[] list = os.getList();

		if( list == null || list.length < 2) return os;

		List retList = new ArrayList(list.length);
		BitSet discard = new BitSet();

		for(int i=0; i< list.length; ++i) {

			if( discard.get(i)) {
				continue;
			}

			for(int j=i+1; j < list.length; ++j) {
				ServerNode sn = (ServerNode) getServer(list[j]);

				if( sn.isChildOf(list[i])) {
					discard.set(j);
				}
			}

			retList.add(list[i]);
		}


		return new OrderedServers((String[]) retList.toArray(new String[0]), os.getMissing());
	}

	synchronized String[] getOrderedList() {
		OrderedServers os = getOrderedList(null);
		return os.getList();
	}

	synchronized OrderedServers getOrderedList(String[] unsortedList) {

		if( mOrderedArray == null) {
			mOrderedArray = new String[mOrderedList.size()];
			Iterator iter = mOrderedList.iterator();

			int i=0;
			while ( iter.hasNext() ) {
				ServerNode sn = (ServerNode) iter.next();
				mOrderedArray[i++] = sn.getName();
			}
		}

		if( unsortedList == null || unsortedList.length == 0) {

			String[] retArray = new String[mOrderedArray.length];
			System.arraycopy(mOrderedArray, 0, retArray, 0, mOrderedArray.length);
			return new OrderedServers(retArray, null);

		} else if ( unsortedList.length == 1) return new OrderedServers(unsortedList, null);

		if( mOrderedArray.length == 0) return new OrderedServers(unsortedList, null);

		Set s = new HashSet();
		for(int i=0; i < unsortedList.length; ++i) {
			s.add(unsortedList[i]);
		}

		List sortedList = new ArrayList(unsortedList.length);

		for(int i=0; i < mOrderedArray.length && s.size() > 0 ; ++i) {
			if (s.remove(mOrderedArray[i])) {
				sortedList.add(mOrderedArray[i]);
			}
		}

		String missing[] = null;
		if( s.size() > 0) {
			missing = (String[]) s.toArray(new String[0]);
		}
		return new OrderedServers( (String[])sortedList.toArray(new String[0]), missing);
	}

	synchronized Object getServer(String server) {
		return mAllServers.get(server);
	}

	private static class ServerNode implements Comparable {

		private String mName;

		private List mParentNodes;
		private List mSubNodes;

		private int mDepth = -1;

		ServerNode(String name) {
			this.mName = name;
			this.mParentNodes = new ArrayList(5);
			this.mSubNodes = new ArrayList(5);
		}

		public void addPrincipal(ServerNode prin) {
			mParentNodes.add(prin);
		}

		public void addDependent(ServerNode dep) {
			mSubNodes.add(dep);
		}

		public String getName() {
			return mName;
		}

		public boolean equals(Object oth) {
			return mName.equals(((ServerNode)oth).mName);
		}

		public int hashCode() {
			return mName.hashCode();
		}

		public int compareTo(Object other) {
			ServerNode oth = (ServerNode) other;

			if( mDepth == oth.mDepth ) return 0;
			if( mDepth > oth.mDepth ) return 1;
			else return -1;
		}

		boolean isChildOf(String sName) {

			if( mParentNodes.size() == 0) return false;

			for(int i=0; i < mParentNodes.size(); ++i) {
				ServerNode parent = (ServerNode) mParentNodes.get(i);
				if( parent.getName().equals(sName)) return true;
				if( parent.isChildOf(sName) ) return true;
			}
			return false;
		}

		int getDepth() {
			if( mDepth != -1) return mDepth;

			int sz = mParentNodes.size();

			if( sz == 0 ) {
				mDepth = 1;
			} else {
				for(int i=0; i < sz; ++i) {
					ServerNode sn = (ServerNode) mParentNodes.get(i);
					if( sn.getDepth() > mDepth ) {
						mDepth = sn.getDepth();
					}
				}
				mDepth++;
			}
			return mDepth;
		}

		public String toString() {
			return mName + " - " + mDepth;
		}
	}

	public static void main(String args[]) {

		ServerGraph sg = ServerGraph.getInstance();

		sg.addServer("S4");
		sg.addServer("S7");
		sg.addServer("S3");
		sg.addServer("S5");
		sg.addServer("S6");
		sg.addServer("S2");
		sg.addServer("S1");

		sg.addPrincipal("S7", "S3");
		sg.addPrincipal("S7", "S4");

		sg.addPrincipal("S3", "S4");
		sg.addPrincipal("S3", "S1");

		sg.addPrincipal("S5", "S6");
		sg.addPrincipal("S5", "S2");

		sg.addPrincipal("S6", "S2");
		sg.addPrincipal("S6", "S3");

		sg.addPrincipal("S2", "S1");

		sg.sort();

		System.out.println("Model list " + sg.mOrderedList);

		System.out.println("Total list " + WDUtil.toString(sg.getOrderedList()));
		System.out.println("S3, S7, S2 " + sg.getOrderedList( new String[]{ "S3", "S7", "S2"}));
		System.out.println("S6, S5     " + sg.getOrderedList( new String[]{ "S6", "S5"} ));
		System.out.println("S6, S5, S8, S9  " + sg.getOrderedList( new String[]{ "S6", "S5", "S8", "S9"} ));

		System.out.println("Main List -> S1, S4 " + sg.getOrderedListOfPrincipals( null ));
		System.out.println("S3, S7, S2 -> S2, S3 " + sg.getOrderedListOfPrincipals( new String[]{ "S3", "S7", "S2"}));
		System.out.println("S6, S5     -> S6 " + sg.getOrderedListOfPrincipals( new String[]{ "S6", "S5"} ));
		System.out.println("S6, S5, S8, S9 -> S6 " + sg.getOrderedListOfPrincipals( new String[]{ "S6", "S5", "S8", "S9"} ));
		System.out.println("S1, S3, S7, S2 -> S1 " + sg.getOrderedListOfPrincipals( new String[]{ "S3", "S7", "S1", "S2"}));
	}
}

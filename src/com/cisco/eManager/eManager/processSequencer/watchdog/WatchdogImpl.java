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

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;

import java.util.*;
import java.util.Map.Entry;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.cisco.eManager.eManager.processSequencer.common.*;
import com.cisco.eManager.eManager.processSequencer.common.logging.*;

/**
 * Implementation of the Watchdog interface. The master
 * watchdog extends this implementation and overrides
 * parts of it.
 * @see WatchdogManager
 */
public class WatchdogImpl // extends PortableRemoteObject
		implements  Watchdog, ServerManager, WDConstants
{
	protected WatchdogManager mWdMgr;

	protected String mHostName;
	protected String mIpAddress;
	protected Object mId;

	protected ThreadPool mEventThreadPool;
	protected Map mServers;
	protected CiscoLogger mLogger;

	protected EmailThread mEmailThread;

	protected Map mServerGroups;
	protected String mName;
	private Object mServerStartStopLock = new Object();

	protected static WatchdogImpl wdImplInstance = null;

	protected ServerGraph mServerGraph;

	public static WatchdogImpl getWatchdogImpl() {
		return wdImplInstance;
	}

	public WatchdogImpl(WatchdogManager wdMgr)
		throws RemoteException
	{
		this.mWdMgr = wdMgr;
		mServers = Collections.synchronizedMap(new HashMap());
		mServerGroups =  Collections.synchronizedMap(new HashMap());
		init();
	}

	protected WatchdogImpl()
		throws RemoteException
	{
		this.mWdMgr = (WatchdogManager) this;
		mServers = Collections.synchronizedMap(new HashMap());
		mServerGroups =  Collections.synchronizedMap(new HashMap());
		init();
	}

	public void setName(String name) {
		this.mName = name;
	}

	private void reregister()
		throws Exception
	{
		mLogger.info("Attempting to register with the master watchdog");
		mId = mWdMgr.register(this, mHostName, mIpAddress,
					ConfigAdapter.getInstance().getLocalBootConfiguration(), getVersionInfo());

		mLogger.info("Re-registered with the master watchdog");
	}

	protected void init()
	{
		wdImplInstance = this;
		mLogger = CiscoLogger.getCiscoLogger("watchdog");

		try {

			mHostName = PSInetAddress.getLocalHost().getHostName();
			mIpAddress = PSInetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException uhe) {
			throw new WDRuntimeException(WDExConstants.UNKNOWN_HOST , uhe);
		}

		try {
			mLogger.info("Attempting to register with the master watchdog");
			mId = mWdMgr.register(this, mHostName, mIpAddress,
							ConfigAdapter.getInstance().getLocalBootConfiguration(), getVersionInfo());
		} catch (RemoteException re) {
			throw new WDRuntimeException(WDExConstants.CANNOT_REGISTER, re);
		} catch (ConfigStoreException cse) {
			throw new WDRuntimeException(WDExConstants.CANNOT_REGISTER, cse);
		}

		createEventThreadPool();
		try {
			createServers();
		} catch (InvalidConfigException ice) {
			mLogger.severe ("Could not create servers", ice);
			throw new WDRuntimeException(WDExConstants.INSUFFICIENT_CONFIG, ice);
		}

		createGroups();


		try {
			mEmailThread = EmailThread.getInstance();
			mEmailThread.start();
		} catch (InvalidConfigException ice) {
			mLogger.warning("Disabling e-mail on server state changes", ice);
			mEmailThread = null;
		}

		startMonitoring();
	}

	protected void startMonitoring() {

		if( this != mWdMgr ) {
			Thread t = new MWDMonitor();
			t.start();
		}
	}

	private void createEventThreadPool () {
		mEventThreadPool = new ThreadPool ("Watchdog");
		mEventThreadPool.setMaxThreads (10);
		mEventThreadPool.setMaxWaitingThreads (5);
	}

	public void addEventToPool (Runnable r) {
		mEventThreadPool.addRequest (r);
	}

	public String getHostName() {
		return mHostName;
	}

	public String getIpAddress() {
		return mIpAddress;
	}

	private String[] getVersionInfo() {
		List versionLines = new ArrayList();
		try {
			String emHome = System.getProperty("em.home");
			if( emHome != null) {
				java.io.File versionFile = new java.io.File(emHome + "/config/" + VERSION_INFO_FILE);
				if( versionFile.exists()) {
					BufferedReader br = new BufferedReader(new FileReader(versionFile));
					String line = null;
					while ( (line = br.readLine()) != null ) {
						versionLines.add(line);
					}
				}
			}
		} catch (Exception ex) { }

		if( versionLines.size() == 0) {
			System.out.println("Did not find version info. Will use default information.");
			versionLines.add("eManager-1.0");
		}

		//System.out.println("Version lines : " + versionLines + " " + versionLines.size());
		return (String[]) versionLines.toArray(new String[0]);
	}


	public MultiOpStatus restart(TargetComponentData targetComponentData)
		throws RemoteException
	{
		MultiOpStatus ostat = new MultiOpStatus("restart", mHostName);

		try {
			mLogger.finer("restart " + targetComponentData);

			if(targetComponentData.isAllServers()) {
				try {
					restartServers(targetComponentData.getWaitFlag());
					ostat.addSuccess("server", null);
				} catch (Exception ex) {
					ostat.addFailure("server", null, ex);
				}
			} else {
				String[] servers = targetComponentData.getServers();
				if(servers != null) {
					OrderedServers os = mServerGraph.getOrderedListOfPrincipals(servers);
					servers = os.getList();
					for(int i=0; i < servers.length; ++i) {
						try {
							restartServer(servers[i], targetComponentData.getWaitFlag());
							ostat.addSuccess("server", servers[i]);
						} catch (Exception ex) {
							ostat.addFailure("server", servers[i], ex);
						}
					}

					servers = os.getMissing();
					for(int i=0; i < servers.length; ++i) {
						ostat.addFailure("server", servers[i], new UnknownServerException(servers[i]));
					}
				}

				String[] serverGroups = targetComponentData.getServerGroups();
				if(serverGroups != null) {
					for(int i=0; i < serverGroups.length; ++i) {
						try {
							restartServerGroup(serverGroups[i], targetComponentData.getWaitFlag());
							ostat.addSuccess("group", serverGroups[i]);
						} catch (Exception ex) {
							ostat.addFailure("group", serverGroups[i], ex);
						}
					}
				}
			}
			return ostat;
		} catch (Exception ex) {
			throw new WDRemoteException(WDExConstants.FAILED_OPERATION, ex);
		}
	}

	public MultiOpStatus stop(TargetComponentData targetComponentData)
		throws RemoteException
	{
		MultiOpStatus ostat = new MultiOpStatus("stop", mHostName);

		mLogger.finer("stop " + targetComponentData);

		try {
			if(targetComponentData.isAllServers()) {
				try {
					stopServers(targetComponentData.getWaitFlag());
					ostat.addSuccess("server", null);
				} catch (Exception ex) {
					ostat.addFailure("server", null, ex);
				}
			} else {
				String[] servers = targetComponentData.getServers();
				if(servers != null) {
					OrderedServers os = mServerGraph.getOrderedList(servers);
					servers = os.getList();
					for(int i= servers.length -1; i>=0; --i) {
						try {
							stopServer(servers[i], targetComponentData.getWaitFlag());
							ostat.addSuccess("server", servers[i]);
						} catch (Exception ex) {
							ostat.addFailure("server", servers[i], ex);
						}
					}
					servers = os.getMissing();
					for(int i=0; i < servers.length; ++i) {
						ostat.addFailure("server", servers[i], new UnknownServerException(servers[i]));
					}
				}

				String[] serverGroups = targetComponentData.getServerGroups();
				if(serverGroups != null) {
					for(int i=0; i < serverGroups.length; ++i) {
						try {
							stopServerGroup(serverGroups[i], targetComponentData.getWaitFlag());
							ostat.addSuccess("group", serverGroups[i]);
						} catch (Exception ex) {
							ostat.addFailure("group", serverGroups[i], ex);
						}
					}
				}
			}
			return ostat;
		} catch (Exception ex) {
			throw new WDRemoteException(WDExConstants.FAILED_OPERATION, ex);
		}
	}

	public MultiOpStatus start(TargetComponentData targetComponentData)
		throws RemoteException
	{
		MultiOpStatus ostat = new MultiOpStatus("start", mHostName);

		mLogger.finer("start " + targetComponentData);

		try {
			if(targetComponentData.isAllServers()) {
				try {
					startServers(targetComponentData.getWaitFlag());
					ostat.addSuccess("server", null);
				} catch (Exception ex) {
					ostat.addFailure("server", null, ex);
				}
			} else {
				String[] servers = targetComponentData.getServers();
				if(servers != null) {
					OrderedServers os = mServerGraph.getOrderedList(servers);
					for(int i=0; i < servers.length; ++i) {
						try {
							startServer(servers[i], targetComponentData.getWaitFlag());
							ostat.addSuccess("server", servers[i]);
						} catch (Exception ex) {
							ostat.addFailure("server", servers[i], ex);
						}
					}
					servers = os.getMissing();
					for(int i=0; i < servers.length; ++i) {
						ostat.addFailure("server", servers[i], new UnknownServerException(servers[i]));
					}
				}

				String[] serverGroups = targetComponentData.getServerGroups();
				if(serverGroups != null) {
					for(int i=0; i < serverGroups.length; ++i) {
						try {
							startServerGroup(serverGroups[i], targetComponentData.getWaitFlag());
							ostat.addSuccess("group", serverGroups[i]);
						} catch (Exception ex) {
							ostat.addFailure("group", serverGroups[i], ex);
						}
					}
				}
			}
			return ostat;
		} catch (Exception ex) {
			throw new WDRemoteException(WDExConstants.FAILED_OPERATION, ex);
		}
	}

	public void configurationChanged(ConfigChangeData configChangeData)
		throws RemoteException
	{
		String host[] = new String[] { getHostName() };
		if( !configChangeData.isVersionChange()) {
			Map changes = configChangeData.getChanges();

			try {
				Iterator iter = changes.entrySet().iterator();
				while ( iter.hasNext() ) {
					Entry entry = (Entry) iter.next();
					DCPEventChannel.sendDCPChange(host,
							(String) entry.getKey(), (String) entry.getValue());
				}
			} catch (Exception ex) {
				mLogger.warning("In notifiying config change", ex);
			}
		} else {
			try {
				DCPEventChannel.sendDCPChange( host );
			} catch (Exception ex) {
				mLogger.warning("In notifiying config change", ex);
			}
		}
	}

	public WDServerStatus getStatus()
		throws RemoteException
	{
		WDServerStatus status = new WDServerStatus(mHostName);
		Vector servStat = new Vector(mServers.size());
		Iterator iter = mServers.values().iterator();
		while(iter.hasNext()) {
			servStat.add(((Server)iter.next()).getStatus());
		}

		status.setServerStatus( (ServerStatus[]) servStat.toArray(new ServerStatus[0]));
		return status;
	}

	private String readServerNames()
		throws InvalidConfigException
	{
		String servers = null;
		String roleProp = null;
		String propName = null;

		String role = DCPLib.getProperty("SYSTEM.role", null);
		if( role != null) {
			roleProp = WDConstants.WD_PREFIX + ".byRole." + role + ".servers";
			mLogger.config("Retrieving servers using : " + roleProp );
			servers = DCPLib.getProperty(roleProp, null);
		}

		if( servers == null) {
			propName = WDConstants.WD_PREFIX + ".servers";
			mLogger.config("Retrieving servers using : " + propName + " because servers by role did not exist");
			servers = DCPLib.getProperty(propName, null);
		}

		if(servers == null || servers.trim().length() == 0) {
			StringBuffer msg = new StringBuffer(80);
			if( roleProp != null ) msg.append(roleProp).append(" and/or ").append(propName);
			else msg.append(propName);

			throw new InvalidConfigException(WDExConstants.UNSPEC_PROPERTY, WDUtil.toArray(msg.toString()));
		}
		mLogger.config("Servers are : " + servers);
		return servers;
	}


	/**
	 * Create the Server objects, but don't start them running yet.
	 */
	private void createServers()
		throws InvalidConfigException
	{
		String servers = readServerNames();

		StringTokenizer tokenizer = new StringTokenizer(servers, ", \t\n");
		while(tokenizer.hasMoreTokens()) {

			String serverName = tokenizer.nextToken();
			mLogger.config("Attempting to create server " + serverName);

			// find out what class is used to represent the server
			String serverClassName = getServerProperty(serverName, "class");

			if(serverClassName == null) {
				mLogger.severe("Must specify a class name for server '"	 + serverName + "'");
			} else {
				mLogger.finer(serverName + " : Server class is " + serverClassName);
				try {
					// Locate the constructor
					Class serverClass = Class.forName(serverClassName);
					Class[] paramTypes = new Class[1];
					paramTypes[0] = Class.forName("java.lang.String");
					Constructor serverConstructor = serverClass.getConstructor(paramTypes);

					// Create an instance of the class
					Object[] params = new Object[1];
					params[0] = serverName;
					try {
						Server server =(Server) serverConstructor.newInstance(params);
						server.setServerManager(this);
						mServers.put(serverName, server);
						mLogger.finer("Created " + serverName + " server");
					}
					catch(InvocationTargetException ite) {
						mLogger.severe("Cannot create server : " + serverName, ite.getTargetException());
						throw ite;
					}
				}
				catch(Exception e) {
					mLogger.severe("Could not start " + serverName + " server", e);
				}
			}
		}

		mServerGraph = ServerGraph.getInstance();

		// Now build the dependency graph and make sure there are no cycles
		Iterator iter = mServers.values().iterator();
		while(iter.hasNext()) {
			Server server = (Server) iter.next();
			if(server.addDependencies()) {
				mLogger.severe("Exiting because of cyclic dependency graph");
				System.exit(1);
			}
			mServerGraph.addServer(server.getName());
		}

		iter = mServers.values().iterator();
		while(iter.hasNext()) {
			Server server = (Server) iter.next();

			Collection principals = server.getPrincipals();
			if( principals != null && principals.size() > 0) {
				Iterator pIter = principals.iterator();
				while ( pIter.hasNext()) {
					Server prin = (Server) pIter.next();
					mServerGraph.addPrincipal(server.getName(), prin.getName());
				}
			}

			Collection dependents = server.getDependents();
			if( dependents != null && dependents.size() > 0) {
				Iterator dIter = dependents.iterator();
				while ( dIter.hasNext()) {
					Server dep = (Server) dIter.next();
					mServerGraph.addDependent(server.getName(), dep.getName());
				}
			}
		}

		mServerGraph.sort();

		String[] ordered = mServerGraph.getOrderedList();

		for(int i=0; i < ordered.length; ++i) {
			Server server = (Server) mServers.get(ordered[i]);
			if(server != null) {
				//System.out.println("Starting... " + ordered[i]);
				server.start();
			}
		}
	}

	/**
	 * Parse the groups properties and create ServerGroups consisting
	 * of the requested servers.	Groups are added to mServerGroups.
	 */
	private void createGroups() {
		mLogger.finer("Creating server groups");
		String propertyName = WDConstants.WD_PREFIX + ".groups";
		mLogger.finer("looking up property " + propertyName);

		String groups = DCPLib.getProperty(propertyName, null);

		if(groups == null) {
			return;
		}
		StringTokenizer tokenizer = new StringTokenizer(groups, ", \t\n");
		mLogger.finer("Groups are : " + groups);

		while(tokenizer.hasMoreTokens()) {
			String groupName =(String) tokenizer.nextToken();
			mLogger.finer("Group : " + groupName);
			StringBuffer groupMsg = new StringBuffer(80);
			groupMsg.append("Server group ").append(groupName).append(" contains:");

			ServerGroup serverGroup = new ServerGroup(groupName);
			mServerGroups.put(groupName, serverGroup);

			String servers =(String) DCPLib.getProperty(WDConstants.WD_PREFIX + ".group." + groupName, null);
			if(servers == null) continue;
			StringTokenizer srvTokenizer = new StringTokenizer(servers, ", \t\n");
			while(srvTokenizer.hasMoreElements()) {
				String serverName = srvTokenizer.nextToken();
				Server server = getServer(serverName);
				if(server == null) {
					mLogger.warning("Server group '"
						 + groupName
						 + "' contains unknown server '"
						 + serverName + "'");
				} else {
					mLogger.finer("Adding server " + serverName + " to group " + groupName);
					serverGroup.addServer(server);
					groupMsg.append(" ").append(serverName);
				}
			}
			mLogger.finer(groupMsg.toString());
		}
	}


	/**
	 * Get a property for a named server.
	 *
	 * @param serverName The name of the server to get a property for.
	 * @param serverProperty The property to get
	 * @return The specified property, or null if it doesn't exist.
	 */
	private String getServerProperty(String serverName,
									String serverProperty)
	{
		String propertyName =
			WDConstants.WD_PREFIX
			+ ".server."
			+ serverName
			+ "."
			+ serverProperty;

		String str = DCPLib.getProperty(propertyName, null);
		mLogger.finest(propertyName + " --- " + str);
		return str;
	}

	/**
	 * Get a server.
	 *
	 * @param name The server's name.
	 * @return The requested server or null if it doesn't exist.
	 */
	public Server getServer(String name) {
		return(Server) mServers.get(name);
	}

	/**
	 * Get the number of servers.
	 *
	 * @return The number of servers.
	 */
	public int getNumServers() {
		return mServers.size();
	}

	/**
	 * Get the servers.
	 *
	 * @return an Enumeration of Server objects.
	 */
	public Iterator getServers() {
		return mServers.values().iterator();
	}

	/**
	 * Get the list of servers known to the watchdog.
	 *
	 * @return An enumeration of the servers.
	 */
	public Iterator getServerNames() {
		return mServers.keySet().iterator();
	}


	public Iterator getGroupNames() {
		return mServerGroups.keySet().iterator();
	}

	/**
	 * Start a specific server.
	 *
	 * @param serverName The server to start.
	 * @param waitFlag True if the call should wait for the server to
	 *	 start, false if it shouldn't.
	 */
	public void startServer(String serverName, boolean waitFlag)
		throws ServerStateChangeException, PrincipalDisabledException, UnknownServerException
	{
		synchronized(mServerStartStopLock) {
			Server server =(Server) mServers.get(serverName);
			if(server == null) {
				throw new UnknownServerException(serverName);
			} else {
				server.startServer(waitFlag);
			}
		}
	}

	/**
	 * Stop a specific server.	This may have a side effect of stopping
	 * other servers that depend on the specified server.
	 *
	 * @param serverName The server to stop.
	 * @param waitFlag True if the call should wait for the server to
	 *	 stop, false if it shouldn't.
	 */
	public void stopServer(String serverName, boolean waitFlag)
		throws ServerStateChangeException, UnknownServerException
	{
		synchronized(mServerStartStopLock) {
			Server server =(Server) mServers.get(serverName);
			if(server == null) {
				throw new UnknownServerException(serverName);
			} else {
				server.stopServer(waitFlag);
			}
		}
	}

	/**
	 * Restart a specific server.	This may have the side effect of
	 * restarting other servers that depend on the specified server.
	 *
	 * @param serverName The server to restart.
	 * @param waitFlag True if the call should wait for the server to
	 *	 restart, false if it shouldn't.
	 */
	public void restartServer(String serverName, boolean waitFlag)
		throws ServerStateChangeException, UnknownServerException
	{
		synchronized(mServerStartStopLock) {
			Server server =(Server) mServers.get(serverName);
			if(server == null) {
				throw new UnknownServerException(serverName);
			} else {
				server.stopServer(true);
				server.startServer(waitFlag);
			}
		}
	}

	/**
	 * Start server group.	This may have a side effect of starting
	 * other servers that depend on the group's servers and were waiting
	 * for them to start.
	 *
	 * @param groupName The name of the server group to start.
	 * @param waitFlag True if the call should wait for the servers to
	 *	 start, false if it shouldn't.
	 */
	public void startServerGroup(String groupName, boolean waitFlag)
		throws  ServerStateChangeException, PrincipalDisabledException, UnknownGroupException
	{
		synchronized(mServerStartStopLock) {
			ServerGroup serverGroup = (ServerGroup) mServerGroups.get(groupName);
			if(serverGroup == null) {
				throw new UnknownGroupException(groupName);
			} else {
				serverGroup.startServers(waitFlag);
			}
		}
	}

	/**
	 * Stop server group.	This may have a side effect of stopping other
	 * servers that depend on the group's servers.
	 *
	 * @param groupName The name of the server group to stop.
	 * @param waitFlag True if the call should wait for the servers to
	 *	 stop, false if it shouldn't.
	 */
	public void stopServerGroup(String groupName, boolean waitFlag)
		throws  ServerStateChangeException, UnknownGroupException
	{
		synchronized(mServerStartStopLock) {
			ServerGroup serverGroup =(ServerGroup) mServerGroups.get(groupName);
			if(serverGroup == null) {
				throw new UnknownGroupException(groupName);
			} else {
				serverGroup.stopServers(waitFlag);
			}
		}
	}

	/**
	 * Restart a server group.	This may have the side effect of
	 * restarting other servers that depend on the specified server
	 * group.
	 *
	 * @param groupName The group to restart.
	 * @param waitFlag True if the call should wait for the servers to
	 *	 restart, false if it shouldn't.
	 */
	public void restartServerGroup(String groupName, boolean waitFlag)
		throws  ServerStateChangeException, PrincipalDisabledException, UnknownGroupException
	{

		synchronized(mServerStartStopLock) {
			ServerGroup serverGroup =(ServerGroup) mServerGroups.get(groupName);
			if(serverGroup == null) {
				throw new  UnknownGroupException(groupName);
			} else {
				serverGroup.restartServers(waitFlag);
			}
		}
	}

	/**
	 * Start all servers.
	 *
	 * @param waitFlag True if the call should wait for the servers to
	 *	 start, false if it shouldn't.
	 */
	public void startServers(boolean waitFlag)
		throws ServerStateChangeException, PrincipalDisabledException
	{

		synchronized(mServerStartStopLock) {
			mLogger.finer("Starting all servers");
			String[] ordered = mServerGraph.getOrderedList();

			// First, flag them all to start without waiting
			for(int i=0; i < ordered.length; ++i) {
				Server server = (Server) mServers.get(ordered[i]);
				if(server != null) {
					server.startServer(false);
				}
			}

			// Then, if waiting is desired, wait for them to come up
			if(waitFlag) {
				Iterator iter = mServers.values().iterator();
				while(iter.hasNext()) {
					Server server =(Server) iter.next();
					server.startServer(true);
				}
			}
		}
	}

	/**
	 * Stop all servers.
	 *
	 * @param waitFlag True if the call should wait for the servers to
	 *	 stop, false if it shouldn't.
	 */
	public void stopServers(boolean waitFlag)
		throws ServerStateChangeException
	{
		synchronized(mServerStartStopLock) {
			mLogger.finer("Stopping all servers");
			String[] ordered = mServerGraph.getOrderedList();

			// First, flag them all to start without waiting
			for(int i=ordered.length-1; i >= 0 ; --i) {
				Server server = (Server) mServers.get(ordered[i]);
				if(server != null) {
					mLogger.finer("About to call server(" + server.getName() + ".stopServer)");
					server.stopServer(waitFlag);
				}
			}
		}
	}

	/**
	 * Restart all servers.
	 *
	 * @param waitFlag True if the call should wait for the servers to
	 *	 restart, false if it shouldn't.
	 */
	public void restartServers(boolean waitFlag)
		throws ServerStateChangeException, PrincipalDisabledException
	{
		synchronized(mServerStartStopLock) {
			String[] ordered = mServerGraph.getOrderedList();

			// First, flag them all to start without waiting
			for(int i= ordered.length -1; i>= 0 ; --i) {
				Server server = (Server) mServers.get(ordered[i]);
				if(server != null) {
					server.stopServer(false);
				}
			}

			Iterator iter = mServers.values().iterator();
			while(iter.hasNext()) {
				Server server =(Server) iter.next();
				server.stopServer(true);
			}

			for(int i=0; i < ordered.length; ++i) {
				Server server = (Server) mServers.get(ordered[i]);
				if(server != null) {
					server.startServer(waitFlag);
				}
			}
		}
	}

	/**
	 * Shutdown all servers and exit
	 */
	public void shutdownNow()
		throws RemoteException
	{
		shutdown(false, true);
	}

	/**
	 * Shutdown all servers and service threads immediately.
	 * Notify the master to unregister; unbind your name
	 * from the name server
	 */
	void shutdown(boolean unregisterFromMaster, boolean callExit)
	{

		synchronized(this) {
			if( unregisterFromMaster ) {
				if( mWdMgr != null ) {
					mLogger.finer("Will attempt to unregister from master");
					try {
						mWdMgr.unregister(mHostName, mId);
						mLogger.finer("Completed unregistering from master");
					} catch (Exception ex) {
						mLogger.severe("In unregistering from Master", ex);
					}
					mWdMgr = null;
				}
			}

			if( mServers != null) {
				mLogger.finer("Will attempt to shutdown all servers");
				Iterator iter = mServers.values().iterator();
				while(iter.hasNext()) {
					Server server =(Server) iter.next();
					mLogger.finer("Shutting down server : " + server.getName());
					server.shutdown();
					mLogger.finer("Completed shutting down : " + server.getName());
				}
				mServers = null;
			}

			mLogger.finer("All servers are shutdown..");
		}

		if( callExit ) {
			Thread t = new Thread() {
				public void run() {
					try {
						Thread.sleep(300);
					} catch (Exception ex) {}
					mLogger.finer("Will exit now");
					System.exit(0);
				}
			};
			t.start();
		}

		return;
	}

	public String[] getGroups()
		throws RemoteException
	{
		String[] groups = null;
		if( mServerGroups != null) {
			groups = (String[]) mServerGroups.keySet().toArray(new String[0]);
		}

		return groups;
	}

	public String[] getGroup(String groupName)
		throws RemoteException, UnknownGroupException
	{
		String[] servers = null;
		if( mServerGroups != null) {
			ServerGroup group = (ServerGroup) mServerGroups.get(groupName);
			if(group != null) {
				servers = group.getServerNames();
			} else {
				throw new UnknownGroupException(groupName);
			}
		} else {
			throw new UnknownGroupException(groupName);
		}
		return servers;
	}

	public ServerStatus[] getServerStatus()
		throws RemoteException
	{
		ServerStatus[] stat = new ServerStatus [mServers.size()];
		TreeSet servers = new TreeSet(new Server.ServerComparator());

		Iterator iter = mServers.values().iterator();
		while(iter.hasNext()) {
			servers.add(iter.next());
		}
		int index = 0;
		iter = servers.iterator();
		while(iter.hasNext()) {
			Server server =(Server) iter.next();
			stat[index++] = server.getStatus();
		}
		return stat;
	}

	public ServerStatus getUpdatedServerStatus(ServerStatus lastStatus)
		throws UnknownServerException
	{

		Server server =(Server) mServers.get(lastStatus.name);
		if(server == null) {
			throw new UnknownServerException(lastStatus.name);
		}
		return server.getStatus(lastStatus);
	}


	public CiscoLogger getLogger() {
		return mLogger;
	}


	/**
	 * Get the names of all server groups known to the watchdog.	Each
	 * group contains a set of servers.	A server can belong to zero or
	 * more groups.
	 *
	 * @return The names of the server groups.
	 */
	public String[] getServerGroups() {
		String[] rc = (String[]) mServerGroups.keySet().toArray(new String[0]);
		java.util.Arrays.sort(rc);
		return rc;
	}


	/**
	 * Get a list of servers who are members of a named group.
	 * @param groupName The group to get
	 * @return The names of the servers who are members of the group
	 *	 or null if the group does not exist.
	 */
	public String[] getServersInGroup(String groupName) {
		String[] rc = null;
		ServerGroup serverGroup =(ServerGroup) mServerGroups.get(groupName);
		if(serverGroup != null) {
			rc = serverGroup.getServerNames();
		}
		if( rc != null) java.util.Arrays.sort(rc);
		return rc;
	}

	/**
	 * Return the status of this watchdog
	 */
	public Object heartbeat()
		throws RemoteException
	{
		if ( DCPLib.getBooleanProperty("watchdog.working", true) ) {
			return "ok";
		} else {
			throw new PSRemoteException("heartbeat forced failure");
		}
	}

	/**
	 * Return the value of a config property
	 */
	public String getConfigProperty(String str) {
		if(str.startsWith("Logging")) {
			String val = LogUtil.getProperty(str);
			if (val != null) return val;
		}
		return DCPLib.getProperty(str, null);
	}

	/**
	 * @return true if none of the critical servers is in a disabled state; false otherwise
	 */
	public boolean isHealthy()
		throws RemoteException
	{
		try  {
			String cservers = DCPLib.getProperty(WDConstants.WD_PREFIX + ".criticalServers", null);
			if( cservers == null || cservers.trim().equals("")) {
				cservers = readServerNames();
			}

			if( cservers == null || cservers.trim().equals("")) {
				return true;
			}

			StringTokenizer tokenizer = new StringTokenizer(cservers, ", \t\n");
			while(tokenizer.hasMoreTokens()) {
				String critServ = tokenizer.nextToken();
				Server srv = (Server) mServers.get(critServ);
				if( srv == null || srv.isNonFunctional()) {
					return false;
				}
			}

			return true;
		} catch (Exception ex) {
			throw new RemoteException(ex.getMessage(), ex);
		}
	}

	private class MWDMonitor extends Thread {

		int maxConsecutiveFailures;
		int sleepInterval;
		boolean initState = true;

		public void run() {
			int consecutiveFailures = 0;

			sleepInterval = DCPLib.getIntProperty("watchdog.heartbeat.wds.initDelay", 1000);
			maxConsecutiveFailures = DCPLib.getIntProperty("watchdog.heartbeat.wds.maxAttemptsForMasterReconnect", 0);
			mLogger.fine("maxConsecutiveFailures is set to " + maxConsecutiveFailures);

			while ( true ) {

				try {
					Thread.sleep(sleepInterval);
				} catch (InterruptedException iex) {}

				Object status=null;
				try {
					//mLogger.finest("About to contact master watchdog");
					status = mWdMgr.heartbeat();
					//mLogger.finest("Master watchdog is contacted successfully");
				} catch (Throwable t) {
					int x = DCPLib.getIntProperty("watchdog.heartbeat.wds.maxAttemptsForMasterReconnect", 0);

					if( x != maxConsecutiveFailures) {
						maxConsecutiveFailures = x;
						mLogger.fine("maxConsecutiveFailures is reset to " + maxConsecutiveFailures);
					}

					status = t;
				}

				if( status != null && status instanceof Throwable) {
					consecutiveFailures++;
					mLogger.fine("Master watchdog could not be contacted. Number of consecutive failures : " +
									consecutiveFailures);
					if( maxConsecutiveFailures !=0 && consecutiveFailures >= maxConsecutiveFailures ) {

						String sdMsg = "Shutting down because " + consecutiveFailures + " attempts to reconnect to master have failed";
						System.out.println(sdMsg);

						if( status != null ) {
							mLogger.severe(sdMsg, (Throwable)status);
						} else {
							mLogger.severe(sdMsg);
						}

						try {
							shutdownNow();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}

				} else { //successful heartbeat from Master
					if( consecutiveFailures > 0 ) {
						try {
							reregister();
						} catch (Exception ex) {
							mLogger.info("Failed to re-register");
							ex.printStackTrace();
							continue;
						}
					}
					consecutiveFailures = 0;
				}
				setupSleepInterval(consecutiveFailures);
			}
		}

		private void setupSleepInterval(int consecutiveFailures) {

			if( consecutiveFailures > 0 ) { //failed master. attempt to reconnect
				sleepInterval = DCPLib.getIntProperty("watchdog.heartbeat.wds.masterReconnectAttemptDelay", 2000);
			} else { //normal mode
				sleepInterval = DCPLib.getIntProperty("watchdog.heartbeat.wds.delay", 500);
			}
		}
	}
}


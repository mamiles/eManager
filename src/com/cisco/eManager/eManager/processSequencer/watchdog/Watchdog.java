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

/**
 * The interface provided by a Watchdog to manage
 * the services on an particular machine <p>
 *
 * Watchdogs are managed by a WatchdogManager running
 * on the master machine. <p>
 *
 * @author Rama Taraniganty
 *
 * @see WatchdogManager
 */
public interface Watchdog extends java.rmi.Remote {

	/**
	 * Starts the servers or groups specified in the targetComponentData
	 * @param targetComponentData - contains information about the servers or groups
	 *							  that should be started
	 * @return the MultiOpStatus object containing the result of various individual
	 *						   server/server group starts.
	 * @throws RemoteException
	 */
	public MultiOpStatus start(TargetComponentData targetComponentData)
		throws RemoteException;

	/**
	 * Restarts the servers or groups specified in the targetComponentData
	 * @param targetComponentData - contains information about the servers or groups
	 *							  that should be restarted
	 * @return the MultiOpStatus object containing the result of various individual
	 *						   server/server group restarts.
	 *
	 * @throws RemoteException
	 */
	public MultiOpStatus restart(TargetComponentData targetComponentData)
		throws RemoteException;

	/**
	 * Stops the servers or groups specified in the targetComponentData
	 * @param targetComponentData - contains information about the servers or groups
	 *							  that should be started
	 * @return the MultiOpStatus object containing the result of various individual
	 *						   server/server group starts.
	 * @throws RemoteException
	 */
	public MultiOpStatus stop(TargetComponentData targetComponentData)
		throws RemoteException;

	/**
	 * Notifies the listeners on the host on which the watchdog is running
     * that the configuration has changed. The configChange param has the
     * information abt the nature of the configuration change.
	 */
	public void configurationChanged(ConfigChangeData configChange)
		throws RemoteException;

	/**
	 * Gives the value of the configuration property being used by this watchdog.
	 * @param configItem the property name
	 * @return the value of the configuration property being used by this watchdog.
	 * @throws RemoteException
	 */
	public String getConfigProperty(String configItem)
		throws RemoteException;

	/**
	 * Gives the list of the groups specified in the configuration of this watchdog.
	 * @return the list of the groups specified in the configuration of this watchdog.
	 * @throws RemoteException
	 */
	public String[] getGroups()
		throws RemoteException;

	/**
	 * Gives the list of servers in the given group
	 * @param groupName the name of a group
	 * @return the list of servers in the given group
	 * @throws RemoteException
 	 */
	public String[] getGroup(String groupName)
		throws RemoteException, UnknownGroupException;

	/**
	 * Gives the status of various servers being managed by this watchdog
	 * @return the status of various servers being managed by this watchdog
	 * @throws RemoteException
	 */
	public WDServerStatus getStatus()
		throws RemoteException;

	/**
	 * Gives the consolidated status of the "critical" servers managed by this watchdog
	 * @return true if all the critical servers are functioning normally; false otherwise
	 * @throws RemoteException
	 */
	public boolean isHealthy()
		throws RemoteException;

	/**
	 * This method is called to make the Watchdog shutdown immediately. When this method
	 * is called Watchdog should shutdown and is NOT expected to inform the master watchdog
	 * that it is shutting down. Typically called by the master watchdog when it is
	 * shutting down.
	 * @throws RemoteException
	 */
	public void shutdownNow()
		throws RemoteException;

	/**
	 * This method is used by the master watchdog to monitor the status
	 * of the this watchdog.
	 * @throws RemoteException
	 */
	public Object heartbeat()
		throws RemoteException;

}


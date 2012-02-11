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

import java.util.Properties;

/**
 * The remote interface implemented by the Watchdog
 * implementation that has to manage other watchdogs
 * running on different workers.
 */

public interface WatchdogManager extends Watchdog {
	/**
	 * This method registers a Watchdog object that has to
	 * be managed by the WatchdogManagerImpl
	 *
	 * @param wd - the remote Watchdog object to be registered
	 * @param hostName - name of the watchdog host
	 * @param ipAddress - IP Address of the watchdog host
	 * @param bootConfig - boot configuration of this watchdog
	 * @param imageInfo - lines in VERSION_INFO file
	 *
	 * @return - a key that has to be sent by the registering watchdog
	 *		   along with the request to unregister it.
	 */
	public Object register(Watchdog wd, String hostName, String ipAddress,
				Properties bootConfig, String[] imageInfo)
		throws RemoteException;

	/**
	 * This method unregisters a Watchdog object that is being
	 * managed by the WatchdogManagerImpl
	 *
	 * @param machineData - information about the host on which 'wd' is running
	 * @param key - the key that is given in reply to the register
	 *		  operation.
	 */
	public void unregister(String hostName, Object key)
		throws RemoteException, UnknownWatchdogException;

	/**
	 * Gives the status of servers of different hosts specified by the HostData parameter.
	 * @param hd HostData that specifies the list of hosts from which the status should be retrieved.
	 * @return an array of WDServerStatus objects, one for each host in the HostData parameter
	 * @throws RemoteException
	 * @see Watchdog#start
	 */
	public WDServerStatus[] getStatus(HostData hd)
		throws RemoteException;

	/**
	 * Restarts the servers/server groups on different hosts specified by the HostData parameter.
	 * @param hd HostData that specifies the list of hosts on which the restart operation should be performed.
	 * @param targetComponentData specifies the list of servers or server groups that should be restarted
	 * @return an array of MultiOpStatus objects, one for each host in the HostData parameter
	 * @throws RemoteException
	 * @see Watchdog#restart
	 */
	public MultiOpStatus[] restart(HostData hd, TargetComponentData targetComponentData)
		throws RemoteException;

	/**
	 * Stops the servers/server groups on different hosts specified by the HostData parameter.
	 * @param hd HostData that specifies the list of hosts on which the stop operation should be performed
	 * @param targetComponentData specifies the list of servers or server groups that should be stopped
	 * @return an array of MultiOpStatus objects, one for each host in the HostData parameter
	 * @throws RemoteException
	 * @see Watchdog#stop
	 */
	public MultiOpStatus[] stop(HostData hd, TargetComponentData targetComponentData)
		throws RemoteException;

	/**
	 * Starts the servers/server groups on different hosts specified by the HostData parameter.
	 * @param hd HostData that specifies the list of hosts on which the start operation should be performed
	 * @param targetComponentData specifies the list of servers or server groups that should be started
	 * @return an array of MultiOpStatus objects, one for each host in the HostData parameter
	 * @throws RemoteException
	 */
	public MultiOpStatus[] start(HostData hd, TargetComponentData targetComponentData)
		throws RemoteException;

	/**
	 * Gives the list of hosts on which the watchdogs are running (both types - master and slaves)
	 * @return the list of registered hosts
	 * @throws RemoteException
	 */
	public String[] getRegisteredHosts()
		throws RemoteException;

	/**
	 * Gives the status of watchdogs which have registered with the master watchdog.
	 * If a slave watchdog is unregistered (by stopping it), the HostStatus indicates
	 * that situation.
	 * @return the status of hosts which registered with the master watchdog
	 * @throws RemoteException
	 */
	public HostStatus[] getHostInfo()
		throws RemoteException;

	/**
	 * Gives the groups on the hosts specified by the HostData parameter
	 * <p>
 	 * Each OperationStatus object in the returned array contains
	 * a String[] which has the names of the groups on the host, if the operation is
	 * successful. If the operation failed, it contains the Exception that
	 * caused the failure.
	 * <p>
	 * @param hd specifies the hosts on which the operation has to be performed
	 * @return the result of the getGroups operation, one per host.
	 * @throws RemoteException
	 * @see Watchdog#getGroups
	 */
	public OperationStatus[] getGroups(HostData hd)
		throws RemoteException;

	/**
	 * Gives the health on the hosts specified by the HostData parameter
	 * <p>
 	 * Each OperationStatus object in the returned array contains
	 * a Boolean which represents tha health of the host, if the operation is
	 * successful. If the operation failed, it contains the Exception that
	 * caused the failure.
	 * <p>
	 * @param hd specifies the hosts on which the operation has to be performed
	 * @return the result of the getHealth operation, one per host.
	 * @throws RemoteException
	 * @see Watchdog#isHealthy
	 */
	public OperationStatus[] getHealth(HostData hd)
		throws RemoteException;

	/**
	 * Gives the servers in the group  groupName,  on the hosts specified by the
	 * HostData parameter.
	 * <p>
 	 * Each OperationStatus object in the returned array contains
	 * a String[] with the names of servers in the group, if the operation is
	 * successful. If the operation failed, it contains the Exception that
	 * caused the failure.
	 * <p>
	 * @param hd specifies the hosts on which the operation has to be performed
	 * @param groupName specifies the group whose contents are required
	 * @return the result of the getGroup operation, one per host.
	 * @throws RemoteException
	 * @see Watchdog#getGroup
	 */
	public OperationStatus[] getGroup(HostData hd, String groupName)
		throws RemoteException;

	/**
	 * Gives the value of the property configItem,  on the hosts specified by the
	 * HostData parameter.
	 * <p>
 	 * Each OperationStatus object in the returned array contains
	 * a String which is the value of the property configItem, if the operation is
	 * successful. If the operation failed, it contains the Exception that
	 * caused the failure.
	 * <p>
	 * @param hd specifies the hosts on which the operation has to be performed
	 * @param configItem the name of the property whose value is required
	 * @return the result of the getConfigProperty operation, one per host.
	 * @throws RemoteException
	 * @see Watchdog#getConfigProperty
	 */
	public OperationStatus[] getConfigProperty(HostData hd, String configItem)
		throws RemoteException;


	public void shutdown(HostData hd)
		throws RemoteException;
}









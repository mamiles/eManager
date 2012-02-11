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

package com.cisco.eManager.eManager.process;

import com.cisco.eManager.common.process.*;
import com.cisco.eManager.common.exception.PsAPIException;
import com.cisco.eManager.common.admin.EmanagerAdminException;
import com.cisco.eManager.common.inventory.LogLevel;
import com.cisco.eManager.common.util.AccessType;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * <p>Title: Process Sequencer / Watchdog interface</p>
 * <p>Description: This provides all the methods that can be used by sope to access
 * the Process Sequencer and Watchdog </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems</p>
 * @author Marvin Miles
 * @version 1.0
 */

public interface ProcessManagerInterface extends java.rmi.Remote {

    public final static String LOOKUPNAME = "ProcessManager";

    public AccessType login(String userid, String password) throws EmanagerAdminException,RemoteException;

    public String handleRequest(String xmlStream, String userId, AccessType access) throws RemoteException;

    /**
     * Returns an array of ProcessInfo structures for all process defined within
     * Process Sequencer on the local host.  The state is populated as well as the process
     * id if it is known.
     * @param applicationType
     * @param applicationInstance
     * @return ProcessInfo[] array
     * @throws PsAPIException if falure occures getting status
     */
    public ProcessInfoObj[] getProcessStatus(String applicationType, String applicationInstance) throws PsAPIException,RemoteException;

    /**
     * This method will return the status (ProcessInfo structure) for the specified
     * Process name.
     * @param applicationType
     * @param applicationInstance
     * @param processName  The logical name defined in Process Sequencer.
     * @return ProcessInfo
     * @throws PsAPIException if falure occures getting status or if the
     * processName is not found.
     */
    public ProcessInfoRec getProcessStatusFor(String applicationType, String applicationInstance, String processName) throws PsAPIException,RemoteException;

    /**
     * This method will return the status for a given Group Name.  The values can be one of "up" "down"
     * "Transitioning up" "Transitioning down".
     * @param applicationType
     * @param applicationInstance
     * @param groupName Group Name
     * @return String - Group Status Name
     * @throws PsAPIException if falure occures getting status or if the
     * groupName is not found.
     */
    public String getGroupState(String applicationType, String applicationInstance, String groupName) throws PsAPIException,RemoteException;

    /**
     * This will return an array of SolutionStatusInfo object.  You will know the status (health) of each application that makes
     * up the solution.
     * @param solutionName
     * @return
     * @throws PsAPIException
     */
    public SolutionStatusInfoObj[] getSolutionStatus(String solutionName) throws PsAPIException,RemoteException;

    /**
     * This method will return an array of all groups defined within Process Sequencer.
     * @param applicationType
     * @param applicationInstance
     * @return String[] array of group names
     * @throws PsAPIException if falues occures getting group names
     */
    public String[] getAllGroupNames(String applicationType, String applicationInstance) throws PsAPIException,RemoteException;

    /**
     * This method will return an array of process names for a given group.
     * @param applicationType
     * @param applicationInstance
     * @param groupName Group Name
     * @return String[] of process names
     * @throws PsAPIException
     */
    public String[] getProcessesForGroup(String applicationType, String applicationInstance, String groupName) throws PsAPIException,RemoteException;

    /**
     * This method will return the health of the Process Sequencer server and the process
     * managed by Process Sequencer.  If all process are up and running then true is returned
     * else the value will be false.
     * @param applicationType
     * @param applicationInstance
     * @return boolean
     * @throws PsAPIException
     */
    public boolean getHealth(String applicationType, String applicationInstance) throws PsAPIException,RemoteException;

    /**
     * This method will return the health of the solution.  True then all applications are up and running,
     * false and one or more application is down or is having a problem.
     * @param solutionName
     * @return
     * @throws PsAPIException
     */
    public boolean getSolutionHealth(String solutionName) throws PsAPIException,RemoteException;

    /**
     * This method will start the process specified.  The process may not start if it
     * dependent on another process.  A PsAPIException will be generated if it
     * can't start because it is dependent on something else  but the state of the process
     * will be changed to disabled_dependent.
     * @param applicationType
     * @param applicationInstance
     * @param processName Process Name
     * @return There is nothing returned
     * @throws PsAPIException if the process fails to start or there is an error
     * processing the command or the processName is invalid.
     */
    public void startProcess(String applicationType, String applicationInstance, String processName, String userId, AccessType access) throws PsAPIException,RemoteException;

    /**
     * This method will start processes given a command line argument: [all | service_name | group <groupname>]
     * @param applicationType
     * @param applicationInstance
     * @param command
     * @return Result string showing the status of the command.
     * @throws PsAPIException
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    public String startCommand(String applicationType, String applicationInstance, String command, String userId, AccessType access) throws PsAPIException,RemoteException;

    /**
     * This method will start all process belonging to the group specified.  No
     * WatchdogAPIExeception will be returned if one process can't start.  You will need
     * to call getGroupStatus(groupName) to find out if the command was successfull.
     * @param applicationType
     * @param applicationInstance
     * @param groupName Group Name
     * @return nothing returned
     * @throws PsAPIException if there was an error starting the group or
     * the group name was invalid.
     */
    public void startGroup(String applicationType, String applicationInstance, String groupName, String userId, AccessType access) throws PsAPIException,RemoteException;

    /**
     * This method will start all processes defined in watchdog for a given host.
     * @param applicationType
     * @param applicationInstance
     * @return nothing returned
     * @throws PsAPIException if there was an error changing all the process to
     * starting or if there was an internal error.
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    // public void startAll(String applicationType, String applicationInstance) throws PsAPIException;

    /**
     * This method will stop the specified processName.
     * @param applicationType
     * @param applicationInstance
     * @param processName Process Name
     * @return nothing
     * @throws PsAPIException if there was error stoping the process.
     */
    public void stopProcess(String applicationType, String applicationInstance, String processName, String userId, AccessType access) throws PsAPIException,RemoteException;

    /**
     * This method will stop processes given a command line argument: [all | service_name | group <groupname>]
     * @param applicationType
     * @param applicationInstance
     * @param command
     * @return Result string show the status of the command
     * @throws PsAPIException
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    public String stopCommand(String applicationType, String applicationInstance, String command, String userId, AccessType access) throws PsAPIException,RemoteException;

    /**
     * This method will stop the specified groupName.
     * @param applicationType
     * @param applicationInstance
     * @param groupName Group Name
     * @return nothing
     * @throws PsAPIException if there was an error stoping the group
     */
    public void stopGroup(String applicationType, String applicationInstance, String groupName, String userId, AccessType access) throws PsAPIException,RemoteException;

    /**
     * This method will stop all process defined in Process Sequencer for a given host, however
     * Process Sequencer its self will stay running.
     * @param applicationType
     * @param applicationInstance
     * @return nothing
     * @throws PsAPIException if there is an error stopping all the processes.
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    // public void stopAll(String applicationType, String applicationInstance) throws PsAPIException;

    /**
     * This method will restart the specified processName.  This command is simular to
     * issuing a stop waiting for a short time and issueing a start command for the process.
     * @param applicationType
     * @param applicationInstance
     * @param processName Process name
     * @return nothing
     * @throws PsAPIException if there is an error restarting the process.
     */
    public void restartProcess(String applicationType, String applicationInstance, String processName, String userId, AccessType access) throws PsAPIException,RemoteException;

    /**
     * This methog will restart the specified groupName.  This command is simular to
     * issuing a stop for the group, waiting, and then issuing a start command for the
     * group.
     * @param applicationType
     * @param applicationInstance
     * @param groupName Group Name
     * @return nothing
     * @throws PsAPIException if there is an error restarting the group.
     */
    public void restartGroup(String applicationType, String applicationInstance, String groupName, String userId, AccessType access) throws PsAPIException,RemoteException;

    /**
     * This method will restart processes given a command line argument: [all | service_name | group <groupname>]
     * @param applicationType
     * @param applicationInstance
     * @param command
     * @return Result string showing the status of the restart command
     * @throws PsAPIException
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    public String restartCommand(String applicationType, String applicationInstance, String command, String userId, AccessType access) throws PsAPIException,RemoteException;

    /**
     * This will restart all process defined in Process Sequencer.  This command is simular to
     * issuing a stopAll(), waiting a  short time and issueing startAll().
     * @param applicationType
     * @param applicationInstance
     * @return nothing
     * @throws PsAPIException
     */
    // public void restartAll(String applicationType, String applicationInstance) throws PsAPIException;

    /**
     * This will get the Log Level for the Application Watchdog Process
     * @param applicationType
     * @param applicationInstance
     * @return LogLevel
     * @throws PsAPIException
     */
    public LogLevel getLogLevel(String applicationType, String applicationInstance) throws PsAPIException,RemoteException;

    /**
     * This method sets the Watchdog Log Level
     * Valid values: "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER",
     *      "FINEST", "ALL", "OFF"
     * @param applicationType
     * @param applicationInstance
     * @param logLevel
     * @throws PsAPIException
     */
    public void setLogLevel(String applicationType, String applicationInstance, LogLevel logLevel) throws PsAPIException,RemoteException;

    /**
     * This method registers the application to Watchdog with a properties file.  The application
     * must be registered before the application can be started or managed.
     * @param applicationType
     * @param applicationInstance
     * @throws PsAPIException
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    // public void register(String applicationType, String applicationInstance) throws PsAPIException;

    /**
     * This method will register a Solution to Process Sequencer.  Each application must first
     * be registerd before you can register the solution otherwise an exception is thrown.
     * @param solutionDefinition XML string that looks something like this:
     *
     * <Solution>
     *   <SolutionName>solution-name</SolutionName>
     *   <Components>
     *      <Component>
     *         <AppType>CNOTE</AppType>
     *         <AppInstance>cnote1</AppInstance>
     *         <AppVersion>1.0</AppVersion>
     *         <HostName>hostname.cisco.com</HostName>
     *      </Component>
     *   </Components>
     * </Solution>
     *
     * @throws PsAPIException
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    // public void registerSolution(String solutionDefinition) throws PsAPIException;

    /**
     * This method removes the registration from Watchdog for a application and removes the properties file.
     * @param applicationType
     * @param applicationInstance
     * @throws PsAPIException
     */
    // INTERNAL METHOD - NOT CALLED FROM NBAPI
    // public void unregister(String applicationType, String applicationInstance) throws PsAPIException;

    /**
     * This method will start the specific application using Watchdog.
     * @param applicationType
     * @param applicationInstance
     * @throws PsAPIException
     */
    public void startApplication(String applicationType, String applicationInstance, String userId, AccessType access) throws PsAPIException,RemoteException;

    /**
     * This method will allow you to start a solution (multiple applications under the solution).
     * If one or more of the applications are already running then only the ones not running will be
     * started.
     * @param solutionName
     * @throws PsAPIException
     */
    public void startSolution(String solutionName, String userId, AccessType access) throws PsAPIException,RemoteException;

    /**
     * This method will stop the specific application using Watchdog.
     * @param applicationType
     * @param applicationInstance
     * @throws PsAPIException
     */
    public void stopApplication(String applicationType, String applicationInstance, String userId, AccessType access) throws PsAPIException,RemoteException;

    /**
     * This method will allow you to stop a solution and bring down all applications that make up the
     * solution.
     * @param solutionName
     * @throws PsAPIException
     */
    public void stopSolution(String solutionName, String userId, AccessType access) throws PsAPIException,RemoteException;

}

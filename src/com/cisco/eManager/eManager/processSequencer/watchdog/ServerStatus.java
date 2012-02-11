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

/**
 * A struct like class that encapsulates various static
 * and dynamic aspects of a Server being managed by
 * the watchdog. <p>
 * Callers outside of the watchdog package should use the
 * getter part of the overall interface.
 */
public class ServerStatus
    implements java.lang.Cloneable, java.io.Serializable
{
    static final long serialVersionUID = -5588196607289024774L;

    /**
     * Name of the server
     */
    String name;

    /**
     * Unix process id of the server. It will be 0 for monitors.
     * It will be 0 for servers till the system can determine
     * the process id for this server.
     */
    long pid;

    /**
     * Stringfied state of the server
     */
    String state;

    /**
     * Number of times this server has been (re)started.
     */
    int generation;

    /**
     * Time at which this generation was exec'ed  (in milli seconds)
     */
    long execTime;

    /**
     * Number of successful heartbeats
     */
    int successfulHeartbeats;

    /**
     * Number of missed heartbeats
     */
    int missedHeartbeats;

    /**
     * The object returned by the last heartbeat call
     */
    Object hbResult;

    /**
     * If this is true, watchdog performs the job of transferring
     * this server's stdout and stdin to a log file. If false, the
     * server itself is capable of handling its logging.
     */
    boolean usesNativeLogging;

    /**
     * If true this watchdog server just monitors an external
     * service and has not "exec"ed the process. Useful for
     * services that live longer than watchdog itself.
     */
    boolean isMonitor;

    /**
     * If there was an error starting the process then the description will be
     * kept in errorDescription.  The error gets set if the server is disabled
     * because it keeps dying or the process is restarted because of 3 consecutive
     * missed heartbeats or there has not been a sucessful heartbeat.  The state
     * machine will reset this to null if the server is finaly started.
     */
    String errorDescription = null;

    public ServerStatus()
    {}

    public ServerStatus(String name,
                        long pid,
                        String state,
                        int generation,
                        long execTime,
                        int successfulHeartbeats,
                        int missedHeartbeats,
                        Object hbResult,
                        boolean usesNativeLogging,
                        boolean isMonitor)
    {
        this.name = name;
        this.pid = pid;
        this.state = state;
        this.generation = generation;
        this.execTime = execTime;
        this.successfulHeartbeats = successfulHeartbeats;
        this.missedHeartbeats = missedHeartbeats;
        this.hbResult = hbResult;
        this.usesNativeLogging = usesNativeLogging;
        this.isMonitor = isMonitor;
    }

    public java.lang.Object clone()
    {
        ServerStatus dest;
        try
        {
            dest = (ServerStatus)super.clone();
        }
        catch (java.lang.CloneNotSupportedException ex)
        {
            throw new WDRuntimeException("", ex);
        }
        return dest;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(255);
        sb.append("name=").append(name).append("\n");
        sb.append("pid=").append(pid).append("\n");
        sb.append("state=").append(state).append("\n");
        sb.append("gener=").append(generation).append("\n");
        sb.append("execTime=").append(new java.util.Date(execTime)).append("\n");
        sb.append("succ=").append(successfulHeartbeats).append("\n");
        sb.append("miss=").append(missedHeartbeats).append("\n");
        sb.append("hbresult=").append(hbResult).append("\n");
        sb.append("native logging=").append(usesNativeLogging).append("\n");
        sb.append("monitor =").append(isMonitor).append("\n");
        sb.append("error =").append(errorDescription).append("\n");
        return sb.toString();
    }

    /**
     * Gives the name of the server
     * @return the name of the server
     * @see ServerStatus#name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gives the process id of the server or 0 if the pid is not determined. Also returns 0 if this Server is a monitor
     * @return  the process id of the server
     * @see ServerStatus#pid
     * @see ServerStatus#isMonitor
     */
    public long getPid()
    {
        return pid;
    }

    /**
     * Gives the stringified server state
     * @return Stringified server state
     * @see ServerStatus#state
     */
    public String getState()
    {
        return state;
    }

    /**
     * Gives the number of times this server has been restarted
     * @return number of (re)starts
     * @see ServerStatus#generation
     */
    public int getGeneration()
    {
        return generation;
    }

    /**
     * Gives the time (in millis) at which this server was last started
     * @return time at which this server was started (process was exec'ed)
     * @see ServerStatus#execTime
     */
    public long getExecTime()
    {
        return execTime;
    }

    /**
     * Gives  the number of heartbeats succeeded since this server was started
     * @return  the number of heartbeats succeeded since this server was started
     * @see ServerStatus#successfulHeartbeats
     */
    public int getSuccessfulHeartbeats()
    {
        return successfulHeartbeats;
    }

    /**
     * Gives  the number of heartbeats missed since this server was started
     * @return  the number of heartbeats missed since this server was started
     * @see ServerStatus#missedHeartbeats
     */
    public int getMissedHeartbeats()
    {
        return missedHeartbeats;
    }

    /**
     * Gives  the result of last heartbeat (server specific; not guaranteed to be non-null or meaningful)
     * @return last hearbeat result
     * @see ServerStatus#hbResult
     */
    public Object getHbResult()
    {
        return hbResult;
    }

    /**
     * This method is used to find out if the server manages its logs or if it relies on watchdog to
     * gather its stdout and stderr to a log file
     * @return true if this server uses native logging
     * @see ServerStatus#usesNativeLogging
     */
    public boolean isLogNative()
    {
        return usesNativeLogging;
    }

    /**
     * Sets the nativeLog flag to the given param
     * @see ServerStatus#usesNativeLogging
     */
    public void setLogNative(boolean usesNativeLogging)
    {
        this.usesNativeLogging = usesNativeLogging;
    }

    /**
     * This method is used to find out if this server just monitors an external server
     * or if it creates and manages a server process.
     * @return true if this server  is a monitor and has does not really "manage" a process
     * @see ServerStatus#isMonitor
     */
    public boolean isMonitor()
    {
        return isMonitor;
    }

    /**
     * Sets the isMonitor flag to the given param
     * @see ServerStatus#isMonitor
     */
    public void setMonitor(boolean isMonitor)
    {
        this.isMonitor = isMonitor;
    }

    public void setErrorDescription(String error)
    {
        this.errorDescription = error;
    }

    public String getErrorDescription()
    {
        return this.errorDescription;
    }

}

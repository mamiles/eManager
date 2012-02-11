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

import com.cisco.eManager.eManager.processSequencer.watchdog.WDUtil;

/**
 * The class used to hold the status of each host that
 * registers with the master watchdog. <p>
 * The client programs should (generally) use only
 * the getter parts of this class. The setters and constructor
 * are used by the master watchdog.<p>
 * <em> In this API listing, "this host" refers to the host which
 * this HostStatus object represents.</em>
 *
 * @see WatchdogManager#getHostInfo
 */
public class HostStatus
	implements java.lang.Cloneable, java.io.Serializable
{
	static final long serialVersionUID = -6345441615652236885L;

	private boolean mIsMaster;
	private boolean mActive;
	private String mHostName;
	private String mIpAddress;

	private long   mRegisteredAt;
	private long mUnregisteredAt;

	private long mLastHeartbeatDuration;
	private long mLastHeartbeatAt;
	private Object mLastHeartbeatStatus;

	private String mLogLoc;
	private String mTaskLogLoc;
	private String mTmpDir;

	private String[] mImageList;

	private Boolean mLastHeartbeatSuccessful;

	/**
	 * Creates a HostStatus object with the given hostname, ip address and
	 * the time it was registered with the master watchdog. This is instantiated
	 * only by the master watchdog.
	 */
	HostStatus (String hostName, String ipAddress, boolean isMaster, long registeredAt) {
		this.mHostName = hostName;
		this.mIpAddress = ipAddress;
		this.mRegisteredAt = registeredAt;
		this.mIsMaster = isMaster;
		this.mImageList = new String[0];
	}

	void setDirLocations(String tmpDir, String logLoc, String taskLogLoc) {
		this.mTmpDir = tmpDir;
		this.mLogLoc = logLoc;
		this.mTaskLogLoc = taskLogLoc;
	}

	/**
	 * Returns a clone of this object. Returns null if the cloning operation fails.
	 */
	public  Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException cnse) {
			return null;
		}
	}

	/**
	 * Set image list if the parameter is non-null
	 */
	void setImageList(String[] imgList) {
		if( imgList != null) {
			mImageList = imgList;
		}
	}


	/**
	 * Called by the WatchdogManager impl to update the stats
	 * after the last successful/failed heartbeat.
	 * @param lastHBAt the time at which the last heartbeat was received
	 * @param lastHBDuration the time it took to complete the heartbeat (useful to observe the response time)
	 * @param lastHBStatus the Object returned by the last heartbeat call (could be status in descriptive form)
	 */
	synchronized void updateHeartbeatStats(long lastHBAt,
			long lastHBDuration,
			Object lastHBStatus)
	{
		setLastHeartbeatAt(lastHBAt);
		setLastHeartbeatDuration(lastHBDuration);
		setLastHeartbeatStatus(lastHBStatus);
		if(lastHBStatus instanceof Exception) {
			setLastHeartbeatSuccessful(Boolean.FALSE);
		} else {
			setLastHeartbeatSuccessful(Boolean.TRUE);
		}
	}

	/**
	 * Gives the time taken for "making" the last heartbeat
	 * @return time taken for "making" the last heartbeat
	 */
	public long getLastHeartbeatDuration()
	{
		return mLastHeartbeatDuration;
	}

	/**
	 * Sets the value of LastHeartbeatDuration
	 * @param pLastHeartbeatDuration - the  LastHeartbeatDuration value to set
	 */
	private void setLastHeartbeatDuration(long pLastHeartbeatDuration)
	{
		mLastHeartbeatDuration = pLastHeartbeatDuration;
	}

	/**
	 * Gives the time at which the last heartbeat completed
	 * @return  the time at which the last heartbeat completed
	 */
	public long getLastHeartbeatAt()
	{
		return mLastHeartbeatAt;
	}

	/**
	 * Sets the time at which the last heartbeat was made
	 * @param pLastHeartbeatAt - the  time at which the last heartbeat was made
	 */
	private void setLastHeartbeatAt(long pLastHeartbeatAt)
	{
		mLastHeartbeatAt = pLastHeartbeatAt;
	}

	/**
	 * Gives the status object returned by the last heartbeat call
	 * @return the status object returned by the last heartbeat call.
	 */
	public Object getLastHeartbeatStatus()
	{
		return mLastHeartbeatStatus;
	}

	/**
	 * Sets the last heartbeat status
	 * @param pLastHeartbeatStatus - the  last heartbeat status value to set
	 */
	private void setLastHeartbeatStatus(Object pLastHeartbeatStatus)
	{
		mLastHeartbeatStatus = pLastHeartbeatStatus;
	}

	/**
	 * Gives if the watchdog on this host is online and functioning
	 * @return true if the watchdog is still registered and is responding to heartbeat calls
	 */
	public boolean isActive()
	{
		return mActive;
	}

	/**
	 * Sets if the watchdog on this host is active or not
	 * @param pActive - true if watchdog is online. false otherwise.
	 */
	void setActive(boolean pActive)
	{
		mActive = pActive;
	}

	/**
	 * Gives the name of the host represented by this HostStatus
	 * @return hostname
	 */
	public String getHostName()
	{
		return mHostName;
	}

	/**
	 * Gives the ip address of the host represented by this HostStatus
	 * @return ipAddress
	 */
	public String getIPAddress()
	{
		return mIpAddress;
	}

	/**
	 * Gives the time at which the watchdog in this host came up and registered with the
	 * master watchdog.
	 * @return the time at which the watchdog in this host registered with the master.
	 */
	public long getRegisteredAt()
	{
		return mRegisteredAt;
	}

	/**
	 * Sets the time at which the watchdog on this Host registered with the master.
	 * @param pRegisteredAt - the  time of registration
	 */
	void setRegisteredAt(long pRegisteredAt)
	{
		mRegisteredAt = pRegisteredAt;
	}

	/**
	 * If the watchdog on this host was active at one point of time, but was
	 * subsequently brought down (or crashed), this method gives the
	 * time at which the master received a request to unregister or when the
	 * master marked the slave watchdog as offline (not active)
	 * @return time of de-registration
	 * @see HostStatus#isActive
	 */
	public long getUnregisteredAt()
	{
		return mUnregisteredAt;
	}

	/**
	 * Sets the time at which the watchdog on this Host deregistered with the master.
	 * @param pUnregisteredAt - the  time at which the watchdog on this Host deregistered with the master.
	 */
	public  void setUnregisteredAt(long pUnregisteredAt)
	{
		mUnregisteredAt = pUnregisteredAt;
	}

	/**
	 * The status of the last heartbeat call to the watchdog on this host.
	 * @return Boolean.TRUE if the last heartbeat succeeded. Boolean.FALSE if it failed. null if
	 *			 no heartbeats were attempted yet or if the watchdog is unregistered.
	 */
	public Boolean getLastHeartbeatSuccessful()
	{
		return mLastHeartbeatSuccessful;
	}

	/**
	 * Sets the of the last heartbeat call to the watchdog on this host.
	 * @param pLastHeartbeatSuccessful - the  last heartbeat status;Boolean.TRUE if the last
	 * heartbeat succeeded. Boolean.FALSE if it failed. null if no heartbeats were attempted
	 * yet or if the watchdog is unregistered.
	 */
	void setLastHeartbeatSuccessful(Boolean pLastHeartbeatSuccessful)
	{
		mLastHeartbeatSuccessful = pLastHeartbeatSuccessful;
	}

	/**
     * Returns the location of the log files (server log files) on this server
	 */
	public String getLogLocation() {
		return mLogLoc;
	}

	/**
     * Returns the location of the task log files
	 */
	public String getTaskLogLocation() {
		return mTaskLogLoc;
	}

	/**
     * Returns the tmp dir where certain temp files could be created
	 */
	public String getTmpDir() {
		return mTmpDir;
	}

	/**
	 * Returns the lines in VERSION_INFO file
	 */
	public String[] getImageList() {
		return mImageList;
	}

	/**
  	 * Returns true if the host status corresponds to master.
	 */
	public boolean isMaster() {
		return mIsMaster;
	}

}



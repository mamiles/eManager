/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.process;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Date;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public abstract class ProcessInfo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _processName;

    private java.lang.String _state;

    private int _startGeneration;

    /**
     * keeps track of state for field: _startGeneration
    **/
    private boolean _has_startGeneration;

    private java.util.Date _execTime;

    private long _processId;

    /**
     * keeps track of state for field: _processId
    **/
    private boolean _has_processId;

    private int _successfulHeartbeats;

    /**
     * keeps track of state for field: _successfulHeartbeats
    **/
    private boolean _has_successfulHeartbeats;

    private int _missedHeartbeats;

    /**
     * keeps track of state for field: _missedHeartbeats
    **/
    private boolean _has_missedHeartbeats;

    private java.lang.String _hostname;

    private java.lang.String _heartbeatResult;

    private java.lang.String _usesNativeLogging;

    private boolean _isMonitor;

    /**
     * keeps track of state for field: _isMonitor
    **/
    private boolean _has_isMonitor;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProcessInfo() {
        super();
    } //-- com.cisco.eManager.common.process.ProcessInfo()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'execTime'.
     * 
     * @return the value of field 'execTime'.
    **/
    public java.util.Date getExecTime()
    {
        return this._execTime;
    } //-- java.util.Date getExecTime() 

    /**
     * Returns the value of field 'heartbeatResult'.
     * 
     * @return the value of field 'heartbeatResult'.
    **/
    public java.lang.String getHeartbeatResult()
    {
        return this._heartbeatResult;
    } //-- java.lang.String getHeartbeatResult() 

    /**
     * Returns the value of field 'hostname'.
     * 
     * @return the value of field 'hostname'.
    **/
    public java.lang.String getHostname()
    {
        return this._hostname;
    } //-- java.lang.String getHostname() 

    /**
     * Returns the value of field 'isMonitor'.
     * 
     * @return the value of field 'isMonitor'.
    **/
    public boolean getIsMonitor()
    {
        return this._isMonitor;
    } //-- boolean getIsMonitor() 

    /**
     * Returns the value of field 'missedHeartbeats'.
     * 
     * @return the value of field 'missedHeartbeats'.
    **/
    public int getMissedHeartbeats()
    {
        return this._missedHeartbeats;
    } //-- int getMissedHeartbeats() 

    /**
     * Returns the value of field 'processId'.
     * 
     * @return the value of field 'processId'.
    **/
    public long getProcessId()
    {
        return this._processId;
    } //-- long getProcessId() 

    /**
     * Returns the value of field 'processName'.
     * 
     * @return the value of field 'processName'.
    **/
    public java.lang.String getProcessName()
    {
        return this._processName;
    } //-- java.lang.String getProcessName() 

    /**
     * Returns the value of field 'startGeneration'.
     * 
     * @return the value of field 'startGeneration'.
    **/
    public int getStartGeneration()
    {
        return this._startGeneration;
    } //-- int getStartGeneration() 

    /**
     * Returns the value of field 'state'.
     * 
     * @return the value of field 'state'.
    **/
    public java.lang.String getState()
    {
        return this._state;
    } //-- java.lang.String getState() 

    /**
     * Returns the value of field 'successfulHeartbeats'.
     * 
     * @return the value of field 'successfulHeartbeats'.
    **/
    public int getSuccessfulHeartbeats()
    {
        return this._successfulHeartbeats;
    } //-- int getSuccessfulHeartbeats() 

    /**
     * Returns the value of field 'usesNativeLogging'.
     * 
     * @return the value of field 'usesNativeLogging'.
    **/
    public java.lang.String getUsesNativeLogging()
    {
        return this._usesNativeLogging;
    } //-- java.lang.String getUsesNativeLogging() 

    /**
    **/
    public boolean hasIsMonitor()
    {
        return this._has_isMonitor;
    } //-- boolean hasIsMonitor() 

    /**
    **/
    public boolean hasMissedHeartbeats()
    {
        return this._has_missedHeartbeats;
    } //-- boolean hasMissedHeartbeats() 

    /**
    **/
    public boolean hasProcessId()
    {
        return this._has_processId;
    } //-- boolean hasProcessId() 

    /**
    **/
    public boolean hasStartGeneration()
    {
        return this._has_startGeneration;
    } //-- boolean hasStartGeneration() 

    /**
    **/
    public boolean hasSuccessfulHeartbeats()
    {
        return this._has_successfulHeartbeats;
    } //-- boolean hasSuccessfulHeartbeats() 

    /**
    **/
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * 
     * @param out
    **/
    public abstract void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * 
     * 
     * @param handler
    **/
    public abstract void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * Sets the value of field 'execTime'.
     * 
     * @param execTime the value of field 'execTime'.
    **/
    public void setExecTime(java.util.Date execTime)
    {
        this._execTime = execTime;
    } //-- void setExecTime(java.util.Date) 

    /**
     * Sets the value of field 'heartbeatResult'.
     * 
     * @param heartbeatResult the value of field 'heartbeatResult'.
    **/
    public void setHeartbeatResult(java.lang.String heartbeatResult)
    {
        this._heartbeatResult = heartbeatResult;
    } //-- void setHeartbeatResult(java.lang.String) 

    /**
     * Sets the value of field 'hostname'.
     * 
     * @param hostname the value of field 'hostname'.
    **/
    public void setHostname(java.lang.String hostname)
    {
        this._hostname = hostname;
    } //-- void setHostname(java.lang.String) 

    /**
     * Sets the value of field 'isMonitor'.
     * 
     * @param isMonitor the value of field 'isMonitor'.
    **/
    public void setIsMonitor(boolean isMonitor)
    {
        this._isMonitor = isMonitor;
        this._has_isMonitor = true;
    } //-- void setIsMonitor(boolean) 

    /**
     * Sets the value of field 'missedHeartbeats'.
     * 
     * @param missedHeartbeats the value of field 'missedHeartbeats'
    **/
    public void setMissedHeartbeats(int missedHeartbeats)
    {
        this._missedHeartbeats = missedHeartbeats;
        this._has_missedHeartbeats = true;
    } //-- void setMissedHeartbeats(int) 

    /**
     * Sets the value of field 'processId'.
     * 
     * @param processId the value of field 'processId'.
    **/
    public void setProcessId(long processId)
    {
        this._processId = processId;
        this._has_processId = true;
    } //-- void setProcessId(long) 

    /**
     * Sets the value of field 'processName'.
     * 
     * @param processName the value of field 'processName'.
    **/
    public void setProcessName(java.lang.String processName)
    {
        this._processName = processName;
    } //-- void setProcessName(java.lang.String) 

    /**
     * Sets the value of field 'startGeneration'.
     * 
     * @param startGeneration the value of field 'startGeneration'.
    **/
    public void setStartGeneration(int startGeneration)
    {
        this._startGeneration = startGeneration;
        this._has_startGeneration = true;
    } //-- void setStartGeneration(int) 

    /**
     * Sets the value of field 'state'.
     * 
     * @param state the value of field 'state'.
    **/
    public void setState(java.lang.String state)
    {
        this._state = state;
    } //-- void setState(java.lang.String) 

    /**
     * Sets the value of field 'successfulHeartbeats'.
     * 
     * @param successfulHeartbeats the value of field
     * 'successfulHeartbeats'.
    **/
    public void setSuccessfulHeartbeats(int successfulHeartbeats)
    {
        this._successfulHeartbeats = successfulHeartbeats;
        this._has_successfulHeartbeats = true;
    } //-- void setSuccessfulHeartbeats(int) 

    /**
     * Sets the value of field 'usesNativeLogging'.
     * 
     * @param usesNativeLogging the value of field
     * 'usesNativeLogging'.
    **/
    public void setUsesNativeLogging(java.lang.String usesNativeLogging)
    {
        this._usesNativeLogging = usesNativeLogging;
    } //-- void setUsesNativeLogging(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

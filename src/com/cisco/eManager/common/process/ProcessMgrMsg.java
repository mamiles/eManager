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
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Process Sequencer Msgs
 * 
 * @version $Revision$ $Date$
**/
public class ProcessMgrMsg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private GetProcessStatus _getProcessStatus;

    private GetProcessStatusFor _getProcessStatusFor;

    private GetGroupState _getGroupState;

    private java.lang.String _getSolutionStatus;

    private GetAllGroupNames _getAllGroupNames;

    private GetProcesssesForGroup _getProcesssesForGroup;

    private GetHealth _getHealth;

    private java.lang.String _getSolutionHealth;

    private StartProcess _startProcess;

    private StartApplication _startApplication;

    private StartAll _startAll;

    private java.lang.String _startSolution;

    private StartCommand _startCommand;

    private StartGroup _startGroup;

    private StopCommand _stopCommand;

    private StopAll _stopAll;

    private StopApplication _stopApplication;

    private StopProcess _stopProcess;

    private java.lang.String _stopSolution;

    private StopGroup _stopGroup;

    private RestartProcess _restartProcess;

    private RestartGroup _restartGroup;

    private RestartAll _restartAll;

    private RestartCommand _restartCommand;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProcessMgrMsg() {
        super();
    } //-- com.cisco.eManager.common.process.ProcessMgrMsg()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'getAllGroupNames'.
     * 
     * @return the value of field 'getAllGroupNames'.
    **/
    public GetAllGroupNames getGetAllGroupNames()
    {
        return this._getAllGroupNames;
    } //-- GetAllGroupNames getGetAllGroupNames() 

    /**
     * Returns the value of field 'getGroupState'.
     * 
     * @return the value of field 'getGroupState'.
    **/
    public GetGroupState getGetGroupState()
    {
        return this._getGroupState;
    } //-- GetGroupState getGetGroupState() 

    /**
     * Returns the value of field 'getHealth'.
     * 
     * @return the value of field 'getHealth'.
    **/
    public GetHealth getGetHealth()
    {
        return this._getHealth;
    } //-- GetHealth getGetHealth() 

    /**
     * Returns the value of field 'getProcessStatus'.
     * 
     * @return the value of field 'getProcessStatus'.
    **/
    public GetProcessStatus getGetProcessStatus()
    {
        return this._getProcessStatus;
    } //-- GetProcessStatus getGetProcessStatus() 

    /**
     * Returns the value of field 'getProcessStatusFor'.
     * 
     * @return the value of field 'getProcessStatusFor'.
    **/
    public GetProcessStatusFor getGetProcessStatusFor()
    {
        return this._getProcessStatusFor;
    } //-- GetProcessStatusFor getGetProcessStatusFor() 

    /**
     * Returns the value of field 'getProcesssesForGroup'.
     * 
     * @return the value of field 'getProcesssesForGroup'.
    **/
    public GetProcesssesForGroup getGetProcesssesForGroup()
    {
        return this._getProcesssesForGroup;
    } //-- GetProcesssesForGroup getGetProcesssesForGroup() 

    /**
     * Returns the value of field 'getSolutionHealth'.
     * 
     * @return the value of field 'getSolutionHealth'.
    **/
    public java.lang.String getGetSolutionHealth()
    {
        return this._getSolutionHealth;
    } //-- java.lang.String getGetSolutionHealth() 

    /**
     * Returns the value of field 'getSolutionStatus'.
     * 
     * @return the value of field 'getSolutionStatus'.
    **/
    public java.lang.String getGetSolutionStatus()
    {
        return this._getSolutionStatus;
    } //-- java.lang.String getGetSolutionStatus() 

    /**
     * Returns the value of field 'restartAll'.
     * 
     * @return the value of field 'restartAll'.
    **/
    public RestartAll getRestartAll()
    {
        return this._restartAll;
    } //-- RestartAll getRestartAll() 

    /**
     * Returns the value of field 'restartCommand'.
     * 
     * @return the value of field 'restartCommand'.
    **/
    public RestartCommand getRestartCommand()
    {
        return this._restartCommand;
    } //-- RestartCommand getRestartCommand() 

    /**
     * Returns the value of field 'restartGroup'.
     * 
     * @return the value of field 'restartGroup'.
    **/
    public RestartGroup getRestartGroup()
    {
        return this._restartGroup;
    } //-- RestartGroup getRestartGroup() 

    /**
     * Returns the value of field 'restartProcess'.
     * 
     * @return the value of field 'restartProcess'.
    **/
    public RestartProcess getRestartProcess()
    {
        return this._restartProcess;
    } //-- RestartProcess getRestartProcess() 

    /**
     * Returns the value of field 'startAll'.
     * 
     * @return the value of field 'startAll'.
    **/
    public StartAll getStartAll()
    {
        return this._startAll;
    } //-- StartAll getStartAll() 

    /**
     * Returns the value of field 'startApplication'.
     * 
     * @return the value of field 'startApplication'.
    **/
    public StartApplication getStartApplication()
    {
        return this._startApplication;
    } //-- StartApplication getStartApplication() 

    /**
     * Returns the value of field 'startCommand'.
     * 
     * @return the value of field 'startCommand'.
    **/
    public StartCommand getStartCommand()
    {
        return this._startCommand;
    } //-- StartCommand getStartCommand() 

    /**
     * Returns the value of field 'startGroup'.
     * 
     * @return the value of field 'startGroup'.
    **/
    public StartGroup getStartGroup()
    {
        return this._startGroup;
    } //-- StartGroup getStartGroup() 

    /**
     * Returns the value of field 'startProcess'.
     * 
     * @return the value of field 'startProcess'.
    **/
    public StartProcess getStartProcess()
    {
        return this._startProcess;
    } //-- StartProcess getStartProcess() 

    /**
     * Returns the value of field 'startSolution'.
     * 
     * @return the value of field 'startSolution'.
    **/
    public java.lang.String getStartSolution()
    {
        return this._startSolution;
    } //-- java.lang.String getStartSolution() 

    /**
     * Returns the value of field 'stopAll'.
     * 
     * @return the value of field 'stopAll'.
    **/
    public StopAll getStopAll()
    {
        return this._stopAll;
    } //-- StopAll getStopAll() 

    /**
     * Returns the value of field 'stopApplication'.
     * 
     * @return the value of field 'stopApplication'.
    **/
    public StopApplication getStopApplication()
    {
        return this._stopApplication;
    } //-- StopApplication getStopApplication() 

    /**
     * Returns the value of field 'stopCommand'.
     * 
     * @return the value of field 'stopCommand'.
    **/
    public StopCommand getStopCommand()
    {
        return this._stopCommand;
    } //-- StopCommand getStopCommand() 

    /**
     * Returns the value of field 'stopGroup'.
     * 
     * @return the value of field 'stopGroup'.
    **/
    public StopGroup getStopGroup()
    {
        return this._stopGroup;
    } //-- StopGroup getStopGroup() 

    /**
     * Returns the value of field 'stopProcess'.
     * 
     * @return the value of field 'stopProcess'.
    **/
    public StopProcess getStopProcess()
    {
        return this._stopProcess;
    } //-- StopProcess getStopProcess() 

    /**
     * Returns the value of field 'stopSolution'.
     * 
     * @return the value of field 'stopSolution'.
    **/
    public java.lang.String getStopSolution()
    {
        return this._stopSolution;
    } //-- java.lang.String getStopSolution() 

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
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'getAllGroupNames'.
     * 
     * @param getAllGroupNames the value of field 'getAllGroupNames'
    **/
    public void setGetAllGroupNames(GetAllGroupNames getAllGroupNames)
    {
        this._getAllGroupNames = getAllGroupNames;
    } //-- void setGetAllGroupNames(GetAllGroupNames) 

    /**
     * Sets the value of field 'getGroupState'.
     * 
     * @param getGroupState the value of field 'getGroupState'.
    **/
    public void setGetGroupState(GetGroupState getGroupState)
    {
        this._getGroupState = getGroupState;
    } //-- void setGetGroupState(GetGroupState) 

    /**
     * Sets the value of field 'getHealth'.
     * 
     * @param getHealth the value of field 'getHealth'.
    **/
    public void setGetHealth(GetHealth getHealth)
    {
        this._getHealth = getHealth;
    } //-- void setGetHealth(GetHealth) 

    /**
     * Sets the value of field 'getProcessStatus'.
     * 
     * @param getProcessStatus the value of field 'getProcessStatus'
    **/
    public void setGetProcessStatus(GetProcessStatus getProcessStatus)
    {
        this._getProcessStatus = getProcessStatus;
    } //-- void setGetProcessStatus(GetProcessStatus) 

    /**
     * Sets the value of field 'getProcessStatusFor'.
     * 
     * @param getProcessStatusFor the value of field
     * 'getProcessStatusFor'.
    **/
    public void setGetProcessStatusFor(GetProcessStatusFor getProcessStatusFor)
    {
        this._getProcessStatusFor = getProcessStatusFor;
    } //-- void setGetProcessStatusFor(GetProcessStatusFor) 

    /**
     * Sets the value of field 'getProcesssesForGroup'.
     * 
     * @param getProcesssesForGroup the value of field
     * 'getProcesssesForGroup'.
    **/
    public void setGetProcesssesForGroup(GetProcesssesForGroup getProcesssesForGroup)
    {
        this._getProcesssesForGroup = getProcesssesForGroup;
    } //-- void setGetProcesssesForGroup(GetProcesssesForGroup) 

    /**
     * Sets the value of field 'getSolutionHealth'.
     * 
     * @param getSolutionHealth the value of field
     * 'getSolutionHealth'.
    **/
    public void setGetSolutionHealth(java.lang.String getSolutionHealth)
    {
        this._getSolutionHealth = getSolutionHealth;
    } //-- void setGetSolutionHealth(java.lang.String) 

    /**
     * Sets the value of field 'getSolutionStatus'.
     * 
     * @param getSolutionStatus the value of field
     * 'getSolutionStatus'.
    **/
    public void setGetSolutionStatus(java.lang.String getSolutionStatus)
    {
        this._getSolutionStatus = getSolutionStatus;
    } //-- void setGetSolutionStatus(java.lang.String) 

    /**
     * Sets the value of field 'restartAll'.
     * 
     * @param restartAll the value of field 'restartAll'.
    **/
    public void setRestartAll(RestartAll restartAll)
    {
        this._restartAll = restartAll;
    } //-- void setRestartAll(RestartAll) 

    /**
     * Sets the value of field 'restartCommand'.
     * 
     * @param restartCommand the value of field 'restartCommand'.
    **/
    public void setRestartCommand(RestartCommand restartCommand)
    {
        this._restartCommand = restartCommand;
    } //-- void setRestartCommand(RestartCommand) 

    /**
     * Sets the value of field 'restartGroup'.
     * 
     * @param restartGroup the value of field 'restartGroup'.
    **/
    public void setRestartGroup(RestartGroup restartGroup)
    {
        this._restartGroup = restartGroup;
    } //-- void setRestartGroup(RestartGroup) 

    /**
     * Sets the value of field 'restartProcess'.
     * 
     * @param restartProcess the value of field 'restartProcess'.
    **/
    public void setRestartProcess(RestartProcess restartProcess)
    {
        this._restartProcess = restartProcess;
    } //-- void setRestartProcess(RestartProcess) 

    /**
     * Sets the value of field 'startAll'.
     * 
     * @param startAll the value of field 'startAll'.
    **/
    public void setStartAll(StartAll startAll)
    {
        this._startAll = startAll;
    } //-- void setStartAll(StartAll) 

    /**
     * Sets the value of field 'startApplication'.
     * 
     * @param startApplication the value of field 'startApplication'
    **/
    public void setStartApplication(StartApplication startApplication)
    {
        this._startApplication = startApplication;
    } //-- void setStartApplication(StartApplication) 

    /**
     * Sets the value of field 'startCommand'.
     * 
     * @param startCommand the value of field 'startCommand'.
    **/
    public void setStartCommand(StartCommand startCommand)
    {
        this._startCommand = startCommand;
    } //-- void setStartCommand(StartCommand) 

    /**
     * Sets the value of field 'startGroup'.
     * 
     * @param startGroup the value of field 'startGroup'.
    **/
    public void setStartGroup(StartGroup startGroup)
    {
        this._startGroup = startGroup;
    } //-- void setStartGroup(StartGroup) 

    /**
     * Sets the value of field 'startProcess'.
     * 
     * @param startProcess the value of field 'startProcess'.
    **/
    public void setStartProcess(StartProcess startProcess)
    {
        this._startProcess = startProcess;
    } //-- void setStartProcess(StartProcess) 

    /**
     * Sets the value of field 'startSolution'.
     * 
     * @param startSolution the value of field 'startSolution'.
    **/
    public void setStartSolution(java.lang.String startSolution)
    {
        this._startSolution = startSolution;
    } //-- void setStartSolution(java.lang.String) 

    /**
     * Sets the value of field 'stopAll'.
     * 
     * @param stopAll the value of field 'stopAll'.
    **/
    public void setStopAll(StopAll stopAll)
    {
        this._stopAll = stopAll;
    } //-- void setStopAll(StopAll) 

    /**
     * Sets the value of field 'stopApplication'.
     * 
     * @param stopApplication the value of field 'stopApplication'.
    **/
    public void setStopApplication(StopApplication stopApplication)
    {
        this._stopApplication = stopApplication;
    } //-- void setStopApplication(StopApplication) 

    /**
     * Sets the value of field 'stopCommand'.
     * 
     * @param stopCommand the value of field 'stopCommand'.
    **/
    public void setStopCommand(StopCommand stopCommand)
    {
        this._stopCommand = stopCommand;
    } //-- void setStopCommand(StopCommand) 

    /**
     * Sets the value of field 'stopGroup'.
     * 
     * @param stopGroup the value of field 'stopGroup'.
    **/
    public void setStopGroup(StopGroup stopGroup)
    {
        this._stopGroup = stopGroup;
    } //-- void setStopGroup(StopGroup) 

    /**
     * Sets the value of field 'stopProcess'.
     * 
     * @param stopProcess the value of field 'stopProcess'.
    **/
    public void setStopProcess(StopProcess stopProcess)
    {
        this._stopProcess = stopProcess;
    } //-- void setStopProcess(StopProcess) 

    /**
     * Sets the value of field 'stopSolution'.
     * 
     * @param stopSolution the value of field 'stopSolution'.
    **/
    public void setStopSolution(java.lang.String stopSolution)
    {
        this._stopSolution = stopSolution;
    } //-- void setStopSolution(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.process.ProcessMgrMsg unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.process.ProcessMgrMsg) Unmarshaller.unmarshal(com.cisco.eManager.common.process.ProcessMgrMsg.class, reader);
    } //-- com.cisco.eManager.common.process.ProcessMgrMsg unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

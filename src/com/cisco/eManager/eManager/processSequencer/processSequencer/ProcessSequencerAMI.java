package com.cisco.eManager.eManager.processSequencer.processSequencer;

import com.cisco.eManager.eManager.processSequencer.common.*;
import com.cisco.eManager.eManager.processSequencer.common.logging.*;
import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;
import java.util.*;

/**
 * <p>Title: ProcessSequencer AMI</p>
 * <p>Description: This provides the AMI interface to Process Sequencer </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco System Inc</p>
 * @author Marvin Miles
 * @version 1.0
 */

public class ProcessSequencerAMI {

    private ProcessSequencer mProcessSequencer = null;
    private AmiSession mAMI = null;
    protected CiscoLogger mLogger;

    public ProcessSequencerAMI(
        ProcessSequencer inProcessSequencer,
        String rvService,
        String rvNetwork,
        String rvDaemon,
        TibrvQueue rvQueue) throws AmiException {

        mLogger = CiscoLogger.getCiscoLogger("processSequencer");
        mLogger.fine("ProcessSequencerAMI constructor...");
        mProcessSequencer = inProcessSequencer;

        // Create an AMI API session.
        mAMI = new AmiSession(rvService, rvNetwork, rvDaemon, rvQueue,
                              "com.cisco.eManager.eManager.processSequencer.processSequencer",
                              "ProcessSequencer",
            "The ProcessSequencer controls the registration of new applications to " +
            "the Watchdog process.  It also starts and stops a specific application " +
            "that is under the control of the application Watchdog process.", null);

        // Add Receiver methods to session.
        mAMI.addMethod(new methodRegister());
        mAMI.addMethod(new methodUnRegister());
        mAMI.addMethod(new methodStartApplication());
        mAMI.addMethod(new methodStopApplication());
        mAMI.addMethod(new methodGetEMHome());
        mAMI.addMethod(new methodApplicationType());
        mAMI.addMethod(new methodApplicationInstanceId());
        mAMI.addMethod(new methodGetLogLevel());
        mAMI.addMethod(new methodSetLogLevel());
        mAMI.addMethod(new methodRegisterSolution());
        mAMI.addMethod(new methodGetSolutionHealth());
        mAMI.addMethod(new methodStartSolution());
        mAMI.addMethod(new methodStopSolution());
        mAMI.addMethod(new methodGetSolutionStatus());

        mAMI.addMethods(mAMI);

        mAMI.createCommonMethods(
            "ProcessSequencer (Control Watchdog Applicaton Processes)",
            "1.0.0",
            "Wed 09/10/2003",
            1,
            0,
            0);

        // Annouce our existence.
        mAMI.announce();
        mLogger.fine("Constructor completed...");
    }

    public void terminate() throws AmiException {
        mAMI.stop();
    }

    class methodGetEMHome
        extends AmiMethod {
        public AmiParameterList getArguments() {
            return (null);
        }

        public methodGetEMHome() {
            super("getEMHome",
                  "This method returns the eManager Home directory.",
                  AmiConstants.METHOD_TYPE_INFO);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("EMHome",
                "The eManager Home directory ", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;

            try {
                theValues = new AmiParameterList();
                theValues.addElement(
                    new AmiParameter("EMHome",
                              new String(mProcessSequencer.getEMHome())));
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);

        }
    }

    class methodApplicationType
        extends AmiMethod {
        public AmiParameterList getArguments() {
            return (null);
        }

        public methodApplicationType() {
            super("getApplicationType",
                  "This method returns the Application Type (applicaiton name).",
                  AmiConstants.METHOD_TYPE_INFO);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("ApplicationType",
                "Application Type ", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;

            try {
                theValues = new AmiParameterList();
                theValues.addElement(
                    new AmiParameter("ApplicationType",
                              new String("eManager")));
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);

        }
    }

    class methodApplicationInstanceId
        extends AmiMethod {
        public AmiParameterList getArguments() {
            return (null);
        }

        public methodApplicationInstanceId() {
            super("getApplicationInstanceId",
                  "This method returns the Application Type (applicaiton name).",
                  AmiConstants.METHOD_TYPE_INFO);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("ApplicationInstanceId",
                "Application Instance Id ", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;

            try {
                theValues = new AmiParameterList();
                theValues.addElement(
                    new AmiParameter("ApplicationInstanceId",
                              new String("app")));
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);

        }
    }

    class methodGetLogLevel
        extends AmiMethod {
        public AmiParameterList getArguments() {
            return (null);
        }

        public methodGetLogLevel() {
            super("getLogLevel",
                  "This method returns the Logging Level for ProcessSequencer.",
                  AmiConstants.METHOD_TYPE_INFO);
        }

        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("Log Level",
                "The current Process Sequencer Log Level.  Log messages below this level will not be logged.",
                ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;

            try {
                theValues = new AmiParameterList();
                theValues.addElement(
                    new AmiParameter("Log Level",
                                     new String(mProcessSequencer.getLogLevelName())));
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);

        }
    }

    class methodSetLogLevel
        extends AmiMethod {
        private final String[] sLegalChoices
            = {
            "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER",
            "FINEST", "ALL", "OFF"};

        public AmiParameterList getReturns() {
            return (null);
        }

        public methodSetLogLevel() {
            super("setLogLevel",
                  "This method sets the Process Sequencer Log Level.",
                  AmiConstants.METHOD_TYPE_ACTION);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter theArg = null;

            theArg = new AmiParameter("LogLevel",
                                      "The new Log Level name.", "");
            theArg.setLegalChoices(sLegalChoices);
            theArgs.addElement(theArg);

            return (theArgs);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            try {
                String theLogLevel = (String) ( (AmiParameter) inParms.elementAt(0)).getValue();
                mProcessSequencer.setLogLevel(theLogLevel);
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodRegister
        extends AmiMethod {

        public AmiParameterList getReturns() {
            return (null);
        }

        public methodRegister() {
            super("register",
                  "This method registers the application to Watchdog with a properties file.",
                  AmiConstants.METHOD_TYPE_ACTION);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter appTypeArg = null;
            AmiParameter appInstArg = null;
            AmiParameter appPropArg = null;

            appTypeArg = new AmiParameter("AppType",
                                          "The Application Type or Application Name.", "");
            appInstArg = new AmiParameter("AppInst",
                                          "The Application Instance Name.", "");
            appPropArg = new AmiParameter("AppPropFile",
                                          "The Application Watchdog Property File Name " +
                                          "(must be the full path " +
                                          "to the property file).", "");

            theArgs.addElement(appTypeArg);
            theArgs.addElement(appInstArg);
            theArgs.addElement(appPropArg);

            return (theArgs);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            try {

                String appTypeArg = ( (String) ( (AmiParameter) inParms.elementAt(0)).getValue());
                String appInstArg = ( (String) ( (AmiParameter) inParms.elementAt(1)).getValue());
                String appPropArg = ( (String) ( (AmiParameter) inParms.elementAt(2)).getValue());
                mProcessSequencer.register(appTypeArg, appInstArg, appPropArg);

            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodUnRegister
        extends AmiMethod {

        public AmiParameterList getReturns() {
            return (null);
        }

        public methodUnRegister() {
            super("unregister",
                  "This method removes the registration from Watchdog for a application and removes the properties file.",
                  AmiConstants.METHOD_TYPE_ACTION);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter appTypeArg = null;
            AmiParameter appInstArg = null;

            appTypeArg = new AmiParameter("AppType",
                                          "The Application Type or Application Name.", "");
            appInstArg = new AmiParameter("AppInst",
                                          "The Application Instance Name.", "");

            theArgs.addElement(appTypeArg);
            theArgs.addElement(appInstArg);

            return (theArgs);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            try {

                String appTypeArg = ( (String) ( (AmiParameter) inParms.elementAt(0)).getValue());
                String appInstArg = ( (String) ( (AmiParameter) inParms.elementAt(1)).getValue());
                mProcessSequencer.unregister(appTypeArg, appInstArg);

            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodStartApplication
        extends AmiMethod {

        public AmiParameterList getReturns() {
            return (null);
        }

        public methodStartApplication() {
            super("startApplication",
                  "This method will start the specific application using Watchdog.",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter appTypeArg = null;
            AmiParameter appInstArg = null;

            appTypeArg = new AmiParameter("AppType",
                                          "The Application Type or Application Name.", "");
            appInstArg = new AmiParameter("AppInst",
                                          "The Application Instance Name.", "");

            theArgs.addElement(appTypeArg);
            theArgs.addElement(appInstArg);

            return (theArgs);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            try {

                String appTypeArg = ( (String) ( (AmiParameter) inParms.elementAt(0)).getValue());
                String appInstArg = ( (String) ( (AmiParameter) inParms.elementAt(1)).getValue());
                mProcessSequencer.startApplication(appTypeArg, appInstArg);
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodStopApplication
        extends AmiMethod {

        public AmiParameterList getReturns() {
            return (null);
        }

        public methodStopApplication() {
            super("stopApplication",
                  "This method will stop the specific application by bringing down Watchdog.",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter appTypeArg = null;
            AmiParameter appInstArg = null;

            appTypeArg = new AmiParameter("AppType",
                                          "The Application Type or Application Name.", "");
            appInstArg = new AmiParameter("AppInst",
                                          "The Application Instance Name.", "");

            theArgs.addElement(appTypeArg);
            theArgs.addElement(appInstArg);

            return (theArgs);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            try {

                String appTypeArg = ( (String) ( (AmiParameter) inParms.elementAt(0)).getValue());
                String appInstArg = ( (String) ( (AmiParameter) inParms.elementAt(1)).getValue());
                mProcessSequencer.stopApplication(appTypeArg, appInstArg);
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodRegisterSolution
        extends AmiMethod {

        public AmiParameterList getReturns() {
            return (null);
        }

        public methodRegisterSolution() {
            super("registerSolution",
                  "This method registers a solution to Process Sequencer and takes a XML file.",
                  AmiConstants.METHOD_TYPE_ACTION);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter xmlString = null;
            xmlString = new AmiParameter("SolutionRegisterXML",
                                          "The Solution Registration XML message.", "");
            theArgs.addElement(xmlString);
            return (theArgs);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            try {

                String xmlString = ( (String) ( (AmiParameter) inParms.elementAt(0)).getValue());
                mProcessSequencer.registerSolution(xmlString);

            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodGetSolutionHealth
        extends AmiMethod {

        public methodGetSolutionHealth() {
            super("getSolutionHealth",
                  "This method check the health of all Application process in the solution by calling the getHealth method for each application.",
                  AmiConstants.METHOD_TYPE_ACTION_INFO, 90000);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter solutionName = null;

            solutionName = new AmiParameter("SolutionName",
                                          "The Name of the Solution.", "");

            theArgs.addElement(solutionName);
            return (theArgs);
        }


        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("isSolutionHealthy", "All applications in solution are up and running.", false));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;

            try {
                theValues = new AmiParameterList();
                String solutionName = ( (String) ( (AmiParameter) inParms.elementAt(0)).getValue());
                theValues.addElement(
                    new AmiParameter("isSolutionHealthy",
                              new Boolean(mProcessSequencer.getSolutionHealth(solutionName).booleanValue())));
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);
        }
    }

    class methodGetSolutionStatus
        extends AmiMethod {

        public methodGetSolutionStatus() {
            super("getSolutionStatus",
                  "This method check the state of all Application process in the solution by calling the getApplicationState method for each application.",
                  AmiConstants.METHOD_TYPE_ACTION_INFO, 90000, "ApplicatonName");
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter solutionName = null;

            solutionName = new AmiParameter("SolutionName",
                                          "The Name of the Solution.", "");

            theArgs.addElement(solutionName);
            return (theArgs);
        }


        public AmiParameterList getReturns() {
            AmiParameterList theReturns = new AmiParameterList();
            theReturns.addElement(new AmiParameter("ApplicatonName", "The Name of Application (Application Type and Application InstanceID).", ""));
            theReturns.addElement(new AmiParameter("ApplicationState", "The State of Application.", ""));
            return (theReturns);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            AmiParameterList theValues = null;

            try {
                theValues = new AmiParameterList();
                String solutionName = ( (String) ( (AmiParameter) inParms.elementAt(0)).getValue());
                SolutionRegistrationComponent[] component = mProcessSequencer.getSolutionStatus(solutionName);
                for (int i = 0; i < component.length; i++) {
                    theValues.addElement(
                        new AmiParameter("ApplicatonName", new String(component[i].getAppType() + "." + component[i].getAppInstance()) ));
                    theValues.addElement(
                        new AmiParameter("ApplicationState", new String(component[i].getState()) ));
                }
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (theValues);
        }
    }


    class methodStartSolution
        extends AmiMethod {

        public AmiParameterList getReturns() {
            return (null);
        }

        public methodStartSolution() {
            super("startSolution",
                  "This method will start all the applications that are part of the solution if they are not running.",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter solutionName = null;

            solutionName = new AmiParameter("SolutionName",
                                          "The Name of the Solution.", "");
            theArgs.addElement(solutionName);

            return (theArgs);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            try {

                String solutionName = ( (String) ( (AmiParameter) inParms.elementAt(0)).getValue());
                mProcessSequencer.startSolution(solutionName);
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }

    class methodStopSolution
        extends AmiMethod {

        public AmiParameterList getReturns() {
            return (null);
        }

        public methodStopSolution() {
            super("stopSolution",
                  "This method will stop all the applications that are part of the solution if they are running.",
                  AmiConstants.METHOD_TYPE_ACTION, 90000);
        }

        public AmiParameterList getArguments() {
            AmiParameterList theArgs = new AmiParameterList();
            AmiParameter solutionName = null;

            solutionName = new AmiParameter("SolutionName",
                                          "The Name of the Solution.", "");
            theArgs.addElement(solutionName);

            return (theArgs);
        }

        public AmiParameterList onInvoke(
            AmiParameterList inParms) throws AmiException {
            try {

                String solutionName = ( (String) ( (AmiParameter) inParms.elementAt(0)).getValue());
                mProcessSequencer.stopSolution(solutionName);
            }
            catch (Exception caughtException) {
                throw (new AmiException(AmiErrors.AMI_REPLY_ERR,
                                        caughtException.getMessage()));
            }
            return (null);
        }
    }


}

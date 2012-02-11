package com.cisco.eManager.client.cmdLine;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import com.cisco.eManager.common.util.AccessType;
//import com.cisco.eManager.soap.common.process.*;
//import com.cisco.eManager.soap.process.*;
//  Temp
import com.cisco.eManager.eManager.process.ProcessManager;
import com.cisco.eManager.common.process.*;
//  Temp
import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.AxisProperties;
import java.net.URL;
import org.apache.axis.*;
import javax.xml.rpc.*;
import java.rmi.*;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author Marvin Miles
 * @version 1.0
 */

public class CommandLine
{
    protected static final int EXIT_SUCCESS = 0;
    protected static final int EXIT_FAILURE = 1;
    protected static final int EXIT_CONNECT = 2;
    protected static final int EXIT_USAGE = 3;
    protected static final int EXIT_UNKNOWN = 4;

    final static String msSpaces = "                                                                                                                ";

    protected static PrintWriter pw = new PrintWriter(System.err, true);

    private static String mAppType = null;
    private static String mAppInst = null;
    private static String mUserId = null;
    private static String mPassword = null;
    private static AccessType mAccess = null;
    private static int pollInterval = -1;
    private static boolean execCommand = false;

    private static StringWriter messageWrite = new StringWriter();

    private static ProcessManager pmStub = null;

    final static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss z");
    static String servStatusHeader = null;
    static {
        StringBuffer sb = new StringBuffer("Name"
                                           + msSpaces.substring (4, 21)
                                           + "State"
                                           + msSpaces.substring (5, 16)
                                           + "Gen"
                                           + msSpaces.substring (3, 6)
                                           + "Exec Time"
                                           + msSpaces.substring (9, 21)
                                           + "PID"
                                           + msSpaces.substring (3, 7)
                                           + "Success"
                                           + msSpaces.substring (7, 9)
                                           + "Missed"
                                           + msSpaces.substring (6, 8)
                                           + "Heartbeat"
                                           + msSpaces.substring (9, 10)
                                           + "Log"
                                           + msSpaces.substring (3, 6)
                                           + "Monitor");
        servStatusHeader = sb.toString();
    }
    static String servSolutionHeader = null;
    static {
        StringBuffer sb = new StringBuffer("Application"
                                           + msSpaces.substring(11,21)
                                           + "Instance"
                                           + msSpaces.substring(8, 16)
                                           + "State");
        servSolutionHeader = sb.toString();
    }

    public static void main(String[] args)
    {
        //String CONFIG_FILE = "WEB-INF/client-config.wsdd";
        String emHome = System.getProperty("EMANAGER_ROOT");
        String filename = emHome + "/config/em.properties";
        Properties emProp = new Properties(System.getProperties());
        try
        {
            FileInputStream propFileStream =
                new FileInputStream(filename);
            emProp.load(propFileStream);
            System.setProperties(emProp);
        }
        catch (FileNotFoundException e)
        {
            pw.println("Property File: " + filename + " is not found");
            System.exit(EXIT_CONNECT);
        }
        catch (IOException e)
        {
            pw.println("Caught IOException while processing property file: " + filename);
            System.exit(EXIT_CONNECT);
        }
/*
        String nbapiURL = emProp.getProperty("EM.NBAPI.ProcessManager.URL", null);
        String timeoutStr = emProp.getProperty("EM.NBAPI.Timeout", "30000");
        Integer in = Integer.valueOf(timeoutStr);
        int timeout = in.intValue();

        URL url = null;
        try
        {
            url = new java.net.URL(nbapiURL);
            //pmStub = loc.getProcessManager(url);
            pmStub = (ProcessManagerSoapBindingStub)
                     new ProcessManagerServiceLocator().getProcessManager(url);

            ((ProcessManagerSoapBindingStub)pmStub).setTimeout(timeout);
        }
        catch (MalformedURLException ex)
        {
            pw.println("There is a problem connecting to the eManager Server:");
            pw.println("MalformedURLException: " + nbapiURL);
            System.exit(EXIT_CONNECT);
        }
        catch (ServiceException ex2)
        {
            pw.println("There is a problem connecting to the eManager Server:");
            pw.println("ServiceException:  exiting...");
            System.exit(EXIT_CONNECT);
        }

*/
        String log4jConfigFile = emHome + "/config/cmdLineLog.properties";
        org.apache.log4j.PropertyConfigurator.configure(log4jConfigFile);
        pmStub = ProcessManager.instance();


        if (args.length == 0)
        {
            usage();
        }

        for (int i = 0; i < args.length; i++)
        {
            i = evaluateCommand(args, i);
            if (execCommand)
            {
                break;
            }
        }
        //pw.println("appType:" + mAppType + " appInst:" + mAppInst + " userid:" + mUserId + " password:" + mPassword);
        if (!execCommand)
        {
            pw.println("Entering interactive mode.  Enter 'quit' to exit");
            pw.println("");
            pw.write(mAppType + "." + mAppInst + ">> ");
            pw.flush();
            try
            {
                BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
                String inputLine;
                while ((inputLine = is.readLine()) != null)
                {
                    StringTokenizer st = new StringTokenizer(inputLine);
                    String[] arg = new String[st.countTokens()];
                    //pw.println(st.countTokens());
                    int j = 0;
                    while (st.hasMoreTokens())
                    {
                        arg[j] = st.nextToken();
                        //pw.println(j + " = " + arg[j]);
                        j = j + 1;
                    }
                    for (int ix = 0; ix < arg.length; ix++)
                    {
                        ix = evaluateCommand(arg, ix);
                        break;
                    }
                    pw.write(mAppType + "." + mAppInst + ">> ");
                    pw.flush();
                }
                is.close();
            }
            catch (IOException e)
            {
                System.out.println("IOExecption: " + e);
            }
        }
        System.exit(EXIT_SUCCESS);
    }

    private static int evaluateCommand(String[] args, int i)
    {
        if (args[i].equals("-u") && i < args.length - 1)
        {
            mUserId = args[++i];
        }
        else if (args[i].equals("-p") && i < args.length - 1)
        {
            mPassword = args[++i];
        }
        else if (args[i].equals("-a") && i < args.length - 1)
        {
            mAppType = args[++i];
        }
        else if (args[i].equals("appname") && i < args.length - 1)
        {
            mAppType = args[++i];
        }
        else if (args[i].equals("-i") && i < args.length - 1)
        {
            mAppInst = args[++i];
        }
        else if (args[i].equals("instname") && i < args.length - 1)
        {
            mAppInst = args[++i];
        }
        else if (args[i].equals("-poll") && i < args.length - 1)
        {
            pollInterval = Integer.parseInt(args[++i]) * 1000;
            if (pollInterval < 0)
            {
                pw.println("-poll argument must be positive");
                pw.println("");
                usage();
                System.exit(EXIT_USAGE);
            }
        }
        else if (args[i].equals("-help"))
        {
            usage();
            System.exit(EXIT_USAGE);
        }
        else if (args[i].equals("help"))
        {
            usage();
        }
        else if (args[i].equals("health"))
        {
            execCommand = true;
            cmdHealth(getCmdArgs(args, i));
        }
        else if (args[i].equals("solhealth"))
        {
            execCommand = true;
            cmdSolhealth(getCmdArgs(args, i));
        }
        else if (args[i].equals("status"))
        {
            execCommand = true;
            cmdStatus(getCmdArgs(args, i));
        }
        else if (args[i].equals("solstatus"))
        {
            execCommand = true;
            cmdSolstatus(getCmdArgs(args, i));
        }
        else if (args[i].equals("grpstatus"))
        {
            execCommand = true;
            cmdGrpstatus(getCmdArgs(args, i));
        }
        else if (args[i].equals("groups"))
        {
            execCommand = true;
            cmdGroups(getCmdArgs(args, i));
        }
        else if (args[i].equals("group"))
        {
            execCommand = true;
            cmdGroup(getCmdArgs(args, i));
        }
        else if (args[i].equals("start"))
        {
            execCommand = true;
            if (i < args.length - 1) {
                if (args[i + 1].equalsIgnoreCase("app"))
                {
                    cmdStartApplication(getCmdArgs(args, i));
                }
                else
                {
                    cmdStart(getCmdArgs(args, i));
                }
            }
            else {
                cmdStartApplication(getCmdArgs(args, i));
            }
        }
        else if (args[i].equals("stop"))
        {
            execCommand = true;
            if (i < args.length - 1) {
                if (args[i + 1].equalsIgnoreCase("app"))
                {
                    cmdStopApplication(getCmdArgs(args, i));
                }
                else
                {
                    cmdStop(getCmdArgs(args, i));
                }
            }
            else {
                cmdStopApplication(getCmdArgs(args, i));
            }
        }
        else if (args[i].equals("restart"))
        {
            execCommand = true;
            cmdRestart(getCmdArgs(args, i));
        }
        else if (args[i].equals("solstart"))
        {
            execCommand = true;
            cmdSolstart(getCmdArgs(args, i));
        }
        else if (args[i].equals("solstop"))
        {
            execCommand = true;
            cmdSolstop(getCmdArgs(args, i));
        }
        else if (args[i].equals("quit"))
        {
            System.exit(EXIT_SUCCESS);
        }
        else if (args[i].equals("q"))
        {
            System.exit(EXIT_SUCCESS);
        }
        else if (args[i].equals("exit"))
        {
            System.exit(EXIT_SUCCESS);
        }
        else
        {
            pw.println("Invalid Command");
            usage();
        }
        return i;
    }

    private static void usage()
    {
        pw.println("");
        pw.println("Usage: cmd -u <user id> -p <password> [<OPTION>] <CMD>");
        pw.println("");
        pw.println("         * No <CMD> specified will enter interactive mode");
        pw.println("");
        pw.println("   OPTION is one of:");
        pw.println("");
        pw.println("       -help ");
        pw.println("         * print this message");
        pw.println("       -a <application name>");
        pw.println("         * The application name that has been registered to eManager");
        pw.println("         * You can specify the application name and instance id seperated");
        pw.println("         * by a period for this parameter.  eg. 'myapp.inst1'");
        pw.println("       -i <instance id>");
        pw.println("         * The application instance ID registered to eManager");
        pw.println("         * If not specified then it defaults to the local hostname");
        pw.println("       -poll <seconds>");
        pw.println("         * run indefinitely, polling for data. Applies only to status CMDs");
        pw.println("");
        pw.println("   CMD is one of:");
        pw.println("");
        pw.println("       health");
        pw.println("         * Checks if all the processes are stable for the specified application");
        pw.println("         * Allowed options for this command : -a, -i");
        pw.println("");
        pw.println("       solhealth <solution name>");
        pw.println("         * Checks if all applications in the solution are stable");
        pw.println("         * Allowed options for this command : NONE");
        pw.println("");
        pw.println("       status");
        pw.println("         * Returns the status of all the processes for the specified application");
        pw.println("         * Allowed options for this command : -a, -i, -poll");
        pw.println("");
        pw.println("       status <process name>");
        pw.println("         * Returns the status of the process specified in the application");
        pw.println("         * Allowed options for this command : -a, -i, -poll");
        pw.println("");
        pw.println("       solstatus <solution name>");
        pw.println("         * Returns the state of each application in the solution");
        pw.println("         * Allowed options for this command : -poll");
        pw.println("");
        pw.println("       grpstatus <group name>");
        pw.println(
            "         * Returns the state of the application group by looking at the state of each process");
        pw.println("         * Allowed options for this command : -a, -i, -poll");
        pw.println("");
        pw.println("       groups");
        pw.println("         * Returns the list of groups defined for the specified application");
        pw.println("         * Allowed options for this command : -a, -i");
        pw.println("");
        pw.println("       group <group name>");
        pw.println("         * Returns the list of process that are part of the group");
        pw.println("         * Allowed options for this command : -a, -i");
        pw.println("");
        pw.println("       start [app | all | <process name> | group <group name>]");
        pw.println("         * app = used to start the application by starting the watchdog for that application");
        pw.println("         * if no argument is specified, starts the application (same as 'start app')");
        pw.println("         * all = used to start all process for application, watchdog must already be running");
        pw.println("         * Allowed options for this command : -a, -i");
        pw.println("");
        pw.println("       stop [app | all | <process name> | group <group name>]");
        pw.println("         * app = used to stop the application by stopping the watchdog for that application");
        pw.println("         * if no argument is specified, stops the application (same as 'stop app')");
        pw.println("         * all = used to stop all process for application, watchdog must already be running");
        pw.println("         * Allowed options for this command : -a, -i");
        pw.println("");
        pw.println("       restart [all | <process name> | group <group name>] ");
        pw.println("         * if no argument is specified, restarts all process for application");
        pw.println("         * Allowed options for this command : -a, -i");
        pw.println("");
        pw.println("       solstart <solution name>");
        pw.println("         * Start all applications that are part of the solution if they are not running");
        pw.println("         * Allowed options for this command : NONE");
        pw.println("");
        pw.println("       solstop <solution name>");
        pw.println("         * Stop all applications that are part of the solution if they are not running");
        pw.println("         * Allowed options for this command : NONE");
        pw.println("");
        pw.println("   Additional interactive Mode Commands: ");
        pw.println("");
        pw.println("       quit     * Will exit interactive mode");
        pw.println("");
        pw.println("       help     * Shows this help");
        pw.println("");
        pw.println("       appname <application name>  * Will change the default application name");
        pw.println("");
        pw.println("       instname <instance id>      * Will change the default instance id");
        pw.println("");
    }

    /**
     * @param inArgs The args parameter to main.
     * @param cmdIndex The index of the command name.
     * @return The arguments that occur after the command name.
     */
    protected static String[] getCmdArgs(String[] inArgs, int cmdIndex)
    {
        String[] outArgs = new String[inArgs.length - 1 - cmdIndex];
        int outIndex = 0;
        int inIndex = cmdIndex + 1;
        while (inIndex < inArgs.length)
        {
            outArgs[outIndex++] = inArgs[inIndex++];
        }
        return outArgs;
    }

    private static void loginNBAPI()
    {

        if (mUserId == null)
        {
            pw.println("The userid was not specified");
            pw.println("Please use the -u <userid> parameter and try again");
            System.exit(EXIT_CONNECT);
        }
        else
        {
            if (mUserId.equals("admin"))
            {
                mAccess = AccessType.WRITE;
            }
            else
            {
                mAccess = AccessType.READ;
            }
        }
        if (mPassword == null)
        {
            pw.println("The password was not specified");
            pw.println("Please use the -p <password> parameter and try again");
            System.exit(EXIT_CONNECT);
        }

    }

    private static void validateApp()
    {
        if (mAppType == null)
        {
            pw.println("The Application Name was not specified");
            pw.println("Please use the -a <application name> parameter and try again");
            System.exit(EXIT_CONNECT);
        }
        if (mAppInst == null)
        {
            InetAddress host = null;
            try
            {
                host = InetAddress.getLocalHost();
                mAppInst = host.getHostName();
            }
            catch (UnknownHostException ex)
            {
                pw.println("Error getting hostname.  Please use -i <instance id> when calling this utility");
            }
        }

    }

    private static void cmdHealth(String[] args)
    {
        validateApp();
        // pw.println("health for " + mAppType + " " + mAppInst);
        try
        {
            boolean health = pmStub.getHealth(mAppType, mAppInst);
            if (health)
            {
                pw.println("Is healthy : true");
            }
            else
            {
                pw.println("Is not healthy : false");
            }
        } /*
        catch (RemoteException ex)
        {
            pw.println("Error Calling eManager: " + ex);
            System.exit(EXIT_CONNECT);
        } */
        catch (Exception e)
        {
            pw.println("Exception when Calling eManager: " + e);
            System.exit(EXIT_CONNECT);
        }
        // boolean getHealth(String applicationType, String applicationInstance)
    }

    private static void cmdSolhealth(String[] args)
    {
        loginNBAPI();
        if (args.length == 1)
        {
            String solutionName = args[0];
            // pw.println("solhealth for " + solutionName);
            try
            {
                boolean health = pmStub.getSolutionHealth(solutionName);
                if (health)
                {
                    pw.println("Is healthy : true");
                }
                else
                {
                    pw.println("Is not healthy : false");
                }
            } /*
            catch (RemoteException ex)
            {
                pw.println("Error Calling eManager: " + ex);
                System.exit(EXIT_CONNECT);
            } */
            catch (Exception e)
            {
                pw.println("Exception when Calling eManager: " + e);
                System.exit(EXIT_CONNECT);
            }
            // boolean getSolutionHealth(String solutionName)
        }
        else
        {
            pw.println("Solution Name not specified");
        }
    }

    private static String padding (int len, int fieldLen) {
        return (len < fieldLen) ? msSpaces.substring (len, fieldLen) : "";
    }

    public static void printProcessInfo(ProcessInfo[] status) {
        for (int i = 0; i < status.length; i++) {
            String generationString = Integer.toString (status[i].getStartGeneration());
            String successfulString =
                Integer.toString (status[i].getSuccessfulHeartbeats());
            String pidString =
                status[i].getProcessId() == 0 ? "" : Long.toString (status[i].getProcessId());
            String missedString =
                Integer.toString (status[i].getMissedHeartbeats());
            String dateString = "";
            Object exTime = status[i].getExecTime();
            if (exTime instanceof Date ) {
                dateString = dateFormat.format(exTime);
            } else if (exTime instanceof Calendar ) {
                dateString = dateFormat.format(((Calendar)exTime).getTime());
            }
            String name  = status[i].getProcessName().trim();
            String state = status[i].getState();
            Boolean isMon = new Boolean(status[i].getIsMonitor());
            String monitor = isMon.toString();

            pw.println (name
                           + padding (name.length(), 20)
                           + " "
                           + state
                           + padding (state.length(), 15)
                           + " "
                           + generationString
                           + padding (generationString.length(), 5)
                           + " "
                           + dateString
                           + padding (dateString.length (), 20)
                           + " "
                           + pidString
                           + padding (pidString.length (), 6)
                           + " "
                           + successfulString
                           + padding (successfulString.length(), 8)
                           + " "
                           + missedString
                           + padding (missedString.length(), 7)
                           + " "
                           + status[i].getHeartbeatResult()
                           + padding (status[i].getHeartbeatResult().length(), 9)
                           + " "
                           + status[i].getUsesNativeLogging()
                           + padding (status[i].getUsesNativeLogging().length(), 5)
                           + " "
                           + monitor);
        }
        pw.flush();
    }

    private static void cmdStatus(String[] args)
    {
        validateApp();
        //pw.println(args.length);
        if (args.length == 1)
        {
            String processName = args[0];
            pw.println("Status for process: " + processName + " in application: " + mAppType + "." + mAppInst);
            pw.println("");
            try
            {
                ProcessInfoRec status = pmStub.getProcessStatusFor(mAppType, mAppInst, processName);
                if (status != null) {
                    pw.println(servStatusHeader);
                    ProcessInfo[] info = new ProcessInfo[1];
                    info[0] = status;
                    printProcessInfo(info);
                }
            } /*
            catch (RemoteException ex)
            {
                pw.println("Error Calling eManager: " + ex);
                System.exit(EXIT_CONNECT);
            } */
            catch (Exception e)
            {
                pw.println("Exception when Calling eManager: " + e);
                System.exit(EXIT_CONNECT);
            }

            // ProcessInfoRec getProcessStatusFor(String applicationType, String applicationInstance, String processName)
            while (true)
            {
                if (pollInterval == -1)
                {
                    break;
                }
                try
                {
                    Thread.sleep(pollInterval);
                }
                catch (InterruptedException ie)
                {
                }
                // pw.println("status for process: " + processName + " " + mAppType + " " + mAppInst);
                try
                {
                    ProcessInfoRec status = pmStub.getProcessStatusFor(mAppType, mAppInst, processName);
                    if (status != null) {
                        ProcessInfo[] info = new ProcessInfo[1];
                        info[0] = status;
                        printProcessInfo(info);
                    }
                } /*
                catch (RemoteException ex)
                {
                    pw.println("Error Calling eManager: " + ex);
                    System.exit(EXIT_CONNECT);
                } */
                catch (Exception e)
                {
                    pw.println("Exception when Calling eManager: " + e);
                    System.exit(EXIT_CONNECT);
                }
                // ProcessInfoRec getProcessStatusFor(String applicationType, String applicationInstance, String processName)
            }
        }
        else if (args.length == 0)
        {
            pw.println("Status for " + mAppType + "." + mAppInst);
            pw.println("");
            try
            {
                ProcessInfoObj[] status = pmStub.getProcessStatus(mAppType, mAppInst);
                if (status != null) {
                    pw.println(servStatusHeader);
                    printProcessInfo(status);
                }
            } /*
            catch (RemoteException ex)
            {
                pw.println("Error Calling eManager: " + ex);
                System.exit(EXIT_CONNECT);
            } */
            catch (Exception e)
            {
                pw.println("Exception when Calling eManager: " + e);
                System.exit(EXIT_CONNECT);
            }
            // ProcessInfo[] getProcessStatus(String applicationType, String applicationInstance)
            while (true)
            {
                if (pollInterval == -1)
                {
                    break;
                }
                try
                {
                    Thread.sleep(pollInterval);
                }
                catch (InterruptedException ie)
                {
                }
                //pw.println("status for " + mAppType + " " + mAppInst);
                try
                {
                    ProcessInfoObj[] status = pmStub.getProcessStatus(mAppType, mAppInst);
                    if (status != null) {
                        printProcessInfo(status);
                    }
                } /*
                catch (RemoteException ex)
                {
                    pw.println("Error Calling eManager: " + ex);
                    System.exit(EXIT_CONNECT);
                } */
                catch (Exception e)
                {
                    pw.println("Exception when Calling eManager: " + e);
                    System.exit(EXIT_CONNECT);
                }
                // ProcessInfo[] getProcessStatus(String applicationType, String applicationInstance)
            }
        }
        else
        {
            pw.println("Invalid number of parameters");
        }
    }

    public static void printSolutionStatus(SolutionStatusInfoObj[] status) {
        for (int i = 0; i < status.length; i++) {
            pw.println (status[i].getAppType()
                           + padding (status[i].getAppType().length(), 20)
                           + " "
                           + status[i].getAppInstance()
                           + padding (status[i].getAppInstance().length(), 15)
                           + " "
                           + status[i].getState());
        }
        pw.flush();
    }

    private static void cmdSolstatus(String[] args)
    {
        //pw.println(args.length);
        if (args.length == 1)
        {
            String solutionName = args[0];
            pw.println("Solution Status for " + solutionName);
            pw.println("");
            try
            {
                SolutionStatusInfoObj[] status = pmStub.getSolutionStatus(solutionName);
                if (status != null) {
                    pw.println(servSolutionHeader);
                    printSolutionStatus(status);
                }
            } /*
            catch (RemoteException ex)
            {
                pw.println("Error Calling eManager: " + ex);
                System.exit(EXIT_CONNECT);
            } */
            catch (Exception e)
            {
                pw.println("Exception when Calling eManager: " + e);
                System.exit(EXIT_CONNECT);
            }
            // SolutionStatusInfo[] getSolutionStatus(String solutionName)
            while (true)
            {
                if (pollInterval == -1)
                {
                    break;
                }
                try
                {
                    Thread.sleep(pollInterval);
                }
                catch (InterruptedException ie)
                {
                }
                try
                {
                    SolutionStatusInfoObj[] status = pmStub.getSolutionStatus(solutionName);
                    if (status != null) {
                        printSolutionStatus(status);
                    }
                } /*
                catch (RemoteException ex)
                {
                    pw.println("Error Calling eManager: " + ex);
                    System.exit(EXIT_CONNECT);
                } */
                catch (Exception e)
                {
                    pw.println("Exception when Calling eManager: " + e);
                    System.exit(EXIT_CONNECT);
                }
                // SolutionStatusInfo[] getSolutionStatus(String solutionName)
            }
        }
        else
        {
            pw.println("Solution Name not specified");
        }
    }

    private static void cmdGrpstatus(String[] args)
    {
        validateApp();
        //pw.println(args.length);
        if (args.length == 1)
        {
            String groupName = args[0];
            // pw.println("grpstatus for " + groupName + " " + mAppType + " " + mAppInst);
            try
            {
                String groupState = pmStub.getGroupState(mAppType, mAppInst, groupName);
                pw.println("Group : " + groupName + " State: " + groupState);

            } /*
            catch (RemoteException ex)
            {
                pw.println("Error Calling eManager: " + ex);
                System.exit(EXIT_CONNECT);
            } */
            catch (Exception e)
            {
                pw.println("Exception when Calling eManager: " + e);
                System.exit(EXIT_CONNECT);
            }
            // String getGroupState(String applicationType, String applicationInstance, String groupName)
            while (true)
            {
                if (pollInterval == -1)
                {
                    break;
                }
                try
                {
                    Thread.sleep(pollInterval);
                }
                catch (InterruptedException ie)
                {
                }
                try
                {
                    String groupState = pmStub.getGroupState(mAppType, mAppInst, groupName);
                    pw.println("Group : " + groupName + " State: " + groupState);

                } /*
                catch (RemoteException ex)
                {
                    pw.println("Error Calling eManager: " + ex);
                    System.exit(EXIT_CONNECT);
                } */
                catch (Exception e)
                {
                    pw.println("Exception when Calling eManager: " + e);
                    System.exit(EXIT_CONNECT);
                }
                // String getGroupState(String applicationType, String applicationInstance, String groupName)
            }
        }
        else
        {
            pw.println("Group Name not specified");
        }
    }

    private static void cmdGroups(String[] args)
    {
        validateApp();

        // pw.println("groups for " + mAppType + " " + mAppInst);
        try
        {
            String[] groupNames = pmStub.getAllGroupNames(mAppType, mAppInst);
            for (int i = 0; i < groupNames.length; i++)
            {
                pw.println(i + ") " + groupNames[i]);
            }
        } /*
        catch (RemoteException ex)
        {
            pw.println("Error Calling eManager: " + ex);
            System.exit(EXIT_CONNECT);
        } */
        catch (Exception e)
        {
            pw.println("Exception when Calling eManager: " + e);
            System.exit(EXIT_CONNECT);
        }
        // String[] getAllGroupNames(String applicationType, String applicationInstance)
    }

    private static void cmdGroup(String[] args)
    {
        validateApp();
        if (args.length == 1)
        {
            String groupName = args[0];
            // pw.println("process for group " + groupName + " " + mAppType + " " + mAppInst);
            try
            {
                String[] processNames = pmStub.getProcessesForGroup(mAppType, mAppInst, groupName);
                for (int i = 0; i < processNames.length; i++)
                {
                    pw.println(i + ") " + processNames[i]);
                }
            } /*
            catch (RemoteException ex)
            {
                pw.println("Error Calling eManager: " + ex);
                System.exit(EXIT_CONNECT);
            } */
            catch (Exception e)
            {
                pw.println("Exception when Calling eManager: " + e);
                System.exit(EXIT_CONNECT);
            }

            // String[] getProcessesForGroup(String applicationType, String applicationInstance, String groupName)
        }
        else
        {
            pw.println("Group Name not specified");
        }
    }

    private static void cmdStart(String[] args)
    {
        loginNBAPI();
        validateApp();

        StringBuffer sb = new StringBuffer();
        //pw.println(args.length);
        for (int i = 0; i < args.length; i++)
        {
            pw.println(args[i]);
            sb.append(args[i]);
            sb.append(" ");
        }
        String command = sb.toString().trim();
        // pw.println("start " + command + " for " + mAppType + " " + mAppInst);
        try
        {
            String result = pmStub.startCommand(mAppType, mAppInst, command, mUserId, mAccess);
            pw.println(result);
        } /*
        catch (RemoteException ex)
        {
            pw.println("Error Calling eManager: " + ex);
            System.exit(EXIT_CONNECT);
        } */
        catch (Exception e)
        {
            pw.println("Exception when Calling eManager: " + e);
            System.exit(EXIT_CONNECT);
        }
        // String startCommand(String applicationType, String applicationInstance, String command)
    }

    private static void cmdStartApplication(String[] args)
    {
        loginNBAPI();
        validateApp();

        try
        {
            pmStub.startApplication(mAppType, mAppInst, mUserId, mAccess);
            pw.println("Successful");
        } /*
        catch (RemoteException ex)
        {
            pw.println("Error Calling eManager: " + ex);
            System.exit(EXIT_CONNECT);
        } */
        catch (Exception e)
        {
            pw.println("Exception when Calling eManager: " + e);
            System.exit(EXIT_CONNECT);
        }
        // startApplication(String applicationType, String applicationInstance)
    }


    private static void cmdStop(String[] args)
    {
        loginNBAPI();
        validateApp();

        StringBuffer sb = new StringBuffer();
        //pw.println(args.length);
        for (int i = 0; i < args.length; i++)
        {
            pw.println(args[i]);
            sb.append(args[i]);
            sb.append(" ");
        }
        String command = sb.toString().trim();
        // pw.println("stop " + command + " for " + mAppType + " " + mAppInst);
        try
        {
            String result = pmStub.stopCommand(mAppType, mAppInst, command, mUserId, mAccess);
            pw.println(result);
        } /*
        catch (RemoteException ex)
        {
            pw.println("Error Calling eManager: " + ex);
            System.exit(EXIT_CONNECT);
        } */
        catch (Exception e)
        {
            pw.println("Exception when Calling eManager: " + e);
            System.exit(EXIT_CONNECT);
        }
        // String stopCommand(String applicationType, String applicationInstance, String command)

    }

    private static void cmdStopApplication(String[] args)
    {
        loginNBAPI();
        validateApp();

        try
        {
            pmStub.stopApplication(mAppType, mAppInst, mUserId, mAccess);
            pw.println("Successful");
        } /*
        catch (RemoteException ex)
        {
            pw.println("Error Calling eManager: " + ex);
            System.exit(EXIT_CONNECT);
        } */
        catch (Exception e)
        {
            pw.println("Exception when Calling eManager: " + e);
            System.exit(EXIT_CONNECT);
        }
        // stopApplication(String applicationType, String applicationInstance)

    }

    private static void cmdRestart(String[] args)
    {
        loginNBAPI();
        validateApp();

        StringBuffer sb = new StringBuffer();
        //pw.println(args.length);
        for (int i = 0; i < args.length; i++)
        {
            pw.println(args[i]);
            sb.append(args[i]);
            sb.append(" ");
        }
        String command = sb.toString().trim();
        // pw.println("restart " + command + " for " + mAppType + " " + mAppInst);
        try
        {
            String result = pmStub.restartCommand(mAppType, mAppInst, command, mUserId, mAccess);
            pw.println(result);
        } /*
        catch (RemoteException ex)
        {
            pw.println("Error Calling eManager: " + ex);
            System.exit(EXIT_CONNECT);
        } */
        catch (Exception e)
        {
            pw.println("Exception when Calling eManager: " + e);
            System.exit(EXIT_CONNECT);
        }
        // String restartCommand(String applicationType, String applicationInstance, String command)

    }

    private static void cmdSolstart(String[] args)
    {
        loginNBAPI();
        if (args.length == 1)
        {
            String solutionName = args[0];
            //pw.println("solstart for " + solutionName);
            try
            {
                pmStub.startSolution(solutionName, mUserId, mAccess);
                pw.println("Successful");
            } /*
            catch (RemoteException ex)
            {
                pw.println("Error Calling eManager: " + ex);
                System.exit(EXIT_CONNECT);
            } */
            catch (Exception e)
            {
                pw.println("Exception when Calling eManager: " + e);
                System.exit(EXIT_CONNECT);
            }
            // void startSolution(String solutionName)
        }
        else
        {
            pw.println("Solution Name not specified");
        }
    }

    private static void cmdSolstop(String[] args)
    {
        loginNBAPI();
        if (args.length == 1)
        {
            String solutionName = args[0];
            //pw.println("solstop for " + solutionName);
            try
            {
                pmStub.stopSolution(solutionName, mUserId, mAccess);
                pw.println("Successful");
            } /*
            catch (RemoteException ex)
            {
                pw.println("Error Calling eManager: " + ex);
                System.exit(EXIT_CONNECT);
            } */
            catch (Exception e)
            {
                pw.println("Exception when Calling eManager: " + e);
                System.exit(EXIT_CONNECT);
            }
            // void stopSolution(String solutionName)
        }
        else
        {
            pw.println("Solution Name not specified");
        }
    }

}

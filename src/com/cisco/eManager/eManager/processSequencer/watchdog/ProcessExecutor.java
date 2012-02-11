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

import com.tibco.tibrv.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import com.cisco.eManager.eManager.processSequencer.common.DCPLib;
import com.cisco.eManager.eManager.processSequencer.common.NoSuchProperty;
import com.cisco.eManager.eManager.processSequencer.common.logging.LogUtil;
import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;
import com.cisco.eManager.eManager.processSequencer.common.UnixStdlib;

import java.util.logging.*;

/**
 * Abstracts a server within the watchdog that manages a Process.
 * This class handles creation of the server process, reading
 * its stdout/stderr (if necessary),
 * waiting for the process to die, restarting it, etc.
 * Concrete classes are derived from this and must implement a
 * heartbeat method to verify that the server is functioning
 * correctly.
 * @see ProcessMonitor
 */
public abstract class ProcessExecutor
	extends Server
{
	/**
	 * This is the ProcessWaitForThread who's sole job is to notice when
	 * the server process dies and communicate that death back to the
	 * state machine.
	 */
	protected ProcessWaitForThread mProcessWaitForThread = null;

	/**
	 * The thread that gathers the output from stdout
	 * and transfers it to the log
	 */
	protected ServerOutputThread mStdout;

	/**
	 * The thread that gathers the output from stderr
	 * and transfers it to the log
	 */
	protected ServerOutputThread mStderr;

	/**
	 * If true, this class has the responsibility of taking the
	 * stdout/stderr output and moving to the log. Otherwise
	 * the server process itself can manage it logging (e.g using
	 * the logging API)
	 */
	protected boolean mNativeLoggingUsed;

	/**
	 * The arguments array to exec the process
	 */
	protected String[] mExecArgs = null;
	/**
	 * This is used in logging
	 */
	protected String mPrintableCommand = "";

	/**
	 * Is this a Java based server
	 */
	protected boolean mIsJavaServer = false;

	/**
	 * ServerCommand
	 */
	private ServerCommand mServerCmd;

	/**
	 * This is the Java debug port. If this is a Java server.
	 * Set the first time the server is debugged.
	 */
	protected String mDebugPort=null;

	/**
	 * This is the OptimizeIt port. If this is a Java server.
	 * Set the first time the server is profiled.
	 */
	protected String mOptitAuditPort = null;

	/**
	 * Handle to the server process
	 */
	protected Process mProcess = null;


	/**
	 * @param name This server's name.
	 */
	public ProcessExecutor(String name) {
		super(name);
		mServerStatus.setMonitor(false);
		mProcessWaitForThread = new ProcessWaitForThread(name);
		mProcessWaitForThread.start();
	}

	/**
	 * Returns true if the Watchdog is responsible for
	 * moving the stdout/stderr output to a log file.
	 */
	protected boolean isNativeLoggingUsed() {
		return mNativeLoggingUsed;
	}

	/**
	 * If param b is set to true, starts the necessary threads
	 * to monitor the process' stdout/stderr and transfer the
	 * contents to a log file.
	 */
	protected void setNativeLoggingUsed(boolean b) {
		if( b ) {

			mServerStatus.setLogNative(shouldIndicateNativeLogging());

			FileHandler fileHandler = null;

			try {
				String logLocation = LogUtil.getLogLocation();

				int logFileSize = -1;
				try {
					logFileSize = DCPLib.getIntProperty("Logging.Servers." + mName + ".logFileSize");
				} catch(NoSuchProperty nsp) {
					logFileSize = DCPLib.getIntProperty("Logging.Defaults.logFileSize",
								CiscoLogger.DEFAULT_LOG_FILE_SIZE);
				}

				int logFileNumber = -1;

	  			try {
					logFileNumber = DCPLib.getIntProperty("Logging.Servers." + mName + ".logFileNumber");
	  			} catch(NoSuchProperty nsp) {
					logFileNumber = DCPLib.getIntProperty("Logging.Defaults.logFileNumber",
											CiscoLogger.DEFAULT_LOG_FILE_NUMBER);
	  			}

                                if (LogUtil.getAppType().equals("unknown")) {
                                    fileHandler = new FileHandler(logLocation + File.separator + getProcessNameForLog(), logFileSize, logFileNumber);
                                }
                                else {
                                    fileHandler = new FileHandler(logLocation + File.separator + LogUtil.getAppType() + "." + LogUtil.getAppInst() + "." + getProcessNameForLog(), logFileSize, logFileNumber);
                                }

				fileHandler.setFormatter( new java.util.logging.SimpleFormatter()
				{
					public synchronized String format(LogRecord record) {
						return formatMessage(record) + "\n";
					}
				});

			} catch(Exception ex) {
				mLogger.severe("Could not set up native handler for external process", ex);
			}

			mStdout = new ServerOutputThread(mName + ".stdout", mLogger, fileHandler);
			mStderr = new ServerOutputThread(mName + ".stderr", mLogger, fileHandler);

			// Get them running
			mStdout.start();
			mStderr.start();
		}

		mNativeLoggingUsed = b;
	}

	/**
	 * If the server is being logged natively, this method
	 * can be used to find out if the ServerStatus should
     * have nativeLogging flag set to true or not. Normally
     * This method doesn't do anything. But if the server
     * generates a normal CiscoLog (along with the native log)
	 * this method can be overridden to indicate that there
	 * is a CiscoLog. This has been introduced to workaround
     * the problem of httpd processes which generate both
     * native logs and Cisco logs.<p>
	 *
	 * This has to work in conjunction with a
	 * overriden getProcessNameForLog
	 *
	 * @see getProcessNameForLog
	 */
	protected boolean shouldIndicateNativeLogging() {
		return true;
	}

	/**
	 * Returns the name of the process. Used for log file name generation.
	 * @return the name of the process. Used for log file name generation.
	 */
	protected String getProcessNameForLog() {
		return mName;
	}


	/**
	 * In this method the required configuration is read
	 * to start the server process and the command to execute it
	 * is built.
	 */
	protected void _prepare()
		throws Exception
	{
		//long l1 = System.currentTimeMillis();
		prepare();
		//System.out.println(mName + " : " +(System.currentTimeMillis() - l1));

	}

	protected void prepare()
		throws Exception
	{
		try {

			List execCommand = buildExecCommand();

			if (execCommand == null) {
				mLogger.severe( "No command property was specified; disabling server");
				synchronized(this) {
					changeState(STATE_DISABLED);
				}
				return;
			} else {
				mExecArgs = (String[]) execCommand.toArray( new String[0]);
				StringBuffer printableCmd = new StringBuffer(4096);
				int index = 0;
				for (; index < mExecArgs.length -1; index++) {
					printableCmd.append(mExecArgs[index]).append(" ");
				}
				printableCmd.append(mExecArgs[index]);
				mPrintableCommand = printableCmd.toString();

				if( useOptimizeIt()) {
					System.err.println(mPrintableCommand);
				}
			}
		} catch (Exception ex) {
			mLogger.severe( "Failed In preparing execargs");
			throw ex;
		}
	}

	/**
	 * Hook method called by the super-class if the server
	 * enters unexpected state. This method destroys the
	 * server process and changes state to disabled.
	 */
	protected void handleUnexpectedState(Boolean stateChanged) {
		if (mProcess != null) {
			try {
				mProcess.exitValue();
				// the process has already exitted, try to restart it
				changeState(STATE_START_DEPENDENCIES);
			}
			catch (IllegalThreadStateException itse) {
				// the process is still running, so kill it
				mProcess.destroy();
			}
		} else {
			setDisabled(true);
			changeState(STATE_DISABLED);
		}
	}

	/**
	 * Retrieves the command from the configuration, that
	 * indicates how to start the server process.
 	 */
	protected String getCommand() {
		return 	getProperty(mPropertyPrefix + ".cmd");
	}

	/**
	 * Looks for properties "watchdog.server.&lt;SERVER_NAME&gt;.java.home" followed by
	 * "watchdog.java.home" in the configuration and returns the first
	 * non-null value. If both are not available, returns the "java.home"
	 * system property.
	 */
	protected String getJavaHome() {
		List queue = new LinkedList();
		queue.add(mPropertyPrefix + ".java.home");
		queue.add(WDConstants.WD_PREFIX + ".java.home");
		String javaHome = getProperty(queue);
		if(javaHome == null) javaHome = System.getProperty("java.home");
		return javaHome;
	}

	/**
	 * Looks for properties "watchdog.server.&lt;SERVER_NAME&gt;.java.class.path" followed by
	 * "watchdog.java.class.path" in the configuration and returns the first
	 * non-null value. If both are not available, returns the "java.class.path"
	 * system property.
	 */
	protected String getJavaClasspath() {
		List queue = new LinkedList();
		queue.add(mPropertyPrefix + ".java.class.path");
		queue.add(WDConstants.WD_PREFIX + ".java.class.path");
		String cpath = getProperty(queue);
		if(cpath == null) {
			cpath = System.getProperty("java.class.path");
		}
		return cpath;
	}

	/**
	 * Looks for properties "watchdog.server.&lt;SERVER_NAME&gt;.java.cp" followed by
	 * "watchdog.java.cp" in the configuration and returns the first
	 * non-null value. If both are null, returns null.
	 */
	protected String getJavaAdditionalClasspath() {
		List queue = new LinkedList();
		queue.add(mPropertyPrefix + ".java.cp");
		queue.add(WDConstants.WD_PREFIX + ".java.cp");
		return getProperty(queue);
	}

	/**
	 * Looks for properties "watchdog.server.&lt;SERVER_NAME&gt;.java.jvm" followed by
	 * "watchdog.java.jvm" in the configuration and returns the first
	 * non-null value. If both are not available, returns the
	 * /bin/java in the directory returned by getJavaHome
	 *
	 * @see ProcessExecutor#getJavaHome
	 */
	protected String getJavaJVM() {
		List queue = new LinkedList();
		queue.add(mPropertyPrefix	+ ".java.jvm");
		queue.add(WDConstants.WD_PREFIX + ".java.jvm");
		String javaVM = getProperty(queue);
		if(javaVM == null) {
			String fileSeparator = System.getProperty("file.separator");
			String javaHome = getJavaHome();
			javaVM = javaHome + fileSeparator + "bin" + fileSeparator + "java";
		}
		return javaVM;
	}

	/**
	 * Looks for properties "watchdog.server.&lt;SERVER_NAME&gt;.java.flags" followed by
	 * "watchdog.java.flags" in the configuration and returns the first
	 * non-null value. If both are not available, returns an
	 * empty list.
	 */
	protected List getJVMFlags() {
		String servJVMFlags = getProperty(mPropertyPrefix + ".java.flags");
		String wdJVMFlags = getProperty(WDConstants.WD_PREFIX + ".java.flags");

		StringBuffer sb = new StringBuffer();
		if( wdJVMFlags != null) sb.append(wdJVMFlags);
		if( servJVMFlags != null) sb.append(servJVMFlags);

		String javaFlags = sb.toString();

		String debugFlags = getJVMDebugFlags();
		if( debugFlags == null) debugFlags = "";

		javaFlags = debugFlags + " " + javaFlags;
		javaFlags = javaFlags.trim();

		if (javaFlags.equals("")) {
			return new LinkedList();
		}

		return tokenizeCommand(javaFlags);
	}


	/**
	 * Return the java debugger flags. If useJDB is true for this server,
	 * then this method updates the debug port in watchdog.java.debug.flags
     * and returns the resultant flags.
	 */
	protected String getJVMDebugFlags() {
		if( useJDB()) {

			String port = getDebugPort();
			System.out.println(mName + " Java debug port is : " + port);
			mLogger.config("Java debug port is : " + port);

			String dflags = getProperty(WDConstants.WD_PREFIX +".java.debug.flags");
			if( dflags == null) return "";
			dflags = WDUtil.sreplace(dflags, "PORT", port);
			return dflags;
		}
		return "";
	}

	/**
	 * If true, run this java server under debugger.
	 * Checks watchdog.server.&lt;SERVER_NAME&gt;.debugOrProfile to see if
	 * it is set to JAVA_DEBUG.
	 */

	protected boolean useJDB() {
		String debOrProfile = getProperty( mPropertyPrefix	+ ".debugOrProfile");
		if( "JAVA_DEBUG".equals(debOrProfile)) return true;
		return false;
	}


	/**
	 * The default number from which the allocation of
	 * optimizeit prot number for the server processes should
	 * start.
	 */
	protected static int msJDBPort = DCPLib.getIntProperty("watchdog.java.debug.startPort", 14000);

	/**
	 * This method finds out the next port on which
	 * Java debuggere can send its debug output.  <p>
	 * If "watchdog.server.&lt;SERVER_NAME&gt;.profilers.OptimizeIt.port is specified
	 * then, the property value is returned as an int. <p>
	 * If not the "watchdog.java.profilers.OptimizeIt.startPort" property
	 * is looked up to find out where the port numbers start for
	 * all the servers (and incremented by one for each server) <p>
	 * If this property is unspecified too, then the default is OPTIMIZEIT_START_PORT.
	 */

	private synchronized static String getJDBPort(String serverPropertyPrefix) {
		String rc = DCPLib.getProperty(serverPropertyPrefix+".java.debug.port", null);
		if( rc != null) return rc;
		return Integer.toString(msJDBPort++);
	}

	protected String getDebugPort() {
		if( mDebugPort == null) {
			mDebugPort = getJDBPort(mPropertyPrefix);
		}
		return mDebugPort;
	}

	/**
	 * Looks for properties "watchdog.server.&lt;SERVER_NAME&gt;.java.vmtype" followed by
	 * "watchdog.java.vmtype" in the configuration and returns the first
	 * non-null value. If both are not available, returns null.
	 */
	protected String getJVMType() {
		List queue = new LinkedList();
		queue.add(mPropertyPrefix + ".java.vmtype");
		queue.add(WDConstants.WD_PREFIX + ".java.vmtype");
		String vmType = getProperty (queue);
		return vmType;
	}

	/**
	 * Looks for property "watchdog.server.&lt;SERVER_NAME&gt;.app_args"
	 * and if it is non-null, tokenizes the args and
	 * returns the list. If it is null, returns an empty list.
	 */
	protected List getAppArgs() {
		String appArgs = getProperty(mPropertyPrefix + ".app_args");
		if (appArgs == null) {
			return new LinkedList();
		}
		return tokenizeCommand(appArgs);
	}

	/**
	 * Looks for properties "watchdog.server.&lt;SERVER_NAME&gt;.useOptimizeIt" followed by
	 * "watchdog.java.useOptimizeIt" in the configuration and returns true
	 * if one of them is true. If not, returns false.
	 */
	protected boolean useOptimizeIt() {
		String optimizeIt = getProperty(mPropertyPrefix + ".debugOrProfile");
		boolean useOptimizeIt = (optimizeIt != null)
				&& ( optimizeIt.equals("OPTIMIZEIT"));
		mLogger.config(mPropertyPrefix + "  using optit " + useOptimizeIt);
		return useOptimizeIt;
	}

	private static int msOptimizeItStartPort = -1;

	/**
	 * The default number from which the allocation of
	 * optimizeit prot number for the server processes should
	 * start.
	 */
	public static final int OPTIMIZEIT_START_PORT = 60000;

	/**
	 * This method finds out the next port on which
	 * OptimizeIt can send its profiler output.  <p>
	 * If "watchdog.server.&lt;SERVER_NAME&gt;.profilers.OptimizeIt.port is specified
	 * then, the property value is returned as an int. <p>
	 * If not the "watchdog.profilers.OptimizeIt.startPort" property
	 * is looked up to find out where the port numbers start for
	 * all the servers (and incremented by one for each server) <p>
	 * If this property is unspecified too, then the default is OPTIMIZEIT_START_PORT.
	 */

	private synchronized static String getOptimizeItAuditPort(String serverPropertyPrefix) {
		String rc = DCPLib.getProperty(serverPropertyPrefix+".profilers.OptimizeIt.port", null);
		if( rc != null) return rc;

		if(msOptimizeItStartPort < 0) {
			rc = DCPLib.getProperty("watchdog.java.profilers.OptimizeIt.startPort", null);
			if (rc == null) msOptimizeItStartPort = OPTIMIZEIT_START_PORT;
			else {
				try {
					msOptimizeItStartPort = Integer.parseInt(rc);
				} catch (NumberFormatException ex) {
					msOptimizeItStartPort = OPTIMIZEIT_START_PORT;
				}
			}
		}
		return Integer.toString(msOptimizeItStartPort++);
	}

	/**
	 * This method should update the ServerCommand "struct" and setup
	 * the necessary values for the server to be executed under
	 * OptimizeIt.
	 */
	protected void updateServerCommandForOptimizeIt(ServerCommand serverCmd) {

		mLogger.config(mPropertyPrefix + " checking for optit");

		String optitPrefix = WDConstants.WD_PREFIX + ".java.profilers.OptimizeIt.";

		String home = DCPLib.getProperty(optitPrefix + "home", null);
		if(home == null) {
			mLogger.warning("OptimizeItHome is not specified. Cannot start OptimizeIt");
			return;
		}

		if( ! (new java.io.File(home)).exists()) {
			mLogger.warning("OptimizeItHome : " + home + " does not exist."
								+ " Cannot start OptimizeIt");
			return;
		}

		String classPath = DCPLib.getProperty(optitPrefix + "addonClassPath", "");
		classPath = WDUtil.sreplace(classPath, "OPTIMIZEIT_HOME", home);
		serverCmd.setClasspath(serverCmd.getClasspath() + File.pathSeparator + classPath);

		String libPath = DCPLib.getProperty(optitPrefix + "addonLibPath", "");
		libPath = WDUtil.sreplace(libPath, "OPTIMIZEIT_HOME", home);

		String sysLibPath = serverCmd.getLibPath();
		if (sysLibPath == null) sysLibPath = System.getProperty("java.library.path", "");
		serverCmd.setLibPath(libPath + File.pathSeparator + sysLibPath);

		String javaArgs = DCPLib.getProperty(optitPrefix + "javaArgs", "");
		javaArgs = WDUtil.sreplace(javaArgs, "OPTIMIZEIT_HOME", home);
		List jvmFlags = serverCmd.getJVMFlags();
		jvmFlags.add(0, javaArgs);

		String profilerClass = DCPLib.getProperty(optitPrefix + "class", "intuitive.audit.Audit");

		if( mOptitAuditPort == null) {
			mOptitAuditPort = getOptimizeItAuditPort(mPropertyPrefix);
		}
		System.out.println("OptimizeIt for " + mName + " is running at " + mOptitAuditPort);
		mLogger.config("OptimizeIt running at " + mOptitAuditPort);

		String optitArgs = DCPLib.getProperty(optitPrefix + "args", "");
		optitArgs = WDUtil.sreplace(optitArgs, "OPTIMIZEIT_HOME", home);
		optitArgs = WDUtil.sreplace(optitArgs, "AUDIT_PORT", mOptitAuditPort);


		String mainClass = serverCmd.getMainClassName();
		List   appArgs   = serverCmd.getAppArgs();

		serverCmd.setMainClassName(profilerClass);

		appArgs.add(0, mainClass);
		appArgs.add(0, optitArgs);
	}

	/*
	 ******************************************************************
	 ******************************************************************
	 ******************************************************************
	 ******************************************************************
	protected boolean useJProbe() {
		List queue = new LinkedList();

		queue.add(mPropertyPrefix + ".JProbe");
		queue.add(WDConstants.WD_PREFIX
				+ ".JProbe");
		String jProbe = getProperty(queue);
		boolean useJProbe =
			jProbe != null
			&& Boolean.valueOf(jProbe).booleanValue();

		return useJProbe;
	}

	protected void updateServerCommandForJProbe(ServerCommand serverCmd){

		// now build up the command line
		List newExecTokens = new LinkedList();

		// Identify any extra arguments to be used for JProbe

		List queue = new LinkedList();

		queue.add(mPropertyPrefix + ".JProbe.args");
		queue.add(WDConstants.WD_PREFIX
				+ ".JProbe.args");

		String jProbeArgs = getProperty(queue);

		// Locate JProbe's installation directory
		queue.clear();
		queue.add(mPropertyPrefix + ".JProbe.home");
		queue.add(WDConstants.WD_PREFIX
				+ ".JProbe.home");
		String jProbeHome = getProperty(queue);
		if(jProbeHome == null) {
			jProbeHome = "/auto/ntglocalsol/java"
			+ "/JProbe-2.5b2/JProbeProfiler25Beta/profiler";
		}

		serverCmd.setJVM(jProbeHome + File.separator + "jplauncher");

		boolean needProfile = true;

		List jProbeArgList = new	LinkedList();
		// Additional OptimizeIt arguments
		boolean addDllPath = true;
		if(jProbeArgs != null) {
			List cmds = tokenizeCommand(jProbeArgs);
			Iterator e = cmds.iterator();
			while(e.hasNext()) {
				String arg = (String) e.next();
				if (arg.startsWith("-jp_function=")) {
					needProfile = false;
				}
				jProbeArgList.add(arg);
			}
		}

		// JProbe needs the following additional arguments
		if (needProfile) {
			jProbeArgList.add("-jp_function=profile");
			jProbeArgList.add("-jp_vm=jdk11");
			jProbeArgList.add("-Djava.compiler=NONE");
		}

		serverCmd.setJVMFlags(jProbeArgList);

		serverCmd.setSpecialHandling(true);
	}
	******************************************************************
	******************************************************************
	******************************************************************
	******************************************************************
	*************************************************************************/

	protected void updateServerCommandForSpecialHandling(ServerCommand args) {
		/**
		if(useJProbe()) {
			updateServerCommandForJProbe(args);
		} else if(useOptimizeIt()) {
			updateServerCommandForOptimizeIt(args);
		}
		**/
		if(useOptimizeIt()) {
			updateServerCommandForOptimizeIt(args);
		}
	}

	/**
	 * This method should setup the flags/switches for the
	 * java command which will determine the actual command
	 * to be executed. <p>
	 * Sets up flags like :
	 * <ul>
	 * <li> -Dem.home </li>
	 * <li> -Dprocess.name </li>
	 * </ul> <br>
 	 * add computes the classpath.
	 */
	protected void updateFlags(ServerCommand serverCmd) {

		String bootDir = System.getProperty("em.home");
                String appType = System.getProperty("appType");
                String appInst = System.getProperty("appInst");

		String javaClasspath = serverCmd.getClasspath();
		String processName = getProcessNameForLog();

		List jvmFlags= serverCmd.getJVMFlags();

		int cpToken = -1;

		for (int i = 0; i < jvmFlags.size(); i++) {
			String token = (String) jvmFlags.get(i);
			if (token.startsWith("-classpath")) {
				cpToken = i;
				javaClasspath = null;
			} else if(token.startsWith("-Dem.home")) {
				bootDir = null;
			} else if (token.startsWith("-Dprocess.name")) {
				processName = null;
			}
		}

		List addonJVMFlags = new LinkedList();

		if (bootDir != null) {
			addonJVMFlags.add("-Dem.home=" + bootDir);
		}

		if(processName != null) {
			addonJVMFlags.add("-Dprocess.name=" + processName);
		}

                if(!(appType == null || appType.trim().equals(""))) {
                    addonJVMFlags.add("-DappType=" + appType);
                }

                if(!(appInst == null || appInst.trim().equals(""))) {
                    addonJVMFlags.add("-DappInst=" + appInst);
                }


		if( cpToken != -1) {
			jvmFlags.remove(cpToken);
			String str = (String) jvmFlags.remove(cpToken);
			serverCmd.setClasspath(str);
		}

		jvmFlags.addAll(addonJVMFlags);
	}

	/**
	 * Tokenizes a command string into a list.
	 * Returns an empty list if cmd cannot be tokenized.
	 */
	public static LinkedList tokenizeCommand(String cmd){
		try {
			LinkedList tmp = _tokenizeCommand(cmd);
			return tmp;
		} catch (Exception ex) {
			return new LinkedList();
		}
	}

	/**
	 * Tokenizes a command string into a list.
	 * @throws Exception if the command cannot be tokenized
	 */
	protected static LinkedList _tokenizeCommand(String cmd) throws Exception {

		LinkedList tokens = new LinkedList();
		int startIndex = 0;
		int dQuoteAt = cmd.indexOf('"');
		int sQuoteAt = cmd.indexOf('\'');

		if(dQuoteAt == -1 && sQuoteAt == -1) {
			StringTokenizer st = new StringTokenizer(cmd.trim());
			while(st.hasMoreTokens()) {
				tokens.add(st.nextToken());
			}
			return tokens;
		}

		char[] chArray = cmd.trim().toCharArray();

		int endIndex = 0;
		boolean inQuotes = false;
		char c = 0;
		char lastc = 0;
		char lastqc = 0;
		StringBuffer sb = new StringBuffer(80);

		while (endIndex < chArray.length)
		{
			c = chArray[endIndex];
			if(!Character.isWhitespace(c)) {
				if (c == '"' || c == '\'') {
					if(inQuotes && lastc != '\\' && lastqc == c) {
						tokens.add(sb.toString());
						inQuotes = false;
						sb.setLength(0);
					} else if (!inQuotes){
						inQuotes = true;
						lastqc = c;
					} else {
						sb.append(c);
					}
				} else if (c == '\\') {
					if(lastc == '\\') sb.append(c);
				} else {
					sb.append(c);
				}
			} else {
				 if(inQuotes) {
					sb.append(c);
				 } else {
					if(sb.length() > 0) {
						tokens.add(sb.toString());
						sb.setLength(0);
					}
				 }
			}
			lastc = c;
			++endIndex;
		}

		if(inQuotes) {
			throw new Exception(WDExUtil.formatMessage(WDExConstants.UNTERMINATED_STRING,
											WDUtil.toArray(cmd)));
		}
		return tokens;
	}

	/**
	 * Look at the various property settings and create the command line
	 * for this server.
	 *
	 * @return the command line for this server.
	 */
	protected List buildExecCommand() {

		try {
			String cmd = getCommand();
			if (cmd == null) {
				return null;
			}
			mLogger.finest( LogUtil.splitLine(cmd));
			// Get java specific properties
			LinkedList execTokens = tokenizeCommand(cmd);

			String firstToken = (String) execTokens.get(0);

			if (firstToken.equals("java")) {	// This is a java command, so rework it

				if( execTokens.size() < 2 ) {
					return null;
				}

				mIsJavaServer = true;

				List props = new LinkedList();
				props.add (mPropertyPrefix + ".nativeLogging");
				props.add(WDConstants.WD_PREFIX+ ".nativeLogging");
				String nativeLog = getProperty(props);
				if( nativeLog == null || nativeLog.equals("false")) {
					setNativeLoggingUsed(false);
				} else {
					setNativeLoggingUsed(true);
				}

				String mainClassName = (String) execTokens.getLast();

				List cmdLineFlags = null;
				if( execTokens.size() > 2) {
					cmdLineFlags = execTokens.subList(1, execTokens.size()-1);
				}

				String javaClasspath = getJavaClasspath();
				String addCp = getJavaAdditionalClasspath();
				if(addCp != null) {
					javaClasspath += File.pathSeparator + addCp;
				}
				String jvm = getJavaJVM();
				String jvmType = getJVMType();
				List jvmFlags = getJVMFlags();
				List appArgs = getAppArgs();

				mServerCmd = new ServerCommand(jvm, jvmType, jvmFlags,
									javaClasspath, mainClassName, cmdLineFlags, appArgs);

				updateFlags(mServerCmd);
				updateServerCommandForSpecialHandling(mServerCmd);

				execTokens = mServerCmd.getTokens();
				mLogger.finest(LogUtil.splitLine( execTokens.toString()));
			} else {
				setNativeLoggingUsed(true);
			}
			return execTokens;
		}	catch (Exception e) {
			mLogger.severe( "Failed to buildExecCmd", e);
			return null;
		}
	}

	/**
	 * Stop this server hard. Destroy the server process.
	 */
	protected void stopServerHard() {
		if (mProcess != null) {
                        String propertyName = getPropertyPrefix () + ".shutdown.cmd";
                        String shutdownCmd = DCPLib.getProperty (propertyName, null);
                        if ( shutdownCmd != null ) {
                            mLogger.finer(propertyName + " " + shutdownCmd);
                            ExecuteShutdownCmdThread script = new ExecuteShutdownCmdThread(shutdownCmd);
                            script.start();
                        }

			mLogger.finer( "Destroying server process using destory()");
			mProcess.destroy();
			mLogger.finer( "Destroyed server process");
			mProcess = null;
			mPid = 0;
		}
	}

	/**
	 * Exec the server process, notify mProcessWaitForThread (so that it will
	 * notice when the process dies) and enable the heartbeat thread.
	 */
	protected void startExecution()
	{
		customStartup();

		if( mGeneration > 0 && mIsJavaServer ) {
			try {
				prepare();
			} catch (Exception ex ) {
            	mLogger.severe("Improper configuration for server", ex);
				synchronized(this) {
					mDisabled = true;
					changeState(STATE_DISABLED);
				}
            	return;
			}
		}

		try {
			String runSeparator = "-------------------- Generation "
				+ (mGeneration + 1)
				+ " --------------------";

			mLogger.finest( runSeparator );
			mLogger.finest( LogUtil.splitLine(mPrintableCommand));
			mProcess = Runtime.getRuntime().exec(mPrintableCommand);
			new GetPidThread("server." + getName() + ".getpid").start();
			mExecTime = System.currentTimeMillis();
			mGeneration++;
			updateServerStatus();

			mLogger.finer( "Spawned process");

			if( isNativeLoggingUsed() ) {
				mStderr.setStream(mProcess.getErrorStream());
				mStdout.setStream(mProcess.getInputStream());
			}


			// Wake up the thread that waits for a process to die
			synchronized(mProcessWaitForThread) {
				mProcessWaitForThread.notifyAll();
			}
		}
		catch (IOException ioe) {
			mLogger.severe( "Failure in starting server; Generation : mGeneration", ioe);
			synchronized(this) {
				mDisabled = true;
				changeState(STATE_DISABLED);
			}
		}
	}

	/**
	 * This thread is responsible for detecting the death of the
	 * external server processes and communicating that death back to
	 * the ProcessExecutor class.
	 */
	class ProcessWaitForThread extends Thread {
		int mNoticedGeneration = 0;

		/**
		 * Create a new ProcessWaitForThread.
		 */
		public ProcessWaitForThread(String name) {
			super( name + "wait");
			setDaemon(true);
		}

		public void run() {
			while(true) {
				synchronized(this) {
					while(mNoticedGeneration == mGeneration) {
						try {
							mLogger.finer( "ProcessWaitForThread is waiting");
							wait();
						}
						catch (InterruptedException ie) {
							// ignore
						}
					}
					mNoticedGeneration = mGeneration;
				}

				while(true) {
					try {
						mLogger.finer( "Calling Process.waitFor");
						int exitStatus = mProcess.waitFor();
						mLogger.severe( "Server exited with status " + exitStatus);
						synchronized(ProcessExecutor.this) {
							changeState(STATE_STOPPED);
						}
						break;
					}	catch (InterruptedException ie) {
						mLogger.warning( "Interrupted while waiting for server to exit");
					}
				}
			}
		}
	}

	/**
	 * This thread instance is created and run to obtain the
	 * process id of the server process started. Uses native
	 * code in UnixStdlib (which navigates and reads files in
	 * the /proc filesystem to obtain the process id)
	 */
	class GetPidThread extends Thread {
		public GetPidThread(String name) {
			super(name);
		}

		public void run() {
			int i;
			String cmdToLookFor = getServerProperty("pidHint");
			if (cmdToLookFor == null) {
				StringBuffer buf = new StringBuffer();
				for (i = 0; i < mExecArgs.length; i++) {
					if (i > 0) {
						buf.append(" ");
					}
					buf.append(mExecArgs[i]);
				}
				cmdToLookFor = buf.toString();
			}

                        mLogger.finer("Looking for pid for command: " + cmdToLookFor);

			i = 0;
			do {
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException ie) { }
				mPid = UnixStdlib.getProcessID(cmdToLookFor);
			} while (mPid == 0 && ++i < 300); // up to 5 minutes

			if (mPid == 0) {
				mLogger.warning( "Giving up on trying to identify pid");
			} else {
				mLogger.finer( "Serverpid server=" + mName + " pid=" + mPid);
			}
		}
	}

	/**
	 * Class that encapsulates all the information needed to
	 * build the command for running a server program implemented in Java.
	 */
	static class ServerCommand {
		private String mJVM ="";
		private String mVmType;
		private List mJVMFlags;
		private String mClasspath="";
		private String mLibPath=null;
		private String mMainClassName="";
		private List mAppArgs;
		private boolean mSpecialHandling;

		private List mCmdLineFlags;

		/**
		 * @param jvm path to java executable
		 * @param vmType -server or -client or null
		 * @param jvmFlags -D settings and other java specfic flags
		 * @param classPath classpath
		 * @param mainClassName name of the class that serves as the entry point for the server
		 * @param appArgs list of arguments to the server program
		 */
		ServerCommand(String jvm, String vmType,
								List jvmFlags, String classpath,
								String mainClassName, List cmdLineFlags,
								List appArgs)
		{
			mJVM = jvm;
			mVmType = vmType;
			mJVMFlags = jvmFlags;
			mClasspath = classpath;
			mMainClassName = mainClassName;
			mCmdLineFlags = cmdLineFlags;
			mAppArgs = appArgs;
		}

		void setJVM(String jvm) { mJVM = jvm; }
		String getJVM() { return mJVM; }

		void setVmType(String type) { mVmType = type; }
		String getVmType() { return mVmType; }

		void setSpecialHandling(boolean b) { mSpecialHandling = b; }
		boolean isSpecialHandling() { return mSpecialHandling; }

		void setMainClassName(String s) { mMainClassName = s; }
		String getMainClassName() { return mMainClassName; }

		void setAppArgs(List flags) { mAppArgs = flags; }
		List getAppArgs() { return mAppArgs; }

		void setJVMFlags(List flags) { mJVMFlags = flags; }
		List getJVMFlags() { return mJVMFlags; }

		String getClasspath() { return mClasspath;}
		void setClasspath(String str) { mClasspath = str; }

		String getLibPath() { return mLibPath;}
		void setLibPath(String str) { mLibPath = str; }

		/**
 	 	 * Converts the state of this object into a
 		 * list of tokens, which when combined give the
		 * command to execute for starting the server program.
		 */
		LinkedList getTokens() {
			LinkedList tokens = new LinkedList();
			tokens.add(mJVM);
			if( mCmdLineFlags != null ) tokens.addAll(mCmdLineFlags);
			if(mVmType != null) tokens.add(mVmType);
			if(mJVMFlags != null) tokens.addAll(mJVMFlags);
			if( mLibPath != null ) {
				tokens.add("-Djava.library.path=" + mLibPath);
			}
			tokens.add("-classpath");
			tokens.add(mClasspath);
			tokens.add(mMainClassName);
			if(mAppArgs != null) tokens.addAll(mAppArgs);
			return tokens;
		}
	}
}


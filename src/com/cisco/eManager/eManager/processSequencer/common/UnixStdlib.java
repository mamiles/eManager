package com.cisco.eManager.eManager.processSequencer.common;

/**
 * <p>Title: Process Sequencer / Watchdog</p>
 * <p>Description: eManger</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems</p>
 * @author Marvin Miles
 * @version 1.0
 */

/**
 * An interface to the library the useful native
 * capabilities on Unix.
 */

public class UnixStdlib {
  static {
    System.loadLibrary("StdlibJNI");
  }

  public static final int SIGTERM = 15;
  public static final int SIGUSR1 = 16;
  public static final int SIGUSR2 = 17;

  /**
   *This sleeps until a signal (SIGHUP, SIGINT, ...) is received.
   *@return the signal that was received.
   */
  public static native int waitForSignal(int[] signals);

  /**
   * Kills the process (pid) by issuing a SIGKILL.
   * Makes sure that all the child process of the target process
   * are killed before killing the main process.
   *@param pid the process id of the process to be killed
   */
  public static native void nativeKill(long pid);

  /**
   *Get the process id for a subprocess of the watchdog.
   *
   *@param cmdline The command line for the process to identify.
   *@return The process id.	A Java long is used because UNIX process
   *	 ids are a 32 bit unsigned value.
   **/
  public static native long getProcessID(String cmdline);

}
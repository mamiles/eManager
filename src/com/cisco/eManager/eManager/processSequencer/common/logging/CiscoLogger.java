/******************************************************************************
 * CiscoLogger.java: This class provides the logging interface for all
 * Watchdog / Process Sequencer java components. If the logging for a component has to be
 * controlled, it has to have the "logLevel" property defined in DCPL.
 *****************************************************************************/

package com.cisco.eManager.eManager.processSequencer.common.logging;


import java.io.File;
import java.util.*;
import java.util.logging.*;

import com.cisco.eManager.eManager.processSequencer.common.DCPCallback;
import com.cisco.eManager.eManager.processSequencer.common.DCPLib;
import com.cisco.eManager.eManager.processSequencer.common.NoSuchProperty;
import com.cisco.eManager.eManager.processSequencer.common.Util;

/**
 * A CiscoLogger object is used to log messages for a specific
 * component or subcomponent.  CiscoLoggers are named
 * using the hierarchical dot-separated namespace of DCPL.
 * <p>
 * CiscoLogger objects may be obtained by calls on one of the getCiscoLogger
 * factory methods.  These will either create a new CiscoLogger or
 * return a suitable existing CiscoLogger.
 * <p>
 * Logging messages will be recorded in the log files. The default
 * values for the location,
 * size, and number of these log files for each process can be
 * configured using "loggingDirectory", "logFileSize", and
 * "LogFileNumber" properties in the "Logging.Defaults" component in DCPL. If a
 * process wants to override these default values, it can define
 * it's own values under it's process name in "Logging.Servers" in DCPL.
 * A process can
 * also provide a custom "Handler" class using the property "logHandler".
 * <p>
 * On each logging call the CiscoLogger initially performs a cheap
 * check of the request level (e.g. SEVERE or FINE) against a
 * log level maintained by the logger.  If the request level is
 * lower than the log level, the logging call returns immediately.
 * SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST
 * <p>
 * The log level is originally initialized based on the "logLevel"
 * property of the component. Later on the log level can be changed
 * on the fly using DCPL and it will automatically take affect for
 * that component.
 * <p>
 * By default the log file is created in XML format.
 * <p>
 * There are various logging convenience methods:
 * <p>
 * When the log method is called, the Logging framework will make
 * a "best effort" to determine which class and method called into
 * the logging method.
 * <P>
 * All methods on Logger are multi-thread safe.
 *
 * @author khari@cisco.com
 */

public class CiscoLogger extends Logger implements LoggerInterface {

  /**
   * Find or create a CiscoLogger for a named component.  If a logger has
   * already been created for the given component it is returned.  Otherwise
   * a new logger is created.
   * <p>
   * If a new logger is created its log level will be configured
   * based on the "logLevel" property and it will configured
   * to send logging output to log files named after the base component.
   * This new logger will also respond to the log level changes using
   * the DCPL mechanism.
   *
   * @param	name		A name for the logger.  This should
   *				be a dot-separated name and should  name
   *				of the component in DCPL, such as "Scheduler"
   * @return a suitable CiscoLogger
   */
  public static CiscoLogger getCiscoLogger(String name) {
    return getCiscoLogger(name, null, false);
  }


  public static CiscoLogger getCiscoLogger(String name,
                                           boolean disableTaskLogs) {
    return getCiscoLogger(name, null, disableTaskLogs);
  }



  /**
   * Find or create a CiscoLogger for a named component.  If a logger has
   * already been created for the given component it is returned.  Otherwise
   * a new logger is created.
   * <p>
   * If a new logger is created its log level will be configured
   * based on the "logLevel" property and it will configured
   * to send logging output to log files named after the base component.
   * This new logger will also respond to the log level changes using
   * the DCPL mechanism.
   *
   * @param	name		A name for the logger.  This should
   *				be a dot-separated name and should  name
   *				of the component in DCPL, such as "Scheduler"
   * @param	rb		Name of the resource bundle
   *
   * @return a suitable CiscoLogger
   */
  public static CiscoLogger getCiscoLogger(String name, String rb) {
    return getCiscoLogger(name, rb, false);
  }


  public synchronized static CiscoLogger getCiscoLogger(String name, String rb,
                                           boolean disableTaskLogs) {
    if (name.equals("")) {
      throw new IllegalArgumentException("need a logger name");
    }


    Logger result = lm.getLogger(name);

    if (result == null || !(result instanceof CiscoLogger)) {
      result = new CiscoLogger(name, rb, disableTaskLogs);
      result.setUseParentHandlers(false);

      Level level = CiscoLogger.getComponentLogLevel(name);

      Handler handler = getComponentHandler(name);

      DCPLib.registerComponent(name, CiscoLogger.cb);
      lm.addLogger(result);

      /*
       * Previously we used to call this method on the LogManager. However,
       * the public LogManager API does not seem to have this anymore. So,
       * we are calling this method on the Logger now. Have to verify the
       * behaviour w.r.t the log levels of the child loggers.
       */
      result.setLevel(level);

      /*
       * Remove any handlers present
       */
      Handler[] handlers = result.getHandlers();
      for (int i = 0 ; i < handlers.length ; i++) {
        result.removeHandler(handlers[i]);
      }

      result.addHandler(handler);
    }

    return (CiscoLogger)result;
  }


  /**
   * Log the location of another sub-log.
   * <p>
   * This method adds the location of another log in the log file.
   * To make sure that we always log this location, it adds the location
   * at OFF level.
   * <p>
   * @param   name	Name of this sub-log
   * @param   location	Location of this sub-log
   */
  public void subLog(String name, String location) {
    String msg = "<sublog><name>" + name + "</name><location>" +
      location + "</location></sublog>";

    log(Level.OFF, msg);
  }


  /**
   * Log a SEVERE message.
   * <p>
   * If the logger is currently enabled for the SEVERE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void severe(String msg) {
    super.severe(msg);
  }

  /**
   * Log an exception at severe level
   * <p>
   * If the logger is currently enabled for the SEVERE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   thrown    Exception to be logged
   */
  public void severe(String msg, Throwable thrown) {
    log(Level.SEVERE, msg, thrown);
  }


  /**
   * Log a SEVERE message with a parameter
   * <p>
   * If the logger is currently enabled for the SEVERE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj       Parameter to the message
   */
  public void severe(String msg, Object obj) {
    log(Level.SEVERE, msg, new Object[]{obj});
  }


  /**
   * Log a SEVERE message with two parameters
   * <p>
   * If the logger is currently enabled for the SEVERE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   */
  public void severe(String msg, Object obj1, Object obj2) {
    log(Level.SEVERE, msg, new Object[]{obj1, obj2});
  }


  /**
   * Log a SEVERE message with three parameters
   * <p>
   * If the logger is currently enabled for the SEVERE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   * @param   obj3       Third parameter to the message
   */
  public void severe(String msg, Object obj1, Object obj2, Object obj3) {
    log(Level.SEVERE, msg, new Object[]{obj1, obj2, obj3});
  }

  /**
   * Log a SEVERE message with an array of parameters
   * <p>
   * If the logger is currently enabled for the SEVERE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   objs      Array of parameters to message
   */
  public void severe(String msg, Object objs[]) {
    log(Level.SEVERE, msg, objs);
  }


  /**
   * Log a WARNING message.
   * <p>
   * If the logger is currently enabled for the WARNING message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void warning(String msg) {
    super.warning(msg);
  }


  /**
   * Log an exception at warning level
   * <p>
   * If the logger is currently enabled for the WARNING message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   thrown    Exception to be logged
   */
  public void warning(String msg, Throwable thrown) {
    log(Level.WARNING, msg, thrown);
  }


  /**
   * Log a WARNING message with a parameter
   * <p>
   * If the logger is currently enabled for the WARNING message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj       Parameter to the message
   */
  public void warning(String msg, Object obj) {
    log(Level.WARNING, msg, new Object[]{obj});
  }


  /**
   * Log a WARNING message with two parameters
   * <p>
   * If the logger is currently enabled for the WARNING message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   */
  public void warning(String msg, Object obj1, Object obj2) {
    log(Level.WARNING, msg, new Object[]{obj1, obj2});
  }


  /**
   * Log a WARNING message with three parameters
   * <p>
   * If the logger is currently enabled for the WARNING message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   * @param   obj3       Third parameter to the message
   */
  public void warning(String msg, Object obj1, Object obj2, Object obj3) {
    log(Level.WARNING, msg, new Object[]{obj1, obj2, obj3});
  }

  /**
   * Log a WARNING message with an array of parameters
   * <p>
   * If the logger is currently enabled for the WARNING message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   objs      Array of parameters to message
   */
  public void warning(String msg, Object objs[]) {
    log(Level.WARNING, msg, objs);
  }


  /**
   * Log an INFO message.
   * <p>
   * If the logger is currently enabled for the INFO message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void info(String msg) {
    super.info(msg);
  }

  /**
   * Log an exception at info level
   * <p>
   * If the logger is currently enabled for the INFO message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   thrown    Exception to be logged
   */
  public void info(String msg, Throwable thrown) {
    log(Level.INFO, msg, thrown);
  }


  /**
   * Log a INFO message with a parameter
   * <p>
   * If the logger is currently enabled for the INFO message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj       Parameter to the message
   */
  public void info(String msg, Object obj) {
    log(Level.INFO, msg, new Object[]{obj});
  }


  /**
   * Log a INFO message with two parameters
   * <p>
   * If the logger is currently enabled for the INFO message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   */
  public void info(String msg, Object obj1, Object obj2) {
    log(Level.INFO, msg, new Object[]{obj1, obj2});
  }


  /**
   * Log a INFO message with three parameters
   * <p>
   * If the logger is currently enabled for the INFO message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   * @param   obj3       Third parameter to the message
   */
  public void info(String msg, Object obj1, Object obj2, Object obj3) {
    log(Level.INFO, msg, new Object[]{obj1, obj2, obj3});
  }

  /**
   * Log a INFO message with an array of parameters
   * <p>
   * If the logger is currently enabled for the INFO message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   objs      Array of parameters to message
   */
  public void info(String msg, Object objs[]) {
    log(Level.INFO, msg, objs);
  }


  /**
   * Log a CONFIG message.
   * <p>
   * If the logger is currently enabled for the CONFIG message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void config(String msg) {
    super.config(msg);
  }


  /**
   * Log an exception at config level
   * <p>
   * If the logger is currently enabled for the CONFIG message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   thrown    Exception to be logged
   */
  public void config(String msg, Throwable thrown) {
    log(Level.CONFIG, msg, thrown);
  }


  /**
   * Log a CONFIG message with a parameter
   * <p>
   * If the logger is currently enabled for the CONFIG message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj       Parameter to the message
   */
  public void config(String msg, Object obj) {
    log(Level.CONFIG, msg, new Object[]{obj});
  }


  /**
   * Log a CONFIG message with two parameters
   * <p>
   * If the logger is currently enabled for the CONFIG message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   */
  public void config(String msg, Object obj1, Object obj2) {
    log(Level.CONFIG, msg, new Object[]{obj1, obj2});
  }


  /**
   * Log a CONFIG message with three parameters
   * <p>
   * If the logger is currently enabled for the CONFIG message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   * @param   obj3       Third parameter to the message
   */
  public void config(String msg, Object obj1, Object obj2, Object obj3) {
    log(Level.CONFIG, msg, new Object[]{obj1, obj2, obj3});
  }

  /**
   * Log a CONFIG message with an array of parameters
   * <p>
   * If the logger is currently enabled for the CONFIG message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   objs      Array of parameters to message
   */
  public void config(String msg, Object objs[]) {
    log(Level.CONFIG, msg, objs);
  }


  /**
   * Log a FINE message.
   * <p>
   * If the logger is currently enabled for the CONFIG message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void fine(String msg) {
    super.fine(msg);
  }

  /**
   * Log an exception at fine level
   * <p>
   * If the logger is currently enabled for the FINE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   thrown    Exception to be logged
   */
  public void fine(String msg, Throwable thrown) {
    log(Level.FINE, msg, thrown);
  }


  /**
   * Log a FINE message with a parameter
   * <p>
   * If the logger is currently enabled for the FINE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj       Parameter to the message
   */
  public void fine(String msg, Object obj) {
    log(Level.FINE, msg, new Object[]{obj});
  }


  /**
   * Log a FINE message with two parameters
   * <p>
   * If the logger is currently enabled for the FINE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   */
  public void fine(String msg, Object obj1, Object obj2) {
    log(Level.FINE, msg, new Object[]{obj1, obj2});
  }


  /**
   * Log a FINE message with three parameters
   * <p>
   * If the logger is currently enabled for the FINE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   * @param   obj3       Third parameter to the message
   */
  public void fine(String msg, Object obj1, Object obj2, Object obj3) {
    log(Level.FINE, msg, new Object[]{obj1, obj2, obj3});
  }

  /**
   * Log a FINE message with an array of parameters
   * <p>
   * If the logger is currently enabled for the FINE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   objs      Array of parameters to message
   */
  public void fine(String msg, Object objs[]) {
    log(Level.FINE, msg, objs);
  }


  /**
   * Log a FINER message.
   * <p>
   * If the logger is currently enabled for the FINER message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void finer(String msg) {
    super.finer(msg);
  }


  /**
   * Log an exception at finer level
   * <p>
   * If the logger is currently enabled for the FINER message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   thrown    Exception to be logged
   */
  public void finer(String msg, Throwable thrown) {
    log(Level.FINER, msg, thrown);
  }


  /**
   * Log a FINER message with a parameter
   * <p>
   * If the logger is currently enabled for the FINER message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj       Parameter to the message
   */
  public void finer(String msg, Object obj) {
    log(Level.FINER, msg, new Object[]{obj});
  }


  /**
   * Log a FINER message with two parameters
   * <p>
   * If the logger is currently enabled for the FINER message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   */
  public void finer(String msg, Object obj1, Object obj2) {
    log(Level.FINER, msg, new Object[]{obj1, obj2});
  }


  /**
   * Log a FINER message with three parameters
   * <p>
   * If the logger is currently enabled for the FINER message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   * @param   obj3       Third parameter to the message
   */
  public void finer(String msg, Object obj1, Object obj2, Object obj3) {
    log(Level.FINER, msg, new Object[]{obj1, obj2, obj3});
  }

  /**
   * Log a FINER message with an array of parameters
   * <p>
   * If the logger is currently enabled for the FINER message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   objs      Array of parameters to message
   */
  public void finer(String msg, Object objs[]) {
    log(Level.FINER, msg, objs);
  }


  /**
   * Log a FINEST message.
   * <p>
   * If the logger is currently enabled for the FINEST message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void finest(String msg) {
    super.finest(msg);
  }

  /**
   * Log an exception at finest level
   * <p>
   * If the logger is currently enabled for the FINEST message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   thrown    Exception to be logged
   */
  public void finest(String msg, Throwable thrown) {
    log(Level.FINEST, msg, thrown);
  }


  /**
   * Log a FINEST message with a parameter
   * <p>
   * If the logger is currently enabled for the FINEST message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj       Parameter to the message
   */
  public void finest(String msg, Object obj) {
    log(Level.FINEST, msg, new Object[]{obj});
  }


  /**
   * Log a FINEST message with two parameters
   * <p>
   * If the logger is currently enabled for the FINEST message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   */
  public void finest(String msg, Object obj1, Object obj2) {
    log(Level.FINEST, msg, new Object[]{obj1, obj2});
  }


  /**
   * Log a FINEST message with three parameters
   * <p>
   * If the logger is currently enabled for the FINEST message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   obj1       First parameter to the message
   * @param   obj2       Second parameter to the message
   * @param   obj3       Third parameter to the message
   */
  public void finest(String msg, Object obj1, Object obj2, Object obj3) {
    log(Level.FINEST, msg, new Object[]{obj1, obj2, obj3});
  }

  /**
   * Log a FINEST message with an array of parameters
   * <p>
   * If the logger is currently enabled for the FINESTmessage
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   *
   * @param   objs      Array of parameters to message
   */
  public void finest(String msg, Object objs[]) {
    log(Level.FINEST, msg, objs);
  }

  /**
   * Prefix all messages logged by this logger with the given prefix
   *
   * @param   prefix	The prefix
   */
  public void setMsgPrefix(String prefix) {
  }


  /**
   * Returns the prefix currently in use by this logger.
   *
   * @return prefix used by this logger
   */
  public String getMsgPrefix() {
    return "";
  }

  /**
   * Log a message, with no arguments.
   * <p>
   * If the logger is currently enabled for the given message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param	level	One of the message level identifiers, e.g. SEVERE
   * @param   msg	The string message (or a key in the message catalog)
   */
  public void log(Level level, String msg) {
    /*
     * 06/06/2002 bcalmac
     * During a shutdown hook the logger is invalid. Redirect to System.out.
     */
    if (getHandlers().length == 0) {
      dumpMessage(msg, null);
    /*
     * 06/06/2002 bcalmac
     * Fixed NullPointerException when getLevel() returns null.
     */
    } else if (isLoggable(level)) {
      LogRecord lr = new LogRecord(level, msg);
      inferCaller(lr);
      log(lr);
    }
  }

  /**
   * Log a message, with an array of object arguments.
   * <p>
   * If the logger is currently enabled for the given message
   * level then a corresponding LogRecord is created and forwarded
   * to all the registered output Handler objects.
   * <p>
   * @param	level   One of the message level identifiers, e.g. SEVERE
   * @param   msg	The string message (or a key in the message catalog)
   * @param   params	array of parameters to the message
   */
  public void log(Level level, String msg, Object params[]) {
    /*
     * 06/06/2002 bcalmac
     * During a shutdown hook the logger is invalid. Redirect to System.out.
     *
     * @todo  Format the message using the parameters .
     */
    if (getHandlers().length == 0) {
      dumpMessage(msg, null);
    /*
     * 06/06/2002 bcalmac
     * Fixed NullPointerException when getLevel() returns null.
     */
    } else if (isLoggable(level)) {
      LogRecord lr = new LogRecord(level, msg);
      lr.setParameters(params);
      inferCaller(lr);
      log(lr);
    }
  }

  /**
   * Log a message, with associated Throwable information.
   * <p>
   * If the logger is currently enabled for the given message
   * level then the given arguments are stored in a LogRecord
   * which is forwarded to all registered output handlers.
   * <p>
   * Note that the thrown argument is stored in the LogRecord thrown
   * property, rather than the LogRecord parameters property.  Thus is it
   * processed specially by output Formatters and is not treated
   * as a formatting parameter to the LogRecord message property.
   * <p>
   * @param	level   One of the message level identifiers, e.g. SEVERE
   * @param   msg	The string message (or a key in the message catalog)
   * @param   thrown  Throwable associated with log message.
   */
  public void log(Level level, String msg, Throwable thrown) {

    String newMsg = msg + "\n" + Util.stackTraceToString(thrown);
    log(level, newMsg);

  }



  /**
   * Log a LogRecord.
   * <p>
   * All the other logging methods in this class call through
   * this method to actually perform any logging.  Subclasses can
   * override this single method to capture all log activity.
   * <p>
   * The current logger's name, source class, source method and
   * other information is recorded in the LogRecord
   *
   * @param record the LogRecord to be published
   */
  public void log(LogRecord record) {

    record.setLoggerName(getName());
    record.setResourceBundle(getResourceBundle());

    if (checkTaskLogs) {
      // Check if a handler is present in the ThreadContext
      Handler h = getTaskLogHandler();
      if (h != null) {
        h.publish(record);
      } else {
        super.log(record);
      }
    }
    else {
      super.log(record);
    }
  }

  /**
   * Write the message to stdout
   * @param msg - string msg to be written to stdout
   * @param thrown - if not null, print its stack trace to stdout
   */
  private void dumpMessage(String msg, Throwable thrown) {
    System.out.println(msg);

    if( thrown != null) {
      thrown.printStackTrace(System.out);
    }
  }

  public static Set getLoggersWithDefaultLogLevel() {
    return loggersWithDefaultLogLevel;
  }

  // PRIVATE

  private boolean checkTaskLogs;

  private static final int offValue = Level.OFF.intValue();
  public static final int DEFAULT_LOG_FILE_SIZE = 2000000;
  public static final int DEFAULT_LOG_FILE_NUMBER = 2;

  private static LogLevelCallback cb = new LogLevelCallback();
  private static Handler defaultHandler;
  private static Formatter defaultFormatter;
  private static String serverName;
  private static String logLocation;
  private static String logFileName;
  private static int logFileSize;
  private static int logFileNumber;
  private static LogManager lm;
  private static InheritableThreadLocal taskLogHandler =
    new InheritableThreadLocal();
  private static Set loggersWithDefaultLogLevel = new HashSet();



  // Private method to infer the caller's class and method names
  private void inferCaller(LogRecord r) {
    // This is an initial implementation.
    // We may get VM support later.
    try {
      java.io.StringWriter sw = new java.io.StringWriter();
      java.io.PrintWriter pw = new java.io.PrintWriter(sw);
      (new Exception("")).printStackTrace(pw);
      pw.close();
      String trace = sw.toString();

      // Find first call on Logger.
      int ix = trace.lastIndexOf("com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger");

      ix = trace.indexOf("(", ix)+3;
      // Now skip to the next stack frame.
      int end = trace.indexOf("(", ix);
      int dot = trace.lastIndexOf(".", end);
      int start = trace.lastIndexOf(" ", dot)+1;
      r.setSourceClassName(trace.substring(start, dot));
      r.setSourceMethodName(trace.substring(dot+1, end));
    } catch (Exception ex) {
      // Something went wrong.  We punt.  This is legal, we
      // are only commited to making a "best effort" here.
      return;
    }
  }

  /**
   * Private method to construct a CiscoLogger for a named component.
   * <p>
   *
   * @param	name	Name of the CiscoLogger.  This should
   *				be a dot-separated component name
   * @param 	resourceBundleName  Not used in this release
   */
  private CiscoLogger(String name, String resourceBundleName,
                      boolean disableTaskLogs) {
    super(name, resourceBundleName);
    checkTaskLogs = !disableTaskLogs;
  }


  private static void usage() {
    System.out.println("Please use one of the following: " +
         "\n\tCiscoLogger.getCiscoLogger(String name)" +
         "\n\tCiscoLogger.getCiscoLogger(String name, String resourceBundle)");

    Thread.dumpStack();
  }


  private static Level getLevelFromString(String name) {
    if (name.equals("ALL")) {
      return Level.ALL;
    } else if (name.equals("CONFIG")) {
      return Level.CONFIG;
    }  else if (name.equals("FINE")) {
      return Level.FINE;
    } else if (name.equals("FINER")) {
      return Level.FINER;
    } else if (name.equals("FINEST")) {
      return Level.FINEST;
    } else if (name.equals("INFO")) {
      return Level.INFO;
    } else if (name.equals("OFF")) {
      return Level.OFF;
    } else if (name.equals("SEVERE")) {
      return Level.SEVERE;
    } else if (name.equals("WARNING")) {
      return Level.WARNING;
    }
    return Level.WARNING;
  }


  private static Level getComponentLogLevel(String c) {
    String levelName;
    levelName = DCPLib.getProperty(c + ".logLevel", null);
    if (levelName == null) {
      levelName = DCPLib.getProperty("Logging.Defaults.logLevel", "WARNING");
      loggersWithDefaultLogLevel.add(c);
    }

    return CiscoLogger.getLevelFromString(levelName);
  }


  private static Handler getComponentHandler(String name) {
    Handler h = null;

    // Check if this component has a custom handler
    String customName;
    customName = DCPLib.getProperty(name + ".logHandler", null);

    if (customName != null) {
      try {
        h = (Handler)Class.forName(customName).newInstance();
      } catch (Exception e) {
        System.out.println("Exception creating custom handler: " + e);
        h = null;
      }
    }

    if (h == null) {
      h = defaultHandler;
    }
    return h;
  }


  private static class LogLevelCallback implements DCPCallback {
    public boolean handleChange(String property, String value) {

      if (!property.endsWith(".logLevel")) {
        return true;
      }

      LogManager lm = LogManager.getLogManager();
      String component = property.substring(0, property.indexOf(".logLevel"));
      Level level = CiscoLogger.getLevelFromString(value);

      if (component.equals("Logging.Defaults")) {
        Set defaultLoggers = CiscoLogger.getLoggersWithDefaultLogLevel();
        Iterator loggerIter = defaultLoggers.iterator();
        while (loggerIter.hasNext() ) {
          String loggerName = (String)loggerIter.next();
          Logger l = lm.getLogger(loggerName);
          if (l != null) {
            l.setLevel(level);
          }
        }

      } else {
        Logger l = lm.getLogger(component);
        if (l != null) {
          l.setLevel(level);
        }
      }

      return true;
    }
    public LogLevelCallback() {}
  }


  static {

    lm = LogManager.getLogManager();
    // lm.reset();

    serverName = LogUtil.getServerName();

    // Check if a custom handler is defined for this server
    String customHandlerName = DCPLib.getProperty(
      "Logging.Servers."+serverName+".logHandler", null);
    if (customHandlerName != null) {
      try {
        defaultHandler=(Handler)Class.forName(customHandlerName).newInstance();
      } catch (Exception e) {
        System.out.println("Exception creating custom handler "
                           + customHandlerName + e);
        defaultHandler = null;
      }
    }


    if (defaultHandler == null) {
      logLocation = LogUtil.getLogLocation();

      logFileSize = DCPLib.getIntProperty("Logging.Defaults.logFileSize",
                                          DEFAULT_LOG_FILE_SIZE);

      logFileNumber =DCPLib.getIntProperty("Logging.Defaults.logFileNumber",
                                           DEFAULT_LOG_FILE_NUMBER);

      if (LogUtil.getAppType().equals("unknown")) {
          logFileName = logLocation + "/" + serverName;
      }
      else {
          logFileName = logLocation + "/" + LogUtil.getAppType() + "." + LogUtil.getAppInst() + "." + serverName;
      }

      // Create any non-existent directories
      String dir = logFileName.substring(0, logFileName.lastIndexOf("/"));
      File f = new File(dir);
      if (!f.exists()) {
        f.mkdirs();
      }

      try {
        defaultHandler = new FileHandler(logFileName, logFileSize,
                                         logFileNumber);
        String formatterName = DCPLib.getProperty(
                                  "Logging.Defaults.logFormatter",
                                  "java.util.logging.XMLFormatter");

        defaultFormatter=(Formatter)Class.forName(formatterName).newInstance();
        defaultHandler.setFormatter(defaultFormatter);
      }
      catch (Exception ex) {
        System.out.println("Exception creating handler: " + ex);
        ex.printStackTrace();
      }
    }

    DCPLib.registerComponent("Logging.Defaults", CiscoLogger.cb);
  }



  // support for task-logs

  public static Handler getTaskLogHandler() {
    return (Handler)taskLogHandler.get();
  }

  public static void setTaskLogHandler(Handler handler) {
    taskLogHandler.set(handler);
  }
}

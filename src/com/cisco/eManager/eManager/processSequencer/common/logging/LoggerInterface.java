package com.cisco.eManager.eManager.processSequencer.common.logging;

public interface LoggerInterface {

  /**
   * Log a SEVERE message.
   * <p>
   * If the logger is currently enabled for the SEVERE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void severe(String msg);

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
  public void severe(String msg, Throwable thrown);


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
  public void severe(String msg, Object obj);


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
  public void severe(String msg, Object obj1, Object obj2);

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
  public void severe(String msg, Object obj1, Object obj2, Object obj3);

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
  public void severe(String msg, Object objs[]);


  /**
   * Log a WARNING message.
   * <p>
   * If the logger is currently enabled for the WARNING message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void warning(String msg);


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
  public void warning(String msg, Throwable thrown);


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
  public void warning(String msg, Object obj);


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
  public void warning(String msg, Object obj1, Object obj2);


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
  public void warning(String msg, Object obj1, Object obj2, Object obj3);

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
  public void warning(String msg, Object objs[]);


  /**
   * Log an INFO message.
   * <p>
   * If the logger is currently enabled for the INFO message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void info(String msg);

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
  public void info(String msg, Throwable thrown);


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
  public void info(String msg, Object obj);


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
  public void info(String msg, Object obj1, Object obj2);


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
  public void info(String msg, Object obj1, Object obj2, Object obj3);

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
  public void info(String msg, Object objs[]);


  /**
   * Log a CONFIG message.
   * <p>
   * If the logger is currently enabled for the CONFIG message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void config(String msg);


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
  public void config(String msg, Throwable thrown);


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
  public void config(String msg, Object obj);


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
  public void config(String msg, Object obj1, Object obj2);


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
  public void config(String msg, Object obj1, Object obj2, Object obj3);

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
  public void config(String msg, Object objs[]);


  /**
   * Log a FINE message.
   * <p>
   * If the logger is currently enabled for the CONFIG message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void fine(String msg);

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
  public void fine(String msg, Throwable thrown);


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
  public void fine(String msg, Object obj);


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
  public void fine(String msg, Object obj1, Object obj2);


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
  public void fine(String msg, Object obj1, Object obj2, Object obj3);

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
  public void fine(String msg, Object objs[]);


  /**
   * Log a FINER message.
   * <p>
   * If the logger is currently enabled for the FINER message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void finer(String msg);


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
  public void finer(String msg, Throwable thrown);


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
  public void finer(String msg, Object obj);


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
  public void finer(String msg, Object obj1, Object obj2);


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
  public void finer(String msg, Object obj1, Object obj2, Object obj3);

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
  public void finer(String msg, Object objs[]);


  /**
   * Log a FINEST message.
   * <p>
   * If the logger is currently enabled for the FINEST message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   * @param   msg	The string message
   */
  public void finest(String msg);

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
  public void finest(String msg, Throwable thrown);


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
  public void finest(String msg, Object obj);


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
  public void finest(String msg, Object obj1, Object obj2);


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
  public void finest(String msg, Object obj1, Object obj2, Object obj3);

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
  public void finest(String msg, Object objs[]);

  /**
   * Prefix all messages logged by this logger with the given prefix
   *
   * @param   prefix	The prefix
   */
  public void setMsgPrefix(String prefix);

  /**
   * Returns the prefix currently in use by this logger.
   *
   * @return prefix used by this logger
   */
  public String getMsgPrefix();

}


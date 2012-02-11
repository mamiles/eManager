/**
 *  This computer program is the confidential information and proprietary trade
 *  secret of Cisco Systems, Inc. Possessions and use of this program must
 *  conform strictly to the license agreement between the user and Cisco
 *  Systems, Inc., and receipt or possession does not convey any rights to
 *  divulge, reproduce, or allow others to use this program without specific
 *  written authorization of Cisco Systems, Inc. Copyright (c) 2002 by Cisco
 *  Systems, Inc. All rights reserved.
 */
package com.cisco.eManager.eManager.processSequencer.common;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

import java.util.Locale;
import java.util.ResourceBundle;
import com.cisco.eManager.eManager.processSequencer.common.DCPLib;
import com.cisco.eManager.eManager.processSequencer.common.NoSuchProperty;

import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;

/**
 *
 * The class <code>PSException</code> and its subclasses, for example, {@link com.cisco.vpnsc.repository.RepException}
 * and {@link com.cisco.vpnsc.genericquery.FindException} are a form of {@link java.lang.Throwable} that indicates conditions that a various ISC
 * application modules might want to catch and take appropriate corrective steps.<p>
 * @author Raj Pokalori
 * @since ISC 3.1
 * <p> The modules that need to create more specific class of <code>PSException</code> should do the following:
 * <ul>
 * <li> Create a class that extends <code>PSException</code>, for example, {@link com.cisco.vpnsc.genericquery.FindException}.</li>
 * <li> Create a class that extends {@link EnumeratedType}, for example, {@link com.cisco.vpnsc.exceptionMsgs.FindExceptionMsgEnum}.
 * In this class, declare all possible errors that can occur in this module as ENUM values, for example: <p>
 * <code>
 *  <p>public final static FindExceptionMsgEnum UNKNOWN = new FindExceptionMsgEnum(-1);
 *  <p>public final static FindExceptionMsgEnum INVALID_ROW_NUMBER = new FindExceptionMsgEnum(0);
 *  <p>public final static FindExceptionMsgEnum UNABLE_TO_MAKE_JOIN   = new FindExceptionMsgEnum(1);
 * </code>
 * </li>
 * <li> Create a default (English) resource bundle file, say, {@link FindExceptionMsg_en.properties}.
 * <p>Provide the detailed message string of the exceptions with place holders for any necessary arguments as follows:
 * <p><code>0 = {0}::{1}: genericquery ==> FindException, Invalid row number {2} specified, please check and retry
 * <p>1 = {0}::{1}: genericquery ==> FindException, Unable to create table Join between {2} and {3}
 * <p>2 = {0}::{1}: genericquery ==> RepException, Database Connection Failure</code>
 * </li>
 * <li>Implement the following method in the subclass of <code>PSException</code> to set the
 * resource bundle for the default locale
 * <code>
 *    <p> protected void setResourceBundle_Locale() {
 *        <p>  super.setResourceBundle_Locale(MESSAGE_FILE, Locale.ENGLISH);
 *    <p> }</code>
 * <em><p>where MESSAGE_FILE is the resource bundle file, for example,
 * "com.cisco.vpnsc.exceptionMsgs.FindExceptionMsg_en"</em></li>
 * </ul>
 */
public class PSException extends java.lang.Exception {



    /**
     *  Constructs a new <code>PSException</code> with the specified message key.  The
     *  cause is not initialized, and may subsequently be initialized by
     *  a call to {@link #initCause}.<p>
     *  The detailed message of this exception will be formatted using the default resource bundle for the default locale.
     *  This detail message could be later retrieved by the {@link #getMessage()} method.<p>
     *  (A <i>null</i> value is set for the cause as it is not provided in this constructor which
     *  indicates that the cause is nonexistent or unknown.)<p>
     *  @param  msgKey is an enumerated type representing the error code of the exception being thrown.
     */
    public PSException(EnumeratedType msgKey) {
        this(msgKey, (Throwable) null, (Object[]) null);
    }


    /**
     *  Constructs a <code>PSException</code> using the message key. The compound message would be
     *  bound by values of the args parameter. The
     *  cause is not initialized, and may subsequently be initialized by
     *  a call to {@link #initCause}.<p>
     *  The detailed message of this exception will be formatted using the default resource
     *  bundle for the default locale.
     *  This detail message could be later retrieved by the {@link #getMessage()} method).<p>
     *  A <i>null</i> value is set for the cause as it is not provided in this constructor which
     *  indicates that the cause is nonexistent or unknown.<p>
     *  @param  msgKey  is an enumerated type representing the code of the exception being thrown.
     *  @param  args    Arguments to format the message string retreived by msgKey from the resource bundle.
     */
    public PSException(EnumeratedType msgKey, Object[] args) {
        this(msgKey, (Throwable) null, args);
    }


    /**
     *  Constructs a new PSException with the specified message key and the cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this PSExceptions's detail message.<p>
     *  The detailed message of this exception will be formatted using the default resource
     *  bundle for the default locale.
     *  This detail message could be later retrieved by the {@link #getMessage()} method.<p>
     *  @param  msgKey  is an enumerated type representing the error code of the exception being thrown.
     *  @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  A <i>null</i> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)

     */
    public PSException(EnumeratedType msgKey, Throwable cause) {
        this(msgKey, cause, (Object[]) null);
    }


    /**
     *  Constructs a new PSException with the specified message key and the cause.  <p>
     *  The detailed message of this exception will be formatted using the default resource
     *  bundle for the default locale.<p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this PSExceptions's detail message.
     *  This detail message could be later retrieved by the {@link #getMessage()} method.
     *  @param  msgKey  is an enumerated type representing the error code of the exception being thrown.
     *  @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  A <i>null</i> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     *  @param  args Arguments to format the message string retreived by msgKey from the resource bundle.

     */
    public PSException(EnumeratedType msgKey, Throwable cause, Object[] args) {
        super(msgKey.toString(),cause);
        m_args = args;
        m_msgKey = msgKey;
        m_msgKeyStr = msgKey.toString();
    }

    /**
     * Constructs a new PSException with <code>null</code> as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * <p>The {@link #fillInStackTrace()} method is called to initialize
     * the stack trace data in the newly created exception.
     *
     * Starting ISC v3.1, there shall not be any exceptions without a proper message key.
     * PLEASE start using the constructor {@link #PSException(EnumeratedType msgKey, Throwable cause, Object[] args)}<p>
     */

     public PSException() {
       //replace this implementation and throw an Exception to prevent users calling this constructor
       super();
       m_oldStyleException=true;
   }
   /**
    * Constructs a new PSException with the specified detail message.  The
    * cause is not initialized, and may subsequently be initialized by
    * a call to {@link #initCause}.
    *
    * <p>The {@link #fillInStackTrace()} method is called to initialize
    * the stack trace data in the newly created exception.
    *

    *
    * Starting ISC v3.1, there shall not be any exceptions without a proper message key.
    * PLEASE start using the constructor {@link #PSException(EnumeratedType msgKey)}<p>
    * @param   message   the detail message. The detail message is saved for
    *          later retrieval by the {@link #getMessage()} method.
    */
   public PSException(String message) {
       //replace this implementation and throw an Exception to prevent users calling this constructor
       super(message);
       m_oldStyleException=true;
   }


   /**
    * Constructs a new PSException with the specified detail message and
    * cause.  <p>Note that the detail message associated with
    * <code>cause</code> is <i>not</i> automatically incorporated in
    * this PSExceptions's detail message.
    *
    * <p>The {@link #fillInStackTrace()} method is called to initialize
    * the stack trace data in the newly created throwable.
    *
    * Starting ISC v3.1, there shall not be any exceptions without a proper message key.
    * PLEASE start using the constructor {@link #PSException(EnumeratedType msgKey, Throwable cause)}<p>

    * @param  message the detail message (which is saved for later retrieval
    *         by the {@link #getMessage()} method).
    * @param  cause the cause (which is saved for later retrieval by the
    *         {@link #getCause()} method).  (A <tt>null</tt> value is
    *         permitted, and indicates that the cause is nonexistent or
    *         unknown.)
     */
   public PSException(String message, Throwable cause) {
       //replace this implementation and throw an Exception to prevent users calling this constructor
       super(message,cause);
       m_oldStyleException=true;
   }


   /**
    * Constructs a new PSException with the specified cause and a detail
    * message of <tt>(cause==null ? null : cause.toString())</tt> (which
    * typically contains the class and detail message of <tt>cause</tt>).
    * This constructor is useful for throwables that are little more than
    * wrappers for other throwables (for example, {@link
    * java.security.PrivilegedActionException}).
    *
    * <p>The {@link #fillInStackTrace()} method is called to initialize
    * the stack trace data in the newly created throwable.
    * @param  cause the cause (which is saved for later retrieval by the
    *         {@link #getCause()} method).  (A <tt>null</tt> value is
    *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
    */
   public PSException(Throwable cause) {
       //replace this implementation and throw an Exception to prevent users calling this constructor
       super(cause);
       m_oldStyleException=true;
   }

    /**
     * Returns the list of arguments used to format the detailed message of
     * this exception. The message string will be formatted as per the position
     * of the argument in the array.
     * @return array of arguments used to format the detailed message string.
     */
    public Object[] getMessageStringArguments() {
      return this.m_args;
    }

    /**
     * Returns the error code associated with this exception.
     * <em>Note: The subclasses of PSException are expected to implement <code>
     * getExceptionMsgKey()</code> that return a specific type of EnumeratedType with respect
     * to the subclass that implements it. This method here is available, just
     * in case the subclasses do not implement <code>getExceptionMsgKey()</code></em>
     * @return The exception code as an EnumeratedType
     */
    public EnumeratedType getExceptionErrorCode() {
      return this.m_msgKey;
    }



    /**
     * Returns the detailed message string of this Exception.
     * This method is overridden by PSException to construct the message string, binding the
     * arguments with the message string using the default resource bundle and default locale.<p>
     *@return    the detalied message string of this exception, formatted using
     * the default resource bundle and default locale.
     */
    public String getMessage() {
      if ( m_oldStyleException ) {
        return super.getMessage();
      }
      else {
        return getMessage(null, null);
      }
    }
    /**
     * Returns the loacle-specific detailed message of this exception using the default resource bundle.
     *@param  locale  The locale to be used for this exception message string
     *@return         The locale-specific messgae using the default resource bundle.
     */
    public String getMessage(Locale locale) {
        return getMessage(null,locale);
    }
    /**
     * Returns the loacle-specific message using the given resource bundle.
     *@param  bundle  The ResourceBundle to use for this exception message
     *@param  locale  The locale to be used for this exception message
     *@return         The locale-specific messgae using the given resource bundle.
     */
    public String getMessage(ResourceBundle bundle, Locale locale) {
        if (bundle != null) //if null, will use the default resource bundle
          this.m_resourceBundle = bundle;
        if (locale != null) //if null, will use the default locale
          this.m_locale = locale;

        formatMessage(bundle, locale);
        return m_formattedMessage;
    }


    /**
     *  Will be removed in ISC 4.0. Currently, the call to this method
     * is redirected to {@link #getMessage()}.
     *  Gets the formattedMessage attribute of the PSException object,
     *  which is defined in resource bundle, ignoring the nested exception
     *  if any. THis is needed by GUI when displaying error message to end user.
     *
     *@return         The formattedMessage value
     */
    public String getErrorMessageOnly() {
        return this.getMessage();
    }
    /**
     *  Records in the exception the current stack trace. This is intended to be
     *  used by RMI clients that want to see the local stack trace in addition
     *  to the one from the server VM.
     *
     *  Will be removed in ISC 4.0
     */
    public void fillInLocalStackTrace() {
        fillInStackTrace();
        if (this.getCause() != null) {
            this.getCause().fillInStackTrace();
        }
        m_localStackTraceEnabled = true;
    }

    /**
     *  To be removed in ISC 4.0
     *  Returns the stack trace of the nested stack trace as a String
     *
     *@return    The stackTraceString value
     */
    public String getNestedStackTrace() {
        StringWriter sw = new StringWriter(2048);
        PrintWriter pw = new PrintWriter(sw);
        if (this.getCause() != null)
          this.getCause().printStackTrace(pw);
        pw.close();
        return sw.toString();
    }
    /**
     * To be removed in ISC 4.0
     *  Returns the stack trace of the nested stack trace as a String
     *
     *@return    The stackTraceString value
     */
    public String getSuperStackTrace() {
        StringWriter sw = new StringWriter(2048);
        PrintWriter pw = new PrintWriter(sw);
        super.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }
    /**
     *  To be removed in ISC 4.0
     * Use standard java api public StackTraceElement[] getStackTrace() instead.
     *  Returns the stack trace as a String for printing
     *
     *@return    The stackTraceString value
     */
    public String getStackTraceAsString() {

        StringBuffer retStrBuf = new StringBuffer("");

        // Print the local stack trace.
        if (m_localStackTraceEnabled) {
            // If the stack trace is already generated, use it.
            if (m_localStackTrace != null) {
                retStrBuf.append(m_localStackTrace);
            }
            // Else, if there's no nested exception, delegate to the parent.
            else {
                retStrBuf.append(getStackTraceStr());
            }

            // Print the header for the initial exception
            retStrBuf.append("INITIAL EXCEPTION IS: ");
        }

        // Print the initial exception

        // If the stack trace is already generated, use it.
        if (m_initialStackTrace != null) {
            retStrBuf.append(m_initialStackTrace);
        }
        // Else, if there's no nested exception, delegate to the parent.
        else {
            retStrBuf.append(getStackTraceStr());
        }
        return retStrBuf.toString();
    }
    /**
     *  Sets the resourceBundle and Locale to format the detailed message of this exception.
     * <em>The subclasses of PSException should call this method to
     *  set the resource bundle file name and locale.
     *
     *@param  bundle  The resource bundle message file name
     *@param  locale  The locale
     */
    public void setResourceBundle_Locale(String bundle, Locale locale) {
        m_locale = locale;
        m_resourceBundle = ResourceBundle.getBundle(bundle, locale);
    }



//==========================================================
// Private Methods
//==========================================================

    /**
     *  Creates a message formatted in accordance with a Locale and Language.
     *  The key for locating message and the argument values for constructing
     *  the message are cached in the constructor. Uses the key to locate the
     *  message in the resource bundle file
     *
     *@param  bundle  The resource bundle to use
     *@param  locale  The locale
     */
    private void formatMessage(ResourceBundle bundle, Locale locale) {

      String msg = super.getMessage();
      if (msg != null)  {
         if (msg.trim().equals(""))
           msg = "Unknown: Exception message not provided.";
         else
           msg = msg + " : ";
      }
      else {
        msg = "Unknown: Exception message not provided.";
      }
      String message ="";
      if (m_msgKeyStr != null) {
          message = m_resourceBundle.getString(m_msgKeyStr);
      }

      //Plug in the arguments into place holders in the message
      MessageFormat msgFmt = new MessageFormat(message);
      if (m_args != null) {
          m_formattedMessage = msg + msgFmt.format(m_args);
      } else {
          m_formattedMessage = msg + message;
        }
    }





    /**
     *  to be removed in ISC4.0
     *  Gets the stackTrace attribute of the PSException object
     *
     *@return    The stackTrace value
     */
    private String getStackTraceStr() {
        StringBuffer retStrBuf = new StringBuffer("");
        if (this.getCause() == null) {
            retStrBuf.append(getSuperStackTrace());
        }
        // Else print my message and the stack trace of the nested exception.
        else {
            retStrBuf.append(getClass().getName());
            retStrBuf.append(": ");
            retStrBuf.append(getMessage());
            retStrBuf.append("\n");
            retStrBuf.append(getNestedStackTrace());
        }
        return retStrBuf.toString();
    }


    /**
     *  Logs the message to a log file
    private void log() {
        CiscoLogger logger;
        if (m_resourceBundleName != null) {
            logger = CiscoLogger.getCiscoLogger("Logging", m_resourceBundleName);
        } else {
            logger = CiscoLogger.getCiscoLogger("Logging");
        }
        if (m_args != null) {
            logger.severe(m_msgKeyStr, m_args);
        } else {
            logger.severe(m_msgKeyStr);
        }
    }
     */


    /**
     *  Overridden to implement serialization of stack traces
     *
     *@param  out                      The output stream
     *@exception  java.io.IOException  on errors accessing the stream
     */
    private void writeObject(java.io.ObjectOutputStream out)
             throws java.io.IOException {
        // If I am in the initial VM, generate the initial stack trace.
        if (m_initialStackTrace == null) {
            m_initialStackTrace = getStackTraceAsString();
        }

        // If local stack trace is enabled, generate it too.
        if (m_localStackTraceEnabled && m_localStackTrace == null) {
            m_localStackTrace = getStackTraceAsString();
        }
        out.defaultWriteObject();
    }


//==========================================================
// Protected Members
//==========================================================
    /**
     *  The resource bundle to format the detailed message of this exception.
     */
    protected transient ResourceBundle m_resourceBundle;

//==========================================================
// Private Members
//==========================================================
    private Object[] m_args;
    private String m_formattedMessage="";
    private String m_initialStackTrace;
    private Locale m_locale;
    private String m_localStackTrace;
    private boolean m_localStackTraceEnabled;
    private String m_msgKeyStr;
    /**
     * The error code of this exception as an EnumeratedType
     */
    protected EnumeratedType m_msgKey = null; //12/26/2002 weili,
              // please see comments in RepException.getExceptionMsgKey().
    private boolean m_oldStyleException=false;
}


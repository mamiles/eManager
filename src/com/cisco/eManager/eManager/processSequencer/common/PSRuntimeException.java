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
 *  Extension of Exception that provides a serializable stack trace and the
 *  ability to wrap other exceptions. <p>
 *
 *  Based on work done earlier by Bogdan Calmac and Rama Kumar Taraniganty
 *
 *@author     Bogdan Calmac <bcalmac@cisco.com>
 *@author     Rama Kumar Taraniganty <rtaranig@cisco.com>
 *@author     Venkatram Pramod (vpramod@cisco.com>
 *@created    February 8, 2002
 */
public class PSRuntimeException extends java.lang.RuntimeException {

    /**
     *  Constructor
     *
     *@param  msgKey  Key is an enumerated type
     */
    public PSRuntimeException(EnumeratedType msgKey) {
        this(msgKey, (Throwable) null, (Object[]) null);
    }


    /**
     *  Constructor
     *
     *@param  msgKey  Key is an enumerated type
     *@param  args    Arguments to the message retreived by key from a resource
     *      bundle
     */
    public PSRuntimeException(EnumeratedType msgKey, Object[] args) {
        this(msgKey, (Throwable) null, args);
    }


    /**
     *  Constructor
     *
     *@param  msgKey  Key is an enumerated type
     *@param  e       The exception to be nested
     */
    public PSRuntimeException(EnumeratedType msgKey, Throwable e) {
        this(msgKey, e, (Object[]) null);
    }


    /**
     *  Constructor
     *
     *@param  msgKey  Key is an enumerated type
     *@param  e       The exception to be nested
     *@param  args    Arguments to the message retreived by key from a resource
     *      bundle
     */
    public PSRuntimeException(EnumeratedType msgKey, Throwable e, Object[] args) {
        super(msgKey.toString());
        m_args = args;
        m_msgKeyStr = msgKey.toString();
        m_nestedException = e;
    }


    /**
     *  Constructor. To be used when the message is not defined in a resource
     *  bundle
     *
     *@param  reason  a description of the exception
     */
    public PSRuntimeException(String reason) {
        super(reason);
        m_oldStyleException=true;
    }


    /**
     *  Constructor. To be used when the message is not defined in a resource
     *  bundle
     *
     *@param  reason  a description of the exception
     *@param  e       the exception to be nested
     */
    public PSRuntimeException(String reason, Throwable e) {
        this(reason);
        m_nestedException = e;
        m_oldStyleException=true;
    }


    /**
     *  Constructor with an undefined reason
     *
     *@param  e  the exception to be nested
     */
    public PSRuntimeException(Throwable e) {
        this("Unknown", e);
        m_oldStyleException=true;
    }


    /**
     *  Records in the exception the current stack trace. This is intended to be
     *  used by RMI clients that want to see the local stack trace in addition
     *  to the one from the server VM.
     */
    public void fillInLocalStackTrace() {
        fillInStackTrace();
        if (m_nestedException != null) {
            m_nestedException.fillInStackTrace();
        }
        m_localStackTraceEnabled = true;
    }


    /**
     *  Retrieves the nested nexception
     *
     *@return    the nested exception
     */
    public Throwable getCause() {
        return (m_nestedException);
    }


    /**
     *  Gets the formattedMessage attribute of the PSRuntimeException object
     *
     *@return    The formattedMessage value
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
     *  Gets the formattedMessage attribute of the PSRuntimeException object
     *
     *@param  bundle  Description of the Parameter
     *@param  locale  Description of the Parameter
     *@return         The formattedMessage value
     */
    public String getMessage(String bundle, Locale locale) {
        String msg = super.getMessage();
        formatMessage(bundle, locale);
        if (m_nestedException != null) {
            return msg + ": " + m_formattedMessage + ", nested message: " + m_nestedException.getMessage();
        } else {
            return msg + ": " + m_formattedMessage;
        }
    }


    /**
     *  Returns the stack trace of the nested stack trace as a String
     *
     *@return    The stackTraceString value
     */
    public String getNestedStackTrace() {
        StringWriter sw = new StringWriter(2048);
        PrintWriter pw = new PrintWriter(sw);
        m_nestedException.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }


    /**
     *  Retrieves the nested nexception
     *
     *@return    the nested exception
     */
    public Throwable getNestedException() {
        return (m_nestedException);
    }


    /**
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
     *  Prints the stack trace
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }


    /**
     *  Prints the stack trace to a PrintStream
     *
     *@param  ps  the PrintStream
     */
    public void printStackTrace(PrintStream ps) {
        synchronized (ps) {
            ps.println(getStackTraceAsString());
        }
    }


    /**
     *  Prints the stack trace to a PrintWriter
     *
     *@param  pw  Description of the Parameter
     */
    public void printStackTrace(PrintWriter pw) {
        synchronized (pw) {
            pw.println(getStackTraceAsString());
        }
    }


    /**
     *  Sets the resourceBundle and Locale
     *
     *@param  bundle  The resource bundle
     *@param  locale  The locale
     */
    public void setResourceBundle_Locale(String bundle, Locale locale) {
        m_resourceBundleName = bundle;
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
     *@param  bundle  The location of the resource bundle
     *@param  locale  The locale
     */
    private void formatMessage(String bundle, Locale locale) {
        //Get the resource bundle
        if (bundle != null && locale != null) {
            //cache the bundle and locale
            m_resourceBundleName = bundle;
            m_locale = locale;
            m_resourceBundle = ResourceBundle.getBundle(m_resourceBundleName, m_locale);
        }

        //if resource bundle is null, which can be if this exception
        //came across a stream (because ResourceBundle is not serializable,
        //it cannot be sent across, try to create one from the existing
        //resourceBundleName and local
        if (m_resourceBundle == null) {
            if (m_resourceBundleName != null && m_locale != null) {
                m_resourceBundle = ResourceBundle.getBundle(m_resourceBundleName, m_locale);
            } else {
                //we don't have the bundleName and locale, this is a bug
                throw new RuntimeException("PSRuntimeException::NULL Resource Bundle");
            }
        }

        //Get hold of the string in the appropriate language
        String message = "Unknown";
        if (m_msgKeyStr != null) {
            message = m_resourceBundle.getString(m_msgKeyStr);
        }

        //Plug in the arguments into place holders in the message
        MessageFormat msgFmt = new MessageFormat(message);
        if (m_args != null) {
            m_formattedMessage = msgFmt.format(m_args);
        } else {
            m_formattedMessage = message;
        }

    }


    /**
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
     *  Gets the stackTrace attribute of the PSRuntimeException object
     *
     *@return    The stackTrace value
     */
    private String getStackTraceStr() {
        StringBuffer retStrBuf = new StringBuffer("");
        if (m_nestedException == null) {
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
     */
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
     *  Description of the Field
     */
    protected transient ResourceBundle m_resourceBundle;

//==========================================================
// Private Members
//==========================================================
    private Object[] m_args;
    private String m_formattedMessage;
    private String m_initialStackTrace;
    private Locale m_locale;
    private String m_localStackTrace;
    private boolean m_localStackTraceEnabled;
    private String m_msgKeyStr;
    private String m_resourceBundleName;
    private Throwable m_nestedException;
    private boolean m_oldStyleException=false;

}


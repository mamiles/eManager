/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.log;

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
 * 
 * 
 * @version $Revision$ $Date$
**/
public abstract class LogFile implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private LogFileChoice _logFileChoice;

    private java.lang.String _hostName;

    private java.lang.String _logPath;

    private java.lang.String _logName;

    private java.lang.String _logSize;

    private java.lang.String _dateTime;


      //----------------/
     //- Constructors -/
    //----------------/

    public LogFile() {
        super();
    } //-- com.cisco.eManager.common.log.LogFile()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dateTime'.
     * 
     * @return the value of field 'dateTime'.
    **/
    public java.lang.String getDateTime()
    {
        return this._dateTime;
    } //-- java.lang.String getDateTime() 

    /**
     * Returns the value of field 'hostName'.
     * 
     * @return the value of field 'hostName'.
    **/
    public java.lang.String getHostName()
    {
        return this._hostName;
    } //-- java.lang.String getHostName() 

    /**
     * Returns the value of field 'logFileChoice'.
     * 
     * @return the value of field 'logFileChoice'.
    **/
    public LogFileChoice getLogFileChoice()
    {
        return this._logFileChoice;
    } //-- LogFileChoice getLogFileChoice() 

    /**
     * Returns the value of field 'logName'.
     * 
     * @return the value of field 'logName'.
    **/
    public java.lang.String getLogName()
    {
        return this._logName;
    } //-- java.lang.String getLogName() 

    /**
     * Returns the value of field 'logPath'.
     * 
     * @return the value of field 'logPath'.
    **/
    public java.lang.String getLogPath()
    {
        return this._logPath;
    } //-- java.lang.String getLogPath() 

    /**
     * Returns the value of field 'logSize'.
     * 
     * @return the value of field 'logSize'.
    **/
    public java.lang.String getLogSize()
    {
        return this._logSize;
    } //-- java.lang.String getLogSize() 

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
     * Sets the value of field 'dateTime'.
     * 
     * @param dateTime the value of field 'dateTime'.
    **/
    public void setDateTime(java.lang.String dateTime)
    {
        this._dateTime = dateTime;
    } //-- void setDateTime(java.lang.String) 

    /**
     * Sets the value of field 'hostName'.
     * 
     * @param hostName the value of field 'hostName'.
    **/
    public void setHostName(java.lang.String hostName)
    {
        this._hostName = hostName;
    } //-- void setHostName(java.lang.String) 

    /**
     * Sets the value of field 'logFileChoice'.
     * 
     * @param logFileChoice the value of field 'logFileChoice'.
    **/
    public void setLogFileChoice(LogFileChoice logFileChoice)
    {
        this._logFileChoice = logFileChoice;
    } //-- void setLogFileChoice(LogFileChoice) 

    /**
     * Sets the value of field 'logName'.
     * 
     * @param logName the value of field 'logName'.
    **/
    public void setLogName(java.lang.String logName)
    {
        this._logName = logName;
    } //-- void setLogName(java.lang.String) 

    /**
     * Sets the value of field 'logPath'.
     * 
     * @param logPath the value of field 'logPath'.
    **/
    public void setLogPath(java.lang.String logPath)
    {
        this._logPath = logPath;
    } //-- void setLogPath(java.lang.String) 

    /**
     * Sets the value of field 'logSize'.
     * 
     * @param logSize the value of field 'logSize'.
    **/
    public void setLogSize(java.lang.String logSize)
    {
        this._logSize = logSize;
    } //-- void setLogSize(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

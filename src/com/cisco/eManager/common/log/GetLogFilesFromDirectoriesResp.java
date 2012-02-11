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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class GetLogFilesFromDirectoriesResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _logFilesList;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetLogFilesFromDirectoriesResp() {
        super();
        _logFilesList = new ArrayList();
    } //-- com.cisco.eManager.common.log.GetLogFilesFromDirectoriesResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vLogFiles
    **/
    public void addLogFiles(LogFiles vLogFiles)
        throws java.lang.IndexOutOfBoundsException
    {
        _logFilesList.add(vLogFiles);
    } //-- void addLogFiles(LogFiles) 

    /**
     * 
     * 
     * @param index
     * @param vLogFiles
    **/
    public void addLogFiles(int index, LogFiles vLogFiles)
        throws java.lang.IndexOutOfBoundsException
    {
        _logFilesList.add(index, vLogFiles);
    } //-- void addLogFiles(int, LogFiles) 

    /**
    **/
    public void clearLogFiles()
    {
        _logFilesList.clear();
    } //-- void clearLogFiles() 

    /**
    **/
    public java.util.Enumeration enumerateLogFiles()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_logFilesList.iterator());
    } //-- java.util.Enumeration enumerateLogFiles() 

    /**
     * 
     * 
     * @param index
    **/
    public LogFiles getLogFiles(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _logFilesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (LogFiles) _logFilesList.get(index);
    } //-- LogFiles getLogFiles(int) 

    /**
    **/
    public LogFiles[] getLogFiles()
    {
        int size = _logFilesList.size();
        LogFiles[] mArray = new LogFiles[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (LogFiles) _logFilesList.get(index);
        }
        return mArray;
    } //-- LogFiles[] getLogFiles() 

    /**
    **/
    public int getLogFilesCount()
    {
        return _logFilesList.size();
    } //-- int getLogFilesCount() 

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
     * 
     * 
     * @param vLogFiles
    **/
    public boolean removeLogFiles(LogFiles vLogFiles)
    {
        boolean removed = _logFilesList.remove(vLogFiles);
        return removed;
    } //-- boolean removeLogFiles(LogFiles) 

    /**
     * 
     * 
     * @param index
     * @param vLogFiles
    **/
    public void setLogFiles(int index, LogFiles vLogFiles)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _logFilesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _logFilesList.set(index, vLogFiles);
    } //-- void setLogFiles(int, LogFiles) 

    /**
     * 
     * 
     * @param logFilesArray
    **/
    public void setLogFiles(LogFiles[] logFilesArray)
    {
        //-- copy array
        _logFilesList.clear();
        for (int i = 0; i < logFilesArray.length; i++) {
            _logFilesList.add(logFilesArray[i]);
        }
    } //-- void setLogFiles(LogFiles) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.log.GetLogFilesFromDirectoriesResp unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.log.GetLogFilesFromDirectoriesResp) Unmarshaller.unmarshal(com.cisco.eManager.common.log.GetLogFilesFromDirectoriesResp.class, reader);
    } //-- com.cisco.eManager.common.log.GetLogFilesFromDirectoriesResp unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

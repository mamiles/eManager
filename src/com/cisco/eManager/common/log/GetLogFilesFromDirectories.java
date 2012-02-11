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
public class GetLogFilesFromDirectories implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private GetLogFilesFromDirectoriesChoice _getLogFilesFromDirectoriesChoice;

    private java.lang.String _searchFilter;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetLogFilesFromDirectories() {
        super();
    } //-- com.cisco.eManager.common.log.GetLogFilesFromDirectories()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field
     * 'getLogFilesFromDirectoriesChoice'.
     * 
     * @return the value of field 'getLogFilesFromDirectoriesChoice'
    **/
    public GetLogFilesFromDirectoriesChoice getGetLogFilesFromDirectoriesChoice()
    {
        return this._getLogFilesFromDirectoriesChoice;
    } //-- GetLogFilesFromDirectoriesChoice getGetLogFilesFromDirectoriesChoice() 

    /**
     * Returns the value of field 'searchFilter'.
     * 
     * @return the value of field 'searchFilter'.
    **/
    public java.lang.String getSearchFilter()
    {
        return this._searchFilter;
    } //-- java.lang.String getSearchFilter() 

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
     * Sets the value of field 'getLogFilesFromDirectoriesChoice'.
     * 
     * @param getLogFilesFromDirectoriesChoice the value of field
     * 'getLogFilesFromDirectoriesChoice'.
    **/
    public void setGetLogFilesFromDirectoriesChoice(GetLogFilesFromDirectoriesChoice getLogFilesFromDirectoriesChoice)
    {
        this._getLogFilesFromDirectoriesChoice = getLogFilesFromDirectoriesChoice;
    } //-- void setGetLogFilesFromDirectoriesChoice(GetLogFilesFromDirectoriesChoice) 

    /**
     * Sets the value of field 'searchFilter'.
     * 
     * @param searchFilter the value of field 'searchFilter'.
    **/
    public void setSearchFilter(java.lang.String searchFilter)
    {
        this._searchFilter = searchFilter;
    } //-- void setSearchFilter(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.log.GetLogFilesFromDirectories unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.log.GetLogFilesFromDirectories) Unmarshaller.unmarshal(com.cisco.eManager.common.log.GetLogFilesFromDirectories.class, reader);
    } //-- com.cisco.eManager.common.log.GetLogFilesFromDirectories unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

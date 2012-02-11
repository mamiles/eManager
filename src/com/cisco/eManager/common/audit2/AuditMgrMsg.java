/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.audit2;

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
 * Comment describing your root element
 * 
 * @version $Revision$ $Date$
**/
public class AuditMgrMsg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private RetrieveAuditLogEntries _retrieveAuditLogEntries;

    private DeleteAuditLogEntries _deleteAuditLogEntries;


      //----------------/
     //- Constructors -/
    //----------------/

    public AuditMgrMsg() {
        super();
    } //-- com.cisco.eManager.common.audit2.AuditMgrMsg()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'deleteAuditLogEntries'.
     * 
     * @return the value of field 'deleteAuditLogEntries'.
    **/
    public DeleteAuditLogEntries getDeleteAuditLogEntries()
    {
        return this._deleteAuditLogEntries;
    } //-- DeleteAuditLogEntries getDeleteAuditLogEntries() 

    /**
     * Returns the value of field 'retrieveAuditLogEntries'.
     * 
     * @return the value of field 'retrieveAuditLogEntries'.
    **/
    public RetrieveAuditLogEntries getRetrieveAuditLogEntries()
    {
        return this._retrieveAuditLogEntries;
    } //-- RetrieveAuditLogEntries getRetrieveAuditLogEntries() 

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
     * Sets the value of field 'deleteAuditLogEntries'.
     * 
     * @param deleteAuditLogEntries the value of field
     * 'deleteAuditLogEntries'.
    **/
    public void setDeleteAuditLogEntries(DeleteAuditLogEntries deleteAuditLogEntries)
    {
        this._deleteAuditLogEntries = deleteAuditLogEntries;
    } //-- void setDeleteAuditLogEntries(DeleteAuditLogEntries) 

    /**
     * Sets the value of field 'retrieveAuditLogEntries'.
     * 
     * @param retrieveAuditLogEntries the value of field
     * 'retrieveAuditLogEntries'.
    **/
    public void setRetrieveAuditLogEntries(RetrieveAuditLogEntries retrieveAuditLogEntries)
    {
        this._retrieveAuditLogEntries = retrieveAuditLogEntries;
    } //-- void setRetrieveAuditLogEntries(RetrieveAuditLogEntries) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.audit2.AuditMgrMsg unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.audit2.AuditMgrMsg) Unmarshaller.unmarshal(com.cisco.eManager.common.audit2.AuditMgrMsg.class, reader);
    } //-- com.cisco.eManager.common.audit2.AuditMgrMsg unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

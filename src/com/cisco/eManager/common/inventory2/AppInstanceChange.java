/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cisco.eManager.common.inventory2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class AppInstanceChange.
 * 
 * @version $Revision$ $Date$
 */
public class AppInstanceChange implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _created
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _created;

    /**
     * Field _restored
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _restored;

    /**
     * Field _managed
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _managed;

    /**
     * Field _unmanaged
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _unmanaged;

    /**
     * Field _deleted
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _deleted;

    /**
     * Field _appsConsolidation
     */
    private com.cisco.eManager.common.inventory2.AppsConsolidationNotification _appsConsolidation;


      //----------------/
     //- Constructors -/
    //----------------/

    public AppInstanceChange() {
        super();
    } //-- com.cisco.eManager.common.inventory2.AppInstanceChange()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'appsConsolidation'.
     * 
     * @return the value of field 'appsConsolidation'.
     */
    public com.cisco.eManager.common.inventory2.AppsConsolidationNotification getAppsConsolidation()
    {
        return this._appsConsolidation;
    } //-- com.cisco.eManager.common.inventory2.AppsConsolidationNotification getAppsConsolidation() 

    /**
     * Returns the value of field 'created'.
     * 
     * @return the value of field 'created'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getCreated()
    {
        return this._created;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getCreated() 

    /**
     * Returns the value of field 'deleted'.
     * 
     * @return the value of field 'deleted'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getDeleted()
    {
        return this._deleted;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getDeleted() 

    /**
     * Returns the value of field 'managed'.
     * 
     * @return the value of field 'managed'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getManaged()
    {
        return this._managed;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getManaged() 

    /**
     * Returns the value of field 'restored'.
     * 
     * @return the value of field 'restored'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getRestored()
    {
        return this._restored;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getRestored() 

    /**
     * Returns the value of field 'unmanaged'.
     * 
     * @return the value of field 'unmanaged'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getUnmanaged()
    {
        return this._unmanaged;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getUnmanaged() 

    /**
     * Method isValid
     */
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
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'appsConsolidation'.
     * 
     * @param appsConsolidation the value of field
     * 'appsConsolidation'.
     */
    public void setAppsConsolidation(com.cisco.eManager.common.inventory2.AppsConsolidationNotification appsConsolidation)
    {
        this._appsConsolidation = appsConsolidation;
    } //-- void setAppsConsolidation(com.cisco.eManager.common.inventory2.AppsConsolidationNotification) 

    /**
     * Sets the value of field 'created'.
     * 
     * @param created the value of field 'created'.
     */
    public void setCreated(com.cisco.eManager.common.inventory2.ManagedObjectId created)
    {
        this._created = created;
    } //-- void setCreated(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Sets the value of field 'deleted'.
     * 
     * @param deleted the value of field 'deleted'.
     */
    public void setDeleted(com.cisco.eManager.common.inventory2.ManagedObjectId deleted)
    {
        this._deleted = deleted;
    } //-- void setDeleted(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Sets the value of field 'managed'.
     * 
     * @param managed the value of field 'managed'.
     */
    public void setManaged(com.cisco.eManager.common.inventory2.ManagedObjectId managed)
    {
        this._managed = managed;
    } //-- void setManaged(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Sets the value of field 'restored'.
     * 
     * @param restored the value of field 'restored'.
     */
    public void setRestored(com.cisco.eManager.common.inventory2.ManagedObjectId restored)
    {
        this._restored = restored;
    } //-- void setRestored(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Sets the value of field 'unmanaged'.
     * 
     * @param unmanaged the value of field 'unmanaged'.
     */
    public void setUnmanaged(com.cisco.eManager.common.inventory2.ManagedObjectId unmanaged)
    {
        this._unmanaged = unmanaged;
    } //-- void setUnmanaged(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method unmarshalAppInstanceChange
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAppInstanceChange(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.AppInstanceChange) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.AppInstanceChange.class, reader);
    } //-- java.lang.Object unmarshalAppInstanceChange(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

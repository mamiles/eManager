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
 * Class HostChange.
 * 
 * @version $Revision$ $Date$
 */
public class HostChange implements java.io.Serializable {


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
     * Field _deleted
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _deleted;


      //----------------/
     //- Constructors -/
    //----------------/

    public HostChange() {
        super();
    } //-- com.cisco.eManager.common.inventory2.HostChange()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'restored'.
     * 
     * @return the value of field 'restored'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getRestored()
    {
        return this._restored;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getRestored() 

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
     * Sets the value of field 'restored'.
     * 
     * @param restored the value of field 'restored'.
     */
    public void setRestored(com.cisco.eManager.common.inventory2.ManagedObjectId restored)
    {
        this._restored = restored;
    } //-- void setRestored(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method unmarshalHostChange
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalHostChange(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.HostChange) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.HostChange.class, reader);
    } //-- java.lang.Object unmarshalHostChange(java.io.Reader) 

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

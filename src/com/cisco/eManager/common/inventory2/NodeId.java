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
 * Class NodeId.
 * 
 * @version $Revision$ $Date$
 */
public class NodeId implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _id;

    /**
     * Field _fdn
     */
    private com.cisco.eManager.common.inventory2.FullyDistinguishedName _fdn;


      //----------------/
     //- Constructors -/
    //----------------/

    public NodeId() {
        super();
    } //-- com.cisco.eManager.common.inventory2.NodeId()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'fdn'.
     * 
     * @return the value of field 'fdn'.
     */
    public com.cisco.eManager.common.inventory2.FullyDistinguishedName getFdn()
    {
        return this._fdn;
    } //-- com.cisco.eManager.common.inventory2.FullyDistinguishedName getFdn() 

    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'id'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getId()
    {
        return this._id;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getId() 

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
     * Sets the value of field 'fdn'.
     * 
     * @param fdn the value of field 'fdn'.
     */
    public void setFdn(com.cisco.eManager.common.inventory2.FullyDistinguishedName fdn)
    {
        this._fdn = fdn;
    } //-- void setFdn(com.cisco.eManager.common.inventory2.FullyDistinguishedName) 

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(com.cisco.eManager.common.inventory2.ManagedObjectId id)
    {
        this._id = id;
    } //-- void setId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method unmarshalNodeId
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalNodeId(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.NodeId) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.NodeId.class, reader);
    } //-- java.lang.Object unmarshalNodeId(java.io.Reader) 

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

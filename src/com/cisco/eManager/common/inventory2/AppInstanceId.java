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
 * Class AppInstanceId.
 * 
 * @version $Revision$ $Date$
 */
public class AppInstanceId implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _id;

    /**
     * Field _typeAndName
     */
    private com.cisco.eManager.common.inventory2.AppTypeInstanceName _typeAndName;

    /**
     * Apps View FDN
     */
    private com.cisco.eManager.common.inventory2.FullyDistinguishedName _avFdn;

    /**
     * Physical View FDN
     */
    private com.cisco.eManager.common.inventory2.FullyDistinguishedName _pvFdn;


      //----------------/
     //- Constructors -/
    //----------------/

    public AppInstanceId() {
        super();
    } //-- com.cisco.eManager.common.inventory2.AppInstanceId()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'avFdn'. The field 'avFdn' has
     * the following description: Apps View FDN
     * 
     * @return the value of field 'avFdn'.
     */
    public com.cisco.eManager.common.inventory2.FullyDistinguishedName getAvFdn()
    {
        return this._avFdn;
    } //-- com.cisco.eManager.common.inventory2.FullyDistinguishedName getAvFdn() 

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
     * Returns the value of field 'pvFdn'. The field 'pvFdn' has
     * the following description: Physical View FDN
     * 
     * @return the value of field 'pvFdn'.
     */
    public com.cisco.eManager.common.inventory2.FullyDistinguishedName getPvFdn()
    {
        return this._pvFdn;
    } //-- com.cisco.eManager.common.inventory2.FullyDistinguishedName getPvFdn() 

    /**
     * Returns the value of field 'typeAndName'.
     * 
     * @return the value of field 'typeAndName'.
     */
    public com.cisco.eManager.common.inventory2.AppTypeInstanceName getTypeAndName()
    {
        return this._typeAndName;
    } //-- com.cisco.eManager.common.inventory2.AppTypeInstanceName getTypeAndName() 

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
     * Sets the value of field 'avFdn'. The field 'avFdn' has the
     * following description: Apps View FDN
     * 
     * @param avFdn the value of field 'avFdn'.
     */
    public void setAvFdn(com.cisco.eManager.common.inventory2.FullyDistinguishedName avFdn)
    {
        this._avFdn = avFdn;
    } //-- void setAvFdn(com.cisco.eManager.common.inventory2.FullyDistinguishedName) 

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
     * Sets the value of field 'pvFdn'. The field 'pvFdn' has the
     * following description: Physical View FDN
     * 
     * @param pvFdn the value of field 'pvFdn'.
     */
    public void setPvFdn(com.cisco.eManager.common.inventory2.FullyDistinguishedName pvFdn)
    {
        this._pvFdn = pvFdn;
    } //-- void setPvFdn(com.cisco.eManager.common.inventory2.FullyDistinguishedName) 

    /**
     * Sets the value of field 'typeAndName'.
     * 
     * @param typeAndName the value of field 'typeAndName'.
     */
    public void setTypeAndName(com.cisco.eManager.common.inventory2.AppTypeInstanceName typeAndName)
    {
        this._typeAndName = typeAndName;
    } //-- void setTypeAndName(com.cisco.eManager.common.inventory2.AppTypeInstanceName) 

    /**
     * Method unmarshalAppInstanceId
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAppInstanceId(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.AppInstanceId) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.AppInstanceId.class, reader);
    } //-- java.lang.Object unmarshalAppInstanceId(java.io.Reader) 

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

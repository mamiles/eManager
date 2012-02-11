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
 * Class SolutionId.
 * 
 * @version $Revision$ $Date$
 */
public class SolutionId implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _id;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Solution View FDN
     */
    private com.cisco.eManager.common.inventory2.FullyDistinguishedName _svFdn;


      //----------------/
     //- Constructors -/
    //----------------/

    public SolutionId() {
        super();
    } //-- com.cisco.eManager.common.inventory2.SolutionId()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'svFdn'. The field 'svFdn' has
     * the following description: Solution View FDN
     * 
     * @return the value of field 'svFdn'.
     */
    public com.cisco.eManager.common.inventory2.FullyDistinguishedName getSvFdn()
    {
        return this._svFdn;
    } //-- com.cisco.eManager.common.inventory2.FullyDistinguishedName getSvFdn() 

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
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(com.cisco.eManager.common.inventory2.ManagedObjectId id)
    {
        this._id = id;
    } //-- void setId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'svFdn'. The field 'svFdn' has the
     * following description: Solution View FDN
     * 
     * @param svFdn the value of field 'svFdn'.
     */
    public void setSvFdn(com.cisco.eManager.common.inventory2.FullyDistinguishedName svFdn)
    {
        this._svFdn = svFdn;
    } //-- void setSvFdn(com.cisco.eManager.common.inventory2.FullyDistinguishedName) 

    /**
     * Method unmarshalSolutionId
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSolutionId(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.SolutionId) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.SolutionId.class, reader);
    } //-- java.lang.Object unmarshalSolutionId(java.io.Reader) 

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

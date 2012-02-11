/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cisco.eManager.common.register.solutionRegistration;

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
 * Class SolutionRegistration.
 * 
 * @version $Revision$ $Date$
 */
public class SolutionRegistration implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _solutionName
     */
    private java.lang.String _solutionName;

    /**
     * Field _components
     */
    private com.cisco.eManager.common.register.solutionRegistration.Components _components;


      //----------------/
     //- Constructors -/
    //----------------/

    public SolutionRegistration() {
        super();
    } //-- com.cisco.eManager.common.register.solutionRegistration.SolutionRegistration()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'components'.
     * 
     * @return the value of field 'components'.
     */
    public com.cisco.eManager.common.register.solutionRegistration.Components getComponents()
    {
        return this._components;
    } //-- com.cisco.eManager.common.register.solutionRegistration.Components getComponents() 

    /**
     * Returns the value of field 'solutionName'.
     * 
     * @return the value of field 'solutionName'.
     */
    public java.lang.String getSolutionName()
    {
        return this._solutionName;
    } //-- java.lang.String getSolutionName() 

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
     * Sets the value of field 'components'.
     * 
     * @param components the value of field 'components'.
     */
    public void setComponents(com.cisco.eManager.common.register.solutionRegistration.Components components)
    {
        this._components = components;
    } //-- void setComponents(com.cisco.eManager.common.register.solutionRegistration.Components) 

    /**
     * Sets the value of field 'solutionName'.
     * 
     * @param solutionName the value of field 'solutionName'.
     */
    public void setSolutionName(java.lang.String solutionName)
    {
        this._solutionName = solutionName;
    } //-- void setSolutionName(java.lang.String) 

    /**
     * Method unmarshalSolutionRegistration
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSolutionRegistration(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.register.solutionRegistration.SolutionRegistration) Unmarshaller.unmarshal(com.cisco.eManager.common.register.solutionRegistration.SolutionRegistration.class, reader);
    } //-- java.lang.Object unmarshalSolutionRegistration(java.io.Reader) 

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

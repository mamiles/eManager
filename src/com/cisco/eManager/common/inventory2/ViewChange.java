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
 * Class ViewChange.
 * 
 * @version $Revision$ $Date$
 */
public class ViewChange implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _appsViewChange
     */
    private com.cisco.eManager.common.inventory2.AppsViewChange _appsViewChange;

    /**
     * Field _physicalViewChange
     */
    private com.cisco.eManager.common.inventory2.PhysicalViewChange _physicalViewChange;

    /**
     * Field _solutionViewChange
     */
    private com.cisco.eManager.common.inventory2.SolutionViewChange _solutionViewChange;


      //----------------/
     //- Constructors -/
    //----------------/

    public ViewChange() {
        super();
    } //-- com.cisco.eManager.common.inventory2.ViewChange()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'appsViewChange'.
     * 
     * @return the value of field 'appsViewChange'.
     */
    public com.cisco.eManager.common.inventory2.AppsViewChange getAppsViewChange()
    {
        return this._appsViewChange;
    } //-- com.cisco.eManager.common.inventory2.AppsViewChange getAppsViewChange() 

    /**
     * Returns the value of field 'physicalViewChange'.
     * 
     * @return the value of field 'physicalViewChange'.
     */
    public com.cisco.eManager.common.inventory2.PhysicalViewChange getPhysicalViewChange()
    {
        return this._physicalViewChange;
    } //-- com.cisco.eManager.common.inventory2.PhysicalViewChange getPhysicalViewChange() 

    /**
     * Returns the value of field 'solutionViewChange'.
     * 
     * @return the value of field 'solutionViewChange'.
     */
    public com.cisco.eManager.common.inventory2.SolutionViewChange getSolutionViewChange()
    {
        return this._solutionViewChange;
    } //-- com.cisco.eManager.common.inventory2.SolutionViewChange getSolutionViewChange() 

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
     * Sets the value of field 'appsViewChange'.
     * 
     * @param appsViewChange the value of field 'appsViewChange'.
     */
    public void setAppsViewChange(com.cisco.eManager.common.inventory2.AppsViewChange appsViewChange)
    {
        this._appsViewChange = appsViewChange;
    } //-- void setAppsViewChange(com.cisco.eManager.common.inventory2.AppsViewChange) 

    /**
     * Sets the value of field 'physicalViewChange'.
     * 
     * @param physicalViewChange the value of field
     * 'physicalViewChange'.
     */
    public void setPhysicalViewChange(com.cisco.eManager.common.inventory2.PhysicalViewChange physicalViewChange)
    {
        this._physicalViewChange = physicalViewChange;
    } //-- void setPhysicalViewChange(com.cisco.eManager.common.inventory2.PhysicalViewChange) 

    /**
     * Sets the value of field 'solutionViewChange'.
     * 
     * @param solutionViewChange the value of field
     * 'solutionViewChange'.
     */
    public void setSolutionViewChange(com.cisco.eManager.common.inventory2.SolutionViewChange solutionViewChange)
    {
        this._solutionViewChange = solutionViewChange;
    } //-- void setSolutionViewChange(com.cisco.eManager.common.inventory2.SolutionViewChange) 

    /**
     * Method unmarshalViewChange
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalViewChange(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.ViewChange) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.ViewChange.class, reader);
    } //-- java.lang.Object unmarshalViewChange(java.io.Reader) 

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

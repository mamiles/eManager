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
 * Class SolutionResp.
 * 
 * @version $Revision$ $Date$
 */
public class SolutionResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _sRspGetHosts
     */
    private com.cisco.eManager.common.inventory2.SRspGetHosts _sRspGetHosts;


      //----------------/
     //- Constructors -/
    //----------------/

    public SolutionResp() {
        super();
    } //-- com.cisco.eManager.common.inventory2.SolutionResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'sRspGetHosts'.
     * 
     * @return the value of field 'sRspGetHosts'.
     */
    public com.cisco.eManager.common.inventory2.SRspGetHosts getSRspGetHosts()
    {
        return this._sRspGetHosts;
    } //-- com.cisco.eManager.common.inventory2.SRspGetHosts getSRspGetHosts() 

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
     * Sets the value of field 'sRspGetHosts'.
     * 
     * @param sRspGetHosts the value of field 'sRspGetHosts'.
     */
    public void setSRspGetHosts(com.cisco.eManager.common.inventory2.SRspGetHosts sRspGetHosts)
    {
        this._sRspGetHosts = sRspGetHosts;
    } //-- void setSRspGetHosts(com.cisco.eManager.common.inventory2.SRspGetHosts) 

    /**
     * Method unmarshalSolutionResp
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSolutionResp(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.SolutionResp) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.SolutionResp.class, reader);
    } //-- java.lang.Object unmarshalSolutionResp(java.io.Reader) 

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

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
 * Class AvRspCreateNode.
 * 
 * @version $Revision$ $Date$
 */
public class AvRspCreateNode implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _node
     */
    private com.cisco.eManager.common.inventory2.ContainerNode _node;


      //----------------/
     //- Constructors -/
    //----------------/

    public AvRspCreateNode() {
        super();
    } //-- com.cisco.eManager.common.inventory2.AvRspCreateNode()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'node'.
     * 
     * @return the value of field 'node'.
     */
    public com.cisco.eManager.common.inventory2.ContainerNode getNode()
    {
        return this._node;
    } //-- com.cisco.eManager.common.inventory2.ContainerNode getNode() 

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
     * Sets the value of field 'node'.
     * 
     * @param node the value of field 'node'.
     */
    public void setNode(com.cisco.eManager.common.inventory2.ContainerNode node)
    {
        this._node = node;
    } //-- void setNode(com.cisco.eManager.common.inventory2.ContainerNode) 

    /**
     * Method unmarshalAvRspCreateNode
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAvRspCreateNode(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.AvRspCreateNode) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.AvRspCreateNode.class, reader);
    } //-- java.lang.Object unmarshalAvRspCreateNode(java.io.Reader) 

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

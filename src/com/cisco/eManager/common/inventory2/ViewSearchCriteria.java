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
 * Class ViewSearchCriteria.
 * 
 * @version $Revision$ $Date$
 */
public class ViewSearchCriteria implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * zero orone match possible
     */
    private com.cisco.eManager.common.inventory2.NodeId _nodeId;

    /**
     * many matches possible
     */
    private java.lang.String _nodeName;


      //----------------/
     //- Constructors -/
    //----------------/

    public ViewSearchCriteria() {
        super();
    } //-- com.cisco.eManager.common.inventory2.ViewSearchCriteria()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'nodeId'. The field 'nodeId' has
     * the following description: zero orone match possible
     * 
     * @return the value of field 'nodeId'.
     */
    public com.cisco.eManager.common.inventory2.NodeId getNodeId()
    {
        return this._nodeId;
    } //-- com.cisco.eManager.common.inventory2.NodeId getNodeId() 

    /**
     * Returns the value of field 'nodeName'. The field 'nodeName'
     * has the following description: many matches possible
     * 
     * @return the value of field 'nodeName'.
     */
    public java.lang.String getNodeName()
    {
        return this._nodeName;
    } //-- java.lang.String getNodeName() 

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
     * Sets the value of field 'nodeId'. The field 'nodeId' has the
     * following description: zero orone match possible
     * 
     * @param nodeId the value of field 'nodeId'.
     */
    public void setNodeId(com.cisco.eManager.common.inventory2.NodeId nodeId)
    {
        this._nodeId = nodeId;
    } //-- void setNodeId(com.cisco.eManager.common.inventory2.NodeId) 

    /**
     * Sets the value of field 'nodeName'. The field 'nodeName' has
     * the following description: many matches possible
     * 
     * @param nodeName the value of field 'nodeName'.
     */
    public void setNodeName(java.lang.String nodeName)
    {
        this._nodeName = nodeName;
    } //-- void setNodeName(java.lang.String) 

    /**
     * Method unmarshalViewSearchCriteria
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalViewSearchCriteria(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.ViewSearchCriteria) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.ViewSearchCriteria.class, reader);
    } //-- java.lang.Object unmarshalViewSearchCriteria(java.io.Reader) 

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

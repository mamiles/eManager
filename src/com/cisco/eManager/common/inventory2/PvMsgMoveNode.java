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
 * Class PvMsgMoveNode.
 * 
 * @version $Revision$ $Date$
 */
public class PvMsgMoveNode implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _nodeId
     */
    private com.cisco.eManager.common.inventory2.NodeId _nodeId;

    /**
     * new parent node
     */
    private com.cisco.eManager.common.inventory2.NodeId _parentNodeId;


      //----------------/
     //- Constructors -/
    //----------------/

    public PvMsgMoveNode() {
        super();
    } //-- com.cisco.eManager.common.inventory2.PvMsgMoveNode()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'nodeId'.
     * 
     * @return the value of field 'nodeId'.
     */
    public com.cisco.eManager.common.inventory2.NodeId getNodeId()
    {
        return this._nodeId;
    } //-- com.cisco.eManager.common.inventory2.NodeId getNodeId() 

    /**
     * Returns the value of field 'parentNodeId'. The field
     * 'parentNodeId' has the following description: new parent
     * node
     * 
     * @return the value of field 'parentNodeId'.
     */
    public com.cisco.eManager.common.inventory2.NodeId getParentNodeId()
    {
        return this._parentNodeId;
    } //-- com.cisco.eManager.common.inventory2.NodeId getParentNodeId() 

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
     * Sets the value of field 'nodeId'.
     * 
     * @param nodeId the value of field 'nodeId'.
     */
    public void setNodeId(com.cisco.eManager.common.inventory2.NodeId nodeId)
    {
        this._nodeId = nodeId;
    } //-- void setNodeId(com.cisco.eManager.common.inventory2.NodeId) 

    /**
     * Sets the value of field 'parentNodeId'. The field
     * 'parentNodeId' has the following description: new parent
     * node
     * 
     * @param parentNodeId the value of field 'parentNodeId'.
     */
    public void setParentNodeId(com.cisco.eManager.common.inventory2.NodeId parentNodeId)
    {
        this._parentNodeId = parentNodeId;
    } //-- void setParentNodeId(com.cisco.eManager.common.inventory2.NodeId) 

    /**
     * Method unmarshalPvMsgMoveNode
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalPvMsgMoveNode(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.PvMsgMoveNode) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.PvMsgMoveNode.class, reader);
    } //-- java.lang.Object unmarshalPvMsgMoveNode(java.io.Reader) 

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

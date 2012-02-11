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
 * Class ApplicationViewMsg.
 * 
 * @version $Revision$ $Date$
 */
public class ApplicationViewMsg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _avMsgGetRoot
     */
    private java.lang.String _avMsgGetRoot;

    /**
     * Field _avMsgCreateNode
     */
    private com.cisco.eManager.common.inventory2.AvMsgCreateNode _avMsgCreateNode;

    /**
     * Field _avMsgFindNodes
     */
    private com.cisco.eManager.common.inventory2.AvMsgFindNodes _avMsgFindNodes;

    /**
     * Field _avMsgMoveNode
     */
    private com.cisco.eManager.common.inventory2.AvMsgMoveNode _avMsgMoveNode;

    /**
     * Field _avMsgDeleteNode
     */
    private com.cisco.eManager.common.inventory2.NodeId _avMsgDeleteNode;

    /**
     * Field _avMsgGetChildren
     */
    private com.cisco.eManager.common.inventory2.NodeId _avMsgGetChildren;


      //----------------/
     //- Constructors -/
    //----------------/

    public ApplicationViewMsg() {
        super();
    } //-- com.cisco.eManager.common.inventory2.ApplicationViewMsg()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'avMsgCreateNode'.
     * 
     * @return the value of field 'avMsgCreateNode'.
     */
    public com.cisco.eManager.common.inventory2.AvMsgCreateNode getAvMsgCreateNode()
    {
        return this._avMsgCreateNode;
    } //-- com.cisco.eManager.common.inventory2.AvMsgCreateNode getAvMsgCreateNode() 

    /**
     * Returns the value of field 'avMsgDeleteNode'.
     * 
     * @return the value of field 'avMsgDeleteNode'.
     */
    public com.cisco.eManager.common.inventory2.NodeId getAvMsgDeleteNode()
    {
        return this._avMsgDeleteNode;
    } //-- com.cisco.eManager.common.inventory2.NodeId getAvMsgDeleteNode() 

    /**
     * Returns the value of field 'avMsgFindNodes'.
     * 
     * @return the value of field 'avMsgFindNodes'.
     */
    public com.cisco.eManager.common.inventory2.AvMsgFindNodes getAvMsgFindNodes()
    {
        return this._avMsgFindNodes;
    } //-- com.cisco.eManager.common.inventory2.AvMsgFindNodes getAvMsgFindNodes() 

    /**
     * Returns the value of field 'avMsgGetChildren'.
     * 
     * @return the value of field 'avMsgGetChildren'.
     */
    public com.cisco.eManager.common.inventory2.NodeId getAvMsgGetChildren()
    {
        return this._avMsgGetChildren;
    } //-- com.cisco.eManager.common.inventory2.NodeId getAvMsgGetChildren() 

    /**
     * Returns the value of field 'avMsgGetRoot'.
     * 
     * @return the value of field 'avMsgGetRoot'.
     */
    public java.lang.String getAvMsgGetRoot()
    {
        return this._avMsgGetRoot;
    } //-- java.lang.String getAvMsgGetRoot() 

    /**
     * Returns the value of field 'avMsgMoveNode'.
     * 
     * @return the value of field 'avMsgMoveNode'.
     */
    public com.cisco.eManager.common.inventory2.AvMsgMoveNode getAvMsgMoveNode()
    {
        return this._avMsgMoveNode;
    } //-- com.cisco.eManager.common.inventory2.AvMsgMoveNode getAvMsgMoveNode() 

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
     * Sets the value of field 'avMsgCreateNode'.
     * 
     * @param avMsgCreateNode the value of field 'avMsgCreateNode'.
     */
    public void setAvMsgCreateNode(com.cisco.eManager.common.inventory2.AvMsgCreateNode avMsgCreateNode)
    {
        this._avMsgCreateNode = avMsgCreateNode;
    } //-- void setAvMsgCreateNode(com.cisco.eManager.common.inventory2.AvMsgCreateNode) 

    /**
     * Sets the value of field 'avMsgDeleteNode'.
     * 
     * @param avMsgDeleteNode the value of field 'avMsgDeleteNode'.
     */
    public void setAvMsgDeleteNode(com.cisco.eManager.common.inventory2.NodeId avMsgDeleteNode)
    {
        this._avMsgDeleteNode = avMsgDeleteNode;
    } //-- void setAvMsgDeleteNode(com.cisco.eManager.common.inventory2.NodeId) 

    /**
     * Sets the value of field 'avMsgFindNodes'.
     * 
     * @param avMsgFindNodes the value of field 'avMsgFindNodes'.
     */
    public void setAvMsgFindNodes(com.cisco.eManager.common.inventory2.AvMsgFindNodes avMsgFindNodes)
    {
        this._avMsgFindNodes = avMsgFindNodes;
    } //-- void setAvMsgFindNodes(com.cisco.eManager.common.inventory2.AvMsgFindNodes) 

    /**
     * Sets the value of field 'avMsgGetChildren'.
     * 
     * @param avMsgGetChildren the value of field 'avMsgGetChildren'
     */
    public void setAvMsgGetChildren(com.cisco.eManager.common.inventory2.NodeId avMsgGetChildren)
    {
        this._avMsgGetChildren = avMsgGetChildren;
    } //-- void setAvMsgGetChildren(com.cisco.eManager.common.inventory2.NodeId) 

    /**
     * Sets the value of field 'avMsgGetRoot'.
     * 
     * @param avMsgGetRoot the value of field 'avMsgGetRoot'.
     */
    public void setAvMsgGetRoot(java.lang.String avMsgGetRoot)
    {
        this._avMsgGetRoot = avMsgGetRoot;
    } //-- void setAvMsgGetRoot(java.lang.String) 

    /**
     * Sets the value of field 'avMsgMoveNode'.
     * 
     * @param avMsgMoveNode the value of field 'avMsgMoveNode'.
     */
    public void setAvMsgMoveNode(com.cisco.eManager.common.inventory2.AvMsgMoveNode avMsgMoveNode)
    {
        this._avMsgMoveNode = avMsgMoveNode;
    } //-- void setAvMsgMoveNode(com.cisco.eManager.common.inventory2.AvMsgMoveNode) 

    /**
     * Method unmarshalApplicationViewMsg
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalApplicationViewMsg(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.ApplicationViewMsg) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.ApplicationViewMsg.class, reader);
    } //-- java.lang.Object unmarshalApplicationViewMsg(java.io.Reader) 

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

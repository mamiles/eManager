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
 * Class PhysicalViewMsg.
 * 
 * @version $Revision$ $Date$
 */
public class PhysicalViewMsg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _pvMsgGetRoot
     */
    private java.lang.String _pvMsgGetRoot;

    /**
     * Field _pvMsgCreateNode
     */
    private com.cisco.eManager.common.inventory2.PvMsgCreateNode _pvMsgCreateNode;

    /**
     * Field _pvMsgFindNodes
     */
    private com.cisco.eManager.common.inventory2.PvMsgFindNodes _pvMsgFindNodes;

    /**
     * Field _pvMsgMoveNode
     */
    private com.cisco.eManager.common.inventory2.PvMsgMoveNode _pvMsgMoveNode;

    /**
     * Field _pvMsgDeleteNode
     */
    private com.cisco.eManager.common.inventory2.NodeId _pvMsgDeleteNode;

    /**
     * Field _pvMsgGetChildren
     */
    private com.cisco.eManager.common.inventory2.NodeId _pvMsgGetChildren;


      //----------------/
     //- Constructors -/
    //----------------/

    public PhysicalViewMsg() {
        super();
    } //-- com.cisco.eManager.common.inventory2.PhysicalViewMsg()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'pvMsgCreateNode'.
     * 
     * @return the value of field 'pvMsgCreateNode'.
     */
    public com.cisco.eManager.common.inventory2.PvMsgCreateNode getPvMsgCreateNode()
    {
        return this._pvMsgCreateNode;
    } //-- com.cisco.eManager.common.inventory2.PvMsgCreateNode getPvMsgCreateNode() 

    /**
     * Returns the value of field 'pvMsgDeleteNode'.
     * 
     * @return the value of field 'pvMsgDeleteNode'.
     */
    public com.cisco.eManager.common.inventory2.NodeId getPvMsgDeleteNode()
    {
        return this._pvMsgDeleteNode;
    } //-- com.cisco.eManager.common.inventory2.NodeId getPvMsgDeleteNode() 

    /**
     * Returns the value of field 'pvMsgFindNodes'.
     * 
     * @return the value of field 'pvMsgFindNodes'.
     */
    public com.cisco.eManager.common.inventory2.PvMsgFindNodes getPvMsgFindNodes()
    {
        return this._pvMsgFindNodes;
    } //-- com.cisco.eManager.common.inventory2.PvMsgFindNodes getPvMsgFindNodes() 

    /**
     * Returns the value of field 'pvMsgGetChildren'.
     * 
     * @return the value of field 'pvMsgGetChildren'.
     */
    public com.cisco.eManager.common.inventory2.NodeId getPvMsgGetChildren()
    {
        return this._pvMsgGetChildren;
    } //-- com.cisco.eManager.common.inventory2.NodeId getPvMsgGetChildren() 

    /**
     * Returns the value of field 'pvMsgGetRoot'.
     * 
     * @return the value of field 'pvMsgGetRoot'.
     */
    public java.lang.String getPvMsgGetRoot()
    {
        return this._pvMsgGetRoot;
    } //-- java.lang.String getPvMsgGetRoot() 

    /**
     * Returns the value of field 'pvMsgMoveNode'.
     * 
     * @return the value of field 'pvMsgMoveNode'.
     */
    public com.cisco.eManager.common.inventory2.PvMsgMoveNode getPvMsgMoveNode()
    {
        return this._pvMsgMoveNode;
    } //-- com.cisco.eManager.common.inventory2.PvMsgMoveNode getPvMsgMoveNode() 

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
     * Sets the value of field 'pvMsgCreateNode'.
     * 
     * @param pvMsgCreateNode the value of field 'pvMsgCreateNode'.
     */
    public void setPvMsgCreateNode(com.cisco.eManager.common.inventory2.PvMsgCreateNode pvMsgCreateNode)
    {
        this._pvMsgCreateNode = pvMsgCreateNode;
    } //-- void setPvMsgCreateNode(com.cisco.eManager.common.inventory2.PvMsgCreateNode) 

    /**
     * Sets the value of field 'pvMsgDeleteNode'.
     * 
     * @param pvMsgDeleteNode the value of field 'pvMsgDeleteNode'.
     */
    public void setPvMsgDeleteNode(com.cisco.eManager.common.inventory2.NodeId pvMsgDeleteNode)
    {
        this._pvMsgDeleteNode = pvMsgDeleteNode;
    } //-- void setPvMsgDeleteNode(com.cisco.eManager.common.inventory2.NodeId) 

    /**
     * Sets the value of field 'pvMsgFindNodes'.
     * 
     * @param pvMsgFindNodes the value of field 'pvMsgFindNodes'.
     */
    public void setPvMsgFindNodes(com.cisco.eManager.common.inventory2.PvMsgFindNodes pvMsgFindNodes)
    {
        this._pvMsgFindNodes = pvMsgFindNodes;
    } //-- void setPvMsgFindNodes(com.cisco.eManager.common.inventory2.PvMsgFindNodes) 

    /**
     * Sets the value of field 'pvMsgGetChildren'.
     * 
     * @param pvMsgGetChildren the value of field 'pvMsgGetChildren'
     */
    public void setPvMsgGetChildren(com.cisco.eManager.common.inventory2.NodeId pvMsgGetChildren)
    {
        this._pvMsgGetChildren = pvMsgGetChildren;
    } //-- void setPvMsgGetChildren(com.cisco.eManager.common.inventory2.NodeId) 

    /**
     * Sets the value of field 'pvMsgGetRoot'.
     * 
     * @param pvMsgGetRoot the value of field 'pvMsgGetRoot'.
     */
    public void setPvMsgGetRoot(java.lang.String pvMsgGetRoot)
    {
        this._pvMsgGetRoot = pvMsgGetRoot;
    } //-- void setPvMsgGetRoot(java.lang.String) 

    /**
     * Sets the value of field 'pvMsgMoveNode'.
     * 
     * @param pvMsgMoveNode the value of field 'pvMsgMoveNode'.
     */
    public void setPvMsgMoveNode(com.cisco.eManager.common.inventory2.PvMsgMoveNode pvMsgMoveNode)
    {
        this._pvMsgMoveNode = pvMsgMoveNode;
    } //-- void setPvMsgMoveNode(com.cisco.eManager.common.inventory2.PvMsgMoveNode) 

    /**
     * Method unmarshalPhysicalViewMsg
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalPhysicalViewMsg(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.PhysicalViewMsg) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.PhysicalViewMsg.class, reader);
    } //-- java.lang.Object unmarshalPhysicalViewMsg(java.io.Reader) 

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

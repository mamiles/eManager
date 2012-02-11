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
 * Class SolutionViewMsg.
 * 
 * @version $Revision$ $Date$
 */
public class SolutionViewMsg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _svMsgGetRoot
     */
    private java.lang.String _svMsgGetRoot;

    /**
     * Field _svMsgCreateNode
     */
    private com.cisco.eManager.common.inventory2.SvMsgCreateNode _svMsgCreateNode;

    /**
     * Field _svMsgFindNodes
     */
    private com.cisco.eManager.common.inventory2.SvMsgFindNodes _svMsgFindNodes;

    /**
     * Field _svMsgMoveNode
     */
    private com.cisco.eManager.common.inventory2.SvMsgMoveNode _svMsgMoveNode;

    /**
     * Field _svMsgDeleteNode
     */
    private com.cisco.eManager.common.inventory2.NodeId _svMsgDeleteNode;

    /**
     * Field _svMsgGetChildren
     */
    private com.cisco.eManager.common.inventory2.NodeId _svMsgGetChildren;


      //----------------/
     //- Constructors -/
    //----------------/

    public SolutionViewMsg() {
        super();
    } //-- com.cisco.eManager.common.inventory2.SolutionViewMsg()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'svMsgCreateNode'.
     * 
     * @return the value of field 'svMsgCreateNode'.
     */
    public com.cisco.eManager.common.inventory2.SvMsgCreateNode getSvMsgCreateNode()
    {
        return this._svMsgCreateNode;
    } //-- com.cisco.eManager.common.inventory2.SvMsgCreateNode getSvMsgCreateNode() 

    /**
     * Returns the value of field 'svMsgDeleteNode'.
     * 
     * @return the value of field 'svMsgDeleteNode'.
     */
    public com.cisco.eManager.common.inventory2.NodeId getSvMsgDeleteNode()
    {
        return this._svMsgDeleteNode;
    } //-- com.cisco.eManager.common.inventory2.NodeId getSvMsgDeleteNode() 

    /**
     * Returns the value of field 'svMsgFindNodes'.
     * 
     * @return the value of field 'svMsgFindNodes'.
     */
    public com.cisco.eManager.common.inventory2.SvMsgFindNodes getSvMsgFindNodes()
    {
        return this._svMsgFindNodes;
    } //-- com.cisco.eManager.common.inventory2.SvMsgFindNodes getSvMsgFindNodes() 

    /**
     * Returns the value of field 'svMsgGetChildren'.
     * 
     * @return the value of field 'svMsgGetChildren'.
     */
    public com.cisco.eManager.common.inventory2.NodeId getSvMsgGetChildren()
    {
        return this._svMsgGetChildren;
    } //-- com.cisco.eManager.common.inventory2.NodeId getSvMsgGetChildren() 

    /**
     * Returns the value of field 'svMsgGetRoot'.
     * 
     * @return the value of field 'svMsgGetRoot'.
     */
    public java.lang.String getSvMsgGetRoot()
    {
        return this._svMsgGetRoot;
    } //-- java.lang.String getSvMsgGetRoot() 

    /**
     * Returns the value of field 'svMsgMoveNode'.
     * 
     * @return the value of field 'svMsgMoveNode'.
     */
    public com.cisco.eManager.common.inventory2.SvMsgMoveNode getSvMsgMoveNode()
    {
        return this._svMsgMoveNode;
    } //-- com.cisco.eManager.common.inventory2.SvMsgMoveNode getSvMsgMoveNode() 

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
     * Sets the value of field 'svMsgCreateNode'.
     * 
     * @param svMsgCreateNode the value of field 'svMsgCreateNode'.
     */
    public void setSvMsgCreateNode(com.cisco.eManager.common.inventory2.SvMsgCreateNode svMsgCreateNode)
    {
        this._svMsgCreateNode = svMsgCreateNode;
    } //-- void setSvMsgCreateNode(com.cisco.eManager.common.inventory2.SvMsgCreateNode) 

    /**
     * Sets the value of field 'svMsgDeleteNode'.
     * 
     * @param svMsgDeleteNode the value of field 'svMsgDeleteNode'.
     */
    public void setSvMsgDeleteNode(com.cisco.eManager.common.inventory2.NodeId svMsgDeleteNode)
    {
        this._svMsgDeleteNode = svMsgDeleteNode;
    } //-- void setSvMsgDeleteNode(com.cisco.eManager.common.inventory2.NodeId) 

    /**
     * Sets the value of field 'svMsgFindNodes'.
     * 
     * @param svMsgFindNodes the value of field 'svMsgFindNodes'.
     */
    public void setSvMsgFindNodes(com.cisco.eManager.common.inventory2.SvMsgFindNodes svMsgFindNodes)
    {
        this._svMsgFindNodes = svMsgFindNodes;
    } //-- void setSvMsgFindNodes(com.cisco.eManager.common.inventory2.SvMsgFindNodes) 

    /**
     * Sets the value of field 'svMsgGetChildren'.
     * 
     * @param svMsgGetChildren the value of field 'svMsgGetChildren'
     */
    public void setSvMsgGetChildren(com.cisco.eManager.common.inventory2.NodeId svMsgGetChildren)
    {
        this._svMsgGetChildren = svMsgGetChildren;
    } //-- void setSvMsgGetChildren(com.cisco.eManager.common.inventory2.NodeId) 

    /**
     * Sets the value of field 'svMsgGetRoot'.
     * 
     * @param svMsgGetRoot the value of field 'svMsgGetRoot'.
     */
    public void setSvMsgGetRoot(java.lang.String svMsgGetRoot)
    {
        this._svMsgGetRoot = svMsgGetRoot;
    } //-- void setSvMsgGetRoot(java.lang.String) 

    /**
     * Sets the value of field 'svMsgMoveNode'.
     * 
     * @param svMsgMoveNode the value of field 'svMsgMoveNode'.
     */
    public void setSvMsgMoveNode(com.cisco.eManager.common.inventory2.SvMsgMoveNode svMsgMoveNode)
    {
        this._svMsgMoveNode = svMsgMoveNode;
    } //-- void setSvMsgMoveNode(com.cisco.eManager.common.inventory2.SvMsgMoveNode) 

    /**
     * Method unmarshalSolutionViewMsg
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSolutionViewMsg(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.SolutionViewMsg) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.SolutionViewMsg.class, reader);
    } //-- java.lang.Object unmarshalSolutionViewMsg(java.io.Reader) 

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

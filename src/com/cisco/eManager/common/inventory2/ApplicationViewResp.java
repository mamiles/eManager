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
 * Class ApplicationViewResp.
 * 
 * @version $Revision$ $Date$
 */
public class ApplicationViewResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _avRspGetRoot
     */
    private com.cisco.eManager.common.inventory2.AvRspGetRoot _avRspGetRoot;

    /**
     * Field _avRspCreateNode
     */
    private com.cisco.eManager.common.inventory2.AvRspCreateNode _avRspCreateNode;

    /**
     * Field _avRspFindNodes
     */
    private com.cisco.eManager.common.inventory2.AvRspFindNodes _avRspFindNodes;

    /**
     * Field _avRspMoveNode
     */
    private com.cisco.eManager.common.inventory2.AvRspMoveNode _avRspMoveNode;

    /**
     * Field _avRspDeleteNode
     */
    private com.cisco.eManager.common.inventory2.AvRspDeleteNode _avRspDeleteNode;

    /**
     * Field _avRspMoveAppType
     */
    private com.cisco.eManager.common.inventory2.AvRspMoveAppType _avRspMoveAppType;

    /**
     * Field _avRspGetChildren
     */
    private com.cisco.eManager.common.inventory2.AvRspGetChildren _avRspGetChildren;


      //----------------/
     //- Constructors -/
    //----------------/

    public ApplicationViewResp() {
        super();
    } //-- com.cisco.eManager.common.inventory2.ApplicationViewResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'avRspCreateNode'.
     * 
     * @return the value of field 'avRspCreateNode'.
     */
    public com.cisco.eManager.common.inventory2.AvRspCreateNode getAvRspCreateNode()
    {
        return this._avRspCreateNode;
    } //-- com.cisco.eManager.common.inventory2.AvRspCreateNode getAvRspCreateNode() 

    /**
     * Returns the value of field 'avRspDeleteNode'.
     * 
     * @return the value of field 'avRspDeleteNode'.
     */
    public com.cisco.eManager.common.inventory2.AvRspDeleteNode getAvRspDeleteNode()
    {
        return this._avRspDeleteNode;
    } //-- com.cisco.eManager.common.inventory2.AvRspDeleteNode getAvRspDeleteNode() 

    /**
     * Returns the value of field 'avRspFindNodes'.
     * 
     * @return the value of field 'avRspFindNodes'.
     */
    public com.cisco.eManager.common.inventory2.AvRspFindNodes getAvRspFindNodes()
    {
        return this._avRspFindNodes;
    } //-- com.cisco.eManager.common.inventory2.AvRspFindNodes getAvRspFindNodes() 

    /**
     * Returns the value of field 'avRspGetChildren'.
     * 
     * @return the value of field 'avRspGetChildren'.
     */
    public com.cisco.eManager.common.inventory2.AvRspGetChildren getAvRspGetChildren()
    {
        return this._avRspGetChildren;
    } //-- com.cisco.eManager.common.inventory2.AvRspGetChildren getAvRspGetChildren() 

    /**
     * Returns the value of field 'avRspGetRoot'.
     * 
     * @return the value of field 'avRspGetRoot'.
     */
    public com.cisco.eManager.common.inventory2.AvRspGetRoot getAvRspGetRoot()
    {
        return this._avRspGetRoot;
    } //-- com.cisco.eManager.common.inventory2.AvRspGetRoot getAvRspGetRoot() 

    /**
     * Returns the value of field 'avRspMoveAppType'.
     * 
     * @return the value of field 'avRspMoveAppType'.
     */
    public com.cisco.eManager.common.inventory2.AvRspMoveAppType getAvRspMoveAppType()
    {
        return this._avRspMoveAppType;
    } //-- com.cisco.eManager.common.inventory2.AvRspMoveAppType getAvRspMoveAppType() 

    /**
     * Returns the value of field 'avRspMoveNode'.
     * 
     * @return the value of field 'avRspMoveNode'.
     */
    public com.cisco.eManager.common.inventory2.AvRspMoveNode getAvRspMoveNode()
    {
        return this._avRspMoveNode;
    } //-- com.cisco.eManager.common.inventory2.AvRspMoveNode getAvRspMoveNode() 

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
     * Sets the value of field 'avRspCreateNode'.
     * 
     * @param avRspCreateNode the value of field 'avRspCreateNode'.
     */
    public void setAvRspCreateNode(com.cisco.eManager.common.inventory2.AvRspCreateNode avRspCreateNode)
    {
        this._avRspCreateNode = avRspCreateNode;
    } //-- void setAvRspCreateNode(com.cisco.eManager.common.inventory2.AvRspCreateNode) 

    /**
     * Sets the value of field 'avRspDeleteNode'.
     * 
     * @param avRspDeleteNode the value of field 'avRspDeleteNode'.
     */
    public void setAvRspDeleteNode(com.cisco.eManager.common.inventory2.AvRspDeleteNode avRspDeleteNode)
    {
        this._avRspDeleteNode = avRspDeleteNode;
    } //-- void setAvRspDeleteNode(com.cisco.eManager.common.inventory2.AvRspDeleteNode) 

    /**
     * Sets the value of field 'avRspFindNodes'.
     * 
     * @param avRspFindNodes the value of field 'avRspFindNodes'.
     */
    public void setAvRspFindNodes(com.cisco.eManager.common.inventory2.AvRspFindNodes avRspFindNodes)
    {
        this._avRspFindNodes = avRspFindNodes;
    } //-- void setAvRspFindNodes(com.cisco.eManager.common.inventory2.AvRspFindNodes) 

    /**
     * Sets the value of field 'avRspGetChildren'.
     * 
     * @param avRspGetChildren the value of field 'avRspGetChildren'
     */
    public void setAvRspGetChildren(com.cisco.eManager.common.inventory2.AvRspGetChildren avRspGetChildren)
    {
        this._avRspGetChildren = avRspGetChildren;
    } //-- void setAvRspGetChildren(com.cisco.eManager.common.inventory2.AvRspGetChildren) 

    /**
     * Sets the value of field 'avRspGetRoot'.
     * 
     * @param avRspGetRoot the value of field 'avRspGetRoot'.
     */
    public void setAvRspGetRoot(com.cisco.eManager.common.inventory2.AvRspGetRoot avRspGetRoot)
    {
        this._avRspGetRoot = avRspGetRoot;
    } //-- void setAvRspGetRoot(com.cisco.eManager.common.inventory2.AvRspGetRoot) 

    /**
     * Sets the value of field 'avRspMoveAppType'.
     * 
     * @param avRspMoveAppType the value of field 'avRspMoveAppType'
     */
    public void setAvRspMoveAppType(com.cisco.eManager.common.inventory2.AvRspMoveAppType avRspMoveAppType)
    {
        this._avRspMoveAppType = avRspMoveAppType;
    } //-- void setAvRspMoveAppType(com.cisco.eManager.common.inventory2.AvRspMoveAppType) 

    /**
     * Sets the value of field 'avRspMoveNode'.
     * 
     * @param avRspMoveNode the value of field 'avRspMoveNode'.
     */
    public void setAvRspMoveNode(com.cisco.eManager.common.inventory2.AvRspMoveNode avRspMoveNode)
    {
        this._avRspMoveNode = avRspMoveNode;
    } //-- void setAvRspMoveNode(com.cisco.eManager.common.inventory2.AvRspMoveNode) 

    /**
     * Method unmarshalApplicationViewResp
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalApplicationViewResp(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.ApplicationViewResp) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.ApplicationViewResp.class, reader);
    } //-- java.lang.Object unmarshalApplicationViewResp(java.io.Reader) 

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

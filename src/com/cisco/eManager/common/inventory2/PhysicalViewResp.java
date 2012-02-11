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
 * Class PhysicalViewResp.
 * 
 * @version $Revision$ $Date$
 */
public class PhysicalViewResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _pvRspGetRoot
     */
    private com.cisco.eManager.common.inventory2.PvRspGetRoot _pvRspGetRoot;

    /**
     * Field _pvRspCreateNode
     */
    private com.cisco.eManager.common.inventory2.PvRspCreateNode _pvRspCreateNode;

    /**
     * Field _pvRspFindNodes
     */
    private com.cisco.eManager.common.inventory2.PvRspFindNodes _pvRspFindNodes;

    /**
     * Field _pvRspMoveNode
     */
    private com.cisco.eManager.common.inventory2.PvRspMoveNode _pvRspMoveNode;

    /**
     * Field _pvRspDeleteNode
     */
    private com.cisco.eManager.common.inventory2.PvRspDeleteNode _pvRspDeleteNode;

    /**
     * Field _pvRspMoveAppType
     */
    private com.cisco.eManager.common.inventory2.PvRspMoveAppType _pvRspMoveAppType;

    /**
     * Field _pvRspGetChildren
     */
    private com.cisco.eManager.common.inventory2.PvRspGetChildren _pvRspGetChildren;


      //----------------/
     //- Constructors -/
    //----------------/

    public PhysicalViewResp() {
        super();
    } //-- com.cisco.eManager.common.inventory2.PhysicalViewResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'pvRspCreateNode'.
     * 
     * @return the value of field 'pvRspCreateNode'.
     */
    public com.cisco.eManager.common.inventory2.PvRspCreateNode getPvRspCreateNode()
    {
        return this._pvRspCreateNode;
    } //-- com.cisco.eManager.common.inventory2.PvRspCreateNode getPvRspCreateNode() 

    /**
     * Returns the value of field 'pvRspDeleteNode'.
     * 
     * @return the value of field 'pvRspDeleteNode'.
     */
    public com.cisco.eManager.common.inventory2.PvRspDeleteNode getPvRspDeleteNode()
    {
        return this._pvRspDeleteNode;
    } //-- com.cisco.eManager.common.inventory2.PvRspDeleteNode getPvRspDeleteNode() 

    /**
     * Returns the value of field 'pvRspFindNodes'.
     * 
     * @return the value of field 'pvRspFindNodes'.
     */
    public com.cisco.eManager.common.inventory2.PvRspFindNodes getPvRspFindNodes()
    {
        return this._pvRspFindNodes;
    } //-- com.cisco.eManager.common.inventory2.PvRspFindNodes getPvRspFindNodes() 

    /**
     * Returns the value of field 'pvRspGetChildren'.
     * 
     * @return the value of field 'pvRspGetChildren'.
     */
    public com.cisco.eManager.common.inventory2.PvRspGetChildren getPvRspGetChildren()
    {
        return this._pvRspGetChildren;
    } //-- com.cisco.eManager.common.inventory2.PvRspGetChildren getPvRspGetChildren() 

    /**
     * Returns the value of field 'pvRspGetRoot'.
     * 
     * @return the value of field 'pvRspGetRoot'.
     */
    public com.cisco.eManager.common.inventory2.PvRspGetRoot getPvRspGetRoot()
    {
        return this._pvRspGetRoot;
    } //-- com.cisco.eManager.common.inventory2.PvRspGetRoot getPvRspGetRoot() 

    /**
     * Returns the value of field 'pvRspMoveAppType'.
     * 
     * @return the value of field 'pvRspMoveAppType'.
     */
    public com.cisco.eManager.common.inventory2.PvRspMoveAppType getPvRspMoveAppType()
    {
        return this._pvRspMoveAppType;
    } //-- com.cisco.eManager.common.inventory2.PvRspMoveAppType getPvRspMoveAppType() 

    /**
     * Returns the value of field 'pvRspMoveNode'.
     * 
     * @return the value of field 'pvRspMoveNode'.
     */
    public com.cisco.eManager.common.inventory2.PvRspMoveNode getPvRspMoveNode()
    {
        return this._pvRspMoveNode;
    } //-- com.cisco.eManager.common.inventory2.PvRspMoveNode getPvRspMoveNode() 

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
     * Sets the value of field 'pvRspCreateNode'.
     * 
     * @param pvRspCreateNode the value of field 'pvRspCreateNode'.
     */
    public void setPvRspCreateNode(com.cisco.eManager.common.inventory2.PvRspCreateNode pvRspCreateNode)
    {
        this._pvRspCreateNode = pvRspCreateNode;
    } //-- void setPvRspCreateNode(com.cisco.eManager.common.inventory2.PvRspCreateNode) 

    /**
     * Sets the value of field 'pvRspDeleteNode'.
     * 
     * @param pvRspDeleteNode the value of field 'pvRspDeleteNode'.
     */
    public void setPvRspDeleteNode(com.cisco.eManager.common.inventory2.PvRspDeleteNode pvRspDeleteNode)
    {
        this._pvRspDeleteNode = pvRspDeleteNode;
    } //-- void setPvRspDeleteNode(com.cisco.eManager.common.inventory2.PvRspDeleteNode) 

    /**
     * Sets the value of field 'pvRspFindNodes'.
     * 
     * @param pvRspFindNodes the value of field 'pvRspFindNodes'.
     */
    public void setPvRspFindNodes(com.cisco.eManager.common.inventory2.PvRspFindNodes pvRspFindNodes)
    {
        this._pvRspFindNodes = pvRspFindNodes;
    } //-- void setPvRspFindNodes(com.cisco.eManager.common.inventory2.PvRspFindNodes) 

    /**
     * Sets the value of field 'pvRspGetChildren'.
     * 
     * @param pvRspGetChildren the value of field 'pvRspGetChildren'
     */
    public void setPvRspGetChildren(com.cisco.eManager.common.inventory2.PvRspGetChildren pvRspGetChildren)
    {
        this._pvRspGetChildren = pvRspGetChildren;
    } //-- void setPvRspGetChildren(com.cisco.eManager.common.inventory2.PvRspGetChildren) 

    /**
     * Sets the value of field 'pvRspGetRoot'.
     * 
     * @param pvRspGetRoot the value of field 'pvRspGetRoot'.
     */
    public void setPvRspGetRoot(com.cisco.eManager.common.inventory2.PvRspGetRoot pvRspGetRoot)
    {
        this._pvRspGetRoot = pvRspGetRoot;
    } //-- void setPvRspGetRoot(com.cisco.eManager.common.inventory2.PvRspGetRoot) 

    /**
     * Sets the value of field 'pvRspMoveAppType'.
     * 
     * @param pvRspMoveAppType the value of field 'pvRspMoveAppType'
     */
    public void setPvRspMoveAppType(com.cisco.eManager.common.inventory2.PvRspMoveAppType pvRspMoveAppType)
    {
        this._pvRspMoveAppType = pvRspMoveAppType;
    } //-- void setPvRspMoveAppType(com.cisco.eManager.common.inventory2.PvRspMoveAppType) 

    /**
     * Sets the value of field 'pvRspMoveNode'.
     * 
     * @param pvRspMoveNode the value of field 'pvRspMoveNode'.
     */
    public void setPvRspMoveNode(com.cisco.eManager.common.inventory2.PvRspMoveNode pvRspMoveNode)
    {
        this._pvRspMoveNode = pvRspMoveNode;
    } //-- void setPvRspMoveNode(com.cisco.eManager.common.inventory2.PvRspMoveNode) 

    /**
     * Method unmarshalPhysicalViewResp
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalPhysicalViewResp(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.PhysicalViewResp) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.PhysicalViewResp.class, reader);
    } //-- java.lang.Object unmarshalPhysicalViewResp(java.io.Reader) 

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

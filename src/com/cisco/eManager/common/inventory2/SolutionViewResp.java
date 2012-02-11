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
 * Class SolutionViewResp.
 * 
 * @version $Revision$ $Date$
 */
public class SolutionViewResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _svRspGetRoot
     */
    private com.cisco.eManager.common.inventory2.SvRspGetRoot _svRspGetRoot;

    /**
     * Field _svRspCreateNode
     */
    private com.cisco.eManager.common.inventory2.SvRspCreateNode _svRspCreateNode;

    /**
     * Field _svRspFindNodes
     */
    private com.cisco.eManager.common.inventory2.SvRspFindNodes _svRspFindNodes;

    /**
     * Field _svRspMoveNode
     */
    private com.cisco.eManager.common.inventory2.SvRspMoveNode _svRspMoveNode;

    /**
     * Field _svRspDeleteNode
     */
    private com.cisco.eManager.common.inventory2.SvRspDeleteNode _svRspDeleteNode;

    /**
     * Field _svRspGetChildren
     */
    private com.cisco.eManager.common.inventory2.SvRspGetChildren _svRspGetChildren;


      //----------------/
     //- Constructors -/
    //----------------/

    public SolutionViewResp() {
        super();
    } //-- com.cisco.eManager.common.inventory2.SolutionViewResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'svRspCreateNode'.
     * 
     * @return the value of field 'svRspCreateNode'.
     */
    public com.cisco.eManager.common.inventory2.SvRspCreateNode getSvRspCreateNode()
    {
        return this._svRspCreateNode;
    } //-- com.cisco.eManager.common.inventory2.SvRspCreateNode getSvRspCreateNode() 

    /**
     * Returns the value of field 'svRspDeleteNode'.
     * 
     * @return the value of field 'svRspDeleteNode'.
     */
    public com.cisco.eManager.common.inventory2.SvRspDeleteNode getSvRspDeleteNode()
    {
        return this._svRspDeleteNode;
    } //-- com.cisco.eManager.common.inventory2.SvRspDeleteNode getSvRspDeleteNode() 

    /**
     * Returns the value of field 'svRspFindNodes'.
     * 
     * @return the value of field 'svRspFindNodes'.
     */
    public com.cisco.eManager.common.inventory2.SvRspFindNodes getSvRspFindNodes()
    {
        return this._svRspFindNodes;
    } //-- com.cisco.eManager.common.inventory2.SvRspFindNodes getSvRspFindNodes() 

    /**
     * Returns the value of field 'svRspGetChildren'.
     * 
     * @return the value of field 'svRspGetChildren'.
     */
    public com.cisco.eManager.common.inventory2.SvRspGetChildren getSvRspGetChildren()
    {
        return this._svRspGetChildren;
    } //-- com.cisco.eManager.common.inventory2.SvRspGetChildren getSvRspGetChildren() 

    /**
     * Returns the value of field 'svRspGetRoot'.
     * 
     * @return the value of field 'svRspGetRoot'.
     */
    public com.cisco.eManager.common.inventory2.SvRspGetRoot getSvRspGetRoot()
    {
        return this._svRspGetRoot;
    } //-- com.cisco.eManager.common.inventory2.SvRspGetRoot getSvRspGetRoot() 

    /**
     * Returns the value of field 'svRspMoveNode'.
     * 
     * @return the value of field 'svRspMoveNode'.
     */
    public com.cisco.eManager.common.inventory2.SvRspMoveNode getSvRspMoveNode()
    {
        return this._svRspMoveNode;
    } //-- com.cisco.eManager.common.inventory2.SvRspMoveNode getSvRspMoveNode() 

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
     * Sets the value of field 'svRspCreateNode'.
     * 
     * @param svRspCreateNode the value of field 'svRspCreateNode'.
     */
    public void setSvRspCreateNode(com.cisco.eManager.common.inventory2.SvRspCreateNode svRspCreateNode)
    {
        this._svRspCreateNode = svRspCreateNode;
    } //-- void setSvRspCreateNode(com.cisco.eManager.common.inventory2.SvRspCreateNode) 

    /**
     * Sets the value of field 'svRspDeleteNode'.
     * 
     * @param svRspDeleteNode the value of field 'svRspDeleteNode'.
     */
    public void setSvRspDeleteNode(com.cisco.eManager.common.inventory2.SvRspDeleteNode svRspDeleteNode)
    {
        this._svRspDeleteNode = svRspDeleteNode;
    } //-- void setSvRspDeleteNode(com.cisco.eManager.common.inventory2.SvRspDeleteNode) 

    /**
     * Sets the value of field 'svRspFindNodes'.
     * 
     * @param svRspFindNodes the value of field 'svRspFindNodes'.
     */
    public void setSvRspFindNodes(com.cisco.eManager.common.inventory2.SvRspFindNodes svRspFindNodes)
    {
        this._svRspFindNodes = svRspFindNodes;
    } //-- void setSvRspFindNodes(com.cisco.eManager.common.inventory2.SvRspFindNodes) 

    /**
     * Sets the value of field 'svRspGetChildren'.
     * 
     * @param svRspGetChildren the value of field 'svRspGetChildren'
     */
    public void setSvRspGetChildren(com.cisco.eManager.common.inventory2.SvRspGetChildren svRspGetChildren)
    {
        this._svRspGetChildren = svRspGetChildren;
    } //-- void setSvRspGetChildren(com.cisco.eManager.common.inventory2.SvRspGetChildren) 

    /**
     * Sets the value of field 'svRspGetRoot'.
     * 
     * @param svRspGetRoot the value of field 'svRspGetRoot'.
     */
    public void setSvRspGetRoot(com.cisco.eManager.common.inventory2.SvRspGetRoot svRspGetRoot)
    {
        this._svRspGetRoot = svRspGetRoot;
    } //-- void setSvRspGetRoot(com.cisco.eManager.common.inventory2.SvRspGetRoot) 

    /**
     * Sets the value of field 'svRspMoveNode'.
     * 
     * @param svRspMoveNode the value of field 'svRspMoveNode'.
     */
    public void setSvRspMoveNode(com.cisco.eManager.common.inventory2.SvRspMoveNode svRspMoveNode)
    {
        this._svRspMoveNode = svRspMoveNode;
    } //-- void setSvRspMoveNode(com.cisco.eManager.common.inventory2.SvRspMoveNode) 

    /**
     * Method unmarshalSolutionViewResp
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSolutionViewResp(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.SolutionViewResp) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.SolutionViewResp.class, reader);
    } //-- java.lang.Object unmarshalSolutionViewResp(java.io.Reader) 

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

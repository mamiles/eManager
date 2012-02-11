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
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ContainerNode.
 * 
 * @version $Revision$ $Date$
 */
public class ContainerNode implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _parentNodeId
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _parentNodeId;

    /**
     * Field _nodeId
     */
    private com.cisco.eManager.common.inventory2.ManagedObjectId _nodeId;

    /**
     * Field _nodeName
     */
    private java.lang.String _nodeName;

    /**
     * Field _childNodeIdList
     */
    private java.util.Vector _childNodeIdList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ContainerNode() {
        super();
        _childNodeIdList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.ContainerNode()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addChildNodeId
     * 
     * @param vChildNodeId
     */
    public void addChildNodeId(com.cisco.eManager.common.inventory2.ManagedObjectId vChildNodeId)
        throws java.lang.IndexOutOfBoundsException
    {
        _childNodeIdList.addElement(vChildNodeId);
    } //-- void addChildNodeId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method addChildNodeId
     * 
     * @param index
     * @param vChildNodeId
     */
    public void addChildNodeId(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vChildNodeId)
        throws java.lang.IndexOutOfBoundsException
    {
        _childNodeIdList.insertElementAt(vChildNodeId, index);
    } //-- void addChildNodeId(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method enumerateChildNodeId
     */
    public java.util.Enumeration enumerateChildNodeId()
    {
        return _childNodeIdList.elements();
    } //-- java.util.Enumeration enumerateChildNodeId() 

    /**
     * Method getChildNodeId
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getChildNodeId(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _childNodeIdList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) _childNodeIdList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getChildNodeId(int) 

    /**
     * Method getChildNodeId
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId[] getChildNodeId()
    {
        int size = _childNodeIdList.size();
        com.cisco.eManager.common.inventory2.ManagedObjectId[] mArray = new com.cisco.eManager.common.inventory2.ManagedObjectId[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.ManagedObjectId) _childNodeIdList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId[] getChildNodeId() 

    /**
     * Method getChildNodeIdCount
     */
    public int getChildNodeIdCount()
    {
        return _childNodeIdList.size();
    } //-- int getChildNodeIdCount() 

    /**
     * Returns the value of field 'nodeId'.
     * 
     * @return the value of field 'nodeId'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getNodeId()
    {
        return this._nodeId;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getNodeId() 

    /**
     * Returns the value of field 'nodeName'.
     * 
     * @return the value of field 'nodeName'.
     */
    public java.lang.String getNodeName()
    {
        return this._nodeName;
    } //-- java.lang.String getNodeName() 

    /**
     * Returns the value of field 'parentNodeId'.
     * 
     * @return the value of field 'parentNodeId'.
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId getParentNodeId()
    {
        return this._parentNodeId;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId getParentNodeId() 

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
     * Method removeAllChildNodeId
     */
    public void removeAllChildNodeId()
    {
        _childNodeIdList.removeAllElements();
    } //-- void removeAllChildNodeId() 

    /**
     * Method removeChildNodeId
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.ManagedObjectId removeChildNodeId(int index)
    {
        java.lang.Object obj = _childNodeIdList.elementAt(index);
        _childNodeIdList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.ManagedObjectId) obj;
    } //-- com.cisco.eManager.common.inventory2.ManagedObjectId removeChildNodeId(int) 

    /**
     * Method setChildNodeId
     * 
     * @param index
     * @param vChildNodeId
     */
    public void setChildNodeId(int index, com.cisco.eManager.common.inventory2.ManagedObjectId vChildNodeId)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _childNodeIdList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _childNodeIdList.setElementAt(vChildNodeId, index);
    } //-- void setChildNodeId(int, com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method setChildNodeId
     * 
     * @param childNodeIdArray
     */
    public void setChildNodeId(com.cisco.eManager.common.inventory2.ManagedObjectId[] childNodeIdArray)
    {
        //-- copy array
        _childNodeIdList.removeAllElements();
        for (int i = 0; i < childNodeIdArray.length; i++) {
            _childNodeIdList.addElement(childNodeIdArray[i]);
        }
    } //-- void setChildNodeId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Sets the value of field 'nodeId'.
     * 
     * @param nodeId the value of field 'nodeId'.
     */
    public void setNodeId(com.cisco.eManager.common.inventory2.ManagedObjectId nodeId)
    {
        this._nodeId = nodeId;
    } //-- void setNodeId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Sets the value of field 'nodeName'.
     * 
     * @param nodeName the value of field 'nodeName'.
     */
    public void setNodeName(java.lang.String nodeName)
    {
        this._nodeName = nodeName;
    } //-- void setNodeName(java.lang.String) 

    /**
     * Sets the value of field 'parentNodeId'.
     * 
     * @param parentNodeId the value of field 'parentNodeId'.
     */
    public void setParentNodeId(com.cisco.eManager.common.inventory2.ManagedObjectId parentNodeId)
    {
        this._parentNodeId = parentNodeId;
    } //-- void setParentNodeId(com.cisco.eManager.common.inventory2.ManagedObjectId) 

    /**
     * Method unmarshalContainerNode
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalContainerNode(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.ContainerNode) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.ContainerNode.class, reader);
    } //-- java.lang.Object unmarshalContainerNode(java.io.Reader) 

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

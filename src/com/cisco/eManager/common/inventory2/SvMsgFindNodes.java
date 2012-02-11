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
 * Class SvMsgFindNodes.
 * 
 * @version $Revision$ $Date$
 */
public class SvMsgFindNodes implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * inclusive; if not provided, search begins at root node
     */
    private com.cisco.eManager.common.inventory2.NodeId _searchRootNodeId;

    /**
     * Field _searchCriteria
     */
    private com.cisco.eManager.common.inventory2.ViewSearchCriteria _searchCriteria;


      //----------------/
     //- Constructors -/
    //----------------/

    public SvMsgFindNodes() {
        super();
    } //-- com.cisco.eManager.common.inventory2.SvMsgFindNodes()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'searchCriteria'.
     * 
     * @return the value of field 'searchCriteria'.
     */
    public com.cisco.eManager.common.inventory2.ViewSearchCriteria getSearchCriteria()
    {
        return this._searchCriteria;
    } //-- com.cisco.eManager.common.inventory2.ViewSearchCriteria getSearchCriteria() 

    /**
     * Returns the value of field 'searchRootNodeId'. The field
     * 'searchRootNodeId' has the following description: inclusive;
     * if not provided, search begins at root node
     * 
     * @return the value of field 'searchRootNodeId'.
     */
    public com.cisco.eManager.common.inventory2.NodeId getSearchRootNodeId()
    {
        return this._searchRootNodeId;
    } //-- com.cisco.eManager.common.inventory2.NodeId getSearchRootNodeId() 

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
     * Sets the value of field 'searchCriteria'.
     * 
     * @param searchCriteria the value of field 'searchCriteria'.
     */
    public void setSearchCriteria(com.cisco.eManager.common.inventory2.ViewSearchCriteria searchCriteria)
    {
        this._searchCriteria = searchCriteria;
    } //-- void setSearchCriteria(com.cisco.eManager.common.inventory2.ViewSearchCriteria) 

    /**
     * Sets the value of field 'searchRootNodeId'. The field
     * 'searchRootNodeId' has the following description: inclusive;
     * if not provided, search begins at root node
     * 
     * @param searchRootNodeId the value of field 'searchRootNodeId'
     */
    public void setSearchRootNodeId(com.cisco.eManager.common.inventory2.NodeId searchRootNodeId)
    {
        this._searchRootNodeId = searchRootNodeId;
    } //-- void setSearchRootNodeId(com.cisco.eManager.common.inventory2.NodeId) 

    /**
     * Method unmarshalSvMsgFindNodes
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSvMsgFindNodes(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.SvMsgFindNodes) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.SvMsgFindNodes.class, reader);
    } //-- java.lang.Object unmarshalSvMsgFindNodes(java.io.Reader) 

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

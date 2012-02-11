/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.event2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.handlers.*;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.xml.validators.*;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class AbstractSearchCriteriaTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public AbstractSearchCriteriaTypeDescriptor() {
        super();
        xmlName = "AbstractSearchCriteriaType";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        XMLFieldDescriptorImpl  desc           = null;
        XMLFieldHandler         handler        = null;
        FieldValidator          fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _postDateSearchCriteria
        desc = new XMLFieldDescriptorImpl(PostDateSearchCriteria.class, "_postDateSearchCriteria", "postDateSearchCriteria", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AbstractSearchCriteriaType target = (AbstractSearchCriteriaType) object;
                return target.getPostDateSearchCriteria();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AbstractSearchCriteriaType target = (AbstractSearchCriteriaType) object;
                    target.setPostDateSearchCriteria( (PostDateSearchCriteria) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new PostDateSearchCriteria();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _postDateSearchCriteria
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _clearDateSearchCriteria
        desc = new XMLFieldDescriptorImpl(ClearDateSearchCriteria.class, "_clearDateSearchCriteria", "clearDateSearchCriteria", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AbstractSearchCriteriaType target = (AbstractSearchCriteriaType) object;
                return target.getClearDateSearchCriteria();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AbstractSearchCriteriaType target = (AbstractSearchCriteriaType) object;
                    target.setClearDateSearchCriteria( (ClearDateSearchCriteria) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new ClearDateSearchCriteria();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _clearDateSearchCriteria
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _emanagerEventIdSearchCriteria
        desc = new XMLFieldDescriptorImpl(EmanagerEventIdSearchCriteria.class, "_emanagerEventIdSearchCriteria", "emanagerEventIdSearchCriteria", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AbstractSearchCriteriaType target = (AbstractSearchCriteriaType) object;
                return target.getEmanagerEventIdSearchCriteria();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AbstractSearchCriteriaType target = (AbstractSearchCriteriaType) object;
                    target.setEmanagerEventIdSearchCriteria( (EmanagerEventIdSearchCriteria) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new EmanagerEventIdSearchCriteria();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _emanagerEventIdSearchCriteria
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _acknowledgementSearchCriteria
        desc = new XMLFieldDescriptorImpl(AcknowledgementSearchCriteria.class, "_acknowledgementSearchCriteria", "acknowledgementSearchCriteria", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AbstractSearchCriteriaType target = (AbstractSearchCriteriaType) object;
                return target.getAcknowledgementSearchCriteria();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AbstractSearchCriteriaType target = (AbstractSearchCriteriaType) object;
                    target.setAcknowledgementSearchCriteria( (AcknowledgementSearchCriteria) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new AcknowledgementSearchCriteria();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _acknowledgementSearchCriteria
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _managedObjectsList
        desc = new XMLFieldDescriptorImpl(ManagedObjects.class, "_managedObjectsList", "managedObjects", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AbstractSearchCriteriaType target = (AbstractSearchCriteriaType) object;
                return target.getManagedObjects();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AbstractSearchCriteriaType target = (AbstractSearchCriteriaType) object;
                    target.addManagedObjects( (ManagedObjects) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new ManagedObjects();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _managedObjectsList
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(0);
        desc.setValidator(fieldValidator);
        
        //-- _severitiesList
        desc = new XMLFieldDescriptorImpl(Severities.class, "_severitiesList", "severities", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AbstractSearchCriteriaType target = (AbstractSearchCriteriaType) object;
                return target.getSeverities();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AbstractSearchCriteriaType target = (AbstractSearchCriteriaType) object;
                    target.addSeverities( (Severities) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new Severities();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _severitiesList
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(0);
        desc.setValidator(fieldValidator);
        
    } //-- com.cisco.eManager.common.event2.AbstractSearchCriteriaTypeDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public org.exolab.castor.mapping.AccessMode getAccessMode()
    {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
    **/
    public org.exolab.castor.mapping.ClassDescriptor getExtends()
    {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
    **/
    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
    **/
    public java.lang.Class getJavaClass()
    {
        return com.cisco.eManager.common.event2.AbstractSearchCriteriaType.class;
    } //-- java.lang.Class getJavaClass() 

    /**
    **/
    public java.lang.String getNameSpacePrefix()
    {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
    **/
    public java.lang.String getNameSpaceURI()
    {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
    **/
    public org.exolab.castor.xml.TypeValidator getValidator()
    {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
    **/
    public java.lang.String getXMLName()
    {
        return xmlName;
    } //-- java.lang.String getXMLName() 

}

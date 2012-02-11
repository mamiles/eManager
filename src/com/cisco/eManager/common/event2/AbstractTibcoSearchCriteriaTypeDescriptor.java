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
public class AbstractTibcoSearchCriteriaTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public AbstractTibcoSearchCriteriaTypeDescriptor() {
        super();
        xmlName = "AbstractTibcoSearchCriteriaType";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        XMLFieldDescriptorImpl  desc           = null;
        XMLFieldHandler         handler        = null;
        FieldValidator          fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _tibcoEventIdSearchCriteria
        desc = new XMLFieldDescriptorImpl(TibcoEventIdSearchCriteria.class, "_tibcoEventIdSearchCriteria", "tibcoEventIdSearchCriteria", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AbstractTibcoSearchCriteriaType target = (AbstractTibcoSearchCriteriaType) object;
                return target.getTibcoEventIdSearchCriteria();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AbstractTibcoSearchCriteriaType target = (AbstractTibcoSearchCriteriaType) object;
                    target.setTibcoEventIdSearchCriteria( (TibcoEventIdSearchCriteria) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new TibcoEventIdSearchCriteria();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _tibcoEventIdSearchCriteria
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _ruleSearchCriteria
        desc = new XMLFieldDescriptorImpl(RuleSearchCriteria.class, "_ruleSearchCriteria", "ruleSearchCriteria", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AbstractTibcoSearchCriteriaType target = (AbstractTibcoSearchCriteriaType) object;
                return target.getRuleSearchCriteria();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AbstractTibcoSearchCriteriaType target = (AbstractTibcoSearchCriteriaType) object;
                    target.setRuleSearchCriteria( (RuleSearchCriteria) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new RuleSearchCriteria();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _ruleSearchCriteria
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _testSearchCriteria
        desc = new XMLFieldDescriptorImpl(TestSearchCriteria.class, "_testSearchCriteria", "testSearchCriteria", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AbstractTibcoSearchCriteriaType target = (AbstractTibcoSearchCriteriaType) object;
                return target.getTestSearchCriteria();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AbstractTibcoSearchCriteriaType target = (AbstractTibcoSearchCriteriaType) object;
                    target.setTestSearchCriteria( (TestSearchCriteria) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new TestSearchCriteria();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _testSearchCriteria
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _instrumentationSearchCriteria
        desc = new XMLFieldDescriptorImpl(InstrumentationSearchCriteria.class, "_instrumentationSearchCriteria", "instrumentationSearchCriteria", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AbstractTibcoSearchCriteriaType target = (AbstractTibcoSearchCriteriaType) object;
                return target.getInstrumentationSearchCriteria();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AbstractTibcoSearchCriteriaType target = (AbstractTibcoSearchCriteriaType) object;
                    target.setInstrumentationSearchCriteria( (InstrumentationSearchCriteria) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new InstrumentationSearchCriteria();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _instrumentationSearchCriteria
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _managementPolicyIdsList
        desc = new XMLFieldDescriptorImpl(ManagementPolicyIds.class, "_managementPolicyIdsList", "managementPolicyIds", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AbstractTibcoSearchCriteriaType target = (AbstractTibcoSearchCriteriaType) object;
                return target.getManagementPolicyIds();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AbstractTibcoSearchCriteriaType target = (AbstractTibcoSearchCriteriaType) object;
                    target.addManagementPolicyIds( (ManagementPolicyIds) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new ManagementPolicyIds();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _managementPolicyIdsList
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(0);
        desc.setValidator(fieldValidator);
        
    } //-- com.cisco.eManager.common.event2.AbstractTibcoSearchCriteriaTypeDescriptor()


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
        return com.cisco.eManager.common.event2.AbstractTibcoSearchCriteriaType.class;
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

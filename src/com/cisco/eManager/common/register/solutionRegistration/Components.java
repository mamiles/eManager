/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cisco.eManager.common.register.solutionRegistration;

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
 * Class Components.
 * 
 * @version $Revision$ $Date$
 */
public class Components implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _componentList
     */
    private java.util.Vector _componentList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Components() {
        super();
        _componentList = new Vector();
    } //-- com.cisco.eManager.common.register.solutionRegistration.Components()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addComponent
     * 
     * @param vComponent
     */
    public void addComponent(com.cisco.eManager.common.register.solutionRegistration.Component vComponent)
        throws java.lang.IndexOutOfBoundsException
    {
        _componentList.addElement(vComponent);
    } //-- void addComponent(com.cisco.eManager.common.register.solutionRegistration.Component) 

    /**
     * Method addComponent
     * 
     * @param index
     * @param vComponent
     */
    public void addComponent(int index, com.cisco.eManager.common.register.solutionRegistration.Component vComponent)
        throws java.lang.IndexOutOfBoundsException
    {
        _componentList.insertElementAt(vComponent, index);
    } //-- void addComponent(int, com.cisco.eManager.common.register.solutionRegistration.Component) 

    /**
     * Method enumerateComponent
     */
    public java.util.Enumeration enumerateComponent()
    {
        return _componentList.elements();
    } //-- java.util.Enumeration enumerateComponent() 

    /**
     * Method getComponent
     * 
     * @param index
     */
    public com.cisco.eManager.common.register.solutionRegistration.Component getComponent(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _componentList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.register.solutionRegistration.Component) _componentList.elementAt(index);
    } //-- com.cisco.eManager.common.register.solutionRegistration.Component getComponent(int) 

    /**
     * Method getComponent
     */
    public com.cisco.eManager.common.register.solutionRegistration.Component[] getComponent()
    {
        int size = _componentList.size();
        com.cisco.eManager.common.register.solutionRegistration.Component[] mArray = new com.cisco.eManager.common.register.solutionRegistration.Component[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.register.solutionRegistration.Component) _componentList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.register.solutionRegistration.Component[] getComponent() 

    /**
     * Method getComponentCount
     */
    public int getComponentCount()
    {
        return _componentList.size();
    } //-- int getComponentCount() 

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
     * Method removeAllComponent
     */
    public void removeAllComponent()
    {
        _componentList.removeAllElements();
    } //-- void removeAllComponent() 

    /**
     * Method removeComponent
     * 
     * @param index
     */
    public com.cisco.eManager.common.register.solutionRegistration.Component removeComponent(int index)
    {
        java.lang.Object obj = _componentList.elementAt(index);
        _componentList.removeElementAt(index);
        return (com.cisco.eManager.common.register.solutionRegistration.Component) obj;
    } //-- com.cisco.eManager.common.register.solutionRegistration.Component removeComponent(int) 

    /**
     * Method setComponent
     * 
     * @param index
     * @param vComponent
     */
    public void setComponent(int index, com.cisco.eManager.common.register.solutionRegistration.Component vComponent)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _componentList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _componentList.setElementAt(vComponent, index);
    } //-- void setComponent(int, com.cisco.eManager.common.register.solutionRegistration.Component) 

    /**
     * Method setComponent
     * 
     * @param componentArray
     */
    public void setComponent(com.cisco.eManager.common.register.solutionRegistration.Component[] componentArray)
    {
        //-- copy array
        _componentList.removeAllElements();
        for (int i = 0; i < componentArray.length; i++) {
            _componentList.addElement(componentArray[i]);
        }
    } //-- void setComponent(com.cisco.eManager.common.register.solutionRegistration.Component) 

    /**
     * Method unmarshalComponents
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalComponents(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.register.solutionRegistration.Components) Unmarshaller.unmarshal(com.cisco.eManager.common.register.solutionRegistration.Components.class, reader);
    } //-- java.lang.Object unmarshalComponents(java.io.Reader) 

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

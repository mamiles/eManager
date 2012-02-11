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
 * Class AiRspGetAppInstances.
 * 
 * @version $Revision$ $Date$
 */
public class AiRspGetAppInstances implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _appInstancesList
     */
    private java.util.Vector _appInstancesList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AiRspGetAppInstances() {
        super();
        _appInstancesList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.AiRspGetAppInstances()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAppInstances
     * 
     * @param vAppInstances
     */
    public void addAppInstances(com.cisco.eManager.common.inventory2.AppInstance vAppInstances)
        throws java.lang.IndexOutOfBoundsException
    {
        _appInstancesList.addElement(vAppInstances);
    } //-- void addAppInstances(com.cisco.eManager.common.inventory2.AppInstance) 

    /**
     * Method addAppInstances
     * 
     * @param index
     * @param vAppInstances
     */
    public void addAppInstances(int index, com.cisco.eManager.common.inventory2.AppInstance vAppInstances)
        throws java.lang.IndexOutOfBoundsException
    {
        _appInstancesList.insertElementAt(vAppInstances, index);
    } //-- void addAppInstances(int, com.cisco.eManager.common.inventory2.AppInstance) 

    /**
     * Method enumerateAppInstances
     */
    public java.util.Enumeration enumerateAppInstances()
    {
        return _appInstancesList.elements();
    } //-- java.util.Enumeration enumerateAppInstances() 

    /**
     * Method getAppInstances
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.AppInstance getAppInstances(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appInstancesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.AppInstance) _appInstancesList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.AppInstance getAppInstances(int) 

    /**
     * Method getAppInstances
     */
    public com.cisco.eManager.common.inventory2.AppInstance[] getAppInstances()
    {
        int size = _appInstancesList.size();
        com.cisco.eManager.common.inventory2.AppInstance[] mArray = new com.cisco.eManager.common.inventory2.AppInstance[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.AppInstance) _appInstancesList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.AppInstance[] getAppInstances() 

    /**
     * Method getAppInstancesCount
     */
    public int getAppInstancesCount()
    {
        return _appInstancesList.size();
    } //-- int getAppInstancesCount() 

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
     * Method removeAllAppInstances
     */
    public void removeAllAppInstances()
    {
        _appInstancesList.removeAllElements();
    } //-- void removeAllAppInstances() 

    /**
     * Method removeAppInstances
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.AppInstance removeAppInstances(int index)
    {
        java.lang.Object obj = _appInstancesList.elementAt(index);
        _appInstancesList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.AppInstance) obj;
    } //-- com.cisco.eManager.common.inventory2.AppInstance removeAppInstances(int) 

    /**
     * Method setAppInstances
     * 
     * @param index
     * @param vAppInstances
     */
    public void setAppInstances(int index, com.cisco.eManager.common.inventory2.AppInstance vAppInstances)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _appInstancesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _appInstancesList.setElementAt(vAppInstances, index);
    } //-- void setAppInstances(int, com.cisco.eManager.common.inventory2.AppInstance) 

    /**
     * Method setAppInstances
     * 
     * @param appInstancesArray
     */
    public void setAppInstances(com.cisco.eManager.common.inventory2.AppInstance[] appInstancesArray)
    {
        //-- copy array
        _appInstancesList.removeAllElements();
        for (int i = 0; i < appInstancesArray.length; i++) {
            _appInstancesList.addElement(appInstancesArray[i]);
        }
    } //-- void setAppInstances(com.cisco.eManager.common.inventory2.AppInstance) 

    /**
     * Method unmarshalAiRspGetAppInstances
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalAiRspGetAppInstances(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.AiRspGetAppInstances) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.AiRspGetAppInstances.class, reader);
    } //-- java.lang.Object unmarshalAiRspGetAppInstances(java.io.Reader) 

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

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
 * Class HRspGetHosts.
 * 
 * @version $Revision$ $Date$
 */
public class HRspGetHosts implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _hostsList
     */
    private java.util.Vector _hostsList;


      //----------------/
     //- Constructors -/
    //----------------/

    public HRspGetHosts() {
        super();
        _hostsList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.HRspGetHosts()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addHosts
     * 
     * @param vHosts
     */
    public void addHosts(com.cisco.eManager.common.inventory2.Host vHosts)
        throws java.lang.IndexOutOfBoundsException
    {
        _hostsList.addElement(vHosts);
    } //-- void addHosts(com.cisco.eManager.common.inventory2.Host) 

    /**
     * Method addHosts
     * 
     * @param index
     * @param vHosts
     */
    public void addHosts(int index, com.cisco.eManager.common.inventory2.Host vHosts)
        throws java.lang.IndexOutOfBoundsException
    {
        _hostsList.insertElementAt(vHosts, index);
    } //-- void addHosts(int, com.cisco.eManager.common.inventory2.Host) 

    /**
     * Method enumerateHosts
     */
    public java.util.Enumeration enumerateHosts()
    {
        return _hostsList.elements();
    } //-- java.util.Enumeration enumerateHosts() 

    /**
     * Method getHosts
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.Host getHosts(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _hostsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.Host) _hostsList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.Host getHosts(int) 

    /**
     * Method getHosts
     */
    public com.cisco.eManager.common.inventory2.Host[] getHosts()
    {
        int size = _hostsList.size();
        com.cisco.eManager.common.inventory2.Host[] mArray = new com.cisco.eManager.common.inventory2.Host[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.Host) _hostsList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.Host[] getHosts() 

    /**
     * Method getHostsCount
     */
    public int getHostsCount()
    {
        return _hostsList.size();
    } //-- int getHostsCount() 

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
     * Method removeAllHosts
     */
    public void removeAllHosts()
    {
        _hostsList.removeAllElements();
    } //-- void removeAllHosts() 

    /**
     * Method removeHosts
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.Host removeHosts(int index)
    {
        java.lang.Object obj = _hostsList.elementAt(index);
        _hostsList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.Host) obj;
    } //-- com.cisco.eManager.common.inventory2.Host removeHosts(int) 

    /**
     * Method setHosts
     * 
     * @param index
     * @param vHosts
     */
    public void setHosts(int index, com.cisco.eManager.common.inventory2.Host vHosts)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _hostsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _hostsList.setElementAt(vHosts, index);
    } //-- void setHosts(int, com.cisco.eManager.common.inventory2.Host) 

    /**
     * Method setHosts
     * 
     * @param hostsArray
     */
    public void setHosts(com.cisco.eManager.common.inventory2.Host[] hostsArray)
    {
        //-- copy array
        _hostsList.removeAllElements();
        for (int i = 0; i < hostsArray.length; i++) {
            _hostsList.addElement(hostsArray[i]);
        }
    } //-- void setHosts(com.cisco.eManager.common.inventory2.Host) 

    /**
     * Method unmarshalHRspGetHosts
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalHRspGetHosts(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.HRspGetHosts) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.HRspGetHosts.class, reader);
    } //-- java.lang.Object unmarshalHRspGetHosts(java.io.Reader) 

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

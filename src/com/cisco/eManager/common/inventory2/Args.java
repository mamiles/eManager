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
 * Class Args.
 * 
 * @version $Revision$ $Date$
 */
public class Args implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _argList
     */
    private java.util.Vector _argList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Args() {
        super();
        _argList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.Args()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addArg
     * 
     * @param vArg
     */
    public void addArg(com.cisco.eManager.common.inventory2.NvPair vArg)
        throws java.lang.IndexOutOfBoundsException
    {
        _argList.addElement(vArg);
    } //-- void addArg(com.cisco.eManager.common.inventory2.NvPair) 

    /**
     * Method addArg
     * 
     * @param index
     * @param vArg
     */
    public void addArg(int index, com.cisco.eManager.common.inventory2.NvPair vArg)
        throws java.lang.IndexOutOfBoundsException
    {
        _argList.insertElementAt(vArg, index);
    } //-- void addArg(int, com.cisco.eManager.common.inventory2.NvPair) 

    /**
     * Method enumerateArg
     */
    public java.util.Enumeration enumerateArg()
    {
        return _argList.elements();
    } //-- java.util.Enumeration enumerateArg() 

    /**
     * Method getArg
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.NvPair getArg(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _argList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.NvPair) _argList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.NvPair getArg(int) 

    /**
     * Method getArg
     */
    public com.cisco.eManager.common.inventory2.NvPair[] getArg()
    {
        int size = _argList.size();
        com.cisco.eManager.common.inventory2.NvPair[] mArray = new com.cisco.eManager.common.inventory2.NvPair[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.NvPair) _argList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.NvPair[] getArg() 

    /**
     * Method getArgCount
     */
    public int getArgCount()
    {
        return _argList.size();
    } //-- int getArgCount() 

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
     * Method removeAllArg
     */
    public void removeAllArg()
    {
        _argList.removeAllElements();
    } //-- void removeAllArg() 

    /**
     * Method removeArg
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.NvPair removeArg(int index)
    {
        java.lang.Object obj = _argList.elementAt(index);
        _argList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.NvPair) obj;
    } //-- com.cisco.eManager.common.inventory2.NvPair removeArg(int) 

    /**
     * Method setArg
     * 
     * @param index
     * @param vArg
     */
    public void setArg(int index, com.cisco.eManager.common.inventory2.NvPair vArg)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _argList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _argList.setElementAt(vArg, index);
    } //-- void setArg(int, com.cisco.eManager.common.inventory2.NvPair) 

    /**
     * Method setArg
     * 
     * @param argArray
     */
    public void setArg(com.cisco.eManager.common.inventory2.NvPair[] argArray)
    {
        //-- copy array
        _argList.removeAllElements();
        for (int i = 0; i < argArray.length; i++) {
            _argList.addElement(argArray[i]);
        }
    } //-- void setArg(com.cisco.eManager.common.inventory2.NvPair) 

    /**
     * Method unmarshalArgs
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalArgs(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.Args) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.Args.class, reader);
    } //-- java.lang.Object unmarshalArgs(java.io.Reader) 

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

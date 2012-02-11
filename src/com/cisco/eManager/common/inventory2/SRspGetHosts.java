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
 * Class SRspGetHosts.
 * 
 * @version $Revision$ $Date$
 */
public class SRspGetHosts implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _solutionsList
     */
    private java.util.Vector _solutionsList;


      //----------------/
     //- Constructors -/
    //----------------/

    public SRspGetHosts() {
        super();
        _solutionsList = new Vector();
    } //-- com.cisco.eManager.common.inventory2.SRspGetHosts()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addSolutions
     * 
     * @param vSolutions
     */
    public void addSolutions(com.cisco.eManager.common.inventory2.Solution vSolutions)
        throws java.lang.IndexOutOfBoundsException
    {
        _solutionsList.addElement(vSolutions);
    } //-- void addSolutions(com.cisco.eManager.common.inventory2.Solution) 

    /**
     * Method addSolutions
     * 
     * @param index
     * @param vSolutions
     */
    public void addSolutions(int index, com.cisco.eManager.common.inventory2.Solution vSolutions)
        throws java.lang.IndexOutOfBoundsException
    {
        _solutionsList.insertElementAt(vSolutions, index);
    } //-- void addSolutions(int, com.cisco.eManager.common.inventory2.Solution) 

    /**
     * Method enumerateSolutions
     */
    public java.util.Enumeration enumerateSolutions()
    {
        return _solutionsList.elements();
    } //-- java.util.Enumeration enumerateSolutions() 

    /**
     * Method getSolutions
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.Solution getSolutions(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _solutionsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.cisco.eManager.common.inventory2.Solution) _solutionsList.elementAt(index);
    } //-- com.cisco.eManager.common.inventory2.Solution getSolutions(int) 

    /**
     * Method getSolutions
     */
    public com.cisco.eManager.common.inventory2.Solution[] getSolutions()
    {
        int size = _solutionsList.size();
        com.cisco.eManager.common.inventory2.Solution[] mArray = new com.cisco.eManager.common.inventory2.Solution[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.cisco.eManager.common.inventory2.Solution) _solutionsList.elementAt(index);
        }
        return mArray;
    } //-- com.cisco.eManager.common.inventory2.Solution[] getSolutions() 

    /**
     * Method getSolutionsCount
     */
    public int getSolutionsCount()
    {
        return _solutionsList.size();
    } //-- int getSolutionsCount() 

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
     * Method removeAllSolutions
     */
    public void removeAllSolutions()
    {
        _solutionsList.removeAllElements();
    } //-- void removeAllSolutions() 

    /**
     * Method removeSolutions
     * 
     * @param index
     */
    public com.cisco.eManager.common.inventory2.Solution removeSolutions(int index)
    {
        java.lang.Object obj = _solutionsList.elementAt(index);
        _solutionsList.removeElementAt(index);
        return (com.cisco.eManager.common.inventory2.Solution) obj;
    } //-- com.cisco.eManager.common.inventory2.Solution removeSolutions(int) 

    /**
     * Method setSolutions
     * 
     * @param index
     * @param vSolutions
     */
    public void setSolutions(int index, com.cisco.eManager.common.inventory2.Solution vSolutions)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _solutionsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _solutionsList.setElementAt(vSolutions, index);
    } //-- void setSolutions(int, com.cisco.eManager.common.inventory2.Solution) 

    /**
     * Method setSolutions
     * 
     * @param solutionsArray
     */
    public void setSolutions(com.cisco.eManager.common.inventory2.Solution[] solutionsArray)
    {
        //-- copy array
        _solutionsList.removeAllElements();
        for (int i = 0; i < solutionsArray.length; i++) {
            _solutionsList.addElement(solutionsArray[i]);
        }
    } //-- void setSolutions(com.cisco.eManager.common.inventory2.Solution) 

    /**
     * Method unmarshalSRspGetHosts
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSRspGetHosts(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.inventory2.SRspGetHosts) Unmarshaller.unmarshal(com.cisco.eManager.common.inventory2.SRspGetHosts.class, reader);
    } //-- java.lang.Object unmarshalSRspGetHosts(java.io.Reader) 

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

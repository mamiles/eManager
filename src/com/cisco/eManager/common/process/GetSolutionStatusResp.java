/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.process;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class GetSolutionStatusResp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _solutionStatusInfoObjList;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetSolutionStatusResp() {
        super();
        _solutionStatusInfoObjList = new ArrayList();
    } //-- com.cisco.eManager.common.process.GetSolutionStatusResp()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vSolutionStatusInfoObj
    **/
    public void addSolutionStatusInfoObj(SolutionStatusInfoObj vSolutionStatusInfoObj)
        throws java.lang.IndexOutOfBoundsException
    {
        _solutionStatusInfoObjList.add(vSolutionStatusInfoObj);
    } //-- void addSolutionStatusInfoObj(SolutionStatusInfoObj) 

    /**
     * 
     * 
     * @param index
     * @param vSolutionStatusInfoObj
    **/
    public void addSolutionStatusInfoObj(int index, SolutionStatusInfoObj vSolutionStatusInfoObj)
        throws java.lang.IndexOutOfBoundsException
    {
        _solutionStatusInfoObjList.add(index, vSolutionStatusInfoObj);
    } //-- void addSolutionStatusInfoObj(int, SolutionStatusInfoObj) 

    /**
    **/
    public void clearSolutionStatusInfoObj()
    {
        _solutionStatusInfoObjList.clear();
    } //-- void clearSolutionStatusInfoObj() 

    /**
    **/
    public java.util.Enumeration enumerateSolutionStatusInfoObj()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_solutionStatusInfoObjList.iterator());
    } //-- java.util.Enumeration enumerateSolutionStatusInfoObj() 

    /**
     * 
     * 
     * @param index
    **/
    public SolutionStatusInfoObj getSolutionStatusInfoObj(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _solutionStatusInfoObjList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (SolutionStatusInfoObj) _solutionStatusInfoObjList.get(index);
    } //-- SolutionStatusInfoObj getSolutionStatusInfoObj(int) 

    /**
    **/
    public SolutionStatusInfoObj[] getSolutionStatusInfoObj()
    {
        int size = _solutionStatusInfoObjList.size();
        SolutionStatusInfoObj[] mArray = new SolutionStatusInfoObj[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (SolutionStatusInfoObj) _solutionStatusInfoObjList.get(index);
        }
        return mArray;
    } //-- SolutionStatusInfoObj[] getSolutionStatusInfoObj() 

    /**
    **/
    public int getSolutionStatusInfoObjCount()
    {
        return _solutionStatusInfoObjList.size();
    } //-- int getSolutionStatusInfoObjCount() 

    /**
    **/
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
     * 
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * 
     * 
     * @param vSolutionStatusInfoObj
    **/
    public boolean removeSolutionStatusInfoObj(SolutionStatusInfoObj vSolutionStatusInfoObj)
    {
        boolean removed = _solutionStatusInfoObjList.remove(vSolutionStatusInfoObj);
        return removed;
    } //-- boolean removeSolutionStatusInfoObj(SolutionStatusInfoObj) 

    /**
     * 
     * 
     * @param index
     * @param vSolutionStatusInfoObj
    **/
    public void setSolutionStatusInfoObj(int index, SolutionStatusInfoObj vSolutionStatusInfoObj)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _solutionStatusInfoObjList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _solutionStatusInfoObjList.set(index, vSolutionStatusInfoObj);
    } //-- void setSolutionStatusInfoObj(int, SolutionStatusInfoObj) 

    /**
     * 
     * 
     * @param solutionStatusInfoObjArray
    **/
    public void setSolutionStatusInfoObj(SolutionStatusInfoObj[] solutionStatusInfoObjArray)
    {
        //-- copy array
        _solutionStatusInfoObjList.clear();
        for (int i = 0; i < solutionStatusInfoObjArray.length; i++) {
            _solutionStatusInfoObjList.add(solutionStatusInfoObjArray[i]);
        }
    } //-- void setSolutionStatusInfoObj(SolutionStatusInfoObj) 

    /**
     * 
     * 
     * @param reader
    **/
    public static com.cisco.eManager.common.process.GetSolutionStatusResp unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.cisco.eManager.common.process.GetSolutionStatusResp) Unmarshaller.unmarshal(com.cisco.eManager.common.process.GetSolutionStatusResp.class, reader);
    } //-- com.cisco.eManager.common.process.GetSolutionStatusResp unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

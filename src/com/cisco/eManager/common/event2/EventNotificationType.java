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

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public abstract class EventNotificationType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private int _post;

    /**
     * keeps track of state for field: _post
    **/
    private boolean _has_post;

    private int _clear;

    /**
     * keeps track of state for field: _clear
    **/
    private boolean _has_clear;


      //----------------/
     //- Constructors -/
    //----------------/

    public EventNotificationType() {
        super();
    } //-- com.cisco.eManager.common.event2.EventNotificationType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'clear'.
     * 
     * @return the value of field 'clear'.
    **/
    public int getClear()
    {
        return this._clear;
    } //-- int getClear() 

    /**
     * Returns the value of field 'post'.
     * 
     * @return the value of field 'post'.
    **/
    public int getPost()
    {
        return this._post;
    } //-- int getPost() 

    /**
    **/
    public boolean hasClear()
    {
        return this._has_clear;
    } //-- boolean hasClear() 

    /**
    **/
    public boolean hasPost()
    {
        return this._has_post;
    } //-- boolean hasPost() 

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
    public abstract void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * 
     * 
     * @param handler
    **/
    public abstract void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * Sets the value of field 'clear'.
     * 
     * @param clear the value of field 'clear'.
    **/
    public void setClear(int clear)
    {
        this._clear = clear;
        this._has_clear = true;
    } //-- void setClear(int) 

    /**
     * Sets the value of field 'post'.
     * 
     * @param post the value of field 'post'.
    **/
    public void setPost(int post)
    {
        this._post = post;
        this._has_post = true;
    } //-- void setPost(int) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.cisco.eManager.common.audit2;

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
public abstract class AbstractSearchCriteriaType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private IdSearchCriteria _idSearchCriteria;

    private java.util.ArrayList _domainsList;

    private java.util.ArrayList _actionsList;

    private SubjectSearchCriteria _subjectSearchCriteria;

    private TimeSearchCriteria _timeSearchCriteria;

    private UserIdSearchCriteria _userIdSearchCriteria;


      //----------------/
     //- Constructors -/
    //----------------/

    public AbstractSearchCriteriaType() {
        super();
        _domainsList = new ArrayList();
        _actionsList = new ArrayList();
    } //-- com.cisco.eManager.common.audit2.AbstractSearchCriteriaType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vActions
    **/
    public void addActions(Actions vActions)
        throws java.lang.IndexOutOfBoundsException
    {
        _actionsList.add(vActions);
    } //-- void addActions(Actions) 

    /**
     * 
     * 
     * @param index
     * @param vActions
    **/
    public void addActions(int index, Actions vActions)
        throws java.lang.IndexOutOfBoundsException
    {
        _actionsList.add(index, vActions);
    } //-- void addActions(int, Actions) 

    /**
     * 
     * 
     * @param vDomains
    **/
    public void addDomains(Domains vDomains)
        throws java.lang.IndexOutOfBoundsException
    {
        _domainsList.add(vDomains);
    } //-- void addDomains(Domains) 

    /**
     * 
     * 
     * @param index
     * @param vDomains
    **/
    public void addDomains(int index, Domains vDomains)
        throws java.lang.IndexOutOfBoundsException
    {
        _domainsList.add(index, vDomains);
    } //-- void addDomains(int, Domains) 

    /**
    **/
    public void clearActions()
    {
        _actionsList.clear();
    } //-- void clearActions() 

    /**
    **/
    public void clearDomains()
    {
        _domainsList.clear();
    } //-- void clearDomains() 

    /**
    **/
    public java.util.Enumeration enumerateActions()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_actionsList.iterator());
    } //-- java.util.Enumeration enumerateActions() 

    /**
    **/
    public java.util.Enumeration enumerateDomains()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_domainsList.iterator());
    } //-- java.util.Enumeration enumerateDomains() 

    /**
     * 
     * 
     * @param index
    **/
    public Actions getActions(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _actionsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Actions) _actionsList.get(index);
    } //-- Actions getActions(int) 

    /**
    **/
    public Actions[] getActions()
    {
        int size = _actionsList.size();
        Actions[] mArray = new Actions[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Actions) _actionsList.get(index);
        }
        return mArray;
    } //-- Actions[] getActions() 

    /**
    **/
    public int getActionsCount()
    {
        return _actionsList.size();
    } //-- int getActionsCount() 

    /**
     * 
     * 
     * @param index
    **/
    public Domains getDomains(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _domainsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Domains) _domainsList.get(index);
    } //-- Domains getDomains(int) 

    /**
    **/
    public Domains[] getDomains()
    {
        int size = _domainsList.size();
        Domains[] mArray = new Domains[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Domains) _domainsList.get(index);
        }
        return mArray;
    } //-- Domains[] getDomains() 

    /**
    **/
    public int getDomainsCount()
    {
        return _domainsList.size();
    } //-- int getDomainsCount() 

    /**
     * Returns the value of field 'idSearchCriteria'.
     * 
     * @return the value of field 'idSearchCriteria'.
    **/
    public IdSearchCriteria getIdSearchCriteria()
    {
        return this._idSearchCriteria;
    } //-- IdSearchCriteria getIdSearchCriteria() 

    /**
     * Returns the value of field 'subjectSearchCriteria'.
     * 
     * @return the value of field 'subjectSearchCriteria'.
    **/
    public SubjectSearchCriteria getSubjectSearchCriteria()
    {
        return this._subjectSearchCriteria;
    } //-- SubjectSearchCriteria getSubjectSearchCriteria() 

    /**
     * Returns the value of field 'timeSearchCriteria'.
     * 
     * @return the value of field 'timeSearchCriteria'.
    **/
    public TimeSearchCriteria getTimeSearchCriteria()
    {
        return this._timeSearchCriteria;
    } //-- TimeSearchCriteria getTimeSearchCriteria() 

    /**
     * Returns the value of field 'userIdSearchCriteria'.
     * 
     * @return the value of field 'userIdSearchCriteria'.
    **/
    public UserIdSearchCriteria getUserIdSearchCriteria()
    {
        return this._userIdSearchCriteria;
    } //-- UserIdSearchCriteria getUserIdSearchCriteria() 

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
     * 
     * 
     * @param vActions
    **/
    public boolean removeActions(Actions vActions)
    {
        boolean removed = _actionsList.remove(vActions);
        return removed;
    } //-- boolean removeActions(Actions) 

    /**
     * 
     * 
     * @param vDomains
    **/
    public boolean removeDomains(Domains vDomains)
    {
        boolean removed = _domainsList.remove(vDomains);
        return removed;
    } //-- boolean removeDomains(Domains) 

    /**
     * 
     * 
     * @param index
     * @param vActions
    **/
    public void setActions(int index, Actions vActions)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _actionsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _actionsList.set(index, vActions);
    } //-- void setActions(int, Actions) 

    /**
     * 
     * 
     * @param actionsArray
    **/
    public void setActions(Actions[] actionsArray)
    {
        //-- copy array
        _actionsList.clear();
        for (int i = 0; i < actionsArray.length; i++) {
            _actionsList.add(actionsArray[i]);
        }
    } //-- void setActions(Actions) 

    /**
     * 
     * 
     * @param index
     * @param vDomains
    **/
    public void setDomains(int index, Domains vDomains)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _domainsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _domainsList.set(index, vDomains);
    } //-- void setDomains(int, Domains) 

    /**
     * 
     * 
     * @param domainsArray
    **/
    public void setDomains(Domains[] domainsArray)
    {
        //-- copy array
        _domainsList.clear();
        for (int i = 0; i < domainsArray.length; i++) {
            _domainsList.add(domainsArray[i]);
        }
    } //-- void setDomains(Domains) 

    /**
     * Sets the value of field 'idSearchCriteria'.
     * 
     * @param idSearchCriteria the value of field 'idSearchCriteria'
    **/
    public void setIdSearchCriteria(IdSearchCriteria idSearchCriteria)
    {
        this._idSearchCriteria = idSearchCriteria;
    } //-- void setIdSearchCriteria(IdSearchCriteria) 

    /**
     * Sets the value of field 'subjectSearchCriteria'.
     * 
     * @param subjectSearchCriteria the value of field
     * 'subjectSearchCriteria'.
    **/
    public void setSubjectSearchCriteria(SubjectSearchCriteria subjectSearchCriteria)
    {
        this._subjectSearchCriteria = subjectSearchCriteria;
    } //-- void setSubjectSearchCriteria(SubjectSearchCriteria) 

    /**
     * Sets the value of field 'timeSearchCriteria'.
     * 
     * @param timeSearchCriteria the value of field
     * 'timeSearchCriteria'.
    **/
    public void setTimeSearchCriteria(TimeSearchCriteria timeSearchCriteria)
    {
        this._timeSearchCriteria = timeSearchCriteria;
    } //-- void setTimeSearchCriteria(TimeSearchCriteria) 

    /**
     * Sets the value of field 'userIdSearchCriteria'.
     * 
     * @param userIdSearchCriteria the value of field
     * 'userIdSearchCriteria'.
    **/
    public void setUserIdSearchCriteria(UserIdSearchCriteria userIdSearchCriteria)
    {
        this._userIdSearchCriteria = userIdSearchCriteria;
    } //-- void setUserIdSearchCriteria(UserIdSearchCriteria) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

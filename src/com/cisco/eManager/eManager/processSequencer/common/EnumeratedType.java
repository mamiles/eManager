package com.cisco.eManager.eManager.processSequencer.common;
/**
 *  This computer program is the confidential information and proprietary trade
 *  secret of Cisco Systems, Inc. Possessions and use of this program must
 *  conform strictly to the license agreement between the user and Cisco
 *  Systems, Inc., and receipt or possession does not convey any rights to
 *  divulge, reproduce, or allow others to use this program without specific
 *  written authorization of Cisco Systems, Inc. Copyright (c) 2001 by Cisco
 *  Systems, Inc. All rights reserved.
 */

import java.util.Hashtable;

/**
 *  The class is the base class for an enumerated type
 *
 *@author     Venkatram Pramod
 *@created    November 7, 2001
 *@version    1.00, September 6, 2001
 */

public class EnumeratedType implements java.io.Serializable, Cloneable {
    /**
     *  Constructor, will only be used by derived classes
     *
     *@param  value             The ordinal value of the enumerated type
     *@param  name              The name of the enumerated type
     */
    protected EnumeratedType(int value){
        this(value, Integer.toString(value));
    }

    /**
     *  Constructor, will only be used by derived classes
     *
     *@param  value             The ordinal value of the enumerated type
     *@param  name              The name of the enumerated type
     */
    protected EnumeratedType(int value, String name){
        m_value = new Integer(value);
        m_name = name;
        try {
          checkForDuplicates(this);
        }
        catch (PSException e) {
          throw new RuntimeException(e.getMessage());
        //Do nothing; Exception only occurs when there are duplicate enumerations
        //this can only happen in development; can't happend in run-time
        }
        storeEnumeratedType(this);
    }


    /**
     *  Returns the enumeration value
     *
     *@return    The integerValue value
     */
    public Integer getIntegerValue() {
        return m_value;
    }


    /**
     *  Returns the list of strings representing each value
     *
     *@param  classRef  The Class representing the particular enumerated type
     *@param  maxValue  The max value of the individual enumerated type
     *@return           The values as an String Array
     */
    public static String[] getStringList(Class classRef, int maxValue) {
        String[] retValues = new String[maxValue];
        for (int i = 0; i < maxValue; i++) {
            retValues[i] = getByValue(classRef, i).toString();
        }
        return retValues;
    }


    /**
     *  Returns the enumeration value
     *
     *@return    the ordinal value of the enumeration value
     */
    public int value() {
        return m_value.intValue();
    }


    /**
     *  Returns the string name for the enumeration
     *
     *@return    The string representation of the enumerated type
     */
    public String toString() {
        return m_name;
    }


    /**
     *  Returns the String Value as an enumerated type
     *
     *@param  classRef  Class of the particular enumerated type
     *@param  name      the name of the enumerated type
     *@return           The enumerated type selected by name
     */
    public static EnumeratedType getByString(Class classRef, String name) {
        if ("".equals(name)) {
            return null;
        }

        EnumeratedType type = null;

        String className = classRef.getName();

        Hashtable values = (Hashtable) m_typesAsStr.get(className);

        if (values != null) {
            type = (EnumeratedType) values.get(name);
        }

        return type;
    }


    /**
     *  Returns the Value as an enumerated type
     *
     *@param  classRef  Class of the particular enumerated type
     *@param  value      the ordinal value of the enumerated type
     *@return           The enumerated type selected by ordinal value
     */
    public static EnumeratedType getByValue(Class classRef, Integer value) {
        if (value == null) {
            return null;
        }

        return EnumeratedType.getByValue(classRef, value.intValue());
    }


    /**
     *  Returns the Value as an enumerated type
     *
     *@param  classRef  Class of the particular enumerated type
     *@param  value      the ordinal value of the enumerated type
     *@return           The enumerated type selected by ordinal value
     */
    public static EnumeratedType getByValue(Class classRef, int value) {
        EnumeratedType type = null;
        String className = classRef.getName();

        Hashtable values = (Hashtable) m_types.get(className);

        if (values != null) {
            type = (EnumeratedType) values.get(new Integer(value));
        }

        return type;
    }


    /**
     *  Compares two enumerated types for equality
     *
     *@param  obj  The object to be compared with
     *@return      true if equal to obj
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof EnumeratedType)) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if ((this.getClass() == obj.getClass()) &&
                (this.getIntegerValue() == ((EnumeratedType) obj).getIntegerValue())) {
            return true;
        }

        return false;
    }


    /**
     *  Checks for duplicates.
     *
     *@param  type              The type being checked
     *@exception  PSException  if there are duplicates
     */
    private void checkForDuplicates(EnumeratedType type) throws PSException {
        String className = type.getClass().getName();

        Hashtable values;
        Hashtable valuesStr;

        //check for existence of current type
        values = (Hashtable) m_types.get(className);
        if (values != null) {
            if (values.get(type.getIntegerValue()) != null) {
                throw new PSException("EnumeratedType:No Dupes Allowed: " + className + "=" + type);
            }
        }

        //check for existence of current name
        valuesStr = (Hashtable) m_typesAsStr.get(className);
        if (valuesStr != null) {
            if (valuesStr.get(type.toString()) != null) {
                throw new PSException("EnumeratedType: " + className + "=" + type);
            }
        }
    }


    /**
     *  stores the type in the appropriate locatin in the hashtable
     *
     *@param  type  the type to be stored in the hashtable
     */
    private void storeEnumeratedType(EnumeratedType type) {
        String className = type.getClass().getName();

        Hashtable values;
        Hashtable valuesStr;

        synchronized (m_types) {
            //avoid race condition for creating inner table

            //store by value
            values = (Hashtable) m_types.get(className);
            if (values == null) {
                values = new Hashtable();
                m_types.put(className, values);
            }
            values.put(type.getIntegerValue(), type);

            //store by name
            valuesStr = (Hashtable) m_typesAsStr.get(className);
            if (valuesStr == null) {
                valuesStr = new Hashtable();
                m_typesAsStr.put(className, valuesStr);
            }
            valuesStr.put(type.toString(), type);

        }
    }


    /**
     *  The enumeration value
     */
    private Integer m_value;

    /**
     *  The string name of the value
     */
    private transient String m_name;

    /**
     *  Hashtable of enumerations, indexed by number
     */
    private final static Hashtable m_types = new Hashtable();

    /**
     *  Hashtable of enumerations, indexed by name
     */
    private final static Hashtable m_typesAsStr = new Hashtable();
}

// NAME
//      $RCSfile: AsnOctets.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.31 $
// CREATED
//      $Date: 2002/10/29 14:44:40 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 1995, 1996 by West Consulting BV
 *
 * Permission to use, copy, modify, and distribute this software
 * for any purpose and without fee is hereby granted, provided
 * that the above copyright notices appear in all copies and that
 * both the copyright notice and this permission notice appear in
 * supporting documentation.
 * This software is provided "as is" without express or implied
 * warranty.
 * author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * original version by hargrave@dellgate.us.dell.com (Jordan Hargrave)
 */

/*
 * Copyright (C) 1996, 1997, 1998, 1999, 2000, 2001, 2002 by Westhawk Ltd
 * <a href="www.westhawk.co.uk">www.westhawk.co.uk</a>
 *
 * Permission to use, copy, modify, and distribute this software
 * for any purpose and without fee is hereby granted, provided
 * that the above copyright notices appear in all copies and that
 * both the copyright notice and this permission notice appear in
 * supporting documentation.
 * This software is provided "as is" without express or implied
 * warranty.
 * author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 */

package uk.co.westhawk.snmp.stack;

import uk.co.westhawk.snmp.util.*;
import java.io.*;
import java.util.*;

/**
 * This class represents the ASN.1 Octet class.
 * It can be used for Octets, Ip Addresses and Opaque types.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 3.31 $ $Date: 2002/10/29 14:44:40 $
 */
public class AsnOctets extends AsnObject
{
    private static final String     version_id =
        "@(#)$Id: AsnOctets.java,v 3.31 2002/10/29 14:44:40 birgit Exp $ Copyright Westhawk Ltd";

    /**
     * The hexadecimal prefix that is used when printing a hexadecimal
     * number in toString(). By default this is "0x".
     */
    public static String HEX_PREFIX = "0x";

    byte value[];

    /** Cache the hash code for the OID */
    private int hash = 0;

    /** 
     * Constructor. The type of the AsnOctets defaults to ASN_OCTET_STR.
     *
     * @param s The character array representing the AsnOctets
     * @see SnmpConstants#ASN_OCTET_STR 
     */
    public AsnOctets(char s[]) 
    {
        int idx;
        
        value = new byte[s.length];
        type = ASN_OCTET_STR;
        for(idx=0; idx<s.length; idx++) 
        {
            value[idx] = (byte)s[idx];
        }
    }

    /** 
     * Constructor. The type of the AsnOctets defaults to ASN_OCTET_STR.
     *
     * @param s The string representing the AsnOctets
     * @see AsnObject#ASN_OCTET_STR 
     */
    public AsnOctets(String s) 
    {
        this(s.toCharArray());
    }

    /** 
     * Constructor. The type of the AsnOctets defaults to ASN_OCTET_STR.
     *
     * @param s The byte array representing the AsnOctets
     * @see AsnObject#ASN_OCTET_STR 
     */
    public AsnOctets(byte s[]) 
    throws IllegalArgumentException
    {
        this(s, ASN_OCTET_STR);
    }

    /** 
     * Constructor to create an ASN IP Address. 
     *
     * @param iad The Inet Address representing the AsnIPAddress
     * @see java.net.InetAddress
     * @see #AsnOctets(byte[], byte)
     */
    public AsnOctets(java.net.InetAddress iad)
    throws IllegalArgumentException
    {
        this(iad.getAddress(), IPADDRESS);
    }


    /** 
     * Constructor to create a specific type of AsnOctets.
     *
     * @param s The byte array representing the AsnOctets
     * @param t The type of the AsnOctets
     * @see AsnObject#ASN_OCTET_STR 
     * @see AsnObject#IPADDRESS
     * @see AsnObject#OPAQUE
     */
    public AsnOctets(byte s[], byte t) 
    throws IllegalArgumentException
    {
        value = s;
        type = t;
        if (value == null)
        {
            throw new IllegalArgumentException("Value is null");
        }
    }

    /** 
     * Constructor.
     *
     * @param in The input stream from which the value should be read
     * @param len The length of the AsnOctets
     */
    public AsnOctets(InputStream in, int len) throws IOException 
    {
        value = new byte[len];
        if (len != 0)
        {
            if (len == in.read(value,0,len))
            {
                String str = "";
                // str = new String(value,0);
                str = new String(value);
            }
            else 
            {
                throw new IOException("AsnOctets(): Not enough data");
            }
        }
        else
        {
            // if len is zero, the in.read will return -1
            // a length of zero is a valid case.
            ;
        }
    }

    /**
     * Set the global hexadecimal prefix. This prefix will be used in
     * toString() when it prints out a hexadecimal number. It is not
     * used in toHex(). The default is "0x".
     *
     * @see #toString()
     * @see #toHex()
     * @see #HEX_PREFIX
     */
    public static void setHexPrefix(String newPrefix)
    {
        HEX_PREFIX = newPrefix;
    }

    /** 
     * Returns the value.
     *
     * @return The value of the AsnOctets
     * @see #toString()
     */
    public String getValue()
    {
        return toString();
    }

    /** 
     * Returns the bytes.
     *
     * @return The bytes of the AsnOctets
     */
    public byte[] getBytes()
    {
        return value;
    }

    /** 
     * Returns the string representation of the AsnOctets.
     * <p>
     * The string will have one of the following formats:
     * <ul>
     * <li>aaa.bbb.ccc.ddd, if this class represents an IP Address</li> 
     * <li>&lt;prefix&gt;aa[:bb]*, if this class represents a non-printable
     * string. The output will be in hexadecimal numbers. It will be prefixed
     * according to the hex. prefix value</li> 
     * <li>a printable string, if this class represents a printable
     * string</li> 
     * </ul>
     * </p>
     *
     * <p>
     * When the type is ASN_OCTET_STR, the method tries to guess whether 
     * or not the string is printable; without the knowledge of the MIB 
     * it cannot distinguish between OctetString and DisplayString.  
     * </p>
     *
     * @see #HEX_PREFIX
     * @see #setHexPrefix(String)
     * @see #toHex
     * @see #toIpAddress
     * @see #toDisplayString
     * @return The string of the AsnOctets
     */
    public String toString()
    {
        String str = "";
        // str = new String(value, 0);

        if (type == IPADDRESS)
        {
            str = toIpAddress();
        }
        else if (type == OPAQUE)
        {
            str = HEX_PREFIX + toHex(); 
        }
        else
        {
            int length = value.length;
            int b = ' '; // the first printable char in the ASCII table
            int e = '~'; // the last printable char in the ASCII table

           /*
            * About the test for 'value[i] == 0':
            * (Quote from one of our customers:)
            * I've seen cases where there are embedded nulls in a sysdescr 
            * - not always at the end either - and we need to get complete 
            * data back from the device even in this situation. 
            */

            boolean isPrintable = true;
            int i=0;
            while (i<length && isPrintable)
            {
                isPrintable = ((value[i] >= b && value[i] <= e) 
                                    ||
                               Character.isWhitespace((char)value[i])
                                    ||
                               value[i] == 0);
                i++;
            }

            if (isPrintable)
            {
                str = new String(value);
            }
            else
            {
                str = HEX_PREFIX + toHex(); 
            }
        }

        return str;
    }

    int size() 
    { 
        return value.length; 
    }

    void write(OutputStream out, int pos) throws IOException 
    {
        int idx;

        // Output header
        AsnBuildHeader(out, type, value.length);
        if (debug > 10)
        {
            System.out.println("\tAsnOctets(): value = " + toString()
                + ", pos = " + pos);
        }
        
        // Output data
        for(idx=0; idx<value.length; idx++) 
        {
            out.write(value[idx]);
        }
    }

    /**
     * Returns this object as an IP Address. The format is
     * aaa.bbb.ccc.ddd .
     * @return The IP Address representation.
     * @see #toString
     */
    public String toIpAddress()
    {
        String str = "";
        int length;
        long val;
        length = value.length;
        if (length > 0)
        {
            for (int i=0; i<length-1; i++)
            {
                val = getPositiveValue(i);
                str = str + String.valueOf(val) + ".";
            }
            val = getPositiveValue(length-1);
            str = str + String.valueOf(val);
        }
        return str;
    }

    /**
     * Returns the positive long for an octet. Only if type is IPADDRESS
     * can the value be negative anyway.
     */
    private long getPositiveValue(int index)
    {
        long val = (long) value[index];
        if (val <0)
        {
            val += 256; 
        }
        return val;
    }
    
    /**
     * Returns this object as an hexadecimal. It does <em>not</em> use
     * the HEX_PREFIX here.
     * @return The hex representation.
     * @see #toString
     */
    public String toHex()
    {
        int length; 
        StringBuffer buffer = new StringBuffer("");

        length = value.length;
        if (length > 0)
        {
            for (int i=0; i<length-1; i++)
            {
                buffer.append(SnmpUtilities.toHex(value[i])).append(":");
            }
            buffer.append(SnmpUtilities.toHex(value[length-1]));
        }

        return buffer.toString();
    }


    /**
     * Returns this object as a display string. In contrast to the
     * method toString(), this method does not try to guess whether or
     * not this string is a display string, it just converts it to a
     * String. 
     *
     * @return The string representation.
     * @see #toString
     */
    public String toDisplayString()
    {
        String str = "";
        int length; 

        length = value.length;
        if (length > 0)
        {
            str = new String(value);
        }

        return str;
    }

/**
 * Converts this object to its corresponding sub-identifiers.
 * Each octet will be encoded in a separate sub-identifier, by
 * converting the octet into a positive long.
 * 
 * <p>
 * Use this method when building an OID when this object specifies a
 * conceptual row. For example ipNetToMediaEntry, see 
 * <a href="http://ietf.org/rfc/rfc2011.txt">RFC 2011</a>
 * or SnmpCommunityEntry, see
 * <a href="http://ietf.org/rfc/rfc2576.txt">RFC 2576</a>
 * </p>
 *
 * <p>
 * The variable <code>length_implied</code> indicates that this MIB variable 
 * is preceded by the IMPLIED keyword.
 * Note that the IMPLIED keyword can only be present for an object having 
 * a variable-length syntax (e.g., variable-length strings or object 
 * identifier-valued objects). Further, the IMPLIED keyword can only be 
 * associated with the last object in the INDEX clause.  Finally, the 
 * IMPLIED keyword may not be used on a variable-length string object if 
 * that string might have a value of zero-length. 
 * </p>
 *
 * <p>
 * If the length is implied, no extra sub-identifier will be created to
 * indicate its length. If the length is not implied, the first
 * sub-identifier will be the length of the Octet.
 * </p>
 *
 * <p>
 * If this object is of type IPADDRESS, length_implied should be false.
 * </p>
 *
 * <p>
 * The mapping of the INDEX clause is
 * explained in <a href="http://ietf.org/rfc/rfc2578.txt">RFC 2578</a>,
 * section 7.7.
 * </p>
 *
 * @param length_implied Indicates if the length of this octet is
 * implied. 
 *
 * @see AsnObjectId#add(long[])
 */
public long [] toSubOid(boolean length_implied)
{
    long sub_oid[];
    int index = 0;
    int length = value.length;

    if (length_implied)
    {
        sub_oid = new long[length];
    }
    else
    {
        sub_oid = new long[length+1];
        sub_oid[0] = length;
        index++;
    }

    for (int i=0; i<length; i++)
    {
        sub_oid[index] = getPositiveValue(i);
        index++;
    }
    return sub_oid;
}


/**
 * Compares this Octet to the specified object.
 * The result is <code>true</code> if and only if the argument is not
 * <code>null</code> and is a <code>AsnOctets</code> object that represents
 * the same sequence of octets as this object.
 *
 * @param anObject the object to compare this <code>AsnOctets</code> 
 *                 against.
 * @return <code>true</code> if the <code>AsnOctets </code>are equal;
 *         <code>false</code> otherwise.
 */
public boolean equals(Object anObject) 
{
    if (this == anObject) 
    {
        return true;
    }
    if (anObject instanceof AsnOctets) 
    {
        AsnOctets anotherOctet = (AsnOctets)anObject;
        int n = value.length;
        if (n == anotherOctet.value.length) 
        {
            byte v1[] = value;
            byte v2[] = anotherOctet.value;
            int i = 0;
            int j = 0;
            while (n-- != 0) 
            {
                if (v1[i++] != v2[j++])
                {
                    return false;
                }
            }
            return true;
        }
    }
    return false;
}


/**
 * Returns a hash code for this Octet. The hash code for a
 * <code>AsnOctets</code> object is computed as
 * <blockquote><pre>
 * s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
 * </pre></blockquote>
 * using <code>int</code> arithmetic, where <code>s[i]</code> is the
 * <i>i</i>th character of the Octet, <code>n</code> is the length of
 * the Octet, and <code>^</code> indicates exponentiation.
 * (The hash value of the empty Octet is zero.)
 *
 * @return  a hash code value for this object.
 */
public int hashCode() 
{
    int h = hash;
    if (h == 0) 
    {
        int off = 0;
        byte val[] = value;
        int len = value.length;

        for (int i=0; i<len; i++) 
        {
            h = 31*h + val[off++];
        }
        hash = h;
    }
    return h;
}


}


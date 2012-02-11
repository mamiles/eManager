// NAME
//      $RCSfile: AsnObjectId.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.17 $
// CREATED
//      $Date: 2002/10/22 16:52:35 $
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

import java.io.*;
import java.util.*;

// unsigned 32-bit integers encoded in the following format:
// MSB..LSB
// 76543210
// xyyyyyyy
// .\     /
// . \   /
// .  \ /
// .   +-------- OID bits
// +------------ 0 if this is the last byte, 1 if additional bytes follow
//
// Output sid in the form: abcdefgh.ijklmnop.qrstuvwx.yzABCDEF
//
// if (abcd != 0) {
//   output 1.000abcd 1.efghijk 1.lmnopqr 1.stuvwxy 0.zABCDEF
// } else if (efghijk != 0) {
//   output 1.efghijk 1.lmnopqr 1.stuvwxy 0.zABCDEF
// } else if (lmnopqr != 0) {
//   output 1.lmnopqr 1.stuvwxy 0.zABCDEF
// } else if (stuvwxy != 0) {
//   output 1.stuvwxy 0.zABCDEF
// } else {
//   output 0.zABCDEF
// }


/**
 * This class represents the ASN.1 ObjectID class. An Object ID (OID) 
 * identifies a variable in a MIB. 
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 3.17 $ $Date: 2002/10/22 16:52:35 $
 */
public class AsnObjectId extends AsnObject
{
    private static final String     version_id =
        "@(#)$Id: AsnObjectId.java,v 3.17 2002/10/22 16:52:35 birgit Exp $ Copyright Westhawk Ltd";

    // Thanks to Ken Swanson (gks@navcomm1.dnrc.bell-labs.com) for 
    // pointing out that this should be an array of longs.  

    // default value for test purposes.
    private long value[] = {1,3,6,1,4,1,674,10889,2,1,0};

/**
 * The empty constructor.
 */
AsnObjectId() 
{
}

/**
 * Constructor.
 *
 * @param in The input stream from which the value should be read
 * @param len The length of the AsnInteger
 */
AsnObjectId(InputStream in, int len) throws IOException 
{
    // get our data
    byte data[] = new byte[len];
    if (len != in.read(data,0,len))
    {
        throw new IOException("AsnObjectId(): Not enough data");
    }

    // now decide how many SID we will need
    // count the bytes with 0 in the top bit - then add 1
    int sids = 1;    // first byte has 2 sids in it
    for (int off=0; off<len;off++) 
    {
        if (data[off] >= 0) 
        {
            sids++;
        }
    }
    // so allocate some space for the sids
    value = new long[sids];

    // decode the first two
    value[0] = data[0] / 40; 
    value[1] = data[0] % 40;

    // now decode the rest
    int off = 1;
    for (int idx=2; idx<value.length; idx++) 
    {
        long tval = 0;
        do 
        {
            tval = tval << 7;
            tval |= (data[off] & 0x7f);
        } while (data[off++] < 0);

        value[idx] = tval;
    }
}


/**
 * Constructs an AsnObjectId out of an OID string.
 *
 * @param s The OID, format a[.b]*
 */
public AsnObjectId(String s) 
throws IllegalArgumentException
{
    value = toArrayOfLongs(s);
}

/**
 * Converts the dotted string into an array of longs.
 *
 * @param s The OID, format a[.b]*
 */
private long [] toArrayOfLongs(String s)
throws IllegalArgumentException
{
    long [] oidArray = new long[0];
    if (s != null && s.length() > 0)
    {
        StringTokenizer tok = new StringTokenizer(s, ".");
        int count = tok.countTokens();
        oidArray = new long[count];

        int n=0;
        while (tok.hasMoreTokens())
        {
            try
            {
                String num = tok.nextToken();
                Long val = Long.valueOf(num);
                oidArray[n] = val.longValue();
                n++;
            }
            catch (java.lang.NumberFormatException exc)
            {
                throw new IllegalArgumentException("AsnObjectId(): Bad OID '" 
                    + s + "' " + exc.getMessage());
            }
            catch (NoSuchElementException exc)
            {
            }
        }
    }
    else
    {
        throw new IllegalArgumentException("AsnObjectId(): Bad OID '" 
            + s + "' ");
    }

    return oidArray;
}


/**
 * Checks if this OID starts with the specified prefix.
 * 
 * @return true if starts with the prefix, false otherwise
 */
public boolean startsWith(AsnObjectId prefix)
{
    boolean sw = true;
    if (prefix.value.length < this.value.length)
    {
        int pos=0;
        while (pos < prefix.value.length && sw == true)
        {
            sw = (prefix.value[pos] == this.value[pos]);
            pos++;
        }
    }
    else
    {
        sw = false;
    }
    return sw;
}

/**
 * Adds a single sub-identifier to the end of the OID.
 *
 * @param sub_oid the sub-identifier
 */
public void add(long sub_oid)
{
    int size = value.length;

    long tmp_value[] = value;
    value = new long[size+1];
    System.arraycopy(tmp_value, 0, value, 0, size);
    value[size] = sub_oid;
}


/**
 * Adds a number of sub-identifiers to the end of the OID.
 *
 * @param sub_oid the sub-identifiers
 * @see AsnOctets#toSubOid
 */
public void add(long[] sub_oid)
{
    int size1 = value.length;
    int size2 = sub_oid.length;

    long tmp_value[] = value;
    value = new long[size1+size2];
    System.arraycopy(tmp_value, 0, value, 0, size1);
    System.arraycopy(sub_oid, 0, value, size1, size2);
}


/**
 * Adds sub-identifiers to the end of the OID.
 *
 * @param s the sub-identifiers, format a[.b]*
 */
public void add(String s) throws IllegalArgumentException
{
    long [] sub_oid = toArrayOfLongs(s);
    add(sub_oid);
}

/** 
 * Get total size of object ID.
 */
int size() 
{
    int val, idx, len;

    if (value.length > 1)
    {
        // First entry = OID[0]*40 + OID[1];
        len = getSIDLen(value[0]*40 + value[1]);
        for(idx=2; idx<value.length; idx++) 
        {
            len += getSIDLen(value[idx]);
        }
    }
    else if (value.length == 1)
    {
        len = getSIDLen(value[0]*40);
    }
    else 
    {
        len = getSIDLen(0);
    }
    return len;
}

/** 
 * Output data.
 */
void write(OutputStream out, int pos) throws IOException 
{
    int idx;
    
    // Output header
    AsnBuildHeader(out, ASN_OBJECT_ID, size());
    if (debug > 10)
    {
        System.out.println("\tAsnObjectId(): value = " + this.toString()
            + ", pos = " + pos);
    }
    
    // Output data bytes
    if (value.length > 1)
    {
        // First entry = OID[0]*40 + OID[1];    
        EncodeSID(out, value[0]*40 + value[1]);
        for(idx=2; idx<value.length; idx++) 
        {
            EncodeSID(out, value[idx]);
        }
    }
    else if (value.length == 1)
    {
        EncodeSID(out, value[0]*40);
    }
    else
    {
        EncodeSID(out, 0);
    }
}

/** 
 * Get length of OID subidentifier.
 */
private int getSIDLen(long value) 
{
    int count;
      
    for(count=1; (value>>=7) != 0; count++)
        ;
    return count;
}

/** 
 * Encode OID subidentifier.
 */
private void EncodeSID(OutputStream out, long value) throws IOException 
{
    byte mask = (byte)0x0F;
    int  count = 0;

    // Upper mask is 4 bits
    mask = 0xF;
    
    // Loop while value and mask is zero
    for(count=28; count>0; count-=7) 
    {
        if (((value >> count) & mask) != 0) break;
        mask = 0x7f;
    }
    
    // While count, output value. If this isn't the last byte, output
    // 0x80 | value.
    for(; count>=0; count-=7) 
    {
        out.write((byte)(((value >> count) & mask) | (count>0 ? 0x80 : 0x00)) );
        mask = 0x7f;
    }
}

/** 
 * Returns the value of the AsnObjectId.
 *
 * @return The value (the OID)
 */
public String getValue()
{
    return toString();
}


/** 
 * Returns the string representation of the AsnObjectId.
 *
 * @return The string of the AsnObjectId
 */
public String toString() 
{
    return toString(value);
}

/** 
 * Returns the string representation of the AsnObjectId.
 *
 * @return The string of the AsnObjectId
 */
public String toString(long v[]) 
{
    StringBuffer buffer = new StringBuffer("");
    for (int n=0; n < v.length-1 && n<100 ; n++) 
    {
        buffer.append(v[n]).append(".");
    }
    if (v.length-1 > 100)
    {
        buffer.append("[.. cut ..].");
    }
    buffer.append(v[v.length-1]);
    return buffer.toString();
}

/**
 * Returns the number of elements in the AsnObjectId.
 * We cannot use size(), since that is already in use.
 *
 * @return the number of elements
 */
public int getSize()
{
    return value.length;
}

/**
 * Returns the element in the AsnObjectId at the specified index.
 *
 * @param index the index
 * @return the element at the specified index
 * @exception java.lang.ArrayIndexOutOfBoundsException if an invalid 
 * index was given
 */
public synchronized long getElementAt(int index)
    throws ArrayIndexOutOfBoundsException
{
    if (index >= value.length)
    {
        throw new ArrayIndexOutOfBoundsException(index 
              + " >= " + value.length);
    }
    try
    {
        return value[index];
    }
    catch (ArrayIndexOutOfBoundsException exc)
    {
        throw new ArrayIndexOutOfBoundsException(index + " < 0");
    }
}


/**
 * Compares this OID to the specified object.
 * The result is <code>true</code> if and only if the argument is not
 * <code>null</code> and is a <code>AsnObjectId</code> object that represents
 * the same sequence of numbers as this object.
 *
 *
 * <p>
 * Thanks to Eli Bishop (eli@graphesthesia.com) for the suggestion of
 * adding it.
 * </p>
 *
 * @param anObject the object to compare this <code>AsnObjectId</code> 
 *                 against.
 * @return <code>true</code> if the <code>AsnObjectId </code>are equal;
 *         <code>false</code> otherwise.
 */
public boolean equals(Object anObject) 
{
    if (this == anObject) 
    {
        return true;
    }
    if (anObject instanceof AsnObjectId) 
    {
        AsnObjectId anotherOid = (AsnObjectId)anObject;
        int n = value.length;
        if (n == anotherOid.value.length) 
        {
            long v1[] = value;
            long v2[] = anotherOid.value;
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
 * Returns a hash code for this OID. 
 * The hash value of the empty OID is zero.
 *
 * @return  a hash code value for this object.
 */
public int hashCode() 
{
    int h = 0;
    if (h == 0) 
    {
        int off = 0;
        long val[] = value;
        int len = value.length;

        for (int i=0; i<len; i++) 
        {
            long l = val[off++];
            // nicked from Long.hashCode()
            int hi = (int)(l ^ (l >>> 32)); 
            h = 31*h + hi;
        }
    }
    return h;
}


}

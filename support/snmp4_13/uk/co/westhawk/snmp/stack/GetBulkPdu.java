// NAME
//      $RCSfile: GetBulkPdu.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.14 $
// CREATED
//      $Date: 2002/10/10 15:13:57 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 2000, 2001, 2002 by Westhawk Ltd
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

/**
 * This class represents the SNMP GetBulk Pdu.
 *
 * <p>
 * The request is processed on the agent site in the following way:
 * <pre>
 * for (n=0; n&lt;N; n++)
 * {
 *    getValue(var[n]);
 * }
 * for (m=1; m&lt;M; m++)
 * {
 *     for (r=1; r&lt;R; r++)
 *     {
 *         getValue(var[N+r]);
 *     }
 * }
 * </pre>
 * </p>
 *
 * <p>
 * Where:<br>
 * <ul>
 * <li><code>L</code> is the number of vars in the request </li>
 * <li><code>N = Max(Min(</code>non_repeaters<code>,L), 0)</code></li>
 * <li><code>M = Max(</code>max_repetitions<code>, 0)</code></li>
 * <li><code>R = L-N</code></li>
 * </ul>
 * </p>
 *
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.14 $ $Date: 2002/10/10 15:13:57 $
 */
public abstract class GetBulkPdu extends Pdu 
{
    private static final String     version_id =
        "@(#)$Id: GetBulkPdu.java,v 3.14 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

    protected int non_repeaters = 0;
    protected int max_repetitions = 0;

/** 
 * Constructor.
 * The GetBulkRequest has been added in SNMPv2. Its purpose is to
 * retrieve a large amount of information with one request.
 *
 * @param con The context of the Pdu
 */
public GetBulkPdu(SnmpContextBasisFace con) 
{
    super(con);
    setMsgType(AsnObject.GETBULK_REQ_MSG);
}

/**
 * Sets the non_repeaters. The non_repeaters specifies the number of
 * variables in the varbind list for which a single lexicographic
 * successor is to be returned.
 * If they are not set, the value will be 0 (zero).
 */
public void setNonRepeaters(int no)
{
    // same field as error status in any other PDU.
    non_repeaters = no;
}

/**
 * Returns the non_repeaters. 
 */
public int getNonRepeaters()
{
    return non_repeaters;
}

/**
 * Sets the max_repetitions. The max_repetitions specifies the number of
 * lexicographic successors to be returned for the remaining variables
 * in the varbind list.
 * If they are not set, the value will be 0 (zero).
 */
public void setMaxRepetitions(int no)
{
    // same field as error index in any other PDU.
    max_repetitions = no;
}

/**
 * Old method to set the max_repetitions. Was a spelling mistake,
 * it is still in here for backwards compatibility.
 *
 * @deprecated Use setMaxRepetitions(int no).
 * @see #setMaxRepetitions
 */
public void setMaxRepititions(int no)
{
    setMaxRepetitions(no);
}

/**
 * Returns the max_repetitions. 
 */
public int getMaxRepetitions()
{
    return max_repetitions;
}

/** 
 * Send the Pdu.
 *
 * <p>
 * The GetBulk request has the same format as any other request, except
 * that the error_status field is replaced by non_repeaters and the
 * error_index field is replaces by max_repetitions.
 * </p>
 * @see Pdu#send(int, int)
 */
public boolean send() throws java.io.IOException, PduException
{
    return send(non_repeaters, max_repetitions);
}

/**
 * Returns a string representation of the object.
 * @return The string
 */
public String toString()
{
    StringBuffer buffer = new StringBuffer(super.toString());
    int l = buffer.length();
    buffer.setLength(l-1);

    buffer.append(", non_rep=").append(getNonRepeaters());
    buffer.append(", max_rep=").append(getMaxRepetitions());
    buffer.append("]");
    return buffer.toString();
}

}

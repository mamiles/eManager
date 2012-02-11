// NAME
//      $RCSfile: DiscoveryPdu.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.10 $
// CREATED
//      $Date: 2002/10/23 09:56:20 $
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

package uk.co.westhawk.snmp.pdu;
import uk.co.westhawk.snmp.stack.*;
import java.util.*;

/**
 * This class is used to perform the SNMPv3 USM discovery.
 * This Pdu cannot have any OIDs.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.10 $ $Date: 2002/10/23 09:56:20 $
 */
public class DiscoveryPdu extends Pdu
{
    private static final String     version_id =
        "@(#)$Id: DiscoveryPdu.java,v 3.10 2002/10/23 09:56:20 birgit Exp $ Copyright Westhawk Ltd";

    private SnmpContextv3Face context;

/**
 * Constructor.
 *
 * @param context The v3 context of the Pdu
 */
public DiscoveryPdu(SnmpContextv3Face cntxt)
{
    super(cntxt);
    context = cntxt;
}

/**
 * Cannot add any OID. This method is overwritten to prevent users from
 * adding any OID.
 *
 * @exception IllegalArgumentException A discovery Pdu cannot have any
 * OID.
 */
public void addOid(String oid)
throws IllegalArgumentException
{
    throw new IllegalArgumentException("DiscoveryPdu cannot have OID");
}

/** 
 * Cannot add any OID. This method is overwritten to prevent users from
 * adding any OID.
 *
 * @exception IllegalArgumentException A discovery Pdu cannot have any
 * OID.
 * @since 4_12
 */
public void addOid(String oid, AsnObject val) 
{
    throw new IllegalArgumentException("DiscoveryPdu cannot have OID");
}

/** 
 * Cannot add any OID. This method is overwritten to prevent users from
 * adding any OID.
 *
 * @exception IllegalArgumentException A discovery Pdu cannot have any
 * OID.
 * @since 4_12
 */
public void addOid(AsnObjectId oid, AsnObject val) 
{
    throw new IllegalArgumentException("DiscoveryPdu cannot have OID");
}

/**
 * Cannot add any OID. This method is overwritten to prevent users from
 * adding any OID.
 *
 * @exception IllegalArgumentException A discovery Pdu cannot have any
 * OID.
 */
public void addOid(varbind var)
throws IllegalArgumentException
{
    throw new IllegalArgumentException("DiscoveryPdu cannot have OID");
}

/**
 * Cannot add any OID. This method is overwritten to prevent users from
 * adding any OID.
 *
 * @exception IllegalArgumentException A discovery Pdu cannot have any
 * OID.
 * @since 4_12
 */
public void addOid(AsnObjectId oid) 
{
    throw new IllegalArgumentException("DiscoveryPdu cannot have OID");
}

/**
 * Send the Pdu.
 * Note that all properties of the context have to be set before this
 * point.
 */
public boolean send() throws java.io.IOException, PduException
{
    if (added == false)
    {
        // Moved this statement from the constructor because it
        // conflicts with the way the SnmpContextXPool works.
        added = context.addDiscoveryPdu(this);
    }
    Enumeration vbs = reqVarbinds.elements();
    encodedPacket = context.encodeDiscoveryPacket(msg_type, getReqId(),
        getErrorStatus(), getErrorIndex(), vbs);
    addToTrans();
    return added;
}

/**
 * The value of the request is set. This will be called by
 * Pdu.fillin().
 *
 * @see Pdu#new_value
 */
protected void new_value(int n, varbind a_var) { }

/**
 * The methods notifies all observers.
 * This will be called by Pdu.fillin().
 *
 * <p>
 * If no exception occurred whilst receiving the response, the Object to the 
 * update() method of the Observer will be an array of
 * varbinds, so they may contains any AsnObject type.
 * If an exception occurred, that exception will be passed as the Object
 * to the update() method.
 * </p>
 */
protected void tell_them()
{
    notifyObservers();
}

}

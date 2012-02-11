// NAME
//      $RCSfile: TrapPduv2.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.5 $
// CREATED
//      $Date: 2002/10/10 15:13:57 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 2001, 2002 by Westhawk Ltd
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
 * This class represents the ASN SNMP v2c (and higher) Trap Pdu object.
 * See <a href="http://ietf.org/rfc/rfc1905.txt">RFC 1905</a>.
 *
 * @see TrapPduv1
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.5 $ $Date: 2002/10/10 15:13:57 $
 */
public abstract class TrapPduv2 extends Pdu 
{
    private static final String     version_id =
        "@(#)$Id: TrapPduv2.java,v 3.5 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

/** 
 * Constructor.
 *
 * @param con The context (v2c or v3) of the Pdu
 *
 */
public TrapPduv2(SnmpContextBasisFace con) 
{
    super(con);
    setMsgType(AsnObject.TRPV2_REQ_MSG);

    // Thanks to Steven Bolton (sbolton@cereva.com) for pointing out
    // that the trap pdu is not filled in properly (see Pdu.fillin())
    // because answered stays true. 
    answered = false; 
}

/**
 * The trap Pdu does not get a response back. So it should be sent once.
 *
 */
void transmit() 
{
    transmit(false);
}

/**
 * Returns the string representation of this object.
 *
 * @return The string of the Pdu
 */
public String toString()
{
    return super.toString(true);
}

/**
 * Has no meaning, since there is not response.
 */
protected void new_value(int n, varbind res){}

/**
 * Has no meaning, since there is not response.
 */
protected void tell_them(){}

}

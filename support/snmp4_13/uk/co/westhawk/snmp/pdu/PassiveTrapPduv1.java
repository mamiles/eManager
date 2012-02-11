// NAME
//      $RCSfile: PassiveTrapPduv1.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.3 $
// CREATED
//      $Date: 2002/10/15 13:37:02 $
// COPYRIGHT
//      ERG Group Ltd
// TO DO
//

/*
 * Copyright (C) 2002 by Westhawk Ltd
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

/**
 * This class represents the ASN SNMP v1 Trap Pdu object
 * that does not create a thread to send itself. It must be used with the
 * context class PassiveSnmpContext. The original purpose of the
 * Passive classes is to allow the stack to be used in environments where
 * thread creation is unwanted, eg database JVMs such as Oracle JServer.
 * See <a href="http://ietf.org/rfc/rfc1905.txt">RFC 1905</a>.
 *
 * <p>
 * See 
 * <a
 * href="../../../../../uk/co/westhawk/nothread/trap/package-summary.html">notes</a>
 * on how to send traps in an Oracle JServer environment.
 * </p>
 *
 * @see PassiveTrapPduv2
 * @since 4_12
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.3 $ $Date: 2002/10/15 13:37:02 $
 */
public class PassiveTrapPduv1 extends TrapPduv1
{
    private static final String     version_id =
        "@(#)$Id: PassiveTrapPduv1.java,v 3.3 2002/10/15 13:37:02 birgit Exp $ Copyright ERG Group Ltd";

/**
 * Constructor.
 *
 * @param con The context (v1) of the Pdu.
 * This is of type PassiveSnmpContext to ensure that the correct threading
 * behaviour occurs.
 */
public PassiveTrapPduv1(PassiveSnmpContext con)
{
    super(con);

    // this makes the base class Pdu believe that the trap is already 
    // awaiting transmission therefore it does not create a transmitter 
    // for this pdu
    added = true;
}

/**
 * Override of the operation in Pdu. Send the trap in the
 * callers thread. That is, don't create a sending thread
 * or add it to a queue or anything, just go straight to the socket.
 */
public void addToTrans()
{
    sendme();
}


}

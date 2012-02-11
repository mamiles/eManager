// NAME
//      $RCSfile: PassiveSnmpContext.java,v $
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

package uk.co.westhawk.snmp.stack;

/**
 * This class contains the SNMP v1 context that is needed by a Pdu to
 * send a SNMP v1 request in environments where thread creation is
 * unwanted.
 *
 * <p>
 * This extends SnmpContext so that it does not create any
 * threads to send pdus. It must be used with the
 * PDU class PassiveTrapPduv1. The original purpose of the
 * Passive classes is to allow the stack to be used in environments where
 * thread creation is unwanted, eg database JVMs such as Oracle JServer.
 * See <a href="http://ietf.org/rfc/rfc1905.txt">RFC 1905</a>.
 * </p>
 *
 * <p>
 * See 
 * <a
 * href="../../../../../uk/co/westhawk/nothread/trap/package-summary.html">notes</a>
 * on how to send traps in an Oracle JServer environment.
 * </p>
 *
 * @see uk.co.westhawk.snmp.pdu.PassiveTrapPduv1
 * @since 4_12
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.3 $ $Date: 2002/10/15 13:37:02 $
 */
public class PassiveSnmpContext extends SnmpContext
{
    private static final String     version_id =
        "@(#)$Id: PassiveSnmpContext.java,v 3.3 2002/10/15 13:37:02 birgit Exp $ Copyright Westhawk Ltd";


/**
 * Constructor.
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @see SnmpContext#SnmpContext(String, int)
 */
public PassiveSnmpContext(String a_host, int a_port)
throws java.io.IOException
{
    super(a_host, a_port);
}

/**
 * Constructor.
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @param typeSocketA The type of socket to use.
 * @see SnmpContext#SnmpContext(String, int, String)
 * @see SnmpContextFace#STANDARD_SOCKET
 * @see SnmpContextFace#NETSCAPE_SOCKET
 * @see SnmpContextFace#KVM_SOCKET
 */
public PassiveSnmpContext(String a_host, int a_port, String socketType)
throws java.io.IOException
{
    super(a_host, a_port, socketType);
}

/**
 * Overrides the AbstractSnmpContext.activate() to do nothing.
 * This prevents the creation of threads in the base class.
 *
 * @see AbstractSnmpContext#activate()
 */
protected void activate()
{
    // do nothing
}

}

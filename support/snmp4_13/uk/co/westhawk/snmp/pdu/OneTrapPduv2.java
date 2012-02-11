// NAME
//      $RCSfile: OneTrapPduv2.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.4 $
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


package uk.co.westhawk.snmp.pdu;
import java.util.*;
import java.io.*;

import uk.co.westhawk.snmp.stack.*;
import uk.co.westhawk.snmp.util.*;

/**
 * This class represents the ASN SNMPv2c (and higher) Trap Pdu object. 
 * See <a href="http://ietf.org/rfc/rfc1157.txt">RFC 1157</a>.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.4 $ $Date: 2002/10/10 15:13:57 $
 */
public class OneTrapPduv2 extends TrapPduv2 
{
    private static final String     version_id =
        "@(#)$Id: OneTrapPduv2.java,v 3.4 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";


/** 
 * Constructor.
 *
 * @param con The context (v2c, v3) of the OneTrapPduv2
 * @see SnmpContext
 */
public OneTrapPduv2(SnmpContextBasisFace con) 
{
    super(con);
}


}

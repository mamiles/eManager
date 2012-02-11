// NAME
//      $RCSfile: SnmpContextv2cFace.java,v $
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
 * This interface contains the SNMP context interface that is needed by every 
 * Pdu to send a SNMP v2c request.
 *
 * @see SnmpContext
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.5 $ $Date: 2002/10/10 15:13:57 $
 */
public interface SnmpContextv2cFace extends SnmpContextFace 
{
    static final String     version_id =
        "@(#)$Id: SnmpContextv2cFace.java,v 3.5 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";


}

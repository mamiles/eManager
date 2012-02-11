// NAME
//      $RCSfile: InformPdu.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.1 $
// CREATED
//      $Date: 2002/10/10 10:24:14 $
// COPYRIGHT
//      Westhawk Ltd
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
 * This class represents the SNMP Inform Request Pdu.
 *
 * <p>
 * Note this PDU should be send to port 162 (the default trap port) by
 * default. You will have to create a SnmpContext with the 
 * DefaultTrapContext.DEFAULT_TRAP_PORT as parameter!
 * </p>
 *
 * <p><em>
 * <font color="red">
 * Note:
 * The stack so far only supports <u>sending</u> an Inform. Receiving an Inform
 * and replying with a Response is NOT yet supported!
 * </font>
 * </em></p>
 *
 * @see DefaultTrapContext#DEFAULT_TRAP_PORT
 * @since 4_12
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.1 $ $Date: 2002/10/10 10:24:14 $
 */
public abstract class InformPdu extends Pdu 
{
    private static final String     version_id =
        "@(#)$Id: InformPdu.java,v 3.1 2002/10/10 10:24:14 birgit Exp $ Copyright Westhawk Ltd";

/** 
 * Constructor.
 *
 * @param con The context of the Pdu
 */
public InformPdu(SnmpContextBasisFace con) 
{
    super(con);
    setMsgType(AsnObject.INFORM_REQ_MSG);
}

}

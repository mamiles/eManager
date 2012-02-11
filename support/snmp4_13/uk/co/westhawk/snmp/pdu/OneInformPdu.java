// NAME
//      $RCSfile: OneInformPdu.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.1 $
// CREATED
//      $Date: 2002/10/10 10:13:40 $
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
 
package uk.co.westhawk.snmp.pdu;
import uk.co.westhawk.snmp.stack.*;
import java.util.*;

/**
 * <p>
 * The OneInformPdu class will inform a manager about one (1) 
 * object (OIDs), based on the Inform request.
 * </p>
 *
 * <p>
 * Specify with <code>addOid()</code> the OIDs that should be informed with this
 * InformPdu request. No more than <code>count</code> (see constructor) 
 * should be added.
 * Add an Observer to the InformPdu with <code>addObserver()</code>, and 
 * send the InformPdu with <code>send()</code>.
 * </p>
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
 * @see InformPdu_vec
 * @see varbind
 * @see DefaultTrapContext#DEFAULT_TRAP_PORT
 * @since 4_12
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.1 $ $Date: 2002/10/10 10:13:40 $
 */
public class OneInformPdu extends InformPdu 
{
    private static final String     version_id =
        "@(#)$Id: OneInformPdu.java,v 3.1 2002/10/10 10:13:40 birgit Exp $ Copyright Westhawk Ltd";

    varbind var;

/**
 * Constructor.
 *
 * @param con The context of the request
 */
public OneInformPdu(SnmpContextBasisFace con)
{
    super(con);
}

/**
 * Constructor that will send the request immediately. No Observer
 * is set.
 *
 * @param con the SnmpContextBasisFace
 * @param oid the oid 
 */
public OneInformPdu(SnmpContextBasisFace con, String oid) 
throws PduException, java.io.IOException
{
    this(con, oid, null);
}

/**
 * Constructor that will send the request immediately. 
 *
 * @param con the SnmpContextBasisFace
 * @param oid the oid 
 * @param o the Observer that will be notified when the answer is received
 */
public OneInformPdu(SnmpContextBasisFace con, String oid, Observer o) 
throws PduException, java.io.IOException
{
    super(con);
    if (o != null) 
    {
        addObserver(o);
    }
    addOid(oid);
    send();
}

/**
 * The value of the request is set. This will be called by
 * InformPdu.fillin().
 *
 * @param n the index of the value
 * @param a_var the value
 * @see Pdu#new_value 
 */
protected void new_value(int n, varbind a_var) 
{
    if (n == 0) 
    {
        var = a_var;
    }
}

/**
 * The methods notifies all observers. 
 * This will be called by InformPdu.fillin().
 * 
 * <p>
 * Unless an exception occurred the Object to the update() method of the
 * Observer will be a varbind, so any AsnObject type can be returned.
 * In the case of an exception, that exception will be passed.
 * </p>
 */
protected void tell_them()  
{
    notifyObservers(var);
}

}
